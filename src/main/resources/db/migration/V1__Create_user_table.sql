# mvn flyway:migrate
# 使用flyway后，无论对表的改动或大或小，都不允许在原来的表上直接修改
# 需要新建一个sql脚本，使用mvn flyway:migrate命令，可以避免冲突
create table user(
	id int primary key auto_increment,
    account_id varchar(100),
    name varchar(50),
    token char(36),
    gmt_create bigint,
    gmt_modified bigint
);