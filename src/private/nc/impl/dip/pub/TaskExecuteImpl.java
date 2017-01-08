package nc.impl.dip.pub;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.dip.tyzh.TyzhProcessor;
import nc.bs.dip.tyzhxml.TyzhXMLProcessor;
import nc.bs.dip.voucher.DataChangeProcessor;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.uap.lock.PKLock;
import nc.impl.dip.optByPluginSrv.OptByPlgImpl;
import nc.itf.dip.pub.IDataProcess;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.itf.dip.pub.IQueryField;
import nc.itf.dip.pub.IRecDataService;
import nc.itf.dip.pub.ITaskExecute;
import nc.jdbc.framework.exception.DbException;
import nc.ui.pub.ClientEnvironment;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.RetMessage;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.dataproce.DipDataproceHVO;
import nc.vo.dip.datarec.DipDatarecHVO;
import nc.vo.dip.datasend.DipDatasendVO;
import nc.vo.dip.datasynch.DipDatasynchVO;
import nc.vo.dip.dateprocess.DateprocessVO;
import nc.vo.dip.filepath.FilepathVO;
import nc.vo.dip.message.MsrVO;
import nc.vo.dip.processflow.DipProcessflowBVO;
import nc.vo.dip.processflow.DipProcessflowHVO;
import nc.vo.dip.processstyle.ProcessstyleVO;
import nc.vo.dip.recserver.DipRecserverVO;
import nc.vo.dip.statemanage.DipStateManageHVO;
import nc.vo.dip.sysregister.DipSysregisterHVO;
import nc.vo.dip.tabstatus.DipTabstatusVO;
import nc.vo.dip.warningset.DipWarningsetBVO;
import nc.vo.dip.warningset.DipWarningsetVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.formulaset.FormulaParseFather;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.tibco.tibjms.TibjmsQueue;
import com.tibco.tibjms.TibjmsQueueConnectionFactory;
import com.tibco.tibjms.TibjmsTopic;
import com.tibco.tibjms.TibjmsTopicConnectionFactory;

