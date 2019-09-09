package life.majiang.community.controller;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action, Model model, HttpServletRequest request,
                          @RequestParam(name="page",defaultValue = "1") Integer page,
                          // 每页显示的条目数
                          @RequestParam(name="size",defaultValue = "3") Integer size){

        /**
         * 登录验证：防止用户直接在地址栏访问profile.html
         */
        User user = null;

        Cookie[] cookies = request.getCookies();

        if(cookies != null && cookies.length != 0){
            for(Cookie cookie : cookies){
                // 遍历cookie
                if(cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    // 根据token去数据库查询用户，若不为空，则证明登陆成功
                    user = userMapper.findByToken(token);
                    // 当user不为空，即登陆成功时，将登陆信息写入session，便于页面的展示和跳转
                    if(user != null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }

        // 判断用户是否登录，若无，则跳转到登录页
        if (user == null){
            return "redirect:/";
        }

        /**
         * 根据用户的点击，展示"我的提问"和"最新回复"
         */
        if ("questions".equals(action)){
            // 只要是前端数据不对了，我们就要向着去后端排查问题
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
        }else if ("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }

        // 重载QuestionService的list()方法：
        //     传入userId后，我们就可以根据user和question两表中userId和creator的关系，
        //     找到该用户发布的问题和回复
        PaginationDTO paginationDTO = questionService.list(user.getId(),page,size);// 分页展示用户的提问和回复

        // 将查询结果(指定用户提出的问题和回复)返回前端
        model.addAttribute("pagination",paginationDTO);

        return "profile";
    }
}