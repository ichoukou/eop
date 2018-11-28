-- 用户的权限信息
-- 父节点
INSERT INTO `ec_core_configcode` (id, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (93, NULL, 'permission_admin', 'permission_admin', '管理员的action权限', '2', 1, '', 0);
INSERT INTO `ec_core_configcode` (id, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (94, NULL, 'permission_wangdian', 'permission_wangdian', '网点的action权限', '2', 1, '', 0);
INSERT INTO `ec_core_configcode` (id, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (95, NULL, 'permission_wangdian_kefu', 'permission_wangdian_kefu', '网点客服的action权限', '2', 1, '', 0);
INSERT INTO `ec_core_configcode` (id, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (96, NULL, 'permission_wangdian_caiwu', 'permission_wangdian_caiwu', '网点财务的action权限', '2', 1, '', 0);
INSERT INTO `ec_core_configcode` (id, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (97, NULL, 'permission_maijia', 'permission_maijia', '卖家的action权限', '2', 1, '', 0);
INSERT INTO `ec_core_configcode` (id, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (98, NULL, 'permission_maijia_kefu', 'permission_maijia_kefu', '卖家客服的action权限', '2', 1, '', 0);
INSERT INTO `ec_core_configcode` (id, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (99, NULL, 'permission_maijia_caiwu', 'permission_maijia_caiwu', '卖家财务的action权限', '2', 1, '', 0);
INSERT INTO `ec_core_configcode` (id, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (100, NULL, 'permission_pingtai', 'permission_pingtai', '平台用户的action权限', '2', 1, '', 0);
	
-- 子节点/管理员
insert into `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
values('174','93','admin.net.ytoec.kernel.action.login.MainPageAction','net.ytoec.kernel.action.login.MainPageAction','后台主页','1','1','管理员的action权限','0');
insert into `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`)
values('175','93','admin.net.ytoec.kernel.action.user.UserAction','net.ytoec.kernel.action.user.UserAction','用户','1','1','管理员的action权限','0');
insert into `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
values('176','93','admin.net.ytoec.kernel.action.leavemessage.LeaveMessageAction','net.ytoec.kernel.action.leavemessage.LeaveMessageAction','留言管理','1','1','管理员的action权限','0');
insert into `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
values('177','93','admin.net.ytoec.kernel.action.leavemessage.SendMessageAction','net.ytoec.kernel.action.leavemessage.SendMessageAction','发送留言','1','1','管理员的action权限','0');
insert into `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
values('178','93','admin.net.ytoec.kernel.action.questionnaire.QuestionnaireMonitorAction','net.ytoec.kernel.action.questionnaire.QuestionnaireMonitorAction','卖家对运单监控管理','1','1','管理员的action权限','0');
insert into `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
values('179','93','admin.net.ytoec.kernel.action.channel.ConfigAction','net.ytoec.kernel.action.channel.ConfigAction','系统配置项目Action','1','1','管理员的action权限','0');
insert into `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
values('180','93','admin.net.ytoec.kernel.action.waybill.WayBillAction','net.ytoec.kernel.action.waybill.WayBillAction','核心平台运单查询Action','1','1','管理员的action权限','0');
insert into `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
values('232','93','admin.net.ytoec.kernel.action.questionnaire.LogisticOrderInfoAction','net.ytoec.kernel.action.questionnaire.LogisticOrderInfoAction','根据运单号查询物流跟踪信息和订单信息','1','1','管理员的action权限','0');
insert into `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
values('243','93','admin.net.ytoec.kernel.action.channel.ChannelAction','net.ytoec.kernel.action.channel.ChannelAction','渠道消息管理Action','1','1','管理员的action权限','0');
insert into `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
values('255','93','admin.net.ytoec.kernel.action.home.HomeAction','net.ytoec.kernel.action.home.HomeAction','首页ActionAction','1','1','管理员的action权限','0');
insert into `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
values('295','93','admin.net.ytoec.kernel.action.user.YtoUserAction','net.ytoec.kernel.action.user.YtoUserAction','易通用户管理','1','2','管理员的action权限','0');
insert into `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
values('291','93','admin.net.ytoec.kernel.action.user.EopUserAction','net.ytoec.kernel.action.user.EopUserAction','EOP用户管理','1','2','管理员的action权限','0');
insert into `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
values('292','93','admin.net.ytoec.kernel.action.cms.ArticleAction','net.ytoec.kernel.action.cms.ArticleAction','栏目文章管理','1','3',NULL,'0');
insert into `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
values('293','93','admin.net.ytoec.kernel.action.cms.ColumnAction','net.ytoec.kernel.action.cms.ColumnAction','栏目管理','1','4',NULL,'0');
insert into `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
values('294','93','admin.net.ytoec.kernel.action.app.AppAction','net.ytoec.kernel.action.app.AppAction','应用管理','1','5',NULL,'0');

-- 子节点/网点
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (94, 'wangdian.net.ytoec.kernel.action.login.MainPageAction', 'net.ytoec.kernel.action.login.MainPageAction', '后台主页', '1', 1, '网点的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (94, 'wangdian.net.ytoec.kernel.action.user.UserAction', 'net.ytoec.kernel.action.user.UserAction', '用户', '1', 1, '网点的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (94, 'wangdian.net.ytoec.kernel.action.leavemessage.LeaveMessageAction', 'net.ytoec.kernel.action.leavemessage.LeaveMessageAction', '留言管理', '1', 1, '网点的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (94, 'wangdian.net.ytoec.kernel.action.leavemessage.SendMessageAction', 'net.ytoec.kernel.action.leavemessage.SendMessageAction', '发送留言', '1', 1, '网点的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (94, 'wangdian.net.ytoec.kernel.action.waybill.WayBillAction', 'net.ytoec.kernel.action.waybill.WayBillAction', '核心平台运单查询Action', '1', 1, '网点的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (94, 'wangdian.net.ytoec.kernel.action.order.OrderAction', 'net.ytoec.kernel.action.order.OrderAction', '订单action类', '1', 1, '网点的action权限', 0);
/**
 * 面单打印
 */
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (94, 'wangdian.net.ytoec.kernel.action.order.OrderPrintAction', 'net.ytoec.kernel.action.order.OrderPrintAction', '订单action类', '1', 1, '网点的action权限', 0);
	
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (94, 'wangdian.net.ytoec.kernel.action.order.PosttempAction', 'net.ytoec.kernel.action.order.PosttempAction', '运费模板', '1', 1, '网点的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (94, 'wangdian.net.ytoec.kernel.action.questionnaire.QuestionnaireAction', 'net.ytoec.kernel.action.questionnaire.QuestionnaireAction', '问题单', '1', 1, '网点的action权限', 0);

-- 子节点/网点-客服
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (95, 'wangdian_kefu.net.ytoec.kernel.action.login.MainPageAction', 'net.ytoec.kernel.action.login.MainPageAction', '后台主页', '1', 1, '网点客服(21)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (95, 'wangdian_kefu.net.ytoec.kernel.action.user.UserAction', 'net.ytoec.kernel.action.user.UserAction', '用户', '1', 1, '网点客服(21)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (95, 'wangdian_kefu.net.ytoec.kernel.action.leavemessage.LeaveMessageAction', 'net.ytoec.kernel.action.leavemessage.LeaveMessageAction', '留言管理', '1', 1, '网点客服(21)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (95, 'wangdian_kefu.net.ytoec.kernel.action.leavemessage.SendMessageAction', 'net.ytoec.kernel.action.leavemessage.SendMessageAction', '发送留言', '1', 1, '网点客服(21)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (95, 'wangdian_kefu.net.ytoec.kernel.action.questionnaire.QuestionnaireMonitorAction', 'net.ytoec.kernel.action.questionnaire.QuestionnaireMonitorAction', '运单监控', '1', 1, '网点客服(21)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (95, 'wangdian_kefu.net.ytoec.kernel.action.questionnaire.QuestionnaireAction', 'net.ytoec.kernel.action.questionnaire.QuestionnaireAction', '问题单', '1', 1, '网点客服(21)的action权限', 0);

-- 子节点/网点-财务
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (96, 'wangdian_caiwu.net.ytoec.kernel.action.login.MainPageAction', 'net.ytoec.kernel.action.login.MainPageAction', '后台主页', '1', 1, '网点财务(22)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (96, 'wangdian_caiwu.net.ytoec.kernel.action.user.UserAction', 'net.ytoec.kernel.action.user.UserAction', '用户', '1', 1, '网点财务(22)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (96, 'wangdian_caiwu.net.ytoec.kernel.action.leavemessage.LeaveMessageAction', 'net.ytoec.kernel.action.leavemessage.LeaveMessageAction', '留言管理', '1', 1, '网点财务(22)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (96, 'wangdian_caiwu.net.ytoec.kernel.action.leavemessage.SendMessageAction', 'net.ytoec.kernel.action.leavemessage.SendMessageAction', '发送留言', '1', 1, '网点财务(22)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (96, 'wangdian_caiwu.net.ytoec.kernel.action.questionnaire.QuestionnaireMonitorAction', 'net.ytoec.kernel.action.questionnaire.QuestionnaireMonitorAction', '运单监控', '1', 1, '网点财务(22)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (96, 'wangdian_caiwu.net.ytoec.kernel.action.order.OrderAction', 'net.ytoec.kernel.action.order.OrderAction', '订单action类', '1', 1, '网点财务(22)的action权限', 0);
/**
 *面单打印
 */	
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (96, 'wangdian_caiwu.net.ytoec.kernel.action.order.OrderPrintAction', 'net.ytoec.kernel.action.order.OrderPrintAction', '订单action类', '1', 1, '网点财务(22)的action权限', 0);
	
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (96, 'wangdian_caiwu.net.ytoec.kernel.action.order.PosttempAction', 'net.ytoec.kernel.action.order.PosttempAction', '运费模板', '1', 1, '网点财务(22)的action权限', 0);


-- 子节点/卖家
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (97, 'maijia.net.ytoec.kernel.action.login.MainPageAction', 'net.ytoec.kernel.action.login.MainPageAction', '后台主页', '1', 1, '卖家的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (97, 'maijia.net.ytoec.kernel.action.user.UserAction', 'net.ytoec.kernel.action.user.UserAction', '用户', '1', 1, '卖家的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (97, 'maijia.net.ytoec.kernel.action.leavemessage.LeaveMessageAction', 'net.ytoec.kernel.action.leavemessage.LeaveMessageAction', '留言管理', '1', 1, '卖家的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (97, 'maijia.net.ytoec.kernel.action.leavemessage.SendMessageAction', 'net.ytoec.kernel.action.leavemessage.SendMessageAction', '发送留言', '1', 1, '卖家的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (97, 'maijia.net.ytoec.kernel.action.waybill.WayBillAction', 'net.ytoec.kernel.action.waybill.WayBillAction', '核心平台运单查询Action', '1', 1, '卖家的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (97, 'maijia.net.ytoec.kernel.action.order.OrderAction', 'net.ytoec.kernel.action.order.OrderAction', '订单action类', '1', 1, '卖家的action权限', 0);
/**
 * 面单打印
 */
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (97, 'maijia.net.ytoec.kernel.action.order.OrderPrintAction', 'net.ytoec.kernel.action.order.OrderPrintAction', '订单action类', '1', 1, '卖家的action权限', 0);
	
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (97, 'maijia.net.ytoec.kernel.action.order.PosttempAction', 'net.ytoec.kernel.action.order.PosttempAction', '运费模板', '1', 1, '卖家的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (97, 'maijia.net.ytoec.kernel.action.questionnaire.QuestionnaireMonitorAction', 'net.ytoec.kernel.action.questionnaire.QuestionnaireMonitorAction', '运单监控', '1', 1, '卖家的action权限', 0);

-- 子节点/卖家-客服
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (98, 'maijia_kefu.net.ytoec.kernel.action.login.MainPageAction', 'net.ytoec.kernel.action.login.MainPageAction', '后台主页', '1', 1, '卖家客服(11)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (98, 'maijia_kefu.net.ytoec.kernel.action.user.UserAction', 'net.ytoec.kernel.action.user.UserAction', '用户', '1', 1, '卖家客服(11)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (98, 'maijia_kefu.net.ytoec.kernel.action.leavemessage.LeaveMessageAction', 'net.ytoec.kernel.action.leavemessage.LeaveMessageAction', '留言管理', '1', 1, '卖家客服(11)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (98, 'maijia_kefu.net.ytoec.kernel.action.leavemessage.SendMessageAction', 'net.ytoec.kernel.action.leavemessage.SendMessageAction', '发送留言', '1', 1, '卖家客服(11)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (98, 'maijia_kefu.net.ytoec.kernel.action.questionnaire.QuestionnaireMonitorAction', 'net.ytoec.kernel.action.questionnaire.QuestionnaireMonitorAction', '运单监控', '1', 1, '卖家客服(11)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (98, 'maijia_kefu.net.ytoec.kernel.action.waybill.WayBillAction', 'net.ytoec.kernel.action.waybill.WayBillAction', '核心平台运单查询Action', '1', 1, '卖家客服(11)的action权限', 0);

-- 子节点/卖家-财务
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (99, 'maijia_caiwu.net.ytoec.kernel.action.login.MainPageAction', 'net.ytoec.kernel.action.login.MainPageAction', '后台主页', '1', 1, '卖家财务(12)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (99, 'maijia_caiwu.net.ytoec.kernel.action.user.UserAction', 'net.ytoec.kernel.action.user.UserAction', '用户', '1', 1, '卖家财务(12)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (99, 'maijia_caiwu.net.ytoec.kernel.action.leavemessage.LeaveMessageAction', 'net.ytoec.kernel.action.leavemessage.LeaveMessageAction', '留言管理', '1', 1, '卖家财务(12)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (99, 'maijia_caiwu.net.ytoec.kernel.action.leavemessage.SendMessageAction', 'net.ytoec.kernel.action.leavemessage.SendMessageAction', '发送留言', '1', 1, '卖家财务(12)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (99, 'maijia_caiwu.net.ytoec.kernel.action.questionnaire.QuestionnaireMonitorAction', 'net.ytoec.kernel.action.questionnaire.QuestionnaireMonitorAction', '运单监控', '1', 1, '卖家财务(12)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (99, 'maijia_caiwu.net.ytoec.kernel.action.order.OrderAction', 'net.ytoec.kernel.action.order.OrderAction', '订单action类', '1', 1, '卖家财务(12)的action权限', 0);
	/**
	 * 面单打印
	 */
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (99, 'maijia_caiwu.net.ytoec.kernel.action.order.OrderPrintAction', 'net.ytoec.kernel.action.order.OrderPrintAction', '订单action类', '1', 1, '卖家财务(12)的action权限', 0);

-- 子节点/平台用户
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (100, 'pingtai.net.ytoec.kernel.action.login.MainPageAction', 'net.ytoec.kernel.action.login.MainPageAction', '后台主页', '1', 1, '平台用户(4)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (100, 'pingtai.net.ytoec.kernel.action.user.UserAction', 'net.ytoec.kernel.action.user.UserAction', '用户', '1', 1, '平台用户(4)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (100, 'pingtai.net.ytoec.kernel.action.leavemessage.LeaveMessageAction', 'net.ytoec.kernel.action.leavemessage.LeaveMessageAction', '留言管理', '1', 1, '平台用户(4)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (100, 'pingtai.net.ytoec.kernel.action.leavemessage.SendMessageAction', 'net.ytoec.kernel.action.leavemessage.SendMessageAction', '发送留言', '1', 1, '平台用户(4)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (100, 'pingtai.net.ytoec.kernel.action.questionnaire.QuestionnaireMonitorAction', 'net.ytoec.kernel.action.questionnaire.QuestionnaireMonitorAction', '运单监控', '1', 1, '平台用户(4)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (100, 'pingtai.net.ytoec.kernel.action.waybill.WayBillAction', 'net.ytoec.kernel.action.waybill.WayBillAction', '核心平台运单查询Action', '1', 1, '平台用户(4)的action权限', 0);

-- 增加卖家和卖家客服对问题件管理的访问权限
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (97, 'maijia.net.ytoec.kernel.action.questionnaire.QuestionnaireAction', 'net.ytoec.kernel.action.questionnaire.QuestionnaireAction', '问题件', '1', 1, '卖家的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (98, 'maijia_kefu.net.ytoec.kernel.action.questionnaire.QuestionnaireAction', 'net.ytoec.kernel.action.questionnaire.QuestionnaireAction', '问题件', '1', 1, '卖家客服(11)的action权限', 0);
-- 增加卖家和卖家客服对运单监控/关注运单的访问权限
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (97, 'maijia.net.ytoec.kernel.action.questionnaire.AttentionMailAction', 'net.ytoec.kernel.action.questionnaire.AttentionMailAction', '关注运单', '1', 1, '卖家的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (98, 'maijia_kefu.net.ytoec.kernel.action.questionnaire.AttentionMailAction', 'net.ytoec.kernel.action.questionnaire.AttentionMailAction', '关注运单', '1', 1, '卖家客服(11)的action权限', 0);

	
-- 增加所有用户对功能'根据运单号查询物流跟踪信息和订单信息'的操作权限(电子对账、高级查询、问题件管理)
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (93, 'admin.net.ytoec.kernel.action.questionnaire.LogisticOrderInfoAction', 'net.ytoec.kernel.action.questionnaire.LogisticOrderInfoAction', '根据运单号查询物流跟踪信息和订单信息', '1', 1, '管理员的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (94, 'wangdian.net.ytoec.kernel.action.questionnaire.LogisticOrderInfoAction', 'net.ytoec.kernel.action.questionnaire.LogisticOrderInfoAction', '根据运单号查询物流跟踪信息和订单信息', '1', 1, '网点的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (95, 'wangdian_kefu.net.ytoec.kernel.action.questionnaire.LogisticOrderInfoAction', 'net.ytoec.kernel.action.questionnaire.LogisticOrderInfoAction', '根据运单号查询物流跟踪信息和订单信息', '1', 1, '网点客服(21)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (96, 'wangdian_caiwu.net.ytoec.kernel.action.questionnaire.LogisticOrderInfoAction', 'net.ytoec.kernel.action.questionnaire.LogisticOrderInfoAction', '根据运单号查询物流跟踪信息和订单信息', '1', 1, '网点财务(22)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (97, 'maijia.net.ytoec.kernel.action.questionnaire.LogisticOrderInfoAction', 'net.ytoec.kernel.action.questionnaire.LogisticOrderInfoAction', '根据运单号查询物流跟踪信息和订单信息', '1', 1, '卖家的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (98, 'maijia_kefu.net.ytoec.kernel.action.questionnaire.LogisticOrderInfoAction', 'net.ytoec.kernel.action.questionnaire.LogisticOrderInfoAction', '根据运单号查询物流跟踪信息和订单信息', '1', 1, '卖家客服(11)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (99, 'maijia_caiwu.net.ytoec.kernel.action.questionnaire.LogisticOrderInfoAction', 'net.ytoec.kernel.action.questionnaire.LogisticOrderInfoAction', '根据运单号查询物流跟踪信息和订单信息', '1', 1, '卖家财务(12)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (100, 'pingtai.net.ytoec.kernel.action.questionnaire.LogisticOrderInfoAction', 'net.ytoec.kernel.action.questionnaire.LogisticOrderInfoAction', '根据运单号查询物流跟踪信息和订单信息', '1', 1, '平台用户(4)的action权限', 0);

-- 给网点客服添加高级查询的权限
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (95, 'wangdian_kefu.net.ytoec.kernel.action.waybill.WayBillAction', 'net.ytoec.kernel.action.waybill.WayBillAction', '核心平台运单查询Action', '1', 1, '网点客服(21)的action权限', 0);

-- @2012-02-08/ChenRen
-- 给管理员添加访问 #渠道消息管理 权限
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (93, 'admin.net.ytoec.kernel.action.channel.ChannelAction', 'net.ytoec.kernel.action.channel.ChannelAction', '渠道消息管理Action', '1', 1, '管理员的action权限', 0);
--@2012-02-21/Longyong.li
--卖家所有角色具备网点搜索权限
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (97, 'maijia.net.ytoec.kernel.search.action.branchBranchSearchAction', 'net.ytoec.kernel.search.action.branch.BranchSearchAction', '网点信息搜索Action', '1', 1, '卖家的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (98, 'maijia_kefu.net.ytoec.kernel.search.action.branchBranchSearchAction', 'net.ytoec.kernel.search.action.branch.BranchSearchAction', '网点信息搜索Action', '1', 1, '卖家客服的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (99, 'maijia_caiwu.net.ytoec.kernel.search.action.branchBranchSearchAction', 'net.ytoec.kernel.search.action.branch.BranchSearchAction', '网点信息搜索Action', '1', 1, '卖家财务的action权限', 0);

--@2012-02-21/Longyong.li
--卖家所有角色具备网点搜索权限
/*
 * 和上面重复了
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (97, 'maijia.net.ytoec.kernel.search.action.branchBranchSearchAction', 'net.ytoec.kernel.search.action.branch.BranchSearchAction', '网点信息搜索Action', '1', 1, '卖家的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (98, 'maijia_kefu.net.ytoec.kernel.search.action.branchBranchSearchAction', 'net.ytoec.kernel.search.action.branch.BranchSearchAction', '网点信息搜索Action', '1', 1, '卖家客服的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (99, 'maijia_caiwu.net.ytoec.kernel.search.action.branchBranchSearchAction', 'net.ytoec.kernel.search.action.branch.BranchSearchAction', '网点信息搜索Action', '1', 1, '卖家财务的action权限', 0);
*/
--@2012-02-21/Wangyong
--所有角色具备首页权限
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (97, 'maijia.net.ytoec.kernel.action.home.HomeAction', 'net.ytoec.kernel.action.home.HomeAction', '首页Action', '1', 1, '卖家的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (98, 'maijia_kefu.net.ytoec.kernel.action.home.HomeAction', 'net.ytoec.kernel.action.home.HomeAction', '首页ActionAction', '1', 1, '卖家客服的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (99, 'maijia_caiwu.net.ytoec.kernel.action.home.HomeAction', 'net.ytoec.kernel.action.home.HomeAction', '首页ActionAction', '1', 1, '卖家财务的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (93, 'admin.net.ytoec.kernel.action.home.HomeAction', 'net.ytoec.kernel.action.home.HomeAction', '首页ActionAction', '1', 1, '管理员的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (94, 'wangdian.net.ytoec.kernel.action.home.HomeAction', 'net.ytoec.kernel.action.home.HomeAction', '首页ActionAction', '1', 1, '网点的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (95, 'wangdian_kefu.net.ytoec.kernel.action.home.HomeAction', 'net.ytoec.kernel.action.home.HomeAction', '首页ActionAction', '1', 1, '网点客服(21)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (96, 'wangdian_caiwu.net.ytoec.kernel.action.home.HomeAction', 'net.ytoec.kernel.action.home.HomeAction', '首页ActionAction', '1', 1, '网点财务(22)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (100, 'pingtai.net.ytoec.kernel.action.home.HomeAction', 'net.ytoec.kernel.action.home.HomeAction', '首页ActionAction', '1', 1, '平台用户(4)的action权限', 0);

INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (99, 'maijia_caiwu.net.ytoec.kernel.action.order.PosttempAction', 'net.ytoec.kernel.action.order.PosttempAction', '运费模板', '1', 1, '卖家财务(12)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (99, 'maijia_caiwu.net.ytoec.kernel.action.waybill.WayBillAction', 'net.ytoec.kernel.action.waybill.WayBillAction', '特殊账单', '1', 1, '卖家财务(12)的action权限', 0);

/**
 * 2012-03-06/ChenRen
 * 卖家子账号#网点查找、我要发货
 */
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (98, 'maijia_kefu.net.ytoec.kernel.action.order.OrderPlaceAction', 'net.ytoec.kernel.action.order.OrderPlaceAction', '我要发货', '1', 1, '卖家客服(11)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (98, 'maijia_kefu.net.ytoec.kernel.search.action.branch.BranchSearchAction', 'net.ytoec.kernel.search.action.branch.BranchSearchAction', '网点查找', '1', 1, '卖家客服(11)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (98, 'maijia_kefu.net.ytoec.kernel.action.order.OrderAction', 'net.ytoec.kernel.action.order.OrderAction', '面单打印', '1', 1, '卖家客服(11)的action权限', 0);
/**
 * 新增面单打印功能权限
 */	
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (98, 'maijia_kefu.net.ytoec.kernel.action.order.OrderPrintAction', 'net.ytoec.kernel.action.order.OrderPrintAction', '面单打印', '1', 1, '卖家客服(11)的action权限', 0);
	
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (99, 'maijia_caiwu.net.ytoec.kernel.action.order.OrderPlaceAction', 'net.ytoec.kernel.action.order.OrderPlaceAction', '我要发货', '1', 1, '卖家财务(12)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (99, 'maijia_caiwu.net.ytoec.kernel.search.action.branch.BranchSearchAction', 'net.ytoec.kernel.search.action.branch.BranchSearchAction', '网点查找', '1', 1, '卖家财务(12)的action权限', 0);

/**
 * 2012-03-08/ChenRen
 * 网点财务#运费调整
 */
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (96, 'wangdian_caiwu.net.ytoec.kernel.action.waybill.WayBillAction', 'net.ytoec.kernel.action.waybill.WayBillAction', '运费调整', '1', 1, '网点财务(22)', 0);

/**
 * 2012-03-10/ChenRen
 * 卖家/子账号 #个性化配置
 */
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (97, 'maijia.net.ytoec.kernel.action.questionnaire.AssociationAccountAction', 'net.ytoec.kernel.action.user.AssociationAccountAction', '关联账号/个性化配置', '1', 1, '卖家(1)', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (98, 'maijia_kefu.net.ytoec.kernel.action.questionnaire.AssociationAccountAction', 'net.ytoec.kernel.action.user.AssociationAccountAction', '关联账号/个性化配置', '1', 1, '卖家客服(11)', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (99, 'maijia_caiwu.net.ytoec.kernel.action.questionnaire.AssociationAccountAction', 'net.ytoec.kernel.action.user.AssociationAccountAction', '关联账号/个性化配置', '1', 1, '卖家财务(12)', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (94, 'wangdian.net.ytoec.kernel.search.action.branch.BranchSearchAction', 'net.ytoec.kernel.search.action.branch.BranchSearchAction', '关联账号/个性化配置', '1', 1, '网点(2)', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (95, 'wangdian_kefu.net.ytoec.kernel.search.action.branch.BranchSearchAction', 'net.ytoec.kernel.search.action.branch.BranchSearchAction', '关联账号/个性化配置', '1', 1, '网点客服(21)', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (96, 'wangdian_caiwu.net.ytoec.kernel.search.action.branch.BranchSearchAction', 'net.ytoec.kernel.search.action.branch.BranchSearchAction', '关联账号/个性化配置', '1', 1, '网点财务(22)', 0);
	
/**
 * 2012-03-10/ChenRen
 * 卖家我要发货
 */	
	
INSERT INTO `ec_core_configcode`(`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
VALUES (97, 'maijia.net.ytoec.kernel.action.order.OrderPlaceAction', 'net.ytoec.kernel.action.order.OrderPlaceAction', '我要发货Action', '1', 1, '卖家action权限', 0);	
	
INSERT INTO `ec_core_configcode`(`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
VALUES (97, 'maijia.net.ytoec.kernel.action.order.PosttempAction', 'net.ytoec.kernel.action.order.PosttempAction', '运费模版Action', '1', 1, '卖家action权限', 0);	
INSERT INTO `ec_core_configcode`(`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
VALUES (98, 'maijia.net.ytoec.kernel.action.order.PosttempAction', 'net.ytoec.kernel.action.order.PosttempAction', '运费模版Action', '1', 1, '卖家action权限', 0);	
INSERT INTO `ec_core_configcode`(`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
VALUES (99, 'maijia.net.ytoec.kernel.action.order.PosttempAction', 'net.ytoec.kernel.action.order.PosttempAction', '运费模版Action', '1', 1, '卖家action权限', 0);	
INSERT INTO `ec_core_configcode`(`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
VALUES (94, 'maijia.net.ytoec.kernel.action.order.PosttempAction', 'net.ytoec.kernel.action.order.PosttempAction', '运费模版Action', '1', 1, '卖家action权限', 0);	
INSERT INTO `ec_core_configcode`(`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
VALUES (95, 'maijia.net.ytoec.kernel.action.order.PosttempAction', 'net.ytoec.kernel.action.order.PosttempAction', '运费模版Action', '1', 1, '卖家action权限', 0);	
INSERT INTO `ec_core_configcode`(`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
VALUES (96, 'maijia.net.ytoec.kernel.action.order.PosttempAction', 'net.ytoec.kernel.action.order.PosttempAction', '运费模版Action', '1', 1, '卖家action权限', 0);	

/**
 * 2012-3-22/ChenRen
 * 给平台用户增加action的访问权限
 */
INSERT INTO `ec_core_configcode`(`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (100, 'pingtai.net.ytoec.kernel.action.order.PosttempAction', 'net.ytoec.kernel.action.order.PosttempAction', '运费模版Action', '1', 1, '平台用户action权限', 0);	
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (100, 'pingtai.net.ytoec.kernel.action.order.OrderAction', 'net.ytoec.kernel.action.order.OrderAction', '订单相关actoin', '1', 1, '平台用户的action权限', 0);
/**
 * 面单打印
 */
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (100, 'pingtai.net.ytoec.kernel.action.order.OrderPrintAction', 'net.ytoec.kernel.action.order.OrderPrintAction', '订单相关actoin', '1', 1, '平台用户的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (100, 'pingtai.net.ytoec.kernel.search.action.branch.BranchSearchAction', 'net.ytoec.kernel.search.action.branch.BranchSearchAction', '网点信息搜索Action', '1', 1, '平台用户的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (100, 'pingtai.net.ytoec.kernel.action.questionnaire.QuestionnaireAction', 'net.ytoec.kernel.action.questionnaire.QuestionnaireAction', '问题件相关actoin', '1', 1, '平台用户的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (100, 'pingtai.net.ytoec.kernel.action.order.OrderPlaceAction', 'net.ytoec.kernel.action.order.OrderPlaceAction', '我要发货', '1', 1, '平台用户的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (100, 'pingtai.net.ytoec.kernel.action.questionnaire.AttentionMailAction', 'net.ytoec.kernel.action.questionnaire.AttentionMailAction', '关注运单', '1', 1, '平台用户的action权限', 0);

	
/**
 * 2012-08-02/Yuyuezhong
 * 卖家短信首页
 */	
INSERT INTO `ec_core_configcode`(`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
VALUES (97, 'maijia.net.ytoec.kernel.action.sms.SmsHomeAction', 'net.ytoec.kernel.action.sms.SmsHomeAction', '短信服务Action', '1', 1, '卖家action权限', 0);	
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (98, 'maijia_kefu.net.ytoec.kernel.action.sms.SmsHomeAction', 'net.ytoec.kernel.action.sms.SmsHomeAction', '短信服务Action', '1', 1, '卖家客服(11)的action权限', 0);
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (99, 'maijia_caiwu.net.ytoec.kernel.action.sms.SmsHomeAction', 'net.ytoec.kernel.action.sms.SmsHomeAction', '短信服务Action', '1', 1, '卖家财务(12)的action权限', 0);
	
/**
 * @2012-03-30/ChenRen
 * 平台没有关联用户的权限
INSERT INTO `ec_core_configcode` (`pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) 
	VALUES (100, 'pingtai.net.ytoec.kernel.action.user.AssociationAccountAction', 'net.ytoec.kernel.action.user.AssociationAccountAction', '关联帐号action', '1', 1, '平台用户的action权限', 0);

 */