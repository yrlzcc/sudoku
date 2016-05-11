package shirley.com.sudoku.net.model;

import shirley.com.sudoku.net.NetManager;

public abstract class BaseData {

	public Object _key;

	protected String urlSuffix = "";
	public String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	// public String message;
	//
	// public String getMessage() {
	// return message;
	// }
	//
	// public void setMessage(String message) {
	// this.message = message;
	// }

	public String getUrl() {
		return NetManager.BASE_URL + urlSuffix;
	}
}
