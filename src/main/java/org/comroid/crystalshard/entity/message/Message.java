package org.comroid.crystalshard.entity.message;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.DiscordEntity;

@Entity
@Table(name = "messages", schema = DiscordBot.CACHE_SCHEMA)
public class Message extends DiscordEntity {
}
