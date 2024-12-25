package com.asejnr.tradingplatform.service;

import com.asejnr.tradingplatform.domain.WithdrawalStatus;
import com.asejnr.tradingplatform.model.User;
import com.asejnr.tradingplatform.model.Withdrawal;
import com.asejnr.tradingplatform.repository.WithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {
    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Override
    public Withdrawal requestWithdrawal(Long amount, User user) {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setAmount(amount);
        withdrawal.setUser(user);
        withdrawal.setWithdrawalStatus(WithdrawalStatus.PENDING);

        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public Withdrawal proceedWithdrawal(Long withdrawalId, boolean accepted) throws Exception {
        Optional<Withdrawal> withdrawal = withdrawalRepository.findById(withdrawalId);
        if (withdrawal.isEmpty()) {
            throw new Exception("Withdrawal not found.");
        }
        Withdrawal withdrawal1 = withdrawal.get();
        withdrawal1.setDateTime(LocalDateTime.now());

        if (accepted) {
            withdrawal1.setWithdrawalStatus(WithdrawalStatus.SUCCESS);
        }else {
            withdrawal1.setWithdrawalStatus(WithdrawalStatus.DECLINED);
        }
        return withdrawalRepository.save(withdrawal1);
    }

    @Override
    public List<Withdrawal> getUserWithdrawalHistory(User user) {
        return withdrawalRepository.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawal> getAllWithdrawalRequests() {
        return withdrawalRepository.findAll();
    }
}
