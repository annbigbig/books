package back.controller.base;

import java.util.Map;

import org.apache.struts2.dispatcher.ActionContextCleanUp;
import org.apache.struts2.interceptor.SessionAware;

import back.dao.AttachmentDao;
import back.dao.MailDao;
import back.dao.WorkerDao;
import back.model.Worker;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
//請以這個類別為基礎，進行後台控制器的開發
public class BaseAction extends ActionSupport implements SessionAware{
	//session物件
	Map<String,Object> session;
	
	//執行進度百分比(進度條顯示用)
	int progress = 0;
	
	Worker worker = null;
	//取出登入session域的工作人員實體，此方法會在各控制器裡一再重用，故寫成父類別讓其他的控制器類來繼承
	public Worker wrapSessionWorker(){
		Worker worker = null;
		try{
		//取得session的舊方法，不建議使用，因為我有使用execAndWait攔截器，此時ActionContext可能還沒產生還是三小
		//Map<String,Object> session = ActionContext.getContext().getSession();
		worker = (Worker) session.get("worker");
		}catch(Exception e){
			e.printStackTrace();
		}
		return worker;
	}
	
	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}
	
	public WorkerDao getWorkerDao(){
		WorkerDao workerDao = WorkerDao.getInstance();
		return workerDao;
	}
	
	public MailDao getMailDao(){
		MailDao mailDao = MailDao.getInstance();
		return mailDao;
	}
	
	public AttachmentDao getAttachmentDao(){
		AttachmentDao attachmentDao = AttachmentDao.getInstance();
		return attachmentDao;
	}

	public int getProgress() {
		progress += 10;
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public Map<String,Object> getSession(){
		return session;
	}
}
