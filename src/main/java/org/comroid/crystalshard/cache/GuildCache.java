package org.comroid.crystalshard.cache;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import org.comroid.crystalshard.entity.guild.Guild;
import org.comroid.crystalshard.entity.message.Message;
import org.springframework.data.repository.CrudRepository;

@Table(name = "guilds")
@PersistenceContext(name = "cache")
public interface GuildCache extends CrudRepository<Guild, Long>{
}
