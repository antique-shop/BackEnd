package com.antique.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestDto {
    private Long userId;
    private String nickname;
    private String address;
}
