package de.kaleidox.crystalshard.util.discord.input;

import de.kaleidox.crystalshard.main.handling.event.message.generic.MessageCreateEvent;
import de.kaleidox.crystalshard.main.handling.listener.message.generic.MessageCreateListener;
import de.kaleidox.crystalshard.main.items.message.MessageReciever;
import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.user.User;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TextInput extends Base<String> {
    protected TextInput(MessageReciever parent, String name, String defaultValue) {
        super(parent, name, defaultValue);
    }
    
    protected TextInput(MessageReciever parent, Consumer<Embed.Builder> embedModifier, String name, String defaultValue) {
        super(parent, embedModifier, name, defaultValue);
    }
    
    protected TextInput(MessageReciever parent, Consumer<Embed.Builder> embedModifier, String name, long time, TimeUnit unit, String defaultValue) {
        super(parent, embedModifier, name, time, unit, defaultValue);
    }
    
    protected TextInput(MessageReciever parent, User user, String name, String defaultValue) {
        super(parent, user, name, defaultValue);
    }
    
    protected TextInput(MessageReciever parent, Consumer<Embed.Builder> embedModifier, User user, String name, String defaultValue) {
        super(parent, embedModifier, user, name, defaultValue);
    }
    
    protected TextInput(MessageReciever parent, Consumer<Embed.Builder> embedModifier, User user, String name, long time, TimeUnit unit, String defaultValue) {
        super(parent, embedModifier, user, name, time, unit, defaultValue);
    }
    
    protected TextInput(MessageReciever parent,
                        Consumer<Embed.Builder> embedModifier,
                        Predicate<User> participantTester,
                        String name,
                        long time,
                        TimeUnit unit,
                        String defaultValue,
                        boolean deleteOnResponse) {
        super(parent, embedModifier, participantTester, name, time, unit, defaultValue, deleteOnResponse);
    }
    
    @Override
    public CompletableFuture<String> build() {
        CompletableFuture<String> future = createFuture();
        Embed.Builder embed = createEmbed();
        
        parent.sendMessage(embed.build()).exceptionally(logger::exception).thenAcceptAsync(message -> {
            addAffiliate(message);
            message.getChannel().attachListener(new MessageCreateListener() {
                @Override
                public void onMessageCreate(MessageCreateEvent event) {
                    event.getMessageAuthorUser().filter(participantTester::test).filter(usr -> !usr.isYourself()).ifPresent(author -> {
                        future.complete(event.getMessageContent());
                        message.getChannel().detachListener(this).onFailure(bFalse -> logger.error("Error detaching listener: " + this));
                        cleanup();
                    });
                }
            }).detachIn(time, unit).onDetach(() -> {
                if (!future.isDone()) future.complete(defaultValue);
            });
        });
        
        return future;
    }
}
