package sc.ustc.dynamicProxy;

import java.net.URL;

/**
 * I��̬������ӿ�InvocationHandler+Proxy��ʽ
 * �ο���https://www.jianshu.com/p/9d5ef621f2d1 
 *  http://www.cnblogs.com/xiaoluo501395377/p/3383130.html
 *  http://www.cnblogs.com/ygj0930/p/6542259.html
 * @author asus
 *
 */
public interface CustomInterceptor {
	//����java�����������������predo��������
	//public void predo();
	
	//����java������Ƶ�action���з���,���ؽ��
	public String action(URL url,String actionName);
	
	//������������afterdo()������ͨ��java�������ʵ��
	//public void afterdo();
	

}
