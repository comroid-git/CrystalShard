package de.kaleidox.crystalshard.util.discord.input;

import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.main.items.channel.ServerChannel;
import de.kaleidox.crystalshard.main.items.message.Message;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.server.Server;
import de.kaleidox.crystalshard.main.items.user.ServerMember;
import de.kaleidox.crystalshard.main.items.user.User;
import de.kaleidox.crystalshard.util.objects.CompletableFutureExtended;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Base<T> {
    protected final static Logger logger = new Logger(Base.class);
    protected final boolean deleteWhenDone;
    protected final String name;
    protected final long time;
    protected final TimeUnit unit;
    protected final T defaultValue;
    protected final MessageReciever parent;
    protected final Predicate<User> participantTester;
    private final Consumer<Embed.Builder> embedModifier;
    private final List<Message> affiliates;
    protected Function<T, String> toStringFunction = Object::toString;
    private User constructedForSpecified = null;

    protected Base(MessageReciever parent, String name, T defaultValue) {
        this(parent, builder -> builder.addField(name.toUpperCase(), "Please enter a value for ```" + name + "```:"), name, defaultValue);
    }

    protected Base(MessageReciever parent, Consumer<Embed.Builder> embedModifier, String name, T defaultValue) {
        this(parent, embedModifier, name, 5, TimeUnit.MINUTES, defaultValue);
    }

    protected Base(MessageReciever parent, Consumer<Embed.Builder> embedModifier, String name, long time, TimeUnit unit, T defaultValue) {
        this(parent, embedModifier, usr -> true, name, time, unit, defaultValue, true);
    }

    protected Base(MessageReciever parent, User user, String name, T defaultValue) {
        this(parent, builder -> builder.addField(name.toUpperCase(), "Please enter a value for ```" + name + "```:"), user, name, defaultValue);
    }

    protected Base(MessageReciever parent, Consumer<Embed.Builder> embedModifier, User user, String name, T defaultValue) {
        this(parent, embedModifier, user, name, 5, TimeUnit.MINUTES, defaultValue);
    }

    protected Base(MessageReciever parent, Consumer<Embed.Builder> embedModifier, User user, String name, long time, TimeUnit unit, T defaultValue) {
        this(parent, embedModifier, user::equals, name, time, unit, defaultValue, true);
        this.constructedForSpecified = user;
    }

    protected Base(MessageReciever parent,
                   Consumer<Embed.Builder> embedModifier,
                   Predicate<User> participantTester,
                   String name,
                   long time,
                   TimeUnit unit,
                   T defaultValue,
                   boolean deleteWhenDone) {
        this.parent = Objects.requireNonNull(parent);
        this.embedModifier = Objects.requireNonNull(embedModifier);
        this.participantTester = Objects.requireNonNull(participantTester);
        this.name = Objects.requireNonNull(name);
        this.time = time;
        this.unit = Objects.requireNonNull(unit);
        this.defaultValue = Objects.requireNonNull(defaultValue);
        this.deleteWhenDone = deleteWhenDone;

        affiliates = new ArrayList<>();
    }

    public abstract CompletableFuture<T> build();

    protected void cleanup() {
        if (deleteWhenDone) affiliates.forEach(Message::delete);
    }

    protected void addAffiliate(Message message) {
        affiliates.add(message);
    }

    protected CompletableFutureExtended<T> createFuture() {
        return new CompletableFutureExtended<>(parent.getDiscord()
                .getExecutor());
    }

    protected Embed.Builder createEmbed() {
        Embed.Builder builder = Embed.BUILDER();
        builder.setTimestampNow();
        builder.setFooter("If you dont respond within " + time + " " + unit.name()
                .toLowerCase() + ", the automatic response will be `" +
                toStringFunction.apply(defaultValue) + "`!");
        if (parent instanceof ServerChannel) {
            Server server = ((ServerChannel) parent).getServer();
            ServerMember member = server.getDiscord()
                    .getSelf()
                    .toServerMember(server)
                    .orElseThrow(AssertionError::new);
            member.getRoleColor()
                    .ifPresent(builder::setColor);
        }
        if (constructedForSpecified != null)
            builder.setAuthor("Only " + constructedForSpecified.getDiscriminatedName() + " can respond to this!",
                    constructedForSpecified.getAvatarUrl()
                            .map(URL::toExternalForm)
                            .orElse("http://google.com"),
                    constructedForSpecified.getAvatarUrl()
                            .map(URL::toExternalForm)
                            .orElse("http://google.com"));
        embedModifier.accept(builder);
        return builder;
    }

    public void setToStringFunction(Function<T, String> toStringFunction) {
        this.toStringFunction = toStringFunction;
    }
}
