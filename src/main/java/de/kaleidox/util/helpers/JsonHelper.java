package de.kaleidox.util.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.kaleidox.logging.Logger;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
public class JsonHelper {
    private final static Logger logger = new Logger(JsonHelper.class);

    public static JsonNode nodeOf(Object of) {
        if (of == null) {
            return JsonNodeFactory.instance.nullNode();
        } else if (of instanceof Integer) {
            return JsonNodeFactory.instance.numberNode((Integer) of);
        } else if (of instanceof Long) {
            return JsonNodeFactory.instance.numberNode((Long) of);
        } else if (of instanceof Double) {
            return JsonNodeFactory.instance.numberNode((Double) of);
        } else if (of instanceof String) {
            return JsonNodeFactory.instance.textNode((String) of);
        } else if (of instanceof Boolean) {
            return JsonNodeFactory.instance.booleanNode((Boolean) of);
        } else {
            return JsonNodeFactory.instance.textNode(of.toString());
        }
    }

    public static <T, N> ArrayNode arrayNode(List<T> items, Function<T, N> mapper) {
        ArrayNode node = JsonNodeFactory.instance.arrayNode(items.size());
        items.stream()
                .map(mapper)
                .map(JsonHelper::nodeOf)
                .forEach(node::add);
        return node;
    }

    public static <T> ArrayNode arrayNode(List<T> items) {
        ArrayNode node = JsonNodeFactory.instance.arrayNode(items.size());
        items.stream()
                .map(JsonHelper::nodeOf)
                .forEach(node::add);
        return node;
    }

    public static ArrayNode arrayNode(Object... items) {
        ArrayNode node = JsonNodeFactory.instance.arrayNode(items.length);

        List.of(items).forEach(item -> node.add(nodeOf(item)));

        return node;
    }

    public static ObjectNode objectNode() {
        return JsonNodeFactory.instance.objectNode();
    }

    public static JsonNode parse(String body) {
        try {
            return new ObjectMapper().readTree(body);
        } catch (IOException e) {
            logger.exception(e);
            return objectNode();
        }
    }
}
