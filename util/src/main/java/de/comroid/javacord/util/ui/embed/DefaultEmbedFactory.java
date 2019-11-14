package de.comroid.javacord.util.ui.embed;

import java.awt.Color;
import java.util.function.Supplier;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

public enum DefaultEmbedFactory implements Supplier<EmbedBuilder> {
    INSTANCE;

    private Supplier<EmbedBuilder> embedSupplier;

    DefaultEmbedFactory() {
        this.embedSupplier = EmbedBuilder::new;
    }

    @Override
    public EmbedBuilder get() {
        return embedSupplier.get()
                .removeAllFields();
    }

    public EmbedBuilder get(Server server) {
        return get()
                .setColor(server.getRoleColor(server.getApi().getYourself())
                        .orElse(new Color(0x7289da))); // discord's blurple is default color
    }

    public EmbedBuilder get(User user) {
        return get()
                .setAuthor(user);
    }

    public EmbedBuilder get(Server server, User user) {
        return get()
                .setColor(server.getRoleColor(server.getApi().getYourself())
                        .orElse(new Color(0x7289da))) // discord's blurple is default color
                .setAuthor(user);
    }

    public static void setEmbedSupplier(Supplier<EmbedBuilder> embedSupplier) {
        INSTANCE.embedSupplier = embedSupplier;
    }

    public static EmbedBuilder create() {
        return INSTANCE.get();
    }

    public static EmbedBuilder create(Server server) {
        return INSTANCE.get(server);
    }

    public static EmbedBuilder create(User user) {
        return INSTANCE.get(user);
    }

    public static EmbedBuilder create(Server server, User user) {
        return INSTANCE.get(server, user);
    }
}
