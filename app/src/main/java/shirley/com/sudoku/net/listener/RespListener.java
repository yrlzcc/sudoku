package shirley.com.sudoku.net.listener;

import shirley.com.sudoku.net.model.BaseData;

public interface RespListener {
	
	/**
	 * 200的回调
	 * @param data
	 */
	 void onSuccess(BaseData data);
	/**
	 * 网络错误的回调
	 * @param data 网络错误返回的data是没有的 回调是传入的是请求的data
	 */
	void onNetError(BaseData data);
	/**
	 * 失败的回调 非200 非403 非408
	 * @param data
	 * @return 如果返回true 则重发当前协议
	 */
	boolean onFailed(BaseData data);
	
}
