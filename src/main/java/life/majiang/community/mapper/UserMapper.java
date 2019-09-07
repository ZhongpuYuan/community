package life.majiang.community.mapper;

import life.majiang.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

// command + o：自动移除无用导包
@Repository
@Mapper
public interface UserMapper {
    @Insert("insert into USER(name,account_id,token,gmt_create,gmt_modified) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    public void insert(User user);// 插入操作会自动调用get方法

    // #{token}：编译时，将形参的值放入token
    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);// 当参数是User(类)时，不需要@Param注解
}
