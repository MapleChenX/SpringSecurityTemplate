package personal.MapleChenX.securityTest.auth;

import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import personal.MapleChenX.securityTest.auth.utils.LoadUserService;
import personal.MapleChenX.securityTest.auth.utils.MyPasswordEncoder;
import personal.MapleChenX.securityTest.model.UserDTO;
import personal.MapleChenX.securityTest.service.UserService;


@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    @Resource
    UserService userService;

    @Resource
    private LoadUserService userDetailsService;

    @Resource
    private MyPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails != null && passwordEncoder.matches(password, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
        }
        throw new BadCredentialsException("用户名或密码错误");
    }

    public Authentication authenticate(LoginReq loginReq){
        UserDTO one = userService.query().eq("username", loginReq.getUsername()).one();
        if (one == null) {
            throw new BadCredentialsException("用户名或密码错误");
        }
        // 封装为Authentication对象
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword(), one.getRoles());
        return this.authenticate(authentication);
    }

    // 在你的MyAuthenticationProvider类中，supports方法的实现是检查传入的Authentication类型是否可以被分配给UsernamePasswordAuthenticationToken类。这意味着，如果传入的Authentication是UsernamePasswordAuthenticationToken或其子类的实例，那么这个AuthenticationProvider就可以处理它。
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }



}
