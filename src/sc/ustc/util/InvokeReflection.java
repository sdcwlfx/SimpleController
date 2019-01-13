package sc.ustc.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

import org.dom4j.Element;

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
	
	
	
	/**
	 * e7 �ȷ��乹�챻����beanʵ�����ٷ��乹������beanʵ����Ȼ��ͨ��java��ʡ���ƽ�������beanʵ��ע������beansʵ���У����������
	 * @param goalAction water.ustc.action.LoginAction
	 * @param goalActionMethod  handleLogin
	 * @param userName 
	 * @param userPassword
	 * @param fieldMap  ��������bean������������������
	 * @return
	 * ��ʡ���ƣ�https://blog.csdn.net/u010445297/article/details/60967146
	 */
	public static String invokeIntrospectorBean(String goalAction,String goalActionMethod,String userName,String userPassword,Map<String, String> fieldChildMap,Map<String, String> fieldGrandsonMap) {
		String result="failure";
		try {
			//Ӧ�������пպ����ʹ��
			if(fieldGrandsonMap.size()>0) {
				
			}
			//���Ҫ���õ�action�����
			Class<?> clazz=Class.forName(goalAction);
			//��ñ���������bean�����
			Class<?> fieldChildBean=Class.forName(fieldChildMap.get("class"));
			//��ñ�����������bean�����
			Class<?> fieldGrandsonBean=Class.forName(fieldGrandsonMap.get("class"));
			//��ȡ����������������beanʵ��������beanʵ���е���������
			String fieldChildName=fieldChildMap.get("name");
			System.out.println("fieldName:     "+fieldChildName);
			//��ȡ������������������beanʵ����������beanʵ���е���������
			String fieldGrandsonName=fieldGrandsonMap.get("name");
			
			
			
			//��������beanʵ��
			Object clazzInstance=clazz.newInstance();
			//������������beanʵ��
			Object fieldChildBeanInstance=fieldChildBean.newInstance();
			//��������������beanʵ��
			Object fieldGrandonBeanInstance=fieldGrandsonBean.newInstance();
			
			//��ʡ���ƽ�������beanʵ��ע������beanʵ����
			//��������ʵ���л�ȡfieldGrandsonName��ָ���Ե�����������
			PropertyDescriptor pGrandsonDescriptor=new PropertyDescriptor(fieldGrandsonName,fieldChildBeanInstance.getClass());
			//�õ�fieldGrandsonName��ָ���Ե�setter����
			Method wGrandsonMethod=pGrandsonDescriptor.getWriteMethod();
			//���÷���ִ��setter����
			wGrandsonMethod.invoke(fieldChildBeanInstance, fieldGrandonBeanInstance);
			
			
			//������ʵ���л�ȡfieldName��ָ���Ե�����������
			PropertyDescriptor pDescriptor=new PropertyDescriptor(fieldChildName,clazzInstance.getClass());
			//�õ�fieldName��ָ���Ե�setter����
			Method wMethod=pDescriptor.getWriteMethod();
			//���÷���ִ��setter����
			wMethod.invoke(clazzInstance, fieldChildBeanInstance);
			
			//clazz.newInstance();
			
			
			//����action���з���goalActionMethod,��һ�����������������ڶ���������������������
			//Method method=clazz.getMethod(goalActionMethod, null);
			Method method=clazz.getMethod(goalActionMethod, String.class,String.class);
			//result=String.valueOf(method.invoke(clazz.newInstance(),userName,userPassword));
			result=String.valueOf(method.invoke(clazzInstance,userName,userPassword));
			
			//ִ�и÷�������һ�����������һ��ʵ�����ڶ��������������Ĳ���
			//result=String.valueOf(method.invoke(clazz.newInstance(),null));
			System.out.println("Refelction result: "+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	}
	
	
	
	
	
	
	
}
