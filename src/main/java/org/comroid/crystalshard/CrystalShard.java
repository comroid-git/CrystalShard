package org.comroid.crystalshard;

import org.comroid.crystalshard.event.BotEvent;
import org.comroid.crystalshard.event.guild.GuildEvent;
import org.comroid.crystalshard.event.message.MessageEvent;
import org.comroid.crystalshard.event.message.OptionalMessageEvent;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.restless.HttpAdapter;
import org.comroid.uniform.SerializationAdapter;
import org.comroid.uniform.adapter.http.jdk.JavaHttpAdapter;
import org.comroid.uniform.adapter.json.fastjson.FastJSONLib;

public final class CrystalShard {
    public static final ThreadGroup THREAD_GROUP = new ThreadGroup("CrystalShard");
    public static HttpAdapter HTTP_ADAPTER = new JavaHttpAdapter();
    public static SerializationAdapter<?, ?, ?> SERIALIZATION_ADAPTER = FastJSONLib.fastJsonLib;

    public static final class EventCarrier implements BotBound {
        public final BotEvent.Container Bot;

        public final GuildEvent.Container Guild;

        public final MessageEvent.Container Message;
        public final OptionalMessageEvent.Container OptionalMessage;

        private final DiscordBot bot;

        @Override
        public DiscordBot getBot() {
            return bot;
        }

        public EventCarrier(DiscordBot bot) {
            this.bot = bot;

            this.Bot = new BotEvent.Container(bot);
            this.Guild = new GuildEvent.Container(bot);
            this.Message = new MessageEvent.Container(bot);
            this.OptionalMessage = new OptionalMessageEvent.Container(bot);
        }
    }
}
