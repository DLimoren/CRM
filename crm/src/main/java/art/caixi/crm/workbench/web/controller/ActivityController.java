package art.caixi.crm.workbench.web.controller;

import art.caixi.crm.commons.contants.Contants;
import art.caixi.crm.commons.domain.ReturnObject;
import art.caixi.crm.commons.utils.DateUtils;
import art.caixi.crm.commons.utils.UUIDUtils;
import art.caixi.crm.settings.domain.User;
import art.caixi.crm.settings.service.UserService;
import art.caixi.crm.workbench.domain.Activity;
import art.caixi.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    private UserService userService;
    @RequestMapping("/workbenck/activity/index.do")
    public String index(HttpServletRequest request){
        List<User> userList = userService.queryAllUsers();
        request.setAttribute("userList",userList);
        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    public @ResponseBody Object saveCreateActivity(Activity activity , HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));
        activity.setCreateBy(user.getId());

        ReturnObject returnObject  = new ReturnObject();
        try {
            int ret = activityService.saveCreateActivity(activity);

            if(ret > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试......");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试......");
        }

        return returnObject;
    }
}
