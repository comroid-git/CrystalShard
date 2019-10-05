package de.kaleidox.crystalshard.api.entity.channel;

import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import de.kaleidox.crystalshard.api.entity.EntityType;
import de.kaleidox.crystalshard.api.model.ImageHelper;
import de.kaleidox.crystalshard.api.model.channel.ChannelType;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;
import de.kaleidox.crystalshard.util.model.serialization.JsonTrait;

import com.fasterxml.jackson.databind.JsonNode;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.API;
import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;
import static de.kaleidox.crystalshard.util.model.serialization.JsonTrait.simple;

public interface  GroupTextChannel extends PrivateChannel, TextChannel {
    @Override
    default ChannelType getChannelType() {
        return ChannelType.GROUP_DM;
    }

    @Override
    default EntityType getEntityType() {
        return EntityType.GROUP_TEXT_CHANNEL;
    }
    
    @IntroducedBy(GETTER)
    default Optional<URL> getIconUrl() {
        return wrapTraitValue(Trait.ICON);
    }
    
    interface Trait extends PrivateChannel.Trait, TextChannel.Trait {
        JsonTrait<String, URL> ICON = simple(JsonNode::asText, "icon", ImageHelper.CHANNEL_ICON::urlFromHash);
    }

    interface Builder extends
            PrivateChannel.Builder<GroupTextChannel, GroupTextChannel.Builder>,
            TextChannel.Builder<GroupTextChannel, GroupTextChannel.Builder> {
        @Override
        @IntroducedBy(value = API, docs = "https://discordapp.com/developers/docs/resources/user#create-group-dm")
        CompletableFuture<GroupTextChannel> build();
    }

    interface Updater extends
            PrivateChannel.Updater<GroupTextChannel, GroupTextChannel.Updater>,
            TextChannel.Updater<GroupTextChannel, GroupTextChannel.Updater> {
    }
}
