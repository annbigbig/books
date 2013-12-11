package back.controller.logout;

import java.util.Map;

import back.controller.base.BaseAction;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class LogoutAction extends BaseAction {
	public String execute(){
		logout();
		return LOGIN;
	}
	
	public void logout(){
		//Map<String,Object> session = ActionContext.getContext().getSession();
		getSession().clear();
	}
}
