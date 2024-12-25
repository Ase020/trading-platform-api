package com.asejnr.tradingplatform.service;

import com.asejnr.tradingplatform.model.PaymentDetails;
import com.asejnr.tradingplatform.model.User;

public interface PaymentDetailsService {
    public PaymentDetails addPaymentDetails(String accountNumber, String accountHolderName, String ifscCode, String bankName, User user);
    public PaymentDetails getUserPaymentDetails(User user);

}
