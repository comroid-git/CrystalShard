package de.kaleidox.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.kaleidox.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class Payload {
    private OpCode code;
    private JsonNode node;

    public static Payload create(OpCode code, JsonNode node) {
        return new Payload()
                .opcode(code)
                .node(node);
    }

    public Payload opcode(OpCode code) {
        this.code = code;
        return this;
    }

    public Payload node(JsonNode node) {
        this.node = node;
        return this;
    }

    public OpCode getCode() {
        return code;
    }

    public CharSequence getBody() {
        return node.toString();
    }

    public List<Payload> split() {
        List<Payload> value = new ArrayList<>();

        value.add(this);

        return value;
    }

    public CharSequence getSendableNote() {
        ObjectNode data = JsonHelper.objectNode();

        data.set("op", JsonHelper.nodeOf(code.getCode()));
        if (!(node instanceof NullNode)) {
            data.set("d", node);
        }

        return data.toString();
    }
}
