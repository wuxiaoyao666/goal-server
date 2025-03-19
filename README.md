# 数据库脚本
```sql
create table record(
   id bigint primary key auto_increment comment '唯一标识',
   title varchar(255) not null comment '标题',
   start_time time not null comment '创建时间',
   end_time time comment '结束时间',
   today date not null comment '当天',
   tag_id bigint comment '所属标签 ID',
   first_tag varchar(255) comment '一级标签',
   second_tag varchar(255) comment '二级标签'
)comment '记录表';

create table tag(
 id bigint primary key auto_increment comment '编号',
 name varchar(255) not null comment '名称',
 parent bigint default 0 comment '父标签'
)comment '标签表';

insert into tag value (1,'工作',0);
insert into tag value (2,'学习',0);
insert into tag value (3,'健康',0);
insert into tag value (4,'社交',0);
insert into tag value (5,'休息',0);

insert into tag value (default,'社会工作',1);
insert into tag value (default,'阅读',2);
insert into tag value (default,'课程',2);
insert into tag value (default,'运动',3);
insert into tag value (default,'冥想',3);

create table user(
    id int primary key auto_increment comment '编号',
    username varchar(255) not null unique comment '用户名',
    password varchar(255) not null comment '密码',
    nickname varchar(255) comment '昵称',
    sex tinyint not null comment '性别：1:男 2:女',
    phone varchar(11) not null unique comment '手机号',
    email varchar(50) unique comment '邮箱'
)comment '用户表';

insert into user value (default,'xiaoyao','$2a$12$/2XyzTJ2uqETGLW7xzeSZ.n5KSr8UHTDfKGMXaDyckIpjsfePr.Ki','逍遥',1,'15822054833','1214166598@qq.com');
```