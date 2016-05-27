package shirley.com.sudoku.model;

/**
 * Created by Administrator on 2016/5/26.
 */
public class GradeItem {
    private int[][] game; //游戏面板
    private int[][] solution; //解决方案

    public int[][] getGame() {
        return game;
    }

    public void setGame(int[][] game) {
        this.game = game;
    }

    public int[][] getSolution() {
        return solution;
    }

    public void setSolution(int[][] solution) {
        this.solution = solution;
    }
}
