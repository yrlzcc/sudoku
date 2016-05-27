package shirley.com.sudoku;

import android.app.Application;

import com.pgyersdk.crash.PgyCrashManager;

import sdw.sea.erd.AdManager;
import sdw.sea.erd.normal.spot.SpotManager;
import shirley.com.sudoku.model.SudokuData;
import shirley.com.sudoku.utils.ReadSudokuUtil;

/**
 * Created by Administrator on 2016/5/5.
 */
public class BaseApplication extends Application {

        public static final String TAG = BaseApplication.class.getSimpleName();
        private static BaseApplication application;
        public SudokuData sudokuData = null;
        public static BaseApplication getInstance(){
            return application;
        }

        @Override
        public void onCreate() {
            // TODO Auto-generated method stub
            super.onCreate();
            application = this;
            PgyCrashManager.register(this);

            AdManager.getInstance(this).init("b0935234b0c9d58f", "d5372ef76aaab0a4", false, true);
            SpotManager.getInstance(this).setSpotOrientation(SpotManager.ORIENTATION_PORTRAIT);


        }

}
