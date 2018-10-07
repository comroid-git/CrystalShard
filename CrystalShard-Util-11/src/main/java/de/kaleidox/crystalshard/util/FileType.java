package de.kaleidox.crystalshard.util;

import java.io.File;

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class FileType {
    private final String vanityName;
    private final String regexPiece;
    
    FileType(String vanityName, String regexPiece) {
        this.vanityName = vanityName;
        this.regexPiece = regexPiece;
    }
    
    public boolean isType(String test) {
        return test.matches(".+" + regexPiece);
    }
    
    public boolean isType(File file) {
        return isType(file.getName());
    }
    
    public String vanityName() {
        return vanityName;
    }
    
    @Override
    public String toString() {
        return regexPiece;
    }
    
    public static class IMAGE extends FileType {
        // Static Fields
        public final static IMAGE   PNG    = new IMAGE("png", ".png");
        public final static IMAGE   JPEG   = new IMAGE("jpeg", ".jpe?g");
        public final static IMAGE   WebP   = new IMAGE("webp", ".webp");
        public final static IMAGE   GIF    = new IMAGE("gif", ".gifv?");
        public final static IMAGE[] ALL    = new IMAGE[]{PNG, JPEG, WebP, GIF};
        public final static IMAGE[] NO_GIF = new IMAGE[]{PNG, JPEG, WebP};
        
        private IMAGE(String vanityName, String regexPiece) {
            super(vanityName, regexPiece);
        }
    }
    
    public static class MUSIC extends FileType {
        // Static Fields
        public final static MUSIC   MP3 = new MUSIC("mp3", ".mp3");
        public final static MUSIC[] ALL = new MUSIC[]{MP3};
        
        private MUSIC(String vanityName, String regexPiece) {
            super(vanityName, regexPiece);
        }
    }
    
    public static class VIDEO extends FileType {
        // Static Fields
        public final static VIDEO   MP4 = new VIDEO("mp4", ".mp4");
        public final static VIDEO[] ALL = new VIDEO[]{MP4};
        
        private VIDEO(String vanityName, String regexPiece) {
            super(vanityName, regexPiece);
        }
    }
}
