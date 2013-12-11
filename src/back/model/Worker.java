package back.model;

import back.dao.FolderDao;

public class Worker {
	String name;		//主鍵:工作人員帳號
	String password;	//密碼
	int permission;		//後台模塊權限，範圍0-255
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getPermission() {
		return permission;
	}
	public void setPermission(int permission) {
		this.permission = permission;
	}
	//返回自已的inbox信件夾
	public Folder getInbox(){
		FolderDao folderDao = FolderDao.getInstance();
		Folder inbox = null;
		inbox = folderDao.getInbox(name);
		return inbox;
	}
}
