package org.comroid.crystalshard.util.ui.messages.categorizing;

import java.util.ArrayList;
import java.util.List;

import org.comroid.crystalshard.adapter.Adapter;
import org.comroid.crystalshard.api.model.message.embed.Embed;

public class EmbedCategory {
    private final String name;
    private final String description;
    private final List<Embed.Field> fields;

    public EmbedCategory(String name, String description) {
        this.name = name;
        this.description = description;

        fields = new ArrayList<>();
    }

    public EmbedCategory addField(String name, String value) {
        return addField(name, value, false);
    }

    public EmbedCategory addField(String name, String value, boolean inline) {
        fields.add(Adapter.require(Embed.Field.class, name, value, inline));
        return this;
    }

    public List<Embed.Field> getFields() {
        return fields;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
