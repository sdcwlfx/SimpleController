package sc.ustc.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/**
 * 负责将对象操作映射为数据表操作，即在 Conversation 中定义数据操作 CRUD 方法，
 * 每个方法将对象操 作解释成目标数据库的 DML 或 DDL，通过 JDBC 完成数据持久化
 * @author 追枫萨
 *
 */
public class Conversation {
	//查询语句
	public static Object selectObject(Object object) {
		Class<?> cla=object.getClass();//UseBean类
		//依据类名获取该类的所有配置 cla.getName()：UseBean类的类名 https://www.cnblogs.com/wjf0/p/8098109.html 
		OR_class or_class=Configuration.getClassConfig(cla.getName());
		System.out.println("类名："+cla.getName());
		if(or_class==null) {
			return null;
		}
		//获取对应表
		String table=or_class.getTable();
		System.out.println("表明："+table);
		//获取配置文件中该类的所有属性
		List<List<String>> propertyList=or_class.getPropertyList();
		//得到object的属性及值
		List<List<String>> fieldValueList=new ArrayList<List<String>>();
		for (int i = 0; i < propertyList.size(); i++) {
			try {
				//获取userName
				System.out.println("propertyList.get(i).get(1):  "+propertyList.get(i).get(0));
				Field field=cla.getDeclaredField(propertyList.get(i).get(0));
				//设置可访问
				field.setAccessible(true);
				//返回指定对象object上由此field表示的字段的值,即userName字段的值
				//https://www.yiibai.com/javareflect/javareflect_field_get.html
				String fieldString=(String)field.get(object);
				System.out.println("fieldString:  "+fieldString);
				if(fieldString!=null) {
					List<String> sList=new ArrayList<String>();
					//对应的表中的列的值
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
		//构造查询sql
		StringBuilder sqlBuilder=new StringBuilder();
		sqlBuilder.append("select ");
		//设置要查询的属性字段
		for(int i=0;i<propertyList.size();i++) {
			//属性设置为非懒加载时，从数据库中选择该字段，否则不选择该字段
			if(propertyList.get(i).get(3).equals("false")) {
				sqlBuilder.append(propertyList.get(i).get(1)+","); //列
			}
		}
		//去除最后的,号
		sqlBuilder.deleteCharAt(sqlBuilder.length()-1);
		sqlBuilder.append(" from "+table+" where ");
		for(int i=0;i<fieldValueList.size();i++) {
			//if(fieldValueList.get(i).get(3).equals("false")) {
			sqlBuilder.append(fieldValueList.get(i).get(0)+" =? and ");
			
		}
		//去除最后的空格+and+空格共5位
		sqlBuilder.delete(sqlBuilder.length()-5, sqlBuilder.length()-1);
		try {
			//链接DB
			Connection connection=getConnection();
			//select
			PreparedStatement pStatement=connection.prepareStatement(sqlBuilder.toString());
			for (int i = 0; i < fieldValueList.size(); i++) {
				//为sql语句中占位符赋值
				pStatement.setString(i+1, fieldValueList.get(i).get(1));
			}
			System.out.println("sql查询语句：" +sqlBuilder.toString());
			return pStatement.executeQuery();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}
	//依据id删除用户，这里系统并没实现，因为在实现查询时以用户名作为主键
	public static boolean deleteObjectById(Object object) {
		try {
			//通过反射机制得到object的id属性
			Class<?> cla=object.getClass();//UserBean类
			Field id=cla.getDeclaredField("userId");//获得userId域
			id.setAccessible(true);
			String idString=(String)id.get(object);//获得object对象域id的值
			//解析xml文件得到对应数据库表
			OR_class or_class=Configuration.getClassConfig(cla.getName());//依据类名
			String table=or_class.getTable();
			//获取该类所有属性
			List<List<String>> propertyList=or_class.getPropertyList();
			String tableId=null;
			for (int i = 0; i < propertyList.size(); i++) {
				if (propertyList.get(i).get(0).equals("userId")) {
					//获取表的主键
					tableId=propertyList.get(i).get(1);
				}
			}
			//链接DB
			Connection connection=getConnection();
			//delete
			String sql="delete from "+table+" where "+tableId+"=?";
			PreparedStatement pStatement=connection.prepareStatement(sql);
			pStatement.setString(1, idString);//删除传入对象的id的记录
			return pStatement.executeUpdate()==1;
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	//链接数据库
	private static Connection getConnection() {
		//获取连接数据库所需配置信息：driver、url、数据库用户名、数据库密码
		Map<String, String> jdbcMap=Configuration.getJDBCConfig();
		Connection connection=null;
		try {
			Class.forName(jdbcMap.get("driver_class"));
			//获取配置信息中数据库路径、用户名、密码
			connection=DriverManager.getConnection(jdbcMap.get("url_path"), jdbcMap.get("db_username"), jdbcMap.get("db_userpassword"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
		
	}
	
}
