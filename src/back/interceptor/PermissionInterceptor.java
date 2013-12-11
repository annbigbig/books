package back.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.dispatcher.Dispatcher;

import ann.tool.BaseConversion;
import ann.tool.RegexTool;
import back.controller.IndexAction;
import back.controller.login.LoginAction;
import back.controller.logout.LogoutAction;
import back.model.Worker;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.InterceptorConfig;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class PermissionInterceptor extends AbstractInterceptor{
	private String requestURI ="";
	private String contextPath = "";
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		//預設全部的Action都放行，只檢查工作人員有沒有特定url的進入權限
				
		//test();
		
		if(!permit(invocation)){
			return "denied";
		}
		
		return invocation.invoke();	
	}
	
	//工作人員是否有該Action的進入權限？(預設返回true，僅在特定url檢查出沒有進入權限時才會返回false)
	public boolean permit(ActionInvocation invocation){
		boolean result = true;
		//取得此request的請求url:[例:/annbookstore/back/mis/index.action]
		HttpServletRequest request = ServletActionContext.getRequest();
		requestURI = request.getRequestURI();
		contextPath = request.getContextPath();
		
		//從session域取出已經登入的Worker實體
		Map<String,Object> session = invocation.getInvocationContext().getSession();
		Worker worker = (Worker)session.get("worker");
		//將十進位的permission轉換成二進制字串(範圍 00000000 到 11111111)，如果從左數來第2個字元不是'1'，就進行攔截
		
		//準備工具類
		BaseConversion basetool = BaseConversion.getInstance();
		RegexTool regextool = RegexTool.getInstance();
		
		//工作人員沒有資訊部門權限[01000000]，禁止通行，namespace=/back/mis	
		if(regextool.match(requestURI, ".*/back/mis/.*")&&(!basetool.isOne(worker.getPermission(), 2, true))){
			invocation.getInvocationContext().put("reason", "您沒有資訊部門的權限[/back/mis]");
			result = false;
		}
		
		//工作人員沒有銷售部門權限[00100000]，禁止通行，namespace=/back/sales	
		if(regextool.match(requestURI, ".*/back/sales/.*")&&(!basetool.isOne(worker.getPermission(), 3, true))){
			invocation.getInvocationContext().put("reason", "您沒有銷售部門的權限[/back/sales]");
			result = false;
		}				
		
		if(!result){
			invocation.getInvocationContext().put("message", "十秒後跳轉到後台首頁");
			invocation.getInvocationContext().put("url", contextPath + "/back/index.action");
		}
		
		String msg = "[PermissionInterceptor] ";
		msg += "requestURI=" + requestURI;
		if(worker!=null){
			msg += " worker權限:" + basetool.binaryString(worker.getPermission(), false);
		}
		msg += " " + (result ? "允許":"拒絕");
		msg += " contextPath=" + contextPath;
		System.out.println(msg);
		
		return result;
	}
	
	//----純測試用---取出在back.xml裡面為此攔截器設定的參數
	public void test(){
		PackageConfig packageConfig = Dispatcher.getInstance().getConfigurationManager().getConfiguration().getPackageConfig("back");
		Map<String, Object> interceptorConfigs = packageConfig.getInterceptorConfigs();
		InterceptorConfig interceptorConfig =  (InterceptorConfig)interceptorConfigs.get("permissionCheck");
		Map<String, String> params = interceptorConfig.getParams();
		String param1 = params.get("param1");
		String param2 = params.get("param2");
		System.out.println("[PermissionInterceptor.test()] param1=" + param1 + "\t param2=" + param2);
	}
	
	//--debug
	public void debug(){
		
	}

	public String getRequestURI() {
		return requestURI;
	}

	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
}
