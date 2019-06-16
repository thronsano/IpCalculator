package com.ipCalculator.entity.model;

import com.google.common.collect.Ordering;
import com.ipCalculator.entity.db.Network;
import com.ipCalculator.utility.IpUtils;
import org.checkerframework.checker.nullness.qual.Nullable;

public class OrderingByAddress extends Ordering<Network> {

    @Override
    public int compare(@Nullable Network network, @Nullable Network networkTwo) {
        if (network == null || networkTwo == null || network.getNetworkAddress().equals(networkTwo.getNetworkAddress()))
            return 0;
        String higherIp = IpUtils.getHigherIp(network.getNetworkAddress(), networkTwo.getNetworkAddress());
        return network.getNetworkAddress().equals(higherIp) ? 1 : -1;
    }
}
