package org.comroid.crystalshard.model;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.uniform.node.UniObjectNode;
import org.comroid.varbind.container.DataContainerBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface BotBound {
    DiscordBot getBot();

    abstract class DataBase extends DataContainerBase<DiscordBot> implements BotBound {
        protected DataBase(@NotNull DiscordBot dependencyObject, @Nullable UniObjectNode initialData) {
            super(initialData, Objects.requireNonNull(dependencyObject));
        }

        @Override
        public final DiscordBot getBot() {
            return getDependent();
        }
    }
}
