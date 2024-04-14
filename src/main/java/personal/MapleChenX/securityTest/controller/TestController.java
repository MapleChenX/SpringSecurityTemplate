package personal.MapleChenX.securityTest.controller;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.MapleChenX.securityTest.service.UserService;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    StringRedisTemplate redis;

    @Resource
    UserService userService;


    @RequestMapping("/1")
    public String test() {
        return "test";
    }

}
