package com.asejnr.tradingplatform.controller;

import com.asejnr.tradingplatform.domain.VerificationType;
import com.asejnr.tradingplatform.modal.ForgotPasswordToken;
import com.asejnr.tradingplatform.modal.User;
import com.asejnr.tradingplatform.modal.VerificationCode;
import com.asejnr.tradingplatform.request.ForgotPasswordTokenRequest;
import com.asejnr.tradingplatform.request.ResetPasswordRequest;
import com.asejnr.tradingplatform.response.ApiResponse;
import com.asejnr.tradingplatform.response.AuthResponse;
import com.asejnr.tradingplatform.service.EmailService;
import com.asejnr.tradingplatform.service.ForgotPasswordService;
import com.asejnr.tradingplatform.service.UserService;
import com.asejnr.tradingplatform.service.VerificationCodeService;
import com.asejnr.tradingplatform.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private ForgotPasswordService forgotPasswordService;

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

    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOTP(
            @RequestBody ForgotPasswordTokenRequest forgotPasswordTokenRequest) throws Exception {

        User user = userService.findUserByEmail(forgotPasswordTokenRequest.getSendTo());
        String otp = OtpUtils.generateOtp();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findByUserId(user.getId());

        if (forgotPasswordToken == null) forgotPasswordToken = forgotPasswordService.createForgotPasswordToken(user,
                id,
                otp,
                forgotPasswordTokenRequest.getVerificationType(),
                forgotPasswordTokenRequest.getSendTo());

        if (forgotPasswordTokenRequest.getVerificationType().equals(VerificationType.EMAIL))
            emailService.sendVerificationOtpEmail(user.getEmail(), forgotPasswordToken.getOtp());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setSession(forgotPasswordToken.getId());
        authResponse.setMessage("Password reset OTP sent successfully");

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> verifyForgotPasswordOTP(@RequestParam String id, @RequestBody ResetPasswordRequest resetPasswordRequest) throws Exception {

        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findById(id);

        boolean isVerified = forgotPasswordToken.getOtp().equals(resetPasswordRequest.getOtp());

        if (isVerified) {
            userService.updatePassword(forgotPasswordToken.getUser(), resetPasswordRequest.getPassword());

            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setMessage("Password reset successfully");

            return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
        }
        throw new Exception("Wrong OTP. Failed to verify password");
    }
}