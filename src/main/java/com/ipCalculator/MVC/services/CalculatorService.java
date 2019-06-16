package com.ipCalculator.MVC.services;

import com.ipCalculator.entity.builders.NetworkBuilder;
import com.ipCalculator.entity.db.Network;
import com.ipCalculator.entity.db.User;
import com.ipCalculator.entity.exceptions.IpCalculatorException;
import com.ipCalculator.entity.model.NetworkWorkflowResult;
import com.ipCalculator.utility.IpUtils;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.ipCalculator.MVC.services.CacheService.networkCache;
import static java.lang.Integer.parseInt;

@Repository
public class CalculatorService extends PersistenceService<Network> {

    @Autowired
    UserService userService;

    public NetworkWorkflowResult createNetworkUsingMask(String networkAddress, String networkMask, String subnetAmount) {
        return createNetworks(convertToNetworkCidr(networkAddress, networkMask), Integer.parseInt(subnetAmount));
    }

    public NetworkWorkflowResult createNetworkUsingClientsAmount(String networkAddress, String clientsAmountString, String paddingString, String subnetAmount) {
        int padding = Integer.valueOf(paddingString);
        int clientsAmount = Integer.valueOf(clientsAmountString);

        String networkMask = calculateMinimumMask(clientsAmount + (int) Math.ceil(clientsAmount * padding / 100.0));

        return createNetworks(convertToNetworkCidr(networkAddress, networkMask), Integer.parseInt(subnetAmount));
    }

    private String calculateMinimumMask(int clientsAmount) {
        int hostBits = (int) (32.0 - Math.ceil(Math.log(clientsAmount + 2) / Math.log(2)));
        return String.valueOf(hostBits);
    }

    private NetworkWorkflowResult createNetworks(String networkCidr, int subnetAmount) {
        NetworkWorkflowResult result = new NetworkWorkflowResult();
        String nonCollidingCidr = getNonCollidingCidr(networkCidr, result);

        if (nonCollidingCidr != null) {
            List<String> subnetList = IpUtils.divideNetworkIntoSubnets(nonCollidingCidr, subnetAmount);
            List<Network> networkList = new ArrayList<>();

            for (String subnet : subnetList) {
                SubnetInfo networkInfo = new SubnetUtils(subnet).getInfo();
                networkList.add(new NetworkBuilder()
                        .setNetworkAddress(networkInfo.getNetworkAddress())
                        .setBroadcastIp(networkInfo.getBroadcastAddress())
                        .setIpRange(networkInfo.getLowAddress(), networkInfo.getHighAddress())
                        .setAddressesAmount(networkInfo.getAddressCountLong())
                        .setSubnetMask(networkInfo.getNetmask())
                        .build());
            }

            result.setNetworkList(networkList);
            result.cacheNetworkList();
        }

        return result;
    }

    private String convertToNetworkCidr(String address, String mask) {
        String networkAddress = new SubnetUtils(address + "/" + mask).getInfo().getNetworkAddress();
        return networkAddress + "/" + mask;
    }

    private String getNonCollidingCidr(String networkCandidateCidr, NetworkWorkflowResult result) {
        String nonCollidingNetworkCidr = null;
        try {
            nonCollidingNetworkCidr = IpUtils.findFirstPossibleNonCollidingNetwork(networkCandidateCidr, getAllObjects("Network"));

            if (!networkCandidateCidr.equals(nonCollidingNetworkCidr)) {
                result.appendLog(String.format("Network %s is overlapping a different network.\nUsing closest next possible network - %s - instead.", networkCandidateCidr, nonCollidingNetworkCidr));
            }
        } catch (IllegalStateException ex) {
            result.appendLog(String.format("No network can be created to avoid overlapping for %s!", networkCandidateCidr));
        }

        return nonCollidingNetworkCidr;
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
