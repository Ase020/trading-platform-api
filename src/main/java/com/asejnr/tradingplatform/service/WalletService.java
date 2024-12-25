package com.asejnr.tradingplatform.service;

import com.asejnr.tradingplatform.model.Order;
import com.asejnr.tradingplatform.model.User;
import com.asejnr.tradingplatform.model.Wallet;

public interface WalletService {
    Wallet getUserWallet(User user);
    Wallet addBalance(Wallet wallet, long amount);
    Wallet findWalletById(long walletId) throws Exception;
    Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, long amount) throws Exception;
    Wallet payOrderPayment(Order order, User user) throws Exception;
}
