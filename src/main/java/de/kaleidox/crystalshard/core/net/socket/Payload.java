package de.kaleidox.crystalshard.core.net.socket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.kaleidox.util.helpers.JsonHelper;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class Payload {
    private OpCode   code;
    private JsonNode node;
    private long     lastSeq;
    private boolean  last = true;
    
    Payload opcode(OpCode code) {
        this.code = code;
        return this;
    }
    
    Payload node(JsonNode node) {
        this.node = node;
        return this;
    }
    
    OpCode getCode() {
        return code;
    }
    
    CharSequence getBody() {
        return node.toString();
    }
    
    List<Payload> split() {
        List<Payload> value = new ArrayList<>();
        
        value.add(this);
        
        return value;
    }
    
    CharSequence getSendableNode() {
        ObjectNode data = JsonHelper.objectNode();
        
        data.set("op", JsonHelper.nodeOf(code.getCode()));
        data.set("s", JsonHelper.nodeOf(lastSeq));
        if (!(node instanceof NullNode)) {
            data.set("d", node);
        }
        
        return data.toString();
    }
    
    void setLastSeq(long addAndGet) {
        this.lastSeq = addAndGet;
    }
    
    boolean isLast() {
        return last;
    }
    
    void addNode(String name, JsonNode nodeOf) {
        ObjectNode node = this.node.isNull() ? JsonHelper.objectNode() : this.node.deepCopy();
        node.set(name, nodeOf);
        this.node = node;
    }
    
// Static members
    // Static membe
    public static Payload create(OpCode code, JsonNode node) {
        return new Payload().opcode(code).node(node);
    }
}
