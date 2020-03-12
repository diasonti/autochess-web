package fun.diasonti.autochessweb.controller.api;

import fun.diasonti.autochessweb.config.security.data.AppUser;
import fun.diasonti.autochessweb.data.form.UserAccountForm;
import fun.diasonti.autochessweb.engine.MatchmakingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/matchmaking")
public class MatchmakingController {

    private static final Logger log = LoggerFactory.getLogger(MatchmakingController.class);

    private final ExecutorService searchThreadPool = Executors.newCachedThreadPool();
    private final Map<String, UserAccountForm> searchTokens = Collections.synchronizedMap(new HashMap<>());

    private final MatchmakingService matchmakingService;

    @Autowired
    public MatchmakingController(MatchmakingService matchmakingService) {
        this.matchmakingService = matchmakingService;
    }

    @GetMapping("/search")
    public SseEmitter searchStream(AppUser user) {
        final SseEmitter emitter = new SseEmitter();
        searchThreadPool.execute(() -> {
            matchmakingService.addToSearchQueue(user.getUsername());
            try {
                while (true) {
                    Thread.sleep(10_000);
                    emitter.send(SseEmitter.event().name("heartbeat").data("null"));
                }
            } catch (Exception e) {
                log.error("Search SSE emitter error", e);
                matchmakingService.removeFromSearchQueue(user.getUsername());
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }
}
