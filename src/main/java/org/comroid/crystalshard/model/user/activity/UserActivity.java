package org.comroid.crystalshard.model.user.activity;

import org.comroid.api.IntEnum;
import org.comroid.api.Polyfill;
import org.comroid.api.Named;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.model.BotBound;
import org.comroid.uniform.ValueType;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.annotation.RootBind;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

import java.net.URL;

public final class UserActivity extends BotBound.DataBase {
    @RootBind
    public static final GroupBind<DataBase, DiscordBot> Root
            = BaseGroup.rootGroup("user-activity");
    public static final VarBind<Object, String, String, String> name
            = Root.createBind("name")
            .extractAs(ValueType.STRING)
            .asIdentities()
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Object, Integer, Type, Type> type
            = Root.createBind("type")
            .extractAs(ValueType.INTEGER)
            .andRemap(Type::valueOf)
            .onceEach()
            .setRequired()
            .build();
    public static final VarBind<Object, String, URL, URL> url
            = Root.createBind("url")
            .extractAs(ValueType.STRING)
            .andRemap(Polyfill::url)
            .onceEach()
            .build();

    public UserActivity(DiscordBot bot, UniObjectNode initialData) {
        super(bot, initialData);
    }

    public enum Type implements IntEnum, Named {
        GAME(0),
        STREAMING(1),
        LISTENING(2),

        CUSTOM(4);

        private final int value;

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public String getName() {
            return name();
        }

        Type(int value) {
            this.value = value;
        }

        public static Type valueOf(int value) {
            for (Type type : values()) {
                if (type.value == value)
                    return type;
            }

            return null;
        }
    }
}
