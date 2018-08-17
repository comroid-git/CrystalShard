package de.kaleidox.crystalshard.main.items.message;

import de.kaleidox.crystalshard.main.items.message.embed.EmbedDraft;

public class Sendable {
    private EmbedDraft embed;
    private String content = "";

    public Sendable() {
    }

    public Sendable(String content) {
        this(content, null);
    }

    public Sendable(EmbedDraft embed) {
        this(null, embed);
    }

    public Sendable(String content, EmbedDraft embed) {
        this.embed = embed;
        this.content = content;
    }

    public Sendable add(Object object) {
        return add(object.toString());
    }

    public Sendable add(String string) {
        content = content + string;
        return this;
    }

    public Sendable set(EmbedDraft embed) {
        this.embed = embed;
        return this;
    }
}
