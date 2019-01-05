package sc.ustc.dao;
/**
 * 负责解析UseSC工程的配置or_mapping.xml
 * @author 追枫萨
 *
 */

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Configuration {
	
	private static String or_mapping_path=Thread.currentThread().getContextClassLoader().getResource("../../pages/or_mapping.xml").getPath();
	
	//获取xml的doc描述
	public static Document getDocument() {
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder=null;
		Document document=null;
		try {
			documentBuilder = dbf.newDocumentBuilder();
			document=documentBuilder.parse(new File(or_mapping_path));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}
	
	//解析xml文件中的数据库配置信息
	public static Map<String, String> getJDBCConfig(){
		Map<String, String> jdbcConfig=new HashMap<String, String>();
		Document document=getDocument();
		try {
			NodeList jdbcList=document.getElementsByTagName("jdbc");
			NodeList jdbcPropertys=((Element)jdbcList.item(0)).getElementsByTagName("property");
			
			for (int i = 0; i < jdbcPropertys.getLength(); i++) {
				//获取属性名字
				String propertyName=((Element)jdbcPropertys.item(i)).getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
				//获取属性值
				String propertyValue=((Element)jdbcPropertys.item(i)).getElementsByTagName("value").item(0).getFirstChild().getNodeValue();
				//将属性名及属性值存入map
				jdbcConfig.put(propertyName, propertyValue);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jdbcConfig;
	}
	
	//解析xml文件中类映射配置
	public static OR_class getClassConfig(String className) {
		OR_class or_class=new OR_class();
		Document document=getDocument();
		NodeList classList=document.getElementsByTagName("class");
		
		//遍历class节点
		for (int i = 0; i < classList.getLength(); i++) {
			//获取class节点
			Element classElement=(Element)classList.item(i);
			//找到对应className的class节点
			if(classElement.getAttribute("name").equals(className)) {
				//设置类名
				or_class.setName(className);
				//设置表明
				or_class.setTable(classElement.getAttribute("table"));
				//获取class对应表所有属性节点
				NodeList propertyList=classElement.getElementsByTagName("property");
				//存储该类表的所有属性
				List<List<String>> propertyLists=new ArrayList<List<String>>();
				for (int j = 0; j <propertyList.getLength(); j++) {
					List<String> property=new ArrayList<String>();
					
					String name=((Element)propertyList.item(j)).getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
					System.out.println("xml解析name属性值："+name);
					property.add(name);
					
					String column=((Element)propertyList.item(j)).getElementsByTagName("column").item(0).getFirstChild().getNodeValue();
					System.out.println("xml解析column属性值："+column);
					property.add(column);
					
					String type=((Element)propertyList.item(j)).getElementsByTagName("type").item(0).getFirstChild().getNodeValue();
					property.add(type);
					
					String lazy=((Element)propertyList.item(j)).getElementsByTagName("lazy").item(0).getFirstChild().getNodeValue();
					property.add(lazy);
					propertyLists.add(property);
				}
				or_class.setPropertyList(propertyLists);
				return or_class;
			}
			
		}
		return null;
		
		
		
	}
	
	

}
