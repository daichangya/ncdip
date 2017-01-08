package nc.impl.dip.pub;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;


import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.itf.dip.pub.IQueryField;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.RetMessage;
import nc.vo.dip.esbfilesend.DipEsbSendVO;
import nc.vo.dip.message.MsrVO;
import nc.vo.pub.lang.UFDate;

public class DealFile {
	String pk;
	DipEsbSendVO sendvo;
	IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	public DealFile(String pk){
		super();
		this.pk=pk;
	}
	public DealFile(){
		super();
	}
	BaseDAO bd;
	private BaseDAO getBaseDAO(){
		if(bd==null){
			bd=new BaseDAO(IContrastUtil.DESIGN_CON);
		}
		return bd;
	}
	public RetMessage execDealFile(){
		try {
			sendvo=(DipEsbSendVO) getBaseDAO().retrieveByPK(DipEsbSendVO.class, pk);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		if(sendvo==null){
			return new RetMessage(false,"没有找到对应的文件处理定义");
		}
		if(sendvo.getDeeltype().equals("m1")){//备份
			return BackFile();
		}else if(sendvo.getDeeltype().equals("m2")){//清理
			return CleanFile();
		}else if(sendvo.getDeeltype().equals("m3")){//发送
			return SendEsbFile();
		}else if(sendvo.getDeeltype().equals("m4")){//上传
			String pk_ftpsourceregister=sendvo.getServer();
			String pk_filepath=sendvo.getFilepath();
			String path=getFilepath(pk_filepath);
			String pk_backFilepath=sendvo.getBakpath();
			String ftpFile=getFilepath(pk_backFilepath);
			if(path.equals("")){
				return  new RetMessage(false,"文件路径为空");
			}
			String fileName =sendvo.getSendfilename();
			if(fileName!=null&&fileName.trim().equals("")){
				fileName=null;
			}
			RetMessage rr=new RetMessage();
			try {
				rr=iqf.upFileFromSevriceToFtp(path, fileName, pk_ftpsourceregister,ftpFile,pk);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return rr;
		}else if(sendvo.getDeeltype().equals("m5")){//下载
			String pk_ftpsourceregister=sendvo.getServer();
			String pk_filepath=sendvo.getFilepath();
			String pk_backFilepath=sendvo.getBakpath();
			String path=getFilepath(pk_filepath);
			String serviceFile=getFilepath(pk_backFilepath);
			if(path.trim().equals("")){
				return  new RetMessage(false,"文件路径为空");
			}
			if(serviceFile.trim().equals("")){
				return  new RetMessage(false,"备份文件路径为空");
			}
			String fileName =sendvo.getSendfilename();
			if(fileName!=null&&fileName.trim().equals("")){
				fileName=null;
			}
			RetMessage rr=new RetMessage();
			try {
				rr=iqf.downFileFromFtpToService(path, fileName, pk_ftpsourceregister, serviceFile,pk);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			return rr;
		}else if(sendvo.getDeeltype().equals("m6")){
			RetMessage ret=  new RetMessage();
			if(!File.separator.equals("/")){
				ret.setIssucc(false);
				ret.setMessage("必须是lunux或者unix系统才可以使用zip备份");
				return ret;
			}
			String filePath=getFilepath(sendvo.getFilepath());
			String fileName=sendvo.getSendfilename();
			String zipFileBack=getFilepath(sendvo.getBakpath());
			if(filePath==null||filePath.length()<=0||zipFileBack==null||zipFileBack.length()<=0){
				return new RetMessage(false,"文件路径和备份路径不能为空");
			}
			if(!(filePath.startsWith("/")&&filePath.length()>1&&zipFileBack.startsWith("/")&&zipFileBack.length()>1)){
				return new RetMessage(false,"文件路径和备份路径必须是绝对路径");
			}
			File recFile=new File(zipFileBack);
			if(recFile.exists()){
				if(!recFile.isDirectory()){
					return new RetMessage(false,"所选的目录不是路径");
				}
			}else{
				recFile.mkdir();
			}
			String zipName="";
			UFDate date=new UFDate();
			if(fileName==null||fileName.trim().equals("")){
				zipName=zipFileBack.substring(zipFileBack.lastIndexOf("/"),zipFileBack.length()-1)+date.toString()+".zip";
			}else{
				zipName=fileName+date.toString()+".zip";
			}
			zipName=zipName.trim();
			String cmd="zip -qr "+zipName+" "+filePath;
			try {
//				String []cmdArray={"cd "+zipFileBack,"zip -qr "+zipName+" "+filePath};
				Runtime.getRuntime().exec(cmd);
				Logger.debug("nnnnnnnnnnnnnnnnnnnnn"+cmd);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ret.setMessage(e.getMessage());
				ret.setIssucc(false);
			}
			ret.setIssucc(true);
			ret.setMessage("zip备份处理成功"+cmd);
			return ret;
		}else{
			return new RetMessage(false,"没有找到对应的文件处理定义");
		}
	}
	public String getFilepath(String pkFilepath){
		String fp="";
		if(pkFilepath==null||pkFilepath.length()<=0){
			fp="";
		}else{
			String sql5 = "select descriptions from dip_filepath where pk_filepath='" + pkFilepath + "' and nvl(dr,0)=0";
			try {
				fp = iqf.queryfield(sql5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fp;
	}
	public RetMessage BackFile(){
		String bakfpath=getFilepath(sendvo.getFilepath());
		String baktpath=getFilepath(sendvo.getBakpath());
		if(bakfpath==null||bakfpath.length()<=0||baktpath==null||baktpath.length()<=0){
			return new RetMessage(false,"文件路径和备份路径不能为空");
		}
		File recFile=new File(bakfpath);
		int su=0;
		if(recFile.exists()){
			if(!recFile.isDirectory()){
				return new RetMessage(false,"所选的目录不是路径");
			}
				
		File []  listFiles= recFile.listFiles(); //所有单位的文件夹
		if(listFiles==null||listFiles.length<=0){
			return new RetMessage(false,"文件路径为空！");
		}
		boolean isbakfolder=false;
		String filename=sendvo.getSendfilename();
		if(filename==null||filename.equals("")){
			isbakfolder=true;
		}
		File s=new File(baktpath);
		if(!s.exists()){
			s.mkdirs();
		}
		for(int i=0;i<listFiles.length;i++){
			if(!listFiles[i].isDirectory()){
				String newpathback = baktpath+"/"+listFiles[i].getName();
				if(isbakfolder){
					File newfile=new File(newpathback);
					if(newfile.exists()){
						newfile.delete();
					}
					listFiles[i].renameTo(new File(newpathback));
					su++;
				}else{
					if(listFiles[i].getName().equals(filename)){
						File newfile=new File(newpathback);
						if(newfile.exists()){
							newfile.delete();
						}
						listFiles[i].renameTo(new File(newpathback));
						su++;
					}
				}
			}
		}
		}else{
			return new RetMessage(false,"路径不存在");
		}
		RetMessage rt=new RetMessage(true,"成功");
		rt.setSu(su);
		return  rt;
	}
	public RetMessage CleanFile(){
		String bakfpath=getFilepath(sendvo.getFilepath());
		if(bakfpath==null||bakfpath.length()<=0){
			return new RetMessage(false,"文件路径不能为空");
		}
		File recFile=new File(bakfpath);
		int su=0;
		if(recFile.exists()){
			if(!recFile.isDirectory()){
				return new  RetMessage(false,"所选路径不是文件夹！");
			}
			File []  listFiles= recFile.listFiles(); //所有单位的文件夹
			if(listFiles!=null&&listFiles.length>0){
				boolean isbakfolder=true;
				String filename=sendvo.getSendfilename();
				if(filename==null||filename.equals("")){
					isbakfolder=false;
				}
				for(int i=0;i<listFiles.length;i++){
					if(!listFiles[i].isDirectory()){
						if(isbakfolder){
							if(listFiles[i].getName().equals(filename)){
							listFiles[i].delete();
							su++;
							}
						}else{
								listFiles[i].delete();
								su++;
						}
					}
				}
			}
		}
		RetMessage rt=new RetMessage(true,"成功");
		rt.setSu(su);
		return  rt;
	}
	public RetMessage SendEsbFile(){
		//判断文件路径是否定义
		String bakfpath=getFilepath(sendvo.getFilepath());
		if(bakfpath==null||bakfpath.length()<=0){
			return new RetMessage(false,"文件路径不能为空");
		}
		//取得所有的文件，判断文件夹是否为空
		File recFile=new File(bakfpath);
		File []  listFiles= recFile.listFiles(); //所有单位的文件夹
		if(listFiles==null||listFiles.length<=0){
			return new RetMessage(false,"文件夹为空");
		}
		//判断是发送文件还是发送文件夹，如果是文件夹，isbakfolder=true
		boolean isbakfolder=true;
		String filename=sendvo.getSendfilename();
		if(filename!=null&&filename.length()>0){
			isbakfolder=false;
		}
		//查询消息服务器，如果没有找到，返回
		MsrVO vo = null;
		try {
			vo = (MsrVO) getBaseDAO().retrieveByPK(MsrVO.class, sendvo.getServer());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		if(vo==null){
			return new RetMessage(false,"没有找到对应的消息服务器");
		}
		//如果不是ESB消息服务器，那么返回
		if(!vo.getMessageplug().equals("0001AA1000000003CL1V")){//如果是esb消息服务器
			 return new RetMessage(false,"没有对应的消息服务器类型");
		}
		List<String> path=new ArrayList<String>();
		for(int i=0;i<listFiles.length;i++){
			if(!listFiles[i].isDirectory()){
				if(isbakfolder){
					if(!listFiles[i].getName().endsWith(".properties")&&findPropFile(listFiles,listFiles[i].getName())){
						path.add(bakfpath+"/"+listFiles[i].getName());
					}
				}else{
					if(listFiles[i].getName().equals(filename)&&findPropFile(listFiles,listFiles[i].getName())){
						path.add(bakfpath+"/"+listFiles[i].getName());
					}
				}
			}
		}
		RetMessage rt=new RetMessage(true,"成功");
		if(path!=null&&path.size()>0){
			rt=sendToESBFile(vo,path);
		}else {
			return new RetMessage(false,"没有找到符合规则的文件");
		}
		return  rt;
	}

	ILogAndTabMonitorSys ilm=(ILogAndTabMonitorSys) NCLocator.getInstance().lookup(ILogAndTabMonitorSys.class.getName());
	private RetMessage sendToESBFile(MsrVO vo, List<String> path) {

		String url=vo.getAddress()+":"+vo.getDuankou();
		String strJmsUser=vo.getUname();
		String strJmsPwd=vo.getUpass();
		String strJmsQuName=vo.getVdef1();
		QueueConnectionFactory factory=null;
		QueueConnection connection=null;
		QueueSession session =null;
		javax.jms.Queue queue=null;
		QueueSender sender=null;
		try{
			factory = new com.tibco.tibjms.TibjmsQueueConnectionFactory( url);

			connection = factory.createQueueConnection( strJmsUser, strJmsPwd);

			session= connection.createQueueSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);

			 queue= session.createQueue(strJmsQuName);
			 sender= session.createSender(queue);

			System.out.println(new StringBuffer(
					"JMS发送信息开始开始:------------------------"));
			connection.start();
			BytesMessage msg=null; 
			int number=0;
			for(int i=0;i<path.size();i++){
				File from = new File(path.get(i)); 
				byte ba[] = new byte[(int) from.length()];
				FileInputStream fis =null;
				try {
					fis= new FileInputStream(from);
					int nBebinReadLoc = 0;
					do {
						int nReadedInSize = fis.read(ba, nBebinReadLoc, ba.length
								- nBebinReadLoc);
						nBebinReadLoc += nReadedInSize;
					} while (nBebinReadLoc < ba.length);
					
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					fis.close();
				}
	            msg=session.createBytesMessage(); 
	            PropUtilVO pvo=getPropUtilVO(path.get(i));
	            msg.setStringProperty("type", "file");
	            msg.setStringProperty("xtbz", pvo.getXtbz());
	            msg.setStringProperty("zdbz", pvo.getZdbz());
	            msg.setStringProperty("ywbz", pvo.getYwbz());
	            msg.setStringProperty("tablename", pvo.getTablename());
	            msg.setStringProperty("datasumnum", pvo.getDatasumnum());
	            msg.setStringProperty("datanum", pvo.getDatanum());
	            msg.setStringProperty("wjlname", pvo.getWjlname());
	            msg.writeBytes(ba);
	            sender.send(msg);

	            number++;
			}
			RetMessage rt=new RetMessage(true,"发送成功,共发送"+number+"个文件");
			rt.setSu(number);
			return rt; 
		}catch (JMSException jmse) {
			Logger.error("JMS 接收方法错误－－－－－－－－－－－－");
			jmse.printStackTrace();
			return new RetMessage(false,"ESB消息接收连接失败"+"-"+jmse.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			return new RetMessage(false,"发送失败");
		}finally{
			if(sender!=null){
				try {
					sender.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
			if(session!=null){
				try {
					session.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
			if(connection!=null){
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}

	}
	public PropUtilVO getPropUtilVO(String string) {
		FileReader fr = null;
		BufferedReader br = null ;
		if(string.indexOf(".")>0){
			string=string.substring(0,string.indexOf("."));
		}
		string=string+".properties";
		PropUtilVO pv=new PropUtilVO();
		try {
			fr = new FileReader(string);
			String line = null;
			br = new BufferedReader(fr);
			while ((line = br.readLine()) != null){
				if(line!=null&&line.trim().length()>0){
					if(line.indexOf("=")>0){
						pv.setAttribuate(line.split("=")[0], line.split("=")[1]);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fr!=null){
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
			
		return pv;
	}
	public boolean findPropFile(File [] listFiles,String filename){
		int index=filename.indexOf(".");
		if(index>=0){
			filename=filename.substring(0,index);
		}
		filename=filename+".properties";
		for(int i=0;i<listFiles.length;i++){
			if(listFiles[i].getName().equals(filename)){
				return true;
			}
		}
		return false;
	}
}
