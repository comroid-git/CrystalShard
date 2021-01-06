package org.comroid.crystalshard;

import org.comroid.common.io.FileHandle;
import org.comroid.restless.adapter.java.JavaHttpAdapter;
import org.comroid.uniform.adapter.json.fastjson.FastJSONLib;

public final class Sandbox extends DiscordBotShard {
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
        DiscordAPI.SERIALIZATION = FastJSONLib.fastJsonLib;
        DiscordAPI api = new DiscordAPI(new JavaHttpAdapter());

        Sandbox sandbox = new Sandbox(api);
    }
}
