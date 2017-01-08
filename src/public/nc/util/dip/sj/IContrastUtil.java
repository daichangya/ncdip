package nc.util.dip.sj;

import java.util.HashMap;
import java.util.Map;

import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.sysregister.DipSysregisterBVO;
import nc.vo.dip.sysregister.DipSysregisterHVO;

public class IContrastUtil {
	//VERSION的值必须是nc502或者nc507。
	public final static String VERSION="nc507";
	public final static String DATAPROCESS_ERR="错误";
	public final static String DATAPROCESS_HINT="提示";
	public final static String DESIGN_CON="ncdip";
	
	public final static Integer WH_ADD=0;//数据对照维护的增加
	public final static Integer WH_REMOVE=1;//数据对照维护的删除
	
	public final static String TABMONSTA_ON="jxz";//表状态监控的运行中
	public final static String TABMONSTA_FIN="finash";//表状态监控的运行完成
	
	
	public final static String YWLX_DY="dy";//数据定义
	public final static String YWLX_JS="js";//数据接收
	public final static String YWLX_TB="tb";//数据同步
	public final static String YWLX_DZ="dz";//数据对照定义
	public final static String YWLX_JG="jg";//数据加工
	public final static String YWLX_ZH="zh";//数据转换
	public final static String YWLX_FS="fs";//数据发送
	public final static String YWLX_ESBSEND="esbsend";//文件操作
	public final static String YWLX_LC="lc";//数据流程
	public final static String YWLX_SJQX="sjqx";//数据权限定义
	public final static String YWLX_TYZH="tyzh";//通用转换器
	public final static String YWLX_TYZHXML="tyzhxml";//通用XML转换器
	public final static String YWLX_ZTYZ="ztyz";//状态预置
	public final static String SYSCODE="SYS001";//回写到数据定义文件夹
	public final static String SYSNAME="系统文件夹";//回写到数据定义文件夹
	
	public final static String LOG_ALL="ALL";//add by zhw 2012-05-18 DIP记录日志级别
	public final static String LOG_ERROR="ERROR";//add by zhw 2012-05-18 DIP记录日志级别
	public final static String LOG_SUCESS="SUCESS";//add by zhw 2012-05-18 DIP记录日志级别
	
	public final static String ESCS="$,(,),*,+,.,[,],?,\\,^,{,},|";//转义字符
	



//add by liyunzhe 2012-06-06 start   来源类型基本档案修改

	/**
	 *0001AA10000000021R3Y    格式文件
	 */
	public final static String DATAORIGIN_GSWJ="0001AA10000000021R3Y";
	/**
	 * 0001AA1000000001NY7F    中间表
	 */
	public final static String DATAORIGIN_ZJB="0001AA1000000001NY7F";
	/**
	 * 0001AA10000000013SVH    主动抓取
	 */
	public final static String DATAORIGIN_ZDZQ="0001AA10000000013SVH";
	/**
	 * 0001AA10000000013SVI    消息总线
	 */
	public final static String DATAORIGIN_XXZX="0001AA10000000013SVI";
	/**
	 * 0001AA1000000003493X    webservice抓取	
	 */
	public final static String DATAORIGIN_WEBSERVICEZQ="0001AA1000000003493X";
//	add by liyunzhe 2012-06-06 end
	/** 数据加工类型档案 主键 0001AA1000000002G4UD，名称是数据卸载*/
	public final static String PROCESSSTYLE_SJXZ="0001AA1000000002G4UD";
	/** 数据加工类型档案 主键0001AA1000000002TJVJ ，名称是 自定义*/
	public final static String PROCESSSTYLE_ZDY="0001AA1000000002TJVJ";
	/** 数据加工类型档案 主键0001AA1000000003HBMO ，名称是 状态预置*/
	public final static String PROCESSSTYLE_ZTYZ="0001AA1000000003HBMO";
	/** 重复数据控制 0001AA10000000018UZ1  忽略已存在数据 */
	public final static String REPEATDATA_CONTROL_HULUE="0001AA10000000018UZ1";
	/** 重复数据控制 0001ZZ10000000018K6M 覆盖已存在数据 */
	public final static String REPEATDATA_CONTROL_FUGAI="0001ZZ10000000018K6M";
	
	
    /**	1	01	基础信息结构	0001BB100000000JJOKC*/
	public final static String DATASTYLE_BASEINF="0001BB100000000JJOKC";
	/**	2	02	业务信息结构	0001BB100000000JJOKM*/
	public final static String DATASTYLE_BUSINESSINF="0001BB100000000JJOKM";
	/**		3	03	系统信息结构	0001BB100000000JKANO*/
	public final static String DATASTYLE_SYSTEMINF="0001BB100000000JKANO";
	/**		4	04	加工信息结构	0001BB100000000JKANP*/
	public final static String DATASTYLE_PROCESSINF="0001BB100000000JKANP";
	
