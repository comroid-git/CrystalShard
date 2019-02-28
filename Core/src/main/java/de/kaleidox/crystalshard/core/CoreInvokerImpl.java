package de.kaleidox.crystalshard.core;

import de.kaleidox.crystalshard.CoreInvoker;
import de.kaleidox.crystalshard.Log;
import de.kaleidox.crystalshard.api.Discord;
import de.kaleidox.crystalshard.core.net.request.DiscordRequestImpl;
import de.kaleidox.crystalshard.core.net.request.endpoint.DiscordEndpoint;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;

import static de.kaleidox.crystalshard.core.net.request.HttpMethod.GET;

public class CoreInvokerImpl extends CoreInvoker {
    public static final Logger logger = Log.get(CoreInvokerImpl.class);

    @SuppressWarnings("unchecked")
    @Override
    public <T> T fromIDs(Discord discord, int entityType, long... ids) {
        switch (entityType) {
            case 0: // EMOJI
                assert ids.length == 2 : "Too " + cmpr(ids.length, 2) + " IDs were provided! [2]";

                return (T) new DiscordRequestImpl<>(discord)
                        .setUri(DiscordEndpoint.CUSTOM_EMOJI_SPECIFIC.createUri(ids[0], ids[1]))
                        .setMethod(GET)
                        .executeAs(JsonNode::toString) // TODO
                        .join();
            case 1: // MESSAGE
                assert ids.length == 2 : "Too " + cmpr(ids.length, 2) + " IDs were provided! [2]";

                return (T) new DiscordRequestImpl<>(discord)
                        .setUri(DiscordEndpoint.MESSAGE_SPECIFIC.createUri(ids[0], ids[1]))
                        .setMethod(GET)
                        .executeAs(JsonNode::toString) // TODO
                        .join();
            case 2: // CHANNEL
                assert ids.length == 1 : "Too " + cmpr(ids.length, 1) + " IDs were provided! [1]";

                return (T) new DiscordRequestImpl<>(discord)
                        .setUri(DiscordEndpoint.CHANNEL.createUri(ids[0]))
                        .setMethod(GET)
                        .executeAs(JsonNode::toString) // TODO + Testing
                        .join();
            case 3: // USER
                assert ids.length == 1 : "Too " + cmpr(ids.length, 1) + " IDs were provided! [1]";

                return (T) new DiscordRequestImpl<>(discord)
                        .setUri(DiscordEndpoint.USER.createUri(ids[0]))
                        .setMethod(GET)
                        .executeAs(JsonNode::toString) // TODO
                        .join();
            case 4: // ROLE
                assert ids.length == 2 : "Too " + cmpr(ids.length, 2) + " IDs were provided! [2]";

                return (T) new DiscordRequestImpl<>(discord)
                        .setUri(DiscordEndpoint.GUILD_ROLE_SPECIFIC.createUri(ids[0], ids[1]))
                        .setMethod(GET)
                        .executeAs(JsonNode::toString) // TODO
                        .join();
            case 5: // SERVER
                assert ids.length == 1 : "Too " + cmpr(ids.length, 1) + " IDs were provided! [1]";

                return (T) new DiscordRequestImpl<>(discord)
                        .setUri(DiscordEndpoint.GUILD_SPECIFIC.createUri(ids[0]))
                        .setMethod(GET)
                        .executeAs(JsonNode::toString) // TODO
                        .join();
            default: // error
                logger.error(new IllegalArgumentException("Unknown entity type: " + entityType));
                //noinspection ConstantConditions
                return null;
        }
    }

    private static String cmpr(int actual, int should) {
        return actual > should ? "many" : "few";
    }
}
