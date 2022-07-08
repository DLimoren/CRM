package art.caixi.crm.settings.web.controller;

import art.caixi.crm.commons.domain.ReturnObject;
import art.caixi.crm.settings.domain.User;
import art.caixi.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
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
    public Object login(String loginAct , String loginPwd , String isRemPwd , HttpServletRequest request){
        Map<String , Object> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        User user = userService.queryUserByLoginActAndLoginPwd(map);

        ReturnObject returnObject = new ReturnObject();
        System.out.println(request.getRemoteAddr());
        if(user == null){
            //登录失败
            returnObject.setCode("0");
            returnObject.setMessage("用户名或密码错误");
        }
        else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowStr = sdf.format(new Date());
            if(nowStr.compareTo(user.getExpireTime()) > 0){
                // 登录失败，账号过期
                returnObject.setCode("0");
                returnObject.setMessage("账号已过期");
            }
            else if("0".equals(user.getLockState())) {
                // 登录失败 状态锁定
                returnObject.setCode("0");
                returnObject.setMessage("账号被锁定");
            }
            else if(!user.getAllowIps().contains(request.getRemoteAddr())){
                // 登录失败 ip受限
                returnObject.setCode("0");
                returnObject.setMessage("您当前存在安全风险");
            }
            else{
                returnObject.setCode("1");
            }
        }
        return returnObject;
    }
}
