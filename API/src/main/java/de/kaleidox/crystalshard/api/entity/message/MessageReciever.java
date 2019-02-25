package de.kaleidox.crystalshard.api.entity.message;

import de.kaleidox.crystalshard.api.entity.DiscordItem;
import de.kaleidox.crystalshard.api.entity.channel.PrivateChannel;
import de.kaleidox.crystalshard.api.entity.channel.TextChannel;
import de.kaleidox.crystalshard.api.entity.permission.Permission;
import de.kaleidox.crystalshard.api.entity.user.Self;
import de.kaleidox.crystalshard.api.entity.user.User;
import de.kaleidox.crystalshard.api.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.api.exception.IllegalThreadException;
import de.kaleidox.crystalshard.core.CoreInjector;
import de.kaleidox.crystalshard.core.concurrent.ThreadPool;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.WebRequest;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.internal.InternalInjector;
import de.kaleidox.crystalshard.util.DefaultEmbed;
import de.kaleidox.util.annotations.Range;
import de.kaleidox.util.helpers.FutureHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static de.kaleidox.util.helpers.JsonHelper.objectNode;

/**
 * This interface extends another item that can recieve messages.
 * <b>EVERYTIME</b> one of those methods is invoked on a {@link Self}
 * object, an {@link AbstractMethodError} is thrown.
 *
 * @see User
 * @see TextChannel
 */
public interface MessageReciever extends DiscordItem {
    /**
     * Sends a plain text message to this object.
     * <p>
     * The returned future will complete with a {@link DiscordPermissionException}
     * if you do not have the permission to message this person or channel.
     *
     * @param content The content of the message.
     * @return A future to contain the sent message.
     * @throws AbstractMethodError If this object is an instance of {@link Self}.
     */
    default CompletableFuture<Message> sendMessage(String content) throws AbstractMethodError {
        if (this instanceof Self) throw new AbstractMethodError("You cannot message yourself!");
        return InternalInjector.newInstance(Message.Builder.class)
                .addText(content)
                .send(this);
    }

    /**
     * Sends an embed message to this object.
     * <p>
     * The returned future will complete with a {@link DiscordPermissionException}
     * if you do not have the permission to message this person or channel.
     *
     * @param embedBuilder An EmbedBuilder to build and send.
     * @return A future to contain the sent message.
     * @throws AbstractMethodError If this object is an instance of {@link Self}.
     */
    default CompletableFuture<Message> sendMessage(Embed.Builder embedBuilder) throws AbstractMethodError {
        if (this instanceof Self) throw new AbstractMethodError("You cannot message yourself!");
        return InternalInjector.newInstance(Message.Builder.class)
                .setEmbed(embedBuilder)
                .send(this);
    }

    /**
     * Sends an embed message to this object.
     * <p>
     * The returned future will complete with a {@link DiscordPermissionException}
     * if you do not have the permission to message this person or channel.
     *
     * @param embed An EmbedDraft to send.
     * @return A future to contain the sent message.
     * @throws AbstractMethodError If this object is an instance of {@link Self}.
     */
    default CompletableFuture<Message> sendMessage(Embed embed) throws AbstractMethodError {
        if (this instanceof Self) throw new AbstractMethodError("You cannot message yourself!");
        return InternalInjector.newInstance(Message.Builder.class)
                .setEmbed(embed)
                .send(this);
    }

    /**
     * Sends an embed message to this object.
     * The embed is defined by the {@link DefaultEmbed},
     * and will be modified using the provided modifier.
     * <p>
     * The returned future will complete with a {@link DiscordPermissionException}
     * if you do not have the permission to message this person or channel.
     *
     * @param defaultEmbedModifier The content of the message.
     * @return A future to contain the sent message.
     * @throws AbstractMethodError    If this object is an instance of {@link Self}.
     * @throws IllegalThreadException See {@link ThreadPool#getThreadDiscord()}.
     */
    default CompletableFuture<Message> sendMessage(Consumer<Embed.Builder> defaultEmbedModifier)
            throws IllegalThreadException, AbstractMethodError {
        if (this instanceof Self) throw new AbstractMethodError("You cannot message yourself!");
        return sendMessage(DefaultEmbed.getStatic(defaultEmbedModifier)); // TODO: 08.11.2018 Create interface
    }

    /**
     * Triggers the "typing..." indicator in this reciever.
     *
     * @return A future that completes with {@code null} when the typing indicator has been triggered.
     * @throws AbstractMethodError If this object is an instance of {@link Self}.
     */
    default CompletableFuture<Void> typing() throws AbstractMethodError {
        if (this instanceof Self) throw new AbstractMethodError("You cannot type to yourself!");
        return CoreInjector.webRequest(Void.class, getDiscord())
                .setMethod(HttpMethod.POST)
                .setUri(DiscordEndpoint.CHANNEL_TYPING.createUri(getId()))
                .executeAsVoid();
    }

    /**
     * Gets a collection of messages from this reciever.
     * <p>
     * The returned future will complete with a {@link DiscordPermissionException}
     * if you don't have the permission {@link Permission#READ_MESSAGES}.
     * The returned future's collection will be empty if you don't
     * have the permission {@link Permission#READ_MESSAGE_HISTORY}.
     *
     * @param limit How many messages to get. See {@link Range}.
     * @return A future that completes with a collection of messages
     * @throws AbstractMethodError      If this object is an instance of {@link Self}.
     * @throws IllegalArgumentException If the parameter {@code limit} is not within its annotated boundaries.
     */
    default CompletableFuture<Collection<Message>> getMessages(@Range(min = 1, max = 100) int limit)
            throws AbstractMethodError, IllegalArgumentException {
        if (this instanceof Self) throw new AbstractMethodError("You cannot message yourself!");
        if (this instanceof TextChannel) {
            final TextChannel target = (TextChannel) this;
            if (limit < 1 || limit > 100)
                throw new IllegalArgumentException("Parameter 'limit' is not within its bounds! [1, 100]");
            if (!target.hasPermission(getDiscord(), Permission.READ_MESSAGES))
                return FutureHelper.failedFuture(new DiscordPermissionException("Cannot read " + toString(),
                        Permission.READ_MESSAGES));
            if (!target.hasPermission(getDiscord(), Permission.READ_MESSAGE_HISTORY))
                return CompletableFuture.completedFuture(Collections.emptyList());
        }
        WebRequest<Collection<Message>> request = CoreInjector.webRequest(getDiscord());
        return request.setMethod(HttpMethod.GET)
                .setUri(DiscordEndpoint.MESSAGE.createUri(this instanceof User ?
                        ((User) this).openPrivateChannel().thenApply(PrivateChannel::getId).join() : getId()))
                .setNode(objectNode("limit", limit))
                .executeAs(data -> {
                    List<Message> list = new ArrayList<>();
                    data.forEach(msg -> list.add(getDiscord().getMessageCache()
                            .getOrCreate(getDiscord(), msg)));
                    return list;
                });
    }

    /**
     * Gets a collection of 50 messages from this reciever.
     * <p>
     * The returned future will complete with a {@link DiscordPermissionException}
     * if you don't have the permission {@link Permission#READ_MESSAGES}.
     * The returned future's collection will be empty if you don't
     * have the permission {@link Permission#READ_MESSAGE_HISTORY}.
     *
     * @return A future that completes with a collection of messages
     * @throws AbstractMethodError If this object is an instance of {@link Self}.
     * @see #getMessages(int)
     */
    default CompletableFuture<Collection<Message>> getMessages()
            throws AbstractMethodError {
        return getMessages(50);
    }
}
