// 单例方式

package net.ytoec.kernel.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * 从kernel.properties读取配置文件，在spring*.xml里为其注入值
 * 
 * @author MGL
 */
public class ConfigUtilSingle {

    private String                  solrBranchUrl;
    private String                  solrEccoreUrl;
    private String                  TOP_APPKEY;
    private String                  TOP_SECRET;
    private String                  OFFICALURL;
    private String                  LOGOFF_URL;
    private String                  TAOBAOFUWU_URL;
    private String                  TAOBAOUSERMETHOD;
    private String                  FORMAT_XML;
    private String                  FORMAT_JSON;
    private String                  TAOBAO_USER_ENCRYPTEDID;
    private String                  PARTERID_TAOBAO;
    private String                  PARTERID_COMMON;
    private String                  PARTERID_HR;
    private String                  SIGN_METHOD_MD5;
    private int                     COOKIE_EXPIRY_WEEK;
    private String                  COOKIE_ENCODE_KEY;
    private boolean                 CONTRALSYNCHRONIZED;
    private String                  QQ_APPID;
    private String                  QQ_APPKEY;
    private String                  QQ_API;

    private String                  SELLER_USERNAME;
    private String                  BRANCH_USERNAME;
    //运单号服务地址
    private String 					GEN_NUM_SERVICE_URL;
    //文件版本号
    private static String           version;
    
    private String 					CANGPEI_URL;
    private String    				MANAGERMAILNO_URL;
    private String					PARTERNID;
    private String                  MAILPARTERNID;
    private String 					USERINFO_URL;
    private String                  TAOBAO_TRACEPUSH;
    
    

	public String getTAOBAO_TRACEPUSH() {
		return TAOBAO_TRACEPUSH;
	}

	public void setTAOBAO_TRACEPUSH(String tAOBAO_TRACEPUSH) {
		TAOBAO_TRACEPUSH = tAOBAO_TRACEPUSH;
	}

	public String getMAILPARTERNID() {
		return MAILPARTERNID;
	}

	public void setMAILPARTERNID(String mAILPARTERNID) {
		MAILPARTERNID = mAILPARTERNID;
	}

	public String getCANGPEI_URL() {
		return CANGPEI_URL;
	}

	public void setCANGPEI_URL(String cANGPEI_URL) {
		CANGPEI_URL = cANGPEI_URL;
	}

	public String getPARTERNID() {
		return PARTERNID;
	}

	public void setPARTERNID(String pARTERNID) {
		PARTERNID = pARTERNID;
	}

	public String getSELLER_USERNAME() {
		return SELLER_USERNAME;
	}

	public void setSELLER_USERNAME(String sELLER_USERNAME) {
		SELLER_USERNAME = sELLER_USERNAME;
	}

	public String getBRANCH_USERNAME() {
		return BRANCH_USERNAME;
	}

	public void setBRANCH_USERNAME(String bRANCH_USERNAME) {
		BRANCH_USERNAME = bRANCH_USERNAME;
	}

	public String getQQ_API() {
		return QQ_API;
	}

	public void setQQ_API(String qQ_API) {
		QQ_API = qQ_API;
	}

	//#------------------充值、支付[支付宝]相关的参数begin----------------
    private String ALIPAY_PARTNER;
    private String ALIPAY_KEY;
    private String ALIPAY_SELLER_EMAIL;
    private String NOTIFY_URL;
    private String RETURN_URL;
    private String ALIPAY_INPUT_CHARSET;
    
    private String EMAY_SOFTWARESERIALNO;
    private String EMAY_KEY;
    private String EMAY_PASSOWRD;
    //#------------------充值、支付[支付宝]相关的参数end------------------
    
    /**
     * 从淘宝下载会员信息ZIP包路径
     */
    private String 					TAOBAO_MEMBER_ZIP_URL;
    
    /**
     * 解压ZIP的路径
     */
    private String 					TAOBAO_MEMBER_UNZIP_URL;

    /**
     * 允许登陆生产环境的买家名字
     */
    private String                  testBuyerName;

    /**
     * 问题件
     */
    private String                  questionnaireIssue;
    private String                  questionnaireIssuedeal;
    private int                     questionnairePeriod;
    private String                  questionnaireFile;

    /**
     * 禁止买家登陆
     */
    private String                  forbidBuyer;
    /**
     * 性能分析的阀值
     */
    private String                  performanceLimt;
    
