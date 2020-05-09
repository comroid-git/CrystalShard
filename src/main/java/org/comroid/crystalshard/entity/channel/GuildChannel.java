package org.comroid.crystalshard.entity.channel;

import org.comroid.common.ref.Named;

public interface GuildChannel extends Channel, Named {
    @Override
    default String getDefaultFormattedName() {
        return getName();
    }

    @Override
    default String getAlternateFormattedName() {
        return getMentionTag();
    }
}
