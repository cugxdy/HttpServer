package org.cugxdy.core;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.RejectedExecutionHandlers;

public class HandlerInitializer extends ChannelInitializer<SocketChannel> {

	private int maxContentLength;
	
	private static int eventExecutorGroupThreads = 0;
	
	private static int eventExecutorGroupQueues = 0;
	
	static {
        eventExecutorGroupThreads = Integer.getInteger("http.executor.threads", 0);
        if(eventExecutorGroupThreads == 0) {
            eventExecutorGroupThreads = Runtime.getRuntime().availableProcessors() * 2;
        }
        
        eventExecutorGroupQueues = Integer.getInteger("http.executor.queues", 0);
        if(eventExecutorGroupQueues == 0) {
            eventExecutorGroupQueues = 1024;
        }
	}
	
	
	private static final EventExecutorGroup eventExecutorGroup = new DefaultEventExecutorGroup(eventExecutorGroupThreads, new ThreadFactory() {
        private AtomicInteger threadIndex = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "HttpRequestHandlerThread_" + this.threadIndex.incrementAndGet());
        }
    }, eventExecutorGroupQueues, RejectedExecutionHandlers.reject());
	
	
	
	public HandlerInitializer(int maxContentLength) {
		// TODO Auto-generated constructor stub
		this.maxContentLength = maxContentLength;
	}
	
	
	

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// TODO Auto-generated method stub
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpObjectAggregator(maxContentLength));
        pipeline.addLast("encoder", new HttpResponseEncoder());
        // 启用gzip（由于使用本地存储文件，不能启用gzip）
        //pipeline.addLast(new HttpContentCompressor(1));
        pipeline.addLast(new ChunkedWriteHandler());
        // 将HttpRequestHandler放在业务线程池中执行，避免阻塞worker线程。
        pipeline.addLast(eventExecutorGroup, "httpRequestHandler", new HttpRequestHandler());
	}


}
