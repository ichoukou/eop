var regexEnumBeta = 
{
	intege:"^-?[1-9]\\d*$",					//整数
	intege1:"^[1-9]\\d*$",					//正整数
	intege2:"^-[1-9]\\d*$",					//负整数
	num:"^([+-]?)\\d*\\.?\\d+$",			//数字
	num1:"^[1-9]\\d*|0$",					//正数（正整数 + 0）
	num2:"^-[1-9]\\d*|0$",					//负数（负整数 + 0）
	decmal:"^([+-]?)\\d*\\.\\d+$",			//浮点数
	decmal1:"^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*$",　　	//正浮点数
	decmal2:"^-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*)$",　 //负浮点数
	decmal3:"^-?([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0)$",　 //浮点数
	decmal4:"^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0$",　　 //非负浮点数（正浮点数 + 0）
	decmal5:"^(-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*))|0?.0+|0$",　　//非正浮点数（负浮点数 + 0）
	//email:"^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$", //邮件
	email:"^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.(com|cn|mobi|so|net|org|name|me|co|tel|info|biz|cc|tv|hk|asia)$",									//枚举邮箱
	color:"^[a-fA-F0-9]{6}$",				//颜色
	url:"^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$",	//url
	chinese:"^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$",					//仅中文
	ascii:"^[\\x00-\\xFF]+$",				//仅ACSII字符
	zipcode:"^\\d{6}$",						//邮编
	mobile:"^13[0-9]{9}|15[012356789][0-9]{8}|18[0256789][0-9]{8}|147[0-9]{8}$",				//手机
	ip4:"^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)$",	//ip地址
	notempty:"^\\S+$",						//非空
	picture:"(.*)\\.(jpg|bmp|gif|ico|pcx|jpeg|tif|png|raw|tga)$",	//图片
	rar:"(.*)\\.(rar|zip|7zip|tgz)$",								//压缩文件
	date:"^\\d{4}(\\-|\\/|\.)\\d{1,2}\\1\\d{1,2}$",					//日期
	qq:"^[1-9]*[1-9][0-9]*$",				//QQ号码
	tel:"^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$",	//电话号码的函数(包括验证国内区号,国际区号,分机号)
	username:"^\\w+$",						//用来用户注册。匹配由数字、26个英文字母或者下划线组成的字符串
	letter:"^[A-Za-z]+$",					//字母
	letter_u:"^[A-Z]+$",					//大写字母
	letter_l:"^[a-z]+$",					//小写字母
	idcard:"^[1-9]([0-9]{14}|[0-9]{17})$",	//身份证

	/**
	 * 补充验证方法
	 * @author 朱一成
	**/
	//accountId: "^[a-zA-Z0-9\\u4E00-\\u9FA5\\uF900-\\uFA2D]{3,20}$",		// 用户名，中文、英文字母、数字组合，3-20个字符。
	accountId: "^.{1,100}$",												// 用户名，任意字符，3-20个字符（from 余建江）
	//shipmentNum: "^[A-Za-z0-9]{1}\\d{9}$",								// 运单号，1位数字或字母开头，后面9位数字
	shipmentNum: "^(0|1|2|3|4|5|6|7|8|9|E|D|F|G|V|W|e|d|f|g|v|w)[0-9]{9}$",	// 运单号（from 李隆勇）
	orderNum: "^\\d{0,20}$",											// 订单号
	password: "^[^\\u4E00-\\u9FA5\\uF900-\\uFA2D]{6,16}$",				// 密码
	name: "^[a-zA-Z\\u4E00-\\u9FA5\\uF900-\\uFA2D]{2,12}$",				// 姓名
	customerCode: "^(K|YT)[A-Za-z0-9]{8,14}$",							// 客户编码
	empty: "^$"															// 空
}

/**
 * 自定义验证方法
 * @author 朱一成
**/

// 智能查件查询类型
var dataType = {
	// 运单组合
	isGroup: function(val) {
		if (val.indexOf('/') != -1) {
			return true;
		} else if (val.indexOf('\n') != -1) {
			return true;
		} else if (val.indexOf(' ') != -1) {
			return true;
		} else {
			return false;
		}
	},
	// 手机号
	isMobile: function(val) {
		// 数字1开头，后面10位数字
		return /^1\d{10}$/.test(val);
	},
	// 固定电话
	isTel: function(val) {
		// 数字0开头，后面10-15位数字
		return /^0\d{10,15}$/.test(val);
	},
	// 运单号
	isShipNum: function(val) {
		// 1位数字或字母开头，后面9位数字
		return /^[A-Za-z0-9]{1}\d{9}$/.test(val);
	},
	// 姓名
	isName: function(val) {
		// 中文、字母组合，2-12个字符
		return /^[a-zA-Z\u4E00-\u9FA5\uF900-\uFA2D]{2,12}$/.test(val);
	}
};

// 验证智能查件
function validateSearch(val, el) {
	var val = el.value = $.trim(val),
		defaultText = '请输入买家姓名/买家电话/运单号，查多号时用斜杠"/"分隔' ||
						'请输入买家姓名/买家电话/运单号' || 
						'输入运单号/手机号码/买家姓名，查询近7天的订单' || '',		// 输入框默认值
		output;		// 返回结果
	
	if (val == defaultText) {					// 如果是默认值
		output = true;
	} else if (dataType.isGroup(val)) {					// 如果是运单组合
		if (val.substr(-1, 1) == '/') {		// 如果最后一个字符是 "/"
			// 移除最后一个 "/"
			val = val.substring(0, val.length-1);
		}
		// 拆分成数组
		var arr = [];
		val = val.ReplaceAll('\n', '@@@');
		val = val.ReplaceAll('/', '@@@');
		val = val.ReplaceAll(' ', '@@@');
		arr = val.split('@@@');
		
		for (var i=0, len = arr.length; i<len; i++) {
			if (!dataType.isShipNum(arr[i])) {
				
				output = false;
			}
		}
		
	} else {
		if (
			dataType.isMobile(val) ||
			dataType.isShipNum(val) ||
			dataType.isTel(val) ||
			dataType.isName(val)
		) {
			output = true;
		} else {
			output = false;
		}
	}
	
	
	return output;
}

// 验证重量
function validateWeight(val, el) {
	// 格式化重量
	var formatVal = parseFloat(val, 10).toFixed(2),
		outputVal = el.value = formatVal;
	if (outputVal >= 0 && outputVal <= 50) {
		return true;
	} else {
		return false;
	}
}

// 验证价格
function validatePrice(val, el) {
	// 格式化价格
	var formatPrice = parseFloat(val, 10).toFixed(2),
		outputPrice = el.value = formatPrice;
		
	if (outputPrice > 0) {
		return true;
	} else {
		return false;
	}
}

String.prototype.ReplaceAll = stringReplaceAll;

function  stringReplaceAll(AFindText,ARepText){
  raRegExp = new RegExp(AFindText,"g")
  return this.replace(raRegExp,ARepText)
}