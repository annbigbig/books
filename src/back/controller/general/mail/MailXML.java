package back.controller.general.mail;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;

import ann.tool.RegexTool;
import back.controller.base.BaseAction;
import back.model.Mail;

public class MailXML extends BaseAction {
	private InputStream inputStream;
	private long mail_id;
	public String execute(){
		String xmlData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		Mail mail = getMailDao().getMail(mail_id);
		
		//準備工具類RegexTool，待會兒可能要將<轉換成&lt;或是將>轉換成&gt;
		RegexTool regexTool = RegexTool.getInstance();
		
		//尚未完成功能:避免有小聰明的用戶，使用這個控制器去偷看其他人的信件
		
		if(mail==null){
			xmlData += "<Mail>\n";
			xmlData += "	<id></id>\n";
			xmlData += "	<from></from>\n";
			xmlData += "	<to></to>\n";
			xmlData += "	<cc></cc>\n";
			xmlData += "	<bcc></bcc>\n";
			xmlData += "	<contentType></contentType>\n";
			xmlData += "	<subject></subject>\n";
			xmlData += "	<sentDate></sentData>\n";
			//xmlData += "	<bodyMessage></bodyMessage>\n";
			xmlData += "</Mail>\n";
		}else{
			xmlData += "<Mail>\n";
			xmlData += "	<id>" + (mail.getId()>0 ? mail.getId() : "") + "</id>\n";
			xmlData += "	<from>" + (mail.getFrom()!=null ? regexTool.escapeHtml(mail.getFrom()) : "") + "</from>\n";	//加了此行導致客戶端無法解析XML，因為此欄位內含<>這兩個特殊字元
			xmlData += "	<to>" + (mail.getTo()!=null ? regexTool.escapeHtml(mail.getTo()) : "") + "</to>\n";
			xmlData += "	<cc>" + (mail.getCc()!=null ? regexTool.escapeHtml(mail.getCc()) : "") + "</cc>\n";
			xmlData += "	<bcc>" + (mail.getBcc()!=null ? regexTool.escapeHtml(mail.getBcc()) : "") + "</bcc>\n";
			xmlData += "	<contentType>" + (mail.getContentType()!=null ? regexTool.escapeHtml(mail.getContentType()) : "") + "</contentType>\n";
			xmlData += "	<subject>" + (mail.getSubject()!=null ? regexTool.escapeHtml(mail.getSubject()) : "") + "</subject>\n";
			xmlData += "	<sentDate>" + (mail.getSentDate()!=null ? regexTool.escapeHtml(mail.getSentDate()) : "") + "</sentDate>\n";
 			//xmlData += "	<bodyMessage>" + (mail.getBodyMessage()!=null ? regexTool.escapeHtml(mail.getBodyMessage()) : "") + "</bodyMessage>\n";
			int attachmentsCount = (mail.getAttachments()==null ? 0 : mail.getAttachments().size());
			if(attachmentsCount>0){
				xmlData += "	<Attachments>\n";	
					for(int i=0;i<attachmentsCount;i++){
						xmlData += "<Attachment>\n";
						xmlData += "	<id>" + mail.getAttachments().get(i).getId() + "</id>\n";
						xmlData += "	<filename>" + mail.getAttachments().get(i).getFilename() + "</filename>\n";
						xmlData += "</Attachment>\n";
					}
				xmlData += "	</Attachments>\n";
			}
			xmlData += "</Mail>\n";
		}
		
		
		try {
			//不這樣轉碼，待會兒客戶端html只會拿到中文亂碼
			inputStream = IOUtils.toInputStream(xmlData, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//inputStream = new StringBufferInputStream(xmlData);
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
