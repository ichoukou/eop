package net.ytoec.kernel.exception;

public class GenNumException extends RuntimeException {
	private static final long serialVersionUID = 8226185151741057437L;
	private String errorCode;
	private String paramter;

	public GenNumException() {

	}

	public GenNumException(String errorCode) {
		this.errorCode = errorCode;

	}

	public GenNumException(String errorCode, String paramter) {
		this.errorCode = errorCode;
		this.paramter = paramter;

	}

	public String getErrorCode() {
		return this.errorCode;

	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;

	}

	public void setParamter(String paramter) {
		this.paramter = paramter;

	}

	public String getParamter() {
		return this.paramter;

	}

	public String toString() {
		return "{\"errorCode\":\"" + this.errorCode + "\",\"paramter\":\""
				+ this.paramter + "\"}";

	}

}