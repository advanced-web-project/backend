package com.hcmus.demo.config;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = ObjectUtils.asMap(
                "cloud_name", "awd",
                "api_key", "713662145664962",
                "api_secret", "7KFSNkdck5kbTcnoD__Jo-yvGdk"
        );
        return new Cloudinary(config);
    }
}
