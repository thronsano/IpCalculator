package com.ipCalculator.utility;

import org.apache.commons.net.util.SubnetUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class IpParsers {
    public static String extractMask(String cidr) {
        return cidr.split("/")[1];
    }

    public static String getBroadcastAddress(String cidr) {
        return new SubnetUtils(cidr).getInfo().getBroadcastAddress();
    }

    static List<AtomicInteger> extractSegments(String address) {
        return Arrays.stream(address.split("\\."))
                .map(Integer::parseInt)
                .map(AtomicInteger::new)
                .collect(Collectors.toList());
    }
}
