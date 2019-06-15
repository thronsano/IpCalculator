package com.ipCalculator.entity.db;

import javax.persistence.*;

@Entity
@Table(name = "network")
public class Network {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String addressesAmount;

    @Column
    private String networkIp;

    @Column
    private String broadcastIp;

    @Column
    private String routerIp;

    @Column
    private String firstAvailableIp;

    @Column
    private String lastAvailableIp;

    @Column
    private String subnetMask;

    @Column
    private String name;

    @Transient
    private String networkCacheKey;

    public Network() {
    }

    public Network(String addressesAmount, String networkIp, String broadcastIp, String routerIp, String firstAvailableIp, String lastAvailableIp) {
        this.addressesAmount = addressesAmount;
        this.networkIp = networkIp;
        this.broadcastIp = broadcastIp;
        this.routerIp = routerIp;
        this.lastAvailableIp = lastAvailableIp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddressesAmount() {
        return addressesAmount;
    }

    public void setAddressesAmount(String addressesAmount) {
        this.addressesAmount = addressesAmount;
    }

    public String getNetworkIp() {
        return networkIp;
    }

    public void setNetworkIp(String networkIp) {
        this.networkIp = networkIp;
    }

    public String getBroadcastIp() {
        return broadcastIp;
    }

    public void setBroadcastIp(String broadcastIp) {
        this.broadcastIp = broadcastIp;
    }

    public String getRouterIp() {
        return routerIp;
    }

    public void setRouterIp(String routerIp) {
        this.routerIp = routerIp;
    }

    public String getFirstAvailableIp() {
        return firstAvailableIp;
    }

    public void setFirstAvailableIp(String firstAvailableIp) {
        this.firstAvailableIp = firstAvailableIp;
    }

    public String getLastAvailableIp() {
        return lastAvailableIp;
    }

    public void setLastAvailableIp(String lastAvailableIp) {
        this.lastAvailableIp = lastAvailableIp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubnetMask() {
        return subnetMask;
    }

    public void setSubnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
    }

    public String getNetworkCacheKey() {
        return networkCacheKey;
    }

    public void setNetworkCacheKey(String networkCacheKey) {
        this.networkCacheKey = networkCacheKey;
    }
}
