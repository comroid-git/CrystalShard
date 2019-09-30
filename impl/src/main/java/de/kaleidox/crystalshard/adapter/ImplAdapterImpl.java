package de.kaleidox.crystalshard.adapter;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.impl.DiscordImpl;

public final class ImplAdapterImpl extends ImplAdapter {
    public ImplAdapterImpl() {
        mappingTool.implement(Discord.class, DiscordImpl.class, String.class); // todo
    }
}
