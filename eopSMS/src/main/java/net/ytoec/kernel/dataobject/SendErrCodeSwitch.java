package net.ytoec.kernel.dataobject;

import java.util.HashMap;
import java.util.Map;
/**
 * 错误代码转换类
 * @author Administrator
 *
 */
public class SendErrCodeSwitch {
	
	private static Map<String,String> numberType = new HashMap<String,String>();
	
	//移动错误代码    String[0]:错误码解释   String[1]:易通发送失败提示
	private static Map<String,String[]> mobileErrCodes = new HashMap<String,String[]>();
	
	//联通错误代码
	private static Map<String,String[]> linkErrCodes = new HashMap<String,String[]>();
	
	//电信错误代码
	private static Map<String,String[]> telecomErrCodes = new HashMap<String,String[]>();
	
	static {
		//手机号码,号段类型区分
		//移动
		numberType.put("134","1");
		numberType.put("135","1");
		numberType.put("136","1");
		numberType.put("137","1");
		numberType.put("138","1");
		numberType.put("139","1");
		numberType.put("150","1");
		numberType.put("151","1");
		numberType.put("152","1");
		numberType.put("157","1");
		numberType.put("158","1");
		numberType.put("159","1");
		numberType.put("182","1");
		numberType.put("187","1");
		numberType.put("188","1");
		numberType.put("147","1");
		//联通
		numberType.put("130","2");
		numberType.put("131","2");
		numberType.put("132","2");
		numberType.put("155","2");
		numberType.put("156","2");
		numberType.put("186","2");
		numberType.put("145","2");
		//电信
		numberType.put("133","3");
		numberType.put("153","3");
		numberType.put("189","3");
		
		//移动错误代码初始化
		mobileErrCodes.put("CB:0054",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("CB:0055",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("CB:0056",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("CB:0057",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("CB:0061",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("CB:0062",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("CB:0063",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("CB:0064",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("CB:0065",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("CB:0066",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("CB:0067",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("CB:0068",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("CB:0001",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("CB:0005",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("CB:0007",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("CB:0008",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("CB:0009",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("CB:0010",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("CB:0011",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("CB:0047",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("CB:0052",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("CB:0053",new String[]{"","收信人手机出现故障!"});
		
		mobileErrCodes.put("MB:0017",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("MB:0024",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("MI:xxxx",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("MK:xxxx",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("MK:0015",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("EXPIRED",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("UNDELIV",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("UNKNOWN",new String[]{"","收信人手机出现故障!"});
		
		mobileErrCodes.put("DB:0100",new String[]{"","手机号码不存在!"});
		mobileErrCodes.put("DB:0101",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("DB:0102",new String[]{"","收信人手机停机!"});
		mobileErrCodes.put("DB:0103",new String[]{"","收信人手机已欠费!"});
		mobileErrCodes.put("DB:0104",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("DB:0115",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("DB:0116",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("DB:0117",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("DB:0118",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("DB:0119",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("DB:0126",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("DB:0127",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("DB:0128",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("DB:0129",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("DB:0130",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("DB:0131",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("DB:0139",new String[]{"","手机号为黑名单号码!"});
		mobileErrCodes.put("DB:0140",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("DB:0141",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("DB:0150",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("DB:0151",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("DB:0160",new String[]{"","收信人手机出现故障!"});
		mobileErrCodes.put("ID:XXXX",new String[]{"","收信人手机出现故障!"});
		
		/*
		mobileErrCodes.put("CB:XXXX",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0002",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0003",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0004",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0006",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0014",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0015",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0016",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0017",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0018",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0020",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0021",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0022",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0023",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0024",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0025",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0026",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0040",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0041",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0042",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0043",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0044",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0045",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("CB:0046",new String[]{"","移动通信运营商通道出现故障!"});
		
		mobileErrCodes.put("MA:XXXX",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("MB:XXXX",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("MB:0066",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("Mx：XXXX",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("MI:0000",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("MN:xxxx",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("IA:XXXX",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("IB:XXXX",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("IC:XXXX",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("ID:XXXX",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("INVALID",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DELETED",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("REJECTD",new String[]{"","移动通信运营商通道出现故障!"});
		
		mobileErrCodes.put("DB：XXXX",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:0105",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:0106",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:0107",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:0108",new String[]{"","移动通信运营商通道出现故障!"});
		
		mobileErrCodes.put("DB:0109",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:0110",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:0111",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:0112",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:0113",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:0114",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:0120",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:0121",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:0122",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:0123",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:0124",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:0132",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:0133",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:0134",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:0135",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:0136",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:0137",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:0138",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:0152",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("MC:xxxx",new String[]{"","移动通信运营商通道出现故障!"});
		
		mobileErrCodes.put("DA:0054",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DA:0070",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DA:0084",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:9001",new String[]{"","移动通信运营商通道出现故障!"});
		mobileErrCodes.put("DB:9007",new String[]{"","移动通信运营商通道出现故障!"});
		*/
		
		//联通错误代码初始化
		/*
		linkErrCodes.put("7",new String[]{"","联通通信运营商通道出现故障!"});
		linkErrCodes.put("10",new String[]{"","联通通信运营商通道出现故障!"});
		linkErrCodes.put("11",new String[]{"","联通通信运营商通道出现故障!"});
		linkErrCodes.put("21",new String[]{"","联通通信运营商通道出现故障!"});
		linkErrCodes.put("22",new String[]{"","联通通信运营商通道出现故障!"});
		linkErrCodes.put("23",new String[]{"","联通通信运营商通道出现故障!"});
		linkErrCodes.put("24",new String[]{"","联通通信运营商通道出现故障!"});
		linkErrCodes.put("32",new String[]{"","联通通信运营商通道出现故障!"});
		linkErrCodes.put("33",new String[]{"","联通通信运营商通道出现故障!"});
		linkErrCodes.put("其它 ",new String[]{"","联通通信运营商通道出现故障!"});
		linkErrCodes.put("61",new String[]{"","联通通信运营商通道出现故障!"});
		linkErrCodes.put("64",new String[]{"","联通通信运营商通道出现故障!"});
		*/
		linkErrCodes.put("6",new String[]{"","手机号错误!"});
		linkErrCodes.put("25",new String[]{"","收信人手机未开机或不在服务区!"});
		linkErrCodes.put("26",new String[]{"","收信人手机出现故障!"});
		linkErrCodes.put("27",new String[]{"","收信人手机出现故障!"});
		linkErrCodes.put("28",new String[]{"","收信人手机出现故障!"});
		linkErrCodes.put("29",new String[]{"","收信人手机出现故障!"});
		linkErrCodes.put("30",new String[]{"","收信人手机出现故障!"});
		linkErrCodes.put("31",new String[]{"","收信人手机出现故障!"});
		linkErrCodes.put("67",new String[]{"","手机号为黑名单号码!"});
		linkErrCodes.put("97",new String[]{"","手机号为黑名单号码!"});
		linkErrCodes.put("51",new String[]{"","手机号为黑名单号码!"});
		linkErrCodes.put("79",new String[]{"","手机号为黑名单号码!"});
		linkErrCodes.put("90",new String[]{"","手机号为黑名单号码!"});
		linkErrCodes.put("无状态报告",new String[]{"","手机号为黑名单号码!"});
		linkErrCodes.put("24",new String[]{"","手机号为黑名单号码!"});
		/*
		linkErrCodes.put("1",new String[]{"","联通通信运营商通道出现故障!"});
		linkErrCodes.put("2",new String[]{"","联通通信运营商通道出现故障!"});
		linkErrCodes.put("3",new String[]{"","联通通信运营商通道出现故障!"});
		linkErrCodes.put("4",new String[]{"","联通通信运营商通道出现故障!"});
		linkErrCodes.put("5",new String[]{"","联通通信运营商通道出现故障!"});
		linkErrCodes.put("6",new String[]{"","联通通信运营商通道出现故障!"});
		linkErrCodes.put("7",new String[]{"","联通通信运营商通道出现故障!"});
		linkErrCodes.put("8",new String[]{"","联通通信运营商通道出现故障!"});
		linkErrCodes.put("9",new String[]{"","联通通信运营商通道出现故障!"});
		linkErrCodes.put("69",new String[]{"","联通通信运营商通道出现故障!"});
		linkErrCodes.put("10",new String[]{"","联通通信运营商通道出现故障!"});
		linkErrCodes.put("0",new String[]{"","联通通信运营商通道出现故障!"});
		*/
	
		//电信错误代码初始化
		/*
		telecomErrCodes.put("110",new String[]{"","电信通信运营商通道出现故障!"});
		telecomErrCodes.put("101",new String[]{"","电信通信运营商通道出现故障!"});
		telecomErrCodes.put("104",new String[]{"","电信通信运营商通道出现故障!"});
		telecomErrCodes.put("112",new String[]{"","电信通信运营商通道出现故障!"});
		telecomErrCodes.put("113",new String[]{"","电信通信运营商通道出现故障!"});
		telecomErrCodes.put("115",new String[]{"","电信通信运营商通道出现故障!"});
		telecomErrCodes.put("116",new String[]{"","电信通信运营商通道出现故障!"});
		telecomErrCodes.put("117",new String[]{"","电信通信运营商通道出现故障!"});
		telecomErrCodes.put("EXPIRED",new String[]{"","电信通信运营商通道出现故障!"});
		telecomErrCodes.put("DELETED",new String[]{"","电信通信运营商通道出现故障!"});
		telecomErrCodes.put("UNKNOWN",new String[]{"","电信通信运营商通道出现故障!"});
		telecomErrCodes.put("REJECTD",new String[]{"","电信通信运营商通道出现故障!"});
		telecomErrCodes.put("114",new String[]{"","电信通信运营商通道出现故障!"});
		*/
		telecomErrCodes.put("109",new String[]{"","错误的手机号!"});
		telecomErrCodes.put("UNDELIV",new String[]{"","收信人手机出现故障!"});
		telecomErrCodes.put("DTBLACK",new String[]{"","手机号为黑名单号码!"});
		telecomErrCodes.put("106",new String[]{"","短信含敏感内容!"});
		/*
		telecomErrCodes.put("102",new String[]{"","电信通信运营商通道出现故障!"});
		telecomErrCodes.put("103",new String[]{"","电信通信运营商通道出现故障!"});
		telecomErrCodes.put("105",new String[]{"","电信通信运营商通道出现故障!"});
		telecomErrCodes.put("107",new String[]{"","电信通信运营商通道出现故障!"});
		telecomErrCodes.put("108",new String[]{"","电信通信运营商通道出现故障!"});
		
		telecomErrCodes.put("111",new String[]{"","电信通信运营商通道出现故障!"});
		telecomErrCodes.put("119",new String[]{"","电信通信运营商通道出现故障!"});
		telecomErrCodes.put("VMFLUCTL",new String[]{"","电信通信运营商通道出现故障!"});
		*/
		telecomErrCodes.put("0",new String[]{"",""});
		telecomErrCodes.put("DELIVRD",new String[]{"",""});
		telecomErrCodes.put("ACCEPTD",new String[]{"",""});
	}
	
	public static String errCodeSwitch(String mobile,String errCode) {
		if(mobile==null||mobile.length()<11) {
			return  "无效的手机号!";
		}
		String numberHead = mobile.substring(0, 3);
		String type = numberType.get(numberHead);
		String codeMsg = "";
		String[] msgs = null;
		if("1".equals(type)) {
			msgs = mobileErrCodes.get(errCode);
			if(msgs!=null) {
				codeMsg = msgs[1];
			}else {
				codeMsg = "移动通信运营商通道出现故障!";
			}
		}else if("2".equals(type)) {
			msgs = linkErrCodes.get(errCode);
			if(msgs!=null) {
				codeMsg = msgs[1];
			}else {
				codeMsg = "联通通信运营商通道出现故障!";
			}
		}else if("3".equals(type)) {
			msgs = telecomErrCodes.get(errCode);
			if(msgs!=null) {
				codeMsg = msgs[1];
			}else {
				codeMsg = "电信通信运营商通道出现故障!";
			}
		}else {
			//非三大运营商商手机号码,应该为错误的手机号码
			codeMsg = "通信运营商通道出现故障!";
		}
		return codeMsg;
	}
}
