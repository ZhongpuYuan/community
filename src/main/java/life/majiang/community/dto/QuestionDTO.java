package life.majiang.community.dto;

import life.majiang.community.model.User;
import lombok.Data;

@Data
/**
 * 中间表：根据Question类来关联User，从而获取用户的头像
 *        就是在Question类的基础上做了一点小改动
 */
public class QuestionDTO {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;

    private User user;// 关联User对象
}
