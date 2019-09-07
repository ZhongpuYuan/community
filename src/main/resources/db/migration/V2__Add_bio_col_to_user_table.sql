# 使用flyway后，无论对表的改动或大或小，都不允许在原来的表上直接修改
# 需要新建一个sql脚本，使用mvn flyway:migrate命令，可以避免冲突
alter table user add bio varchar(256) null;