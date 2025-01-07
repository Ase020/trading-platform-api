package com.asejnr.tradingplatform.model;

import com.asejnr.tradingplatform.domain.OrderStatus;
import com.asejnr.tradingplatform.domain.OrderType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private User user;
    @Column(nullable = false)
    private OrderType orderType;
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private BigDecimal totalPrice;
    private LocalDateTime orderDateTime=LocalDateTime.now();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "order", fetch = FetchType.EAGER)
    private OrderItem orderItem;
}
