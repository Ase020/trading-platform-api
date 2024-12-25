package com.asejnr.tradingplatform.service;

import com.asejnr.tradingplatform.domain.PaymentMethod;
import com.asejnr.tradingplatform.domain.PaymentOrderStatus;
import com.asejnr.tradingplatform.model.PaymentOrder;
import com.asejnr.tradingplatform.model.User;
import com.asejnr.tradingplatform.repository.PaymentOrderRepository;
import com.asejnr.tradingplatform.response.PaymentOrderResponse;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentOrderServiceImpl implements PaymentOrderService {
    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Value("${stripe.api.key")
    private String stripeSecretKey;

    @Value("${razorpay.api.key}")
    private String razorpayApiKey;

    @Value("${razorpay.api.secret}")
    private String razorpaySecretKey;

    @Override
    public PaymentOrder createPaymentOrder(User user, Long amount, PaymentMethod paymentMethod) {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setAmount(amount);
        paymentOrder.setPaymentMethod(paymentMethod);
        paymentOrder.setUser(user);

        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {

        return paymentOrderRepository.findById(id).orElseThrow(()->new Exception("Payment order not found."));
    }

    @Override
    public boolean processPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {
        if (paymentOrder.getPaymentOrderStatus().equals(PaymentOrderStatus.PENDING)){
            if (paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){
                RazorpayClient razorpayClient = new RazorpayClient(razorpayApiKey, razorpaySecretKey);
                Payment payment = razorpayClient.payments.fetch(paymentId);
                Integer amount = payment.get("amount");
                String status = payment.get("status");

                if (status.equals("captured")){
                    paymentOrder.setPaymentOrderStatus(PaymentOrderStatus.SUCCESS);
                    return true;
                }
                paymentOrder.setPaymentOrderStatus(PaymentOrderStatus.FAILED);
                paymentOrderRepository.save(paymentOrder);
                return false;
            }
            paymentOrder.setPaymentOrderStatus(PaymentOrderStatus.SUCCESS);
            paymentOrderRepository.save(paymentOrder);
            return true;
        }
        return false;
    }

    @Override
    public PaymentOrderResponse createRazorpayPaymentLink(User user, Long amount) throws RazorpayException {
        Long Amount = amount * 100;

        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorpayApiKey, razorpaySecretKey);

//            Create a JSON object with the payment link request params
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", Amount);
            paymentLinkRequest.put("currency", "USD");

//            Create a JSON object with the customer details
            JSONObject customerRequest = new JSONObject();
            customerRequest.put("name", user.getFullName());
            customerRequest.put("email", user.getEmail());
            customerRequest.put("customer", customerRequest);

//            Create a JSON object with the notification settings
            JSONObject notificationRequest = new JSONObject();
            notificationRequest.put("email", true);
            paymentLinkRequest.put("notify", notificationRequest);

//            Set the reminder settings
            paymentLinkRequest.put("reminder_enable", true);

//            Set the callback URL and method
            paymentLinkRequest.put("callback_url", "http://localhost:5173/wallet");
            paymentLinkRequest.put("callback_method", "get");

//            Create the payment link using the paymentLink.create() method
            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);

            String paymentLinkId = paymentLink.get("id");
            String paymentLinkUrl = paymentLink.get("short_url");

            PaymentOrderResponse paymentOrderResponse = new PaymentOrderResponse();
            paymentOrderResponse.setPaymentUrl(paymentLinkUrl);

            return paymentOrderResponse;
        } catch (RazorpayException e) {
            log.error("Error creating payment link: {}", e.getMessage());
            throw new RazorpayException(e.getMessage());
        }
    }

    @Override
    public PaymentOrderResponse createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams sessionCreateParams = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/wallet?order_id="+orderId)
                .setCancelUrl("http://localhost:5173/payment/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amount*100)
                                .setProductData(SessionCreateParams
                                        .LineItem
                                        .PriceData
                                        .ProductData
                                        .builder()
                                        .setName("Top up wallet")
                                        .build()
                                ).build()
                        ).build()
                ).build();

        Session session = Session.create(sessionCreateParams);
        log.info("Session ___ " + session);
        System.out.println("Session ___ " + session);

        PaymentOrderResponse paymentOrderResponse = new PaymentOrderResponse();
        paymentOrderResponse.setPaymentUrl(session.getUrl());
        return paymentOrderResponse;
    }
}
