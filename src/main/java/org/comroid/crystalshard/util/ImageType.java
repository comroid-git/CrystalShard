package org.comroid.crystalshard.util;

import org.comroid.api.WrappedFormattable;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ImageType implements WrappedFormattable {
    PNG,
    GIF,
    JPEG,
    WebP;

    @Override
    public String getDefaultFormattedName() {
        return toString();
    }

    @Override
    public String getAlternateFormattedName() {
        return name();
    }

    public static String combineRegEx(ImageType... types) {
        return Stream.of(types)
                .map(it -> String.format("(%s)", it))
                .collect(Collectors.joining("(", "|", ")"));
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
