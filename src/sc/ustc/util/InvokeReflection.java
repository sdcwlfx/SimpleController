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
	public static String invokeActionReflection(String goalAction,String goalActionMethod) {
		String result="failure";
		try {
			//���Ҫ���õ�action�����
			Class<?> clazz=Class.forName(goalAction);
			
			//����action���з���goalActionMethod,��һ�����������������ڶ���������������������
			Method method=clazz.getMethod(goalActionMethod, null);
			
			//ִ�и÷�������һ�����������һ��ʵ�����ڶ��������������Ĳ���
			result=String.valueOf(method.invoke(clazz.newInstance(),null));
			System.out.println("Refelction result: "+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	}
	
}
