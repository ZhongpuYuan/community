package life.majiang.community.dto;

import lombok.Data;

@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    // This should contain a random string to protect against forgery attacks
    // and could contain any other arbitrary data.
    private String state;
    private String code;
}