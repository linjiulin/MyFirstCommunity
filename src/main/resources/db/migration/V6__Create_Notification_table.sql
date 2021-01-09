create table NOTIFICATION
(
    id int auto_increment primary key,
    notifier int not null,
    receiver int not null,
    outer_Id int not null,
    type int not null,
    gmt_create BIGINT not null,
    status int default 0 not null,
    notifier_name VARCHAR(50) not null ,
    outer_title VARCHAR(50) not null
);


