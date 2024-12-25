package com.asejnr.tradingplatform.service;

import com.asejnr.tradingplatform.domain.OrderType;
import com.asejnr.tradingplatform.model.Order;
import com.asejnr.tradingplatform.model.OrderItem;
import com.asejnr.tradingplatform.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public Order createOrder(User user, OrderItem orderItem, OrderType orderType) {
        return null;
    }

    @Override
    public Order getOrderById(Long orderId) {

        return null;
    }

    @Override
    public List<Order> getAllOrdersOfUser(User user, OrderType orderType, String assetSymbol) {
        return List.of();
    }
}
