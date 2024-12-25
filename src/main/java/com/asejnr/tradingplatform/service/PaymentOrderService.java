package com.asejnr.tradingplatform.service;

import com.asejnr.tradingplatform.domain.PaymentMethod;
import com.asejnr.tradingplatform.model.PaymentOrder;
import com.asejnr.tradingplatform.model.User;
import com.asejnr.tradingplatform.response.PaymentOrderResponse;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentOrderService {
    PaymentOrder createPaymentOrder(User user, Long amount, PaymentMethod paymentMethod);
    PaymentOrder getPaymentOrderById(Long id) throws Exception;
    boolean processPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException;
    PaymentOrderResponse createRazorpayPaymentLink(User user, Long amount) throws RazorpayException;
    PaymentOrderResponse createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException;
}
