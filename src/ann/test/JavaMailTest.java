package ann.test;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailTest {
	 public static void postMail( String recipient, String subject, String message, String from ) throws MessagingException { 
		 Properties props = new Properties(); 	
		 props.put( "mail.smtp.host", "smtp.pchome.com.tw" );
		 props.put( "mail.smtp.auth","true");

		 Authenticator authenticator = new Authenticator () {
			 public PasswordAuthentication getPasswordAuthentication(){
				 return new PasswordAuthentication("tonychacha","car007e2");
			 }
		 };

		 Session session = Session.getDefaultInstance( props , authenticator); 
		 Message msg = new MimeMessage( session ); 
		 InternetAddress addressFrom = new InternetAddress( from ); 
		 msg.setFrom( addressFrom ); 
		 InternetAddress addressTo = new InternetAddress( recipient ); 
		 msg.setRecipient( Message.RecipientType.TO, addressTo ); 
		 msg.setSubject( subject ); 
		 //msg.setContent( message, "text/plain" );
		 msg.setContent(message,"text/html;charset=UTF-8");

		 Transport.send( msg ); 
	 }
	 
	 public static void readmail() throws Exception {
		    // mail server connection parameters
		    String host = "pop3.pchome.com.tw";
		    String user = "tonychacha";
		    String password = "car007e2";

		    // connect to my pop3 inbox
		    Properties properties = System.getProperties();
		    Session session = Session.getDefaultInstance(properties);
		    Store store = session.getStore("pop3");
		    store.connect(host, user, password);
		    Folder inbox = store.getFolder("Inbox");
		    inbox.open(Folder.READ_ONLY);

		    // get the list of inbox messages
		    Message[] messages = inbox.getMessages();

		    if (messages.length == 0) System.out.println("No messages found.");

		    for (int i = 0; i < messages.length; i++) {
		      // stop after listing ten messages
		      if (i > 10) {
		        System.exit(0);
		        inbox.close(true);
		        store.close();
		      }

		      System.out.println("-----------------------------");
		      System.out.println("Message " + (i + 1));
		      System.out.println("From : " + messages[i].getFrom()[0]);
		      System.out.println("Subject : " + messages[i].getSubject());
		      System.out.println("Content :" + messages[i].getContent());
		      System.out.println("Sent Date : " + messages[i].getSentDate());
		      System.out.println();
		    }

		    inbox.close(true);
		    store.close();
	 }
	 
	 public static void main( String[] args ) throws Exception {
		 /*
	    postMail( "annbigbig@gmail.com", 
	              "我只是試試JavaMail", 
	              "沒什麼啦我只是想試試JavaMail，哈哈哈", 
	              "tonychacha@pchome.com.tw"); 
	    System.out.println("郵件已寄出");
	    */
		 readmail();
	 } 
}
