/*
Copyright (c) 2003-2011, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.editorConfig = function( config )
{
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	//config.language = 'zh-cn'; // 配置语言
	//config.uiColor = '#FFF'; // 背景颜色 
	//config.width = 'auto'; // 宽度   
	//config.height = '300px'; // 高度 
	//config.skin = 'office2003';//界面v2,kama,office2003 
	config.toolbar = 'Full';// 工具栏风格Full,Basic   

	//config.toolbar = 'MyToolbar' ; 
	config.toolbar_MyToolbar = 
	[ 
		[ 'Source' , 'Preview' ], 
		[ 'Cut' , 'Copy' , 'Paste' , 'PasteText' , 'PasteFromWord' , '-' , 'Scayt' ],
		[ 'Undo' , 'Redo' , '-' , 'Find' , 'Replace' , '-' , 'SelectAll' , 'RemoveFormat' ],
		[ 'Image' , 'Table' , 'HorizontalRule' , 'Smiley' , 'SpecialChar' , 'PageBreak' ],
		'/' , 
		[ 'Styles' , 'Format' ], 
		[ 'Bold' , 'Italic' , 'Strike' ], 
		[ 'TextColor','BGColor' ],
		[ 'NumberedList' , 'BulletedList' , '-' , 'Outdent' , 'Indent' , 'Blockquote' ],
		[ 'Link' , 'Unlink' , 'Anchor' ]
		//[ 'Maximize' , '-' , 'About' ] 
	];
	
	//配置CKFinder  
//	config.filebrowserBrowseUrl ='js/ckfinder/ckfinder.html';  
//	config.filebrowserImageBrowseUrl ='js/ckfinder/ckfinder.html?Type=Images';  
//	config.filebrowserFlashBrowseUrl = 'js/ckfinder/ckfinder.html?Type=Flash';  
//	config.filebrowserUploadUrl = 'js/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Files';  
//	config.filebrowserImageUploadUrl = 'js/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Image';  
//	config.filebrowserFlashUploadUrl = 'js/ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Flash';  
//	config.filebrowserWindowHeight='50%';//CKFinder浏览窗口高度,默认值70%，也可以赋像素值如：1000  
//	config.filebrowserWindowWidth='70%';//CKFinder浏览窗口宽度,默认值80%，也可以赋像素值 
	
	config.filebrowserUploadUrl = 'uploadFile.action?companyId=0&category=article';  
	config.filebrowserImageUploadUrl = 'uploadFile.action?companyId=0&category=article';  
	config.filebrowserFlashUploadUrl = 'uploadFile.action?companyId=0&category=article'; 
	
};
