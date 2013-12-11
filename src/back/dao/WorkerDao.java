package back.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import back.model.Worker;

public class WorkerDao {
	private static WorkerDao instance = null;
	public static WorkerDao getInstance(){
		if(instance==null) instance = new WorkerDao();
		return instance;
	}
	
	//新增一個工作人員
	/*
	CREATE DEFINER=`root`@`localhost` PROCEDURE `addWorker`(
	p1 VARCHAR(30) CHARACTER SET utf8,
	p2 VARCHAR(30) CHARACTER SET utf8,
	p3 INTEGER)
	BEGIN
		insert into workers(name,password,permission) values(p1,p2,p3);
	END
	 */
	public boolean addWorker(Worker worker){
		boolean result = false;
		Connection conn = null;
		try{
			conn = DBConn.getConn();
			CallableStatement stmt = conn.prepareCall("{call addWorker(?,?,?)}");//呼叫資料庫端的預儲程序
			stmt.setString(1, worker.getName());
			stmt.setString(2, worker.getPassword());
			stmt.setInt(3, worker.getPermission());
			int i = stmt.executeUpdate();
			if(i==1){
				System.out.println("成功新增一個工作人員:");
				result = true;
			}else{
				System.out.println("新增工作人員失敗:");
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
	
	//依帳號名稱及密碼返回實體
	/*
	CREATE DEFINER=`root`@`localhost` PROCEDURE `getWorker`(
	p1 VARCHAR(30) CHARACTER SET utf8,
	p2 VARCHAR(30) CHARACTER SET utf8)
	BEGIN
		select * from workers where name=p1 AND password=p2;
	END	
 	*/
	public Worker getWorker(String name,String password){
		Worker worker = null;
		Connection conn = null;
		try{
			conn = DBConn.getConn();
			CallableStatement stmt = conn.prepareCall("{call getWorker(?,?)}");//呼叫資料庫端的預儲程序
			stmt.setString(1, name);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				worker = new Worker();
				worker.setName(rs.getString("name"));
				worker.setPassword(rs.getString("password"));
				worker.setPermission(rs.getInt("permission"));
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
		return worker;
	}
	
	/*
	public Worker getWorker(String name,String password){
		Worker worker = null;
		if(name.equals("ann")&&(password.equals("12345"))){
			worker = new Worker();
			worker.setName(name);
			worker.setPassword(password);
			worker.setPermission(200);
		}
		return worker;
	}
	*/
}
