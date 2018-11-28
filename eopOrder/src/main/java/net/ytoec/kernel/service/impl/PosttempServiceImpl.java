package net.ytoec.kernel.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.PosttempDao;
import net.ytoec.kernel.dao.PosttempUserDao;
import net.ytoec.kernel.dao.StandardPosttempDao;
import net.ytoec.kernel.dao.UserDao;
import net.ytoec.kernel.dao.UserThreadContractDao;
import net.ytoec.kernel.dao.UserThreadDao;
import net.ytoec.kernel.dataobject.Postinfo;
import net.ytoec.kernel.dataobject.Posttemp;
import net.ytoec.kernel.dataobject.PosttempUser;
import net.ytoec.kernel.dataobject.StandardPosttemp;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.dataobject.UserThreadContract;
import net.ytoec.kernel.service.PosttempService;
import net.ytoec.kernel.util.XmlUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 运费模板
 * 
 * @author ChenRen
 * @date 2011-09-09
 * 
 * @param <T>
 */
@Service
@Transactional
@SuppressWarnings("unchecked")
public class PosttempServiceImpl<T extends Posttemp> implements
        PosttempService<T> {

    private static Logger logger = LoggerFactory.getLogger(PosttempServiceImpl.class);
    @Inject
    private PosttempDao<T> dao;
    @Inject
    private PosttempUserDao<PosttempUser> puDao;
    @Inject
    private UserDao<User> userDao;
    @Inject
    private UserThreadDao<UserThread> userThreadDao;
    @Inject
	private UserThreadContractDao<UserThreadContract> userThreadContractDao;
    @Inject
    private StandardPosttempDao<StandardPosttemp> spDao;
	@Inject
	private PosttempUserDao<PosttempUser> posttempUserDao;
	
	@Inject
    private PosttempDao<Posttemp> posttemDao;
    

    /**
     * 运费信息模板 <br>
     * 模板中有三个标识符：$fwp,$owp,$provs分别表示：首重、超重、地区<br>
     * 使用这个模板的时候要使用正确的数值替换这三个标识符
     */
    private static String postinfoTemp;

    /**
     * 如果 {@link #postinfoTemp} 为空, 就创建运费信息模板； 否则返回 TA
     * 
     * @return
     */
    private static String getPostinfoTemp() {
        if (StringUtils.isEmpty(postinfoTemp)) {
            StringBuilder x = new StringBuilder();
            
            x.append("<object class=\"net.ytoec.kernel.dataobject.Postinfo\">")
                    .append("<void property=\"destId\"><string>$did</string></void>")
                    .append("<void property=\"destText\"><string>$dtxt</string></void>")
                    .append("<void property=\"srcId\"><string>$sid</string></void>")
                    .append("<void property=\"srcText\"><string>$stxt</string></void>")
                    .append("<void property=\"fwStandardPrice\"><float>$fws</float></void>")
                    .append("<void property=\"fwDiscount\"><float>$fwd</float></void>")
                    .append("<void property=\"fwRealPirce\"><float>$fwr</float></void>")
                    .append("<void property=\"owStandardPrice\"><float>$ows</float></void>")
                    .append("<void property=\"owDiscount\"><float>$owd</float></void>")
                    .append("<void property=\"owRealPirce\"><float>$owr</float></void>")
                    .append("<void property=\"fixedPirce\"><float>$owe</float></void>")
                    .append("<void property=\"weightPirce\"><float>$owf</float></void>")
                    .append("<void property=\"floorPirce\"><float>$owg</float></void>")
                    .append("<void property=\"firstWeight\"><float>$owh</float></void>")
                    .append("<void property=\"addWeightChoice\"><float>$addWeightChoice</float></void>")
                    .append("</object>");

            postinfoTemp = x.toString();
            x = null;
        }
        return postinfoTemp;
    }

    @Override
    public boolean addPosttemp(T entity, String vipIds) {
        if (entity == null) {
            logger.error("entity is empity");
            return false;
        }

        if (StringUtils.isEmpty(vipIds)) {
            logger.error("vipId is empity");
            return false;
        }

        // postinfoXML 的内容(标签)格式要遵循 net.ytoec.kernel.util.XmlUtil#outputXml
        // 中定义的格式才能在以后的应用中解析成javaBean对象
        StringBuffer postinfoXML = new StringBuffer();
        postinfoXML
                .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<java version=\"1.6.0_16\" class=\"java.beans.XMLDecoder\">");

        String postinfo = entity.getPostinfo();
        try {
            if (StringUtils.isEmpty(postinfo)) {
                logger.error("postinfo is empity");
                return false;
            } // if

            /*
             * 将前台传过来的postinfo字符串解析、拼接成正确的xml字符存入DB 前台数据传过来的格式：首重#超重#地区;首重#超重#地区
             * ";"为多个运费信息数据的分隔符，"#"为一条运费信息数据中不同字段的分隔符 见页面：posttemp_add.jsp @
             * $("#f_save").click()
             * 
             * 省id#首重标准价#首重折扣#首重实收价#超重标准价#超重折扣#超重实收价
             * $did#$dtxt#$sid#$stxt#$fws#$fwd#$fwr#$ows#$owd#$owr
             */
            System.out.println(postinfo);
            String[] piArr = postinfo.split(";");
            for (String pi_sub : piArr) {
                String[] subArr = pi_sub.split("#");
                // 运费信息
                
                postinfoXML.append(getPostinfoTemp().replace("$did", subArr[0]) // 目的地省份id
                        .replace("$dtxt", subArr[1]) // 目的地省份显示值
                        .replace("$sid", subArr[2]) // 始发地省份id
                        .replace("$stxt", subArr[3]) // 始发地省份显示值
                        .replace("$fws", subArr[4]) // 首重-标准价
                        .replace("$fwd", trimToZero(subArr[5])) // 首重-折扣
                        .replace("$fwr", trimToZero(subArr[6])) // 首重-实收价
                        .replace("$ows", trimToZero(subArr[7])) // 超重-标准价
                        .replace("$owd", trimToZero(subArr[8])) // 超重-折扣
                        .replace("$owr", trimToZero(subArr[9])) // 超重-实收价
                        .replace("$owe", trimToZero(subArr[10])) // 固定价格
                        .replace("$owf", trimToZero(subArr[11])) // 重量单价
                        .replace("$owg", trimToZero(subArr[12])) // 最低收费价格
                        .replace("$owh", trimToZero(subArr[13])) // 首重
                        .replace("$addWeightChoice", trimToZero(subArr[14]).trim().replace(",", "").trim()) // 续重统计单位

                );
            } // for piArr

            postinfoXML.append("</java>");
        } catch (Exception e) {
            logger.error(LogInfoEnum.PARAM_INVALID.getValue(), e);
            logger.error("运费信息数据异常! 数据信息:[postinfo=" + postinfo + "]");
        }

        entity.setPostinfo(postinfoXML.toString());
        System.out.println(postinfoXML.toString());
        boolean flag = dao.addPosttemp(entity);

        // 新增失败
        if (!flag) {
            return false;
        }

        // 修改当前网点的field003为true
        User user = userDao.getUserById(entity.getCreateUser());
        user.setField003("true");
        userDao.edit(user);

        // 系统模板不需要往 用户关系表中插数据
        if (!"1".equals(entity.getPtType())) {
            // 绑定运费模板和用户的关系
            // 一个vip用户插入一条记录
            int postId = entity.getId();
            String[] vipIdsArr = vipIds.split(";");
            for (String vipId : vipIdsArr) {
                PosttempUser pu = new PosttempUser();
                pu.setPostId(postId); // 模板id
                pu.setVipId(Integer.parseInt(vipId)); // vipId
                /* 这里注意子账号存放的是主账号的id */
                Integer createUserId = entity.getCreateUser();
                User u = userDao.getUserById(entity.getCreateUser());
                if(!("2").equals(u.getUserType())){
                    User siteU = userDao.getUserById(u.getParentId());
                    createUserId = siteU.getId();
                }
                pu.setBranchId(createUserId); // 网点Id

                flag = puDao.addPosttempUser(pu);
                if (!flag)
                    return flag;
            }
        }

        return flag;
    } // addPosttemp

    @Override
    public T getPosttempById(Integer id) {
        if (id == null) {
            logger.error("id is empity");
        }
        return dao.getPosttempById(id);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map getPosttempByUserId(Integer userId, Pagination<T> pagination,
            String userType) {
        if (userId == null) {
            logger.error("userId is empity");
            return Collections.EMPTY_MAP;
        }

        List<PosttempUser> puList = null;
        List<T> ptList = new ArrayList<T>();
        List<T> ptListAll = new ArrayList<T>();
        int pageNum = pagination.getPageNum();
        int startIndex = pagination.getStartIndex() - 1;
        startIndex = startIndex > 0 ? startIndex : 0;

        // 官方标准模板
        T sysPt = (T) new Posttemp();
        if (pagination.getCurrentPage() == 1) {
            // 因为sysPt会手动添加到ptList里面，如果pageNum不减1，第一页会多一条记录
            if (!"4".equals(userType)) {
                pageNum -= 1;
            }

            sysPt.setPtName("系统默认运费模板");
            sysPt.setId(0);
            sysPt.setCreateUserText("管理员");
            sysPt.setPtType("1");
            sysPt.setRemark("系统自动分配");

            // ptList.add(sysPt);
        }
        Map resultMap = new Hashtable();
        boolean isPage = true; // true/false = 分页/不分页
        Map map = new HashMap();
        if (isPage) {
            isPage = false;

            map.put("startIndex", startIndex);
            map.put("pageNum", pageNum);
            map.put("userId", userId);
            // 卖家 || 大卖家用户(大卖家用户权限和vip差不多) || 卖家财务
            if ("1".equals(userType) || "4".equals(userType)
                    || "12".equals(userType) || "13".equals(userType)) {
                if ("4".equals(userType)) {
                    ptList = this.getPosttempByTerrace(userId);
//                  if(ptList!=null){
//                      ptList.add(sysPt);
//                  }
                } else {
                    puList = puDao.getPosttempUserByVipId(map);
                }
                if (null != puList && puList.size() > 0) {
                    for (PosttempUser pu : puList) {
                        T tempPosttemp = dao.getPosttempById(pu.getPostId());
                        if (tempPosttemp != null)
                            ptList.add(tempPosttemp);
                    }
                }

                // 添加系统模板
                if (ptList.size() < 1 && !"4".equals(userType)) {
                    ptList.add(sysPt);
                }
            }
            // 网点用户
            else if ("2".equals(userType) || "22".equals(userType)
                    || "23".equals(userType)) {
                ptList.add(sysPt);
               
//              List<T> l = dao.getPosttempByBranchId(map);
                map.put("branchId", userId);
                //add by yuyz 2012-6-4
                map.put("userType", userType);
                List<T> l = (List<T>) this.getPosttempShowUserThreadByBranch(map,true,userId);
                for (T tt : l) {
                    ptList.add(tt);
                }
            } else {
                // 根据创建人Id查询
                ptList = dao.getPosttempByBranchId(map);
                ptList.add(sysPt);
            }
        }

        for (T posttemp : ptList) {
            if (posttemp == null) {
                logger.error("posttemp is empity");
                return Collections.EMPTY_MAP;
            }

            // 这里不需要postinfo信息。置空postinfo，节省带宽。
            posttemp.setPostinfo(null);
            /*
             * @ 2011-11-16/ChenRen
             * 
             * @ 运费模板列表也不需要显示创建人
             * if(StringUtils.isEmpty(posttemp.getCreateUserText() ) ) { //
             * 设置创建人显示值 String nameText =
             * userDao.getUserById(posttemp.getCreateUser()) .getUserNameText();
             * posttemp.setCreateUserText(nameText); }
             */
        }

        resultMap.put("list", ptList);

        // 不分页
        if (!isPage) {
            isPage = true;

            map.remove("startIndex");
            map.remove("pageNum");
            map.put("userId", userId);
            // VIP用户 || 大卖家用户(大卖家用户权限和vip差不多)
            if ("1".equals(userType) || "4".equals(userType)
                    || "12".equals(userType) || "13".equals(userType)) {
                if ("4".equals(userType)) {
                    ptList = this.getPosttempByTerrace(userId);
                    for(T t :ptList){
                        ptListAll.add(t);
                    }
                }
                puList = puDao.getPosttempUserByVipId(map);
                for (PosttempUser pu : puList) {
                    T tempPosttemp = dao.getPosttempById(pu.getPostId());
                    if (tempPosttemp != null)
                        ptListAll.add(tempPosttemp);
                }
            }
            // 网点用户 || 其他用户
            else {
                ptListAll =(List<T>) this.getPosttempShowUserThreadByBranch(map,false,userId);
            }
            // 非管理员用户
            if (!"3".equals(userType) && !"4".equals(userType)) {
                // 添加系统模板
                ptListAll.add(sysPt);
                resultMap.put("totalRecords", ptList.size());
                return resultMap;
            }
        }

        resultMap.put("totalRecords", ptListAll.size());
        return resultMap;
    } // getPosttempByUserId

    // 这个方法是打开电子对账页面时，显示运费模板的时候用
    @SuppressWarnings("rawtypes")
    @Override
    public List<T> getPosttempByUserId(Integer userId, String userType) {
        if (userId == null) {
            logger.error("userId is empity");
            return Collections.EMPTY_LIST;
        }

        Map map = new HashMap();
        map.put("userId", userId);

        List<PosttempUser> puList = null;
        List<T> ptList = new ArrayList();

        // 官方标准模板
        T sysPt = (T) new Posttemp();
        sysPt.setPtName("系统默认运费模板");
        sysPt.setId(0);
        sysPt.setCreateUserText("管理员");

        // VIP用户 || 大卖家用户(大卖家用户权限和vip差不多)
        if ("1".equals(userType) || "4".equals(userType)
                || "12".equals(userType) || "11".equals(userType)
                || "13".equals(userType)) {
            puList = puDao.getPosttempUserByVipId(map);
            for (PosttempUser pu : puList) {
                T tempPosttemp = dao.getPosttempById(pu.getPostId());
                if (tempPosttemp != null)
                    ptList.add(tempPosttemp);
            }
            if (ptList.size() < 1) {
                ptList.add(sysPt);
            }
        }
        // 网点用户
        else if ("2".equals(userType) || "21".equals(userType)
                || "22".equals(userType) || "23".equals(userType)) {
            ptList.add(sysPt);
            List<T> l = dao.getPosttempByBranchId(map);
            for (T tt : l) {
                ptList.add(tt);
            }
        } else {
            // 根据创建人Id查询
            ptList = dao.getPosttempByBranchId(map);
            ptList.add(sysPt);
        }

        // 把poettemp里的postinfo清空。因为前台不需要，而postinfo会占用太多带宽
        // 清理不必要的数据，节省带宽
        for (T posttemp : ptList) {
            if (posttemp == null)
                continue;
            posttemp.setPostinfo(null);
            posttemp.setRemark(null);
            posttemp.setCreateTime(null);
            posttemp.setUpdateTime(null);
        }
        return ptList;
    } // getPosttempByUserId

    @Override
    public boolean editPosttemp(T entity) {
        if (entity == null) {
            logger.error("entity is empity");
            return false;
        }

        if (StringUtils.isEmpty(entity.getVipIds())) {
            logger.error("vipId is empity");
            return false;
        }

        // postinfoXML 的内容(标签)格式要遵循 net.ytoec.kernel.util.XmlUtil#outputXml
        // 中定义的格式才能在以后的应用中解析成javaBean对象
        StringBuffer postinfoXML = new StringBuffer();
        postinfoXML
                .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<java version=\"1.6.0_16\" class=\"java.beans.XMLDecoder\">");

        String postinfo = entity.getPostinfo();
        try {
            if (StringUtils.isEmpty(postinfo)) {
                logger.error("postinfo is empity");
            } // if

            /*
             * 将前台传过来的postinfo字符串解析、拼接成正确的xml字符存入DB 前台数据传过来的格式：首重#超重#地区;首重#超重#地区
             * ";"为多个运费信息数据的分隔符，"#"为一条运费信息数据中不同字段的分隔符 见页面：posttemp_add.jsp @
             * $("#f_save").click()
             */
            String[] piArr = postinfo.split(";");
            for (String pi_sub : piArr) {
                String[] subArr = pi_sub.split("#");
                // 运费信息
                
                postinfoXML.append(getPostinfoTemp().replace("$did", subArr[0]) // 目的地省份id
                        .replace("$dtxt", subArr[1]) // 目的地省份显示值
                        .replace("$sid", subArr[2]) // 始发地省份id
                        .replace("$stxt", subArr[3]) // 始发地省份显示值
                        .replace("$fws", trimToZero(subArr[4])) // 首重-标准价
                        .replace("$fwd", trimToZero(subArr[5])) // 首重-折扣
                        .replace("$fwr", trimToZero(subArr[6])) // 首重-实收价
                        .replace("$ows", trimToZero(subArr[7])) // 超重-标准价
                        .replace("$owd", trimToZero(subArr[8])) // 超重-折扣
                        .replace("$owr", trimToZero(subArr[9])) // 超重-实收价
                        .replace("$owe", trimToZero(subArr[10])) // 固定价格
                        .replace("$owf", trimToZero(subArr[11])) // 重量单价
                        .replace("$owg", trimToZero(subArr[12])) // 最低收费价格
                        .replace("$owh", trimToZero(subArr[13])) // 首重
                        .replace("$addWeightChoice", trimToZero(subArr[14]).trim().replace(",", "").trim()) // 续重统计单位
                        );
            } // for piArr

            postinfoXML.append("</java>");
        } catch (Exception e) {
            logger.error(LogInfoEnum.PARAM_INVALID.getValue(), e);
            logger.error("运费信息数据异常! 数据信息:[postinfo=" + postinfo + "]");
        }

        entity.setPostinfo(postinfoXML.toString());
        boolean flag = dao.editPosttemp(entity);

        // 修改失败
        if (!flag) {
            return false;
        }

        // 系统模板不需要往 用户关系表中插数据
        if (!"1".equals(entity.getPtType())) {
            // 绑定运费模板和用户的关系
            // 1. 先根据模板Id清空原有数据
            flag = puDao.delPosttempUserByPostId(entity.getId());
            if (!flag) {
                return flag;
            }
            
            // 2. 一个vip用户插入一条记录
            int postId = entity.getId();
            String[] vipIdsArr = entity.getVipIds().split(";");
            for (String vipId : vipIdsArr) {
                PosttempUser pu = new PosttempUser();
                pu.setPostId(postId); // 模板id
                pu.setVipId(Integer.parseInt(vipId)); // vipId
                pu.setBranchId(entity.getCreateUser()); // 网点Id

                flag = puDao.addPosttempUser(pu);
                if (!flag) {
                    return flag;
                }
            }
            
        }

        return flag;
    } // 编辑-保存
    
    @Override
    public boolean editPosttemp(User currentUser, T entity) {
        if (entity == null) {
            logger.error("entity is empity");
            return false;
        }

        if (StringUtils.isEmpty(entity.getVipIds())) {
            logger.error("vipId is empity");
            return false;
        }

        // postinfoXML 的内容(标签)格式要遵循 net.ytoec.kernel.util.XmlUtil#outputXml
        // 中定义的格式才能在以后的应用中解析成javaBean对象
        StringBuffer postinfoXML = new StringBuffer();
        postinfoXML
                .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<java version=\"1.6.0_16\" class=\"java.beans.XMLDecoder\">");

        String postinfo = entity.getPostinfo();
        try {
            if (StringUtils.isEmpty(postinfo)) {
                logger.error("postinfo is empity");
            } // if

            /*
             * 将前台传过来的postinfo字符串解析、拼接成正确的xml字符存入DB 前台数据传过来的格式：首重#超重#地区;首重#超重#地区
             * ";"为多个运费信息数据的分隔符，"#"为一条运费信息数据中不同字段的分隔符 见页面：posttemp_add.jsp @
             * $("#f_save").click()
             */
            String[] piArr = postinfo.split(";");
            for (String pi_sub : piArr) {
                String[] subArr = pi_sub.split("#");
                // 运费信息
                postinfoXML.append(getPostinfoTemp().replace("$did", subArr[0]) // 目的地省份id
                        .replace("$dtxt", subArr[1]) // 目的地省份显示值
                        .replace("$sid", subArr[2]) // 始发地省份id
                        .replace("$stxt", subArr[3]) // 始发地省份显示值
                        .replace("$fws", trimToZero(subArr[4])) // 首重-标准价
                        .replace("$fwd", trimToZero(subArr[5])) // 首重-折扣
                        .replace("$fwr", trimToZero(subArr[6])) // 首重-实收价
                        .replace("$ows", trimToZero(subArr[7])) // 超重-标准价
                        .replace("$owd", trimToZero(subArr[8])) // 超重-折扣
                        .replace("$owr", trimToZero(subArr[9])) // 超重-实收价
                        .replace("$owe", trimToZero(subArr[10])) // 固定价格
                        .replace("$owf", trimToZero(subArr[11])) // 重量单价
                        .replace("$owg", trimToZero(subArr[12])) // 最低收费价格
                        .replace("$owh", trimToZero(subArr[13])) // 首重
                        .replace("$addWeightChoice", trimToZero(subArr[14])) // 续重统计单位
                        );
            } // for piArr

            postinfoXML.append("</java>");
        } catch (Exception e) {
            logger.error(LogInfoEnum.PARAM_INVALID.getValue(), e);
            logger.error("运费信息数据异常! 数据信息:[postinfo=" + postinfo + "]");
        }

        entity.setPostinfo(postinfoXML.toString());
        boolean flag = dao.editPosttemp(entity);

        // 修改失败
        if (!flag) {
            return false;
        }

        // 系统模板不需要往 用户关系表中插数据
        if (!"1".equals(entity.getPtType())) {
            
            int postId = entity.getId();
            
            //该运费模板的所有直客列表
            List<PosttempUser> postUserList = posttempUserDao.getPosttempUserByPostId(postId);
            
            //找到当前页面上操作的账号
            String[] vipIdsArr = entity.getVipIds().split(";");
            List<UserThreadContract> utcList = userThreadContractDao.searchContractByConractAreaId(Integer.parseInt(vipIdsArr[0]),null);
            if(utcList != null && utcList.size() != 0 ){
            	String addUserName = utcList.get(0).getAddUserName();
            	UserThreadContract uTemp = new UserThreadContract();
            	uTemp.setAddUserName(addUserName);
            	uTemp.setAccountType(null);
            	//该账号登陆进来，所拥有的直客列表
            	utcList = userThreadContractDao.getContractersByUserNameAndType(uTemp);
            }
            
            //去除当前页面上操作的账号下的所有直客
            for(int i=0; i<postUserList.size(); i++){
            	for(int j=0; j<utcList.size(); j++){
            		if(postUserList.get(i).getVipId().equals(utcList.get(j).getConractAreaId())){
            			postUserList.remove(i);
            			i--;
            			utcList.remove(j);
            			j--;
            			break;
                	}
            	}
            }
            
            // 绑定运费模板和用户的关系
            // 1. 先根据模板Id清空原有数据
            flag = puDao.delPosttempUserByPostId(entity.getId());
            if (!flag) {
                return flag;
            }
            
            // 2. 一个vip用户插入一条记录
            //添加当前账号的直客
            for (String vipId : vipIdsArr) {
                PosttempUser pu = new PosttempUser();
                pu.setPostId(postId); // 模板id
                pu.setVipId(Integer.parseInt(vipId)); // vipId
                pu.setBranchId(entity.getCreateUser()); // 网点Id

                flag = puDao.addPosttempUser(pu);
                if (!flag) {
                    return flag;
                }
            }
            
            if(!"2".equals(currentUser.getUserType())){
            	for(PosttempUser pu: postUserList){
            		puDao.addPosttempUser(pu);
            	}
            }
            
        }

        return flag;
    } // 编辑-保存

    @Override
    public boolean delPosttemp(T entity) {
        if (entity == null) {
            logger.error("entity is empity");
            return false;
        }

        puDao.delPosttempUserByPostId(entity.getId());
        return dao.delPosttemp(entity);
    }

    @Override
    public T toPosttempEdit(T entity, Integer id, String uType) {

        if (entity == null) {
            logger.error("entity is empity");
            return (T) new Posttemp();
        }

        T posttemp = (T) new Posttemp();
        // 系统模板
        if ("1".equals(entity.getPtType()) || entity.getId() == 0) {
            if (id == null) {
                logger.error("id is empity");
                return posttemp;
            }
            // 卖家 || 卖家财务
            if ("1".equals(uType) || "12".equals(uType) || "13".equals(uType)) {
                posttemp = getSysPosttempByVipId(id);
            }
            if ("4".equals(uType)) {
                posttemp = getSysPosttempByVipId(Integer.parseInt(entity
                        .getVipIds()));
            }
            // 网点 || 网点卖家
            else if ("2".equals(uType) || "22".equals(uType)
                    || "23".equals(uType)) {
                posttemp = getSysPosttempBySiteId(id);
            } else {
                logger.error("参数异常! 未知用户类型! 参数信息[usertype:" + uType + "]");
                return posttemp;
            }
        } else {
            posttemp = getPosttempById(entity.getId());
            if (posttemp == null) {
                logger.error("posttemp is empity");
                return posttemp;
            }

            // 将模板的postinfo的xml字符串解析成postinfo对象，存放在PostinfoList里
            List<Postinfo> piList = null;
            try {
                piList = XmlUtil.xmlDecoder2List(posttemp.getPostinfo());
            } catch (Exception e) {
                logger.error(LogInfoEnum.PARAM_INVALID.getValue(), e);
                logger.error(e.getMessage());
            }
            // Collections.sort(piList); // 将地区为other的对象排序到最底部
            posttemp.setPostinfoList(piList);

            StringBuilder vipText = new StringBuilder("");
            StringBuilder vipIds = new StringBuilder("");
            // 系统模板
            if ("1".equals(posttemp.getPtType())) {
                vipIds = vipIds.append(";all");
                vipText = vipText.append(";所有用户(all)");
            } else {
                // 设置模板对应的vip用户信息
                List<UserThread> uList = userThreadDao
                        .getUserThreadByPostId(posttemp.getId());
                // List<User> uList =
                // userDao.getVipUserByPostId(posttemp.getId());
                for (UserThread user : uList) {
                	//多账号的直客共同拥有一个模板的时候，财务，财务兼客服子账号进来，只能看到自己账号分配的直客，不能看到其他账号的直客
                	if("22".equals(uType) || "23".equals(uType)){
                		User u = new User();
                		u.setId(id);
                		String userName = userDao.get(u).getUserName();
                		
                		//查看该账号进来拥有的自己的直客
                		UserThreadContract utc = new UserThreadContract();
                		utc.setAddUserName(userName);
                		utc.setAccountType(uType);
                		List<UserThreadContract> utcList = userThreadContractDao.getContractersByUserNameAndType(utc);
                		
                		//直客主键ID的集合
                		List<Integer> userThreadIds = new ArrayList<Integer>();
                		if(utcList != null && utcList.size() != 0){
                			for(UserThreadContract c: utcList){
                				userThreadIds.add(c.getConractAreaId());
                			}
                		}
                		//如果某直客ID不在直客集合中，说明该直客不属于该账号
                		if(userThreadIds != null && userThreadIds.size() != 0 && !userThreadIds.contains(user.getId())){
                			continue;
                		}
                	}
                    vipIds = vipIds.append(";").append(user.getId());

                    vipText = vipText.append(";").append(user.getUserName())
                            .append("(").append(user.getUserName()).append(")");
                }
            }
            posttemp.setVipIds(vipIds.length() > 0 ? vipIds.substring(1) : "");
            posttemp.setVipText(vipText.length() > 0 ? vipText.substring(1)
                    : "");

        }
        return posttemp;
    } // toPosttempEdit

    @Override
    public T getSysPosttempByVipId(Integer vipId) {
        if (vipId == null) {
            logger.error("vipId is empity");
            return null; // 返回json，null不会报异常
        }

        List<User> list = userDao.getSiteByVipId(vipId);
        // UserThread ut = userThreadDao.getUserById(vipId);
        // if(ut==null){
        // logger.error("数据异常! 没有查到该用户匹配的网点对象! 参数信息[userThreadId:" + vipId +
        // "]");
        // return null;
        // }
        if (list == null || list.size() == 0) {
            logger.error("数据异常! 没有查到该用户匹配的网点对象! 参数信息[userId:" + vipId + "]");
            return null;
        }
        if (list.size() > 1) {
            logger.error("数据异常! 当前用户存在多个匹配的网点对象! 参数信息[userId:" + vipId
                    + "; lise.size():" + list.size() + "]");
            return null;
        }

        // String provId = ut.getSiteCode();
        String provId = list.get(0).getField001();

        // // @2011-12-14/ChenRen
        // // 如果vip对应的网点的省份Id（field001）值为空，就返回vip本身的省份Id
        if (StringUtils.isEmpty(provId) || ("(NULL)".equals(provId))) {

            logger.error("当前用户对应的网点没有省份Id字段(field001)值，可能会影响用户的电子对账操作。"
                    + "参数信息[当前用户Id:" + vipId + "]");

            User self = userDao.getUserById(vipId);
            if (self == null) {
                logger.error("当前用户在数据库中不存在! 参数信息[userId:" + vipId + "]");
                return null;
            }
            provId = self.getField001(); // 省份id
            if (StringUtils.isEmpty(provId)) {
                logger.error("当前用户没有省份Id值。将无法查看运费模板信息! " + " 参数信息[userId:"
                        + vipId + "]");
                return null;
            }
        }

        return getSysPosttemp(provId);
    }

    @Override
    public T getSysPosttempBySiteId(Integer siteId) {
        if (siteId == null) {
            logger.error("siteId is empity");
            return (T) new Posttemp();
        }
        User u = userDao.getUserById(siteId);
        if (u == null) {
            logger.error("u is empity");
            return (T) new Posttemp();
        }
        String provId = u.getField001();

        return getSysPosttemp(provId);
    }

    @SuppressWarnings("rawtypes")
    private T getSysPosttemp(String provId) {

        provId = provId == null ? "" : provId;

        Map map = new HashMap();
        map.put("sourceId", provId);
        List<StandardPosttemp> spList = spDao
                .getStandardPosttempListBySourceId(map);

        T sysPt = (T) new Posttemp();
        sysPt.setId(0);
        sysPt.setPtName("系统默认运费模板");
        sysPt.setCreateUserText("管理员");
        sysPt.setVipIds("all");
        sysPt.setVipText("所有用户(all)");
        sysPt.setModule(0.01);
        //add 增加首重重量：系统模板设置为1
        sysPt.setFirstWeight(1);
        sysPt.setRemark("如果网点没有给用户分配运费模板，系统就会自动给用户分配系统默认运费模板。");

        List<Postinfo> piList = new ArrayList<Postinfo>();
        for (StandardPosttemp sp : spList) {
            Postinfo pi = new Postinfo();
            pi.setDestId(sp.getDestId() + "");
            pi.setDestText(Resource.getNameById(sp.getDestId()));
            pi.setSrcId(sp.getSourceId() + "");
            pi.setSrcText(Resource.getNameById(sp.getSourceId()));

            pi.setFwStandardPrice(Float.parseFloat(sp.getStandardPrice()));
            pi.setFwDiscount(0);
            pi.setFwRealPirce(Float.parseFloat(sp.getStandardPrice()));

            pi.setOwStandardPrice(Float.parseFloat(sp.getContinuationPrice()));
            pi.setOwDiscount(0);
            pi.setOwRealPirce(Float.parseFloat(sp.getContinuationPrice()));
            pi.setFloorPirce(Float.parseFloat(sp.getStandardPrice()));
            pi.setFirstWeight((Float.parseFloat(sp.getFirstWeight())));
            if (sp.getFixedPirce() != null)
                pi.setFixedPirce(Float.parseFloat(sp.getFixedPirce()));
            if (sp.getWeightPirce() != null)
                pi.setWeightPirce(Float.parseFloat(sp.getWeightPirce()));
            //系统模板中“续重统计单位”固定为0.01
            pi.setAddWeightChoice(Float.parseFloat("0.01"));
            
            piList.add(pi);
        }
        sysPt.setPtGround(Resource.getNameById(Integer.parseInt(provId)));
        sysPt.setPostinfoList(piList);

        return sysPt;
    }

    @Override
    public T viewPTByVip(String vipId) {
        if (StringUtils.isEmpty(vipId)) {
            logger.error("客户Id为空! ");
            return null;
        }

        // 根据用户的Id去查询用户的运费模板
        // 如果有，就返回查到的，否则返回官方模板
        Map map = new HashMap();
        map.put("userId", vipId);

        // 根据用户Id取 运费模板-用户 关系数据
        List<PosttempUser> puList = puDao.getPosttempUserByVipId(map);
        List<T> ptList = new ArrayList<T>();
        for (PosttempUser pu : puList) {
            // 根据 模板Id 取模板
            T tempPosttemp = dao.getPosttempById(pu.getPostId());
            if (tempPosttemp != null)
                ptList.add(tempPosttemp);
        }

        if (ptList.size() != 0) {
            //
            return toPosttempEdit(ptList.get(0), Integer.parseInt(vipId), "2");
        }

        return null;
        /*
         * else { // 查找官方模板 T sysPT =
         * getSysPosttempByVipId(Integer.parseInt(vipId) );
         * 
         * // 设置页面显示的用户为页面传过来的用户 User user =
         * userDao.getUserById(Integer.parseInt(vipId) );
         * sysPT.setVipIds(vipId);
         * sysPT.setVipText(user.getUserNameText()+"("+vipId+")");
         * 
         * return (sysPT == null ? (T) new Posttemp() : sysPT); }
         */
    } // viewPTByVip

    @Override
    public T getPosttempByVipId(String vipId) {
        if (StringUtils.isEmpty(vipId)) {
            logger.error("用户id为空");
            return null;
        }

        // 根据用户的Id去查询用户的运费模板
        Map map = new HashMap();
        map.put("userId", vipId);

        List<PosttempUser> puList = puDao.getPosttempUserByVipId(map);
        List<T> ptList = new ArrayList<T>();
        for (PosttempUser pu : puList) {
            T tempPosttemp = dao.getPosttempById(pu.getPostId());
            if (tempPosttemp != null)
                ptList.add(tempPosttemp);
        }

        if (ptList.size() != 0) {
            return toPosttempEdit(ptList.get(0), Integer.parseInt(vipId), "2");
        }

        return null;
    }

    @Override
    public T toPosttempView2(String userCode) {

        if (StringUtils.isEmpty(userCode)) {
            logger.info("用户编码为空");
            //运费模版，用户没有绑定网点，也没有完善信息点击运费模版，显示模版为始发地为北京的运费模版
            return getSysPosttemp("110000");
        }

        UserThread userThread = new UserThread();
        userThread.setUserCode(userCode);
        List<UserThread> userThreadsList = userThreadDao
                .searchUsersByCode(userThread);

        if (userThreadsList == null) {
            logger.info("用户编码对应的直客信息不存在. 参数信息[usercode:" + userCode + "]");
            return null;
        }
        if (userThreadsList.size() == 0) {
            logger.info("用户编码对应的直客信息不存在. 参数信息[usercode:" + userCode + "]");
            return null;
        }
        if (userThreadsList.size() != 0) {
            userThread = userThreadsList.get(0);
            Map map = new HashMap();
            map.put("userId", userThread.getId());

            // 根据用户Id取 运费模板-用户 关系数据
            List<PosttempUser> puList = puDao.getPosttempUserByVipId(map);
            List<T> ptList = new ArrayList<T>();
            for (PosttempUser pu : puList) {
                // 根据 模板Id 取模板
                T tempPosttemp = dao.getPosttempById(pu.getPostId());
                if (tempPosttemp != null)
                    ptList.add(tempPosttemp);
            }

            // 可以查到模板信息
            if (ptList.size() != 0) {
                T posttemp = this.getPosttempById(ptList.get(0).getId());
                if (posttemp == null) {
                    logger.error("模板Id对应的模板不存在. 参数信息[模板Id:"
                            + ptList.get(0).getId() + "]");
                    return null;
                }

                // 将模板的postinfo的xml字符串解析成postinfo对象，存放在PostinfoList里
                List<Postinfo> piList = null;
                try {
                    piList = XmlUtil.xmlDecoder2List(posttemp.getPostinfo());
                } catch (Exception e) {
                    logger.error(LogInfoEnum.PARAM_INVALID.getValue(), e);
                }
                posttemp.setPostinfoList(piList);

                return posttemp;
            }
            // 模板信息不存在. 使用官方标准模板
            else {
                User user = new User();
                user.setUserCode(userCode);
                user.setUserType("1");
                user.setUserState("1");
                List<User> userList = userDao.searchUsersByCodeTypeState(user);
                if (userList != null) {
                    if (userList.size() > 0) {
                        return getSysPosttempByVipId(userList.get(0).getId());
                    }
                }
            }
        } // 直客信息存在

        return null;
    }

    @Override
    public Postinfo queryPostinfoByProv(User cuser, int destId) {
        T entity = toPosttempView2(cuser.getUserCode());
        if (entity != null) {
            List<Postinfo> list = entity.getPostinfoList();
            for (Postinfo postinfo : list) {
                if ((destId + "").equals(postinfo.getDestId())) {
                    return postinfo;
                }
            }
        }
        return null;
    }

    /**
     * 
     * 获取平台用户的运费模板列表 [主要功能] A：取得平台用户的分仓账户(已激活并绑定运费模板，已激活未绑定，未激活分仓账户)
     * B：平台用户下的分仓账户没有运费模板绑定时,需要返回分仓账户的默认系统模板
     * 
     * 
     * 2012/04/28 修改:只显示已激活并绑定运费模板的分仓帐户
     * @param userId
     *            用户编号
     * @return 模板集合
     */
    @SuppressWarnings("rawtypes")
    protected List getPosttempByTerrace(Integer userId) {
        List<T> ptList = new ArrayList();// 模板

        List<User> tbauserList = new ArrayList(); // 已激活但不存在用户模板关系的分仓账户
        List<User> tbapuList = new ArrayList(); // 已激活但不存在用户模板关系的分仓账户
        List<User> nottbapuList = new ArrayList(); // 未激活的分仓账号

        // 根据平台用户的的userId来获取所有的分仓账号
        Map queryUserMap = new HashMap();
        queryUserMap.put("userSource", userId);
        queryUserMap.put("type", 1);
        List<User> listUser = userDao.searchDepotHosting(queryUserMap);

        // 获取其平台用户的所有userCode
        Iterator<User> iterator = listUser.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            String code = user.getUserCode();
            UserThread userThread = new UserThread();
            userThread.setUserCode(code);
            // 直客表中查不到此usercode时为未激活分仓账户，存放在nottbapuList中
            if (null == userThreadDao.searchUsersByCode(userThread)
                    || 0 == userThreadDao.searchUsersByCode(userThread).size()) {
                nottbapuList.add(user);
            } else {
                // 根据已经存在的直客ID在用户模板关系表中查不到时为激活未分配运费模板账户，存放于tbapuList
                UserThread userThread2 = userThreadDao.searchUsersByCode(
                        userThread).get(0);
                Integer uId = userThread2.getId();
                Map queryPURelation = new HashMap();
                queryPURelation.put("startIndex", 0);
                queryPURelation.put("pageNum", Integer.MAX_VALUE);
                queryPURelation.put("userId", uId);
                if (null == puDao.getPosttempUserByVipId(queryPURelation)
                        || 0 == puDao.getPosttempUserByVipId(queryPURelation)
                                .size()) {
                    tbapuList.add(user);
                } else {
                    // 已激活且绑定运费模板的user
                    tbauserList.add(user);
                }
            }

        }
        // 组建平台用户的模板数据
        // 1.已存在用户模板关系的运费模板组建
        for (User user : tbauserList) {
            UserThread userThread = new UserThread();
            userThread.setUserCode(user.getUserCode());

            userThread = userThreadDao.searchUsersByCode(userThread).get(0);
            Map queryPURelation = new HashMap();
            queryPURelation.put("startIndex", 0);
            queryPURelation.put("pageNum", Integer.MAX_VALUE);
            queryPURelation.put("userId", userThread.getId());
            PosttempUser posttempUser = puDao.getPosttempUserByVipId(
                    queryPURelation).get(0);
            Posttemp tempPosttemp22 = new Posttemp();
            Posttemp tempPosttemp = dao.getPosttempById(posttempUser
                    .getPostId());
            BeanUtils.copyProperties(tempPosttemp, tempPosttemp22);
            tempPosttemp22
                    .setVipText("分仓:" + user.getUserName() == null ? "未命名分仓账户"
                            : user.getUserName());
            //找到分仓对应的网点的所属省份
            List<User> list = userDao.getSiteByVipId(user.getId());
            String provId = list.get(0).getField001();
            if(!provId.isEmpty()){
                String ptGroud=Resource.getNameById(Integer.parseInt(provId));
                tempPosttemp22.setPtGround(ptGroud);
            }
            if(tempPosttemp22 != null){
                ptList.add((T)tempPosttemp22);
            }
        }
        
        // 2012/04/28 修改:只显示已激活并绑定运费模板的分仓帐户
