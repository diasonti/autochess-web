package fun.diasonti.autochessweb.engine;

import com.google.common.collect.Maps;
import fun.diasonti.autochessweb.data.form.UserAccountForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Component
public class ConnectionService {

    private static final Logger log = LoggerFactory.getLogger(ConnectionService.class);

    private final Map<Long, SseEmitter> connections = Maps.newConcurrentMap();

    public void addConnection(UserAccountForm user, SseEmitter emitter) {
        connections.put(user.getId(), emitter);
        emitter.onCompletion(() -> {
            connections.remove(user.getId());
            if (log.isDebugEnabled())
                log.debug("Connection removed on completion for {}", user);
        });
        emitter.onError(throwable -> {
            connections.remove(user.getId());
            if (log.isDebugEnabled())
                log.debug("Connection removed on error for {}", user, throwable);
        });
        if (log.isDebugEnabled())
            log.debug("Connection added for {}", user);
    }

    public void sendGameFound(UserAccountForm player1, UserAccountForm player2, String gameId) {
        // Two events should be simultaneously sent here
        if (log.isDebugEnabled())
            log.debug("Game found sent to players {} and {}. Game id: {}", player1.getUsername(), player2.getUsername(), gameId);
    }

}
