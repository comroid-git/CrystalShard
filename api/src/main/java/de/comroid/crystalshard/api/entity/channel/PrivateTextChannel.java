package de.comroid.crystalshard.api.entity.channel;

import java.util.Collection;
import java.util.function.Predicate;

import de.comroid.crystalshard.api.entity.EntityType;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.api.model.channel.ChannelType;

import org.jetbrains.annotations.Contract;

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
