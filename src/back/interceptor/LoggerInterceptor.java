package back.interceptor;

import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import ann.tool.DatetimeTool;
import back.model.Worker;
import back.model.WorkerActivity;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;

public class LoggerInterceptor extends AbstractInterceptor{

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
	      invocation.addPreResultListener(new PreResultListener() { 
	          @Override
	          public void beforeResult(ActionInvocation invocation, String resultCode) {
	        	//將工作人員的操作行為，封裝到WorkerActivity物件內，並透過DAO寫入資料庫
	        	  
	        	//準備工具類
	        	DatetimeTool dtTool = DatetimeTool.getInstance();
	        	
		        //創建一個空白的WorkerActivity物件
		        WorkerActivity activity = new WorkerActivity();
		        
		        //賦值-datetime
		        Date now = dtTool.now();
		        activity.setDatetime(now);
		        
		        //賦值-action
	            Object o = invocation.getAction();
	            String className = o.getClass().getName();
	            activity.setAction(className);
	            
	            //賦值-result
	            activity.setResult(resultCode);
	            
	            //賦值-workerName
	            Map<String,Object> session = invocation.getInvocationContext().getSession();
	            Worker worker = (Worker)session.get("worker");
	            if(worker==null){
	            	worker = new Worker();
	            	worker.setName("尚未登入");
	            }
	            activity.setWorkerName(worker.getName());
	            
	            //賦值-parameters
	            String parameters = getParameters();
	            activity.setParameters(parameters);
	            
	            //debug
	            String msg = "[LoggerInterceptor]";
	            		msg += " " + dtTool.standardFormat(activity.getDatetime());
	            		msg += " 執行的Action名稱是:" + activity.getAction();
	            		msg += " 返回的視圖:" + activity.getResult();
	            		msg += " 工作人員:" + activity.getWorkerName();
	            		msg += " 參數:" + activity.getParameters();
	             System.out.println(msg);
	          }
	      });
		
		return invocation.invoke();
	}

	//取出用戶請求內的參數，然後拼成一個字串返回
	public String getParameters(){
		String parameters = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		Enumeration<String> names = request.getParameterNames();
		while(names.hasMoreElements()){
			if(parameters.length()>0) parameters += ",";
			String paraName = names.nextElement();
			String[] paraValue = request.getParameterValues(paraName);
			parameters += paraName + "=";
			for(int i=0;i<paraValue.length;i++){
				parameters += paraValue[i]+" ";
			}
		}
		return parameters;
	}
}
