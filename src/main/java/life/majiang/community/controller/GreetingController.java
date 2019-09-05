package life.majiang.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {
    // @GetMapping：将用户的请求路由到此方法
    @GetMapping("/hello")
    // @RequestParam：localhost/greeting?name=12345(形参是name)
    // String name：实参，接受形参的值
    // Model：内置类。类似于map，负责向前端传递结果
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";// 到resources/templates到同名html文件

        /**
         * 访问：http://localhost:8080/hello?name=yuan
         *
         * 网页打印：Hello, yuan!
         */
    }
}