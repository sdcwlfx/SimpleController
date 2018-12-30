package sc.ustc.dynamicProxy;

import java.net.URL;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import sc.ustc.dom.XMLParse;
import sc.ustc.util.InvokeReflection;

/**
 * ��̬����ӿ�ʵ���� InvocationHandler+Proxy��ʽ 
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
			interceptorElement=XMLParse.getInterceptorElement(url, actionName);//��ȡ������Ԫ��
			if(actionElement!=null&&interceptorElement!=null) {//action������������
				//�������༰����
				String interceptorClass=interceptorElement.attributeValue("class");
				String predo=interceptorElement.attributeValue("predo");
				String afterdo=interceptorElement.attributeValue("afterdo");
				
				//action�༰����
				String actionClass=actionElement.attributeValue("class");
				String actionMethod=actionElement.attributeValue("method");
				
				//�������ִ��predo()��action()��afterdo()
				InvokeReflection.invokeInterceptorReflection(interceptorClass, predo, actionName);
				//actionResult=InvokeReflection.invokeActionReflection(actionClass, actionMethod);
				actionResult=InvokeReflection.invokeActionReflection(actionClass, actionMethod,userName,userPassword);
				InvokeReflection.invokeInterceptorReflection(interceptorClass,afterdo, actionResult);
					
			}else if(actionElement!=null) {//δ������������ֱ��ִ��action
				//action�༰����
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
		
		
		
		
		
//		String result=InvokeReflection.invokeActionReflection(goalAction, goalActionMethod);//java�������jar���⣨��UserSC���̣���Action��goalAction������ָ��������goalActionMethod��������÷������ؽ��
//		//����actionName����Ԫ��result��Ӧ����Ӧҳ��
//		Element resultElement=XMLParse.getResultElement(goalElement, result,actionName);//��ȡ��resultԪ��   ??????goalElement��actionԪ�ص����ݣ������������д�ӡȷ������xml����
//		if(resultElement==null) {//�Դ�xml�з��ص�resultԪ����null
//			out.println("<html><head><title>SimpleController</title></head><body>404!!Controller.xml is wrong!! Page goes to Mars!!!</body></html>");
//		}else {
//			String resultType=resultElement.attributeValue("type");//type����
//			String resultValue=resultElement.attributeValue("value");//��Ӧҳ��
//			System.out.println("resultType: "+resultType+"    "+"resultValue: "+resultValue);
//			if(resultType.endsWith("forward")) {
//				RequestDispatcher requestDispatcher=request.getRequestDispatcher(resultValue);//��ȡ����ת�������󣬸�ת������ָ��ͨ��getRequestDisPatcher()�Ĳ�������
//				requestDispatcher.forward(request, response);//ת������
//			}else {
//				response.sendRedirect(resultValue);//�ض���ת������
//			}
//		}
		

	

	//@Override
//	public void afterdo() {
//		// TODO Auto-generated method stub
//		
//
//	}

}
