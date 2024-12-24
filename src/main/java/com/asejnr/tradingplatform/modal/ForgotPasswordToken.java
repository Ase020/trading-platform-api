package com.asejnr.tradingplatform.modal;

import com.asejnr.tradingplatform.domain.VerificationType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ForgotPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String otp;
    private VerificationType verificationType;
    private String sendTo;

    @OneToOne
    private User user;
}