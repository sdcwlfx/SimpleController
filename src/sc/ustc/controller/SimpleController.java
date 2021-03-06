package sc.ustc.controller;

import java.io.IOException;
//import java.io.PrintWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Map;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.BufferedReader;
import sc.ustc.dom.XMLParse;
import sc.ustc.dynamicProxy.CustomInterceptor;
import sc.ustc.dynamicProxy.ProxyHandler;
import sc.ustc.dynamicProxy.RealCustomInterceptor;
import sc.ustc.util.InvokeReflection;
import sc.ustc.xslt.XSLT;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.io.SAXReader;

import com.sun.org.apache.bcel.internal.generic.NEW;

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
		String actionURI=request.getRequestURI();//    内容：/UseSC/Login.sc
		String userName=request.getParameter("userName");//获取表单用户名
		String userPassword=request.getParameter("userPassword");//获取表单密码
		System.out.println("userName: "+userName+"    userPassword:  "+userPassword);
		System.out.println(actionURI);
		System.out.println(actionURI.length());
		
		//获取action名字
		int actionBegin=actionURI.lastIndexOf("/");//获取uri最后的‘/’的位置
		int actionEnd=actionURI.lastIndexOf(".");//获得最后'.'的位置
		if(actionBegin<0||actionEnd<0) {
			String pathGetClass =this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			
			
			System.out.println("pathGetClass: "+pathGetClass);	
			out.println("<html><head><title>SimpleController</title></head><body>404!!!未找到该网页！Controller!</body></html>");
		}else {
			String actionName=actionURI.substring(actionBegin+1,actionEnd);//获取请求的action名字，返回login
			String pathGetClass =this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			System.out.println("pathGetClass: "+pathGetClass);
			//成功获取jar包同目录下controller.xml文件内容 参考： https://blog.csdn.net/T1DMzks/article/details/75099029
//			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("controller.xml");
//            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));         
//            String line="";
//            while((line=br.readLine())!=null){
//                System.out.println("getClassLoader: "+line);
//            }
			//获取调用jar包的工程的配置文件controller.xml的URL
			URL controllerUrl = getClass().getClassLoader().getResource("controller.xml");
//            String controllerPath=controllerUrl.getPath();//资源路径
            
			
			
			
            //dom4j+jaxen方式解析xml文件参考：https://www.cnblogs.com/warrior4236/p/5863024.html
            try {
            	System.out.println("controllerUrl: "+controllerUrl+"      actionName: "+actionName);
				Node node=XMLParse.getNode(controllerUrl, actionName);
				//未找到指定的action
				if(node==null) {
					out.println("<html><head><title>SimpleController</title></head><body>404!!! 无法识别该请求!!!</body></html>");
				}else {
//					String goalClass=node.valueOf("class");//获取节点属性名为class的的值
//					String goalMethod=node.valueOf("method");//获取节点属性为method的值
					Element goalElement=(Element)node;//将节点转化为元素,这里包含了action的子节点
					System.out.println("goalElement: "+goalElement.asXML());
					
					//使用java反射调用jar包外的Action类中方法
					String goalActionMethod=goalElement.attributeValue("method");//获取目标元素属性为method的值
					String goalAction=goalElement.attributeValue("class");//获取目标元素属性为class的值
					System.out.println("goalAction: "+goalAction+"    goalActionMethod: "+goalActionMethod);
					
					//e3隐藏 String result=InvokeReflection.invokeActionReflection(goalAction, goalActionMethod);//java反射调用jar包外（即UserSC工程）中Action（goalAction）类中指定方法（goalActionMethod），并获得方法返回结果
					
					//e7
					//di.xml路径URL
					String result=null;
					URL diUrl = getClass().getClassLoader().getResource("../../pages/di.xml");
					//获取di.xml中对应action的Bean元素
					Element beanElement=XMLParse.getBeanElementFromDI(diUrl, actionName);
					if(beanElement==null) {
						//直接通过反射调用Action的指定方法
						result=InvokeReflection.invokeActionReflection(goalAction, goalActionMethod,userName,userPassword);//java反射调用jar包外（即UserSC工程）中Action（goalAction）类中指定方法（goalActionMethod），并获得方法返回结果
					}else {
						//找到了，则查看是否有属性依赖其它bean节点，无依赖直接通过反射构造该bean实例，并将请求分发至其处理
						Map<String, String> fieldChildMap=XMLParse.getFieldElement(diUrl, actionName);
						//获取被依赖bean的id
						String beanId=XMLParse.getFieldRef(diUrl, actionName);
						if(fieldChildMap.size()==0) {
							//无属性依赖其它bean节点，直接通过反射构造实例处理请求
							result=InvokeReflection.invokeActionReflection(goalAction, goalActionMethod,userName,userPassword);//java反射调用jar包外（即UserSC工程）中Action（goalAction）类中指定方法（goalActionMethod），并获得方法返回结果
						}else {
							//目标action的bean节点有属性依赖其它bean节点，先通过反射构造被依赖的bean实例，再构造依赖bean实例，通过内省机制将被依赖bean实例注入依赖bean实例中
							Map<String, String> fieldGrandsonMap=XMLParse.getFieldElement(diUrl, beanId);
							result=InvokeReflection.invokeIntrospectorBean(goalAction, goalActionMethod, userName, userPassword, fieldChildMap,fieldGrandsonMap);
						}
						
					}
					
					
					
					
					
					
					//e7隐藏
					//使用动态代理调用action
//					ProxyHandler proxyHandler=new ProxyHandler();
//					//绑定该类实现的所有接口
//					CustomInterceptor customInterceptor=(CustomInterceptor)proxyHandler.bind(new RealCustomInterceptor());
//					//String result=customInterceptor.action(controllerUrl, actionName);
//					String result=customInterceptor.action(controllerUrl, actionName,userName,userPassword);
					//e7隐藏
					System.out.println("返回的result:  "+result);
					
					
					//返回actionName下子元素result对应的响应页面
					Element resultElement=XMLParse.getResultElement(goalElement, result,actionName);//获取到result元素   ??????goalElement是action元素的内容，但传到函数中打印确是整个xml内容
					if(resultElement==null) {//对从xml中返回的result元素判null
						out.println("<html><head><title>SimpleController</title></head><body>404!!Controller.xml is wrong!! Page goes to Mars!!!</body></html>");
					}else {
						String resultType=resultElement.attributeValue("type");//type类型
						String resultValue=resultElement.attributeValue("value");//响应页面
						
						
						//e4  若响应页面为xml结尾，则生成html页面，再使用html页面作为响应,关键在于路径UseSC项目中xml文件和xsl文件路径
						if(resultValue.endsWith("_view.xml")) {
							URL xmlUrl = getClass().getClassLoader().getResource("../../"+resultValue);//getResource（）会在jar包所在路径内查找，所以需要../../转化到WebContent路径
							String xmlPath=xmlUrl.getPath();
							System.out.println("xmlPath:  "+xmlPath);///F:/ustcsse/J2EE/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/UseSC/pages/success_view.xml
							resultValue=XSLT.xslToHtml(xmlPath);//返回的值为：pages/success_view.html
						}
						
						
						
						
						System.out.println("resultType: "+resultType+"    "+"resultValue: "+resultValue);
						if(resultType.endsWith("forward")) {
							RequestDispatcher requestDispatcher=request.getRequestDispatcher(resultValue);//获取请求转发器对象，该转发器的指向通过getRequestDisPatcher()的参数设置
							requestDispatcher.forward(request, response);//转发请求
						}else {
							response.sendRedirect(resultValue);//重定向转发请求
						}
					}
					
					//out.println("<html><head><title>SimpleController</title></head><body>find it!!</body></html>");
				}
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
   
			System.out.println((actionBegin+1)+"   "+actionEnd);
			System.out.println(actionURI.substring(actionBegin+1,actionEnd));
			//e1out.println("<html><head><title>SimpleController</title></head><body>欢迎使用SimpleController!</body></html>");
		}
		
	}

}
