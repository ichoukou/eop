SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `surfacebill`;
CREATE TABLE `surfacebill` (
  `mail_no` varchar(255) NOT NULL,
  `customer_code` varchar(255) NOT NULL,
  `sequence` int(11) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`mail_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;