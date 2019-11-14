package de.comroid.crystalshard.api.event.message.reaction;

import de.comroid.crystalshard.api.entity.channel.TextChannel;
import de.comroid.crystalshard.api.entity.emoji.Emoji;
import de.comroid.crystalshard.api.event.DiscordEvent;
import de.comroid.crystalshard.api.event.channel.ChannelEvent;
import de.comroid.crystalshard.api.event.guild.WrappedGuildEvent;
import de.comroid.crystalshard.api.event.message.MessageEvent;
import de.comroid.crystalshard.api.event.role.WrappedRoleEvent;
import de.comroid.crystalshard.api.event.user.UserEvent;
import de.comroid.crystalshard.api.model.message.reaction.Reaction;

public interface ReactionEvent extends
        DiscordEvent,
        WrappedGuildEvent,
        ChannelEvent<TextChannel>,
        WrappedRoleEvent,
        UserEvent,
        MessageEvent {
    Reaction getReaction();
    
    Emoji getEmoji();
}
