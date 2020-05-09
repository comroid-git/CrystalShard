package org.comroid.crystalshard.entity.user;

import org.comroid.crystalshard.entity.Snowflake;
import org.comroid.crystalshard.model.Mentionable;
import org.comroid.crystalshard.model.message.MessageOperator;

public interface User extends Snowflake, Mentionable, MessageOperator {
}
