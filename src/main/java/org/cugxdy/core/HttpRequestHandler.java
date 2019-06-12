package org.cugxdy.core;

import org.cugxdy.rest.RequestDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final static Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error(cause.getMessage());
        ctx.close();
    }
	
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request)
			throws Exception {
		// TODO Auto-generated method stub
        if(WebServer.getIgnoreUrls().contains(request.uri())) {
            return;
        }
        
        System.out.println("请求过程中");
        
		new RequestDispatcher().doDispatch(request, ctx);
	}
}
