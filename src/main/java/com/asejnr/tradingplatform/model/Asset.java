package com.asejnr.tradingplatform.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "asset")
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private double quantity;
    private double buyPrice;
    private double sellPrice;

    @ManyToOne
    private Coin coin;

    @ManyToOne
    private User user;
}
