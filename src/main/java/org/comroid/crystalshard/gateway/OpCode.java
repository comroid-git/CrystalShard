package org.comroid.crystalshard.gateway;

import org.comroid.api.IntEnum;
import org.comroid.api.Named;
import org.comroid.common.info.Described;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniNode;
import org.comroid.uniform.node.UniObjectNode;

import java.util.function.Function;
import java.util.function.Predicate;

public enum OpCode implements IntEnum, Named, Described, Predicate<UniNode>, Function<UniObjectNode, UniObjectNode> {
    DISPATCH(0, "An event was dispatched."),
    HEARTBEAT(1, "Fired periodically by the client to keep the connection alive."),
    IDENTIFY(2, "Starts a new session during the initial handshake."),
    PRESENCE_UPDATE(3, "Update the client's presence."),
    VOICE_STATE_UPDATE(4, "Used to join/leave or move between voice channels."),
    RESUME(6, "Resume a previous session that was disconnected."),
    RECONNECT(7, "You should attempt to reconnect and resume immediately."),
    REQUEST_GUILD_MEMBERS(8, "Request information about offline guild members in a large guild."),
    INVALID_SESSION(9, "The session has been invalidated. You should reconnect and identify/resume accordingly."),
    HELLO(10, "Sent immediately after connecting, contains the heartbeat_interval to use."),
    HEARTBEAT_ACK(11, "Sent in response to receiving a heartbeat to acknowledge that it has been received.");

    private final int value;
    private final String description;

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return description;
    }

    OpCode(int value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public boolean test(UniNode data) {
        return data.get("op").asInt() == value;
    }

    @Override
    public UniObjectNode apply(UniObjectNode node) {
        node.put("op", ValueType.INTEGER, value);

        return node;
    }
}
