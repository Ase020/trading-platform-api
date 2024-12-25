package com.asejnr.tradingplatform.controller;

import com.asejnr.tradingplatform.domain.PaymentMethod;
import com.asejnr.tradingplatform.model.PaymentOrder;
import com.asejnr.tradingplatform.model.User;
import com.asejnr.tradingplatform.response.PaymentOrderResponse;
import com.asejnr.tradingplatform.service.PaymentOrderService;
import com.asejnr.tradingplatform.service.UserService;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentOrderController {
    @Autowired
    private UserService userService;
    @Autowired
    private PaymentOrderService paymentOrderService;

    @PostMapping("/payment/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentOrderResponse> paymentHandler(
            @PathVariable PaymentMethod paymentMethod,
            @PathVariable Long amount,
            @RequestHeader("Authorization") String jwtToken
            ) throws Exception, RazorpayException, StripeException {
        User user = userService.findUserProfileByJwt(jwtToken);
        PaymentOrderResponse paymentOrderResponse;

        PaymentOrder paymentOrder = paymentOrderService.createPaymentOrder(user,amount,paymentMethod);

        if (paymentMethod.equals(PaymentMethod.RAZORPAY)){
            paymentOrderResponse = paymentOrderService.createRazorpayPaymentLink(user,amount);
        }else {
            paymentOrderResponse = paymentOrderService.createStripePaymentLink(user, amount, paymentOrder.getId());
        }
        return new ResponseEntity<>(paymentOrderResponse, HttpStatus.CREATED);
    }
}
