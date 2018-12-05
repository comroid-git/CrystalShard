package de.kaleidox.crystalshard.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.vdurmont.emoji.EmojiParser;

import de.kaleidox.crystalshard.internal.handling.handlers.HandlerBase;
import de.kaleidox.crystalshard.internal.items.channel.ChannelBuilderInternal;
import de.kaleidox.crystalshard.internal.items.channel.ChannelCategoryInternal;
import de.kaleidox.crystalshard.internal.items.channel.GroupChannelInternal;
import de.kaleidox.crystalshard.internal.items.channel.PrivateTextChannelInternal;
import de.kaleidox.crystalshard.internal.items.channel.ServerTextChannelInternal;
import de.kaleidox.crystalshard.internal.items.channel.ServerVoiceChannelInternal;
import de.kaleidox.crystalshard.internal.items.message.MessageInternal;
import de.kaleidox.crystalshard.internal.items.message.MessageModifier;
import de.kaleidox.crystalshard.internal.items.message.embed.EmbedBuilderInternal;
import de.kaleidox.crystalshard.internal.items.message.embed.EmbedDraftInternal;
import de.kaleidox.crystalshard.internal.items.message.embed.SentEmbedInternal;
import de.kaleidox.crystalshard.internal.items.permission.PermissionListInternal;
import de.kaleidox.crystalshard.internal.items.role.RoleInternal;
import de.kaleidox.crystalshard.internal.items.server.ServerInternal;
import de.kaleidox.crystalshard.internal.items.server.emoji.CustomEmojiInternal;
import de.kaleidox.crystalshard.internal.items.server.emoji.UnicodeEmojiInternal;
import de.kaleidox.crystalshard.internal.items.server.interactive.InviteInternal;
import de.kaleidox.crystalshard.internal.items.user.UserInternal;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.main.items.channel.ChannelCategory;
import de.kaleidox.crystalshard.main.items.channel.GroupChannel;
import de.kaleidox.crystalshard.main.items.channel.PrivateTextChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerTextChannel;
import de.kaleidox.crystalshard.main.items.channel.ServerVoiceChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.main.items.message.embed.SentEmbed;
import de.kaleidox.crystalshard.main.items.permission.PermissionList;
import de.kaleidox.crystalshard.main.items.role.Role;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.server.emoji.CustomEmoji;
import de.kaleidox.crystalshard.main.items.server.emoji.UnicodeEmoji;
import de.kaleidox.crystalshard.main.items.server.interactive.Invite;
import de.kaleidox.crystalshard.main.items.user.User;

import java.util.Hashtable;

public class InternalInjectorJRE8Impl extends InternalInjector {
    private final static Hashtable<Class, Class> implementations;

    static {
        implementations = new Hashtable<>();
        implementations.put(ServerTextChannel.class, ServerTextChannelInternal.class);
        implementations.put(PrivateTextChannel.class, PrivateTextChannelInternal.class);
        implementations.put(ServerVoiceChannel.class, ServerVoiceChannelInternal.class);
        implementations.put(GroupChannel.class, GroupChannelInternal.class);
        implementations.put(ChannelCategory.class, ChannelCategoryInternal.class);
        implementations.put(CustomEmoji.class, CustomEmojiInternal.class);
        implementations.put(Message.class, MessageInternal.class);
        implementations.put(Role.class, RoleInternal.class);
        implementations.put(Server.class, ServerInternal.class);
        implementations.put(User.class, UserInternal.class);
        implementations.put(Embed.Builder.class, EmbedBuilderInternal.class);
        implementations.put(Discord.class, DiscordInternal.class);
        implementations.put(ChannelCategory.Builder.class, ChannelBuilderInternal.ServerCategoryBuilder.class);
        implementations.put(ServerTextChannel.Builder.class, ChannelBuilderInternal.ServerTextChannelBuilder.class);
        implementations.put(ServerVoiceChannel.Builder.class, ChannelBuilderInternal.ServerVoiceChannelBuilder.class);
        implementations.put(EmbedDraft.Footer.class, EmbedDraftInternal.Footer.class);
        implementations.put(EmbedDraft.Image.class, EmbedDraftInternal.Image.class);
        implementations.put(EmbedDraft.Author.class, EmbedDraftInternal.Author.class);
        implementations.put(EmbedDraft.Thumbnail.class, EmbedDraftInternal.Thumbnail.class);
        implementations.put(EmbedDraft.Field.class, EmbedDraftInternal.Field.class);
        implementations.put(EmbedDraft.EditableField.class, EmbedDraftInternal.EditableField.class);
        implementations.put(Message.BulkDelete.class, MessageInternal.BulkDeleteInternal.class);
        implementations.put(PermissionList.class, PermissionListInternal.class);
        implementations.put(UnicodeEmoji.class, UnicodeEmojiInternal.class);
        implementations.put(Invite.class, InviteInternal.class);
        implementations.put(SentEmbed.class, SentEmbedInternal.class);
        implementations.put(Message.Builder.class, MessageModifier.Builder.class);
    }

    public InternalInjectorJRE8Impl() {
        super(implementations);
    }

    @Override
    protected void tryHandleinjector(Discord discord, JsonNode data) {
        HandlerBase.tryHandle((DiscordInternal) discord, data);
    }

    @Override
    public int getJdkVersion() {
        return 8;
    }

    @Override
    public String parseToAliasesDel(String emojiExact) {
        return EmojiParser.parseToAliases(emojiExact);
    }

    @Override
    public String parseToUnicodeDel(String emojiExact) {
        return EmojiParser.parseToUnicode(emojiExact);
    }
}
