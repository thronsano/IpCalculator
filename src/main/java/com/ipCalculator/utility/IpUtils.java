package com.ipCalculator.utility;

import com.google.common.collect.ImmutableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class IpUtils {
    public static String incrementByOne(String address) throws IllegalStateException {
        List<AtomicInteger> segments = Arrays.stream(address.split("\\."))
                .map(Integer::parseInt)
                .map(AtomicInteger::new)
                .collect(Collectors.toList());
        if (tryAdding(segments, 3)
                || tryAdding(segments, 2)
                || tryAdding(segments, 1)
                || tryAdding(segments, 0)) {
            return segments.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining("."));
        }

        throw new IllegalStateException(String.format("Failed to increment address %s", address));
    }

    private static boolean tryAdding(List<AtomicInteger> segments, int i) {
        if (segments.get(i).get() + 1 <= 255) {
            segments.get(i).getAndAdd(1);
            return true;
        }
        segments.get(i).set(0);
        return false;
    }

    public static boolean networksOverlap(String firstNetworkCidr, String secondNetworkCidr) {
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

    public static List<String> divideNetworkToSubnets(String networkCidr, int targetAmount) {
        if (targetAmount == 1) {
            return ImmutableList.of(networkCidr);
        }
        try {
            int divider = getDivider(targetAmount);
            String parameters = String.format("%s %d", networkCidr, divider);
            try (BufferedReader reader = getCommandExecutionResultsReader("subnet.py", parameters)) {
                return reader.lines().collect(Collectors.toList());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private static int getDivider(int targetAmount) {
        for (int i = 0; i < 10; i++) {
            if (targetAmount <= Math.pow(2, i)) {
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

}
