package com.asejnr.tradingplatform.service;

import com.asejnr.tradingplatform.modal.TwoFactorOTP;
import com.asejnr.tradingplatform.modal.User;

public interface TwoFactorOTPService {
    TwoFactorOTP createTwoFactorOTP(User user, String otp, String jwtToken);

    TwoFactorOTP findByUser(Long userId);

    TwoFactorOTP findById(String id);

    boolean verifyTwoFactorOTP(TwoFactorOTP twoFactorOTP, String otp);

    void deleteTwoFactorOTP(TwoFactorOTP twoFactorOTP);
}
