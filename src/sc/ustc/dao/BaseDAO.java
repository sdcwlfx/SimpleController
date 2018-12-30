package sc.ustc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseDAO {
	
	//���ݿ�������
	protected String driver;
	//���ݿ����·��
	protected String url;
	//���ݿ��û���
	protected String userName;
	//���ݿ��û�����
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
	
	//��������ݿ�����
	public Connection openDBConnection() {
		
		try {
			//����MySQL����
			Class.forName(driver);
			//�����ݿ⽨������
			connection=DriverManager.getConnection(url, userName, userPassword);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
		
	//����ر����ݿ�����
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
	
	//ִ��sql��䣬���ز�ѯ�������
	public abstract Object query(String sql);
	
	//ִ��sql��䣬���ز���������
	public abstract boolean insert(String sql);
	
	//ִ��sql��䣬���ظ��²������
	public abstract boolean update(String sql);
	
	//ִ��sql��䣬����ɾ���������
	public abstract boolean delete(String sql);
	
	
	

}
