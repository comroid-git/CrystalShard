package de.comroid.crystalshard.api.event.multipart;

import de.comroid.crystalshard.api.Discord;
import de.comroid.crystalshard.api.event.EventBase;

public interface APIEvent extends EventBase {
    Discord getAPI();
}
