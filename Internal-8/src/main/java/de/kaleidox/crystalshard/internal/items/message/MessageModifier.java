package de.kaleidox.crystalshard.internal.items.message;

import de.kaleidox.crystalshard.core.CoreDelegate;
import de.kaleidox.crystalshard.core.net.request.HttpMethod;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.exception.DiscordPermissionException;
import de.kaleidox.crystalshard.main.items.Mentionable;
import de.kaleidox.crystalshard.main.items.channel.PrivateChannel;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.main.items.permission.Permission;
import de.kaleidox.crystalshard.main.items.server.emoji.Emoji;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.util.helpers.FutureHelper;
import java.util.concurrent.CompletableFuture;

import static de.kaleidox.util.helpers.JsonHelper.*;

public class MessageModifier {
    public static class Builder implements Message.Builder {
        private final StringBuilder sb;
        private EmbedDraft draft = null;

        public Builder() {
            this(null);
        }

        public Builder(Message fromMessage) {
            sb = new StringBuilder();

            if (fromMessage != null) {
                addText(fromMessage.getContent());
                fromMessage.getEmbed().map(Embed::toEmbedDraft).ifPresent(this::setEmbed);
            }
        }

        @Override
        public Message.Builder addText(String text) {
            sb.append(text);
            return this;
        }

        @Override
        public Message.Builder addMention(Mentionable mentionable) {
            sb.append(mentionable.getMentionTag());
            return this;
        }

        @Override
        public Message.Builder addEmoji(Emoji emoji) {
            sb.append(emoji.toDiscordPrintable());
            return this;
        }

        @Override
        public Message.Builder setEmbed(EmbedDraft embed) {
            draft = embed;
            return this;
        }

        @Override
        public CompletableFuture<Message> send(MessageReciever target) {
            final Discord discord = target.getDiscord();
            long useId = -1;
            if (target instanceof TextChannel) {
                final TextChannel tc = (TextChannel) target;
                if (!tc.isPrivate() && tc.hasPermission(discord.getSelf(), Permission.SEND_MESSAGES))
                    return FutureHelper.failedFuture(new DiscordPermissionException("Sending Message to Text Channel ["
                            + target.getId() + "])", Permission.SEND_MESSAGES));
                useId = tc.getId();
            }
            if (target instanceof User) {
                final User usr = (User) target;
                useId = usr.openPrivateChannel().thenApply(PrivateChannel::getId).join();
            }
            assert useId != -1 : "Unexpected type: " + target.getClass().getSimpleName();
            return CoreDelegate.webRequest(Message.class, discord)
                    .setMethod(HttpMethod.POST)
                    .setUri(DiscordEndpoint.MESSAGE.createUri(useId))
                    .setNode(objectNode("content", sb.toString(),
                            "embed", draft,
                            "file", "content")) // TODO: 09.11.2018 Add file support
                    .executeAs(node -> discord.getMessageCache().getOrCreate(discord, node))
                    .whenComplete(((message, throwable) -> discord.getTunnelFramework().accept(message, draft)));
        }
    }
}
