package nc.impl.dip.pub;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.TextMessage;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.impl.dip.optByPluginSrv.OptByPlgImpl;
import nc.itf.dip.pub.IDipLogger;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.pub.dip.startup.ThreadUtil;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.RetMessage;
import nc.vo.dip.datasynch.DipDatasynchVO;
import nc.vo.dip.dateprocess.DateprocessVO;
import nc.vo.dip.filepath.FilepathVO;
import nc.vo.dip.managerserver.DipManagerserverVO;
import nc.vo.dip.message.MsrVO;
import nc.vo.dip.messservtype.MessservtypeVO;
import nc.vo.dip.runsys.DipRunsysBVO;
import nc.vo.pub.lang.UFDateTime;

/**
 * @desc ��Ϣ���н���
 * */
public class DataReceiverJMSImpl  {
	

//	private static Logger logger = Logger.getLogger("DEFAULT");
//	private static Logger logger2 = Logger.getLogger("REC1");
	private static BaseDAO bdao;
	IDipLogger diplog=(IDipLogger) NCLocator.getInstance().lookup(IDipLogger.class.getName());
	DipManagerserverVO task;
	public DataReceiverJMSImpl(){
		super();
	}
	public DataReceiverJMSImpl(DipManagerserverVO task){
		super();
		this.task=task;
		MessservtypeVO mtvo=task.getMesvo();
		if(mtvo==null){
			Logger.debug("û���ҵ���Ӧ����Ϣ��ʽ�����Ӷ���");
			return;
		}
		//�õ���Ϣ��������ע����Ϣ
		String ippk=mtvo.getVdef1();
		if(ippk==null||ippk.length()<=0){
			Logger.debug("û���ҵ���Ϣ������������");
			return;
		}
		try{
			MsrVO mvo=(MsrVO) getBaseDao().retrieveByPK(MsrVO.class, ippk);
			if(mvo==null){
				Logger.debug("û�в�ѯ����Ϣ����������"+ippk);
				return;
			}
//			if(ThreadUtil.getInstanceMap()!=null){
				gsmap=(Map) ThreadUtil.getInstanceMap();
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private BaseDAO getBaseDao(){
		if(bdao==null){
			bdao=new BaseDAO(IContrastUtil.DESIGN_CON);
		}
		return bdao;
	}
	ILogAndTabMonitorSys ilm= (ILogAndTabMonitorSys) NCLocator.getInstance().lookup(ILogAndTabMonitorSys.class.getName());
	private Map gsmap=new HashMap();//String, EsbMapUtilVO //key�Ǹ�ʽ���� ��ʼ�����ݡ�����ͷ��ʽ��
	public int dataRecive(String threadname,long l,long sleeptime) throws Exception {
		int ret = 0;
		MessservtypeVO mtvo=task.getMesvo();
		if(mtvo==null){
			Logger.debug(new StringBuffer("JMS������Ϣ��ȡ��ʼ:").append("û���ҵ���Ӧ����Ϣע�������"));
			throw new Exception("û���ҵ���Ӧ����Ϣע�������");
		}
		//�õ���Ϣ��������ע����Ϣ
		String ippk=mtvo.getVdef1();
		if(ippk==null||ippk.length()<=0){
			Logger.debug(new StringBuffer("JMS������Ϣ��ȡ��ʼ:").append("û���ҵ���Ӧ����Ϣע�������������"));
			throw new Exception("û���ҵ���Ӧ����Ϣע�������������");
		}
		MsrVO mvo=(MsrVO) getBaseDao().retrieveByPK(MsrVO.class, ippk);
		FilepathVO fvo=(FilepathVO) getBaseDao().retrieveByPK(FilepathVO.class, mvo.getSavefilepath());
		String path="";
		if(fvo!=null){
			path=fvo.getDescriptions();	
		}
		
		
		if(mvo==null){
			Logger.debug(new StringBuffer("JMS������Ϣ��ȡ��ʼ:").append("û���ҵ�����"+ippk+"��Ӧ����Ϣע�������"));
			throw new Exception("û���ҵ�����"+ippk+"��Ӧ����Ϣע�������");
		}
		String strJmsURL =mvo.getAddress()+":"+mvo.getDuankou();
		//TODO wyd ��Ϣ���ж�Ҫ�������������������������ȡ
//		String strJmsQuName = mtvo.getDlm();
		String strJmsQuName = mvo.getVdef2();
		String strJmsUser = mvo.getUname();
		String strJmsPwd = mvo.getUpass();

		Logger.debug(new StringBuffer("JMS������Ϣ��ȡ��ʼ:")
				.append("-------------------"));
		Logger.debug(new StringBuffer("strJmsURL:").append(strJmsURL));
		Logger.debug(new StringBuffer("strJmsQuName:").append(strJmsQuName));
		Logger.debug(new StringBuffer("strJmsUser:").append(strJmsUser));
		Logger.debug(new StringBuffer("strJmsPwd:").append(strJmsPwd));
		Logger.debug(new StringBuffer("JMS������Ϣ��ȡ����:")
				.append("-------------------"));
		UFDateTime lnowTime = new UFDateTime(new Date());


		String strMessage ="";
		String type=mtvo.getType();
//		String pre=mtvo.getPreference();
//		String date;
		UFDateTime etime=null;
		if(type.equals("ʱ���")){
//			date=(new UFDate(new Date()).toString())+" "+pre.split("-")[1]+":00";
			etime=new UFDateTime(mtvo.getVdef5());
		}
		/*String[] str=new String[5];
		str[0]="##begin##bj##000000##xxx##41010000##����1##1234.00##end##";
		str[1]="##begin##bj##000000##xxx##41010001##����2##1233.00##end##";
		str[2]="##begin##bj##000000##xxx##41010002##����3##1235.00##end##";
		str[3]="##begin##bj##000000##xxx##41010003##����4##1236.00##end##";
		str[4]="##begin##bj##000000##xxx##41010004##����5##1234.00##end##";
		for(int i=0;i<5;i++){
			OptByPlgImpl op=new OptByPlgImpl();
			str[i]=str[i].toUpperCase();
			Logger.debug(new StringBuffer("BytesMsg message: ").append("��ʼ����:"+str[i]));
			op.styleToMsg(str[i],gsmap);
		}*/
		Logger.debug("׼�������ȡѭ�� (type.equals(\"ʱ���\" )?(lnowTime.before(etime)):true)��"+(type.equals("ʱ���" )?(lnowTime.before(etime)):true));
		Logger.debug("׼�������ȡѭ�� ThreadUtil.isStart(task)��"+ThreadUtil.isStart(task));
		while ((type.equals("ʱ���" )?(lnowTime.before(etime)):true)&&ThreadUtil.isStart(task)) {
			QueueConnection connection =null;
			QueueSession session =null;
			QueueReceiver receiver=null;
			try {
				lnowTime=new UFDateTime();
				QueueConnectionFactory factory = new com.tibco.tibjms.TibjmsQueueConnectionFactory(
						strJmsURL);
	
				connection = factory.createQueueConnection(
						strJmsUser, strJmsPwd);
	
				 session = connection.createQueueSession(false,
						javax.jms.Session.AUTO_ACKNOWLEDGE);
	
				javax.jms.Queue queue = session.createQueue(strJmsQuName);
	
				receiver = session.createReceiver(queue);
	
				Logger.debug(new StringBuffer(
						"JMS������Ϣ��ʼ��ʼ:------------------------"));
				connection.start();
				TextMessage message = null;
				BytesMessage bytesMessage = null;
				while ((type.equals("ʱ���" )?(lnowTime.before(etime)):true)&&ThreadUtil.isStart(task)) {

					lnowTime = new UFDateTime();
					Logger.debug(new StringBuffer("Received message: ��ʼ����������������"));
					javax.jms.Message m = receiver.receive(l);
	
					Logger.debug(new StringBuffer("Received message: ").append(m));
					if (m != null) {
						boolean isfile=false;
						if (m instanceof TextMessage) {
							message = (TextMessage) m;
							strMessage=message.getText();
							Logger.debug(new StringBuffer("TextMsg message: ")
									.append(message.getText()));
							diplog.writediplog("��Ϣ�� TextMsg message: "+message.getText(), IContrastUtil.LOG_ALL);
						} else if (m instanceof BytesMessage) {
							bytesMessage = (BytesMessage) m;
							Logger.debug("----��BytesMessage");
							 Enumeration propertyNames = bytesMessage.getPropertyNames( );
							 String fileanme="";
						    while(propertyNames.hasMoreElements( )){
						        String name = (String)propertyNames.nextElement( );
						        if(name.equals("wjlname")){
						        	fileanme=bytesMessage.getStringProperty(name);
						        }
						        Object value=bytesMessage.getObjectProperty(name);
						        Logger.debug("4---���ԡ�"+name+"��ֵ��" +value+"��");
						    }
						    int imsgLen = (int) bytesMessage.getBodyLength();
							byte[] byteArg = new byte[imsgLen];
							bytesMessage.readBytes(byteArg);
							strMessage = new String(byteArg);
							strMessage = strMessage.replace("\n", "");
							Logger.debug(new StringBuffer("BytesMsg message: ").append(strMessage));
							diplog.writediplog("�ļ��� BytesMsg message: "+strMessage, IContrastUtil.LOG_ALL);
							if(fileanme!=null&&fileanme.length()>0){
								String pathname=path+fileanme.substring(fileanme.lastIndexOf("/"));
								Logger.debug("----��BytesMessage  pathname��"+pathname);
								isfile=true;
								writeMethod(pathname, bytesMessage);
							}
//							else{
//								int imsgLen = (int) bytesMessage.getBodyLength();
//								byte[] byteArg = new byte[imsgLen];
//								bytesMessage.readBytes(byteArg);
//								strMessage = new String(byteArg);
//								strMessage = strMessage.replace("\n", "");
//								Logger.debug(new StringBuffer("BytesMsg message: ").append(strMessage));
//							}
						}
						if(!isfile){
							if(strMessage!=null&&strMessage.length()>0){
								OptByPlgImpl op=new OptByPlgImpl();
								String str=strMessage.toUpperCase();
								Logger.debug(new StringBuffer("BytesMsg message: ").append("��ʼ����:"+str));
								
								String[] strerror=new String[3];
								strerror[0]=mtvo.getName();
								strerror[1]=mtvo.getCode();
								op.styleToMsg(strMessage,str,gsmap,mvo, strerror);
							}else{
								Logger.debug(new StringBuffer("BytesMsg message: ").append("���յ�����ϢΪ��"));
								
							}
						}
						
					} else{
						break;
					}
				}
				
			} catch (JMSException jmse) {
				Logger.error("JMS ���շ������󣭣���������������������");
				jmse.printStackTrace();
				DateprocessVO vop=new DateprocessVO();
				vop.setActivetype("���ݽ���");
				vop.setActive("ESB��Ϣ����");
				vop.setSdate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				vop.setContent("ESB��Ϣ��������ʧ��-"+task.getSysname()+"-"+jmse.getMessage());
				vop.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
				ilm.writToDataLog_RequiresNew(vop);
				ret=1;
				try{
					Thread.sleep(sleeptime);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}catch(Exception e){
				Logger.error("JMS ���շ��ܴ��ˣ���������������������������");
				e.printStackTrace();
				try{
					Thread.sleep(sleeptime);
				}catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			finally{
				lnowTime = new UFDateTime();
				if(receiver!=null){
					receiver.close();
				}
				if(session!=null){
					session.close();
				}
				if(connection!=null){
					connection.close();
				}
			}
		}
		Logger.debug(new StringBuffer("JMS������Ϣ��ʼ����:------------------------"));
		return ret;
		}
	public void writeMethod(String pathname,BytesMessage byteMessage){
		//pathname="d://aa/bb/cc.txt";
		int k=0;
		try {
			k = (int) byteMessage.getBodyLength();
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte []buf=new byte[k];
		int w=0;
		File file=new File(pathname);
		if(file.exists()){
			file.delete();
		}
		FileOutputStream out=null;
		DataOutputStream dataout=null;
		try {
			out = new FileOutputStream(file);
			dataout=new DataOutputStream(out);
			while((w=byteMessage.readBytes(buf))!=-1){
				dataout.write(buf);
				String str=new String(buf);
				diplog.writediplog("�ļ���--"+str, IContrastUtil.LOG_ALL);
				diplog.writediplog("�ļ���--"+str, IContrastUtil.LOG_SUCESS);
			}
			String xtbz="";
			String zdbz="";
			String ywbz="";
			String tablename="";
			String datasumnum="";
			String datanum="";
			 Enumeration propertyNames = byteMessage.getPropertyNames( );
		    while(propertyNames.hasMoreElements( )){
		        String name = (String)propertyNames.nextElement( );
		        if(name.equals("xtbz")){
		        	xtbz=byteMessage.getStringProperty(name);
		        }else if(name.equals("zdbz")){
		        	zdbz=byteMessage.getStringProperty(name);
		        }else if(name.equals("ywbz")){
		        	ywbz=byteMessage.getStringProperty(name);
		        }else if(name.equals("tablename")){
		        	tablename=byteMessage.getStringProperty(name);
		        }else if(name.equals("datasumnum")){
		        	datasumnum=byteMessage.getStringProperty(name);
		        }else if(name.equals("datanum")){
		        	datanum=byteMessage.getStringProperty(name);
		        }
		    }
		    OptByPlgImpl il=new OptByPlgImpl();
		    il.writeToPropFile(pathname, null, xtbz, zdbz, ywbz, tablename, datasumnum==null||datasumnum.length()<=0?0:Integer.parseInt(datasumnum),
		    		datanum==null||datanum.length()<=0?0:Integer.parseInt(datanum));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.debug(new StringBuffer("д�ļ�:"+pathname+"����-error-----error-------error-----------"+e.getMessage()));
		}finally{
			if(dataout!=null){
				try {
					dataout.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					dataout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		}
		
		Logger.debug("��ʾ:"+pathname+"�ļ���������ϣ�");
	}
	/**
	 *  
	 * @param synchvo
	 * @param receiveFormatMap  //���н������ݸ�ʽ
	 * @param backFormatMap     //��ִ��ʽ
	 * @param time
	 */
	public RetMessage receiveMessageTB(DipDatasynchVO synchvo,Map<String,EsbMapUtilVO> receiveFormatMap,Map<String,String> backFormatMap,MsrVO msrvo){
		long sleeptime=60000;//��λ���룬1���� 
		int count=getSleepCount();
		RetMessage ret=new RetMessage();
		MsrVO mvo=msrvo;
		QueueConnection connection=null;
		QueueSession session=null;
		QueueReceiver receiver=null;
		String path="";
		FilepathVO fvo=null;
		try {
			fvo = (FilepathVO) getBaseDao().retrieveByPK(FilepathVO.class, mvo.getSavefilepath());
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(fvo!=null){
			path=fvo.getDescriptions();	
		}
		String strJmsURL =mvo.getAddress()+":"+mvo.getDuankou();
		//TODO wyd ��Ϣ���ж�Ҫ�������������������������ȡ
//		String strJmsQuName = mtvo.getDlm();
		String strJmsQuName = mvo.getVdef2();
		String strJmsUser = mvo.getUname();
		String strJmsPwd = mvo.getUpass();

		Logger.debug(new StringBuffer("JMS������Ϣ��ȡ��ʼ:")
				.append("-------------------"));
		Logger.debug(new StringBuffer("strJmsURL:").append(strJmsURL));
		Logger.debug(new StringBuffer("strJmsQuName:").append(strJmsQuName));
		Logger.debug(new StringBuffer("strJmsUser:").append(strJmsUser));
		Logger.debug(new StringBuffer("strJmsPwd:").append(strJmsPwd));
		Logger.debug(new StringBuffer("JMS������Ϣ��ȡ����:")
				.append("-------------------"));
		
//		lnowTime=new UFDateTime();
		QueueConnectionFactory factory = new com.tibco.tibjms.TibjmsQueueConnectionFactory(
				strJmsURL);

		try {
			connection = factory.createQueueConnection(
					strJmsUser, strJmsPwd);

			 session = connection.createQueueSession(false,
					javax.jms.Session.AUTO_ACKNOWLEDGE);

			javax.jms.Queue queue = session.createQueue(strJmsQuName);

			receiver = session.createReceiver(queue);

			Logger.debug(new StringBuffer(
					"JMS������Ϣ��ʼ��ʼ:------------------------"));
			connection.start();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		RetMessage ret=createQueueReceiverMethod(mvo, connection, session, receiver,path);
		if(true){
			try {
				int receiveNullCount=0;
			
				while(true){
					if(receiveNullCount==count){
						Logger.debug(new StringBuffer("BytesMsg message: ").append("----------------------esb��Ϣ������ʮ����û�н��յ���Ϣ��ֹͣ����------------------------"));
					    ret.setMessage("----------------------esb��Ϣ������ʮ����û�н��յ���Ϣ��ֹͣ����------------------------");
					    ret.setIssucc(true);
						break;
					}
					javax.jms.Message m = receiver.receive(12);
					if(m!=null){
						dealMessage(m, path,mvo,synchvo,receiveFormatMap,backFormatMap);	
						receiveNullCount=0;
					}else{
						try {
							Thread.sleep(sleeptime*2);//һ����Ϣ2����
							receiveNullCount++;
							Logger.debug(new StringBuffer("BytesMsg message: ").append("----------------------receiveNullCount------------------------"+receiveNullCount));
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					if(receiver!=null){
						receiver.close();
					}
					if(session!=null){
						session.close();
					}
					if(connection!=null){
						connection.close();
					}
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		return ret;
	}
	public int getSleepCount(){
		int count=5;
		DipRunsysBVO runbvo=null;
		try {
			runbvo=(DipRunsysBVO) getBaseDao().retrieveByClause(DipRunsysBVO.class, "syscode='DIP-0000013' and nvl(dr,0)=0");
		} catch (DAOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(runbvo!=null&&runbvo.getSysvalue()!=null){
			if(runbvo.getSysvalue().matches("[0-9]*")){
				int time=Integer.parseInt(runbvo.getSysvalue());
				count=time/2;//��������
				if(count<=0){
					count=5;
				}
			}
		}
		return count;
	}
	public void dealMessage(Message m,String path,MsrVO mvo,DipDatasynchVO synchvo,Map<String,EsbMapUtilVO> receiveFormatMap,Map<String,String> backFormatMap){
		String strMessage="";
		TextMessage message = null;
		BytesMessage bytesMessage = null;
		try {
			if (m instanceof TextMessage) {
				message = (TextMessage) m;
				strMessage=message.getText();
				Logger.debug(new StringBuffer("TextMsg message: ")
						.append(message.getText()));
				diplog.writediplog("��Ϣ�� TextMsg message: "+message.getText(), IContrastUtil.LOG_ALL);
				if(strMessage!=null&&strMessage.length()>0){
					OptByPlgImpl op=new OptByPlgImpl();
					String str=strMessage.toUpperCase();
					Logger.debug(new StringBuffer("BytesMsg message: ").append("��ʼ����:"+str));
					
					String[] strerror=new String[3];
					strerror[0]=synchvo.getName();
					strerror[1]=synchvo.getCode();
					op.styleToMsgTB(strMessage,str,receiveFormatMap,mvo, strerror,backFormatMap);
				}else{
					Logger.debug(new StringBuffer("BytesMsg message: ").append("���յ�����ϢΪ��"));
					
				}
			} else if (m instanceof BytesMessage) {
				bytesMessage = (BytesMessage) m;
				Logger.debug("----��BytesMessage");
				 Enumeration propertyNames = bytesMessage.getPropertyNames( );
				 String fileanme="";
			    while(propertyNames.hasMoreElements( )){
			        String name = (String)propertyNames.nextElement( );
			        if(name.equals("wjlname")){
			        	fileanme=bytesMessage.getStringProperty(name);
			        }
			        Object value=bytesMessage.getObjectProperty(name);
			        Logger.debug("4---���ԡ�"+name+"��ֵ��" +value+"��");
			    }
			    int imsgLen = (int) bytesMessage.getBodyLength();
				byte[] byteArg = new byte[imsgLen];
				bytesMessage.readBytes(byteArg);
				strMessage = new String(byteArg);
				strMessage = strMessage.replace("\n", "");
				Logger.debug(new StringBuffer("BytesMsg message: ").append(strMessage));
				diplog.writediplog("�ļ��� BytesMsg message: "+strMessage, IContrastUtil.LOG_ALL);
				if(fileanme!=null&&fileanme.length()>0){
					String pathname=path+fileanme.substring(fileanme.lastIndexOf("/"));
					Logger.debug("----��BytesMessage  pathname��"+pathname);
					writeMethod(pathname, bytesMessage);
				}
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public RetMessage createQueueReceiverMethod(MsrVO mvo,QueueConnection connection,QueueSession session,QueueReceiver receiver,String path){
			FilepathVO fvo=null;
			try {
				fvo = (FilepathVO) getBaseDao().retrieveByPK(FilepathVO.class, mvo.getSavefilepath());
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(fvo!=null){
				path=fvo.getDescriptions();	
			}
			String strJmsURL =mvo.getAddress()+":"+mvo.getDuankou();
			//TODO wyd ��Ϣ���ж�Ҫ�������������������������ȡ
//			String strJmsQuName = mtvo.getDlm();
			String strJmsQuName = mvo.getVdef2();
			String strJmsUser = mvo.getUname();
			String strJmsPwd = mvo.getUpass();

			Logger.debug(new StringBuffer("JMS������Ϣ��ȡ��ʼ:")
					.append("-------------------"));
			Logger.debug(new StringBuffer("strJmsURL:").append(strJmsURL));
			Logger.debug(new StringBuffer("strJmsQuName:").append(strJmsQuName));
			Logger.debug(new StringBuffer("strJmsUser:").append(strJmsUser));
			Logger.debug(new StringBuffer("strJmsPwd:").append(strJmsPwd));
			Logger.debug(new StringBuffer("JMS������Ϣ��ȡ����:")
					.append("-------------------"));
			
//			lnowTime=new UFDateTime();
			QueueConnectionFactory factory = new com.tibco.tibjms.TibjmsQueueConnectionFactory(
					strJmsURL);

			try {
				connection = factory.createQueueConnection(
						strJmsUser, strJmsPwd);

				 session = connection.createQueueSession(false,
						javax.jms.Session.AUTO_ACKNOWLEDGE);

				javax.jms.Queue queue = session.createQueue(strJmsQuName);

				receiver = session.createReceiver(queue);

				Logger.debug(new StringBuffer(
						"JMS������Ϣ��ʼ��ʼ:------------------------"));
				connection.start();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new RetMessage(true,"");
	}
}
