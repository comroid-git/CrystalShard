package de.kaleidox.crystalshard.internal.util;

import de.kaleidox.util.FileType;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.imageio.ImageIO;

public class Container {
    public Container(File file) {
    }
    
    public String getFullName() {
        return null;
    }
    
    public interface Interface {
        Container getContainer();
    }
    
    public static String encodeBase64(File image, FileType type, Dimension res) {
        if (!type.isType(image)) throw new IllegalArgumentException("Only " + type + " type files accepted!");
        try {
            BufferedImage read = ImageIO.read(image);
            if (read.getHeight() != res.height || read.getWidth() != res.width) throw new IllegalArgumentException("Image must fit " + res);
            return encodeFileToBase64Binary(image);
        } catch (IOException e) {
            throw new NullPointerException("Image could not be read: " + e.getMessage());
        }
    }
    
    private static String encodeFileToBase64Binary(File file) {
        String encodedfile;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            //noinspection ResultOfMethodCallIgnored
            fileInputStreamReader.read(bytes);
            encodedfile = new String(Base64.getEncoder()
                                             .encode(bytes), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new NullPointerException("Error encoding file; " + e.getMessage());
        }
        return encodedfile;
    }
}
