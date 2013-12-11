package back.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import back.model.Mail;

public class MailDao {
	private static MailDao instance = null;
	public static MailDao getInstance(){
		if(instance==null) instance = new MailDao();
		return instance;
	}
	
	/*
	CREATE DEFINER=`root`@`localhost` PROCEDURE `addMail`(
	p1 BIGINT,
	p2 VARCHAR(100) CHARACTER SET utf8,
	p3 VARCHAR(255) CHARACTER SET utf8,
	p4 VARCHAR(255) CHARACTER SET utf8,
	p5 VARCHAR(255) CHARACTER SET utf8,
	p6 VARCHAR(100) CHARACTER SET utf8,
	p7 VARCHAR(100) CHARACTER SET utf8,
	p8 VARCHAR(30) CHARACTER SET utf8,
	p9 TEXT CHARACTER SET utf8,
	OUT lastId BIGINT
	)
	BEGIN
		insert into mails(folderId,_from,_to,cc,bcc,contentType,subject,sentDate,bodyMessage) values(p1,p2,p3,p4,p5,p6,p7,p8,p9);
		select LAST_INSERT_ID() into lastId;
	END
	 */
	//新增一封郵件到資料庫，如果成功會返回主鍵id的值，失敗則會返回-1
	public long addMail(Mail mail){
		long result = -1;
		Connection conn = null;
		try{
			cutString(mail);
			conn = DBConn.getConn();
			CallableStatement stmt = conn.prepareCall("{call addMail(?,?,?,?,?,?,?,?,?,?)}");//呼叫資料庫端的預儲程序
			stmt.setLong(1, mail.getFolderId());
			stmt.setString(2, mail.getFrom());
			stmt.setString(3, mail.getTo());
			stmt.setString(4, mail.getCc());
			stmt.setString(5, mail.getBcc());
			stmt.setString(6, mail.getContentType());
			stmt.setString(7, mail.getSubject());
			stmt.setString(8, mail.getSentDate());
			stmt.setString(9, mail.getBodyMessage());
			stmt.registerOutParameter(10, Types.BIGINT);
			int i = stmt.executeUpdate();
			long lastId = stmt.getLong(10);
			if(i==1){
				System.out.println("成功新增一封郵件:" + lastId);
				result = lastId;
			}else{
				System.out.println("新增郵件失敗:");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	//依id取出一封郵件
	/*
	CREATE DEFINER=`root`@`localhost` PROCEDURE `getMail`(
	p1 BIGINT
	)
	BEGIN
		select * from mails where id=p1;
	END	
	 */
	public Mail getMail(long id){
		Mail mail = null;
		Connection conn = null;
		try{
			conn = DBConn.getConn();
			CallableStatement stmt = conn.prepareCall("{call getMail(?)}");//呼叫資料庫端的預儲程序
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				mail = new Mail();
				mail.setId(rs.getLong("id"));
				mail.setFolderId(rs.getLong("folderId"));
				mail.setFrom(rs.getString("_from"));
				mail.setTo(rs.getString("_to"));
				mail.setCc(rs.getString("cc"));
				mail.setBcc(rs.getString("bcc"));
				mail.setContentType(rs.getString("contentType"));
				mail.setSubject(rs.getString("subject"));
				mail.setSentDate(rs.getString("sentDate"));
				mail.setBodyMessage(rs.getString("bodyMessage"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mail;
	}
	
	//依指定的條件查詢郵件
	public List<Mail> queryMails(String where,String order,int pageNumber,int pageSize){
		List<Mail> mails = new ArrayList<Mail>();
		Connection conn = null;
		String sql = "select * from mails";
		if(where.length()>0){
			sql+= " " + where;
		}
		if(order.length()>0){
			sql+= " " + order;
		}
		if(pageNumber>0&&pageSize>0){
			String limit = " limit " + (pageNumber-1)*pageSize + "," + pageSize;
			sql += limit;
		}
		sql+= ";";
		System.out.println("[queryMails函數]執行的SQL句:" + sql);
		try{
			conn = DBConn.getConn();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Mail mail = new Mail();
				mail.setId(rs.getLong("id"));
				mail.setFolderId(rs.getLong("folderId"));
				mail.setFrom(rs.getString("_from"));
				mail.setTo(rs.getString("_to"));
				mail.setCc(rs.getString("cc"));
				mail.setBcc(rs.getString("bcc"));
				mail.setContentType(rs.getString("contentType"));
				mail.setSubject(rs.getString("subject"));
				mail.setSentDate(rs.getString("sentDate"));
				mail.setBodyMessage(rs.getString("bodyMessage"));
				mails.add(mail);
			}
			System.out.println("取出了" + mails.size() + "封郵件");
			if(mails.size()==0) mails = null;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mails;
	}
	
	//依指定的條件查詢郵件，為了避免選到其他工作人員的郵件，使用SQL句的Inner Join
	//select * from mails,folders where mails.folderId=folders.id AND workerName='annbigbig' AND subject like '%tes%' limit 0,200;
	public List<Mail> queryMails(String tables,String where,String order,int pageNumber,int pageSize){
		List<Mail> mails = new ArrayList<Mail>();
		Connection conn = null;
		String sql = "select * from " + tables;
		if(where.length()>0){
			sql+= " " + where;
		}
		if(order.length()>0){
			sql+= " " + order;
		}
		if(pageNumber>0&&pageSize>0){
			String limit = " limit " + (pageNumber-1)*pageSize + "," + pageSize;
			sql += limit;
		}
		sql+= ";";
		System.out.println("[queryMails函數]執行的SQL句:" + sql);
		try{
			conn = DBConn.getConn();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Mail mail = new Mail();
				mail.setId(rs.getLong("id"));
				mail.setFolderId(rs.getLong("folderId"));
				mail.setFrom(rs.getString("_from"));
				mail.setTo(rs.getString("_to"));
				mail.setCc(rs.getString("cc"));
				mail.setBcc(rs.getString("bcc"));
				mail.setContentType(rs.getString("contentType"));
				mail.setSubject(rs.getString("subject"));
				mail.setSentDate(rs.getString("sentDate"));
				mail.setBodyMessage(rs.getString("bodyMessage"));
				mails.add(mail);
			}
			System.out.println("取出了" + mails.size() + "封郵件");
			if(mails.size()==0) mails = null;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mails;
	}
	
	//依[查詢條件]返回符合條件的記錄列數
	public int countTotalRows(String where){
		Connection conn = null;
		int totalRows = 0;
		String sql = "select count(*) as rows from mails " + where;
		try{
			conn = DBConn.getConn();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				totalRows = rs.getInt("rows");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return totalRows;
	}
	
	//依[查詢條件]返回符合條件的記錄列數，多了一個參數tables，這是為了使用SQL句的INNER Join
	public int countTotalRows(String tables,String where){
		Connection conn = null;
		int totalRows = 0;
		String sql = "select count(*) as rows from " + tables + " " + where;
		try{
			conn = DBConn.getConn();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				totalRows = rs.getInt("rows");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return totalRows;
	}
	
	public void cutString(Mail mail){
		//如果字串太長就縮短它，為什麼這麼麻煩，因為無法預期外部寄信者會不會寄含有超長欄位值的信件(超長主旨或超長的CC)
		if(mail.getFrom().length()>100) mail.setFrom(mail.getFrom().substring(0, 100));
		if(mail.getTo().length()>255) mail.setTo(mail.getTo().substring(0,255));
		if(mail.getCc().length()>255) mail.setCc(mail.getCc().substring(0, 255));
		if(mail.getBcc().length()>255) mail.setBcc(mail.getBcc().substring(0, 255));
		if(mail.getContentType().length()>100) mail.setContentType(mail.getContentType().substring(0, 100));
		if(mail.getSubject().length()>100) mail.setSubject(mail.getSubject().substring(0, 100));
		if(mail.getSentDate().length()>30) mail.setSentDate(mail.getSentDate().substring(0, 30));		
	}
}
