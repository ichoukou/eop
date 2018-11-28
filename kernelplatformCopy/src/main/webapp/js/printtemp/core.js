function $(el) {
	if (arguments.length > 1) {
		for (var i = 0, els = [], length = arguments.length; i < length;i=i+1) {
			els.push($(arguments[i]));
		}
		return els;
	}
	if (typeof el == "string") {
		el = $j('#'+el).get(0);
	}
	if ((typeof Prototype=='undefined') || (typeof Element == 'undefined') || (typeof Element.Methods=='undefined')) {
		return el;
	} else {
		return Element.extend(el);
	}
}

function trim(str) {
    return str===null?null:str.trim();
} 

String.prototype.trim = function()
{
	return this.replace(/(^\s*)|(\s*$)/g, "");
};

Array.prototype.remove=function(index)
 {
   if(isNaN(index)||index>this.length){return false;}
   for(var i=0,n=0;i<this.length;i=i+1)
   {
       if(this[i]!=this[index])
       {
           this[n]=this[i];
		   n = n +1;
       }
   }
   this.length-=1;
 };
 
 Array.prototype.indexOf=function(value)
 {
   
   for(var i=0;i<this.length;i=i+1)
   {
 		if(this[i]==value){
 			return i;
 		}
   }
   return -1;
 };
 
  Array.prototype.toStringUsingDel=function(del)
 {
   var str = ""; 
   for(var i=0;i<this.length;i=i+1)
   {
		str += this[i]+ del;
   }
   if(this.length==0){
 	  return str;
   }else{
   	  return str.substring(0,str.length-del.length)
   }	
   
 };
 

//取到当前的事件对象,同时兼容ie和ff的写法 
function getEvent(){   
    if (document.all) {
    	return window.event;
    }         
    var func=getEvent.caller;             
    while(func!=null){     
        var arg0=func.arguments[0]; 
        if(arg0){ 
            if((arg0.constructor==Event || arg0.constructor ==MouseEvent) 
                || (typeof(arg0)=="object" && arg0.preventDefault && arg0.stopPropagation)){     
               return arg0; 
            } 
        } 
        func=func.caller; 
    } 
    return null; 
}
/**
	取到当前事件的源对象
**/
function getEventSourceObject(){
	var evt=getEvent();
    return evt.srcElement || evt.target;
}


/*Cookie管理注意事项：Cookie会被发到服务器因而占用带宽，要提高性能就要减少Cookie的消耗，有以下原则：
 *原则1：尽量不使用Cookie，而且一般也不应有非全局的Cookie，还需要限制Cookie的path的范围
 *原则2：尽量在服务器端处理Cookie而不是客户端，客户端只读
 *原则3：Cookie必须尽量简短，且不能存储敏感数据
 *原则4：尽量集中管理全局Cookie（如在一个隐藏Frame处理；统一不设置path，因为隐藏页面只能读本身URL匹配的path的Cookie，但需采用异步方式，较麻烦－－Cookie的读取和进一步处理一般是在onload里面，或onclick等事件，以保证这时隐藏frame已经准备好）；
 */
function setCookie(name,value,expires,path,domain,secure) {
	var pathString;
	if(path == undefined || path == null){
		pathString = "";
	}else if(path == ""){
		pathString = "; path=" +"/";
	}else{
		pathString = "; path=" +path;
	}
	
  document.cookie = name + "=" + escape (value) +
    ((expires) ? "; expires=" + expires.toGMTString() : "") +
    pathString +
    ((domain) ? "; domain=" + domain : "") + ((secure) ? "; secure" : "");
}
function getCookie(name) {
	var prefix = name + "=" ;
	var start = document.cookie.indexOf(prefix); 
	if (start==-1) {
		return null;
	}
	start+=prefix.length;
	var end = document.cookie.indexOf(";", start) ;
	if (end==-1) {
		end=document.cookie.length;
	}
	return unescape(document.cookie.substring(start, end));
}
//删除名称为name的Cookie
function deleteCookie (name,path) 
{   
    var exp = new Date();
    exp.setTime (exp.getTime() - 1);
    var cval = getCookie (name);
    document.cookie = name + "=" + cval + "; expires=" + exp.toGMTString()+((path) ? "; path=" + path : "");
}



