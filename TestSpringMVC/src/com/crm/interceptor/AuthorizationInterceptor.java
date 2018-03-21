package com.crm.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.crm.pojos.User;

public class AuthorizationInterceptor implements HandlerInterceptor{
	private static final Log logger=LogFactory.getLog(AuthorizationInterceptor.class);
   //����Ϊ������
	private static final String[] IGNORE_URI={"user/loginForm","user/login","user/registerForm","user/register"};
    /**
     * �÷������������������֮��ִ�У���Ҫ����������������Դ
     * �÷���Ҳֻ���ڵ�ǰInterceptor��preHandle�����ķ���ֵΪtrueʱ����ִ�С�
     */
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		logger.info("AuthorizationInterceptor afterCompletion -->");
		
	}
    /**
     * �÷�������Controller�ķ�������֮��ִ�У������п��Զ�ModelAndView���в�����
     * �÷���Ҳֻ�� �ڵ�ǰInterceptor��preHandle�����ķ���ֵΪtrueʱ�Ż�ִ�С�
     */
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		logger.info("AuthorizationInterceptor postHandle-->");
		
	}
    /**
     * preHandle�����ǽ��д����������õģ��÷�������Controller����֮ǰ���е��ã�
     * �÷����ķ���ֵΪtrue�������Ż��������ִ�У��÷����ķ���ֵΪfalse��ʱ����������ͽ�����
     */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		System.out.println("AuthorizationInterceptor preHandle-->");
		boolean flag=false;
		//��ȡ����·�������ж�
		String servletPath=request.getServletPath();
		logger.info("����·��Ϊ��"+servletPath);
		//�ж������Ƿ���Ҫ����
		for(String s:IGNORE_URI){
			if(servletPath.contains(s)){
				flag=true;
				break;
			}
		}
		//��������
		if(!flag){
			User user=(User)request.getSession().getAttribute("user");
			if(user==null){
				logger.info("δ��¼������");
				request.setAttribute("message", "���ȵ�¼�ٷ�����վ");
				request.getRequestDispatcher("user/loginForm").forward(request, response);
			}else{
				logger.info("�Ѿ���¼������");
				flag=true;
			}
		}
		return false;
	}

}
