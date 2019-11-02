package de.comroid.crystalshard.impl.event.message;

import java.util.List;
import java.util.Optional;

import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.entity.channel.TextChannel;
import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.guild.Role;
import de.comroid.crystalshard.api.entity.guild.webhook.Webhook;
import de.comroid.crystalshard.api.entity.message.Message;
import de.comroid.crystalshard.api.entity.user.GuildMember;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.api.event.message.MessageSentEvent;
import de.comroid.crystalshard.api.listener.model.ListenerAttachable;
import org.jetbrains.annotations.NotNull;

public class MessageSentEventImpl implements MessageSentEvent {
    private final ListenerAttachable[] affected;
    private final Message message;

    public MessageSentEventImpl(ListenerAttachable[] affected, Message message) {
        this.affected = affected;
        this.message = message;
    }

    @Override
    public @NotNull Discord getDiscord() {
        return message.getAPI();
    }

    @Override
    public @NotNull TextChannel getTriggeringChannel() {
        return message.getChannel();
    }

    @Override
    public Optional<Guild> wrapTriggeringGuild() {
        return message.getGuild();
    }

    @Override
    public @NotNull Message getTriggeringMessage() {
        return message;
    }

    @Override
    public Optional<Role> wrapTriggeringRole() {
        return wrapTriggeringGuild()
                .flatMap(getTriggeringUser()::asGuildMember)
                .map(GuildMember::getRoles)
                .map(roles -> ((List) roles).get(0))
                .map(Role.class::cast)
                .or(() -> wrapTriggeringGuild().map(Guild::getEveryoneRole));
    }

    @Override
    public ListenerAttachable[] getAffected() {
        return affected;
    }

    @Override 
    public Optional<Webhook> wrapTriggeringWebhook() {
        return message.getAuthor().castAuthorToWebhook();
    }

    @Override
    public Optional<User> wrapTriggeringUser() {
        return message.getAuthor().castAuthorToUser();
    }
}
