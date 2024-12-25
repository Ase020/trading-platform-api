package com.asejnr.tradingplatform.service;

import com.asejnr.tradingplatform.domain.OrderType;
import com.asejnr.tradingplatform.model.Order;
import com.asejnr.tradingplatform.model.User;
import com.asejnr.tradingplatform.model.Wallet;
import com.asejnr.tradingplatform.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Override
    public Wallet getUserWallet(User user) {
        Wallet wallet = walletRepository.findByUserId(user.getId());
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUser(user);
        }
        return wallet;
    }

    @Override
    public Wallet addBalance(Wallet wallet, long amount) {
        BigDecimal balance = wallet.getBalance();
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(amount));
        wallet.setBalance(newBalance);

        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findWalletById(long walletId) throws Exception {
        Optional<Wallet> wallet = walletRepository.findById(walletId);
        if (wallet.isPresent()) {
            return wallet.get();
        }
        throw new Exception("Wallet not found.");
    }

    @Override
    public Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, long amount) throws Exception {
        Wallet senderWallet = getUserWallet(sender);

        if (senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
          throw new Exception("Insufficient balance!");
        }
        BigDecimal senderWalletBalance = senderWallet.getBalance().subtract(BigDecimal.valueOf(amount));
        senderWallet.setBalance(senderWalletBalance);
        walletRepository.save(senderWallet);

        BigDecimal receiverWalletBalance = receiverWallet.getBalance().add(BigDecimal.valueOf(amount));
        receiverWallet.setBalance(receiverWalletBalance);
        walletRepository.save(receiverWallet);

        return senderWallet;
    }

    @Override
    public Wallet payOrderPayment(Order order, User user) throws Exception {
        Wallet wallet = getUserWallet(user);
        BigDecimal newBalance;
        if (order.getOrderType().equals(OrderType.BUY)){
            newBalance = wallet.getBalance().subtract(order.getTotalPrice());
          if (newBalance.compareTo(order.getTotalPrice()) < 0)
              throw new Exception("Insufficient balance to complete transaction.");
        }else {
            newBalance = wallet.getBalance().add(order.getTotalPrice());
            wallet.setBalance(newBalance);
        }
        walletRepository.save(wallet);

        return wallet;
    }
}
