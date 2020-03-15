package fun.diasonti.autochessweb.service;

import fun.diasonti.autochessweb.data.entity.MatchHistory_;
import fun.diasonti.autochessweb.data.view.MatchView;
import fun.diasonti.autochessweb.repository.MatchHistoryRepository;
import fun.diasonti.chessengine.data.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchViewService {

    private final MatchHistoryRepository matchHistoryRepository;

    @Autowired
    public MatchViewService(MatchHistoryRepository matchHistoryRepository) {
        this.matchHistoryRepository = matchHistoryRepository;
    }

    @Transactional(readOnly = true)
    public List<MatchView> getMatchHistory(String username, int page) {
        return matchHistoryRepository.findAllByWhitePlayerUsernameOrBlackPlayerUsername(username, username,
                PageRequest.of(page, 10, Sort.by(Sort.Order.desc(MatchHistory_.FINISHED_AT))))
                .stream()
                .map(match -> {
                    final MatchView view = new MatchView();
                    if (match.getWhitePlayer().getUsername().equals(username)) {
                        view.setColor(Color.WHITE);
                        view.setOpponentUsername(match.getBlackPlayer().getUsername());
                        view.setRankDelta(match.getWhiteRankDelta());
                    } else {
                        view.setColor(Color.BLACK);
                        view.setOpponentUsername(match.getWhitePlayer().getUsername());
                        view.setRankDelta(match.getBlackRankDelta());
                    }
                    view.setWinner(match.getWinner());
                    view.setFinishedAt(match.getFinishedAt());
                    return view;
                })
                .collect(Collectors.toList());
    }
}

