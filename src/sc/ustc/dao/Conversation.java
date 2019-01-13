package sc.ustc.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/**
 * ���𽫶������ӳ��Ϊ���ݱ���������� Conversation �ж������ݲ��� CRUD ������
 * ÿ������������� �����ͳ�Ŀ�����ݿ�� DML �� DDL��ͨ�� JDBC ������ݳ־û�
 * @author ׷����
 *
 */
public class Conversation {
	//��ѯ���
	public static Object selectObject(Object object) {
		Class<?> cla=object.getClass();//UseBean��
		//����������ȡ������������� cla.getName()��UseBean������� https://www.cnblogs.com/wjf0/p/8098109.html 
		OR_class or_class=Configuration.getClassConfig(cla.getName());
		System.out.println("������"+cla.getName());
		if(or_class==null) {
			return null;
		}
		//��ȡ��Ӧ��
		String table=or_class.getTable();
		System.out.println("������"+table);
		//��ȡ�����ļ��и������������
		List<List<String>> propertyList=or_class.getPropertyList();
		//�õ�object�����Լ�ֵ
		List<List<String>> fieldValueList=new ArrayList<List<String>>();
		for (int i = 0; i < propertyList.size(); i++) {
			try {
				//��ȡuserName
				System.out.println("propertyList.get(i).get(1):  "+propertyList.get(i).get(0));
				Field field=cla.getDeclaredField(propertyList.get(i).get(0));
				//���ÿɷ���
				field.setAccessible(true);
				//����ָ������object���ɴ�field��ʾ���ֶε�ֵ,��userName�ֶε�ֵ
				//https://www.yiibai.com/javareflect/javareflect_field_get.html
				String fieldString=(String)field.get(object);
				System.out.println("fieldString:  "+fieldString);
				if(fieldString!=null) {
					List<String> sList=new ArrayList<String>();
					//��Ӧ�ı��е��е�ֵ
					sList.add(propertyList.get(i).get(1));
					//userName
					sList.add(fieldString);
					fieldValueList.add(sList);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		//�����ѯsql
		StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("select ");
		//����Ҫ��ѯ�������ֶ�
		for(int i=0;i<propertyList.size();i++) {
			//��������Ϊ��������ʱ�������ݿ���ѡ����ֶΣ�����ѡ����ֶ�
			if(propertyList.get(i).get(3).equals("false")) {
				sqlBuilder.append(propertyList.get(i).get(1)+","); //��
			}
		}
		//ȥ������,��
		sqlBuilder.deleteCharAt(sqlBuilder.length()-1);
		sqlBuilder.append(" from "+table+" where ");
		for(int i=0;i<fieldValueList.size();i++) {
			//if(fieldValueList.get(i).get(3).equals("false")) {
			sqlBuilder.append(fieldValueList.get(i).get(0)+" =? and ");
			
		}
		//ȥ�����Ŀո�+and+�ո�5λ
		sqlBuilder.delete(sqlBuilder.length()-5, sqlBuilder.length()-1);
		try {
			//����DB
			Connection connection=getConnection();
			//select
			PreparedStatement pStatement=connection.prepareStatement(sqlBuilder.toString());
			for (int i = 0; i < fieldValueList.size(); i++) {
				//Ϊsql�����ռλ����ֵ
				pStatement.setString(i+1, fieldValueList.get(i).get(1));
			}
			System.out.println("sql��ѯ��䣺" +sqlBuilder.toString());
			return pStatement.executeQuery();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}
	//����idɾ���û�������ϵͳ��ûʵ�֣���Ϊ��ʵ�ֲ�ѯʱ���û�����Ϊ����
	public static boolean deleteObjectById(Object object) {
		try {
			//ͨ��������Ƶõ�object��id����
			Class<?> cla=object.getClass();//UserBean��
			Field id=cla.getDeclaredField("userId");//���userId��
			id.setAccessible(true);
			String idString=(String)id.get(object);//���object������id��ֵ
			//����xml�ļ��õ���Ӧ���ݿ��
			OR_class or_class=Configuration.getClassConfig(cla.getName());//��������
			String table=or_class.getTable();
			//��ȡ������������
			List<List<String>> propertyList=or_class.getPropertyList();
			String tableId=null;
			for (int i = 0; i < propertyList.size(); i++) {
				if (propertyList.get(i).get(0).equals("userId")) {
					//��ȡ�������
					tableId=propertyList.get(i).get(1);
				}
			}
			//����DB
			Connection connection=getConnection();
			//delete
			String sql="delete from "+table+" where "+tableId+"=?";
			PreparedStatement pStatement=connection.prepareStatement(sql);
			pStatement.setString(1, idString);//ɾ����������id�ļ�¼
			return pStatement.executeUpdate()==1;
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	//�������ݿ�
	private static Connection getConnection() {
		//��ȡ�������ݿ�����������Ϣ��driver��url�����ݿ��û��������ݿ�����
		Map<String, String> jdbcMap=Configuration.getJDBCConfig();
		Connection connection=null;
		try {
			Class.forName(jdbcMap.get("driver_class"));
			//��ȡ������Ϣ�����ݿ�·�����û���������
			connection=DriverManager.getConnection(jdbcMap.get("url_path"), jdbcMap.get("db_username"), jdbcMap.get("db_userpassword"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
		
	}
	
}
