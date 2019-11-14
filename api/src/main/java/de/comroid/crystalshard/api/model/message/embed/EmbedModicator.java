package de.comroid.crystalshard.api.model.message.embed;

import java.awt.Color;

import de.comroid.crystalshard.api.entity.user.User;

public interface EmbedModicator<Self extends EmbedModicator<Self>> { // todo
    Self removeAllFields();

    Self setColor(Color color);

    Self setAuthor(User user);

    default Self addField(String name, String value) {
        return addField(name, value, false);
    }

    default Self addInlineField(String name, String value) {
        return addField(name, value, true);
    }

    Self addField(String name, String value, boolean inline);

    Self setFooter(String s);

    Self setDescription(String toString);

    Self setAuthor(String name);
}
