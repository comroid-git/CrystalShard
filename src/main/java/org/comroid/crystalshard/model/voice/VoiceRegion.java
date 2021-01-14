package org.comroid.crystalshard.model.voice;

import org.comroid.api.ContextualProvider;
import org.comroid.api.Named;
import org.comroid.crystalshard.Bot;
import org.comroid.crystalshard.model.AbstractDataContainer;
import org.comroid.crystalshard.rest.BoundEndpoint;
import org.comroid.mutatio.ref.Reference;
import org.comroid.restless.REST;
import org.comroid.uniform.node.impl.StandardValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public final class VoiceRegion extends AbstractDataContainer implements Named {
    @RootBind
    public final static GroupBind<VoiceRegion> TYPE
            = BASETYPE.rootGroup("voice-region");
    public static final VarBind<VoiceRegion, String, String, String> ID
            = TYPE.createBind("id")
            .extractAs(StandardValueType.STRING)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<VoiceRegion, String, String, String> NAME
            = TYPE.createBind("name")
            .extractAs(StandardValueType.STRING)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<VoiceRegion, Boolean, Boolean, Boolean> IS_VIP
            = TYPE.createBind("vip")
            .extractAs(StandardValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<VoiceRegion, Boolean, Boolean, Boolean> IS_OPTIMAL
            = TYPE.createBind("optimal")
            .extractAs(StandardValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<VoiceRegion, Boolean, Boolean, Boolean> IS_DEPRECATED
            = TYPE.createBind("deprecated")
            .extractAs(StandardValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<VoiceRegion, Boolean, Boolean, Boolean> IS_CUSTOM
            = TYPE.createBind("custom")
            .extractAs(StandardValueType.BOOLEAN)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    private final static Map<String, VoiceRegion> cache = new ConcurrentHashMap<>();
    public final Reference<String> id = getComputedReference(ID);
    public final Reference<String> name = getComputedReference(NAME);
    public final Reference<Boolean> isVip = getComputedReference(IS_VIP);
    public final Reference<Boolean> isOptimal = getComputedReference(IS_OPTIMAL);
    public final Reference<Boolean> isDeprecated = getComputedReference(IS_DEPRECATED);
    public final Reference<Boolean> isCustom = getComputedReference(IS_CUSTOM);

    @Override
    public String getName() {
        return null;
    }

    public VoiceRegion(ContextualProvider context, @Nullable UniObjectNode initialData) {
        super(context, initialData);
    }

    @Internal
    public static @Nullable VoiceRegion find(ContextualProvider context, String name) {
        if (!cache.containsKey(name))
            refreshCache(context).join();
        return cache.get(name);
    }

    @Internal
    public static CompletableFuture<Void> refreshCache(ContextualProvider context) {
        return context.requireFromContext(Bot.class)
                .newRequest(REST.Method.GET, BoundEndpoint.VOICE_REGIONS)
                .thenAccept(vrr -> vrr.regions.assertion()
                        .forEach(region -> {
                            String name = region.getName();
                            if (cache.containsKey(name))
                                cache.replace(name, region);
                            else cache.put(name, region);
                        }));
    }
}
