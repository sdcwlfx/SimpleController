package sc.ustc.dao;

import java.util.List;

/**
 * e6 存储映射类属性
 * @author 追枫萨
 *
 */
public class OR_class{
	//useBean类名
	private String name;
	//表名字
	private String table;
	//表属性
	private List<List<String>> propertyList;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public List<List<String>> getPropertyList() {
		return propertyList;
	}
	public void setPropertyList(List<List<String>> propertyList) {
		this.propertyList = propertyList;
	}
	
	
	
	

}
