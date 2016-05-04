package shirley.com.sudoku.utils;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import shirley.com.sudoku.GridItem;

/**
 * Created by shirley on 2016/4/14.
 *
 * 采用挖洞算法实现。不同难度将会有不同的最小填充格子数和已知格子数
 *
 */
public class CreateSudoku {
    public static final int SUDOKU_NUM = 1;
    private int[][] orginData;  //保存初始状态的数独
    // 初始化生成数组
    private int[][] sudokuData = { { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
    // 终盘数组
    private int[][] resultData;
    // 最小填充格子数
    private int minFilled;
    // 最小已知格子数
    private int minKnow;
    // 当前难度级别;1-2简单难度；3普通难度；4高级难度；5骨灰级难度
    private int level;

    private Random ran = new Random();

    public CreateSudoku() {
      this(3); // 默认为普通难度
    }

    public CreateSudoku(int level) {
        if (level < 0 || level > 6) {
            this.level = 3;
        } else {
            this.level = level;
        }
        switch (level) {
            case 1:
            case 2:
                int ranNum = ran.nextInt(10);
                if(ranNum > 4) {
                    minKnow = 5;
                } else {
                    minKnow = 4;
                }
                minFilled = 45 + ranNum;
                break;
//      case 2:
//          minFilled = 45 + ran.nextInt(5);
//          minKnow = 4;
//          break;
            case 3:
                minFilled = 31 + ran.nextInt(10);
                minKnow = 3;
                break;
            case 4:
                minFilled = 21 + ran.nextInt(10);
                minKnow = 2;
                break;
            case 5:
                minFilled = 17 + ran.nextInt(10);
                minKnow = 0;
                break;
            default:
                break;
        }
//        genSuduku();
        create();
        orginData = new int[9][9];
        for(int i = 0; i < 9; i++) {
            System.arraycopy(sudokuData[i], 0, orginData[i], 0, 9);
        }
    }

    public void create(){

//        getSudoku();

    }
    /**
     * 生成唯一解的数独
     */
    public void genSuduku() {
        int startX = ran.nextInt(9), startY = ran.nextInt(9); // 初始挖洞格子
        int orignStartX = startX, orignStartY = startY; // 暂存初始格子
        int filledCount = 81; // 当前已知格子数
//      int curMinKnow = 9; // 当前已知行列已知格子的下限
        genShuduKnow(11);
        if (solve(sudokuData, true)) {
            // 将终盘赋值给初始数独数组
            for (int i = 0; i < 9; i++) {
                System.arraycopy(resultData[i], 0, sudokuData[i], 0, 9);
            }
            // 实行等量交换
            sudokuData = equalChange(sudokuData);
            Point nextP; // 下一个挖洞位置
            do {
                int temMinKnow = getMinknow(sudokuData, startX, startY);
                if (isOnlyAnswer(sudokuData, startX, startY)
                        && temMinKnow >= minKnow) {
                    sudokuData[startX][startY] = 0;
                    filledCount--;
//                  curMinKnow = temMinKnow;
                }
                nextP = next(startX, startY);
                startX = nextP.x;
                startY = nextP.y;
                //校验此洞是否已挖
                while(sudokuData[startX][startY] == 0 && (orignStartX != startX || orignStartY != startY)) {
                    nextP = next(startX, startY);
                    startX = nextP.x;
                    startY = nextP.y;
                }
                //初级难度处理
                if(level == 1 || level == 2) {
                    while(startX == orignStartX && startY == orignStartY) {
                        nextP = next(startX, startY);
                        startX = nextP.x;
                        startY = nextP.y;
                    }
                }
            } while (filledCount > minFilled
                    && (orignStartX != startX || orignStartY != startY));
        } else {
            // 重新生成唯一解数独，直到成功为止
            genSuduku();
        }
    }

    /**
     * 获取下一个挖洞点
     *
     * @param x
     * @param y
     * @return
     */
    public Point next(int x, int y) {
        Point p = null;
        switch (level) {
            case 1:
            case 2: // 难度1、2均为随机算法
                p = new Point(ran.nextInt(9), ran.nextInt(9));
                break;
            case 3: // 难度3 间隔算法
                if (x == 8 && y == 7) {
                    p = new Point(0, 0);
                } else if (x == 8 && y == 8) {
                    p = new Point(0, 1);
                } else if ((x % 2 == 0 && y == 7) || (x % 2 == 1) && y == 0) {
                    p = new Point(x + 1, y + 1);
                } else if ((x % 2 == 0 && y == 8) || (x % 2 == 1) && y == 1) {
                    p = new Point(x + 1, y - 1);
                } else if (x % 2 == 0) {
                    p = new Point(x, y + 2);
                } else if (x % 2 == 1) {
                    p = new Point(x, y - 2);
                }
                break;
            case 4: // 难度4 蛇形算法
                p = new Point();
                if (x == 8 && y == 8) {
                    p.y = 0;
                } else if (x % 2 == 0 && y < 8) { // 蛇形顺序，对下个位置列的求解
                    p.y = y + 1;
                } else if ((x % 2 == 0 && y == 8) || (x % 2 == 1 && y == 0)) {
                    p.y = y;
                } else if (x % 2 == 1 && y > 0) {
                    p.y = y - 1;
                }

                if (x == 8 && y == 8) { // 蛇形顺序，对下个位置行的求解
                    p.x = 0;
                } else if ((x % 2 == 0 && y == 8) || (x % 2 == 1) && y == 0) {
                    p.x = x + 1;
                } else {
                    p.x = x;
                }
                break;
            case 5: // 从左至右，从上到下
                p = new Point();
                if (y == 8) {
                    if (x == 8) {
                        p.x = 0;
                    } else {
                        p.x = x + 1;
                    }
                    p.y = 0;
                } else {
                    p.x = x;
                    p.y = y + 1;
                }
            default:
                break;
        }
        return p;
    }

    /**
     * 生成指定个数的已知格子
     *
     * @param n
     */
    public void genShuduKnow(int n) {
        // 生成n个已知格子
        Point[] knowPonits = new Point[n];
        for (int i = 0; i < n; i++) {
            knowPonits[i] = new Point(ran.nextInt(9), ran.nextInt(9));
            // 检测是否重复
            for (int k = 0; k < i; k++) {
                if (knowPonits[k].equals(knowPonits[i])) {
                    i--;
                    break;
                }
            }
        }
        // 填充数字
        int num;
        Point p;
        for (int i = 0; i < n; i++) {
            num = 1 + ran.nextInt(9);
            p = knowPonits[i];
            sudokuData[p.x][p.y] = num;
            if (!validateIandJ(sudokuData, p.x, p.y)) {
                // 生成格子填充数字错误
                i--;
            }
        }

    }

    /**
     * 等量交换
     *
     * @param data
     * @return
     */
    public int[][] equalChange(int[][] data) {
        Random rand = new Random();
        int num1 = 1 + rand.nextInt(9);
        int num2 = 1 + rand.nextInt(9);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (data[i][j] == 1) {
                    data[i][j] = num1;
                } else if (data[i][j] == num1) {
                    data[i][j] = 1;
                }

                if (data[i][j] == 2) {
                    data[i][j] = num2;
                } else if (data[i][j] == num2) {
                    data[i][j] = 2;
                }
            }
        }
        return data;
    }

