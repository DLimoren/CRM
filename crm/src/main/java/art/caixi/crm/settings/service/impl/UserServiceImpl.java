package art.caixi.crm.settings.service.impl;

import art.caixi.crm.settings.domain.User;
import art.caixi.crm.settings.mapper.UserMapper;
import art.caixi.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public User queryUserByLoginActAndLoginPwd(Map<String, Object> map) {
        return userMapper.selectUserByLoginActAndLoginPwd(map);
    }
}
