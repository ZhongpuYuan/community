package life.majiang.community.controller;

import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    // get请求渲染页面
    @GetMapping("/publish")
    public String publish(){
        System.out.println("publish");
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            // required = false：非必须属性，表单中可以不提交
            @RequestParam(value = "title",required = false) String title,
            @RequestParam(value = "description",required = false) String description,
            @RequestParam(value = "tag",required = false) String tag,
            HttpServletRequest request,
            Model model // 负责向前端写回信息
    ){

        // 添加异常信息，当出现异常时，打印下面的信息，否则就不打印
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        // 校验标题、正文的格式
        if (title == null || title== ""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }

        if (description == null || description== ""){
            model.addAttribute("error","问题补充不能为空");
            return "publish";
        }

        if (tag == null || tag== ""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }

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

        if (user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }

        Question question = new Question();// command+alt+v：抽取变量
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.create(question);

        return "redirect:index";// 刷新页面，展示新增内容
    }
}