    /**
     * 判断挖去i,j位置后，是否有唯一解
     *
     * @param data
     * @param i
     * @param j
     * @return
     */
    public boolean isOnlyAnswer(int[][] data, int i, int j) {
        int k = data[i][j]; // 待挖洞的原始数字
        for (int num = 1; num < 10; num++) {
            data[i][j] = num;
            if (num != k && solve(data, false)) {
                // 除待挖的数字之外，还有其他的解，则返回false
                data[i][j] = k;
                return false;
            }
        }
        data[i][j] = k;
        return true;
    }

    /**
     * 删除指定位置后，新的行列下限
     *
     * @param data
     * @param p
     * @param q
     * @return
     */
    public int getMinknow(int[][] data, int p, int q) {
        int temp = data[p][q];
        int minKnow = 9;
        int tempKnow = 9;
        data[p][q] = 0;
        for (int i = 0; i < 9; i++) {
            // 行下限
            for (int j = 0; j < 9; j++) {
                if (data[i][j] == 0) {
                    tempKnow--;
                    if (tempKnow < minKnow) {
                        minKnow = tempKnow;
                    }
                }
            }
            tempKnow = 9;
        }
        tempKnow = 9;
        for (int j = 0; j < 9; j++) {
            // 列下限
            for (int i = 0; i < 9; i++) {
                if (data[i][j] == 0) {
                    tempKnow--;
                    if (tempKnow < minKnow) {
                        minKnow = tempKnow;
                    }
                }
            }
            tempKnow = 9;
        }
        data[p][q] = temp;
        // 返回删除后的下限
        return minKnow;
    }

