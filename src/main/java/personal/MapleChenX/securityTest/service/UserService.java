package personal.MapleChenX.securityTest.service;


import com.baomidou.mybatisplus.extension.service.IService;
import personal.MapleChenX.securityTest.model.UserDTO;

public interface UserService extends IService<UserDTO> {
    public Integer getUserId(String username);
}
