package com.asejnr.tradingplatform.controller;

import com.asejnr.tradingplatform.model.Coin;
import com.asejnr.tradingplatform.model.User;
import com.asejnr.tradingplatform.model.Watchlist;
import com.asejnr.tradingplatform.service.CoinService;
import com.asejnr.tradingplatform.service.UserService;
import com.asejnr.tradingplatform.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {
    @Autowired
    private WatchlistService watchlistService;
    @Autowired
    UserService userService;
    @Autowired
    private CoinService coinService;

    @GetMapping("/user")
    public ResponseEntity<Watchlist> findUserWatchlist(@RequestHeader("Authorization") String jwtToken) throws Exception {
        User user = userService.findUserProfileByJwt(jwtToken);
        Watchlist watchlist = watchlistService.findUserWatchlist(user.getId());

        return new ResponseEntity<>(watchlist, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Watchlist> createUserWatchlist(@RequestHeader("Authorization") String jwtToken) throws Exception {
        User user = userService.findUserProfileByJwt(jwtToken);
        Watchlist watchlist = watchlistService.createUserWatchlist(user);

        return new ResponseEntity<>(watchlist, HttpStatus.CREATED);
    }

    @GetMapping("/{watchlistId}")
    public ResponseEntity<Watchlist> findUserWatchlistById(@PathVariable("watchlistId") Long watchlistId) throws Exception {
        Watchlist watchlist = watchlistService.findWatchlistById(watchlistId);

        return new ResponseEntity<>(watchlist, HttpStatus.OK);
    }

    @PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> addCoinToWatchlist(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String coinId
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Coin coin = coinService.findCoinById(coinId);
        Coin addedCoin = watchlistService.addCoinToWatchlist(coin, user);

        return new ResponseEntity<>(addedCoin, HttpStatus.OK);
    }
}
