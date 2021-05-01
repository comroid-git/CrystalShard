package org.comroid.crystalshard.model.message;

import org.comroid.api.Named;
import org.comroid.crystalshard.entity.Snowflake;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface Mentionable extends Snowflake, Named {
    default String getMentionTag() {
        return null; // todo
    }

    @Override
    default String getAlternateName() {
        return getMentionTag();
    }
}
