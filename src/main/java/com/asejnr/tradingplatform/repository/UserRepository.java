package com.asejnr.tradingplatform.repository;

import com.asejnr.tradingplatform.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
