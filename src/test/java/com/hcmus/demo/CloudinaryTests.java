package com.hcmus.demo;

import com.hcmus.demo.security.auth.AuthenticationService;
import com.hcmus.demo.util.CloudinaryUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CloudinaryTests {
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    CloudinaryUtil cloudinaryUtil;

    @Test
    public void test() {
        // Write your test code here
       String image = cloudinaryUtil.uploadImageToCloudinary("https://lh3.googleusercontent.com/a/ACg8ocKwJ-YNd9ikdC4EWU6dXlsYnudmNxpzY1YpW6_E2xv9uTbHzoM8=s96-c");
       System.out.println("Url image" + image);
    }
}
