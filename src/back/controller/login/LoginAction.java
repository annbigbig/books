package back.controller.login;

import java.util.Map;

import back.controller.base.BaseAction;
import back.dao.WorkerDao;
import back.model.Worker;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class LoginAction extends BaseAction implements ModelDriven{
	Worker worker = new Worker();
	
	public String execute(){
		if(login()){
			return SUCCESS;
		}
		return INPUT;
	}
	
	public boolean login(){
		boolean result = false;
		//WorkerDao dao = WorkerDao.getInstance();
		String name = worker.getName();
		String password = worker.getPassword();
		//Worker w = dao.getWorker(name, password);
		Worker w = getWorkerDao().getWorker(name, password);
		if(w!=null){
			//將取出的Worker實體放入session域
			//Map<String,Object> session = ActionContext.getContext().getSession();
			getSession().put("worker", w);
			result = true;
		}
		//偵錯用:
		String msg = "[LoginAction.login()] worker.getName()=" + name;
		msg += "\t worker.getPassword()=" + password;
		System.out.println(msg);
		
		return result;
	}
	
	public void validate(){
		this.clearFieldErrors();
		//檢查工作人員名稱
		if(worker.getName()==null||worker.getName().length()<=0){
			addFieldError("name","請輸入工作人員名稱");
		}
		
		//檢查密碼
		if(worker.getPassword()==null||worker.getPassword().length()<=0){
			addFieldError("password","請輸入您的密碼");
		}
	}

	@Override
	public Object getModel() {
		return worker;
	}
}
