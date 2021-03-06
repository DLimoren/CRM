package art.caixi.crm.workbench.web.controller;

import art.caixi.crm.commons.contants.Contants;
import art.caixi.crm.commons.domain.ReturnObject;
import art.caixi.crm.commons.utils.CellValueUtils;
import art.caixi.crm.commons.utils.DateUtils;
import art.caixi.crm.commons.utils.ExportExcelUtils;
import art.caixi.crm.commons.utils.UUIDUtils;
import art.caixi.crm.settings.domain.User;
import art.caixi.crm.settings.service.UserService;
import art.caixi.crm.workbench.domain.Activity;
import art.caixi.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    @Autowired
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
            }
            else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("???????????????????????????......");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("???????????????????????????......");
        }

        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name , String owner , String startDate,
                                                  String endDate, int pageNo , int pageSize){
        Map<String , Object> map = new HashMap<>();
        map.put("name" , name );
        map.put("owner" , owner );
        map.put("startDate" , startDate );
        map.put("endDate" , endDate );
        int beginNo = (pageNo-1) * pageSize;
      map.put("beginNo" , beginNo);
        map.put("pageSize", pageSize);
        List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
        int totalRows = activityService.queryCountOfActivityByCondition(map);

        // ????????????????????????????????????
        Map<String , Object> retMap = new HashMap<>();
        retMap.put("activityList" , activityList);
        retMap.put("totalRows" , totalRows);
        System.out.println(retMap);
        return retMap;
    }

    @RequestMapping("workbench/activity/deleteActivityByIds.do")
    @ResponseBody
    public Object deleteActivityByIds(String[] id){
        ReturnObject returnObject = new ReturnObject();
        try{
            int ret = activityService.deleteActivityByIds(id);
            if(ret > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }
            else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("?????????????????????");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("?????????????????????");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityById.do")
    public @ResponseBody Object queryActivityById(String id){
        Activity activity = activityService.queryActivityById(id);
        return activity;
    }

    @RequestMapping("/workbench/activity/saveEditActivity.do")
    public @ResponseBody Object saveEditActivity(Activity activity , HttpSession session){
        ReturnObject returnObject = new ReturnObject();
        activity.setEditTime(DateUtils.formatDateTime(new Date()));
        activity.setEditBy(((User)session.getAttribute(Contants.SESSION_USER)).getId());
        try {
            int ret = activityService.saveEditActivity(activity);
            if (ret > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("???????????????????????????.....");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("???????????????????????????.....");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/exportAllActivities.do")
    public void exportAllActivities(HttpServletResponse response) throws IOException {
        List<Activity> activityList = activityService.queryAllActivities();

        // ??????excel????????????????????????????????????excel???
        HSSFWorkbook wb = ExportExcelUtils.exportUtilsByList(activityList);
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition" ,  "attachment;filename=activityList.xls");
        OutputStream out = response.getOutputStream();
        wb.write(out);
//        wb.close();
        out.flush();
//        OutputStream os = new FileOutputStream("C:\\DEV\\CODE\\test\\activityList.xls");
//        wb.write(os);
//        os.close();

        // ???excel????????????????????????

//        FileInputStream is = new FileInputStream("C:\\DEV\\CODE\\test\\activityList.xls");
//        byte[] buff = new byte[256];
//        int len = 0 ;
//        while((len = is.read(buff)) != -1){
//            out.write(buff,0 , len);
//        }
//        is.close();
        return ;
    }

    @RequestMapping("/workbench/activity/exportCheckedActivities.do")
    public void exportCheckedActivities( HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] ids =  request.getParameterValues("id");
        List<Activity> activityList = activityService.queryCheckedActivities(ids);
        HSSFWorkbook wb = ExportExcelUtils.exportUtilsByList(activityList);
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition" ,  "attachment;filename=activityList.xls");
        OutputStream out = response.getOutputStream();
        wb.write(out);
//        wb.close();
        out.flush();
    }

    @RequestMapping("/workbench/activity/importActivity")
    public @ResponseBody Object importActivity(MultipartFile activityFile , HttpSession session){
        ReturnObject returnObject = new ReturnObject();
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        String originalFilename = activityFile.getOriginalFilename();
        File file = new File("/root/importFile/" ,originalFilename);
        try {
            activityFile.transferTo(file);
            FileInputStream is =  new FileInputStream("/root/importFile/" + originalFilename);
            HSSFWorkbook wb = new HSSFWorkbook(is);
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row = null;
            HSSFCell  cell = null;
            Activity activity = null;
            List<Activity> activityList = new ArrayList<>();
            for(int i = 1 ; i <= sheet.getLastRowNum() ; i ++){
                activity = new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateTime(DateUtils.formatDateTime(new Date()));
                activity.setCreateBy(user.getId());
                row = sheet.getRow(i);
                for(int j = 0 ; j < row.getLastCellNum() ; j ++){
                    cell = row.getCell(j);
                    String cellValue = CellValueUtils.getCellValue(cell);
                    if(j == 0){
                        activity.setName(cellValue);
                    }else if(j == 1){
                        activity.setStartDate(cellValue);
                    }else if(j == 2){
                        activity.setEndDate(cellValue);
                    }else if(j == 3){
                        activity.setCost(cellValue);
                    }else if(j == 4){
                        activity.setDescription(cellValue);
                    }
                }
                activityList.add(activity);
            }
            int ret = activityService.saveCreateActivityByList(activityList);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(ret);
        } catch (IOException e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("???????????????????????????.....");
        }
        return returnObject;
    }
}
