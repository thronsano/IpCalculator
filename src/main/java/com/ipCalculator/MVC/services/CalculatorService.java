package com.ipCalculator.MVC.services;

import com.ipCalculator.entity.builders.NetworkBuilder;
import com.ipCalculator.entity.db.Network;
import com.ipCalculator.entity.db.User;
import com.ipCalculator.entity.exceptions.IpCalculatorException;
import com.ipCalculator.utility.IpUtils;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ipCalculator.MVC.services.CacheService.networkCache;
import static com.ipCalculator.utility.IpUtils.networksOverlap;
import static java.lang.Integer.parseInt;

@Repository
public class CalculatorService extends PersistenceService<Network> {

    @Autowired
    UserService userService;

    public List<Network> createNetworkUsingMask(String networkAddress, String networkMask, String subnetAmount) {
        return createNetworks(networkAddress, networkMask, Integer.parseInt(subnetAmount));
    }

    public List<Network> createNetworkUsingClientsAmount(String networkAddress, String clientsAmountString, String paddingString, String subnetAmount) {
        int padding = Integer.valueOf(paddingString);
        int clientsAmount = Integer.valueOf(clientsAmountString);

        String networkMask = calculateMinimumMask(clientsAmount + (int) Math.ceil(clientsAmount * padding / 100.0));

        return createNetworks(networkAddress, networkMask, Integer.parseInt(subnetAmount));
    }

    private String calculateMinimumMask(int clientsAmount) {
        int hostBits = (int) (32.0 - Math.ceil(Math.log(clientsAmount + 2) / Math.log(2)));
        return String.valueOf(hostBits);
    }

    private List<Network> createNetworks(String address, String networkMask, int subnetAmount) {
        List<String> subnets = IpUtils.divideNetworkToSubnets(getNonCollidingCidr(convertToNetworkCidr(address, networkMask)), subnetAmount);
        String networkCacheKey = UUID.randomUUID().toString();
        List<Network> networkList = new ArrayList<>();

        for (String subnet : subnets) {
            SubnetInfo networkInfo = new SubnetUtils(subnet).getInfo();
            networkList.add(new NetworkBuilder()
                    .setNetworkAddress(networkInfo.getNetworkAddress())
                    .setBroadcastIp(networkInfo.getBroadcastAddress())
                    .setIpRange(networkInfo.getLowAddress(), networkInfo.getHighAddress())
                    .setAddressesAmount(networkInfo.getAddressCountLong())
                    .setSubnetMask(networkInfo.getNetmask())
                    .setNetworkCacheKey(networkCacheKey)
                    .build());
        }

        networkCache.put(networkCacheKey, networkList);
        return networkList;
    }

    private String convertToNetworkCidr(String address, String mask) {
        String networkAddress = new SubnetUtils(address + "/" + mask).getInfo().getNetworkAddress();
        return networkAddress + "/" + mask;
    }

    private String getNonCollidingCidr(String networkCidr) {
        boolean collision = getAllObjects("Network").stream().anyMatch(network -> networksOverlap(network.getCidrAddress(), networkCidr));

        if (collision) {
            //TODO: IMPLEMENT
        }

        return networkCidr;
    }


    public void saveNetworks(String networkCacheKey, String networkName) {
        List<Network> networks = networkCache.getIfPresent(networkCacheKey);

        if (networks != null) {
            for (Network network : networks) {
                network.setName(networkName);
                User user = userService.getCurrentUser();
                user.getNetworks().add(network);
                userService.persistObject(user);
            }
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
