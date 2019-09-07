package life.majiang.community.controller;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/index")
    public String index(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        for(Cookie cookie : cookies){
            // 遍历cookie
            if(cookie.getName().equals("token")){
                String token = cookie.getValue();
                // 根据token去数据库查询用户，若不为空，则证明登陆成功
                User user = userMapper.findByToken(token);
                // 当user不为空，即登陆成功时，将登陆信息写入session，便于页面的展示和跳转
                if(user != null){
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }
        return "index";
    }
}