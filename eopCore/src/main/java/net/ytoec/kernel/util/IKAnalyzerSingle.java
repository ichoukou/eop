package net.ytoec.kernel.util;

import org.wltea.analyzer.lucene.IKAnalyzer;


public class IKAnalyzerSingle {

	private IKAnalyzer ikAnalyzer = new IKAnalyzer();
	// 构造私有实例
	private static IKAnalyzerSingle instance = null;

	// 构造函数私有话 不允许构造
	private IKAnalyzerSingle() {
	}

	public static IKAnalyzerSingle getInstance() {
		// //延迟加载
		// if (instance == null) {
		// //加锁 防止线程并发
		// synchronized (JDBCUtilSingle.class) {
		// 必须有的判断
		if (instance == null) {
			instance = new IKAnalyzerSingle();
		}
		// }
		// }
		return instance;
	}

	public IKAnalyzer getIkAnalyzer() {
		return ikAnalyzer;
	}

}