	/** 数据接口平台写数据字典时候最外层的文件夹名称是   数据接口平台*/
	public final static String DATADICTFATHER_NAME="数据接口平台";
	/** 数据接口平台写数据字典时候最外层的文件夹编码是  sjjkpt*/
	public final static String DATADICTFATHER_CODE="sjjkpt";
	/** 数据接口平台写数据字典时候第二层的文件夹名称是 接口平台*/
	public final static String DATADICTFORDER_NAME="接口平台";
	/** 数据接口平台写数据字典时候第二层的文件夹编码是 jkpt*/
	public final static String DATADICTFORDER_CODE="jkpt";
	
	/**数据同步定义主动抓取参数显示中文名称*/
	public final static String DATAORIGIN_ZDZQ_PARAMETAR="数据源,用户名,表名";
	/**数据同步定义消息总线 文件流 ，输入参数显示中文名称*/
	public final static String DATAORIGIN_XXZX_IN_FILE_PARAMETAR="文件名称";
	/**数据同步定义格式文件参数显示中文名称*/
	public final static String DATAORIGIN_GSWJ_PARAMETAR="文件名称";
	/**数据同步定义 中间表 参数显示中文名称*/
	public final static String DATAORIGIN_ZJB_PARAMETAR="数据源,用户名,表名";
	/**数据同步定义 webservic抓取 参数显示中文名称*///系统标识，站点标志，业务标识，唯一标识,取数个数,第几次取数
//	public final static String DATAORIGIN_WEBSERVICEZQ_PARAMETAR="系统标识,站点标志,业务标识,唯一标识,取数个数,第几次取数";
	public final static String DATAORIGIN_WEBSERVICEZQ_PARAMETAR="数据源,用户名,表名";
	/**webservice抓取在同步时候分页大小 */
	public final static int DATAORIGIN_WEBSERVICEZQ_PAGESIZE=5000;
	
	
	
