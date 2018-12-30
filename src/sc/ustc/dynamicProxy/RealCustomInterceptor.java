package sc.ustc.dynamicProxy;

import java.net.URL;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import sc.ustc.dom.XMLParse;
import sc.ustc.util.InvokeReflection;

/**
 * 动态代理接口实现类 InvocationHandler+Proxy方式 
 * @author asus
 *
 */
public class RealCustomInterceptor implements CustomInterceptor {
	@Override
	public String action(URL url,String actionName,String userName,String userPassword) {
		
		Element actionElement=null;
		Element interceptorElement=null;
		String actionResult="";
		try {
			actionElement=(Element)XMLParse.getNode(url, actionName);
			interceptorElement=XMLParse.getInterceptorElement(url, actionName);//获取拦截器元素
			if(actionElement!=null&&interceptorElement!=null) {//action配置了拦截器
				//拦截器类及方法
				String interceptorClass=interceptorElement.attributeValue("class");
				String predo=interceptorElement.attributeValue("predo");
				String afterdo=interceptorElement.attributeValue("afterdo");
				
				//action类及方法
				String actionClass=actionElement.attributeValue("class");
				String actionMethod=actionElement.attributeValue("method");
				
				//反射机制执行predo()、action()、afterdo()
				InvokeReflection.invokeInterceptorReflection(interceptorClass, predo, actionName);
				//actionResult=InvokeReflection.invokeActionReflection(actionClass, actionMethod);
				actionResult=InvokeReflection.invokeActionReflection(actionClass, actionMethod,userName,userPassword);
				InvokeReflection.invokeInterceptorReflection(interceptorClass,afterdo, actionResult);
					
			}else if(actionElement!=null) {//未配置拦截器，直接执行action
				//action类及方法
				String actionClass=actionElement.attributeValue("class");
				String actionMethod=actionElement.attributeValue("method");
				//actionResult=InvokeReflection.invokeActionReflection(actionClass, actionMethod);
				actionResult=InvokeReflection.invokeActionReflection(actionClass, actionMethod,userName,userPassword);
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return actionResult;
	}
		
		
		
		
		
//		String result=InvokeReflection.invokeActionReflection(goalAction, goalActionMethod);//java反射调用jar包外（即UserSC工程）中Action（goalAction）类中指定方法（goalActionMethod），并获得方法返回结果
//		//返回actionName下子元素result对应的响应页面
//		Element resultElement=XMLParse.getResultElement(goalElement, result,actionName);//获取到result元素   ??????goalElement是action元素的内容，但传到函数中打印确是整个xml内容
//		if(resultElement==null) {//对从xml中返回的result元素判null
//			out.println("<html><head><title>SimpleController</title></head><body>404!!Controller.xml is wrong!! Page goes to Mars!!!</body></html>");
//		}else {
//			String resultType=resultElement.attributeValue("type");//type类型
//			String resultValue=resultElement.attributeValue("value");//响应页面
//			System.out.println("resultType: "+resultType+"    "+"resultValue: "+resultValue);
//			if(resultType.endsWith("forward")) {
//				RequestDispatcher requestDispatcher=request.getRequestDispatcher(resultValue);//获取请求转发器对象，该转发器的指向通过getRequestDisPatcher()的参数设置
//				requestDispatcher.forward(request, response);//转发请求
//			}else {
//				response.sendRedirect(resultValue);//重定向转发请求
//			}
//		}
		

	

	//@Override
//	public void afterdo() {
//		// TODO Auto-generated method stub
//		
//
//	}

}