    /**
     * 淘宝登录失败时发送邮件的email
     */
    private String 					emailListString;
    
    /**
     * 时效提醒免费日期
     */
    private String 					freeAgingTime;

    // 构造私有实例
    private static ConfigUtilSingle instance = null;

    // 构造函数私有话 不允许构造
    private ConfigUtilSingle() {
    }

    public static ConfigUtilSingle getInstance() {
        // //延迟加载
        // if (instance == null) {
        // //加锁 防止线程并发
        // synchronized (JDBCUtilSingle.class) {
        // 必须有的判断
        if (instance == null) {
            instance = new ConfigUtilSingle();
        }
        // }
        // }
        return instance;
    }

    public String getSolrBranchUrl() {
        return solrBranchUrl;
    }

    public void setSolrBranchUrl(String solrBranchUrl) {
        this.solrBranchUrl = solrBranchUrl;
    }

    public String getSolrEccoreUrl() {
        return solrEccoreUrl;
    }

    public void setSolrEccoreUrl(String solrEccoreUrl) {
        this.solrEccoreUrl = solrEccoreUrl;
    }

    public String getTOP_APPKEY() {
        return TOP_APPKEY;
    }

    public void setTOP_APPKEY(String tOP_APPKEY) {
        TOP_APPKEY = tOP_APPKEY;
    }

    public String getTOP_SECRET() {
        return TOP_SECRET;
    }

    public void setTOP_SECRET(String tOP_SECRET) {
        TOP_SECRET = tOP_SECRET;
    }

    public String getOFFICALURL() {
        return OFFICALURL;
    }

    public void setOFFICALURL(String oFFICALURL) {
        OFFICALURL = oFFICALURL;
    }

    public String getLOGOFF_URL() {
        return LOGOFF_URL;
    }

    public void setLOGOFF_URL(String lOGOFF_URL) {
        LOGOFF_URL = lOGOFF_URL;
    }

    public String getTAOBAOFUWU_URL() {
		return TAOBAOFUWU_URL;
	}

	public void setTAOBAOFUWU_URL(String tAOBAOFUWU_URL) {
		TAOBAOFUWU_URL = tAOBAOFUWU_URL;
	}

	public String getTAOBAOUSERMETHOD() {
        return TAOBAOUSERMETHOD;
    }

    public void setTAOBAOUSERMETHOD(String tAOBAOUSERMETHOD) {
        TAOBAOUSERMETHOD = tAOBAOUSERMETHOD;
    }

    public String getFORMAT_XML() {
        return FORMAT_XML;
    }

    public void setFORMAT_XML(String fORMAT_XML) {
        FORMAT_XML = fORMAT_XML;
    }

    public String getFORMAT_JSON() {
        return FORMAT_JSON;
    }

    public void setFORMAT_JSON(String fORMAT_JSON) {
        FORMAT_JSON = fORMAT_JSON;
    }

    public String getTAOBAO_USER_ENCRYPTEDID() {
        return TAOBAO_USER_ENCRYPTEDID;
    }

    public void setTAOBAO_USER_ENCRYPTEDID(String tAOBAO_USER_ENCRYPTEDID) {
        TAOBAO_USER_ENCRYPTEDID = tAOBAO_USER_ENCRYPTEDID;
    }

    public String getPARTERID_TAOBAO() {
        return PARTERID_TAOBAO;
    }

    public void setPARTERID_TAOBAO(String pARTERID_TAOBAO) {
        PARTERID_TAOBAO = pARTERID_TAOBAO;
    }

    public String getPARTERID_COMMON() {
        return PARTERID_COMMON;
    }

    public void setPARTERID_COMMON(String pARTERID_COMMON) {
        PARTERID_COMMON = pARTERID_COMMON;
    }

    public String getPARTERID_HR() {
        return PARTERID_HR;
    }

    public void setPARTERID_HR(String pARTERID_HR) {
        PARTERID_HR = pARTERID_HR;
    }

    public int getCOOKIE_EXPIRY_WEEK() {
        return COOKIE_EXPIRY_WEEK;
    }

    public void setCOOKIE_EXPIRY_WEEK(int cOOKIE_EXPIRY_WEEK) {
        COOKIE_EXPIRY_WEEK = cOOKIE_EXPIRY_WEEK;
    }

    public void setCOOKIE_ENCODE_KEY(String cOOKIE_ENCODE_KEY) {
        COOKIE_ENCODE_KEY = cOOKIE_ENCODE_KEY;
    }

