package sc.ustc.dom;
import java.net.URL;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.util.List;




/**
 * dom4j方式解析controller.xml文件
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
	
	

}
