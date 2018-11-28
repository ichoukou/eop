package net.ytoec.kernel.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.QuestionnaireTagDao;
import net.ytoec.kernel.dataobject.QuestionnaireTag;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.service.QuestionnaireTagService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.service.UserThreadService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 问题件标签service接口实现
 * @author wangjianzhong
 * @2012-8-2
 * @param <T>
 */

@Service
@Transactional
public class QuestionnaireTagServiceImpl<T extends QuestionnaireTag> implements QuestionnaireTagService<T> {
	
	private static final int TAG_MAX_COUNT = 10;

	private static Logger logger = LoggerFactory.getLogger(QuestionnaireTagServiceImpl.class);
	
	@Inject
	private QuestionnaireTagDao questionnaireTagDao;
	
	@Inject
	private UserService userService; 
	
	@Inject
	private UserThreadService userThreadService; 
	
	@Override
	public boolean addQuestionnaireTag(T questionnaireTag) {
		return questionnaireTagDao.addQuestionnaireTag(questionnaireTag);
	}

	@Override
	public boolean editQuestionnaireTag(T questionnaireTag) {
		return questionnaireTagDao.editQuestionnaireTag(questionnaireTag);
	}

	@Override
	public T getQestionnaireTag(Integer id) {
		return (T)questionnaireTagDao.getQestionnaireTag(id);
	}

	@Override
	public List<T> getQestionnaireTagsByUserCode(String userCode) {
		UserThread userThread = getUserThreadByUserCode(userCode);
		if(userThread!=null) {
			return questionnaireTagDao.getQestionnaireTagsByUserThreadId(userThread.getId());
		}
		return new ArrayList<T>();
	}
	
	@Override
	public Integer getQestionnaireTagsCountByUserCode(String userCode) {
		UserThread userThread = getUserThreadByUserCode(userCode);
		if(userThread!=null) {
			return questionnaireTagDao.getQestionnaireTagsCountByUserThreadId(userThread.getId());
		}
		return 0;
	}
	
	public List<T> getQestionnaireTagsByUserThreadId(Integer userThreadId) {
		return questionnaireTagDao.getQestionnaireTagsByUserThreadId(userThreadId);
	}

	@Override
	public Integer getQestionnaireTagsCountByUserThreadId(Integer userThreadId) {
		return questionnaireTagDao.getQestionnaireTagsCountByUserThreadId(userThreadId);
	}

	@Override
	public Map<String,Object> addQuestionnaireTag(String userCode,String tagName) {
		Map<String,Object> resMap = new HashMap<String,Object>();
		//获取UserThread
		UserThread userThread = getUserThreadByUserCode(userCode);
		if(userThread==null) {
			resMap.put("err", "4");
			return resMap;
		}
		int userThreadId = userThread.getId();
		int count = questionnaireTagDao.getQestionnaireTagsCountByUserThreadId(userThreadId);
		if(count>=TAG_MAX_COUNT) {  //验证数量
			resMap.put("err", "1");
			return resMap;
		}
		if(isUniquenessTagName(resMap,userThreadId,tagName,null)) {  //验证唯一
			int max = questionnaireTagDao.getQestionnaireTagMaxPos(userThreadId);
			Date date = new Date();	
			QuestionnaireTag tagBean = new QuestionnaireTag();
			tagBean.setTagUserThreadId(userThreadId);
			tagBean.setTagName(tagName);  
			tagBean.setTagPos(max+1);    
			tagBean.setTagType(0);
			tagBean.setCreateTime(date);
			tagBean.setUpdateTime(date);
			boolean isSaveOk = questionnaireTagDao.addQuestionnaireTag(tagBean);
			if(isSaveOk) {
				proAddQuestionnaireTag(userThreadId);
				resMap.put("res",tagBean);
			}else {
				resMap.put("err", "3");
			}
		}else {
			resMap.put("err", "2");
		}
		return resMap;
	}
	
	public int getQuestionnaireTagType(String userCode) {
		QuestionnaireTag tagBean = new QuestionnaireTag();
		UserThread userThread = getUserThreadByUserCode(userCode);
		if(userThread==null) {
			return -1;
		}
		int userThreadId = userThread.getId();
		tagBean.setTagUserThreadId(userThreadId);
		tagBean.setTagType(1);
		List<T> list = questionnaireTagDao.getQestionnaireTags(tagBean);
		if(list!=null&&list.size()>0) {
			return 1;
		}
		return -1;
	}
	
	/**
	 * 处理标签新增,用来判断是否第1次新增标签
	 */
	private void proAddQuestionnaireTag(int userThreadId) {
		QuestionnaireTag tagBean = new QuestionnaireTag();
		tagBean.setTagUserThreadId(userThreadId);
		tagBean.setTagType(-1);
		List<T> list = questionnaireTagDao.getQestionnaireTags(tagBean);
		if(list!=null&&list.size()>0) {
			tagBean = list.get(0);
			tagBean.setTagType(1);
			questionnaireTagDao.editQuestionnaireTag(tagBean);
		}
	}
	
	@Override
	public List<T> getQestionnaireTagsByUserCodeAndCreateDef(String userCode) {
		
		//判断标签是否已拥有
		Integer count = getQestionnaireTagsCountByUserCode(userCode);
		if(count!=null&&count>0) {  //表示存在
			return getQestionnaireTagsByUserCode(userCode);
		}
		//无标签,创建默认标签
		UserThread userThread = getUserThreadByUserCode(userCode);
		if(userThread!=null) {
			saveTags(userThread.getId(),"已处理",0,-1);
			saveTags(userThread.getId(),"紧急件",1,0);
			saveTags(userThread.getId(),"跟踪件",2,0);
			saveTags(userThread.getId(),"不处理",3,0);
			saveTags(userThread.getId(),"待处理 ",4,0);
		}
		return getQestionnaireTagsByUserCode(userCode);
	}
	
