package org.cugxdy.rest;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.cugxdy.interceptor.Interceptor;
import org.cugxdy.interceptor.InterceptorRegistry;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GenericFutureListener;

public final class RequestDispatcher {

	public void doDispatch(FullHttpRequest request, ChannelHandlerContext channelHandlerContext) throws IOException {
		
		System.out.println("请求分发中");		
		// TODO Auto-generated method stub
		HttpResponse response = new HttpResponse(channelHandlerContext);
        response.getHeaders().put("Access-Control-Allow-Origin", "*");
        response.getHeaders().put("Access-Control-Allow-Methods", "GET, POST");
		
		java.util.Stack<Interceptor> executedInterceptors = new java.util.Stack<Interceptor>();
		
		for(Interceptor interceptor : InterceptorRegistry.getInterceptor()) {
			if(this.allowExecuteInterceptor(request.uri() , interceptor)) {
				
				executedInterceptors.push(interceptor);
				if(!interceptor.prePressor(request, response)) {
					while(!executedInterceptors.isEmpty()) {
						executedInterceptors.pop().afterComplete(request, response);
					}
					return;
				}
			}
		}
		
		System.out.println("1");	
		
		ChannelFuture f = null;
		if(request.method().name().equalsIgnoreCase("OPTIONS")) {
            // 处理“预检”请求
			System.out.println("2");
            f = processOptionsRequest(request, response, channelHandlerContext);
		}
		
		System.out.println("Uri = " + request.uri().toString());
		System.out.println("Method = " + request.method().name());
		
		if(!request.method().name().equalsIgnoreCase("OPTIONS")) {
			
			System.out.println("3");
			
			RequestInfo requestInfo = new RequestInfo();
			requestInfo.setRequest(request);
			requestInfo.setResponse(response);
			
			QueryStringDecoder queryStrdecoder = new QueryStringDecoder(request.uri());
			
			Set<Entry<String,List<String>>> entrySet = queryStrdecoder.parameters().entrySet();
			
			for(Entry<String,List<String>> entry : entrySet) {
				// 存储GET请求时的参数。
				requestInfo.getParamters().put(entry.getKey(), entry.getValue().get(0));
				System.out.println("key = " + entry.getKey() + " value = " + entry.getValue().get(0));
			}
			
			Set<String> names = request.headers().names();
			for(String name : names) {
				// 获取HTTP请求头参数
				requestInfo.getHeaders().put(name, request.headers().get(name));
				System.out.println("name = " + name + " value = "+ request.headers().get(name));
			}
			
			System.out.println(request.method().name());
			
			if(!request.method().name().equalsIgnoreCase("GET")) {
				String contentType = requestInfo.getHeaders().get("Content-Type");
				
				if(contentType != null) {
					if(contentType.contains(";")) {
						contentType = contentType.split(";")[0];
					}
					
					System.out.println(contentType);
					
					switch(contentType) {
					case "application/json":
					case "application/json;charset=utf-8":
					case "text/xml": 
                        requestInfo.setBody(request.content().toString(Charset.forName("UTF-8")));
                        break;
                    case "application/x-www-form-urlencoded":
                    	
                    	System.out.println("form submit ...");
                    	
                    	HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
                    	decoder.offer(request);
                    	List<InterfaceHttpData> paramList = decoder.getBodyHttpDatas();
                    	
                    	System.out.println("form submit ..." + paramList.size());
                    	for(InterfaceHttpData attr : paramList) {
                    		Attribute data = (Attribute) attr;
                    		System.out.println("Key = " + data.getName() + " value = " + data.getValue());
                    		requestInfo.getFormData().put(data.getName(), data.getValue());
                    	}
                        break;
                    case "multipart/form-data":
                    	HttpPostRequestDecoder decoder1 = new HttpPostRequestDecoder(request);
                    	List<InterfaceHttpData> param2 = decoder1.getBodyHttpDatas();
                    	for(InterfaceHttpData attr : param2) {
                    		if(attr.getHttpDataType() == HttpDataType.FileUpload) {
	                    		FileUpload fileUpload = (FileUpload) attr;
	                    		if(fileUpload.isCompleted()) {
	                    			MultipartFile file = new MultipartFile();
	                    			file.setFileName(fileUpload.getFilename());
	                    			file.setFileType(fileUpload.getContentType());
	                    			file.setFileData(fileUpload.get());
	                    			requestInfo.getFiles().add(file);
	                        		continue;
	                    		}
                    		}
                            if(attr.getHttpDataType() == HttpDataType.Attribute) {
                                Attribute attribute = (Attribute) attr;
                                System.out.println("Key = " + attribute.getName() + " value = " + attribute.getValue());
                                requestInfo.getFormData().put(attribute.getName(), attribute.getValue());
                            }
                    	}
					}
				}
			}
			
			System.out.println("3.4");
			f = new RequestHandler().handleRequest(requestInfo);
		}
		
		for(Interceptor interceptor : InterceptorRegistry.getInterceptor()) {
			if(this.allowExecuteInterceptor(request.uri(), interceptor)) {
				interceptor.afterComplete(request, response);
			}
		}
		
		if(request.method().name() == "OPTIONS") {
			if(f != null) {
				f.addListener(ChannelFutureListener.CLOSE);
			}
		}
		
		if(!HttpUtil.isKeepAlive(request)) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	
	
	private ChannelFuture processOptionsRequest(FullHttpRequest request, HttpResponse response,
			ChannelHandlerContext channelHandlerContext) {
		System.out.println("2.1");
		String[] requestHeaders = request.headers().get("Access-Control-Request-Headers").split(",");
		
		System.out.println(request.headers().get("Access-Control-Request-Headers"));
		
		for(String header : requestHeaders) {
			if(!header.trim().isEmpty()) {
				
				if(!requestHeaderAllowed(header, response.getHeaders())) {
					
					System.out.println("2.2");
					FullHttpResponse optionResponse = 
							 new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.NOT_FOUND,
							Unpooled.copiedBuffer("welcome",CharsetUtil.UTF_8));

					return 	HttpContextHolder.getResponse().getChannelHandlerContext()
							.writeAndFlush(optionResponse);
				}
			}
		}
		
		System.out.println("2.3");
		
		FullHttpResponse resp = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK,
				Unpooled.copiedBuffer("",CharsetUtil.UTF_8));
		
