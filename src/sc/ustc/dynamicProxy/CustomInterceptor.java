package sc.ustc.dynamicProxy;

import java.net.URL;

/**
 * I动态代理类接口InvocationHandler+Proxy方式
 * 参考：https://www.jianshu.com/p/9d5ef621f2d1 
 *  http://www.cnblogs.com/xiaoluo501395377/p/3383130.html
 *  http://www.cnblogs.com/ygj0930/p/6542259.html
 * @author asus
 *
 */
public interface CustomInterceptor {
	//调用java反射机制中拦截器的predo（）方法
	//public void predo();
	
	//调用java反射机制的action类中方法,返回结果
	public String action(URL url,String actionName);
	
	//调用拦截器中afterdo()方法，通过java反射机制实现
	//public void afterdo();
	

}
