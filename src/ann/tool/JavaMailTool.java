package ann.tool;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;

import back.dao.AttachmentDao;
import back.dao.FolderDao;
import back.dao.MailDao;
import back.model.Attachment;
import back.model.Mail;

public class JavaMailTool {
    String host = "your.host.com.tw";
    String port = "110";
    String userName = "your_account";
    String password = "your_password";
    long inboxFolderId;
    
    public JavaMailTool(String[] args){
    	this.host = args[0];
    	this.port = args[1];
    	this.userName = args[2];
    	this.password = args[3];
    }
    
    public void downloadEmailAttachments() throws Exception {
        Properties properties = new Properties();
 
        // server setting
        properties.put("mail.pop3.host", host);
        properties.put("mail.pop3.port", port);
 
        // SSL setting
        
        properties.setProperty("mail.pop3.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.pop3.socketFactory.fallback", "false");
        properties.setProperty("mail.pop3.socketFactory.port",
                String.valueOf(port));
 		
        Session session = Session.getDefaultInstance(properties);
 
        //準備工具類
        MailDao maildao = MailDao.getInstance();
        AttachmentDao attachmentDao = AttachmentDao.getInstance();
        FolderDao folderDao = FolderDao.getInstance();
        
        //找出該工作人員的inbox信件夾，取出inbox信件夾的id
        inboxFolderId = folderDao.getInbox(userName).getId();
        if(inboxFolderId<=0){
        	System.out.println("folders資料表中不存在工作人員" + userName + "的inbox信件夾，退出下載信件程序...");
        	return;
        }
        
        try {
            // connects to the message store
            Store store = session.getStore("pop3");
            store.connect(userName, password);

            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);		//這樣子開信箱才安全，信件不可能被刪除
            //folderInbox.open(Folder.READ_WRITE);	//這樣子收件匣的信件有可能被誤刪
            
            // fetches new messages from server
            Message[] arrayMessages = folderInbox.getMessages();
 
            for (int i = 0; i < arrayMessages.length; i++) {
                Message message = arrayMessages[i];
                Mail mail;
                try{
                	mail = wrapToMail(message);
                	System.out.println("---------------Message" + i + "----------------");
                	long mailId = maildao.addMail(mail);
                	//新增郵件成功
                	if(mailId>0){
                		List<Attachment> attachments = mail.getAttachments();
                		for(int j=0;j<attachments.size();j++){
                			Attachment atta = attachments.get(j);
                			atta.setMailId(mailId);
                			attachmentDao.addAttachment(atta);
                		}
                		printMail(mail);
                		//已經完整的寫完這封信了，就把它從服務器上刪掉
                		//message.setFlag(Flags.Flag.DELETED, true);
                	}
                }catch(Exception ex){
                	ex.printStackTrace();
                }
            }
 
            // disconnect
            //folderInbox.close(true);	//這樣子就真的刪除掉了
            folderInbox.close(false);	//這樣子不會真的刪掉信件
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for pop3.");
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        } catch (Exception ex){
        	ex.printStackTrace();
        }
    }
    
    //將Message封裝成Mail物件
    public Mail wrapToMail(Message message) throws Exception{
    	Mail mail = new Mail();
    	List<Attachment> attachments = new ArrayList<Attachment>();
    	attachments.clear();
    	//取出郵件Header部分處理
        Address[] fromAddress = message.getFrom();
        String from = fromAddress[0].toString();
        Address[] ccAddresses = message.getRecipients(RecipientType.CC);
        String cc = getAddresses(ccAddresses);
        Address[] bccAddresses = message.getRecipients(RecipientType.BCC);
        String bcc = getAddresses(bccAddresses);
        Address[] toAddresses = message.getRecipients(RecipientType.TO);
        String to = getAddresses(toAddresses);
        String subject = message.getSubject();
        String sentDate = message.getSentDate().toString();
        String contentType = message.getContentType();
        String bodyMessage = "";

        if (contentType.contains("multipart")) {
            // content may contain attachments
            Multipart multiPart = (Multipart) message.getContent();
            int numberOfParts = multiPart.getCount();
            for (int partCount = 0; partCount < numberOfParts; partCount++) {
                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                    // this part is attachment
                    Attachment atta = wrapToAttachment(part);
                    attachments.add(atta);
                } else {
                    // this part may be the message content
                	bodyMessage = getText(part);
                }
            }
            
        } else if (contentType.contains("text/plain")
                || contentType.contains("text/html")) {
            Object content = message.getContent();
            if (content != null) {
                bodyMessage = content.toString();
            }
        }
    
        //你覺得有可能產生中文亂碼的欄位，就用MimeUtility去decodeText
        from = MimeUtility.decodeText(from);
        to = MimeUtility.decodeText(to);
        cc = MimeUtility.decodeText(cc);
        bcc = MimeUtility.decodeText(bcc);
        contentType = MimeUtility.decodeText(contentType);
        subject = MimeUtility.decodeText(subject);
        sentDate = MimeUtility.decodeText(sentDate);
        bodyMessage = MimeUtility.decodeText(bodyMessage);
        
    	//為實體賦值
        mail.setFolderId(inboxFolderId);
        mail.setFrom(from);
        mail.setTo(to);
        mail.setCc(cc);
        mail.setBcc(bcc);
        mail.setContentType(contentType);
        mail.setSubject(subject);
        mail.setSentDate(sentDate);
        mail.setBodyMessage(bodyMessage);
        mail.setAttachments(attachments);
        
    	return mail;
    }
    
    /**
     * Return the primary text content of the message.
     */
    //Oracle官網抄來的，傳入一個BodyPart，返回裡面的body message
    private String getText(Part p) throws MessagingException, IOException {

        if (p.isMimeType("text/*")||p.isMimeType("text/html")) {
            String s = (String)p.getContent();
           
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }
    
    //自已寫的，傳入一個附件的BodyPart，返回一個我自定義的Attachment物件
    public Attachment wrapToAttachment(MimeBodyPart part) throws Exception{
    	Attachment atta = new Attachment();
    	System.out.println("part.getDisposition()=" + part.getDisposition());
    	if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
    		String filename = MimeUtility.decodeText(part.getFileName());
    		InputStream in = part.getInputStream();
    		atta.setFilename(filename);
    		atta.setIn(in);
    	}
    	return atta;
    }
    
    //
    public String getAddresses(Address[] addrs){
    	String result = "";
    	try{
    		for(int i=0;i<addrs.length;i++){
    			if(result.length()==0){
    				result += MimeUtility.decodeText(addrs[i].toString());
    			}else{
    				result += ", " + MimeUtility.decodeText(addrs[i].toString());
    			}
    		}
    	}catch(NullPointerException ex){
    		//do nothing because addrs maybe null
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return result;
    }
    
    //把封裝好的郵件打印到console看看
    public void printMail(Mail mail){
        System.out.println("\t From: " + mail.getFrom());
        System.out.println("\t To: " + mail.getTo());
        System.out.println("\t Cc: " + mail.getCc());
        System.out.println("\t Bcc: " + mail.getBcc());
        System.out.println("\t ContentType:" + mail.getContentType());
        System.out.println("\t Subject: " + mail.getSubject());
        System.out.println("\t Sent Date: " + mail.getSentDate());
        System.out.println("\t Message: " + mail.getBodyMessage());
        System.out.print("\t Attachments: ");
        for(int i=0;i<mail.getAttachments().size();i++){
        	if(i==0){
        		System.out.print(mail.getAttachments().get(i).getFilename());
        	}else{
        		System.out.print(", " + mail.getAttachments().get(i).getFilename());
        	}
        }
        System.out.println("");
    }

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getInboxFolderId() {
		return inboxFolderId;
	}

	public void setInboxFolderId(long inboxFolderId) {
		this.inboxFolderId = inboxFolderId;
	}	
}
