package art.caixi.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @RequestMapping("/")
    public String index(HttpServletRequest request){
        System.out.println(request.getRemoteAddr());
        return "index";
    }
}
