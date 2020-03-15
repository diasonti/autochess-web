package fun.diasonti.autochessweb.service;

import fun.diasonti.autochessweb.data.entity.MatchHistory;
import fun.diasonti.autochessweb.data.view.PlayerView;
import fun.diasonti.autochessweb.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerViewService {

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public PlayerViewService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Transactional(readOnly = true)
    public PlayerView getByUsername(String username) {
        return userAccountRepository.findByUsername(username)
                .map(userAccount -> {
                    final PlayerView view = new PlayerView();

                    view.setUsername(userAccount.getUsername());

                    int rank = 0;
                    rank = userAccount.getWhiteMatchHistory().stream()
                            .mapToInt(MatchHistory::getWhiteRankDelta)
                            .reduce(rank, Integer::sum);
                    rank = userAccount.getBlackMatchHistory().stream()
                            .mapToInt(MatchHistory::getBlackRankDelta)
                            .reduce(rank, Integer::sum);
                    view.setRank(Math.max(rank, 0));

                    return view;
                }).orElse(null);
    }
}
