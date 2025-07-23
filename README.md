# 数据库脚本
```sql
create table time_flow.task
(
    id             bigint auto_increment comment '唯一标识'
        primary key,
    title          varchar(255) not null comment '标题',
    start_date     date         not null comment '当日',
    start_time     time         not null comment '创建时间',
    finish_date    date         null comment '结束日',
    finish_time    time         null comment '结束时间',
    first_tag      varchar(255) null comment '一级标签',
    second_tag     varchar(255) null comment '二级标签',
    status         tinyint      null comment '1: 记录中；2:已完成',
    platform       tinyint      not null comment '平台: 1: PC 端；2:移动端',
    user_id        bigint          null,
    is_in_progress tinyint(1) as (if((`status` = 1), 1, NULL)),
    constraint idx_user_in_progress
        unique (user_id, is_in_progress)
)
    comment '记录表'

create table tag
(
    id     bigint auto_increment comment '编号'
        primary key,
    name   varchar(255) not null comment '名称',
    parent bigint default 0 null comment '父标签',
    user_id bigint comment '用户id'
) comment '标签表';
insert into tag value (1,'工作',0,1);
insert into tag value (2,'学习',0,1);
insert into tag value (3,'健康',0,1);
insert into tag value (4,'社交',0,1);
insert into tag value (5,'休息',0,1);

insert into tag value (default,'社会工作',1,1);
insert into tag value (default,'自研',1,1);
insert into tag value (default,'阅读',2,1);
insert into tag value (default,'课程',2,1);
insert into tag value (default,'无氧',3,1);
insert into tag value (default,'冥想',3,1);
insert into tag value (default,'跑步',3,1);
insert into tag value (default,'骑行',3,1);

create table user
(
    id       bigint auto_increment comment '编号'
        primary key,
    username varchar(255) not null comment '用户名',
    password varchar(255) not null comment '密码',
    nickname varchar(255) null comment '昵称',
    avatar   varchar(255) null comment '头像',
    sex      tinyint      not null comment '性别：1:男 2:女',
    phone    varchar(11)  not null comment '手机号',
    email    varchar(50) null comment '邮箱',
    constraint email
        unique (email),
    constraint phone
        unique (phone),
    constraint username
        unique (username)
)comment '用户表';
insert into user value (1,'xiaoyao','$2a$12$/2XyzTJ2uqETGLW7xzeSZ.n5KSr8UHTDfKGMXaDyckIpjsfePr.Ki','逍遥','https://pic1.imgdb.cn/item/675533b5d0e0a243d4dfdff8.webp',1,'15822054833','1214166598@qq.com');
```