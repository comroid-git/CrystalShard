package org.comroid.crystalshard.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.comroid.crystalshard.entity.DiscordEntity;

@Entity
@Table(name = "users")
public class User extends DiscordEntity {
}
