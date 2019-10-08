package de.kaleidox.crystalshard.api.entity.emoji;

// https://discordapp.com/developers/docs/resources/emoji#emoji-object-emoji-structure

import de.kaleidox.crystalshard.util.model.serialization.JsonDeserializable;

public interface Emoji extends JsonDeserializable { // todo deserialize
    String getName();

    String getDiscordPrintableString();
}
