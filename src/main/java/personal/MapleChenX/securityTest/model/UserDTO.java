package personal.MapleChenX.securityTest.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.List;

@Data
@TableName("user")
public class UserDTO {
    public Integer id;
    public String username;
    public String password;
    public String role;

    public List<GrantedAuthority> getRoles() {
        return Collections.singletonList((GrantedAuthority) () -> role);
    }

}
