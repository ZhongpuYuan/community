# mvn flyway:migrate
create table user(
	id int primary key auto_increment,
    account_id varchar(100),
    name varchar(50),
    token char(36),
    gmt_create bigint,
    gmt_modified bigint
);