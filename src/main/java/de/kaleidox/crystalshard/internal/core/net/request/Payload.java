package de.kaleidox.crystalshard.internal.core.net.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.kaleidox.crystalshard.internal.core.net.socket.OpCode;
import de.kaleidox.util.helpers.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class Payload {
    private OpCode code;
    private JsonNode node;
    private long lastSeq;
    private boolean last = true;

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

    public CharSequence getSendableNode() {
        ObjectNode data = JsonHelper.objectNode();

        data.set("op", JsonHelper.nodeOf(code.getCode()));
        data.set("s", JsonHelper.nodeOf(lastSeq));
        if (!(node instanceof NullNode)) {
            data.set("d", node);
        }

        return data.toString();
    }

    public void setLastSeq(long addAndGet) {
        this.lastSeq = addAndGet;
    }

    public boolean isLast() {
        return last;
    }

    public void addNode(String name, JsonNode nodeOf) {
        ObjectNode node = this.node.isNull() ? JsonHelper.objectNode() : this.node.deepCopy();
        node.set(name, nodeOf);
        this.node = node;
    }

    public static Payload create(OpCode code, JsonNode node) {
        return new Payload()
                .opcode(code)
                .node(node);
    }
}
