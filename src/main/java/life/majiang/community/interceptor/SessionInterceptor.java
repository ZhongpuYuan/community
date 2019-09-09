package life.majiang.community.interceptor;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

// 自定义一个拦截器
/**
 * 注意：如果不交给spring容器来管理SessionInterceptor，
 *      则UserMapper无法注入，最终在下面这行语句会发生空指针异常：
 *          User user = userMapper.findByToken(token);
 */
@Service
public class SessionInterceptor implements HandlerInterceptor {

    /**
     * 使用IDEA工具时使用@Autowired自动注解bean时会显示红色，但是项目能运行，解决方法：
     *     File – Settings – Inspections - Spring Core – Autowiring for Bean Class：
     *         将Severity的级别由之前的error改成warning。
     */
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
                    UserExample userExample = new UserExample();

                    // 拼接SQL语句
                    userExample.createCriteria()
                            .andTokenEqualTo(token);

                    List<User> users = userMapper.selectByExample(userExample);

                    // 当user不为空，即登陆成功时，将登陆信息写入session，便于页面的展示和跳转
                    if(users.size() != 0){
                        request.getSession().setAttribute("user",users.get(0));
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