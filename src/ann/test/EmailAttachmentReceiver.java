package ann.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
 
/**
 * This program demonstrates how to download e-mail messages and save
 * attachments into files on disk.
 *
 * @author www.codejava.net
 *
 */
public class EmailAttachmentReceiver {
    private String saveDirectory;
 
    /**
     * Sets the directory where attached files will be stored.
     * @param dir absolute path of the directory
     */
    public void setSaveDirectory(String dir) {
        this.saveDirectory = dir;
    }
 
    /**
     * Downloads new messages and saves attachments to disk if any.
     * @param host
     * @param port
     * @param userName
     * @param password
     */
    public void downloadEmailAttachments(String host, String port,
            String userName, String password) {
        Properties properties = new Properties();
 
        // server setting
        properties.put("mail.pop3.host", host);
        properties.put("mail.pop3.port", port);
 
        // SSL setting
        /*
        properties.setProperty("mail.pop3.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.pop3.socketFactory.fallback", "false");
        properties.setProperty("mail.pop3.socketFactory.port",
                String.valueOf(port));
 		*/
        Session session = Session.getDefaultInstance(properties);
 
        try {
            // connects to the message store
            Store store = session.getStore("pop3");
            store.connect(userName, password);

            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);
            
            // fetches new messages from server
            Message[] arrayMessages = folderInbox.getMessages();
 
            for (int i = 0; i < arrayMessages.length; i++) {
                Message message = arrayMessages[i];

                Address[] fromAddress = message.getFrom();
                String from = MimeUtility.decodeText(fromAddress[0].toString());
                Address[] ccAddresses = message.getRecipients(RecipientType.CC);
                String cc = getAddresses(ccAddresses);
                Address[] bccAddresses = message.getRecipients(RecipientType.BCC);
                String bcc = getAddresses(bccAddresses);
                Address[] toAddresses = message.getRecipients(RecipientType.TO);
                String to = getAddresses(toAddresses);
                String subject = message.getSubject();
                String sentDate = message.getSentDate().toString();
 
                String contentType = message.getContentType();
                String messageContent = "";
 
                // store attachment file name, separated by comma
                String attachFiles = "";
 
                if (contentType.contains("multipart")) {
                    // content may contain attachments
                    Multipart multiPart = (Multipart) message.getContent();
                    int numberOfParts = multiPart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            // this part is attachment
                            String fileName = part.getFileName();
                            attachFiles += fileName + ", ";
                            part.saveFile(saveDirectory + File.separator + fileName);
                        } else {
                            // this part may be the message content
                            //messageContent = part.getContent().toString();
                        	messageContent = getText(part);
                        }
                    }
 
                    if (attachFiles.length() > 1) {
                        attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
                    }
                } else if (contentType.contains("text/plain")
                        || contentType.contains("text/html")) {
                    Object content = message.getContent();
                    if (content != null) {
                        messageContent = content.toString();
                    }
                }
 
                // print out details of each message
                System.out.println("Message #" + (i + 1) + ":");
                System.out.println("\t From: " + from);
                System.out.println("\t To: " + to);
                System.out.println("\t Cc: " + cc);
                System.out.println("\t Bcc: " + bcc);
                System.out.println("\t ContentType:" + contentType);
                System.out.println("\t Subject: " + subject);
                System.out.println("\t Sent Date: " + sentDate);
                System.out.println("\t Message: " + messageContent);
                System.out.println("\t Attachments: " + attachFiles);
            }
 
            // disconnect
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for pop3.");
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
 
    /**
     * Return the primary text content of the message.
     */
    //Oracle官網抄來的，傳入一個BodyPart，返回裡面的body message
    private String getText(Part p) throws MessagingException, IOException {
       	boolean textIsHtml = false;
        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
            textIsHtml = p.isMimeType("text/html");
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
    
    //自已寫的，傳入一個附件的BodyPart，返回一個InputStream
    public InputStream getInputStream(MimeBodyPart part) throws MessagingException, IOException{
    	InputStream in = null;
    	System.out.println("part.getDisposition()=" + part.getDisposition());
    	if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
    		in = part.getInputStream();
    	}
    	return in;
    }
    
    //
    public String getAddresses(Address[] addrs){
    	String result = "";
    	try{
    		for(int i=0;i<addrs.length;i++){
    			if(result.length()==0){
    				result += MimeUtility.decodeText(addrs[i].toString());
    			}else{
    				result += "," + MimeUtility.decodeText(addrs[i].toString());
    			}
    		}
    	}catch(NullPointerException ex){
    		//do nothing because addrs maybe null
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return result;
    }
    
    /**
     * Runs this program with Gmail POP3 server
     */
    public static void main(String[] args) {
        String host = "pop3.pchome.com.tw";
        String port = "110";
        String userName = "tonychacha";
        String password = "car007e2";
 
        String saveDirectory = "/home/anntony/Attachment";
 
        EmailAttachmentReceiver receiver = new EmailAttachmentReceiver();
        receiver.setSaveDirectory(saveDirectory);
        receiver.downloadEmailAttachments(host, port, userName, password);
 
    }
}