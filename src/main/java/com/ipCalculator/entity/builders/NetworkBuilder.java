package com.ipCalculator.entity.builders;

import com.ipCalculator.entity.db.Network;

public class NetworkBuilder {
    Network network = new Network();

    public NetworkBuilder setIpRange(String routerIp, String lastAvailableIp) {
        network.setRouterIp(routerIp);
        network.setLastAvailableIp(lastAvailableIp);
        return this;
    }

    public NetworkBuilder setNetworkIp(String networkIp) {
        network.setNetworkIp(networkIp);
        return this;
    }

    public NetworkBuilder setBroadcastIp(String broadcastIp) {
        network.setBroadcastIp(broadcastIp);
        return this;
    }

    public NetworkBuilder setAddressesAmount(long addressesAmount) {
        network.setAddressesAmount(String.valueOf(addressesAmount));
        return this;
    }

    public NetworkBuilder setSubnetMask(String subnetMask) {
        network.setSubnetMask(subnetMask);
        return this;
    }

    public Network build() {
        return network;
    }
}
