package ann.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import back.dao.FolderDao;
import back.dao.MailDao;
import back.dao.WorkerDao;
import back.model.Folder;
import back.model.Mail;
import back.model.Worker;
import ann.tool.BaseConversion;
import ann.tool.JavaMailTool;
import ann.tool.RegexTool;
import ann.tool.StringTool;

public class annTest {

	public static void main(String[] args) throws Exception {
		test16();

	}

	public static void test1(){
		int i = 128;
		//System.out.println(Integer.toBinaryString(i));
		
		System.out.println(Integer.bitCount(i));
		
	}
	
	//測試BaseConversion類
	public static void test2(){
		BaseConversion tool = BaseConversion.getInstance();
		tool.isOne(63, 2,true);
	}
	
	//測試餘數和商數
	public static void test3(){
		System.out.println("155%2=" + 155%2);
		System.out.println("155/2=" + 155/2);
		System.out.println("1/2=" + 1/2);
		System.out.println("500/10=" + 500/10);
		System.out.println("501/10=" + 501/10);
		System.out.println("509/10=" + 509/10);
		System.out.println("510/10=" + 510/10);
	}
	
	//測試BaseConversion類
	public static void test4(){
		BaseConversion tool = BaseConversion.getInstance();
		tool.binaryString(63,true);
	}
	
	//測試regular expression
	public static void test5(){
		String text    =
		        "This is the text to be searched " +
		        "for occurrences of the http:// pattern.";

		String patternString = ".*http://.*";

		Pattern pattern = Pattern.compile(patternString);

		Matcher matcher = pattern.matcher(text);
		boolean result = matcher.matches();
		System.out.println(result ? "符合":"不符合");
	}
	
	//測試regular expression
	public static void test6(){
		String text  = "/annbookstore/back/mis/index.action";

		String patternString = ".*/back/mis/.*";
		Pattern pattern = Pattern.compile(patternString);

		Matcher matcher = pattern.matcher(text);
		boolean result = matcher.matches();
		System.out.println(result ? "符合":"不符合");
	}
	
	//測試自定義工具類RegexTool
	public static void test7(){
		RegexTool tool = RegexTool.getInstance();
		String text  = "/annbookstore/back/mis/index.action";
		String patternStr = ".*/back/mis/.*";
		boolean result = tool.match(text, patternStr);
		System.out.println("text=" + text);
		System.out.println("patternStr=" + patternStr);
		System.out.println(result ? "符合":"不符合");
		
	}
	
	//測試StringTool類
	public static void test8(){
		StringTool sTool = StringTool.getInstance();
		String str = "55321";
		System.out.println(sTool.fillzero(str, 8));
	}
	
	//測試原生的String[]
	public static void test9(){
		String[] args = {"myhost","myport","username","password"};
		for(int i=0;i<args.length;i++){
			System.out.println("args[" + i + "]=" +args[i]);
		}
	}
	
	//測試我寫的JavaMailTool，使用pchome信箱pop3收信
	public static void test10() throws Exception{
		String[] args = {"pop3.pchome.com.tw","110","tonychacha","car007e2"};
		JavaMailTool tool = new JavaMailTool(args);
		tool.downloadEmailAttachments();
	}
	
	//測試我寫的JavaMailTool，使用yahoo信箱pop3收信
	public static void test11() throws Exception{
		String[] args = {"pop.mail.yahoo.com","110","annbigbig","tel26366591"};
		JavaMailTool tool = new JavaMailTool(args);
		tool.downloadEmailAttachments();
	}
	
	//測試WorkerDao
	public static void test12(){
		WorkerDao dao = WorkerDao.getInstance();
		Worker worker = new Worker();
		worker.setName("annbigbig");
		worker.setPassword("tel26366591");
		worker.setPermission(200);
		if(dao.addWorker(worker)){
			System.out.println("新增Worker成功");
		}else{
			System.out.println("新增Worker失敗");
		}
	}
	
	//測試FolderDao
	public static void test13(){
		FolderDao dao = FolderDao.getInstance();
		Folder folder = new Folder();
		folder.setParentId(0); 	//根節點，所有的inbox資料夾都應該是根節點
		folder.setWorkerName("annbigbig");
		folder.setName("inbox");
		if(dao.addFolder(folder)){
			System.out.println("新增資料夾成功");
		}else{
			System.out.println("新增資料夾失敗");
		}
			
	}
	
	//測試Folder的pid初值
	public static void test14(){
		Folder folder = new Folder();
		System.out.println("folder.getParentId()=" + folder.getParentId());
	}
	
	//測試FolderDao的getInbox方法
	public static void test15(){
		FolderDao dao = FolderDao.getInstance();
		Folder inbox;
		inbox = dao.getInbox("annbigbig");
		if(inbox!=null){
			System.out.println("inbox.getId()=" + inbox.getId());
			System.out.println("inbox.getParentId()=" + inbox.getParentId());
			System.out.println("inbox.getWorkerName()=" + inbox.getWorkerName());
			System.out.println("inbox.getName()=" + inbox.getName());
		}else{
			System.out.println("什麼都沒有取出");
		}
		
	}
	
	//測試JavaMailTool，把Gmail信箱的信件收下來
	public static void test16() throws Exception{
		String[] args = {"pop.gmail.com","995","annbigbig","tel26366591"};
		JavaMailTool tool = new JavaMailTool(args);
		tool.downloadEmailAttachments();
	}
	
	//測試MailDao
	public static void test17(){
		MailDao dao = MailDao.getInstance();
		long id = 14934;
		Mail mail = dao.getMail(id);
		if(mail!=null){
			printMail(mail);
		}
	}
	
	//測試字串的replace置換
	public static void test18(){
		String target = "<div id='hey'></div>";
		target = target.replaceAll("[<]", "&lt;");
		target = target.replaceAll("[>]", "&gt;");
		System.out.println("target = [" + target + "]");
	}
	
	//測試RegexTool的escapeHtml方法
	public static void test19(){
		RegexTool tool = RegexTool.getInstance();
		String target = "<div id='hey'></div>";
		System.out.println("target = " + tool.escapeHtml(target));
	}
	
	public static void printMail(Mail mail){
		System.out.println("-----------------");
		System.out.println("mail.getId()=" + mail.getId());
		System.out.println("mail.getFolderId()=" + mail.getFolderId());
		System.out.println("mail.getSubject()=" + mail.getSubject());
		System.out.println("mail.getSentDate()=" + mail.getSentDate());
		System.out.println("mail.getBodyMessage()=" + mail.getBodyMessage());
	}
}
