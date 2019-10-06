package de.kaleidox.crystalshard.api.model.message;

import java.util.Optional;

import de.kaleidox.crystalshard.api.entity.channel.Channel;
import de.kaleidox.crystalshard.api.entity.guild.Guild;
import de.kaleidox.crystalshard.api.entity.message.Message;
import de.kaleidox.crystalshard.util.model.serialization.JsonDeserializable;

public interface MessageReference extends JsonDeserializable { // todo deserialize
    Optional<Message> getMessage();
    
    Optional<Channel> getChannel();
    
    Optional<Guild> getGuild();
}