	/**
	 * 根据用户CODE获取UserThread
	 * @param userCode
	 * @return
	 */
	private UserThread getUserThreadByUserCode(String userCode) {
		UserThread userThread = new UserThread();
		userThread.setUserCode(userCode);
		List<UserThread> list = userThreadService.searchUsersByCode(userThread);
		if(list!=null&&list.size()>0) {
			userThread = list.get(0);
		}else {
			logger.error("SERVICE:QuestionnaireTagServiceImpl---METHOD:getUserThreadByUserCode: can not found userThread by the userCode: "+userThread);
			return null;
		}
		return userThread;
	}
	
	private void saveTags(int userThreadId,String tagName,int tagPos,int tagType) {
		Date date = new Date();	
		QuestionnaireTag tagBean = new QuestionnaireTag();
		tagBean.setTagUserThreadId(userThreadId);
		tagBean.setTagName(tagName);  
		tagBean.setTagPos(tagPos);    
		tagBean.setTagType(tagType);
		tagBean.setCreateTime(date);
		tagBean.setUpdateTime(date);
		boolean isSaveOk = questionnaireTagDao.addQuestionnaireTag(tagBean);
	}

	@Override
	public boolean isUniquenessTagName(Map<String,Object> resMap,Integer userThreadId,String tagName,Integer quondamId) {
		QuestionnaireTag tagBean = new QuestionnaireTag();
		tagBean.setTagUserThreadId(userThreadId);
		tagBean.setTagName(tagName);
		List<T> list = questionnaireTagDao.getQestionnaireTags(tagBean);
		if(list!=null&&list.size()>0) {
			if(quondamId!=null) {   //非新增
				tagBean = list.get(0);
				resMap.put("res", tagBean);
				if(tagBean.getId().intValue() == quondamId.intValue()) {  //修改对象的自身
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
		}
		return true;
	}

	@Override
	public int removeQuestionnaireTag(Integer id) {
		//获取判断该标签在问题件表中,是否被使用
		Integer count = questionnaireTagDao.getTagCountInQuestionnaire(id);
		if(count!=null&&count>0) {
			return 2;  //标签被使用中
		}
		return questionnaireTagDao.removeQuestionnaireTag(id)?0:1;
	}

	@Override
	public Map<String,Object> editQuestionnaireTagName(String userCode,Integer id,String tagName) {
		Map<String,Object> resMap = new HashMap<String,Object>();
		//获取UserThread
		UserThread userThread = getUserThreadByUserCode(userCode);
		if(userThread==null) {
			resMap.put("err", "4");
			return resMap;
		}
		int userThreadId = userThread.getId();
		if(isUniquenessTagName(resMap,userThreadId,tagName,id)) {
			QuestionnaireTag tagBean = (QuestionnaireTag)questionnaireTagDao.getQestionnaireTag(id);
			tagBean.setTagName(tagName);
			boolean isSaveOk = questionnaireTagDao.editQuestionnaireTag(tagBean);
			if(isSaveOk) {
				resMap.put("res",tagBean);
			}else {
				resMap.put("err", "3");
			}
		}else {
			resMap.put("err", "2");
		}
		return resMap;
	}

	@Override
	public boolean swapQuestionnaireTag(Integer beforeTagId, Integer afterTagId) {
		//验证参数
		if(beforeTagId==null&&afterTagId==null) {
			return false;
		}
		QuestionnaireTag beforeTag = null;
		QuestionnaireTag afterTag = null;
		int beforePos = 0;
		int afterPos = 0; 
		//查询对象,获取他们的位置
		if(beforeTagId!=null) {
			beforeTag = (QuestionnaireTag)questionnaireTagDao.getQestionnaireTag(beforeTagId);
			beforePos = beforeTag.getTagPos();
		}
		if(afterTagId!=null) {
			afterTag = (QuestionnaireTag)questionnaireTagDao.getQestionnaireTag(afterTagId);
			afterPos = afterTag.getTagPos();
		}
		//两对象不存在时,交换失败
		if(beforeTag==null&&afterTag==null) {
			return false;
		}
		boolean edit1 = false;
		boolean edit2 = false;
		//两对象存在时,直接交换位置
		if(beforeTag!=null&&afterTag!=null) {
			beforeTag.setTagPos(afterPos);
			afterTag.setTagPos(beforePos);
			edit1 = questionnaireTagDao.editQuestionnaireTag(beforeTag);
			edit2 = questionnaireTagDao.editQuestionnaireTag(afterTag);
			return (edit1&&edit2);
		}
		//前对象为NULL表示后对象上移一位
		if(beforeTag==null) {
			if(afterPos>0) {  //0为顶层
				afterPos = afterPos - 1;
				afterTag.setTagPos(afterPos);
				return questionnaireTagDao.editQuestionnaireTag(afterTag);
			}
		}
		//后对象为NULL表示前对象下移一位
		if(afterTag==null) {
			//if(afterPos<TAG_MAX_COUNT) {
				beforePos = beforePos + 1;
				beforeTag.setTagPos(beforePos);
				return questionnaireTagDao.editQuestionnaireTag(beforeTag);
			//}
		}
		return false;
	}

	
	/*@Override
	public boolean moveQuestionnaireTag(Integer id, Integer step) {
		if(id!=null) {
			QuestionnaireTag tag = (QuestionnaireTag)questionnaireTagDao.getQestionnaireTag(id);
		}
		return false;
	}
	*/

	@Override
	public List<T> getQestionnaireTags(T entity) {
		return questionnaireTagDao.getQestionnaireTags(entity);
	}

}
