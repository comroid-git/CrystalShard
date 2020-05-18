package org.comroid.crystalshard.entity.message.reaction;

import org.comroid.crystalshard.entity.user.User;
import org.comroid.crystalshard.model.emoji.Emoji;

import java.util.Map;

public interface Reactions extends TriDimensionalMap<Emoji, User, Reaction> {
}
