package de.comroid.crystalshard.api.entity.emoji;

// https://discordapp.com/developers/docs/resources/emoji#emoji-object-emoji-structure

import java.util.Optional;

import de.comroid.crystalshard.adapter.Adapter;
import de.comroid.crystalshard.util.model.TypeGroup;
import de.comroid.crystalshard.util.model.serialization.JSONBinding;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;

import com.alibaba.fastjson.JSONObject;

import static de.comroid.crystalshard.util.model.serialization.JSONBinding.identity;

public interface Emoji extends JsonDeserializable, TypeGroup<Emoji> {
    default String getName() {
        return getBindingValue(JSON.NAME);
    }

    String getDiscordPrintableString();

    default Optional<CustomEmoji> asCustomEmoji() {
        return as(CustomEmoji.class);
    }
    
    default Optional<Emoji> asUnicodeEmoji() {
        return (this instanceof Emoji && !(this instanceof CustomEmoji)) ? Optional.of(this) : Optional.empty();
    }

    static Emoji unicode(String emoji) {
        return Adapter.require(Emoji.class, emoji);
    }

    interface JSON {
        JSONBinding.OneStage<String> NAME = identity("name", JSONObject::getString);
    }
}
