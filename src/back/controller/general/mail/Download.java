package back.controller.general.mail;

import java.io.InputStream;

import com.opensymphony.xwork2.ModelDriven;

import back.controller.base.BaseAction;
import back.model.Attachment;

public class Download extends BaseAction implements ModelDriven{
	private InputStream inputStream;
	private Attachment attachment = new Attachment();
	public String execute(){
		attachment = getAttachmentDao().getAttachment(attachment.getId());
		inputStream = attachment.getIn();
		return SUCCESS;
	}

	@Override
	public Object getModel() {
		return attachment;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

}
