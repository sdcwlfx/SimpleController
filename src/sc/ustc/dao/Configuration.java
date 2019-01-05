package sc.ustc.dao;
/**
 * �������UseSC���̵�����or_mapping.xml
 * @author ׷����
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
	
	//��ȡxml��doc����
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
	
	//����xml�ļ��е����ݿ�������Ϣ
	public static Map<String, String> getJDBCConfig(){
		Map<String, String> jdbcConfig=new HashMap<String, String>();
		Document document=getDocument();
		try {
			NodeList jdbcList=document.getElementsByTagName("jdbc");
			NodeList jdbcPropertys=((Element)jdbcList.item(0)).getElementsByTagName("property");
			
			for (int i = 0; i < jdbcPropertys.getLength(); i++) {
				//��ȡ��������
				String propertyName=((Element)jdbcPropertys.item(i)).getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
				//��ȡ����ֵ
				String propertyValue=((Element)jdbcPropertys.item(i)).getElementsByTagName("value").item(0).getFirstChild().getNodeValue();
				//��������������ֵ����map
				jdbcConfig.put(propertyName, propertyValue);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jdbcConfig;
	}
	
	//����xml�ļ�����ӳ������
	public static OR_class getClassConfig(String className) {
		OR_class or_class=new OR_class();
		Document document=getDocument();
		NodeList classList=document.getElementsByTagName("class");
		
		//����class�ڵ�
		for (int i = 0; i < classList.getLength(); i++) {
			//��ȡclass�ڵ�
			Element classElement=(Element)classList.item(i);
			//�ҵ���ӦclassName��class�ڵ�
			if(classElement.getAttribute("name").equals(className)) {
				//��������
				or_class.setName(className);
				//���ñ���
				or_class.setTable(classElement.getAttribute("table"));
				//��ȡclass��Ӧ���������Խڵ�
				NodeList propertyList=classElement.getElementsByTagName("property");
				//�洢��������������
				List<List<String>> propertyLists=new ArrayList<List<String>>();
				for (int j = 0; j <propertyList.getLength(); j++) {
					List<String> property=new ArrayList<String>();
					
					String name=((Element)propertyList.item(j)).getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
					System.out.println("xml����name����ֵ��"+name);
					property.add(name);
					
					String column=((Element)propertyList.item(j)).getElementsByTagName("column").item(0).getFirstChild().getNodeValue();
					System.out.println("xml����column����ֵ��"+column);
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
