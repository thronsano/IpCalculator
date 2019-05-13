package com.ipCalculator.MVC.services;

import com.ipCalculator.entity.db.Network;
import com.ipCalculator.entity.db.User;
import com.ipCalculator.entity.exceptions.IpCalculatorException;
import com.ipCalculator.entity.model.NetworkTable;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static com.ipCalculator.MVC.services.CacheService.networkCache;

@Repository
public class CalculatorService extends PersistenceService<Network> {

    @Autowired
    UserService userService;

    public NetworkTable getNetworkByMask(String networkIp, String networkMask) {
        return createNetwork(networkIp, networkMask);
    }

    public NetworkTable getNetworkByClientsAmount(String networkIp, String clientsAmountString, String paddingString) {
        int padding = Integer.valueOf(paddingString);
        int clientsAmount = Integer.valueOf(clientsAmountString);

        String networkMask = calculateMinimumMask(clientsAmount + (int) Math.ceil(clientsAmount * padding / 100.0));

        return createNetwork(networkIp, networkMask);
    }

    private String calculateMinimumMask(int clientsAmount) {
        int hostBits = (int) (32.0 - Math.ceil(Math.log(clientsAmount + 2) / Math.log(2)));
        return String.valueOf(hostBits);
    }

    private NetworkTable createNetwork(String networkIp, String networkMask) {
        SubnetInfo networkInfo = new SubnetUtils(networkIp + "/" + networkMask).getInfo();
        Network network = new Network()
                .setNetworkIpBuilder(networkInfo.getNetworkAddress())
                .setBroadcastIpBuilder(networkInfo.getBroadcastAddress())
                .setIpRangeBuilder(networkInfo.getLowAddress(), networkInfo.getHighAddress())
                .setAddressesAmountBuilder(networkInfo.getAddressCountLong())
                .setSubnetMaskBuilder(networkInfo.getNetmask());
        String networkCacheKey = UUID.randomUUID().toString();
        networkCache.put(networkCacheKey, network);

        return new NetworkTable(networkCacheKey, network);
    }

    public void saveNetwork(String networkCacheKey, String networkName) {
        Network network = networkCache.getIfPresent(networkCacheKey);

        if (network != null) {
            network.setName(networkName);
            User user = userService.getCurrentUser();
            user.getNetworks().add(network);
            userService.persistObject(user);
        } else {
            throw new IpCalculatorException(String.format("Couldn't find the network %s in cache!", networkCacheKey));
        }
    }

    public NetworkTable getPreviousNetworks() {
        User user = userService.getCurrentUser();
        List<Network> networks = user.getSortedNetworks();

        if (networks.size() > 0) {
            return new NetworkTable(networks);
        } else {
            return null;
        }
    }
}
