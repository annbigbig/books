package back.interceptor;

import java.util.Map;

import back.controller.login.LoginAction;
import back.controller.logout.LogoutAction;
import back.model.Worker;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class LoginInterceptor extends AbstractInterceptor{

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		//預設值是攔截(返回login視圖)，只放行登入(LoginAction)與(LogoutAction)，確定工作人員已經登入才可放行
		
		//debug
		System.out.println("[LoginInterceptor]");
		
		//如果攔截器發現request的目標是LoginAction或LogoutAction，就放行
		if(whiteList(invocation)){
			return invocation.invoke();
		}
		
		//工作人員已經登入後台，故放行
		if(logon(invocation)){
			return invocation.invoke();
		}
		
		return "login";
	}
	
	public boolean logon(ActionInvocation invocation){
		boolean result = false;
		//從session域取出已經登入的Worker實體
		Map<String,Object> session = invocation.getInvocationContext().getSession();
		Worker worker = (Worker)session.get("worker");
		if(worker!=null){
			String name = worker.getName();
			if(name!=null&&name.length()>0){
				//工作人員已經登入
				result = true;
			}
		}
		return result;
	}
	
	//跳過某些Action不檢查
	public boolean whiteList(ActionInvocation invocation){
		boolean result = false;
		Object action = invocation.getAction();
		if((action instanceof LoginAction)||(action instanceof LogoutAction)){
			result = true;
		}
		return result;
	}
	
	//舊寫法
	/*
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		//預設值是攔截(返回login視圖)，只放行登入(LoginAction)與(LogoutAction)，確定工作人員已經登入才可放行
		
		//debug
		System.out.println("執行了:LoginInterceptor類別的intercept()方法");
		//如果攔截器發現request的目標是LoginAction或LogoutAction，就放行
		Object action = invocation.getAction();
		if((action instanceof LoginAction)||(action instanceof LogoutAction)){
			return invocation.invoke();
		}
		//從session域取出已經登入的Worker實體
		Map<String,Object> session = invocation.getInvocationContext().getSession();
		Worker worker = (Worker)session.get("worker");
		if(worker!=null){
			String name = worker.getName();
			if(name!=null&&name.length()>0){
				//工作人員已經登入，故放行
				return invocation.invoke();
			}
		}
		return "login";
	}	
	 */

}
