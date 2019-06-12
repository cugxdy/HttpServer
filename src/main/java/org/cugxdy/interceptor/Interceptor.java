package org.cugxdy.interceptor;


import org.cugxdy.rest.HttpResponse;

import io.netty.handler.codec.http.FullHttpRequest;

public interface Interceptor {
	
	public boolean prePressor(FullHttpRequest request, HttpResponse response);
	
	public void postPressor(FullHttpRequest request, HttpResponse response);
	
	public void afterComplete(FullHttpRequest request, HttpResponse response);

}
