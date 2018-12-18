package sc.ustc.controller;

import java.io.IOException;
//import java.io.PrintWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.BufferedReader;
import sc.ustc.dom.XMLParse;
import sc.ustc.util.InvokeReflection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.io.SAXReader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
/**
 * Servlet implementation class SimpleController
 */
@WebServlet("/Controller")
public class SimpleController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SimpleController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	    doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out=response.getWriter();
		String actionURI=request.getRequestURI();//    ���ݣ�/UseSC/Login.sc
		System.out.println(actionURI);
		System.out.println(actionURI.length());
		
		//��ȡaction����
		int actionBegin=actionURI.lastIndexOf("/");//��ȡuri���ġ�/����λ��
		int actionEnd=actionURI.lastIndexOf(".");//������'.'��λ��
		if(actionBegin<0||actionEnd<0) {
			String pathGetClass =this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			System.out.println("pathGetClass: "+pathGetClass);	
			out.println("<html><head><title>SimpleController</title></head><body>404!!!δ�ҵ�����ҳ��Controller!</body></html>");
		}else {
			String actionName=actionURI.substring(actionBegin+1,actionEnd);//��ȡ�����action���֣�����login
			String pathGetClass =this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			System.out.println("pathGetClass: "+pathGetClass);
			//�ɹ���ȡjar��ͬĿ¼��controller.xml�ļ����� �ο��� https://blog.csdn.net/T1DMzks/article/details/75099029
//			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("controller.xml");
//            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));         
//            String line="";
//            while((line=br.readLine())!=null){
//                System.out.println("getClassLoader: "+line);
//            }
			//��ȡ����jar���Ĺ��̵������ļ�controller.xml��URL
			URL controllerUrl = getClass().getClassLoader().getResource("controller.xml");
//            String controllerPath=controllerUrl.getPath();//��Դ·��
            
			
			
			
            //dom4j+jaxen��ʽ����xml�ļ��ο���https://www.cnblogs.com/warrior4236/p/5863024.html
            try {
            	System.out.println("controllerUrl: "+controllerUrl+"      actionName: "+actionName);
				Node node=XMLParse.getNode(controllerUrl, actionName);
				//δ�ҵ�ָ����action
				if(node==null) {
					out.println("<html><head><title>SimpleController</title></head><body>404!!! ����ʶ���Action����!!!</body></html>");
				}else {
//					String goalClass=node.valueOf("class");//��ȡ�ڵ�������Ϊclass�ĵ�ֵ
//					String goalMethod=node.valueOf("method");//��ȡ�ڵ�����Ϊmethod��ֵ
					Element goalElement=(Element)node;//���ڵ�ת��ΪԪ��,���������action���ӽڵ�
					System.out.println("goalElement: "+goalElement.asXML());
					
					//ʹ��java�������jar�����Action���з���
					String goalActionMethod=goalElement.attributeValue("method");//��ȡĿ��Ԫ������Ϊmethod��ֵ
					String goalAction=goalElement.attributeValue("class");//��ȡĿ��Ԫ������Ϊclass��ֵ
					System.out.println("goalAction: "+goalAction+"    goalActionMethod: "+goalActionMethod);
					String result=InvokeReflection.invokeActionReflection(goalAction, goalActionMethod);//java�������jar���⣨��UserSC���̣���Action��goalAction������ָ��������goalActionMethod��������÷������ؽ��
					
					//����actionName����Ԫ��result��Ӧ����Ӧҳ��
					Element resultElement=XMLParse.getResultElement(goalElement, result,actionName);//��ȡ��resultԪ��   ??????goalElement��actionԪ�ص����ݣ������������д�ӡȷ������xml����
					//if()
					String resultType=resultElement.attributeValue("type");//type����
					String resultValue=resultElement.attributeValue("value");//��Ӧҳ��
					System.out.println("resultType: "+resultType+"    "+"resultValue: "+resultValue);
					if(resultType.endsWith("forward")) {
						RequestDispatcher requestDispatcher=request.getRequestDispatcher(resultValue);//��ȡ����ת�������󣬸�ת������ָ��ͨ��getRequestDisPatcher()�Ĳ�������
						requestDispatcher.forward(request, response);//ת������
					}else {
						response.sendRedirect(resultValue);//�ض���ת������
					}
					
					
					//out.println("<html><head><title>SimpleController</title></head><body>find it!!</body></html>");
				}
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
   
			System.out.println((actionBegin+1)+"   "+actionEnd);
			System.out.println(actionURI.substring(actionBegin+1,actionEnd));
			//e1out.println("<html><head><title>SimpleController</title></head><body>��ӭʹ��SimpleController!</body></html>");
		}
		
	}

}
