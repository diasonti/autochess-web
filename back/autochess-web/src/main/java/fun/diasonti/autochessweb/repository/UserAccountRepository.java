package fun.diasonti.autochessweb.repository;

import fun.diasonti.autochessweb.data.entity.UserAccount;
import fun.diasonti.autochessweb.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends BaseRepository<UserAccount> {
    Optional<UserAccount> findByUsername(String username);
}
