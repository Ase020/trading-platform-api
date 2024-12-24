package com.asejnr.tradingplatform.service;

import com.asejnr.tradingplatform.domain.VerificationType;
import com.asejnr.tradingplatform.modal.User;
import com.asejnr.tradingplatform.modal.VerificationCode;

public interface VerificationCodeService {
    VerificationCode sendVerificationOTPCode(User user, VerificationType verificationType);

    VerificationCode getVerificationOTPCodeById(Long id) throws Exception;

    VerificationCode getVerificationOTPCodeByUser(Long userId);

    void deleteVerificationOTPCodeById(VerificationCode verificationCode);
}
