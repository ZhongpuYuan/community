package life.majiang.community.controller;

import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// 完成对问题的修改和删除
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("question/{id}")
    public String question(@PathVariable(name = "id") Integer id, Model model){
        QuestionDTO questionDTO = questionService.getById(id);

        // 先根据id去question表查询该条目是否存在，然后通过model回显到页面
        model.addAttribute("question",questionDTO);

        return "question";
    }
}
