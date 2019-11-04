package de.comroid.crystalshard.core.api.rest;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.alibaba.fastjson.JSONObject;
import org.intellij.lang.annotations.MagicConstant;

public interface WebRequest<T> {
    WebRequest<T> method(RestMethod method);

    WebRequest<T> header(String name, String value);

    WebRequest<T> uri(URI uri);

    WebRequest<T> body(String body);

    WebRequest<T> expectCode(@MagicConstant(valuesFromClass = HTTPStatusCodes.class) int code);

    CompletableFuture<T> executeAs(Function<JSONObject, T> mapper);
}
