package de.kaleidox.crystalshard.internal.util;

import de.kaleidox.crystalshard.main.util.FileContainer;
import de.kaleidox.crystalshard.util.FileType;

import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Container implements FileContainer {
    public Container(File file) {
    }
    
    public String getFullName() {
        return null;
    }
    
    public static String encodeBase64(File image, FileType type, Dimension res) {
        if (!type.isType(image)) throw new IllegalArgumentException("Only " + type + " type files accepted!");
        try {
            BufferedImage read = ImageIO.read(image);
            if (read.getHeight() == res.height && read.getWidth() == res.width) return encodeFileToBase64Binary(image);
            else throw new IllegalArgumentException("Resolution of image is invalid; required " + res);
        } catch (IOException e) {
            throw new NullPointerException("Image could not be read: " + e.getMessage());
        }
    }
    
    private static String encodeFileToBase64Binary(File file) {
        String encodedFile;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            //noinspection ResultOfMethodCallIgnored
            int read = fileInputStreamReader.read(bytes);
            if (read > 7500000) // keep 500 kb free
                throw new IllegalArgumentException("File is too big!");
            encodedFile = new String(Base64.getEncoder().encode(bytes), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new NullPointerException("Error encoding file; " + e.getMessage());
        }
        return encodedFile;
    }
}
