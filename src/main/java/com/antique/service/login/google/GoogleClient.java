package com.antique.service.login.google;

import com.antique.dto.login.google.GoogleAccessTokenRequest;
import com.antique.dto.login.google.GoogleAccessTokenResponse;
import com.antique.dto.login.google.GoogleAccountProfileResponse;
import com.antique.exception.BaseException;
import com.antique.exception.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GoogleClient {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.google.authorization-grant-type}")
    private String authorizationCode;
    @Value("${url.access-token}")
    private String accessTokenUrl;
    @Value("${url.profile}")
    private String profileUrl;

    private final RestTemplate restTemplate;

    /**
     * Google OAuth 로그인 후 사용자 프로필 가져오기
     */
    @Transactional(readOnly = true)
    public GoogleAccountProfileResponse getGoogleAccountProfile(final String code) {
        String accessToken = requestGoogleAccessToken(code);
        return requestGoogleAccountProfile(accessToken);
    }

    /**
     * Google OAuth Access Token 요청
     */
    public String requestGoogleAccessToken(final String code) {
        final String decodedCode = URLDecoder.decode(code, StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        GoogleAccessTokenRequest requestBody = new GoogleAccessTokenRequest(
                decodedCode, clientId, clientSecret, redirectUri, authorizationCode
        );

        HttpEntity<GoogleAccessTokenRequest> httpEntity = new HttpEntity<>(requestBody, headers);

        GoogleAccessTokenResponse response = restTemplate.exchange(
                accessTokenUrl, HttpMethod.POST, httpEntity, GoogleAccessTokenResponse.class
        ).getBody();

        return Optional.ofNullable(response)
                .orElseThrow(() -> new BaseException(CommonErrorCode.GOOGLE_TOKEN_REQUEST_FAILED))
                .getAccessToken();
    }

    /**
     * Google API에서 사용자 정보 가져오기
     */
    GoogleAccountProfileResponse requestGoogleAccountProfile(final String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                profileUrl, HttpMethod.GET, httpEntity, GoogleAccountProfileResponse.class
        ).getBody();
    }
}
