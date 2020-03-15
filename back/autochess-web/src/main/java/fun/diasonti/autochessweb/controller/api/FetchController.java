package fun.diasonti.autochessweb.controller.api;

import fun.diasonti.autochessweb.config.security.data.AppUser;
import fun.diasonti.autochessweb.data.view.MatchView;
import fun.diasonti.autochessweb.data.view.PlayerView;
import fun.diasonti.autochessweb.service.MatchViewService;
import fun.diasonti.autochessweb.service.PlayerViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fetch")
public class FetchController {

    private final PlayerViewService playerViewService;
    private final MatchViewService matchViewService;

    @Autowired
    public FetchController(PlayerViewService playerViewService, MatchViewService matchViewService) {
        this.playerViewService = playerViewService;
        this.matchViewService = matchViewService;
    }

    @GetMapping("/player")
    public PlayerView fetchPlayer(AppUser user) {
        return playerViewService.getByUsername(user.getUsername());
    }

    @GetMapping("/match")
    public List<MatchView> fetchMatches(AppUser user, @RequestParam(defaultValue = "0") int page) {
        return matchViewService.getMatchHistory(user.getUsername(), page);
    }

}
