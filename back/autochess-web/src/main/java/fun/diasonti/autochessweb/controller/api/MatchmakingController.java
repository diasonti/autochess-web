package fun.diasonti.autochessweb.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
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

    private final ExecutorService searchThreadPool = Executors.newCachedThreadPool();
    private final Map<String, User> searchTokens = Collections.synchronizedMap(new HashMap<>());

    @GetMapping("/search/token")
    public ResponseEntity<Map<String, String>> searchToken() {
        final String token = UUID.randomUUID().toString();
        searchTokens.put(token, null);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @GetMapping("/search/stream")
    public SseEmitter searchStream(@RequestParam String token) {
        final SseEmitter emitter = new SseEmitter();
        searchThreadPool.execute(() -> {
            try {
                if (searchTokens.containsKey(token)) {
                    searchTokens.remove(token);
                } else {
                    throw new Exception("Invalid search token");
                }
                emitter.send(SseEmitter.event().name("search").data("{}"));
                Thread.sleep(5_000);
                emitter.send(SseEmitter.event().name("search").data("{}"));
                Thread.sleep(5_000);
                emitter.send(SseEmitter.event().name("game").data(Collections.singletonMap("gameId", 322)));
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }
}
