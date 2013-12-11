package back.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import back.model.Attachment;
import back.model.Folder;

public class AttachmentDao {
	private static AttachmentDao instance = null;
	public static AttachmentDao getInstance(){
		if(instance==null) instance = new AttachmentDao();
		return instance;
	}
	/*
	CREATE DEFINER=`root`@`localhost` PROCEDURE `addAttachment`(
	p1 BIGINT,
	p2 VARCHAR(255) CHARACTER SET utf8,
	p3 MEDIUMBLOB
	)
	BEGIN
		insert into attachments(mailId,filename,_in) values(p1,p2,p3);
	END
	 */
	//新增一個附件
	public boolean addAttachment(Attachment atta){
		boolean result = false;
		Connection conn = null;
		try{
			conn = DBConn.getConn();
			CallableStatement stmt = conn.prepareCall("{call addAttachment(?,?,?)}");//呼叫資料庫端的預儲程序
			stmt.setLong(1, atta.getMailId());
			stmt.setString(2, atta.getFilename());
			stmt.setBlob(3, atta.getIn());
			int i = stmt.executeUpdate();
			if(i==1){
				System.out.println("成功新增一個附件:");
				result = true;
			}else{
				System.out.println("新增附件失敗:");
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
	
	//依id取出一個附件
	/*
	CREATE PROCEDURE `bookstore`.`getAttachment` (
	p1 BIGINT
	)
	BEGIN
		select * from attachments where id=p1;
	END
	 */
	public Attachment getAttachment(long id){
		Attachment atta = null;
		Connection conn = null;
		try{
			conn = DBConn.getConn();
			CallableStatement stmt = conn.prepareCall("{call getAttachment(?)}");//呼叫資料庫端的預儲程序
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				atta = new Attachment();
				atta.setId(rs.getLong("id"));
				atta.setMailId(rs.getLong("mailId"));
				atta.setFilename(rs.getString("filename"));
				atta.setIn(rs.getBinaryStream("_in"));
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
		return atta;
	}
	
	//依指定的條件查詢附件
	public List<Attachment> queryAttachments(String where,String order,int pageNumber,int pageSize){
		List<Attachment> attachments = new ArrayList<Attachment>();
		Connection conn = null;
		String sql = "select * from attachments";
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
		System.out.println("[queryAttachments函數]執行的SQL句:" + sql);
		try{
			conn = DBConn.getConn();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Attachment atta = new Attachment();
				atta.setId(rs.getLong("id"));
				atta.setMailId(rs.getLong("mailId"));
				atta.setFilename(rs.getString("filename"));
				atta.setIn(rs.getBinaryStream("_in"));
				attachments.add(atta);
			}
			System.out.println("返回了" + attachments.size() + "個附件");
			if(attachments.size()==0) attachments = null;
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
		return attachments;
	}
	
	//依mailId查詢附件
	public List<Attachment> getAttachments(long mailId){
		List<Attachment> attachments = null;
		String where = "where mailId=" + mailId;
		String order = "";
		int pageNumber = 0;
		int pageSize = 0;
		attachments = queryAttachments(where,order,pageNumber,pageSize);
		return attachments;
	}
}
