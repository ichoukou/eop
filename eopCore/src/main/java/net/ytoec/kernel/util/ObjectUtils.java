package net.ytoec.kernel.util;

import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 
 * 对象处理集合类 主要功能：排序，克隆，比较属性，拷贝等方法
 * 
 */

public class ObjectUtils {
	private static Logger log = LoggerFactory.getLogger(ObjectUtils.class);

	/**
	 * 调整list的顺序
	 * 
	 * @param list
	 *            数组
	 * @param field
	 *            字段
	 * @param value
	 *            值
	 * @param seqField
	 *            排序字段
	 * @param seqOffset
	 *            调换位置
	 * @return
	 */
	public static List adjustSeq(List list, String field, Object value, String seqField, int seqOffset) {
		List modifiedList = new ArrayList();
		if (seqOffset == 0) {
			return modifiedList;
		}

		int m = -1;
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			Object prop = getProperties(obj, field);
			if (prop == null) {
				continue;
			}
			if (prop.equals(value)) {
				m = i;
				break;
			}
		}

		Object target = list.remove(m);
		int newPos = m + seqOffset;
		if (newPos >= list.size()) {
			newPos = list.size() - 1;
		}
		if (newPos < 0) {
			newPos = 0;
		}
		log.debug(newPos + "");
		list.add(newPos, target);

		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			setProperties(obj, seqField, new Integer(i));
			modifiedList.add(obj);
		}
		return modifiedList;
	}

	/**
	 * 克隆对象
	 * 
	 * @param o1
	 *            对象
	 * @return
	 */
	public static Object clone(Object o1) {
		if (o1 == null) {
			return null;
		}
		Object o2;
		try {
			if (o1 instanceof Number) {
				return o1;
			}
			if (o1 instanceof String) {
				return o1;
			}

			return BeanUtils.cloneBean(o1);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 比较对象中的字段
	 * 
	 * @param o1
	 *            对象1
	 * @param o2
	 *            对象2
	 * @param orderField
	 *            字段
	 * @return
	 */
	private static int compareField(Object o1, Object o2, final String orderField) {
		try {
			Object f1 = getProperties(o1, orderField);
			Object f2 = getProperties(o2, orderField);
			Object[] ary = new Object[] { f1, f2 };
			if (f1 instanceof String && f2 instanceof String) {
				Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
				Arrays.sort(ary, cmp);
			} else {
				Arrays.sort(ary);
			}
			if (ary[0].equals(f1)) {
				return -1;
			} else {
				return 1;
			}
		} catch (Exception e) {
			
		}
		return 0;
	}

	/**
	 * 拷贝数组
	 * 
	 * @param srcList
	 *            源数组
	 * @param objList
	 *            目标数组
	 */
	public static void copyCollection(List srcList, List objList) {
		try {
			for (int i = 0; i < srcList.size(); i++) {
				Object o = srcList.get(i);
				objList.add(clone(o));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 拷贝集合
	 * 
	 * @param srcList
	 *            源集合
	 * @param objMap
	 *            目标集合
	 */
	public static void copyCollection(Map srcList, Map objMap) {
		try {
			for (Iterator iter = srcList.entrySet().iterator(); iter.hasNext();) {
				Map.Entry e = (Map.Entry) iter.next();
				objMap.put(clone(e.getKey()), clone(e.getValue()));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 拷贝对象属性
	 * 
	 * @param fromObj
	 *            源对象
	 * @param toObj
	 *            目标对象
	 */
	public static void copyProperties(Object fromObj, Object toObj) {
		try {
			PropertyUtils.copyProperties(toObj, fromObj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 拷贝属性
	 * 
	 * @param fromObj
	 *            源对象
	 * @param toObj
	 *            目标对象
	 * @param fieldName
	 *            字段名称
	 */
	public static void copyProperty(Object fromObj, Object toObj, String fieldName) {
		try {
			Object value = getProperties(fromObj, fieldName);
			setProperties(toObj, fieldName, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 取出二个数组中相同的部份
	 * 
	 * @param c1
	 *            数组1
	 * @param c2
	 *            数组2
	 * @return
	 */
	public static List crossList(Collection c1, Collection c2) {
		List list = new ArrayList();
		for (Iterator iter = c1.iterator(); iter.hasNext();) {
			Object o = iter.next();
			if (c2.contains(o)) {
				list.add(o);
			}
		}
		return list;
	}

	/**
	 * 数组中对象字段名转成long数组
	 * 
	 * @param objs
	 *            数组
	 * @param fieldName
	 *            字段名
	 * @return
	 */
	public static Long[] fieldArrayLong(List objs, String fieldName) {
		List list = new ArrayList();
		for (int i = 0; i < objs.size(); i++) {
			Object v = getProperties(objs.get(i), fieldName);
			if (v != null) {
				list.add(v);
			}
		}
		return (Long[]) list.toArray(new Long[0]);
	}

	/**
	 * 数组中对象字段名转成string数组
	 * 
	 * @param objs
	 *            数组
	 * @param fieldName
	 *            字段名
	 * @return
	 */
	public static String[] fieldArrayString(List objs, String fieldName) {
		List list = new ArrayList();
		for (int i = 0; i < objs.size(); i++) {
			Object v = getProperties(objs.get(i), fieldName);
			if (v != null) {
				list.add(v);
			}
		}
		return (String[]) list.toArray(new String[0]);
	}

	/**
	 * 数组中对象字段名转成数组
	 * 
	 * @param objs
	 *            数组
	 * @param fieldName
	 *            字段名
	 */
	public static List fieldListString(List objs, String fieldName) {
		List list = new ArrayList();
		for (int i = 0; i < objs.size(); i++) {
			Object v = getProperties(objs.get(i), fieldName);
			if (v != null) {
				String v2 = (String) v;
				if (!StringUtil.isBlank(v2)) {
					list.add(v2);
				}
			}
		}
		return list;
	}

	/**
	 * 数组中对象字段名转成string数组
	 * 
	 * @param objs
	 *            数组
	 * @param fieldName
	 *            字段名
	 * @return
	 */
	public static String[] fieldArrayString(Object[] objs, String fieldName) {
		List list = new ArrayList();
		for (int i = 0; i < objs.length; i++) {
			Object v = getProperties(objs[i], fieldName);
			if (v != null) {
				list.add(v);
			}
		}
		return (String[]) list.toArray(new String[0]);
	}

	/**
	 * 数组中对象字段名转成对象数组
	 * 
	 * @param objs
	 *            数组
	 * @param fieldName
	 *            字段名
	 * @return
	 */
	public static Object[] fieldArray(Object[] objs, String fieldName) {
		List list = new ArrayList();
		for (int i = 0; i < objs.length; i++) {
			Object v = getProperties(objs[i], fieldName);
			if (v != null) {
				list.add(v);
			}
		}
		return (Object[]) list.toArray(new Object[0]);
	}

	/**
	 * 格式化日期
	 * 
	 * @param format
	 *            格式
	 * @return
	 */
	public static Converter getDateConverter(final String format) {
		return new Converter() {
			public Object convert(Class type, Object value) {
				if (value == null) {
					return null;
				}

				if (value instanceof Date) {
					return value;
				}

				try {
					SimpleDateFormat sdf = new SimpleDateFormat(format);
					return sdf.parse(value.toString());

				} catch (Exception e) {
					throw new ConversionException(e);
				}
			}
		};
	}

	/**
	 * 获取数组中最大的对象
	 * 
	 * @param c
	 *            数组
	 * @param fieldName
	 *            字段名
	 * @return
	 */
	public static Object getMaxObject(Collection c, String fieldName) {
		Object maxObject = null;
		for (Iterator iter = c.iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			if (maxObject == null) {
				maxObject = element;
			} else {
				Object maxValue = getProperties(maxObject, fieldName);
				Object theValue = getProperties(element, fieldName);
				if (compareField(maxValue, theValue, fieldName) == 1) {
					maxObject = element;
				}
			}
		}
		return maxObject;
	}

	/**
	 * 获取数组中最小的对象
	 * 
	 * @param c
	 *            数组
	 * @param fieldName
	 *            字段名
	 * @return
	 */
	public static Object getMinObject(Collection c, String fieldName) {
		Object minObject = null;
		for (Iterator iter = c.iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			if (minObject == null) {
				minObject = element;
			} else {
				Object minValue = getProperties(minObject, fieldName);
				Object theValue = getProperties(element, fieldName);
				if (compareField(minValue, theValue, fieldName) == -1) {
					minObject = element;
				}
			}
		}
		return minObject;
	}

	/**
	 * 获取对象的属性值
	 * 
	 * @param obj
	 *            对象
	 * @param fieldName
	 *            字段名
	 * @return
	 */
	public static Object getProperties(Object obj, String fieldName) {
		try {
			return PropertyUtils.getProperty(obj, fieldName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 验证对象是否中数组对象中并返回此对象
	 * 
	 * @param list
	 *            数组
	 * @param field
	 *            字段
	 * @param value
	 *            对象
	 * @return
	 */
	public static Object indexOf(List list, String field, Object value) {
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			Object prop = getProperties(obj, field);
			if (prop == null) {
				continue;
			}
			if (prop.equals(value)) {
				return obj;
			}
		}
		return null;
	}

	/**
	 * 验证对象是否中数组对象中并返回此对象在数组中的位置
	 * 
	 * @param obj
	 *            对象
	 * @param list
	 *            数组
	 * @param property
	 *            属性名
	 * @return
	 * @throws Exception
	 */
	public static int indexOf(Object obj, List list, String property) throws Exception {
		for (int i = 0; i < list.size(); i++) {
			Object element = list.get(i);
			Object value = PropertyUtils.getProperty(element, property);
			if (value.equals(obj)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 不验证大小写判断数组中是否有此字符串
	 * 
	 * @param list
	 *            数组
	 * @param field
	 *            字段名
	 * @param value
	 *            字符串
	 * @return
	 */
	public static Object indexOfString(List list, String field, String value) {
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			String prop = (String) getProperties(obj, field);
			if (prop == null) {
				continue;
			}
			if (prop.equalsIgnoreCase(value)) {
				return obj;
			}
		}
		return null;
	}

	/**
	 * list 转为 set 集合
	 * 
	 * @param list
	 *            数组
	 * @return
	 */
	public static Set list2Set(List list) {
		Set set = new HashSet();
		set.addAll(list);
		return set;
	}

	public static void main(String[] args) {
		// String a = "chinA/中国";
		// String b = "chIna/中国";
		// //logger.debug(b.equals(a));
		// //logger.debug(b.equalsIgnoreCase(a));
		Converter c = getDateConverter("yyyyMMdd");
		//logger.debug(c.convert(null, "2008-02-15"));
	}

	/**
	 * 为对象设置属性
	 * 
	 * @param obj
	 *            对象
	 * @param fieldName
	 *            字段名
	 * @param value
	 *            值
	 */
	public static void setProperties(Object obj, String fieldName, Object value) {
		try {
			BeanUtils.setProperty(obj, fieldName, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 为数组以desc属性排序如asc,desc
	 * 例如sor(List<Article>, "createTime", true);
	 * @param collectoin
	 *            数组
	 * @param orderField
	 *            排序字段名
	 * @param desc
	 *            排序方式
	 * @return
	 */
	public static List sort(Collection collectoin, final String orderField, boolean desc) {
		List list = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			log.debug("list.get(i).getClass()", list.get(i).getClass());
		}
		if (collectoin == null) {
			return list;
		}
		Comparator c = new Comparator() {
			public int compare(Object o1, Object o2) {
				return compareField(o1, o2, orderField);
			}

			public boolean equals(Object obj) {
				return true;
			}
		};

		list.addAll(collectoin);

		Collections.sort(list, c);
		if (desc) {
			Collections.reverse(list);
		}
		return list;
	}

	/**
	 * 根据list中某个属性的值返回新的列表
	 * 
	 * @param list
	 * @param field
	 *            属性名
	 * @param value
	 *            条件的值
	 * @return
	 */
	public static List sublist(List list, String field, Object value) {
		List l = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			Object prop = getProperties(obj, field);
			if (prop == null) {
				if (prop == value) {
					l.add(obj);
				}
			} else {
				if (prop.equals(value)) {
					l.add(obj);
				}
			}
		}
		return l;
	}

	/**
	 * 重载 sublist方法
	 * 
	 * @param list
	 * @param field
	 *            属性名
	 * @param values
	 *            条件值数组
	 * @return
	 */
	public static List sublist(List list, String field, Object[] values) {
		List l = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			Object prop = getProperties(obj, field);
			if (prop == null) {
			} else {
				for (int j = 0; j < values.length; j++) {
					if (prop.equals(values[j])) {
						l.add(obj);
						break;
					}
				}
			}
		}
		return l;
	}

	/**
	 * 重载 sublist方法
	 * 
	 * @param list
	 *            数组
	 * @param from
	 *            开始位置
	 * @param to
	 *            结束位置
	 * @return
	 */
	public static List subList(List list, int from, int to) {
		if (from > list.size() || from < 0) {
			from = 0;
		}
		if (to > list.size() || to < 0) {
			to = list.size();
		}
		return list.subList(from, to);
	}

	public static void spaceToNull(Object obj) {
		Map map;
		try {
			map = PropertyUtils.describe(obj);
			Set set = map.entrySet();
			for (Iterator iter = set.iterator(); iter.hasNext();) {
				Map.Entry element = (Map.Entry) iter.next();
				Object v = element.getValue();
				if (v instanceof String) {
					if (StringUtil.isBlank((String) v)) {
						ObjectUtils.setProperties(obj, (String) element.getKey(), null);
					}
				} else if (v instanceof Long) {
					Long l = (Long) v;
					if (l.longValue() < 0) {
						ObjectUtils.setProperties(obj, (String) element.getKey(), null);
					}
				} else if (v instanceof Double) {
					Double d = (Double) v;
					if (d.doubleValue() < 0) {
						ObjectUtils.setProperties(obj, (String) element.getKey(), null);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void numberNull(Object obj) {

		try {
			Map map = PropertyUtils.describe(obj);
			Set set = map.entrySet();
			for (Iterator iter = set.iterator(); iter.hasNext();) {
				Map.Entry elem = (Map.Entry) iter.next();
				String key = (String) elem.getKey();
				Object value = elem.getValue();
				if (value != null) {
					if (value instanceof Number) {
						Number num = (Number) value;
						if (num.longValue() < 0) {
							PropertyUtils.setProperty(obj, key, null);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
/*	public static Object dao(String daoName){
		return Framework.getDaoFactory().getBean(daoName);
	}*/
}
