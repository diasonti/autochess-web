package fun.diasonti.autochessweb.config;

import fun.diasonti.chessengine.engine.BitwiseOperationsMoveEngine;
import fun.diasonti.chessengine.engine.MaterialPositionalEvaluationEngine;
import fun.diasonti.chessengine.engine.MinimaxAlphaBetaSearchEngine;
import fun.diasonti.chessengine.engine.interfaces.EvaluationEngine;
import fun.diasonti.chessengine.engine.interfaces.MoveEngine;
import fun.diasonti.chessengine.engine.interfaces.SearchEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChessEngineConfig {

    @Bean
    public MoveEngine bitwiseOperationsMoveEngine() {
        return new BitwiseOperationsMoveEngine();
    }

    @Bean
    public EvaluationEngine materialPositionalEvaluationEngine() {
        return new MaterialPositionalEvaluationEngine();
    }

    @Bean
    public SearchEngine alphaBetaSearchEngine() {
        final MinimaxAlphaBetaSearchEngine searchEngine = new MinimaxAlphaBetaSearchEngine();
        searchEngine.setMoveEngine(bitwiseOperationsMoveEngine());
        searchEngine.setEvaluationEngine(materialPositionalEvaluationEngine());
        return searchEngine;
    }
}
