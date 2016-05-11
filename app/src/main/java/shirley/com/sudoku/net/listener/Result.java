package shirley.com.sudoku.net.listener;

import java.util.ArrayList;
import java.util.List;

import shirley.com.sudoku.net.model.BaseData;
public class Result {
	
	private List<BaseData> list ;

	public List<BaseData> getList() {
		return list;
	}

	public void setList(List<BaseData> list) {
		this.list = list;
	}
	
	public void add(BaseData data) {
		if(list == null){
			list = new ArrayList<BaseData>();
		}
		list.add(data);
	}
	
}
