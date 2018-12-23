package de.kaleidox.crystalshard.api.entity.user.presence;

import org.intellij.lang.annotations.MagicConstant;

import de.kaleidox.crystalshard.api.entity.Nameable;
import de.kaleidox.util.markers.BiTimestamp;

import java.net.URL;
import java.util.Optional;
import java.util.OptionalInt;
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

    @MagicConstant(valuesFromClass = Flag.class)
    OptionalInt getFlag();

    enum Type {
        UNKNOWN(-1, "Unknown Activity"),
        PLAYING(0, "Playing %s"),
        STREAMING(1, "Streaming %s"),
        LISTENING(2, "Listening to %s");
        private final int id;
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

        public static Type getFromId(int id) {
            return Stream.of(Type.values())
                    .filter(type -> type.id == id)
                    .findAny()
                    .orElse(UNKNOWN);
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

    class Flag {
        public final static int INSTANCE = 0;
        public final static int JOIN = 1;
        public final static int SPECTATE = 2;
        public final static int JOIN_REQUEST = 3;
        public final static int SYNC = 4;
        public final static int PLAY = 5;
    }
}
