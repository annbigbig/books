package back.model;

import java.util.Date;

public class WorkerActivity {
	long id;			//主鍵:流水號
	Date datetime;		//執行時間
	String action;		//控制器名稱
	String result;		//返回視圖名稱
	String workerName;	//外鍵:工作人員名稱
	String parameters;	//執行控制器時遞交的參數
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getWorkerName() {
		return workerName;
	}
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}
	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}	
}
