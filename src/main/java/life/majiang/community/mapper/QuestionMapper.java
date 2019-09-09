package life.majiang.community.mapper;

import life.majiang.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 异常：There is no getter for property named
 *
 * 原因：sql语句中，gmt_create和gmt_create错误匹配，应该是gmt_create和gmtCreate才对
 */
@Mapper
@Repository
// 注意：不允许出现同名方法
public interface QuestionMapper {
    @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,tag) values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    // 分页查询
    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") Integer offset, @Param(value = "size")Integer size);

    // 查询总记录数
    @Select("select count(1) from question")
    Integer count();

    // listByUserId()：根据userId，分页查询查询指定用户提出的问题和回复
    @Select("select * from question where creator=#{userId} limit #{offset},#{size}")
    List<Question> listByUserId(@Param("userId") Integer userId, @Param(value = "offset") Integer offset, @Param(value = "size")Integer size);

    // countByUserId()：查询指定用户提出的问题总数
    @Select("select count(1) from question where creator=#{userId}")
    Integer countByUserId(@Param("userId") Integer userId);
}