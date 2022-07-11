package art.caixi.crm.workbench.service;

import art.caixi.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int saveCreateActivity(Activity activity);
    List<Activity> queryActivityByConditionForPage(Map<String , Object> map);

    int queryCountOfActivityByCondition(Map<String , Object> map);

    int deleteActivityByIds(String[] ids);
}
