package com.asejnr.tradingplatform.repository;

import com.asejnr.tradingplatform.modal.TwoFactorOTP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwoFactorOTPRepository extends JpaRepository<TwoFactorOTP, Long> {
    TwoFactorOTP findByUserId(Long userId);
}
