package de.kaleidox.crystalshard.util;

import com.fasterxml.jackson.databind.JsonNode;
import de.kaleidox.crystalshard.logging.Logger;
import de.kaleidox.crystalshard.main.Discord;
import de.kaleidox.crystalshard.util.command.CommandFramework;
import de.kaleidox.crystalshard.util.command.CommandFrameworkImpl;
import de.kaleidox.util.helpers.JsonHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class DiscordUtilsImpl {
    private final static Logger logger = new Logger(DiscordUtilsImpl.class);
    private final static JsonNode configuration;
    private final static String configFile = "/discordutils_settings.json";

    static {
        // Get or Create configuration node
        InputStream configStream = ClassLoader.getSystemResourceAsStream(configFile);
        if (configStream != null) {
            Scanner s = new Scanner(configStream).useDelimiter("\\A");
            configuration = (s.hasNext() ? JsonHelper.parse(s.next()) : createDefaultConfig());
            try {
                configStream.close();
            } catch (IOException e) {
                logger.exception(e, "Error closing DiscordUtils initialization file stream.");
            }
        } else configuration = createDefaultConfig();
    }

    private final DefaultEmbed defaultEmbed;
    private final Discord discord;
    private final CommandFramework commandFramework;

    public DiscordUtilsImpl(Discord discord) {
        this.discord = discord;

        commandFramework = new CommandFrameworkImpl(discord,
                configuration.path("commands")
                        .path("prefix")
                        .asText("!"),
                configuration.path("commands")
                        .path("enable_default_help")
                        .asBoolean(true));

        defaultEmbed = new DefaultEmbedImpl(discord, configuration.has("default_embd") ? configuration.path("default_embed") : JsonHelper.nodeOf(null));
    }

    private static JsonNode createDefaultConfig() {
        logger.info("No configuration file " + configFile + " found at resources root. Using default configuration.");
        return JsonHelper.objectNode("commands", JsonHelper.objectNode("prefix", "!", "enable_default_help", true), "default_embed", JsonHelper.nodeOf(null));
    }

    public DefaultEmbed getDefaultEmbed() {
        return defaultEmbed;
    }

    public CommandFramework getCommandFramework() {
        return commandFramework;
    }

    public Discord getDiscord() {
        return discord;
    }
}