    /**
     * 判断数独是否能解
     *
     * @param data
     * @return
     */
    public boolean solve(int[][] data, boolean flag) {
        int blankCount = 0; // 保存空位个数
        int[][] tempData = new int[9][9]; // 中间数组
        ArrayList<Point> blankList = new ArrayList<Point>(70); // 保存各个空格坐标
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tempData[i][j] = data[i][j];
                if (tempData[i][j] == 0) {
                    blankCount++;
                    blankList.add(new Point(i, j));
                }
            }
        }
        // 校验数独合法性
        if (!validate(tempData)) {
            return false;
        }
        if (blankCount == 0) {
            // 玩家已经成功解出数独
            return true;
        } else if (put(tempData, 0, blankList)) {
            if(flag) {
                // 智能解出答案，供生成数独终盘使用
                resultData = tempData;
            }
            return true;

        }
        return false;
    }

    /**
     * 在第n个空位子放入数字
     *
     * @param data
     * @param n
     * @param blankList
     * @return
     */
    public boolean put(int[][] data, int n, ArrayList<Point> blankList) {
        if (n < blankList.size()) {
            Point p = blankList.get(n);
            for (int i = 1; i < 10; i++) {
                data[p.x][p.y] = i;
                if (validateIandJ(data, p.x, p.y)
                        && put(data, n + 1, blankList)) {
                    return true;
                }
            }
            data[p.x][p.y] = 0;
            return false;
        } else {
            return true;
        }
    }

    /**
     * 校验x, y位置填充的数字是否可行
     *
     * @param data
     * @param x
     * @param y
     * @return
     */
    public boolean validateIandJ(int[][] data, int x, int y) {
        int m = 0, n = 0, p = 0, q = 0; // m,n是计数器，p,q用于确定测试点的方格位置
        for (m = 0; m < 9; m++) {
            if (m != x && data[m][y] == data[x][y]) {
                return false;
            }
        }
        for (n = 0; n < 9; n++) {
            if (n != y && data[x][n] == data[x][y]) {
                return false;
            }
        }
        for (p = x / 3 * 3, m = 0; m < 3; m++) {
            for (q = y / 3 * 3, n = 0; n < 3; n++) {
                if ((p + m != x || q + n != y)
                        && (data[p + m][q + n] == data[x][y])) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 校验x, y位置填充的数字是否可行
     *
     * @param data
     * @param x
     * @param y
     * @return
     */
    public boolean validateIandJ(List<GridItem> data, int position) {
        int m = 0, n = 0, p = 0, q = 0; // m,n是计数器，p,q用于确定测试点的方格位置
        int x = position/9;
        int y = position%9;
        for (m = 0; m < 9; m++) {  //判断同一列是否有重复
            int tempm = m*9+y;
            if (m != x  && (data.get(tempm).num == data.get(position).num)) {
                return false;
            }
        }
        for (n = 0; n < 9; n++) {  //判断同一行是否有重复
            int tempn = 9*x+n;
            if (n != y && data.get(tempn).num == data.get(position).num) {
                return false;
            }
        }
        for (p = x / 3 * 3, m = 0; m < 3; m++) { //判断一个九宫格里是否有重复
            for (q = y / 3 * 3, n = 0; n < 3; n++) {
                int temppq = 9 * (p + m) + q + n;
                if ((p + m != x || q + n != y)
                        && (data.get(temppq).num == data.get(position).num)) {
                   return false;
                }
            }
        }
        return true;
    }




    /**
     * 校验整个数独数组的合法性
     *
     * @param data
     * @return
     */
    public boolean validate(int[][] data) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (data[i][j] != 0 && !validateIandJ(data, i, j)) {
                    // 任何一个数字填充错误，则返回false
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 获取冲突列表
     * @return
     */
    public ArrayList<Point> validateForList(List<GridItem> gridItemsData) {
        ArrayList<Point> pList = new ArrayList<Point>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int position = i * 9 + j;
                if (gridItemsData.get(position).num!= 0 && !validateIandJ(gridItemsData,position)) {
                    pList.add(new Point(i, j));
                }
            }
        }
        return pList;
    }

    /**
     * 获取当前格对应的冲突列表
     * @param data
     * @return
     */
    public ArrayList<Point> validateForCurrentList(List<GridItem> data,int position) {
        if(position == -1){
            return null;
        }
        ArrayList<Point> pList = new ArrayList<Point>();
        int m = 0, n = 0, p = 0, q = 0; // m,n是计数器，p,q用于确定测试点的方格位置
        int x = position/9;
        int y = position%9;
        for (m = 0; m < 9; m++) {  //判断同一列是否有重复
            int tempm = m*9+y;
            if (m != x  && (data.get(tempm).num == data.get(position).num)) {
                pList.add(new Point(m,y));
            }
        }
        for (n = 0; n < 9; n++) {  //判断同一行是否有重复
            int tempn = 9*x+n;
            if (n != y && data.get(tempn).num == data.get(position).num) {
                pList.add(new Point(x,tempn));
            }
        }
        for (p = x / 3 * 3, m = 0; m < 3; m++) { //判断一个九宫格里是否有重复
            for (q = y / 3 * 3, n = 0; n < 3; n++) {
                int temppq = 9 * (p + m) + q + n;
                if ((p + m != x || q + n != y)
                        && (data.get(temppq).num == data.get(position).num)) {
                    pList.add(new Point(p+m,q+n));
                }
            }
        }
        return pList;
    }


    public int[][] myResultData() {
        return resultData;
    }

    public int[][] myInitData() {
        return orginData;
    }

    public int[][] mySudoku() {
        return sudokuData;
    }

    /**
     * 判断格子是否为已知格子
     * @param x
     * @param y
     * @return
     */
    public boolean isKnownCell(int x, int y) {
        return orginData[x][y] != 0;
    }

    public void makeToInitData() {
        for(int i = 0; i < 9; i++) {
            System.arraycopy(orginData[i], 0, sudokuData[i], 0, 9);
        }
    }

    public void set(int x, int y, int num) {
        sudokuData[x][y] = num;
    }

    public boolean isSuccess() {
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
//              if(sudokuData[i][j] == 0 || sudokuData[i][j] != resultData[i][j]) {
//                  return false;
//              }
                if(sudokuData[i][j] == 0) {
                    return false;
                }
            }
        }
        return validate(sudokuData);
//      return true;
    }

    public void setOrginData(int[][] orginData) {
        this.orginData = orginData;
    }

    public int[][] getOrginData() {
        return orginData;
    }

    public void setSudokuData(int[][] sudokuData) {
        this.sudokuData = sudokuData;
    }

    public int[][] getSudokuData() {
        return sudokuData;
    }

    public void setResultData(int[][] resultData) {
        this.resultData = resultData;
    }

    public int[][] getResultData() {
        return resultData;
    }

    int is_digital_match(int sudoku[][], int i, int j)
    {
        int temp = sudoku[i][j];
        int p, q;
        int m, n;

        for(p=0; p<9; p++)
            if(p!=i && sudoku[p][j]==temp)
                return 0;
        for(p=0; p<9; p++)
            if(p!=j && sudoku[i][p]==temp)
                return 0;

        p = i/3;
        q = j/3;
        for(m=p*3; m<p*3+3; m++)
            for(n=q*3; n<q*3+3; n++)
                if(m!=i && n!=j && sudoku[m][n]==temp)
                    return 0;

        return 1;
    }

    /*输出数独矩阵*/
    void sudoku_print(int sudoku[][])
    {
        int i,j;
        for(i=0; i<9; i++)
        {
            for(j=0; j<9; j++)
                System.out.print(sudokuData[i][j]);
            System.out.print("\n");
        }
    }

   private void getSudoku()
    {
        int i, j, temp=0;
        int k=0, num=0;
        Random rand = new Random();
        for(i = 0;i < 9;i++){
            for(j = 0;j < 9;j++){
                sudokuData[i][j] = 0;
            }
        }
    /*通过添加一些随机性，来每次生成不同的数独矩阵*/
    /*添加更多随机性，则生成的矩阵将更随即*/
        for(i=0; i<9; i++)
        {
            temp = rand.nextInt() % 81;
            sudokuData[temp/9][temp%9] = i+1;
        }
    /*回溯法，构造数独矩阵*/
        while(true)
        {
            i = k/9;
            j = k%9;

            while(true)
            {
                System.out.print("========================i,j = " + i + ","+j);
                sudokuData[i][j]++;
                if(sudokuData[i][j] == 10)
                {
                    sudokuData[i][j] = 0;
                    --k;
                    break;
                }
                else if(validateIandJ(sudokuData, i, j))
                {
                    ++k;
                    break;
                }
            }

            if(k == 81)
            {
                sudoku_print(sudokuData);
                if(num >= SUDOKU_NUM)
                    return;

                --k;
            }
        }
    }
}

