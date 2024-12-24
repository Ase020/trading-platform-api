package com.asejnr.tradingplatform.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String jwtToken;
    private boolean status;
    private String message;
    private boolean isTwoFactorAuthEnabled;
    private String session;
}