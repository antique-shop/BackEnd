package com.antique.auth.kakao.dto;

import com.antique.domain.User;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoLoginResponseDTO {
    private String email;
    private String jwtToken;
    private String refreshToken;
}