	//每种类型的PK字段
	private static Map<String,String> pkMap=new HashMap<String,String>();
	public static Map getPkMap(){
		if(pkMap==null||pkMap.size()<=0){
			pkMap=new HashMap<String,String>();
			pkMap.put(YWLX_DY, "pk_datadefinit_h");
			pkMap.put(YWLX_JS, "pk_datarec_h");
			pkMap.put(YWLX_TB, "pk_datasynch");
			pkMap.put(YWLX_DZ, "pk_contdata_h");
			pkMap.put(YWLX_JG, "pk_dataproce_h");
			pkMap.put(YWLX_ZH, "pk_datachange_h");
			pkMap.put(YWLX_FS, "pk_datasend");
			pkMap.put(YWLX_ESBSEND, "pk_esbsend");
			pkMap.put(YWLX_LC, "pk_processflow_h");
			pkMap.put(YWLX_TYZH, "pk_tyzhq_h");
			pkMap.put(YWLX_SJQX, "pk_contdata_h");
			pkMap.put(YWLX_ZTYZ, "pk_statemanage_h");
			pkMap.put(YWLX_TYZHXML, "pk_tyxml_h");
		}
		return pkMap;
	}
	//每种类型的父主键字段
	private static Map<String,String> fpkMap=new HashMap<String,String>();
	public static Map getFpkMap(){

		if(fpkMap==null||fpkMap.size()<=0){
			fpkMap=new HashMap<String,String>();
					fpkMap.put(YWLX_DY, "PK_SYSREGISTER_H".toLowerCase());
					fpkMap.put(YWLX_LC, "fpk");
					fpkMap.put(YWLX_JS, "fpk");
					fpkMap.put(YWLX_TB, "fpk");
					fpkMap.put(YWLX_ZH, "fpk");
					fpkMap.put(YWLX_JG, "fpk");
					fpkMap.put(YWLX_FS, "pk_sys");
					fpkMap.put(YWLX_DZ, "pk_sysregister_h");
					fpkMap.put(YWLX_TYZH, "fpk");
					fpkMap.put(YWLX_ESBSEND, "pk_sys");
					fpkMap.put(YWLX_SJQX, "pk_sysregister_h");
					fpkMap.put(YWLX_ZTYZ, "fpk");
					fpkMap.put(YWLX_TYZHXML, "fpk");
		}
		return fpkMap;
	}
	//每种类型的数据表主键字段
	private static Map<String,String> tabpkMap=new HashMap<String,String>();
	public static Map getTabpkMap(){
		if(tabpkMap==null||tabpkMap.size()<=0){
			tabpkMap=new HashMap<String,String>();
					tabpkMap.put(YWLX_DY, "memorytable");
					tabpkMap.put(YWLX_LC, "");
					tabpkMap.put(YWLX_JS, "memorytable");
					tabpkMap.put(YWLX_TB, "datasite");
					tabpkMap.put(YWLX_ZH, "busidata");
					tabpkMap.put(YWLX_JG, "firsttab");
					//2011-5-30
					tabpkMap.put(YWLX_FS, "filepath");
					tabpkMap.put(YWLX_DZ, "contabcode");
					tabpkMap.put(YWLX_TYZH, "sourcetab");
					tabpkMap.put(YWLX_ESBSEND, "filepath");
					tabpkMap.put(YWLX_TYZHXML, "sourcetab");
		}
		return tabpkMap;
	}
	private static Map<String,String> tabNameMap=new HashMap<String,String>();
	public static Map getTabNameMap(){

		if(tabNameMap==null||tabNameMap.size()<=0){
			tabNameMap=new HashMap<String,String>();
					tabNameMap.put(YWLX_DY, "dip_datadefinit_h");
					tabNameMap.put(YWLX_LC, "dip_processflow_h");
					tabNameMap.put(YWLX_JS, "dip_datarec_h");
					tabNameMap.put(YWLX_TB, "dip_datasynch");
					tabNameMap.put(YWLX_ZH, "dip_datachange_h");
					tabNameMap.put(YWLX_JG, "dip_dataproce_h");
					tabNameMap.put(YWLX_FS, "dip_datasend");
					tabNameMap.put(YWLX_DZ, "dip_contdata");
					tabNameMap.put(YWLX_TYZH, "dip_tyzhq_h");
					tabNameMap.put(YWLX_ESBSEND, "dip_esbsend");
					tabNameMap.put(YWLX_SJQX, "dip_adcontdata");
					tabNameMap.put(YWLX_ZTYZ, "dip_statemanage_h");
					tabNameMap.put(YWLX_TYZHXML, "dip_tyxml_h");
		}
		return tabNameMap;
	}
		//控制处理状态
/*	private static Map<String,String> tabStartMap=new HashMap<String,String>();
	public static Map getTabStartMap(){

		if(tabStartMap==null||tabStartMap.size()<=0){
			tabStartMap=new HashMap<String,String>();
					tabStartMap.put(YWLX_DY, "dispostatus");
					tabStartMap.put(YWLX_LC, "");
					tabStartMap.put(YWLX_JS, "constatus");
					tabStartMap.put(YWLX_TB, "constatus");
					tabStartMap.put(YWLX_ZH, "constatus");
					tabStartMap.put(YWLX_JG, "constatus");
					tabStartMap.put(YWLX_FS, "contorlstatus");
		}
		return tabStartMap;
	
	}*/
		//控制结束状态
	/*private static Map<String,String> tabEndMap=new HashMap<String,String>();
	public static Map getTabEndMap(){
		if(tabEndMap==null||tabEndMap.size()<=0){
			tabEndMap=new HashMap<String,String>();
				tabEndMap.put(YWLX_DY, "endstatus");
				tabEndMap.put(YWLX_LC, "");
				tabEndMap.put(YWLX_JS, "endstatus");
				tabEndMap.put(YWLX_TB, "endstatus");
				tabEndMap.put(YWLX_ZH, "endstatus");
				tabEndMap.put(YWLX_JG, "endstatus");
				tabEndMap.put(YWLX_FS, "endstatus");
		}
		return tabEndMap;
	}*/
//		业务名称
	private static Map<String,String> tabName=new HashMap<String,String>();
	public static Map getTabName(){
		if(tabName==null||tabName.size()<=0){
			tabName=new HashMap<String,String>();
					tabName.put(YWLX_DY, "数据定义");
					tabName.put(YWLX_LC, "数据流程");
					tabName.put(YWLX_JS, "数据接收定义");
					tabName.put(YWLX_TB, "数据同步");
					tabName.put(YWLX_ZH, "数据转换");
					tabName.put(YWLX_JG, "数据加工");
					tabName.put(YWLX_FS, "数据发送");
					tabName.put(YWLX_DZ, "数据对照");
					tabName.put(YWLX_TYZH, "通用转换适配器");
					tabName.put(YWLX_ESBSEND, "文件处理");
					tabName.put(YWLX_TYZHXML, "通用XML转换");
		}
		return tabName;
	}
		//各个业务类型的名称字段
	private static Map<String,String> ywName=new HashMap<String,String>();
	public static Map getYwName(){
		if(ywName==null||ywName.size()<=0){
			ywName=new HashMap<String,String>();
				ywName.put(YWLX_DY, "sysname");//数据定义
				ywName.put(YWLX_LC, "name");//数据流程
				ywName.put(YWLX_JS, "recname");//数据对照定义
				ywName.put(YWLX_TB, "name");//数据同步
				ywName.put(YWLX_ZH, "name");//数据转换
				ywName.put(YWLX_JG, "name");//数据加工
				ywName.put(YWLX_FS, "name");//数据发送
				ywName.put(YWLX_DZ, "name");//数据发送
				ywName.put(YWLX_TYZH, "name");//数据发送
				ywName.put(YWLX_ESBSEND, "name");//数据发送
				ywName.put(YWLX_SJQX, "name");
				ywName.put(YWLX_ZTYZ, "name");
				ywName.put(YWLX_TYZHXML, "name");
			
		}
		return ywName;
	}
//		各个业务类型的编码字段
	private static Map<String,String> ywbmName=new HashMap<String,String>();
	public static Map getYwbmName(){
		if(ywbmName==null||ywbmName.size()<=0){
			 ywbmName=new HashMap<String,String>();
						ywbmName.put(YWLX_DY, "syscode");//数据定义
						ywbmName.put(YWLX_LC, "code");//数据流程
						ywbmName.put(YWLX_JS, "syscode");//数据对照定义
						ywbmName.put(YWLX_TB, "code");//数据同步
						ywbmName.put(YWLX_ZH, "code");//数据转换
						ywbmName.put(YWLX_JG, "code");//数据加工
						ywbmName.put(YWLX_FS, "code");//数据发送
						ywbmName.put(YWLX_DZ, "code");//数据发送
						ywbmName.put(YWLX_TYZH, "code");//数据发送
						ywbmName.put(YWLX_ESBSEND, "code");//数据发送
						ywbmName.put(YWLX_SJQX, "code");
						ywbmName.put(YWLX_ZTYZ, "code");
						ywbmName.put(YWLX_TYZHXML, "code");
		}
		return ywbmName;
	}


	
	
