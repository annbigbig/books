package back.controller.general.mail;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import back.controller.base.BaseAction;
import back.model.Mail;

public class BodyMessageAction extends BaseAction{
	private InputStream inputStream;
	private long mail_id;
	
	public String execute(){
		String bodyMessage = "";
		Mail mail = getMailDao().getMail(mail_id);
		if(mail!=null){
			bodyMessage = mail.getBodyMessage();
		}else{
			bodyMessage = "空白的bodyMessage...";
		}
		try {
			//不這樣轉碼，待會兒客戶端html只會拿到中文亂碼
			inputStream = IOUtils.toInputStream(bodyMessage, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public long getMail_id() {
		return mail_id;
	}
	public void setMail_id(long mail_id) {
		this.mail_id = mail_id;
	}
	
}
