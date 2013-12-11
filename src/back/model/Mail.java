package back.model;

import java.util.List;

import back.dao.AttachmentDao;

public class Mail {
	long id;			//主鍵
	long folderId;	//外鍵，用來關聯信件夾
	//header
	String from;		//寄件人
	String to;			//收件人
	String cc;			//副本
	String bcc;			//密件副本
	String contentType;	//類型
	String subject;		//信件標題
	String sentDate;	//寄送時間
	//body
	String bodyMessage;	//內文
	//attachments
	List<Attachment> attachments;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getFolderId() {
		return folderId;
	}
	public void setFolderId(long folderId) {
		this.folderId = folderId;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getBcc() {
		return bcc;
	}
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSentDate() {
		return sentDate;
	}
	public void setSentDate(String sentDate) {
		this.sentDate = sentDate;
	}
	public String getBodyMessage() {
		return bodyMessage;
	}
	public void setBodyMessage(String bodyMessage) {
		this.bodyMessage = bodyMessage;
	}
	public List<Attachment> getAttachments() {
		if(attachments==null){
			AttachmentDao attachmentDao = AttachmentDao.getInstance();
			attachments = attachmentDao.getAttachments(id);
		}
		return attachments;
	}
	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
	public int getAttachmentsCount(){
		int count = 0;
		List<Attachment> attas = getAttachments();
		count = (attas!=null) ? attas.size() : 0;
		return count;
		
	}
}
