package com.asejnr.tradingplatform.repository;

import com.asejnr.tradingplatform.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
