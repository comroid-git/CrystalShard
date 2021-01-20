package org.comroid.crystalshard.gateway.presence;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.model.presence.Activity;
import org.comroid.crystalshard.model.presence.UserStatus;
import org.comroid.crystalshard.util.Updater;
import org.comroid.uniform.node.UniObjectNode;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface OwnPresence extends Updater<Void>, ContextualProvider.Underlying {
    List<Activity> getActivities();

    OwnPresence setStatus(UserStatus status);

    OwnPresence setAFK(boolean afkState);

    OwnPresence addActivity(Activity activity);

    OwnPresence addActivity(Consumer<UniObjectNode> dataSetup);

    boolean removeActivity(Predicate<Activity> filter);

    @Override
    CompletableFuture<Void> update();
}
