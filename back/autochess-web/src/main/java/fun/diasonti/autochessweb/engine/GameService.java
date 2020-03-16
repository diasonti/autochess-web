package fun.diasonti.autochessweb.engine;

import com.google.common.collect.Maps;
import fun.diasonti.autochessweb.data.enums.ActiveGameState;
import fun.diasonti.autochessweb.data.form.UserAccountForm;
import fun.diasonti.autochessweb.data.pojo.ActiveGame;
import fun.diasonti.chessengine.data.ChessBoard;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GameService {

    private final Map<String, ActiveGame> activeGames = Maps.newConcurrentMap();
    private final ExecutorService worker = Executors.newCachedThreadPool();

    private final ConnectionService connectionService;

    @Autowired
    public GameService(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    public String startGameAsync(UserAccountForm player1, UserAccountForm player2) {
        final String gameId = UUID.randomUUID().toString();
        worker.execute(() -> {
            try {
                doStartGame(gameId, player1, player2);
            } catch (Exception e) {
                activeGames.remove(gameId);
            }
        });
        return gameId;
    }

    private void doStartGame(String gameId, UserAccountForm player1, UserAccountForm player2) throws InterruptedException {
        final ActiveGame game = new ActiveGame();
        game.setState(ActiveGameState.CREATED);
        game.setBoard(new ChessBoard());
        final List<UserAccountForm> players = Stream.of(player1, player2).collect(Collectors.toList());
        Collections.shuffle(players);
        game.setWhitePlayer(players.get(0));
        game.setBlackPlayer(players.get(1));
        game.setWhitePieces(RandomStringUtils.random(5, "RNBQP"));
        game.setBlackPieces(RandomStringUtils.random(5, "rnbqp"));
        activeGames.put(gameId, game);

        game.setState(ActiveGameState.PREPARED);
        connectionService.sendGamePrepared(game);
        Thread.sleep(Duration.ofSeconds(5).toMillis());

        game.setState(ActiveGameState.PLACEMENT);
        connectionService.sendPlacementStarted(game);
        Thread.sleep(Duration.ofSeconds(25).toMillis());

        game.setState(ActiveGameState.AUTO_GAME);
        connectionService.sendPlacementEnded(game);
        doAutoGame(game);

        game.setState(ActiveGameState.FINISHED);
        flushGameHistory(game);
        activeGames.remove(gameId);
    }

    private void doAutoGame(ActiveGame game) {

    }

    private void flushGameHistory(ActiveGame game) {

    }

}
