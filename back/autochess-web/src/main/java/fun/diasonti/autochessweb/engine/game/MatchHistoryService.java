package fun.diasonti.autochessweb.engine.game;

import fun.diasonti.autochessweb.data.entity.MatchHistory;
import fun.diasonti.autochessweb.data.entity.UserAccount;
import fun.diasonti.autochessweb.data.pojo.ActiveGame;
import fun.diasonti.autochessweb.engine.PlayerRankService;
import fun.diasonti.autochessweb.repository.MatchHistoryRepository;
import fun.diasonti.autochessweb.repository.UserAccountRepository;
import fun.diasonti.chessengine.data.Color;
import fun.diasonti.chessengine.util.BoardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MatchHistoryService {

    private final UserAccountRepository userAccountRepository;
    private final MatchHistoryRepository matchHistoryRepository;
    private final PlayerRankService playerRankService;

    @Autowired
    public MatchHistoryService(UserAccountRepository userAccountRepository, MatchHistoryRepository matchHistoryRepository, PlayerRankService playerRankService) {
        this.userAccountRepository = userAccountRepository;
        this.matchHistoryRepository = matchHistoryRepository;
        this.playerRankService = playerRankService;
    }


    @Transactional
    public void recordGameHistory(ActiveGame game) {
        final UserAccount whitePlayer = userAccountRepository.findById(game.getWhitePlayer().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        final UserAccount blackPlayer = userAccountRepository.findById(game.getBlackPlayer().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        final MatchHistory record = new MatchHistory();
        record.setActiveGame(game);
        record.setStartedAt(game.getStartedAt());
        record.setFinishedAt(game.getFinishedAt());
        record.setWhitePlayer(whitePlayer);
        record.setBlackPlayer(blackPlayer);
        fillGameResults(record);
        playerRankService.updateRank(whitePlayer.getId(), record.getWhiteRankDelta());
        playerRankService.updateRank(blackPlayer.getId(), record.getBlackRankDelta());
        matchHistoryRepository.save(record);
    }

    private void fillGameResults(MatchHistory record) {
        final ActiveGame game = record.getActiveGame();
        final String fen = BoardUtils.bitboardToFen(game.getBoard());

        final String whitePieces = fen.replaceAll("[prnbqk/1-8]", "");
        final String blackPieces = fen.replaceAll("[PRNBQK/1-8]", "");

        int whiteScore = 0;
        int blackScore = 0;
        for (char piece : whitePieces.toCharArray()) {
            whiteScore += getScore(piece);
        }
        for (char piece : blackPieces.toCharArray()) {
            blackScore += getScore(piece);
        }

        if (whiteScore > blackScore) {
            record.setWinner(Color.WHITE);
        } else if (whiteScore < blackScore) {
            record.setWinner(Color.BLACK);
        }

        record.setWhiteRankDelta(whiteScore - blackScore);
        record.setBlackRankDelta(blackScore - whiteScore);
    }

    private int getScore(char piece) {
        switch (piece) {
            case 'P':
            case 'p':
                return 10;
            case 'R':
            case 'r':
                return 7;
            case 'N':
            case 'n':
                return 5;
            case 'B':
            case 'b':
                return 3;
            case 'Q':
            case 'q':
                return 2;
            default:
                return 0;
        }
    }

}
