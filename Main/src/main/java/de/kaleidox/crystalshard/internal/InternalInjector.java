package de.kaleidox.crystalshard.internal;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.InjectorBase;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

public abstract class InternalInjector extends InjectorBase {
    public final static InternalInjector injector;
    private final static Set<Class> mustOverride;

    static {
        InternalInjector using;
        ServiceLoader<InternalInjector> load = ServiceLoader.load(InternalInjector.class);
        Iterator<InternalInjector> iterator = load.iterator();
        if (iterator.hasNext()) using = iterator.next();
        else throw new IllegalStateException("No implementation for " + InternalInjector.class.getName() + " found!");
        if (iterator.hasNext()) {
            List<InternalInjector> allImplementations = new ArrayList<>();
            allImplementations.add(using);
            iterator.forEachRemaining(allImplementations::add);
            allImplementations.sort(Comparator.comparingInt(injector -> injector.getJdkVersion() * -1));
            using = allImplementations.get(0);
            logger.warn("More than one implementation for " + InternalInjector.class.getSimpleName() +
                    " found! Using " + using.getClass().getName());
        }
        injector = using;
        mustOverride = new HashSet<>();
        mustOverride.addAll(Arrays.asList(ServerTextChannel.class,
                PrivateTextChannel.class,
                ServerVoiceChannel.class,
                GroupChannel.class,
                ChannelCategory.class,
                SentEmbed.class,
                Invite.class,
                UnicodeEmoji.class,
                PermissionList.class,
                Message.BulkDelete.class,
                CustomEmoji.class,
                Message.class,
                Role.class,
                Server.class,
                User.class,
                Embed.Builder.class,
                Discord.class,
                ChannelCategory.Builder.class,
                ServerTextChannel.Builder.class,
                ServerVoiceChannel.Builder.class,
                EmbedDraft.Footer.class,
                EmbedDraft.Image.class,
                EmbedDraft.Author.class,
                EmbedDraft.Thumbnail.class,
                EmbedDraft.Field.class,
                EmbedDraft.EditableField.class,
                Message.Builder.class
        ));
    }

    public InternalInjector(Hashtable<Class, Class> implementations) {
        super(implementations, mustOverride);
    }

    public static <T> T newInstance(Class<T> tClass, Object... args) {
        return injector.makeInstance(tClass, args);
    }

    public static void tryHandle(Discord discord, JsonNode data) {
        injector.tryHandleinjector(discord, data);
    }

    protected abstract void tryHandleinjector(Discord discord, JsonNode data);
}
