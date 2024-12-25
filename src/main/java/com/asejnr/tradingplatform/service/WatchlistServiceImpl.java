package com.asejnr.tradingplatform.service;

import com.asejnr.tradingplatform.model.Coin;
import com.asejnr.tradingplatform.model.User;
import com.asejnr.tradingplatform.model.Watchlist;
import com.asejnr.tradingplatform.repository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class WatchlistServiceImpl implements WatchlistService {
    @Autowired
    private WatchlistRepository watchlistRepository;

    @Override
    public Watchlist findUserWatchlist(Long userId) throws Exception {
        Watchlist watchlist = watchlistRepository.findByUserId(userId);
        if (watchlist == null) {
            throw new Exception("Watchlist not found");
        }

        return watchlist;
    }

    @Override
    public Watchlist createUserWatchlist(User user) {
        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);
        return watchlistRepository.save(watchlist);
    }

    @Override
    public Watchlist findWatchlistById(Long watchlistId) throws Exception {
        Optional<Watchlist> watchlist = watchlistRepository.findById(watchlistId);
        if (watchlist.isEmpty()) {
            throw new Exception("Watchlist not found.");
        }
        return watchlist.get();
    }

    @Override
    public Coin addCoinToWatchlist(Coin coin, User user) throws Exception {
        Watchlist watchlist = findUserWatchlist(user.getId());
        if (watchlist == null) {
            watchlist = new Watchlist();
            watchlist.setUser(user);
        }

        if (watchlist.getCoins().contains(coin)) {
            watchlist.getCoins().remove(coin);
        }else {
            watchlist.getCoins().add(coin);
        }
        watchlistRepository.save(watchlist);
        return coin;
    }
}
