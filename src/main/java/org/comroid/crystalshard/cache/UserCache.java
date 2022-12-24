package org.comroid.crystalshard.cache;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import org.comroid.crystalshard.entity.message.Message;
import org.comroid.crystalshard.entity.user.User;
import org.springframework.data.repository.CrudRepository;

@Table(name = "users")
@PersistenceContext(name = "cache")
public interface UserCache extends CrudRepository<User, Long>{
}
