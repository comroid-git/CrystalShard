package org.comroid.restless.socket.model;

import org.comroid.common.ref.IntEnum;

public interface OPCode extends IntEnum {
    Usage getUsage();

    default boolean canSend() {
        return getUsage().canSend();
    }

    default boolean canReceive() {
        return getUsage().canReceive();
    }

    enum Usage {
        SEND(true, false),
        RECEIVE(false, true),
        BIDIRECTIONAL(true, true);

        private final boolean send;
        private final boolean receive;

        Usage(boolean send, boolean receive) {
            this.send    = send;
            this.receive = receive;
        }

        public boolean canSend() {
            return send;
        }

        public boolean canReceive() {
            return receive;
        }
    }
}
