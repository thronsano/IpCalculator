package com.ipCalculator.utility;

import com.google.common.collect.ImmutableList;
import com.ipCalculator.entity.db.Network;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

enum Comparison {
    SMALLER, EQUAL, BIGGER
}

public class IpUtils {
    public static String incrementByOne(String address) throws IllegalStateException {
        List<AtomicInteger> segments = IpParsers.extractSegments(address);
        if (tryIncrementing(segments, 3)
                || tryIncrementing(segments, 2)
                || tryIncrementing(segments, 1)
                || tryIncrementing(segments, 0)) {
            return segments.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining("."));
        }

        throw new IllegalStateException(String.format("Failed to increment address %s", address));
    }

    private static boolean tryIncrementing(List<AtomicInteger> allSegments, int segmentIndex) {
        if (allSegments.get(segmentIndex).get() + 1 <= 255) {
            allSegments.get(segmentIndex).getAndAdd(1);
            return true;
        }
        allSegments.get(segmentIndex).set(0);
        return false;
    }

    private static boolean networksOverlap(String firstNetworkCidr, String secondNetworkCidr) {
        try {
            String parameters = String.format("%s %s", firstNetworkCidr, secondNetworkCidr);
            try (BufferedReader reader = getCommandExecutionResultsReader("collides.py", parameters)) {
                String line = reader.readLine();
                return "True".equals(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<String> divideNetworkIntoSubnets(String networkCidr, int targetAmount) {
        if (targetAmount == 1) {
            return ImmutableList.of(networkCidr);
        }
        try {
            int divider = getDivider(targetAmount);
            String parameters = String.format("%s %d", networkCidr, divider);
            try (BufferedReader reader = getCommandExecutionResultsReader("subnet.py", parameters)) {
                return reader.lines().limit(targetAmount).collect(Collectors.toList());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private static int getDivider(int targetSubnetsAmount) {
        for (int i = 0; i < 10; i++) {
            if (targetSubnetsAmount <= Math.pow(2, i)) {
                return i;
            }
        }
        throw new ArithmeticException("Cannot divide subnet more than 10 times!");
    }

    private static BufferedReader getCommandExecutionResultsReader(String filename, String parameters) throws IOException {
        File file = new File(String.format("src\\main\\resources\\%s", filename));
        String pathToScript = file.getAbsolutePath();
        Process p = Runtime.getRuntime().exec(String.format("python %s %s", pathToScript, parameters));
        return new BufferedReader(new InputStreamReader(p.getInputStream()));
    }

    public static String getHigherIp(String firstNetwork, String secondNetwork) {
        List<AtomicInteger> firstNetworkSegments = IpParsers.extractSegments(firstNetwork);
        List<AtomicInteger> secondNetworkSegments = IpParsers.extractSegments(secondNetwork);

        for (int i = 0; i < 4; i++) {
            Comparison segmentComparison = compareSegment(i, firstNetworkSegments, secondNetworkSegments);
            if (segmentComparison == Comparison.BIGGER)
                return firstNetwork;
            else if (segmentComparison == Comparison.SMALLER)
                return secondNetwork;
        }

        return firstNetwork;
    }

    private static Comparison compareSegment(int index, List<AtomicInteger> segmentsOne, List<AtomicInteger> segmentsTwo) {
        if (segmentsOne.get(index).get() < segmentsTwo.get(index).get())
            return Comparison.SMALLER;
        if (segmentsOne.get(index).get() > segmentsTwo.get(index).get())
            return Comparison.BIGGER;
        return Comparison.EQUAL;
    }

    public static String findFirstPossibleNonCollidingNetwork(String candidateCidr, List<Network> allNetworks) {
        Network collidingNetwork = allNetworks.stream()
                .filter(network -> networksOverlap(network.getCidrAddress(), candidateCidr))
                .findAny()
                .orElse(null);

        if (collidingNetwork == null) {
            return candidateCidr;
        }

        String highestIp = getHigherIp(IpParsers.getBroadcastAddress(candidateCidr), collidingNetwork.getBroadcastIp());
        String nextCandidate = incrementByOne(highestIp) + "/" + IpParsers.extractMask(candidateCidr);
        return findFirstPossibleNonCollidingNetwork(nextCandidate, allNetworks);
    }
}
