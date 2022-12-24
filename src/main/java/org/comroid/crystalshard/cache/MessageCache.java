package org.comroid.crystalshard.cache;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import org.comroid.crystalshard.entity.message.Message;
import org.springframework.data.repository.CrudRepository;

@Table(name = "messages")
@PersistenceContext(name = "cache")
public interface MessageCache extends CrudRepository<Message, Long>{
}
