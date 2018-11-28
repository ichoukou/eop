package net.ytoec.kernel.dataobject;

/**
 * 敏感词过滤结果类
 * @author zhengliang
 */


import java.util.List;

public class FilterResult {
	
	/**
	 * 敏感词验证结果，true-通过；false-不通过。
	 */
	private boolean isvalid;
	
	/**
	 * 目标内容含有的敏感词列表，当目标内容验证不通过时，此属性才会有值。
	 */
	private List<String> invalidWords;

	public boolean isvalid() {
		return isvalid;
	}

	public void setIsvalid(boolean isvalid) {
		this.isvalid = isvalid;
	}

	/**
	 * 返回目标内容含有的所有敏感词列表
	 * @return
	 */
	public List<String> getInvalidWords() {
		return invalidWords;
	}

	public void setInvalidWords(List<String> invalidWords) {
		this.invalidWords = invalidWords;
	}
	
	/**
	 * 返回目标内容含有的所有敏感词拼接字符串
	 * @return
	 */
	public String getInvalidWordsStr(){
		StringBuilder wordBuilder = new StringBuilder();
		for(String word : invalidWords){
			wordBuilder.append("“"+word+"”");
			if(invalidWords.indexOf(word) < invalidWords.size()-1){
				wordBuilder.append("、");
			}
		}
		return wordBuilder.toString();
	}
	
	/**
	 * 将FilterResult对象输出，以便调试查看。
	 */
	public void printOut(){
		System.out.println("************************FilterResult对象解析输出开始***************************");
		System.out.println("目标内容验证结果： "+isvalid+"->"+(isvalid ? "通过":"不通过"));
		if(!isvalid){
			System.out.print("目标内容含有不和谐的内容："+getInvalidWordsStr());
			System.out.println("");
		}
		System.out.println("************************FilterResult对象解析输出完毕***************************");
	}

}
