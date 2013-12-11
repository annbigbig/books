package back.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import ann.tool.RegexTool;
import back.model.Worker;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class AllowListInterceptor extends AbstractInterceptor{
	private String allowWorkers;
	private String requestURI;
	private String contextPath;
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
	    System.out.println("[AllowListInterceptor] allowWorkers=" + allowWorkers);
	    
	    if(allowList(invocation)){
	    	return invocation.invoke();
	    }
	    //不在允許清單上的工作人員，會被強制轉發到denied視圖
		return "denied";
	}
    
	//目前登入的工作人員，是否在允許清單內？
	public boolean allowList(ActionInvocation invocation){
		boolean result = false;
		
		//取出登入的工作人員實體
		Map<String,Object> session = invocation.getInvocationContext().getSession();
		Worker worker = (Worker)session.get("worker");
		
		String name = worker.getName();
		
		String[] workerslist = allowWorkers.split(",");
		for(int i=0;i<workerslist.length;i++){
			System.out.println("workerslist[" + i + "]=" + workerslist[i] + "\t worker.getName()=" + name);
			if(name.equals(workerslist[i])){
				result = true;
				break;
			}
		}
		//此工作人員沒有權限執行此Action，準備輸出必要信息到ActionContext
		if(!result){
			output(invocation);
		}
		
		return result;
	}
	
	//輸出訊息到ActionContext
	public void output(ActionInvocation invocation){
		
		Object action = invocation.getAction();
		String className = action.getClass().getName();
		String reason = initReasons().get(className);
		if((reason!=null)&&(reason.length()>0)){
			invocation.getInvocationContext().put("reason", reason);
		}else{
			invocation.getInvocationContext().put("reason", "原因不明");
		}
		
		//取得此request的請求url:[例:/annbookstore/back/mis/index.action]
		HttpServletRequest request = ServletActionContext.getRequest();
		requestURI = request.getRequestURI();
		contextPath = request.getContextPath();
		
		//準備工具類
		RegexTool regextool = RegexTool.getInstance();
		
		if(regextool.match(requestURI, ".*/back/mis/.*")){
			invocation.getInvocationContext().put("message", "十秒後跳轉到資訊部首頁");
			invocation.getInvocationContext().put("url", contextPath + "/back/mis/index.action");
		}
		
		if(regextool.match(requestURI, ".*/back/sales/.*")){
			invocation.getInvocationContext().put("message", "十秒後跳轉到銷售部首頁");
			invocation.getInvocationContext().put("url", contextPath + "/back/sales/index.action");
		}
	}
	
	public Map<String,String> initReasons(){
		Map<String,String> reasons = new HashMap<String,String>();
		reasons.clear();
		reasons.put("back.controller.mis.worker.QueryWorker", "您沒有管理後台帳號的權限");
		return reasons;
	}
	
	public String getAllowWorkers() {
		return allowWorkers;
	}
	
	public void setAllowWorkers(String allowWorkers) {
		this.allowWorkers = allowWorkers;
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
