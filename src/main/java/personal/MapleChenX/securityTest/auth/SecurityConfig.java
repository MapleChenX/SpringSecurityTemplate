package personal.MapleChenX.securityTest.auth;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import personal.MapleChenX.securityTest.auth.utils.JwtAuthenticationFilter;
import personal.MapleChenX.securityTest.auth.utils.JwtUtils;
import personal.MapleChenX.securityTest.auth.utils.RestBean;

import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig {


    @Resource
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Resource
    JwtUtils utils;



    //事实上，logout不会做任何事，唯一会做的事情就是失效token而已
    //首先，登录成功之后会办法token，以后的每次请求都是解析token，解析成功之后会载入ctx一个认证信息中；任何请求都会执行这一步，每次都要载入ctx
    //所以并不存在什么清除ctx的认证，因为security根本就没保存这个认证，认证是基于token的，登录登出都是对token进行操作而已

    // 需要做的事情：自定义UserDetails，自定义UserDetailsService，自定义AuthenticationProvider，自定义PasswordEncoder，最后放进ctx
    // todo 还有两件事需要做
    // token解析验证拦截器
    // 载入用户的时候权限还没载入

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(conf -> conf
                        .requestMatchers( "/login").permitAll()
                        .anyRequest().authenticated()
                )
                .logout(conf -> conf
                        .logoutUrl("/logout") //自动删除ctx中的Authentication
                        .logoutSuccessHandler(this::doLogoutSuccess)
                )
                .exceptionHandling(conf -> conf // 认证不通过异常处理
                        .accessDeniedHandler(this::doAccessDenied)
                        .authenticationEntryPoint(this::doAuthenticationException)
                )
                .csrf(AbstractHttpConfigurer::disable) // 都前后端分离了，关闭csrf
                // 无状态（stateless）的应用中，Spring Security的SecurityContext不会在用户的会话中存储Authentication对象。
                // Spring Security会在每个请求开始时创建一个新的SecurityContext，并在请求结束时清除这个SecurityContext。
                .sessionManagement(conf -> conf
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private void doLogoutSuccess(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        String authorization = request.getHeader("Authorization");
        if(utils.invalidateJwt(authorization)) {
            writer.write(RestBean.success("退出登录成功").asJsonString());
            return;
        }
        writer.write(RestBean.failure(400, "退出登录失败").asJsonString());
    }

    private void doAccessDenied(HttpServletRequest request,
                                HttpServletResponse response,
                                AccessDeniedException e) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        response.setStatus(401);
        writer.write("访问拒绝");
    }

    private void doAuthenticationException(HttpServletRequest request,
                                           HttpServletResponse response,
                                           AuthenticationException e) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        response.setStatus(401);
        writer.write("未认证");
    }


}
