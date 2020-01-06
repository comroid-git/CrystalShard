package org.comroid.crystalshard.util.model;

import java.io.File;
import java.util.Optional;

import org.intellij.lang.annotations.Language;

public enum FileType {
    JPEG("\\.jpe?g"),
    PNG("\\.png"),
    WebP("\\.webp"),
    GIF("\\.gif");

    private final @Language("RegExp") String pattern;

    FileType(@Language("RegExp") String patternAppendix) {
        this.pattern = ".*" + patternAppendix;
    }
    
    public static Optional<FileType> guessFromFilename(File file) {
        String name = file.getName();

        for (FileType type : values())
            if (name.matches(type.pattern))
                return Optional.of(type);
        
        return Optional.empty();
    }
}
