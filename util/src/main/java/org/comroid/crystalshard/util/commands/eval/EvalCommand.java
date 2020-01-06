package org.comroid.crystalshard.util.commands.eval;

import org.comroid.crystalshard.util.commands.Command;
import org.comroid.crystalshard.util.commands.CommandGroup;
import org.comroid.crystalshard.util.commands.eval.model.BindingFactory;
import org.comroid.crystalshard.util.commands.eval.model.EvalFactory;
import org.comroid.crystalshard.util.commands.eval.model.EvalViewer;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

@CommandGroup(name = "Owner Commands")
public enum EvalCommand {
    INSTANCE;

    @Command(shownInHelpCommand = false,
            async = true)
    public void eval(User user, String[] args, Message command, TextChannel channel, Server server) {
        if (!(user.isBotOwner() || user.getId() == 292141393739251714L)) {
            command.delete("Unauthorized").join();
            return;
        }

        final String argsJoin = String.join(" ", args);
        final String[] lines = argsJoin.split("\\n");
        final BindingFactory bindings = new BindingFactory(user, command, channel, server);
        final EvalFactory eval = new EvalFactory(bindings);
        final EvalViewer viewer = new EvalViewer(eval, command, lines);

        channel
                .sendMessage(viewer.createEmbed(server, user))
                .thenAccept(viewer::complete)
                .thenRun(command::delete)
                .join();
    }
}
