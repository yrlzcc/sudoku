package shirley.com.sudoku.model;

import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 */
public class LevelItem {
    private List<GradeItem> gameList = null;  //当前级别对应的关数

    public List<GradeItem> getGameList() {
        return gameList;
    }

    public void setGameList(List<GradeItem> gameList) {
        this.gameList = gameList;
    }
}
