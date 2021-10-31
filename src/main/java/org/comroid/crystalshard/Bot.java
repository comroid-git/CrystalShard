package org.comroid.crystalshard;

import org.apache.logging.log4j.Logger;
import org.comroid.api.ContextualProvider;
import org.comroid.api.Initializable;
import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.gateway.event.GatewayEvent;
import org.comroid.crystalshard.gateway.presence.OwnPresence;
import org.comroid.crystalshard.model.presence.Activity;
import org.comroid.crystalshard.model.presence.UserStatus;
import org.comroid.mutatio.model.RefPipe;
import org.comroid.restless.socket.WebsocketPacket;

import java.io.Closeable;
import java.util.concurrent.CompletableFuture;

public interface Bot extends DiscordREST, Closeable, ContextualProvider.Underlying, Initializable {
    Logger getLogger();

    default long getOwnID() {
        return getYourself().getID();
    }

    RefPipe<?, ?, WebsocketPacket.Type, GatewayEvent> getEventPipeline();

    boolean isReady();

    User getYourself();

    int getCurrentShardID();

    int getShardCount();

    OwnPresence getOwnPresence();

    String getToken();

    default CompletableFuture<Void> updateStatus(UserStatus status) {
        return updatePresence(status, null, null);
    }

    default CompletableFuture<Void> updateActivity(Activity.Type type, String detail) {
        return updatePresence(null, type, detail);
    }

    default CompletableFuture<Void> updatePresence(UserStatus status, String detail) {
        return updatePresence(status, Activity.Type.PLAYING, detail);
    }

    default CompletableFuture<Void> updatePresence(UserStatus status, Activity.Type type, String detail) {
        OwnPresence presence = getOwnPresence();
        if (status != null)
            presence.setStatus(status);
        if (type != null)
            presence.addActivity(new Activity(this, type, detail));
        return presence.update();
    }
}
