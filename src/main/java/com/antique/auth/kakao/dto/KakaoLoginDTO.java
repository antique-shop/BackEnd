package com.antique.auth.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class KakaoLoginDTO {
    public String accessToken;
    public String refreshToken;
}
