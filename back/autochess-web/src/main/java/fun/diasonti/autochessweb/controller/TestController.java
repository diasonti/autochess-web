package fun.diasonti.autochessweb.controller;

import fun.diasonti.chessengine.data.ChessBoard;
import fun.diasonti.chessengine.data.Color;
import fun.diasonti.chessengine.data.Move;
import fun.diasonti.chessengine.engine.BitwiseOperationsMoveEngine;
import fun.diasonti.chessengine.engine.MinimaxAlphaBetaSearchEngine;
import fun.diasonti.chessengine.engine.interfaces.MoveEngine;
import fun.diasonti.chessengine.engine.interfaces.SearchEngine;
import fun.diasonti.chessengine.util.BoardUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/test")
public class TestController {

    private final MoveEngine moveEngine = new BitwiseOperationsMoveEngine();
    private final SearchEngine searchEngine = new MinimaxAlphaBetaSearchEngine();
    private final Random random = new Random();

    @GetMapping("")
    public String test(@RequestParam(defaultValue = "rnbq1bnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQ1BNR") String fen) {
//        websocket.convertAndSend("/topic/test", fen);
        return fen;
    }

    @GetMapping("/game")
    public String game(@RequestParam(defaultValue = "rnbq1bnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQ1BNR") String fen) {
        new Thread(() -> {
            final Random random = new Random();
            Color player = Color.WHITE;
            ChessBoard board = BoardUtils.fenToBitboard(fen);
//            websocket.convertAndSend("/topic/test", "BOARD:" + BoardUtils.bitboardToFen(board));
            for (int i = 0; i < 50; i++) {
                try {
                    Thread.sleep(250L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final Move move = searchEngine.getBestMove(board, player, random.nextInt(4) + 1);
//                websocket.convertAndSend("/topic/test", "MOVE:" + Long.numberOfTrailingZeros(move.from) + "," + Long.numberOfTrailingZeros(move.to));
                board = moveEngine.makeMove(board, move);
                player = player.getOpposite();
            }
        }).start();
        return fen;
    }

    @GetMapping("/board")
    public Mono<String> board(@RequestParam(defaultValue = "rnbq1bnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQ1BNR") String fen) {
        return Mono.just(fen);
    }

    @GetMapping(value = "/moves", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Object> moves(@RequestParam(defaultValue = "rnbq1bnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQ1BNR") String startingFen) {
        return Flux.generate(
                () -> BoardUtils.fenToBitboard(startingFen),
                (board, sink) -> {
                    if (board.moveCount == 5) {
                        sink.complete();
                        return null;
                    }
                    final Move bestMove = searchEngine.getBestMove(board, board.currentPlayer, random.nextInt(4) + 1);
                    sink.next(bestMove);
                    board.currentPlayer = board.currentPlayer.getOpposite();
                    board.moveCount++;
                    return moveEngine.makeMove(board, bestMove);
                }).cast(Move.class)
                .map(move -> {
                    Map<String, Integer> map = new HashMap<>();
                    map.put("from", Long.numberOfTrailingZeros(move.from));
                    map.put("to", Long.numberOfTrailingZeros(move.to));
                    return map;
                })
                .map(move -> ServerSentEvent.builder(move).event("move").build())
                .cast(Object.class)
                .concatWith(Flux.just(ServerSentEvent.builder().event("close").data("null").build()))
                .delayElements(Duration.ofSeconds(1));
    }

}
