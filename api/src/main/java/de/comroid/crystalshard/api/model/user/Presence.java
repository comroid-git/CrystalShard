package de.comroid.crystalshard.api.model.user;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.OptionalInt;

import de.comroid.crystalshard.api.entity.Snowflake;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;

import org.intellij.lang.annotations.MagicConstant;

public interface Presence extends JsonDeserializable { // todo serialize
    Guild getGuild();

    Activity getActivity();

    Status getStatus();

    Collection<Activity> getActivities();

    ClientStatus getClientStatus();

    interface ClientStatus {
        Status getDesktopStatus();

        Status getMobileStatus();

        Status getWebStatus();
    }

    interface Activity {
        String getName();

        Type getType();

        Optional<URL> getStreamURL();

        Timestamps getTimestamps();

        Snowflake getApplicationID();

        Optional<String> getDetails();

        Optional<Party> getParty();

        Optional<Assets> getAssets();

        Optional<Secrets> getSecrets();

        boolean isInstanced();

        @MagicConstant(flagsFromClass = Flags.class)
        int getFlags();

        interface Timestamps {
            Optional<Instant> getStartTimestamp();

            Optional<Instant> getEndTimestamp();

            default Optional<Duration> getDuration() {
                return getEndTimestamp().flatMap(end ->
                        getStartTimestamp().map(start ->
                                Duration.between(start, end)));
            }
        }

        interface Party {
            Optional<String> getID();

            OptionalInt getCurrentSize();

            OptionalInt getMaximumSize();
        }

        interface Assets {
            Optional<URL> getLargeImageURL();

            Optional<String> getLargeImageHoverText();

            Optional<URL> getSmallImageURL();

            Optional<String> getSmallImageHoverText();
        }

        interface Secrets {
            Optional<String> getJoinSecret();

            Optional<String> getSpectateSecret();

            Optional<String> getMatchSecret();
        }

        @SuppressWarnings("PointlessBitwiseExpression") final class Flags {
            public static final int INSTANCE = 1 << 0;

            public static final int JOIN = 1 << 1;

            public static final int SPECTATE = 1 << 2;

            public static final int JOIN_REQUEST = 1 << 3;

            public static final int SYNC = 1 << 4;

            public static final int PLAY = 1 << 5;
        }

        enum Type {
            GAME(0, "Playing %s"),

            STREAMING(1, "Streaming %s"),

            LISTENING(2, "Listening to %s");

            public final int value;
            public final String format;

            Type(int value, String format) {
                this.value = value;
                this.format = format;
            }
        }
    }

    enum Status {
        ONLINE("online"),

        IDLE("idle"),

        DO_NOT_DISTURB("dnd"),

        OFFLINE("offline");

        public final String value;

        Status(String value) {
            this.value = value;
        }
    }
}
