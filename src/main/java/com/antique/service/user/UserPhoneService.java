package com.antique.service.user;

import com.antique.exception.BaseException;
import com.antique.exception.CommonErrorCode;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserPhoneService {

    @Value("${coolsms.api.number}")
    private String fromPhoneNumber;

    private final DefaultMessageService messageService;

    public UserPhoneService(@Value("${coolsms.api.key}") String apiKey,
                            @Value("${coolsms.api.secret}") String apiSecret) {
        // CoolSMS 서비스 초기화 (NurigoApp 활용)
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    public String sendSms(String to) {
        try {
            // 랜덤한 4자리 인증번호 생성
            String numStr = generateRandomNumber();

            // 메시지 객체 생성
            Message message = new Message();
            message.setFrom(fromPhoneNumber);
            message.setTo(to);
            message.setText("인증번호는 [" + numStr + "] 입니다.");

            // CoolSMS를 사용하여 메시지 전송
            messageService.sendOne(new SingleMessageSendingRequest(message));

            return numStr; // 생성된 인증번호 반환

        } catch (Exception e) {
            throw new BaseException(CommonErrorCode.SMS_SEND_FAILED);
        }
    }

    // 랜덤한 4자리 숫자 생성 메서드
    private String generateRandomNumber() {
        Random rand = new Random();
        StringBuilder numStr = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            numStr.append(rand.nextInt(10));
        }
        return numStr.toString();
    }
}