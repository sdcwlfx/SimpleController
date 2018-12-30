package sc.ustc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseDAO {
	
	//数据库驱动类
	protected String driver;
	//数据库访问路径
	protected String url;
	//数据库用户名
	protected String userName;
	//数据库用户密码
	protected String userPassword;
	
	private Connection connection;
	
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
	//负责打开数据库连接
	public Connection openDBConnection() {
		
		try {
			//加载MySQL驱动
			Class.forName(driver);
			//与数据库建立连接
			connection=DriverManager.getConnection(url, userName, userPassword);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
		
	//负责关闭数据库连接
	public boolean closeDBConnection() {
		boolean isClosed=false;
		try {
			connection.close();
			if(connection.isClosed()) {
				isClosed=true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isClosed;
	}
	
	//执行sql语句，返回查询结果对象
	public abstract Object query(String sql);
	
	//执行sql语句，返回插入操作结果
	public abstract boolean insert(String sql);
	
	//执行sql语句，返回更新操作结果
	public abstract boolean update(String sql);
	
	//执行sql语句，返回删除操作结果
	public abstract boolean delete(String sql);
	
	
	

}
