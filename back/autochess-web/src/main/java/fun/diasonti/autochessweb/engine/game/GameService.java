package fun.diasonti.autochessweb.engine.game;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import fun.diasonti.autochessweb.data.enums.ActiveGameState;
import fun.diasonti.autochessweb.data.form.UserAccountForm;
import fun.diasonti.autochessweb.data.pojo.ActiveGame;
import fun.diasonti.autochessweb.data.pojo.MovablePiece;
import fun.diasonti.chessengine.data.ChessBoard;
import fun.diasonti.chessengine.data.Color;
import fun.diasonti.chessengine.data.Move;
import fun.diasonti.chessengine.engine.interfaces.MoveEngine;
import fun.diasonti.chessengine.engine.interfaces.SearchEngine;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GameService {

    private final static Logger log = LoggerFactory.getLogger(GameService.class);
    private final static Map<String, ActiveGame> activeGamesById = Collections.synchronizedMap(new HashMap<>());
    private final static Map<String, ActiveGame> activeGamesByUsername = Collections.synchronizedMap(new HashMap<>());
    private final static ExecutorService executor = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("game-thread-%d").build());

    private final SearchEngine searchEngine;
    private final MoveEngine moveEngine;

    @Autowired
    public GameService(SearchEngine searchEngine, MoveEngine moveEngine) {
        this.searchEngine = searchEngine;
        this.moveEngine = moveEngine;
    }

    public ActiveGame findActiveGameByPlayer(String username) {
        return activeGamesByUsername.get(username);
    }

    public void attemptMovePiece(String username, int fromCell, int targetCell) {
        final ActiveGame game = activeGamesByUsername.get(username);
        if (game == null || game.getState() != ActiveGameState.PLACEMENT)
            return;
        MovablePiece movablePiece = null;
        List<MovablePiece> playersPieces = Collections.emptyList();
        if (game.getWhitePlayer().getUsername().equals(username)) {
            playersPieces = game.getWhitePieces();
        } else if (game.getBlackPlayer().getUsername().equals(username)) {
            playersPieces = game.getBlackPieces();
        }
        movablePiece = playersPieces.stream().filter(p -> p.getPosition() == fromCell).findFirst().orElse(null);
        if (movablePiece != null) {
            log.debug("Move: {} to {}; gameId: {}", movablePiece.getPosition(), targetCell, game.getId());
            final Move move = Move.fromIndex(movablePiece.getPosition(), targetCell);
            final ChessBoard boardAfterMove = moveEngine.makeMove(game.getBoard(), move);
            game.setBoard(boardAfterMove);
            movablePiece.setPosition(targetCell);
        }
    }

    public String startGameAsync(UserAccountForm player1, UserAccountForm player2) {
        final String gameId = UUID.randomUUID().toString();
        executor.execute(() -> {
            try {
                doStartGame(gameId, player1, player2);
            } catch (Exception ignored) {
            } finally {
                activeGamesById.remove(gameId);
                activeGamesByUsername.remove(player1.getUsername());
                activeGamesByUsername.remove(player2.getUsername());
            }
        });
        return gameId;
    }

    private void doStartGame(String gameId, UserAccountForm player1, UserAccountForm player2) throws InterruptedException {
        final ActiveGame game = new ActiveGame();

        log.debug("Game state: {}; gameId: {}", ActiveGameState.INITIALIZATION, gameId);
        game.setState(ActiveGameState.INITIALIZATION);
        final List<UserAccountForm> players = Stream.of(player1, player2).collect(Collectors.toList());
        Collections.shuffle(players);
        game.setWhitePlayer(players.get(0));
        game.setBlackPlayer(players.get(1));
        activeGamesById.put(gameId, game);
        activeGamesByUsername.put(game.getWhitePlayer().getUsername(), game);
        activeGamesByUsername.put(game.getBlackPlayer().getUsername(), game);
        game.setBoard(new ChessBoard());
        game.setWhitePieces(RandomStringUtils.random(5, "RNBQP"));
        game.setBlackPieces(RandomStringUtils.random(5, "rnbqp"));
        game.setupStartingBoard();

        log.debug("Game state: {}; gameId: {}", ActiveGameState.PRE_PLACEMENT, gameId);
        game.setState(ActiveGameState.PRE_PLACEMENT);
        Thread.sleep(Duration.ofSeconds(5).toMillis());

        log.debug("Game state: {}; gameId: {}", ActiveGameState.PLACEMENT, gameId);
        game.setState(ActiveGameState.PLACEMENT);
        Thread.sleep(Duration.ofSeconds(25).toMillis());

        log.debug("Game state: {}; gameId: {}", ActiveGameState.GAME, gameId);
        game.setState(ActiveGameState.GAME);
        doAutoGame(game);

        log.debug("Game state: {}; gameId: {}", ActiveGameState.FINISHED, gameId);
        game.setState(ActiveGameState.FINISHED);
        flushGameHistory(game);
    }

    private void doAutoGame(ActiveGame game) throws InterruptedException {
        final int turnLimit = 10;
        Color turn = Color.WHITE;
        for (int i = 0; i < turnLimit; i++) {
            final int depth = RandomUtils.nextInt(1, 5);
            ChessBoard board = game.getBoard();
            final Move move = searchEngine.getBestMove(board, turn, depth);
            board = moveEngine.makeMove(board, move);
            game.setBoard(board);
            turn = turn.getOpposite();
            Thread.sleep(Duration.ofSeconds(1).toMillis());
        }
    }

    private void flushGameHistory(ActiveGame game) {

    }
}
