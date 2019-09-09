package life.majiang.community.controller;

import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,Model model){
        QuestionDTO question = questionService.getById(id);

        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());

        model.addAttribute("id",question.getId());// 唯一标识

        return "publish";
    }

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
            @RequestParam(value = "id",required = false) int id,
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

        User user = (User) request.getSession().getAttribute("user");

        if (user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }

        Question question = new Question();// command+alt+v：抽取变量

        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);

        questionService.createOrUpdate(question);

        return "redirect:/";// 刷新页面，展示新增内容
    }
}