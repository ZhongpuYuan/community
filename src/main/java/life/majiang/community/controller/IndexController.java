package life.majiang.community.controller;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(Model model,
                        // 分页数，默认为1，用户可自己输入想要跳转的页数
                        @RequestParam(name="page",defaultValue = "1") Integer page,
                        // 每页显示的条目数
                        @RequestParam(name="size",defaultValue = "3") Integer size){

        // 在页面返回之前，从数据库中执行查询，回显数据
        PaginationDTO pagination = questionService.list(page,size);

        // 测试热部署工具：
        //      1.设置：compiler的自动build project
        //      2.command+option+shift+?，设置compiler....app when running
        //      3.修改后，option+f9，即可热部署
        /*
            for (QuestionDTO questionDTO : questionList) {
                questionDTO.setDescription("change");
            }
        */

        // 将数据发送到前端(在此处打断点可以观察)
        /**
         * Debug思路：
         *     1.前端的SpringEL表达式question.title报红，这说明前端拿到的数据不对劲
         *         即：question对象中没有title属性，为什么没有，这是问题的关键。
         *
         *     2.在数据发送之前的此处打断点，发现questions封装的对象不对，全是null
         *         questionList中都是QuestionDTO对象，肯定是封装过程出问题了
         *
         *     3.为什么封装不进去？向上追溯，继续打断点
         *
         *     4.最终发现，QuestionDTO错误地和User写成一致了。属性和Question不匹配，封装失败
         *
         *     总结：先判断问题出现的地方，然后debug，写代码要小心。不要太马虎
         */
        model.addAttribute("pagination",pagination);

        return "index";
    }
}