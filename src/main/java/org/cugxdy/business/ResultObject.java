package org.cugxdy.business;

import java.util.List;
import java.util.Map;

// 数据服务结果对象
public class ResultObject {
	private String ErrerCode;
	private String ErrerText;
	
	private List<?> list;

	public final String getErrerCode() {
		return ErrerCode;
	}

	public final void setErrerCode(String errerCode) {
		ErrerCode = errerCode;
	}

	public final String getErrerText() {
		return ErrerText;
	}

	public final void setErrerText(String errerText) {
		ErrerText = errerText;
	}

	public final List<?> getList() {
		return list;
	}

	public final void setList(List<?> list) {
		this.list = list;
	}
}
