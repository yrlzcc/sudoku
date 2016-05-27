package shirley.com.sudoku.utils;

import android.content.Context;
import android.content.res.AssetManager;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import shirley.com.sudoku.model.SudokuData;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ReadSudokuUtil {

    private Context context;

    public ReadSudokuUtil(Context context){
        this.context = context;
    }

    public SudokuData read() {
        // 把assert里的文件拷到sd卡上相应目录
        AssetManager mag = context.getAssets();
        InputStream in = null;
        int ilen = 0;
        try {
            in = mag.open("data.txt");
            ilen = in.available();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] buffer = new byte[ilen];
        try {
            in.read(buffer);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        String str = new String(buffer);
        Gson g = new Gson();
        SudokuData data = g.fromJson(str, SudokuData.class);
        return data;
    }
}
