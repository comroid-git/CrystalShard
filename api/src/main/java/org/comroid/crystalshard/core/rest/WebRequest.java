package org.comroid.crystalshard.core.rest;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.intellij.lang.annotations.MagicConstant;

public interface WebRequest<T> {
    WebRequest<T> method(RestMethod method);

    WebRequest<T> header(String name, String value);

    WebRequest<T> uri(URI uri);

    WebRequest<T> body(String body);

    WebRequest<T> expectCode(@MagicConstant(valuesFromClass = HTTPStatusCodes.class) int code);

    CompletableFuture<T> executeAsObject(Function<JSONObject, T> mapper);

    CompletableFuture<T> executeAsArray(Function<JSONArray, T> mapper);
}
