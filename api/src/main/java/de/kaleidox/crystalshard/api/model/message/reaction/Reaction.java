package de.kaleidox.crystalshard.api.model.message.reaction;

// https://discordapp.com/developers/docs/resources/channel#reaction-object-reaction-structure

import de.kaleidox.crystalshard.api.entity.emoji.Emoji;
import de.kaleidox.crystalshard.api.model.ApiBound;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;
import de.kaleidox.crystalshard.util.model.serialization.JsonDeserializable;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.GETTER;

public interface Reaction extends JsonDeserializable {
    @IntroducedBy(GETTER)
    int getCount();

    @IntroducedBy(GETTER)
    boolean haveYouReacted();

    @IntroducedBy(GETTER)
    Emoji getEmoji();
}
