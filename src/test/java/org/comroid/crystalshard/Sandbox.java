package org.comroid.crystalshard;

import org.comroid.common.io.FileHandle;

public final class Sandbox extends DiscordBot {
    public static final FileHandle DIR = new FileHandle("/srv/dcb/tester/", true);
    public static final FileHandle LOGIN = DIR.createSubDir("login");

    @Override
    protected String getToken() {
        return LOGIN.createSubFile("discord.cred").getContent(true);
    }

    protected Sandbox(DiscordAPI context) {
        super(context);
    }

    public static void main(String[] args) {
    }
}
