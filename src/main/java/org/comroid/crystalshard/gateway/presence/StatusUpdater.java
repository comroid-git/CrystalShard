package org.comroid.crystalshard.gateway.presence;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.Bot;
import org.comroid.crystalshard.DiscordBotBase;
import org.comroid.crystalshard.DiscordBotShard;
import org.comroid.crystalshard.gateway.OpCode;
import org.comroid.crystalshard.model.presence.Activity;
import org.comroid.crystalshard.model.presence.UserStatus;
import org.comroid.crystalshard.util.Updater;
import org.comroid.uniform.SerializationAdapter;
import org.comroid.uniform.node.UniArrayNode;
import org.comroid.uniform.node.UniObjectNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class StatusUpdater implements Updater<Void>, ContextualProvider.Underlying {
    private final Bot bot;
    private final List<Activity> activities;
    private UserStatus newStatus;
    private boolean afkState;

    @Override
    public ContextualProvider getUnderlyingContextualProvider() {
        return bot;
    }

    public StatusUpdater setStatus(UserStatus status) {
        this.newStatus = status;
        return this;
    }

    public StatusUpdater setAFK(boolean afkState) {
        this.afkState = afkState;
        return this;
    }

    public StatusUpdater(Bot bot) {
        this.bot = bot;
        this.activities = new ArrayList<>();
    }

    public StatusUpdater addActivity(Activity activity) {
        activities.add(activity);
        return this;
    }

    public StatusUpdater addActivity(Consumer<UniObjectNode> dataSetup) {
        UniObjectNode obj = requireFromContext(SerializationAdapter.class).createUniObjectNode();
        dataSetup.accept(obj);
        return addActivity(new Activity(this, obj));
    }

    public boolean removeActivity(Predicate<Activity> filter) {
        return activities.removeIf(filter);
    }

    public List<Activity> getActivities() {
        return Collections.unmodifiableList(activities);
    }

    @Override
    public CompletableFuture<Void> update() {
        if (bot instanceof DiscordBotShard) {
            final UniObjectNode obj = ((DiscordBotShard) bot).getGateway()
                    .createPayloadBase(OpCode.PRESENCE_UPDATE);
            final UniObjectNode data = obj.putObject("d");
            data.put("since", System.currentTimeMillis());
            data.put("status", newStatus.getIdent());
            data.put("afk", afkState);
            final UniArrayNode activities = data.putArray("activities");
            this.activities.forEach(activity -> activity.toObjectNode(activities.addObject()));

            return ((DiscordBotShard) bot).getGateway()
                    .getSocket().send(obj.toString())
                    .thenApply(nil -> null);
        } else return CompletableFuture.allOf(
                ((DiscordBotBase) bot).getShards()
                        .stream()
                        .map(shard -> {
                            StatusUpdater updater = shard.updateOwnPresence();
                            updater.newStatus = newStatus;
                            updater.afkState = afkState;
                            activities.forEach(updater::addActivity);
                            return updater.update();
                        })
                        .toArray(CompletableFuture[]::new));
    }
}
