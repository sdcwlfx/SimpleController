package sc.ustc.util;

import java.lang.reflect.Method;

/**
 * java������Ƶ�������Ľ�����controller.xml��action�༰�䷽��
 * @author asus
 *
 */
public class InvokeReflection {

	/**
	 * java�������action�ķ��������ص��ý��
	 * @param goalAction��water.ustc.action.RegisterAction
	 * @param goalActionMethod��handleRegister
	 * @return
	 */
	public static String invokeActionReflection(String goalAction,String goalActionMethod,String userName,String userPassword) {
		String result="failure";
		try {
			//���Ҫ���õ�action�����
			Class<?> clazz=Class.forName(goalAction);
			
			//����action���з���goalActionMethod,��һ�����������������ڶ���������������������
			//Method method=clazz.getMethod(goalActionMethod, null);
			Method method=clazz.getMethod(goalActionMethod, String.class,String.class);
			result=String.valueOf(method.invoke(clazz.newInstance(),userName,userPassword));
			
			//ִ�и÷�������һ�����������һ��ʵ�����ڶ��������������Ĳ���
			//result=String.valueOf(method.invoke(clazz.newInstance(),null));
			System.out.println("Refelction result: "+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	}
	
	//��������java������ƣ��������������з����ĵ���
	public static void invokeInterceptorReflection(String goalClass,String goalClassMethod,String str) {
		try {
			//���Ҫ���õ�action�����
			System.out.println("�������з��䣺"+goalClass+"   ������"+goalClassMethod+"    ������"+str);
			Class<?> clazz=Class.forName(goalClass);
			
			//����action���з���goalActionMethod,��һ�����������������ڶ���������������������
			Method method=clazz.getMethod(goalClassMethod,String.class);
			
			//ִ�и÷�������һ�����������һ��ʵ�����ڶ��������������Ĳ���
			String.valueOf(method.invoke(clazz.newInstance(),str));
			//System.out.println("Refelction result: "+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
}
