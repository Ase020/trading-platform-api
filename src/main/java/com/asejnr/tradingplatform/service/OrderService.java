package com.asejnr.tradingplatform.service;

import com.asejnr.tradingplatform.domain.OrderType;
import com.asejnr.tradingplatform.model.Order;
import com.asejnr.tradingplatform.model.OrderItem;
import com.asejnr.tradingplatform.model.User;

import java.util.List;

public interface OrderService {
    Order createOrder(User user, OrderItem orderItem, OrderType orderType);
    Order getOrderById(Long orderId);
    List<Order> getAllOrdersOfUser(User user, OrderType orderType, String assetSymbol);
}
