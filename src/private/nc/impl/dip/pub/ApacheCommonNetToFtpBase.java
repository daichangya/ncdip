package nc.impl.dip.pub;





import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.util.dip.sj.RetMessage;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import sun.net.ftp.FtpProtocolException;

/**
 * 文件FTP上传基类
 * 
 * @author : 王鹏
 * @createDate: 2008.09.08
 * @version $Revision: 1.0 $
 * 
 */
public class ApacheCommonNetToFtpBase {
	private static SimpleDateFormat sdf_time   = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	private static String ip;

	private static String user;

	private static String password;

	private static int port;

	private static FTPClient ftpClient;

	/**
	 * 初始化
	 * 
	 * @param ip
	 *            FTP服务器地址
	 * @param user
	 *            服务名
	 * @param password
	 *            密码
	 * @param port
	 *            端口号
	 */
	public ApacheCommonNetToFtpBase(String ip, String user, String password,
			int port) {
		this.ip = ip;
		this.user = user;
		this.password = password;
		this.port = port;
		ftpClient = new FTPClient();
	}

	/**
	 * 上传文件
	 * 
	 * @param file
	 *            文件
	 * @return
	 */
	public RetMessage uploadFile(File file,boolean flag) throws Exception {
		if (!file.isFile()) {
			// 不是文件
			return new RetMessage(false,file.getName()+"不是文件");
		} else if (flag&&!fileSize(file)) {
			// 控制文件大小
			return new RetMessage(false,"超过文件大小限制");
		}

		//
		try{
			if (!connectServer()) {
				return new RetMessage(false,"连接ftp服务器失败");
			}
		}catch(Exception e){
			throw e;
		}

		// 给文件加上时间戳,防止服务器文件重复
		String fileName = file.getName();
		int lastIndex = fileName.lastIndexOf(".");
//		//给fileName重命名，加上时间戳，保证名字不重复
//		fileName = fileName.substring(0, lastIndex) + "_"
//				+ System.currentTimeMillis() + fileName.substring(lastIndex);

		try {
			FileInputStream fis = new FileInputStream(file);
			if (!ftpClient.storeFile(gbktoiso8859(fileName), fis)) {
				closeConnect();
				return new RetMessage(false,"上传文件失败");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return new RetMessage(false,"上传文件失败");
		} finally {
			closeConnect();
		}

		 return new RetMessage(true,"上传文件成功");
	}

	/**
	 * 上传文件
	 * 
	 * @param file
	 *            文件
	 * @param path
	 *            上传服务器路径
	 * @return
	 */
	public RetMessage uploadFile(File file, String path,boolean flag) throws Exception{
		RetMessage ret = new RetMessage();
		// 转到path路径
//		path = this.pathAddress(path);
//		// 创建路径
//		mkdir(path);
		// 进入服务器目录
//		ret=this.changeWorkingDirectory(path);
		if (!connectServer()) {
			throw new Exception("ftp连接失败");
		}
		if(path!=null&&!path.trim().equals("")){
			int state=ftpClient.cwd(path);
			if(state!=250){
				String str[]=path.split("/");
				if(str.length>0){
					for(int i=0;i<str.length;i++){
						boolean bb=ftpClient.changeWorkingDirectory(str[i]);
						if(!bb){
							boolean dd=ftpClient.makeDirectory(str[i]);
							if(!dd){
								ret.setIssucc(false);
								ret.setMessage("ftp上创建"+str[i]+"目录失败");
								return ret;
							}
							boolean dd1=ftpClient.changeWorkingDirectory(str[i]);
							if(!dd1){
								ret.setIssucc(false);
								ret.setMessage("ftp上创建"+str[i]+"目录后，进入"+str[i]+"失败");
								return ret;
							}
						}
					}
				}
//				ret.setIssucc(false);
//				ret.setMessage("进入"+path+"路径错误");
//				return ret;
			}
		}
		
//		if (!this.changeWorkingDirectory(path)) {
//			return new RetMessage(false,"进入"+path+"路径错误");
//		}
//		if(!ret.getIssucc()){
//			return ret;
//		}
		try{
			 ret=uploadFile(file,flag);
		}catch(Exception e){
			throw e;
		}
		
		return ret;
	}

	/**
	 * 下载文件
	 * 
	 * @param filename
	 *            文件名
	 * @return
	 */
	public InputStream downloadFile(String filename) throws Exception {
		try{
			if (!connectServer()) {
				return null;
			}
		}catch(Exception ex){
			throw ex;
		}
		InputStream is = null;
		try {
			is = ftpClient.retrieveFileStream(gbktoiso8859(filename));
			//System.out.println("read:" + is.available());
			return is;
		} catch (IOException e) {
			e.printStackTrace();
			closeConnect();
			return null;
		} // 退出FTP服务器
		// 不能马上退出,不然会出现文件下载到一半,没有下载完整的情况
		// this.closeConnect();
	}

	/**
	 * 下载文件
	 * 
	 * @param filename
	 *            文件名
	 * @param path
	 *            路径
	 * @return
	 */
	public RetMessage downloadFile(String filename, String path) throws Exception {
		RetMessage ret=new RetMessage();
		// 转到path路径
		InputStream bReturn = null;
//		path = this.pathAddress(path);
		if (!connectServer()) {
			throw new Exception("ftp连接失败");
		}
//		if (!this.changeWorkingDirectory(path)) {
//			return null;
//		}
		int w=ftpClient.cwd(path);
		if(w!=250){
			return new RetMessage(false,"下载"+filename+"文件出错");
		}
//			return null;
//		}else{
//			this.changeWorkingDirectory(path);
//		}
		try{
			Logger.debug("开始下载文件："+filename);
//			System.out.println("开始下载文件："+filename);
			bReturn = downloadFile(filename);
			if(bReturn!=null){
				ret.setIssucc(true);
				ret.setValue(bReturn);
				Logger.debug("下载文件"+filename+"完毕。"+"read0:" + bReturn.available());	
			}else{
				ret.setIssucc(false);
				ret.setMessage("下载文件"+filename+"出错");
				Logger.debug("下载文件"+filename+"出错");
			}
			
//			System.out.println("read0:" + bReturn.available());
		}catch(Exception e){
			throw e;
		}finally{
			this.closeConnect();
		}
		
		return ret;
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 *            路径
	 * @param filename
	 *            文件名
	 * @return
	 */
	public boolean deleteFile(String filename, String path) throws Exception {
		RetMessage ret=new RetMessage();
		// 转到path路径
		path = this.pathAddress(path);
		ret=this.changeWorkingDirectory(path);
		if (!ret.getIssucc()) {
			return false;
		} // 删除文件
		try{			
			return deleteFile(filename);
			
		}catch(Exception e){
			throw e;
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filename
	 *            文件名
	 * @return
	 */
	public boolean deleteFile(String filename) throws Exception {
		try {
			if (!connectServer()) {
				return false;
			}
			boolean b =ftpClient.deleteFile(gbktoiso8859(filename));
			
			return b;
		} catch (Exception ioe) {
			ioe.printStackTrace();			
			throw ioe;
		}finally{		
			closeConnect();
			
			
		}
	}
	/**
	 * 删除ftp上的文件
	 * 
	 * @param filename
	 *            文件名
	 * @return
	 */
	public boolean deleteFile(String[] files,String uploadDir) throws Exception {
		try {
			if (!connectServer()) {
				return false;
			}
//			boolean b =ftpClient.deleteFile(gbktoiso8859(filename));
			boolean b=true;
			if(files!=null&&files.length>0){
				for(int i=0;i<files.length;i++){
					try {
						System.out.println();
//						ftp.deleteFile(uploadDir+files[i]);
						b=ftpClient.deleteFile(gbktoiso8859(uploadDir+files[i]));
						Logger.debug("【"+sdf_time.format(new Date())+"】 "+"  删除ftp上的文件成功:"+uploadDir+files[i]+"  第"+(i+1)+"/"+files.length+"个文件", this.getClass(), "uploadFtp");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Logger.debug("【"+sdf_time.format(new Date())+"】 "+"  删除ftp上的文件失败:"+uploadDir+files[i]+"  第"+(i+1)+"/"+files.length+"个文件"+e.getMessage(), this.getClass(), "uploadFtp");
						
					}
				}
			}
			return b;
		} catch (Exception ioe) {
			ioe.printStackTrace();			
			throw ioe;
		}finally{		
			closeConnect();
			
			
		}
	}

	/**
	 * 获得指定目录下的文件或目录名称集合
	 * 
	 * @param path
	 */
	public String[] fileList(String path,String sType) throws Exception {
		Vector<String> vec = new Vector<String>();
		
		try {			
			path = pathAddress(path);
			if (connectServer()) {				
				FTPFile[] vFiles = ftpClient.listFiles(gbktoiso8859(path));
				
				for (int i = 0; i < vFiles.length; i++) {
					if (vFiles[i] != null) {
						if(sType.equals("file")  && vFiles[i].isFile()){
							vec.addElement(vFiles[i].getName());
						}else if(sType.equals("dir") && vFiles[i].isDirectory()){
							if(vFiles[i].getName().equals(".") || vFiles[i].getName().equals("..")){
								continue;
							}
							vec.addElement(vFiles[i].getName());
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();			
			throw e;
		} finally {
			// 退出FTP服务器
			//
			this.closeConnect();
		}
		String [] fileNames = new String[vec.size()];
		fileNames = (String[])vec.toArray(fileNames);
		return fileNames;

	}

	/**
	 * 创建路径
	 * 
	 * @param path
	 *            路径名称
	 * @return
	 */
	public boolean mkdir(String path) throws Exception {
		try{
			if (!connectServer()) {
				return false;
			}
		}catch(Exception e){
			throw e;
		}
		try {
			path = this.pathAddress(path);
			return ftpClient.makeDirectory(gbktoiso8859(path));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 重命名文件
	 * 
	 * @param oldFileName
	 *            --原文件名
	 * @param newFileName
	 *            --新文件名
	 */
	public boolean renameFile(String oldFileName, String newFileName) throws Exception {
		try {
			if (!connectServer()) {
				return false;
			}
			return ftpClient.rename(gbktoiso8859(oldFileName),
					gbktoiso8859(newFileName));
		} catch (Exception ioe) {
			ioe.printStackTrace();
			closeConnect();
			throw ioe;
		}
	}

	/**
	 * 进入到服务器的某个目录下
	 * 
	 * @param directory
	 */
	public RetMessage changeWorkingDirectory(String directory) throws Exception {
		RetMessage bReturn=new RetMessage();
		try {
			
			if (!connectServer()) {
				return new RetMessage(false,"ftp连接失败");
			}
			

//			bReturn = ftpClient.changeWorkingDirectory("/");
			int i=ftpClient.cwd(gbktoiso8859(directory));
			if(i!=250){
				return new RetMessage(false,"进入"+directory+"目录失败");
			}
			//bReturn = ftpClient.changeWorkingDirectory(gbktoiso8859(directory));
			bReturn.setIssucc(true);
			return bReturn;
		} catch (Exception ioe) {
			this.closeConnect();
			ioe.printStackTrace();
			throw ioe;
		}
	}
	public RetMessage getListFileName(String file){
		RetMessage ret=new RetMessage();
		try {
			if (!connectServer()) {
				ret.setIssucc(false);
				ret.setMessage("ftp连接失败");
				return ret;
			}
			int w=ftpClient.cwd(file);
			if(w==250){
				String[]str=ftpClient.listNames();
				ftpClient.cwd("..");
				if(str!=null&&str.length>0){
					for(int i=0;i<str.length;i++){
						str[i]=iso8859togbk(str[i]);
						ret.setFileName(str[i]);
					}
					ret.setIssucc(true);
				}else{
					ret.setIssucc(false);
					ret.setMessage(file+"路径下没有文件");
				}
			}else{
				ret.setIssucc(false);
				ret.setMessage("进入"+file+"路径错误");
				return ret;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeConnect();
		}
		return ret;
	}
	/**
	 * 返回到上一层目录
	 */
	public boolean changeToParentDirectory() throws Exception {
		try {
			if (!connectServer()) {
				return false;
			}
			return ftpClient.changeToParentDirectory();
		} catch (Exception ioe) {
			ioe.printStackTrace();
			closeConnect();
			throw ioe;
		}
	}

	/**
	 * 登录ftp服务器
	 * 
	 */
	public boolean connectServer() throws Exception{
		boolean flag=true;
		if (ftpClient.isConnected() == false) {
			try {
				
				// 所有使用iso-8859-1做为通讯编码集
//				ftpClient.setControlEncoding("iso-8859-1");
				// ftpClient.configure(getFTPClientConfig());
//				Logger.debug("===================\n  ysjh FTP connect begin:ip:"+ip+"\tport:"+port,ApacheCommonNetToFtpBase.class,"connectServer\n");
			
				ftpClient.connect(ip, port);
		
//				Logger.debug("===================\nysjh FTP connect end:ip:"+ip+"\tport:"+port,ApacheCommonNetToFtpBase.class,"connectServer\n");
//				System.out.println("开始登录ftp");
				Logger.debug("开始登录ftp");
				if (!ftpClient.login(user, password)) {
					// 登录失败
					flag=false;
				throw new Exception("ftp：登陆用户（"+user+"）或密码错误！");					
					
				}
				Logger.debug("登录ftp结束");
//				System.out.println("登录ftp结束");
				// 状态
				int reply = ftpClient.getReplyCode();
				if (!FTPReply.isPositiveCompletion(reply)) {
					closeConnect();
					flag=false;
					return flag;
				}

				// 用被动模式传输
				ftpClient.enterLocalPassiveMode();
				// 将文件传输类型设置为二进制
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				// 防止server超时断开
				//ftpClient.setDefaultTimeout(60000);
				// 10连接超时
				ftpClient.setSoTimeout(10000);
			} catch (SocketException se) {
//				Logger.debug("ysjh FTP connect error:ip:"+ip+"\tport:"+port,ApacheCommonNetToFtpBase.class,"connectServer");
				se.printStackTrace();	
				flag=false;
				throw new Exception("FTP CONNECT ERROR IP:"+ip+"\tPORT:"+port+" 请检查此IP及端口号配置是否正确或指定的ftp服务器是否正常运行！");				
			} catch (Exception e) {
				e.printStackTrace();
				flag=false;
				throw e;
			}
			flag=true;
		}
		return flag;
	}

	/**
	 * 关闭连接
	 */
	public void closeConnect() {
		try {
			System.out.println("开始关闭ftp连接ftpClient.isConnected()"+ftpClient.isConnected());
			if (ftpClient != null) {
//				System.out.println("开始推出ftp连接");
				Logger.debug("开始推出ftp连接");
//				Boolean b;
//				b = ftpClient.logout();
//				System.out.println("logout完成,b:"+b);
				ftpClient.disconnect();
//				System.out.println("推出ftp连接成功");
				Logger.debug("推出ftp连接成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 写文件到本地
	 * 
	 * @param is
	 *            文件流
	 * @param allPath
	 *            全路径=文件路径+文件名
	 * @return
	 */
	public RetMessage writeFile(InputStream is, String allPath) {
		if (is == null || allPath == null) {
			return new RetMessage(false,"文件和文件路径不能为空");
		}
		File file=new File(allPath);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		BufferedInputStream bis = null; 			
		BufferedOutputStream bos = null;
		try {
			// 默认也是2048
		//	int buffered = 2048;
			 bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(new FileOutputStream(allPath));
			System.out.println("read2:"+bis.available());
			byte[] bytes = new byte[2048];
						
			int len = bis.read(bytes);
			while(len!=-1){
				bos.write(bytes,0,len);
				len = bis.read(bytes);				
			}
			bos.flush();	
//			System.out.println("文件("+allPath+")下载本地写入完毕");
			Logger.debug("文件("+allPath+")下载本地写入完毕");
			return new RetMessage(true,"");
		} catch (Exception e) {
			e.printStackTrace();
			return new RetMessage(false,e.getMessage());
		}finally{
			// 将缓冲区中的数据全部写出
			try {
				
				// 关闭流
				bis.close();
				bos.close();
//				System.out.println("文件("+allPath+")已关闭");
				Logger.debug("文件("+allPath+")已关闭");
			} catch (IOException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		
		}
	}
	/**
	 * ftp写文件到本地
	 * 
	 * @param is
	 *            文件流
	 * @param allPath
	 *            全路径=文件路径+文件名
	 * @return
	 */
	public boolean writeFileFTP(InputStream is, String allPath) {
		
		if (is == null || allPath == null) {
			return false;
		}
		BufferedInputStream bis = null; 			
		BufferedOutputStream bos = null;
		try {
			// 默认也是2048
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(new FileOutputStream(allPath));
			System.out.println("read2:"+bis.available());
//			byte[] bytes = new byte[bis.available()];
			byte[] bytes = new byte[1];
			
			
			/*bis.read(bytes);
			bos.write(bytes);
			bos.flush();*/	
			int len = bis.read(bytes);
			while(len!=-1){
				bos.write(bytes,0,len);
				len = bis.read(bytes);				
			}
			System.out.println("--"+new String(bytes));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			// 将缓冲区中的数据全部写出
			try {
				
				// 关闭流
				bis.close();
				bos.close();
			} catch (IOException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		
		}
	}
	/**
	 * 写文件到本地
	 * 
	 * @param is
	 *            文件流
	 * @param path
	 *            文件路径
	 * @param filename
	 *            文件名
	 * @return
	 */
	public RetMessage writeFile(InputStream is, String path, String filename) {
		path = pathAddress(path);
		return writeFile(is, path + filename);
	}

	/**
	 * 写文件到本地
	 * 
	 * @param file
	 *            文件
	 * @param path
	 *            写到的路径
	 * @return
	 */
	public boolean writeFile(File file, String path) {
		if (!file.isFile()) {
			return false;
		}
		try {
			FileInputStream is = new FileInputStream(file);
			path = pathAddress(path);
			this.writeFile(is, path + file.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * 设置FTP客服端的配置--一般可以不设置
	 * 
	 * @return
	 */
	private static FTPClientConfig getFTPClientConfig() {
		FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
		conf.setServerLanguageCode("zh");

		return conf;
	}

	/**
	 * 控制文件的大小,默认为5M
	 * 
	 * @param file_in
	 *            文件
	 */
	private boolean fileSize(File file_in) {
		return this.fileSize(file_in, 5);
	}

	/**
	 * 控制文件的大小
	 * 
	 * @param file_in
	 *            文件
	 * @param size
	 *            文件大小,单位为M
	 */
	private boolean fileSize(File file_in, int size) {
		if (file_in == null || file_in.length() == 0) {
			// 文件为空
			return false;
		} else {
			if (file_in.length() > (1024 * 1024 * size)) {
				// 文件大小不能大与size
				return false;
			}
		}
		return true;
	}

	/**
	 * 格式化文件路径 检查path最后有没有分隔符'\'
	 * 
	 * @param path
	 * @return
	 */
	public String pathAddress(String path) {

		if (path == null || path.length() < 1) {
			return "";
		}
		String temp_path = path.substring(path.length() - 1);
		if (!temp_path.equals("/") && !temp_path.equals("\\")) {
			temp_path = path + File.separator;
		} else {
			temp_path = path;
		}
		return temp_path;

	}

	/**
	 * 转码[ISO-8859-1 -> GBK] 不同的平台需要不同的转码
	 * 
	 * @param obj
	 * @return
	 */
	private static String iso8859togbk(Object obj) {
		try {
			if (obj == null)
				return "";
			else
				
				return new String(obj.toString().getBytes("iso8859-1"), "GB2312");
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 转码[GBK -> ISO-8859-1] 不同的平台需要不同的转码
	 * 
	 * @param obj
	 * @return
	 */
	private static String gbktoiso8859(Object obj) {
		try {
			if (obj == null)
				return "";
			else
				return new String(obj.toString().getBytes("GB2312"), "iso8859-1");
		} catch (Exception e) {
			return "";
		}
	}

	public static String getIp() {
		return ip;
	}

	public static void setIp(String ip) {
		ApacheCommonNetToFtpBase.ip = ip;
	}

	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		ApacheCommonNetToFtpBase.user = user;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		ApacheCommonNetToFtpBase.password = password;
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		ApacheCommonNetToFtpBase.port = port;
	}
	
//	获得服务器时间方法
//	public static UFDateTime getServerTime() {
//		try {
//			String strCSTimeDiff = "CLIENT_SERVER_TIME_DIFF";
//			String strSynchronizeTime = "CLIENT_SERVER_TIME_SYNCHRONIZED_POINT";
//			long lSynChronizeInterval = 3600000; // 1小时和服务器同步一次时钟；
//			java.util.Properties propSystem = System.getProperties();
//			if (propSystem.containsKey(strCSTimeDiff)
//					&& propSystem.containsKey(strSynchronizeTime)) {
//				long lLastSynTime = ((Long) propSystem.get(strSynchronizeTime))
//						.longValue();
//				if ((System.currentTimeMillis() - lLastSynTime) < lSynChronizeInterval) {
//					// OK, use clinet time + TimeDiff as Server time
//					long lCSDiff = ((Long) propSystem.get(strCSTimeDiff))
//							.longValue();
//					return new nc.vo.pub.lang.UFDateTime(lCSDiff
//							+ System.currentTimeMillis());
//				}
//			}
//			nc.vo.pub.lang.UFDateTime dtServer = SFServiceFacility
//					.getServiceProviderService().getServerTime();// (nc.vo.pub.lang.UFDateTime)
//			// result;
//			long lCurrtime = System.currentTimeMillis();
//			long lDiff = dtServer.getMillis() - lCurrtime;
//			propSystem.put(strSynchronizeTime, new Long(lCurrtime));
//			propSystem.put(strCSTimeDiff, new Long(lDiff));
//			return dtServer;
//		} catch (Exception e) {
////			Logger.error("Error", e);
//			e.printStackTrace();
//			return new UFDateTime(new Date());
//		}
//	}
}
