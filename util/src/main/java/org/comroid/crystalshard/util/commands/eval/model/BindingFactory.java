package org.comroid.crystalshard.util.commands.eval.model;

import java.util.HashMap;

import org.comroid.crystalshard.util.ui.embed.DefaultEmbedFactory;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

public class BindingFactory {
    private final HashMap<String, Object> bindings = new HashMap<>();

    public BindingFactory(User user, Message command, TextChannel channel, Server server) {
        this.bindings.putAll(new HashMap<String, Object>() {{
            put("msg", command);
            put("usr", user);
            put("chl", channel);
            put("srv", server);
            put("api", command.getApi());
            put("embed", DefaultEmbedFactory.create(server, user));
            channel.getMessagesBefore(1, command)
                    .join()
                    .getOldestMessage()
                    .ifPresent(prev -> put("prev", prev));
        }});
    }

    public HashMap<String, Object> getBindings() {
        return this.bindings;
    }

    public BindingFactory add(String name, Object binding) {
        this.bindings.put(name, binding);
        return this;
    }

    public BindingFactory remove(String name) {
        this.bindings.remove(name);
        return this;
    }
}
