package org.comroid.cobalton.cmd;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.annotation.CommandMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminCommand {
    @Autowired
    public DiscordBot bot;

    @CommandMapping
    public String version() {
        return "v0.1";
    }
}
