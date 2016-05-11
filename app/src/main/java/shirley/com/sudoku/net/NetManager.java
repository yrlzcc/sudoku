package shirley.com.sudoku.net;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import cn.safetrip.edog.net.NET;
import cn.safetrip.edog.net.NetException;
import cn.safetrip.edog.net.RequestParams;
import shirley.com.sudoku.net.listener.RespListener;
import shirley.com.sudoku.net.model.BaseData;
import shirley.com.sudoku.utils.ProtocalUtils;

public class NetManager {
	private static final String TAG = NetManager.class.getSimpleName();
	// 正式环境
	public static final String BASE_HOST = "http://123.126.92.26:8080/time/mobile.shtml?";
	// public static final String BASE_HOST =
	// "http://111.207.40.115/time/mobile.shtml?";
	// 上传URL
	public static final String ZIP_URL = "http://orbit.bang58.com/orbit/index.php?c=trace&m=daily";

	public static final String FILE_DOWN_URL = BASE_HOST + "c=util&m=download&file=";

	public static String BASE_URL;

	static {
		BASE_URL = BASE_HOST;
	}

	private boolean debuggable;
	private Context mContext;
	private RespListener handshakelistener;

	private NetManager() {
	}

	public synchronized static NetManager getInstance() {
		if (instance == null) {
			synchronized (NetManager.class) {
				if (instance == null) {
					instance = new NetManager();
				}
			}
		}
		return instance;
	}

	public boolean isDebuggable() {
		return debuggable;
	}

	public void setDebuggable(boolean debuggable) {
		this.debuggable = debuggable;
	}

	private static NetManager instance;

	/**
	 * 异步 采用task
	 * 
	 * @param data
	 * @param listener
	 *            用来回调的Listener
	 */
	public <D extends BaseData> NetTask requestByTask(D data, RespListener listener) {
		NetTask<D> task = new NetTask<D>(listener);
		task.execute(data);
		return task;
	}

	/**
	 * 同步请求 异步请求在线程内也是调用该方法
	 * 
	 * @param srcData
	 *            请求的对象
	 * @return
	 */
	public <D extends BaseData> D requestSync(D srcData) {
		if (srcData == null) {
			return null;
		}

		String rspJson = null;
		RequestParams netParams = ProtocalUtils.extractParams(srcData);

		String url = srcData.getUrl();
		// 增加token
		String newUrl = url;// ProtocalUtils.addEncrpt(null, url, netParams);

		try {
			rspJson = NET.postSync(newUrl, netParams);
		} catch (NetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
		}

		Log.d("json-url", newUrl + "");
		Log.d("json", rspJson + "");

		D object = null;
		// 将解析json为java对象
		if (rspJson != null) {
			Gson gson = new Gson();
			try {
				object = (D) gson.fromJson(rspJson, srcData.getClass());
			} catch (Exception e) {

			}
			if (object != null) {
				object._key = srcData._key;
			}

		}
		return object;

	}

	/**
	 * 同步下载文件 下载图片 头像 声音等
	 * 
	 * @param name
	 * @param isCached
	 * @param cachedTime
	 * @return
	 * @throws NetException
	 */
	public InputStream FileDownLoadSync(String name, boolean isCached, long cachedTime) throws NetException {
		String fileUrl = NetManager.FILE_DOWN_URL + name;
		byte[] buf = NET.getSyncWithBinary(isCached, cachedTime, fileUrl, null);
		if (buf != null) {
			ByteArrayInputStream baIns = new ByteArrayInputStream(buf);
			return baIns;
		}
		return null;
	}

	/**
	 * 下载实时路况看板
	 * 
	 * @param bid
	 *            bid
	 * @param with
	 *            宽
	 * @param height
	 *            高
	 * @param isCached
	 *            是否缓存
	 * @param cachedTime
	 *            缓存时间
	 * @return
	 * @throws NetException
	 */
	public InputStream TrafficImageDown(String bid, int with, int height, boolean isCached, long cachedTime) throws NetException {
		String url = BASE_HOST + "c=traffic&m=board&bid=" + bid + "&width=" + with + "&height=" + height;
		url = ProtocalUtils.addEncrpt(null, url, null);
		byte[] buf = NET.getSyncWithBinary(isCached, cachedTime, url, null);
		if (buf != null) {
			ByteArrayInputStream baIns = new ByteArrayInputStream(buf);
			return baIns;
		}
		return null;
	}

	public RespListener getHandshakelistener() {
		return handshakelistener;
	}

	/**
	 * 声音 图片 文件 下载的地址
	 * 
	 * @param name
	 *            名字
	 * @return
	 */
	public String getFileUrl(String name) {
		return NetManager.FILE_DOWN_URL + name;
	}
}
