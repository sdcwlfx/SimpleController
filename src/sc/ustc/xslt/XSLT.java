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
 * �ѵ㣺·�����⣬����jar�����xml��xsl
 * ����xslģ�潫xml�ļ�ת����html�ļ������浽xmlͰĿ¼�£�����html��·��pages/success_view.html
 * �ο���https://wangmengbk.iteye.com/blog/706469
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
		
		//ʵ��������
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		try {
			//�õ�DocumentBuilder����
			DocumentBuilder dBuilder=dbf.newDocumentBuilder();
			//����xml�ļ�������ת��ΪDocument����
			Document document=dBuilder.parse(xmlPath);
			//ʵ��DOMSource����
			DOMSource source=new DOMSource(document);
			//�������������������·��
			StreamResult result=new StreamResult(new File(htmlPath));
			//����xsl�ļ�
			StreamSource streamSource=new StreamSource(new File(xslPath));
			//ʵ����TransformerFactory����
			TransformerFactory transformerFactory=TransformerFactory.newInstance();
			Transformer transformer=transformerFactory.newTransformer(streamSource);
			
			//����д���ַ���
			StringWriter stringWriter=new StringWriter();
			Result resulted=new StreamResult(stringWriter);
			//�趨�ַ����뷽ʽ
			transformer.setOutputProperty("encoding", "utf-8");
			//������õ�xsl��ʽת��
			transformer.transform(source, result);//��html������浽htmlPath·���£�/F:/ustcsse/J2EE/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/UseSC/pages/success_view.html
			transformer.transform(source, resulted);//����̨�ַ������
			String str=null;
			//�����дΪһ���ַ�������ӡ������̨
			str=stringWriter.toString();
			System.out.println(str);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resultHtmlPath;// pages/success_view.html
	}
	
}
