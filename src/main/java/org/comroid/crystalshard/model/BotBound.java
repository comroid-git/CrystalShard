package org.comroid.crystalshard.model;

import org.comroid.crystalshard.CrystalShard;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.container.DataContainerBase;

import java.util.Objects;

public interface BotBound {
    DiscordBot getBot();

    abstract class Base implements BotBound {
        private final DiscordBot bot;

        @Override
        public final DiscordBot getBot() {
            return bot;
        }

        protected Base(DiscordBot bot) {
            this.bot = bot;
        }
    }

    abstract class DataBase extends DataContainerBase<DiscordBot> implements BotBound {
        public static final GroupBind<DataBase, DiscordBot> BaseGroup
                = new GroupBind<>(CrystalShard.SERIALIZATION_ADAPTER, "crystalshard-base");

        protected DataBase(DiscordBot bot, UniObjectNode initialData) {
            super(initialData, Objects.requireNonNull(bot, "Bot Object missing"));
        }

        @Override
        public final DiscordBot getBot() {
            return getDependent();
        }
    }
}
