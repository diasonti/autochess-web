package fun.diasonti.autochessweb.repository;

import fun.diasonti.autochessweb.data.entity.MatchHistory;
import fun.diasonti.autochessweb.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchHistoryRepository extends BaseRepository<MatchHistory> {
    Page<MatchHistory> findAllByWhitePlayerUsernameOrBlackPlayerUsername(String wUsername, String bUsername, Pageable pageable);
}
