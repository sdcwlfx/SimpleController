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
	public static String invokeActionReflection(String goalAction,String goalActionMethod) {
		String result="failure";
		try {
			//获得要调用的action类对象
			Class<?> clazz=Class.forName(goalAction);
			
			//返回action类中方法goalActionMethod,第一个参数：方法名；第二个参数：方法参数类型
			Method method=clazz.getMethod(goalActionMethod, null);
			
			//执行该方法，第一个参数：类的一个实例；第二个参数：方法的参数
			result=String.valueOf(method.invoke(clazz.newInstance(),null));
			System.out.println("Refelction result: "+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	}
	
}
