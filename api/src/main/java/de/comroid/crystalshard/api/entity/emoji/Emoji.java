package de.comroid.crystalshard.api.entity.emoji;

// https://discordapp.com/developers/docs/resources/emoji#emoji-object-emoji-structure

import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;

public interface Emoji extends JsonDeserializable { // todo deserialize
    String getName();

    String getDiscordPrintableString();
}
