package de.comroid.test;

import java.io.File;
import java.util.concurrent.TimeUnit;

import de.comroid.crystalshard.util.commands.Command;
import de.comroid.crystalshard.util.commands.CommandGroup;
import de.comroid.crystalshard.util.commands.CommandHandler;
import de.comroid.crystalshard.util.server.properties.PropertyGroup;
import de.comroid.crystalshard.util.server.properties.ServerPropertiesManager;
import de.comroid.crystalshard.util.ui.embed.DefaultEmbedFactory;
import de.comroid.crystalshard.util.ui.messages.categorizing.CategorizedEmbed;
import de.comroid.crystalshard.util.ui.reactions.InfoReaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;

public class Main {
    public static void main(String[] args) throws Exception {
        DiscordApi api = new DiscordApiBuilder()
                .setToken(args[0])
                .login()
                .join();

        CommandHandler cmd = new CommandHandler(api);

        cmd.registerCommands(new GroupC());
        cmd.registerCommands(new GroupA());
        cmd.registerCommands(new GroupB());

        cmd.useDefaultHelp(null);

        ServerPropertiesManager prop = new ServerPropertiesManager(new File("props/runProps.json"));

        prop.usePropertyCommand(null, cmd);

        prop.register("isPublic", false);

        PropertyGroup prefix = prop.register("bot.prefix", "!");

        cmd.withCommandChannelProvider(id -> 412663733064695832L);
        cmd.withCustomPrefixProvider(prop.register("prefix", "!").function(String.class));
    }

    public static class GroupA {
        @Command
        public Object aOne(TextChannel tc) {
            CategorizedEmbed embed = new CategorizedEmbed(tc);

            embed.addCategory("cat1", "first category")
                    .addField("field 1", "first field")
                    .addField("field 2", "second field");

            embed.addCategory("cat2", "second category")
                    .addField("field 1b", "first field")
                    .addField("field 2b", "second field");

            return embed;
        }

        @Command(minimumArguments = 1, maximumArguments = 3)
        public String aTwo(String[] args) {
            return "args.length = " + args.length;
        }
    }

    @CommandGroup(description = "detroit become humid")
    public static class GroupB {
        @Command(minimumArguments = 2)
        public String bOne(String[] args) {
            return "args.length = " + args.length;
        }

        @Command
        public String bTwo(Command.Parameters cmd) {
            return cmd.getCommand().method.toString();
        }
    }

    @CommandGroup
    public static class GroupC {
        @Command
        public Object cOne(Message message) {
            return new InfoReaction(
                    message,
                    InfoReaction.MessageTypeEmoji.WARNING,
                    "abc",
                    10,
                    TimeUnit.SECONDS,
                    DefaultEmbedFactory.INSTANCE
            );
        }

        @Command(useTypingIndicator = true)
        public String cTwo() throws Exception {
            Thread.sleep(3000);
            return "okay!";
        }
    }
}
