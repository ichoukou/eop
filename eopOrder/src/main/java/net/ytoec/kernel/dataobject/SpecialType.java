package net.ytoec.kernel.dataobject;

/**
 * 
 * 特殊商品性质枚举类.
 * 
 */
public enum SpecialType {
	COMMON("0", "common", "普通品"), EASY_BROKEN("1", "easyBroken", "易碎品"), FLUID(
			"2", "fluid", "液态品"), CHEMICAL("3", "chemical", "化学品"), WHITE_POWDER(
			"4", "whitePowder", "白色粉末状品"), CIGARETTE("5", "cigarette", "香烟");
	private String code;
	private String name;
	private String descriptor;

	private SpecialType(String code, String name, String descriptor) {
		this.code = code;
		this.name = name;
		this.descriptor = descriptor;
	}

	public String getCode() {
		return this.code;
	}

	public String getName() {
		return this.name;
	}

	public String getDescriptor() {
		return this.descriptor;
	}

	/**
	 * 根据code码获取SpecialType对象.
	 * 
	 * @param code
	 *            code码.
	 * @return SpecialType对象.
	 */
	public static SpecialType valueOfByCode(String code) {
		SpecialType result = null;
		StringBuilder sb = new StringBuilder();
		for (SpecialType specialType : SpecialType.values()) {
			if (specialType.getCode().equals(code)) {
				result = specialType;
			}
			sb.append(specialType.getCode());
			sb.append(",");
		}
		if (result == null) {
			throw new IllegalStateException("不合法的状态:无效的code码,正确的code码应为"
					+ sb.toString() + "其中之一,而当前code码为:" + code);
		}
		return result;
	}
}
