drop table workitem_tb;
drop table case1_tb;
drop table workitem_step_tb;

create table case1_tb(
 workItemId varchar(50),
 bid varchar(50),
 payload varchar(50)
);

create table workitem_tb(
 workItemId varchar(50),
 name varchar(50),
 creator varchar(50),
 lastupdatets timestamp default CURRENT_TIMESTAMP,
 duedate timestamp default CURRENT_TIMESTAMP ,
 status tinyint default 0,
 memo varchar(300),
 routePolicy varchar(100),
 routePolicyMeta varchar(300)
);

create table workitem_step_tb(
 stepId varchar(50),
 workItemId varchar(50),
 name varchar(50),
 owner varchar(50),
 receivedTs timestamp default CURRENT_TIMESTAMP,
 processedTs timestamp default CURRENT_TIMESTAMP,
 duedate timestamp default CURRENT_TIMESTAMP ,
 status tinyint default 0,
 memo varchar(30),
 preSid  varchar(50)
);

