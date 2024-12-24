package com.asejnr.tradingplatform.request;

import com.asejnr.tradingplatform.domain.VerificationType;
import lombok.Data;

@Data
public class ForgotPasswordTokenRequest {
    private String sendTo;
    private VerificationType verificationType;
}
