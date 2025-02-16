package com.antique.controller.user;

import com.antique.dto.user.SmsResponseDTO;
import com.antique.service.user.UserPhoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sms")
@RequiredArgsConstructor
@Tag(name = "유저 번호 인증 API", description = "번호 인증 관련된 API 목록입니다.")
public class UserPhoneController {
    private final UserPhoneService userPhoneService;

    @Operation(summary = "유저 번호 인증", description = "유저의 핸드폰 번호를 인증하는 API입니다.")
    @Parameter(name = "phoneNumber", description = "핸드폰 번호", required = true)
    @PostMapping("/send")
    public ResponseEntity<SmsResponseDTO> sendSms(@RequestParam String phoneNumber) {
        String verificationCode = userPhoneService.sendSms(phoneNumber);

        SmsResponseDTO response = new SmsResponseDTO(
                "SMS 전송 성공",
                verificationCode
        );

        return ResponseEntity.ok(response);
    }
}
