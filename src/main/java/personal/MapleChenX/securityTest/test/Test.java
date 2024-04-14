package personal.MapleChenX.securityTest.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {
    public static void main(String[] args) {
        String encode = new BCryptPasswordEncoder().encode("111111");
        System.out.println(encode);

    }
}
