package com.ipCalculator.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
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
            File file = new File("src\\main\\resources\\collides.py");
            String pathToScript = file.getAbsolutePath();
            Process p = Runtime.getRuntime().exec(String.format("python %s %s %s", pathToScript, firstNetworkCidr, secondNetworkCidr));
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line = reader.readLine();
                return "True".equals(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
