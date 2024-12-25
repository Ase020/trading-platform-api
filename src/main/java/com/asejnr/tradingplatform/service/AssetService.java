package com.asejnr.tradingplatform.service;

import com.asejnr.tradingplatform.model.Asset;
import com.asejnr.tradingplatform.model.Coin;
import com.asejnr.tradingplatform.model.User;

import java.util.List;

public interface AssetService {
    Asset createAsset(User user, Coin coin, double quantity);
    Asset getAssetById(Long assetId) throws Exception;
    Asset getAssetByUserIdAndId(Long userId, Long assetId);
    List<Asset> getAssetsByUserId(Long userId);
    Asset updateAsset(Long assetId, double quantity) throws Exception;
    Asset findAssetByUserIdAndCoinId(Long userId, String coinId);
    void deleteAsset(Long assetId);

}