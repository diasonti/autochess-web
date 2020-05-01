package fun.diasonti.autochessweb.controller.api;

import fun.diasonti.autochessweb.config.security.data.AppUser;
import fun.diasonti.autochessweb.data.pojo.playerstate.AnonymousPlayerState;
import fun.diasonti.autochessweb.data.pojo.playerstate.PlayerState;
import fun.diasonti.autochessweb.service.StateFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fetch")
public class StateFetchController {

    private final StateFetchService stateFetchService;

    @Autowired
    public StateFetchController(StateFetchService stateFetchService) {
        this.stateFetchService = stateFetchService;
    }


    @GetMapping("/state")
    public PlayerState fetchState(AppUser user) {
        if (user == null) {
            return AnonymousPlayerState.getInstance();
        }
        return stateFetchService.fetchState(user.getUsername());
    }
}
