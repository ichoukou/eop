/**
 * 易通公共弹窗组件
 * 使用方法：var firstDialog = new Dialog(); firstDialog.init(options);
 * @param options {Object} 参数对象
 * @param options.contentHtml {String} 弹窗内容，支持 HTML（必填）
 * @param options.iconType {String} icon 类型，可选值：success / question / warn（选填）
 * @param options.yesVal {String} 第一个按钮的文字，默认值：确 定（选填）
 * @param options.noVal {String} 第二个按钮的文字，默认值：取 消（选填）
 * @param options.yes {Function} 第一个按钮的回调函数，无默认值（选填）
 * @param options.no {Function} 第二个按钮的回调函数，无默认值（选填）
 * @param options.closeBtn {Boolean} 是否显示关闭按钮，默认值：不显示（选填）
 * @param options.maskOpacity {Float} 遮罩层透明度，默认值：0（选填）
 * @param options.maskColor {String} 遮罩层背景颜色，默认值：#fff（白色）（选填）
 * @param options.autoClose {Number} 自动关闭时间，单位：毫秒，默认值：0/不自动关闭（选填）
 * @author 朱一成 i@wange.im
**/

function Dialog() {
	// 默认配置
	this.defConfig = {
		iconType: '',
		yesVal: '确 定',
		noVal: '取 消',
		closeBtn: false,
		maskOpacity: 0,
		maskColor: '#fff',
		autoClose: 0
	}
}

Dialog.prototype = {
	// 缓存弹窗用到的元素
	_setEls: function() {
		this.els = {
			bodyTag: $('body'),
			wrapperId: $('#dialog'),
			maskId:  $('#dialog_mask'),
			closeId:  $('#dialog_close'),
			yesBtnId:  $('#dialog_ft .btn_d'),
			noBtnId:  $('#dialog_ft .btn_e')
		}
	},

	/**
	 * 注入弹层
	 * @param options {Object} 参数对象
	**/
	_buildDialog: function(options) {
		var _this = this,
			_thisEls = _this.els,
			_thisDef = _this.defConfig;
		
		var closeVal = '<a href="javascript:;" id="dialog_close" class="png" title="关闭">关闭</a>',	// 关闭按钮 html
			defCloseVal = '',	// 关闭按钮默认的 html
			
			iconType = '<i class="dialog_icon dialog_icon_' + options.iconType + '"></i>',	// icon html
			defIconType = '';	// icon 默认 html
		
		// 如果 closeBtn 默认值是 true
		if (_thisDef.closeBtn) {
			// 那默认的关闭按钮 html 就是关闭按钮
			defCloseVal = closeVal;
		}
		
		// 如果 iconType 默认值不为空
		if (_thisDef.iconType !== '') {
			// 那默认的 iconType 要加上 html
			defIconType = '<i class="dialog_icon dialog_icon_' + _thisDef.iconType + '"></i>';
		}
		
		var maskColor = (options.maskColor) ? options.maskColor.replace('#','') : _thisDef.maskColor.replace('#',''),
		    src = ($.browser.msie && parseInt($.browser.version) < 9) 
					? '.../../js/module/select/mask.html#' + maskColor
					: '';
					
		var contentHtml = options.contentHtml,
			iconImg = options.iconType ? iconType : defIconType,
			yesVal = options.yesVal ? options.yesVal : _thisDef.yesVal,
			noVal = options.noVal ? options.noVal : _thisDef.noVal,
			yesBtn = options.yes ? '<a href="javascript:;" class="btn btn_d" title="' + yesVal + '"><span>' + yesVal + '</span></a>' : '',
			noBtn = options.no ? '<a href="javascript:;" class="btn btn_e" title="' + noVal + '"><span>' + noVal + '</span></a>' : '',
			closeBtn = options.closeBtn ? closeVal : defCloseVal,
			dialogHtml = '<div id="dialog">' +
						 '	<div id="dialog_bd">' +
								iconImg +
								contentHtml +
						 '	</div>' +
						 '	<div id="dialog_ft">' +
								yesBtn +
								noBtn +
								closeBtn +
						 '	</div>' +
						 '</div>',
			maskHtml = '<iframe id="dialog_mask" src="' + src + '"></iframe>';
		
		$('body').prepend(maskHtml).append(dialogHtml);
		
		_this._setEls();

		return null;
	},
	
	/**
	 * 按钮回调
	 * @param yes {Function} “确认”的回调函数
	 * @param no {Function} “取消”的回调函数
	**/
	_callbackDialog: function(yes, no) {
		var _thisEls = this.els;
		if (yes) {
			_thisEls.yesBtnId.bind('click', function() {
				yes();
			});
		}
		
		if (no) {
			_thisEls.noBtnId.bind('click', function() {
				no();
			});
		}
		
		return null;
	},
	
	/**
	 * 生成弹层
	**/
	build: function(options) {
		var _this = this,
		    _thisDef = _this.defConfig;
			
		// 先注入弹窗
		_this._buildDialog(options);
		
		var _thisEls = _this.els,
			winEl = $(window),
			posLeft = (winEl.outerWidth() - _thisEls.wrapperId.outerWidth()) / 2,
			posTop = (winEl.outerHeight() - _thisEls.wrapperId.outerHeight()) / 2,
			maskOpacity = options.maskOpacity ? options.maskOpacity : _thisDef.maskOpacity,
			maskColor = options.maskColor ? options.maskColor : _thisDef.maskColor;
			
		// 解决 ie6 fixed bug
		if ($.browser.msie && ($.browser.version == '6.0') && !$.support.style) {
			_thisEls.wrapperId.PositionFixed({
				x: posLeft,
				y: posTop
			});
			 
		} else {
			_thisEls.wrapperId.css({
				left: posLeft,
				top: posTop
			});
		}
		
		// 设置弹窗和遮罩层的样式
		_thisEls.wrapperId.css({
			display: 'block'
		});
		
		_thisEls.maskId.css({
			width: _thisEls.bodyTag.outerWidth(true),
			height: _thisEls.bodyTag.outerHeight(true),
			opacity: maskOpacity,
			backgroundColor: maskColor
		});
		
		// 绑定 “确定” 和 “取消” 的回调
		_this._callbackDialog(options.yes, options.no);
		
		// 绑定关闭按钮的点击事件
		_thisEls.closeId.bind('click', function() {
			_this.close();
		});
		
		return null;
	},
	
	/**
	 * 关闭弹层
	**/
	close: function() {
		var _thisEls = this.els;
		// 如果有弹层和遮罩
		if (_thisEls.maskId && _thisEls.wrapperId) {
			// 按钮解绑
			_thisEls.yesBtnId.unbind('click');
			_thisEls.noBtnId.unbind('click');
			_thisEls.closeId.unbind('click');
			
			// 删除弹层和遮罩
			_thisEls.maskId.remove();
			_thisEls.wrapperId.remove();
		}
		
		return null;
	},
	
	/**
	 * 初始化
	 * @param options {Object} 参数对象
	**/
	init: function(options) {
		var _this = this,
			autoClose = options.autoClose || _this.defConfig.autoClose;
			
		_this.build(options);

		// 如果设置了自动关闭时间
		if (autoClose > 0) {
			setTimeout(function() {
				_this.close();
			}, autoClose);
		}
		
		return null;
	}
}