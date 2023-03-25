package org.comroid.crystalshard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity // included for IDE features
public abstract class DiscordEntity {
    @Id
    public long ID;
}
