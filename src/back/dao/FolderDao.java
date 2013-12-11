package back.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import back.model.Folder;

public class FolderDao {
	private static FolderDao instance = null;
	public static FolderDao getInstance(){
		if(instance==null) instance = new FolderDao();
		return instance;
	}

	/*
	CREATE DEFINER=`root`@`localhost` PROCEDURE `addFolder`(
	p1 BIGINT,
	p2 VARCHAR(30) CHARACTER SET utf8,
	p3 VARCHAR(100) CHARACTER SET utf8
	)
	BEGIN
		insert into folders(parentId,workerName,name) values(p1,p2,p3);
	END
	 */
	//新增一個信件夾
	public boolean addFolder(Folder folder){
		boolean result = false;
		Connection conn = null;
		try{
			conn = DBConn.getConn();
			CallableStatement stmt = conn.prepareCall("{call addFolder(?,?,?)}");//呼叫資料庫端的預儲程序
			long pid = folder.getParentId();
			if(pid>0){
				stmt.setLong(1, pid);
			}else{
				stmt.setNull(1, Types.BIGINT);
			}
			stmt.setString(2, folder.getWorkerName());
			stmt.setString(3, folder.getName());
			int i = stmt.executeUpdate();
			if(i==1){
				System.out.println("成功新增一個信件夾:");
				result = true;
			}else{
				System.out.println("新增信件夾失敗:");
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
	
	//依id取出指定的一個信件夾
	/*
	CREATE DEFINER=`root`@`localhost` PROCEDURE `getFolder`(
	p1 BIGINT
	)
	BEGIN
		select * from folders where id=p1;
	END	
 	*/
	public Folder getFolder(long id){
		Folder folder = null;
		Connection conn = null;
		try{
			conn = DBConn.getConn();
			CallableStatement stmt = conn.prepareCall("{call getFolder(?)}");//呼叫資料庫端的預儲程序
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				folder = new Folder();
				folder.setId(rs.getLong("id"));
				folder.setParentId(rs.getLong("parentId"));
				folder.setWorkerName(rs.getString("workerName"));
				folder.setName(rs.getString("name"));
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
		return folder;
	}
	
	//取出指定條件的信件夾
	public List<Folder> queryFolders(String where,String order,int pageNumber,int pageSize){
		List<Folder> folders = new ArrayList<Folder>();
		Connection conn = null;
		String sql = "select * from folders";
		if(where.length()>0){
			sql+= " " + where;
		}
		if(order.length()>0){
			sql+= " " + order;
		}
		String limit = " limit " + (pageNumber-1)*pageSize + "," + pageSize;
		sql+= limit +";";
		System.out.println("[queryFolders函數]執行的SQL句:" + sql);
		try{
			conn = DBConn.getConn();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Folder f = new Folder();
				f.setId(rs.getLong("id"));
				f.setParentId(rs.getLong("parentId"));
				f.setWorkerName(rs.getString("workerName"));
				f.setName(rs.getString("name"));
				folders.add(f);
			}
			System.out.println("返回了" + folders.size() + "個資料夾");
			if(folders.size()==0) folders = null;
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
		return folders;
	}
	
	//傳入workerName，取出此工作人員的inbox信件夾
	public Folder getInbox(String workerName){
		String where = "where parentId IS NULL AND workerName='" + workerName + "' AND name='inbox'";
		String order = "";
		int pageNumber = 1;
		int pageSize = 1;
		List<Folder> folders = queryFolders(where,order,pageNumber,pageSize);
		Folder folder = null;
		if(folders.size()==1){
			folder = folders.get(0);
		}
		return folder;
	}
}
