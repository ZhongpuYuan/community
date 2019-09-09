package life.majiang.community.controller;

import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GitHubUser;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.utils.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 1.发出authorize请求
 * 2.回调redirect_uri，携带code
 * 3.access_token，携带上面的code
 * 4.user，携带access_token
 * 5.返回user信息
 */
@Controller
public class AuthorizeController {

    @Autowired
    private GitHubProvider gitHubProvider;

    @Autowired
    private UserMapper userMapper;

    // 获取配置文件信息
    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response) {

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();// shift + 回车
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_secret(clientSecret);
        String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);// command + alt + v
        GitHubUser gitHubUser = gitHubProvider.getUser(accessToken);

        /**
         * 1.当成功登陆后
         * 2.获取用户信息，生成token
         * 3.把token放到user中，存入数据库
         * 4.把token放入cookie(无需将user放入session，这是实现持久化登陆的又一个方法)
         */
        // shift + f6：改变局部变量名称
        if (gitHubUser != null && gitHubUser.getId() != null) {
            // 将用户存入数据库
            User user = new User();

            String token = UUID.randomUUID().toString();// command + alt + v
            user.setToken(token);// 使用UUID
            user.setName(gitHubUser.getName());
            user.setAccountId(String.valueOf(gitHubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());

            user.setAvatarUrl(gitHubUser.getAvatarUrl());

            userMapper.insert(user);

            // 插入成功，添加cookie，
            response.addCookie(new Cookie("token", token));// command + p：查看参数类型

            // 用户登录成功，刷新页面也会保持登录状态
            // request.getSession().setAttribute("user",gitHubUser);// 将user对象放入session中
            return "redirect:index";// 重定向
        } else {
            // 重新登录
            return "redirect:index";// 重定向￿￿
        }
    }
}