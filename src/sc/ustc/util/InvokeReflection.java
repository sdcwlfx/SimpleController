package sc.ustc.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

import org.dom4j.Element;

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
	
	
	
	/**
	 * e7 先反射构造被依赖bean实例，再反射构造依赖bean实例，然后通过java内省机制将被依赖bean实例注入依赖beans实例中，最后处理请求
	 * @param goalAction water.ustc.action.LoginAction
	 * @param goalActionMethod  handleLogin
	 * @param userName 
	 * @param userPassword
	 * @param fieldMap  被依赖的bean：包含域名和域类型
	 * @return
	 * 内省机制：https://blog.csdn.net/u010445297/article/details/60967146
	 */
	public static String invokeIntrospectorBean(String goalAction,String goalActionMethod,String userName,String userPassword,Map<String, String> fieldChildMap,Map<String, String> fieldGrandsonMap) {
		String result="failure";
		try {
			//应该首先判空后才能使用
			if(fieldGrandsonMap.size()>0) {
				
			}
			//获得要调用的action类对象
			Class<?> clazz=Class.forName(goalAction);
			//获得被依赖的子bean类对象
			Class<?> fieldChildBean=Class.forName(fieldChildMap.get("class"));
			//获得被依赖的孙子bean类对象
			Class<?> fieldGrandsonBean=Class.forName(fieldGrandsonMap.get("class"));
			//获取域名（即被依赖子bean实例在依赖bean实例中的属性名）
			String fieldChildName=fieldChildMap.get("name");
			System.out.println("fieldName:     "+fieldChildName);
			//获取域名（即被依赖孙子bean实例在依赖子bean实例中的属性名）
			String fieldGrandsonName=fieldGrandsonMap.get("name");
			
			
			
			//创建依赖bean实例
			Object clazzInstance=clazz.newInstance();
			//创建被依赖子bean实例
			Object fieldChildBeanInstance=fieldChildBean.newInstance();
			//创建被依赖孙子bean实例
			Object fieldGrandonBeanInstance=fieldGrandsonBean.newInstance();
			
			//内省机制将被依赖bean实例注入依赖bean实例中
			//从依赖子实例中获取fieldGrandsonName所指属性的属性描述器
			PropertyDescriptor pGrandsonDescriptor=new PropertyDescriptor(fieldGrandsonName,fieldChildBeanInstance.getClass());
			//得到fieldGrandsonName所指属性的setter方法
			Method wGrandsonMethod=pGrandsonDescriptor.getWriteMethod();
			//利用反射执行setter方法
			wGrandsonMethod.invoke(fieldChildBeanInstance, fieldGrandonBeanInstance);
			
			
			//从依赖实例中获取fieldName所指属性的属性描述器
			PropertyDescriptor pDescriptor=new PropertyDescriptor(fieldChildName,clazzInstance.getClass());
			//得到fieldName所指属性的setter方法
			Method wMethod=pDescriptor.getWriteMethod();
			//利用反射执行setter方法
			wMethod.invoke(clazzInstance, fieldChildBeanInstance);
			
			//clazz.newInstance();
			
			
			//返回action类中方法goalActionMethod,第一个参数：方法名；第二个参数：方法参数类型
			//Method method=clazz.getMethod(goalActionMethod, null);
			Method method=clazz.getMethod(goalActionMethod, String.class,String.class);
			//result=String.valueOf(method.invoke(clazz.newInstance(),userName,userPassword));
			result=String.valueOf(method.invoke(clazzInstance,userName,userPassword));
			
			//执行该方法，第一个参数：类的一个实例；第二个参数：方法的参数
			//result=String.valueOf(method.invoke(clazz.newInstance(),null));
			System.out.println("Refelction result: "+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	}
	
	
	
	
	
	
	
}
