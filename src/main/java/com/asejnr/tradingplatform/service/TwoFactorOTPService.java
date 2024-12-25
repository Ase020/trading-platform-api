package com.asejnr.tradingplatform.service;

import com.asejnr.tradingplatform.model.TwoFactorOTP;
import com.asejnr.tradingplatform.model.User;

public interface TwoFactorOTPService {
    TwoFactorOTP createTwoFactorOTP(User user, String otp, String jwtToken);

    TwoFactorOTP findByUser(Long userId);

    TwoFactorOTP findById(String id);

    boolean verifyTwoFactorOTP(TwoFactorOTP twoFactorOTP, String otp);

    void deleteTwoFactorOTP(TwoFactorOTP twoFactorOTP);
}
