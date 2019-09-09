package life.majiang.community.model;

import lombok.Data;

/**
 * @Data
 * All together now:
 *     A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields,
 *     and @Setter on all non-final fields, and @RequiredArgsConstructor!
 */
@Data
public class User {
    private Integer id;
    private String name;
    private String accountId;
    private Long gmtCreate;
    private Long gmtModified;
    private String token;// 相当于银行卡。session相当于银行账户
    private String avatarUrl;// 头像地址
}