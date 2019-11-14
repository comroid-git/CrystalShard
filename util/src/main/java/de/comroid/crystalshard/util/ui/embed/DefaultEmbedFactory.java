package de.comroid.crystalshard.util.ui.embed;

import java.awt.Color;
import java.util.function.Supplier;

import de.comroid.crystalshard.api.entity.guild.Guild;
import de.comroid.crystalshard.api.entity.user.User;
import de.comroid.crystalshard.api.model.message.embed.Embed;

public enum DefaultEmbedFactory implements Supplier<Embed> {
    INSTANCE;

    private Supplier<Embed.Composer> predefinition;

    DefaultEmbedFactory() {
        this.predefinition = Embed::composer;
    }

    @Override
    public Embed get() {
        return predefinition.get()
                .removeAllFields()
                .compose();
    }

    public Embed get(Guild guild) {
        return predefinition.get()
                .setColor(guild.getRoleColor(guild.getAPI().getYourself())
                        .orElse(new Color(0x7289da))) // discord's blurple is default color
                .compose();
    }

    public Embed get(User user) {
        return predefinition.get()
                .setAuthor(user)
                .compose();
    }

    public Embed get(Guild guild, User user) {
        return predefinition.get()
                .setColor(guild.getRoleColor(guild.getAPI().getYourself())
                        .orElse(new Color(0x7289da))) // discord's blurple is default color
                .setAuthor(user)
                .compose();
    }

    public static void setEmbedSupplier(Supplier<Embed.Composer> composerSupplier) {
        INSTANCE.predefinition = composerSupplier;
    }

    public static Embed create() {
        return INSTANCE.get();
    }

    public static Embed create(Guild guild) {
        return INSTANCE.get(guild);
    }

    public static Embed create(User user) {
        return INSTANCE.get(user);
    }

    public static Embed create(Guild guild, User user) {
        return INSTANCE.get(guild, user);
    }
}
