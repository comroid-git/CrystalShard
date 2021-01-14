package org.comroid.crystalshard.cdn;

import org.comroid.api.Named;
import org.intellij.lang.annotations.Language;

import java.util.stream.Collectors;
import java.util.stream.Stream;

// todo: make endpoint structure to accept one parameter
public enum ImageType implements Named {
    PNG("png"),
    JPEG("jpe?g"),
    WebP("webp"),
    GIF("gif");

    private final @Language("RegExp") String regex;

    @Override
    public String getName() {
        return name().toLowerCase();
    }

    @Language("RegExp")
    public String getRegex() {
        return regex;
    }

    ImageType(@Language("RegExp") String regex) {
        this.regex = regex;
    }

    @Language("RegExp")
    public static String combine(ImageType... types) {
        return Stream.of(types)
                .map(ImageType::getRegex)
                .map(str -> String.format("(%s)", str))
                .collect(Collectors.joining("|","\\.(",")"));
    }
}
