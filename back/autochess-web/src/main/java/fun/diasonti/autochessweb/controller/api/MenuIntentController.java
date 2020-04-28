package fun.diasonti.autochessweb.controller.api;

import fun.diasonti.autochessweb.config.security.data.AppUser;
import fun.diasonti.autochessweb.engine.MatchmakingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/intent/menu")
public class MenuIntentController {

    private final MatchmakingService matchmakingService;

    @Autowired
    public MenuIntentController(MatchmakingService matchmakingService) {
        this.matchmakingService = matchmakingService;
    }

    @PostMapping("/search/start")
    public void startSearch(AppUser user) {
        matchmakingService.addToSearchQueue(user.getUsername());
    }

    @PostMapping("/search/stop")
    public void stopSearch(AppUser user) {
        matchmakingService.removeFromSearchQueue(user.getUsername());
    }

}
