package personal.MapleChenX.securityTest.auth.utils;


import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import personal.MapleChenX.securityTest.model.UserDTO;
import personal.MapleChenX.securityTest.service.UserService;

@Component
public class LoadUserService implements UserDetailsService {

    @Resource
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = userService.query().eq("username", username).one();

//        UserDetailsImpl details = new UserDetailsImpl(user);
//        return details;

        return User
                .withUsername(username)
                .password(user.getPassword())
                .roles("user")
                .build();
    }

}
