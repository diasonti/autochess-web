package fun.diasonti.autochessweb.controller.api;

import fun.diasonti.autochessweb.config.security.data.AppUser;
import fun.diasonti.autochessweb.data.pojo.*;
import fun.diasonti.autochessweb.engine.MatchmakingService;
import fun.diasonti.autochessweb.engine.game.GameService;
import fun.diasonti.autochessweb.service.PlayerViewService;
import fun.diasonti.chessengine.data.Color;
import fun.diasonti.chessengine.util.BoardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fetch")
public class StateFetchController {

    private final GameService gameService;
    private final PlayerViewService playerViewService;
    private final MatchmakingService matchmakingService;

    @Autowired
    public StateFetchController(GameService gameService, PlayerViewService playerViewService, MatchmakingService matchmakingService) {
        this.gameService = gameService;
        this.playerViewService = playerViewService;
        this.matchmakingService = matchmakingService;
    }

    @GetMapping("/state")
    public PlayerState fetchState(AppUser user) {
        if (user == null) {
            return AnonymousPlayerState.getInstance();
        }
        final ActiveGame game = gameService.findActiveGameByPlayer(user.getUsername());
        if (game != null) {
            return getGameState(user, game);
        }
        return getMenuState(user);
    }

    private InMenuPlayerState getMenuState(AppUser user) {
        final InMenuPlayerState state = new InMenuPlayerState();
        state.setProfile(playerViewService.getByUsername(user.getUsername()));
        state.setSearch(matchmakingService.getStatus(user.getUsername()));
        state.setStatistics(new GlobalStatistics());
        return state;
    }

    private InGamePlayerState getGameState(AppUser user, ActiveGame game) {
        final InGamePlayerState state = new InGamePlayerState();
        state.setStage(game.getState());
        state.setBoard(BoardUtils.bitboardToFen(game.getBoard()));
        if (game.getWhitePlayer().getUsername().equals(user.getUsername())) {
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
