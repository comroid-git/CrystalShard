package org.comroid.crystalshard.api.model;

import org.comroid.crystalshard.adapter.Constructor;
import org.comroid.crystalshard.api.Discord;
import org.comroid.crystalshard.util.annotation.IntroducedBy;

import static org.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;

@Constructor(Discord.class)
public interface ApiBound {
    @IntroducedBy(PRODUCTION)
    Discord getAPI();
}