//      // 2.已激活未绑定网点的分仓账户的运费模板组建
//      for (User user : tbapuList) {
//          Integer id = user.getId();
//          T tempPosttemp = this.getSysPosttempByVipId(id);
//          tempPosttemp.setRemark("未绑定运费模板");
//          tempPosttemp
//                  .setVipText("分仓:" + user.getUserNameText() == null ? "未命名分仓账户"
//                          : user.getUserNameText());
//          tempPosttemp.setVipIds(user.getId() + "");
//          ptList.add(tempPosttemp);
//      }
//      // 3.未激活未绑定的分仓账户的运费模板组建
//      for (User user : nottbapuList) {
//          Posttemp tempPosttemp = new Posttemp();
//          tempPosttemp.setPtName("账号未激活");
//          tempPosttemp
//                  .setVipText("分仓:" + user.getUserNameText() == null ? "未命名分仓账户"
//                          : user.getUserNameText());
//          ptList.add((T) tempPosttemp);
//      }
        return ptList;
    }
    
    /**
     * 根据网点查找运费模板将直客名称拼装到前台并显示
     * flag : true分页，false不分页
     */
    @SuppressWarnings("rawtypes")
    private List<Posttemp> getPosttempShowUserThreadByBranch(Map map,Boolean flag,Integer userId){
        List<Posttemp> listPosttemp=new ArrayList();
        List <PosttempUser> puList= new ArrayList();
        //1.根据网点找关系表,确定运费模板的数量
        /**add by yuyz 2012-6-4 start */
        //根据登陆用户的id查找它的名称
        User entity = new User();
        entity.setId(userId);
        String userName = userDao.get(entity).getUserName();
        
        //获取userThreadContractArea表中保存的属于登陆用户的直客信息列表
        List<UserThreadContract> contractList = new ArrayList<UserThreadContract>();
		UserThreadContract utc = new UserThreadContract();
		utc.setAddUserName(userName);
		utc.setAccountType((String)map.get("userType"));
		contractList = userThreadContractDao.getContractersByUserNameAndType(utc);
		
		boolean isSite = true;
		List<Integer> vipIds = new ArrayList<Integer>();
		if(contractList!=null && contractList.size() != 0){
			int parentId = 0;
			for(UserThreadContract utcon :contractList){
				vipIds.add(utcon.getConractAreaId());
				parentId = utcon.getSiteId();
			}
			map.put("vipIds", vipIds); //得到所有直客Id
			map.put("branchId", parentId);//得到所属网点Id
			isSite = false;
		}
		/**add by yuyz 2012-6-4 end */
		
        if(flag){
        	if(!isSite){
        		//add by yuyz 2012-6-4
        		puList = puDao.getPosttempUserByContractVipIdMap(map);
        	} else {
        		puList = puDao.getPosttempUserByBranchIdMap(map);
        	}
            
        }else{
        	if(!isSite){
        		//add by yuyz 2012-6-4
        		puList = puDao.getPosttempUserByContractVipIdMap(map);
        	} else {
        		puList = puDao.getPosttempUserByBranchId(userId);
        	}
            
        }
        Map<Integer, Posttemp> postMap = new HashMap<Integer, Posttemp>();
        Posttemp posttemp = null;
        if(puList != null && puList.size() > 0){
        	 for(PosttempUser pu: puList){
                 //2.获取运费模板
                 if(postMap.containsKey(pu.getPostId())){
                     posttemp = postMap.get(pu.getPostId());
                 }
                 else{
                     posttemp = dao.getPosttempById(pu.getPostId());
                 }
                 String vipText = "" ;
                 if(posttemp!=null){
                	 vipText = StringUtils.defaultIfBlank(posttemp.getVipText(), "");
                	 
                	//2.根据关系表找到对应的直客信息的直客名称
                     UserThread userthread=userThreadDao.getUserById(pu.getVipId());
                     if(null!=posttemp && null!=userthread){
                         //3.将需要的直客名称放入运费模板的VipText字段中
                    	 /**update by yuyz 2012-08-06 start*/
                         /*if(vipText.contains(userthread.getUserName())){
                             continue;
                         }*/
                    	 if(userthread.getUserName().equals(vipText) || vipText.contains(userthread.getUserName()+" ") || vipText.contains(" "+userthread.getUserName())){
                             continue;
                         }
                         /**delete by yuyz 2012-08-06 end*/
                         if(StringUtils.isNotBlank(vipText)){
                             vipText += " ";
                         }
                         vipText += userthread.getUserName();
                     }
                     posttemp.setVipText(vipText);
                     postMap.put(pu.getPostId(), posttemp);
                 }
                 
                 
             }
        	 listPosttemp.addAll(postMap.values());
        }
       
        
        return listPosttemp;
    }

    /**
     * 把空字符串替换成 0
     * 
     * @return
     */
    public String trimToZero(String str) {
        if (str == null || str.trim().equals(""))
            return "0";
        else
            return str.trim();
    }

    @Override
    public void clearPostForRepeatUser(Integer compareId, Integer siteId) {
        
        updateRetentionIdPosttempUser(compareId, siteId);
        updateRepeatSite(compareId, siteId);
        updateRetentionIdPosttemp(compareId,siteId);
    }
    
    
    /**
     * 更新模板用户关联表数据
     * @param siteId 需更改的网点id
     * @param retentionId 更改后的网点id
     */
    private void updateRetentionIdPosttempUser(Integer siteId, Integer retentionId){
        List<PosttempUser> posttempUserList = posttempUserDao.getPosttempUserByBranchId(siteId);
        if(posttempUserList!=null && !posttempUserList.isEmpty()){
            for(PosttempUser pu : posttempUserList){
                if(pu.getBranchId()!=retentionId){
                    pu.setBranchId(retentionId);
                    boolean b = posttempUserDao.editPosttempUser(pu);
                    if(!b){
                        logger.error("更新模板用户关联表数据失败，失败的用户id是："+siteId);
                    }
                }
            }
        }
    }
    /**
     * 更新模板的创建者
     * @param siteId 需更改的网点id
     * @param retentionId 更改后的网点id
     */
    private void updateRetentionIdPosttemp(Integer siteId, Integer retentionId){
        
        Map<String, Integer> map=new HashMap<String, Integer>();
        map.put("userId", siteId);
        List<Posttemp> posttempList = posttemDao.getPosttempByBranchId(map);
        if(posttempList!=null && !posttempList.isEmpty()){
            for(Posttemp pu : posttempList){
                if(pu.getCreateUser()!=retentionId){
                    pu.setCreateUser(retentionId);
                    boolean b = posttemDao.editPosttemp(pu);
                    if(!b){
                        logger.error("更新模板用户创建者失败，失败的用户id是："+siteId);
                    }
                }
            }
        }
    }
    /**
     * 更新非retentionId的网点用户:在非保留的网点用户名后面追加字符串"correct"
     * @param retentionId
     * @param siteIdArr
     * @return
     */
    private void updateRepeatSite(Integer siteId, Integer retentionId){
        if(retentionId!=siteId){
            User user = userDao.getUserById(siteId);
            if(user.getUserName()!=null && !user.getUserName().equals(""))
                user.setUserName(user.getUserName()+"correct");
            else
                user.setUserName(user.getSite()+"correct");
            if(user.getRemark()!=null && !user.getRemark().equals(""))
                user.setRemark(user.getRemark()+"correct");
            else
                user.setRemark("correct");
            
            user.setUserType("9");
            boolean b = userDao.edit(user);
            if(!b){
                logger.error("更新非retentionId重复数据失败，失败的用户id是："+siteId);
            }
        }
    }

	@Override
	public List<PosttempUser> getPosttempUserByBranchIdAndVipId(Integer branchId,
			Integer vipId) {
		Map map = new HashMap();
		map.put("branchId", branchId);
		map.put("vipId", vipId);
		return posttempUserDao.getPosttempUserByBranchIdAndVipId(map);
	}
}
