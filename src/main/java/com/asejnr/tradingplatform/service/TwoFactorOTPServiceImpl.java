package com.asejnr.tradingplatform.service;

import com.asejnr.tradingplatform.modal.TwoFactorOTP;
import com.asejnr.tradingplatform.modal.User;
import com.asejnr.tradingplatform.repository.TwoFactorOTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TwoFactorOTPServiceImpl implements TwoFactorOTPService {
    @Autowired
    private TwoFactorOTPRepository twoFactorOTPRepository;

    @Override
    public TwoFactorOTP createTwoFactorOTP(User user, String otp, String jwtToken) {
        UUID uuid = UUID.randomUUID();

        String id = uuid.toString();

        TwoFactorOTP twoFactorOTP = new TwoFactorOTP();
        twoFactorOTP.setOtpCode(otp);
        twoFactorOTP.setJwtToken(jwtToken);
        twoFactorOTP.setUser(user);
        twoFactorOTP.setId(id);

        return twoFactorOTPRepository.save(twoFactorOTP);
    }

    @Override
    public TwoFactorOTP findByUser(Long userId) {
        return twoFactorOTPRepository.findByUserId(userId);
    }

    @Override
    public TwoFactorOTP findById(String id) {
        Optional<TwoFactorOTP> twoFactorOTP = twoFactorOTPRepository.findById(Long.valueOf(id));
        return twoFactorOTP.orElse(null);
    }

    @Override
    public boolean verifyTwoFactorOTP(TwoFactorOTP twoFactorOTP, String otp) {
        return twoFactorOTP.getOtpCode().equals(otp);
    }

    @Override
    public void deleteTwoFactorOTP(TwoFactorOTP twoFactorOTP) {
        twoFactorOTPRepository.delete(twoFactorOTP);
    }
}
