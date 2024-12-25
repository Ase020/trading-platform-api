package com.asejnr.tradingplatform.request;

import com.asejnr.tradingplatform.domain.OrderType;
import lombok.Data;

@Data
public class CreateOrderRequest {
    private String coinId;
    private double quantity;
    private OrderType orderType;
}
