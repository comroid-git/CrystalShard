package org.comroid.crystalshard.entity.channel;

import jakarta.persistence.Table;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.DiscordEntity;

@Table(name = "channels", schema = DiscordBot.CACHE_SCHEMA)
public class Channel extends DiscordEntity {
}
