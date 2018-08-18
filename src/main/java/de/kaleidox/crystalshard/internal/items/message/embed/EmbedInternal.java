package de.kaleidox.crystalshard.internal.items.message.embed;

import de.kaleidox.crystalshard.main.items.message.embed.Embed;
import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.main.items.message.embed.SentEmbed;

import java.util.Optional;

public class EmbedInternal implements Embed {
    @Override
    public Optional<EmbedDraft> toEmbedDraft() {
        if (this instanceof EmbedDraft) {
            return Optional.of((EmbedDraft) this);
        } else if (this instanceof SentEmbed) {
            return ((SentEmbed) this).toEmbedDraft();
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<SentEmbed> toSentEmbed() {
        return castTo(SentEmbed.class);
    }

    @Override
    public Optional<Builder> toBuilder() {
        if (this instanceof EmbedDraft) {
            return ((EmbedDraft) this).toBuilder();
        } else if (this instanceof SentEmbed) {
            return ((SentEmbed) this).toBuilder();
        } else if (this instanceof Embed.Builder) {
            return Optional.of((Builder) this);
        } else {
            throw new AssertionError("Embed is orNull unknown type.");
        }
    }
}
