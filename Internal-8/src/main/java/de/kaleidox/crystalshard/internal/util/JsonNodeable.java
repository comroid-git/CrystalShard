package de.kaleidox.crystalshard.internal.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.kaleidox.util.helpers.JsonHelper;

public interface JsonNodeable {
    JsonNode toJsonNode(ObjectNode baseNode);

    default JsonNode toJsonNode() {
        return toJsonNode(JsonHelper.objectNode());
    }
}
