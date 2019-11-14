package de.comroid.javacord.util.ui.messages.categorizing;

import java.util.ArrayList;
import java.util.List;

import de.comroid.javacord.util.ui.embed.EmbedFieldRepresentative;

import org.javacord.api.entity.Nameable;
import org.javacord.api.entity.message.embed.EmbedField;

public class EmbedCategory implements Nameable {
    private final String name;
    private final String description;
    private final List<EmbedField> fields;

    public EmbedCategory(String name, String description) {
        this.name = name;
        this.description = description;

        fields = new ArrayList<>();
    }

    public EmbedCategory addField(String name, String value) {
        return addField(name, value, false);
    }

    public EmbedCategory addField(String name, String value, boolean inline) {
        fields.add(new EmbedFieldRepresentative(name, value, inline));
        return this;
    }

    public List<EmbedField> getFields() {
        return fields;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
