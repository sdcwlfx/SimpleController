package sc.ustc.xslt;

import java.io.File;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
/**
 * 难点：路径问题，访问jar包外的xml和xsl
 * 依据xsl模版将xml文件转换成html文件并保存到xml桶目录下，返回html的路径pages/success_view.html
 * 参考：https://wangmengbk.iteye.com/blog/706469
 * @author asus
 *
 */
public class XSLT {

	//xmlPath :/F:/ustcsse/J2EE/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/UseSC/pages/success_view.xml
	public static String xslToHtml(String xmlPath) {
		String htmlPath="";
		String xslPath="";
		int end=xmlPath.lastIndexOf(".");
		String front=xmlPath.substring(0, end);
		htmlPath=front+".html";//
		xslPath=front+".xsl";
		
		
		String resultHtmlPath="pages"+htmlPath.substring(htmlPath.lastIndexOf("/"));
		System.out.println("resultHtmlPath:  "+resultHtmlPath);
		
		System.out.println("htmlPath:   "+htmlPath);///F:/ustcsse/J2EE/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/UseSC/pages/success_view.html
		
		//实例化对象
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		try {
			//得到DocumentBuilder对象
			DocumentBuilder dBuilder=dbf.newDocumentBuilder();
			//加载xml文件，将其转化为Document对象
			Document document=dBuilder.parse(xmlPath);
			//实例DOMSource对象
			DOMSource source=new DOMSource(document);
			//输出结果，并定义结果输出路径
			StreamResult result=new StreamResult(new File(htmlPath));
			//加载xsl文件
			StreamSource streamSource=new StreamSource(new File(xslPath));
			//实例化TransformerFactory对象
			TransformerFactory transformerFactory=TransformerFactory.newInstance();
			Transformer transformer=transformerFactory.newTransformer(streamSource);
			
			//定义写个字符串
			StringWriter stringWriter=new StringWriter();
			Result resulted=new StreamResult(stringWriter);
			//设定字符编码方式
			transformer.setOutputProperty("encoding", "utf-8");
			//将定义好的xsl格式转换
			transformer.transform(source, result);//将html输出保存到htmlPath路径下：/F:/ustcsse/J2EE/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/UseSC/pages/success_view.html
			transformer.transform(source, resulted);//控制台字符串输出
			String str=null;
			//将结果写为一个字符串，打印到控制台
			str=stringWriter.toString();
			System.out.println(str);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resultHtmlPath;// pages/success_view.html
	}
	
}
