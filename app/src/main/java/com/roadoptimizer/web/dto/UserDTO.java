package com.roadoptimizer.web.dto;

import lombok.Data;
import lombok.experimental.Builder;


@Data
@Builder
public class UserDTO {
    private final String nick;
    private final String password;
    private final String firstName;
    private final String lastName;
}
