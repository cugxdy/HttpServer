package org.cugxdy.core;


import java.util.ArrayList;

import org.cugxdy.controller.ExceptionHandler;
import org.cugxdy.interceptor.Interceptor;
import org.cugxdy.interceptor.InterceptorRegistry;
import org.cugxdy.rest.ControllerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class WebServer {
	
	private final static Logger log = LoggerFactory.getLogger(WebServer.class);
	
	private int port = 0;
	
	private int bossThread = 1;
	
	private int worderThread = 2;
	
	private String controllerBasePackge = "";
	
	private static ArrayList<String> ignoreUrls = new ArrayList<String>(16);
	
	private static ExceptionHandler handler;
	
	private int maxContentLength = 10 * 1024 * 1024;

	public WebServer(int i) {
		// TODO Auto-generated constructor stub
		this.port = i;
	}

	public final int getPort() {
		return port;
	}

	public final void setPort(int port) {
		this.port = port;
	}

	public final int getBossThread() {
		return bossThread;
	}

	public final void setBossThread(int bossThread) {
		this.bossThread = bossThread;
	}

	public final int getWorderThread() {
		return worderThread;
	}

	public final void setWorderThread(int worderThread) {
		this.worderThread = worderThread;
	}

	public final String getControllerBasePackge() {
		return controllerBasePackge;
	}

	public final void setControllerBasePackge(String controllerBasePackge) {
		this.controllerBasePackge = controllerBasePackge;
	}

	public static final ArrayList<String> getIgnoreUrls() {
		return ignoreUrls;
	}

	public static final void setIgnoreUrls(ArrayList<String> list) {
		WebServer.ignoreUrls = list;
	}

	public static final ExceptionHandler getExceptionHandler() {
		return handler;
	}

	public static final void setExceptionHandler(ExceptionHandler handler) {
		WebServer.handler = handler;
	}

	public final int getMaxContentLength() {
		return maxContentLength;
	}

	public final void setMaxContentLength(int maxContentLength) {
		this.maxContentLength = maxContentLength;
	}

	public void addIntercepter(Interceptor interceptor) {
		try {
			InterceptorRegistry.addInterceptor(interceptor);
		} catch (Exception e) {
			// TODO: handle exception
			log.error(" add fail : " + e.getMessage());
		}
	}
	
	public void addIntercepter(Interceptor interceptor , String... excludeMapping ) {
		try {
			InterceptorRegistry.addInterceptor(interceptor, excludeMapping);
		} catch (Exception e) {
			// TODO: handle exception
			log.error(" add fail : " + e.getMessage());
		}
	}
	
	public void start() {
		new ControllerFactory().registerController(controllerBasePackge);
		
		NioEventLoopGroup boss = new NioEventLoopGroup(this.bossThread);
		NioEventLoopGroup worker = new NioEventLoopGroup(this.worderThread);
		
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 128)
			.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
			.childHandler(new HandlerInitializer(this.maxContentLength));
			
			bootstrap.bind(this.port).sync().channel().closeFuture().sync();
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
		
		
	}
	
	
	

}
