package life.majiang.community.mapper;

import life.majiang.community.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

// command + o：自动移除无用导包

/**
 * 异常：Unknown column 'gmt_modified,avatar_url' in 'field list'
 *
 * 原因：在Insert语句中，使用中文的逗号
 */
@Repository
@Mapper
public interface UserMapper {
    @Insert("insert into user(name,account_id,token,gmt_create,gmt_modified,avatar_url) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    public void insert(User user);// 插入操作会自动调用get方法

    // #{token}：编译时，将形参的值放入token
    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);// 当参数是User(类)时，不需要@Param注解

    @Select("select * from user where id = #{id}")
    User findById(@Param("id") Integer id);

    /**
     * 异常：Parameter 'account_id' not found. Available parameters are [accountId, param1]
     * 原因：@Select("select * from user where account_id = #{account_id}")
     *
     * 正确：@Select("select * from user where account_id = #{accountId}")
     */
    @Select("select * from user where account_id = #{accountId}")
    User findByAccountId(@Param("accountId") String accountId);

    // gmt_modified：映射数据库的column，没有驼峰。
    @Update({"update user set name=#{name},token=#{token},gmt_modified=#{gmtModified},avatar_url=#{avatarUrl} where id=#{id}"})
    void update(User user);// 当参数是类对象时，不需要@Param注解
}