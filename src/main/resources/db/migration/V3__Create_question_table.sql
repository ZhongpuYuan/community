# ctrl + r + mvn：在终端中快速搜索使用过的命令
# 在执行flyway:migrate之前，先执行flyway:repair，之后执行新的flyway脚本，会忽略原有的checksum值
create table question
(
    id int auto_increment primary key,
    title varchar(50),
    description text,
    gmt_create bigint,
    gmt_modified bigint,
    creator int,
    comment_count int default 0,
    view_count int default 0,
    like_count int default 0,
    tag varchar(256)
);