package com.ipCalculator.MVC.services;

import com.ipCalculator.entity.model.Network;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;
import org.springframework.stereotype.Repository;

@Repository
public class CalculatorService {
    public Network getNetworkByMask(String networkIp, String networkMask) {
        return createNetwork(networkIp, networkMask);
    }

    public Network getNetworkByClientsAmount(String networkIp, String clientsAmount) {
        String networkMask = calculateMinimumMask(clientsAmount);
        return createNetwork(networkIp, networkMask);
    }

    private String calculateMinimumMask(String clientsAmount) {
        int hostBits = (int) (32.0 - Math.ceil(Math.log(Integer.valueOf(clientsAmount) + 2) / Math.log(2)));
        return String.valueOf(hostBits);
    }

    private Network createNetwork(String networkIp, String networkMask) {
        SubnetInfo networkInfo = new SubnetUtils(networkIp + "/" + networkMask).getInfo();

        return new Network()
                .setNetworkIp(networkInfo.getNetworkAddress())
                .setBroadcastIp(networkInfo.getBroadcastAddress())
                .setIpRange(networkInfo.getLowAddress(), networkInfo.getHighAddress());
    }
}
