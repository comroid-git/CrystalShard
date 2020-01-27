package org.comroid.test.crystalshard;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.comroid.crystalshard.api.Discord;
import org.comroid.crystalshard.api.entity.channel.TextChannel;
import org.comroid.crystalshard.api.entity.message.Message;
import org.comroid.crystalshard.util.commands.Command;
import org.comroid.crystalshard.util.commands.CommandGroup;
import org.comroid.crystalshard.util.commands.CommandHandler;
import org.comroid.crystalshard.util.server.properties.PropertyGroup;
import org.comroid.crystalshard.util.server.properties.ServerPropertiesManager;
import org.comroid.crystalshard.util.ui.embed.DefaultEmbedFactory;
import org.comroid.crystalshard.util.ui.messages.categorizing.CategorizedEmbed;
import org.comroid.crystalshard.util.ui.reactions.InfoReaction;

public class CrystalShardTest {
    public static void main(String[] args) throws Exception {
        Discord api = new Discord.Builder()
                .setToken(args[0])
                .build()
                .join();
    }
}