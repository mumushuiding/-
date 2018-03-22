create database hrm;
USE hrm;

create table dept(
	did int(11) PRIMARY key AUTO_INCREMENT,
	name varchar(32) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8; 
insert into dept(name) VALUES('财务部');
insert into dept(name) VALUES('人事部');
CREATE TABLE `user` (
  `ID` int(11) PRIMARY key AUTO_INCREMENT,
  `LOGINNAME` varchar(32) DEFAULT NULL,
  `PASSWORD` varchar(32) DEFAULT NULL,
  `USERNAME` varchar(32) DEFAULT NULL,
  `SEX` int(11) DEFAULT NULL,
  `did` int(11) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  phone varchar(32)  unique DEFAULT NULL,
  FOREIGN KEY (did) references dept(did)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8;

insert into user (loginname,PASSWORD,did) values('admin1','123',100);
insert into user (loginname,PASSWORD,did) values('admin2','123',100);

insert into user (loginname,PASSWORD,did) values('admin3','123',101);

insert into user (loginname,PASSWORD,did) values('admin4','123',101);

