package de.kaleidox.crystalshard.core.api.rest;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Contract;

public interface DiscordRequest<T> extends WebRequest<T> {
    @Override
    DiscordRequest<T> method(RestMethod method);

    @Override
    DiscordRequest<T> header(String name, String value);

    @Override
    @Contract("_ -> fail")
    default DiscordRequest<T> uri(URI uri) throws IllegalStateException {
        throw new IllegalStateException("Cannot use method #uri(URI), use #endpoint(DiscordEndpoint, Object...) instead!");
    }

    @Override
    DiscordRequest<T> expectCode(@MagicConstant(valuesFromClass = HTTPCodes.class) int code);

    @Override
    CompletableFuture<T> executeAs(Function<String, T> mapper);

    DiscordRequest<T> endpoint(DiscordEndpoint endpoint, Object... args) throws IllegalArgumentException;

    DiscordEndpoint getEndpoint();
}
