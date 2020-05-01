package fun.diasonti.autochessweb.data.pojo;

import fun.diasonti.autochessweb.data.enums.GameResult;

public class FinishedGame {

    private GameResult result;
    private int rankBefore;
    private int rankAfter;

    public GameResult getResult() {
        return result;
    }

    public void setResult(GameResult result) {
        this.result = result;
    }

    public int getRankBefore() {
        return rankBefore;
    }

    public void setRankBefore(int rankBefore) {
        this.rankBefore = rankBefore;
    }

    public int getRankAfter() {
        return rankAfter;
    }

    public void setRankAfter(int rankAfter) {
        this.rankAfter = rankAfter;
    }
}
