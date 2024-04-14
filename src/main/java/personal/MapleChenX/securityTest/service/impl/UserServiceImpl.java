package personal.MapleChenX.securityTest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import personal.MapleChenX.securityTest.mapper.UserMapper;
import personal.MapleChenX.securityTest.model.UserDTO;
import personal.MapleChenX.securityTest.service.UserService;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDTO> implements UserService {
    @Override
    public Integer getUserId(String username) {
        UserDTO user = this.query().eq("username", username).one();
        return user.getId();
    }
}
