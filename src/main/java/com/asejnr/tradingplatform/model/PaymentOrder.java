package com.asejnr.tradingplatform.model;

import com.asejnr.tradingplatform.domain.PaymentMethod;
import com.asejnr.tradingplatform.domain.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class PaymentOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long amount;
    private PaymentOrderStatus paymentOrderStatus;
    private PaymentMethod paymentMethod;

    @ManyToOne
    private User user;

}