//获取元素的绝对位置
function getElementPos(obj) {
		var ua = navigator.userAgent.toLowerCase();
		var isOpera = (ua.indexOf('opera') != -1);
		var isIE = (ua.indexOf('msie') != -1 && !isOpera); // not opera spoof
	
		var el = obj;
	
		if(el.parentNode == null || el.style.display == 'none') 
		{
			return false;
		}
	
		var parent = null;
		var pos = [];
		var box;
	
		if(el.getBoundingClientRect)	//IE
		{
			box = el.getBoundingClientRect();
			var scrollTop = Math.max(document.documentElement.scrollTop, document.body.scrollTop);
			var scrollLeft = Math.max(document.documentElement.scrollLeft, document.body.scrollLeft);
	
			return {x:box.left + scrollLeft, y:box.top + scrollTop};
		}
		else if(document.getBoxObjectFor)	// gecko
		{
			box = document.getBoxObjectFor(el);
			   
			var borderLeft = (el.style.borderLeftWidth)?parseInt(el.style.borderLeftWidth):0;
			var borderTop = (el.style.borderTopWidth)?parseInt(el.style.borderTopWidth):0;
	
			pos = [box.x - borderLeft, box.y - borderTop];
		}
		else	// safari & opera
		{
			pos = [el.offsetLeft, el.offsetTop];
			parent = el.offsetParent;
			if (parent != el) {
				while (parent) {
					pos[0] += parent.offsetLeft;
					pos[1] += parent.offsetTop;
					parent = parent.offsetParent;
				}
			}
			if (ua.indexOf('opera') != -1 
				|| ( ua.indexOf('safari') != -1 && el.style.position == 'absolute' )) 
			{
					pos[0] -= document.body.offsetLeft;
					pos[1] -= document.body.offsetTop;
			} 
		}
			
		if (el.parentNode) { parent = el.parentNode; }
		else { parent = null; }
	  
		while (parent && parent.tagName != 'BODY' && parent.tagName != 'HTML') 
		{ // account for any scrolled ancestors
			pos[0] -= parent.scrollLeft;
			pos[1] -= parent.scrollTop;
	  
			if (parent.parentNode) { parent = parent.parentNode; } 
			else { parent = null; }
		}
		return {x:pos[0], y:pos[1]};
}
 
function isIE()
{
	return (navigator.userAgent.toLowerCase().indexOf("msie") != -1);
}

function _isLoaded(url) {
	var ss = document.getElementsByTagName(url.toLowerCase().lastIndexOf(".css")>0?"link":"script");
	for (i = 0; i < ss.length; i++) {
		var _url=ss.href||ss[i].src||ss[i].syncUrl;
		if (_url && _url.indexOf(url) != -1) {
			return true;
		}
	}
	return false;
}

/**用同步方式载入JS（CSS不需要），慢网络会有停顿*/
function $importSync(url, defer) {
	if (_isLoaded(url)) {
		return;
	}
	ajaxCall(url.indexOf(__ctxPath)==0?url:(__ctxPath+url), null, "GET", false, function(responseText){
		if (responseText != null) {
			var oHead = document.getElementsByTagName("head")[0];
			var oScript = document.createElement("script");
			oScript.type = "text/javascript";
			oScript.defer = defer || true;
			oScript.text = responseText;
			oScript.syncUrl=url;
			oHead.appendChild(oScript);
		}
	},function(){
		alert("Failed to load script: "+url);
	});
}
function JsCssLoader() {
	this.load = function (url) {
		if (_isLoaded(url)) {
			this.onsuccess();
			return;
		}
        //动态创建script/link结点装入外联的.js/.css文件
		if (url.toLowerCase().lastIndexOf(".css")>0) {
			var s=document.createElement("link");
			s.setAttribute("rel", "stylesheet");
			s.setAttribute("type", "text/css");
			s.setAttribute("href",url);
		} else {
			var s = document.createElement("script");
			s.type = "text/javascript";
			s.src = url;
		}
        //获取head结点，并将script/link插入到其中
		var head = document.getElementsByTagName("head")[0];
		head.appendChild(s);
           
        //保存自身以便在事件中引用
		var self = this;
        //使用readystatechange(IE)或onload(FF等)事件在载入成功后进行处理
		s.onload = s.onreadystatechange = function () {
            //在此函数中this指针指的是s结点对象，而不是JsCssLoader实例,
            //所以必须用self来调用onsuccess事件，下同。
			if (s.readyState && s.readyState == "loading") {
				return;
			}
			self.onsuccess(url);
		};
		s.onerror = function () {
			head.removeChild(s);
			self.onfailure(url,arguments);
		};
	};
    //定义载入成功事件
	this.onsuccess = function (url) {
	};
	//定义失败事件
	this.onfailure = function (url,args) {
		alert("Error loading JavaScript/StyleSheet: "+args[0]+" -- "+args[1]+" -- "+args[2]);
	};
}

/**动态载入脚本并执行callback。如果没有callback，一般只适合不需要等待就可继续的情况。已知问题：FF下CSS没有事件处理。IE下ext事件处理部分必须先装入。*/
function $import(urls,callback) {
	if (!(urls instanceof Array)) {
		alert("Invalid $import call syntax, must be array.");
		return;
	}
	var _loader=new JsCssLoader();
	if (urls.length>1) {
		_loader.onsuccess=function() {
			$import(urls.slice(1,urls.length),callback);
		}
	} else if (callback) {
		_loader.onsuccess=callback;
	}
	//这里永远只是载入数组里的第一个，因为其他的会在callback处理
	_loader.load(urls[0].indexOf(__ctxPath)==0?urls[0]:(__ctxPath+urls[0]));
}
function $importDwr(callback) {
	$import(["/scripts/dwr/engine.min.js","/scripts/dwr/util.pack.js"],callback);
}



