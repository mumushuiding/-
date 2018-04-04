DROP database IF EXISTS `hrm`;
create database hrm;
USE hrm;

-- ----------------------------
-- Table structure for dept
-- ----------------------------
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept` (
  `did` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`did`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LOGINNAME` varchar(32) DEFAULT NULL,
  `PASSWORD` varchar(32) DEFAULT NULL,
  `USERNAME` varchar(32) DEFAULT NULL,
  `SEX` int(11) DEFAULT NULL,
  `did` int(11) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `phone` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `phone` (`phone`),
  KEY `did` (`did`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`did`) REFERENCES `dept` (`did`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8;

DROP Table IF EXISTS `leaveinfo`;
create table `leaveinfo`(
  `LID` varchar(64) not null,
  `LEAVEMSG` text DEFAULT NULL,
  `STATUS` VARCHAR(32) DEFAULT NULL,
  PRIMARY KEY (`LID`)
)ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8;



DROP Table IF EXISTS `action`;
create table `action`(
  `actionid` int(11) not null AUTO_INCREMENT,
  `actionname` varchar(32) DEFAULT null,
  `actioncolumnid` int(11) DEFAULT null,
  PRIMARY KEY (`actionid`)

)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP table if EXISTS `actioncolumn`;
create table `actioncolumn`(
  `actioncolumnid` int(11) not null AUTO_INCREMENT,
  `actioncolumnname`varchar(32) DEFAULT null,
  PRIMARY KEY (`actioncolumnid`)
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP table if EXISTS `actiongroup`;
create table `actiongroup`(
  `actiongroupid` int(11) not null AUTO_INCREMENT,
  `actionid` int(11) DEFAULT null,
  `groupid`  int(11) DEFAULT null,
  `masterid` int(11) DEFAULT null,
  `mastername` varchar(32) DEFAULT null,
  `createdate` date DEFAULT null,
  PRIMARY KEY (`actiongroupid`)
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP table if EXISTS `groupmanager`;
create table `groupmanager`(
  `groupmanagerid` int(11) not null AUTO_INCREMENT,
  `userid` int(11) DEFAULT null,
  `username` varchar(32) DEFAULT null,
  `groupid` int(11) DEFAULT null,
  `creatorid` int(11) DEFAULT null,
  `creatorname` varchar(32) DEFAULT null,
  `createdate` date DEFAULT null,
  PRIMARY KEY (`groupmanagerid`)

)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

DROP table if EXISTS `group`;
create table `group`(
  `groupid` int(11) not null AUTO_INCREMENT,
  `groupname` varchar(32) DEFAULT null,
  PRIMARY KEY (`groupid`)
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
