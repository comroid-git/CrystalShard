package org.comroid.crystalshard.entity.message;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.comroid.crystalshard.entity.DiscordEntity;

@Entity
@Table(name = "messages")
public class Message extends DiscordEntity {
}
