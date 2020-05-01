package fun.diasonti.autochessweb.engine;

import fun.diasonti.autochessweb.data.entity.MatchHistory;
import fun.diasonti.autochessweb.data.entity.UserAccount;
import fun.diasonti.autochessweb.repository.MatchHistoryRepository;
import fun.diasonti.autochessweb.repository.UserAccountRepository;
import fun.diasonti.autochessweb.utils.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class PlayerRankService {

    private final UserAccountRepository userAccountRepository;
    private final MatchHistoryRepository matchHistoryRepository;

    @Autowired
    public PlayerRankService(UserAccountRepository userAccountRepository, MatchHistoryRepository matchHistoryRepository) {
        this.userAccountRepository = userAccountRepository;
        this.matchHistoryRepository = matchHistoryRepository;
    }

    @Transactional
    public void updateRank(long userAccountId, int delta) {
        final UserAccount player = userAccountRepository.findById(userAccountId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int rank = player.getRank();
        rank += delta;
        if (rank < 0)
            rank = 0;
        player.setRank(rank);
    }

    @Transactional
    public void recalculateRank(long userAccountId) {
        final UserAccount player = userAccountRepository.findById(userAccountId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        final List<MatchHistory> whiteMatches = matchHistoryRepository.findByWhitePlayerOrderByFinishedAt(player);
        final List<MatchHistory> blackMatches = matchHistoryRepository.findByBlackPlayerOrderByFinishedAt(player);
        final List<MatchHistory> allMatches = ListUtils.sortingMerge(whiteMatches, blackMatches, Comparator.comparing(MatchHistory::getFinishedAt));

        int rank = 0;
        for (MatchHistory match : allMatches) {
            int delta;
            if (match.getWhitePlayer().equals(player)) {
                delta = match.getWhiteRankDelta();
            } else {
                delta = match.getBlackRankDelta();
            }
            rank += delta;
            if (rank < 0)
                rank = 0;
        }

        player.setRank(rank);
    }
}
