package com.asejnr.tradingplatform.model;

import com.asejnr.tradingplatform.domain.VerificationType;
import lombok.Data;

@Data
public class TwoFactorAuth {
    private boolean isEnabled = false;
    private VerificationType sendTo;
}
