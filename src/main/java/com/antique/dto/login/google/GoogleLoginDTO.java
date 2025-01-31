package com.antique.dto.login.google;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleLoginDTO {
    private String message;
    private int statusCode;
    private String accessToken;
    private String refreshToken;
}
