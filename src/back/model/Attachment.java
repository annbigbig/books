package back.model;

import java.io.InputStream;

public class Attachment {
	long id;			//主鍵
	long mailId;		//外鍵，用來關聯所屬郵件
	String filename;	//檔名
	InputStream in;		//檔案輸入流
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getMailId() {
		return mailId;
	}
	public void setMailId(long mailId) {
		this.mailId = mailId;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public InputStream getIn() {
		return in;
	}
	public void setIn(InputStream in) {
		this.in = in;
	}	
}
