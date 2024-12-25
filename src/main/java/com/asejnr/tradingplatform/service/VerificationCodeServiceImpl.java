package com.asejnr.tradingplatform.service;

import com.asejnr.tradingplatform.domain.VerificationType;
import com.asejnr.tradingplatform.model.User;
import com.asejnr.tradingplatform.model.VerificationCode;
import com.asejnr.tradingplatform.repository.VerificationCodeRepository;
import com.asejnr.tradingplatform.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {
    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Override
    public VerificationCode sendVerificationOTPCode(User user, VerificationType verificationType) {
        VerificationCode verificationCode1 = new VerificationCode();
        verificationCode1.setOtpCode(OtpUtils.generateOtp());
        verificationCode1.setVerificationType(verificationType);
        verificationCode1.setUser(user);

        return verificationCodeRepository.save(verificationCode1);
    }

    @Override
    public VerificationCode getVerificationOTPCodeById(Long id) throws Exception {
        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findById(id);
        if (verificationCodeOptional.isEmpty()) {
            throw new Exception("Verification not found");
        } else {
            return verificationCodeOptional.get();
        }
    }

    @Override
    public VerificationCode getVerificationOTPCodeByUser(Long userId) {
        return verificationCodeRepository.findByUserId(userId);
    }

    @Override
    public void deleteVerificationOTPCodeById(VerificationCode verificationCode) {
        verificationCodeRepository.delete(verificationCode);
    }

}
