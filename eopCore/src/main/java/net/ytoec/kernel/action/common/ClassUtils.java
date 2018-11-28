package net.ytoec.kernel.action.common;

public class ClassUtils {
	private ClassUtils() {

	}

	/**
	 * ��ݶ���������õ������һ��ʵ��.
	 * 
	 * @param className
	 *            ����������.
	 * @return �����͵�һ��ʵ��.
	 * @throws ClassNotFoundException
	 *             ����������һ����Ч���������������ʾ�����ļ�������,�׳����쳣.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static Object getInstance(String className) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		Object result = null;
		try {
			result = clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
}
