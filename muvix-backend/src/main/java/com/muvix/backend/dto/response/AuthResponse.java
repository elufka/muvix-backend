package com.muvix.backend.dto.response;
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType="Bearer";
    private long expiresInSeconds;

    public AuthResponse(){}

    public AuthResponse(String accessToken,String refreshToken,long expiresInSeconds){
        this.accessToken=accessToken;
        this.refreshToken=refreshToken;
        this.expiresInSeconds=expiresInSeconds;
    }

    public String getAccessToken(){return accessToken;} public void setAccessToken(String v){accessToken=v;}
    public String getRefreshToken(){return refreshToken;} public void setRefreshToken(String v){refreshToken=v;}
    public String getTokenType(){return tokenType;} public void setTokenType(String v){tokenType=v;}
    public long getExpiresInSeconds(){return expiresInSeconds;} public void setExpiresInSeconds(long v){expiresInSeconds=v;}
}
