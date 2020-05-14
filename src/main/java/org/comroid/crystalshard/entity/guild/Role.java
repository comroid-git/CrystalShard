package org.comroid.crystalshard.entity.guild;

import org.comroid.common.ref.Named;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.model.Mentionable;
import org.comroid.crystalshard.model.channel.PermissionOverride;

public interface Role extends Snowflake, Named, Mentionable, PermissionOverride.Settable {
    @Override
    default String getDefaultFormattedName() {
        return getName();
    }

    @Override
    default String getAlternateFormattedName() {
        return getMentionTag();
    }
}