public class TaskExecuteImpl implements ITaskExecute {
	private static Logger logger = Logger.getLogger("DEFAULT");
	private BaseDAO bd=null;
	ILogAndTabMonitorSys ilm=(ILogAndTabMonitorSys) NCLocator.getInstance().lookup(ILogAndTabMonitorSys.class.getName());
	IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	private boolean liucheng =false;
	private BaseDAO getBaseDAO(){
		if(bd==null){
			bd=new BaseDAO(IContrastUtil.DESIGN_CON);
		}
		return bd;
	}
	/**
	 * @desc 立即执行--根据预警主键
	 * @author wyd
	 * @param 预警的主表主键
	 * */
	public RetMessage doTaskAtOnce(String pk_warning) {
		try {
			DipWarningsetVO wvo=(DipWarningsetVO) getBaseDAO().retrieveByPK(DipWarningsetVO.class, pk_warning);
			if(wvo==null){
				return new RetMessage(false,"没有找到相应的预警设置！");
			}
			String tasktype=wvo.getTasktype();
			if(tasktype==null||tasktype.length()<=0){
				return new RetMessage(false,"没有找到相应的预警任务类型！");
			}
			String pk_bus=wvo.getPk_bustab();
			if(pk_bus==null||pk_bus.length()<=0){
				return new RetMessage(false,"没有找到相应的业务数据！");
			}
//			1	0001AA1000000001A2YB	数据同步	0001	22	22	22	22	22	0	2011-04-20 16:18:41
//			//	2	0001AA1000000001TQJ4	混合流程	0006						0	2011-05-18 11:00:04
//			3	0001AA1000000001A5AX	数据加工	0002						0	2011-04-20 16:18:58
//			4	0001AA1000000001A5AY	数据转换	0003						0	2011-04-20 16:19:11
//			5	0001AA1000000001A5AZ	数据发送	0004						0	2011-04-20 16:19:24
//			6	0001AA1000000001FVAD	数据流程	0005						0	2011-04-28 14:07:11
			if(tasktype.equals("0001AA1000000001A2YB")){
				return doTBTask(pk_bus);
			}else if(tasktype.equals("0001AA1000000001A5AX")){
				return doJGTask(pk_bus);
			}else if(tasktype.equals("0001AA1000000001A5AY")){
				return doDataChangeTask(pk_bus);
			}else if(tasktype.equals("0001AA1000000001A5AZ")){
				return doFSTask(pk_bus);
			}else if(tasktype.equals("0001AA1000000001FVAD")){
				return doLCTask(pk_bus);
			}else if(tasktype.equals("0001AA100000000343JO")){
				return doAutoContData(pk_bus);
			}else{
				return new RetMessage(false,"没有找到相应的流程！");
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new RetMessage(false,"操作失败！"+e.getMessage());
		}
	}
	/**
	 * @desc 自动对照数据
	 * @author wyd
	 * @param hpk 数据对照定义的主表主键
	 * */
	public RetMessage doAutoContData(String hpk){
		String type=IContrastUtil.YWLX_DZ;
		boolean ret=ilm.writeToTabMonitor_RequiresNew(hpk, type, IContrastUtil.TABMONSTA_ON,null,null);
		if(!ret){
			return new RetMessage(false,"表状态已经被占用！");
		}
		RetMessage rt=null;
		try {
			List errlist=new ArrayList<String>();
			AutoContData iop=new AutoContData(hpk);
			rt = iop.autoContData(errlist);
			if(!rt.getIssucc()){
				ilm.writToDataLog_RequiresNew(hpk, type, rt.getMessage());
				if(errlist!=null&&errlist.size()>0){
					ilm.writToDataLog_RequiresNew(hpk,type,errlist);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ilm.writToDataLog_RequiresNew(hpk, type, e.getMessage());
			rt=new RetMessage(false,e.getMessage());
		}
		ilm.writeToTabMonitor_RequiresNew(hpk, type, IContrastUtil.TABMONSTA_FIN,rt.getSu()==null?0:rt.getSu()+rt.getExit(),rt.getFa()==null?0:rt.getFa());
		ilm.writToDataLog_RequiresNew(hpk, type, rt.getMessage());
		return rt;
	}
	/**
	 * @desc 数据转换
	 * @author wyd
	 * @param hpk 数据转换的主表主键
	 * */
	public RetMessage doDataChangeTask(String hpk) {
		RetMessage rm=null;
		if(hpk==null||hpk.length()<=0){
			return new RetMessage(false,"转换失败！没有找到转换的主键！");
		}
		boolean ret=ilm.writeToTabMonitor_RequiresNew(hpk, IContrastUtil.YWLX_ZH, IContrastUtil.TABMONSTA_ON,null,null);
		if(!ret){
			return new RetMessage(false,"表状态已经被占用");
		}
		try{
			DataChangeProcessor dcp=new DataChangeProcessor(hpk);
			rm=dcp.execute();
		}catch(Exception e){
			e.printStackTrace();
			rm=new RetMessage(false,"转化失败！"+e.getMessage());
		}
		ilm.writeToTabMonitor_RequiresNew(hpk, IContrastUtil.YWLX_ZH, IContrastUtil.TABMONSTA_FIN,rm.getSu()==null?0:rm.getSu(),rm.getFa()==null?0:rm.getFa());
		return rm;
	}
	
	/**
	 * @desc 前台同步
	 * @param hpk
	 * @param filename
	 * */
	public RetMessage doTbTaskFrom(String hpk,String filename){
		String type=IContrastUtil.YWLX_TB;
		boolean ret=ilm.writeToTabMonitor_RequiresNew(hpk, type, IContrastUtil.TABMONSTA_ON,null,null);
		if(!ret){
			return new RetMessage(false,"表状态已经被占用！");
		}
		RetMessage rt=null;

		try {
			List errlist=new ArrayList<String>();
			OptByPlgImpl iop=new OptByPlgImpl();
			String dataname="";
			DipDatasynchVO hvo=(DipDatasynchVO) getBaseDAO().retrieveByPK(DipDatasynchVO.class, hpk);
			dataname = hvo.getDataname();// 存放的是主键
			DipDatarecHVO rhvo=(DipDatarecHVO) getBaseDAO().retrieveByPK(DipDatarecHVO.class, dataname);
			if(rhvo==null){
				rt=new RetMessage(false, "没有找到对应的数据同步定义！");
			}else{
				if(rhvo.getIoflag()!=null&&rhvo.getIoflag().equals("输入")){
					rt = iop.msgToStyle(hpk,filename,dataname,errlist);
				}else{
					rt=iop.dataToFile(hpk,dataname,errlist);
				}
			}
			if(!rt.getIssucc()){
				ilm.writToDataLog_RequiresNew(hpk, type, rt.getMessage());
				if(errlist!=null&&errlist.size()>0){
					ilm.writToDataLog_RequiresNew(hpk,type,errlist);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ilm.writToDataLog_RequiresNew(hpk, type, e.getMessage());
			rt=new RetMessage(false,e.getMessage());
		}
		ilm.writeToTabMonitor_RequiresNew(hpk, type, IContrastUtil.TABMONSTA_FIN,((rt.getSu()==null?0:rt.getSu())+rt.getExit()),rt.getFa());
		if(rt.getErrlist()!=null&&rt.getErrlist().size()>0&&(rt.getMessage()==null||rt.getMessage().length()>0)){
			rt.setMessage("没有全部成功！");
		}
		return rt;
	}
	/**
	 * @desc 数据同步
	 * @author wyd
	 * @param hpk 数据转换的主表主键
	 * */
	public static String tongbu="";
	public static String tongbuType="";
	public RetMessage doTBTask(String hpk){
		String type=IContrastUtil.YWLX_TB;
		/*liyunzhe 修改 传输过来的hpk是为了在中间表和webservice抓取中增加前台ip，来判断访问权限。只是在中间表类型时候把hpk改成了hpk,ip*/
		String shpk=hpk.contains(",")==true?hpk.split(",")[0]:hpk;
		tongbuType=type;
		tongbu=shpk;
		boolean ret=ilm.writeToTabMonitor_RequiresNew(shpk, type, IContrastUtil.TABMONSTA_ON,null,null);
		if(!ret){
			return new RetMessage(false,"表状态已经被占用！");
		}
		RetMessage rt=null;

//		IOptByPlg iop=(IOptByPlg) NCLocator.getInstance().lookup(IOptByPlg.class.getName());
		try {
			List errlist=new ArrayList<String>();
			OptByPlgImpl iop=new OptByPlgImpl();
			rt = iop.msgToStyle(hpk,errlist);
//			if(!rt.getIssucc()){
//				ilm.writToDataLog_RequiresNew(hpk, type, rt.getMessage());
				if(errlist!=null&&errlist.size()>0){
					ilm.writToDataLog_RequiresNew(shpk,type,errlist);
				}
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ilm.writToDataLog_RequiresNew(shpk, type, e.getMessage());
			rt=new RetMessage(false,e.getMessage());
		}
		ilm.writeToTabMonitor_RequiresNew(shpk, type, IContrastUtil.TABMONSTA_FIN,rt.getSu()==null?0:rt.getSu()+rt.getExit(),rt.getFa()==null?0:rt.getFa());
		if(rt.getIssucc()){
			ilm.writToDataLog_RequiresNew(shpk, type, rt.getMessage()+"--"+IContrastUtil.DATAPROCESS_HINT);
		}else{
			ilm.writToDataLog_RequiresNew(shpk, type, rt.getMessage());	
		}
		
		tongbu="";
		tongbuType="";
		return rt;
	}
	/**
	 * @desc 数据加工
	 * @author wyd
	 * @param hpk 数据转换的主表主键
	 * */
	public RetMessage doJGTask(String hpk){
		RetMessage rm=null;
//		写表状态监控
		String type=IContrastUtil.YWLX_JG;
		boolean ret=ilm.writeToTabMonitor_RequiresNew( hpk, type, IContrastUtil.TABMONSTA_ON,null,null);
		if(!ret){
			return new RetMessage(false,"表状态已经被占用！");
		}
		try{
			rm=dataProc(hpk);
			if(!rm.getIssucc()){
				ilm.writToDataLog_RequiresNew(hpk, type, rm.getMessage());
			}else{
				ilm.writToDataLog_RequiresNew(hpk, type, rm.getMessage()+"--"+IContrastUtil.DATAPROCESS_HINT);
			}
		}catch(Exception e){
			e.printStackTrace();
			ilm.writToDataLog_RequiresNew(hpk, type, e.getMessage());
			rm=new RetMessage(false,"同步失败"+e.getMessage());
		}
		if(rm.getIssucc()){
			ilm.writeToTabMonitor_RequiresNew(hpk, type, IContrastUtil.TABMONSTA_FIN,null,null);
		}
		return rm;
	}
	public RetMessage dataProc(String hpk){
		if(hpk==null||hpk.length()<=0){
//			ilm.writToDataLog(hpk, IContrastUtil.YWLX_JG, "没有传入相应的数据加工主键！");
			return new RetMessage(false,"没有传入相应的数据加工主键！");
		}
		DipDataproceHVO hvo = null;
		try {
			hvo = (DipDataproceHVO) getBaseDAO().retrieveByPK(DipDataproceHVO.class, hpk);
			if(hvo==null){
//				ilm.writToDataLog(hpk, IContrastUtil.YWLX_JG, "没有找到相应的数据加工父VO！");
				return new RetMessage(false,"没有找到相应的数据加工父VO！");
			}
			String sql=hvo.getProcecond();
			String tableanme=hvo.getProcetab();//加工表名
			String procetype=hvo.getProcetype();
			String refproce=hvo.getDef_str_2();//加工主键
			String oldtable=hvo.getFirstdata();//原始表名
			ProcessstyleVO styleVO = new ProcessstyleVO();
			styleVO = (ProcessstyleVO)getBaseDAO().retrieveByPK(ProcessstyleVO.class, refproce);
			if(styleVO==null){
				return new RetMessage(false,"没有找到对应加工插件类定义"+oldtable);
			}
			String cName = styleVO.getImpclass();
			if(cName==null||cName.length()<=0){
				return new RetMessage(false,"没有找到对应的加工插件类！");
			}
//			Class<? extends IDataProcess> ipd=(Class<? extends IDataProcess>) Class.forName(cName);
//			ipd.dataprocess();
			IDataProcess c = null;
				try {
					c = (IDataProcess) Class.forName(cName)
							.newInstance();
				} catch (RuntimeException e) {
					e.printStackTrace();
					return new RetMessage(false,"初始化数据加工插件类失败"+e.getMessage());
				}catch(ClassNotFoundException e1){
					e1.printStackTrace();
					return new RetMessage(false,"没有找到插件类："+cName);
				
				}catch(Exception e2){
					e2.printStackTrace();
					return new RetMessage(false,"初始化数据加工插件类失败"+e2.getMessage()+e2.getStackTrace()[0]);
				
				}
				 /*lyz 2012-12-26 增加通用校验 start*/
				RetMessage retcheck=iqf.docheck(hpk);
				RetMessage ret=new RetMessage();
				if(retcheck.getIssucc()){
					ret=c.dataprocess(hpk, sql, tableanme, oldtable);
					if(retcheck.getErrlist()!=null&&retcheck.getErrlist().size()>0){
						ret.setErrlist(retcheck.getErrlist());
					}
				}else{
					return retcheck;
				}
				return ret;
				/*lyz 2012-12-26 增加通用校验 end*/
			
//				return c.dataprocess(hpk, sql, tableanme, oldtable);
			//2011-6-14
			/*if("数据卸载".equals(procetype)||"数据预置".equals(procetype)){
				dataproc(sql,oldtable,procetype);
			}else{
				dataproc(sql,tableanme,procetype);
			}
			return new RetMessage(true,"");*/
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_JG, e.getMessage());
			return new RetMessage(false,e.getMessage());
		}catch(Exception e1){
			e1.printStackTrace();
			ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_JG, e1.getMessage());
			return new RetMessage(false,e1.getMessage());
		}
	}
	public void dataproc(String sql, String tablename,String procetype) throws Exception {
		// TODO Auto-generated method stub

		if("数据汇总".equals(procetype)){
			sjjg(sql,tablename,procetype);
		}else if("数据清洗".equals(procetype)){
			sjqx(sql,tablename,procetype);
		}else if("数据卸载".equals(procetype)){
			sjxz(sql,tablename,procetype);
		}else if("数据预置".equals(procetype)){
			sjyz(sql,tablename,procetype);
		}else if("数据转换".equals(procetype)){
			sjzh(sql,tablename,procetype);
		}else{
			throw new Exception("还没有实现此功能:"+procetype);
		}

	}
	public RetMessage doStateChange(String hpk){

		if(hpk==null||hpk.length()<=0){
//			ilm.writToDataLog(hpk, IContrastUtil.YWLX_JG, "没有传入相应的数据加工主键！");
			return new RetMessage(false,"没有传入相应的数据加工主键！");
		}
		DipStateManageHVO hvo = null;
		try {
			hvo = (DipStateManageHVO) getBaseDAO().retrieveByPK(DipStateManageHVO.class, hpk);
			if(hvo==null){
//				ilm.writToDataLog(hpk, IContrastUtil.YWLX_JG, "没有找到相应的数据加工父VO！");
				return new RetMessage(false,"没有找到相应的数据状态管理父VO！");
			}
			String sql=hvo.getProcecond();
			String tableanme=hvo.getProcetab();//加工表名
			String refproce=hvo.getTasktype();//加工主键
			String oldtable=hvo.getFirstdata();//原始表名
			String tablekey=hvo.getFirsttab();
			String successState=hvo.getConstatus();
			String faildState=hvo.getEndstatus();
			ProcessstyleVO styleVO = new ProcessstyleVO();
			styleVO = (ProcessstyleVO)getBaseDAO().retrieveByPK(ProcessstyleVO.class, refproce);
			if(styleVO==null){
				return new RetMessage(false,"没有找到对应加工插件类定义"+oldtable);
			}
			String cName = styleVO.getImpclass();
			if(cName==null||cName.length()<=0){
				return new RetMessage(false,"没有找到对应的加工插件类！");
			}
//			Class<? extends IDataProcess> ipd=(Class<? extends IDataProcess>) Class.forName(cName);
//			ipd.dataprocess();
			IDataProcess c = null;
				try {
					c = (IDataProcess) Class.forName(cName)
							.newInstance();
				} catch (RuntimeException e) {
					e.printStackTrace();
					return new RetMessage(false,"初始化数据加工插件类失败"+e.getMessage());
				}catch(ClassNotFoundException e1){
					e1.printStackTrace();
					return new RetMessage(false,"没有找到插件类："+cName);
				
				}catch(Exception e2){
					e2.printStackTrace();
					return new RetMessage(false,"初始化数据加工插件类失败"+e2.getMessage()+e2.getStackTrace()[0]);
				
				}
			
				RetMessage rm= c.dataprocess(hpk, sql, tableanme, oldtable);
				
				
				
				
				
				if(rm.getIssucc()){
					if(rm.getMessage().equals("0")){//表示满足表状态更新条件，
						boolean flag=ilm.writeToTabMonitor_RequiresNew_StateManage(hpk, IContrastUtil.YWLX_ZTYZ, IContrastUtil.TABMONSTA_FIN, null, null, successState, tablekey);
						if(flag){
							rm.setMessage("更改0状态完成");
						}else{
							rm.setMessage("更改0状态出现错误");
						}
					}else if(rm.getMessage().equals("1")){//表示没有满足表状态更新条件
						boolean flag=ilm.writeToTabMonitor_RequiresNew_StateManage(hpk, IContrastUtil.YWLX_ZTYZ, IContrastUtil.TABMONSTA_FIN, null, null, faildState, tablekey);
						if(flag){
							rm.setMessage("更改1状态完成");
						}else{
							rm.setMessage("更改1状态出现错误");
						}
					} 
				}
					return rm;
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
			//2011-6-14
			/*if("数据卸载".equals(procetype)||"数据预置".equals(procetype)){
				dataproc(sql,oldtable,procetype);
			}else{
				dataproc(sql,tableanme,procetype);
			}
			return new RetMessage(true,"");*/
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_JG, e.getMessage());
			return new RetMessage(false,e.getMessage());
		}catch(Exception e1){
			e1.printStackTrace();
			ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_JG, e1.getMessage());
			return new RetMessage(false,e1.getMessage());
		}
	
	}
	/**
	 * 数据转换
	 * @param sql
	 * @param tablename
	 * @param procetype
	 * @throws DbException 
	 * @throws SQLException 
	 * @throws BusinessException 
	 */
	public void sjzh(String sql, String tablename, String procetype) throws BusinessException, SQLException, DbException {
		
		iqf.exesql(sql);
	}
	/**
	 * 数据预置
	 * @param sql
	 * @param tablename
	 * @param procetype
	 * @throws DbException 
	 * @throws SQLException 
	 * @throws BusinessException 
	 */
	public void sjyz(String sql, String tablename, String procetype) throws BusinessException, SQLException, DbException {
		// TODO Auto-generated method stub
		iqf.exesql(sql);
		
	}
	/**
	 * 数据汇总
	 * */
	public void sjjg(String sql, String tablename,String procetype) throws BusinessException, SQLException, DbException{
//		查询加工后的结果集
		List listValue = iqf.queryListMapSingleSql(sql);
		String tagertField = ""; //目标字段
		String sourceField = "";//源字段
		String tagertFieldValue = "";//目标字段值
		if(listValue!=null && listValue.size()>0){
			//查询需要插入加工数据表的字段，后续需优化
			sql = "select b.cname,b.ename,b.quotetable,b.quotecolu,b.ispk from dip_dataproce_h h " +
			" left join dip_dataproce_b b on b.pk_dataproce_h = h.pk_dataproce_h " +
			" where nvl(h.dr,0)=0 and nvl(b.dr,0)=0 and h.procetab='"+tablename+"' ";
			List listTableV = iqf.queryFieldSingleSql(sql);
			UFBoolean isPK = new UFBoolean(false);
			String insertSql = "";

			for(int i = 0;i<listValue.size();i++){
				StringBuffer fileds = new StringBuffer();
				StringBuffer fieldsValue = new StringBuffer();
				HashMap mapValue = (HashMap)listValue.get(i);
				//组建插入语句
				insertSql = "insert into "+tablename+" (";

				if(listTableV!=null && listTableV.size()>0){
					for(int j = 0;j<listTableV.size();j++){
						List listField = (ArrayList)listTableV.get(j);
						tagertField = listField.get(1) == null ?"":listField.get(1).toString();
						sourceField = listField.get(1) == null?"":listField.get(1).toString();
						isPK = listField.get(4)==null ? new UFBoolean(false):new UFBoolean(listField.get(4).toString());
						//判断是否是PK
						if(isPK.booleanValue()){
							tagertFieldValue = iqf.getOID();
						}else{
							tagertFieldValue = mapValue.get(sourceField.toUpperCase())==null?"":mapValue.get(sourceField.toUpperCase()).toString();
						}

						//fileds.append(sourceField+",");
						//2011-05-27 
						fileds.append(tagertField+",");

						fieldsValue.append("'"+tagertFieldValue+"',");
					}
				}
				String field = fileds.toString().substring(0, fileds.toString().length()-1);
				String fieldValue = fieldsValue.toString().substring(0, fieldsValue.toString().length()-1);
				insertSql = insertSql+field+") values ("+fieldValue+") ";
				iqf.exesql(insertSql);
			}
		}
	}

	/**
	 * 数据清洗
	 * */
	public void sjqx(String sql, String tablename,String procetype) throws BusinessException, SQLException, DbException{
		iqf.exesql(sql);

	}

	/**
	 * 数据卸载
	 * */
	public void sjxz(String sql, String tablename,String procetype) throws BusinessException, SQLException, DbException{

		//第一种情况，源表追加备份，把当前表的所有数据，插入到备份表，然后清空当前表
		if("DIP_BAK1".equals(sql)){
			sql = "select 1 from user_tables where table_name = '"+tablename+"_BAK'";
			String tablebak = iqf.queryfield(sql);
			//查询备份表是否已创建
			if("1".equals(tablebak)){
				//直接备份数据
				sql = "insert into "+tablename+"_BAK select * from "+tablename+"";
				iqf.exesql(sql);
			}else{
				//创建表，并备份数据
				sql = "create table "+tablename+"_BAK as select * from "+tablename+" ";
				iqf.exesql(sql);
			}

			sql = "delete from "+tablename;
			iqf.exesql(sql);
		}
		/*将卸载备份的表名改为dip_bak加年月日时分秒的格式，便于一天对此备份
		 * 2011-06-21
		 * zlc*/
		if("DIP_BAKTS".equals(sql)){
//			UFDate date = new UFDate(new Date());
//			UFTime ts=new UFTime(new Time(0));//2011-06-21 
			UFDateTime da=ClientEnvironment.getInstance().getServerTime();
//			da.getDay();
//			if(da.getDay()<){}
			String ss=""+da.getYear()+da.getMonth()+da.getDay()+da.getHour()+da.getMinute()+da.getSecond();
			//ss=ss.trim();
			String str=da.toString();
//			nc.bs.logging.Logger.debug(str+"**********************************************************************8");
			
			//第二种情况，把当前表的数据插入到新创建的“当前表明时间戳”的表中，然后清空当前表
			String tablebakname = "";
			tablebakname ="DIP_BAK"+ss.trim();
			sql = "select 1 from user_tables where table_name = '"+tablebakname+"'";
			String tablebak = iqf.queryfield(sql);
			if("1".equals(tablebak)){
				sql = "drop table "+tablebakname;
				iqf.exesql(sql);
			}

			//备份表
			sql = "create table "+tablebakname+" as select * from "+tablename+" ";
			iqf.exesql(sql);
			//清空当前表
			sql = "delete from "+tablename;
			iqf.exesql(sql);
		}


	}
//	public static int threadNum=0;
	public static Map<String,Integer> countmap;//线程计数器
	public static Map<String,StringBuffer> messmap;


	/**
	 * @desc 数据发送
	 * @author 张进双 服务器注册  
	 * @since 2011-5-21
	 * @param hpk 数据转换的主表主键
	 * */
	public RetMessage doFSTask(String hpk){
		if(countmap==null){
			countmap=new HashMap<String, Integer>();
		}
		if(messmap==null){
			messmap=new HashMap<String, StringBuffer>();
		}
		countmap.put(hpk, 0);
		countmap.put(hpk+"s", 0);
		countmap.put(hpk+"f", 0);
		RetMessage ret=doFsTask(hpk);
		int s=countmap.containsKey(hpk+"s")?countmap.get(hpk+"s"):null;
		int f=countmap.containsKey(hpk+"f")?countmap.get(hpk+"f"):null;

		if(!ret.getIssucc()){
			ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_FS, ret.getMessage()+" 【成功】"+s+"条记录，【失败】"+f+"条记录。");
		}else{
			ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_FS, "【成功】"+s+"条记录，【失败】"+f+"条记录。");
		}
				ilm.writeToTabMonitor_RequiresNew(hpk, IContrastUtil.YWLX_FS, IContrastUtil.TABMONSTA_FIN,s,f);
		return ret;
	}
	
	public RetMessage doFsTask(String hpk){
		
		messmap.put(hpk, new StringBuffer());
		if(hpk==null||hpk.length()<=0){
			return new RetMessage(false,"业务主键为空！");
		}
//		写表状态监控
		String type=IContrastUtil.YWLX_FS;
		boolean ret=ilm.writeToTabMonitor_RequiresNew(hpk, type, IContrastUtil.TABMONSTA_ON,null,null);
		if(!ret){
			return new RetMessage(false,"目录状态已经被占用！");
		}


		DipDatasendVO hvo=null;
		try {
			hvo = (DipDatasendVO) getBaseDAO().retrieveByPK(DipDatasendVO.class, hpk);
		} catch (DAOException e) {
			e.printStackTrace();
			return new RetMessage(false,"查询对应的数据发送定义出错！"+hpk+e.getMessage());
		}
		if(hvo==null){
			return new RetMessage(false,"没有找到对应的数据发送定义！"+hpk);
		}

		DipRecserverVO recserver = null;
		try {
			recserver = (DipRecserverVO) getBaseDAO().retrieveByPK(DipRecserverVO.class, hvo.getServer());
		} catch (DAOException e) {
			e.printStackTrace();
			return new RetMessage(false,"查询对应的数据接收服务器错误！"+hpk+e.getMessage());
		}
		if(recserver==null){
			return new RetMessage(false,"没有找到对应的数据数据接收服务器！"+hpk);
		}
		// 路径
		FilepathVO filepath = null;
		try {
			filepath = (FilepathVO) getBaseDAO().retrieveByPK(FilepathVO.class, hvo.getFilepath());
		} catch (DAOException e) {
			e.printStackTrace();
			return new RetMessage(false,"查询对应的发送文件目录错误！"+hpk+e.getMessage());
		}
		if(filepath==null){
			return new RetMessage(false,"没有找到对应的发送文件目录！"+hpk);
		}

		String strMsg = ""; 
		//从配置文件中读取每次启动的线程的数量  从而可以手工进行启动线程数量的配置
		String transnum=hvo.getThreadno();//并行线程数
		//如果前台没有输入线程数，默认是启动10个线程。
		if(transnum==null||!transnum.matches("[0-9]*")){
			transnum="10";
		}
		//存放所有的已经转成功的省份的省份编码//10,5-30
		int num=0;
		//存放访问目录下的文件夹，及具体的文件信息
		HashMap<String,File> map=new HashMap<String,File>();
		//如果没有已经传输结束的单位，则结束此次操作,如果有，加入查询条件中


		String strFoldPath = filepath.getDescriptions()+"/send";
		File recFile=new File(strFoldPath);
		File []  listFiles= recFile.listFiles(); //所有单位的文件夹		
		
		//把指定目录下的文件放到MAP集合中，存放所有的文件夹
		if(listFiles!=null){
			for(int i=0;i<listFiles.length;i++){
				map.put(listFiles[i].getName(), listFiles[i]);
			}
		}else{

			return new RetMessage(false,"发送的文件夹为空"+strFoldPath);
		}
		List list=new ArrayList<String>();
		// 用于标识是否开始下一轮循环
		for(int k=0;k<3;k++){
			list=new ArrayList<String>();
			
			// 遍历当前目录下所有的文件，如果此文件如已经生成的单位的编码存在包含关系，则把此目录进行生成
			Iterator iter = map.keySet().iterator(); 
			while (iter.hasNext()) { 
				if(countmap.get(hpk)<Integer.parseInt(transnum)){
					countmap.put(hpk,((Integer) countmap.get(hpk)+1));
					String filename=(String) iter.next();
//					Map.Entry<String,File> entry = (Map.Entry<String,File>) iter.next(); 
					File f=map.get(filename);
					String name=f.getName();//类似42010000的单位名称。
					{
						test   t   =   new   test(name,num,f,filepath.getDescriptions(),recserver.getDescs(),hpk);
						t.start();	
						list.add(filename);
						
					}
	
				}else{
					while((Integer)countmap.get(hpk)>=Integer.parseInt(transnum)){
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			while(!((Integer)countmap.get(hpk)==0)){
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					map.remove(list.get(i));
				}
			}
		}
		if(messmap.get(hpk)!=null&&messmap.get(hpk).length()>0){
			return new RetMessage(false,messmap.get(hpk).toString());
		}else{
			return new RetMessage(true,"发送成功！");
		}

	}
	/**
	 * @desc 数据流程
	 * @author 张进双
	 * @param hpk 数据转换的主表主键
	 * */
	public RetMessage doLCTask(String hpk){
		if(hpk==null){
			return new RetMessage(false,"流程主键是空！");
		}
		//1、根据流程主键（hpk）找到附表vo[]（按照序号排序，用getBaseDAO()操作vo），如果vo[]是空或者数组长度等于0，返回，否则，执行下一步
		//2、循环vo[]，根据vo[i]的类型，掉相应的方法
		String type=null;
		List<DipProcessflowBVO> bvo=null;
		try {
			//拼数据处理日志的VO
			DateprocessVO vo=new DateprocessVO();
			vo.setPk_bus(hpk);
			vo.setActivetype("数据流程");
			DipProcessflowHVO hvo=(DipProcessflowHVO) getBaseDAO().retrieveByPK(DipProcessflowHVO.class, hpk);
			if(hvo!=null){
				DipSysregisterHVO shvo=(DipSysregisterHVO) getBaseDAO().retrieveByPK(DipSysregisterHVO.class, hvo.getPk_xt());
				vo.setActivecode(hvo.getCode()==null?"":hvo.getCode());
				vo.setActive(hvo.getName()==null?"":hvo.getName());
				if(shvo!=null){
					vo.setSyscode(shvo.getCode()==null?"":shvo.getCode());
					vo.setSysname(shvo.getExtname()==null?"":shvo.getExtname());
				}
				
			}
			
			bvo=(List<DipProcessflowBVO>) getBaseDAO().retrieveByClause(DipProcessflowBVO.class, " pk_processflow_h='"+hpk+"' and nvl(dr,0)=0 "," orderno");
			if(bvo!=null && bvo.size()>0){
				liucheng=true;
				for(int i=0;i<bvo.size();i++){
					RetMessage rt=new RetMessage();
					type=bvo.get(i).getTasktype();
					String tasktype=SJUtil.getYWbyYWLX(type);
					String pk=bvo.get(i).getCode()==null?"":bvo.get(i).getCode();
					String str="";//预警主键。
					List<DipWarningsetVO> warset=(List<DipWarningsetVO>)getBaseDAO().retrieveByClause(DipWarningsetVO.class, " pk_bustab='"+pk+"' and nvl(dr,0)=0 ");
					if(warset!=null&&warset.size()==1){
						str=warset.get(0).getPrimaryKey();
					}
					String edate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//设置开始时间
					vo.setEdate(edate);
					vo.setPk_processflow_b(bvo.get(i).getPk_processflow_b());
					if((str==null||str.length()<=0)||isFitWarnCondition(str)){
						if(tasktype.equals(IContrastUtil.YWLX_FS)){
							rt=doFSTask(pk);
						}else if(tasktype.equals(IContrastUtil.YWLX_JG)){
							rt=doJGTask(pk);
						}else if(tasktype.equals(IContrastUtil.YWLX_TB)){
							rt=doTBTask(pk);
						}else if(tasktype.equals(IContrastUtil.YWLX_ZH)){
							rt=doDataChangeTask(pk);
						}else if(tasktype.equals(IContrastUtil.YWLX_ESBSEND)){
							rt=doESBSendTask(pk);
						}else if(tasktype.equals(IContrastUtil.YWLX_TYZH)){
							rt=doTYZHTask(pk);
						}else if(tasktype.equals(IContrastUtil.YWLX_TYZHXML)){
							rt=doTYXMLTask(pk);
						}else if(tasktype.equals(IContrastUtil.YWLX_ZTYZ)){
							rt=doStateChange(pk);
						}else if(tasktype.equals(IContrastUtil.YWLX_DZ)){
							rt=doAutoContData(pk);
						}
					}else{
						rt.setIssucc(false);
						rt.setMessage("没有满足预警条件");
					}
					DateprocessVO dvo=(DateprocessVO) vo.clone();
					dvo.setSdate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//设置结束时间
					if(!rt.getIssucc()){
						rt.setMessage("第"+(i+1)+"个任务【"+bvo.get(i).getName()+"】执行【不通过】：原因【"+rt.getMessage()+"】");
						dvo.setSuccess(new UFBoolean(false));
						dvo.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
					}else{
						rt.setMessage("第"+(i+1)+"个任务【"+bvo.get(i).getName()+"】执行【通过】");
						dvo.setSuccess(new UFBoolean(true));
						dvo.setDef_str_1(IContrastUtil.DATAPROCESS_HINT);
					}
					dvo.setContent(rt.getMessage());
					if(dvo!=null){
						ilm.writToDataLog_RequiresNew(dvo);
					}
				}
				liucheng=false;
			}else{
				return new RetMessage(false,"没有找到相应的流程");
			}
		} catch (DAOException e) {
			e.printStackTrace();
			return new RetMessage(false,"数据库处理失败！"+e.getMessage());
		}
		return new RetMessage(true,"流程执行成功！");

	}
	/**
	 * @desc ESB文件发送
	 * @author wyd
	 * @param hpk ESB文件发送的主表主键
	 * */
	public RetMessage doESBSendTask(String hpk){
		RetMessage rm=null;
		if(hpk==null||hpk.length()<=0){
			return new RetMessage(false,"文件操作失败！没有找到文件操作的主键！");
		}
		boolean ret=ilm.writeToTabMonitor_RequiresNew(hpk, IContrastUtil.YWLX_ESBSEND, IContrastUtil.TABMONSTA_ON,null,null);
		if(!ret){
			return new RetMessage(false,"表状态已经被占用");
		}
		try{
			DealFile dcp=new DealFile(hpk);
			rm=dcp.execDealFile();
		}catch(Exception e){
			e.printStackTrace();
			rm=new RetMessage(false,"文件操作失败！"+e.getMessage());
		}
		if(!rm.getIssucc()){
			ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_ESBSEND, rm.getMessage());
		}
		ilm.writeToTabMonitor_RequiresNew(hpk, IContrastUtil.YWLX_ZH, IContrastUtil.YWLX_ESBSEND,rm.getSu()==null?0:rm.getSu(),rm.getFa()==null?0:rm.getFa());
		return rm;
	}
	/**
	 * @desc 通用XML转换
	 * @author wyd
	 * @param hpk 通用xml转换的主表主键
	 * */
	public RetMessage doTYXMLTask(String hpk){
		RetMessage rm=null;
		if(hpk==null||hpk.length()<=0){
			return new RetMessage(false,"转换失败！没有找到转换的主键！");
		}
		boolean ret=ilm.writeToTabMonitor_RequiresNew(hpk, IContrastUtil.YWLX_TYZHXML, IContrastUtil.TABMONSTA_ON,null,null);
		if(!ret){
			return new RetMessage(false,"表状态已经被占用");
		}
		try{
			TyzhXMLProcessor dcp=new TyzhXMLProcessor(hpk);
			rm=dcp.execute();
		}catch(Exception e){
			e.printStackTrace();
			rm=new RetMessage(false,"转化失败！"+e.getMessage());
		}
		if(!rm.getIssucc()){
			ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_TYZHXML, rm.getMessage());
		}
		ilm.writeToTabMonitor_RequiresNew(hpk, IContrastUtil.YWLX_TYZHXML, IContrastUtil.TABMONSTA_FIN,rm.getSu()==null?0:rm.getSu(),rm.getFa()==null?0:rm.getFa());
		return rm;
	}
	/**
	 * @desc 通用数据转换
	 * @author wyd
	 * @param hpk 通用数据转换的主表主键
	 * */
	public RetMessage doTYZHTask(String hpk){
		RetMessage rm=null;
		if(hpk==null||hpk.length()<=0){
			return new RetMessage(false,"转换失败！没有找到转换的主键！");
		}
		boolean ret=ilm.writeToTabMonitor_RequiresNew(hpk, IContrastUtil.YWLX_TYZH, IContrastUtil.TABMONSTA_ON,null,null);
		if(!ret){
			return new RetMessage(false,"表状态已经被占用");
		}
		try{
			TyzhProcessor dcp=new TyzhProcessor(hpk);
			rm=dcp.execute();
		}catch(Exception e){
			e.printStackTrace();
			rm=new RetMessage(false,"转化失败！"+e.getMessage());
		}
		if(!rm.getIssucc()){
			ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_TYZH, rm.getMessage());
		}
		ilm.writeToTabMonitor_RequiresNew(hpk, IContrastUtil.YWLX_TYZH, IContrastUtil.TABMONSTA_FIN,rm.getSu()==null?0:rm.getSu(),rm.getFa()==null?0:rm.getFa());
		return rm;
	}
	class test extends Thread {
		String url = null;
		IRecDataService recService=(IRecDataService)NCLocator.getInstance().lookup(IRecDataService.class.getName());
		private int iNum = 0;

		String strMsg=null;
		private File ifile=null;
		boolean stoped = false;
		String path = null;
		String src = null;
		String hpk="";
		String name="";


		public test(){

		}
		/**
		 * @desc
		 * @param name 文件路径下的公司目录
		 * @param num 大概是第几个线程
		 * @param file 发送的目录
		 * @param path 路径
		 * @param src HTTP连接的源
		 * @param hpk 发送定义的主键
		 * */
		public test(String name,int num,File file,String path,String src,String hpk) {
			this.name=name;
			this.iNum = num;
			this.ifile=file;
			this.path=path;
			this.src = src;
			this.hpk=hpk;
		}

		public void run() {
			//synchronized (this) {
				try {
					if(PKLock.getInstance().acquireLock(name, null, null)){
						//每次启动线程的时候，间隔2秒
						BgWorkingContext bgwc=new BgWorkingContext();
						LinkedHashMap<String, Object> keyMap =bgwc.getKeyMap();
						//PKLock pklock=PKLock.getInstance();
					//	pklock.addDynamicLock();
						//LRUCache<K, V>;
						keyMap.put("logdir",path+"/return"+"/"+name);
						keyMap.put("backdir",path+"/back/"+name);
						keyMap.put("postconfig0","billtype=gl;sysno=1;urladdress="+src+"");
						File[] listfiles=ifile.listFiles();
						if(listfiles!=null&&listfiles.length>0){
							for(int i=0;i<listfiles.length-1;i++){
								for(int j=0;j<listfiles.length-i-1;j++){
									if(listfiles[j+1].getName().compareTo(listfiles[j].getName())<0){
										File tempfile=listfiles[j+1];
										listfiles[j+1]=listfiles[j];
										listfiles[j]=tempfile;
									}
								}
							}
							for(int i=0;i<listfiles.length;i++){
								File f=listfiles[i];
								if(f==null){
									continue;
								}else{
									if(f.getName()==null){
										continue;
									}else{
										String name=f.getName();
										String regex="[0-9|A-Z|a-z]+\\.xml$";
										if(!name.matches(regex)){
											continue;
										}
									}
								}
								
								keyMap.put("sendfilepath", f.getAbsolutePath());
								bgwc.setRegistryFileName("外部交换平台后台发送");						
								try {
									HTTPClient httpclient = new HTTPClient(src);
									SAXReader sr=new SAXReader();
									httpclient.postDoc(sr.read(f));
									String response=httpclient.getResponseDoc();//.getResponse();
									boolean issuc=genSorFcount(response);
									//存放新路径的文件下
									String newPath = issuc?keyMap.get("logdir").toString():path+"/returnerror"+"/"+name;
									File myfile = new File(newPath);
									if(!myfile.exists()) {
										myfile.mkdirs();
									}
									myfile=new File(newPath+"/"+f.getName());
									BufferedWriter out = new BufferedWriter(new FileWriter(myfile));
									out.write(response);
									out.close();
									//把源文件移动到另一个文件夹下
		
									String backdir=issuc?keyMap.get("backdir").toString():path+"/senderror/"+name;
									String newpathback = backdir+"/"+f.getName();
									File s=new File(backdir);
									if(!s.exists()){
										s.mkdirs();
									}else{
										File[] fs=s.listFiles();
										if(fs!=null&&fs.length>0){
											for(int k=0;k<fs.length;k++){
												if(fs[k].getName().equals(f.getName())){
													fs[k].delete();
												}
											}
										}
									}
									f.renameTo(new File(newpathback));
								} catch (BusinessException e) {
									e.printStackTrace();
									TaskExecuteImpl.messmap.put(hpk,(messmap.get(hpk).append(f.getName()+":"+e.getMessage()+",")));
								} 
								
								
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					TaskExecuteImpl.messmap.put(hpk,(messmap.get(hpk).append(ifile.getName()+"目录:"+e.getMessage()+",")));
				}finally{
					PKLock.getInstance().releaseLock(name, null, null);
					TaskExecuteImpl.countmap.put(hpk,((Integer) countmap.get(hpk)-1));
				}
				//存储当前结束的线程个数
				System.out.println("正在运行第 " + iNum + "个线程! ");
			}
			public boolean genSorFcount(String xml){
				boolean issuc=false;
				try {
					Document doc = DocumentHelper.parseText(xml);
					Element root = doc.getRootElement();
					Object ob=root.attributeValue("successful");
					if(ob!=null&&ob.toString().equals("Y")){
						countmap.put(hpk+"s", countmap.get(hpk+"s")+1);
						issuc=true;
					}else{
						countmap.put(hpk+"f", countmap.get(hpk+"f")+1);
					}
				} catch (DocumentException e) {
					e.printStackTrace();
					countmap.put(hpk+"f", countmap.get(hpk+"f")+1);
				}catch(Exception e){
					e.printStackTrace();
					countmap.put(hpk+"f", countmap.get(hpk+"f")+1);
				}
				return issuc;
			}
		}

	//} 
	/**
	 * @desc 创建文件路径，如果path不存在，创建路径，如果存在，就算了
	 * @author wyd
	 * @param path 文件路径
	 * */
	public boolean createFilePath(String path){
		File f = new File(path);
		if(!f.exists()){
			return f.mkdirs();
		}else{
			return true;
		}
	}

	/**
	 * @desc 判断是否符合预警条件，如果符合，返回true,否则返回false
	 * @param String pk_warn 预警的主键 
	 * */
	public boolean isFitWarnCondition(String pk_warn){
		boolean ret=true;
		try {
			List<DipWarningsetBVO> bvos=(List<DipWarningsetBVO>) getBaseDAO().retrieveByClause(DipWarningsetBVO.class, "pk_warningset='"+pk_warn+"' and nvl(dr,0)=0 ","orderno");
//			DipWarningsetVO warsetvo=(DipWarningsetVO)getBaseDAO().retrieveByPK(DipWarningsetVO.class, pk_warn);
//				//在数据流程中执行单个任务时候要查看单个任务的预警时间是否满足执行条件，如果满足执行，否者就不执行。
//			//boolean sigYj=true;//
//			if(WarnThread.YJLC&&warsetvo!=null){
//			  String nexttime=warsetvo.getNexttime();
//			  String thistime=warsetvo.getThistime();
//			  long time=System.currentTimeMillis();
//			try {
//				time = iqf.getDate();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			  String times=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));//服务器的当前时间
//			  if(nexttime!=null&&!nexttime.equals("")){
//				  if(times.compareTo(nexttime)==1&&(thistime==null||thistime.equals("")||thistime.length()<=0)){
//						 // sigYj=true;
//					  }else{
//						  //sigYj=false;//如果为false，则表示在数据流程执行的时候，单个任务的预警执行时间不满足，不能执行
//						  return false;//如果为false，则表示在数据流程执行的时候，单个任务的预警执行时间不满足，不能执行
//					  }  
//			  }
//			  
//			}
			
			if(bvos==null||bvos.size()<=0){				
				ret=true;
			}else{
				Collection tsvo=null;
				StringBuffer foumu=new StringBuffer();
				for(DipWarningsetBVO bvo:bvos){
					if(bvo.getVdef1()!=null&&bvo.getVdef1().length()>0){
						foumu.append(bvo.getVdef1());
					}
					String fh=bvo.getOperation();
					
					//表主键
					String tabname=bvo.getPk_datadefinit_h();
					//状态主键
					String status=bvo.getPk_statusregist();
					if(tabname!=null&&tabname.length()>0&&status!=null&&status.length()>0){
						tsvo=getBaseDAO().retrieveByClause(DipTabstatusVO.class, "tablecode='"+tabname+"' and def_str_1='"+status+"' and nvl(dr,0)=0");
						if(tsvo==null||tsvo.size()<=0){
							foumu.append("\"1.00\"!=\"1.00\"");
						}else {
						//	foumu.append("true");//把true,传入公式中，得到的值为null
						//	foumu.append("\"1.00\"");
							foumu.append("\"1.00\"=\"1.00\"");
						}
					}
					if(fh!=null&&fh.length()>0){
						String op="";
						if(fh.equals("AND")){
							op="&&";
						}else if(fh.equals("OR")){
							op="||";
						}else if(fh.equals("NOT")){
							op="!";
						}
						foumu.append(op);
					}
				}
				nc.bs.logging.Logger.debug("数据预警打印出来的公式是-----"+foumu);
				if(foumu!=null&&foumu.length()>0){
				
					FormulaParseFather f=new nc.bs.pub.formulaparse.FormulaParse();
					f.setExpress(foumu.toString());//设置公式
					String res = f.getValue();
					nc.bs.logging.Logger.debug("数据预警打印出来的公式是-----"+res);
					if(res!=null&&!res.equals("null")){
						res=res.replace("0", "");
						res=res.replace(".", "");
						
						if(!(res.equals("1")||res.equals("Y"))){
							ret=false;
						}
					}else{
						ret=false;
					}
					
				}
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret= false;
		}

		if(!ret){//如果不符合业务预警条件，往数据处理日志里写一条日志
			//if(!liucheng){//如果是走的数据流程时候，在数据流程中的单个任务不满足预警条件不需要写在处理日志中，由数据流程中的写日志方法写进去。
				ilm.writToDataLog_RequiresNew(pk_warn, "不符合预警条件");	
			//}
			
		}
		nc.bs.logging.Logger.debug("是否满足预警条件 ------------"+ret);
		return ret;
	}
	
public String dosend(MsrVO vo){

	//JY.CTFMS.SEND 集邮发送，财务接收 财务 expctfms 123456
	//CTFMS.JY.SEND 财务发送，集邮接收 集邮 expjy 123456
//	String str1="##begin##bj##000000##xxx##41010000##北京1##1234.00##end##";
//	String str2="##begin##bj##000000##xxx##41010001##北京2##1233.00##end##";
//	String str3="##begin##bj##000000##xxx##41010002##北京3##1235.00##end##";
//	String str4="##begin##bj##000000##xxx##41010003##北京4##1236.00##end##";
//	String str5="##begin##bj##000000##xxx##41010004##北京5##1234.00##end##";
//	
	String str=vo.getCanShuSend();
	String strSend[]=str.split(",");
	int count=vo.getCnaShuCount();
	
	String url=vo.getAddress()+":"+vo.getDuankou();
	String strJmsUser=vo.getUname();
	String strJmsPwd=vo.getUpass();
	String strJmsQuName=vo.getVdef1();
	String jmsMessagePrepertys=vo.getVdef9();
	UFBoolean isDecode=vo.getVdef4();
//	String url="10.3.14.137:7322";
//	String strJmsUser="expctfms";
//	String strJmsPwd="123456";
//	String strJmsQuName="JY.CTFMS.SEND";
	try{
		QueueConnectionFactory factory = new com.tibco.tibjms.TibjmsQueueConnectionFactory(
			url);

		QueueConnection connection = factory.createQueueConnection(
			strJmsUser, strJmsPwd);

		QueueSession session = connection.createQueueSession(false,
			javax.jms.Session.AUTO_ACKNOWLEDGE);

		javax.jms.Queue queue = session.createQueue(strJmsQuName);
		QueueSender sender = session.createSender(queue);

		nc.bs.logging.Logger.debug(new StringBuffer(
				"JMS发送信息开始开始:------------------------"));
		connection.start();
		TextMessage message = null;
		int number=0;
		for(int j=0;j<count;j++){
			for(int i=0;i<strSend.length;i++){
				String enmsg="";
//				如果需要加密，就按照上海邮政提供的加盟方法去加密报文 2013-11-29 
				if(isDecode!=null&&isDecode.booleanValue()){
					enmsg=encode(strSend[i]);
				}else{
					enmsg=strSend[i];
				}
					message = session.createTextMessage(enmsg);//reateTextMessage(expectedBody);
				//TextMessage te=new TibjmsTextMessage();
					if(jmsMessagePrepertys!=null&&jmsMessagePrepertys.trim().length()>0){
						setJMSMessagePrepertys(message, jmsMessagePrepertys);
					}
				sender.send(message);
				number++;
			}
		}
		
		connection.close();
		return"发送完,发送了"+number+"条消息！";
	}catch(Exception e){
		e.printStackTrace();
		return e.getMessage();
	}


	

}
	

public RetMessage dosendListmsg(String pk_msr,List<String> msg,boolean control,String pk_bus,int count,int pagesize,Map<String,Integer> delayedmap ){
	MsrVO vo = null;
	try {
		vo = (MsrVO) getBaseDAO().retrieveByPK(MsrVO.class, pk_msr);
	} catch (DAOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	if(vo==null){
		return new RetMessage(false,"没有找到对应的消息服务器定义");
	}
	UFBoolean isTopic=vo.getVdef10();//是否路由选择
	if(isTopic!=null&&isTopic.booleanValue()){//topic发送
		return  sendEsbListByTopic(pk_msr, msg, control, pk_bus, count, pagesize, vo,delayedmap);
	}else{//队列发送
		return sendEsbListByQueue(pk_msr, msg, control, pk_bus, count, pagesize, vo,delayedmap);
	}
}

public RetMessage sendEsbListByQueue(String pk_msr,List<String> msg,boolean control,String pk_bus,int count,int pagesize,MsrVO vo,Map<String,Integer> delayedmap){
	String url=vo.getAddress()+":"+vo.getDuankou();
	String strJmsUser=vo.getUname();
	String strJmsPwd=vo.getUpass();
	String strJmsQuName=vo.getVdef1();
	String jmsMessagePrepertys=vo.getVdef9();
	UFBoolean isDecode=vo.getVdef4();
	QueueConnectionFactory factory=null;
	QueueConnection connection=null;
	QueueSession session =null;
	javax.jms.Queue queue=null;
	QueueSender sender=null;
	try{
		factory = new com.tibco.tibjms.TibjmsQueueConnectionFactory(
			url);

		connection = factory.createQueueConnection(
			strJmsUser, strJmsPwd);

		session= connection.createQueueSession(false,
			javax.jms.Session.AUTO_ACKNOWLEDGE);

		 queue= session.createQueue(strJmsQuName);
		 sender= session.createSender(queue);

		nc.bs.logging.Logger.debug(new StringBuffer(
				"JMS Queue 发送信息开始开始:------------------------"));
		connection.start();
		TextMessage message = null;
		int number=0;
		boolean beginflag=false;
		boolean endflag=false;
		if(control&&count==0){//如果是发送控制并且还是第一页的时候，发送开始标志
			beginflag=true;
		}
		if(control&&count==pagesize-1){
			endflag=true;
		}
		for(int i=0;i<msg.size();i++){
			//如果有延时 发送顺序。 开始标志   延时b，  数据 延时d 数据 延时d   ，延时e  结束标志
			if(i==msg.size()-1&&endflag){
				//写开始标志。
				ilm.writToDataLog_RequiresNew(pk_bus,IContrastUtil.YWLX_TB,"(Queue)结束标志"+msg.get(i)+"--"+IContrastUtil.DATAPROCESS_HINT);
				number--;
				if(delayedmap!=null&&delayedmap.get("e")!=null){
					Integer endSleep=delayedmap.get("e");
					if(endSleep!=null&&endSleep!=0&&endSleep.intValue()>0){
						ilm.writToDataLog_RequiresNew(pk_bus,IContrastUtil.YWLX_TB,"(Queue)结束延迟"+endSleep+"（秒）--"+IContrastUtil.DATAPROCESS_HINT);
						Thread.sleep(endSleep*1000);
					}
				}
			}
			
			String enmsg="";
			//如果需要加密，就按照上海邮政提供的加盟方法去加密报文 2013-11-29
			if(isDecode!=null&&isDecode.booleanValue()){
				enmsg=encode(msg.get(i));
			}else{
				enmsg=msg.get(i);
			}
			message = session.createTextMessage(enmsg);
			setJMSMessagePrepertys(message, jmsMessagePrepertys);
			sender.send(message);
			number++;
			if(i>0&&i<msg.size()-1&&delayedmap!=null&&delayedmap.get("d")!=null){
				Integer dataSleep=delayedmap.get("d");
				if(dataSleep!=null&&dataSleep!=0&&dataSleep.intValue()>0){
					Thread.sleep(dataSleep*1000);
				}
			}
			
			
			//发送报文不包括开始报文和结束报文
			if(i==0&&beginflag){
				//写开始标志。
				ilm.writToDataLog_RequiresNew(pk_bus,IContrastUtil.YWLX_TB,"(Queue)开始标志"+msg.get(i)+"--"+IContrastUtil.DATAPROCESS_HINT);
				number--;
				if(delayedmap!=null&&delayedmap.get("b")!=null){
					Integer beginSleep=delayedmap.get("b");
					if(beginSleep!=null&&beginSleep!=0&&beginSleep.intValue()>0){
						ilm.writToDataLog_RequiresNew(pk_bus,IContrastUtil.YWLX_TB,"(Queue)开始延迟"+beginSleep+"（秒）--"+IContrastUtil.DATAPROCESS_HINT);
						Thread.sleep(beginSleep*1000);
					}
				}
			}
			
		}
		if(pagesize>1){
			RetMessage ret=new RetMessage();
			ret.setIssucc(true);
			ret.setValue(number);
			return ret;
		}
		return new RetMessage(true,"(Queue)发送成功,总共发送"+number+"条报文");
	}catch(Exception e){
		e.printStackTrace();
		return new RetMessage(false,"(Queue)发送失败");
	}finally{
		if(sender!=null){
			try {
				sender.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(session!=null){
			try {
				session.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(connection!=null){
			try {
				connection.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

public String dosendTopic(MsrVO vo){
	String str=vo.getCanShuSend();
	String strSend[]=str.split(",");
	int count=vo.getCnaShuCount();
	
	String url=vo.getAddress()+":"+vo.getDuankou();
	String strJmsUser=vo.getUname();
	String strJmsPwd=vo.getUpass();
	String topicname=vo.getVdef1();
	String jmsMessagePrepertys=vo.getVdef9();
	TopicConnectionFactory topicFactory=null;
	TopicConnection  topicconection=null;
	TopicSession topicssion=null;
	javax.jms.Topic topic=null;
	javax.jms.TopicPublisher topicpublisher=null;
	UFBoolean isDecode=vo.getVdef4();
	try{
		 topicFactory=new TibjmsTopicConnectionFactory(url);
		 topicconection=topicFactory.createTopicConnection(strJmsUser, strJmsPwd);
		 topicssion= topicconection.createTopicSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
		 topic=topicssion.createTopic(topicname);
		 topicpublisher=topicssion.createPublisher(topic);
		 
		nc.bs.logging.Logger.debug(new StringBuffer(
				"JMS TOPIC  发布信息开始:------------------------"));
		TextMessage message = null;
		int number=0;
		for(int j=0;j<count;j++){
			for(int i=0;i<strSend.length;i++){
				String enmsg="";
//				如果需要加密，就按照上海邮政提供的加盟方法去加密报文 2013-11-29 
				if(isDecode!=null&&isDecode.booleanValue()){
					enmsg=encode(strSend[i]);
				}else{
					enmsg=strSend[i];
				}
					message =topicssion.createTextMessage(enmsg);
					if(jmsMessagePrepertys!=null&&jmsMessagePrepertys.trim().length()>0){
						setJMSMessagePrepertys(message, jmsMessagePrepertys);
//						if(queueDestination.contains(";")){
//							String prepertys[]=queueDestination.split(";");
//							if(prepertys!=null&&prepertys.length>0){
//								for(int w=0;w<prepertys.length;w++){
//									if(prepertys[w]!=null&&prepertys[w].trim().length()>0&&prepertys[w].contains("=")){
//										String prepertysvalue[]=prepertys[w].split("=");
//										if(prepertysvalue!=null&&prepertysvalue.length==2&&prepertysvalue[0]!=null&&prepertysvalue[0].trim().length()>0&&prepertysvalue[1]!=null&&prepertysvalue[1].trim().length()>0){
//											message.setStringProperty(prepertysvalue[0], prepertysvalue[1]);
//										}
//									}
//								}
//							}
//						}else{
//							if(queueDestination.contains("=")){
//								String prepertys[]=queueDestination.split("=");
//								if(prepertys!=null&&prepertys.length==2&&prepertys[0]!=null&&prepertys[0].trim().length()>0&&prepertys[1]!=null&&prepertys[1].trim().length()>0){
//									message.setStringProperty(prepertys[0], prepertys[1]);
//								}
//							}
//						}
						
					}
						topicpublisher.publish(message);
				    number++;
			}
		}
		topicconection.close();
		return"发送完,发送了"+number+"条消息！";
	}catch(Exception e){
		e.printStackTrace();
		return e.getMessage();
	}
}
public RetMessage sendEsbListByTopic(String pk_msr,List<String> msg,boolean control,String pk_bus,int count,int pagesize,MsrVO vo,Map<String,Integer> delayedmap){
	String url=vo.getAddress()+":"+vo.getDuankou();
	String strJmsUser=vo.getUname();
	String strJmsPwd=vo.getUpass();
	String topicname=vo.getVdef1();
	UFBoolean isDecode=vo.getVdef4();//是否加密
	String jmsMessagePrepertys=vo.getVdef9();
	TopicConnectionFactory topicFactory=null;
	TopicConnection  topicconection=null;
	TopicSession topicssion=null;
	javax.jms.Topic topic=null;
	javax.jms.TopicPublisher topicpublisher=null;
	try{
		 topicFactory=new TibjmsTopicConnectionFactory(url);
		  topicconection=topicFactory.createTopicConnection(strJmsUser, strJmsPwd);
		 topicssion= topicconection.createTopicSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
		 topic=topicssion.createTopic(topicname);
		 topicpublisher=topicssion.createPublisher(topic);
		 nc.bs.logging.Logger.debug(new StringBuffer(
		"JMS Topic  发布信息开始:------------------------"));
		TextMessage message = null;
		int number=0;
		boolean beginflag=false;
		boolean endflag=false;
		if(control&&count==0){//如果是发送控制并且还是第一页的时候，发送开始标志
			beginflag=true;
		}
		if(control&&count==pagesize-1){
			endflag=true;
		}
		for(int i=0;i<msg.size();i++){
//			如果有延时 发送顺序。 开始标志   延时b，  数据 延时d 数据 延时d   ，延时e  结束标志
			if(i==msg.size()-1&&endflag){
				//写开始标志。
				ilm.writToDataLog_RequiresNew(pk_bus,IContrastUtil.YWLX_TB,"(Topic)结束标志"+msg.get(i)+"--"+IContrastUtil.DATAPROCESS_HINT);
				number--;
				if(delayedmap!=null&&delayedmap.get("e")!=null){
					Integer endSleep=delayedmap.get("e");
					if(endSleep!=null&&endSleep!=0&&endSleep.intValue()>0){
						ilm.writToDataLog_RequiresNew(pk_bus,IContrastUtil.YWLX_TB,"(Topic)结束延迟"+endSleep+"（秒）--"+IContrastUtil.DATAPROCESS_HINT);
						Thread.sleep(endSleep*1000);
					}
				}
			}
			String enmsg="";
//			如果需要加密，就按照上海邮政提供的加盟方法去加密报文 2013-11-29 
			if(isDecode!=null&&isDecode.booleanValue()){
				enmsg=encode(msg.get(i));
			}else{
				enmsg=msg.get(i);
			}
			message = topicssion.createTextMessage(enmsg);
			setJMSMessagePrepertys(message, jmsMessagePrepertys);
			topicpublisher.send(message);
			number++;
			
			
			if(i>0&&i<msg.size()-1&&delayedmap!=null&&delayedmap.get("d")!=null){
				Integer dataSleep=delayedmap.get("d");
				if(dataSleep!=null&&dataSleep!=0&&dataSleep.intValue()>0){
					Thread.sleep(dataSleep*1000);
				}
			}
			
			
			//发送报文不包括开始报文和结束报文
			if(i==0&&beginflag){
				//写开始标志。
				ilm.writToDataLog_RequiresNew(pk_bus,IContrastUtil.YWLX_TB,"(Topic)开始标志"+msg.get(i)+"--"+IContrastUtil.DATAPROCESS_HINT);
				number--;
				if(delayedmap!=null&&delayedmap.get("b")!=null){
					Integer beginSleep=delayedmap.get("b");
					if(beginSleep!=null&&beginSleep!=0&&beginSleep.intValue()>0){
						ilm.writToDataLog_RequiresNew(pk_bus,IContrastUtil.YWLX_TB,"(Topic)开始延迟"+beginSleep+"（秒）--"+IContrastUtil.DATAPROCESS_HINT);
						Thread.sleep(beginSleep*1000);
					}
				}
			}
			
//			if(i==0&&beginflag){
//				//写开始标志。
//				ilm.writToDataLog_RequiresNew(pk_bus,IContrastUtil.YWLX_TB,"(Topic)开始标志"+msg.get(i)+"--"+IContrastUtil.DATAPROCESS_HINT);
//				number--;
//			}
//			if(i==msg.size()-1&&endflag){
//				//写开始标志。
//				ilm.writToDataLog_RequiresNew(pk_bus,IContrastUtil.YWLX_TB,"(Topic)结束标志"+msg.get(i)+"--"+IContrastUtil.DATAPROCESS_HINT);
//				number--;
//			}
			
		}
		if(pagesize>1){
			RetMessage ret=new RetMessage();
			ret.setIssucc(true);
			ret.setValue(number);
			return ret;
		}
		return new RetMessage(true,"发送成功,(Topic)总共发送"+number+"条报文");
	}catch(Exception e){
		e.printStackTrace();
		return new RetMessage(false,"(TOPIC)发送失败");
	}finally{
		if(topicpublisher!=null){
			try {
				topicpublisher.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(topicssion!=null){
			try {
				topicssion.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(topicconection!=null){
			try {
				topicconection.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}


public String dosend(MsrVO vo,String msg){

//	
	String url=vo.getAddress()+":"+vo.getDuankou();
	String strJmsUser=vo.getUname();
	String strJmsPwd=vo.getUpass();
	String strJmsQuName=vo.getVdef1();
	UFBoolean isDecode=vo.getVdef4();
	try{
		QueueConnectionFactory factory = new com.tibco.tibjms.TibjmsQueueConnectionFactory(
			url);

		QueueConnection connection = factory.createQueueConnection(
			strJmsUser, strJmsPwd);

		QueueSession session = connection.createQueueSession(false,
			javax.jms.Session.AUTO_ACKNOWLEDGE);

		javax.jms.Queue queue = session.createQueue(strJmsQuName);
		QueueSender sender = session.createSender(queue);

		nc.bs.logging.Logger.debug(new StringBuffer(
				"JMS发送信息开始开始:------------------------"));
		connection.start();
		String enmsg="";
//		如果需要加密，就按照上海邮政提供的加盟方法去加密报文 2013-11-29 
		if(isDecode!=null&&isDecode.booleanValue()){
			enmsg=encode(msg);
		}else{
			enmsg=msg;
		}
		TextMessage message = null;
					message = session.createTextMessage(enmsg);
		sender.send(message);
				
		sender.close();
		session.close();
		connection.close();
		return"发送完条消息:"+enmsg;
	}catch(Exception e){
		e.printStackTrace();
		return e.getMessage();
	}


	

}
	public String dorec(MsrVO vo){
		//JY.CTFMS.SEND 集邮发送，财务接收 财务 expctfms 123456
		//CTFMS.JY.SEND 财务发送，集邮接收 集邮 expjy 123456
		String url=vo.getAddress()+":"+vo.getDuankou();
		String strJmsUser=vo.getUname();
		String strJmsPwd=vo.getUpass();
		String strJmsQuName=vo.getVdef2();
//		String url="10.3.14.137:7322";
//		String strJmsUser="expctfms";
//		String strJmsPwd="123456";
//		String strJmsQuName="JY.CTFMS.SEND";
		try{
			QueueConnectionFactory factory = new com.tibco.tibjms.TibjmsQueueConnectionFactory(
				url);

			QueueConnection connection = factory.createQueueConnection(
				strJmsUser, strJmsPwd);

			QueueSession session = connection.createQueueSession(false,
				javax.jms.Session.AUTO_ACKNOWLEDGE);

			javax.jms.Queue queue = session.createQueue(strJmsQuName);
			QueueReceiver receiver = session.createReceiver(queue);
			logger.debug(new StringBuffer("JMS基础信息读取开始:")
			.append("-------------------"));
			nc.bs.logging.Logger.debug(new StringBuffer(
					"JMS接收信息开始开始:------------------------"));
			String strMessage="";
			connection.start();
			TextMessage message = null;
			BytesMessage bytesMessage = null;
			String me="";
		//	for(int i=0;i<5;i++){
			int i=1;
				logger.debug(new StringBuffer("JMS基础信息读取i:"+i+"条信息")
				.append("-------------------"));
				
//				
//				do{
//					//javax.jms.Message m = receiver.receive(10 * 60 * 1000);
//					javax.jms.Message m=receiver.receiveNoWait();
//					if (m != null) {
//						if (m instanceof TextMessage) {
//							message = (TextMessage) m;
//							nc.bs.logging.Logger.debug(new StringBuffer("TextMsg message: ")
//									.append(message.getText()));
//							strMessage=message.getText();
//							me=me+"\n"+i+":t:"+message.getText();
//						} else if (m instanceof BytesMessage) {
//							bytesMessage = (BytesMessage) m;
//
//							int imsgLen = (int) bytesMessage.getBodyLength();
//							byte[] byteArg = new byte[imsgLen];
//
//
//							strMessage = new String(byteArg);
//							strMessage = strMessage.replace("\n", "");
//							nc.bs.logging.Logger.debug(new StringBuffer("BytesMsg message: ")
//									.append(strMessage));
//							nc.bs.logging.Logger.debug(new StringBuffer("BytesMsg message: ")
//							.append(strMessage));
//							me=me+"\n"+i+":b:"+strMessage;
//						}
//						i++;
//						nc.bs.logging.Logger.debug(strMessage);
//						continue;
//					} 
//					else{
//						strMessage="";
//						connection.close();
//						
//					}
//
//				}while(strMessage!= null&&strMessage.length()>0);
				
				for(int kk=0;kk<1;kk++){
					javax.jms.Message m=receiver.receiveNoWait();
					
					if (m instanceof TextMessage) {
						message = (TextMessage) m;
						nc.bs.logging.Logger.debug(new StringBuffer("TextMsg message: ")
								.append(message.getText()));
						strMessage=message.getText();
						me=me+"\n"+i+":t:"+message.getText();
					} else if (m instanceof BytesMessage) {
						bytesMessage = (BytesMessage) m;

						int imsgLen = (int) bytesMessage.getBodyLength();
						byte[] byteArg = new byte[imsgLen];


						strMessage = new String(byteArg);
						strMessage = strMessage.replace("\n", "");
						nc.bs.logging.Logger.debug(new StringBuffer("BytesMsg message: ")
								.append(strMessage));
						nc.bs.logging.Logger.debug(new StringBuffer("BytesMsg message: ")
						.append(strMessage));
						me=me+"\n"+i+":b:"+strMessage;
					}
					
					
					
				}
				
				
				connection.close();
				return "成功！"+me;
						//	}
			
		}catch(Exception e){
			e.printStackTrace();
			return "接收失败！"+"\n"+e.getMessage()+"\n"+e.getStackTrace()[0]+"\n"+e.getStackTrace()[1];
		}
	}
	/**
	 * 给消息message增加消息流属性
	 * @param message
	 * @param jmsMessagePrepertys
	 * @throws JMSException
	 */
	public void setJMSMessagePrepertys(Message message,String jmsMessagePrepertys) throws JMSException{
		if(jmsMessagePrepertys==null||jmsMessagePrepertys.trim().length()<=0){
			return;
		}
		if(jmsMessagePrepertys.contains(";")){
			String prepertys[]=jmsMessagePrepertys.split(";");
			if(prepertys!=null&&prepertys.length>0){
				for(int w=0;w<prepertys.length;w++){
					if(prepertys[w]!=null&&prepertys[w].trim().length()>0&&prepertys[w].contains("=")){
						String prepertysvalue[]=prepertys[w].split("=");
						if(prepertysvalue!=null&&prepertysvalue.length==2&&prepertysvalue[0]!=null&&prepertysvalue[0].trim().length()>0&&prepertysvalue[1]!=null&&prepertysvalue[1].trim().length()>0){
							message.setStringProperty(prepertysvalue[0], prepertysvalue[1]);
						}
					}
				}
			}
		}else{
			if(jmsMessagePrepertys.contains("=")){
				String prepertys[]=jmsMessagePrepertys.split("=");
				if(prepertys!=null&&prepertys.length==2&&prepertys[0]!=null&&prepertys[0].trim().length()>0&&prepertys[1]!=null&&prepertys[1].trim().length()>0){
					message.setStringProperty(prepertys[0], prepertys[1]);
				}
			}
		}
	}
	/**
	 * 上海邮政提供的加密方法
	 * @param message
	 * @return
	 */
	public String encode(String message){
		String demessage=DesUtil.encrypt(message);
		return demessage==null?"":demessage;
	}
	
}
