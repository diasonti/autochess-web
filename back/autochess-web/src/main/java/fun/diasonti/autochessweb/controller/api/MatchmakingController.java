package fun.diasonti.autochessweb.controller.api;

import fun.diasonti.autochessweb.config.security.data.AppUser;
import fun.diasonti.autochessweb.engine.ConnectionService;
import fun.diasonti.autochessweb.engine.MatchmakingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matchmaking")
public class MatchmakingController {

    private final MatchmakingService matchmakingService;
    private final ConnectionService connectionService;

    @Autowired
    public MatchmakingController(MatchmakingService matchmakingService, ConnectionService connectionService) {
        this.matchmakingService = matchmakingService;
        this.connectionService = connectionService;
    }

    @PostMapping("/search/start")
    public void searchStart(AppUser user) {
        final boolean searchStarted = matchmakingService.addToSearchQueue(user.getUsername());
        if (searchStarted) {
            connectionService.sendSearchStarted(user);
        }
    }

    @PostMapping("/search/stop")
    public void searchStop(AppUser user) {
        final boolean searchStopped = matchmakingService.removeFromSearchQueue(user.getUsername());
        if (searchStopped) {
            connectionService.sendSearchStopped(user);
        }
    }
}
