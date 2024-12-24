package com.asejnr.tradingplatform.modal;

import com.asejnr.tradingplatform.domain.VerificationType;
import lombok.Data;

@Data
public class TwoFactorAuth {
    private boolean isEnabled = false;
    private VerificationType sendTo;
}
