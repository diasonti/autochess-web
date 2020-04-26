package fun.diasonti.autochessweb.engine;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import fun.diasonti.autochessweb.config.security.data.AppUser;
import fun.diasonti.autochessweb.data.enums.ActiveGameState;
import fun.diasonti.autochessweb.data.form.UserAccountForm;
import fun.diasonti.autochessweb.data.pojo.ActiveGame;
import fun.diasonti.autochessweb.data.pojo.MovablePiece;
import fun.diasonti.chessengine.util.BoardUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
public class ConnectionService {

    private static final Logger log = LoggerFactory.getLogger(ConnectionService.class);
    private static final Executor executor = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("con-thread-%d").build());

    private static final String EVENT_KEEP_ALIVE = "keep.alive";
    private static final String EVENT_SEARCH_STARTED = "search.started";
    private static final String EVENT_SEARCH_STOPPED = "search.stopped";
    private static final String EVENT_GAME_FOUND = "game.found";

    private static final String DATA_EMPTY = "{}";
    private static final String DATA_GAME_ID_KEY = "gameId";

    private final Map<String, SseEmitter> connections = Maps.newConcurrentMap();

    public void registerConnection(AppUser user, SseEmitter emitter) {
        connections.put(user.getUsername(), emitter);
        log.debug("Connection added for {}", user);
        emitter.onCompletion(() -> {
            connections.remove(user.getUsername());
            log.debug("Connection removed on completion for {}", user);
        });
        emitter.onError(throwable -> {
            connections.remove(user.getUsername());
            log.debug("Connection removed on error for {}", user, throwable);
        });
    }

    public void sendGameFound(UserAccountForm player1, UserAccountForm player2, String gameId) {
        executor.execute(() -> send(connections.get(player1.getUsername()), EVENT_GAME_FOUND, ImmutableMap.of(DATA_GAME_ID_KEY, gameId)));
        executor.execute(() -> send(connections.get(player2.getUsername()), EVENT_GAME_FOUND, ImmutableMap.of(DATA_GAME_ID_KEY, gameId)));
        log.debug("Game found sent to players {} and {}. Game id: {}", player1.getUsername(), player2.getUsername(), gameId);
    }

    public void sendSearchStarted(AppUser user) {
        final SseEmitter emitter = connections.get(user.getUsername());
        send(emitter, EVENT_SEARCH_STARTED);
        log.debug("Search started sent to player {}", user.getUsername());
    }

    public void sendSearchStopped(AppUser user) {
        final SseEmitter emitter = connections.get(user.getUsername());
        send(emitter, EVENT_SEARCH_STOPPED);
        log.debug("Search stopped sent to player {}", user.getUsername());
    }

    public int getActiveConnectionsCount() {
        return connections.size();
    }

    @Scheduled(fixedDelay = 5000)
    protected void keepConnectionsAlive() {
        connections.values().parallelStream().forEach(emitter -> send(emitter, EVENT_KEEP_ALIVE));
    }

    private void send(SseEmitter emitter, String event) {
        send(emitter, event, DATA_EMPTY);
    }

    private void send(SseEmitter emitter, String event, Object data) {
        if (emitter == null) {
            log.warn("Attempt to send through null emitter");
            return;
        }
        try {
            emitter.send(SseEmitter.event().name(event).data(data));
        } catch (IOException e) {
            log.error("SSE send error", e);
        }
    }

    public void sendGameState(ActiveGame game) {
        final String event = "state";
        final ActiveGameState data = game.getState();
        executor.execute(() -> send(connections.get(game.getWhitePlayer().getUsername()), event, ImmutableMap.of("data", data)));
        executor.execute(() -> send(connections.get(game.getBlackPlayer().getUsername()), event, ImmutableMap.of("data", data)));
    }

    public void sendBoard(ActiveGame game) {
        final String event = "board";
        final String data = BoardUtils.bitboardToFen(game.getBoard());
        executor.execute(() -> send(connections.get(game.getWhitePlayer().getUsername()), event, ImmutableMap.of("data", data)));
        executor.execute(() -> send(connections.get(game.getBlackPlayer().getUsername()), event, ImmutableMap.of("data", data)));
    }

    public void sendMovablePieces(ActiveGame game) {
        final String event = "pieces";
        final Map<Integer, Character> whiteData = game.getWhitePieces().stream().collect(Collectors.toMap(MovablePiece::getId, MovablePiece::getPiece));
        final Map<Integer, Character> blackData = game.getBlackPieces().stream().collect(Collectors.toMap(MovablePiece::getId, MovablePiece::getPiece));
        executor.execute(() -> send(connections.get(game.getWhitePlayer().getUsername()), event, ImmutableMap.of("data", whiteData)));
        executor.execute(() -> send(connections.get(game.getBlackPlayer().getUsername()), event, ImmutableMap.of("data", blackData)));
    }
}
