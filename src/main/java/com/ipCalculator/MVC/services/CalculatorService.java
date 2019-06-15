package com.ipCalculator.MVC.services;

import com.ipCalculator.entity.builders.NetworkBuilder;
import com.ipCalculator.entity.db.Network;
import com.ipCalculator.entity.db.User;
import com.ipCalculator.entity.exceptions.IpCalculatorException;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static com.ipCalculator.MVC.services.CacheService.networkCache;
import static com.ipCalculator.utility.IpUtils.networksOverlap;
import static java.lang.Integer.parseInt;

@Repository
public class CalculatorService extends PersistenceService<Network> {

    @Autowired
    UserService userService;

    public Network getNetworkByMask(String networkAddress, String networkMask) {
        return createNetwork(networkAddress, networkMask);
    }

    public Network getNetworkByClientsAmount(String networkAddress, String clientsAmountString, String paddingString) {
        int padding = Integer.valueOf(paddingString);
        int clientsAmount = Integer.valueOf(clientsAmountString);

        String networkMask = calculateMinimumMask(clientsAmount + (int) Math.ceil(clientsAmount * padding / 100.0));

        return createNetwork(networkAddress, networkMask);
    }

    private String calculateMinimumMask(int clientsAmount) {
        int hostBits = (int) (32.0 - Math.ceil(Math.log(clientsAmount + 2) / Math.log(2)));
        return String.valueOf(hostBits);
    }

    private Network createNetwork(String networkAddress, String networkMask) {
        List<Network> allNetworks = getAllObjects("Network");
        String cidrNotation = networkAddress + "/" + networkMask;
        boolean collides = allNetworks.stream().anyMatch(network -> networksOverlap(network.getCidrAddress(), networkAddress + "/" + networkMask));

        if (collides) {
            cidrNotation = getNonCollidingCidr(cidrNotation, allNetworks);
        }

        SubnetInfo networkInfo = new SubnetUtils(cidrNotation).getInfo();
        Network network = new NetworkBuilder()
                .setNetworkAddress(networkInfo.getNetworkAddress())
                .setBroadcastIp(networkInfo.getBroadcastAddress())
                .setIpRange(networkInfo.getLowAddress(), networkInfo.getHighAddress())
                .setAddressesAmount(networkInfo.getAddressCountLong())
                .setSubnetMask(networkInfo.getNetmask())
                .build();
        String networkCacheKey = UUID.randomUUID().toString();
        networkCache.put(networkCacheKey, network);
        network.setNetworkCacheKey(networkCacheKey);
        return network;
    }

    //TODO: implement collision avoidance
    private String getNonCollidingCidr(String networkCidr, List<Network> allNetworks) {
        return networkCidr;
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

    public List<Network> getPreviousNetworks() {
        User user = userService.getCurrentUser();
        List<Network> networks = user.getSortedNetworks();

        if (networks.size() > 0) {
            return networks;
        } else {
            return null;
        }
    }

    public void deleteNetwork(String networkId) {
        deleteObjectById("Network", parseInt(networkId));
    }
}
