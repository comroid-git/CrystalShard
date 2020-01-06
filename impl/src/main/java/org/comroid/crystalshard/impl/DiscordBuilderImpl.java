package org.comroid.crystalshard.impl;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.comroid.crystalshard.api.Discord;

public class DiscordBuilderImpl implements Discord.Builder {
    private String token;

    @Override
    public Optional<String> getToken() {
        return Optional.ofNullable(token);
    }

    @Override
    public Discord.Builder setToken(String token) {
        this.token = token;
        
        return this;
    }

    @Override
    public CompletableFuture<Discord> build() {
        return null;
    }
}
