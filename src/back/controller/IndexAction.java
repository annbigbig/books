package back.controller;

import java.util.Map;

import back.controller.base.BaseAction;
import back.dao.FolderDao;
import back.dao.MailDao;
import back.model.Folder;
import back.model.Worker;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

//後台首頁[/back/index.action]的控制器
public class IndexAction extends BaseAction{
	
	public String execute(){
		setWorker(wrapSessionWorker());
		return SUCCESS;
	}
	
}
