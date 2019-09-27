package de.kaleidox.crystalshard.util.model.serialization;

public interface JsonDeserializable {
    //Set<JsonTrait<?, ?>> possibleTraits();

    <T> T getTrait(JsonTrait<?, T> trait);
}
