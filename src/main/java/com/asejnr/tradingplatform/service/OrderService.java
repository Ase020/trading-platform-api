package com.asejnr.tradingplatform.service;

import com.asejnr.tradingplatform.domain.OrderType;
import com.asejnr.tradingplatform.model.Coin;
import com.asejnr.tradingplatform.model.Order;
import com.asejnr.tradingplatform.model.OrderItem;
import com.asejnr.tradingplatform.model.User;

import java.util.List;

public interface OrderService {
    Order createOrder(User user, OrderItem orderItem, OrderType orderType);
    Order getOrderById(Long orderId) throws Exception;
    List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assetSymbol);
    OrderItem createOrderItem(Coin coin, double quantity, double buyPrice, double sellPrice);
    Order buyAsset(Coin coin, double quantity, User user) throws Exception;
    Order sellAsset(Coin coin, double quantity, User user) throws Exception;
    Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception;
}
