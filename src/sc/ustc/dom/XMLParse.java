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
 * dom4j+jaxen组合xpath方式解析controller.xml文件
 * @author asus
 *
 */
public class XMLParse {
	//返回doc文档
	public static Document parse(URL url) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(url);
        return document;
    }
	
	
	//返回属性name满足条件的action节点，否则返回null
	public static Node getNode(URL url,String actionName) throws DocumentException{
		SAXReader reader = new SAXReader();
        Document document = reader.read(url);
        Element root=document.getRootElement();
//        List<Element> list = root.elements("student");
        
        System.out.println("root: "+root.getName());//根元素名字：sc-configuration
        //获取name属性满足条件的action节点
        Node node=document.selectSingleNode("/sc-configuration/controller/action[@name='"+actionName.toLowerCase()+"']");//actionName不区分大小写
        //Node node1=document.selectSingleNode("//action[@name='"+actionName+"']");//选择name满足搜索条件的action节点
        return node;
	}
	
	//根据返回result例如“success”获取element下的result元素
	public static Element getResultElement(Element element,String result,String actionName) throws DocumentException{
		Document document=element.getDocument();//
		System.out.println("element: "+element.asXML());
		Element resultElement=(Element)document.selectSingleNode("/sc-configuration/controller/action[@name='"+actionName.toLowerCase()+"']/result[@name='"+result+"']");//获取name属性等于result的result元素
		//Element resultElement=(Element)document.selectSingleNode("/action/result[@name='"+result+"']");
		if(resultElement!=null) {
			System.out.println(resultElement.asXML());
		}
		return resultElement;
		
	}
	
	
	
	//检查action是否配置了拦截器，若配置了拦截器，将拦截器元素返回
	public static Element getInterceptorElement(URL url,String actionName) throws DocumentException{
		Element interceptorElement=null;
		Element interceptorRef=null;
		SAXReader reader = new SAXReader();
        Document document = reader.read(url);
        Element root=document.getRootElement();
        //获取name属性满足条件的action节点
        Element actionElement=(Element)document.selectSingleNode("/sc-configuration/controller/action[@name='"+actionName.toLowerCase()+"']");//actionName不区分大小写
        if(actionElement!=null) {
        	interceptorRef=actionElement.element("interceptor-ref");//获取action下子元素interceptor-ref
        }
        if(interceptorRef!=null) {//若配置了拦截器，则将拦截器元素返回
        	String interceptorName=interceptorRef.attributeValue("name");//获取拦截器name属性
        	interceptorElement=(Element)document.selectSingleNode("/sc-configuration/interceptor[@name='"+interceptorName+"']");//在xml文件找到对应name的拦截器元素
        	
        }
		return interceptorElement;
		
	}
	
	
	
	/**
	 * e7解析di.xml文件,查找与action同名的bean元素，找不到返回null
	 * @param url di.xml文件路径
	 * @param actionName  请求的action名字,对应bean元素节点中id属性
	 * @return 找不到元素时返回null
	 * @throws DocumentException
	 */
	public static Element getBeanElementFromDI(URL url,String actionName) throws DocumentException{
		
		SAXReader reader = new SAXReader();
        Document document = reader.read(url);
		//xpath方式查询bean元素
		Element resultElement=(Element)document.selectSingleNode("/sc-di/bean[@id='"+actionName.toLowerCase()+"']");//获取id属性等于actionName的bean元素
		//Element resultElement=(Element)document.selectSingleNode("/action/result[@name='"+result+"']");
		if(resultElement!=null) {
			System.out.println(resultElement.asXML());
		}
		return resultElement;
		
	}
	
	
	/**
	 * e7 获取指定bean下的域名和域对应的bean元素，这里只适用于一个被依赖bean的情况
	 * @param url di.xml路径
	 * @param actionName 对应bean元素中id属性
	 * @return 返回域名及对应bean元素
	 * @throws DocumentException
	 */
	public static Map<String, String> getFieldElement(URL url,String actionName)throws DocumentException{
		Map<String, String> fieldMap=new HashMap<>();
		SAXReader reader = new SAXReader();
        Document document = reader.read(url);
        Element fieldElement=null;
		//xpath方式查询bean元素
		Element resultElement=(Element)document.selectSingleNode("/sc-di/bean[@id='"+actionName.toLowerCase()+"']");//获取id属性等于actionName的bean元素
		//Element resultElement=(Element)document.selectSingleNode("/action/result[@name='"+result+"']");
		if(resultElement!=null) {
			System.out.println(resultElement.asXML());
			fieldElement=(Element)document.selectSingleNode("/sc-di/bean[@id='"+actionName.toLowerCase()+"']/field");//获取id属性等于actionName的bean元素下的field域元素
			//若含有field域
			if(fieldElement!=null) {
				String fieldName=fieldElement.attributeValue("name");//域名
				String fieldBeanRef=fieldElement.attributeValue("bean-ref");//域类型
				Element beanRefElement=(Element)document.selectSingleNode("/sc-di/bean[@id='"+fieldBeanRef+"']");
				//将域名和域的类型放入Map对象
				fieldMap.put("name", fieldName);
				fieldMap.put("class", beanRefElement.attributeValue("class"));
				System.out.println("域名： "+fieldName+"    域类型：   "+beanRefElement.attributeValue("class"));
			}
		}
		return fieldMap;
	}
	
	/**
	 * e7 得到依赖的bean的域类型id
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
        
		//xpath方式查询bean元素
		Element resultElement=(Element)document.selectSingleNode("/sc-di/bean[@id='"+actionName.toLowerCase()+"']");//获取id属性等于actionName的bean元素
		//Element resultElement=(Element)document.selectSingleNode("/action/result[@name='"+result+"']");
		if(resultElement!=null) {
			System.out.println(resultElement.asXML());
			fieldElement=(Element)document.selectSingleNode("/sc-di/bean[@id='"+actionName.toLowerCase()+"']/field");//获取id属性等于actionName的bean元素下的field域元素
			//若含有field域
			if(fieldElement!=null) {
				
				fieldBeanRef=fieldElement.attributeValue("bean-ref");//域类型
				
			}
		}
		return fieldBeanRef;
	}
	
	
	
	
	

}
