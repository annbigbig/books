package back.model;

import back.dao.MailDao;

public class Folder {
	long id;		//主鍵
	long parentId;	//外鍵，用來關聯父資料夾
	String workerName;	//外鍵，用來關聯工作人員
	String name;	//資料夾名稱
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}	
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public String getWorkerName() {
		return workerName;
	}
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	//計算此信件夾裡有幾封信件
	public int getMailsCount(){
		int mailsCount = 0;
		MailDao mailDao = MailDao.getInstance();
		String where = "where folderId=" + id;
		mailsCount = mailDao.countTotalRows(where);
		return mailsCount;
	}
}
