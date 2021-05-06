package org.comroid.crystalshard.model.message;

import org.comroid.api.Named;
import org.comroid.crystalshard.entity.Snowflake;

public interface Mentionable extends Snowflake, Named {
    default String getMentionTag() {
        return null; // todo
    }

    @Override
    default String getAlternateName() {
        return getMentionTag();
    }
}
