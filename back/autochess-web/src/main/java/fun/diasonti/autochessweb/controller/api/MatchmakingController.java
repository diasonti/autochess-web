package fun.diasonti.autochessweb.controller.api;

import fun.diasonti.autochessweb.config.security.data.AppUser;
import fun.diasonti.autochessweb.data.exceptions.InvalidSearchTokenException;
import fun.diasonti.autochessweb.data.form.UserAccountForm;
import fun.diasonti.autochessweb.engine.MatchmakingSearchEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/matchmaking")
public class MatchmakingController {

    private static final Logger log = LoggerFactory.getLogger(MatchmakingController.class);

    private final ExecutorService searchThreadPool = Executors.newCachedThreadPool();
    private final Map<String, UserAccountForm> searchTokens = Collections.synchronizedMap(new HashMap<>());

    private final MatchmakingSearchEngine searchEngine;

    @Autowired
    public MatchmakingController(MatchmakingSearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }

    @GetMapping("/search/token")
    public ResponseEntity<Map<String, String>> searchToken(AppUser user) {
        final String token = UUID.randomUUID().toString();
        searchTokens.put(token, user.getUserAccount());
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @GetMapping("/search/stream")
    public SseEmitter searchStream(@RequestParam String token) {
        final SseEmitter emitter = new SseEmitter();
        searchThreadPool.execute(() -> {
            final UserAccountForm user = searchTokens.get(token);
            try {
                if (user == null) {
                    throw new InvalidSearchTokenException();
                }
                while (true) {
                    Thread.sleep(10_000);
                    emitter.send(SseEmitter.event().name("heartbeat").data("null"));
                }
            } catch (InvalidSearchTokenException e) {
                emitter.completeWithError(e);
            } catch (Exception e) {
                log.error("Search SSE emitter error", e);
                searchEngine.remove(user);
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }
}
