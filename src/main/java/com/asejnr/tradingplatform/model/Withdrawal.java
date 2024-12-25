package com.asejnr.tradingplatform.model;

import com.asejnr.tradingplatform.domain.WithdrawalStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Withdrawal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private WithdrawalStatus withdrawalStatus;
    private Long amount;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDateTime dateTime=LocalDateTime.now();

}
