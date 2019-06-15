package com.ipCalculator.MVC.services;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ipCalculator.entity.db.Network;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CacheService {
    public static Cache<String, List<Network>> networkCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(60, TimeUnit.MINUTES)
            .build();
}
