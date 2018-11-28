package net.ytoec.kernel.action.api;

import java.util.List;

/**
 * 响应数据对象
 * 
 * @date 2013-5-26
 * @author tfhuang
 * 
 * @param <T>
 */
public class ResponseData<T> {

	private String message;// 响应消息

	private List<T> results;// 响应数据

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

}
