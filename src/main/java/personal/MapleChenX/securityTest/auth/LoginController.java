package personal.MapleChenX.securityTest.auth;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.MapleChenX.securityTest.auth.utils.JwtUtils;
import personal.MapleChenX.securityTest.model.UserDTO;
import personal.MapleChenX.securityTest.service.UserService;

@RestController
public class LoginController {

    @Resource
    UserService userService;

    @Resource
    MyAuthenticationProvider myAuthenticationProvider;

    @Resource
    StringRedisTemplate redis;

    @Resource
    JwtUtils jwtUtils;


    @RequestMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq user) {

        UserDTO one = userService.query().eq("username", user.getUsername()).one();

        Authentication authenticate = myAuthenticationProvider.authenticate(user);
        // 认证成功，将用户信息存入SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        System.out.println("登录成功");
        UserDetails build = User
                .withUsername(one.getUsername())
                .password(one.getPassword())
                .authorities(one.getRole())
                .build();

        String jwt = jwtUtils.createJwt(build,
                user.getUsername(),
                userService.getUserId(user.getUsername()));
        System.out.println(jwt);
        redis.opsForValue().set("token" + user.getUsername(), jwt);
        return ResponseEntity.ok("---" + jwt + "---");
    }

    @RequestMapping("/test")
    public String test() {
        return "test";
    }

}
