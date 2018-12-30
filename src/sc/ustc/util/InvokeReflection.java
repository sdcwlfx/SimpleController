package sc.ustc.util;

import java.lang.reflect.Method;

/**
 * java反射机制调用请求的解析后controller.xml的action类及其方法
 * @author asus
 *
 */
public class InvokeReflection {

	/**
	 * java反射调用action的方法并返回调用结果
	 * @param goalAction：water.ustc.action.RegisterAction
	 * @param goalActionMethod：handleRegister
	 * @return
	 */
	public static String invokeActionReflection(String goalAction,String goalActionMethod,String userName,String userPassword) {
		String result="failure";
		try {
			//获得要调用的action类对象
			Class<?> clazz=Class.forName(goalAction);
			
			//返回action类中方法goalActionMethod,第一个参数：方法名；第二个参数：方法参数类型
			//Method method=clazz.getMethod(goalActionMethod, null);
			Method method=clazz.getMethod(goalActionMethod, String.class,String.class);
			result=String.valueOf(method.invoke(clazz.newInstance(),userName,userPassword));
			
			//执行该方法，第一个参数：类的一个实例；第二个参数：方法的参数
			//result=String.valueOf(method.invoke(clazz.newInstance(),null));
			System.out.println("Refelction result: "+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	}
	
	//带参数的java反射机制，用于拦截器类中方法的调用
	public static void invokeInterceptorReflection(String goalClass,String goalClassMethod,String str) {
		try {
			//获得要调用的action类对象
			System.out.println("拦截器中反射："+goalClass+"   方法："+goalClassMethod+"    参数："+str);
			Class<?> clazz=Class.forName(goalClass);
			
			//返回action类中方法goalActionMethod,第一个参数：方法名；第二个参数：方法参数类型
			Method method=clazz.getMethod(goalClassMethod,String.class);
			
			//执行该方法，第一个参数：类的一个实例；第二个参数：方法的参数
			String.valueOf(method.invoke(clazz.newInstance(),str));
			//System.out.println("Refelction result: "+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
}
