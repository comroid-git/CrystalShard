package de.kaleidox.crystalshard.main.items.message;

import de.kaleidox.crystalshard.main.items.DiscordItem;
import de.kaleidox.crystalshard.main.items.channel.Channel;
import de.kaleidox.crystalshard.main.items.channel.TextChannel;
import de.kaleidox.crystalshard.main.items.server.emoji.CustomEmoji;
import de.kaleidox.crystalshard.main.items.server.emoji.Emoji;
import de.kaleidox.crystalshard.main.items.server.emoji.UnicodeEmoji;
import de.kaleidox.crystalshard.main.items.message.embed.SentEmbed;
import de.kaleidox.crystalshard.main.items.message.reaction.Reaction;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.user.Author;
import de.kaleidox.crystalshard.main.items.user.AuthorUser;
import de.kaleidox.crystalshard.main.items.user.AuthorWebhook;
import de.kaleidox.crystalshard.main.items.user.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface Message extends DiscordItem {
    TextChannel getChannel();

    Author getAuthor();

    Optional<AuthorUser> getAuthorAsUser();

    Optional<AuthorWebhook> getAuthorAsWebhook();

    String getContent();

    String getReadableContent();

    String getTextContent();

    Instant getTimestamp();

    Optional<Instant> getEditedTimestamp();

    boolean isTTS();

    boolean mentionsEveryone();

    boolean isPinned();

    boolean isPrivate();

    MessageType getType();

    MessageActivity getActivity();

    MessageApplication getApplication();

    List<User> getUserMentions();

    List<Role> getRoleMentions();

    // TODO Discord may only send mentioned ServerTextChannels
    List<Channel> getMentionedChannels();

    List<Attachment> getAttachments();

    List<SentEmbed> getEmbeds();

    List<Reaction> getReactions();

    List<Emoji> getEmojis();

    List<CustomEmoji> getCustomEmojis();

    List<UnicodeEmoji> getUnicodeEmojis();
}
