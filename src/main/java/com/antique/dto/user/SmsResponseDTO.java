package com.antique.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SmsResponseDTO {
    @Schema(description = "메시지")
    private String message;

    @Schema(description = "확인 코드")
    private String verificationCode;
}
