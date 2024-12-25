package com.asejnr.tradingplatform.controller;

import com.asejnr.tradingplatform.domain.OrderType;
import com.asejnr.tradingplatform.model.Coin;
import com.asejnr.tradingplatform.model.Order;
import com.asejnr.tradingplatform.model.User;
import com.asejnr.tradingplatform.model.Withdrawal;
import com.asejnr.tradingplatform.request.CreateOrderRequest;
import com.asejnr.tradingplatform.service.CoinService;
import com.asejnr.tradingplatform.service.OrderService;
import com.asejnr.tradingplatform.service.UserService;
import com.asejnr.tradingplatform.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;
    @Autowired
    private WithdrawalService withdrawalService;
//    @Autowired
//    private WallTransactionService wallTransactionService;

    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(
            @RequestHeader("Authorization") String jwtToken,
            @RequestBody CreateOrderRequest createOrderRequest) throws Exception {
        User user = userService.findUserProfileByJwt(jwtToken);
        Coin coin = coinService.findCoinById(createOrderRequest.getCoinId());

        Order order = orderService.processOrder(coin, createOrderRequest.getQuantity(), createOrderRequest. getOrderType(),user);

        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable("orderId") Long orderId) throws Exception {
        User user = userService.findUserProfileByJwt(jwtToken);
        Order order = orderService.getOrderById(orderId);
        if (order.getUser().getId().equals(user.getId())) {
            return ResponseEntity.ok(order);
        }else {
           throw new Exception("User doesn't have permission to access order");
        }
    }

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrdersForUser(
            @RequestHeader("Authorization") String jwtToken,
            @RequestParam(required = false) OrderType orderType,
            @RequestParam(required = false) String assetSymbol
    ) throws Exception {
        Long userId = userService.findUserProfileByJwt(jwtToken).getId();
        List<Order> userOrders = orderService.getAllOrdersOfUser(userId,orderType,assetSymbol);

        return ResponseEntity.ok(userOrders);
    }

    @GetMapping("/api/admin/withdrawal")
    public ResponseEntity<List<Withdrawal>> getAllWithdrawalRequest(
            @RequestHeader("Authorization") String jwtToken
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwtToken);
        List<Withdrawal> withdrawals = withdrawalService.getAllWithdrawalRequests();

        return ResponseEntity.ok(withdrawals);
    }
}
