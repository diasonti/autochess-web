package fun.diasonti.autochessweb.service;

import fun.diasonti.autochessweb.data.entity.MatchHistory;
import fun.diasonti.autochessweb.data.entity.UserAccount;
import fun.diasonti.autochessweb.data.enums.GameResult;
import fun.diasonti.autochessweb.data.pojo.FinishedGame;
import fun.diasonti.autochessweb.data.view.PlayerView;
import fun.diasonti.autochessweb.repository.UserAccountRepository;
import fun.diasonti.chessengine.data.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class PlayerViewService {

    private final UserAccountRepository userAccountRepository;
    private final MatchViewService matchViewService;

    @Autowired
    public PlayerViewService(UserAccountRepository userAccountRepository, MatchViewService matchViewService) {
        this.userAccountRepository = userAccountRepository;
        this.matchViewService = matchViewService;
    }

    @Transactional(readOnly = true)
    public PlayerView getByUsername(String username) {
        return userAccountRepository.findByUsername(username)
                .map(userAccount -> {
                    final PlayerView view = new PlayerView();
                    view.setUsername(userAccount.getUsername());
                    view.setRank(userAccount.getRank());
                    view.setMatchHistory(matchViewService.getMatchHistory(username, 0));
                    return view;
                }).orElse(null);
    }

    @Transactional(readOnly = true)
    public FinishedGame getRecentlyFinishedGameView(String username) {
        final UserAccount userAccount = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        final MatchHistory lastMatch = userAccount.getLastFinishedMatch();
        if (lastMatch == null || lastMatch.getFinishedAt().until(LocalDateTime.now(), ChronoUnit.MINUTES) > 1)
            return null;

        final FinishedGame view = new FinishedGame();

        view.setResult(GameResult.LOSE);
        if (lastMatch.getWinner() == null) {
            view.setResult(GameResult.TIE);
        }
        if (lastMatch.getWhitePlayer().equals(userAccount)) {
            if (lastMatch.getWinner() == Color.WHITE) {
                view.setResult(GameResult.WIN);
            }
            view.setRankBefore(userAccount.getRank() - lastMatch.getWhiteRankDelta());
        } else {
            if (lastMatch.getWinner() == Color.BLACK) {
                view.setResult(GameResult.WIN);
            }
            view.setRankBefore(userAccount.getRank() - lastMatch.getBlackRankDelta());
        }
        view.setRankAfter(userAccount.getRank());

        return view;
    }
}
