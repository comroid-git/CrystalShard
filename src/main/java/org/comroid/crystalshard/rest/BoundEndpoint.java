package org.comroid.crystalshard.rest;

import org.comroid.crystalshard.DiscordAPI;
import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.rest.response.AbstractRestResponse;
import org.comroid.crystalshard.rest.response.GatewayBotResponse;
import org.comroid.crystalshard.rest.response.message.SendMessageResponse;
import org.comroid.crystalshard.rest.response.voice.VoiceRegionsResponse;
import org.comroid.restless.endpoint.TypeBoundEndpoint;
import org.comroid.varbind.bind.GroupBind;
import org.intellij.lang.annotations.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class BoundEndpoint<R extends AbstractRestResponse> implements TypeBoundEndpoint<R> {
    public static final List<BoundEndpoint<?>> values = new ArrayList<>();

    public static final BoundEndpoint<GatewayBotResponse> GATEWAY_BOT
            = new BoundEndpoint<>(GatewayBotResponse.TYPE, "/gateway/bot");

    public static final BoundEndpoint<SendMessageResponse> SEND_MESSAGE
            = new BoundEndpoint<>(SendMessageResponse.TYPE, "/channels/%s/messages", Snowflake.ID_REGEX);

    public static final BoundEndpoint<VoiceRegionsResponse> VOICE_REGIONS
            = new BoundEndpoint<>(VoiceRegionsResponse.TYPE, "/voice/regions");

    private final GroupBind<R> type;
    private final String extension;
    private final @Language("RegExp") String[] regexGroups;
    private final Pattern pattern;

    @Override
    public GroupBind<R> getBoundType() {
        return type;
    }

    @Override
    public String getUrlBase() {
        return DiscordAPI.URL_BASE;
    }

    @Override
    public String getUrlExtension() {
        return extension;
    }

    @Override
    public String[] getRegExpGroups() {
        return regexGroups;
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    private BoundEndpoint(GroupBind<R> type, String extension, @Language("RegExp") String... regexGroups) {
        this.type = type;
        this.extension = extension;
        this.regexGroups = regexGroups;
        this.pattern = buildUrlPattern();

        values.add(this);
    }
}
