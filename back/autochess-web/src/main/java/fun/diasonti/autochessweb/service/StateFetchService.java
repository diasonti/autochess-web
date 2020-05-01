package fun.diasonti.autochessweb.service;

import fun.diasonti.autochessweb.data.pojo.ActiveGame;
import fun.diasonti.autochessweb.data.pojo.GlobalStatistics;
import fun.diasonti.autochessweb.data.pojo.playerstate.InGamePlayerState;
import fun.diasonti.autochessweb.data.pojo.playerstate.InMenuPlayerState;
import fun.diasonti.autochessweb.data.pojo.playerstate.PlayerState;
import fun.diasonti.autochessweb.engine.MatchmakingService;
import fun.diasonti.autochessweb.engine.game.GameService;
import fun.diasonti.chessengine.data.Color;
import fun.diasonti.chessengine.util.BoardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StateFetchService {

    private final GameService gameService;
    private final PlayerViewService playerViewService;
    private final MatchmakingService matchmakingService;

    @Autowired
    public StateFetchService(GameService gameService, PlayerViewService playerViewService, MatchmakingService matchmakingService) {
        this.gameService = gameService;
        this.playerViewService = playerViewService;
        this.matchmakingService = matchmakingService;
    }

    @Transactional(readOnly = true)
    public PlayerState fetchState(String username) {
        final ActiveGame game = gameService.findActiveGameByPlayer(username);
        if (game != null) {
            return getGameState(username, game);
        }
        return getMenuState(username);
    }

    private InMenuPlayerState getMenuState(String username) {
        final InMenuPlayerState state = new InMenuPlayerState();
        state.setProfile(playerViewService.getByUsername(username));
        state.setSearch(matchmakingService.getStatus(username));
        state.setFinishedGame(playerViewService.getRecentlyFinishedGameView(username));
        state.setStatistics(new GlobalStatistics());
        return state;
    }

    private InGamePlayerState getGameState(String username, ActiveGame game) {
        final InGamePlayerState state = new InGamePlayerState();
        state.setStage(game.getState());
        state.setBoard(BoardUtils.bitboardToFen(game.getBoard()));
        if (game.getWhitePlayer().getUsername().equals(username)) {
            state.setColor(Color.WHITE);
            state.setMovablePieces(game.getWhitePieces());
            state.setOpponentProfile(playerViewService.getByUsername(game.getBlackPlayer().getUsername()));
        } else {
            state.setColor(Color.BLACK);
            state.setMovablePieces(game.getBlackPieces());
            state.setOpponentProfile(playerViewService.getByUsername(game.getWhitePlayer().getUsername()));
        }
        return state;
    }

}
