package com.ipCalculator.MVC.services;

import com.ipCalculator.entity.model.Network;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;
import org.springframework.stereotype.Repository;

@Repository
public class CalculatorService {
    public Network getNetwork(String networkIp, String networkMask) {
        SubnetInfo networkInfo = new SubnetUtils(networkIp + "/" + networkMask).getInfo();

        return new Network()
                .setNetworkIp(networkInfo.getNetworkAddress())
                .setBroadcastIp(networkInfo.getBroadcastAddress())
                .setIpRange(networkInfo.getLowAddress(), networkInfo.getHighAddress());
    }
}
