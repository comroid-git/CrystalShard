package org.comroid.crystalshard.entity.guild;

import org.comroid.api.Named;
import org.comroid.crystalshard.entity.DiscordEntity;
import org.comroid.crystalshard.model.Mentionable;
import org.comroid.crystalshard.model.channel.PermissionOverride;

public interface Role extends DiscordEntity, Named, Mentionable, PermissionOverride.Settable {
    @Override
    default String getDefaultFormattedName() {
        return getName();
    }

    @Override
    default String getAlternateFormattedName() {
        return getMentionTag();
    }
}
