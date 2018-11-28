/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50167
Source Host           : localhost:3306
Source Database       : surfacebill

Target Server Type    : MYSQL
Target Server Version : 50167
File Encoding         : 65001

Date: 2013-11-20 15:47:07
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `surfacebill_upload`
-- ----------------------------
DROP TABLE IF EXISTS `surfacebill_upload`;
CREATE TABLE `surfacebill_upload` (
  `tx_logistic_id` varchar(64) NOT NULL COMMENT '物流订单号',
  `mail_no` varchar(64) DEFAULT NULL COMMENT '运单号',
  `customer_id` varchar(64) NOT NULL COMMENT '客户身份标识',
  `client_id` varchar(64) NOT NULL COMMENT '电商标识',
  `trader_no` varchar(64) DEFAULT NULL COMMENT '交易号',
  `order_type` int(11) NOT NULL DEFAULT '1' COMMENT '订单类型',
  `order_service` int(11) NOT NULL DEFAULT '0' COMMENT '服务类型',
  `deliver_no` varchar(64) DEFAULT NULL COMMENT '发货单号',
  `order_status` varchar(10) NOT NULL DEFAULT 'N' COMMENT '是否下单给易通,N为未下单 Y已下单',
  `sale_name` varchar(200) DEFAULT NULL COMMENT '卖家姓名',
  `sale_mobile` varchar(200) DEFAULT NULL COMMENT '卖家手机',
  `sale_telphone` varchar(200) DEFAULT NULL COMMENT '卖家电话',
  `sale_prov` varchar(200) DEFAULT NULL COMMENT '卖家所属省份',
  `sale_city` varchar(200) DEFAULT NULL COMMENT '卖家所属市',
  `sale_district` varchar(200) DEFAULT NULL COMMENT '卖家所属区',
  `sale_address` varchar(200) DEFAULT NULL COMMENT '卖家详细地址，不要省市区',
  `sale_postcode` varchar(6) DEFAULT NULL COMMENT '卖家邮政编码',
  `buy_name` varchar(200) DEFAULT NULL COMMENT '买家姓名',
  `buy_mobile` varchar(200) DEFAULT NULL COMMENT '买家移动电话（手机）',
  `buy_telphone` varchar(200) DEFAULT NULL COMMENT '买家固话',
  `buy_prov` varchar(200) DEFAULT NULL COMMENT '买家所属省',
  `buy_city` varchar(200) DEFAULT NULL COMMENT '买家的所属市',
  `buy_district` varchar(200) DEFAULT NULL COMMENT '买家的所属地区',
  `buy_address` varchar(200) DEFAULT NULL COMMENT '买家的地址，不包括省市区',
  `buy_postcode` varchar(6) DEFAULT NULL COMMENT '买家的邮政编码',
  `commodity_info` varchar(6000) DEFAULT NULL COMMENT '商品信息，格式为：商品1,数量,单价;商品2,数量,单价;',
  `send_start_time` timestamp NULL DEFAULT NULL COMMENT '物流取货时间',
  `send_end_time` timestamp NULL DEFAULT NULL COMMENT '物流取货时间',
  `insurance_value` double DEFAULT NULL COMMENT '保值金额',
  `package_or_not` varchar(50) NOT NULL DEFAULT 'NO' COMMENT '是否需要包装',
  `special` varchar(50) DEFAULT NULL COMMENT '特殊包裹',
  `weight` double DEFAULT NULL COMMENT '订单重量',
  `vip_id` varchar(50) DEFAULT NULL COMMENT 'user表的Id',
  `line_type` varchar(50) DEFAULT NULL COMMENT '0:online 1:offline',
  `weight_update_flag` varchar(50) DEFAULT NULL COMMENT '订单重量更新状态',
  `goods_value` double DEFAULT NULL COMMENT '商品金额+运费',
  `items_value` double DEFAULT NULL COMMENT '商品金额',
  `total_service_fee` double DEFAULT NULL COMMENT '总服务费',
  `buy_service_fee` double DEFAULT NULL COMMENT '买家服务费',
  `accept_time` timestamp NULL DEFAULT NULL COMMENT '状态变更时间',
  `cod_split_fee` double DEFAULT NULL COMMENT 'cod分润',
  `freight_type` char(1) DEFAULT NULL COMMENT '是否为调整运费订单',
  `freight` double DEFAULT NULL,
  `trim_freight` double DEFAULT NULL,
  `is_print` varchar(1) DEFAULT 'N' COMMENT '订单是否打印快递单标识，y为已打印，n为未打印 ，默认值为n',
  `agency_fund` double DEFAULT NULL COMMENT '代收款',
  `big_pen` varchar(50) DEFAULT NULL COMMENT '大头笔',
  `date` date DEFAULT '0000-00-00' COMMENT '发货单日期',
  `item_name` varchar(200) DEFAULT NULL COMMENT '商品名称',
  `item_number` int(11) NOT NULL DEFAULT '0' COMMENT '商品数量',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `child_logistic_id` varchar(64) DEFAULT NULL,
  `upload_result` varchar(1) DEFAULT 'W' COMMENT '订单上传是否成功，Y为成功，N为失败,W为等待上传，默认为W',
  `failed_reason` varchar(1000) DEFAULT NULL COMMENT '订单上传失败原因',
  PRIMARY KEY (`tx_logistic_id`),
  KEY `IDX_deliver_no` (`deliver_no`),
  KEY `IDX_is_print` (`is_print`),
  KEY `IDX_deliverNo_print` (`deliver_no`,`is_print`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of surfacebill_upload
-- ----------------------------
INSERT INTO `surfacebill_upload` VALUES ('DD00015296', '8876543346', 'CANGPEITONG', 'CANGPEITONG', '', '1', '0', '', 'N', '圆通', '', '', '上海', '上海市', '徐汇区', '桂林路396号1号楼106', '200000', '王春梅', '18668750991', '', '浙江省', '温州市', '乐清市', '北白象镇 进港大道金龙科技园', '325600', '', '2013-10-28 08:42:36', '2013-10-28 08:42:36', '0', 'NO', '', '0', '', '', '', '0', '0', '0', '0', '2013-10-28 08:42:36', '0', '', '0', '0', 'Y', '0', '温州', null, '徐福记 熊博士橡皮糖 果汁软糖 儿童糖 休闲零食 香甜Q弹  60g*3高酸果味', '1', '', '2013-10-28 08:42:36', '2013-10-28 08:42:36', '', 'W', '');
INSERT INTO `surfacebill_upload` VALUES ('DD00015297', '8876543345', 'CANGPEITONG', 'CANGPEITONG', '', '1', '0', '', 'N', '圆通', '', '', '上海', '上海市', '徐汇区', '桂林路396号1号楼106', '200000', '王春梅', '18668750991', '', '浙江省', '温州市', '乐清市', '北白象镇 进港大道金龙科技园', '325600', '', '2013-10-28 08:42:36', '2013-10-28 08:42:36', '0', 'NO', '', '0', '', '', '', '0', '0', '0', '0', '2013-10-28 08:42:36', '0', '', '0', '0', 'Y', '0', '温州', null, '徐福记 熊博士橡皮糖 果汁软糖 儿童糖 休闲零食 香甜Q弹  60g*3高酸果味', '1', '', '2013-10-28 08:42:36', '2013-10-28 08:42:36', '', 'W', '');
INSERT INTO `surfacebill_upload` VALUES ('DD00015298', '8876543344', 'CANGPEITONG', 'CANGPEITONG', '', '1', '0', '', 'N', '圆通', '', '', '上海', '上海市', '徐汇区', '桂林路396号1号楼106', '200000', '王春梅', '18668750991', '', '浙江省', '温州市', '乐清市', '北白象镇 进港大道金龙科技园', '325600', '', '2013-10-28 08:42:36', '2013-10-28 08:42:36', '0', 'NO', '', '0', '', '', '', '0', '0', '0', '0', '2013-10-28 08:42:36', '0', '', '0', '0', 'Y', '0', '温州', null, '徐福记 熊博士橡皮糖 果汁软糖 儿童糖 休闲零食 香甜Q弹  60g*3高酸果味', '1', '', '2013-10-28 08:42:36', '2013-10-28 08:42:36', '', 'W', '');
INSERT INTO `surfacebill_upload` VALUES ('DD00015299', '8876543343', 'CANGPEITONG', 'CANGPEITONG', '', '1', '0', '', 'N', '圆通', '', '', '上海', '上海市', '徐汇区', '桂林路396号1号楼106', '200000', '王春梅', '18668750991', '', '浙江省', '温州市', '乐清市', '北白象镇 进港大道金龙科技园', '325600', '', '2013-10-28 08:42:36', '2013-10-28 08:42:36', '0', 'NO', '', '0', '', '', '', '0', '0', '0', '0', '2013-10-28 08:42:36', '0', '', '0', '0', 'Y', '0', '温州', null, '徐福记 熊博士橡皮糖 果汁软糖 儿童糖 休闲零食 香甜Q弹  60g*3高酸果味', '1', '', '2013-10-28 08:42:36', '2013-10-28 08:42:36', '', 'W', '');
INSERT INTO `surfacebill_upload` VALUES ('DD00015300', '8876543342', 'CANGPEITONG', 'CANGPEITONG', '', '1', '0', '', 'N', '圆通', '', '', '上海', '上海市', '徐汇区', '桂林路396号1号楼106', '200000', '王春梅', '18668750991', '', '浙江省', '温州市', '乐清市', '北白象镇 进港大道金龙科技园', '325600', '', '2013-10-28 08:42:36', '2013-10-28 08:42:36', '0', 'NO', '', '0', '', '', '', '0', '0', '0', '0', '2013-10-28 08:42:36', '0', '', '0', '0', 'Y', '0', '温州', null, '徐福记 熊博士橡皮糖 果汁软糖 儿童糖 休闲零食 香甜Q弹  60g*3高酸果味', '1', '', '2013-10-28 08:42:36', '2013-10-28 08:42:36', '', 'W', '');
INSERT INTO `surfacebill_upload` VALUES ('DD00015301', '8876543341', 'CANGPEITONG', 'CANGPEITONG', '', '1', '0', '', 'N', '圆通', '', '', '上海', '上海市', '徐汇区', '桂林路396号1号楼106', '200000', '王春梅', '18668750991', '', '浙江省', '温州市', '乐清市', '北白象镇 进港大道金龙科技园', '325600', '', '2013-10-28 08:42:36', '2013-10-28 08:42:36', '0', 'NO', '', '0', '', '', '', '0', '0', '0', '0', '2013-10-28 08:42:36', '0', '', '0', '0', 'Y', '0', '温州', null, '徐福记 熊博士橡皮糖 果汁软糖 儿童糖 休闲零食 香甜Q弹  60g*3高酸果味', '1', '', '2013-10-28 08:42:36', '2013-10-28 08:42:36', '', 'W', '');
INSERT INTO `surfacebill_upload` VALUES ('DD00890369', '8876543330', 'CANGPEITONG', 'CANGPEITONG', '', '1', '0', '', 'N', '汇洁', '', '', '上海', '上海', '长宁东', '上海市闵行区金辉路1888号2号楼1楼', '201106', '王彦娜', '18668750991', '', '18610695236', '北京', '北京市 ', '朝阳区 ', '', '', '2013-10-23 11:35:47', '2013-10-23 11:35:47', '0', 'NO', '', '0', '', '', '', '0', '0', '0', '0', '2013-10-23 11:35:47', '0', '', '0', '0', 'Y', '0', '北京', null, 'C15-1-4-4,', '1', '', '2013-10-23 11:35:44', '2013-10-23 11:35:44', '', 'W', '');
