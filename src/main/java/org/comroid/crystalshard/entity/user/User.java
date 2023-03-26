package org.comroid.crystalshard.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.comroid.crystalshard.DiscordBot;
import org.comroid.crystalshard.entity.DiscordEntity;

@Entity
@Table(name = "users", schema = DiscordBot.CACHE_SCHEMA)
public class User extends DiscordEntity {
}
