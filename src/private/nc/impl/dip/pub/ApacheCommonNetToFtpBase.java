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
 * �ļ�FTP�ϴ�����
 * 
 * @author : ����
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
	 * ��ʼ��
	 * 
	 * @param ip
	 *            FTP��������ַ
	 * @param user
	 *            ������
	 * @param password
	 *            ����
	 * @param port
	 *            �˿ں�
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
	 * �ϴ��ļ�
	 * 
	 * @param file
	 *            �ļ�
	 * @return
	 */
	public RetMessage uploadFile(File file,boolean flag) throws Exception {
		if (!file.isFile()) {
			// �����ļ�
			return new RetMessage(false,file.getName()+"�����ļ�");
		} else if (flag&&!fileSize(file)) {
			// �����ļ���С
			return new RetMessage(false,"�����ļ���С����");
		}

		//
		try{
			if (!connectServer()) {
				return new RetMessage(false,"����ftp������ʧ��");
			}
		}catch(Exception e){
			throw e;
		}

		// ���ļ�����ʱ���,��ֹ�������ļ��ظ�
		String fileName = file.getName();
		int lastIndex = fileName.lastIndexOf(".");
//		//��fileName������������ʱ�������֤���ֲ��ظ�
//		fileName = fileName.substring(0, lastIndex) + "_"
//				+ System.currentTimeMillis() + fileName.substring(lastIndex);

		try {
			FileInputStream fis = new FileInputStream(file);
			if (!ftpClient.storeFile(gbktoiso8859(fileName), fis)) {
				closeConnect();
				return new RetMessage(false,"�ϴ��ļ�ʧ��");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return new RetMessage(false,"�ϴ��ļ�ʧ��");
		} finally {
			closeConnect();
		}

		 return new RetMessage(true,"�ϴ��ļ��ɹ�");
	}

	/**
	 * �ϴ��ļ�
	 * 
	 * @param file
	 *            �ļ�
	 * @param path
	 *            �ϴ�������·��
	 * @return
	 */
	public RetMessage uploadFile(File file, String path,boolean flag) throws Exception{
		RetMessage ret = new RetMessage();
		// ת��path·��
//		path = this.pathAddress(path);
//		// ����·��
//		mkdir(path);
		// ���������Ŀ¼
//		ret=this.changeWorkingDirectory(path);
		if (!connectServer()) {
			throw new Exception("ftp����ʧ��");
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
								ret.setMessage("ftp�ϴ���"+str[i]+"Ŀ¼ʧ��");
								return ret;
							}
							boolean dd1=ftpClient.changeWorkingDirectory(str[i]);
							if(!dd1){
								ret.setIssucc(false);
								ret.setMessage("ftp�ϴ���"+str[i]+"Ŀ¼�󣬽���"+str[i]+"ʧ��");
								return ret;
							}
						}
					}
				}
//				ret.setIssucc(false);
//				ret.setMessage("����"+path+"·������");
//				return ret;
			}
		}
		
