package sc.ustc.dom;
import java.net.URL;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;




/**
 * dom4j+jaxen���xpath��ʽ����controller.xml�ļ�
 * @author asus
 *
 */
public class XMLParse {
	//����doc�ĵ�
	public static Document parse(URL url) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(url);
        return document;
    }
	
	
	//��������name����������action�ڵ㣬���򷵻�null
	public static Node getNode(URL url,String actionName) throws DocumentException{
		SAXReader reader = new SAXReader();
        Document document = reader.read(url);
        Element root=document.getRootElement();
//        List<Element> list = root.elements("student");
        
        System.out.println("root: "+root.getName());//��Ԫ�����֣�sc-configuration
        //��ȡname��������������action�ڵ�
        Node node=document.selectSingleNode("/sc-configuration/controller/action[@name='"+actionName.toLowerCase()+"']");//actionName�����ִ�Сд
        //Node node1=document.selectSingleNode("//action[@name='"+actionName+"']");//ѡ��name��������������action�ڵ�
        return node;
	}
	
	//���ݷ���result���硰success����ȡelement�µ�resultԪ��
	public static Element getResultElement(Element element,String result,String actionName) throws DocumentException{
		Document document=element.getDocument();//
		System.out.println("element: "+element.asXML());
		Element resultElement=(Element)document.selectSingleNode("/sc-configuration/controller/action[@name='"+actionName.toLowerCase()+"']/result[@name='"+result+"']");//��ȡname���Ե���result��resultԪ��
		//Element resultElement=(Element)document.selectSingleNode("/action/result[@name='"+result+"']");
		if(resultElement!=null) {
			System.out.println(resultElement.asXML());
		}
		return resultElement;
		
	}
	
	
	
	//���action�Ƿ���������������������������������������Ԫ�ط���
	public static Element getInterceptorElement(URL url,String actionName) throws DocumentException{
		Element interceptorElement=null;
		Element interceptorRef=null;
		SAXReader reader = new SAXReader();
        Document document = reader.read(url);
        Element root=document.getRootElement();
        //��ȡname��������������action�ڵ�
        Element actionElement=(Element)document.selectSingleNode("/sc-configuration/controller/action[@name='"+actionName.toLowerCase()+"']");//actionName�����ִ�Сд
        if(actionElement!=null) {
        	interceptorRef=actionElement.element("interceptor-ref");//��ȡaction����Ԫ��interceptor-ref
        }
        if(interceptorRef!=null) {//������������������������Ԫ�ط���
        	String interceptorName=interceptorRef.attributeValue("name");//��ȡ������name����
        	interceptorElement=(Element)document.selectSingleNode("/sc-configuration/interceptor[@name='"+interceptorName+"']");//��xml�ļ��ҵ���Ӧname��������Ԫ��
        	
        }
		return interceptorElement;
		
	}
	
	
	
	/**
	 * e7����di.xml�ļ�,������actionͬ����beanԪ�أ��Ҳ�������null
	 * @param url di.xml�ļ�·��
	 * @param actionName  �����action����,��ӦbeanԪ�ؽڵ���id����
	 * @return �Ҳ���Ԫ��ʱ����null
	 * @throws DocumentException
	 */
	public static Element getBeanElementFromDI(URL url,String actionName) throws DocumentException{
		
		SAXReader reader = new SAXReader();
        Document document = reader.read(url);
		//xpath��ʽ��ѯbeanԪ��
		Element resultElement=(Element)document.selectSingleNode("/sc-di/bean[@id='"+actionName.toLowerCase()+"']");//��ȡid���Ե���actionName��beanԪ��
		//Element resultElement=(Element)document.selectSingleNode("/action/result[@name='"+result+"']");
		if(resultElement!=null) {
			System.out.println(resultElement.asXML());
		}
		return resultElement;
		
	}
	
	
	/**
	 * e7 ��ȡָ��bean�µ����������Ӧ��beanԪ�أ�����ֻ������һ��������bean�����
	 * @param url di.xml·��
	 * @param actionName ��ӦbeanԪ����id����
	 * @return ������������ӦbeanԪ��
	 * @throws DocumentException
	 */
	public static Map<String, String> getFieldElement(URL url,String actionName)throws DocumentException{
		Map<String, String> fieldMap=new HashMap<>();
		SAXReader reader = new SAXReader();
        Document document = reader.read(url);
        Element fieldElement=null;
		//xpath��ʽ��ѯbeanԪ��
		Element resultElement=(Element)document.selectSingleNode("/sc-di/bean[@id='"+actionName.toLowerCase()+"']");//��ȡid���Ե���actionName��beanԪ��
		//Element resultElement=(Element)document.selectSingleNode("/action/result[@name='"+result+"']");
		if(resultElement!=null) {
			System.out.println(resultElement.asXML());
			fieldElement=(Element)document.selectSingleNode("/sc-di/bean[@id='"+actionName.toLowerCase()+"']/field");//��ȡid���Ե���actionName��beanԪ���µ�field��Ԫ��
			//������field��
			if(fieldElement!=null) {
				String fieldName=fieldElement.attributeValue("name");//����
				String fieldBeanRef=fieldElement.attributeValue("bean-ref");//������
				Element beanRefElement=(Element)document.selectSingleNode("/sc-di/bean[@id='"+fieldBeanRef+"']");
				//��������������ͷ���Map����
				fieldMap.put("name", fieldName);
				fieldMap.put("class", beanRefElement.attributeValue("class"));
				System.out.println("������ "+fieldName+"    �����ͣ�   "+beanRefElement.attributeValue("class"));
			}
		}
		return fieldMap;
	}
	
	/**
	 * e7 �õ�������bean��������id
	 * @param url
	 * @param actionName
	 * @return
	 * @throws DocumentException
	 */
	public static String getFieldRef(URL url,String actionName)throws DocumentException{
		
		SAXReader reader = new SAXReader();
        Document document = reader.read(url);
        String fieldBeanRef="";
        Element fieldElement=null;
        
		//xpath��ʽ��ѯbeanԪ��
		Element resultElement=(Element)document.selectSingleNode("/sc-di/bean[@id='"+actionName.toLowerCase()+"']");//��ȡid���Ե���actionName��beanԪ��
		//Element resultElement=(Element)document.selectSingleNode("/action/result[@name='"+result+"']");
		if(resultElement!=null) {
			System.out.println(resultElement.asXML());
			fieldElement=(Element)document.selectSingleNode("/sc-di/bean[@id='"+actionName.toLowerCase()+"']/field");//��ȡid���Ե���actionName��beanԪ���µ�field��Ԫ��
			//������field��
			if(fieldElement!=null) {
				
				fieldBeanRef=fieldElement.attributeValue("bean-ref");//������
				
			}
		}
		return fieldBeanRef;
	}
	
	
	
	
	

}
