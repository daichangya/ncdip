package nc.util.dip.sj;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.itf.dip.pub.IQueryField;
import nc.jdbc.framework.exception.DbException;
import nc.ui.pub.ButtonObject;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.uif.pub.exception.UifException;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.dataverify.DataverifyBVO;
import nc.vo.dip.sysregister.DipSysregisterHVO;
import nc.vo.dip.tabstatus.DipTabstatusVO;
import nc.vo.dip.warningset.DipWarningsetVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class SJUtil {
	/**
	*版权信息：商佳科技
	*作者：   王艳冬
	*版本：   
	*描述：   在修改和保存之后更新界面上树的当前节点
	*创建日期：2011-04-14
	*参数
	*@param   BillTreeCardUI ui 当前界面的ui
	*@param   Class hclasstype	当前界面的主表类
	*@param   Class bclasstype  当前界面子表的类
	*@param   AggregatedValueObject	checkVO 当前界面的MyBillVO
	*@param   String pk        
	*返回：   返回执行sql语句是否成功
	 * @throws Exception 
	*/
	public static void refreshTree(BillTreeCardUI ui,Class hclasstype,Class bclasstype,AggregatedValueObject checkVO) throws Exception{
		if(isNull(checkVO)||isNull(checkVO.getParentVO())){
			ui.showErrorMessage("没有界面数据！");
			return;
		}
		String pk=checkVO.getParentVO().getPrimaryKey();
		checkVO.setParentVO(HYPubBO_Client.queryByPrimaryKey(hclasstype, pk));
		if(!isNull(bclasstype)){
			checkVO.setChildrenVO(HYPubBO_Client.queryByCondition(bclasstype, checkVO.getParentVO().getPrimaryKey()+"='"+pk+"' and nvl(dr,0)=0"));
		}
		ui.getBufferData().setCurrentVO(checkVO);
		ui.getBufferData().addVOToBuffer(checkVO);
		ui.getBillTreeSelectNode().setData(checkVO.getParentVO());
		ui.onButtonClicked(ui.getButtonManager().getButton(IBillButton.Cancel));
	}
	/**
	*版权信息：商佳科技
	*作者：   王艳冬
	*版本：   
	*描述：   判断object是否为空或者长度为0
	*创建日期：2011-04-14
	*参数
	*@param   S	任意类型的，但是现在只支持数组、list、map、string、integer、set，其它的类型在这里不支持
	*@param   pk_active	任务类型主键
	*@param   pk_desc	回执消息的主键
	*返回：   返回执行sql语句是否成功
	 * @throws Exception 
	*/
	public static boolean isNull(Object s) {
		if(s==null){
			return true;
		}else{
			return false;
		}
		
	}
	/**
	*版权信息：商佳科技
	*作者：   王艳冬
	*版本：   
	*描述：   往数据处理日志里边写日志，要校验传过来的主键是否在这三个基本档案里边存在
	*创建日期：2011-04-14
	*参数
	*@param   pk_system	系统主键
	*@param   pk_active	任务类型主键
	*@param   pk_desc	回执消息的主键
	*返回：   返回执行sql语句是否成功
	*/
//	public static RetMessage WritLog(String pk_system,String pk_active, String pk_desc) {
//		RetMessage rm=new RetMessage();
//		if(isNull(pk_system)||isNull(pk_active)||isNull(pk_desc)){
//			rm.setIssucc(false);
//			rm.setMessage("传入的数据项有空数据");
//			return rm;
//		}
//		SuperVO sysvo=null;
//		try {
//			sysvo = HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, pk_system);
//		} catch (UifException e2) {
//			e2.printStackTrace();
//		}
//		if(isNull(sysvo)){
//			rm.setIssucc(false);
//			rm.setMessage("没有对应的系统");
//			return rm;
//		}
//		SuperVO actvo=null;
//		try {
//			actvo = HYPubBO_Client.queryByPrimaryKey(DipTaskregisterVO.class, pk_active);
//		} catch (UifException e1) {
//			e1.printStackTrace();
//		}
//		if(isNull(actvo)){
//			rm.setIssucc(false);
//			rm.setMessage("没有对应的任务类型");
//			return rm;
//		}
//		DateprocessVO vo=new DateprocessVO();
//		vo.setActive(pk_active);
//		vo.setSysname(pk_system);
//		vo.setContent(pk_desc);
//		String ret="";
//		try {
//			ret = HYPubBO_Client.insert(vo);
//		} catch (UifException e) {
//			e.printStackTrace();
//		}
//		if(ret!=null&&ret.length()==20){
//			rm.setIssucc(true);
//			rm.setMessage("插入日志成功");
//			return rm;
//		}else{
//			rm.setIssucc(false);
//			rm.setMessage("执行失败！原因插入日志失败");
//			return rm;
//		}
//	}
	/**
	*版权信息：商佳科技
	*作者：   王艳冬
	*版本：   
	*描述：   往【表状态监控】里边写日志，要校验传过来的主键是否在这三个基本档案里边存在
	*创建日期：2011-04-14
	*参数
	*@param   pk_system	系统主键（为了展示系统的编码和系统的名称）
	*@param   pk_datadif	数据定义主键（为了展示当前的表名中文名和英文名）
	*@param   pk_desc	表状态（数据接收中、数据加工中）//暂时没有校验，直接保存
	*返回：   返回执行sql语句是否成功
	*/
	public static RetMessage WritTabStatus(String pk_system,String pk_datadif, String pk_desc) {
		RetMessage rm=new RetMessage();
		if(isNull(pk_system)||isNull(pk_datadif)||isNull(pk_desc)){
			rm.setIssucc(false);
			rm.setMessage("传入的数据项有空数据");
			return rm;
		}
		SuperVO sysvo=null;
		try {
			sysvo = HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, pk_system);
		} catch (UifException e2) {
			e2.printStackTrace();
		}
		if(isNull(sysvo)){
			rm.setIssucc(false);
			rm.setMessage("没有对应的系统");
			return rm;
		}
		SuperVO actvo=null;
		try {
			actvo = HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, pk_system);
		} catch (UifException e1) {
			e1.printStackTrace();
		}
		if(isNull(actvo)){
			rm.setIssucc(false);
			rm.setMessage("没有对应的表名");
			return rm;
		}
		DipTabstatusVO vo=new DipTabstatusVO();
		vo.setTabdate(pk_datadif);
		vo.setSyscode(pk_system);
		vo.setTabstatus(pk_desc);
		String ret="";
		try {
			ret = HYPubBO_Client.insert(vo);
		} catch (UifException e) {
			e.printStackTrace();
		}
		if(ret!=null&&ret.length()==20){
			rm.setIssucc(true);
			rm.setMessage("插入日志成功");
			return rm;
		}else{
			rm.setIssucc(false);
			rm.setMessage("执行失败！原因插入日志失败");
			return rm;
		}
	}
	/**
	*版权信息：商佳科技
	*作者：   王艳冬
	*版本：   
	*描述：   返回是否当前表的当前任务类型存在预警
	*创建日期：2011-04-14
	*参数
	*@param   pk_bustype	预警的任务类型主键
	*@param   pk_bustab		当前选中条目的主表主键
	*返回：   返回在预警中是否有此条目的预警配置
	 * @throws UifException 
	*/
	public static boolean isExitWarning(String pk_bustype,String pk_bustab) throws UifException{
		DipWarningsetVO[] warvo=(DipWarningsetVO[]) HYPubBO_Client.queryByCondition(DipWarningsetVO.class, " tasktype='"+pk_bustype+"' and pk_bustab='"+pk_bustab+"'");
		if(isNull(warvo)||warvo.length==0){
			return false;
		}else{
			return true;
		}
	}
	
	
	
	//main方法
	public static void main(String [] ss){
		String[] l=new String[2];
		try {
			System.out.println(SJUtil.isNull(l));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String getYWbyYWLX(String tasktype){
		String yw="";
		if(tasktype.equals("0001AA1000000001A2YB")){
			yw=IContrastUtil.YWLX_TB;
		}else if(tasktype.equals("0001AA1000000001A5AX")){
			yw=IContrastUtil.YWLX_JG;
		}else if(tasktype.equals("0001AA1000000001A5AY")){
			yw=IContrastUtil.YWLX_ZH;
		}else if(tasktype.equals("0001AA1000000001A5AZ")){
			yw=IContrastUtil.YWLX_FS;
		}else if(tasktype.equals("0001AA1000000001FVAD")){
			yw=IContrastUtil.YWLX_LC;
		}else if(tasktype.equals("0001AA1000000003CO5U")){
			yw=IContrastUtil.YWLX_TYZH;
		}else if(tasktype.equals("0001AA1000000003D2T0")){
			yw=IContrastUtil.YWLX_ESBSEND;
		}else if(tasktype.equals("0001AA100000000343JO")){
			yw=IContrastUtil.YWLX_DZ;
		}else if(tasktype.equals("0001AA1000000003HBMO")){
			yw=IContrastUtil.YWLX_ZTYZ;
		}else if(tasktype.equals("0001AA1000000003JZA2")){
			yw=IContrastUtil.YWLX_TYZHXML;
		}
		return yw;
	}
	public static String getYWnameByLX(String tasktype){
		IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		String pk="";
		if(tasktype.equals(IContrastUtil.YWLX_TB)){
			pk="0001AA1000000001A2YB";
		}else if(tasktype.equals(IContrastUtil.YWLX_LC)){
			pk="0001AA1000000001FVAD";
		}else if(tasktype.equals(IContrastUtil.YWLX_JG)){
			pk="0001AA1000000001A5AX";
		}else if(tasktype.equals(IContrastUtil.YWLX_ZH)){
			pk="0001AA1000000001A5AY";
		}else if(tasktype.equals(IContrastUtil.YWLX_FS)){
			pk="0001AA1000000001A5AZ";
		}else if(tasktype.equals(IContrastUtil.YWLX_DZ)){
			pk="0001AA100000000343JO";
		}else if(tasktype.equals(IContrastUtil.YWLX_JS)){
			return "ESB接收";
		}else if(tasktype.equals(IContrastUtil.YWLX_TYZH)){
			pk="0001AA1000000003CO5U";
		}else if(tasktype.equals(IContrastUtil.YWLX_ESBSEND)){
			pk="0001AA1000000003D2T0";
		}else if(tasktype.equals(IContrastUtil.YWLX_ZTYZ)){
			pk="0001AA1000000003FPPF";
		}else if(tasktype.equals(IContrastUtil.YWLX_TYZHXML)){
			pk="0001AA1000000003JZA2";
		}
		String sql="SELECT name FROM dip_taskregister where pk_taskregister='"+pk+"'";
		try {
			return iqf.queryfield(sql);
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
		return pk;
	}
	/**
	 * @desc 判断是否参照已经被引用
	 * @prarm refName 参照的名称。类似<nc.ui.bd.ref.StatusRegistRefModel>
	 * @param pk 当前主键
	 * */
	public static String isExitRef(String refName,String pk){
		String isref="";
		
		String sql="select itemkey,table_code,idcolname,table_name from pub_billtemplet_b where nvl(dr,0)=0 and reftype='"+refName+"'";
		IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		List<Map> res=null;
		try {
			res=iqf.queryListMapSingleSql(sql);
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
		if(res!=null&&res.size()>0){
			for(Map map:res){
				String table_code=map.get("table_code".toUpperCase()).toString();
				String itemkey=map.get("itemkey".toUpperCase()).toString();
				Object ob=map.get("idcolname".toUpperCase());
				String tablename=map.get("table_name".toUpperCase()).toString();
				if(ob!=null&&ob.toString().length()>0){
					itemkey=ob.toString();
				}
				String sqli="select "+itemkey +" from "+table_code+" where "+itemkey+"='"+pk+"' and nvl(dr,0)=0";
				List reso=null;
				try {
					reso=iqf.queryfieldList(sqli);
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
				if(reso!=null&&reso.size()>0){
					return tablename;
				}
				
			}
		}
		
		return isref;
	}
	/**
	 * @desc 根据传入的业务类型和业务主键，找到相应的注册校验插件类的类名
	 * @param String pk_bus 业务数据的主键
	 * @param String ywlx 业务类型，取值为  IContrastUtil.YWLX_DY,IContrastUtil.YWLX_ZH,IContrastUtil.YWLX_TB....
	 * 
	 * */
	public static List<DataverifyBVO> getCheckClassName(String pk_bus,String ywlx){
		List<DataverifyBVO> list=new ArrayList<DataverifyBVO>();
		try {
			list=(List<DataverifyBVO>) new BaseDAO(IContrastUtil.DESIGN_CON).retrieveByClause(DataverifyBVO.class, "pk_dataverify_h=(select pk_dataverify_h from dip_dataverify_h where pk_datachange_h='"+pk_bus+"' and nvl(dr,0)=0 and rownum<=1) and nvl(dr,0)=0");
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	private static SimpleDateFormat sdf_time   = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	public static boolean PrintLog(String filepath, String FunctionName, String msg) {
		PrintWriter fos = null;
		try {
			String pathe=RuntimeEnv.getInstance().getNCHome()+ "/temp";
			File parentDir = new File(pathe);
			if (!parentDir.exists()) {
				parentDir.mkdirs();
			}
			filepath=pathe+"/datachange"+".log";
			//写文件			
			fos = new PrintWriter(new BufferedWriter( new FileWriter(filepath, true)));
			String logmsg = "【" + sdf_time.format(new Date()) + "】【" + FunctionName + "】【" + msg + "】\r\n";
			fos.write(logmsg);
			fos.flush();
			fos.close();
		}catch(FileNotFoundException fnfe) {
			if(fos != null) { fos.close(); }						
			fnfe.printStackTrace();
			return false;
		}catch(IOException ioe) {
			if(fos != null) { fos.close(); }
			ioe.printStackTrace();
			return false;
		}catch(Exception e) {
			if(fos != null) { fos.close(); }
			e.printStackTrace();
			return false;			
		}finally {
			if(fos != null) { fos.close(); }
			fos = null;
		}
		return true;
	}
	static Map<String,DipSysregisterHVO> sysvoMap;
	public static DipSysregisterHVO getSysVOByPK(String pk){
		if(sysvoMap==null){
			sysvoMap=new HashMap();
			IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
			List<DipSysregisterHVO> list=null;
			try {
				list=iqf.querySupervoByVoname(DipSysregisterHVO.class, " nvl(dr,0)=0");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(list!=null&&list.size()>0){
				for(DipSysregisterHVO hvo:list){
					sysvoMap.put(hvo.getPrimaryKey(), hvo);
				}
			}
		}
		if(pk!=null){
			if(sysvoMap.get(pk)!=null){
				return sysvoMap.get(pk);
			}else{
				List<DipSysregisterHVO> list=null;
				try {
					IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
					list=iqf.querySupervoByVoname(DipSysregisterHVO.class, "pk_sysregister_h='"+pk+"' and nvl(dr,0)=0");
					//IUAPQueryBS bs=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
					//bs.retrieveByPK(DipSysregisterHVO.class,pk+" and nvl(dr,0)=0");
					//String sql=" pk_sysregister_h = '"+pk+"' and nvl(dr,0)=0";
					//list=(List<DipSysregisterHVO>) bs.retrieveByClause(DipSysregisterHVO.class, sql);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(list!=null&&list.size()>0){
					if(list!=null&&list.size()>0){
						for(DipSysregisterHVO hvo:list){
							sysvoMap.put(hvo.getPrimaryKey(), hvo);
						}
					}
					return sysvoMap.get(pk);
				}else{
					return null;
				}
			}
		}else{
			return null;
		}
	}
	
	public static void setAllButtonsEnalbe( ButtonObject[] btn){
		for(int i=0;i<btn.length;i++){
			btn[i].setEnabled(false);
		}
	}
	
	public static Object getCellValue(HSSFCell cell) {
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
			//2011-6-23 cl 增加了第1166--1183行
			if(value.contains(".")&& value.contains("E")){
				try{
					String sub=value.substring(0, value.length()-2);
					String temp=sub.replace(".", "-");
					int lastNo=Integer.parseInt(temp.split("-")[1]);
					if(lastNo==0){
						rst = Integer.parseInt(temp.split("-")[0]);
					}else{
						rst = new UFDouble(cell.getNumericCellValue());
						if(rst.toString().contains(".")){
							String str=rst.toString().substring(0, rst.toString().lastIndexOf("."));
							rst=Integer.parseInt(str.toString());
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
					rst = new UFDouble(cell.getNumericCellValue());
				}
			}else if (value.contains(".")) {
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
	
}
