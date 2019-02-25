package de.kaleidox.crystalshard.internal.util;

import de.kaleidox.crystalshard.api.entity.role.Role;
import de.kaleidox.crystalshard.api.handling.listener.ListenerAttachable;
import de.kaleidox.crystalshard.api.handling.listener.ListenerManager;
import de.kaleidox.crystalshard.api.handling.listener.server.role.RoleAttachableListener;
import de.kaleidox.util.functional.Evaluation;

import java.util.Collection;

public class RoleContainer implements ListenerAttachable<RoleAttachableListener> {
    private final Collection<Role> roles;

    public RoleContainer(Collection<Role> roles) {
        this.roles = roles;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    @Override
    public <C extends RoleAttachableListener> ListenerManager<C> attachListener(C listener) {
        throw new AbstractMethodError("RoleContainer is a DummyClass!");
    }

    @Override
    public Evaluation<Boolean> detachListener(RoleAttachableListener listener) {
        throw new AbstractMethodError("RoleContainer is a DummyClass!");
    }

    @Override
    public Collection<ListenerManager<? extends RoleAttachableListener>> getListenerManagers() {
        throw new AbstractMethodError("RoleContainer is a DummyClass!");
    }

    @Override
    public Collection<RoleAttachableListener> getAttachedListeners() {
        throw new AbstractMethodError("RoleContainer is a DummyClass!");
    }
}
