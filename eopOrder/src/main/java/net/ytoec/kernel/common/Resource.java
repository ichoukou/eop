package net.ytoec.kernel.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.ytoec.kernel.constains.CachePrefixConstant;
import net.ytoec.kernel.dataobject.Channel;
import net.ytoec.kernel.dataobject.ConfigCode;
import net.ytoec.kernel.dataobject.Postinfo;
import net.ytoec.kernel.dataobject.Posttemp;
import net.ytoec.kernel.dataobject.Region;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserCustom;
import net.ytoec.kernel.dataobject.UserRelation;
import net.ytoec.kernel.dto.DtoBranch;
import net.ytoec.kernel.service.ChannelService;
import net.ytoec.kernel.service.ConfigCodeService;
import net.ytoec.kernel.service.MemcacheService;
import net.ytoec.kernel.service.RegionService;
import net.ytoec.kernel.service.UserCustomService;
import net.ytoec.kernel.service.UserRelationService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.util.XmlUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Resource {

    private static Map<Integer, String> provinces = new HashMap<Integer, String>();
    // 以省份名为key的map
    private static Map<String, Integer> provincesCH = new HashMap<String, Integer>();

    // /** 电子对账的特殊地区 */
    public static Map<String, String> orderProvinces = new HashMap<String, String>();
    /** 问题件#下次启动的查询起始时间参数 */
    public static final String QSN_NEXTSTARTTIME = "nextStarttime";

    private static Logger logger = LoggerFactory.getLogger(Resource.class);

    private static ChannelService<Channel> channelService;
    private static ConfigCodeService<ConfigCode> configCodeService;
    private static UserService<User> userService;
    private static UserCustomService<UserCustom> userCustomService;
    private static UserRelationService<UserRelation> userRelationService;

    private static RegionService<Region> regionService;

    private static MemcacheService<String> memcacheService;

    private static MemcacheService<DtoBranch> dtoMemcacheService;

    private static MemcacheService<Channel> channelMemcacheService;

    private static MemcacheService<User> userMemcacheService;

    static Map<Integer, String> otherProvinces;
    static Map<String, Integer> otherProvincesCH;

    // 省市县数据初始化开关
    private static boolean regionInit = true;

    /** 管理员的权限信息 */
    public static Map<String, String> permission_adminMap = new HashMap<String, String>();
    public static Map<String, String> permission_wangdianMap = new HashMap<String, String>();
    public static Map<String, String> permission_wangdian_kefuMap = new HashMap<String, String>();
    public static Map<String, String> permission_wangdian_caiwuMap = new HashMap<String, String>();
    public static Map<String, String> permission_maijiaMap = new HashMap<String, String>();
    public static Map<String, String> permission_maijia_kefuMap = new HashMap<String, String>();
    public static Map<String, String> permission_maijia_caiwuMap = new HashMap<String, String>();
    public static Map<String, String> permission_pingtaiMap = new HashMap<String, String>();
    private static boolean initPermission = true;
    /**
     * 个性化配置缓存:关联关系数据存放在单独的一个表中，原始UserCusto对象中relationalQuery表示用户关联店铺了的逻辑。<br/>
     * 现在更新为在userCustom存在数据就表示用户个性化配置中选择了该店铺。 key==用户id；value==个性化数据集合
     */
    private static Map<String, Map<String, UserCustom>> userCustomMap = new HashMap<String, Map<String, UserCustom>>();

    /**
     * 关联关系缓存
     */
    public static Map<Integer, List<Integer>> userRelationMap = new HashMap<Integer, List<Integer>>();

    /** 首次登录配置缓存 */
    private static Map<String, UserCustom> firstVisitMap = new HashMap<String, UserCustom>();

    /**
     * 用于重量更新的查询，作为分页的参数
     */
    public static Map<String, Object> orderParam = new HashMap<String, Object>();

    /**
     * 用于问题件补偿的时间参数
     */
    public static Map<String, Object> questionnaireMap = new HashMap<String, Object>();
    /** 问题件#下次启动的查询起始时间参数 */
    public static final String QSN_COMPENSATETIME = "updateDay";
    /** 用来放省份跟新的参数 */
    public static final Map<String, Object> proviceParam = new HashMap<String, Object>();

    private static boolean configInit = true;

    /**
     * 根据Key获取配置参数
     * 
     * @param key
     * @return
     */
    public static String get(String key) {
        if (configInit) {
            configInit = false;
            // TODO 如果取不到值就要判断参数是否正确
            logger.info("=== 开始加载配置信息 ===");

            ConfigCode pCode = (ConfigCode) configCodeService.getConfByKey(key);
            List<ConfigCode> list = (List<ConfigCode>) configCodeService.getConfByPid(pCode.getId());

            for (ConfigCode code : list) {
                logger.info("配置信息. key:" + code.getConfKey() + ";value:" + code.getConfValue());
                memcacheService.add(CachePrefixConstant.CONFIG + code.getConfKey(), code.getConfValue());
            }
            logger.info("=== 结束加载配置信息 ===");
        }
        return memcacheService.get(CachePrefixConstant.CONFIG + key);
    }

    /**
     * 更新缓存。同时更新数据库配置信息
     * 
     * @param key
     * @param value
     * @return
     */
    public static void put(String key, String value) {

        String v = memcacheService.get(CachePrefixConstant.CONFIG + key);
        if (v != null) {
            // 更新DB配置
            ConfigCode code = (ConfigCode) configCodeService.getConfByKey(key);
            code.setConfValue(value);
            boolean flag = configCodeService.editConfig(code);
            if (!flag) {
                logger.error("参数缓存更新失败! 参数信息[key:" + key + ";value:" + value + "]");
                return;
            }
            // 更新内存缓存
            memcacheService.add(CachePrefixConstant.CONFIG + key, value);
            logger.info("更新参数缓存成功! 参数信息[key:" + key + ";新值:" + value + ";旧值:" + v + "]");
        } else {
            logger.error("参数缓存更新失败! 根据key查到的缓存对象! 参数信息[key:" + key + "]");
        }
    }

    /**
     * 根据Key取问题件的配置参数，如果没有就查DB，然后存入{@link Resource#qsnConfigMap}
     * 
     * @param key
     * @return
     */
    public static String getQsnConfig(String key) {
    	String value = "";
        try
        {
        	value = memcacheService.get(CachePrefixConstant.CONFIG + key);
        	//logger.error(" 从缓存里面取得的问题件开始时间："+(value==null?" 为空":value));
        }catch(Exception e){
        	logger.error("getQsnConfig 从Memcache中取数据出错 原因：",e);
        }
        if (StringUtils.isBlank(value)) {
            ConfigCode pCode = (ConfigCode) configCodeService.getConfByKeyAndType("questionnaire", "2");
            List<ConfigCode> list = (List<ConfigCode>) configCodeService.getConfByPid(pCode.getId());
            for (ConfigCode code : list) {
                logger.info("问题件配置信息. key:" + code.getConfKey() + ";value:" + code.getConfValue());
                if (key.equals(code.getConfKey())) {
                    value = code.getConfValue();
                }
                try
                {
                	memcacheService.add(CachePrefixConstant.CONFIG + code.getConfKey(), code.getConfValue());
                }catch(Exception e){
                	logger.error("getQsnConfig 添加数据到Memcache中出错 原因：",e);
                }
            }
        }
        return value;
    }

    /**
     * 更新缓存。同时更新数据库配置信息
     * 
     * @param key
     * @param value
     * @return
     */
    public static void updateQsnConfig(String key, String value) {
    	String v = "";
    	try
    	{
    		v = memcacheService.get(CachePrefixConstant.CONFIG + key);
    		if (v != null) {
                // 更新DB配置
            	ConfigCode code = null;
            	try
            	{
            		 code = (ConfigCode) configCodeService.getConfByKey(key);
                     code.setConfValue(value);
            	}catch(Exception e){
            		logger.error("Resource updateQsnConfig key:"+key+" value:"+value+" 从数据库取出错 原因：",e);
            	}
                if(code!=null){
                	 boolean flag = configCodeService.editConfig(code);
                     if (!flag) {
                         logger.error("问题件参数缓存更新失败! 参数信息[key:" + key + ";value:" + value + "]");
                         return;
                     }
                     // 更新内存缓存
                     try
                     {
                    	 memcacheService.add(CachePrefixConstant.CONFIG + key, value);
                     }catch(Exception e){
                 		logger.error("Resource updateQsnConfig key:"+key+" value:"+value+" 更新内存缓存出错 原因：",e);
                 	}
                     
                     logger.info("更新问题件参数缓存成功! 参数信息[key:" + key + ";新值:" + value + ";旧值:" + v + "]");
                }
            } 
    	}catch(Exception e){
    		logger.error("问题件参数缓存更新失败 Resource updateQsnConfig key:"+key+" value:"+value+" 从缓存取出错 原因：",e);
    	}
    }

    /**
     * 根据网点编码取网点对象<br>
     * 如果对象不存在，就去数据库中查，然后存入缓存<br>
     * 否则返回空对象<code>new DtoBranch()</code><br>
     * <br>
     * TODO 是否要定期更新缓存？如果网点用户更新个人信息后，缓存信息（userNameText）是否要改变
     * 
     * @param site
     * @return {@link DtoBranch}
     */
    public static DtoBranch getDtoBranchByCode(String site) {
    	//long t1 = System.currentTimeMillis();
    	DtoBranch branch = null;
    	try
    	{
    		branch = dtoMemcacheService.get(CachePrefixConstant.WANG + site);
    		//long t2 = System.currentTimeMillis();
            //logger.error(" 从 Memcache中取网点对象耗时："+(t2-t1)+" ms 取得对象 branch:"+(branch==null?" 为空":" 成功"));
    	}catch(Exception e){
    		logger.error("从 Memcache中取网点对象出错 原因：",e);
    	}
        if (branch == null) {
        	//long t3 = System.currentTimeMillis();
            List<User> list = userService.searchUsersBySiteAndUserType(site, "2"); // 网点
            //logger.error(" 从 数据库中取网点对象耗时："+(System.currentTimeMillis()-t3)+" ms");
            if (list != null && list.size() != 0) {
                User user = list.get(0);
                String mobile = "";
                String phone = "";
                if (user.getMobilePhone() != null)
                    mobile = user.getMobilePhone();
                if (user.getTelePhone() != null)
                    phone = user.getTelePhone();
                branch = new DtoBranch(site, user.getUserNameText(), user.getUserState(), phone, mobile);
                //long t4 = System.currentTimeMillis();
                try
                {
                	 dtoMemcacheService.add(CachePrefixConstant.WANG + site, branch);
                }catch(Exception e){
            		logger.error("将对象写入Memcache出错 原因：",e);
            	}
                //long t5 = System.currentTimeMillis();
                //logger.error(" 将对象写入Memcache耗时："+(t5-t4)+" ms");
            }
        }

        // 没查到网点或网点为空
        if (branch == null) {
            DtoBranch dto = new DtoBranch();
            dto.setText(site + "网点");
            logger.error("网点对象为空! 用户编码在用户表(user)中没有对应的数据. [code:" + site + "]");
            // return dto; // @2012-03-28/ChenRen 如果user表中不存在网点. 验证不通过
            return new DtoBranch();
        }

        return branch;
    }

    /**
     * 自动通知客户网点数据autoNotifyUserMap的初始化，同时记录当前build的时间赋值给lastNotifyUserMapTime
     */
    public static boolean getAutoNotifyUser(String userName) {

        String autoNotify = memcacheService.get(CachePrefixConstant.AOTUNOTIFY + userName);
        if (StringUtils.isEmpty(autoNotify)) {
            UserCustom uc = new UserCustom();
            uc.setUserName(userName);
            uc.setType(UserCustom.OPENNOTIFY);
            List<UserCustom> ucs = userCustomService.searchUserCustom(uc);
            if (ucs != null && ucs.size() > 0) {// 表示网点开启了问题件自动通知客户功能,则自动通知所有类型的问题件
                memcacheService.add(CachePrefixConstant.AOTUNOTIFY + userName, "1");
            }
        }
        if (StringUtils.equals(autoNotify, "1")) {
            return true;
        }

        return false;

    }

    /**
     * 初始化渠道信息
     */
    public static Channel initChannel(String clientId) {
        Channel channel = channelMemcacheService.get(CachePrefixConstant.CHANNEL + StringUtils.upperCase(clientId));
        if (channel == null || StringUtils.isBlank(channel.getIpAddress())) {
            channel = channelService.getChannelByClientId(clientId);
            channelMemcacheService.add(CachePrefixConstant.CHANNEL + clientId, channel);
        }
        return channel;

    }

    /**
     * 获取电商客户对应的接口url地址
     * 
     * @param clientId
     * @return
     */
    public static String getChannel(String clientId) {
        Channel channel = initChannel(clientId);
        return channel != null ? channel.getIpAddress() : null;
    }

    /**
     * 获取电商客户的加密密钥
     * 
     * @param clientId
     * @return
     */
    public static String getSecretId(String clientId) {
        Channel channel = initChannel(clientId);
        return channel != null ? channel.getParternId() : null;
    }

    /**
     * 根据clientId判断是否是圆通新龙的查询vip客户
     * 
     * @param clientId
     * @return
     */
    public static boolean getQueryVip(String clientId) {
        Channel channel = initChannel(clientId);
        return channel != null && StringUtils.equals(channel.getVip(), "Y") ? true : false;
    }

    /**
     * 根据省份名称获取对应的省份id
     * 
     * @param name
     * @return
     */
    public static Integer getIdByName(String name) {
        initRegion();
        Integer result = 0;
        if (StringUtils.isEmpty(name)) {
            return result;
        }
        if (provinces.containsValue(name)) {
            Iterator<Entry<Integer, String>> iterators = provinces.entrySet().iterator();
            Map.Entry<Integer, String> entry = null;
            while (iterators.hasNext()) {
                entry = iterators.next();
                if (StringUtils.equalsIgnoreCase(name, entry.getValue())) {
                    result = entry.getKey();
                    break;
                }

            }
        } else {
        	//--去掉打印未知省份日志
            //logger.error("unknown province " + name);
        }
        return result;
    }

    /**
     * 根据省份id获取对应的省份名称
     * 
     * @param id
     * @return
     */
    public static String getNameById(Integer id) {
        initRegion();
        if (id == null || id.intValue() == 0) {
            return StringUtils.EMPTY;
        }
        if (provinces.containsKey(id)) {
            return provinces.get(id);
        } else {
            logger.error("unknown province id");
        }
        return StringUtils.EMPTY;
    }

    /**
     * 根据省份名获取对应的省份编码
     * 
     * @param name
     * @return code
     */
    public static Integer getCodeByName(String name) {
        initRegion();
        if (name == null) {
            return 0;
        }
        if (provincesCH.containsKey(name)) {
            return provincesCH.get(name);
        } 
        /*else {
            logger.error("unknown province-->" + name);
        }*/
        return 0;
    }

    /**
     * 获取电商客户对应的推送标记
     * 
     * @param clientId
     * @return
     */
    public static boolean verifyDispatch(String clientId) {
        Channel channel = initChannel(clientId);
        return channel != null && Channel.DISPATCH.equals(channel.getValue()) ? true : false;
    }

    private static void initPermissionData() {
        if (initPermission) {
            initPermission = false;

            ConfigCode admin = (ConfigCode) configCodeService.getConfByKeyAndType("permission_admin", "2");
            List<ConfigCode> admin_list = (List<ConfigCode>) configCodeService.getConfByPid(admin.getId());
            for (ConfigCode code : admin_list) {
                permission_adminMap.put(code.getConfKey(), code.getConfValue());
            }

            ConfigCode wangdian = (ConfigCode) configCodeService.getConfByKeyAndType("permission_wangdian", "2");
            List<ConfigCode> wangdian_list = (List<ConfigCode>) configCodeService.getConfByPid(wangdian.getId());
            for (ConfigCode code : wangdian_list) {
                permission_wangdianMap.put(code.getConfKey(), code.getConfValue());
            }

            ConfigCode wangdian_kefu = (ConfigCode) configCodeService.getConfByKeyAndType("permission_wangdian_kefu",
                    "2");
            List<ConfigCode> wangdian_kefu_list = (List<ConfigCode>) configCodeService.getConfByPid(wangdian_kefu
                    .getId());
            for (ConfigCode code : wangdian_kefu_list) {
                permission_wangdian_kefuMap.put(code.getConfKey(), code.getConfValue());
            }

            ConfigCode wangdian_caiwu = (ConfigCode) configCodeService.getConfByKeyAndType("permission_wangdian_caiwu",
                    "2");
            List<ConfigCode> wangdian_caiwu_list = (List<ConfigCode>) configCodeService.getConfByPid(wangdian_caiwu
                    .getId());
            for (ConfigCode code : wangdian_caiwu_list) {
                permission_wangdian_caiwuMap.put(code.getConfKey(), code.getConfValue());
            }

            ConfigCode maijia = (ConfigCode) configCodeService.getConfByKeyAndType("permission_maijia", "2");
            List<ConfigCode> maijia_list = (List<ConfigCode>) configCodeService.getConfByPid(maijia.getId());
            for (ConfigCode code : maijia_list) {
                permission_maijiaMap.put(code.getConfKey(), code.getConfValue());
            }

            ConfigCode maijia_kefu = (ConfigCode) configCodeService.getConfByKeyAndType("permission_maijia_kefu", "2");
            List<ConfigCode> maijia_kefu_list = (List<ConfigCode>) configCodeService.getConfByPid(maijia_kefu.getId());
            for (ConfigCode code : maijia_kefu_list) {
                permission_maijia_kefuMap.put(code.getConfKey(), code.getConfValue());
            }

            ConfigCode maijia_caiwu = (ConfigCode) configCodeService
                    .getConfByKeyAndType("permission_maijia_caiwu", "2");
            List<ConfigCode> maijia_caiwu_list = (List<ConfigCode>) configCodeService
                    .getConfByPid(maijia_caiwu.getId());
            for (ConfigCode code : maijia_caiwu_list) {
                permission_maijia_caiwuMap.put(code.getConfKey(), code.getConfValue());
            }

            ConfigCode pingtai = (ConfigCode) configCodeService.getConfByKeyAndType("permission_pingtai", "2");
            List<ConfigCode> pingtai_list = (List<ConfigCode>) configCodeService.getConfByPid(pingtai.getId());
            for (ConfigCode code : pingtai_list) {
                permission_pingtaiMap.put(code.getConfKey(), code.getConfValue());
            }
        }
    }

    public static boolean hasPermission(String usertype, String action) {
        initPermissionData();
        if ("3".equals(usertype) || "31".equals(usertype)) {
            return Resource.permission_adminMap.containsValue(action);
        }
        // 卖家
        else if ("1".equals(usertype)) {
            return Resource.permission_maijiaMap.containsValue(action);
        }
        // 卖家客服
        else if ("11".equals(usertype)) {
            return Resource.permission_maijia_kefuMap.containsValue(action);
        }
        // 卖家财务
        else if ("12".equals(usertype)) {
            return Resource.permission_maijia_caiwuMap.containsValue(action);
        }
        // 网点
        else if ("2".equals(usertype)) {
            return Resource.permission_wangdianMap.containsValue(action);
        }
        // 网点客服
        else if ("21".equals(usertype)) {
            return Resource.permission_wangdian_kefuMap.containsValue(action);
        }
        // 网点财务
        else if ("22".equals(usertype)) {
            return Resource.permission_wangdian_caiwuMap.containsValue(action);
        }
        // 平台用户
        else if ("4".equals(usertype)) {
            return Resource.permission_pingtaiMap.containsValue(action);
        }

        return false;
    }

    /**
     * 获取与user用户有关联关系的用户主账号id（不包括自己）
     * 
     * @param user
     * @return
     */
    public static List<Integer> getUserRelationUserIdList(User user) {
        Integer id = user.getId();
        if (!user.getUserType().equals("1"))
            id = user.getParentId();
        List<Integer> result = userRelationMap.get(id);
        if (result != null) {
            return result;
        }
        // 查db
        setUserRelationUserIdList(user);
        return userRelationMap.get(id);
    }

    /**
     * 将与user对象有关联关系的用户主账号id（不包括自己）放入缓存
     * 
     * @param user
     */
    public static void setUserRelationUserIdList(User user) {
        Integer id = user.getId();
        if (!user.getUserType().equals("1"))
            id = user.getParentId();
        List<UserRelation> urList = userRelationService.searchByUserId(id);
        if (urList != null && urList.size() > 0) {
            List<Integer> urIdList = new ArrayList<Integer>();
            for (UserRelation ur : urList) {
                if (id.equals(ur.getUserId())) {
                    if (!urIdList.contains(ur.getRelatedUserId()))
                        urIdList.add(ur.getRelatedUserId());
                } else {
                    if (!urIdList.contains(ur.getUserId()))
                        urIdList.add(ur.getUserId());
                }
            }
            userRelationMap.put(id, urIdList);
        } else {// 如果没有关联关系直接清空缓存
            userRelationMap.remove(id);
        }
    }

    /**
     * 根据当前用户取个性化配置信息（既已经关联上的店铺）<br>
     * 如果缓存中木有就去DB查，然后存入缓存<br>
     * 
     * @param user
     * @return
     */
    public static Map<String, UserCustom> getRelationAccountCustoms(User user) {
        Map map = userCustomMap.get(user.getId().toString());
        if (map != null)
            return map;
        // 去DB查，然后存入缓存
        setRelationAccountCustoms(user);
        return userCustomMap.get(user.getId().toString());
    }

    /**
     * 将user个性化配置信息（既已经关联上的店铺）加入缓存<br>
     * 
     * @param user
     * @return
     */
    public static void setRelationAccountCustoms(User user) {
        UserCustom uc = new UserCustom();
        uc.setUserName(user.getUserName());
        uc.setType(UserCustom.RELATIONAL);
        uc.setRelationalQuery(UserCustom.RELATIONAL);
        List<UserCustom> ucs = userCustomService.searchUserCustom(uc);
        if (ucs.size() > 0) {
            Map<String, UserCustom> relationAccountCustoms = new HashMap<String, UserCustom>();
            for (UserCustom u : ucs) {
                relationAccountCustoms.put(u.getBindedUserName(), u);
            }
            userCustomMap.put(user.getId().toString(), relationAccountCustoms);
        }
    }

    public static void setFirstVisit(User user) {
        UserCustom uc = new UserCustom();
        uc.setUserName(user.getUserName());
        uc.setBindedUserName(user.getUserName());
        uc.setType(UserCustom.FIRST_VISIT);
        List<UserCustom> ucs = userCustomService.searchUserCustom(uc);
        if (ucs.size() > 0) {
            firstVisitMap.put(user.getUserName(), ucs.get(0));
        } else {
            // 查不到则插入一条数据
            UserCustom newUC = new UserCustom();
            newUC.setUserName(user.getUserName());
            newUC.setBindedUserName(user.getUserName());
            newUC.setCustomerId(user.getTaobaoEncodeKey());
            newUC.setType(UserCustom.FIRST_VISIT);
            newUC.setRelationalQuery("2");
            userCustomService.add(newUC);
            firstVisitMap.put(user.getUserName(), newUC);
        }
    }

    /**
     * 获取账号的初次点击页面配置记录
     */
    public static UserCustom getFirstVisit(User user) {
        UserCustom uc = firstVisitMap.get(user.getUserName());
        if (uc != null)
            return uc;
        setFirstVisit(user);
        return firstVisitMap.get(user.getUserName());
    }

    /**
     * 获取user用户的个性化配置信息
     */
    public static List<String> getBindedCustomerIdList(User user) {
        List<String> list = new ArrayList<String>();
        Map<String, UserCustom> map = getRelationAccountCustoms(user);
        if (map != null) {
            Iterator<UserCustom> it = map.values().iterator();
            while (it.hasNext()) {
                UserCustom uc = (UserCustom) it.next();
                if (StringUtils.isNotEmpty(uc.getCustomerId()))
                    list.add(uc.getCustomerId());
            }
        }
        return list;
    }

    /**
     * 获取绑定账号的主帐号的userId list
     */
    public static List<Integer> getBindedUserIdList(User user) {
        List<Integer> list = new ArrayList<Integer>();
        Map<String, UserCustom> map = getRelationAccountCustoms(user);
        if (map != null) {
            Iterator<UserCustom> it = map.values().iterator();
            while (it.hasNext()) {
                UserCustom uc = (UserCustom) it.next();
                if (getUserByCustomerId(uc.getCustomerId()) != null
                        && getUserByCustomerId(uc.getCustomerId()).getId() != null) {
                    list.add(getUserByCustomerId(uc.getCustomerId()).getId());
                }
            }
        }
        return list;
    }

    private static void initRegion() {
        if (regionInit) {
            List<Region> regions = regionService.getAllRegion();
            for (Region region : regions) {
                provinces.put(region.getId(), region.getRegionName());
                provincesCH.put(region.getRegionName(), region.getId());
                // 缓存一份没有省字的
                if (region.getRegionName() != null && region.getRegionName().indexOf("省") > -1) {
                    provincesCH.put(region.getRegionName().replace("省", ""), region.getId());
                }
                if (region.getRegionName() != null && region.getRegionName().indexOf("特别行政区") > -1) {
                    provincesCH.put(region.getRegionName().replace("特别行政区", ""), region.getId());
                }

            }

            if (otherProvinces != null) {
                provinces.putAll(otherProvinces);
            }
            if (otherProvincesCH != null) {
                provincesCH.putAll(otherProvincesCH);
            }
            regionInit = false;
        }
    }

    /**
     * 根据运费模板对象解析运费信息<br>
     * map中增加存放Constants.POSTINFO和Constants.SRCPROVCODE分别表示运费信息map(以<省份id(目的地), Postinfo>方式存)和始发地省份编码
     * 
     * @param posttemp
     * @param map
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void buildPostinfo(Posttemp posttemp, Map map) {
        List<Postinfo> pList = null;
        if (posttemp.getId() == 0) {// 系统模板
            pList = posttemp.getPostinfoList();
        } else {
            String xml = posttemp.getPostinfo(); // 运费信息xml字符串
            try {
                pList = XmlUtil.xmlDecoder2List(xml);// 解析
            } catch (Exception e) {
                logger.error("数据异常!xml解析无法解析![xml:" + xml + "]");
            }
            if (pList.size() < 1) {
                logger.error("数据异常!运费信息为空![xml:" + xml + "]");
            }
        }
        // 运费信息Map; 值以<省份id(目的地), postinfo>方式存; 计算的时候用省份id取运费信息
        Map<String, Postinfo> postinfoMap = new HashMap<String, Postinfo>();
        String srcProvCode = ""; // 运费模板的 始发地省份编码
        for (Postinfo postinfo : pList) {
            if (StringUtils.isEmpty(srcProvCode)) {
                srcProvCode = postinfo.getSrcId();
            }
            postinfoMap.put(postinfo.getDestId(), postinfo);
        }
        map.put(Constants.POSTINFO, postinfoMap);
        map.put(Constants.SRCPROVCODE, srcProvCode);
    }

    /**
     * 根据customerId查询user
     * 
     * @param customerId
     * @return
     */
    public static User getUserByCustomerId(String customerId) {

        User user = userMemcacheService.get(CachePrefixConstant.USER + customerId);
        if (user == null) {
            user = userService.getUserByCustomerId(customerId);
            userMemcacheService.add(CachePrefixConstant.USER + customerId, user);
        }
        return user;
    }

    /**
     * 通过userName获取sellerUserMap中user
     * 
     * @param userName
     * @return
     */
    public static User getUserByUserName(String userName) {

        User user = userMemcacheService.get(CachePrefixConstant.USER + userName);
        if (user == null) {
            user = userService.getUserByUserName(userName);
            userMemcacheService.add(CachePrefixConstant.USER + userName, user);
        }
        return user;
    }

    /**
     * 根据siteCode查询user
     * 
     * @param siteCode
     * @return
     */
    public static User getUserBySiteCode(String siteCode) {

        return getUserByUserName(siteCode);
    }

    /**
     * 获取电商客户的ip
     * 
     * @param clientId
     * @return
     */
    public static String getIp(String clientId) {
        Channel channel = initChannel(clientId);
        return channel != null ? channel.getIp() : null;
    }

    /**
     * 获取电商客户的isSend
     * 
     * @param clientId
     *            电商标识
     * 
     * 
     * @return
     */
    public static boolean getIsSend(String clientId) {
        Channel channel = initChannel(clientId);
        return channel != null && Channel.ISSEND.equals(channel.getIsSend()) ? true : false;
    }

    /**
     * 获取电商客户的isPrint
     * 
     * @param clientId
     *            电商标识
     * @return
     */
    public static boolean getIsPrint(String clientId) {
        Channel channel = initChannel(clientId);
        return channel != null && Channel.ISPRINT.equals(channel.getIsPrint()) ? true : false;
    }

    /**
     * 获取电商客户的isSend
     * 
     * @param clientId
     *            电商标识
     * 
     * 
     * @return
     */
    public static String getLineType(String clientId) {
        Channel channel = initChannel(clientId);
        return channel != null ? channel.getLineType() : null;
    }

    /**
     * 根据key值获取config表的value(定时器jobMonitor获取重量和问题件更新时间)
     * 
     * @param key
     * @return
     */
    public static String getJobMonitorTime(String key) {
        return configCodeService.getJobMonitorTime(key);
    }

    /**
     * 根据key值获取config表的value(定时器jobMonitor获取订单创建和订单状态更新更新时间)
     * 
     * @param key
     * @return
     * @author liuchunyan
     */
    public static List<ConfigCode> getJobMonitorTime1(String key) {
        return configCodeService.getJobMonitorTime1(key);
    }

    /**
     * 根据clientId取得channel_info表中的channel_key值 （易迅数据中，channel_key值保存易迅提供的APIKey）
     * 
     * @param clientId
     * @return
     */
    public static String getYixunApikey(String clientId) {
        Channel channel = initChannel(clientId);
        return channel != null ? channel.getKey() : null;
    }

    public ChannelService<Channel> getChannelService() {
        return channelService;
    }

    public void setChannelService(ChannelService<Channel> channelService) {
        Resource.channelService = channelService;
    }

    public ConfigCodeService<ConfigCode> getConfigCodeService() {
        return configCodeService;
    }

    public void setConfigCodeService(ConfigCodeService<ConfigCode> configCodeService) {
        Resource.configCodeService = configCodeService;
    }

    public UserService<User> getUserService() {
        return userService;
    }

    public void setUserService(UserService<User> userService) {
        Resource.userService = userService;
    }

    public UserCustomService<UserCustom> getUserCustomService() {
        return userCustomService;
    }

    public void setUserCustomService(UserCustomService<UserCustom> userCustomService) {
        Resource.userCustomService = userCustomService;
    }

    public RegionService<Region> getRegionService() {
        return regionService;
    }

    public void setRegionService(RegionService<Region> regionService) {
        Resource.regionService = regionService;
    }

    public Map<Integer, String> getOtherProvinces() {
        return otherProvinces;
    }

    public void setOtherProvinces(Map<Integer, String> otherProvinces) {
        this.otherProvinces = otherProvinces;
    }

    public Map<String, Integer> getProvincesCH() {
        return provincesCH;
    }

    public void setProvincesCH(Map<String, Integer> provincesCH) {
        Resource.provincesCH = provincesCH;
    }

    public Map<String, Integer> getOtherProvincesCH() {
        return otherProvincesCH;
    }

    public void setOtherProvincesCH(Map<String, Integer> otherProvincesCH) {
        Resource.otherProvincesCH = otherProvincesCH;
    }

    public UserRelationService<UserRelation> getUserRelationService() {
        return userRelationService;
    }

    public void setUserRelationService(UserRelationService<UserRelation> userRelationService) {
        Resource.userRelationService = userRelationService;
    }

    public Map<String, String> getOrderProvinces() {
        return orderProvinces;
    }

    public void setOrderProvinces(Map<String, String> orderProvinces) {
        Resource.orderProvinces = orderProvinces;
    }

    public MemcacheService<DtoBranch> getDtoMemcacheService() {
        return dtoMemcacheService;
    }

    public void setDtoMemcacheService(MemcacheService<DtoBranch> dtoMemcacheService) {
        Resource.dtoMemcacheService = dtoMemcacheService;
    }

    public MemcacheService<Channel> getChannelMemcacheService() {
        return channelMemcacheService;
    }

    public void setChannelMemcacheService(MemcacheService<Channel> channelMemcacheService) {
        Resource.channelMemcacheService = channelMemcacheService;
    }

    public MemcacheService<User> getUserMemcacheService() {
        return userMemcacheService;
    }

    public void setUserMemcacheService(MemcacheService<User> userMemcacheService) {
        Resource.userMemcacheService = userMemcacheService;
    }

    public MemcacheService<String> getMemcacheService() {
        return memcacheService;
    }

    public void setMemcacheService(MemcacheService<String> memcacheService) {
        Resource.memcacheService = memcacheService;
    }

}