    public String getCOOKIE_ENCODE_KEY() {
        return COOKIE_ENCODE_KEY;
    }

    public String getSIGN_METHOD_MD5() {
        return SIGN_METHOD_MD5;
    }

    public void setSIGN_METHOD_MD5(String sIGN_METHOD_MD5) {
        SIGN_METHOD_MD5 = sIGN_METHOD_MD5;
    }

    public String getQuestionnaireIssue() {
        return questionnaireIssue;
    }

    public void setQuestionnaireIssue(String questionnaireIssue) {
        this.questionnaireIssue = questionnaireIssue;
    }

    public String getQuestionnaireIssuedeal() {
        return questionnaireIssuedeal;
    }

    public void setQuestionnaireIssuedeal(String questionnaireIssuedeal) {
        this.questionnaireIssuedeal = questionnaireIssuedeal;
    }

    public int getQuestionnairePeriod() {
        return questionnairePeriod;
    }

    public void setQuestionnairePeriod(int questionnairePeriod) {
        this.questionnairePeriod = questionnairePeriod;
    }

    public String isForbidBuyer() {
        return forbidBuyer;
    }

    public void setForbidBuyer(String forbidBuyer) {
        this.forbidBuyer = forbidBuyer;
    }

    public List<String> getTestBuyerNameList() {
        if (StringUtils.isEmpty(testBuyerName)) {
            return Collections.EMPTY_LIST;
        }
        List<String> buyers = new ArrayList<String>();
        String[] buyerStrings = StringUtils.split(testBuyerName, ",");
        for (int i = 0; i < buyerStrings.length; i++) {
            if (StringUtils.isEmpty(buyerStrings[i])) {
                continue;
            }
            buyers.add(buyerStrings[i].trim());
        }
        return buyers;
    }
    
    public String getTestBuyerName() {
        return testBuyerName;
    }

    public void setTestBuyerName(String testBuyerName) {
        this.testBuyerName = testBuyerName;
    }

	public boolean getCONTRALSYNCHRONIZED() {
		return CONTRALSYNCHRONIZED;
	}

	public void setCONTRALSYNCHRONIZED(boolean cONTRALSYNCHRONIZED) {
		CONTRALSYNCHRONIZED = cONTRALSYNCHRONIZED;
	}

    
    public String getPerformanceLimt() {
        return performanceLimt;
    }

    
    public void setPerformanceLimt(String performanceLimt) {
        this.performanceLimt = performanceLimt;
    }

	
	public String getTAOBAO_MEMBER_ZIP_URL() {
		return TAOBAO_MEMBER_ZIP_URL;
	}

	public void setTAOBAO_MEMBER_ZIP_URL(String tAOBAO_MEMBER_ZIP_URL) {
		TAOBAO_MEMBER_ZIP_URL = tAOBAO_MEMBER_ZIP_URL;
	}

	public String getTAOBAO_MEMBER_UNZIP_URL() {
		return TAOBAO_MEMBER_UNZIP_URL;
	}

	public void setTAOBAO_MEMBER_UNZIP_URL(String tAOBAO_MEMBER_UNZIP_URL) {
		TAOBAO_MEMBER_UNZIP_URL = tAOBAO_MEMBER_UNZIP_URL;
	}

	public String getALIPAY_PARTNER() {
		return ALIPAY_PARTNER;
	}

	public void setALIPAY_PARTNER(String aLIPAY_PARTNER) {
		ALIPAY_PARTNER = aLIPAY_PARTNER;
	}

	public String getALIPAY_KEY() {
		return ALIPAY_KEY;
	}

	public void setALIPAY_KEY(String aLIPAY_KEY) {
		ALIPAY_KEY = aLIPAY_KEY;
	}

	public String getALIPAY_SELLER_EMAIL() {
		return ALIPAY_SELLER_EMAIL;
	}

	public void setALIPAY_SELLER_EMAIL(String aLIPAY_SELLER_EMAIL) {
		ALIPAY_SELLER_EMAIL = aLIPAY_SELLER_EMAIL;
	}

	

	public String getNOTIFY_URL() {
		return NOTIFY_URL;
	}

	public void setNOTIFY_URL(String nOTIFY_URL) {
		NOTIFY_URL = nOTIFY_URL;
	}

	public String getRETURN_URL() {
		return RETURN_URL;
	}

