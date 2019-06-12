package org.cugxdy.rest;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.cugxdy.converter.Converter;
import org.cugxdy.converter.ConverterFactory;
import org.cugxdy.core.WebServer;
import org.cugxdy.exception.HandleRequestException;
import org.cugxdy.exception.ResourceNotFoundException;
import org.cugxdy.mapping.ControllerBean;
import org.cugxdy.mapping.ControllerMapping;
import org.cugxdy.mapping.ControllerMappingParameter;
import org.cugxdy.mapping.ControllerMappingRegistry;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpChunkedInput;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

public class RequestHandler {

	public ChannelFuture handleRequest(RequestInfo requestInfo) {
		// TODO Auto-generated method stub
		ControllerMapping mapping = this.lookupMappings(requestInfo);
		
		if(mapping == null) {
			
			HttpContextHolder.setRequest(requestInfo.getRequest());
			HttpContextHolder.setResponse(requestInfo.getResponse());
			
			if(WebServer.getExceptionHandler() != null) {
				WebServer.getExceptionHandler().doHandle(new ResourceNotFoundException());
				return null;
			}
			throw new ResourceNotFoundException();
		}
		
		Object[] paramValues = new Object[mapping.getParameters().size()];
		Class<?>[] paramTypes = new Class<?>[mapping.getParameters().size()];
		
		System.out.println("请求正在准备执行中");
		System.out.println("Length = " + paramValues.length);
		
		for(int i = 0; i< paramValues.length ; i++) {
			
			ControllerMappingParameter cmp = mapping.getParameters().get(i);
			Converter<?> converter = null;
			
			System.out.println(cmp.getType().toString());
			System.out.println(cmp.getDataType().toString());
			System.out.println(cmp.getName().toString());
			System.out.println(cmp.getClass().toString());
			
			switch(cmp.getType()) {
			case HTTP_REQUEST :
				paramValues[i] = requestInfo.getRequest();
                break;
			case HTTP_RESPONSE :
				paramValues[i] = requestInfo.getResponse();
				break;
			case REQUEST_BODY : 
				paramValues[i] = requestInfo.getBody();
				break;
			case REQUEST_PARAM :
				paramValues[i] = requestInfo.getParamters().get(cmp.getName());
				converter = ConverterFactory.create(cmp.getDataType());
				if(converter != null) {
					paramValues[i] = converter.convert(paramValues[i]);
				}
				
				break;
			case REQUEST_HEADER :
				paramValues[i] = requestInfo.getParamters().get(cmp.getName());
				converter = ConverterFactory.create(cmp.getDataType());
				if(converter != null) {
					paramValues[i] = converter.convert(paramValues[i]);
				}
				break;
			case PATH_VARIABLE :
				paramValues[i] = this.getPathVariable(requestInfo.getRequest().uri(),mapping.getUrl() , cmp.getName());
				converter = ConverterFactory.create(cmp.getDataType());
				if(converter != null) {
					paramValues[i] = converter.convert(paramValues[i]);
				}
				break;
			case URL_ENCODED_FORM:
				paramValues[i] = requestInfo.getFormData();
				break;
			case UPLOAD_FILE:
				paramValues[i] = requestInfo.getFiles().size() > 0 ? requestInfo.getFiles().get(0) : null;
				break;
			case UPLOAD_FILES : 
				paramValues[i] = requestInfo.getFiles().size() > 0 ? requestInfo.getFiles() : null;
				break;
			}
			if(cmp.isRequired() && paramValues[i] == null) {
				throw new HandleRequestException("参数 " + cmp.getName() + " 为null");
			}
			
			paramTypes[i] = cmp.getDataType();
			
			System.out.println("param" + i + "Value = " + paramValues[i]);
			System.out.println("param" + i + "Types = " + paramTypes[i]);
		}
		
		
		try {
			
			HttpContextHolder.setRequest(requestInfo.getRequest());
			HttpContextHolder.setResponse(requestInfo.getResponse());
			
			Object result = this.execute(mapping,paramValues,paramTypes);
			
			if(!(result instanceof ResponseEntity)) {
				result = ResponseEntity.ok().build();
			}
			
			return writeResponse((ResponseEntity<?>)result, mapping.isJsonResponse());

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	private ChannelFuture writeResponse(ResponseEntity<?> result, boolean jsonResponse) throws Exception {
		// TODO Auto-generated method stub
		if(result.getBody() instanceof RandomAccessFile) {
			return writeFileResponse(result);
		}

		FullHttpResponse response = null;
		
		HttpResponseStatus status = HttpResponseStatus.parseLine(String.valueOf(result.getStatus().value()));
		
		System.out.println("status = " + result.getStatus().toString());
		System.out.println(result.getBody());
		
		// 回复JSON字符串
		if(result.getBody() != null ) {
			if(jsonResponse) {
				System.out.println("返回json数据中");
				String jsonStr = JSON.toJSONString(result.getBody());
				response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,Unpooled.copiedBuffer(jsonStr,CharsetUtil.UTF_8));
			}else {
				System.out.println("返回json key - value 数据中");
				String jsonStr = JSON.toJSONString(result.getBody());
				System.out.println(jsonStr);
				response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,Unpooled.copiedBuffer(jsonStr,CharsetUtil.UTF_8));
			}
		}else {
			
			System.out.println("欢迎来到后端的世界");
			response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,Unpooled.copiedBuffer("welcome",CharsetUtil.UTF_8));
		}
		
