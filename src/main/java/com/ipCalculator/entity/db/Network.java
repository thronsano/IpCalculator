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
    private String lastAvailableIp;

    @Column
    private String subnetMask;

    @Column
    private String name;

    public Network() {
    }

    public Network(String addressesAmount, String networkIp, String broadcastIp, String routerIp, String lastAvailableIp) {
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

    public Network setIpRangeBuilder(String routerIp, String lastAvailableIp) {
        this.routerIp = routerIp;
        this.lastAvailableIp = lastAvailableIp;
        return this;
    }

    public Network setNetworkIpBuilder(String networkIp) {
        this.networkIp = networkIp;
        return this;
    }

    public Network setBroadcastIpBuilder(String broadcastIp) {
        this.broadcastIp = broadcastIp;
        return this;
    }

    public Network setAddressesAmountBuilder(long addressesAmount) {
        this.addressesAmount = String.valueOf(addressesAmount);
        return this;
    }

    public Network setSubnetMaskBuilder(String subnetMask) {
        this.subnetMask = subnetMask;
        return this;
    }
}
