package com.ipCalculator.utility;

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
}
