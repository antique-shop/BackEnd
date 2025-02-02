package com.antique.auth.kakao.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;

/*
* 사용자가 동의한 항목만 담겨지고, 동의하지 않은 항목은 null로 들어오게 됨
*/
@Data
@NoArgsConstructor //역직렬화를 위한 기본 생성자
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfoResponseDTO {
    //회원 번호
    @JsonProperty("id")
    public Long id;

    //사용자 프로퍼티
    @JsonProperty("properties")
    public HashMap<String, String> properties;

    //카카오 계정 정보
    @JsonProperty("kakao_account")
    public KakaoAccount kakaoAccount;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class KakaoAccount {
        //사용자 프로필 정보
        @JsonProperty("profile")
        public Profile profile;

        //카카오계정 이름
        @JsonProperty("name")
        public String name;

        //카카오계정 대표 이메일
        @JsonProperty("email")
        public String email;


        @Getter
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public class Profile {

            //닉네임
            @JsonProperty("nickname")
            public String nickName;

            //프로필 사진 URL
            @JsonProperty("profile_image_url")
            public String profileImageUrl;
        }
    }
}
