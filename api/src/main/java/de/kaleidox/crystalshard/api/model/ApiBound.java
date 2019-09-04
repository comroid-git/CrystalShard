package de.kaleidox.crystalshard.api.model;

import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.util.annotation.IntroducedBy;

import static de.kaleidox.crystalshard.util.annotation.IntroducedBy.ImplementationSource.PRODUCTION;

public interface ApiBound {
    @IntroducedBy(PRODUCTION)
    Discord getAPI();
}
