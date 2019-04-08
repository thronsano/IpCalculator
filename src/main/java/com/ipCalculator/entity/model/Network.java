package com.ipCalculator.entity.model;

public class Network {
    private String networkIp;
    private String broadcastIp;
    private String[] ipRange;

    public Network setIpRange(String startingIp, String lastIp) {
        ipRange = new String[2];
        ipRange[0] = startingIp;
        ipRange[1] = lastIp;
        return this;
    }

    public String getNetworkIp() {
        return networkIp;
    }

    public Network setNetworkIp(String networkIp) {
        this.networkIp = networkIp;
        return this;
    }

    public String getBroadcastIp() {
        return broadcastIp;
    }

    public Network setBroadcastIp(String broadcastIp) {
        this.broadcastIp = broadcastIp;
        return this;
    }

    public String getFirstAvailableIp() {
        return ipRange[0];
    }

    public String getLastAvailableIp() {
        return ipRange[1];
    }
}
