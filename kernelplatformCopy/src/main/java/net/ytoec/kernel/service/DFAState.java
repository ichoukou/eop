package net.ytoec.kernel.service;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dao.DFAOutCallBack;


public class DFAState {
	public DFAState(DFAState parent) {
		this.m_Parent = parent;
		this.m_Failure = null;
	}

	// 记录了该状态节点下，字符-->另一个状态的对应关系
	private Map<Character, DFAState> m_Goto = new HashMap<Character, DFAState>();
	// 如果该状态下某具体字符找不到对应的下一状态，应该跳转到m_Failure状态继续查找
	private DFAState m_Failure;
	// 该状态节点的前一个节点
	private DFAState m_Parent;
	// 记录了到达该节点时，匹配到的关键词
	private List<String> m_Output = new ArrayList<String>();

	public Map<Character, DFAState> getM_Goto() {
		return m_Goto;
	}

	public void setM_Goto(Map<Character, DFAState> m_Goto) {
		this.m_Goto = m_Goto;
	}

	public DFAState getM_Failure() {
		return m_Failure;
	}

	public void setM_Failure(DFAState m_Failure) {
		this.m_Failure = m_Failure;
	}

	public DFAState getM_Parent() {
		return m_Parent;
	}

	public void setM_Parent(DFAState m_Parent) {
		this.m_Parent = m_Parent;
	}

	public List<String> getM_Output() {
		return m_Output;
	}

	public void setM_Output(List<String> m_Output) {
		this.m_Output = m_Output;
	}

	// 为当前状态节点添加字符c对应的下一状态
	DFAState AddGoto(char c) {
		if (!m_Goto.containsKey(c)) {
			// not in the goto table
			DFAState newState = new DFAState(this);
			m_Goto.put(c, newState);
			return newState;
		} else {
			return m_Goto.get(c);
		}
	}

	// 调用outputCallback处理当前状态节点对应的关键词列表
	void Output(DFAOutCallBack outputCallback) {
		for (@SuppressWarnings("rawtypes")
		Iterator iter = m_Output.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			outputCallback.CallBack(element);
		}
	}
	
	/**
	 *  调用outputCallback处理当前状态节点对应的关键词列表,并返回列表。
	 * @param keywords
	 * @author zhengliang
	 * @return
	 */
	List<String> Output(List<String> keywords) {
		for (@SuppressWarnings("rawtypes")
				Iterator iter = m_Output.iterator(); iter.hasNext();) {
					String element = (String) iter.next();
					if(!keywords.contains(element)){
						keywords.add(element);
					}
		}
		return keywords;
	}
}
