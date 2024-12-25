package com.asejnr.tradingplatform.repository;

import com.asejnr.tradingplatform.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
}
