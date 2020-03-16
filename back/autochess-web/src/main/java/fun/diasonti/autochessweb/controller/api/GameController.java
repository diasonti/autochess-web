package fun.diasonti.autochessweb.controller.api;

import fun.diasonti.autochessweb.config.security.data.AppUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @PostMapping("/move")
    public void makeMove(AppUser user, @RequestParam int piece, @RequestParam int cell) {

    }

}
