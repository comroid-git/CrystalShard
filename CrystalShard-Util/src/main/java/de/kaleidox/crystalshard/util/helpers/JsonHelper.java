package de.kaleidox.crystalshard.util.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.kaleidox.crystalshard.logging.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

@SuppressWarnings("FinalStaticMethod")
public final class JsonHelper extends NullHelper {
    private final static Logger logger = new Logger(JsonHelper.class);
    
    public final static JsonNode nodeOf(Object of) {
        if (of == null) {
            return JsonNodeFactory.instance.nullNode();
        } else if (of instanceof JsonNode) {
            return (JsonNode) of;
        } else if (of instanceof Collection) {
            return arrayNode((Collection) of);
        } else if (of instanceof Stream) {
            return arrayNode(((Stream) of).toArray());
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
    
    public final static <T, N> ArrayNode arrayNode(List<T> items, Function<T, N> mapper) {
        ArrayNode node = JsonNodeFactory.instance.arrayNode(items.size());
        for (T item : items) node.add(nodeOf(mapper.apply(item)));
        return node;
    }
    
    public final static <T> ArrayNode arrayNode(List<T> items) {
        ArrayNode node = JsonNodeFactory.instance.arrayNode(items.size());
        for (T item : items) node.add(nodeOf(item));
        return node;
    }
    
    public final static ArrayNode arrayNode(Object... items) {
        ArrayNode node = JsonNodeFactory.instance.arrayNode(items.length);
        for (Object item : items) node.add(nodeOf(item));
        return node;
    }
    
    public final static ObjectNode objectNode(Object... data) {
        if (data.length == 0) return JsonNodeFactory.instance.objectNode();
        if (data.length % 2 != 0) throw new IllegalArgumentException("You must provide an even amount of objects to be placed in the node.");
        ObjectNode objectNode = objectNode();
        for (List<Object> pair : ListHelper.everyOfList(2, ListHelper.ofWithNulls(data))) {
            if (Objects.nonNull(pair.get(0)) && Objects.nonNull(pair.get(1))) objectNode.set(pair.get(0).toString(), nodeOf(pair.get(1)));
            // ignore all pairs of which both sides are NULL
        }
        return objectNode;
    }
    
    public final static JsonNode parse(String body) {
        try {
            return new ObjectMapper().readTree(body);
        } catch (IOException e) {
            logger.exception(e, "IOException when parsing Node. Empty object node was returned. " + "Body: " + body);
            return objectNode();
        }
    }
}
