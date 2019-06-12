package org.cugxdy.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.handler.codec.http.FullHttpRequest;

public final class RequestInfo {
	// HTTP请求
	private FullHttpRequest request;
	// 响应
	private HttpResponse response;
	// url请求头
	private Map<String ,Object> paramters = new HashMap<String,Object>();
	// 请求头
	private Map<String , String> headers = new HashMap<String,String>();
	// 请求主体
	private String body;
	// 表单数据
	private Map<String,String> formData = new HashMap<String,String>();
	// 上传文件
	private List<MultipartFile> files = new ArrayList<MultipartFile>();

	public final FullHttpRequest getRequest() {
		return request;
	}

	public final void setRequest(FullHttpRequest request) {
		this.request = request;
	}

	public final HttpResponse getResponse() {
		return response;
	}

	public final void setResponse(HttpResponse response) {
		this.response = response;
	}

	public final Map<String, Object> getParamters() {
		return paramters;
	}

	public final void setParamters(Map<String, Object> paramters) {
		this.paramters = paramters;
	}

	public final Map<String, String> getHeaders() {
		return headers;
	}

	public final void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public final String getBody() {
		return body;
	}

	public final void setBody(String body) {
		this.body = body;
	}

	public final Map<String, String> getFormData() {
		return formData;
	}

	public final void setFormData(Map<String, String> formData) {
		this.formData = formData;
	}

	public final List<MultipartFile> getFiles() {
		return files;
	}

	public final void setFiles(List<MultipartFile> files) {
		this.files = files;
	}

	@Override
	public String toString() {
		return "RequestInfo [request=" + request + ", response=" + response + ", paramters=" + paramters + ", headers="
				+ headers + ", body=" + body + ", formData=" + formData + ", files=" + files + "]";
	}
	
	
	
	
}
