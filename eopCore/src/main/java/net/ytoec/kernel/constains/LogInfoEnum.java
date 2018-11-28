package net.ytoec.kernel.constains;


public enum LogInfoEnum {

	PARAM_EMPTY("params is empty"),
	XML_FORMAT_INVALID("xml format is invalid"),
	PARAM_INVALID("params is invalid"),
	PERSISTENCE_FAILED("data persistence failed"),
	PARSE_INVALID("parse is invalid"),
	STATE_EXCEPTION("state is exceptional"),
	DATA_REPEAT("data repeat"),
	DATA_ACCESS_EXCEPTION("data access is exceptional"),
	RUNTIME_EXCEPTION("runtime is exceptional"),
	TASK_EXCEPTION("task is exceptional"),
	UPDATE_TAOBAOENCODEKEY_BY_ID("ec_core_user update taobaoencodekey by id failed");
	
	String errorInfo;

	private LogInfoEnum(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	
	public  String getValue(){
		return errorInfo;
	}
}
