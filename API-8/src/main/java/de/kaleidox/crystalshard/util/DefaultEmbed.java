package de.kaleidox.crystalshard.util;

import de.kaleidox.crystalshard.api.entity.message.embed.Embed;
import de.kaleidox.crystalshard.api.entity.message.embed.EmbedDraft;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface DefaultEmbed extends Supplier<EmbedDraft> {
    Embed.Builder getBuilder();

    static EmbedDraft getStatic(Consumer<Embed.Builder> defaultEmbedModifier) {
        return null; // TODO: 08.11.2018
    }
}
