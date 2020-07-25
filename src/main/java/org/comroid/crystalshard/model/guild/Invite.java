package org.comroid.crystalshard.model.guild;

import org.comroid.api.IntEnum;
import org.comroid.api.Polyfill;
import org.comroid.common.ref.Named;
import org.comroid.crystalshard.entity.channel.Channel;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.mutatio.ref.Reference;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;

public final class Invite {
    public Invite(
            Reference<@Nullable Guild> guild,
            Reference<Channel> channel,
            URL inviteURL,
            Instant creationTimestamp,
            Duration maximumAge,
            @Nullable User inviter,
            @Nullable User targetUser,
            @Nullable TargetUserType targetUserType,
            boolean temporary,
            int useCount
    ) {
    }

    public static URL createUrl(String inviteCode) {
        return Polyfill.url("https://discord.gg/%s" + inviteCode);
    }

    public enum TargetUserType implements IntEnum, Named {
        STREAM(1);

        private final int value;

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public String getName() {
            return name();
        }

        TargetUserType(int value) {
            this.value = value;
        }

        public static @Nullable TargetUserType valueOf(int value) {
            for (TargetUserType type : values()) {
                if (type.value == value)
                    return type;
            }

            return null;
        }
    }
}
