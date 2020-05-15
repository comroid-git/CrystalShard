package org.comroid.crystalshard.entity.channel;

import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.model.message.MessageOperator;
import org.comroid.uniform.node.UniValueNode.ValueType;
import org.comroid.varbind.bind.GroupBind;
import org.comroid.varbind.bind.VarBind;

import java.time.Instant;
import java.util.Optional;

public interface TextChannel extends Channel, MessageOperator {
    default Optional<Message> getLastMessage() {
        return wrap(Bind.LastMessage);
    }

    default Optional<Instant> getLastPinTimestamp() {
        return wrap(Bind.LastPinTimestamp);
    }

    interface Bind extends Channel.Bind {
        GroupBind<TextChannel, DiscordBot> Root
                = Channel.Bind.Root.subGroup("text_channel");
        VarBind.DependentTwoStage<Long, DiscordBot, Message> LastMessage
                = Root.bindDependent("last_message_id", ValueType.LONG, (bot, id) -> bot.getMessageByID(id).get());
        VarBind.TwoStage<String, Instant> LastPinTimestamp
                = Root.bind2stage("last_pin_timestamp", ValueType.STRING, Instant::parse);
    }
}
