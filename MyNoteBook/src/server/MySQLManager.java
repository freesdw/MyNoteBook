package server;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import client.io.User;

public class MySQLManager {
	private String driver = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/notepad";
	private String user = "root";
	private String password = "960919";
	
	private Connection connection = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	private PreparedStatement ps = null;
	
	private static MySQLManager instance = new MySQLManager();
	
	public MySQLManager(){
		getConnection();
	}
	
	public static MySQLManager getInstance(){
		return instance;
	}

	// 获取数据库连接 --
	public void getConnection(){
		try{
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			System.out.println("连接驱动失败");
			e.printStackTrace();
		}
		
		try{
			connection = DriverManager.getConnection(url, user, password);
			statement = connection.createStatement();
		} catch (Exception e) {
			System.out.println("连接数据库失败");
			e.printStackTrace();
		}
	}
	
	// 添加用户 --
	public boolean add(String email, String password){
		int flag = 0;
		try{
			ps = connection.prepareStatement("insert into newuser (email,password,user) values(?,?,?)");
			ps.setString(1, email);
			ps.setString(2, password);
			ps.setObject(3, new User(email, password));
			flag = ps.executeUpdate();
			ps.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		return flag == 1;
	}
	
	// 删除用户
	public boolean remove(String email, boolean removeFile){
		int flag = 0;
		try{
			flag = statement.executeUpdate("delete from newuser where email = '" + email + "'");
			if(flag == 1 && removeFile)
				flag = statement.executeUpdate("delete from notes where email = '" + email + "'");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag == 1;
	}
	
	// 查询用户 --
	public User query(String email){
		User user = null;
		try{
			resultSet = statement.executeQuery("select * from newuser where email = '" + email + "'");
			if(resultSet.next()){
				Blob inBlob = resultSet.getBlob(3);
				InputStream is = inBlob.getBinaryStream();
				BufferedInputStream bis = new BufferedInputStream(is);
				byte [] buff = new byte[(int)(inBlob.length())];
				while(-1 != (bis.read(buff, 0, buff.length))){
					ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buff));
					user = (User)ois.readObject();
				}
			}
			resultSet.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	// 改变用户
	public boolean update(String email, String type, Object obj){
		int flag = 0;
		try{
			if(type.equals("user")){
				User user = (User)obj;
				remove(email, false);
				ps = connection.prepareStatement("insert into newuser (email,password,user) values(?,?,?)");
				ps.setString(1, email);
				ps.setString(2, password);
				ps.setObject(3, user);
				flag = ps.executeUpdate();
				ps.close();
			}
			else
				flag = statement.executeUpdate("update newuser set " + type + " = '" + (String)obj +"' where email = '" 
						+ email + "'");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag == 1;
	}
	
	// 添加文件/图片 --
	public boolean addFile(String email, String groupName, String noteName, File file){
		int flag = 0;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			ps = connection.prepareStatement("insert into notes (email, groupname, notename, file) values (?,?,?,?)");
			ps.setString(1, email);
			ps.setString(2, groupName);
			ps.setString(3, noteName);
			ps.setBinaryStream(4, fis, (int) file.length());
			flag = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				if(ps != null)
					ps.close();
				if(fis != null)
					fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag == 1;
	}
	
	// 读取文件/图片
	public File readFile(String email, String groupName, String notename){
		byte [] buffer = new byte[4096];
		File file = null;
		FileOutputStream fos = null;
		InputStream is = null;
		ResultSet rs = null;
		try{
			rs = statement.executeQuery("select * from notes where email = '" + email + "' and groupname = '" + groupName + "' and notename = '" + notename +"'");
			rs.next();
			file = new File("data/Server/new.txt");
			if(!file.exists())
				file.createNewFile();
			fos = new FileOutputStream(file);
			is = rs.getBlob("file").getBinaryStream();
			int size = 0;
			while ((size = is.read(buffer)) != -1) {
				System.out.println(1);
    	    	fos.write(buffer, 0, size);
    	    }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				if(is != null)
					is.close();
				fos.close();
				rs.close();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		return file;
	}
	
	// 删除指定文件/图片
	public boolean removeFile(String email, String groupName, String noteName){
		int flag = 0;
		try{
			String sql = "delete from notes where email = '" + email + "' and groupname = '" + groupName + "'";
			if(!noteName.equals(""))
				sql += " and notename = '" + noteName + "'";
			flag = statement.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag == 1;
	}
	
	// 改变数据库名称或者文件名
	public boolean changeNote(String email, String groupName, String noteName, String newName, boolean f){
		int flag = 0;
		try{
			if(f){
				flag = statement.executeUpdate("update notes set groupname = '" + newName +"' where email = '" 
					+ email + "' AND groupname = '" + groupName +"'");
				
			}
			else{
				flag = statement.executeUpdate("update notes set notename = '" + newName +"' where email = '" 
						+ email + "' AND groupname = '" + groupName +"' AND notename = '" + noteName + "'");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag > 0;
	}
	
	// 改变文件/图片
	public boolean updateFile(String email, String groupName, String noteName, File file){
		int flag = 0;
		removeFile(email, groupName, noteName);
		addFile(email, groupName, noteName, file);
		return flag == 1;
	}
	
	// 查询信息
	public File[] queryMessage(String email){
		byte [] buffer = new byte[4096];
		File [] files = null;
		FileOutputStream fos = null;
		InputStream is = null;
		try{
			ps = connection.prepareStatement("select file from message where email = '" + email + "'");
			resultSet = ps.executeQuery();
			files = new File[resultSet.getRow()];
			resultSet.beforeFirst();
			int i = 0;
			while(resultSet.next()){
				String fileName = resultSet.getString(2);
				files[i] = new File("data/Server/" + fileName);
				if(!files[i].exists())
					files[i].createNewFile();
				fos = new FileOutputStream(files[i]);
				is = resultSet.getBlob(3).getBinaryStream();
				int size = 0;
				while ((size = is.read(buffer)) != -1) {
	    	    	fos.write(buffer, 0, size);
	    	    }
				statement.executeUpdate("delete from newuser where email = '" + email + "' AND fileName = '" + fileName + "'");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				if(is != null)
					is.close();
				if(fos != null)
					fos.close();
				if(ps != null)
					ps.close();
				if(resultSet != null)
					resultSet.close();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		return files;
	}

	// 添加未发送信息
	public boolean addMessage(String email, String fileName, File file){
		int flag = 0;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			ps = connection.prepareStatement("insert into message (email, filename, file) values (?,?,?)");
			ps.setString(1, email);
			ps.setString(2, fileName);
			ps.setBinaryStream(3, fis, (int) file.length());
			flag = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				if(ps != null)
					ps.close();
				if(fis != null)
					fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag == 1;
	}
}
