package shirley.com.sudoku.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import shirley.com.sudoku.model.GridItem;

/**
 * This class represents a Sudoku game. It contains the solution, the user
 * input, the selected number and methods to check the validation of the user
 * input.
 *
 * @author Eric Beijer
 */
public class Game extends Observable {
    private int[][] solution;       // Generated solution.
    private int[][] game;           // Generated game with user input.
    private boolean[][] check;      // Holder for checking validity of game.
    private boolean[][] checkReference; //是否与当前选中的相关，进行高亮提示
    private boolean[][] checkConflict; //是否和其它格子冲突
    private int selectedNumber;     // Selected number by user.
    private int[] position;       //选中的位置
    private boolean help;           // Help turned on or off.
    private int minFilled;
    private Random ran = new Random();
    // 当前难度级别;1-2简单难度；3普通难度；4高级难度；5骨灰级难度
    private int level;
    private Context context;

    /**
     * Constructor
     */
    public Game(Context context) {
//        newGame();
        this.context = context;
        check = new boolean[9][9];
        checkReference = new boolean[9][9];
        checkConflict = new boolean[9][9];
        game = new int[9][9];
        help = true;
    }

    /**
     * Generates a new Sudoku game.<br />
     * All observers will be notified, update action: new game.
     */
    public void newGame(int level) {
        setLevel(level);
        solution = generateSolution(new int[9][9]);
        game = generateGame(copy(solution));
        setChanged();
//        game = copy(solution);
        notifyObservers(UpdateAction.NEW_GAME);
//        print(solution);
//        print(game);
    }

    /**
     * 生成好的数独，设置数据源
     * @param data
     */
    public void setGame(int[][] data,int[][] solution){
        game = copy(data);
        this.solution = copy(solution);
//        print(solution);
//        print(game);
        setChanged();
        notifyObservers(UpdateAction.NEW_GAME);
    }

    public void resumeGame(int[][] solution){
        this.solution = copy(solution);
//        print(solution);
        setChanged();
        notifyObservers(UpdateAction.RESUME_GAME);
    }

    /**
     * 设置级别
     *
     * @param level
     */
    public void setLevel(int level) {
        if (level < 0 || level > 3) {
            this.level = 1;
        } else {
            this.level = level;
        }
        switch (level) {
            case 0:
                int ranNum = ran.nextInt(10);
                minFilled = 45 + ranNum;
                break;
            case 1:
                minFilled = 31 + ran.nextInt(10);
                break;
            case 2:
                minFilled = 21 + ran.nextInt(10);
                break;
            case 3:
                minFilled = 17 + ran.nextInt(10);
                break;
            default:
                break;
        }
    }

    /**
     * 生成数独游戏,根据等级决定最少填充个数，随机生成数字决定挖洞位置
     *
     * @param game
     * @return
     */
//    private int[][] generateGame(int[][] game) {
//        List<Integer> numbers = new ArrayList<Integer>();
//        for (int i = 0; i <= 80; i++) numbers.add(i);
//        Collections.shuffle(numbers);
//        //删掉填充位置，留下挖洞位置
//        for (int i = 0; i <= minFilled; i++) {
//            numbers.remove(0);
//        }
//        for (int i = 0; i < numbers.size(); i++) {
//            int index = numbers.get(i);
//            int x = index / 9;
//            int y = index % 9;
//            game[x][y] = 0;
//        }
//        return game;
//    }

