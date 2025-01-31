package com.antique.dto.login.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenDTO {
    private String message;
    private int statusCode;
    private String accessToken;
}
