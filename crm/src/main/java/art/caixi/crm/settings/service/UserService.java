package art.caixi.crm.settings.service;

import art.caixi.crm.settings.domain.User;

import java.util.Map;

public interface UserService {
    User queryUserByLoginActAndLoginPwd(Map<String , Object> map);
}
