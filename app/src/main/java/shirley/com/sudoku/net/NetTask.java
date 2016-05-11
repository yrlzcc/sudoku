package shirley.com.sudoku.net;

import android.os.AsyncTask;

import shirley.com.sudoku.net.listener.RespListener;
import shirley.com.sudoku.net.model.BaseData;

public class NetTask<T extends BaseData> extends AsyncTask<T, Integer, T> {
	private T data;
	private RespListener listener;

	public NetTask(RespListener listener) {
		super();
		this.listener = listener;
	}

	@Override
	protected T doInBackground(T... params) {
		data = params[0];

		T object = NetManager.getInstance().requestSync(data);
		return object;
	}

	protected void onPostExecute(T result) {
		if (listener == null) {
			return;
		}
		if (result == null) {
			// 网络连接失败
			listener.onNetError(data);
		} else if (!"-1".equals(result.getResult())) {

			listener.onSuccess(result);
		} else {
			boolean flag = listener.onFailed(result);
			if (flag) {
				NetManager.getInstance().requestByTask(data, listener);
			}
		}

	};
}
