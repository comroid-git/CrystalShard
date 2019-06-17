package de.kaleidox.crystalshard.internal.items.message.embed;

import de.kaleidox.crystalshard.api.entity.message.embed.Embed;
import de.kaleidox.crystalshard.api.entity.message.embed.EmbedDraft;
import de.kaleidox.crystalshard.api.entity.message.embed.SentEmbed;

import java.util.Optional;

public class EmbedInternal implements Embed {
    // Override Methods
    @Override
    public EmbedDraft toEmbedDraft() {
        if (this instanceof EmbedDraft) {
            return (EmbedDraft) this;
        } else if (this instanceof SentEmbed) {
            return ((SentEmbed) this).toEmbedDraft();
        } else {
            throw new AssertionError();
        }
    }

    @Override
    public Builder toBuilder() {
        if (this instanceof EmbedDraft) {
            return ((EmbedDraft) this).toBuilder();
        } else if (this instanceof SentEmbed) {
            return ((SentEmbed) this).toBuilder();
        } else {
            throw new AssertionError("Embed has unknown type.");
        }
    }

    @Override
    public Optional<SentEmbed> toSentEmbed() {
        return castTo(SentEmbed.class);
    }
}