package net.ytoec.kernel.action.waybill;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.ytoec.kernel.action.remote.xml.QueryOrder;
import net.ytoec.kernel.action.remote.xml.StepInfo;

@SuppressWarnings("all")
public class SortList<E> {

	public void Sort(List<E> list, final String method, final String sort) {
		Collections.sort(list, new Comparator() {
			public int compare(Object a, Object b) {
				int ret = 0;
				try {
					Method m1 = ((E) a).getClass().getMethod(method, null);
					Method m2 = ((E) b).getClass().getMethod(method, null);
					if (sort != null && "desc".equals(sort))// 倒序
						ret = m2.invoke(((E) b), null).toString()
								.compareTo(m1.invoke(((E) a), null).toString());
					else
						// 正序
						ret = m1.invoke(((E) a), null).toString()
								.compareTo(m2.invoke(((E) b), null).toString());
				} catch (NoSuchMethodException ne) {
					System.out.println(ne);
				} catch (IllegalAccessException ie) {
					System.out.println(ie);
				} catch (InvocationTargetException it) {
					System.out.println(it);
				}
				return ret;
			}
		});
	}

	// 排序测试
	public static void main(String[] args) {

		List<QueryOrder> list = new ArrayList<QueryOrder>();

		QueryOrder queryOrder = new QueryOrder();
		queryOrder.setValue(0);
		queryOrder.setAcceptTime("");

		QueryOrder queryOrder6 = new QueryOrder();
		queryOrder6.setValue(0);
		queryOrder6.setAcceptTime(null);

		QueryOrder queryOrder2 = new QueryOrder();
		queryOrder2.setValue(1);
		queryOrder2.setAcceptTime("Fri Jun 15 15:36:31 CST 2012");

		QueryOrder queryOrder3 = new QueryOrder();
		queryOrder3.setValue(2);
		queryOrder3.setAcceptTime("Fri Jun 15 15:36:32 CST 2012");

		QueryOrder queryOrder4 = new QueryOrder();
		queryOrder4.setValue(3);
		queryOrder4.setAcceptTime("Fri Jun 15 15:36:33 CST 2012");

		QueryOrder queryOrder5 = new QueryOrder();
		queryOrder5.setValue(4);
		queryOrder5.setAcceptTime("Fri Jun 15 15:36:34 CST 2012");

		list.add(queryOrder);
		list.add(queryOrder4);
		list.add(queryOrder2);
		list.add(queryOrder3);
		list.add(queryOrder5);
		list.add(queryOrder6);

		// 按状态进行排序
		SortList<QueryOrder> sortList = new SortList<QueryOrder>();
		sortList.Sort(list, "getAcceptTime", "asc");
		sortList.Sort(list, "getValue", "asc");

		for (QueryOrder order : list) {
			System.out.println(order.toString());
		}

	}

}
