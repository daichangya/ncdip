package nc.impl.dip.optByPluginSrv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.dip.in.ValueTranslator;
import nc.bs.dip.pub.ResvDataSrv;
import nc.bs.dip.txt.TxtDataVO;
import nc.bs.excel.pub.ExpExcelVO;
import nc.bs.excel.pub.ExpToExcel;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.logging.Logger;
import nc.impl.dip.pub.DataReceiverJMSImpl;
import nc.impl.dip.pub.DealFile;
import nc.impl.dip.pub.EsbMapUtilVO;
import nc.impl.dip.pub.PropUtilVO;
import nc.impl.dip.pub.QueryFieldImpl;
import nc.impl.webservice.client57.IServiceInputDataRecLocator;
import nc.impl.webservice.client57.IServiceInputDataRecSOAP11BindingStub;
import nc.itf.dip.optByPluginSrv.IOptByPlg;
import nc.itf.dip.pub.IDipLogger;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.itf.dip.pub.IQueryField;
import nc.itf.dip.pub.ITaskExecute;
import nc.itf.dip.pub.checkplugin.ICheckPlugin;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.generator.SequenceGenerator;
import nc.pub.dip.startup.ThreadUtil;
import nc.servlet.dip.pub.DipServletUtil;
import nc.servlet.dip.pub.SynDataUtil;
import nc.ui.dip.datarec.formatedlg.DataformatPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.util.dip.sj.CheckMessage;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.RetMessage;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.dataformatdef.DataformatdefBVO;
import nc.vo.dip.dataformatdef.DataformatdefHVO;
import nc.vo.dip.dataorigin.DipDataoriginVO;
import nc.vo.dip.datarec.DipDatarecHVO;
import nc.vo.dip.datarec.DipDatarecSpecialTab;
import nc.vo.dip.datarecD.ArgDataRecDVO;
import nc.vo.dip.datasynch.DipDatasynchVO;
import nc.vo.dip.dataverify.DataverifyBVO;
import nc.vo.dip.dataverify.DataverifyHVO;
import nc.vo.dip.dateprocess.DateprocessVO;
import nc.vo.dip.dbcontype.DbcontypeVO;
import nc.vo.dip.in.RowDataVO;
import nc.vo.dip.message.MsrVO;
import nc.vo.dip.repeatdata.DipRepeatdataVO;
import nc.vo.dip.runsys.DipRunsysBVO;
import nc.vo.dip.sysregister.DipSysregisterBVO;
import nc.vo.dip.sysregister.DipSysregisterHVO;
import nc.vo.dip.util.DipCountVO;
import nc.vo.dip.util.DipSuccOrFailClass;
import nc.vo.dip.view.VDipDsreceiveVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;


public class OptByPlgImpl implements IOptByPlg {
	private BaseDAO bd = null;

	private BaseDAO getBaseDAO() {
		if (bd == null) {
			bd = new BaseDAO(IContrastUtil.DESIGN_CON);
		}
		return bd;
	}

	IQueryField query;
	IDipLogger diplog=(IDipLogger) NCLocator.getInstance().lookup(IDipLogger.class.getName());
	ResvDataSrv rds = new ResvDataSrv();

	// 值转换
	ValueTranslator vtv = new ValueTranslator();

