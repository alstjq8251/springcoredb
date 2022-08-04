package com.sparta.springcoredb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@Component
public class UserInfoDto {
    String username;
    boolean isAdmin;
}
