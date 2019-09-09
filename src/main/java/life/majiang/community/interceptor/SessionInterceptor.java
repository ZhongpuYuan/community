package life.majiang.community.interceptor;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 自定义一个拦截器
@Service
/**
 * 注意：如果不交给spring容器来管理SessionInterceptor，
 *      则UserMapper无法注入，最终在下面这行语句会发生空指针异常：
 *          User user = userMapper.findByToken(token);
 */
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Cookie[] cookies = request.getCookies();

        if(cookies != null && cookies.length != 0){
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
        }
        return true;// 放行，交给下一个拦截器
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}