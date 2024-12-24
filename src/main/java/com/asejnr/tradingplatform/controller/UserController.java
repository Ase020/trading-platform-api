package com.asejnr.tradingplatform.controller;

import com.asejnr.tradingplatform.domain.VerificationType;
import com.asejnr.tradingplatform.modal.User;
import com.asejnr.tradingplatform.modal.VerificationCode;
import com.asejnr.tradingplatform.service.EmailService;
import com.asejnr.tradingplatform.service.UserService;
import com.asejnr.tradingplatform.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private VerificationCodeService verificationCodeService;

    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwtToken) throws Exception {
        User user = userService.findUserProfileByJwt(jwtToken);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOTP(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable VerificationType verificationType) throws Exception {
        User user = userService.findUserProfileByJwt(jwtToken);

        VerificationCode verificationCode = verificationCodeService.getVerificationOTPCodeByUser(user.getId());

        if (verificationCode == null) {
            verificationCode = verificationCodeService.sendVerificationOTPCode(user, verificationType);
        }
        if (verificationType.equals(VerificationType.EMAIL))
            emailService.sendVerificationOtpEmail(user.getEmail(), verificationCode.getOtpCode());

        return new ResponseEntity<>("Verification OTP sent successfully", HttpStatus.OK);
    }

    @PatchMapping("/api/users/enable-two-factor-auth/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable String otp) throws Exception {
        User user = userService.findUserProfileByJwt(jwtToken);

        VerificationCode verificationCode = verificationCodeService.getVerificationOTPCodeByUser(user.getId());
        String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL) ? verificationCode.getEmail() : verificationCode.getMobile();

        boolean isVerified = verificationCode.getOtpCode().equals(otp);
        if (isVerified) {
            User updatedUser = userService.enableTwoFactorAuthentication(
                    verificationCode.getVerificationType(), sendTo, user);

            verificationCodeService.deleteVerificationOTPCodeById(verificationCode);

            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        throw new Exception("Wrong OTP");
    }
}