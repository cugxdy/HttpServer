package org.cugxdy.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundInvoker;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class HttpResponse {

	private ChannelHandlerContext ctx;
	
	private Map<String,String> headers = new HashMap<String,String>();
	
	private Map<String,String> cookies = new HashMap<String,String>();
	
	public HttpResponse(ChannelHandlerContext ctx) {
		// TODO Auto-generated constructor stub
		this.ctx = ctx;
	}
	
    public Map<String, String> getHeaders() {
        return this.headers;
    }
    
    public Map<String, String> getCookies() {
        return this.cookies;
    }
    
    public void write(HttpStatus status ,String body ) {
    	HttpResponseStatus sta = HttpResponseStatus.parseLine(String.valueOf(status.value()));
    	
    	FullHttpResponse response = null;
    	
    	if(body != null) {
    		response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, sta,Unpooled.copiedBuffer("",CharsetUtil.UTF_8));
    	}else {
    		response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, sta,Unpooled.copiedBuffer(body,CharsetUtil.UTF_8));
    	} 
    	Set<Entry<String, String>> entrySet = headers.entrySet();
    	for(Entry<String, String> entry : entrySet) {
    		response.headers().add(entry.getKey(), entry.getValue());
    	}
    	
    	response.headers().setInt("Content-length", response.content().readableBytes());
    	ctx.writeAndFlush(response);
    }
    
    /**
     * 关闭Channel
     */
    public void closeChannel() {
        if(this.ctx != null && this.ctx.channel() != null) {
            this.ctx.channel().close();
        }
    }

	public ChannelHandlerContext getChannelHandlerContext() {
		// TODO Auto-generated method stub
		return ctx;
	}
	
	
}
