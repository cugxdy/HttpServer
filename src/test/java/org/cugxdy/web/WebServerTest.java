package org.cugxdy.web;

import org.cugxdy.controller.ExceptionController;
import org.cugxdy.core.WebServer;
import org.junit.Test;



public class WebServerTest {
    @Test
    public void test() throws InterruptedException {
    	
        // 忽略指定url
        WebServer.getIgnoreUrls().add("/favicon.ico");
        
        // 全局异常处理
        WebServer.setExceptionHandler(new ExceptionController());
        
        // 设置监听端口号
        WebServer server = new WebServer(2006);
        
        // 设置Http最大内容长度（默认 为10M）
        server.setMaxContentLength(1024 * 1024 * 50);
        
        // 设置Controller所在包
        server.setControllerBasePackge("org.cugxdy.controller");
        
        // 添加拦截器，按照添加的顺序执行。
        // 跨域拦截器
        // server.addInterceptor(new CorsInterceptor(), "/不用拦截的url");
        
        server.start();
    }
}
