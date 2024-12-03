package com.hcmus.demo.user;

import lombok.*;
import org.springframework.hateoas.CollectionModel;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String profile;
}
