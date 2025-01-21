package com.antique.dto.dibs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DibsResponseDTO {
    private Long dibsId;      // 찜 ID
    private String message;   // 응답 메시지
    private int statusCode;   // HTTP 상태 코드
}
