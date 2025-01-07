package com.asejnr.tradingplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double quantity;
    private double buyPrice;
    private double sellPrice;

    @ManyToOne
    private Coin coin;
    @JsonIgnore
    @OneToOne
    private Order order;
}
