package com.roadoptimizer.web.dto;

import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class LoginDTO {
    private final String nick;
    private final String password;
}
