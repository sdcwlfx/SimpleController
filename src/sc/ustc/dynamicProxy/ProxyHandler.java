package sc.ustc.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.prefs.BackingStoreException;

import javax.xml.bind.Binder;
/**
 * ʵ�ֶ�̬����
 * @author asus
 *
 */
public class ProxyHandler implements InvocationHandler {
	
	private Object tar;
	
	public Object bind(Object tar){
		this.tar=tar;
		//�󶨸���ʵ�ֵ����нӿڣ�ȡ�ô�����
		return Proxy.newProxyInstance(tar.getClass().getClassLoader(), tar.getClass().getInterfaces(), this);
		
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		Object result=null;
		//����Ϳ��Խ���AOP�����
		//�ڵ��þ��庯������ǰ��ִ�д�����
		System.out.println("�������в���args:  "+args);
		result=method.invoke(tar, args);
		
		return result;
	}

}
