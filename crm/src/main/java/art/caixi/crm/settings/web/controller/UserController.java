package art.caixi.crm.settings.web.controller;

import art.caixi.crm.commons.contants.Contants;
import art.caixi.crm.commons.domain.ReturnObject;
import art.caixi.crm.commons.utils.DateUtils;
import art.caixi.crm.settings.domain.User;
import art.caixi.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;


    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){

        return "settings/qx/user/login";
    }

    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct , String loginPwd , String isRemPwd , HttpServletRequest request , HttpServletResponse response, HttpSession httpSession){
        Map<String , Object> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        User user = userService.queryUserByLoginActAndLoginPwd(map);

        ReturnObject returnObject = new ReturnObject();
        returnObject.setRetData(request.getServerName());
        System.out.println(request.getServerName());
        System.out.println(request.getServerName());
        System.out.println(request.getServerName());
        System.out.println(request.getServerName());
        System.out.println(request.getServerName());
        System.out.println(request.getServerName());
        System.out.println(request.getServerName());
        System.out.println(request.getServerName());
        System.out.println(request.getServerName());
        System.out.println(request.getServerName());
        if(user == null){
            //????????????
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("????????????????????????");
        }
        else{
            if(DateUtils.formatDateTime(new Date()).compareTo(user.getExpireTime()) > 0){
                // ???????????????????????????
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("???????????????");
            }
            else if("0".equals(user.getLockState())) {
                // ???????????? ????????????
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("???????????????");
            }
            else if(!user.getAllowIps().contains(request.getRemoteAddr())){
                // ???????????? ip??????
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("???????????????????????????");
            }
            else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                // ???user????????????????????????
                httpSession.setAttribute(Contants.SESSION_USER , user);


                Cookie c1 = new Cookie("loginAct" , user.getLoginAct());
                Cookie c2 = new Cookie("loginPwd" , user.getLoginPwd());
                //  ????????????????????????????????????cookies
                if("true".equals(isRemPwd)){
                    c1.setMaxAge(10*24*60*60);
                    c2.setMaxAge(10*24*60*60);
                }
                else {
                    c1.setMaxAge(0);
                    c2.setMaxAge(0);
                }
                response.addCookie(c1);
                response.addCookie(c2);
            }
        }
        return returnObject;
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response , HttpSession session){
        //  ??????cookies
        Cookie c1 = new Cookie("loginAct" , "1");
        Cookie c2 = new Cookie("loginPwd" , "1");
        c1.setMaxAge(0);
        c2.setMaxAge(0);
        response.addCookie(c1);
        response.addCookie(c2);

        // ??????session
        session.invalidate();
        return "redirect:/";
    }
}
