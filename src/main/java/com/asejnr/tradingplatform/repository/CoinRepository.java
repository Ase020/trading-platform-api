package com.asejnr.tradingplatform.repository;

import com.asejnr.tradingplatform.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin, String> {
}
