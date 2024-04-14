package personal.MapleChenX.securityTest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import personal.MapleChenX.securityTest.model.UserDTO;

@Mapper
public interface UserMapper extends BaseMapper<UserDTO> {
}
