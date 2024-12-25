package com.asejnr.tradingplatform.service;

import com.asejnr.tradingplatform.model.Coin;
import com.asejnr.tradingplatform.model.User;
import com.asejnr.tradingplatform.model.Watchlist;

public interface WatchlistService {
    Watchlist findUserWatchlist(Long userId) throws Exception;
    Watchlist createUserWatchlist(User user);
    Watchlist findWatchlistById(Long watchlistId) throws Exception;
    Coin addCoinToWatchlist(Coin coin, User user) throws Exception;
}