//		if (!this.changeWorkingDirectory(path)) {
//			return new RetMessage(false,"����"+path+"·������");
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
	 * �����ļ�
	 * 
	 * @param filename
	 *            �ļ���
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
		} // �˳�FTP������
		// ���������˳�,��Ȼ������ļ����ص�һ��,û���������������
		// this.closeConnect();
	}

	/**
	 * �����ļ�
	 * 
	 * @param filename
	 *            �ļ���
	 * @param path
	 *            ·��
	 * @return
	 */
	public RetMessage downloadFile(String filename, String path) throws Exception {
		RetMessage ret=new RetMessage();
		// ת��path·��
		InputStream bReturn = null;
//		path = this.pathAddress(path);
		if (!connectServer()) {
			throw new Exception("ftp����ʧ��");
		}
//		if (!this.changeWorkingDirectory(path)) {
//			return null;
//		}
		int w=ftpClient.cwd(path);
		if(w!=250){
			return new RetMessage(false,"����"+filename+"�ļ�����");
		}
//			return null;
//		}else{
//			this.changeWorkingDirectory(path);
//		}
		try{
			Logger.debug("��ʼ�����ļ���"+filename);
//			System.out.println("��ʼ�����ļ���"+filename);
			bReturn = downloadFile(filename);
			if(bReturn!=null){
				ret.setIssucc(true);
				ret.setValue(bReturn);
				Logger.debug("�����ļ�"+filename+"��ϡ�"+"read0:" + bReturn.available());	
			}else{
				ret.setIssucc(false);
				ret.setMessage("�����ļ�"+filename+"����");
				Logger.debug("�����ļ�"+filename+"����");
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
	 * ɾ���ļ�
	 * 
	 * @param path
	 *            ·��
	 * @param filename
	 *            �ļ���
	 * @return
	 */
	public boolean deleteFile(String filename, String path) throws Exception {
		RetMessage ret=new RetMessage();
		// ת��path·��
		path = this.pathAddress(path);
		ret=this.changeWorkingDirectory(path);
		if (!ret.getIssucc()) {
			return false;
		} // ɾ���ļ�
		try{			
			return deleteFile(filename);
			
		}catch(Exception e){
			throw e;
		}
	}

	/**
	 * ɾ���ļ�
	 * 
	 * @param filename
	 *            �ļ���
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
	 * ɾ��ftp�ϵ��ļ�
	 * 
	 * @param filename
	 *            �ļ���
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
						Logger.debug("��"+sdf_time.format(new Date())+"�� "+"  ɾ��ftp�ϵ��ļ��ɹ�:"+uploadDir+files[i]+"  ��"+(i+1)+"/"+files.length+"���ļ�", this.getClass(), "uploadFtp");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Logger.debug("��"+sdf_time.format(new Date())+"�� "+"  ɾ��ftp�ϵ��ļ�ʧ��:"+uploadDir+files[i]+"  ��"+(i+1)+"/"+files.length+"���ļ�"+e.getMessage(), this.getClass(), "uploadFtp");
						
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
	 * ���ָ��Ŀ¼�µ��ļ���Ŀ¼���Ƽ���
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
			// �˳�FTP������
			//
			this.closeConnect();
		}
		String [] fileNames = new String[vec.size()];
		fileNames = (String[])vec.toArray(fileNames);
		return fileNames;

	}

	/**
	 * ����·��
	 * 
	 * @param path
	 *            ·������
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
	 * �������ļ�
	 * 
	 * @param oldFileName
	 *            --ԭ�ļ���
	 * @param newFileName
	 *            --���ļ���
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
	 * ���뵽��������ĳ��Ŀ¼��
	 * 
	 * @param directory
	 */
	public RetMessage changeWorkingDirectory(String directory) throws Exception {
		RetMessage bReturn=new RetMessage();
		try {
			
			if (!connectServer()) {
				return new RetMessage(false,"ftp����ʧ��");
			}
			

//			bReturn = ftpClient.changeWorkingDirectory("/");
			int i=ftpClient.cwd(gbktoiso8859(directory));
			if(i!=250){
				return new RetMessage(false,"����"+directory+"Ŀ¼ʧ��");
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
				ret.setMessage("ftp����ʧ��");
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
					ret.setMessage(file+"·����û���ļ�");
				}
			}else{
				ret.setIssucc(false);
				ret.setMessage("����"+file+"·������");
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
	 * ���ص���һ��Ŀ¼
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
	 * ��¼ftp������
	 * 
	 */
	public boolean connectServer() throws Exception{
		boolean flag=true;
		if (ftpClient.isConnected() == false) {
			try {
				
				// ����ʹ��iso-8859-1��ΪͨѶ���뼯
//				ftpClient.setControlEncoding("iso-8859-1");
				// ftpClient.configure(getFTPClientConfig());
//				Logger.debug("===================\n  ysjh FTP connect begin:ip:"+ip+"\tport:"+port,ApacheCommonNetToFtpBase.class,"connectServer\n");
			
				ftpClient.connect(ip, port);
		
//				Logger.debug("===================\nysjh FTP connect end:ip:"+ip+"\tport:"+port,ApacheCommonNetToFtpBase.class,"connectServer\n");
//				System.out.println("��ʼ��¼ftp");
				Logger.debug("��ʼ��¼ftp");
				if (!ftpClient.login(user, password)) {
					// ��¼ʧ��
					flag=false;
				throw new Exception("ftp����½�û���"+user+"�����������");					
					
				}
				Logger.debug("��¼ftp����");
//				System.out.println("��¼ftp����");
				// ״̬
				int reply = ftpClient.getReplyCode();
				if (!FTPReply.isPositiveCompletion(reply)) {
					closeConnect();
					flag=false;
					return flag;
				}

				// �ñ���ģʽ����
				ftpClient.enterLocalPassiveMode();
				// ���ļ�������������Ϊ������
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				// ��ֹserver��ʱ�Ͽ�
				//ftpClient.setDefaultTimeout(60000);
				// 10���ӳ�ʱ
				ftpClient.setSoTimeout(10000);
			} catch (SocketException se) {
//				Logger.debug("ysjh FTP connect error:ip:"+ip+"\tport:"+port,ApacheCommonNetToFtpBase.class,"connectServer");
				se.printStackTrace();	
				flag=false;
				throw new Exception("FTP CONNECT ERROR IP:"+ip+"\tPORT:"+port+" �����IP���˿ں������Ƿ���ȷ��ָ����ftp�������Ƿ��������У�");				
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
	 * �ر�����
	 */
	public void closeConnect() {
		try {
			System.out.println("��ʼ�ر�ftp����ftpClient.isConnected()"+ftpClient.isConnected());
			if (ftpClient != null) {
//				System.out.println("��ʼ�Ƴ�ftp����");
				Logger.debug("��ʼ�Ƴ�ftp����");
//				Boolean b;
//				b = ftpClient.logout();
//				System.out.println("logout���,b:"+b);
				ftpClient.disconnect();
//				System.out.println("�Ƴ�ftp���ӳɹ�");
				Logger.debug("�Ƴ�ftp���ӳɹ�");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * д�ļ�������
	 * 
	 * @param is
	 *            �ļ���
	 * @param allPath
	 *            ȫ·��=�ļ�·��+�ļ���
	 * @return
	 */
	public RetMessage writeFile(InputStream is, String allPath) {
		if (is == null || allPath == null) {
			return new RetMessage(false,"�ļ����ļ�·������Ϊ��");
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
			// Ĭ��Ҳ��2048
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
//			System.out.println("�ļ�("+allPath+")���ر���д�����");
			Logger.debug("�ļ�("+allPath+")���ر���д�����");
			return new RetMessage(true,"");
		} catch (Exception e) {
			e.printStackTrace();
			return new RetMessage(false,e.getMessage());
		}finally{
			// ���������е�����ȫ��д��
			try {
				
				// �ر���
				bis.close();
				bos.close();
//				System.out.println("�ļ�("+allPath+")�ѹر�");
				Logger.debug("�ļ�("+allPath+")�ѹر�");
			} catch (IOException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}
		
		}
	}
	/**
	 * ftpд�ļ�������
	 * 
	 * @param is
	 *            �ļ���
	 * @param allPath
	 *            ȫ·��=�ļ�·��+�ļ���
	 * @return
	 */
	public boolean writeFileFTP(InputStream is, String allPath) {
		
		if (is == null || allPath == null) {
			return false;
		}
		BufferedInputStream bis = null; 			
		BufferedOutputStream bos = null;
		try {
			// Ĭ��Ҳ��2048
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
			// ���������е�����ȫ��д��
			try {
				
				// �ر���
				bis.close();
				bos.close();
			} catch (IOException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}
		
		}
	}
	/**
	 * д�ļ�������
	 * 
	 * @param is
	 *            �ļ���
	 * @param path
	 *            �ļ�·��
	 * @param filename
	 *            �ļ���
	 * @return
	 */
	public RetMessage writeFile(InputStream is, String path, String filename) {
		path = pathAddress(path);
		return writeFile(is, path + filename);
	}

	/**
	 * д�ļ�������
	 * 
	 * @param file
	 *            �ļ�
	 * @param path
	 *            д����·��
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
	 * ����FTP�ͷ��˵�����--һ����Բ�����
	 * 
	 * @return
	 */
	private static FTPClientConfig getFTPClientConfig() {
		FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
		conf.setServerLanguageCode("zh");

		return conf;
	}

	/**
	 * �����ļ��Ĵ�С,Ĭ��Ϊ5M
	 * 
	 * @param file_in
	 *            �ļ�
	 */
	private boolean fileSize(File file_in) {
		return this.fileSize(file_in, 5);
	}

	/**
	 * �����ļ��Ĵ�С
	 * 
	 * @param file_in
	 *            �ļ�
	 * @param size
	 *            �ļ���С,��λΪM
	 */
	private boolean fileSize(File file_in, int size) {
		if (file_in == null || file_in.length() == 0) {
			// �ļ�Ϊ��
			return false;
		} else {
			if (file_in.length() > (1024 * 1024 * size)) {
				// �ļ���С���ܴ���size
				return false;
			}
		}
		return true;
	}

	/**
	 * ��ʽ���ļ�·�� ���path�����û�зָ���'\'
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
	 * ת��[ISO-8859-1 -> GBK] ��ͬ��ƽ̨��Ҫ��ͬ��ת��
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
	 * ת��[GBK -> ISO-8859-1] ��ͬ��ƽ̨��Ҫ��ͬ��ת��
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
	
//	��÷�����ʱ�䷽��
//	public static UFDateTime getServerTime() {
//		try {
//			String strCSTimeDiff = "CLIENT_SERVER_TIME_DIFF";
//			String strSynchronizeTime = "CLIENT_SERVER_TIME_SYNCHRONIZED_POINT";
//			long lSynChronizeInterval = 3600000; // 1Сʱ�ͷ�����ͬ��һ��ʱ�ӣ�
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
