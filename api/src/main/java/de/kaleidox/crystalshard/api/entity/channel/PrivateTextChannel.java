package de.kaleidox.crystalshard.api.entity.channel;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

import de.kaleidox.crystalshard.api.entity.EntityType;
import de.kaleidox.crystalshard.api.entity.Snowflake;
import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.model.channel.ChannelType;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Contract;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;

public interface PrivateTextChannel extends PrivateChannel, TextChannel {
    @Override
    default ChannelType getChannelType() {
        return ChannelType.DM;
    }

    @Override
    default EntityType getEntityType() {
        return EntityType.PRIVATE_TEXT_CHANNEL;
    }
    
    interface Trait extends PrivateChannel.Trait, TextChannel.Trait {
    }

    interface Builder extends
            PrivateChannel.Builder<PrivateTextChannel, PrivateTextChannel.Builder>,
            TextChannel.Builder<PrivateTextChannel, PrivateTextChannel.Builder> {
        @Override
        @Contract("-> fail")
        default Collection<User> getRecipients() {
            throw new IllegalArgumentException("Cannot modify recipients of a PrivateTextChannel!");
        }

        @Override
        @Contract("_ -> fail")
        default Builder addRecipient(User user) {
            throw new IllegalArgumentException("Cannot modify recipients of a PrivateTextChannel!");
        }

        @Override
        @Contract("_ -> fail")
        default Builder removeRecipientIf(Predicate<User> tester) {
            throw new IllegalArgumentException("Cannot modify recipients of a PrivateTextChannel!");
        }
    }

    interface Updater extends
            PrivateChannel.Updater<PrivateTextChannel, PrivateTextChannel.Updater>,
            TextChannel.Updater<PrivateTextChannel, PrivateTextChannel.Updater> {
        @Override
        @Contract("-> fail")
        default Collection<User> getRecipients() {
            throw new IllegalArgumentException("Cannot modify recipients of a PrivateTextChannel!");
        }

        @Override
        @Contract("_ -> fail")
        default Updater addRecipient(User user) {
            throw new IllegalArgumentException("Cannot modify recipients of a PrivateTextChannel!");
        }

        @Override
        @Contract("_ -> fail")
        default Updater removeRecipientIf(Predicate<User> tester) {
            throw new IllegalArgumentException("Cannot modify recipients of a PrivateTextChannel!");
        }
    }
}