		Map<String,String> respHeaders = response.getHeaders();
		
		Set<Entry<String, String>> headersEntrySet = respHeaders.entrySet();
		
		for(Entry<String,String> entry : headersEntrySet) {
			resp.headers().add(entry.getKey(), entry.getValue());
		}
	
		resp.headers().setInt("Content-Length", resp.content().readableBytes());  

		System.out.println("2.4");
		// TODO Auto-generated method stub
		return channelHandlerContext.writeAndFlush(resp);
	}
	
	
	

	private boolean requestHeaderAllowed(String header, Map<String, String> headers) {
		// TODO Auto-generated method stub
		String allowString = headers.get("Access-Control-Allow-Headers");
		if(allowString != null && !allowString.trim().equals("")) {
			String[] splitArray = allowString.split(",");
			for(int i = 0 ; i < splitArray.length ;i ++) {
				if(splitArray[i].equalsIgnoreCase(header) ) {
					return true;
				}
			}
		}
		return false;
	}



	private boolean allowExecuteInterceptor(String uri, Interceptor interceptor) {
		// TODO Auto-generated method stub
        List<String> excludeMappings = InterceptorRegistry.getExcludemappings(interceptor);
        if(excludeMappings != null) {
            for (String excludeMapping : excludeMappings) {
                if (uri.startsWith(excludeMapping)) {
                    return false;
                }
            }
        }
        return true;
	}
	
	
}
