package com.asejnr.tradingplatform.controller;

import com.asejnr.tradingplatform.domain.WalletTransactionType;
import com.asejnr.tradingplatform.model.User;
import com.asejnr.tradingplatform.model.Wallet;
import com.asejnr.tradingplatform.model.WalletTransaction;
import com.asejnr.tradingplatform.model.Withdrawal;
import com.asejnr.tradingplatform.service.UserService;
import com.asejnr.tradingplatform.service.WalletService;
import com.asejnr.tradingplatform.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class WithdrawalController {
    @Autowired
    private WithdrawalService withdrawalService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private UserService userService;
//    @Autowired
//    private WalletTransactionService walletTransactionService;

    @PostMapping("/api/withdrawals/{amount}")
    public ResponseEntity<?> withdrawalRequest(
            @PathVariable Long amount,
            @RequestHeader("Authorization") String jwtToken
    ) throws Exception{
        User user = userService.findUserProfileByJwt(jwtToken);
        Wallet userWallet = walletService.getUserWallet(user);

        Withdrawal withdrawal = withdrawalService.requestWithdrawal(amount, user);
        walletService.addBalance(userWallet, -withdrawal.getAmount());

//        WalletTransaction walletTransaction = walletTransactionService.createTransaction(
//                userWallet,
//                WalletTransactionType.WITHDRAWAL,
//                null,
//                "bank account withdrawal",
//                withdrawal.getAmount()
//        );
        return ResponseEntity.ok(withdrawal);
    }

    @PatchMapping("/api/admin/withdrawals/{id}/proceed/{accepted}")
    public ResponseEntity<?> proceedWithdrawalRequest(
            @PathVariable Long id,
            @PathVariable boolean accepted,
            @RequestHeader("Authorization") String jwtToken
    ) throws Exception{
        User user = userService.findUserProfileByJwt(jwtToken);
        Withdrawal withdrawal = withdrawalService.proceedWithdrawal(id, accepted);

        Wallet userWallet = walletService.getUserWallet(user);
        if (!accepted){
            walletService.addBalance(userWallet, withdrawal.getAmount());
        }
        return ResponseEntity.ok(withdrawal);
    }

    @GetMapping("/api/withdrawal")
    public ResponseEntity<?> getWithdrawalHistory(
            @RequestHeader("Authorization") String jwtToken
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwtToken);
        List<Withdrawal> withdrawals = withdrawalService.getUserWithdrawalHistory(user);

        return ResponseEntity.ok(withdrawals);
    }
}