    /**
     * Checks user input agains the solution and puts it into a check matrix.<br />
     * All observers will be notified, update action: check.
     */
    public void checkGame() {
        selectedNumber = 0;
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++)
                check[x][y] = game[x][y] == solution[x][y];
        }
        setChanged();
        notifyObservers(UpdateAction.CHECK);
    }

    /**
     * Sets help turned on or off.<br />
     * All observers will be notified, update action: help.
     *
     * @param help True for help on, false for help off.
     */
    public void setHelp(boolean help) {
        this.help = help;
        setChanged();
        notifyObservers(UpdateAction.HELP);
    }

    /**
     * Sets selected number to user input.<br />
     * All observers will be notified, update action: selected number.
     *
     * @param selectedNumber Number selected by user.
     */
    public void setSelectedNumber(int selectedNumber) {
        this.selectedNumber = selectedNumber;
        setChanged();
        notifyObservers(UpdateAction.SELECTED_NUMBER);
    }

    /**
     * Returns number selected user.
     *
     * @return Number selected by user.
     */
    public int getSelectedNumber() {
        return selectedNumber;
    }

    public void setSelectedPosition(int x,int y) {
        this.position = new int[2];
        position[0] = x;
        position[1] = y;
        setChanged();
        notifyObservers(UpdateAction.SELECTED_POSITION);
    }

    /**
     * Returns number selected user.
     *
     * @return Number selected by user.
     */
    public int[] getSelectedPosition() {
        return position;
    }
    /**
     * Returns whether help is turned on or off.
     *
     * @return True if help is turned on, false if help is turned off.
     */
    public boolean isHelp() {
        return help;
    }

    /**
     * Returns whether selected number is candidate at given position.
     *
     * @param x X position in game.
     * @param y Y position in game.
     * @return True if selected number on given position is candidate,
     * false otherwise.
     */
    public boolean isSelectedNumberCandidate(int x, int y) {
        return isNumberCandidate(x, y, selectedNumber);
    }

    /**
     * Returns whether current number is candidate at given position.
     *
     * @param x X position in game.
     * @param y Y position in game.
     * @return True if selected number on given position is candidate,
     * false otherwise.
     */
    public boolean isNumberCandidate(int x, int y, int number) {
        return game[x][y] == 0 && isPossibleX(game, x, y, number)
                && isPossibleY(game, x, y, number) && isPossibleBlock(game, x, y, number);
    }

    /**
     * check user input is valid
     * X position in game.
     *
     * @param y Y position in game.
     * @return true 说明填入的是有效的，错误的说明有冲突
     */
    public boolean checkValid(int x, int y) {
        if (game == null) {
            return true;
        }
        int number = game[x][y];
        if (number == 0) {
            return true;
        }
        return isPossibleX(game, x, y, number) && isPossibleY(game, x, y, number) && isPossibleBlock(game, x, y, number);
    }

    /**
     * Sets given number on given position in the game.
     *
     * @param x      The x position in the game.
     * @param y      The y position in the game.
     * @param number The number to be set.
     */
    public void setNumber(int x, int y, int number) {
        game[x][y] = number;
        setChanged();
        notifyObservers(UpdateAction.INPUT_NUMBER);
    }

    /**
     * Returns number of given position.
     *
     * @param x X position in game.
     * @param y Y position in game.
     * @return Number of given position.
     */
    public int getNumber(int x, int y) {
        return game[x][y];
    }

    /**
     * 获取解决方案中指定位置的数
     *
     * @param x
     * @param y
     * @return
     */
    public int getSolutionNumber(int x, int y) {
        return solution[x][y];
    }

    /**
     * Returns whether user input is valid of given position.
     *
     * @param x X position in game.
     * @param y Y position in game.
     * @return True if user input of given position is valid, false
     * otherwise.
     */
    public boolean isCheckValid(int x, int y) {
        return check[x][y];
    }

    /**
     * Returns whether given number is candidate on x axis for given game.
     *
     * @param game   Game to check.
     * @param cx     Position of x axis to check.
     * @param number Number to check.
     * @return True if number is candidate on x axis, false otherwise.
     */
    private boolean isPossibleX(int[][] game, int cx, int cy, int number) {
        for (int y = 0; y < 9; y++) {
            //不比较当前格子
            if (cy != y && game[cx][y] == number)
                return false;
        }
        return true;
    }

    /**
     * Returns whether given number is candidate on y axis for given game.
     *
     * @param game   Game to check.
     * @param cy     Position of y axis to check.
     * @param number Number to check.
     * @return True if number is candidate on y axis, false otherwise.
     */
    private boolean isPossibleY(int[][] game, int cx, int cy, int number) {
        for (int x = 0; x < 9; x++) {
            if (x != cx && game[x][cy] == number)
                return false;
        }
        return true;
    }

    /**
     * Returns whether given number is candidate in block for given game.
     *
     * @param game   Game to check.
     * @param x      Position of number on x axis in game to check.
     * @param y      Position of number on y axis in game to check.
     * @param number Number to check.
     * @return True if number is candidate in block, false otherwise.
     */
    private boolean isPossibleBlock(int[][] game, int x, int y, int number) {
        int x1 = x < 3 ? 0 : x < 6 ? 3 : 6;
        int y1 = y < 3 ? 0 : y < 6 ? 3 : 6;

        for (int xx = x1; xx < x1 + 3; xx++) {
            for (int yy = y1; yy < y1 + 3; yy++) {
                if (xx != x && yy != y && game[xx][yy] == number)
                    return false;
            }
        }
        return true;
    }

    /**
     * Returns next posible number from list for given position or -1 when list
     * is empty.
     *
     * @param game    Game to check.
     * @param x       X position in game.
     * @param y       Y position in game.
     * @param numbers List of remaining numbers.
     * @return Next possible number for position in game or -1 when
     * list is empty.
     */
    private int getNextPossibleNumber(int[][] game, int x, int y, List<Integer> numbers) {
        while (numbers.size() > 0) {
            int number = numbers.remove(0);
            if (isPossibleX(game, x, y, number) && isPossibleY(game, x, y, number) && isPossibleBlock(game, x, y, number))
                return number;
        }
        return -1;
    }

    /**
     * Generates Sudoku game solution.
     *
     * @param game  Game to fill, user should pass 'new int[9][9]'.
     * @param index Current index, user should pass 0.
     * @return Sudoku game solution.
     */
    private int[][] generateSolution(int[][] game, int index) {
//        long time1 = System.currentTimeMillis();
        if (index > 80)
            return game;


        int x = index / 9;
        int y = index % 9;

        List<Integer> numbers = new ArrayList<Integer>();
        for (int i = 1; i <= 9; i++) numbers.add(i);
        Collections.shuffle(numbers);

        while (numbers.size() > 0) {
            int number = getNextPossibleNumber(game, x, y, numbers);
            if (number == -1)
                return null;

            game[x][y] = number;
            int[][] tmpGame = generateSolution(game, index + 1);
            if (tmpGame != null)
                return tmpGame;
            game[x][y] = 0;
        }
        long time2 = System.currentTimeMillis();
//        System.out.println("生成数独time:" + (time2 - time1));
        return null;
    }

    /**
     * 数独生成算法，将原有算法改进了下，先随机生成一条对角线上的九宫格的数字，原后对剩余九宫格分别用回溯算法
     *
     * @param game
     * @return
     */
    private int[][] generateSolution(int[][] game) {
        long time1 = System.currentTimeMillis();
        //生成一条对角线的格子
        game = generateBlock(game, 0, 0);
        game = generateBlock(game, 3, 3);
        game = generateBlock(game, 6, 6);
        //生成其它格子
        int[] index = {6, 7, 8, 15, 16, 17, 24, 25, 26, 54, 55, 56, 63, 64, 65, 72, 73, 74, 3, 4, 5, 12, 13, 14, 21, 22, 23, 27, 28, 29, 36, 37, 38, 45, 46, 47, 33, 34, 35, 42, 43, 44, 51, 52, 53, 57, 58, 59, 66, 67, 68, 75, 76, 77};
        game = generateSolution(game, index, 0);
//        int[] index2 = {54, 55, 56, 63, 64, 65, 72, 73, 74};
//        game = generateSolution(game, index2, 0);
//        int[] index3 = {3, 4, 5, 12, 13, 14, 21, 22, 23};
//        game = generateSolution(game, index3, 0);
//        int[] index4 = {27, 28, 29, 36, 37, 38, 45, 46, 47};
//        game = generateSolution(game, index4, 0);
//        int[] index5 = {33, 34, 35, 42, 43, 44, 51, 52, 53};
//        game = generateSolution(game, index5, 0);
//        int[] index6 = {57, 58, 59, 66, 67, 68, 75, 76, 77};
//        game = generateSolution(game, index6, 0);
        long time2 = System.currentTimeMillis();
        System.out.println("生成数独time:" + (time2 - time1));
        return game;
    }

    /**
     * 对指定格子采用回溯算法
     *
     * @param game   整个数独对应的数组
     * @param index  指定格子序列
     * @param indexi 指定格子在序列中的下标
     * @return
     */
    private int[][] generateSolution(int[][] game, int[] index, int indexi) {
        if (indexi > 53)
            return game;


        int x = index[indexi] / 9;
        int y = index[indexi] % 9;

        List<Integer> numbers = new ArrayList<Integer>();
        for (int i = 1; i <= 9; i++) numbers.add(i);
        Collections.shuffle(numbers);

        while (numbers.size() > 0) {
            int number = getNextPossibleNumber(game, x, y, numbers);
            if (number == -1)
                return null;

            game[x][y] = number;
            int[][] tmpGame = generateSolution(game, index, indexi + 1);
            if (tmpGame != null)
                return tmpGame;
            game[x][y] = 0;
        }
        return null;
    }

    /**
     * 生成一个九宫格里的数据
     *
     * @param game
     * @param x
     * @param y
     * @return
     */
    private int[][] generateBlock(int[][] game, int x, int y) {
        List<Integer> numbers = new ArrayList<Integer>();
        for (int i = 1; i <= 9; i++) numbers.add(i);
        Collections.shuffle(numbers);
        for (int xx = x; xx < x + 3; xx++) {
            for (int yy = y; yy < y + 3; yy++) {
                int number = numbers.remove(0);
                game[xx][yy] = number;
            }
        }
        return game;
    }

    /**
     * Generates Sudoku game from solution.
     *
     * @param game Game to be generated, user should pass a solution.
     * @return Generated Sudoku game.
     */
    private int[][] generateGame(int[][] game) {
        List<Integer> positions = new ArrayList<Integer>();
        for (int i = 0; i < 81; i++)
            positions.add(i);
        Collections.shuffle(positions);
        return generateGame(game, positions);
    }

    /**
     * Generates Sudoku game from solution, user should use the other
     * generateGame method. This method simple removes a number at a position.
     * If the game isn't anymore valid after this action, the game will be
     * brought back to previous state.
     *
     * @param game      Game to be generated.
     * @param positions List of remaining positions to clear.
     * @return Generated Sudoku game.
     */
    private int[][] generateGame(int[][] game, List<Integer> positions) {
        while (positions.size() > 0) {
            int position = positions.remove(0);

            int x = position / 9;
            int y = position % 9;
            int temp = game[x][y];
            game[x][y] = 0;

            if (!isValid(game))
                game[x][y] = temp;
        }

        return game;
    }

    /**
     * Checks whether given game is valid.
     *
     * @param game Game to check.
     * @return True if game is valid, false otherwise.
     */
    private boolean isValid(int[][] game) {
        return isValid(game, 0, new int[]{0});
    }

    /**
     * Checks whether given game is valid, user should use the other isValid
     * method. There may only be one solution.
     *
     * @param game              Game to check.
     * @param index             Current index to check.
     * @param numberOfSolutions Number of found solutions. Int[] instead of
     *                          int because of pass by reference.
     * @return True if game is valid, false otherwise.
     */
    private boolean isValid(int[][] game, int index, int[] numberOfSolutions) {
        if (index > 80)
            return ++numberOfSolutions[0] == 1;


        int x = index / 9;
        int y = index % 9;

        if (game[x][y] == 0) {
            List<Integer> numbers = new ArrayList<Integer>();
            for (int i = 1; i <= 9; i++)
                numbers.add(i);

            while (numbers.size() > 0) {
                int number = getNextPossibleNumber(game, x, y, numbers);
                if (number == -1)
                    break;
                game[x][y] = number;

                if (!isValid(game, index + 1, numberOfSolutions)) {
                    game[x][y] = 0;
                    return false;
                }
                game[x][y] = 0;
            }
        } else if (!isValid(game, index + 1, numberOfSolutions))
            return false;

        return true;
    }

    /**
     * Copies a game.
     *
     * @param game Game to be copied.
     * @return Copy of given game.
     */
    private int[][] copy(int[][] game) {
        int[][] copy = new int[9][9];

        for (int x = 0; x < 9; x++)
            for (int y = 0; y < 9; y++) {
                copy[x][y] = game[x][y];
            }
        return copy;
    }


    /**
     * Prints given game to console. Used for debug.
     *
     * @param game Game to be printed.
     */
    private void print(int[][] game) {
        System.out.println();

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                System.out.print(" " + game[x][y]);
            }
            System.out.println();
        }
    }

    /**
     * 检查是否和当前格子相关，同一行，同一列，同一小九宫格里的都认为相关
     *
     * @param x
     * @param y
     */
    public void checkReference(int x, int y) {
        if (game == null) {
            return;
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int px = x / 3 * 3;
                int qy = y / 3 * 3;
                boolean isBlock = ((i >= px && i < (px + 3)) && (j >= qy && j < (qy + 3)));
                if (j == y || i == x || isBlock) {
                    checkReference[i][j] = true;
                } else {
                    checkReference[i][j] = false;
                }
            }
        }
    }

    public void setReference(int x, int y, boolean isReference) {
        checkReference[x][y] = isReference;
    }
    /**
     * 获取和当前选中相关的位置
     *
     * @return
     */
    public boolean[][] isReference() {
        return checkReference;
    }

    /**
     * 检查当前存在冲突的格子
     */
    public void checkConflict() {
        if (game == null) {
            return;
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (game[i][j] != 0 && !checkValid(i, j)) {
                    checkConflict[i][j] = true;
                } else {checkConflict[i][j] = false;

                }
            }
        }
    }

    /**
     * 清除冲突
     */
    public void clearConflict(){
        if (game == null) {
            return;
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                checkConflict[i][j] = false;
            }
        }
    }

    /**
     * 获取冲突的状态
     *
     * @return
     */
    public boolean[][] isConflict() {
        return checkConflict;
    }

    public void setConflict(int x, int y, boolean isConflict) {
        checkConflict[x][y] = isConflict;
    }
}