package art.caixi.crm.workbench.service.impl;

import art.caixi.crm.workbench.domain.Activity;
import art.caixi.crm.workbench.mapper.ActivityMapper;
import art.caixi.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    ActivityMapper activityMapper;
    @Override
    public int saveCreateActivity(Activity activity) {
        return activityMapper.insertActivity(activity);
    }
}