	//基本档案与之对应的参照的map
	private static Map<String ,String> docRefMap=new HashMap<String, String>();
	public static Map getDocRefMap(){
		if(docRefMap==null||docRefMap.size()<=0){
			docRefMap=new HashMap<String, String>();
			docRefMap.put("dip_datacheckregist", "<nc.ui.bd.ref.DataCheckRefModel>");//数据校验注册器
			docRefMap.put("dip_statusregist", "<nc.ui.bd.ref.StatusRegistRefModel>");//数据状态注册
			docRefMap.put("dip_sourceregist", "<nc.ui.bd.ref.SourceRegistRefModel>");//数据源注册
			docRefMap.put("dip_recstatus", "<nc.ui.bd.ref.RecStatusRefModel>");//数据接收状态维护
			docRefMap.put("dip_recformatdef_h","<nc.ui.bd.ref.RecFormatRefModel>");//数据接收格式维护
			docRefMap.put("dip_datastyle", "<nc.ui.bd.ref.DataStyleRefModel>");//数据类型
			docRefMap.put("dip_dataorigin","<nc.ui.bd.ref.DataOriginRefModel>");//数据来源注册
			docRefMap.put("dataconsult","<nc.ui.bd.ref.DataConsultRefModel>");//数据项定义
			docRefMap.put("dip_recserver", "<nc.ui.bd.ref.RecserverPathRefModel>");//NC接收服务器注册
			docRefMap.put("dip_dataurl", "<nc.ui.bd.ref.DataUrlRefModel>");//数据同步URL注册
			docRefMap.put("dip_filepath", "<nc.ui.bd.ref.FilePathModel>");//文件访问路径注册
			docRefMap.put("dip_filepath", "<nc.ui.bd.ref.FilePathRefModel>");//文件访问路径注册
			docRefMap.put("dip_filepath", "<nc.ui.bd.ref.GSWJRefModel>");//文件访问路径注册
			docRefMap.put("dip_messagelogo", "<nc.ui.bd.ref.MessageLogoRefModel>");//消息标志
			docRefMap.put("dip_messtype","<nc.ui.bd.ref.MessTypeRefModel>");//消息类型
			docRefMap.put("dip_msr","<nc.ui.bd.ref.MessageRefModel>");//消息服务器注册
			docRefMap.put("dip_repeatdata","<nc.ui.bd.ref.RepedtDataRefModel>");//重复数据控制
			docRefMap.put("dip_obtainsign","<nc.ui.bd.ref.ObtainSignRefModel>");//获取标志
			docRefMap.put("dip_processstyle", "<nc.ui.bd.ref.ProcessStyleRefModel>");//加工类型档案
			docRefMap.put("dip_returnmess_h","<nc.ui.bd.ref.ReturnMsgRefModel>");//回执错误码表
			docRefMap.put("dip_taskregister","<nc.ui.bd.ref.TaskRegisterRefMode>");//任务类型注册
			docRefMap.put("dip_dbcontype","<nc.ui.bd.ref.ZJBRefModel>");//数据库连接中间表
			docRefMap.put("dip_dataurl","<nc.ui.bd.ref.ZDZQRefModel>");//数据同步URL注册
			docRefMap.put("dip_msr","<nc.ui.bd.ref.ZDJSRefModel>");//消息服务器注册
		}
		return docRefMap;
	}
	
	public static String getCodeRepeatHint(String name,String code){
		return name+"【"+code+"】"+"已经存在！";
	}
	public static String[] getSysSideBussinessCode(String pk_xt,String pk_datadefint_h){
		String str[]=new String[]{"","",""};
		try {
			DipSysregisterHVO syshvo=(DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, pk_xt);
			DipDatadefinitHVO datadefinthvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, pk_datadefint_h);
			if(syshvo!=null){
				str[0]=syshvo.getExtcode();//系统标志
				str[1]=syshvo.getDef_str_1();//固定标志
			}
			if(datadefinthvo!=null){
				if(datadefinthvo.getIsdeploy()!=null&&datadefinthvo.getIsdeploy().booleanValue()&&datadefinthvo.getDeploycode()!=null&&datadefinthvo.getDeploycode().trim().length()>0){
					String pk_deycode=datadefinthvo.getDeploycode();
					DipSysregisterBVO bvo=(DipSysregisterBVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterBVO.class,pk_deycode);
					str[1]=bvo.getSitecode();//站点标志 
				}
				str[2]=datadefinthvo.getBusicode();//业务标志
			}
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return str;	
	}
}
