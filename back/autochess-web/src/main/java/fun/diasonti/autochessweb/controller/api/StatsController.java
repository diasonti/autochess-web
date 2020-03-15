package fun.diasonti.autochessweb.controller.api;

import fun.diasonti.autochessweb.engine.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final ConnectionService connectionService;

    @Autowired
    public StatsController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @GetMapping("/online")
    public int playersOnline() {
        return connectionService.getActiveConnectionsCount();
    }

}
