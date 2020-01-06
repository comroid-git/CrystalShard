package org.comroid.crystalshard.api.event.multipart;

import org.comroid.crystalshard.api.Discord;
import org.comroid.crystalshard.api.event.EventBase;

public interface APIEvent extends EventBase {
    Discord getAPI();
}
