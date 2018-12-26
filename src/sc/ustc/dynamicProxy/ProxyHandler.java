package sc.ustc.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.prefs.BackingStoreException;

import javax.xml.bind.Binder;
/**
 * 实现动态代理
 * @author asus
 *
 */
public class ProxyHandler implements InvocationHandler {
	
	private Object tar;
	
	public Object bind(Object tar){
		this.tar=tar;
		//绑定该类实现的所有接口，取得代理类
		return Proxy.newProxyInstance(tar.getClass().getClassLoader(), tar.getClass().getInterfaces(), this);
		
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		Object result=null;
		//这里就可以进行AOP编程了
		//在调用具体函数方法前，执行处理功能
		System.out.println("代理类中参数args:  "+args);
		result=method.invoke(tar, args);
		
		return result;
	}

}