		String conentType = jsonResponse ? "application/json; charset=UTF-8" : "text/plain; charset=UTF-8";
		response.headers().set("Content-Type", conentType);
		
		Map<String,String> cookies = HttpContextHolder.getResponse().getCookies();
		
		// 设置Cookie
		Set<Entry<String,String>> entrySet = cookies.entrySet();
		for(Entry<String,String> entry : entrySet) {
			
			Cookie cookie = new DefaultCookie(entry.getKey(), entry.getValue());
			cookie.setPath("/");
			response.headers().set(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookie));
		}
		
        Map<String, String> responseHeaders = HttpContextHolder.getResponse().getHeaders();
        Set<Entry<String, String>> headersEntrySet = responseHeaders.entrySet();
        
        for(Entry<String, String> entry : headersEntrySet) {
        	System.out.println("Key = " + entry.getKey()  + " value = " + entry.getValue());
            response.headers().add(entry.getKey(), entry.getValue());
        }

        response.headers().setInt("Content-Length", response.content().readableBytes());
		
		return HttpContextHolder.getResponse().getChannelHandlerContext().writeAndFlush(response);
	}

	private ChannelFuture writeFileResponse(ResponseEntity<?> body) throws Exception {
		// TODO Auto-generated method stub
		
		RandomAccessFile randomAccessFile = (RandomAccessFile) body.getBody();
		long fileLength = 0;
		try {
			fileLength = randomAccessFile.length();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
		 HttpUtil.setContentLength(response, fileLength);
		 
		 if(body.getMimetype() != null && !body.getMimetype().trim().equals("")) {
			 response.headers().set(HttpHeaderNames.CONTENT_TYPE, body.getMimetype());
		 }
		 
		 if(body.getFileName() != null && !body.getFileName().trim().equals("")) {
			 String fileName = new String(body.getFileName().getBytes("gb2312"),"ISO8859-1");
			 response.headers().set(HttpHeaderNames.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
		 }
		 
		 if(HttpUtil.isKeepAlive(HttpContextHolder.getRequest())) {
			 response.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
			 
		 }
		 
		 ChannelHandlerContext ctx = HttpContextHolder.getResponse().getChannelHandlerContext();
		 ctx.write(response);
		 
        ChannelFuture sendFileFuture;
        ChannelFuture lastContentFuture = null;
        if (ctx.pipeline().get(SslHandler.class) == null) {
            sendFileFuture =
                    ctx.write(new DefaultFileRegion(randomAccessFile.getChannel(), 0, fileLength), ctx.newProgressivePromise());
            // Write the end marker.
            lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        } else {
            sendFileFuture = ctx.writeAndFlush(new HttpChunkedInput(new ChunkedFile(randomAccessFile, 0, fileLength, 8192)),
                    ctx.newProgressivePromise());
            // HttpChunkedInput will write the end marker (LastHttpContent) for us.
            lastContentFuture = sendFileFuture;
        }
	
        return lastContentFuture;
	}

	private Object execute(ControllerMapping mapping, Object[] paramValues, Class<?>[] paramTypes) {
		// TODO Auto-generated method stub
		ControllerBean bean = ControllerMappingRegistry.getBean(mapping.getClassName());
		Object instance = null;
		System.out.println("Class-name = " + mapping.getClassName());
		
		if(bean.isSingleton()) {
			instance = ControllerMappingRegistry.getSingleton(mapping.getClassName());
			if(instance == null) {
				Class<?> clazz;
				try {
					clazz = Class.forName(mapping.getClassName());
					try {
						instance = clazz.newInstance();
						ControllerMappingRegistry.registerSingleton(mapping.getClassName(),instance);
					} catch (InstantiationException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else {
			try {
				instance = Class.forName(mapping.getClassName()).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			Method method = Class.forName(mapping.getClassName()).getMethod(mapping.getClassMethod(), paramTypes);
			System.out.println("Method-name = " + method.getName());
			try {
				return method.invoke(instance, paramValues);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}                  
		return null;
	}


	private Object getPathVariable(String uri, String url, String name) {
		// TODO Auto-generated method stub
		String[] urlSpit = uri.split("/");
		String[] mappingUrlSplit = url.split("/");
		
		for(int i = 0; i< mappingUrlSplit.length ; i++) {
			if(mappingUrlSplit[i].equals("{" + name + "}") ) {
				if(urlSpit[i].contains("?")) {
					return urlSpit[i].split("[?]")[0];
				}else if(urlSpit[i].contains("&")) {
					return urlSpit[i].split("&")[0];
				}
				return urlSpit[i];
			}
		}
		
		return null;
	}


	private ControllerMapping lookupMappings(RequestInfo requestInfo) {
		// TODO Auto-generated method stub
		String lookupPath = requestInfo.getRequest().uri().endsWith("/") ? 
				requestInfo.getRequest().uri().substring(0, requestInfo.getRequest().uri().length() - 1):
					requestInfo.getRequest().uri();
				
		System.out.println(lookupPath);
		
		int paramIndex = lookupPath.indexOf("?");
		if(paramIndex >0) {
			lookupPath = lookupPath.substring(0,paramIndex);
		}
		
		System.out.println(lookupPath);
		
		Map<String, ControllerMapping> mappings = this.getMappings(requestInfo.getRequest().method().name());
		
		if(mappings == null || mappings.size() == 0) {
			return null;
		}
		
		Set<Entry<String,ControllerMapping>> entrySet = mappings.entrySet();
		
        for (Entry<String, ControllerMapping> entry : entrySet) {
        	
        	System.out.println(entry.getKey().toString());
        	
            // 完全匹配
            if (entry.getKey().equals(lookupPath)) {
                return entry.getValue();
            }
        }
		
		for(Entry<String,ControllerMapping> entry : entrySet) {
			
			String matcher = this.getMatcher(entry.getKey());
			if(lookupPath.startsWith(matcher)) {
				boolean matched = true;
				String[] lookPathSplit = lookupPath.split("/");
				String[] mappingUrlSplit = entry.getKey().split("/");
                if (lookPathSplit.length != mappingUrlSplit.length) {
                    continue;
                }
                
                for(int i = 0; i < lookPathSplit.length; i++) {
                	if(!lookPathSplit[i].equals(mappingUrlSplit[i])) {
                		if(!mappingUrlSplit[i].startsWith("{")) {
                            matched = false;
                            break;
                		}
                	}
                }
                if(matched) {
                    return entry.getValue();
                }
			}
		}
		
		return null;
	}


	private String getMatcher(String key) {
		// TODO Auto-generated method stub
        StringBuilder matcher = new StringBuilder(128);
        for (char c : key.toCharArray()) {
            if (c == '{') {
                break;
            }
            matcher.append(c);
        }
        return matcher.toString();
	}


	private Map<String, ControllerMapping> getMappings(String httpMethod) {
		// TODO Auto-generated method stub
		if(httpMethod == null) {
			return null;
		}
		
		switch(httpMethod) {
		case "GET" : 
			return ControllerMappingRegistry.getGetMappings();
		case "POST" : 
			return ControllerMappingRegistry.getPostMappings();
		case "PATCH" : 
			return ControllerMappingRegistry.getPatchMappings();
		case "DELETE" : 
			return ControllerMappingRegistry.getDeleteMappings();
		case "PUT" : 
			return ControllerMappingRegistry.getPutMappings();
        default:
            return null;
		}
	}

}
