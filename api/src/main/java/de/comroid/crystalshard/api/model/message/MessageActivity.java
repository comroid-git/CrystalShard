package de.comroid.crystalshard.api.model.message;

// https://discordapp.com/developers/docs/resources/channel#message-object-message-activity-structure
// https://discordapp.com/developers/docs/resources/channel#message-object-message-activity-types

import java.util.Optional;

import de.comroid.crystalshard.adapter.MainAPI;
import de.comroid.crystalshard.util.model.serialization.JsonBinding;
import de.comroid.crystalshard.util.model.serialization.JsonDeserializable;
import de.comroid.crystalshard.util.model.serialization.JsonTraits;

import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.Nullable;

import static de.comroid.crystalshard.util.model.serialization.JsonBinding.identity;
import static de.comroid.crystalshard.util.model.serialization.JsonBinding.simple;

@MainAPI
@JsonTraits(MessageActivity.Trait.class)
public interface MessageActivity extends JsonDeserializable {
    default Type getType() {
        return getTraitValue(Trait.TYPE);
    }

    default Optional<String> getPartyID() {
        return wrapTraitValue(Trait.PARTY_ID);
    }

    interface Trait {
        JsonBinding.TwoStage<Integer, Type> TYPE = simple("type", JSONObject::getInteger, Type::valueOf);
        JsonBinding.OneStage<String> PARTY_ID = identity("party_id", JSONObject::getString);
    }

    enum Type {
        JOIN(1),
        SPECTATE(2),
        LISTEN(3),
        JOIN_REQUEST(5);

        public final int value;

        Type(int value) {
            this.value = value;
        }

        public static @Nullable Type valueOf(int value) {
            for (Type type : values())
                if (type.value == value)
                    return type;

            return null;
        }
    }
}
