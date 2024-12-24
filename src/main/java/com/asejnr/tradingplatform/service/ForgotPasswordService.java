package com.asejnr.tradingplatform.service;

import com.asejnr.tradingplatform.domain.VerificationType;
import com.asejnr.tradingplatform.modal.ForgotPasswordToken;
import com.asejnr.tradingplatform.modal.User;

public interface ForgotPasswordService {
    ForgotPasswordToken createForgotPasswordToken(User user,
                                                  String id,
                                                  String otp,
                                                  VerificationType verificationType,
                                                  String sendTo);

    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUserId(Long userId);

    void deleteForgotPasswordToken(ForgotPasswordToken forgotPasswordToken);
}