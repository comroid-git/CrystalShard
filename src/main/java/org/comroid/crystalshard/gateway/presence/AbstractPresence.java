package org.comroid.crystalshard.gateway.presence;

import org.comroid.api.ContextualProvider;
import org.comroid.crystalshard.model.presence.Activity;
import org.comroid.crystalshard.model.presence.UserStatus;
import org.comroid.uniform.SerializationAdapter;
import org.comroid.uniform.node.UniObjectNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class AbstractPresence implements OwnPresence {
    protected final List<Activity> activities;
    private final ContextualProvider context;
    protected UserStatus status;
    protected boolean afkState;

    @Override
    public final ContextualProvider getUnderlyingContextualProvider() {
        return context;
    }

    @Override
    public final List<Activity> getActivities() {
        return Collections.unmodifiableList(activities);
    }

    public AbstractPresence(ContextualProvider context) {
        this.context = context;
        this.activities = new ArrayList<>();
    }

    @Override
    public final OwnPresence setAFK(boolean afkState) {
        this.afkState = afkState;
        return this;
    }

    @Override
    public final OwnPresence setStatus(UserStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public final OwnPresence addActivity(Activity activity) {
        activities.add(activity);
        return this;
    }

    @Override
    public final OwnPresence addActivity(Consumer<UniObjectNode> dataSetup) {
        UniObjectNode obj = requireFromContext(SerializationAdapter.class).createObjectNode();
        dataSetup.accept(obj);
        return addActivity(new Activity(this, obj));
    }

    @Override
    public final boolean removeActivity(Predicate<Activity> filter) {
        return activities.removeIf(filter);
    }
}