	public OptByPlgImpl() {
		try {
			query = new QueryFieldImpl();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	ILogAndTabMonitorSys ilm = (ILogAndTabMonitorSys) NCLocator.getInstance().lookup(ILogAndTabMonitorSys.class.getName());

	/**
	 * @desc 得到消息后再去找对应的格式来进行解析(非消息总线) 数据同步，
	 * 
	 * @param hpk
	 *            数据同步pk
	 * @throws Exception 
	 */
	public RetMessage msgToStyle(String hpk,List<String> errlist) throws Exception {
		RetMessage m = new RetMessage();
		m.setIssucc(true);
		if (null == hpk || hpk.length() <= 0) {
			return new RetMessage(false, "没有传入相应的数据同步主键！",errlist);
		} else {
			/*
			 * <非消息总线格式无标志头、标志尾、及系统站点标志等只有数据> 1、调用相应方法拿到消息数据；(格式文件时才需要解析)
			 * 2、根据接收到的数据同步pk拿到数据接收格式定义的相关数据（非消息总线的相应格式及对应的表）；
			 * 3、根据数据格式定义的数据格式去解析消息数据，并将数据存放到对应的表中。
			 */
			// 数据接收定义主vo
			// 数据接收定义名称:存的主键
			String dataname = null;
			// 数据来源类型:存放的是pk
			String sourcetype = null;
			// 数据来源类型名称
			String sourcetypename = null;
			String file = null;// 数据来源连接，存放的是pk
			String filename = null;// 数据来源连接名称
			//liyunzhe 如果是中间表和webservice抓取的时候要传入ip start
			String ip=hpk.contains(",")==true?hpk.split(",")[1]:"";
			hpk=hpk.contains(",")==true?hpk.split(",")[0]:hpk;
			//liyunzhe 如果是中间表的时候要传入ip end
			
			DipDatasynchVO syshvo=(DipDatasynchVO) getBaseDAO().retrieveByPK(DipDatasynchVO.class, hpk);
			dataname = syshvo.getDataname();// 存放的是主键
			DipDatarecHVO dataRecHvo=(DipDatarecHVO) getBaseDAO().retrieveByPK(DipDatarecHVO.class, dataname);
			sourcetype=dataRecHvo.getSourcetype();
			/*liyunzhe modify 把数据来源类型比较由名称改成pk主键比较 start 2012-06-05*/
			if(sourcetype==null||sourcetype.trim().equals("")){
				return new RetMessage(false,"没有找到对应数据来源类型！");
			}
			DipDataoriginVO ovo=(DipDataoriginVO) getBaseDAO().retrieveByPK(DipDataoriginVO.class, sourcetype);
			if(ovo==null){
				return new RetMessage(false,"没有找到对应数据来源类型！");
			}
			//sourcetypename = ovo.getName();//query.queryfield(sql3);// 来源类型名称
			sourcetypename=sourcetype;
			
//			0001AA10000000021R3Y    格式文件
//			0001AA1000000001NY7F    中间表
//			0001AA10000000013SVH    主动抓取
//			0001AA10000000013SVI    消息总线
//			0001AA1000000003493X    webservice抓取
//			IContrastUtil.DATAORIGIN_GSWJ;
			if(dataRecHvo.getIoflag()==null||dataRecHvo.getIoflag().equals("输入")){
//				if ("主动抓取".equals(sourcetypename)) {
				if (IContrastUtil.DATAORIGIN_ZDZQ.equals(sourcetypename)) {
					file = dataRecHvo.getSourcecon();// 存放的pk
					if (file != null && !"".equals(file)) {
						String sql4 = "select descriptions from dip_dataurl where pk_dataurl='" + file + "' and nvl(dr,0)=0";
						filename = query.queryfield(sql4);// 数据来源连接名称
					}
					SynDataUtil util = new SynDataUtil();
				//	Logger.debug("数据同步开始\n");
				    return	util.resAllDataByPages(hpk,dataRecHvo,filename, dataRecHvo.getDataname(),dataRecHvo.getMemorytable(),dataRecHvo.getPrimaryKey());
//				} else if ("格式文件".equals(sourcetypename)) {
				} else if (IContrastUtil.DATAORIGIN_GSWJ.equals(sourcetypename)) {
					return sysDateByFile(hpk, dataname,errlist);
//				} else if ("中间表".equals(sourcetypename)) {
				} else if (IContrastUtil.DATAORIGIN_ZJB.equals(sourcetypename)) {
//					RetMessage retmessage=validateZJBSource(hpk,ip);
//					if(!retmessage.getIssucc()){
//					   return retmessage;	
//					}
					return midTable(dataRecHvo,hpk,errlist);
//				} else if("消息总线".equals(sourcetypename)){
				} else if(IContrastUtil.DATAORIGIN_XXZX.equals(sourcetypename)){
					if(dataRecHvo!=null&&dataRecHvo.getDatabakfile()!=null&&dataRecHvo.getDatabakfile().equals("消息流")){
						return esbMessageToTable(dataRecHvo,hpk,errlist,syshvo);
					}else{
					//	RetMessage ret=esbFileToTable(dataRecHvo,hpk,errlist);
						RetMessage ret=esbMessageToTable(dataRecHvo,hpk,errlist,syshvo);
						if(ret!=null&&ret.getIssucc()){
							ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_TB, ret.getMessage()+"--"+IContrastUtil.DATAPROCESS_HINT);
							return esbFileToTable(dataRecHvo,hpk,errlist);	
						}else{
							return ret;
						}
					}
					
//				}else if("webservice抓取".equals(sourcetypename)){
				}else if(IContrastUtil.DATAORIGIN_WEBSERVICEZQ.equals(sourcetypename)){
					return webserviceInput(dataRecHvo,hpk,errlist,ip);
				}else {
					m = new RetMessage(false, "没有找到相应的数据来源类型！【" + sourcetype + ":"
							+ sourcetypename + "】");
				}
			}else{
//				if("格式文件".equals(sourcetypename)){
				if(IContrastUtil.DATAORIGIN_GSWJ.equals(sourcetypename)){
					return dataToFile(hpk, dataname, errlist);
//				}else if("中间表".equals(sourcetypename)){
				}else if(IContrastUtil.DATAORIGIN_ZJB.equals(sourcetypename)){
//					RetMessage retmessage=validateZJBSource(hpk,ip);
//					if(!retmessage.getIssucc()){
//					   return retmessage;	
//					}
					return writeTomidTable(dataRecHvo,hpk,errlist);
//				}else if("消息总线".equals(sourcetypename)){
				}else if(IContrastUtil.DATAORIGIN_XXZX.equals(sourcetypename)){
					return writeToESBFile(dataRecHvo,hpk,errlist);
				}else{
					m = new RetMessage(false, "没有找到相应的数据来源类型！【" + sourcetype + ":"
							+ sourcetypename + "】");
				}
			}
			/*liyunzhe modify 把数据来源类型比较由名称改成pk主键比较 end 2012-06-05*/
		}
		return m;

	}
	/**校验中间表的资源
	 * 同步处理主键
	 * @param hpk
	 * @return
	 */
	private RetMessage validateZJBSource(String hpk,String ip){
		DipDatarecHVO datahvo=null;
		try {
			String pk=iqf.queryfield(" select dataname from Dip_Datasynch where pk_datasynch='"+hpk+"' and nvl(dr,0)=0 ");
			datahvo=(DipDatarecHVO) iqf.querySupervoByPk(DipDatarecHVO.class, pk);
//			datarecHvoArry=(DipDatarecHVO[]) HYPubBO_Client.queryByCondition(DipDatarecHVO.class, " pk_datarec_h=( select dataname from Dip_Datasynch where pk_datasynch='"+hpk+"' and nvl(dr,0)=0 ) and nvl(dr,0)=0 ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(datahvo==null){
			return new RetMessage(false,"没有找到对应的同步定义");
		}else{
			if(IContrastUtil.DATAORIGIN_ZJB.equals(datahvo.getSourcetype())){//去校验ip，用户名，资源是否可以访问
				if(datahvo.getSourceparam()!=null&&datahvo.getSourceparam().split(",").length>=3){
					String datasource=datahvo.getSourceparam().split(",")[0];
					String usercode=datahvo.getSourceparam().split(",")[1];
					String tablename=datahvo.getSourceparam().split(",")[2];
//					try {
//						ServerEnvironment sf=SFServiceFacility.getServiceProviderService().getServerEnv();
//						sf.getLoginUsers();
//					} catch (BusinessException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					Object result=DipServletUtil.validate(datasource, ip, usercode);
					if(result instanceof Boolean){
						Boolean flag=(Boolean) result;
						if(flag){
							if(!DipServletUtil.validateResources(usercode, datasource, DipServletUtil.DIP_ROLEANDTABLE_B, tablename)){
								return new RetMessage(false,usercode+"没有表"+tablename+"访问权限");
							}
						}
					}else{
						return new RetMessage(false,result.toString());
					}
				}else{
					
					return new RetMessage(false,"同步定义数据来源参数错误");
				}
				
			}
		}
		return new RetMessage(true,"校验通过");
	}
	
	
	private RetMessage webserviceCatch(DipDatarecHVO dataRecHvo, String hpk, List<String> errlist) {
		String sReturn = "";
		String urlstr="";
		String fields="";//需要查询的字段。
		try {
			urlstr=iqf.queryfield("SELECT url FROM dip_sourceregist where pk_sourceregist='"+dataRecHvo.getSourcecon()+"' and nvl(dr,0)=0");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(urlstr==null||urlstr.length()<=0){
			return new RetMessage(false,"没有找到webservice链接");
		}
		String phyname = dataRecHvo.getDataname();// 物理表名
		HashMap<String, String> map = new HashMap<String, String>();
		DataformatdefHVO fhvo = null;
		List<DataformatdefHVO> listfhvo = null;
		try {// 数据格式定义表头
			listfhvo=(List<DataformatdefHVO>) getBaseDAO().retrieveByClause(DataformatdefHVO.class, "pk_datarec_h='" + dataRecHvo.getPrimaryKey() + "' and nvl(dr,0)=0");
		} catch (Exception e2) {
			e2.printStackTrace();
			return new RetMessage(false, "查询相应的数据格式定义错误！" + e2.getMessage());
		}
		Hashtable<String, DipDatadefinitBVO> hashTable = new Hashtable<String, DipDatadefinitBVO>();
		if (listfhvo == null||listfhvo.size()<=0) {
			return new RetMessage(false, "没有找到相应的格式定义！");
		} else {
			fhvo=listfhvo.get(0);
			List<DataformatdefBVO> fbvo = null;
			try {
				fbvo = (List<DataformatdefBVO>) getBaseDAO() .retrieveByClause( DataformatdefBVO.class, "pk_dataformatdef_h='"  + fhvo.getPrimaryKey() + "' and datakind is not null and vdef2='业务数据' and nvl(dr,0)=0 order by code");
			} catch (DAOException e2) {
				e2.printStackTrace();
			}
			if (fbvo == null || fbvo.size() <= 0) {// 数据格式定义表体
				return new RetMessage(false, "没有找到相应的格式定义的格式！");
			}
			// 0：顺序，1：字段对照
			Integer datamaptye = Integer.parseInt(fhvo.getMessflowdef());// 是顺序对照还是字段对照
			for (DataformatdefBVO dfbvo : fbvo) {// 对照的map
				if (dfbvo.getDatakind() != null && !"".equals(dfbvo.getDatakind())) {
					if (datamaptye == 0) {
						map.put(dfbvo.getDatakind(), dfbvo.getDatakind());
					} else {
						map.put(dfbvo.getCorreskind(), dfbvo.getDatakind());
					}
				}
			}
			datamaptye=1;
			dataRecHvo.setAttributeValue("datamaptype", datamaptye);
			dataRecHvo.setAttributeValue("map", map);
			dataRecHvo.setAttributeValue("tablename", phyname);
			String memorytable = dataRecHvo.getMemorytable();
			// 2、查数据定义
			List<DipDatadefinitBVO> dataDefinitBvo = null;
			try {// 数据定义表体VO
				dataDefinitBvo = (List<DipDatadefinitBVO>) getBaseDAO().retrieveByClause( DipDatadefinitBVO.class, "pk_datadefinit_h='" + memorytable + "' and nvl(dr,0)=0");
			} catch (DAOException e1) {
				e1.printStackTrace();
			}
			if (dataDefinitBvo == null || dataDefinitBvo.size() <= 0) {
				return new RetMessage(false, "数据定义中没有找到表的结构定义！");
			}
			for (DipDatadefinitBVO bvo : dataDefinitBvo) {
				hashTable.put(bvo.getEname(), bvo);
				if (bvo.getIspk() != null && bvo.getIspk().booleanValue()) {
					hashTable.put("#PKFIELD#", bvo);
				}
			}
			// 3、找文件位置

			//liyunzhe add 增加查询的字段。
			SynDataUtil syndataUtil=new SynDataUtil();
			RetMessage retmessage=syndataUtil.getOneDataFormatdefHFields(dataRecHvo);
			if(!retmessage.getIssucc()){
				return retmessage;
			}
			 fields=retmessage.getMessage();
		}
		try {
//			URL url = new URL("http://127.0.0.1:8008/axis/services/IServiceInputDataRec");//502的URL
//			URL url = new URL("http://127.0.0.1/uapws/service/IServiceInputDataRec");//57的URL
			URL url=new URL(urlstr);
			IServiceInputDataRecSOAP11BindingStub bi=(IServiceInputDataRecSOAP11BindingStub) new IServiceInputDataRecLocator().getIServiceInputDataRecSOAP11port_http(url);
		    sReturn=bi.callData(dataRecHvo.getSourceparam()+"|"+fields);
		} catch (Exception e) {
			e.printStackTrace();
			return new RetMessage(false,"连接webservice失败："+e.getMessage());
		}
		if(sReturn==null||sReturn.length()<=0){
			return new RetMessage(false,"返回的信息为空");
		}
		Document oTmpDom =null;
		try {
			oTmpDom = DocumentHelper.parseText(sReturn);
		} catch (DocumentException e) {
			e.printStackTrace();
			return new RetMessage(false,"返回的信息解析失败！"+sReturn);
		}
		String strTmp = "//data";
		try{
			String retcode =(String) oTmpDom.selectSingleNode("//head/retcode").getText();
			String retmsg=(String) oTmpDom.selectSingleNode("//head/retmsg").getText();
			if(retcode.equals("1")){
				return new RetMessage(false,"失败，webservice返回信息："+retmsg);
			}
			String retcount=(String) oTmpDom.selectSingleNode("root/head/retcount").getText();
			if(retcount==null||retcount.equals("0")){
				return new RetMessage(true,"没有查询到数据！");
			}
			String bq=(String) oTmpDom.selectSingleNode("root/head/rettag").getText();
			TxtDataVO tvo = new TxtDataVO();
	
			tvo.setStartCol(0);
			tvo.setRowCount(Integer.parseInt(retcount));// 去掉标题行
			String[] title = bq.split(",");
			tvo.setColCount(title.length);
			tvo.setFirstDataRow(1);
			tvo.setFirstDataCol(0);
			HashMap<String, String> titlemap = new HashMap<String, String>();
			for (short i = 0; i < title.length; i++) {
				titlemap.put((i)+"", title[i]);
			}
			tvo.setTitlemap(titlemap);
			tvo.setFieldName(title);
			int i=1;
			Node oNodeSrc=null;
			for (Iterator iter = oTmpDom.selectNodes(strTmp).iterator(); iter
					.hasNext();) {
				oNodeSrc=(Node) iter.next();
				String[] val = oNodeSrc.getText().split(",");
				for (short j = 1; j <= val.length; j++) {
					tvo.setCellData(i - 1, j-1, val[j-1]);
				}
				i++;
			}
			return inertTxtDataVO(hpk, tvo, dataRecHvo, hashTable,null,errlist);
		}catch (Exception e){
			e.printStackTrace();
			return new RetMessage(false,e.getMessage());
		}
	}
	
	public RetMessage webserviceInput(DipDatarecHVO dataRecHvo, String hpk, List<String> errlist,String ip){
//		数据源，用户名，表名，字段名，ip，第几页，每页多少数据
        //datasource,usercode,tablename,pk|code|name,ip,5,1000
		String sReturn = "";
		String urlstr="";
		String fields="";//需要查询的字段。
		int currentpage=0;
		int pagesize=IContrastUtil.DATAORIGIN_WEBSERVICEZQ_PAGESIZE;
		try {
			urlstr=iqf.queryfield("SELECT url FROM dip_sourceregist where pk_sourceregist='"+dataRecHvo.getSourcecon()+"' and nvl(dr,0)=0");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(urlstr==null||urlstr.length()<=0){
			return new RetMessage(false,"没有找到webservice链接");
		}
		String phyname = dataRecHvo.getDataname();// 物理表名
		HashMap<String, String> map = new HashMap<String, String>();
		DataformatdefHVO fhvo = null;
		List<DataformatdefHVO> listfhvo = null;
		try {// 数据格式定义表头
			listfhvo=(List<DataformatdefHVO>) getBaseDAO().retrieveByClause(DataformatdefHVO.class, "pk_datarec_h='" + dataRecHvo.getPrimaryKey() + "' and nvl(dr,0)=0");
		} catch (Exception e2) {
			e2.printStackTrace();
			return new RetMessage(false, "查询相应的数据格式定义错误！" + e2.getMessage());
		}
		Hashtable<String, DipDatadefinitBVO> hashTable = new Hashtable<String, DipDatadefinitBVO>();
		if (listfhvo == null||listfhvo.size()<=0) {
			return new RetMessage(false, "没有找到相应的格式定义！");
		} else {
			fhvo=listfhvo.get(0);
			List<DataformatdefBVO> fbvo = null;
			try {
				fbvo = (List<DataformatdefBVO>) getBaseDAO() .retrieveByClause( DataformatdefBVO.class, "pk_dataformatdef_h='"  + fhvo.getPrimaryKey() + "' and datakind is not null and vdef2='业务数据' and nvl(dr,0)=0 order by code");
			} catch (DAOException e2) {
				e2.printStackTrace();
			}
			if (fbvo == null || fbvo.size() <= 0) {// 数据格式定义表体
				return new RetMessage(false, "没有找到相应的格式定义的格式！");
			}
			// 0：顺序，1：字段对照
			Integer datamaptye = Integer.parseInt(fhvo.getMessflowdef());// 是顺序对照还是字段对照
			for (DataformatdefBVO dfbvo : fbvo) {// 对照的map
				if (dfbvo.getDatakind() != null && !"".equals(dfbvo.getDatakind())) {
					if (datamaptye == 0) {
						map.put(dfbvo.getDatakind(), dfbvo.getDatakind());
						fields=fields+dfbvo.getDatakind()+"|";
					} else {
						map.put(dfbvo.getCorreskind(), dfbvo.getDatakind());
						fields=fields+dfbvo.getCorreskind()+"|";
					}
				}
			}
//			datamaptye=1;
			dataRecHvo.setAttributeValue("datamaptype", 1);
			dataRecHvo.setAttributeValue("map", map);
			dataRecHvo.setAttributeValue("tablename", phyname);
			String memorytable = dataRecHvo.getMemorytable();
			// 2、查数据定义
			List<DipDatadefinitBVO> dataDefinitBvo = null;
			try {// 数据定义表体VO
				dataDefinitBvo = (List<DipDatadefinitBVO>) getBaseDAO().retrieveByClause( DipDatadefinitBVO.class, "pk_datadefinit_h='" + memorytable + "' and nvl(dr,0)=0");
			} catch (DAOException e1) {
				e1.printStackTrace();
			}
			if (dataDefinitBvo == null || dataDefinitBvo.size() <= 0) {
				return new RetMessage(false, "数据定义中没有找到表的结构定义！");
			}
			for (DipDatadefinitBVO bvo : dataDefinitBvo) {
				hashTable.put(bvo.getEname(), bvo);
				if (bvo.getIspk() != null && bvo.getIspk().booleanValue()) {
					hashTable.put("#PKFIELD#", bvo);
				}
			}
		}
		boolean flag=true;
          while(flag){
        	  try {
//			URL url = new URL("http://127.0.0.1:8008/axis/services/IServiceInputDataRec");//502的URL
//			URL url = new URL("http://127.0.0.1/uapws/service/IServiceInputDataRec");//57的URL
        		  URL url=new URL(urlstr);
        		  IServiceInputDataRecSOAP11BindingStub bi=(IServiceInputDataRecSOAP11BindingStub) new IServiceInputDataRecLocator().getIServiceInputDataRecSOAP11port_http(url);
//			数据源，用户名，表名，字段名，ip，第几页，每页多少数据
        		  //datasource,usercode,tablename,pk|code|name,ip,5,1000
        		  
        	  
        	  sReturn=bi.callData(dataRecHvo.getSourceparam()+","+fields.substring(0, fields.length()-1)+","+ip+","+currentpage+","+pagesize);
        	  currentpage++;
          } catch (Exception e) {
        	  e.printStackTrace();
        	  return new RetMessage(false,"连接webservice失败："+e.getMessage());
          }
          if(sReturn==null||sReturn.length()<=0){
        	  return new RetMessage(false,"返回的信息为空");
          }
          Document oTmpDom =null;
          try {
        	  oTmpDom = DocumentHelper.parseText(sReturn);
          } catch (DocumentException e) {
        	  e.printStackTrace();
        	  return new RetMessage(false,"返回的信息解析失败！"+sReturn);
          }
          String strTmp = "//data";
          try{
        	  String retcode =(String) oTmpDom.selectSingleNode("//head/retcode").getText();
        	  String retmsg=(String) oTmpDom.selectSingleNode("//head/retmsg").getText();
        	  if(retcode.equals("1")){
        		  return new RetMessage(false,"失败，webservice返回信息："+retmsg);
        	  }
        	  String retcount=(String) oTmpDom.selectSingleNode("root/head/retcount").getText();
        	  int retcountint=Integer.parseInt(retcount);
        	  if(retcountint==0){
        		  flag=false;
        		  if(currentpage==0){
        			  return new RetMessage(true,"没有查询到数据！");
        		  }else{
        			  return new RetMessage(true,"webservice抓取成功");
        		  }
        	  }else{
        		  if(retcountint<pagesize){
        			  flag=false;
        			  //结束
        		  }
        		  String bq=(String) oTmpDom.selectSingleNode("root/head/rettag").getText();
        		  TxtDataVO tvo = new TxtDataVO();
        		  tvo.setStartCol(0);
        		  tvo.setRowCount(Integer.parseInt(retcount));// 去掉标题行
        		  String[] title = bq.split(",");
        		  tvo.setColCount(title.length);
        		  tvo.setFirstDataRow(1);
        		  tvo.setFirstDataCol(0);
        		  HashMap<String, String> titlemap = new HashMap<String, String>();
        		  for (short i = 0; i < title.length; i++) {
        			  titlemap.put((i)+"", title[i]);
        		  }
        		  tvo.setTitlemap(titlemap);
        		  tvo.setFieldName(title);
        		  int i=1;
        		  Node oNodeSrc=null;
        		  for (Iterator iter = oTmpDom.selectNodes(strTmp).iterator(); iter
        		  .hasNext();) {
        			  oNodeSrc=(Node) iter.next();
        			  String[] val = oNodeSrc.getText().split(",");
        			  for (short j = 1; j <= val.length; j++) {
        				  tvo.setCellData(i - 1, j-1, val[j-1]);
        			  }
        			  i++;
        		  }
        		  RetMessage ret= inertTxtDataVO(hpk, tvo, dataRecHvo, hashTable,null,errlist);
        		  
        		  if(errlist!=null&&errlist.size()>0){
        			  List list=new ArrayList();
        			  for(int w=0;w<errlist.size();w++){
        				  list.add("同步第"+currentpage+"页，每页"+pagesize+"条数据："+errlist.get(w));
        			  }
        			  ilm.writToDataLog_RequiresNew(hpk,IContrastUtil.YWLX_TB,list);
        		  }
        		  if(ret.getIssucc()){
        			  ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_TB, "同步第"+currentpage+"页，每页"+pagesize+"条数据："+ret.getMessage()+"--"+IContrastUtil.DATAPROCESS_HINT);
        		  }else{
        			  ilm.writToDataLog_RequiresNew(hpk, IContrastUtil.YWLX_TB, "同步第"+currentpage+"页，每页"+pagesize+"条数据："+ret.getMessage());	
        		  }
        		  errlist=new ArrayList<String>();
        	  }
        	  
          }catch (Exception e){
        	  e.printStackTrace();
        	  return new RetMessage(false,e.getMessage());
          }
         }
		return new RetMessage(true,"webservice抓取成功");
	}
	/**
	 * @desc ESB传过来的文件
	 * @param hpk 数据同步的主键 
	 * @param datanme 数据接收定义
	 * */
public RetMessage esbFileToTable(DipDatarecHVO vo,String hpk,List<String> errlist){
	
	if (null != vo) {
		// 0：顺序，1：字段对照
		// 数据字段
		try {
			
			String phyname = vo.getDataname();// 物理表名
			// 数据来源连接(dataSourse)
			String sourceurl = vo.getSourcecon();
			//流类型
			
			//传输控制标志
			UFBoolean trancon=vo.getTrancon()==null?new UFBoolean(false):new UFBoolean(vo.getTrancon());
			
			// 数据来源参数(表名)
			String filname=vo.getSourceparam();
			String filpathsql="select descriptions from dip_filepath where pk_filepath=(select msr.savefilepath from dip_msr msr where msr.pk_dip_msr='"+vo.getSourcecon()+"')";
			try {
				filpathsql=iqf.queryfield(filpathsql);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (DbException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(filpathsql==null||filpathsql.length()<=0){
				return new RetMessage(false,"没有找到生成文件的路径");
			}
			// 存储表名
			String memorytable = vo.getMemorytable();
			Map<String,DataformatdefHVO> fhmap=new HashMap<String, DataformatdefHVO>(); 
			Map<String,List<DataformatdefBVO>> fbmap=new HashMap<String,List<DataformatdefBVO>>(); 
			List<DataformatdefHVO> listfhvo=(List<DataformatdefHVO>) getBaseDAO().retrieveByClause(DataformatdefHVO.class, "pk_datarec_h='" + vo.getPk_datarec_h() + "' and nvl(dr,0)=0");
			if(listfhvo==null||listfhvo.size()<1){
				return new RetMessage(false, "没有找到相应的格式定义的格式！");
			}
			DipSysregisterHVO xt=(DipSysregisterHVO) getBaseDAO().retrieveByPK(DipSysregisterHVO.class, vo.getPk_xt());
			if(xt==null){
				return new RetMessage(false, "没有找到相应的系统！");
			}
			DipDatadefinitHVO ddfhvo=(DipDatadefinitHVO) getBaseDAO().retrieveByPK(DipDatadefinitHVO.class, memorytable);
			if(ddfhvo==null){
				return new RetMessage(false, "没有找到相应的数据定义！");
			}
			String pkfiled=null;
			try {
				pkfiled=iqf.queryfield("select ENAME from dip_datadefinit_b where dip_datadefinit_b.pk_datadefinit_h='"+memorytable+"' and nvl(dr,0)=0 and ( dip_datadefinit_b.issyspk='Y' or dip_datadefinit_b.ispk='Y')");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			//是否覆盖重复数据
//			boolean isfg=pkfiled!=null&&vo.getRepeatdata().equals("0001ZZ10000000018K6M")?true:false;
			boolean isfg=pkfiled!=null&&vo.getRepeatdata().equals(IContrastUtil.REPEATDATA_CONTROL_FUGAI)?true:false;
			int pkfileindex=-1;
			String xtbz=xt.getExtcode();//系统标志
			String zdzj=ddfhvo.getDeploycode();
			String zdbz="";//站点标志
			if(zdzj==null||zdzj.length()<=0){
				zdbz=xt.getDef_str_1();
			}else{
				DipSysregisterBVO sbvo=(DipSysregisterBVO) getBaseDAO().retrieveByPK(DipSysregisterBVO.class, zdzj);
				if(sbvo==null){
					return new RetMessage(false, "没有找到相应的站点！");
				}
				zdbz=sbvo.getSitecode();
			}
			int fromcount=0;
			if(trancon.booleanValue()){
				if(listfhvo.size()<3){
					return new RetMessage(false, "格式定义不完整！");
				}
				for(DataformatdefHVO hvo:listfhvo){
					if(hvo.getMessflowtype().equals("0001AA100000000142YS")){
						fhmap.put("d", hvo);
						List<DataformatdefBVO> listbvos=(List<DataformatdefBVO>) getBaseDAO().retrieveByClause(DataformatdefBVO.class, "pk_dataformatdef_h='"+hvo.getPrimaryKey()+"' order by code");
						if(listbvos==null||listbvos.size()<=0){
							return new RetMessage(false, "没有找到相应的格式定义的格式！");
						}else{
							fbmap.put("d", listbvos);
						}
					}else if(hvo.getMessflowtype().equals("0001AA100000000142YQ")){
						fhmap.put("b", hvo);
						List<DataformatdefBVO> listbvos=(List<DataformatdefBVO>) getBaseDAO().retrieveByClause(DataformatdefBVO.class, "pk_dataformatdef_h='"+hvo.getPrimaryKey()+"' order by code");
						if(listbvos==null||listbvos.size()<=0){
							return new RetMessage(false, "没有找到相应的格式定义的格式！");
						}else{
							fromcount=listbvos.size();
						}
					}else if(hvo.getMessflowtype().equals("0001ZZ10000000018K7M")){
						fhmap.put("e", hvo);
						List<DataformatdefBVO> listbvos=(List<DataformatdefBVO>) getBaseDAO().retrieveByClause(DataformatdefBVO.class, "pk_dataformatdef_h='"+hvo.getPrimaryKey()+"' order by code");
						if(listbvos==null||listbvos.size()<=0){
							return new RetMessage(false, "没有找到相应的格式定义的格式！");
						}
					}
					if(!fbmap.containsKey("b")||!fbmap.containsKey("d")||!fbmap.containsKey("e")){
						return new RetMessage(false, "格式定义不完整！");
					}
				}
				
			}else{
				for(DataformatdefHVO hvo:listfhvo){
					if(hvo.getMessflowtype().equals("0001AA100000000142YS")){
						fhmap.put("d", hvo);
						List<DataformatdefBVO> listbvos=(List<DataformatdefBVO>) getBaseDAO().retrieveByClause(DataformatdefBVO.class, "pk_dataformatdef_h='"+hvo.getPrimaryKey()+"' order by code");
						if(listbvos==null||listbvos.size()<=0){
							return new RetMessage(false, "没有找到相应的格式定义的格式！");
						}else{
							fbmap.put("d", listbvos);
							break;
						}
					}
				}
			}
			DataformatdefHVO temphvo=fhmap.get("d");
			if(temphvo==null){
				return new RetMessage(false,"没有找到对应的格式定义");
			}
			String fflog="select cdata from dip_messagelogo where pk_messagelogo='"+temphvo.getBeginsign()+"'";
			String fgbj=null;
			try {
				fgbj=iqf.queryfield(fflog);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (DbException e) {
				e.printStackTrace();
			}
			StringBuffer indexc=new StringBuffer();
			StringBuffer insertstr=new StringBuffer();
			for(int i=0;i<fbmap.get("d").size();i++){
				DataformatdefBVO bvoi=fbmap.get("d").get(i);
				if(bvoi.getVdef2()!=null&&bvoi.getVdef2().equals("业务数据")){
					indexc.append((fromcount+i)+",");
					insertstr.append(bvoi.getDatakind()+",");
					if(pkfiled!=null&&pkfiled.toLowerCase().equals(bvoi.getDatakind().toLowerCase())){
						pkfileindex=fromcount+i;
					}
				}
			}
			if(fgbj==null||fgbj.length()<=0){
				return new RetMessage(false, "没有找到相应的分割标记！");
			}

			int succount=0;
			int facount=0;
			int fgcount=0;
			int hhcount=0;
			//判断文件是不是这个业务，系统，站点的，如果是的话，就导入
			boolean iscurrxz=getIcCurrBusProp(filpathsql+"/"+filname,xtbz,zdbz,ddfhvo.getBusicode());
			
			RetMessage rt=null;
			if(iscurrxz){
				List<String> list = new ArrayList<String>();
				FileReader fr = null;
				BufferedReader br = null;
				try {
					fr = new FileReader(filpathsql+"/"+filname);
					br = new BufferedReader(fr);
					String line = "";
					while ((line = br.readLine()) != null) {
						list.add(line);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return new RetMessage(false,"没有找到文件"+filpathsql+"/"+filname+e.getMessage());
				} catch (IOException e) {
					e.printStackTrace();
					return new RetMessage(false,"读文件失败"+filpathsql+"/"+filname+e.getMessage());
				} finally {
					if (br != null) {
						try {
							br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (fr != null) {
						try {
							fr.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				if (list == null || list.size() <1) {
					return new RetMessage(true,"成功0条记录");
				}
				String[] inde=indexc.toString().split(",");
				for(int i=0;i<list.size();i++){
					StringBuffer inv=new StringBuffer();
					String insi=list.get(i);
					String[] ss=insi.split(fgbj);
					for(int j=0;j<inde.length;j++){
						inv.append("'"+ss[Integer.parseInt(inde[j])+1]+"',");
					}
					if(pkfiled!=null&&pkfiled.length()>0){
						String isexit=null;
						try {
							isexit=iqf.queryfield("select "+pkfiled+" from "+ddfhvo.getMemorytable()+" where "+pkfiled+"='"+ss[pkfileindex+1]+"'");
						} catch (SQLException e1) {
							e1.printStackTrace();
						} catch (DbException e1) {
							e1.printStackTrace();
						}
						if(isexit!=null&&isexit.length()>0){
							if(isfg){
								String delsql="delete from "+ddfhvo.getMemorytable()+" where "+pkfiled+"='"+ss[pkfileindex+1]+"'";
								try {
									iqf.exesql(delsql);
								} catch (SQLException e) {
									e.printStackTrace();
								} catch (DbException e) {
									e.printStackTrace();
								}
								fgcount++;
							}else{
								hhcount++;
								succount++;
								continue;
							}
						}
						
					}
					String sql="insert into "+ddfhvo.getMemorytable()+" ("+insertstr.substring(0,insertstr.length()-1)+") values ("+inv.substring(0,inv.length()-1)+")";
					try {
						iqf.exesql(sql);
						succount++;
					} catch (Exception e) {
						e.printStackTrace();
						errlist.add(e.getMessage()+"["+sql+"]");
						facount++;
					} 
				}
				String msg=(facount>0?",失败"+facount+"条记录":"");
				msg=msg+(succount>0?",成功"+succount+"条记录":"");
				msg=msg+(hhcount>0?",其中忽略"+hhcount+"条记录":"");
				msg=msg+(fgcount>0?",其中覆盖"+fgcount+"条记录":"");
				rt=new RetMessage(true,msg);
			}else{
				rt=new RetMessage(false,"指定的文件不是对应的系统、站点、业务的对应文件");
			}
			
			rt.setErrlist(errlist);
			rt.setFa(facount);
			rt.setSu(succount);
			rt.setExit(hhcount);
			return rt;
		}catch (DAOException e2) {
			e2.printStackTrace();
			return new RetMessage(false, "查询相应的数据格式定义错误！"
					+ e2.getMessage());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	} else {
		Logger.error("条件不足，无法进行相应操作！");
		return new RetMessage(false, "条件不足，无法进行相应操作！");
	}
	return new RetMessage(false, "没有找到对应的数据同步定义");

}
/**
 * 通过同步处理来启动线程来接收esb消息流。
 * @param dataRecHvo
 * @param hpk
 * @param errlist
 * @return
 */
private RetMessage esbMessageToTable(DipDatarecHVO dataRecHvo, String hpk, List<String> errlist,DipDatasynchVO syshvo) {
	// TODO Auto-generated method stub
	RetMessage ret=new RetMessage();
	Map<String,EsbMapUtilVO> receiveFormatMap=new HashMap<String,EsbMapUtilVO>();//所有接收数据格式
	Map<String,String> backFormatMap=new HashMap<String, String>();//回执格式
	EsbMapUtilVO esbMapUtilVO=new EsbMapUtilVO();
	String pk_datarech=dataRecHvo.getPk_datarec_h();
	List<DataformatdefHVO> listDataformatdefHVO=null;
	MsrVO msrvo=null;
	try {
		listDataformatdefHVO=(List<DataformatdefHVO>) getBaseDAO().retrieveByClause(DataformatdefHVO.class, "pk_datarec_h='"+pk_datarech+"' and nvl(dr,0)=0 ");
		msrvo=getMsrVoByPkdatarech(pk_datarech);	
		if(msrvo==null){
				return new RetMessage(false,"消息服务器不能为空");
			}	
		
		if(listDataformatdefHVO==null||listDataformatdefHVO.size()==0){
			return new RetMessage(false,"格式定义为空");
		}else{
			VDipDsreceiveVO dsreceiveVo=(VDipDsreceiveVO) iqf.querySupervoByPk(VDipDsreceiveVO.class, pk_datarech);
		 if(dsreceiveVo==null){
			 return new RetMessage(false,"格式定义为空"); 
		 }else{
			 VDipDsreceiveVO vDipDsreceivevo= dsreceiveVo;
			 
			 ret=getRecitveFormatData(esbMapUtilVO, vDipDsreceivevo, pk_datarech, receiveFormatMap, backFormatMap, listDataformatdefHVO);
			if(ret.getIssucc()){
				DataReceiverJMSImpl receiveJms=new DataReceiverJMSImpl();
				return receiveJms.receiveMessageTB(syshvo,receiveFormatMap,backFormatMap,msrvo);
			}else{
				return ret;
			}
		 }
		  
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		ret.setMessage(e.getMessage());
		return ret;
	}
}
private MsrVO getMsrVoByPkdatarech(String pk_datarec_h){
	MsrVO msrvo=null;
	try {
		DipDatarecHVO hvo=(DipDatarecHVO) getBaseDAO().retrieveByPK(DipDatarecHVO.class, pk_datarec_h);
		if(hvo!=null){
			msrvo=(MsrVO) getBaseDAO().retrieveByPK(MsrVO.class, hvo.getSourcecon());	
		}
	} catch (DAOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return msrvo;
}
private RetMessage getRecitveFormatData(EsbMapUtilVO esbMapUtilVO, VDipDsreceiveVO vDipDsreceivevo,String pk_datarech,Map<String,EsbMapUtilVO> receiveFormatMap,Map<String,String> backFormatMap,List<DataformatdefHVO> listDataformatdefHVO){
	RetMessage ret=new RetMessage(); 
	esbMapUtilVO.setSysvo(vDipDsreceivevo);
	 String bj=vDipDsreceivevo.getBj();
	 esbMapUtilVO.setBj(bj);
	 esbMapUtilVO.setCheckname(getVerifyList(pk_datarech));
	 List<DipDatadefinitBVO> listdatadefinit_b=getDatadefinit_bVos(vDipDsreceivevo.getPk_datadefinit_h());
	 if(listdatadefinit_b==null||listdatadefinit_b.size()==0){
		 return new RetMessage(false,"数据定义子表为空");
	 }
	 esbMapUtilVO.setTypeddb(listdatadefinit_b);
	 esbMapUtilVO.setEsc(getEscString());
	 esbMapUtilVO.setPk_datarec(pk_datarech);
	 esbMapUtilVO.setTablename(vDipDsreceivevo.getMemorytable());
	 String fixedlog= bj+vDipDsreceivevo.getExtcode()+bj+(vDipDsreceivevo.getSitecode()==null||vDipDsreceivevo.getSitecode().length()<=0?(vDipDsreceivevo.getDef_str_1()==null?"000000":vDipDsreceivevo.getDef_str_1()):vDipDsreceivevo.getSitecode())+bj+vDipDsreceivevo.getBusicode()+bj;
	 esbMapUtilVO.setKey(fixedlog);
	 if(listDataformatdefHVO!=null&&listDataformatdefHVO.size()>0){
		 for(int i=0;i<listDataformatdefHVO.size();i++){
			 DataformatdefHVO hvo=listDataformatdefHVO.get(i);
			 List<DataformatdefBVO> fmb=null;
			try {
				fmb = (List<DataformatdefBVO>) getBaseDAO().retrieveByClause(DataformatdefBVO.class, "pk_dataformatdef_h ='"+hvo.getPk_dataformatdef_h()+"'","code");
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 if(fmb==null||fmb.size()==0){
				 return new RetMessage(false,"接收格式定义错误");
			 }
			 esbMapUtilVO.setData(fmb);
			 String receiveFormat=bj+fmb.get(0).getDatakind()+fixedlog;
			 String ltype=hvo.getMessflowtype();
			 String type="";
//			 if(ltype.equals("0001AA100000000142YQ")){//开始
			if(ltype.equals(DataformatPanel.MESSTYPE_CSKS_PK)){//开始
				 type="b";
//				}else if(ltype.equals("0001AA100000000142YS")){//数据
				}else if(ltype.equals(DataformatPanel.MESSTYPE_CSSJ_PK)){//数据
					type="d";
//				}else if(ltype.equals("0001ZZ10000000018K7M")){//结束
				}else if(ltype.equals(DataformatPanel.MESSTYPE_CSJS_PK)){//结束
					type="e";
//				}else if(ltype.equals("0001ZZ1000000001GFD7")){
				}else if(ltype.equals(DataformatPanel.MESSTYPE_HZBZ_PK)){
					if(vDipDsreceivevo.getBackcon()!=null&&vDipDsreceivevo.getBackcon().equals("Y")){
						  getBackFormat(pk_datarech, vDipDsreceivevo, backFormatMap);	 
						  if(backFormatMap==null||backFormatMap.size()!=2){
							  return new RetMessage(false,"回执信息错误");
						  }
				    }
					continue;
				}else{
					return new RetMessage(false,"数据流类型只能是【传输开始标志、传输数据标志、传输结束标志、回执标志】");
				}
			 esbMapUtilVO.setType(type);
			 receiveFormatMap.put(receiveFormat, esbMapUtilVO);
		 }
		 ret.setIssucc(true);
		 return ret;
	 }else{
		 return new RetMessage(false,"格式定义主表不能为空");	 
	 }
	 
	 
	
}
/**
 *通过业务主键pk_bus,得到校验方法名称
 * @param pk_bus
 * @return
 */
private List getVerifyList(String pk_bus){
	List<DataverifyHVO> vhvos=null;
	List<String> fa=null;
	try {
		vhvos = (List<DataverifyHVO>) getBaseDAO().retrieveByClause(DataverifyHVO.class, "pk_datachange_h in ('"+pk_bus+"') and nvl ( dr, 0 ) = 0");
		Map<String ,List> verfiemap=new HashMap<String,List>();//数据校验的map，key是数据接收的主表主键，值是校验的list
		if(vhvos!=null&&vhvos.size()>0){
			List<DataverifyBVO> vbvos=(List<DataverifyBVO>) getBaseDAO().retrieveByClause(DataverifyBVO.class, "pk_dataverify_h in ('"+vhvos.get(0).getPk_dataverify_h()+"') and nvl(dr,0)=0");
			if(vbvos!=null&&vbvos.size()>0){
				fa=new ArrayList<String>();
				for(DataverifyBVO bvoi:vbvos){
					fa.add(bvoi.getName());
				}
			}
		}
	} catch (DAOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return fa;
}

private List getDatadefinit_bVos(String pk_datadefinit_h){
	List list=null;
	try {
		list=(List<DipDatadefinitBVO>) getBaseDAO().retrieveByClause(DipDatadefinitBVO.class, "pk_datadefinit_h='"+pk_datadefinit_h+"' and nvl(dr,0)=0 ");
	} catch (DAOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return list;
}
private void getBackFormat(String pk_datarech,VDipDsreceiveVO vDipDsreceivevo,Map backFormatMap){

	 List<DipDatarecSpecialTab> listspecialhvo=null;
	try {
		listspecialhvo = (List<DipDatarecSpecialTab>) getBaseDAO().retrieveByClause(DipDatarecSpecialTab.class, "pk_datarec_h='" + pk_datarech + "' and (nvl(dr,0)=0 ) and is_back='Y' ");
	} catch (DAOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
		String xtbz="";
		String zdbz="";
		String ywbz="";
		if(listspecialhvo!=null&&listspecialhvo.size()>0){
			for(int c=0;c<listspecialhvo.size();c++){
				DipDatarecSpecialTab specialvo=listspecialhvo.get(c);
				if(specialvo.getName().equals("系统标志")){
					String xtvalue=specialvo.getValue();
					if(xtvalue!=null&&xtvalue.trim().length()>0){
						xtbz=xtvalue;
					}
				}
				if(specialvo.getName().equals("站点标志")){
					String zdvalue=specialvo.getValue();
					if(zdvalue!=null&&zdvalue.trim().length()>0){
						zdbz=zdvalue;
					}
				}
				if(specialvo.getName().equals("业务标志")){
					String ywvalue=specialvo.getValue();
					if(ywvalue!=null&&ywvalue.trim().length()>0){
						ywbz=ywvalue;
					}
				}
			}
			
			
		}
		
		List<DataformatdefBVO> listi=null;
		try {
			listi = (List<DataformatdefBVO>) getBaseDAO().retrieveByClause(DataformatdefBVO.class, "pk_dataformatdef_h=(" +
					"select pk_dataformatdef_h from dip_dataformatdef_h where messflowtype='0001ZZ1000000001GFD7' and pk_datarec_h='"+pk_datarech+"'" +
					" and rownum<2 and nvl(dr,0)=0) order by code");
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(listi!=null&&listi.size()>0){
			String rets="";
			for(int w=0;w<listi.size();w++){
				DataformatdefBVO bvoi=listi.get(w);
				if(bvoi.getVdef2().equals("业务数据")){
					rets=rets+",";
				}else if(bvoi.getVdef2().equals("标志头")||bvoi.getVdef2().equals("标志尾")||bvoi.getVdef2().equals("自定义")){
					rets=rets+(bvoi.getDatakind()!=null&&bvoi.getDatakind().length()>0?bvoi.getDatakind():"")+",";
				}else if(bvoi.getVdef2().equals("固定标志")&&bvoi.getVdef3().equals("业务标志")){
					if(!ywbz.trim().equals("")){
						rets=rets+ywbz+",";
					}else{
						rets=rets+vDipDsreceivevo.getBusicode()+",";
					}
				}else if(bvoi.getVdef2().equals("固定标志")&&bvoi.getVdef3().equals("系统标志")){
					if(!xtbz.trim().equals("")){
						rets=rets+xtbz+",";
					}else{
						rets=rets+vDipDsreceivevo.getExtcode()+",";
					}
				}else if(bvoi.getVdef2().equals("固定标志")&&bvoi.getVdef3().equals("站点标志")){
					if(!zdbz.trim().equals("")){
						rets=rets+zdbz+",";
					}else{
						rets=rets+vDipDsreceivevo.getSitecode()+",";	
					}
					
				}else if(bvoi.getVdef2().equals("时间函数")){
					rets=rets+"时间函数<"+bvoi.getDatakind()+">"+",";
				}else if(bvoi.getVdef2().equals("主键函数")){
					rets=rets+"主键函数"+",";
				}else if(bvoi.getVdef2().equals("记录数函数")){
					rets=rets+"记录数函数"+",";
				}else{
					rets=rets+bvoi.getVdef3()+",";
				}
			}
			rets=rets.substring(0,rets.length()-1);
			backFormatMap.put(pk_datarech, rets);
			backFormatMap.put(pk_datarech+"s", vDipDsreceivevo.getBackmsr());
		}
		

}
/**
 * 得到转义字符参数
 * @return
 */
private String getEscString(){
	String esc=null;//转义字符运行参数
	List<DipRunsysBVO> runbvo=null;
	try {
		runbvo = (List<DipRunsysBVO>) getBaseDAO().retrieveByClause(DipRunsysBVO.class, " syscode='DIP-0000011' and nvl(dr,0)=0 ");
	} catch (DAOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	if(runbvo!=null&&runbvo.size()==1){
		if(runbvo.get(0).getSysvalue()!=null&&!runbvo.get(0).getSysvalue().equals("")){
			esc=runbvo.get(0).getSysvalue();
		}
	}
	return esc;
}


public void styleToMsgTB(String sourcemsg,String msg,Map<String,EsbMapUtilVO> gsmap,MsrVO mvo,String[] strerror,Map<String,String> backFormatMap) throws BusinessException, SQLException,
DbException {
	DateprocessVO vop=new DateprocessVO();
	vop.setActivetype("数据接收");
	vop.setActive("ESB消息接收");
	vop.setSdate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	if (null != msg && msg.length() > 0) {
		if(gsmap==null||gsmap.size()<=0){
			vop.setContent("没有定义消息总线格式,格式定义为空"+sourcemsg);
			vop.setDef_str_1(IContrastUtil.DATAPROCESS_HINT);
			Logger.debug(new StringBuffer("styleToMsgTB").append("没有定义消息总线格式,格式定义为空"+sourcemsg));
			ilm.writToDataLog_RequiresNew(vop);
			ilm.writToMhccb_RequiresNew(sourcemsg, mvo);
				writeDipLog("消息流--没有定义消息总线格式,格式定义为空--"+sourcemsg, IContrastUtil.LOG_ERROR);
			writeMessageInTable(sourcemsg,strerror);
			return ;
		}
		Logger.debug(new StringBuffer("styleToMsgTB").append("格式定义的MAP大小为:"+gsmap.size()));
		Iterator<String> it=gsmap.keySet().iterator();
		boolean iscontaings=false;
		while(it.hasNext()){
			String key=it.next();
			Logger.debug("key :["+key+"];msg:["+msg+"];msg.indexOf(key)"+msg.indexOf(key));
			if(msg.indexOf(key)==0){
				EsbMapUtilVO vo=(EsbMapUtilVO) gsmap.get(key);
				vop.setPk_bus(vo.getPk_datarec());
				strerror[2]=vo.getTablename();
				Logger.debug("vo.getType()"+vo.getType());
				if(vo.getType().equals("b")){
					VDipDsreceiveVO vdvo=vo.getSysvo();
					ThreadUtil.getOrSetcount(vo.getPk_datarec(), ThreadUtil.OPTYPE_B,null);
					vop.setContent(vdvo.getExtname()+","+vdvo.getSysname()+",接收到数据传输开始标志"+msg);
					vop.setDef_str_1(IContrastUtil.DATAPROCESS_HINT);
					Logger.debug(vdvo.getExtname()+","+vdvo.getSysname()+",接收到数据传输开始标志"+msg);
					ilm.writToDataLog_RequiresNew(vop);
					DateprocessVO vop1=(DateprocessVO) vop.clone();
					vop1.setContent("开始接收数据");
					ilm.writeToTabMonitor_RequiresNew(vo.getPk_datarec(), IContrastUtil.YWLX_JS,IContrastUtil.TABMONSTA_ON,null,null);
						writeDipLog("消息流--接收到数据传输开始标志--"+msg, IContrastUtil.LOG_SUCESS);
				}else if(vo.getType().equals("e")){
					String pk_bus=vo.getPk_datarec();
					String yw=IContrastUtil.YWLX_JS;
					int count=0;
					Logger.debug("取计数器，");
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					DipCountVO retmap=ThreadUtil.getOrSetcount(vo.getPk_datarec(), ThreadUtil.OPTYPE_E,null);
					Logger.debug("取计数器，计数器是不是空 "+retmap);
					if(retmap!=null){
						count=retmap.getCount();
						Integer fa=retmap.getFcount();
						Integer suc=retmap.getScount();
						Logger.debug("fa:"+fa+"suc:"+suc);
						Map<String,DipSuccOrFailClass> sofmap=retmap.getClassverfiy();
						if(sofmap!=null&&sofmap.size()>0){
							Iterator<String> kit=sofmap.keySet().iterator();
							Logger.debug("kitleng:"+sofmap.keySet().size());
							while(kit.hasNext()){
								String ik=kit.next();
								Logger.debug("ik:"+ik);
								DipSuccOrFailClass dofc=sofmap.get(ik);
								Logger.debug("ik:"+ik+"dofc.gets:"+dofc.getSucount()+"dofc.getf"+dofc.getFacount());
								ilm.writeToTabMonitor_RequiresNew(pk_bus, yw, dofc.getSucount()==null?0:dofc.getSucount(), dofc.getFacount()==null?0:dofc.getFacount(), ik);
							}
						}
						Logger.debug("pk_bus:"+pk_bus+"TABMONSTA_FIN:"+"suc："+suc+"fa:"+fa);
						ilm.writeToTabMonitor_RequiresNew(pk_bus, IContrastUtil.YWLX_JS,IContrastUtil.TABMONSTA_FIN,suc,fa);
					}else{
						Logger.debug("pk_bus:"+pk_bus+"TABMONSTA_FIN:"+"suc："+null+"fa:"+null);
						ilm.writeToTabMonitor_RequiresNew(pk_bus, IContrastUtil.YWLX_JS,IContrastUtil.TABMONSTA_FIN,null,null);
					}
					VDipDsreceiveVO vdvo=vo.getSysvo();
					Logger.debug("回执标志：【"+vdvo.getBackcon()+"】");
					Logger.debug("vdvo.getBackcon()!=null&&vdvo.getBackcon().equals(\"Y\"):"+(vdvo.getBackcon()!=null&&vdvo.getBackcon().equals("Y")));
					if(vdvo.getBackcon()!=null&&vdvo.getBackcon().equals("Y")){
						String fsmsg=null;
						String backmsr=null;
						Logger.debug("backFormatMap!=null&&backFormatMap.containsKey(pk_bus):"+(backFormatMap!=null&&backFormatMap.containsKey(pk_bus)));
						if(backFormatMap!=null&&backFormatMap.containsKey(pk_bus)){
							fsmsg=backFormatMap.get(pk_bus);
							Logger.debug("fsmsg:"+(fsmsg));
							Logger.debug("count:"+(count+""));
							fsmsg=fsmsg.replace("记录数函数", count+"");
							Logger.debug("fsmsg记录数函数:"+(fsmsg));
							while(fsmsg.indexOf("时间函数")>0){
								int indexi=fsmsg.indexOf("时间函数");
								int indexr=fsmsg.indexOf(">");
								fsmsg=fsmsg.substring(0,indexi)+( new SimpleDateFormat(fsmsg.substring((indexi+("时间函数<".length())),indexr)).format(new Date()))+fsmsg.substring(indexr+1);
								Logger.debug("fsmsg时间函数:"+(fsmsg));
							}
							String pk=null;
							pk = query.getOID();
							if(pk==null){
								pk="";
							}
							fsmsg=fsmsg.replace("主键函数", pk);
							Logger.debug("fsmsg主键函数:"+(fsmsg));
							fsmsg=fsmsg.replace(",", vo.getBj());
							fsmsg=vo.getBj()+fsmsg+vo.getBj();
							Logger.debug("fsmsg:"+(fsmsg));
							if(backFormatMap.containsKey(pk_bus+"s")){
								backmsr=backFormatMap.get(pk_bus+"s");
								Logger.debug("backmsr:"+(backmsr));
							}
						}
						DateprocessVO vop1=(DateprocessVO) vop.clone();
						vop.setDef_str_1(IContrastUtil.DATAPROCESS_HINT);
						vop1.setContent(vdvo.getExtname()+","+vdvo.getSysname()+",发送回执消息【"+fsmsg+"】");
						Logger.debug("发送回执消息"+(vdvo.getExtname()+","+vdvo.getSysname()+",发送回执消息【"+fsmsg+"】"));
						ilm.writToDataLog_RequiresNew(vop1);
						DateprocessVO vop2=(DateprocessVO) vop.clone();
						MsrVO msvo=(MsrVO) getBaseDAO().retrieveByPK(MsrVO.class, backmsr);
						if(fsmsg==null||fsmsg.length()<=0){
							vop2.setContent(vdvo.getExtname()+","+vdvo.getSysname()+"没有找到回执定义");
							vop2.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
							writeDipLog("消息流--发送回执失败没有找到回执定义--"+msg, IContrastUtil.LOG_ERROR);
						}
						if(msvo==null){
							vop2.setContent(vop2.getContent()+"；没有找到回执消息服务器的注册");
							vop2.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
							writeDipLog("消息流--发送回执失败没有找到回执消息服务器的注册--"+msg, IContrastUtil.LOG_ERROR);
						}
						ilm.writToDataLog_RequiresNew(vop2);
						if(msvo!=null){
							ite.dosend(msvo, fsmsg);
							writeDipLog("消息流--发送回执成功--"+msg, IContrastUtil.LOG_SUCESS);
						}
					}
					DateprocessVO vop1=(DateprocessVO) vop.clone();
					vop1.setDef_str_1(IContrastUtil.DATAPROCESS_HINT);
					vop1.setContent(vdvo.getExtname()+","+vdvo.getSysname()+",接收数据完毕，共接收到"+count+"条数据"+msg);
						writeDipLog("消息流--接收到数据传输结束标志--"+msg, IContrastUtil.LOG_SUCESS);
					ilm.writToDataLog_RequiresNew(vop1);
				}else if(vo.getType().equals("d")){
					Logger.debug("接收的为业务数据，进入计数器");
					String pk_rec=vo.getPk_datarec();
					ThreadUtil.getOrSetcount(pk_rec, ThreadUtil.OPTYPE_D,null);
					
					List check=vo.getCheckname();
					Logger.debug("拿校验类");
					boolean ischeckpas=true;
					if(check!=null&&check.size()>0){
						Logger.debug("进入校验循环中");
						for(int i=0;i<check.size();i++){
							
							String name=(String) check.get(i);
							Class cls;
							try {
								cls = Class.forName(name);
								Object inst = cls.newInstance();
								if (inst instanceof ICheckPlugin) {
									ICheckPlugin m = (ICheckPlugin) inst;
									CheckMessage cmsg=m.doCheck(sourcemsg,vo);
									Logger.debug("校验---"+name+" 是否通过"+cmsg.isSuccessful()+"  错误内容"+cmsg.getMessage());
									if(!cmsg.isSuccessful()){
										ischeckpas=false;
										ThreadUtil.getOrSetcount(pk_rec, ThreadUtil.OPTYPE_C, name+"f");
										Logger.debug("校验---"+name+" 失败了，加入到计数器里边");
										writeDipLog("校验---"+name+" 失败了--"+sourcemsg, IContrastUtil.LOG_ERROR);
										break;
									}else{
										ThreadUtil.getOrSetcount(pk_rec, ThreadUtil.OPTYPE_C, name+"s");
										Logger.debug("校验---"+name+" 成功了，加入到计数器里边");
									}
								}
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
								ThreadUtil.getOrSetcount(pk_rec, ThreadUtil.OPTYPE_C, name+"f");
								vop.setContent("没有找到校验类"+name+":"+sourcemsg);
								vop.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
									writeDipLog("消息流--没有找到校验类"+name+":"+sourcemsg+"--"+sourcemsg, IContrastUtil.LOG_ERROR);
								ischeckpas=false;
							} catch (InstantiationException e) {
								e.printStackTrace();
								ThreadUtil.getOrSetcount(pk_rec, ThreadUtil.OPTYPE_C, name+"f");
								vop.setContent("校验类初始化失败"+name+":"+e.getMessage()+" "+sourcemsg);
								vop.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
									writeDipLog("消息流--校验类初始化失败"+name+":"+e.getMessage()+" "+sourcemsg+"--"+sourcemsg, IContrastUtil.LOG_ERROR);
								ischeckpas=false;
							} catch (IllegalAccessException e) {
								e.printStackTrace();
								ThreadUtil.getOrSetcount(pk_rec, ThreadUtil.OPTYPE_C, name+"f");
								vop.setContent("校验失败"+name+":"+e.getMessage()+" "+sourcemsg);
								vop.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
								writeDipLog("消息流--校验失败"+name+":"+e.getMessage()+" "+sourcemsg+"--"+sourcemsg, IContrastUtil.LOG_ERROR);
								ischeckpas=false;
							}catch(Exception e){
								e.printStackTrace();
								ischeckpas=true;
								Logger.debug("校验---"+name+" 抛错了");
									writeDipLog("消息流--校验---"+name+" 抛错了"+"--"+sourcemsg, IContrastUtil.LOG_ERROR);
								vop.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
							}
						}
					}
					if(ischeckpas){
						Logger.debug("-------保存ESB接收到的数据");
						RetMessage ret=saveMessage(sourcemsg,vo);
						
						if(ret.getIssucc()){
							ThreadUtil.getOrSetcount(pk_rec, ThreadUtil.OPTYPE_S, "s");
								writeDipLog("消息流--"+"保存成功--"+sourcemsg, IContrastUtil.LOG_SUCESS);
						}else{
							ThreadUtil.getOrSetcount(pk_rec, ThreadUtil.OPTYPE_S, "f");
							vop.setContent(ret.getMessage());
							vop.setDef_str_1(IContrastUtil.DATAPROCESS_HINT);
							ilm.writToDataLog_RequiresNew(vop);
							writeDipLog("消息流--"+"保存失败--"+sourcemsg, IContrastUtil.LOG_ERROR);
						}
					}else{
						ThreadUtil.getOrSetcount(pk_rec, ThreadUtil.OPTYPE_S, "f");
						Logger.debug("-------ESB校验失败，写入数据处理日志表和错误消息表中");
						vop.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
						ilm.writToDataLog_RequiresNew(vop);
						writeMessageInTable(sourcemsg,strerror);
							writeDipLog("消息流--"+"ESB校验失败--"+sourcemsg, IContrastUtil.LOG_ERROR);
					}
				}
				iscontaings=true;
				break;
			}
		}
		if(!iscontaings){
			vop.setContent("没有找到对应的格式定义："+sourcemsg);
			Logger.debug(new StringBuffer("styleToMsgTB !contaings").append("没有定义消息总线格式:"+sourcemsg));
			writeDipLog("消息流--"+"没有找到对应的格式定义--"+sourcemsg, IContrastUtil.LOG_ERROR);
			vop.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
			ilm.writToDataLog_RequiresNew(vop);
			ilm.writToMhccb_RequiresNew(sourcemsg, mvo);
			writeMessageInTable(sourcemsg,strerror);
		}
		
	}

}

/**
 * @desc 判断这个文件对应的prop文件是不是给定的系统标志，站点标志，业务标志的prop
 * @param filename 数据文件的全路径
 * @param xtbz 系统标志
 * @param zdbz 站点标志
 * @param busicode 业务标志
 * */
	private boolean getIcCurrBusProp(String filename, String xtbz, String zdbz, String busicode) {
		DealFile dl=new DealFile();
		PropUtilVO pvo=dl.getPropUtilVO(filename);
		if(pvo!=null
				&&pvo.getXtbz()!=null&&pvo.getXtbz().equals(xtbz)
				&&pvo.getYwbz()!=null&&pvo.getYwbz().equals(busicode)
				&&pvo.getZdbz()!=null&&pvo.getZdbz().equals(zdbz)
				){
			return true;
		}
		return false;
	}
	private RetMessage sysDateByFile(String hpk, String pk_datarec,List<String> errlist) {
		// 1.判断哪种格式:.xml、.txt、.xls
		DipDatarecHVO dataRecHvo = null;
		DipDatarecSpecialTab specialxml[]=null;
		try {// 数据接收定义主表vo
			dataRecHvo = (DipDatarecHVO) getBaseDAO().retrieveByPK( DipDatarecHVO.class, pk_datarec);
//			specialxml=(DipDatarecSpecialTab[]) HYPubBO.queryByCondition(DipDatarecSpecialTab.class, "pk_datarec_h='"+pk_datarec+"' and nvl(dr,0)=0 and is_xml='Y' order by nodenumber");
			List list=(List) getBaseDAO().retrieveByClause(DipDatarecSpecialTab.class, "pk_datarec_h='"+pk_datarec+"' and nvl(dr,0)=0 and is_xml='Y' order by nodenumber");
			if(list!=null&&list.size()>0){
				specialxml=new DipDatarecSpecialTab[1];
				list.toArray(specialxml);
			}
//			specialxml=(DipDatarecSpecialTab[]) getBaseDAO().retrieveByClause(DipDatarecSpecialTab.class, "pk_datarec_h='"+pk_datarec+"' and nvl(dr,0)=0 and is_xml='Y' order by nodenumber");
			
		} catch (Exception e2) {
			e2.printStackTrace();
			return new RetMessage(false, "查询相应的数据接收定义错误！" + e2.getMessage()+e2.getStackTrace()[0]);
		}
	
		if (dataRecHvo == null) {// 数据接收定义主表vo
			return new RetMessage(false, "没有找到对应的数据接收定义！");
		}
		// 2、找文件，把文件读出来
		String file = dataRecHvo.getSourcecon();// 存放的pk
		if (file != null && !"".equals(file)) {
			String sql5 = "select descriptions from dip_filepath where pk_filepath='" + file + "' and nvl(dr,0)=0";
			try {
				file = query.queryfield(sql5);
			} catch (Exception e) {
				e.printStackTrace();
				return new RetMessage(false, "查询数据来源路径错误！" + e.getMessage());
			}// 数据来源连接名称
		}
		String filepath = "";
		// 数据来源参数

		if (dataRecHvo == null) {// 数据接收定义主表vo
			return new RetMessage(false, "没有找到对应的数据接收定义！");
		} else {
			String phyname = dataRecHvo.getDataname();// 物理表名
			HashMap<String, String> map = new HashMap<String, String>();
			DataformatdefHVO fhvo = null;
			String fgbj=null;
			List<DataformatdefHVO> listfhvo = null;
			
			DipSysregisterHVO xt=null;
			DipDatadefinitHVO ddfhvo=null;
			try {
				xt = (DipSysregisterHVO) getBaseDAO().retrieveByPK(DipSysregisterHVO.class, dataRecHvo.getPk_xt());
				if(xt==null){
					return new RetMessage(false, "没有找到相应的系统！");
				}
				ddfhvo=(DipDatadefinitHVO) getBaseDAO().retrieveByPK(DipDatadefinitHVO.class, dataRecHvo.getMemorytable());
				if(ddfhvo==null){
					return new RetMessage(false, "没有找到相应的数据定义！");
				}
			} catch (DAOException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			
			
			String xtbz=xt.getExtcode();//系统标志
			String zdzj=ddfhvo.getDeploycode();
			String zdbz="";//站点标志
			if(zdzj==null||zdzj.length()<=0){
				zdbz=xt.getDef_str_1();
			}else{
				DipSysregisterBVO sbvo=null;
				try {
					sbvo = (DipSysregisterBVO) getBaseDAO().retrieveByPK(DipSysregisterBVO.class, zdzj);
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(sbvo==null){
					return new RetMessage(false, "没有找到相应的站点！");
				}
				zdbz=sbvo.getSitecode();
			}
			String ywbz=ddfhvo.getBusicode();
			
			try {// 数据格式定义表头
				listfhvo=(List<DataformatdefHVO>) getBaseDAO().retrieveByClause(DataformatdefHVO.class, "pk_datarec_h='" + pk_datarec + "' and nvl(dr,0)=0");
			} catch (Exception e2) {
				e2.printStackTrace();
				return new RetMessage(false, "查询相应的数据格式定义错误！" + e2.getMessage());
			}
			Hashtable<String, DipDatadefinitBVO> hashTable = new Hashtable<String, DipDatadefinitBVO>();
			if (listfhvo == null||listfhvo.size()<=0) {
				return new RetMessage(false, "没有找到相应的格式定义！");
			} else {
				fhvo=listfhvo.get(0);
				List<DataformatdefBVO> fbvo = null;
				try {
					fbvo = (List<DataformatdefBVO>) getBaseDAO() .retrieveByClause( DataformatdefBVO.class, "pk_dataformatdef_h='"  + fhvo.getPrimaryKey() + "' and datakind is not null and nvl(dr,0)=0 order by code");
				} catch (DAOException e2) {
					e2.printStackTrace();
					return new RetMessage(false, "查询相应的数据格式定义错误！"
							+ e2.getMessage());
				}
				if (fbvo == null || fbvo.size() <= 0) {// 数据格式定义表体
					return new RetMessage(false, "没有找到相应的格式定义的格式！");
				}
				// 0：顺序，1：字段对照
				Integer datamaptye = Integer.parseInt(fhvo.getMessflowdef());// 是顺序对照还是字段对照
				for (DataformatdefBVO dfbvo : fbvo) {// 对照的map
					if (dfbvo.getDatakind() != null && !"".equals(dfbvo.getDatakind())) {
						if (datamaptye == 0) {
							map.put(dfbvo.getCode() + "", dfbvo.getDatakind());
						} else {
							map.put(dfbvo.getCorreskind(), dfbvo.getDatakind());
						}
					}
				}
				String fflog="select cdata from dip_messagelogo where pk_messagelogo='"+fhvo.getBeginsign()+"'";
				try {
					fgbj=iqf.queryfield(fflog);
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dataRecHvo.setAttributeValue("datamaptype", datamaptye);
				dataRecHvo.setAttributeValue("map", map);
				dataRecHvo.setAttributeValue("tablename", phyname);
				String memorytable = dataRecHvo.getMemorytable();
				// 2、查数据定义
				List<DipDatadefinitBVO> dataDefinitBvo = null;
				try {// 数据定义表体VO
					dataDefinitBvo = (List<DipDatadefinitBVO>) getBaseDAO().retrieveByClause( DipDatadefinitBVO.class, "pk_datadefinit_h='" + memorytable + "' and nvl(dr,0)=0");
				} catch (DAOException e1) {
					e1.printStackTrace();
					return new RetMessage(false, "查询数据数据格式定义错误！" + e1.getMessage());
				}
				if (dataDefinitBvo == null || dataDefinitBvo.size() <= 0) {
					return new RetMessage(false, "数据定义中没有找到表的结构定义！");
				}
				for (DipDatadefinitBVO bvo : dataDefinitBvo) {
					hashTable.put(bvo.getEname(), bvo);
					if (bvo.getIspk() != null && bvo.getIspk().booleanValue()) {
						hashTable.put("#PKFIELD#", bvo);
					}
				}
				// 3、找文件位置

			}
			int i=0;
			TxtDataVO vo = null;
			String sourceparam = dataRecHvo.getSourceparam();
			if (file != null && !"".equals(file)) {
				if (sourceparam != null && !"".equals(sourceparam)) {
					//增加 导入所有后缀名称的文件
					
					if(sourceparam.endsWith(".txt")){
						if(fgbj==null||fgbj.trim().equals("")){
							return new RetMessage(false,"分割标记不能为空");
						}
					}
					if(sourceparam.endsWith(".xml")){
						if(specialxml!=null&&specialxml.length>=2){
							for(int k=0;k<specialxml.length;k++){
								DipDatarecSpecialTab special0=specialxml[0];
								DipDatarecSpecialTab special1=specialxml[1];
								if(special0!=null&&special1!=null){
									if(special0.getNodenumber()==1&&special0.getValue()!=null&&special0.getValue().trim().length()>0&&
											special1.getNodenumber()==2&&special1.getValue()!=null&&special1.getValue().trim().length()>0){
										//表示符合校验规则
									}else{
										return new RetMessage(false,"xml格式文件必须要有根标签和一级标签");
									}
								}else{
									return new RetMessage(false,"xml格式文件必须要有根标签和一级标签");
								}
							}
						}else{
							return new RetMessage(false,"xml格式文件必须要有根标签和一级标签");
							
						}
					}
					//liyunzhe add 同步文件夹下的所有后缀名称类型文件
					if(sourceparam.startsWith(".")&&!sourceparam.endsWith(".")){
						RetMessage fileNameRet=query.getFileNamesList(file, sourceparam);
						if(!fileNameRet.getIssucc()){
							return fileNameRet;
						}else{
							List listfileName=fileNameRet.getfilename();
							RetMessage fileRetMessage=new RetMessage();
							StringBuffer fileMessageSb=new StringBuffer();
							List<String> fileMessageList=new ArrayList<String>();
							int count=0;
							if(listfileName!=null&&listfileName.size()>0){
								for(int g=0;g<listfileName.size();g++){
									if(listfileName.get(g)!=null){
										String filename=listfileName.get(g).toString();
//										filepath = (file.endsWith(File.separator) ? file : file + File.separator) + filename;
										filepath = filename;
										errlist.clear();
										RetMessage dealRet=dealFile(filepath, fgbj, specialxml, hpk, dataRecHvo, hashTable, sourceparam, errlist);
										errlist.clear();
										if(dealRet!=null){
											fileRetMessage.setSu((fileRetMessage.getSu()==null?0:fileRetMessage.getSu())+(dealRet.getSu()==null?0:dealRet.getSu()));
											fileRetMessage.setHulue((fileRetMessage.getHulue()==null?0:fileRetMessage.getHulue())+(dealRet.getHulue()==null?0:dealRet.getHulue()));
											fileRetMessage.setFugai((fileRetMessage.getFugai()==null?0:fileRetMessage.getFugai())+(dealRet.getFugai()==null?0:dealRet.getFugai()));
											fileRetMessage.setInsert((fileRetMessage.getInsert()==null?0:fileRetMessage.getInsert())+(dealRet.getInsert()==null?0:dealRet.getInsert()));
											if(dealRet.getIssucc()){
												fileMessageSb.append("[成功]文件"+filepath+(dealRet.getMessage()==null?" ":" "+dealRet.getMessage()));
											}else{
												fileMessageSb.append("[失败]文件"+filepath+(dealRet.getMessage()==null?" ":" "+dealRet.getMessage()));
											}
											if(dealRet.getErrlist()!=null&&dealRet.getErrlist().size()>0){
												fileMessageSb.append("[");
												for(int a=0;a<dealRet.getErrlist().size();a++){
													fileMessageSb.append(dealRet.getErrlist().get(a)+",");
													if(fileMessageSb.length()>1900||dealRet.getErrlist().size()-1==a){
														fileMessageList.add(fileMessageSb.toString().subSequence(0, fileMessageSb.toString().length()-1)+"]--"+IContrastUtil.DATAPROCESS_HINT);
														fileMessageSb=new StringBuffer();
													}
												}
											}else{
												fileMessageList.add(fileMessageSb+"--"+IContrastUtil.DATAPROCESS_HINT);
												fileMessageSb=new StringBuffer();
											}
											count++;
										}	
									}
								}
								fileRetMessage.setIssucc(true);
								
								
								StringBuffer sbb=new StringBuffer();
								if(fileRetMessage.getSu()!=null&&fileRetMessage.getSu()>0){
									String instr="";
									String fugaistr="";
									String huluestr="";
									//String shibaistr=err.size()==0?"":"失败"+err.size()+"条记录,";
									String ss="";
									if(fileRetMessage.getInsert()!=null&&fileRetMessage.getInsert()>0){
										instr="插入"+fileRetMessage.getInsert()+"条记录,";
									}
									if(fileRetMessage.getFugai()!=null&&fileRetMessage.getFugai()>0){
										fugaistr="覆盖"+fileRetMessage.getFugai()+"条记录,";
									}
									if(fileRetMessage.getHulue()!=null&&fileRetMessage.getHulue()>0){
										huluestr="忽略"+fileRetMessage.getHulue()+"条记录,";
										
									}
									ss=instr+fugaistr+huluestr;
									if(ss!=null&&!ss.equals("")){
									    ss=ss.substring(0,ss.lastIndexOf(","));
										ss="其中"+ss+"!";
									}
									sbb.append("[成功处理"+fileRetMessage.getSu()+"条记录：");
									sbb.append(ss+"]");
								}
								fileRetMessage.setMessage("成功处理"+count+"个"+sourceparam+"文件!"+sbb.toString());
								fileRetMessage.setErrlist(fileMessageList);
								//errlist=new ArrayList<String>();
								errlist.clear();
								errlist.addAll(fileMessageList);
								//errlist=fileMessageList;
								return fileRetMessage;
							}else{
								return new RetMessage(false,file+"路径下没有找到"+sourceparam+"类型文件");
							}
						}
					}else{
						filepath = (file.endsWith("/") ? file : file + "/") + sourceparam;// 文件完整路径
						try {
							boolean flag=getIcCurrBusProp(filepath, xtbz, zdbz, ywbz);
							if(!flag){
								return new RetMessage(false,"指定的文件不是对应的系统、站点、业务的对应文件");
							}
							
							vo = radeFile(filepath,i,fgbj,specialxml);
							i++;
						} catch (Exception e) {
							e.printStackTrace();
							return new RetMessage(false, "读取文件错误！" + e.getMessage());
						}
						if (vo == null) {
							return new RetMessage(false, "没有读取到正确的数据");
						}
						return inertTxtDataVO(hpk, vo, dataRecHvo, hashTable,((file.endsWith("/") ? file : file + "/")+"err_"+sourceparam+new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date())),errlist);
					}
					
				}else{
					return new RetMessage(false,"数据来源参数不能为空");
				}
			}
			return new RetMessage(false,"没有找到相应的文件"+filepath);
		}
	}
	//只是数据在处理某类型文件时候引用这个方法。例如把所有excel文件插入到表中。
	/**
	 * 
	 */
	
	public RetMessage dealFile(String filepath,String fgbj,DipDatarecSpecialTab specialxml[],String hpk ,DipDatarecHVO dataRecHvo,
			                   Hashtable<String, DipDatadefinitBVO> hashTable,String sourceparam ,List<String> errlist){
		
		TxtDataVO vo = null;
		int i=0;//文件数量
		try {
			vo = radeFile(filepath,i,fgbj,specialxml);
			i++;
		} catch (Exception e) {
			e.printStackTrace();
			return new RetMessage(false, "读取"+filepath+"文件错误！" + e.getMessage());
		}
		if (vo == null) {
			 return  new RetMessage(false, "读取"+filepath+"为空");
		}else{
			//如果为null就不用把错误日志写到自己定义的日志文件中
			return inertTxtDataVOByAllPrex(hpk, vo, dataRecHvo, hashTable,null,errlist);
		}
	}
	
	
	 /**
     * used to ajust file path; path returned will be like "/.../..." 。
     * 创建日期：(2003-3-5 10:23:55)
     *
     * @return java.lang.String
     * @param path
     *            java.lang.String
     */
    public  String convertFilePath(String path) {

        path = path.replace('\\', '/');

        while (path.indexOf("//") > 0) {
            nc.ui.pub.beans.util.StringReplace.replace(path, "//", "/");
        }

        if (!path.startsWith("/"))
            path = "/" + path;

        if (path.endsWith("/"))
            path = path.substring(0, path.length() - 1);

        return path;
    }
/**
 * @desc 前台同步的代码,把文件同步到数据库
 * */
	public RetMessage msgToStyle(String hpk, String file,String pk_datarec,List<String> errlist) {
		file= convertFilePath(file);
		file=RuntimeEnv.getInstance().getNCHome()+ "/temp" + file.substring( file
				.lastIndexOf("/"));
		// 1.判断哪种格式:.xml、.txt、.xls
		DipDatarecHVO dataRecHvo = null;
		DipDatarecSpecialTab specialxml[]=null;
		try {// 数据接收定义主表vo
			dataRecHvo = (DipDatarecHVO) getBaseDAO().retrieveByPK( DipDatarecHVO.class, pk_datarec);
			specialxml=(DipDatarecSpecialTab[]) HYPubBO_Client.queryByCondition(DipDatarecSpecialTab.class, "pk_datarec_h='"+pk_datarec+"' and nvl(dr,0)=0 and is_xml='Y'");
		} catch (Exception e2) {
			e2.printStackTrace();
			return new RetMessage(false, "查询相应的数据接收定义错误！" + e2.getMessage()+e2.getStackTrace()[0]);
		}
		String fgbj=",";
		String filepath = "";
		// 数据来源参数

		if (dataRecHvo == null) {// 数据接收定义主表vo
			return new RetMessage(false, "没有找到对应的数据接收定义！");
		} else {
			String phyname = dataRecHvo.getDataname();// 物理表名
			HashMap<String, String> map = new HashMap<String, String>();
			DataformatdefHVO fhvo = null;
			try {// 数据格式定义表头
				String pk = "select pk_dataformatdef_h from dip_dataformatdef_h where pk_datarec_h='" + pk_datarec + "' and rownum<=1 and nvl(dr,0)=0";
				pk = query.queryfield(pk);
				fhvo = (DataformatdefHVO) getBaseDAO().retrieveByPK(
						DataformatdefHVO.class, pk);
			} catch (Exception e2) {
				e2.printStackTrace();
				return new RetMessage(false, "查询相应的数据格式定义错误！" + e2.getMessage());
			}
			Hashtable<String, DipDatadefinitBVO> hashTable = new Hashtable<String, DipDatadefinitBVO>();
			if (fhvo == null) {
				return new RetMessage(false, "没有找到相应的格式定义！");
			} else {
				String fflog="select cdata from dip_messagelogo where pk_messagelogo='"+fhvo.getBeginsign()+"'";
					try {
						fgbj=iqf.queryfield(fflog);
					} catch (BusinessException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (DbException e) {
						e.printStackTrace();
					}
				List<DataformatdefBVO> fbvo = null;
				try {
					fbvo = (List<DataformatdefBVO>) getBaseDAO() .retrieveByClause( DataformatdefBVO.class, "pk_dataformatdef_h='"  + fhvo.getPrimaryKey() + "' and datakind is not null and nvl(dr,0)=0 order by code");
				} catch (DAOException e2) {
					e2.printStackTrace();
					return new RetMessage(false, "查询相应的数据格式定义错误！"
							+ e2.getMessage());
				}
				if (fbvo == null || fbvo.size() <= 0) {// 数据格式定义表体
					return new RetMessage(false, "没有找到相应的格式定义的格式！");
				}
				// 0：顺序，1：字段对照
				Integer datamaptye = Integer.parseInt(fhvo.getMessflowdef());// 是顺序对照还是字段对照
				for (DataformatdefBVO dfbvo : fbvo) {// 对照的map
					if (dfbvo.getDatakind() != null && !"".equals(dfbvo.getDatakind())) {
						if (datamaptye == 0) {
							map.put(dfbvo.getCode() + "", dfbvo.getDatakind());
						} else {
							map.put(dfbvo.getCorreskind(), dfbvo.getDatakind());
						}
					}
				}
				dataRecHvo.setAttributeValue("datamaptype", datamaptye);
				dataRecHvo.setAttributeValue("map", map);
				dataRecHvo.setAttributeValue("tablename", phyname);
				String memorytable = dataRecHvo.getMemorytable();
				// 2、查数据定义
				List<DipDatadefinitBVO> dataDefinitBvo = null;
				try {// 数据定义表体VO
					dataDefinitBvo = (List<DipDatadefinitBVO>) getBaseDAO().retrieveByClause( DipDatadefinitBVO.class, "pk_datadefinit_h='" + memorytable + "' and nvl(dr,0)=0");
				} catch (DAOException e1) {
					e1.printStackTrace();
					return new RetMessage(false, "查询数据数据格式定义错误！" + e1.getMessage());
				}
				if (dataDefinitBvo == null || dataDefinitBvo.size() <= 0) {
					return new RetMessage(false, "数据定义中没有找到表的结构定义！");
				}
				for (DipDatadefinitBVO bvo : dataDefinitBvo) {
					hashTable.put(bvo.getEname(), bvo);
					if (bvo.getIspk() != null && bvo.getIspk().booleanValue()) {
						hashTable.put("#PKFIELD#", bvo);
					}
				}
			}
			int i=0;
			TxtDataVO vo = null;
			String sourceparam = dataRecHvo.getSourceparam();
			if (file != null && !"".equals(file)) {
				try {
					vo = radeFile(file,i,fgbj,specialxml);
					i++;
				} catch (Exception e) {
					e.printStackTrace();
					return new RetMessage(false, "读取文件错误！" + e.getMessage());
				}
				if (vo == null) {
					return new RetMessage(true, "");
				}
				return inertTxtDataVO(hpk, vo, dataRecHvo, hashTable,((file.endsWith("/") ? file : file + "/")+"err_"+sourceparam+new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date())),errlist);
			}
		}
				return new RetMessage(false,"没有找到相应的文件"+filepath);
	}
	/**
	 * @desc 数据同步  把数据库的文件同步到前台
	 * @param hpk 数据同步的主表主键
	 * @param 
	 * */
		public RetMessage dataToFile(String hpk, String pk_datarec,List<String> errlist) {
			DipDatarecHVO vo = null;
			try {
				vo = (DipDatarecHVO) getBaseDAO().retrieveByPK(DipDatarecHVO.class, pk_datarec);
			} catch (DAOException e1) {
				e1.printStackTrace();
			}
			RetMessage returnrt=new RetMessage(true,"成功");
			if (null != vo) {
				// 0：顺序，1：字段对照
				// 数据字段
				try {
					
					String phyname = vo.getDataname();// 物理表名
					// 数据来源连接(dataSourse)
					String sourceurl = vo.getSourcecon();
					//文件命名规则
					
					// 存储表名
					String memorytable = vo.getMemorytable();
					DataformatdefHVO fhvo = null;
					List<DataformatdefHVO> listfhvo=(List<DataformatdefHVO>) getBaseDAO().retrieveByClause(DataformatdefHVO.class, "pk_datarec_h='" + vo.getPk_datarec_h() + "' and nvl(dr,0)=0");
					if(listfhvo==null||listfhvo.size()<1){
						return new RetMessage(false, "没有找到相应的格式定义的格式！");
					}
					DipSysregisterHVO xt=(DipSysregisterHVO) getBaseDAO().retrieveByPK(DipSysregisterHVO.class, vo.getPk_xt());
					if(xt==null){
						return new RetMessage(false, "没有找到相应的系统！");
					}
					DipDatadefinitHVO ddfhvo=(DipDatadefinitHVO) getBaseDAO().retrieveByPK(DipDatadefinitHVO.class, memorytable);
					if(ddfhvo==null){
						return new RetMessage(false, "没有找到相应的数据定义！");
					}
					String xtbz=xt.getExtcode();//系统标志
					String zdzj=ddfhvo.getDeploycode();
					String zdbz="";//站点标志
					if(zdzj==null||zdzj.length()<=0){
						zdbz=xt.getDef_str_1();
					}else{
						DipSysregisterBVO sbvo=(DipSysregisterBVO) getBaseDAO().retrieveByPK(DipSysregisterBVO.class, zdzj);
						if(sbvo==null){
							return new RetMessage(false, "没有找到相应的站点！");
						}
						zdbz=sbvo.getSitecode();
					}
					String ywbz=ddfhvo.getBusicode();//业务标志
					List<DipDatarecSpecialTab> listspecialhvo=(List<DipDatarecSpecialTab>) getBaseDAO().retrieveByClause(DipDatarecSpecialTab.class, "pk_datarec_h='" + vo.getPk_datarec_h() + "' and (nvl(dr,0)=0 ) and is_xtfixed='Y' ");		
					if(listspecialhvo!=null&&listspecialhvo.size()>0){
						for(int i=0;i<listspecialhvo.size();i++){
							DipDatarecSpecialTab specialvo=listspecialhvo.get(i);
							if(specialvo.getName().equals("系统标志")){
								String xtvalue=specialvo.getValue();
								if(xtvalue!=null&&xtvalue.trim().length()>0){
									xtbz=xtvalue;
								}
							}
							if(specialvo.getName().equals("站点标志")){
								String zdvalue=specialvo.getValue();
								if(zdvalue!=null&&zdvalue.trim().length()>0){
									zdbz=zdvalue;
								}
							}
							if(specialvo.getName().equals("业务标志")){
								String ywvalue=specialvo.getValue();
								if(ywvalue!=null&&ywvalue.trim().length()>0){
									ywbz=ywvalue;
								}
							}
						}
						
						
					}
					List<DipDatarecSpecialTab> listspecialxmlhvo=(List<DipDatarecSpecialTab>) getBaseDAO().retrieveByClause(DipDatarecSpecialTab.class, "pk_datarec_h='" + vo.getPk_datarec_h() + "' and (nvl(dr,0)=0 ) and is_xml='Y' order by nodenumber ");		
//					if(listspecialhvo!=null&&listspecialhvo.size()>0){
//						for(int i=0;i<listspecialhvo.size();i++){
//							DipDatarecSpecialTab specialvo=listspecialhvo.get(i);
//							if(specialvo.getName().equals("系统标志")){
//								String xtvalue=specialvo.getValue();
//								if(xtvalue!=null&&xtvalue.trim().length()>0){
//									xtbz=xtvalue;
//								}
//							}
//							if(specialvo.getName().equals("站点标志")){
//								String zdvalue=specialvo.getValue();
//								if(zdvalue!=null&&zdvalue.trim().length()>0){
//									zdbz=zdvalue;
//								}
//							}
//							if(specialvo.getName().equals("业务标志")){
//								String ywvalue=specialvo.getValue();
//								if(ywvalue!=null&&ywvalue.trim().length()>0){
//									ywbz=ywvalue;
//								}
//							}
//						}
//						
//						
//					}
					
					List<String> ziduan=null;
					StringBuffer sql=new StringBuffer();
					List<DataformatdefBVO> listbvos=null;
					fhvo=listfhvo.get(0);
					listbvos=(List<DataformatdefBVO>) getBaseDAO().retrieveByClause(DataformatdefBVO.class, "pk_dataformatdef_h='"+fhvo.getPrimaryKey()+"' order by code");
					if(listbvos==null||listbvos.size()<=0){
						return new RetMessage(false, "没有找到相应的格式定义的格式！");
					}
					String fgbj=null;
					String fgbje=null;
					if(fhvo.getFiletype()==0){
						String fflog="select cdata from dip_messagelogo where pk_messagelogo='"+fhvo.getBeginsign()+"'";
						try {
							fgbj=iqf.queryfield(fflog);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if(fgbj==null||fgbj.length()<=0){
							return new RetMessage(false, "没有找到相应的分割标记！");
						}
//						if(fhvo.getFiletype()==1){
//							fflog="select cdata from dip_messagelogo where pk_messagelogo='"+fhvo.getEndsign()+"'";
//							try {
//								fgbje=iqf.queryfield(fflog);
//							} catch (SQLException e) {
//								e.printStackTrace();
//							} catch (DbException e) {
//								e.printStackTrace();
//							}
//							if(fgbje==null||fgbje.length()<=0){
//								return new RetMessage(false, "没有找到相应的二级节点标记！");
//							}
//						}
					}
					if(fhvo.getFiletype()==1){
						if(listspecialxmlhvo!=null&&listspecialxmlhvo.size()==3){
//							for(int i=0;i<listspecialxmlhvo.size();i++){
							if(listspecialxmlhvo.get(0).getValue()!=null&&listspecialxmlhvo.get(0).getValue().trim().length()>0&&
									listspecialxmlhvo.get(1).getValue()!=null&&listspecialxmlhvo.get(1).getValue().trim().length()>0
							){
								
							}else{
								return new RetMessage(false,"没有找到根节点或者一级节点标记");
							}
//							}
						}
					}
					int i=0;
					ziduan=new ArrayList<String>();
					Map<String,DataformatdefBVO> bmap=new HashMap<String, DataformatdefBVO>();
					for(DataformatdefBVO bvoi:listbvos){
						if(bvoi.getVdef2().equals("业务数据")){
							ziduan.add(bvoi.getDatakind().toUpperCase()+i);
							sql.append(bvoi.getDatakind().toUpperCase()+" "+bvoi.getDatakind().toUpperCase()+i+",");
						}else if(bvoi.getVdef2().equals("自定义")||bvoi.getVdef2().equals("标志头")||bvoi.getVdef2().equals("标志尾")){
							ziduan.add((bvoi.getDatakind()==null?"kg":bvoi.getDatakind()).toUpperCase()+i);
							sql.append("'"+(bvoi.getDatakind()==null?"":bvoi.getDatakind()).toUpperCase()+"' "+(bvoi.getDatakind()==null?"kg":bvoi.getDatakind()).toUpperCase()+i+",");
						}else if(bvoi.getVdef2().equals("固定标志")){
							if(bvoi.getVdef3().equals("系统标志")){
								ziduan.add("xtbz_".toUpperCase()+i);
								sql.append("'"+xtbz+"' "+"xtbz_"+i+",");
							}else if(bvoi.getVdef3().equals("站点标志")){
								ziduan.add("zdbz_".toUpperCase()+i);
								sql.append("'"+zdbz+"' "+"zdbz_"+i+",");
							}else if(bvoi.getVdef3().equals("业务标志")){
								ziduan.add("ywbz_".toUpperCase()+i);
								sql.append("'"+ywbz+"' "+"ywbz_"+i+",");
							}
						}else if(bvoi.getVdef2().equals("时间函数")){
							ziduan.add("sjhs_".toUpperCase()+i);
							sql.append("'"+bvoi.getDatakind()+"' "+"sjhs_"+i+",");
						}else if(bvoi.getVdef2().equals("记录数函数")){
							ziduan.add("jlshs_".toUpperCase()+i);
							sql.append("'"+bvoi.getDatakind()+"' "+"jlshs_"+i+",");
						}else if(bvoi.getVdef2().equals("主键函数")){
							ziduan.add("zjhs_".toUpperCase()+i);
							sql.append("'"+bvoi.getDatakind()+"' "+"zjhs_"+i+",");
						}
						bmap.put(ziduan.get(i), (DataformatdefBVO) bvoi.clone());
						i++;
					}
					String querysql="select "+sql.substring(0,sql.length()-1)+" from "+phyname;
					String countsql="select count(*) from "+phyname;
					String count = iqf.queryfield(countsql);
					int s=Integer.parseInt(count);
					if(s==0){
						return new RetMessage(false, "没有符合条件的数据！");
					}
					String filename="";
						filename=getFileName(vo,xtbz,zdbz,ywbz,phyname);
						String pathth="select descriptions from dip_filepath where pk_filepath='"+sourceurl+"'";
						String lj=iqf.queryfield(pathth);
						File file = new File(lj); 
						if(!file.exists()){
							file.mkdirs();
						}else{
							if (!file.isDirectory()) { 
							Logger.debug("1"); 
							} else if (file.isDirectory()) { 
								Logger.debug("2"); 
								String[] filelist = file.list(); 
								for (int k = 0; k < filelist.length; k++) { 
									File delfile = new File(lj + "/" + filelist[k]); 
									if (!delfile.isDirectory()) { 
										Logger.debug("path=" + delfile.getPath()); 
										Logger.debug("absolutepath=" + delfile.getAbsolutePath()); 
										Logger.debug("name=" + delfile.getName()); 
										if(delfile.getName().startsWith(filename.replace("_t_", "")));{
											delfile.delete(); 
											Logger.debug("删除文件成功"); 
										}
									} 
								}
							}
						}
						filename=lj+"/"+filename;
						String code="GBK";
						String wjmname="";
					if(s>50000){
						int k=s%50000==0?s/50000:s/50000+1;
						for(int kkk=0;kkk<k;kkk++){
							String sqltemp=sql.substring(0,sql.length()-1);
							String[] sqls=sqltemp.split(",");
							String sqlt="";
							for(int t=0;t<sqls.length;t++){
								sqlt=sqlt+(sqls[t].indexOf(" ")>0?sqls[t].split(" ")[1]:sqls[t])+",";
							}
							String sss="select "+sqlt.substring(0,sqlt.length()-1) +" from ( "
	                      +	"select "+sql+"rownum rwn  from "+phyname+" where rownum<="+((kkk+1)*50000>s?s:(kkk+1)*50000)
	                      +" )  where rwn>"+kkk*50000;
							List<HashMap> list=new ArrayList();
							try{
								list = iqf.queryListMapSingleSql(sss);
							}catch(Exception e){
								return new RetMessage(false,"查询数据出错"+e.getMessage());
							}
							RetMessage rt=null;
							if(list!=null&&list.size()>0){

								String ft="";
								String fl="";
								if(fhvo.getFiletype()==0){
									ft="txt";
									fl=filename.replace("_t_", "_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))+(kkk>=0?kkk+"_":"")+"."+ft;
									rt= writetoTxtFile(list,fgbj,ziduan,bmap,code,fl,s,fhvo);
								}else if(fhvo.getFiletype()==1){
									ft="xml";
									fl=filename.replace("_t_", "_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))+(kkk>=0?kkk+"_":"")+"."+ft;
									rt= writetoXmlFile(list, ziduan, listspecialxmlhvo, code, fl, bmap, fhvo,s);
								}else {
									ft="xls";
									fl=filename.replace("_t_", "_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))+(kkk>=0?kkk+"_":"")+"."+ft;
									rt= writetoExcelFile(list,ziduan,fl,bmap,fhvo,s);
								}
								returnrt.setFileName(fl);
								if(rt.getIssucc()){
									rt=writeToPropFile(fl, code, xtbz, zdbz, ywbz, phyname, s, list.size());
								}
							   wjmname=wjmname+fl+",";
								if(!rt.getIssucc()){
									return rt;
								}
							}
						}
	        		}else{
						List<HashMap> list=new ArrayList();
						try{
							list = iqf.queryListMapSingleSql(querysql);
						}catch(Exception e){
							return new RetMessage(false,"查询数据出错"+e.getMessage());
						}
						RetMessage rt=null;
						if(list!=null&&list.size()>0){
							String ft="";
							String fl="";
							if(fhvo.getFiletype()==0){
								ft="txt";
								fl=filename.replace("_t_", "_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))+"."+ft;
								rt= writetoTxtFile(list,fgbj,ziduan,bmap,code,fl,s,fhvo);
							}else if(fhvo.getFiletype()==1){
								ft="xml";
								fl=filename.replace("_t_", "_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))+"."+ft;
								rt= writetoXmlFile(list, ziduan, listspecialxmlhvo, code, fl, bmap, fhvo,s);
							}else {
								ft="xls";
								fl=filename.replace("_t_", "_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))+"."+ft;
								rt= writetoExcelFile(list,ziduan,fl,bmap,fhvo,s);
							}
							returnrt.setFileName(fl);
							if(rt.getIssucc()){
								rt=writeToPropFile(fl, code, xtbz, zdbz, ywbz, phyname, s, list.size());
							}
							   wjmname=wjmname+fl+",";
							if(!rt.getIssucc()){
								return rt;
							}
						}
					}
					boolean isneadprop=true;
					if(isneadprop){}
				} catch (SQLException e) {
					e.printStackTrace();
				}  catch (DbException e) {
					e.printStackTrace();
				}catch (DAOException e2) {
					e2.printStackTrace();
					return new RetMessage(false, "查询相应的数据格式定义错误！"
							+ e2.getMessage());
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				return returnrt;
			} else {
				Logger.error("条件不足，无法进行相应操作！");
				return new RetMessage(false, "条件不足，无法进行相应操作！");
			}

		}
		/**
		 * @desc 往prop文件里边写
		 * @param filename prop文件对应的文件的文件名
		 * @param code 文件的编码集
		 * @param xtbz 系统标志
		 * @param zdbz 站点标志
		 * @param ywbz 业务标志
		 * @param phyname 表名
		 * @param s 总共的条数
		 * @param num 本文件的条数
		 * **/
		public RetMessage writeToPropFile(String filename,String code,String xtbz,String zdbz,String ywbz,String phyname,int s,int num){

			String pf="";
			int index=filename.indexOf(".");
			
			pf=(index>=0?filename.substring(0,index):filename)+".properties";
			FileOutputStream fos=null;
			Writer ra=null ;
			try {
				fos = new FileOutputStream(pf);
				ra = new OutputStreamWriter(fos, code==null?"GBK":code);
				try {
					ra.write("xtbz="+xtbz+"\n");
					ra.write("zdbz="+zdbz+"\n");
					ra.write("ywbz="+ywbz+"\n");
					ra.write("tablename="+phyname+"\n");
					ra.write("datasumnum="+s+"\n");
					ra.write("datanum="+num+"\n");
					ra.write("wjlname="+filename);
				} catch (IOException e) {
					e.printStackTrace();
					return new RetMessage(false,"往properties文件里写失败"+e.getMessage());
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return new RetMessage(false,"往properties文件里写失败，找不到路径"+e.getMessage());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return new RetMessage(false,"往properties文件里写失败，不支持的流"+e.getMessage());
			}finally{
				if(ra!=null){
					try {
						ra.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(fos!=null){
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			RetMessage ret=new RetMessage(true,"写"+pf+"配置文件成功");
			ret.setValue(pf);
			
		return ret;
		}
		/*public RetMessage writeToFileFromList(List<HashMap> list,List<String> ziduan,String filename,String fgbj,String fg2,Integer filetype,Map<String,DataformatdefBVO> bmap,int kkk,DataformatdefHVO fhvo){
			String code="GBK";
			String ft="";
			if(filetype==0){
				ft="txt";
				String fl=filename.replace("_t_", "_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))+(kkk>=0?kkk+"_":"")+"."+ft;
				return writetoTxtFile(list,fgbj,ziduan,code,fl);
			}else if(filetype==1){
				ft="xml";
				String fl=filename.replace("_t_", "_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))+(kkk>=0?kkk+"_":"")+"."+ft;
				return writetoXmlFile(list, ziduan, fgbj, fg2, code, fl, bmap, fhvo);
			}else {
				ft="xls";
				String fl=filename.replace("_t_", "_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))+(kkk>=0?kkk+"_":"")+"."+ft;
				return writetoExcelFile(list,ziduan,fl,bmap,fhvo);
			}
		}*/
		public RetMessage writetoExcelFile(List<HashMap> list,List<String> ziduan,String fl,Map<String,DataformatdefBVO> bmap,DataformatdefHVO fhvo,int s){
				
				ExpExcelVO[] vosW = null;
				if(list!=null && list.size()>0){
					List<ExpExcelVO> listW = new ArrayList<ExpExcelVO>();
					for(int i = 0;i<list.size();i++){
						HashMap map = (HashMap)list.get(i);
						ExpExcelVO	vo = new ExpExcelVO();

						for(int j = 0;j<ziduan.size();j++){
							String value= map.get(ziduan.get(j))==null?"":map.get(ziduan.get(j))+"";
							String ziduani=ziduan.get(j).toLowerCase();
							if(ziduani.startsWith("jlshs_")){
								value=s+"";
							}else if(ziduani.startsWith("sjhs_")){
								value=new SimpleDateFormat(value).format(new Date());
							}else if(ziduani.startsWith("zjhs_")){
								value=query.getOID();
							}
							vo.setAttributeValue("line"+Integer.valueOf(j+1),value);
						}
						listW.add(vo);
					}

					vosW = listW.toArray(new ExpExcelVO[0]);
					File file = new File(fl);	
					if(!file.exists()){
						try {
							file.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
							return new RetMessage(false,"创建文件失败:"+fl);
						}	
					}
					String fielPathTemp = fl.substring(0,fl.length()-4)+"-1.xls";
					String filtype=fhvo.getMessflowdef();
					String cname="";
					for(String ename:ziduan){
						DataformatdefBVO bvoi=bmap.get(ename);
						if(filtype.equals("0")){
							cname=cname+bvoi.getVdef3()+",";
						}else{
							cname=cname+bvoi.getCorreskind()+",";
						}
					}
					try {
						ExpToExcel toexcle = new ExpToExcel(fl,"数据对照结果",cname.substring(0,cname.length()-1),vosW,fielPathTemp);
					} catch (IOException e) {
						e.printStackTrace();
						
						return new RetMessage(false,"写入Excle文件失败");
					}
				}
			return new RetMessage(true,"成功");
		}
		public RetMessage writetoXmlFile(List<HashMap> list,List<String> ziduan,List<DipDatarecSpecialTab> specialxmlvos,String code,String fl,Map<String,DataformatdefBVO> bmap,DataformatdefHVO fhvo,int s){
			String rootname="";
			String rootpropertys="";
			String onenodename="";
			String onepropertys="";
			String twonodename="";
			String twopropertys="";
			String filtype=fhvo.getMessflowdef();
			if(specialxmlvos==null||specialxmlvos.size()!=3){
				return new RetMessage(false,"没有找到根标签和一级标签");
			}else{
				if(specialxmlvos.get(0)!=null&&specialxmlvos.get(0).getValue()!=null&&specialxmlvos.get(0).getValue().trim().length()>0){
					rootname=specialxmlvos.get(0).getValue().trim();
				}else{
					return new RetMessage(false,"根标签不能为空");
				}
				if(specialxmlvos.get(0)!=null&&specialxmlvos.get(0).getNodeproperty()!=null&&specialxmlvos.get(0).getNodeproperty().trim().length()>0){
					rootpropertys=specialxmlvos.get(0).getNodeproperty().trim();
					rootpropertys=rootpropertys.replace("\"", "");
				}
				if(specialxmlvos.get(1)!=null&&specialxmlvos.get(1).getValue()!=null&&specialxmlvos.get(1).getValue().trim().length()>0){
					onenodename=specialxmlvos.get(1).getValue().trim();
				}else{
					return new RetMessage(false,"一级标签不能为空");
				}
				if(specialxmlvos.get(1)!=null&&specialxmlvos.get(1).getNodeproperty()!=null&&specialxmlvos.get(1).getNodeproperty().trim().length()>0){
					onepropertys=specialxmlvos.get(1).getNodeproperty().trim();
					onepropertys=onepropertys.replace("\"", "");
				}
				if(specialxmlvos.get(2)!=null&&specialxmlvos.get(2).getValue()!=null&&specialxmlvos.get(2).getValue().trim().length()>0){
					twonodename=specialxmlvos.get(2).getValue().trim();
				}
				if(specialxmlvos.get(2)!=null&&specialxmlvos.get(2).getNodeproperty()!=null&&specialxmlvos.get(2).getNodeproperty().trim().length()>0){
					twopropertys=specialxmlvos.get(2).getNodeproperty().trim();
					twopropertys=twopropertys.replace("\"", "");
				}
				
			}
			Document rootDoc = DocumentHelper.createDocument();
			Element rootEle=rootDoc.addElement(rootname);
			if(!rootpropertys.trim().equals("")&&rootpropertys.contains("=")){
				addXmlPropertys(rootEle, rootpropertys);
//				if(rootpropertys.contains(",")){
//					String str[]=rootpropertys.split(",");
//					if(str!=null&&str.length>0){
//						for(int i=0;i<str.length;i++){
//							String ss=str[i];
//							if(ss!=null&&ss.contains("=")){
//								String sp1[]=ss.split("=");
//							    if(sp1!=null&&sp1.length==2){
//							    	rootEle.addAttribute(sp1[0], sp1[1]);	
//							    }
//							}
//						}
//					}
//				}else{
//					if(rootpropertys.contains("=")&&rootpropertys.trim().length()>2){
//						if(rootpropertys.split("=")!=null&&rootpropertys.split("=").length==2){
//							rootEle.addAttribute(rootpropertys.split("=")[0], rootpropertys.split("=")[1]);	
//						}
//						
//					}
//					
//				}
				
			}
//			&lt; < 小于号 
//			&gt; > 大于号 
//			&amp; & 和 
//			&apos; ' 单引号 
//			&quot; " 双引号 

//			rootEle.addAttribute("version", "1.1");
//			rootEle.addAttribute("encoding", code);
			Element head=null;
			if(!twonodename.trim().equals("")){
				 head=rootEle.addElement(onenodename);
				if(!onepropertys.trim().equals("")&&onepropertys.contains("=")){
//					head.addAttribute(onepropertys.split("=")[0], onepropertys.split("=")[1]);
					addXmlPropertys(head, onepropertys);
					
				}
			}
			for(int i=0;i<list.size();i++){
//				Element head=rootEle.addElement(onenodename);
//					if(!onepropertys.trim().equals("")&&onepropertys.contains("=")){
//						head.addAttribute(onepropertys.split("=")[0], onepropertys.split("=")[1]);
//					}
				 Element two=null;
					if(!twonodename.trim().equals("")){
						two=head.addElement(twonodename);
						if(!twopropertys.trim().equals("")&&twopropertys.contains("=")){
							addXmlPropertys(two, twopropertys);
//							two.addAttribute(twopropertys.split("=")[0], twopropertys.split("=")[1]);
						}
					 }else{
						 head=rootEle.addElement(onenodename);
							if(!onepropertys.trim().equals("")&&onepropertys.contains("=")){
//								head.addAttribute(onepropertys.split("=")[0], onepropertys.split("=")[1]);
								addXmlPropertys(head, onepropertys);
							}
					 }
				for(int j=0;j<ziduan.size();j++){
					String ziduani=ziduan.get(j);
					String value=((list.get(i).get(ziduan.get(j))==null?"":list.get(i).get(ziduan.get(j))))+"";
					DataformatdefBVO bvoi=bmap.get(ziduani);
					ziduani=ziduani.toLowerCase();
					if(ziduani.startsWith("jlshs_")){
						value=s+"";
					}else if(ziduani.startsWith("sjhs_")){
						value=new SimpleDateFormat(value).format(new Date());
					}else if(ziduani.startsWith("zjhs_")){
						value=query.getOID();
					}
					String text=filtype.equals("0")?bvoi.getDatakind():bvoi.getCorreskind();
					if(two==null){
						head.addElement(text).setText(value);	
					}else{
						two.addElement(text).setText(value);
					}	
					
				}
			}
			org.dom4j.io.XMLWriter out =null;
			FileOutputStream fos=null;
			Writer ra=null ;
			try {
//				fos = new FileOutputStream(fl);
//				ra = new OutputStreamWriter(fos, code);
//					try {
						new File(fl).createNewFile();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					OutputFormat format = OutputFormat.createPrettyPrint();
					fos=new FileOutputStream(fl);
					ra=new OutputStreamWriter(fos, code);
					out = new org.dom4j.io.XMLWriter(ra,format);
					out.write(rootDoc);
//					
//					String str=rootDoc.getRootElement().asXML();
//					str=str.replaceAll("&quot;", "\"");
//					ra.write("<?xml version=\"1.0\" encoding=\""+code+"\"?>"+str);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return new RetMessage(false,"往文件里写失败，找不到路径"+e.getMessage());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return new RetMessage(false,"往文件里写失败，不支持的流"+e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new RetMessage(false,"写xml文件失败");
			}finally{
				if(ra!=null){
					try {
						ra.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(fos!=null){
					try {
						fos.close();
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
			return new RetMessage(true,"成功");
		}
		public void addXmlPropertys(Element elt,String propertys){
			if(propertys.contains(",")){
				String str[]=propertys.split(",");
				if(str!=null&&str.length>0){
					for(int i=0;i<str.length;i++){
						String ss=str[i];
						if(ss!=null&&ss.contains("=")){
							String sp1[]=ss.split("=");
						    if(sp1!=null&&sp1.length==2){
						    	elt.addAttribute(sp1[0], sp1[1]);	
						    }
						}
					}
				}
			}else{
				if(propertys.contains("=")&&propertys.trim().length()>2){
					if(propertys.split("=")!=null&&propertys.split("=").length==2){
						elt.addAttribute(propertys.split("=")[0], propertys.split("=")[1]);	
					}
					
				}
				
			}
		}
		public RetMessage writetoTxtFile(List<HashMap>list,String fgbj,List<String> ziduan,Map<String,DataformatdefBVO> bmap,String code,String fl,int s,DataformatdefHVO fhvo){
			List<String> falist=new ArrayList<String>();
			for(int ls = 0;ls<list.size();ls++){
				HashMap mapi = (HashMap)list.get(ls);
				String mls=fgbj;
				for(int j = 0;j<ziduan.size();j++){
					String ziduant=ziduan.get(j).toLowerCase();
					String value=(mapi.get(ziduan.get(j))==null?"":mapi.get(ziduan.get(j)))+"";
					if(ziduant.startsWith("jlshs_")){
						value=s+"";
					}else if(ziduant.startsWith("sjhs_")){
						value=new SimpleDateFormat(value).format(new Date());
					}else if(ziduant.startsWith("zjhs_")){
						value=query.getOID();
					}
					mls=mls+value+fgbj;
				}
				falist.add(mls);
			}
			String filtype=fhvo.getMessflowdef();
			String cname=fgbj;
			for(String ename:ziduan){
				DataformatdefBVO bvoi=bmap.get(ename);
				if(filtype.equals("0")){
					cname=cname+bvoi.getVdef3()+fgbj;
				}else{
					cname=cname+bvoi.getCorreskind()+fgbj;
				}
			}
			FileOutputStream fos=null;
			Writer ra=null ;
			try {
				fos = new FileOutputStream(fl);
				ra = new OutputStreamWriter(fos, code);
				try {
					ra.write(cname+"\n");
					for(String str:falist){
						ra.write(str+"\n");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return new RetMessage(false,"往文件里写失败"+e.getMessage());
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return new RetMessage(false,"往文件里写失败，找不到路径"+e.getMessage());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return new RetMessage(false,"往文件里写失败，不支持的流"+e.getMessage());
			}finally{
				if(ra!=null){
					try {
						ra.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(fos!=null){
					try {
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			RetMessage rt=new RetMessage(true,"成功");
			rt.setFileName(fl);
			return new RetMessage(true,"成功");
		}
	/**
	 * 2011-5-31
	 */
	private RetMessage insertExcelVo(String hpk,String filepath, 
			DipDatarecHVO hvo, Hashtable<String, DipDatadefinitBVO> datadefinb,String errfilepath,List<String> errlist){
		RetMessage rm=null;
		if(filepath==null ||filepath.length() <= 0) {
			return new RetMessage(false,"传入的文件名是空！"+filepath);
		}
		TxtDataVO tvo = new TxtDataVO();
		FileInputStream fin=null;
		HSSFWorkbook book = null ;
		try{		
			fin=new FileInputStream(filepath);
			book= new HSSFWorkbook(fin);
		}catch (Exception e) {
			e.printStackTrace();
			return new RetMessage(false,"读取文件失败！"+filepath);
		}finally{
			try {
				if(fin!=null){
					fin.close();	
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		HSSFSheet sheet = book.getSheetAt(0);
		if(sheet == null){
			return null;
		}
		tvo.setSheetName(book.getSheetName(0));
		tvo.setStartRow(sheet.getFirstRowNum());
		tvo.setStartCol(sheet.getLeftCol());
		tvo.setRowCount(sheet.getPhysicalNumberOfRows() - 1);//去掉标题行
		tvo.setColCount((short)sheet.getRow(sheet.getFirstRowNum()).getPhysicalNumberOfCells());
		tvo.setFirstDataRow(sheet.getFirstRowNum());
		tvo.setFirstDataCol(sheet.getLeftCol());

		HSSFRow titleRow = sheet.getRow(tvo.getStartRow());
		HashMap<String, String> titlemap = new HashMap<String, String>();
		for(short i=0;i<titleRow.getPhysicalNumberOfCells();i++){
			//liyunzhe 把（String）getCellValue(titleRow.getCell(i)改成(getCellValue(titleRow.getCell(i))+"").trim()
			titlemap.put(Short.toString(i),(String)((getCellValue(titleRow.getCell(i))+"").trim()));
		}
		tvo.setTitlemap(titlemap);
		int titlesize=titlemap.size();

		int totalrow=tvo.getRowCount();//总行数
		int pc=0;//读取次数：分批读取，每次读取10000条数据
		int pamount=10000;//每批的数量
		pc=totalrow/pamount;
		for(int i=0;i<=pc;i++){
			tvo=new TxtDataVO();
			tvo.setTitlemap(titlemap);
			tvo.setRowCount(i==pc?(totalrow-pc*pamount):pamount);//去掉标题行
			for(int j=1;j<=(i==pc?tvo.getRowCount():pamount);j++){
				HSSFRow row=sheet.getRow(tvo.getFirstDataRow()+j+i*pamount);
				for(short k=0;k<titlesize;k++){
					tvo.setCellData(j-1, k, getCellValue(row.getCell(k)));
				}
			}
			RetMessage rmi=inertTxtDataVO(hpk,tvo,hvo,datadefinb,errfilepath,errlist);
			if(rm==null){
				rm=rmi;
			}else{
				if(!rm.getIssucc()||!rmi.getIssucc()){
					rm.setIssucc(false);
					rm.setFa(rm.getFa()+rmi.getFa());
					rm.setExit(rm.getExit()+rmi.getExit());
					rm.setSu(rm.getSu()+rmi.getSu());
				}
			}
		}
		rm.setMessage("成功导入"+rm.getSu()+"条记录，忽略"+rm.getExit()+"条记录，失败"+(totalrow-rm.getSu()-rm.getExit())+"条记录！");
		SJUtil.PrintLog(errfilepath, "insertExcelVo", rm.getMessage());
		return rm;
	}





	IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	/**
	 * @author wyd
	 * @desc 把从文件得到的数据vo（txtdvo）根据文件格式(hvo.getAttributeValue("datamaptype"))插入到数据库表中
	 * @param String
	 *            hpk 数据同步的主表主键
	 * @param TxtDataVO
	 *            txtdvo 从文件读到的数据
	 * @param DipDatarecHVO
	 *            hvo 数据同步主表vo，主要里边加了几个属性，包括存储表明，存储表的主键，存储的格式
	 * @param Hashtable
	 *            datadefinb 表体字段与表体vo的map
	 * @param String filepath 如果校验出错，写日志到这个文件里
	 * 
	 */
	public RetMessage inertTxtDataVO(String hpk, TxtDataVO txtdvo,
			DipDatarecHVO hvo, Hashtable<String, DipDatadefinitBVO> datadefinb,String filepath,List<String> errlist) {
		DataRecCheckPlugin check=new DataRecCheckPlugin();
		CheckMessage cm=check.doCheck(new Object[]{txtdvo,hvo,datadefinb,filepath,hvo.getSourcetype().equals(IContrastUtil.DATAORIGIN_WEBSERVICEZQ)?true:false});
		List<DataverifyBVO> list=SJUtil.getCheckClassName(hpk, null);
		String tabname = hvo.getAttributeValue("tablename").toString();//表名
		if(tabname.equals("dip_datarec_h")){
			tabname=(String) hvo.getAttributeValue("dataname");
		}
		String pk = datadefinb.get("#PKFIELD#")==null?"":datadefinb.get("#PKFIELD#").getEname();//表的主键s
		String syspk=datadefinb.get("#SYSPKFIELD#")==null?"":datadefinb.get("#SYSPKFIELD#").getEname();//表的系统预置主键
//		String syspk2=datadefinb.get("#SYSPKFIELD#")==null?"":datadefinb.get("#SYSPKFIELD#").getEname();//表的系统预置主键
//		String syspk3=datadefinb.get("#SYSPKFIELD#")==null?"":datadefinb.get("#SYSPKFIELD#").getEname();//表的系统预置主键
		boolean hasPk=false;
		if(!pk.equals("")){
			hasPk=true;
		}
		Object ob=cm.getValidData();
		List<String> err=new ArrayList<String>();
		RetMessage rm=new RetMessage();;
		boolean isfg=true;//默认为覆盖已存在的数据
		String repeatdata=hvo.getRepeatdata();
		if(repeatdata!=null&&repeatdata.length()>0){////////////////
			if(!IContrastUtil.REPEATDATA_CONTROL_FUGAI.equals(repeatdata)){
				isfg=false;
			}
		}
		if(ob!=null){
			boolean issucc=true;
			RowDataVO[] rowVOs = (RowDataVO[]) cm.getValidData();//表示要插入到目标表中的数据。
			ilm.writeToTabMonitor_RequiresNew(hpk, IContrastUtil.YWLX_TB, rowVOs.length, cm.getErrList()==null?0:cm.getErrList().size(), "nc.impl.dip.optByPluginSrv.DataRecCheckPlugin");
			int su=0;//成功条数
			int hulue=0;//忽略
			int exit=0;//
			int fugai=0;//覆盖
			int in=0;//插入
			if(rowVOs!=null&&rowVOs.length>0){
				for(int i=0;i<rowVOs.length;i++){
					RowDataVO vo=rowVOs[i];
					Map map=vo.getTab();
					if(map==null||map.size()==0){
						continue;
					}
					if(syspk!=null&&syspk.trim().length()>0){
						map.put(syspk, query.getOID());
					}
					int size=map.keySet().size();
					String sql="";
					try {
						if(hasPk){//表示存在主键，
							String value=(String) map.get(pk);
							String wheresql="  where "+pk+"='"+value+"'";
							String querysql="select count(*) from "+tabname + wheresql;
							String retuc=query.queryfield(querysql);
							
							if(retuc==null||retuc.equals("0")){ //插入数据库
								int k=0;
								Iterator set=map.keySet().iterator();
								String insertsql="insert into "+vo.getTableName()+"(" ;
								String keys="";
								String values="";
								while(set.hasNext()){
									k=k+1;
									String key=set.next().toString();
								
									keys=keys+key+"";
								
									
									boolean charStyle=false;
									if(datadefinb.get(key)!=null&&datadefinb.get(key).getType()!=null){
										if(datadefinb.get(key).getType().toUpperCase().contains("CHAR")){
											charStyle=true;
										}	
									}
									
									if(charStyle){
										values=values+"'"+map.get(key)+"'";
										
									}else{
										values=values+map.get(key)+"";
									}
									
									if(k!=size){
										keys=keys+",";
										values=values+",";
									}
								}
								if(keys.length()>0){
									insertsql=insertsql+keys+") values ( "+values+")" ;
									sql=insertsql;
									iqf.exesql(insertsql);
									su=su+1;
									in=in+1;
								}else {
									return new RetMessage(false,"格式定义找到的对应字段为空");
								}
							}else{
								
								if(isfg){//覆盖记录
									Iterator set=map.keySet().iterator();
									int k=0;
									String updatesql="update "+vo.getTableName()+" set " ;
									while(set.hasNext()){
										k=k+1;
										String key=set.next().toString();
										String mapvalue=map.get(key)==null?"":map.get(key).toString();
										if(!key.equals(pk)){
											boolean charStyle=false;
											if(datadefinb.get(key)!=null&&datadefinb.get(key).getType()!=null){
												if(datadefinb.get(key).getType().toUpperCase().contains("CHAR")){
													charStyle=true;
												}	
											}
											
											if(charStyle){
												updatesql=updatesql+" "+key+"='"+mapvalue+"'";
											}else{
												updatesql=updatesql+" "+key+"="+mapvalue+"";
											}
												updatesql=updatesql+",";
										}
										
									}
									if(k==0){
										return new RetMessage(false,"格式定义找到的对应字段为空");
									}
									
										updatesql=updatesql.substring(0,updatesql.length()-1) +wheresql ;
										sql=updatesql;
										iqf.exesql(updatesql);
										su=su+1;
										fugai=fugai+1;
								}else{//忽略
									hulue=hulue+1;
								}
							}
						}else{ //插入数据库
							Iterator set=map.keySet().iterator();
							String insertsql="insert into "+vo.getTableName()+"(" ;
							String keys="";
							String values="";
							while(set.hasNext()){
								String key=set.next().toString();
								keys=keys+key;
								boolean charStyle=false;
								if(datadefinb.get(key)!=null&&datadefinb.get(key).getType()!=null){
									if(datadefinb.get(key).getType().toUpperCase().contains("CHAR")){
										charStyle=true;
									}	
								}
								if(charStyle){
									values=values+"'"+map.get(key)+"'";
								}else{
									values=values+map.get(key)+"";
								}
								values=values+",";
								keys=keys+",";
							}
							if(keys.length()>0){
								insertsql=insertsql+keys.substring(0,keys.length()-1)+") values ( "+values.substring(0,values.length()-1)+")" ;
								sql=insertsql;
								iqf.exesql(insertsql);
								su=su+1;
								in=in+1;
							}else {
								return new RetMessage(false,"格式定义找到的对应字段为空");
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						exit++;
						err.add("插入第"+(i+1)+"条数据出错！"+e.getMessage()+sql);
//						if(filepath!=null){
//							SJUtil.PrintLog(filepath, "inertTxtDataVO", "插入第"+(i+1)+"条数据出错！"+e.getMessage());
//						}
						issucc=false;
					} 
				}
				StringBuffer sbb=new StringBuffer();
				String instr=in==0?"":"插入"+in+"条记录,";
				String fugaistr=fugai==0?"":"覆盖"+fugai+"条记录,";
				String huluestr=hulue==0?"":"忽略"+hulue+"条记录,";
				String shibaistr=err.size()==0?"":"失败"+err.size()+"条记录,";
				String ss=instr+fugaistr+huluestr+shibaistr;
				if(ss!=null&&!ss.equals("")){
				    ss=ss.substring(0,ss.lastIndexOf(","));
					ss="其中"+ss+"!";
				}
				sbb.append("成功导入"+su+"条记录：");
				sbb.append(ss);
				
				if(err!=null){
					errlist.addAll(err);
				}
				if(cm.isSuccessful()&&(err==null||err.size()==0)){
					rm=new  RetMessage(true,sbb.toString(),su,exit,0);
				}else{
					issucc=false;
					if(cm.getErrList()!=null){
						errlist.addAll(cm.getErrList());
					}
					rm= new RetMessage(false,sbb.toString()+(cm.getErrList()==null?"":("其中数据校验失败"+cm.getErrList().size()+"条数据")),su,exit,(txtdvo.getDataSize()-su-exit));
					rm.setErrlist(errlist);
				}
			}
		}else{
			 if(cm.getErrList()!=null){
				 errlist.addAll(cm.getErrList());
			 }
			rm= new RetMessage(false,cm.getMessage(),0,cm.getErrList()==null?0:cm.getErrList().size());
			rm.setErrlist(errlist);
		}
		return rm;
	}
	
	/**
	 * @author lyz 修改inertTxtDataVO方法
	 * @desc 把从文件得到的数据vo（txtdvo）根据文件格式(hvo.getAttributeValue("datamaptype"))插入到数据库表中
	 * @param String
	 *            hpk 数据同步的主表主键
	 * @param TxtDataVO
	 *            txtdvo 从文件读到的数据
	 * @param DipDatarecHVO
	 *            hvo 数据同步主表vo，主要里边加了几个属性，包括存储表明，存储表的主键，存储的格式
	 * @param Hashtable
	 *            datadefinb 表体字段与表体vo的map
	 * @param String filepath 如果校验出错，写日志到这个文件里
	 * 
	 */
	public RetMessage inertTxtDataVOByAllPrex(String hpk, TxtDataVO txtdvo,
			DipDatarecHVO hvo, Hashtable<String, DipDatadefinitBVO> datadefinb,String filepath,List<String> errlist) {
		DataRecCheckPlugin check=new DataRecCheckPlugin();
		CheckMessage cm=check.doCheck(new Object[]{txtdvo,hvo,datadefinb,filepath,hvo.getSourcetype().equals(IContrastUtil.DATAORIGIN_WEBSERVICEZQ)?true:false,true});
		if(!cm.isSuccessful()&&!cm.isAllPrexCheckTile()){
			return new RetMessage(false,cm.getMessage());
		}
		if(!cm.isSuccessful()){
			RetMessage ret=new RetMessage();
			ret.setIssucc(false);
			ret.setMessage(cm.getMessage());
			ret.setErrlist(cm.getErrList());
			return ret;
		}
		List<DataverifyBVO> list=SJUtil.getCheckClassName(hpk, null);
		String tabname = hvo.getAttributeValue("tablename").toString();//表名
		if(tabname.equals("dip_datarec_h")){
			tabname=(String) hvo.getAttributeValue("dataname");
		}
		String pk = datadefinb.get("#PKFIELD#")==null?"":datadefinb.get("#PKFIELD#").getEname();//表的主键s
		String syspk=datadefinb.get("#SYSPKFIELD#")==null?"":datadefinb.get("#SYSPKFIELD#").getEname();//表的系统预置主键
//		String syspk2=datadefinb.get("#SYSPKFIELD#")==null?"":datadefinb.get("#SYSPKFIELD#").getEname();//表的系统预置主键
//		String syspk3=datadefinb.get("#SYSPKFIELD#")==null?"":datadefinb.get("#SYSPKFIELD#").getEname();//表的系统预置主键
		boolean hasPk=false;
		if(!pk.equals("")){
			hasPk=true;
		}
		Object ob=cm.getValidData();
//		List<String> err=new ArrayList<String>();
		RetMessage rm=new RetMessage();;
		boolean isfg=true;//默认为覆盖已存在的数据
		String repeatdata=hvo.getRepeatdata();
		if(repeatdata!=null&&repeatdata.length()>0){////////////////
			if(!IContrastUtil.REPEATDATA_CONTROL_FUGAI.equals(repeatdata)){
				isfg=false;
			}
		}
		if(ob!=null){
			boolean issucc=true;
			RowDataVO[] rowVOs = (RowDataVO[]) cm.getValidData();//表示要插入到目标表中的数据。
			ilm.writeToTabMonitor_RequiresNew(hpk, IContrastUtil.YWLX_TB, rowVOs.length, cm.getErrList()==null?0:cm.getErrList().size(), "nc.impl.dip.optByPluginSrv.DataRecCheckPlugin");
			int su=0;//成功条数
			int hulue=0;//忽略
			int exit=0;//
			int fugai=0;//覆盖
			int in=0;//插入
			List<String> listsql=new ArrayList<String>();
			//List<String> updatelist=new ArrayList<String>();
			if(rowVOs!=null&&rowVOs.length>0){
				for(int i=0;i<rowVOs.length;i++){
					RowDataVO vo=rowVOs[i];
					Map map=vo.getTab();
				try {
					if(map==null||map.size()==0){
						if(i==rowVOs.length-1){
							if(listsql.size()>0){
								RetMessage rrt=iqf.exectEverySqlByHandCommit(listsql);
								if(!rrt.getIssucc()){
									rrt.setMessage("插入文件数据出错！"+rrt.getMessage());
									return rrt;
								}
							}
						}
						continue;
					}
					if(syspk!=null&&syspk.trim().length()>0){
						map.put(syspk, query.getOID());
					}
					int size=map.keySet().size();
					String sql="";
						if(hasPk){//表示存在主键，
							String value=(String) map.get(pk);
							String wheresql="  where "+pk+"='"+value+"'";
							String querysql="select count(*) from "+tabname + wheresql;
							String retuc=query.queryfield(querysql);
							
							if(retuc==null||retuc.equals("0")){ //插入数据库
								int k=0;
								Iterator set=map.keySet().iterator();
								String insertsql="insert into "+vo.getTableName()+"(" ;
								String keys="";
								String values="";
								while(set.hasNext()){
									k=k+1;
									String key=set.next().toString();
								
									keys=keys+key+"";
								
									
									boolean charStyle=false;
									if(datadefinb.get(key)!=null&&datadefinb.get(key).getType()!=null){
										if(datadefinb.get(key).getType().toUpperCase().contains("CHAR")){
											charStyle=true;
										}	
									}
									
									if(charStyle){
										values=values+"'"+map.get(key)+"'";
										
									}else{
										values=values+map.get(key)+"";
									}
									
									if(k!=size){
										keys=keys+",";
										values=values+",";
									}
								}
								if(keys.length()>0){
									insertsql=insertsql+keys+") values ( "+values+")" ;
									sql=insertsql;
//									iqf.exesql(insertsql);
									listsql.add(insertsql);
									su=su+1;
									in=in+1;
								}else {
									return new RetMessage(false,"格式定义找到的对应字段为空");
								}
							}else{
								
								if(isfg){//覆盖记录
									Iterator set=map.keySet().iterator();
									int k=0;
									String updatesql="update "+vo.getTableName()+" set " ;
									while(set.hasNext()){
										k=k+1;
										String key=set.next().toString();
										String mapvalue=map.get(key)==null?"":map.get(key).toString();
										if(!key.equals(pk)){
											boolean charStyle=false;
											if(datadefinb.get(key)!=null&&datadefinb.get(key).getType()!=null){
												if(datadefinb.get(key).getType().toUpperCase().contains("CHAR")){
													charStyle=true;
												}	
											}
											
											if(charStyle){
												updatesql=updatesql+" "+key+"='"+mapvalue+"'";
											}else{
												updatesql=updatesql+" "+key+"="+mapvalue+"";
											}
												updatesql=updatesql+",";
										}
										
									}
									if(k==0){
										return new RetMessage(false,"格式定义找到的对应字段为空");
									}
									
										updatesql=updatesql.substring(0,updatesql.length()-1) +wheresql ;
										sql=updatesql;
//										iqf.exesql(updatesql);
										listsql.add(updatesql);
										su=su+1;
										fugai=fugai+1;
								}else{//忽略
									hulue=hulue+1;
									su=su+1;
								}
							}
						}else{ //插入数据库
							Iterator set=map.keySet().iterator();
							String insertsql="insert into "+vo.getTableName()+"(" ;
							String keys="";
							String values="";
							while(set.hasNext()){
								String key=set.next().toString();
								keys=keys+key;
								boolean charStyle=false;
								if(datadefinb.get(key)!=null&&datadefinb.get(key).getType()!=null){
									if(datadefinb.get(key).getType().toUpperCase().contains("CHAR")){
										charStyle=true;
									}	
								}
								if(charStyle){
									values=values+"'"+map.get(key)+"'";
								}else{
									values=values+map.get(key)+"";
								}
								values=values+",";
								keys=keys+",";
							}
							if(keys.length()>0){
								insertsql=insertsql+keys.substring(0,keys.length()-1)+") values ( "+values.substring(0,values.length()-1)+")" ;
								sql=insertsql;
//								iqf.exesql(insertsql);
								listsql.add(insertsql);
								su=su+1;
								in=in+1;
							}else {
								return new RetMessage(false,"格式定义找到的对应字段为空");
							}
						}
						
						if(i==rowVOs.length-1){
							if(listsql.size()>0){
								RetMessage rrt=iqf.exectEverySqlByHandCommit(listsql);
								if(!rrt.getIssucc()){
									rrt.setMessage("插入文件数据出错！"+rrt.getMessage());
									return rrt;
								}
							}
						}
						
					} catch (Exception e) {
						e.printStackTrace();
						issucc=false;
						RetMessage ret=new RetMessage();
						ret.setIssucc(false);
						ret.setMessage("插入文件数据出错！"+e.getMessage());
						return ret;
						
					} 
				}
				StringBuffer sbb=new StringBuffer();
				String instr=in==0?"":"插入"+in+"条记录,";
				String fugaistr=fugai==0?"":"覆盖"+fugai+"条记录,";
				String huluestr=hulue==0?"":"忽略"+hulue+"条记录,";
				//String shibaistr=err.size()==0?"":"失败"+err.size()+"条记录,";
				String ss=instr+fugaistr+huluestr;
				if(ss!=null&&!ss.equals("")){
				    ss=ss.substring(0,ss.lastIndexOf(","));
					ss="其中"+ss+"!";
				}
				sbb.append("成功处理"+su+"条记录：");
				sbb.append(ss);
//				if(cm.isSuccessful()&&(err==null||err.size()==0)){
//				if(cm.isSuccessful()){
					rm=new  RetMessage(true,sbb.toString());
					rm.setSu(su);
					rm.setInsert(in);
					rm.setHulue(hulue);
					rm.setFugai(fugai);
//				}else{
//					issucc=false;
//					if(cm.getErrList()!=null){
//						errlist.addAll(cm.getErrList());
//					}
//					rm= new RetMessage(false,sbb.toString()+(cm.getErrList()==null?"":("其中数据校验失败"+cm.getErrList().size()+"条数据")),su,exit,(txtdvo.getDataSize()-su-exit));
//					rm.setErrlist(errlist);
//				}
			}
		}else{
			 if(cm.getErrList()!=null){
				 errlist.addAll(cm.getErrList());
			 }
			rm= new RetMessage(false,cm.getMessage());
			rm.setErrlist(errlist);
		}
		return rm;
	}
	public TxtDataVO radeFile(String filepath,int bnum,String fgbj,DipDatarecSpecialTab[] specialxml) throws Exception {
		if (filepath == null || filepath.length() <= 0) {
			throw new Exception("传入的文件名为空");
		}
//		TxtDataVO tvo = new TxtDataVO();
		TxtDataVO tvo = null;
		if (filepath.endsWith("xls")) {
			FileInputStream fin=new FileInputStream(filepath);
			try{
				HSSFWorkbook book = new HSSFWorkbook(fin);
				HSSFSheet sheet = book.getSheetAt(0);
				if(sheet == null){
					return null;
				}
				tvo=new TxtDataVO();
				tvo.setSheetName(book.getSheetName(0));
				tvo.setStartRow(sheet.getFirstRowNum());
				tvo.setStartCol(sheet.getLeftCol());
				tvo.setRowCount(sheet.getPhysicalNumberOfRows() - 1);//去掉标题行
				tvo.setColCount((short)sheet.getRow(sheet.getFirstRowNum()).getPhysicalNumberOfCells());
				tvo.setFirstDataRow(sheet.getFirstRowNum());
				tvo.setFirstDataCol(sheet.getLeftCol());

				HSSFRow titleRow = sheet.getRow(tvo.getStartRow());
				HashMap<String, String> titlemap = new HashMap<String, String>();
				for(short i=0;i<titleRow.getPhysicalNumberOfCells();i++){
					titlemap.put(Short.toString(i),(String)getCellValue(titleRow.getCell(i)));
				}
				tvo.setTitlemap(titlemap);
				int titlesize=titlemap.size();
				for(int i=1;i<=tvo.getRowCount();i++){
					HSSFRow row = sheet.getRow(tvo.getFirstDataRow() + i);
					for(short j=0;j<titlesize;j++){
						tvo.setCellData(i-1, j, getCellValue(row.getCell(j)));
					}
				}
			}finally{
				fin.close();
			}
		} else if (filepath.endsWith("xml")) {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(filepath);
			boolean flag=false;//如果为ture表示没有二级标签，只有根标签和一级标签。
			int count=specialxml.length;
			if(count==2){
				flag=true;
			}
			if(count==3){
				DipDatarecSpecialTab special=specialxml[2];
				if(special.getNodenumber()==3&&special.getValue()!=null&&special.getValue().trim().length()>0){
					flag=false;
				}else{
					flag=true;
				}
			}
			Element root = doc.getRootElement();
//			Iterator it = null;
//			if(flag){
//				it = root.elementIterator(specialxml[1].getValue());
//				if(it==null){
//					throw new Exception(filepath+"文件中没有"+specialxml[1].getValue()+"标签");
//				}
//			}else{
//				it = root.elementIterator(specialxml[2].getValue());
//				if(it==null){
//					throw new Exception(filepath+"文件中没有"+specialxml[2].getValue()+"标签");
//				}
//			}
			String rootname=root.getName();
			if(!(rootname!=null&&rootname.equals(specialxml[0].getValue()))){
				throw new Exception(filepath+"文件中没有根标签"+specialxml[0].getValue());
			}
			List listone=root.elements(specialxml[1].getValue());
			if(listone==null||listone.size()<=0){
				throw new Exception(filepath+"文件中没有一级标签"+specialxml[1].getValue());
			}
//			Iterator it = root.elementIterator(specialxml[1].getValue());
			Iterator it =listone.iterator();
//			if(it.){}
			
			HashMap<String, String> titlemap = new HashMap<String, String>();
			int row = 0;
			if(flag){//只有根标签和一级标签
//				tvo.setStartCol(0);
//				HashMap<String, String> titlemap = new HashMap<String, String>();
//				int row = 0;
				while (it.hasNext()) {
					if(tvo==null){
						tvo=new TxtDataVO();	
						tvo.setStartCol(0);
					}
					
				
					Element head = (Element) it.next();
					List list = head.elements();
					if (list == null || list.size() == 0) {
//						throw new Exception(filepath+"文件中没有一级标签"+specialxml[1].getValue());
						continue;
					}
					for (int i = 0; i < list.size(); i++) {
						Element e = (Element) list.get(i);
						tvo.setCellData(row, i, e.getText());
						if (row == 0) {
							titlemap.put(i + "", e.getName());
						}
					}
					row++;
				}
				tvo.setTitlemap(titlemap);
			}
			else{//表示有二级标签
				while(it.hasNext()){
					if(tvo==null){
						tvo=new TxtDataVO();	
						tvo.setStartCol(0);
					}
					
					Element head=(Element) it.next();
//					if(!(head.getName()!=null&&specialxml[2].getValue()!=null&&head.getName().equals(specialxml[2].getValue()))){
//						throw new Exception(filepath+"文件中没有二级标签"+specialxml[1].getValue());
//					}
					List listtwo=head.elements(specialxml[2].getValue());
					if(listtwo==null||listtwo.size()<=0){
						throw new Exception(filepath+"文件中没有二级标签"+specialxml[2].getValue());
					}
					Iterator ittwo=listtwo.iterator();
//					head
					while (ittwo.hasNext()) {
						Element two = (Element) ittwo.next();
						List list = two.elements();
						if (list == null || list.size() == 0) {
							continue;
//							throw new Exception(filepath+"文件中没有二级标签"+specialxml[2].getValue());
						}
						for (int i = 0; i < list.size(); i++) {
							Element e = (Element) list.get(i);
							tvo.setCellData(row, i, e.getText());
							if (row == 0) {
								titlemap.put(i + "", e.getName());
							}
						}
						row++;
					}
				}
				
			}
			

		} else if (filepath.endsWith("txt")) {
			List<String> list = new ArrayList<String>();
			FileReader fr = null;
			BufferedReader br = null;
			try {
				fr = new FileReader(filepath);
				br = new BufferedReader(fr);
				String line = "";
				while ((line = br.readLine()) != null) {
					list.add(line);
				}

			} finally {
				if (br != null) {
					br.close();
				}
				if (fr != null) {
					fr.close();
				}
			}
			if (list == null || list.size() <= 1) {
				return null;
			}
			tvo=new TxtDataVO();
			tvo.setStartCol(0);
			tvo.setRowCount(list.size() - 1);// 去掉标题行
			String[] title = list.get(0).split(fgbj);
			tvo.setColCount(title.length);
			tvo.setFirstDataRow(1);
			tvo.setFirstDataCol(0);
			HashMap<String, String> titlemap = new HashMap<String, String>();
			for (short i = 1; i < title.length; i++) {
				titlemap.put((i-1)+"", title[i]);
			}
			tvo.setTitlemap(titlemap);
			tvo.setFieldName(title);
			for (int i = 1; i < list.size(); i++) {
				String[] val = list.get(i).split(fgbj);
				for (short j = 1; j < val.length; j++) {
					tvo.setCellData(i - 1, j-1, val[j]);
				}
			}

		}
		return tvo;

	}

	private Object getCellValue(HSSFCell cell) {
		if(cell==null){
			return null;
		}
		Object rst = null;
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_BOOLEAN:
			// 得到Boolean对象的方法
			System.out.print(cell.getBooleanCellValue() + " ");
			rst = new UFBoolean(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			String value = Double.toString(cell.getNumericCellValue());
			if (value.contains(".")) {
				try {
					String temp = value.replace(".", "-");
					int lastNo = Integer.parseInt(temp.split("-")[1]);
					if (lastNo == 0) {
						rst = Integer.parseInt(temp.split("-")[0]);
					} else {
						rst = new UFDouble(cell.getNumericCellValue());
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
					rst = new UFDouble(cell.getNumericCellValue());
				}
			}
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			// 读取公式
			rst = cell.getCellFormula();
			break;
		case HSSFCell.CELL_TYPE_STRING:
			// 读取String
			rst = cell.getRichStringCellValue();
			try {
				rst = new UFDate(rst.toString());
			} catch (Exception e) {
				rst = cell.getRichStringCellValue().toString();
			}
			System.out.print(cell.getRichStringCellValue().toString() + " ");
			break;
		}
		return rst;
	}

	/**
	 * 获取结束流、存储表名及对应格式
	 * 
	 * @param xtId
	 *            系统标志
	 * @param zdId
	 *            站点标志
	 * @param ywId
	 *            业务标志
	 * @return
	 * @throws DbException
	 * @throws SQLException
	 * @throws BusinessException
	 */
	public ArgDataRecDVO getTbFlAndStyle(String xtId, String zdId, String ywId)
	throws BusinessException, SQLException, DbException {
		if (!"".equals(xtId) && !"".equals(zdId) && !"".equals(ywId)) {
			return (ArgDataRecDVO) rds.getrRecAggVo(xtId, zdId, ywId);
		} else {
			return null;
		}
	}
ITaskExecute ite=(ITaskExecute) NCLocator.getInstance().lookup(ITaskExecute.class.getName());
	/**
	 * 拿消息格式去解析对应格式的消息（消息总线）
	 * 
	 * @param 消息数据
	 * @throws DbException
	 * @throws SQLException
	 * @throws BusinessException
	 * @sourcemsg//接收到的message
	 * @msg// 接收msg 转换成大写了。sourcemsg.toUpperCase();
	 */
	public void styleToMsg(String sourcemsg,String msg,Map gsmap,MsrVO mvo,String[] strerror) throws BusinessException, SQLException,
	DbException {
		DateprocessVO vop=new DateprocessVO();
		vop.setActivetype("数据接收");
		vop.setActive("ESB消息接收");
		vop.setSdate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		if (null != msg && msg.length() > 0) {
			if(gsmap==null||gsmap.size()<=0){
				vop.setContent("没有定义消息总线格式,格式定义为空"+sourcemsg);
				vop.setDef_str_1(IContrastUtil.DATAPROCESS_HINT);
				Logger.debug(new StringBuffer("styleToMsg").append("没有定义消息总线格式,格式定义为空"+sourcemsg));
				ilm.writToDataLog_RequiresNew(vop);
				ilm.writToMhccb_RequiresNew(sourcemsg, mvo);
					writeDipLog("消息流--没有定义消息总线格式,格式定义为空--"+sourcemsg, IContrastUtil.LOG_ERROR);
				writeMessageInTable(sourcemsg,strerror);
				return ;
			}
			Logger.debug(new StringBuffer("styleToMsg").append("格式定义的MAP大小为:"+gsmap.size()));
			Iterator<String> it=gsmap.keySet().iterator();
			boolean iscontaings=false;
			while(it.hasNext()){
				String key=it.next();
				Logger.debug("key :["+key+"];msg:["+msg+"];msg.indexOf(key)"+msg.indexOf(key));
				if(msg.indexOf(key)==0){
					EsbMapUtilVO vo=(EsbMapUtilVO) gsmap.get(key);
					if(!(msg.endsWith(vo.getEndflag()+vo.getBj())||msg.endsWith(vo.getEndflag()+vo.getBj()+vo.getMergemark()==null?"":vo.getMergemark()))){//如果尾部不匹配就抛出错误
						vop.setContent("消息流--"+"ESB结束标志匹配失败--结束标志应该为【"+vo.getEndflag()+vo.getBj()+"】或者【"+vo.getEndflag()+vo.getBj()+vo.getMergemark()==null?"":vo.getMergemark()+"】:"+sourcemsg);
						Logger.debug("消息流--"+"ESB结束标志匹配失败--结束标志应该为【"+vo.getEndflag()+vo.getBj()+"】或者【"+vo.getEndflag()+vo.getBj()+vo.getMergemark()==null?"":vo.getMergemark()+"】:"+sourcemsg);
						vop.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
						ilm.writToDataLog_RequiresNew(vop);
						ilm.writToMhccb_RequiresNew(sourcemsg, mvo);
						writeDipLog("消息流--"+"ESB结束标志匹配失败--结束标志应该为【"+vo.getEndflag()+vo.getBj()+"】或者【"+vo.getEndflag()+vo.getBj()+vo.getMergemark()==null?"":vo.getMergemark()+"】:"+sourcemsg, IContrastUtil.LOG_ERROR);
						writeMessageInTable(sourcemsg,strerror);
						return;
					}
					vop.setPk_bus(vo.getPk_datarec());
					strerror[2]=vo.getTablename();
					Logger.debug("vo.getType()"+vo.getType());
					if(vo.getType().equals("b")){
						VDipDsreceiveVO vdvo=vo.getSysvo();
						ThreadUtil.getOrSetcount(vo.getPk_datarec(), ThreadUtil.OPTYPE_B,null);
						vop.setContent(vdvo.getExtname()+","+vdvo.getSysname()+",接收到数据传输开始标志"+msg);
						vop.setDef_str_1(IContrastUtil.DATAPROCESS_HINT);
						Logger.debug(vdvo.getExtname()+","+vdvo.getSysname()+",接收到数据传输开始标志"+msg);
						ilm.writToDataLog_RequiresNew(vop);
						DateprocessVO vop1=(DateprocessVO) vop.clone();
						vop1.setContent("开始接收数据");
						ilm.writeToTabMonitor_RequiresNew(vo.getPk_datarec(), IContrastUtil.YWLX_JS,IContrastUtil.TABMONSTA_ON,null,null);
							writeDipLog("消息流--接收到数据传输开始标志--"+msg, IContrastUtil.LOG_SUCESS);
					}else if(vo.getType().equals("e")){
						String pk_bus=vo.getPk_datarec();
						String yw=IContrastUtil.YWLX_JS;
						int count=0;
						int mcount=0;//报文数个数
						Logger.debug("取计数器，");
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						DipCountVO retmap=ThreadUtil.getOrSetcount(vo.getPk_datarec(), ThreadUtil.OPTYPE_E,null);
						Logger.debug("取计数器，计数器是不是空 "+retmap);
						if(retmap!=null){
							count=retmap.getCount();
							mcount=retmap.getMcount();
							Integer fa=retmap.getFcount();
							Integer suc=retmap.getScount();
							Logger.debug("fa:"+fa+"suc:"+suc);
							Map<String,DipSuccOrFailClass> sofmap=retmap.getClassverfiy();
							if(sofmap!=null&&sofmap.size()>0){
								Iterator<String> kit=sofmap.keySet().iterator();
								Logger.debug("kitleng:"+sofmap.keySet().size());
								while(kit.hasNext()){
									String ik=kit.next();
									Logger.debug("ik:"+ik);
									DipSuccOrFailClass dofc=sofmap.get(ik);
									Logger.debug("ik:"+ik+"dofc.gets:"+dofc.getSucount()+"dofc.getf"+dofc.getFacount());
									ilm.writeToTabMonitor_RequiresNew(pk_bus, yw, dofc.getSucount()==null?0:dofc.getSucount(), dofc.getFacount()==null?0:dofc.getFacount(), ik);
								}
							}
							Logger.debug("pk_bus:"+pk_bus+"TABMONSTA_FIN:"+"suc："+suc+"fa:"+fa);
							ilm.writeToTabMonitor_RequiresNew(pk_bus, IContrastUtil.YWLX_JS,IContrastUtil.TABMONSTA_FIN,suc,fa);
						}else{
							Logger.debug("pk_bus:"+pk_bus+"TABMONSTA_FIN:"+"suc："+null+"fa:"+null);
							ilm.writeToTabMonitor_RequiresNew(pk_bus, IContrastUtil.YWLX_JS,IContrastUtil.TABMONSTA_FIN,null,null);
						}
						VDipDsreceiveVO vdvo=vo.getSysvo();
						Logger.debug("回执标志：【"+vdvo.getBackcon()+"】");
						Logger.debug("vdvo.getBackcon()!=null&&vdvo.getBackcon().equals(\"Y\"):"+(vdvo.getBackcon()!=null&&vdvo.getBackcon().equals("Y")));
						if(vdvo.getBackcon()!=null&&vdvo.getBackcon().equalsIgnoreCase("Y")){
							String fsmsg=null;
							String backmsr=null;
							Logger.debug("ThreadUtil.getHzMap()!=null&&ThreadUtil.getHzMap().containsKey(pk_bus):"+(ThreadUtil.getHzMap()!=null&&ThreadUtil.getHzMap().containsKey(pk_bus)));
							if(ThreadUtil.getHzMap()!=null&&ThreadUtil.getHzMap().containsKey(pk_bus)){
								fsmsg=ThreadUtil.getHzMap().get(pk_bus);
								Logger.debug("fsmsg:"+(fsmsg));
								Logger.debug("count:"+(count+""));
								fsmsg=fsmsg.replace("记录数函数", count+"");
								Logger.debug("fsmsg记录数函数:"+(fsmsg));
								while(fsmsg.indexOf("时间函数")>0){
									int indexi=fsmsg.indexOf("时间函数");
									int indexr=fsmsg.indexOf(">");
									fsmsg=fsmsg.substring(0,indexi)+( new SimpleDateFormat(fsmsg.substring((indexi+("时间函数<".length())),indexr)).format(new Date()))+fsmsg.substring(indexr+1);
									Logger.debug("fsmsg时间函数:"+(fsmsg));
								}
								String pk=null;
								pk = query.getOID();
								if(pk==null){
									pk="";
								}
								fsmsg=fsmsg.replace("主键函数", pk);
								Logger.debug("fsmsg主键函数:"+(fsmsg));
								fsmsg=fsmsg.replace("报文数函数", mcount+"");
								Logger.debug("报文数函数:"+(mcount));
								fsmsg=fsmsg.replace(",", vo.getBj());
								fsmsg=vo.getBj()+fsmsg+vo.getBj();
								Logger.debug("fsmsg:"+(fsmsg));
								if(ThreadUtil.getHzMap().containsKey(pk_bus+"s")){
									backmsr=ThreadUtil.getHzMap().get(pk_bus+"s");
									Logger.debug("backmsr:"+(backmsr));
								}
							}
							DateprocessVO vop1=(DateprocessVO) vop.clone();
							vop.setDef_str_1(IContrastUtil.DATAPROCESS_HINT);
							vop1.setContent(vdvo.getExtname()+","+vdvo.getSysname()+",发送回执消息【"+fsmsg+"】");
							Logger.debug("发送回执消息"+(vdvo.getExtname()+","+vdvo.getSysname()+",发送回执消息【"+fsmsg+"】"));
							ilm.writToDataLog_RequiresNew(vop1);
							DateprocessVO vop2=(DateprocessVO) vop.clone();
							MsrVO msvo=(MsrVO) getBaseDAO().retrieveByPK(MsrVO.class, backmsr);
							if(fsmsg==null||fsmsg.length()<=0){
								vop2.setContent(vdvo.getExtname()+","+vdvo.getSysname()+"没有找到回执定义");
								vop2.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
								writeDipLog("消息流--发送回执失败没有找到回执定义--"+msg, IContrastUtil.LOG_ERROR);
							}
							if(msvo==null){
								vop2.setContent(vop2.getContent()+"；没有找到回执消息服务器的注册");
								vop2.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
								writeDipLog("消息流--发送回执失败没有找到回执消息服务器的注册--"+msg, IContrastUtil.LOG_ERROR);
							}
							ilm.writToDataLog_RequiresNew(vop2);
							if(msvo!=null){
								ite.dosend(msvo, fsmsg);
								writeDipLog("消息流--发送回执成功--"+msg, IContrastUtil.LOG_SUCESS);
							}
						}
						DateprocessVO vop1=(DateprocessVO) vop.clone();
						vop1.setDef_str_1(IContrastUtil.DATAPROCESS_HINT);
						vop1.setContent(vdvo.getExtname()+","+vdvo.getSysname()+",接收数据完毕，共接收到"+count+"条数据"+msg);
							writeDipLog("消息流--接收到数据传输结束标志--"+msg, IContrastUtil.LOG_SUCESS);
						ilm.writToDataLog_RequiresNew(vop1);
					}else if(vo.getType().equals("d")){
						String pk_rec=vo.getPk_datarec();
						//liyunzhe add 增加报文合并 接收
						String mergeStyle=vo.getMergeStyle();
						List<String> listmsg=new ArrayList<String>();
						ThreadUtil.getOrSetcount(pk_rec, ThreadUtil.OPTYPE_M,null);
						if(mergeStyle!=null&&!mergeStyle.trim().equals("0")){
							//就相当于是合并类型的。 
							String mergemark=vo.getMergemark();
							String mergemarkesc=vo.getMergemarkesc();
							String bj=vo.getBj();
							if(mergemark!=null&&mergemark.trim().length()>0){
								if(mergeStyle.equals("1")){//记录合并
									if(sourcemsg.contains(mergemark)){
										String str[]=sourcemsg.split(mergemarkesc);
										String head=bj+"begin"+bj+"xtbz"+bj+"zdbj"+bj+"ywbz";//因为记录合并从第二条数据开始就没有开始和结束标志，所以伪造了一个头和尾，只是在数据解析时候不用修改原有的代码。
										String end="end"+bj;
										for(int i=0;i<str.length;i++){
											String ss=str[i];
											if(i==0){
												ss=ss+end;
											}else if(i==str.length-1){
												ss=head+ss;
											}else{
												ss=head+ss+end;
											}
											listmsg.add(ss);
										}
									}else{
										listmsg.add(sourcemsg);
									}
								}else if(mergeStyle.equals("2")){//完全合并
										if(sourcemsg.contains(mergemark)){
											String str[]=mergeStyle.split(mergemarkesc);
											for(int i=0;i<str.length;i++){
												listmsg.add(str[i]);
											}
										}else{
											listmsg.add(sourcemsg);
										}
								}else{
									listmsg.add(sourcemsg);
								}
							}
						}else{
							listmsg.add(sourcemsg);
						}
						if(listmsg!=null&&listmsg.size()>0){
							for(int k=0;k<listmsg.size();k++){
								 sourcemsg=listmsg.get(k);
								Logger.debug("接收的为业务数据，进入计数器");
								ThreadUtil.getOrSetcount(pk_rec, ThreadUtil.OPTYPE_D,null);
								
								List check=vo.getCheckname();
								Logger.debug("拿校验类");
								boolean ischeckpas=true;
								if(check!=null&&check.size()>0){
									Logger.debug("进入校验循环中");
									for(int i=0;i<check.size();i++){
										
										String name=(String) check.get(i);
										Class cls;
										try {
											cls = Class.forName(name);
											Object inst = cls.newInstance();
											if (inst instanceof ICheckPlugin) {
												ICheckPlugin m = (ICheckPlugin) inst;
												CheckMessage cmsg=m.doCheck(sourcemsg,vo);
												Logger.debug("校验---"+name+" 是否通过"+cmsg.isSuccessful()+"  错误内容"+cmsg.getMessage());
												if(!cmsg.isSuccessful()){
													ischeckpas=false;
													ThreadUtil.getOrSetcount(pk_rec, ThreadUtil.OPTYPE_C, name+"f");
													Logger.debug("校验---"+name+" 失败了，加入到计数器里边");
													writeDipLog("校验---"+name+" 失败了--"+sourcemsg, IContrastUtil.LOG_ERROR);
													break;
												}else{
													ThreadUtil.getOrSetcount(pk_rec, ThreadUtil.OPTYPE_C, name+"s");
													Logger.debug("校验---"+name+" 成功了，加入到计数器里边");
												}
											}
										} catch (ClassNotFoundException e) {
											e.printStackTrace();
											ThreadUtil.getOrSetcount(pk_rec, ThreadUtil.OPTYPE_C, name+"f");
											vop.setContent("没有找到校验类"+name+":"+sourcemsg);
											vop.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
											writeDipLog("消息流--没有找到校验类"+name+":"+sourcemsg+"--"+sourcemsg, IContrastUtil.LOG_ERROR);
											ischeckpas=false;
										} catch (InstantiationException e) {
											e.printStackTrace();
											ThreadUtil.getOrSetcount(pk_rec, ThreadUtil.OPTYPE_C, name+"f");
											vop.setContent("校验类初始化失败"+name+":"+e.getMessage()+" "+sourcemsg);
											vop.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
											writeDipLog("消息流--校验类初始化失败"+name+":"+e.getMessage()+" "+sourcemsg+"--"+sourcemsg, IContrastUtil.LOG_ERROR);
											ischeckpas=false;
										} catch (IllegalAccessException e) {
											e.printStackTrace();
											ThreadUtil.getOrSetcount(pk_rec, ThreadUtil.OPTYPE_C, name+"f");
											vop.setContent("校验失败"+name+":"+e.getMessage()+" "+sourcemsg);
											vop.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
											writeDipLog("消息流--校验失败"+name+":"+e.getMessage()+" "+sourcemsg+"--"+sourcemsg, IContrastUtil.LOG_ERROR);
											ischeckpas=false;
										}catch(Exception e){
											e.printStackTrace();
											ischeckpas=true;
											Logger.debug("校验---"+name+" 抛错了");
											writeDipLog("消息流--校验---"+name+" 抛错了"+"--"+sourcemsg, IContrastUtil.LOG_ERROR);
											vop.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
										}
									}
								}
								if(ischeckpas){
									Logger.debug("-------保存ESB接收到的数据");
									RetMessage ret=saveMessage(sourcemsg,vo);
									
									if(ret.getIssucc()){
										ThreadUtil.getOrSetcount(pk_rec, ThreadUtil.OPTYPE_S, "s");
										writeDipLog("消息流--"+"保存成功--"+sourcemsg, IContrastUtil.LOG_SUCESS);
									}else{
										ThreadUtil.getOrSetcount(pk_rec, ThreadUtil.OPTYPE_S, "f");
										vop.setContent(ret.getMessage());
										vop.setDef_str_1(IContrastUtil.DATAPROCESS_HINT);
										ilm.writToDataLog_RequiresNew(vop);
										writeDipLog("消息流--"+"保存失败--"+sourcemsg, IContrastUtil.LOG_ERROR);
									}
								}else{
									ThreadUtil.getOrSetcount(pk_rec, ThreadUtil.OPTYPE_S, "f");
									Logger.debug("-------ESB校验失败，写入数据处理日志表和错误消息表中");
									vop.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
									ilm.writToDataLog_RequiresNew(vop);
									writeMessageInTable(sourcemsg,strerror);
									writeDipLog("消息流--"+"ESB校验失败--"+sourcemsg, IContrastUtil.LOG_ERROR);
								}
								
							}
						}
						
						
					}
					iscontaings=true;
					break;
				}
			}
			if(!iscontaings){
				vop.setContent("没有找到对应的格式定义："+sourcemsg);
				Logger.debug(new StringBuffer("styleToMsg !contaings").append("没有定义消息总线格式:"+sourcemsg));
				writeDipLog("消息流--"+"没有找到对应的格式定义--"+sourcemsg, IContrastUtil.LOG_ERROR);
				vop.setDef_str_1(IContrastUtil.DATAPROCESS_ERR);
				ilm.writToDataLog_RequiresNew(vop);
				ilm.writToMhccb_RequiresNew(sourcemsg, mvo);
				writeMessageInTable(sourcemsg,strerror);
			}
			
		}

	}
	private RetMessage saveMessage(String msg,EsbMapUtilVO vo){
		if(msg!=null&&!msg.equals("")){
			String esc=vo.getEsc();
			if(esc!=null&&!esc.equals("")){
				
				if(esc.indexOf("#")<0){
					msg=replaceEsc(msg, esc);
				}else{
					String str[]=esc.split("#");
					for(int i=0;i<str.length;i++){
						if(str[i]!=null&&!str[i].equals("")){
							msg=replaceEsc(msg, str[i]);
						}
					}
				}
			}
			Logger.debug("转义符号是-----------  "+esc+"  ----"+"转义符号替换后esb接收消息是------------"+msg);
		}
		
		RetMessage ret=null;
		// 格式间隔
//		String style = vo.getBj();
		String styleesc=vo.getBjesc();
		String tabname=vo.getTablename();//存储表名
		List<DipDatadefinitBVO> ddb=vo.getTypeddb();
		List<DataformatdefBVO> gsl=vo.getData();
		if(gsl==null||gsl.size()<4||ddb==null||ddb.size()<=0){
			return new RetMessage(false,"没有找到数据定义");
		}
		VDipDsreceiveVO dsvo=vo.getSysvo();
		String isrepate=dsvo.getRepeatdata()==null?"":dsvo.getRepeatdata();
		Map<String, DipDatadefinitBVO> map=new HashMap<String, DipDatadefinitBVO>();
		String ename="";
		String value="";
		boolean haspkf=false;
		String pkvalue="";
		String pkfiled="";
		for(DipDatadefinitBVO bvo:ddb){
			if(bvo.getIssyspk()!=null&&bvo.getIssyspk().booleanValue()){
				ename=bvo.getEname()+",";
				value="'"+new SequenceGenerator(null).generate("0001", 1)[0]+"',";
			}
			if(bvo.getIspk()!=null&&bvo.getIspk().booleanValue()){
				pkfiled=bvo.getEname();
				haspkf=true;
			}
			map.put(bvo.getPrimaryKey(), bvo);
		}
		if(msg!=null&&msg.length()>0){
			String[] values=msg.split(styleesc);
			String sql="insert into "+tabname+"(";
			String updatasql="update "+tabname+" set ";
			for(int i=5;i<values.length-1;i++){
				if((i-1)>=gsl.size()){
					break;
				}
				DataformatdefBVO b=gsl.get(i-1);
				if(b.getVdef1()!=null/*&&b.getVdef2()!=null&&b.getVdef2().equals("业务数据")*/){
					if(map.containsKey(b.getVdef1())){
						ename=ename+b.getDatakind()+",";
						DipDatadefinitBVO bvo=map.get(b.getVdef1());
						if(haspkf){
							if(bvo.getEname().toLowerCase().equals(pkfiled.toLowerCase())){
								pkvalue=values[i];
							}
						}
						String type=bvo.getType();
						if(type.toLowerCase().contains("char")){
							value=value+"'"+values[i]+"',";
							updatasql=updatasql+b.getDatakind()+"='"+values[i]+"',";
						}else{
							value=value+values[i]+",";
							updatasql=updatasql+b.getDatakind()+"="+values[i]+",";
						}
					}
				}
			}
			if(value.length()>0){
				sql=sql+ename.substring(0,ename.length()-1)+") values("+value.substring(0,value.length()-1)+")";
				
				try {
					Logger.debug(sql);
//					if(haspkf&&isrepate.equals("0001ZZ10000000018K6M")){//覆盖
					if(haspkf&&isrepate.equals(IContrastUtil.REPEATDATA_CONTROL_FUGAI)){//覆盖	
						updatasql=updatasql.substring(0,updatasql.length()-1)+" where "+pkfiled+"='"+pkvalue+"'";
						String querysql="select count(*) from "+tabname +" where "+pkfiled+"='"+pkvalue+"'";
						String retuc=query.queryfield(querysql);
						if(retuc==null||retuc.equals("0")){
							query.exesql(sql.toString());
						}else{
							query.exesql(updatasql.toString());
						}
//					}else if(haspkf&&isrepate.equals("0001AA10000000018UZ1")){//忽略
					}else if(haspkf&&isrepate.equals(IContrastUtil.REPEATDATA_CONTROL_HULUE)){//忽略
						String querysql="select count(*) from "+tabname +" where "+pkfiled+"='"+pkvalue+"'";
						String retuc=query.queryfield(querysql);
						if(retuc==null||retuc.equals("0")){
							query.exesql(sql);
						}
					}else{//执行
						query.exesql(sql.toString());
					}
					Logger.debug("保存成功");
					ret=new RetMessage(true,"");
				} catch (BusinessException e) {
					e.printStackTrace();
					ret=new RetMessage(false,e.getMessage());
				} catch (SQLException e) {
					e.printStackTrace();
					ret=new RetMessage(false,e.getMessage());
				} catch (DbException e) {
					e.printStackTrace();
					ret=new RetMessage(false,e.getMessage());
				}catch(Exception e){
					e.printStackTrace();
					ret=new RetMessage(false,e.getMessage());
				}
			}
		}
		return ret;
		/**
		 * 1、解析接收到的消息数据，拿到系统标志、站点标志和业务标志；
		 * 2、根据第一步中拿到的数据去数据接收格式定义中获取相应消息总线的数据格式及表名 
		 * 3、根据格式解析数据存放到对应表中
		 */
	}
	
	
	
	public Connection getCon(String pksourceurl) throws Exception{
		DbcontypeVO typevo=null;
		try {
			typevo=(DbcontypeVO) getBaseDAO().retrieveByPK(DbcontypeVO.class, pksourceurl);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new Exception("没有找到相应的数据连接设置！");
		}
		if(typevo==null){
			throw new Exception("没有找到相应的数据连接设置！"+pksourceurl);
		}
		String password=typevo.getPassword();
		String username=typevo.getUsercode();
		String url=typevo.getConstr();
		String classn=typevo.getVdef2();
		if(classn==null||classn.length()<=0){
			throw new Exception("没有找到驱动类类名!");
		}
		Connection con=null;
		String errmsg="";
		Driver dbdriver = null;//"oracle.jdbc.driver.OracleDriver" ;
		dbdriver=(Driver) Class.forName(typevo.getVdef2()).newInstance();
	/*	if(type==null||type.length()==0){
			errmsg="数据连接数据库类型未知";
		}else if(type.equals("oracle")){
			dbdriver =new  oracle.jdbc.driver.OracleDriver() ;
		}else if(type.equals("db2")){
			dbdriver=new com.ibm.db2.jcc.DB2Driver();
//		}else if(type.equals("mysql")){
//			dbdriver=new com.mysql.jdbc.Driver();
		}else if(type.equals("sqlserver")){
			dbdriver=new com.microsoft.sqlserver.jdbc.SQLServerDriver();//.jdbc.sqlserver.SQLServerDriver();
		}else{
			errmsg=type+"类型数据库连接没有实现！";
		}*/
		if(errmsg.length()>0){
			throw new Exception(errmsg);
		}
		try{
			DriverManager.registerDriver(dbdriver);
			con=DriverManager.getConnection(url, username, password);
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("连接数据源失败！"+e.getMessage());
		}
		
		return con;
	}
	private void closeConection(Connection con,PreparedStatement pstmt,ResultSet rs){
		
		try {
			if(rs!=null){
				rs.close();
				//rs=null;
			}
			if(con!=null){
				con.close();
				//con=null;
			}
			if(pstmt!=null){
				pstmt.close();
				//pstmt=null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 处理数据来源类型为中间表时的数据
	 * 
	 * @param vo
	 * @throws DbException 
	 * @throws SQLException 
	 * @throws BusinessException 
	 */
	public RetMessage midTable(DipDatarecHVO vo,String hpk,List<String> errlist){
		// 数据接收格式定义vo
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		Hashtable<String, DipDatadefinitBVO> hashTable = new Hashtable<String, DipDatadefinitBVO>();
		if (null != vo) {
			// 0：顺序，1：字段对照
			Integer datamaptye = 0;
			// 数据字段
			StringBuffer fields = new StringBuffer();
			try {
				//ArgDataRecDVO Dvo = getTbFlAndStyle(vo.getSyscode(), vo.getBackerrinfo(), vo.getBackerrinfo());
				
				String phyname = vo.getDataname();// 物理表名
				// 数据来源连接(dataSourse)
				String sourceurl = vo.getSourcecon();
				
				// 数据来源参数(表名)
				String sourceparam=vo.getSourceparam();
				String sourcearg = sourceparam.split(",")[2];
				// 存储表名
				String memorytable = vo.getMemorytable();
				HashMap<String, String> map = new HashMap<String, String>();
				DataformatdefHVO fhvo = null;
				// 数据格式定义表头
				String pk = "select pk_dataformatdef_h from dip_dataformatdef_h where pk_datarec_h='" + vo.getPk_datarec_h() + "' and rownum<=1 and nvl(dr,0)=0";
				pk = query.queryfield(pk);
				List<DataformatdefHVO> list = (List<DataformatdefHVO>) getBaseDAO().retrieveByClause(DataformatdefHVO.class, "pk_datarec_h='" + vo.getPk_datarec_h() + "' and rownum<=1 and nvl(dr,0)=0");
				if(list!=null&&list.size()>0){
					fhvo=list.get(0);
				}else{
					return new RetMessage(false, "没有找到相应的格式定义的格式！");
				}
				List<DataformatdefBVO> fbvo = null;
				fbvo = (List<DataformatdefBVO>) getBaseDAO() .retrieveByClause( DataformatdefBVO.class, "pk_dataformatdef_h='"  + fhvo.getPrimaryKey() + "' and datakind is not null and nvl(dr,0)=0 order by code");	
				if (fbvo == null || fbvo.size() <= 0) {// 数据格式定义表体
					return new RetMessage(false, "没有找到相应的格式定义的格式！");
				}
				String def_pk="";
				// 2、查数据定义
				List<DipDatadefinitBVO> dataDefinitBvo = null;
				try {// 数据定义表体VO
					dataDefinitBvo = (List<DipDatadefinitBVO>) getBaseDAO().retrieveByClause( DipDatadefinitBVO.class, "pk_datadefinit_h='" + memorytable + "' and nvl(dr,0)=0");
				} catch (DAOException e1) {
					e1.printStackTrace();
					return new RetMessage(false, "查询数据数据格式定义错误！" + e1.getMessage());
				}
				if (dataDefinitBvo == null || dataDefinitBvo.size() <= 0) {
					return new RetMessage(false, "数据定义中没有找掉表的结构定义！");
				}
				for (DipDatadefinitBVO bvo : dataDefinitBvo) {
					hashTable.put(bvo.getEname(), bvo);
					if (bvo.getIspk() != null && bvo.getIspk().booleanValue()&&(bvo.getIssyspk()==null||bvo.getIssyspk().booleanValue()==false)) {
						hashTable.put("#PKFIELD#", bvo);
						def_pk=bvo.getEname();
					}
					if (bvo.getIspk() != null && bvo.getIspk().booleanValue()&&bvo.getIssyspk()!=null&&bvo.getIssyspk().booleanValue()) {
						hashTable.put("#SYSPKFIELD#", bvo);
					}
				}
				
				
				
				// 0：顺序，1：字段对照
				datamaptye = Integer.parseInt(fhvo.getMessflowdef());// 是顺序对照还是字段对照
//				datamaptye=1;
				for (DataformatdefBVO dfbvo : fbvo) {// 对照的map
					if (dfbvo.getDatakind() != null && !"".equals(dfbvo.getDatakind())&&dfbvo.getVdef2().equals("业务数据")) {
						if (datamaptye == 0) {
							
							map.put(dfbvo.getCode() + "", dfbvo.getDatakind());
							fields.append(dfbvo.getDatakind()+",");
						} else {
							if(dfbvo.getCorreskind()!=null&&!dfbvo.getCorreskind().equals("")){
								if(dfbvo.getCode()!=null&&!dfbvo.getCode().equals("")){
									if(dfbvo.getDatakind().equals(def_pk)){
										def_pk=dfbvo.getCorreskind();
									}
								}
								map.put(dfbvo.getCode()+"", dfbvo.getDatakind());
								fields.append(dfbvo.getCorreskind()+",");	
							}
							
						}
						
					}
				}
					if(sourceurl==null||sourceurl.length()<=0){
						return new RetMessage(false,"数据来源连接为空！");
					}else{
						try {
							con=getCon(sourceurl);
						} catch (Exception e) {
							return new RetMessage(false,"数据库连接失败！"+e.getMessage());
						}
					}
					// 根据数据源及表名，查询远程服务器上的数据插入本地库中
					String sql1="select count(*) from "+ sourcearg.toUpperCase() ;
					pstmt=con.prepareStatement(sql1);
					rs=pstmt.executeQuery();
					Integer count=0;
					while(rs.next()){
						count=rs.getInt(1);
					}
					if(count==0){
						return new RetMessage(true,"查询到的数据为空");
					}
					String tab="DY_"+sourcearg.toUpperCase();
					//如果有主键，就把所有的主键都同步过来，然后在分页处理
					if(def_pk!=null&&!def_pk.equals("")&&def_pk.length()>0){
						
						String creattable="create table "+tab+" ( pk varchar2(100) not null,ts varchar2(19))";
						List<String> createsql=new ArrayList<String> ();
						createsql.add(creattable);
						if(!isTableExist(tab)){
							//创建表
							
							boolean ff=iqf.exectEverySql(createsql);
							if(ff){
								return new RetMessage(false,"创建中间表失败！");
							}
						}else{
							String delsql="delete from "+tab;
							iqf.exesql(delsql);
						}
						
						
						
						String ssss=" select "+def_pk+" from  "+sourcearg.toUpperCase();
						pstmt=con.prepareStatement(ssss);
						rs=pstmt.executeQuery();
						List<String> one=new ArrayList<String>();
						while(rs.next()){
							//=rs.getInt(1);
							String ss=rs.getString(1);
							one.add(ss);
						}
						closeConection(con, pstmt, rs);
						//把所有表的主键存到 "表名_dy"
						if(one!=null&&one.size()>0){
							ArrayList<String> insertList=new ArrayList<String>();
							int mk=0;
							for(int m=0;m<one.size();m++){
								String sql3=" insert into "+tab+" (pk) values ('" +one.get(m)+"')";
								insertList.add(sql3);
								mk++;
								if(m==one.size()-1){
									if(insertList.size()>0){
										for(int kk=0;kk<insertList.size();kk++){
											iqf.exesql(insertList.get(kk));
										}
										mk=0;
										insertList=new ArrayList<String> ();
										
										break;
									}
								}
								if(mk==1000){
									if(insertList.size()>0){
										
										for(int kk=0;kk<insertList.size();kk++){
											iqf.exesql(insertList.get(kk));
										}
									}
									
									mk=0;
									insertList=new ArrayList<String> ();
									continue;
								}
							}
						}else{
							return new RetMessage(false,"同步来源表"+sourcearg.toUpperCase()+"没有记录！");
						}
						
						
						
						int h=count%1000;
						if(h!=0){
							h=count/1000+1;
						}else{
							h=count/1000;
						}
						
						for(int p=0;p<h;p++){
//							开始分批插入数据
							String sqls1="select pk from  ( select pk,rownum rwn  from  "+tab+"   where rownum<="+(p+1)*1000+" order by pk ) where rwn>"+p*1000;// order by pk " ;
							ArrayList listqu =(ArrayList) iqf.queryfieldList(sqls1);
							String sql="SELECT  " + fields.substring(0, fields.length()-1).toUpperCase() + " FROM "+ sourcearg.toUpperCase() +" where "+def_pk +" in ( ";
							StringBuffer sb=new StringBuffer(sql);
							for(int y=0;y<listqu.size();y++){
								sb.append("'"+listqu.get(y)+"'");
								if(y<listqu.size()-1){
									sb.append(",");
								}else{
									sb.append(" )");
								}
							}
							RetMessage ret =new RetMessage();
							if(listqu.size()==0){
								return ret;
							}
							
							if(true){
								try {
									con=getCon(sourceurl);
								} catch (Exception e) {
									return new RetMessage(false,"数据库连接失败！"+e.getMessage());
								}
							}
							pstmt=con.prepareStatement(sb.toString());
							rs = pstmt.executeQuery() ;
							TxtDataVO tvo=new TxtDataVO();
							tvo.setRowCount(count);// 去掉标题行
							tvo.setColCount(map.size());
							tvo.setTitlemap(map);
							int i=0;
							while(rs.next())  {
								for (short j = 1; j <=map.size(); j++) {
									tvo.setCellData(i, j-1, rs.getObject(j));
								}
								i++;
							}
							closeConection(con, pstmt, rs);
							
							vo.setAttributeValue("datamaptype", datamaptye);
							vo.setAttributeValue("map", map);
							vo.setAttributeValue("tablename", phyname);
							
							// 3、找文件位置
							
								 ret=inertTxtDataVOFenPi(hpk, tvo, vo, hashTable,null,errlist);
							 if(p==h-1){
								 
								 return ret;
							 }
						}
					}
					
					String sql="SELECT  " + fields.substring(0, fields.length()-1).toUpperCase() + " FROM "+ sourcearg.toUpperCase() ;
					pstmt=con.prepareStatement(sql);
					rs = pstmt.executeQuery() ;
					TxtDataVO tvo=new TxtDataVO();
					tvo.setRowCount(count);// 去掉标题行
					tvo.setColCount(map.size());
					tvo.setTitlemap(map);
					int i=0;
					while(rs.next())  {
						for (short j = 1; j <=map.size(); j++) {
							tvo.setCellData(i, j-1, rs.getObject(j));
						}
						i++;
					}
					vo.setAttributeValue("datamaptype", datamaptye);
					vo.setAttributeValue("map", map);
					vo.setAttributeValue("tablename", phyname);

					return inertTxtDataVO(hpk, tvo, vo, hashTable,null,errlist);

			} catch (DAOException e2) {
				e2.printStackTrace();
				return new RetMessage(false, "查询相应的数据格式定义错误！"
						+ e2.getMessage());
			} catch (BusinessException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
				return new RetMessage(false, "查询sql错误"+ e.getMessage());
			} catch (DbException e) {
				e.printStackTrace();
			}finally{

				if(rs!=null){
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if(pstmt!=null){
					try {
						pstmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if(con!=null){
					try {
						con.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			return null;

		} else {
			Logger.error("条件不足，无法进行相应操作！");
			return new RetMessage(false, "条件不足，无法进行相应操作！");
		}

	}
	/**
	 * 处理数据来源类型为中间表时的数据,输出，写到中间表里边
	 * 
	 * @param vo
	 * @throws DbException 
	 * @throws SQLException 
	 * @throws BusinessException 
	 */
	public RetMessage writeTomidTable(DipDatarecHVO vo,String hpk,List<String> errlist){
		// 数据接收格式定义vo
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		int succcount=0;
		int facount=0;
		int insertcount=0;
		int hlcount=0;
		int fgcount=0;
		Hashtable<String, DipDatadefinitBVO> hashTable = new Hashtable<String, DipDatadefinitBVO>();
		if (null != vo) {
			// 0：顺序，1：字段对照
			Integer datamaptye = 0;
			// 数据字段
			StringBuffer fields = new StringBuffer();
			try {
				String phyname = vo.getDataname();// 物理表名
				// 数据来源连接(dataSourse)
				String sourceurl = vo.getSourcecon();
				// 数据来源参数(表名)
				String sourcearg="";
				if(vo.getSourceparam()!=null&&vo.getSourceparam().contains(",")){
					sourcearg=vo.getSourceparam().split(",")[vo.getSourceparam().split(",").length-1];
				}else{
					sourcearg = vo.getSourceparam();
				}
				// 存储表名
				String memorytable = vo.getMemorytable();
				DipSysregisterHVO xt=(DipSysregisterHVO) getBaseDAO().retrieveByPK(DipSysregisterHVO.class, vo.getPk_xt());
				if(xt==null){
					return new RetMessage(false, "没有找到相应的系统！");
				}
				DipDatadefinitHVO ddfhvo=(DipDatadefinitHVO) getBaseDAO().retrieveByPK(DipDatadefinitHVO.class, memorytable);
				if(ddfhvo==null){
					return new RetMessage(false, "没有找到相应的数据定义！");
				}
				String xtbz=xt.getExtcode();//系统标志
				String zdzj=ddfhvo.getDeploycode();
				String zdbz="";//站点标志
				if(zdzj==null||zdzj.length()<=0){
					zdbz=xt.getDef_str_1();
				}else{
					DipSysregisterBVO sbvo=(DipSysregisterBVO) getBaseDAO().retrieveByPK(DipSysregisterBVO.class, zdzj);
					if(sbvo==null){
						return new RetMessage(false, "没有找到相应的站点！");
					}
					zdbz=sbvo.getSitecode();
				}
				String ywbz=ddfhvo.getBusicode();//业务标志
				HashMap<String, String> map = new HashMap<String, String>();
				DataformatdefHVO fhvo = null;
				// 数据格式定义表头
				String pk = "select pk_dataformatdef_h from dip_dataformatdef_h where pk_datarec_h='" + vo.getPk_datarec_h() + "' and rownum<=1 and nvl(dr,0)=0";
				pk = query.queryfield(pk);
				List<DataformatdefHVO> list = (List<DataformatdefHVO>) getBaseDAO().retrieveByClause(DataformatdefHVO.class, "pk_datarec_h='" + vo.getPk_datarec_h() + "' and rownum<=1 and nvl(dr,0)=0");
				if(list!=null&&list.size()>0){
					fhvo=list.get(0);
				}else{
					return new RetMessage(false, "没有找到相应的格式定义的格式！");
				}
				List<DataformatdefBVO> fbvo = null;
				fbvo = (List<DataformatdefBVO>) getBaseDAO() .retrieveByClause( DataformatdefBVO.class, "pk_dataformatdef_h='"  + fhvo.getPrimaryKey() + "'  and nvl(dr,0)=0 order by code");	
				if (fbvo == null || fbvo.size() <= 0) {// 数据格式定义表体
					return new RetMessage(false, "没有找到相应的格式定义的格式！");
				}
				String def_pk="";//主键
				String def_pkdz="";//主键对照的字段
				
				boolean deleteflag=true;//表示在执行中间表输出时候，要先清空写入数据库的那个表。然后在做插入或者更新
					//中间表输出时候，如果是覆盖的话，要先删除对方表的所有数据，然后再插入
				boolean isfg=true;//默认为覆盖已存在的数据
				String repeatdata=vo.getRepeatdata();
				if(repeatdata!=null&&repeatdata.length()>0){
					try {
						DipRepeatdataVO repvo=(DipRepeatdataVO) getBaseDAO().retrieveByPK(DipRepeatdataVO.class, repeatdata);
						if(repvo!=null&&repvo.getName()!=null&&repvo.getName().indexOf("覆盖")<0){
							isfg=false;
							deleteflag=false;
						}
					} catch (DAOException e) {
						e.printStackTrace();
					}
				}
				
				
				
				
				
//				 2、查数据定义
				List<DipDatadefinitBVO> dataDefinitBvo = null;
				try {// 数据定义表体VO
					dataDefinitBvo = (List<DipDatadefinitBVO>) getBaseDAO().retrieveByClause( DipDatadefinitBVO.class, "pk_datadefinit_h='" + memorytable + "' and nvl(dr,0)=0");
				} catch (DAOException e1) {
					e1.printStackTrace();
					return new RetMessage(false, "查询数据数据格式定义错误！" + e1.getMessage());
				}
				if (dataDefinitBvo == null || dataDefinitBvo.size() <= 0) {
					return new RetMessage(false, "数据定义中没有找掉表的结构定义！");
				}
				for (DipDatadefinitBVO bvo : dataDefinitBvo) {
					hashTable.put(bvo.getEname(), bvo);
					if (bvo.getIspk() != null && bvo.getIspk().booleanValue()&&(bvo.getIssyspk()==null||bvo.getIssyspk().booleanValue()==false)) {
						hashTable.put("#PKFIELD#", bvo);
						def_pk=bvo.getEname().toUpperCase();
					}
					if (bvo.getIspk() != null && bvo.getIspk().booleanValue()&&bvo.getIssyspk()!=null&&bvo.getIssyspk().booleanValue()) {
						hashTable.put("#SYSPKFIELD#", bvo);
						def_pk=bvo.getEname().toUpperCase();
					}
				}
				
				
				
				
				
				
				// 0：顺序，1：字段对照
				datamaptye = Integer.parseInt(fhvo.getMessflowdef());// 是顺序对照还是字段对照
				String selectname="";
				String insertname="";
				
				int zdno=0;
				for (DataformatdefBVO dfbvo : fbvo) { // 对照的map
					if(dfbvo.getVdef2().equals("业务数据")||datamaptye==1){
						if(dfbvo.getVdef2().equals("业务数据")){
							selectname=selectname+dfbvo.getDatakind()+",";
						}else{
							String zd="";
							if(dfbvo.getVdef2().equals("固定标志")){
								if(dfbvo.getDatakind().equals("站点标志")){
									zd="'"+zdbz+"' zdbz_"+zdno;
								}else if(dfbvo.getDatakind().equals("业务标志")){
									zd="'"+ywbz+"' ywbz_"+zdno;
								}else if(dfbvo.getDatakind().equals("系统标志")){
									zd="'"+xtbz+"' xtbz_"+zdno;
								}
							}else if(dfbvo.getVdef2().equals("标志头")||dfbvo.getVdef2().equals("标志尾")||dfbvo.getVdef2().equals("自定义")){
								if(dfbvo.getDatakind()==null||dfbvo.getDatakind().length()<=0){
									zd="'' zdy_"+zdno;
								}else{
									zd="'"+dfbvo.getDatakind()+"' zdy_"+zdno;
								}
							}else if(dfbvo.getVdef2().equals("时间函数")){
								zd="'"+dfbvo.getDatakind()+"' sjhs_"+zdno;
							}else if(dfbvo.getVdef2().equals("记录数函数")){
								zd="'"+dfbvo.getDatakind()+"' jlshs_"+zdno;
							}else if(dfbvo.getVdef2().equals("主键函数")){
								zd="'"+dfbvo.getDatakind()+"' zjhs_"+zdno;
							}
							selectname=selectname+zd+",";
							zdno++;
						}
						if(datamaptye==0){
							if(dfbvo.getVdef2().equals("业务数据")){
								insertname=insertname+dfbvo.getDatakind()+",";
							}else{
								insertname=insertname+((dfbvo.getDatakind()==null||dfbvo.getDatakind().length()<=0)?("zdy_"+zdno):(dfbvo.getDatakind()))+",";
							}
							if(def_pk!=null&&def_pk.trim().length()>0&&dfbvo.getDatakind().equals(def_pk)){
								def_pkdz=dfbvo.getDatakind();//如果是字段对应，找到主键对应的对应项英文名称
							}
						}else{
							insertname=insertname+dfbvo.getCorreskind()+",";
							if(def_pk!=null&&def_pk.trim().length()>0&&dfbvo.getDatakind().equals(def_pk)){
								def_pkdz=dfbvo.getCorreskind();//如果是字段对应，找到主键对应的对应项英文名称
							}
						}
					}
				}
				if(selectname!=null&&selectname.length()>0){
					selectname=selectname.substring(0,selectname.length()-1);
				}
				if(insertname!=null&&insertname.length()>0){
					insertname=insertname.substring(0,insertname.length()-1);
				}
				String count=iqf.queryfield("select count(*) from "+ phyname);
				int s=0;
				if(count!=null&&count.length()>0){
					s=Integer.parseInt(count);
				}
				if(s<=0){
					return new RetMessage(false,"表中没有记录");
				}
				if(sourceurl==null||sourceurl.length()<=0){
					return new RetMessage(false,"数据来源连接为空！");
				}
				
				
				
					int size=10000;//这个是分页插入时候一页的大小。
					int insertsize=50;//这个是插入多少条记录后就关闭连接。
//				if(s>size){
				if(true){
					int k=s%size==0?s/size:s/size+1;
					for(int kkk=0;kkk<k;kkk++){
						String errsql="";
						String sqltemp=selectname;
						String[] sqls=sqltemp.split(",");
						String sqlt="";
						for(int t=0;t<sqls.length;t++){
							sqlt=sqlt+(sqls[t].indexOf(" ")>0?sqls[t].split(" ")[1]:sqls[t])+",";
						}
						String sss="select "+sqlt.substring(0,sqlt.length()-1) +" from ( "
                      +	"select "+selectname+",rownum rwn  from "+phyname+" where rownum<="+((kkk+1)*size>s?s:(kkk+1)*size)
                      +" )  where rwn>"+kkk*size;
						List lists=new ArrayList();
						try{
							lists = iqf.queryListMapSingleSql(sss);
						}catch(Exception e){
							return new RetMessage(false,"查询数据出错"+sss+e.getMessage());
						}
						String insql="";
						try {
//							if(con==null){
								con=getCon(sourceurl);	
//							}
								if(deleteflag){
									pstmt=con.prepareStatement("delete from "+sourcearg);
									pstmt.execute();
									deleteflag=false;
								}
								
								
							for(int i=0;i<lists.size();i++){
								String invalue="";
								String updatesql="update "+sourcearg+" set " ;
								String pkvalue="";
								String selectarry[]=selectname.split(",");
								String insertarry[]=insertname.split(",");
								
								Map mapi=(Map) lists.get(i);
								if(selectarry!=null&&selectarry.length>0&&insertarry!=null&&insertarry.length==selectarry.length){
									for(int j=0;j<selectarry.length;j++){
										String tempvalue;
										String key=selectarry[j].toUpperCase().indexOf(" ")>0?selectarry[j].toUpperCase().split(" ")[1]:selectarry[j].toUpperCase();
										tempvalue=(mapi.get(key)==null?"''":"'"+mapi.get(key)+"'");
										if(def_pk!=null&&def_pk.trim().length()>0&&key.equals(def_pk)){
											pkvalue=tempvalue;
										}
										key=key.toLowerCase();
										if(key.startsWith("sjhs_")){
											tempvalue=new SimpleDateFormat(tempvalue.replace("'", "")).format(new Date());
										}else if(key.startsWith("jlshs_")){
											tempvalue=s+"";
										}else if(key.startsWith("zjhs_")){
											tempvalue="'"+query.getOID()+"'";
										}
										invalue=invalue+tempvalue+",";
										if(def_pk!=null&&def_pk.trim().length()>0){
											updatesql=updatesql+" "+insertarry[j]+"="+tempvalue+",";
										}
									}
									if(def_pk!=null&&def_pk.trim().length()>0){
									 updatesql=updatesql.substring(0,updatesql.length()-1)+" where  "+def_pk+"="+pkvalue+"";
									}
									insql="insert into "+sourcearg+" ( "+insertname+") values ("+invalue.substring(0,invalue.length()-1)+")";
									try{
										if(con==null){
											con=getCon(sourceurl);	
										}
									//*****
									boolean isexist=false;//是否存在
									if(def_pk!=null&&def_pk.trim().length()>0){
										String sql="select count(*) cc from "+sourcearg+" where "+def_pkdz+"="+pkvalue+"";
										errsql=sql;
										pstmt=con.prepareStatement(sql);
										rs=pstmt.executeQuery();
										if(rs!=null&&rs.next()){
											String cc=rs.getString("cc");
											if(cc!=null&&Integer.parseInt(cc)>0){
												isexist=true;
											}
										}
										if(isexist){
											if(isfg){
												errsql=updatesql;
												pstmt=con.prepareStatement(updatesql);
												pstmt.executeUpdate();
												fgcount++;
												succcount++;
											}else{//忽略
												hlcount++;
												succcount++;
											}
											if(succcount+facount>0&&(succcount+facount)%insertsize==0){
												if(con!=null){
													con.commit();
												}
												if(rs!=null){
													rs.close();
												}
												if(pstmt!=null){
													pstmt.close();
												}
												if(con!=null){
													con.close();
												}
												con=null;
												pstmt=null;
												rs=null;
											}
											continue;
										}
										
									}
//									try{
									errsql=insql;
									pstmt=con.prepareStatement(insql);
									pstmt.execute();
									insertcount++;
									succcount++;
									if(succcount+facount>0&&(succcount+facount)%insertsize==0){
										if(con!=null){
											con.commit();
										}
										if(rs!=null){
											rs.close();
										}
										if(pstmt!=null){
											pstmt.close();
										}
										if(con!=null){
											con.close();
										}
										con=null;
										pstmt=null;
										rs=null;
									}
									}catch(Exception e){
										e.printStackTrace();
										errlist.add("["+errsql+"]"+e.getMessage());
										facount++;
										if(succcount+facount>0&&(succcount+facount)%insertsize==0){
											if(rs!=null){
												rs.close();
											}
											if(pstmt!=null){
												pstmt.close();
											}
											if(con!=null){
												con.close();
											}
											con=null;
											pstmt=null;
											
											
										}
									}
								}
								}
						} catch (Exception e) {
							e.printStackTrace();
							return new RetMessage(false,"[连接失败]"+e.getMessage()+"成功"+succcount+"条记录,失败"+facount+"条记录：【其中插入"+insertcount+"条，覆盖"+fgcount+"条，"+"忽略"+hlcount+"条】");
						}finally{
							if(rs!=null){
								rs.close();
								rs=null;
							}
							if(pstmt!=null){
								pstmt.close();
								pstmt=null;
							}
							if(con!=null){
								con.close();
								con=null;
							}
						}
					}
        		}
				//去掉下边不分页查询。
//				else{
//						String sss="select "+selectname +" from "+phyname;
//						List lists=new ArrayList();
//						try{
//							lists = iqf.queryListMapSingleSql(sss);
//						}catch(Exception e){
//							return new RetMessage(false,"查询数据出错"+e.getMessage());
//						}
//						String insql="";
//						try {
////							if(con==null){
////								con=getCon(sourceurl);	
////							}
//							for(int i=0;i<lists.size();i++){
//								String invalue="";
//								String arry[]=selectname.split(",");
//								Map mapi=(Map) lists.get(i);
//								if(arry!=null&&arry.length>0){
//									for(int j=0;j<arry.length;j++){
//										String tempvalue;
//										String key=arry[j].toUpperCase().indexOf(" ")>0?arry[j].toUpperCase().split(" ")[1]:arry[j].toUpperCase();
//										tempvalue=(mapi.get(key)==null?"''":"'"+mapi.get(key)+"'");
//										key=key.toLowerCase();
//										if(key.startsWith("sjhs_")){
//											tempvalue=new SimpleDateFormat(tempvalue.replace("'", "")).format(new Date());
//										}else if(key.startsWith("jlshs_")){
//											tempvalue=s+"";
//										}else if(key.startsWith("zjhs_")){
//											tempvalue="'"+query.getOID()+"'";
//										}
//										invalue=invalue+tempvalue+",";
//									}
//									insql="insert into "+sourcearg+" ( "+insertname+") values ("+invalue.substring(0,invalue.length()-1)+")";
//									if(con==null){
//										con=getCon(sourceurl);	
//									}
//									pstmt=con.prepareStatement(insql);
//									try{
//									boolean ss=pstmt.execute();
//									succcount++;
//									if(succcount+facount>0&&(succcount+facount)%insertsize==0){
//										pstmt.close();
//										con.close();
//										con=null;
//										pstmt=null;
//									}
//									}catch(Exception e){
//										e.printStackTrace();
//										errlist.add("["+insql+"]"+e.getMessage());
//										facount++;
//										if(succcount+facount>0&&(succcount+facount)%insertsize==0){
//											pstmt.close();
//											con.close();
//											con=null;
//											pstmt=null;
//										}
//									}
//								}
//								}
//							} catch (Exception e) {
//								e.printStackTrace();
//								return new RetMessage(false,"[连接失败]"+e.getMessage());
//							}finally{
//								if(pstmt!=null){
//									pstmt.close();
//								}
//								if(con!=null){
//									con.close();
//								}
//								}
//								
//						}
				

			} catch (DAOException e2) {
				e2.printStackTrace();
				return new RetMessage(false, "查询相应的数据格式定义错误！"
						+ e2.getMessage());
			} catch (BusinessException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
				return new RetMessage(false, "查询sql错误"+ e.getMessage());
			} catch (DbException e) {
				e.printStackTrace();
			}finally{

				if(rs!=null){
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if(pstmt!=null){
					try {
						pstmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if(con!=null){
					try {
						con.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			RetMessage rm=new RetMessage(true,"成功"+succcount+"条记录,失败"+facount+"条记录：【其中插入"+insertcount+"条，覆盖"+fgcount+"条，"+"忽略"+hlcount+"条】");
			rm.setFa(facount);
			rm.setSu(succcount);
			return rm;

		} else {
			Logger.error("条件不足，无法进行相应操作！");
			return new RetMessage(false, "条件不足，无法进行相应操作！");
		}

	}
	String prefix="a_";//在查询开始格式，数据格式，结束格式的时候给（自定义常量，开始标志，结束标志加的前缀，防止在查询时候报错）
	
	/**
	 *@desc 输出 数据表里的数据网消息总线文件里边写
	 * 
	 * @param vo
	 * @throws DbException 
	 * @throws SQLException 
	 * @throws BusinessException 
	 */
	public RetMessage writeToESBFile(DipDatarecHVO vo,String hpk,List<String> errlist){
		
		if (null != vo) {
			// 0：顺序，1：字段对照
			// 数据字段
			try {
				
				String phyname = vo.getDataname();// 物理表名
				// 数据来源连接(dataSourse)
				String sourceurl = vo.getSourcecon();
				//流类型
				
				//传输控制标志
				UFBoolean trancon=vo.getTrancon()==null?new UFBoolean(false):new UFBoolean(vo.getTrancon());
				String sendStyle="0";
				// 数据来源参数(表名)
				// 存储表名
				String memorytable = vo.getMemorytable();
				Map<String,DataformatdefHVO> fhmap=new HashMap<String, DataformatdefHVO>(); 
				Map<String,List<DataformatdefBVO>> fbmap=new HashMap<String,List<DataformatdefBVO>>(); 
				Map<String,Integer> delayedmap=new HashMap<String,Integer>(); 
				List<DataformatdefHVO> listfhvo=(List<DataformatdefHVO>) getBaseDAO().retrieveByClause(DataformatdefHVO.class, "pk_datarec_h='" + vo.getPk_datarec_h() + "' and nvl(dr,0)=0");
				List<DipDatarecSpecialTab> listspecialhvo=(List<DipDatarecSpecialTab>) getBaseDAO().retrieveByClause(DipDatarecSpecialTab.class, "pk_datarec_h='" + vo.getPk_datarec_h() + "' and (nvl(dr,0)=0 ) and is_xtfixed='Y' ");
				
				if(listfhvo==null||listfhvo.size()<1){
					return new RetMessage(false, "没有找到相应的格式定义的格式！");
				}
				DipSysregisterHVO xt=(DipSysregisterHVO) getBaseDAO().retrieveByPK(DipSysregisterHVO.class, vo.getPk_xt());
				if(xt==null){
					return new RetMessage(false, "没有找到相应的系统！");
				}
				DipDatadefinitHVO ddfhvo=(DipDatadefinitHVO) getBaseDAO().retrieveByPK(DipDatadefinitHVO.class, memorytable);
				if(ddfhvo==null){
					return new RetMessage(false, "没有找到相应的数据定义！");
				}
				
				String xtbz=xt.getExtcode();//系统标志
				String zdzj=ddfhvo.getDeploycode();
				String zdbz="";//站点标志
				if(zdzj==null||zdzj.length()<=0){
					zdbz=xt.getDef_str_1();
				}else{
					DipSysregisterBVO sbvo=(DipSysregisterBVO) getBaseDAO().retrieveByPK(DipSysregisterBVO.class, zdzj);
					if(sbvo==null){
						return new RetMessage(false, "没有找到相应的站点！");
					}
					zdbz=sbvo.getSitecode();
				}
				String ywbz=ddfhvo.getBusicode();//业务标志
				
				if(listspecialhvo!=null&&listspecialhvo.size()>0){
					for(int i=0;i<listspecialhvo.size();i++){
						DipDatarecSpecialTab specialvo=listspecialhvo.get(i);
						if(specialvo.getName().equals("系统标志")){
							String xtvalue=specialvo.getValue();
							if(xtvalue!=null&&xtvalue.trim().length()>0){
								xtbz=xtvalue;
							}
						}
						if(specialvo.getName().equals("站点标志")){
							String zdvalue=specialvo.getValue();
							if(zdvalue!=null&&zdvalue.trim().length()>0){
								zdbz=zdvalue;
							}
						}
						if(specialvo.getName().equals("业务标志")){
							String ywvalue=specialvo.getValue();
							if(ywvalue!=null&&ywvalue.trim().length()>0){
								ywbz=ywvalue;
							}
						}
					}
				}
				List<String> ziduan=null;
				StringBuffer sql=new StringBuffer();
				if(trancon.booleanValue()){
					if(listfhvo.size()<3){
						return new RetMessage(false, "格式定义不完整！");
					}
					for(DataformatdefHVO hvo:listfhvo){
//						if(hvo.getMessflowtype().equals("0001AA100000000142YS")){
						if(hvo.getMessflowtype().equals(DataformatPanel.MESSTYPE_CSSJ_PK)){
							fhmap.put("d", hvo);
							List<DataformatdefBVO> listbvos=(List<DataformatdefBVO>) getBaseDAO().retrieveByClause(DataformatdefBVO.class, "pk_dataformatdef_h='"+hvo.getPrimaryKey()+"' order by code");
							if(listbvos==null||listbvos.size()<=0){
								return new RetMessage(false, "没有找到相应的格式定义的格式！");
							}else{
								fbmap.put("d", listbvos);
							}
							sendStyle=hvo.getSendstyle();
							delayedmap.put("d", hvo.getDelayed());
							
//						}else if(hvo.getMessflowtype().equals("0001AA100000000142YQ")){
						}else if(hvo.getMessflowtype().equals(DataformatPanel.MESSTYPE_CSKS_PK)){
							fhmap.put("b", hvo);
							List<DataformatdefBVO> listbvos=(List<DataformatdefBVO>) getBaseDAO().retrieveByClause(DataformatdefBVO.class, "pk_dataformatdef_h='"+hvo.getPrimaryKey()+"' order by code");
							if(listbvos==null||listbvos.size()<=0){
								return new RetMessage(false, "没有找到相应的格式定义的格式！");
							}else{
								fbmap.put("b", listbvos);
							}
							delayedmap.put("b", hvo.getDelayed());
//						}else if(hvo.getMessflowtype().equals("0001ZZ10000000018K7M")){
						}else if(hvo.getMessflowtype().equals(DataformatPanel.MESSTYPE_CSJS_PK)){
							fhmap.put("e", hvo);
							List<DataformatdefBVO> listbvos=(List<DataformatdefBVO>) getBaseDAO().retrieveByClause(DataformatdefBVO.class, "pk_dataformatdef_h='"+hvo.getPrimaryKey()+"' order by code");
							if(listbvos==null||listbvos.size()<=0){
								return new RetMessage(false, "没有找到相应的格式定义的格式！");
							}else{
								fbmap.put("e", listbvos);
							}
							delayedmap.put("e", hvo.getDelayed());
						}
					}
					if(!fbmap.containsKey("b")||!fbmap.containsKey("d")||!fbmap.containsKey("e")){
						return new RetMessage(false, "格式定义不完整！");
					}
					
				}else{
					for(DataformatdefHVO hvo:listfhvo){
//						if(hvo.getMessflowtype().equals("0001AA100000000142YS")){
						if(hvo.getMessflowtype().equals(DataformatPanel.MESSTYPE_CSSJ_PK)){	
							fhmap.put("d", hvo);
							List<DataformatdefBVO> listbvos=(List<DataformatdefBVO>) getBaseDAO().retrieveByClause(DataformatdefBVO.class, "pk_dataformatdef_h='"+hvo.getPrimaryKey()+"' order by code");
							if(listbvos==null||listbvos.size()<=0){
								return new RetMessage(false, "没有找到相应的格式定义的格式！");
							}else{
								fbmap.put("d", listbvos);
								break;
							}
						}
					}
				}
				DataformatdefHVO temphvo=fhmap.get("d");
				if(temphvo==null){
					return new RetMessage(false,"没有找到对应的格式定义");
				}
				String fflog="select cdata from dip_messagelogo where pk_messagelogo='"+temphvo.getBeginsign()+"'";
				String fgbj=null;
				/*liyunzhe add 合并数，合并分隔符 start*/
				String mergemarksql="select cdata from dip_messagelogo where pk_messagelogo='"+temphvo.getMergemark()+"'";
				String mergemark=null;
				Integer mergecount=temphvo.getMergecount()==null?0:temphvo.getMergecount();
				Integer pagesysrunsys=temphvo.getPagerunsys()==null?1:temphvo.getPagerunsys();
				String mergeStyle="0";//正常模式0，数据合并1，完整合并2
				if(temphvo.getMergestyle()!=null&&temphvo.getMergestyle().trim().length()>0){
					mergeStyle=temphvo.getMergestyle();
				}
				/*liyunzhe add 合并数，合并分隔符 end*/
				try {
					fgbj=iqf.queryfield(fflog);
					mergemark=iqf.queryfield(mergemarksql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(fgbj==null||fgbj.length()<=0){
					return new RetMessage(false, "没有找到相应的分割标记！");
				}
				int pagesize=0;
				if(!mergeStyle.equals("0")&&mergecount!=null){
					if(mergemark==null||mergemark.length()<=0){
						return new RetMessage(false, "没有找到合并标记！");
					}
					pagesize=pagesysrunsys*mergecount;
					if(pagesize>999999){
						return new RetMessage(false, "一次查询记录不能超过100000条，请减少合并数大小");
					}
				}else{
					pagesize=10000;
					List<DipRunsysBVO> listRunSysBvos=(List<DipRunsysBVO>) getBaseDAO().retrieveByClause(DipRunsysBVO.class, " syscode='DIP-0000015' and nvl(dr,0)=0 ");
					if(listRunSysBvos!=null&&listRunSysBvos.size()>0){
						DipRunsysBVO runsysbvo=listRunSysBvos.get(0);
						String regex="[0-9]*";
						if(runsysbvo.getSysvalue()!=null&&runsysbvo.getSysvalue().matches(regex)){
							pagesize=Integer.parseInt(runsysbvo.getSysvalue());
						}
					}
				}
				
				String split="b,d,e";
				int i=0;
				ziduan=new ArrayList<String>();
				String countsql="select count(*) from "+phyname;
				String count = iqf.queryfield(countsql);
				int s=Integer.parseInt(count);//总共有多少条数据要发送
				if(s==0){
					if(sendStyle==null||sendStyle.equals("")||sendStyle.equals("0")){
						return new RetMessage(false, "没有符合条件的数据！");
					}
				}
				List<String> ziduanb=new ArrayList<String>();
				StringBuffer sqlb=new StringBuffer();
				String msgb="";
				String msge="";
				if(fbmap.containsKey("b")){
					List<DataformatdefBVO> listbvos=fbmap.get("b");
					for(DataformatdefBVO bvoi:listbvos){
						if(bvoi.getVdef2().equals("业务数据")){
							ziduanb.add(bvoi.getDatakind().toUpperCase()+i);
							sqlb.append(bvoi.getDatakind().toUpperCase()+" "+bvoi.getDatakind().toUpperCase()+i+",");
						}else if(bvoi.getVdef2().equals("自定义")||bvoi.getVdef2().equals("标志头")||bvoi.getVdef2().equals("标志尾")){
							ziduanb.add((bvoi.getDatakind()==null?"kg":prefix+bvoi.getDatakind()).toUpperCase()+i);
							sqlb.append("'"+(bvoi.getDatakind()==null?"":bvoi.getDatakind()).toUpperCase()+"' "+(bvoi.getDatakind()==null?"kg":prefix+bvoi.getDatakind()).toUpperCase()+i+",");
						}else if(bvoi.getVdef2().equals("固定标志")){
							if(bvoi.getVdef3().equals("系统标志")){
								ziduanb.add("xtbz_".toUpperCase()+i);
								sqlb.append("'"+xtbz+"' "+"xtbz_"+i+",");
							}else if(bvoi.getVdef3().equals("站点标志")){
								ziduanb.add("zdbz_".toUpperCase()+i);
								sqlb.append("'"+zdbz+"' "+"zdbz_"+i+",");
							}else if(bvoi.getVdef3().equals("业务标志")){
								ziduanb.add("ywbz_".toUpperCase()+i);
								sqlb.append("'"+ywbz+"' "+"ywbz_"+i+",");
							}
						}else if(bvoi.getVdef2().equals("记录数函数")){
							ziduanb.add("jlshs_".toUpperCase()+i);
							sqlb.append("'"+s+"' "+"jlshs_"+i+",");
							
						}else if(bvoi.getVdef2().equals("时间函数")){
							ziduanb.add("sjhs_".toUpperCase()+i);
							sqlb.append("'"+bvoi.getDatakind()+"' "+"sjhs_"+i+",");
						}else if(bvoi.getVdef2().equals("主键函数")){
							ziduanb.add("zjhs_".toUpperCase()+i);
							sqlb.append("'"+bvoi.getDatakind()+"' "+"zjhs_"+i+",");
						}else if(bvoi.getVdef2().equals("报文数函数")){
							ziduanb.add("bwshs_".toUpperCase()+i);
							if(!mergeStyle.equals("0")&&mergecount!=null){
								int w=s%mergecount==0?s/mergecount:s/mergecount+1;
								sqlb.append("'"+w+"'"+"bwshs_"+i+",");
							}else{
								sqlb.append("'"+s+"'"+"bwshs_"+i+",");
							}
						}
						i++;
					}
					
					String querysqlb="";
					if(s>0){
						querysqlb="select "+sqlb.substring(0,sqlb.length()-1)+" from "+phyname +" where rownum<2";
					}else{
						querysqlb="select "+sqlb.substring(0,sqlb.length()-1)+" from dual where rownum<2";
					}
					List listbb=iqf.queryListMapSingleSql(querysqlb);
					if(listbb!=null&&listbb.size()>0){
						HashMap mapi = (HashMap)listbb.get(0);
						String mls=fgbj;
						for(int j = 0;j<ziduanb.size();j++){
							String value=mapi.get(ziduanb.get(j))==null?"":mapi.get(ziduanb.get(j)).toString();
							String zd=ziduanb.get(j).toLowerCase();
							if(zd.startsWith("sjhs_")){
								value=new SimpleDateFormat(value).format(new Date());
							}else if(zd.startsWith("zjhs_")){
								value=query.getOID();
							}
							mls=mls+(value)+fgbj;
						}
						msgb=mls;
					}
				}
				List<String> ziduane=new ArrayList<String>();
				StringBuffer sqle=new StringBuffer();
				if(fbmap.containsKey("e")){
					List<DataformatdefBVO> listbvos=fbmap.get("e");
					for(DataformatdefBVO bvoi:listbvos){
						if(bvoi.getVdef2().equals("业务数据")){
							ziduane.add(bvoi.getDatakind().toUpperCase()+i);
							sqle.append(bvoi.getDatakind().toUpperCase()+" "+bvoi.getDatakind().toUpperCase()+i+",");
						}else if(bvoi.getVdef2().equals("自定义")||bvoi.getVdef2().equals("标志头")||bvoi.getVdef2().equals("标志尾")){
							ziduane.add((bvoi.getDatakind()==null?"kg":prefix+bvoi.getDatakind()).toUpperCase()+i);
							sqle.append("'"+(bvoi.getDatakind()==null?"":bvoi.getDatakind()).toUpperCase()+"' "+(bvoi.getDatakind()==null?"kg":prefix+bvoi.getDatakind()).toUpperCase()+i+",");
						}else if(bvoi.getVdef2().equals("固定标志")){
							if(bvoi.getVdef3().equals("系统标志")){
								ziduane.add("xtbz_".toUpperCase()+i);
								sqle.append("'"+xtbz+"' "+"xtbz_"+i+",");
							}else if(bvoi.getVdef3().equals("站点标志")){
								ziduane.add("zdbz_".toUpperCase()+i);
								sqle.append("'"+zdbz+"' "+"zdbz_"+i+",");
							}else if(bvoi.getVdef3().equals("业务标志")){
								ziduane.add("ywbz_".toUpperCase()+i);
								sqle.append("'"+ywbz+"' "+"ywbz_"+i+",");
							}
						}else if(bvoi.getVdef2().equals("记录数函数")){
							ziduane.add("jlshs_".toUpperCase()+i);
							sqle.append("'"+s+"' "+"jlshs_"+i+",");
							
						}else if(bvoi.getVdef2().equals("时间函数")){
							ziduane.add("sjhs_".toUpperCase()+i);
							sqle.append("'"+bvoi.getDatakind()+"' "+"sjhs_"+i+",");
						}else if(bvoi.getVdef2().equals("主键函数")){
							ziduane.add("zjhs_".toUpperCase()+i);
							sqle.append("'"+bvoi.getDatakind()+"' "+"zjhs_"+i+",");
						}else if(bvoi.getVdef2().equals("报文数函数")){
							ziduane.add("bwshs_".toUpperCase()+i);
							if(!mergeStyle.equals("0")&&mergecount!=null){
								int w=s%mergecount==0?s/mergecount:s/mergecount+1;
								sqle.append("'"+w+"'"+"bwshs_"+i+",");
							}else{
								sqle.append("'"+s+"'"+"bwshs_"+i+",");
							}
						}
						i++;
					}
					String querysqlb="";
					if(s>0){
						querysqlb="select "+sqle.substring(0,sqle.length()-1)+" from "+phyname +" where rownum<2";
					}else{
						querysqlb="select "+sqle.substring(0,sqle.length()-1)+" from dual where rownum<2";
					}
					List listbb=iqf.queryListMapSingleSql(querysqlb);
					if(listbb!=null&&listbb.size()>0){
						HashMap mapi = (HashMap)listbb.get(0);
						String mls=fgbj;
						for(int j = 0;j<ziduane.size();j++){
							String value=mapi.get(ziduane.get(j))==null?"":mapi.get(ziduane.get(j)).toString();
							String zd=ziduane.get(j).toLowerCase();
							if(zd.startsWith("sjhs_")){
								value=new SimpleDateFormat(value).format(new Date());
							}else if(zd.startsWith("zjhs_")){
								value=query.getOID();
							}
							mls=mls+(value)+fgbj;
						}
						msge=mls;
					}
				}
//				for(int k=0;k<split.split(",").length;k++){
					if(fbmap.containsKey("d")){
						List<DataformatdefBVO> listbvos=fbmap.get("d");
						for(DataformatdefBVO bvoi:listbvos){
							if(bvoi.getVdef2().equals("业务数据")){
								ziduan.add(bvoi.getDatakind().toUpperCase()+i);
								sql.append(bvoi.getDatakind().toUpperCase()+" "+bvoi.getDatakind().toUpperCase()+i+",");
							}else if(bvoi.getVdef2().equals("自定义")||bvoi.getVdef2().equals("标志头")||bvoi.getVdef2().equals("标志尾")){
								ziduan.add((bvoi.getDatakind()==null?"kg":prefix+bvoi.getDatakind()).toUpperCase()+i);
								sql.append("'"+(bvoi.getDatakind()==null?"":bvoi.getDatakind()).toUpperCase()+"' "+(bvoi.getDatakind()==null?"kg":prefix+bvoi.getDatakind()).toUpperCase()+i+",");
							}else if(bvoi.getVdef2().equals("固定标志")){
								if(bvoi.getVdef3().equals("系统标志")){
									ziduan.add("xtbz_".toUpperCase()+i);
									sql.append("'"+xtbz+"' "+"xtbz_"+i+",");
								}else if(bvoi.getVdef3().equals("站点标志")){
									ziduan.add("zdbz_".toUpperCase()+i);
									sql.append("'"+zdbz+"' "+"zdbz_"+i+",");
								}else if(bvoi.getVdef3().equals("业务标志")){
									ziduan.add("ywbz_".toUpperCase()+i);
									sql.append("'"+ywbz+"' "+"ywbz_"+i+",");
								}
							}else if(bvoi.getVdef2().equals("记录数函数")){
								ziduan.add("jlshs_".toUpperCase()+i);
								sql.append("'"+s+"' "+"jlshs_"+i+",");
								
							}else if(bvoi.getVdef2().equals("时间函数")){
								ziduan.add("sjhs_".toUpperCase()+i);
								sql.append("'"+bvoi.getDatakind()+"' "+"sjhs_"+i+",");
							}else if(bvoi.getVdef2().equals("主键函数")){
								ziduan.add("zjhs_".toUpperCase()+i);
								sql.append("'"+bvoi.getDatakind()+"' "+"zjhs_"+i+",");
							} if(bvoi.getVdef2().equals("报文数函数")){
								ziduan.add("bwshs_".toUpperCase()+i);
								if(!mergeStyle.equals("0")&&mergecount!=null){
									int w=s%mergecount==0?s/mergecount:s/mergecount+1;
									sql.append("'"+w+"'"+"bwshs_"+i+",");
								}else{
									sql.append("'"+s+"'"+"bwshs_"+i+",");
								}
							}
							i++;
						}
					}
//				}
				String querysql="select "+sql.substring(0,sql.length()-1)+" from "+phyname;
//				String countsql="select count(*) from "+phyname;
//				String count = iqf.queryfield(countsql);
//				int s=Integer.parseInt(count);
//				if(s==0){
//					return new RetMessage(false, "没有符合条件的数据！");
//				}
				List<String> falist=new ArrayList<String>();
				String filename="";
				String code="GBK";
				if(vo.getDatabakfile().equals("文件流")){
					filename=getFileName(vo,xtbz,zdbz,ywbz,phyname);
					String path=vo.getPk_datadefinit_h();
					String pathth="select descriptions from dip_filepath where pk_filepath='"+path+"'";
					String lj=iqf.queryfield(pathth);
					File file = new File(lj); 
					if(!file.exists()){
						file.mkdirs();
					}else{
						if (!file.isDirectory()) { 
						Logger.debug("1"); 
						} else if (file.isDirectory()) { 
							Logger.debug("2"); 
							String[] filelist = file.list(); 
							for (int k = 0; k < filelist.length; k++) { 
								File delfile = new File(lj + "/" + filelist[k]); 
								if (!delfile.isDirectory()) {
									Logger.debug("path=" + delfile.getPath()); 
									Logger.debug("absolutepath=" + delfile.getAbsolutePath()); 
									Logger.debug("name=" + delfile.getName()); 
									if(delfile.getName().startsWith(filename.replace("_t_", ""))){
										delfile.delete(); 
										Logger.debug("删除文件成功"); 
									}
								} 
							}
						}
					}
					filename=lj+"/"+filename;
					
				}
				String wjlname="";
				
				if(s>pagesize){//分页发送
					int pagecount=s%pagesize==0?s/pagesize:s/pagesize+1;
					int totlecount=0;//分页总共发的数据量
					for(int kkk=0;kkk<pagecount;kkk++){
						String sqltemp=sql.substring(0,sql.length()-1);
						String[] sqls=sqltemp.split(",");
						String sqlt="";
						for(int t=0;t<sqls.length;t++){
							sqlt=sqlt+(sqls[t].indexOf(" ")>0?sqls[t].split(" ")[1]:sqls[t])+",";
						}
						String sss="select "+sqlt.substring(0,sqlt.length()-1) +" from ( "
                      +	"select "+sql+"rownum rwn  from "+phyname+" where rownum<="+((kkk+1)*pagesize>s?s:(kkk+1)*pagesize)
                      +" )  where rwn>"+kkk*pagesize;
						List list=new ArrayList();
						try{
							list = iqf.queryListMapSingleSql(sss);
						}catch(Exception e){
							return new RetMessage(false,"查询数据出错"+e.getMessage());
						}
						if(list!=null&&list.size()>0){
							//
							boolean flag=false;
							falist=new ArrayList<String>();
							if(kkk==0&&fbmap.containsKey("e")){
								falist.add(msgb);
								flag=true;
							}
							getMergeData(mergecount, mergeStyle, list, ziduan, fgbj, mergemark, falist);
							if(kkk==(pagecount-1)&&fbmap.containsKey("e")){
								falist.add(msge);
								flag=true;
							}
							if(vo.getDatabakfile().equals("文件流")){
								String fl=filename.replace("_t_", "_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))+"_"+kkk;
								FileOutputStream fos=null;
								Writer ra=null ;
								try {
									wjlname=wjlname+fl+",";
									fos = new FileOutputStream(fl);
									ra = new OutputStreamWriter(fos, code);
									try {
										for(String str:falist){
											ra.write(str+"\n");
										}
										ilm.writToDataLog_RequiresNew(hpk,IContrastUtil.YWLX_TB,"第"+(kkk+1)+"页往文件"+fl+"写入"+(flag==true?falist.size()-1:falist.size())+"条报文--"+IContrastUtil.DATAPROCESS_HINT);
									} catch (IOException e) {
										e.printStackTrace();
										return new RetMessage(false,"往第"+kkk+"个文件里写失败"+e.getMessage());
									}
								} catch (FileNotFoundException e) {
									e.printStackTrace();
									return new RetMessage(false,"往第"+kkk+"个文件里写失败，找不到路径"+e.getMessage());
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
									return new RetMessage(false,"往第"+kkk+"个文件里写失败，不支持的流"+e.getMessage());
								}finally{
									if(ra!=null){
										try {
											ra.close();
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
									if(fos!=null){
										try {
											fos.close();
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								}
								RetMessage rt=writeToPropFile(filename, code, xtbz, zdbz, ywbz, phyname, s, list.size());
								if(!rt.getIssucc()){
									return rt;
								}
								if(rt.getIssucc()){
									ilm.writToDataLog_RequiresNew(hpk,IContrastUtil.YWLX_TB,"第"+(kkk+1)+"页写配置文件"+rt.getValue()+"成功--"+IContrastUtil.DATAPROCESS_HINT);
								}
								if(kkk==pagecount-1){
									return new RetMessage(true,"生成文件成功，总共生成"+pagecount+"个文件和配置文件");
								}
							}else{
								ITaskExecute ite=(ITaskExecute) NCLocator.getInstance().lookup(ITaskExecute.class.getName());
								RetMessage ret=ite.dosendListmsg(sourceurl, falist,trancon.booleanValue(),hpk,kkk,pagecount,delayedmap);
								if(ret.getIssucc()){
									Integer value=(Integer)ret.getValue();
									totlecount+=value;
									ilm.writToDataLog_RequiresNew(hpk,IContrastUtil.YWLX_TB,"第"+(kkk+1)+"页成功发送"+value+"条报文--"+IContrastUtil.DATAPROCESS_HINT);
									if(kkk==pagecount-1){
										return new RetMessage(true,"发送成功，总共发送"+totlecount+"条报文");
									}
								}
								
							}
						}
					}
        		}else{
					List list=new ArrayList();
					try{
						list = iqf.queryListMapSingleSql(querysql);
					}catch(Exception e){
						return new RetMessage(false,"查询数据出错"+e.getMessage());
					}
					boolean dataisnull=false;
//					if(list!=null&&list.size()>0){
						falist=new ArrayList<String>();
						if(fbmap.containsKey("e")){
							falist.add(msgb);
						}
						if(s>0){
							getMergeData(mergecount, mergeStyle, list, ziduan, fgbj, mergemark, falist);
						}else{
							dataisnull=true;
						}
						if(fbmap.containsKey("e")){
							falist.add(msge);
						}else{
							if(dataisnull){
								return new RetMessage(false,"没有符合条件的数据！");
							}
						}
						if(vo.getDatabakfile().equals("文件流")&&falist!=null&&falist.size()>0){

							String fl=filename.replace("_t_", "_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
							FileOutputStream fos=null;
							Writer ra=null ;
							try {
								wjlname=wjlname+fl+",";
								fos = new FileOutputStream(fl);
								ra = new OutputStreamWriter(fos, code);
								try {
									for(String str:falist){
										ra.write(str+"\n");
									}
									if(dataisnull){
										ilm.writToDataLog_RequiresNew(hpk,IContrastUtil.YWLX_TB,"往文件"+fl+"写入开始结束条报文--"+IContrastUtil.DATAPROCESS_HINT);
									}else{
										ilm.writToDataLog_RequiresNew(hpk,IContrastUtil.YWLX_TB,"往文件"+fl+"写入"+falist.size()+"条报文--"+IContrastUtil.DATAPROCESS_HINT);
									}
								} catch (IOException e) {
									e.printStackTrace();
									return new RetMessage(false,"往文件里写失败"+e.getMessage());
								}
							} catch (FileNotFoundException e) {
								e.printStackTrace();
								return new RetMessage(false,"往文件里写失败，找不到路径"+e.getMessage());
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
								return new RetMessage(false,"往文件里写失败，不支持的流"+e.getMessage());
							}finally{
								if(ra!=null){
									try {
										ra.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
								if(fos!=null){
									try {
										fos.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
							RetMessage rt=writeToPropFile(filename, code, xtbz, zdbz, ywbz, phyname, s, list.size());
							if(!rt.getIssucc()){
								return rt;
							}
							if(rt.getIssucc()){
								ilm.writToDataLog_RequiresNew(hpk,IContrastUtil.YWLX_TB,rt.getMessage()+"--"+IContrastUtil.DATAPROCESS_HINT);
							}
							return new RetMessage(true,"生成文件【"+fl+"】和配置文件【"+rt.getValue()+"】成功");
						}else{
							ITaskExecute ite=(ITaskExecute) NCLocator.getInstance().lookup(ITaskExecute.class.getName());
							if(dataisnull){
								RetMessage ret= ite.dosendListmsg(sourceurl, falist,trancon.booleanValue(),hpk,0,1,delayedmap);
								if(ret.getIssucc()){
									return new RetMessage(true,"发送成功，发送开始和结束报文");
								}
							}else{
								return ite.dosendListmsg(sourceurl, falist,trancon.booleanValue(),hpk,0,1,delayedmap);
								
							}
						}
//					}
        		}
			} catch (Exception e) {
				e.printStackTrace();
				return new RetMessage(false, "查询相应的数据格式定义错误！"
						+ e.getMessage());
			} 
			return new RetMessage(true,"成功");
		} else {
			Logger.error("条件不足，无法进行相应操作！");
			return new RetMessage(false, "条件不足，无法进行相应操作！");
		}

	}
	/**
	 * 得到合并后的消息
	 * @param mergecount  合并数
	 * @param mergeStyle  合并类型
	 * @param list        需要发送的数据list
	 * @param ziduan      所有数据的字段list
	 * @param fgbj        数据项之间的分隔符号
	 * @param mergemark   数据分割之间的分割符号
	 * @param falist      最后生成需要发送的list
	 * @return
	 */
	private void getMergeData(int mergecount,String mergeStyle,List list,List<String> ziduan,String fgbj,String mergemark,List<String> falist ){
//		合并消息  0表示正常模式，1表示记录合并，2表示完全合并
		if(mergeStyle!=null&&mergeStyle.equals("2")){
			for(int ls = 0;ls<list.size();ls++){//完整合并
				String mls=fgbj;
				for(int merge=0;merge<mergecount;merge++){
					HashMap mapi = (HashMap)list.get(ls);
					for(int j=0;j<ziduan.size();j++){
						String value=mapi.get(ziduan.get(j))==null?"":mapi.get(ziduan.get(j)).toString();
						String zd=ziduan.get(j).toLowerCase();
						if(zd.startsWith("sjhs_")){
							value=new SimpleDateFormat(value).format(new Date());
						}else if(zd.startsWith("zjhs_")){
							value=query.getOID();
						}
						mls=mls+(value)+fgbj;
					}
					if(merge!=mergecount-1){
						if(ls==list.size()-1){
							mls=mls+mergemark;
							break;
						}
						mls=mls+mergemark+fgbj;
						ls++;
					}else{
						mls=mls+mergemark;
					}
				}
				falist.add(mls);
				
			}
		}else if(mergeStyle!=null&&mergeStyle.equals("1")){
			for(int ls = 0;ls<list.size();ls++){//数据合并
				String mls=fgbj;
				for(int merge=0;merge<mergecount;merge++){
					HashMap mapi = (HashMap)list.get(ls);
					int j=0;
					int size=ziduan.size()-1;
					if(merge>0){
						j=4;
					}
					if(merge==mergecount-1||ls==list.size()-1){
						j=4;
						size=ziduan.size();
					}
					for(;j<size;j++){
						String value=mapi.get(ziduan.get(j))==null?"":mapi.get(ziduan.get(j)).toString();
						String zd=ziduan.get(j).toLowerCase();
						if(zd.startsWith("sjhs_")){
							value=new SimpleDateFormat(value).format(new Date());
						}else if(zd.startsWith("zjhs_")){
							value=query.getOID();
						}
						mls=mls+(value)+fgbj+mergemark;
					}
					if(merge!=mergecount-1){
						if(ls==list.size()-1){
							break;
						}
						mls=mls+fgbj;
						ls++;
					}
					
				}
				falist.add(mls);
				
			}
		}else{//正常模式
			for(int ls = 0;ls<list.size();ls++){
				HashMap mapi = (HashMap)list.get(ls);
				String mls=fgbj;
				for(int j = 0;j<ziduan.size();j++){
					String value=mapi.get(ziduan.get(j))==null?"":mapi.get(ziduan.get(j)).toString();
					String zd=ziduan.get(j).toLowerCase();
					if(zd.startsWith("sjhs_")){
						value=new SimpleDateFormat(value).format(new Date());
					}else if(zd.startsWith("zjhs_")){
						value=query.getOID();
					}
					mls=mls+(value)+fgbj;
				}
				falist.add(mls);
			}
		}
//		return falist;
	}
	
	
	
	private String getFileName(DipDatarecHVO vo,String xtbz,String zdbz,String ywbz,String phyname) {
		String filename="";
		if(vo!=null){
			filename=xtbz+"_"+zdbz+"_"+ywbz+"_"+phyname;
			if(vo.getFormat()!=null&&!vo.getFormat().equals("m1")){
				filename=filename+"_t_";
			}
		}
		return filename;
	}
	public void writeMessageInTable(String msg,String[] str){
		String servername=str[0]==null?"":str[0];
		String servercode=str[1]==null?"":str[1];
		String tablename=str[2]==null?"":str[2];
		String message="";
		if(msg!=null&&msg.length()>4000){
			message=msg.substring(0,4000);
		}else{
			message=msg;
		}
		
		String ts=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		
		String sql="insert into dip_errormessage dd (dd.servername,dd.servercode,dd.tablename,dd.message,dd.ts,dd.dr) values (" +
				"'"+servername+"','"+servercode+"','"+tablename+"','"+message+"','"+ts+"',0)";
		try {
			getBaseDAO().executeUpdate(sql);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	public String replaceEsc(String msg,String esc){
		//Logger.debug("进入replace 方法,   msg=="+msg+ "  -----------   esc=="+esc);
		String msg1=msg;
		if(esc.contains("[")&&esc.contains("]")&&esc.contains(",")){
			esc=esc.replace("[", "");
			esc=esc.replace("]", "");
			esc=esc.trim();
			String str[]=esc.split(",");
			//Logger.debug("转义符号分别是 --  原始是 "+str[0]+"  代替后为--   "+str[1]);
			if(str.length==2){
				if(str[0]!=null&&str[1]!=null){
					if(msg1.contains(str[0])){
						msg1=msg1.replaceAll(str[0], str[1]);
					}
				}
			}
		}
		
		return msg1;

	}
	
	
	public boolean isTableExist(String tableName){
		boolean isExist=false;//默认没有此表
		String sql="select table_name from user_tables where table_name='"+tableName.toUpperCase()+"';";	
		try{
			//ArrayList al=(ArrayList)queryBS.executeQuery(sql, new MapListProcessor());
			ArrayList al=	(ArrayList) iqf.queryfieldList(sql);
			if(al.size()>=1){
				isExist=true;//有此表			
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return isExist;
	}
	
	/**
	 * 数据同步分批插入数据库
	 * @param hpk
	 * @param txtdvo
	 * @param hvo
	 * @param datadefinb
	 * @param filepath
	 * @param errlist
	 * @return
	 */
	int count11=0;//记录数
	int su11=0;//成功条数
	int hulue11=0;//忽略
	int exit11=0;//失败
	int fugai11=0;//覆盖
	int in11=0;//插入
	int jiaoyan=0;//校验失败
	public RetMessage inertTxtDataVOFenPi(String hpk, TxtDataVO txtdvo,
			DipDatarecHVO hvo, Hashtable<String, DipDatadefinitBVO> datadefinb,String filepath,List<String> errlist) {

		DataRecCheckPlugin check=new DataRecCheckPlugin();
		CheckMessage cm=check.doCheck(new Object[]{txtdvo,hvo,datadefinb,filepath});
		List<DataverifyBVO> list=SJUtil.getCheckClassName(hpk, null);
		String tabname = hvo.getAttributeValue("dataname").toString();//表名,同步到目标表的表名称
		String pk = datadefinb.get("#PKFIELD#")==null?"":datadefinb.get("#PKFIELD#").getEname();//同步到目标表的主键
		boolean hasPk=false;
		if(!pk.equals("")){
			hasPk=true;
		}
		Object ob=cm.getValidData();
		List<String> err=new ArrayList<String>();
		RetMessage rm=new RetMessage();;
		boolean isfg=true;//默认为覆盖已存在的数据
		String repeatdata=hvo.getRepeatdata();
		if(repeatdata!=null&&repeatdata.length()>0){
			try {
				DipRepeatdataVO vo=(DipRepeatdataVO) getBaseDAO().retrieveByPK(DipRepeatdataVO.class, repeatdata);
				if(vo!=null&&vo.getName()!=null&&vo.getName().indexOf("覆盖")<0){
					isfg=false;
				}
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}
		if(ob!=null){
			RowDataVO[] rowVOs = (RowDataVO[]) cm.getValidData();//表示要插入到目标表中的数据。
			ilm.writeToTabMonitor_RequiresNew(hpk, IContrastUtil.YWLX_TB, rowVOs.length, cm.getErrList()==null?0:cm.getErrList().size(), "nc.impl.dip.optByPluginSrv.DataRecCheckPlugin");
		
			if(rowVOs!=null&&rowVOs.length>0){
			for(int i=0;i<rowVOs.length;i++){
				RowDataVO vo=rowVOs[i];
				Map map=vo.getTab();
				String sql="";
				try {
					if(hasPk){//表示存在主键，
								String value=(String) map.get(pk);
								String wheresql="  where "+pk+"='"+value+"'";
								String querysql="select count(*) from "+tabname + wheresql;
								String retuc=query.queryfield(querysql);
								
								if(retuc==null||retuc.equals("0")){ //插入数据库
									Iterator set=map.keySet().iterator();
									String insertsql="insert into "+tabname+"(" ;
									String keys="";
									String values="";
									while(set.hasNext()){
										String key=set.next().toString();
									
										keys=keys+key+",";
									
										
										boolean charStyle=false;
										if(datadefinb.get(key)!=null&&datadefinb.get(key).getType()!=null){
											if(datadefinb.get(key).getType().toUpperCase().contains("CHAR")){
												charStyle=true;
											}	
										}
										
										if(charStyle){
											values=values+"'"+map.get(key)+"',";
											
										}else{
											values=values+map.get(key)+",";
										}
										
									}
									if(keys.length()>0){
										insertsql=insertsql+keys.substring(0,keys.length()-1)+") values ( "+values.substring(0,values.length()-1)+")" ;
										sql=insertsql;
										iqf.exesql(insertsql);
										su11=su11+1;
										in11=in11+1;
										count11=count11+1;
									}
								}else{
									
									if(isfg){//覆盖记录
										Iterator set=map.keySet().iterator();
										int k=0;
										String updatesql="update "+tabname+" set " ;
										String keys="";
										String values="";
										while(set.hasNext()){
											k=k+1;
											String key=set.next().toString();
											String mapvalue=map.get(key)==null?"":map.get(key).toString();
											//String mapvalue=(String) map.get(key);
											if(!key.equals(pk)){
												boolean charStyle=false;
												if(datadefinb.get(key)!=null&&datadefinb.get(key).getType()!=null){
													if(datadefinb.get(key).getType().toUpperCase().contains("CHAR")){
														charStyle=true;
													}	
												}
												
												if(charStyle){
													updatesql=updatesql+" "+key+"='"+mapvalue+"'";
												}else{
													updatesql=updatesql+" "+key+"="+mapvalue+"";
												}
													updatesql=updatesql+",";
											}
											
										}
										
											updatesql=updatesql.substring(0,updatesql.length()-1) +wheresql ;
											sql=updatesql;
											iqf.exesql(updatesql);
											su11=su11+1;
											fugai11=fugai11+1;
											count11=count11+1;
									}else{//忽略
										hulue11=hulue11+1;
										count11=count11+1;
									}
									
									
								}
								
					}else{ //插入数据库
						Iterator set=map.keySet().iterator();
						String insertsql="insert into "+tabname+"(" ;
						String keys="";
						String values="";
						while(set.hasNext()){
							String key=set.next().toString();
						
							keys=keys+key+",";
							
							
							boolean charStyle=false;
							if(datadefinb.get(key)!=null&&datadefinb.get(key).getType()!=null){
								if(datadefinb.get(key).getType().toUpperCase().contains("CHAR")){
									charStyle=true;
								}	
							}
							
							if(charStyle){
								values=values+"'"+map.get(key)+"',";
							}else{
								values=values+map.get(key)+",";
							}
							
						}
						if(keys.length()>0){
							insertsql=insertsql+keys.substring(0,keys.length()-1)+") values ( "+values.substring(0,values.length()-1)+")" ;
							sql=insertsql;
							iqf.exesql(insertsql);
							su11=su11+1;
							in11=in11+1;
							count11=count11+1;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					exit11++;
					count11=count11+1;
					err.add("插入第"+count11+"条数据出错！"+e.getMessage()+"  "+sql);
					if(filepath!=null){
						SJUtil.PrintLog(filepath, "inertTxtDataVO", "插入第"+count11+"条数据出错！"+e.getMessage());
					}
				} 
			}
			StringBuffer sbb=new StringBuffer();
			String instr=in11==0?"":"插入"+in11+"条记录,";
			String fugaistr=fugai11==0?"":"覆盖"+fugai11+"条记录,";
			String huluestr=hulue11==0?"":"忽略"+hulue11+"条记录,";
			String sb=exit11==0?"":"失败"+exit11+"条记录,";
			String ss=instr+fugaistr+huluestr;
			if(ss!=null&&!ss.equals("")){
			    ss=ss.substring(0,ss.lastIndexOf(","));
				ss="其中"+ss+"!";
			}
			sbb.append(exit11==0?"":"失败"+exit11+"条记录,");
			sbb.append("成功导入"+su11+"条记录：");
			sbb.append(ss);
			
			
			if(cm.isSuccessful()&&(err==null||err.size()==0)){
//				errlist.addAll(err);

				rm=new  RetMessage(true,sbb.toString(),su11,hulue11,exit11);
			}else{
				if(err!=null){
					errlist.addAll(err);
				}
				if(cm.getErrList()!=null){
					errlist.addAll(cm.getErrList());
				}
				rm= new RetMessage(false,sbb.toString()+"其中数据校验失败"+(cm.getErrList()==null?0:cm.getErrList().size())+"条数据",su11,hulue11,exit11);
				jiaoyan=cm.getErrList()==null?jiaoyan:cm.getErrList().size()+jiaoyan;
			}
			
			}
		 }else{
			 if(cm.getErrList()!=null){
					errlist.addAll(cm.getErrList());
				}
			rm= new RetMessage(false,cm.getMessage(),0,cm.getErrList().size());
		}
		rm.setErrlist(errlist);
		return rm;
	
	}
	public TxtDataVO radeFile(String filepath, int bnum) throws Exception {
		return radeFile( filepath,  bnum,",",null);
	}
	
	public void writeDipLog(String logmsg,String level){
		try {
			diplog.writediplog(logmsg, level);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.debug("保存dip日志出错["+logmsg+"]"+e.getMessage());
		}
	}
	
}
