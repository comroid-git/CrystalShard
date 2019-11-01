package de.comroid.crystalshard.api.model;

import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.util.annotation.IntroducedBy;

import static de.comroid.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;

public interface ApiBound {
    @IntroducedBy(PRODUCTION)
    Discord getAPI();
}
