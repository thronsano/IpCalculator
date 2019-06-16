package com.ipCalculator.entity.model;

import com.ipCalculator.entity.db.Network;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ipCalculator.MVC.services.CacheService.networkCache;

public class NetworkWorkflowResult {
    private String log = "";
    private String networkCacheKey;
    private List<Network> networkList = new ArrayList<>();

    public String getLog() {
        return log;
    }

    public List<Network> getNetworkList() {
        return networkList;
    }

    public void setNetworkList(List<Network> networkList) {
        this.networkList = networkList;
    }

    public void cacheNetworkList() {
        networkCacheKey = UUID.randomUUID().toString();
        networkCache.put(networkCacheKey, networkList);
    }

    public String getNetworkCacheKey() {
        return networkCacheKey;
    }

    public void appendLog(String message) {
        log += message;
    }
}