	public void setRETURN_URL(String rETURN_URL) {
		RETURN_URL = rETURN_URL;
	}

	public String getALIPAY_INPUT_CHARSET() {
		return ALIPAY_INPUT_CHARSET;
	}

	public void setALIPAY_INPUT_CHARSET(String aLIPAY_INPUT_CHARSET) {
		ALIPAY_INPUT_CHARSET = aLIPAY_INPUT_CHARSET;
	}

	public String getEMAY_SOFTWARESERIALNO() {
		return EMAY_SOFTWARESERIALNO;
	}


	public String getQQ_APPID() {
		return QQ_APPID;
	}

	public void setQQ_APPID(String qQ_APPID) {
		QQ_APPID = qQ_APPID;
	}

	public String getQQ_APPKEY() {
		return QQ_APPKEY;
	}

	public void setQQ_APPKEY(String qQ_APPKEY) {
		QQ_APPKEY = qQ_APPKEY;
	}



	public void setEMAY_SOFTWARESERIALNO(String eMAY_SOFTWARESERIALNO) {
		EMAY_SOFTWARESERIALNO = eMAY_SOFTWARESERIALNO;
	}

	public String getEMAY_KEY() {
		return EMAY_KEY;
	}

	public void setEMAY_KEY(String eMAY_KEY) {
		EMAY_KEY = eMAY_KEY;
	}

	public String getEMAY_PASSOWRD() {
		return EMAY_PASSOWRD;
	}

	public void setEMAY_PASSOWRD(String eMAY_PASSOWRD) {
		EMAY_PASSOWRD = eMAY_PASSOWRD;
	}

    
    public String getGEN_NUM_SERVICE_URL() {
        return GEN_NUM_SERVICE_URL;
    }

    
    public void setGEN_NUM_SERVICE_URL(String gEN_NUM_SERVICE_URL) {
        GEN_NUM_SERVICE_URL = gEN_NUM_SERVICE_URL;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

	public String getQuestionnaireFile() {
		return questionnaireFile;
	}

	public void setQuestionnaireFile(String questionnaireFile) {
		this.questionnaireFile = questionnaireFile;
	}

	/**
	 * 获取淘宝登录验证失败时邮件提醒用户email
	 * @return
	 */
	public List<String> getSendEmailList(){
    	if(StringUtils.isEmpty(emailListString)) {
    		return Collections.EMPTY_LIST;
    	}
    	List<String> sendList = new ArrayList<String>();
    	String[] sendListArr = StringUtils.split(emailListString, ",");
    	for (int i = 0; i < sendListArr.length; i++) {
            if (StringUtils.isEmpty(sendListArr[i])) {
                continue;
            }
            sendList.add(sendListArr[i].trim());
        }
        return sendList;
    }
	
	/**
	 * 判断指定时间是否属于时效提醒免费时间
	 * @param date
	 * @return
	 */
	public boolean isFreeAgingTime(Date date) {
		if(StringUtils.isEmpty(freeAgingTime)) {
			return false;
		}
		Set<String> freeSet = new HashSet<String>();
		String[] freeArr = freeAgingTime.split(",");
		for(int i=0; i<freeArr.length; i++) {
			if(StringUtils.isEmpty(freeArr[i])) 
				continue;
			freeSet.add(freeArr[i].trim());
		}
		//获取date属于星期几
		String weekName = DateUtil.getEnglishWeekName(date);
		if(freeSet.contains(weekName)) 
			return true;
		//获取date的字符串形式
		String dateString = DateUtil.format(date, "yyyy-MM-dd");
		if(freeSet.contains(dateString)) 
			return true;
		return false;
	}
	
	public String getEmailListString() {
		return emailListString;
	}

	public void setEmailListString(String emailListString) {
		this.emailListString = emailListString;
	}

	public String getMANAGERMAILNO_URL() {
		return MANAGERMAILNO_URL;
	}

	public void setMANAGERMAILNO_URL(String MANAGERMAILNO_URL) {
		this.MANAGERMAILNO_URL = MANAGERMAILNO_URL;
	}

	public String getFreeAgingTime() {
		return freeAgingTime;
	}

	public void setFreeAgingTime(String freeAgingTime) {
		this.freeAgingTime = freeAgingTime;
	}

	public String getUSERINFO_URL() {
		return USERINFO_URL;
	}

	public void setUSERINFO_URL(String uSERINFO_URL) {
		USERINFO_URL = uSERINFO_URL;
	}

	
}
