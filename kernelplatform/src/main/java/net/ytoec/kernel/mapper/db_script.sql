--开发环境数据库连接信息: address:10.1.198.71:3306  databaseName:eccoredb userName:eccore password:eccore 

--问题件标签表
CREATE TABLE `ec_core_questionnaire_tag` (
  `id` int(11) NOT NULL auto_increment COMMENT '主键ID',
  `tag_name` varchar(50) NOT NULL COMMENT '标签名称',
  `tag_pos` int(2) NOT NULL default '0' COMMENT '标签位置',
  `tag_userthreadid` int(11) NOT NULL COMMENT '标签所属ID',
  `tag_type` int(1) default '0' COMMENT '标签类型',
  `tag_remark` varchar(200) default NULL COMMENT '标签备注',
  `create_time` timestamp NULL default NULL COMMENT '创建时间',
  `update_time` timestamp NULL default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

----渠道信息表.
CREATE TABLE `EC_CORE_CHANNEL_INFO` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channel_key` varchar(200) DEFAULT NULL,
  `channel_value` varchar(200) DEFAULT NULL,
  `ip_address` varchar(200) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp   NULL  ,
clientId varchar(50),
parternId varchar(50),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*
 * ALTER TABLE `ec_core_channel_info` ADD COLUMN `isSend` char NULL , ADD COLUMN `isPrint` char null,ADD COLUMN `ip` varchar(200) null; 
 * */
/**
 *  ALTER TABLE `ec_core_channel_info` ADD COLUMN `vip` char default 'N' COMMENT '新龙vip';
 */

--联系人(发件人、收件人)  @Deprecated 已过时. 现使用表:EC_CORE_TRADERINFO
CREATE TABLE `EC_CORE_CONTACT` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) DEFAULT NULL,
  `post_code` varchar(200) DEFAULT NULL,
  `phone` varchar(200) DEFAULT NULL,
  `mobile` varchar(200) DEFAULT NULL,
  `prov` varchar(200) DEFAULT NULL,
  `city` varchar(200) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp   NULL  ,
  `trade_type` varchar(200) DEFAULT NULL,
  order_id int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--联系人(发件人、收件人)
CREATE TABLE `EC_CORE_TRADERINFO` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) DEFAULT NULL,
  `post_code` varchar(200) DEFAULT NULL,
  `phone` varchar(200) DEFAULT NULL,
  `mobile` varchar(200) DEFAULT NULL,
  `prov` varchar(200) DEFAULT NULL,
  `city` varchar(200) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL  ,
  `trade_type` varchar(200) DEFAULT NULL,
  order_id int(11) DEFAULT NULL,
  `district` varchar(200) DEFAULT NULL,		-- 县 2011-08-01 ChenRen新增
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--出错信息规范表
CREATE TABLE `EC_CORE_ERROR_MESSAGE` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `error_type` varchar(200) DEFAULT NULL,
  `error_reason` varchar(200) DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp   NULL  ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--订单表
CREATE TABLE `EC_CORE_ORDER` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version` varchar(50) DEFAULT NULL,
  `logistic_provider_id` varchar(50) DEFAULT NULL,
  `tx_logistic_id` varchar(50) DEFAULT NULL,
  `tradeNo` varchar(50) DEFAULT NULL,
  `customer_id` varchar(50) DEFAULT NULL,
  `mailNo` varchar(50) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `flag` varchar(50) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL,
  `send_start_time` timestamp NULL,
  `send_end_time` timestamp  NULL ,
  `insurance_value` float DEFAULT NULL,
  `package_or_not` varchar(50) DEFAULT NULL,
  `special` varchar(50) DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `client_id` varchar(50) DEFAULT NULL,
  `status` varchar(200) DEFAULT NULL,
  
  `weight` float DEFAULT NULL,				-- 2011-08-01 ChenRen 新增的几个字段
  `sign_price` float DEFAULT NULL,
  `vip_id` varchar(50) DEFAULT NULL,  
  `line_type` varchar(50) DEFAULT NULL,  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*
ALTER TABLE `ec_core_order`
ADD COLUMN `weight`  float NULL AFTER `status`,
ADD COLUMN `sign_price`  float NULL AFTER `weight`,
ADD COLUMN `vip_id`  varchar(50) NULL AFTER `sign_price`;
ADD COLUMN `line_type`  varchar(50) NULL AFTER `vip_id`;
*/
--商品信息
CREATE TABLE `EC_CORE_PRODUCT` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_name` varchar(200) DEFAULT NULL,
  `item_number` int(11) DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `order_id` int(11) DEFAULT NULL,
  `tx_logistic_id` varchar(50) DEFAULT NULL,
  `mailNo` int(11) varchar(50) DEFAULT NULL,
  `create_time` timestamp   NULL  ,
  `update_time` timestamp   NULL  ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--发送任务表
CREATE TABLE `EC_CORE_SEND_TASK` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `request_url` varchar(200) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `last_send_time` timestamp   NULL  ,
  `remark` varchar(200) DEFAULT NULL,
  `order_id` int(11) DEFAULT NULL,
  `client_id` varchar(200) DEFAULT NULL,
  `request_params` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--请求、响应日志表
CREATE TABLE `EC_CORE_TASK_LOG` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `request_url` varchar(200) DEFAULT NULL,
  `request_time` timestamp NULL DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `order_id` int(11) DEFAULT NULL,
  `client_id` varchar(50) DEFAULT NULL,
  `fail_message` varchar(200) DEFAULT NULL,
   request_params varchar(2000),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--省市县区区域表
CREATE TABLE `EC_CORE_REGION` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `region_number` varchar(50) DEFAULT NULL,
  `region_name` varchar(200) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--留言表
CREATE TABLE EC_CORE_LEAVEMESSAGE (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message_theme` varchar(200) DEFAULT NULL,
  `send_way` int(11) DEFAULT NULL,
  `message_type` int(11) DEFAULT NULL,
  `message_status` int(11) DEFAULT NULL,
  `send_user_id` int(11) DEFAULT NULL,
  `receive_user_id` int(11) DEFAULT NULL,
  `message_content` varchar(200) DEFAULT NULL,
  `send_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `delete_` int(11) DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--数据库脚本文件修改策略:需要注明修改内容,修改人,修改时间,备注(可选).
-- modify info:  EC_CORE_TASK_LOG 表中增加 字段request_params "alter table EC_CORE_TASK_LOG add column request_params varchar(2000)", mod by lijp, 20110721

/**
 * 用户表
 * @author ChenRen
 * @date	2011-07-25
 */
CREATE TABLE `EC_CORE_USER` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(200) NOT NULL,			-- 用户帐号，唯一。
  `user_password` varchar(200) NOT NULL,
  `tele_phone` varchar(200) DEFAULT NULL,		-- 手机号
  `mobile_phone` varchar(200) DEFAULT NULL,		-- 固定电话  (手机号码和固定电话必须填一个)
  `address_province` varchar(50) NOT NULL,		-- 地址：省
  `address_city` varchar(50) NOT NULL,			-- 地址：市
  `address_district` varchar(50) NOT NULL,		-- 地址：区/县
  `address_street` varchar(100) NOT NULL,		-- 地址：详细街道
  `sex` varchar(20) DEFAULT 'M',				-- 性别：男(M)、女(F)、未知(N)
  `shop_name` varchar(200) DEFAULT NULL,		-- 店铺名称
  `shop_account` varchar(100) DEFAULT NULL,		-- 店铺帐号;	如淘宝登录Id
  `mail` varchar(100) DEFAULT NULL,
  `card_type` varchar(20) DEFAULT '1',			-- 证件类型：1(身份证)、2(其他)
  `card_no` varchar(100) DEFAULT NULL,
  `user_type` varchar(20) DEFAULT NULL,			-- 用户类型：1(VIP用户)、2(网店用户) 3(渠道信息员)
  `user_source` varchar(20) DEFAULT NULL,		-- 用户来源：1(电商)、2(淘宝)
  `user_state` varchar(20) DEFAULT NULL,		-- 用户状态：0(失效)、1(正常)、-1(未激活)
  `user_level` varchar(20) DEFAULT NULL,		-- 用户级别
  `create_user` int(11) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  --`update_user` int(11) DEFAULT NULL,			/* @2011-07-27 ChenRen 在工具直接删除了该字段	*/
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `remark` varchar(200) DEFAULT NULL,
  `site` int(11) DEFAULT NULL,					-- 网点编号.VIP用户该值为所属网点的编号, 网点经理/客服等于网点编号相同
  `user_name_text` varchar(200) NOT NULL,		-- 用户的显示名称. 在系统中不显示用户的帐号，显示名称
  `user_code` varchar(50) DEFAULT NULL,			-- 用户编码; VIP用户从金刚同步过来
  `login_time` timestamp,						-- 登录时间
  -- // 2011-10-10/ChenRen 新增字段
  `taobao_encode_key` varchar(200) DEFAULT NULL,	-- 淘宝帐号的密文. 与order表中的customerId一致
  `field001` varchar(200) DEFAULT NULL,			-- 备用字段001
  `field002` varchar(200) DEFAULT NULL,			-- 备用字段002
  `field003` varchar(200) DEFAULT NULL,			-- 备用字段003
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*
 * @ 2011-08-18 By ChenRen
 * @ description	新增字段用户编码; VIP用户从金刚同步过来会有
ALTER TABLE `ec_core_user`
ADD COLUMN `user_code`  varchar(50) NULL AFTER `user_name_text`;
 */

--问题单表
CREATE TABLE EC_CORE_QUESTIONNAIRE (
  `id` int(11) NOT NULL AUTO_INCREMENT,				--问题单主键
  `mail_no` varchar(50) DEFAULT NULL,				--物流运单号
  `order_no` varchar(50) DEFAULT NULL,				--客户下单号
  `tx_logistic_id` varchar(50) DEFAULT NULL,		--物流单号
  `mail_type` varchar(50) DEFAULT NULL,				--寄件状态：根据统计标识。
  `sender_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',--寄件时间
  `destination` varchar(200) DEFAULT NULL,			--目的地
  `deal_status` varchar(200) DEFAULT '1',			--处理状态:默认未处理（1），处理后更改为已处理（2）
  `deal_info` varchar(200) DEFAULT NULL,			--处理信息
  `vip_id` int(11) DEFAULT NULL,					--VIP id
  `vip_name` varchar(50) DEFAULT NULL,				--VIP 账户
  `vip_textname` varchar(50) DEFAULT NULL,			--VIP姓名
  `vip_phone` varchar(50) DEFAULT NULL,				--VIP电话
  `vip_cellphone` varchar(50) DEFAULT NULL,			--VIP手机
  `feedback_info` varchar(200) DEFAULT NULL,		--反馈客户信息
  `branck_id` int(11) DEFAULT NULL,					--网点id;
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,--创建时间
  `deal_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',--处理时间
  `deal_userid` int(11) DEFAULT NULL,				--处理人
  `buy_username` varchar(50) DEFAULT NULL,			--买家姓名
  `buy_userphone` varchar(50) DEFAULT NULL,			--买家电话
  `province` varchar(50) DEFAULT NULL,				--寄件目的地省
  `city` varchar(50) DEFAULT NULL,					--目的地市
  `country` varchar(50) DEFAULT NULL,				--目的地县区
  `address` varchar(50) DEFAULT NULL,				--目的地详细地址
  `backup_info` varchar(50) DEFAULT NULL,			--备用字段
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--金刚接口运单信息数据定义
CREATE TABLE EC_CORE_JGWAYBILL (
  `id` int(11) NOT NULL AUTO_INCREMENT,				--主键
  `mail_no` varchar(50) NOT NULL,					--运单号
  `sname` varchar(50) NOT NULL,						--姓名（发件人）
  `spost_code` varchar(50) DEFAULT NULL,			--邮政编码（发件人）
  `sphone` varchar(50) DEFAULT NULL,				--固定电话（发件人）二取一
  `smobile` varchar(50) DEFAULT NULL,				--移动电话（发件人）
  `sprov` varchar(50) DEFAULT NULL,					--省（发件人）
  `scity` varchar(50) DEFAULT NULL,					--市（发件人）
  `sdistrict` varchar(200) DEFAULT NULL,			--区县（发件人）
  `saddress` varchar(200) DEFAULT NULL,				--详细地址（发件人）
  `bname` varchar(50) NOT NULL,						--姓名（收件人）
  `bpost_code` varchar(50) DEFAULT NULL,			--邮政编码（收件人）
  `bphone` varchar(50) DEFAULT NULL,				--固定电话（收件人）二取一
  `bmobile` varchar(50) DEFAULT NULL,				--移动电话（收件人）
  `bprov` varchar(50) DEFAULT NULL,					--省（收件人）
  `bcity` varchar(50) DEFAULT NULL,					--市（收件人）
  `bdistrict` varchar(200) DEFAULT NULL,			--区县（收件人）
  `baddress` varchar(200) DEFAULT NULL,				--详细地址（收件人）
  `order_id` varchar(50) DEFAULT NULL,				--订单号
  `backup1` varchar(50) DEFAULT NULL,				--备用字段
  `backup2` varchar(50) DEFAULT NULL,				--备用字段
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,--创建时间
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--配置项表,可以存放一些系统中用的配置项
CREATE TABLE `ec_core_configcode` (
  `id` INTEGER(11) NOT NULL AUTO_INCREMENT,
  `pid` INTEGER(11) COMMENT '配置项的父节点Id，取值为父对象的id主键',
  `conf_key` VARCHAR(200) NOT NULL UNIQUE COMMENT '配置项的key值',
  `conf_value` VARCHAR(200) NOT NULL,
  `conf_text` VARCHAR(200) COMMENT '配置项的显示值',
  `conf_type` VARCHAR(50) NOT NULL COMMENT '配置项的类型. key要和type联合唯一',
  `conf_level` VARCHAR(50) NOT NULL COMMENT '配置项级别',
  `seq` INTEGER(11) COMMENT '排序字段',
  `remark` VARCHAR(200),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--多线程服务数据表
CREATE TABLE `ec_core_serverthread` (
  `id` INTEGER(11) NOT NULL AUTO_INCREMENT,
  `ip` VARCHAR(50) COMMENT 'IP号',
  `port` VARCHAR(200) COMMENT '端口号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `start_num` INTEGER(11) COMMENT '开始数',
  `end_num` INTEGER(11) COMMENT '结束数',
  `start_taskid` INTEGER(11) COMMENT '开始任务id',
  `end_taskid` INTEGER(11) COMMENT '结束任务id',
  `remark` VARCHAR(200),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**
 * 打印模板现在暂停开发
 */
CREATE TABLE `ec_core_printtemp` (
  `id` INTEGER(11) NOT NULL AUTO_INCREMENT UNIQUE,
  `temp_name` VARCHAR(200),
  `temp_type` INTEGER(11) COMMENT '0:系统内置；1:用户自定义',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_user` INTEGER(11),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `ec_core_printtemp_details` (
  `id` INTEGER(11) NOT NULL AUTO_INCREMENT UNIQUE,
  `tempId` INTEGER(11) NOT NULL COMMENT '父模板Id',
  `name_code` VARCHAR(50) NOT NULL,
  `name_text` VARCHAR(200),
  `top` INTEGER(11) NOT NULL,
  `left` INTEGER(11) NOT NULL,
  `width` INTEGER(11) NOT NULL,
  `height` INTEGER(11) NOT NULL,
  `isEditable` VARCHAR(20) NOT NULL DEFAULT 'true' COMMENT '能否编辑;true/false',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*
 * @2011-09-09/ChenRen
 * 运费模板
 */
CREATE TABLE `ec_core_posttemp` (
  `id` INTEGER(11) NOT NULL AUTO_INCREMENT,
  `pt_name` varchar(100) NOT NULL COMMENT '模板名称',
  `postinfo` TEXT NOT NULL COMMENT '内容为运费信息.xml字符串.',
  `pt_type` VARCHAR(20) DEFAULT NULL COMMENT '1表示系统模板. 其他值暂无意义', -- @2011-10-21/ChenRen 新增字段
  `create_user` INTEGER(11) NOT NULL COMMENT '创建人Id',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_user` int(11) DEFAULT NULL,-- @2011-11-11/ChenRen 新增
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',-- @2011-11-11/ChenRen新增
  `remark` VARCHAR(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE ec_core_posttemp ADD calclate_type VARCHAR(1 ) NOT NULL COMMENT '计费类型 1 代表固定收费 2 简单重量收费 3 续重价格收费  4 续重统计单位' ;
ALTER TABLE ec_core_posttemp ADD first_weight float DEFAULT NULL COMMENT '首重';

/*
ALTER TABLE `ec_core_posttemp`
ADD COLUMN `update_user`  int(11) NULL AFTER `remark`,
ADD COLUMN `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' AFTER `update_user`;
 */
/*
 * @2011-09-09/ChenRen
 * 模板用户关系表 
 */
CREATE TABLE `ec_core_posttempuser` (
  `id` INTEGER(11) NOT NULL AUTO_INCREMENT UNIQUE,
  `branchId` INTEGER(11) NOT NULL COMMENT '网点Id. 对应网点用户的主键Id',
  `vipId` INTEGER(11) NOT NULL COMMENT 'Vip用户的主键Id. 一个vip用户一条记录',
  `postId` INTEGER(11) NOT NULL COMMENT '模板Id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `EC_CORE_USERTHREAD` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_code` varchar(50) DEFAULT NULL,			-- 用户编码。
  `site_code` varchar(50) DEFAULT NULL,         -- 用户所属网点
  `user_name` varchar(50) DEFAULT NULL,		-- 用户名
  `user_state` varchar(50) DEFAULT NULL,	-- 用户状态
  `cteate_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `backup1` varchar(50) DEFAULT NULL,			-- 备用字段
  `backup2` varchar(50) DEFAULT NULL,		    -- 备用字段
  `backup3` varchar(100) DEFAULT NULL,		    -- 备用字段
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_code` (`user_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**
 * @2011-12-06/ChenRen
 * 问题件处理内容表. 一个问题件可以有多条处理内容.
 * 该表数据全部从金刚同步过来.
 */
create table ec_core_questionnaire_deal (
  id                             int primary key auto_increment,
  issue_id                       varchar(50) not null comment '问题id',
  org_code                       varchar(50) comment '处理网点',
  first_responsibility_org_code  varchar(50) comment '首要责任网点',
  second_responsibility_org_code varchar(50) comment '次要责任网点',
  deal_content                   varchar(4000)  comment '处理内容',
  status                         varchar(4)  comment '处理状态',
  create_time                    timestamp,
  create_user_code               varchar(50),
  create_user_name               varchar(255),
  create_org_code                varchar(50) comment '创建组织编号',
  modify_time                    timestamp,
  modify_user_code               varchar(50),
  modify_user_name               varchar(255),
  modify_org_code                varchar(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**
 * 修改问题件的表
 * 2011-12-07/ChenRen
 */
ALTER TABLE `ec_core_questionnaire`
ADD COLUMN `issue_id`  varchar(50) NULL COMMENT '问题Id. 同步过来的问题id. ' AFTER `id`,
ADD COLUMN `issue_desc`  varchar(2000) NULL COMMENT '问题描述. 金刚同步过来' AFTER `tx_logistic_id`,	
ADD COLUMN `branck_text`  varchar(200) NULL COMMENT '上报网点' AFTER `branck_id`,
ADD COLUMN `issue_create_user_text`  varchar(50) NULL COMMENT '上报人' AFTER `branck_text`,
ADD COLUMN `issue_create_time`  timestamp NULL COMMENT '上报时间' AFTER `issue_create_user_text`,
ADD COLUMN `rec_branck_text`  varchar(200) NULL COMMENT '接收网点' AFTER `issue_create_time`,
ADD COLUMN `issue_status`  varchar(20) NULL COMMENT '问题状态. 直接通过金刚的数据.PD10/未处理；PD20/处理中；PD30/处理完成；PD40/取消；' AFTER `issue_desc`,
ADD COLUMN `customer_id`  varchar(50) NULL COMMENT '替换vipId的作用.通过mailNo从order表中通过过来' AFTER `deal_info`;

ALTER TABLE `ec_core_questionnaire`
MODIFY COLUMN `branck_id`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '揽件网点Id' AFTER `feedback_info`,
MODIFY COLUMN `branck_text`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '揽件网点名称' AFTER `branck_id`,
ADD COLUMN `report_branck_text`  varchar(200) NULL COMMENT '上报网点' AFTER `rec_branck_text`;

/**
 * 2012-06-07 给问题件表添加了上报网点的code字段
 */
ALTER TABLE  ec_core_questionnaire ADD COLUMN report_branck_code  varchar(50) NULL COMMENT '上报网点code';
/**
 * 关注中运单表（EC_CORE_ATTENTIONMAIL）
 * 2011-12-13
 * @auther wangyong
 */
CREATE TABLE `EC_CORE_ATTENTIONMAIL` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mail_no` varchar(50) DEFAULT NULL,			-- 运单号。
  `destination` varchar(100) DEFAULT NULL,      -- 目的地
  `buyer_name` varchar(50) DEFAULT NULL,		-- 买家姓名
  `buyer_phone` varchar(50) DEFAULT NULL,	    -- 买家电话
  `status` varchar(50) DEFAULT NULL,	        -- 状态
  `customer_id` varchar(50) DEFAULT NULL,       -- 客户编码
  `accept_time` timestamp DEFAULT '0000-00-00 00:00:00',         -- 运单流转时间（比如运单创建、揽收、送达时间等等）
  `send_time` timestamp DEFAULT '0000-00-00 00:00:00',           -- 运单发货时间
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 创建时间
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00', -- 更新时间
  `arrive_time` timestamp DEFAULT '0000-00-00 00:00:00',         -- 运单预计到达时间
  `date_out` int(11) DEFAULT NULL,              -- 是否超时(1：未超时，2：已超时)
  `backup` varchar(100) DEFAULT NULL,			-- 备用字段
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**
 * 关注中运单表（EC_CORE_ORDER_TEMP）
 * 2012-6-11
 * @auther wuguiqiang
 */
CREATE TABLE `ec_core_order_temp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mailNo` varchar(64) DEFAULT NULL COMMENT '订单编号',
  `tx_logistic_id` varchar(50) DEFAULT NULL COMMENT '运单号',
  `buyer_name` varchar(200) DEFAULT NULL COMMENT '买家姓名',
  `name` varchar(200) DEFAULT NULL COMMENT '收货人姓名',
  `prov` varchar(200) DEFAULT NULL COMMENT '收货省',
  `city` varchar(200) DEFAULT NULL COMMENT '收货市',
  `district` varchar(200) DEFAULT NULL COMMENT '收货区',
  `address` varchar(200) DEFAULT NULL COMMENT '收货人详细街道地址',
  `post_code` varchar(200) DEFAULT NULL COMMENT '邮编',
  `phone` varchar(200) DEFAULT NULL COMMENT '联系电话',
  `mobile` varchar(200) DEFAULT NULL COMMENT '手机',
  `send_name` varchar(100) DEFAULT NULL COMMENT '发件人姓名',
  `send_mobile` varchar(100) DEFAULT NULL COMMENT '发货人手机',
  `send_phone` varchar(100) DEFAULT NULL COMMENT '发货人电话',
  `create_time` varchar(100) DEFAULT NULL COMMENT '订单创建时间',
  `goods_name` varchar(200) DEFAULT NULL COMMENT '商品名称',
  `total_price` double DEFAULT NULL COMMENT '商品总价格',
  `single_price` double DEFAULT NULL COMMENT '商品单价',
  `quantity` int(11) DEFAULT NULL COMMENT '商品数量',
  `partitiondate` date NOT NULL,
  `enddate` date NOT NULL,
  `remark` varchar(100) DEFAULT NULL COMMENT '备注字段',
  PRIMARY KEY (`id`),
  KEY `tx_logistic_id` (`tx_logistic_id`) USING BTREE,
  KEY `mailNo` (`mailNo`) USING BTREE,
  KEY `partitiondate` (`partitiondate`) USING BTREE,
  KEY `remark` (`remark`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7941 DEFAULT CHARSET=utf8;

ALTER TABLE `ec_core_order_temp` CHANGE total_price total_price varchar(100) default null;
ALTER TABLE `ec_core_order_temp` CHANGE single_price single_price varchar(100) default null;
ALTER TABLE `ec_core_order_temp` CHANGE quantity quantity varchar(100) default null;
ALTER TABLE ec_core_order_temp ADD COLUMN send_address VARCHAR(100) default null COMMENT '发货地址' after send_name;

ALTER TABLE `ec_core_user`
ADD COLUMN `parentId`  int(11) NULL COMMENT '易通子账号有值，指向父ID' AFTER `id`;


ALTER TABLE `ec_core_userthread`
ADD COLUMN `user_code_update_time` timestamp NULL COMMENT '修改用户编码的时间戳' AFTER `switcheccount`;



ALTER TABLE ec_core_standard_posttemp ADD first_weight VARCHAR( 50 ) NULL COMMENT '首重重量';
update ec_core_standard_posttemp set  first_weight ='1';


ALTER TABLE ec_core_userthread ADD isprint VARCHAR( 1 ) DEFAULT 0 COMMENT '代表是否给网点或者承包区打印权限';
ALTER TABLE ec_core_user ADD isprint VARCHAR( 1 ) DEFAULT 0 COMMENT '代表是否给网点或者承包区打印权限';

--ec_core_userthread_contract_area表结构的变动 2012-05-25 update by yuyuezhong
alter table ec_core_userthread_contract_area change user_state account_type varchar(10) DEFAULT NULL COMMENT '账号类型：客服(21)，财务(22)，财务、客服(23)，承包区(2)'
alter table ec_core_userthread_contract_area change add_userName add_userName varchar(200) DEFAULT NULL COMMENT '网点创建子账号的名称'
alter table ec_core_userthread_contract_area drop user_code;
alter table ec_core_userthread_contract_area drop user_name;
alter table ec_core_userthread_contract_area drop switcheccount;


/**
 * 修改问题件表（ec_core_questionnaire）
 * 2012-05-30
 * @auther wangyong
 */
ALTER TABLE ec_core_questionnaire DROP COLUMN order_no;
ALTER TABLE ec_core_questionnaire DROP COLUMN tx_logistic_id;
ALTER TABLE ec_core_questionnaire DROP COLUMN destination;
ALTER TABLE ec_core_questionnaire DROP COLUMN vip_id;
ALTER TABLE ec_core_questionnaire DROP COLUMN vip_name;
ALTER TABLE ec_core_questionnaire DROP COLUMN vip_textname;
ALTER TABLE ec_core_questionnaire DROP COLUMN vip_phone;
ALTER TABLE ec_core_questionnaire DROP COLUMN vip_cellphone;
ALTER TABLE ec_core_questionnaire DROP COLUMN buy_username;
ALTER TABLE ec_core_questionnaire DROP COLUMN buy_userphone;
ALTER TABLE ec_core_questionnaire DROP COLUMN province;
ALTER TABLE ec_core_questionnaire DROP COLUMN city;
ALTER TABLE ec_core_questionnaire DROP COLUMN country;
ALTER TABLE ec_core_questionnaire DROP COLUMN address;
ALTER TABLE ec_core_questionnaire ADD COLUMN buyer_phone varchar(50) default null COMMENT '买家手机号';
ALTER TABLE ec_core_questionnaire ADD COLUMN buyer_mobile varchar(50) default null COMMENT '买家电话';
ALTER TABLE ec_core_questionnaire ADD COLUMN buyer_name varchar(50) default null COMMENT '买家姓名';
ALTER TABLE ec_core_questionnaire ADD COLUMN IMG1 varchar(100) default null COMMENT '图片地址';
ALTER TABLE ec_core_questionnaire ADD COLUMN IMG2 varchar(100) default null COMMENT '图片地址';
ALTER TABLE ec_core_questionnaire ADD COLUMN IMG3 varchar(100) default null COMMENT '图片地址';
ALTER TABLE ec_core_questionnaire ADD COLUMN IMG4 varchar(100) default null COMMENT '图片地址';

/**
 * 修改表ec_core_userthread_contract_area字段conract_area_id的注释变动。  '承包区id，即user表id'-->'承包区id，即userThread表id'
 */
alter table ec_core_userthread_contract_area 
change conract_area_id  conract_area_id int(11) NOT NULL COMMENT '承包区id，即userThread表id'
/**
 * 消息表
 * 2012-06-12
 * @auther wangyong
 */
CREATE TABLE `ec_core_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message_theme` varchar(200) DEFAULT NULL COMMENT '消息主题',
  `send_way` varchar(1) DEFAULT NULL,
  `message_type` int(11) DEFAULT NULL COMMENT '消息类型：1、卖家所发；2、网点所发；3、管理员所发；4、系统消息;5、平台用户所发',
  `send_user` varchar(20) DEFAULT NULL COMMENT '发件人：网点为id，卖家为userCode，管理员为id，平台用户为id',
  `receive_user` varchar(20) DEFAULT NULL COMMENT '接收人：网点为id，卖家为userCode，管理员为id，平台用户为id',
  `send_user_id` int(11) DEFAULT NULL COMMENT '发起人用户Id',
  `message_content` varchar(200) DEFAULT NULL COMMENT '消息内容',
  `send_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**
 * 消息用户关联表
 * 2012-06-12
 * @auther wangyong
 */
CREATE TABLE `ec_core_message_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message_id` int(11) NOT NULL COMMENT '消息主键id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `message_status` int(1) NOT NULL COMMENT '消息读取状态：1已读/0未读',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**
 * 消息回复表
 * 2012-06-12
 * @auther wangyong
 */
CREATE TABLE `ec_core_reply` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message_id` int(11) NOT NULL COMMENT '消息主键id',
  `reply_user` int(11) DEFAULT NULL COMMENT '回复人id',
  `reply_content` varchar(200) DEFAULT NULL COMMENT '回复内容',
  `reply_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**
 * 问题件表增加订单状态"order_status"
 * 2012-07-09
 * @auther wangyong
 */
ALTER TABLE ec_core_questionnaire ADD COLUMN order_status varchar(1) default '0' COMMENT '订单状态:1(已签收)；0(未签收)';

/**
 * 问题件表增加淘宝号("taobao_login_name")和交易号("tradeNo")两个字段
 *  @author wusha 2012-07-20
 */
ALTER TABLE ec_core_questionnaire ADD COLUMN taobao_login_name varchar(30) COMMENT '淘宝登录号';
ALTER TABLE ec_core_questionnaire ADD COLUMN tradeNo varchar(15) COMMENT '订单交易号';
/**
 * 问题件表添加分区
 */
ALTER TABLE ec_core_questionnaire ADD COLUMN partitiondate date NOT NULL DEFAULT '0000-00-00' COMMENT '分区字段';
ALTER TABLE `ec_core_questionnaire`  DROP PRIMARY KEY,  ADD PRIMARY KEY (`id`, `partitiondate`);
ALTER TABLE ec_core_questionnaire PARTITION BY RANGE COLUMNS(partitiondate)(PARTITION p20120723 VALUES LESS THAN ('2012-07-23'));

/**
 * 问题件存储过程
 */

-- Procedure "Set_Partition_Questionnaire" DDL

CREATE DEFINER=`eccore`@`%` PROCEDURE `Set_Partition_Questionnaire`()
begin
    declare exit handler for sqlexception rollback;
    start TRANSACTION;

   select REPLACE(partition_name,'p','') into @P12_Name from INFORMATION_SCHEMA.PARTITIONS where TABLE_SCHEMA='eccoredb' and table_name='ec_core_questionnaire' order by partition_ordinal_position DESC limit 1;

       set @Max_date= date(DATE_ADD(@P12_Name+0, INTERVAL 1 DAY))+0;

    SET @s1=concat('ALTER TABLE ec_core_questionnaire ADD PARTITION (PARTITION p',@Max_date,' VALUES less than (''',date(@Max_date),'''))');
    PREPARE stmt2 FROM @s1;
    EXECUTE stmt2;
    DEALLOCATE PREPARE stmt2;

    COMMIT ;
 end;

 /**
  * 问题件定时器
  */
CREATE EVENT `e_Set_Partition_Questionnaire` ON SCHEDULE
		EVERY 1 DAY STARTS '2012-07-23 23:59:59'
	ON COMPLETION NOT PRESERVE
	ENABLE
	COMMENT '问题件建立时间分区定时器'
	DO call Set_Partition_Questionnaire();
 

/**
 * 投诉表
 * 2012-07-10
 * @auther wangyong
 */
CREATE TABLE `ec_core_complaint` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `complaint_user` varchar(50) DEFAULT NULL COMMENT '投诉人',
  `site_user_code` varchar(50) DEFAULT NULL COMMENT '网点编码',
  `site_user_name` varchar(50) DEFAULT NULL COMMENT '网点名称',
  `mail_no` varchar(50) DEFAULT NULL COMMENT '运单号',
  `complaint_content` varchar(500) NOT NULL COMMENT '投诉内容',
  `exchange_content` varchar(2000) DEFAULT NULL COMMENT '问题件沟通记录',
  `last_time_for_user` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '卖家最后回复时间',
  `last_time_for_site` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '网点最后回复时间',
  `complaint_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '投诉时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**
 * 短信功能用到的所有表    start..........
 */

/**
 * 同步淘宝订单创建任务表
 * 2012-07-20 
 * @author wusha
 */
create table ec_core_order_taobao_task(
	id		int not null auto_increment,
	task_id	int not null comment '淘宝任务id',
	status  varchar(10) comment '任务状态',
	url		varchar(100) comment '任务完成之后返回的URL',
	start_date varchar(8) comment '从此时间开始获取订单',
	end_date 	varchar(8) comment '获取订单的结束时间',
	flag	varchar(1) comment '是否已下载此订单信息',
	userId               int(11) comment '主帐号的ID',  
	create_time          datetime comment '创建时间',
	update_time          datetime comment '更新时间',
    primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
alter table ec_core_order_taobao_task comment '同步淘宝订单创建任务表';

/**
 * 淘宝订单会员表
 */
create table ec_core_sms_buyers
(
   id                   int not null auto_increment,
   buyer_account        varchar(30) comment '买家的登录帐号', 
   receiver_name        varchar(50) comment '收货人姓名',
   receiver_mobile      varchar(15) comment '收货人手机号码',   
   receiver_postcode    varchar(15) comment '收货人邮编',
   receiver_province    varchar(20) comment '收货人所属省',
   receiver_city    	varchar(20) comment '收货人所属市',
   receiver_district     varchar(20) comment '收货人所属区',
   receiver_address     varchar(255) comment '收货人的具体联系地址',  
   source_status        varchar(10) comment '买家来源：淘宝，本系统中创建，导入等',
   marketing_send_count int comment '营销活动发送量',
   the_last_market_time datetime comment '上次活动时间',
   total_trade_count    int comment '总交易量',
   total_trade_amount   double comment '总交易额，单位为元',
   the_last_trade_time  datetime comment '上一次交易时间',
   userId               int(11) comment '主帐号的ID',  
   update_time          datetime comment '修改时间',
   update_userId        int(11) comment '修改人',
   create_time          datetime comment '创建时间',
   create_userId        int(11) comment '创建人',
   remark               varchar(255) comment '备注', 
   primary key (id)
);

alter table ec_core_sms_buyers comment '买家联系人表';


/**
 * 会员表添加固定电话字段
 */
ALTER TABLE ec_core_sms_buyers ADD COLUMN receiver_phone varchar(30) COMMENT '买家固定电话';

/**
 * 会员等级表
 */
create table ec_core_sms_buyers_grade
(
   id                   int not null auto_increment,
   high_account         double comment '高级会员为：大于此值交易额', 
   high_count           int comment '高级会员为：大于此值交易量', 
   vip_account          double comment 'VIP会员为：大于此值交易额', 
   vip_count            int comment 'VIP会员为：大于此值交易量', 
   vip_high_account     double comment '至尊VIP会员为：大于此值交易额', 
   vip_high_count       int comment '至尊VIP会员为：大于此值交易量', 
   userId               int(11) comment '主帐号的ID',  
   update_time          datetime comment '修改时间',
   update_userId        int(11) comment '修改人',
   create_time          datetime comment '创建时间',
   create_userId        int(11) comment '创建人',
   primary key (id)
);

alter table ec_core_sms_buyers_grade comment '买家等级标准';

/**
 * 搜索器表
 */
create table ec_core_sms_buyers_search
(
   id                   int not null auto_increment,
   search_name          varchar(50) comment '搜索器名字',
   search_condition     varchar(500) comment '搜索器内容',
   update_time          datetime comment '修改时间',
   update_userId        int(11) comment '修改人',
   create_time          datetime comment '创建时间',
   create_userId        int(11) comment '创建人',
   primary key (id)
);

alter table ec_core_sms_buyers_search comment '买家的搜索器(每个卖家针对每个短信类型，只允许创建5个)';

/**
 * 模版表
 */
create table ec_core_sms_template
(
   id                   int not null auto_increment,
   sms_type_id          int(11) comment '短信类型ID',
   name                 varchar(40) comment '模版标题',
   content              varchar(1000) comment '模板内容',   
   is_default           varchar(1) comment '是否是默认模板 Y 是 N 否(默认为N，当用户第一次为此短信类型创建模版时设置为Y)',
   status               varchar(1) comment 'Y 审核通过 N 未通过, M 审核中, D已删除 ',
   send_count           int(30) comment '通过此模版发送了多少条短信',
   audit_user           int comment '审核人',
   audit_time           datetime comment '审核时间',
   userId               int(11) comment '主帐号的ID',
   update_userId        int(11) comment '修改人',
   update_time          datetime comment '修改时间',
   create_time          datetime comment '创建时间即提交审核时间',
   create_userId        int(11) comment '创建模版的用户ID',
   remark               varchar(4000) comment '存放管理员审核失败的条件',
   primary key (id)
);

alter table ec_core_sms_template comment '短信模板表(针对短信类型)';

/**
 * 短信服务表
 */
create table ec_core_sms_service
(
   id                   int not null auto_increment,
   name                 varchar(20) comment '服务名称 比如：发货提醒、到货提醒、签收提醒、营销定制、问题件通知',
   image_url            varchar(50) comment '服务对应的图片',
   introduction         varchar(255) comment '服务简介',
   send_time_type       varchar(1) comment '此服务发送短信时间:例如：8：00--20：00之间为1,其他以此类推',
   is_on            	varchar(1) comment '标志此服务是否开启，Y已开启、 N为未开启，默认为N 问题件默认为开启，其他为未开启',  
   is_auto_send         varchar(1) comment '是否自动发送',
   userId               int(11) comment '主帐号的ID',
   update_userId        int(11) comment '修改人',
   update_time          datetime comment '修改时间',
   create_time          datetime comment '创建时间',
   create_userId        int(11) comment '创建服务的用户ID',
   primary key (id)
);

alter table ec_core_sms_service comment '短信管理的子服务(针对短信管理)';

/**
 *  服务地区对应表
 */
create table ec_core_sms_service_area
(
   id                   int not null auto_increment,
   sms_type_id          int(11) comment '短信类型ID',
   code                 varchar(500) comment '对应编码', 
   update_time          datetime comment '修改时间',
   update_userId        int(11) comment '修改人',
   create_time          datetime comment '创建时间',
   create_userId        int(11) comment '创建人',
   primary key (id)
);

alter table ec_core_sms_service_area comment '服务所对应的地区';

/**
 * 营销活动
 */
create table ec_core_sms_service_market
(
   id                   int not null auto_increment,
   name                 varchar(20) comment '营销活动名称（此字段预留）',
   userId               int(11) comment '主帐号的ID',
   create_time          datetime comment '创建时间',
   create_userId        int(11) comment '创建服务的用户ID',
   primary key (id)
);

alter table ec_core_sms_service_market comment '营销活动表'; 

/**
 * 营销活动所对应的买家
 */
create table ec_core_sms_service_buyers
(
   id                   int not null auto_increment,
   sms_market_id          int(11) comment '营销活动的ID',
   buy_id               int(11) comment '营销定制所对应的买家ID',  
   create_time          datetime comment '创建时间',
   create_userId        int(11) comment '创建人',
   primary key (id)
);

alter table ec_core_sms_service_buyers comment '营销定制服务所对应的买家';

/**
 * 短信详细信息
 * 
 *   ALTER TABLE `ec_core_sms_info` ADD COLUMN `is_del` int(1)  NOT NULL DEFAULT '0' COMMENT '逻辑删除判断标识 0不删除 1删除';
 *   alter table ec_core_sms_info add constraint mailNoMobileStatus unique(mailNo,buy_mobile,service_type);
 */
create table ec_core_sms_info
(
   id                   int not null auto_increment,
   sms_type_id          int(11) comment '服务ID', 
   sms_template_id      int(11) comment '模版ID',
   mailNo               varchar(10) comment '运单号',
   buy_name             varchar(25) comment '买家姓名',
   buy_login_name       varchar(25) comment '登录名称',
   buy_mobile           varchar(15) comment '买家手机号码',
   send_time            datetime comment '发送时间',
   content              varchar(1000) comment '短信内容',
   count				int comment '此内容以多少条发送',
   status               varchar(1) comment '短信状态 0 发送成功 1 等待发送 2 发送失败 3发送失败,再次发送中',
   service_type			varchar(10) comment '发货提醒(GOT)，收货提醒(SENT_SCAN)，签收提醒(SIGNED)',
   error_send			varchar(300) comment '发送失败原因',
   service_type			varchar(10) comment '短信类型',
   userId               int(11) comment '主帐号的ID',
   create_time          datetime comment '创建时间',
   create_userId        int(11) comment '创建人',
   primary key (id)
);

alter table ec_core_sms_info comment '短信的详细信息';

 ALTER TABLE `ec_core_sms_info` ADD COLUMN `is_del` int(1)  NOT NULL DEFAULT '0' COMMENT '逻辑删除判断标识 0不删除 1删除';
 
 alter table ec_core_sms_info add constraint orderUnique unique(mailNo, buy_mobile, buy_name);

/**
 * 过滤词
 */
CREATE TABLE ec_core_filter_rule (
  filterRuleId int(11) NOT NULL AUTO_INCREMENT COMMENT '参数id，无业务含义',
  type smallint(6) DEFAULT NULL COMMENT '过滤词类型\r\n1-问题件通知',
  words text COMMENT '敏感词',
  createTime timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  updateTime timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '最后更新（增加/删除/修改）日期。可做为全文检索的时间戳',
  PRIMARY KEY (filterRuleId)
);

alter table filter_rule comment '过滤规则表。一条记录为一个过滤规则，同一过滤规则中多个关键字请用"、"隔开。';

/**
 * 短信功能表    end..................
 */


/**
 * 网点查找纠错通知管理员发送信息，更改数据库信息字段长度
 * 2012-07-26
 * @author shitianzeng
 */
alter table ec_core_message modify column message_content varchar(500);

/**
 * 线上数据库与本地数据库不一致，为网点信息表字段branch_code添加唯一键约束
 * 2012-07-26
 * @author shitianzeng
 */
ALTER TABLE ec_core_branch ADD unique(branch_code);


/**
 * 线上数据库与本地数据库不一致，为会员表加上唯一性约束
 * 2012-07-27
 * @author lilongyong
 */
ALTER TABLE `ec_core_sms_buyers`  ADD UNIQUE INDEX `buyer_account_userId` (`buyer_account`, `userId`);

/**
 * 问题件表新增标签标示
 * 2012-08-6
 * @author wangyong
 */
ALTER TABLE ec_core_questionnaire ADD COLUMN tag_id int(11) COMMENT '标签id';

/**
 * 修改回复表字段长度
 * 2012-08-8
 * @author wangyong
 */
alter table ec_core_reply modify reply_content VARCHAR(500);
/**
 * 问题件表新增索引
 * 2012-07-29
 * @auther wangyong
 */
ALTER TABLE ec_core_questionnaire ADD INDEX index_buyer_phone(buyer_phone);  
ALTER TABLE ec_core_questionnaire ADD INDEX index_buyer_mobile(buyer_mobile); 
ALTER TABLE ec_core_questionnaire ADD INDEX index_buyer_name(buyer_name); 
ALTER TABLE ec_core_questionnaire ADD INDEX index_taobao_login_name(taobao_login_name);

/**
 * 网点增加运单监控权限
 * 2012-7-30
 * @auther wangyong
 */
insert into ec_core_configcode(pid,conf_key,conf_value,conf_text,conf_type,seq,remark,conf_level) 
values(94,'wangdian.net.ytoec.kernel.action.questionnaire.QuestionnaireMonitorAction','net.ytoec.kernel.action.questionnaire.QuestionnaireMonitorAction','运单监控',1,1,'网点的action权限',0);

/**
 * 添加ec_core_order_log
 * 2012-08-05
 * @auther mabo
 */
create table `ec_core_order_log` (
	`tx_logistic_id` varchar(50) not null,
	`create_time` timestamp not null default current_timestamp comment '创建时间',
	`update_time` timestamp null comment '淘宝发起的支付成功/支付失败时间',
	`payAmount` varchar(10) comment '支付金额',
	`payTime` varchar(100) comment '支付时间',
	`unitId` varchar(50) comment 'PDA发起支付时传入的网点id',
	`employeeId` varchar(50) comment 'PDA发起支付时传入的员工号',
	`remark` varchar(512), 
	primary key (`tx_logistic_id`)
) engine=InnoDB default charset=utf8

create table `ec_core_edm` (
	`id` integer(10) not null,
	`source_type` char(1)  null  comment '1:etong发邮件',
	`email` varchar(100) null comment '客户邮箱',
	`ip` varchar(100) null ,
    `create_time` timestamp not null default current_timestamp comment '创建时间',
	primary key (`id`)
) engine=InnoDB default charset=utf8

/**
 * 运费调整表更改调整原因字段为200字符    
 * by wusha   2012-08-09
 */
alter table ec_core_unlike_freight modify column reason varchar(250);

/**
 * 添加ec_core_waybill_print
 * 2012-09-04
 * @auther mabo
 */
create table `ec_core_waybill_print` (
	`id` int(11) not null auto_increment,
	`mailno` varchar(64) null default '' comment '运单号',
	`userId` int(11) null default 0 comment '用户Id',
	`appCode` varchar(50) null comment '系统或者模块的唯一标识',
	`tx_logistic_id` varchar(50) null default null comment '订单号',
	`deliverno` varchar(50) not null default '' comment '发货单号',
	`customer_id` varchar(50) not null comment '客户身份标识',
	`user_name` varchar(200) null default null comment '卖家姓名',
	`remark` varchar(4000) null default null comment '备注',
	`weight` float null default null comment '订单重量',
	`status` varchar(200) null default null comment '订单状态,预留字段',
	`is_print` varchar(1) null default 'N' comment '订单是否打印快递单标识，y为已打印，n为未打印 ，默认值为n',
	`buy_name` varchar(200) null default null comment '买家姓名',
	`buy_mobile` varchar(200) null default null comment '买家移动电话（手机）',
	`buy_telphone` varchar(200) null default null comment '买家固话',
	`buy_prov` varchar(200) null default null comment '买家所属省',
	`buy_city` varchar(200) null default null comment '买家的所属市',
	`buy_district` varchar(200) null default null comment '买家的所属地区',
	`buy_address` varchar(200) null default null comment '买家的地址，不包括省市区',
	`buy_postcode` varchar(6) null default null comment '买家的邮政编码',
	`date` date null default '0000-00-00' comment '发货单日期',
	`item_name` varchar(200) null comment '商品名称',
	`item_number` int not null default 0 comment '商品数量',
	`agency_fund` double null comment '代收款',
	`big_pen` varchar(50) not null comment '大头笔',
	`create_time` timestamp not null default current_timestamp,
	`update_time` timestamp not null default '0000-00-00 00:00:00',
	primary key (`id`)
)
comment='韩都衣舍运单打印表'
collate='utf8_general_ci'

/**
 * 添加发送短信历史记录表ec_core_paysms_info
 * 2012-09-18
 * @auther guolongchao
 */
CREATE TABLE `ec_core_paysms_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sms_type` varchar(2) DEFAULT '0' COMMENT '1 余额不足提醒 2交易提醒  3服务到期提醒 4短信不足提醒',
  `sequenceID` int(11) DEFAULT NULL COMMENT '其他模块短信的id',
  `user_id` int(11) DEFAULT NULL COMMENT '主账号id(其它系统则为名称)',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `send_time` timestamp NULL DEFAULT NULL COMMENT '易通平台发送时间',
  `deliver_time` timestamp NULL DEFAULT NULL COMMENT '客户手机接收时间',
  `send_mobile` varchar(30) DEFAULT NULL COMMENT '发送的手机号',
  `dest_mobile` varchar(30) NOT NULL COMMENT '目的手机号',
  `Pk_total` int(2) DEFAULT NULL COMMENT '此短信实际已多少条发送',
  `message_content` varchar(1000) DEFAULT NULL COMMENT '发送的内容',
  `status` varchar(2) DEFAULT '8' COMMENT '8 待发送 88 易通平台发送0 发送成功 1 网关失败 2 发送失败',
  `error_code` varchar(30) DEFAULT NULL COMMENT '消息错误码',
  `succ_total` int(2) DEFAULT '0' COMMENT '成功数量',
  `fail_total` int(2) DEFAULT '0' COMMENT '失败数量',
  `receive_name` varchar(30) DEFAULT NULL COMMENT '短信接收者姓名',
  PRIMARY KEY (`id`),
  KEY `index_sequenceID` (`sequenceID`),
  KEY `index_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/**
 * 将ec_core_paysms表里的status字段 短信发送错误的记录统统改为2
 */
update  ec_core_paysms_info set `status`=2 where  status!=0 and status!=8 and status!=88
/**
 * 将ec_core_paysms的数据转移到ec_core_paysms_info
 */
insert into ec_core_paysms_info   select * from ec_core_paysms where status!=88  and status!=8


/**
 * 使用mysql load data 时，
 * 将手机号码暂存到ec_core_phone_temp临时表中，
 * 对手机号码去重，验证手机号长度是11位后，
 * 批量转移到ec_core_sms_wait中
 *   2012-09-19
 * @auther guolongchao
 */
CREATE TABLE `ec_core_phone_temp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dest_mobile` varchar(11) DEFAULT NULL,
  `receive_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/**
 * 其他代理商所使用的待发送短信表ec_core_sms_wait，
 * 无线天利回调之后将数据转移到ec_core_paysms_info短信历史记录表中
 *  2012-09-19
 * @auther guolongchao
 */
CREATE TABLE `ec_core_sms_wait` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sms_type` varchar(2) DEFAULT '0' COMMENT '18 汉麻世家      15 本草堂     99 请选择--',
  `sequenceID` int(11) DEFAULT NULL COMMENT '其他模块短信的id',
  `user_id` int(11) DEFAULT NULL COMMENT '此处为空',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `send_time` timestamp NULL DEFAULT NULL COMMENT '易通平台发送时间',
  `deliver_time` timestamp NULL DEFAULT NULL COMMENT '客户手机接收时间',
  `send_mobile` varchar(30) DEFAULT NULL COMMENT '发送的手机号',
  `dest_mobile` varchar(30) NOT NULL COMMENT '目的手机号',
  `Pk_total` int(2) DEFAULT NULL COMMENT '此短信实际已多少条发送',
  `message_content` varchar(1000) DEFAULT NULL COMMENT '发送的内容',
  `status` varchar(2) DEFAULT '8' COMMENT '8 待发送 ',
  `error_code` varchar(30) DEFAULT NULL COMMENT '消息错误码',
  `succ_total` int(2) DEFAULT '0' COMMENT '成功数量',
  `fail_total` int(2) DEFAULT '0' COMMENT '失败数量',
  `receive_name` varchar(30) DEFAULT NULL COMMENT '短信接收者姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/**
 * 关联店铺表
 * 2012-09-25
 * @auther wangyong
 */
CREATE TABLE `ec_core_user_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `related_user_id` int(11) DEFAULT NULL COMMENT '被关联用户id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**
 * 添加操作人ID
 */
alter table ec_core_payment add column creater_id int(11) COMMENT '操作人id'
update ec_core_payment set creater_id=user_id

/**
 * 修订交易明细的历史数据
 */
update ec_core_payment
set deal_name = '开通时效提醒'
where deal_type = 1 and deal_name = '时效提醒'   -----原来的订购服务

update ec_core_payment
set deal_name = '续订时效提醒'
where deal_type = 2 and deal_name = '时效提醒'   ------原来的续费服务

update ec_core_payment
set deal_type = 2     
where (deal_name='开通时效提醒' or deal_name='续订时效提醒')   -----deal_type = 2  时效提醒             deal_type = 1  短信服务(目前交易明细表没有这样的数据)

update ec_core_payment
set deal_name = '购买600条短信'
where deal_name= '50元超值套餐'

update ec_core_payment
set deal_name = '购买1220条短信'
where deal_name= '100元超值套餐'

update ec_core_payment
set deal_name = '购买2500条短信'
where deal_name= '200元超值套餐'

update ec_core_payment
set deal_name = '购买6500条短信'
where deal_name= '500元超值套餐'

update ec_core_payment
set deal_name = '购买15000条短信'
where deal_name= '1000元超值套餐'

update ec_core_payment
set deal_name = '购买32000条短信'
where deal_name= '2000元超值套餐'

update ec_core_payment
set deal_name = '购买85000条短信'
where deal_name= '5000元超值套餐'

update ec_core_servicehistory
set deal_name = '购买600条短信'
where deal_name= '50元超值套餐'

update ec_core_servicehistory
set deal_name = '购买1220条短信'
where deal_name= '100元超值套餐'

update ec_core_servicehistory
set deal_name = '购买2500条短信'
where deal_name= '200元超值套餐'

update ec_core_servicehistory
set deal_name = '购买6500条短信'
where deal_name= '500元超值套餐'

update ec_core_servicehistory
set deal_name = '购买15000条短信'
where deal_name= '1000元超值套餐'

update ec_core_servicehistory
set deal_name = '购买32000条短信'
where deal_name= '2000元超值套餐'

update ec_core_servicehistory
set deal_name = '购买85000条短信'
where deal_name= '5000元超值套餐'

update ec_core_payment
set deal_name = '在线充值'+ deal_Money+'元'
where deal_name= '在线充值' and deal_type=0


/**
 * 网点添加权限
 * 2012-10-9
 * @auther wangyong
 */
insert into ec_core_configcode(pid,conf_key,conf_value,conf_text,conf_type,seq,remark,conf_level) 
values(94,'wangdian.net.ytoec.kernel.action.questionnaire.AttentionMailAction','net.ytoec.kernel.action.questionnaire.AttentionMailAction','关注运单',1,1,'网点的action权限',0);
insert into ec_core_configcode(pid,conf_key,conf_value,conf_text,conf_type,seq,remark,conf_level) 
values(95,'wangdian_kefu.net.ytoec.kernel.action.questionnaire.AttentionMailAction','net.ytoec.kernel.action.questionnaire.AttentionMailAction','关注运单',1,1,'网点客服的action权限',0);

/**
 * 客户关怀和网点代付相关表     始 。。。。。。。
 */
CREATE TABLE ec_core_order_amount (
	id int(11) NOT NULL AUTO_INCREMENT,
	seller_id    int(11) default NULL COMMENT ' 用户id',
	site_id      int(11) default NULL COMMENT ' 网点id',
	amount       int COMMENT '单个用户对应的订单量',  
	sms_count     int COMMENT '网点赠送给卖家的短信数',
	time_alert     int COMMENT '网点赠送给卖家时效提醒的月份',
	create_time timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time timestamp NULL DEFAULT NULL COMMENT '更新时间',
	primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE ec_core_message_alert(
	id int(11) NOT NULL AUTO_INCREMENT,
	seller_id    int(11) default NULL COMMENT ' 用户id',
	site_id      int(11) default NULL COMMENT ' 网点id',
	system_message  varchar(200)  DEFAULT NULL COMMENT  '系统根据赠送或者代付内容自动产生的',   
	dialogue_message varchar(200)  DEFAULT NULL COMMENT '卖家申请代付时发送的消息',  
	is_active  varchar(1)  DEFAULT NULL COMMENT '消息是否有效：0有效，1无效',         
	status   varchar(1)  DEFAULT NULL COMMENT '网点发给卖家0， 卖家发给网点1',           
	pay_id   int(11) default NULL COMMENT ' 网点代付id',           
	create_time timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time timestamp NULL DEFAULT NULL COMMENT '更新时间',
	primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE ec_core_sendmessage_log (
    id int(11) NOT NULL AUTO_INCREMENT,
	site_id int(11) default NULL COMMENT '网点id',
	user_id int(11) default NULL COMMENT '用户id',
	message  varchar(1)  DEFAULT NULL COMMENT '赠送短信内容',  
	type varchar(1) DEFAULT NULL COMMENT '时效提醒1，短信2',
	create_time timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `zebra_partern` (
  `partern_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customer_code` varchar(255) NOT NULL DEFAULT '' COMMENT '商家代码',
  `partern_code` varchar(255) NOT NULL COMMENT '商家密匙',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`partern_id`),
  UNIQUE KEY `customer_code` (`customer_code`)
) ENGINE=InnoDB AUTO_INCREMENT=53296 DEFAULT CHARSET=utf8;

CREATE TABLE `zebra_upload_surfacebill` (
  `waybill_no` varchar(100) NOT NULL COMMENT '主键(仓配通回传给易通的电子面单号)',
  `customer_code` varchar(255) NOT NULL COMMENT '商家代码',
  `order_no` varchar(255) NOT NULL COMMENT '订单号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`waybill_no`),
  KEY `customer_code` (`customer_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `zebra_sequence` (
  `sequence_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键(请求序列)',
  `customer_code` varchar(255) NOT NULL COMMENT '商家代码',
  `partern_code` varchar(255) NOT NULL COMMENT '商家密钥',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`sequence_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8170 DEFAULT CHARSET=utf8;

CREATE TABLE `zebra_surfacebill` (
  `waybill_no` varchar(100) NOT NULL DEFAULT '' COMMENT '运单编号',
  `order_no` varchar(100) DEFAULT NULL COMMENT '订单编号',
  `current_user_no` varchar(11) DEFAULT NULL COMMENT '当前用户编码',
  `fb_createdate` datetime DEFAULT NULL COMMENT '面单创建日期',
  `fb_updatedate` datetime DEFAULT NULL COMMENT '面单更改日期',
  `fb_downloaddate` datetime DEFAULT NULL COMMENT '下载时间',
  `fb_uploaddate` datetime DEFAULT NULL COMMENT '上传时间',
  `fb_printdate` datetime DEFAULT NULL COMMENT '打印时间',
  `fbsend_status` int(2) DEFAULT NULL COMMENT '发送状态',
  `fbsend_times` int(2) DEFAULT NULL COMMENT '发送次数',
  `fbsend_describe` varchar(200) DEFAULT NULL COMMENT '发送状态描述',
  `version_no` varchar(20) DEFAULT NULL,
  `is_use` tinyint(1) DEFAULT NULL COMMENT '是否使用过，0：未使用，1：已使用(也就是是否下发给仓配通(0：否，1：是))',
  `is_print` tinyint(1) DEFAULT NULL COMMENT '是否打印(0:否，1：是)',
  PRIMARY KEY (`waybill_no`),
  KEY `current_user_no` (`current_user_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `zebra_job_status` (
  `job_name` varchar(50) NOT NULL,
  `job_status` int(1) NOT NULL,
  PRIMARY KEY (`job_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `zebra_forewarn` (
  `forewarn_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `branck_id` int(11) NOT NULL COMMENT '网点id',
  `customer_code` varchar(255) NOT NULL COMMENT '商家代码(对应ec_core_user表的user_name)',
  `branck_warn_value` int(11) NOT NULL DEFAULT '0' COMMENT '网点采购预警值',
  `customer_warn_value` int(11) NOT NULL DEFAULT '0' COMMENT '商家号码池预警值',
  `phone` varchar(20) NOT NULL DEFAULT '0' COMMENT '手机号码',
  `remark_phone` varchar(20) NOT NULL DEFAULT '0' COMMENT '备用号码',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `phone_warn` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否短信报警',
  `email_warn` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否邮箱预警',
  `crate_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `init_state` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否是初始状态(0:否，1:是)， 当易通给仓配通下发过电子面单后，状态变为不是初始状态',
  `warn_state` tinyint(4) NOT NULL DEFAULT '0' COMMENT '商家是否处于预警状态(0:否，1:是), 某商家某一段时间处于预警状态时，只发送一条短信，发送后，处于不可再次发送状态，直到网点给商家采购好电子面单，并同步到易通, 此时商家处于正常状态',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`forewarn_id`),
  UNIQUE KEY `customer_code` (`customer_code`)
) ENGINE=MyISAM AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

INSERT INTO zebra_partern(customer_code) SELECT user_code FROM ec_core_userthread

/**
 * 给网点添加权限
 */
insert into `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) values('310','94','wangdian.net.ytoec.kernel.action.forewarn.ForewarnAction','net.ytoec.kernel.action.forewarn.ForewarnAction','预警','1','1','网点的action权限','0');
insert into `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) values('311','93','admin.net.ytoec.kernel.action.user.SellerInfoAction','net.ytoec.kernel.action.user.SellerInfoAction','商家账号管理','1','1','商家账号action权限','0');
insert into `ec_core_configcode` (`id`, `pid`, `conf_key`, `conf_value`, `conf_text`, `conf_type`, `seq`, `remark`, `conf_level`) values('312','312','35__orderCreate_key','2013-06-11 19:42:23','订单创建更新时间','1','1','定时器启动下次查询的开始时间','0');

/**
 * 给渠道表添加字段
 */
ALTER TABLE ec_core_channel_info add column user_name varchar(255);
ALTER TABLE ec_core_channel_info add column user_code varchar(255);

/**
 * 结束 。。。。。。
