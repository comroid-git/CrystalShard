package de.comroid.crystalshard.util.ui.embed;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.embed.EmbedField;

public class EmbedFieldRepresentative implements EmbedField {
    protected String name;
    protected String value;
    protected boolean inline;

    public EmbedFieldRepresentative(String name, String value) {
        this(name, value, false);
    }

    public EmbedFieldRepresentative(String name, String value, boolean inline) {
        this.name = name;
        this.value = value;
        this.inline = inline;
    }

    public EmbedBuilder fillBuilder(EmbedBuilder embedBuilder) {
        return embedBuilder
                .addField(name, value, inline);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean isInline() {
        return inline;
    }

    @Override
    public String getName() {
        return name;
    }
}
