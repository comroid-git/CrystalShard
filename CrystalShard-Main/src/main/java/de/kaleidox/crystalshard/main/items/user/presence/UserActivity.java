package de.kaleidox.crystalshard.main.items.user.presence;

import de.kaleidox.crystalshard.main.items.Nameable;
import de.kaleidox.crystalshard.util.BiTimestamp;

import java.net.URL;
import java.util.Optional;
import java.util.stream.Stream;

public interface UserActivity extends Nameable {
    Type getType();
    
    Optional<URL> getStreamUrl();
    
    Optional<BiTimestamp> getBiTimestamp();
    
    Optional<Long> getApplicationId();
    
    Optional<String> getDetails();
    
    Optional<String> getPartyStatus();
    
    Optional<Party> getParty();
    
    Optional<Assets> getAssets();
    
    Optional<Secrets> getSecrets();
    
    boolean isInstanced();
    
    Optional<Flag> getFlag();
    
    enum Flag {
        UNKNOWN(-1),
        INSTANCE(0),
        JOIN(1),
        SPECTATE(2),
        JOIN_REQUEST(3),
        SYNC(4),
        PLAY(5);
        private final int value;
        
        Flag(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
        
// Static membe
        public static Flag getFromValue(int value) {
            return Stream.of(values()).filter(flag -> flag.value == value).findAny().orElse(UNKNOWN);
        }
    }
    
    enum Type {
        UNKNOWN(-1, "Unknown Activity"),
        PLAYING(0, "Playing %s"),
        STREAMING(1, "Streaming %s"),
        LISTENING(2, "Listening to %s");
        private final int    id;
        private final String pattern;
        
        Type(int id, String pattern) {
            this.id = id;
            this.pattern = pattern;
        }
        
        public int getId() {
            return id;
        }
        
        public String getPattern() {
            return pattern;
        }
        
        public String format(String... param) {
            return String.format(pattern, (Object[]) param);
        }
        
// Static membe
        public static Type getFromId(int id) {
            return Stream.of(Type.values()).filter(type -> type.id == id).findAny().orElse(UNKNOWN);
        }
    }
    
    interface Secrets {
        Optional<String> getJoinSecret();
        
        Optional<String> getSpectateSecret();
        
        Optional<String> getMatchSecret();
    }
    
    interface Party {
        Optional<Long> getId();
        
        Optional<Integer> getCurrentSize();
        
        Optional<Integer> getMaxSize();
    }
    
    interface Assets {
        Optional<String> getLargeImage();
        
        Optional<String> getLargeImageHoverText();
        
        Optional<String> getSmallImage();
        
        Optional<String> getSmallImageHoverText();
    }
}
