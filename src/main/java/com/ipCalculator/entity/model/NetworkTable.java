package com.ipCalculator.entity.model;

import com.ipCalculator.entity.db.Network;

import java.util.Collections;
import java.util.List;

public class NetworkTable extends Table {
    private String networkCacheKey;

    public NetworkTable(String networkCacheKey, Network network) {
        this.networkCacheKey = networkCacheKey;
        this.table = buildTable(Collections.singletonList(network));
    }

    public NetworkTable(List<Network> networks) {
        this.table = buildTable(networks);
    }

    private String[][] buildTable(List<Network> networks) {
        boolean addNames = false;
        int offset = 0;

        if (networks.size() > 0 && networks.get(0).getName() != null) {
            addNames = true;
            offset = 1;
        }

        this.width = 7 + offset;
        this.height = networks.size() + 1;
        String[][] tempTable = new String[width][height];
        int rowNo = 1;

        tempTable[offset][0] = "Network IP";
        tempTable[1 + offset][0] = "Subnet mask";
        tempTable[2 + offset][0] = "Router IP";
        tempTable[3 + offset][0] = "Broadcast IP";
        tempTable[4 + offset][0] = "Available addresses";
        tempTable[5 + offset][0] = "Available addresses start";
        tempTable[6 + offset][0] = "Available addresses end";

        if (addNames) {
            tempTable[0][0] = "Network name";
        }

        for (Network network : networks) {
            tempTable[offset][rowNo] = network.getNetworkIp();
            tempTable[1 + offset][rowNo] = network.getSubnetMask();
            tempTable[2 + offset][rowNo] = network.getRouterIp();
            tempTable[3 + offset][rowNo] = network.getBroadcastIp();
            tempTable[4 + offset][rowNo] = network.getAddressesAmount();
            tempTable[5 + offset][rowNo] = network.getRouterIp();
            tempTable[6 + offset][rowNo] = network.getLastAvailableIp();

            if (addNames) {
                tempTable[0][rowNo] = network.getName();
            }

            rowNo++;
        }

        return tempTable;
    }

    public String getNetworkCacheKey() {
        return networkCacheKey;
    }
}
