package net.ytoec.kernel.service;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dao.DFAOutCallBack;


public class DFA {
	
	public DFAState m_StartState;// 起始状态

	public static Map<String, Integer> m_Keywords = new HashMap<String, Integer>();// 记录了当前关键词集合

	public DFA() {// 初始化DFA，设置初始状态
		m_StartState = new DFAState(null);
		m_StartState.setM_Failure(m_StartState);
	}
	

	// 清除所有DFA状态
	public void closeDFA() {
		CleanStates(m_StartState);
	}

	// 增加关键词，同时重建DFA
	public void AddKeyword(String keyword) {
		m_Keywords.put(keyword, 0);
		RebuildDFA();
	}

	// 删除关键词，同时重建DFA
	public void DeleteKeyword(String keyword) {
		m_Keywords.remove(keyword);
		RebuildDFA();
	}

	// 检索字符串text是否包含关键词，并调用outputCallback处理匹配的关键词
	public void Search(String text, DFAOutCallBack outputCallback) {
		DFAState curState = m_StartState;
		int i;
		for (i = 0; i < text.length(); ++i) {
			// 查看状态机中当前状态下该字符对应的下一状态，如果在当前状态下找不到满足该个字符的状态路线，
			// 则返回到当前状态的失败状态下继续寻找，直到初始状态
			while (curState.getM_Goto().containsKey(text.charAt(i)) == false) {
				if (curState.getM_Failure() != m_StartState) {
					if (curState == curState.getM_Failure()) { // 陷入死循环了...
						System.out.println("DFA Failure");
						break;
					}
					curState = curState.getM_Failure(); // 返回到当前状态的失败状态
				} else {
					curState = m_StartState;
					break;
				}
			}
			// 如果当前状态下能找到该字符对应的下一状态，则跳到下一状态m，
			// 如果状态m包含了m_Output，表示匹配到了关键词，具体原因请继续往下看
			if (curState.getM_Goto().containsKey(text.charAt(i))) {
				curState = curState.getM_Goto().get(text.charAt(i));
				if (!curState.getM_Output().isEmpty()) {
					curState.Output(outputCallback);
				}
			}
		}

	}
	
	/**
	 *  检索字符串text是否包含关键词，并将处理的结果返回
	 * @param text 目标内容
	 * @return 目标内容中含有的敏感词列表。
	 * @author zhengliang
	 */
	public List<String> filter(String text) {
		DFAState curState = m_StartState;
		List<String> keywords = new ArrayList<String>();
		int i;
		for (i = 0; i < text.length(); ++i) {
			// 查看状态机中当前状态下该字符对应的下一状态，如果在当前状态下找不到满足该个字符的状态路线，
			// 则返回到当前状态的失败状态下继续寻找，直到初始状态
			while (curState.getM_Goto().containsKey(text.charAt(i)) == false) {
				if (curState.getM_Failure() != m_StartState) {
					if (curState == curState.getM_Failure()) { // 陷入死循环了...
						//System.out.println("DFA Failure");
						break;
					}
					curState = curState.getM_Failure(); // 返回到当前状态的失败状态
				} else {
					curState = m_StartState;
					break;
				}
			}
			// 如果当前状态下能找到该字符对应的下一状态，则跳到下一状态m，
			// 如果状态m包含了m_Output，表示匹配到了关键词，具体原因请继续往下看
			if (curState.getM_Goto().containsKey(text.charAt(i))) {
				curState = curState.getM_Goto().get(text.charAt(i));
				if (!curState.getM_Output().isEmpty()) {
					keywords = curState.Output(keywords);
				}
			}
		}
		return keywords;

	}


	// 添加关键词到状态机的实际操作，建立状态节点，并设置最后结束节点的m_Output
	void DoAddWord(String keyword) {
		int i;
		DFAState curState = m_StartState;

		for (i = 0; i < keyword.length(); i++) {
			curState = curState.AddGoto(keyword.charAt(i));
		}

		curState.getM_Output().add(keyword);
	}

	// 重建状态机
	void RebuildDFA() {
		CleanStates(m_StartState);

		m_StartState = new DFAState(null);
		m_StartState.setM_Failure(m_StartState);
		// add all keywords
		for (String key : m_Keywords.keySet()) {
			DoAddWord(key);
		}
		// 为每个状态节点设置失败跳转的状态节点
		DoFailure();
	}

	// 清除state下的所有状态节点
	void CleanStates(DFAState state) {
		for (char key : state.getM_Goto().keySet()) {
			CleanStates(state.getM_Goto().get(key));
		}
		state = null;
	}

	// 为每个状态节点设置失败跳转的状态节点
	void DoFailure() {

		LinkedList<DFAState> q = new LinkedList<DFAState>();
		// 首先设置起始状态下的所有子状态,设置他们的m_Failure为起始状态，并将他们添加到q中
		for (char c : m_StartState.getM_Goto().keySet()) {
			q.add(m_StartState.getM_Goto().get(c));
			m_StartState.getM_Goto().get(c).setM_Failure(m_StartState);
		}

		while (!q.isEmpty()) {
			// 获得q的第一个element，并获取它的子节点，为每个子节点设置失败跳转的状态节点
			DFAState r = q.getFirst();
			DFAState state;
			q.remove();
			for (char c : r.getM_Goto().keySet()) {
				q.add(r.getM_Goto().get(c));
				// 从父节点的m_Failure(m1)开始，查找包含字符c对应子节点的节点，
				// 如果m1找不到，则到m1的m_Failure查找，依次类推
				state = r.getM_Failure();
				while (state.getM_Goto().containsKey(c) == false) {
					state = state.getM_Failure();
					if (state == m_StartState) {
						break;
					}
				}
				// 如果找到了，设置该子节点的m_Failure为找到的目标节点(m2)，
				// 并把m2对应的关键词列表添加到该子节点中
				if (state.getM_Goto().containsKey(c)) {
					r.getM_Goto().get(c).setM_Failure(state.getM_Goto().get(c));
					for (String str : r.getM_Goto().get(c).getM_Failure().getM_Output()) {
						r.getM_Goto().get(c).getM_Output().add(str);
					}
				} else { // 找不到，设置该子节点的m_Failure为初始节点
					r.getM_Goto().get(c).setM_Failure(m_StartState);
				}
			}

		}
	}
	

};
