package nc.ui.dip.authorize.browse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.SwingConstants;

import nc.bs.dip.txt.TxtDataVO;
import nc.bs.excel.pub.ExpExcelVO;
import nc.bs.excel.pub.ExpToExcel;
import nc.bs.excel.pub.ExpToExcel1;
import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bd.ref.DipDataRefModel;
import nc.ui.bd.ref.DipDayRefModel;
import nc.ui.bd.ref.DipMonthRefModel;
import nc.ui.bd.ref.DipYearRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.datalookquery.DatalookQueryDlg;
import nc.ui.dip.dlg.AskDLG;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.itemconverters.IntegerConverter;
import nc.ui.pub.bill.itemconverters.StringConverter;
import nc.ui.pub.bill.itemconverters.UFDoubleConverter;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.DataCheckUtil;
import nc.util.dip.sj.FunctionUtil;
import nc.util.dip.sj.RetMessage;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.actionset.ActionSetBVO;
import nc.vo.dip.authorize.browse.DipADDatalookVO;
import nc.vo.dip.authorize.define.DipADContdataVO;
import nc.vo.dip.datadefcheck.DipDatadefinitCVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.datalook.DipAuthDesignVO;
import nc.vo.dip.in.RowDataVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 *该类是AbstractMyEventHandler抽象类的实现类，
 *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 *@author author
 *@version tempProject version
 */

public class MyEventHandler 
extends AbstractMyEventHandler {
	DataLookClientUI ui = (DataLookClientUI)this.getBillUI();
	String pkTableField = "";//数据定义主键英文名称
	String sqlQuery = "";
	String whereCondition="";//查询时候的where条件。
	String defwhere=" 1=1 ";
	String defDeleteAuthWhere=" 1=1 ";//权限控制时候上级不能删除下级数据。这个条件是判断是否含有下级数据的sql语句
	String delWhereCondition="";//删除时候，如果权限控制时候，查询下级的数据的sql条件。 delWhereCondition 包含defDeleteAuthWhere，delSql包含delWhereCondition。
	String delSql="";//这个是删除校验时候的sql语句。
	List<DipAuthDesignVO> listVO;
	boolean isdisplaypk=false;
	IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
	boolean isStartAuth=true;//表示是否启用上级查看下级权限，做个开关，方便取消这个权限。
	String isauthFlag="0";//0表示没有启用权限设置
	  //1表示没有启用查看直接下级权限
	  //2表示没有启用查看所有下级权限
	Map <String,Map<String,String>> lowMap=new HashMap<String, Map<String,String>>();//在查看所有下级单位数据时候，增加缓存，这个map是存登录公司的所有下级单位，
	HashMap<String,String> functionMap=new HashMap<String,String>();
	HashMap<String,String> showFunctionMap=new HashMap<String,String>();
	HashMap<String,String> showAutoInMap=new HashMap<String,String>();
	HashMap<String,String> exportAutoInMap=new HashMap<String,String>();
	HashMap<String,String> exportFunctionMap=new HashMap<String,String>();
	HashMap<String,String> autoInMap=new HashMap<String,String>();
	HashMap<String,String> updateAutoInMap=new HashMap<String,String>();
	HashMap<String, DatalookQueryDlg> dlgMap = new HashMap<String, DatalookQueryDlg>();
	HashMap<String, DipDatadefinitCVO[]> checkMap = new HashMap<String, DipDatadefinitCVO[]>();
	DatalookQueryDlg dlg = null;
	HashMap<String, DipDatadefinitBVO> dataBMap =  new HashMap<String, DipDatadefinitBVO>();
	public HashMap<String, DatalookQueryDlg> getDlgMap() {
		return dlgMap;
	}
	public void setDlgMap(HashMap<String, DatalookQueryDlg> dlgMap) {
		this.dlgMap = dlgMap;
	}
	public MyEventHandler(BillTreeCardUI  billUI, ICardController control){
		super(billUI,control);		
	}
	/**
	 * 取得当前UI类
	 * 
	 * @return
	 */
	private DataLookClientUI getSelfUI() {
		return (DataLookClientUI) getBillUI();
	}

	/**
	 * 取得选中的节点对象
	 * 
	 * @return
	 */
	private VOTreeNode getSelectNode() {
		return getSelfUI().getBillTreeSelectNode();
	}
	/**
	 * 改方法在增行和插行后被调用，可以在该方法中为新增的行设置一些默认值
	 * 
	 * @param newBodyVO
	 * @return TODO
	 */
	protected CircularlyAccessibleValueObject processNewBodyVO(CircularlyAccessibleValueObject newBodyVO) {

		VOTreeNode parent = this.getSelectNode();

		newBodyVO.setAttributeValue("pk_datalook", parent == null ? null : parent.getNodeID());

		// 添加结束
		return newBodyVO;
	}

	/**
	 * TODO 当树节点被选中时，执行该方法
	 * 
	 * 描述:本单据是树单表体类型
	 */
	public void onTreeSelected(VOTreeNode node) {
		try {
			// 获取权VO
			DipADContdataVO vo = (DipADContdataVO) node.getData();
			if(vo.getContcolcode()!=null&&vo.getContcolcode().length()>0){
				String curuser=ClientEnvironment.getInstance().getUser().getPrimaryKey();
				String pkcorp=ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
				 isauthFlag="0";//0表示没有启用权限设置
									  //1表示没有启用查看直接下级权限
									  //2表示没有启用查看所有下级权限
				if(isStartAuth&&vo.getDef_str_1()!=null&&!vo.getDef_str_1().trim().equals("")){
					if(vo.getDef_str_1().equals("直接下级")){
						isauthFlag="1";
					}
					if(vo.getDef_str_1().equals("所有下级")){
						isauthFlag="2";
					}
				}
				if(pkcorp!=null&&!pkcorp.equals("0001")&&curuser!=null&&pkcorp.length()>0&&curuser.length()>0){
				String ss="";//查询条件用的。
				String ssdel="";//删除条件用的。
					defwhere="exists "
						+" (select 1 "
						+"  from "+vo.getMiddletab()+" ss "
						+" where ss.contpk = "+vo.getContabname()+"."+vo.getContcolname()
						+" and ss.extepk in "
						+"  (select pk_role_corp_alloc "
						+"   from v_dip_corproleauth "
						+" where pk_role in "
						+"   (select pk_role "
						+"    from sm_user_role cc "
						+"   where cc.cuserid = '"+curuser+"') ";
//						+"   and pk_corp = '"+pkcorp+"'))" ;
					
					ss="and pk_corp = '"+pkcorp+"'))";
					if(isauthFlag.equals("1")){
						ss="and ( pk_corp = '"+pkcorp+"' or pk_corp in( select pk_corp from bd_corp where bd_corp.fathercorp in('"+pkcorp+"')  ) "+
						   ")))";
						ssdel="and ( pk_corp in( select pk_corp from bd_corp where bd_corp.fathercorp in('"+pkcorp+"')  ) "+
						   ")))";
					}else if(isauthFlag.equals("2")){
						Map<String,String> corpMap=new HashMap<String, String>();
						if(lowMap!=null&&lowMap.get(pkcorp)!=null){
							corpMap=lowMap.get(pkcorp);
						}else{
							corpMap=queryfield.getAllChild(pkcorp, corpMap);
							lowMap.put(pkcorp, corpMap);
						}
						if(corpMap==null){
							corpMap=new HashMap<String, String>();
						}
						corpMap.put(pkcorp, pkcorp);
						int size=corpMap.size();
//						int count=(size%999==0?size/999:size/999+1);
//						String coinds[]=new String[count];
						if(size>0){
							String orsql="";
							List list=new ArrayList();
							Iterator it=corpMap.keySet().iterator();
							StringBuffer sb=new StringBuffer();
							int count=0;
							while(it.hasNext()){
								count++;
								sb.append("'");
								sb.append(it.next().toString());
								sb.append("',");
								if(count%900==0||count==size){
									list.add(sb.toString());
									sb=new StringBuffer();
								}
							}
							if(list.size()>0){
								for(int i=0;i<list.size();i++){
									String pkcorps=list.get(i)==null?"":list.get(i).toString();
									if(pkcorps!=null&&!pkcorps.trim().equals("")){
									 orsql=orsql+" or pk_corp in( select pk_corp from bd_corp where bd_corp.fathercorp in("+pkcorps.substring(0, pkcorps.length()-1)+")  )   ";
									}
								}
								ss=" and ( pk_corp = '"+pkcorp+"'" +orsql
								   +")))";
								ssdel=" and ( " +orsql.replaceFirst("or", "")
								   +")))";
							}
						}
					}
					if(isStartAuth&&(isauthFlag.equals("2")||isauthFlag.equals("1"))){
						defDeleteAuthWhere=defwhere+ssdel;
					}
					defwhere=defwhere+ss;
					DipADContdataVO[] vos = (DipADContdataVO[])HYPubBO_Client.queryByCondition(DipADContdataVO.class, 
							" pk_master='"+vo.getPk_contdata_h()+"' and coalesce(dr,0)=0 ");
					if(null != vos && vos.length>0){
						for (DipADContdataVO dipADContdataVO : vos) {
							defwhere+=" and exists "
								+" (select 1 "
								+"  from "+dipADContdataVO.getMiddletab()+" ss "
								+" where ss.contpk = "+dipADContdataVO.getContabname()+"."+dipADContdataVO.getContcolname()
								+" and ss.extepk in "
								+"  (select pk_role_corp_alloc "
								+"   from v_dip_corproleauth "
								+" where pk_role in "
								+"   (select pk_role "
								+"    from sm_user_role cc "
								+"   where cc.cuserid = '"+curuser+"') "
								+ss;
						}
					}
				}
			}else {
				defwhere=" 1=1 ";
			}
			String pk=vo.getContabcode();
			if(pk==null||pk.length()<=0){
				return;
			}

			String sql = "";
			//查询对照系统字段
			sql = "select * from dip_auth_design d where d.designtype=0 and d.pk_datadefinit_h='"+vo.getPrimaryKey()+"'  order by d.disno";
		
			DipDatadefinitBVO[] definitPKvo=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, " pk_datadefinit_h='"+vo.getContabcode()+"' and nvl(dr,0)=0 ");
			HashMap<String, Integer> bMap = new HashMap<String, Integer>();
			for (DipDatadefinitBVO bvo : definitPKvo) {
				bMap.put(bvo.getEname(), bvo.getLength());
			}
			dataBMap = new HashMap<String, DipDatadefinitBVO>();
			for (DipDatadefinitBVO bvo : definitPKvo) {
				dataBMap.put(bvo.getEname(),bvo);
			}
			IUAPQueryBS querybs = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			List<DipAuthDesignVO> listVO = (ArrayList<DipAuthDesignVO>)querybs.executeQuery(sql, new BeanListProcessor(DipAuthDesignVO.class));
			this.listVO=listVO;
			//修改界面上的表体字段
			//查询被对照系统字段
//			StringBuffer strFileds = new StringBuffer();
//			field="";
//			efield="";
			checkMap = DataCheckUtil.getDataCheckMap(vo.getContabcode());
			ui.contFields=new StringBuffer();
			ui.vdef=new StringBuffer();
			for(int i=1;i<=100;i++){
				String key="vdef"+String.valueOf(i);
				BillItem bodyItem = ui.getBillCardPanel().getBodyItem(key);
				bodyItem.setEdit(true);
				bodyItem.setShow(false);
				bodyItem.setWidth(100);
				bodyItem.setLength(100);
				bodyItem.setDataType(BillItem.STRING);
				bodyItem.setConverter(new StringConverter());
				bodyItem.setDecimalDigits(2);
				bodyItem.setForeground(-1);
				((UIRefPane)bodyItem.getComponent()).setTextType("TextStr");
			}
			for(int i=101;i<=120;i++){
				String key="vdef"+String.valueOf(i);
				BillItem bodyItem = ui.getBillCardPanel().getBodyItem(key);
				if(null != bodyItem){
					bodyItem.setEdit(true);
					bodyItem.setShow(false);
					bodyItem.setWidth(100);
					bodyItem.setLength(100);
					bodyItem.setForeground(-1);
					((UIRefPane)bodyItem.getComponent()).setRefNodeName("人员基本档案");
				}
			}
			int j = 101;
			for(int i=1;i<=100;i++){
				if(listVO!=null&&listVO.size()>0&&listVO.size()>=i){
					DipAuthDesignVO bvo=listVO.get(i-1);
					String cname = bvo.getCname();
					UFBoolean ispk = new UFBoolean(bvo.getIspk()==null?false:bvo.getIspk().booleanValue());
					//2011-6-23 cl 修改了如下内容176--178
					if(ispk.booleanValue() ){
						ui.pkHiddleItemKey = "vdef"+String.valueOf(i);
						pkTableField =bvo.getEname();
					}


					ui.contFields.append(""+bvo.getEname()+",");
					
					//卡片界面，更新表头字段名称
					String key="vdef"+String.valueOf(i);
					boolean isRef = false;
					if(FunctionUtil.SYSREFPK.equals(bvo.getConsvalue())){
						key="vdef"+String.valueOf(j);
						isRef = true;
						j++;
					}
					ui.vdef.append(key+",");
					BillItem bodyItem = ui.getBillCardPanel().getBodyItem(key);
					bodyItem.setEdit(true);
					bodyItem.setName(cname);
					bodyItem.setShow(true);
					Integer disno = bvo.getDisno()==null?1:bvo.getDisno();
					bodyItem.setShowOrder(disno);
					bodyItem.setReadOrder(bvo.getDisno());
					if(null != bvo.getIslock() && bvo.getIslock().booleanValue()){
						bodyItem.setEdit(false);
					}
					if(null != checkMap.get(bvo.getEname())){
						bodyItem.setForeground(12);
					}
					if(bvo.getEname().equals("P_STATUS")){
						ui.statusHiddleItemKey = key;
					}
					if(bvo.getDesignlen()==0){
						bodyItem.setShow(false);
					}
					bodyItem.setWidth(bvo.getDesignlen());
					if(bMap.get(bvo.getEname())==null){
						MessageDialog.showErrorDlg(getBillUI(), "错误", "模板相关字段和数据定义字段不匹配，请重置模板！");
						return;
					}
					bodyItem.setLength(bMap.get(bvo.getEname()));
					if(isRef){
						DipDatadefinitBVO dipDatadefinitBVO = dataBMap.get(bvo.getEname());
						if(null != dipDatadefinitBVO){
							String type = dipDatadefinitBVO.getType();
							if("DATE".equals(type)){
								((UIRefPane)bodyItem.getComponent()).setIsCustomDefined(false);
								((UIRefPane)bodyItem.getComponent()).setRefNodeName("日历");
							}else{
								((UIRefPane)bodyItem.getComponent()).setRefNodeName("人员基本档案");
								String quotecolu = dipDatadefinitBVO.getQuotecolu();
								String memorytable = queryfield.queryfield("SELECT memorytable FROM v_dip_jgyyzdtree WHERE pk_datadefinit_b='"
										+dipDatadefinitBVO.getQuotetable()
										+"'");
								if("ZMDM_OCALYEAR".equals(memorytable)){
									DipYearRefModel yearRefModel = new DipYearRefModel();
									((UIRefPane)bodyItem.getComponent()).setRefModel(yearRefModel);
								}else if("ZMDM_OCALMONTH2".equals(memorytable)){
									DipMonthRefModel monthRefModel = new DipMonthRefModel();
									((UIRefPane)bodyItem.getComponent()).setRefModel(monthRefModel);
								}else if("ZMDM_OCALDAY".equals(memorytable)){
									DipDayRefModel monthRefModel = new DipDayRefModel();
									((UIRefPane)bodyItem.getComponent()).setRefModel(monthRefModel);
								}else{
									DipDataRefModel model = new DipDataRefModel();
									model.setTableName(memorytable);
									model.setPkFieldCode(quotecolu);
									((UIRefPane)bodyItem.getComponent()).setRefModel(model);
								}
//								((UIRefPane)ui.getBillCardPanel().getBodyItem(key).getComponent()).setAutoCheck(false);
//								((UIRefPane)ui.getBillCardPanel().getBodyItem(key).getComponent()).setIsCustomDefined(true);
							}
						}
					}else{
						if(bvo.getDatatype()!=null&&(",BINARY_DOUBLE,BINARY_FLOAT,INTEGER,INTERVAL,LONG,LONGRAW,NUMBER,".indexOf(bvo.getDatatype().toUpperCase())>=0)){
							if(null != bvo.getDesigplen() && bvo.getDesigplen()>0){
								bodyItem.setDataType(BillItem.DECIMAL);
								bodyItem.setDecimalDigits(bvo.getDesigplen());
								bodyItem.setConverter(new UFDoubleConverter(bvo.getDesigplen()));
								bodyItem.setTatol(true);
								((UIRefPane)bodyItem.getComponent()).setTextType("TextDbl");
								((UIRefPane)bodyItem.getComponent()).setNumPoint(bvo.getDesigplen());
							}else{
								bodyItem.setDataType(BillItem.INTEGER);
								bodyItem.setConverter(new IntegerConverter());
								bodyItem.setTatol(true);
								bodyItem.setDecimalDigits(0);
								((UIRefPane)bodyItem.getComponent()).setTextType("TextInt");
								((UIRefPane)bodyItem.getComponent()).setNumPoint(0);
							}
						}else{
							bodyItem.setDataType(BillItem.STRING);
							bodyItem.setConverter(new StringConverter());
							((UIRefPane)bodyItem.getComponent()).setTextType("TextStr");
							((UIRefPane)bodyItem.getComponent()).setNumPoint(0);
						}
					}
//					if(bvo.get){
//						
//					}
//					strFileds.append("vdef"+String.valueOf(i)+",");
//					if(i==listVO.size()){
//						if(definitPKvo!=null&&definitPKvo.length>0){
//							String pkename=definitPKvo[0].getEname();
//							pkTableField =pkename;
//							pkHiddleItemKey = "vdef"+String.valueOf(listVO.size()+1);
//							contFields.append(""+pkTableField+",");
//							vdef.append(pkHiddleItemKey+",");
//						}else{
//							sql=" select ename from dip_auth_design dd where dd.designtype='2' and dd.consvalue='PK' and dd.pk_datadefinit_h='"+vo.getContabcode()+"' and  exists (select * from " 
//							+" dip_datadefinit_b bb where bb.pk_datadefinit_h=dd.pk_datadefinit_h and dd.ename=bb.ename and nvl(bb.dr,0)=0 )";
//							
//							String obj=queryfield.queryfield(sql);
//							if(obj!=null&&!obj.equals("")){
//								pkTableField=obj;
//								pkHiddleItemKey = "vdef"+String.valueOf(listVO.size()+1);
//								contFields.append(""+pkTableField+",");
//								vdef.append(pkHiddleItemKey+",");
//							}
//						}
//					}
				}else{
					ui.getBillCardPanel().getBodyItem("vdef"+String.valueOf(i)).setDataType(BillItem.STRING);
					ui.getBillCardPanel().getBodyItem("vdef"+String.valueOf(i)).setConverter(new StringConverter());
					ui.getBillCardPanel().getBodyItem("vdef"+String.valueOf(i)).setShow(false);
				}
			}
//			if(!"".equals(strFileds.toString())){
//				String strField [] = strFileds.toString().split(",");
//				if(strField.length>0){
//					for(int i = strField.length;i<100-strField.length;i++){
//						ui.getBillCardPanel().getBodyItem("vdef"+String.valueOf(i+1)).setDataType(BillItem.STRING);
//						ui.getBillCardPanel().getBodyItem("vdef"+String.valueOf(i+1)).setConverter(new StringConverter());
//						ui.getBillCardPanel().getBodyItem("vdef"+String.valueOf(i+1)).setShow(false);
//					}
//
//				}
//			}
			BillItem[] bodyItems = ui.getBillCardPanel().getBodyItems();
			ArrayList<BillItem> list = new ArrayList<BillItem>();
			for (BillItem billItem : bodyItems) {
				list.add(billItem);
			}
			Collections.sort(list, new Comparator<BillItem>() {
				public int compare(BillItem o1, BillItem o2) {
					return o1.getShowOrder()-o2.getShowOrder();
				}
			});
			ui.getBillCardPanel().getBillModel().setBodyItems(list.toArray(new BillItem[list.size()]));
			ui.getBillCardPanel().setBillData(ui.getBillCardPanel().getBillData());
			autoInMap.clear();
			updateAutoInMap.clear();
			functionMap.clear();
			showFunctionMap.clear();
			exportFunctionMap.clear();
			showAutoInMap.clear();
			exportAutoInMap.clear();
			DipAuthDesignVO[] vos = (DipAuthDesignVO[])HYPubBO_Client.queryByCondition(DipAuthDesignVO.class, "nvl(dr,0)=0 and pk_datadefinit_h = '"
					+vo.getPrimaryKey()
					+"' and consvalue is not null ");
			if(null != vos && vos.length>0){
				for (DipAuthDesignVO DipAuthDesignVO : vos) {
					String consvalue = DipAuthDesignVO.getConsvalue();
					if(DipAuthDesignVO.getDesigntype()==2){//导入模板设置
						if(FunctionUtil.isAutoIn(consvalue)){
							autoInMap.put(DipAuthDesignVO.getEname(), consvalue);
						}else if(FunctionUtil.isUpdateAutoIn(consvalue)){
							updateAutoInMap.put(DipAuthDesignVO.getEname(), consvalue);
						}else{
							if(null != consvalue && consvalue.startsWith(FunctionUtil.SAVEREPLACEPK)){
								int indexOf = consvalue.indexOf("(");
								String substring = consvalue.substring(indexOf+1, consvalue.length()-1);
								String[] split = substring.split(",");
								HashMap<String, String> map = new HashMap<String, String>();
								map.put(DipAuthDesignVO.getEname(), consvalue);
								ui.refEditMap.put(split[0].toUpperCase(), map);
							}
							functionMap.put(DipAuthDesignVO.getEname(), consvalue);
						}
					}else if(DipAuthDesignVO.getDesigntype()==0){//显示模板设置
						if(FunctionUtil.isAutoIn(consvalue)){
							showAutoInMap.put(DipAuthDesignVO.getEname(), consvalue);
						}else{
							showFunctionMap.put(DipAuthDesignVO.getEname(), consvalue);
						}
					}else if(DipAuthDesignVO.getDesigntype()==3){//导出模板设置
						if(FunctionUtil.isAutoIn(consvalue)){
							exportAutoInMap.put(DipAuthDesignVO.getEname(), consvalue);
						}else{
							exportFunctionMap.put(DipAuthDesignVO.getEname(), consvalue);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 得到所有的下级公司有子公司的公司主键，例如1001 有子公司（1002,1003,1004,1005） ，1002有子公司 （1010,1011），其他公司都没有子公司，就返回（1002）。
	 * 例如1001 有子公司（1002,1003） ，1002有子公司 （1010,1011），1010有子公司（1101,1102）其他公司都没有子公司，就返回（1002，1010）。
	 * @return
	 */
//	public Map<String,String> getAllChild(String pk_corp,Map<String,String> corpMap){
//		if(pk_corp==null||pk_corp.trim().equals("")){
//			return null;
//		}
//		String sql=" select pk_corp from bd_corp where fathercorp='"+pk_corp+"'";
//		try {
//			List list=queryfield.queryfieldList(sql);
//			if(list!=null&&list.size()>0){
//				for(int i=0;i<list.size();i++){
//					String pk_corpi=list.get(i)==null?"":list.get(i).toString();
//					if(pk_corpi!=null&&!pk_corpi.equals("")){
//						if(isHasChild(pk_corpi)){
//							corpMap.put(pk_corpi, pk_corpi);
//							getAllChild(pk_corpi, corpMap);
//						}else{
//							continue;
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return corpMap;
//	}
	
//	public  boolean isHasChild(String pk_corp ){
//		boolean flag=false;
//		String sql=" select pk_corp from bd_corp where fathercorp='"+pk_corp+"'";
//			try {
//				List list=queryfield.queryfieldList(sql);
//				if(list!=null&&list.size()>0){
//					flag=true;
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				flag=false;
//			}
//			
//		return flag;
//	}
	
	
	//数据清理
	@Override

	protected void onDataClear(int intBtn) throws Exception {
		if(isAdd||isEdit){
			getSelfUI().showErrorMessage("正在编辑中，请保存！");
			return;
		}
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			getSelfUI().showWarningMessage("请选择节点！");
			return;
		}

		//String fnode=((DataLookClientUI)getBillUI()).selectnode;
		String pk=(String) node.getParentnodeID();
		String nn=(String) node.getNodeID();
		
		if(pk ==null || pk.trim().equals("")){
			getSelfUI().showWarningMessage("父节点不可编辑！");
			return;
		}
		int flag = MessageDialog.showOkCancelDlg(ui, "清理", "确定是否清理！");
		if(flag==2){
			return;
		}
		
		DipADContdataVO vo1=(DipADContdataVO) node.getData();
		//得到树上需要导出的表的主键
		String pkdetadefinith = vo1.getContabcode();
		DipDatadefinitHVO datafinitVO = (DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, pkdetadefinith);
		int row=getBillCardPanelWrapper().getBillCardPanel().getRowCount();
		if(row>0){
			for(int i=0;i<row;i++){
				getBufferData().setCurrentRow(0);
				getBillCardPanelWrapper().getBillCardPanel().delLine();
			}
		}

//		DipADContdataVO vo=(DipADContdataVO) node.getData();
//		if(vo!=null&&vo.getContabname()!=null&&vo.getContabcode()!=null){
//			
//		}
//		DipDatadefinitHVO dfvo= (DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, nn);
		
		if(datafinitVO!=null&&datafinitVO.getMemorytable()!=null){
			String tname=datafinitVO.getMemorytable();
			String sql="delete from "+tname+" where "+defwhere;
			queryfield.exesql(sql);	
			MessageDialog.showHintDlg(getSelfUI(), "成功", "清理成功");
		}else{
			getSelfUI().showWarningMessage("选择节点对应的数据定义找不到");
		}
		
	}
	boolean ishandaddpkdisplay=false;
	@Override
	protected void onDatalookQuery(int intBtn) throws Exception {
		if(isAdd||isEdit){
			getSelfUI().showErrorMessage("正在编辑中，请保存！");
			return;
		}
		if(ui.contFields.toString()==null||ui.contFields.toString().length()<=0){
			getSelfUI().showErrorMessage("没有定义显示数据！");
			return;
		}
//		if(!isdisplaypk){
//			contFields.append(""+efield+",");
//			vdef.append(field+",");
//			isdisplaypk=true;
//			ishandaddpkdisplay=true;
//		}
		
		VOTreeNode treeNode=getSelectNode();
		if(treeNode==null){
			getSelfUI().showErrorMessage("请选择要查询的节点！");
			return ;
		}
		String str=(String)treeNode.getParentnodeID();
		DipADContdataVO vo1=(DipADContdataVO) treeNode.getData();
		if(vo1.getContcolcode()!=null&&vo1.getContcolcode().length()>0){
			String pkuser=ClientEnvironment.getInstance().getUser().getPrimaryKey();
			if(pkuser!=null&&pkuser.length()>0){
				
			}
		}
//		super.onBoQuery();
		super.onDatalookQuery(intBtn);
		HashMap map = new HashMap();	

		map.put("pk_datadefinit_h", vo1.getContabcode());
		if(null != getDlgMap().get(vo1.getPrimaryKey())){
			dlg = getDlgMap().get(vo1.getPrimaryKey());
		}else{
			dlg = new DatalookQueryDlg(this.getBillUI(),new UFBoolean(true),vo1.getPrimaryKey(),dataBMap);
			getDlgMap().put(vo1.getPrimaryKey(), dlg);
		}
		dlg.show();
		if(dlg.getOnBtnSave()==1){
			if(dlg.getReturnSql()!=null&&dlg.getReturnSql().trim().length()>0){//返回查询弹出框查询条件语句
				whereCondition="  and coalesce(dr,0)=0 and "+defwhere+dlg.getReturnSql();
				delWhereCondition=" and "+defDeleteAuthWhere+dlg.getReturnSql();
			}
			String sql = "";

			DipDatadefinitHVO datadefinitVO = (DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, vo1.getContabcode());

			
			String csql="select count(*) c from "+datadefinitVO.getMemorytable()+" where 1=1 "+whereCondition;
			String ssql="";
			try{
				ssql=queryfield.queryfield(csql);
			}catch(SQLException ex){
//				System.out.println("["+ex.getSQLState()+"]");
//				System.out.println("["+ex.getErrorCode()+"]");
//				System.out.println("["+ex.getMessage()+"]");
				if(ex.getSQLState().equals("42000")){
					if(ex.getErrorCode()==207){
						String msg=ex.getMessage().substring(12);
						throw new Exception(msg.substring(0,msg.indexOf("\""))+" 字段在表中不存在");
					}else if(ex.getErrorCode()==936){
						throw new Exception("查询条件有误，请检查！");
					}else if(ex.getErrorCode()==208){
						throw new Exception("表或视图不存在");
					}else{
						throw ex;
					}
				}else {
					throw ex;
				}
			}
			int count=Integer.parseInt(ssql);
			//如果数据量过大，则给予提示，让其选择查询条件 2011-7-5 cl
			if(count >ui.getCount()){
				MessageDialog.showHintDlg(getSelfUI(), "提示", "当前查询的数据量过多，会导致系统效率变慢，请设置查询条件后进行查询！");
				return;
			}
			//查询对照表中的值
			queryDisplayValueMethod(datadefinitVO.getMemorytable(),whereCondition,datadefinitVO.getPk_datadefinit_h());
		}
	}
	/**
	 * 根据表名和where条件查询数据，抽象出此方法是为了让增加保存数据后，根据增加主键记录来查询展示新增加记录
	 */
	private void queryDisplayValueMethod(String tablename,String whereCondition,String pk_datadefinit_h) throws BusinessException, SQLException, DbException {
		String sql = "select ";
		if(!"".equals(ui.contFields.toString())){
			
			sql = sql+""+ui.contFields.toString().substring(0, ui.contFields.toString().length()-1)+" from "+tablename+" where 1=1 "+whereCondition;
			if(isStartAuth&&pkTableField!=null&&!pkTableField.trim().equals("")){
				delSql="select "+pkTableField+" from "+tablename + " where 1=1  "+delWhereCondition;
			}
		}

		sqlQuery = sql;
		List listValue = queryfield.queryListMapSingleSql(sql);
		String contField [] = ui.contFields.toString().split(",");
		String vdefs [] = ui.vdef.toString().split(",");
		ui.getBillCardPanel().getBillModel().clearBodyData();
		int k = 0;
		for(int i=0;i<vdefs.length;i++){
			String key=vdefs[i];
			BillItem it=ui.getBillCardPanel().getBodyItem(key);
			if(listVO!=null&&listVO.size()>0&&listVO.size()>=i){
				DipAuthDesignVO lvo=listVO.get(i);
				String ss=lvo.getContype();
				if(it!=null&&it.isShow()){
					if(ss!=null){
						int l;
						if(ss.equals("左")){
							l=SwingConstants.LEFT;
						}else if(ss.equals("右")){
							l=SwingConstants.RIGHT;
						}else {
							l=SwingConstants.CENTER;
						}
						ui.getBillCardPanel().getBillTable().getColumnModel().getColumn(k).setCellRenderer(new DefBillTableCellRender(it,new BillRendererVO(),l));
						k++;
					}
				}
			}else{
//					ui.getBillCardPanel().getBodyItem(key).setDataType(BillItem.STRING);
//					ui.getBillCardPanel().getBodyItem(key).getDataType();
//					BillData billData = ui.getBillCardPanel().getBillData();
//					billData.getBillModel().getItemByKey(key).setDataType(BillItem.STRING);
//					ui.getBillCardPanel().setBillData(billData);
//					break;
//					ui.getBillCardPanel().setBillData(arg0)
			}
		}
		ui.getBillCardPanel().setTatolRowShow(true);
		ui.getBillCardPanel().getBillTable().setColumnSelectionAllowed(false);
		if(listValue!=null && listValue.size()>0){
			HashMap<String, HashMap<String, String>> showReplaceMap = new HashMap<String, HashMap<String, String>>();
			for(int i = 0;i<listValue.size();i++){
				HashMap mapValue = (HashMap)listValue.get(i);
				ui.getBillCardPanel().getBillModel().addLine();
				DipADDatalookVO vo=new DipADDatalookVO();
				int c = 0;
				for(int j = 0;j<contField.length;j++){
					String upperCase = contField[j].toUpperCase();
					Object value=mapValue.get(upperCase);
					String string = showFunctionMap.get(upperCase);
					if(null != string && string.startsWith(FunctionUtil.SHOWREPLACEPK)){
						int indexOf = string.indexOf("(");
						String substring = string.substring(indexOf+1, string.length()-1);
						String[] split = substring.split(",");
						if(null != split && split.length==2){
							Object object = mapValue.get(split[0].toUpperCase());
							if(null != object){
								if(null != showReplaceMap.get(contField[j])){
									HashMap<String, String> hashMap = showReplaceMap.get(contField[j]);
									String newStr = hashMap.get(object.toString());
									if(null != newStr){
										value = newStr;
									}else{
										value = putShowReplaceMap(
												pk_datadefinit_h, contField[j],
												showReplaceMap, value,
												split, object, hashMap);
									}
								}else{
									HashMap<String, String> hashMap = new HashMap<String, String>();
									value = putShowReplaceMap(pk_datadefinit_h,
											contField[j], showReplaceMap,
											value, split, object, hashMap);
								}
							}
						}
					}
					string = showAutoInMap.get(upperCase);
					if(null != string){
						value = FunctionUtil.getFuctionValue(string);
					}
					vo.setAttributeValue(vdefs[j], value);
					ui.getBillCardPanel().getBillModel().setValueAt(value, i,vdefs[j]);
					if(j==contField.length-1&&pkTableField.length()>0){
//								ui.getBillCardPanel().getBillTable().getCellRenderer(i, j).getTableCellRendererComponent(ui.getBillCardPanel().getBillTable(), value, false, false, i, j);
					}else{
						BillItem bodyItem = ui.getBillCardPanel().getBodyItem(vdefs[j]);
						if(bodyItem!=null&&bodyItem.isShow()){
							ui.getBillCardPanel().getBillTable().getCellRenderer(i, c).getTableCellRendererComponent(ui.getBillCardPanel().getBillTable(), value, false, false, i, c);
							c++;
						}
					}
				}
			}
		}
	}
	boolean isAdd=false;
	boolean isEdit=false;
	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		VOTreeNode treeNode=getSelectNode();
		if(treeNode ==null){
			getSelfUI().showErrorMessage("请选择要保存的节点!");
			isreturn=true;
			return;
		}
		String pk=(String) treeNode.getParentnodeID();
		if(pk==null || "".equals(pk)){
			getSelfUI().showErrorMessage("系统节点不能保存!");
			isreturn=true;
			return;
			}
		
		DipADContdataVO vo1=(DipADContdataVO) treeNode.getData();
		//得到树上需要导出的表的主键
		String pkdetadefinith = vo1.getContabcode();
		DipDatadefinitHVO datadefinitVO = (DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, pkdetadefinith);
		int headcount = ui.getBillCardPanel().getBillModel().getRowCount();
		List list=new ArrayList();
		//清空表体空行--start
		for(int i=0;i<headcount;i++){
			String vdefs [] = ui.vdef.toString().split(",");
			String vvv[]=new String[vdefs.length];
			for(int j=0;j<vdefs.length;j++){
				vvv[j]=ui.getBillCardPanel().getBodyValueAt(i, vdefs[j])==null?"":ui.getBillCardPanel().getBodyValueAt(i, vdefs[j]).toString();
			}
			boolean ff=false;
			for(int k=0;k<vvv.length;k++){
				if(!vvv[k].equals("")){
					ff=true;
					break;
				}
				if(k==vvv.length-1){
					if(ff==false){
						list.add(i);
					}
				}
			}
		}
		
		if(list.size()!=0){
			int []delrow=new int [list.size()];
			for(int i=0;i<list.size();i++){
				delrow[i]=(Integer)list.get(i);
			}
			
			ui.getBillCardPanel().getBillModel().delLine(delrow);
		}
//		清空表体空行--end
		headcount = ui.getBillCardPanel().getBillModel().getRowCount();
		
		Boolean isInsert = false;
		for (int i = 0; i < headcount; i++) {
			String pkvalue = ui.getBillCardPanel().getBodyValueAt(i, ui.pkHiddleItemKey)==null?"":ui.getBillCardPanel().getBodyValueAt(i, ui.pkHiddleItemKey).toString();
			if("".equals(pkvalue)){
				isInsert = true;
				break;
			}
		}
		String[] oiDs = null;
		if(isInsert){
			oiDs = queryfield.getOIDs(ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), headcount);
		}
		
		
		String sql = " ";
		String pkvalue = "";
		StringBuffer addpkArray=new StringBuffer("");//增加保存时，预制记录主键
		
		//修改保存功能
		String contField [] = ui.contFields.toString().split(",");
		HashMap<String, Integer> contMap = new HashMap<String, Integer>();
		for (int i = 0; i < contField.length; i++) {
			contMap.put(contField[i], i);
		}
		HashMap<String, HashMap<String, String>> checkValueMap = new HashMap<String, HashMap<String, String>>();
		HashMap<String, HashMap<String, String>> saveReplaceMap = new HashMap<String, HashMap<String, String>>();
		ArrayList<String> sqlList = new ArrayList<String>();
		String version = "";
		for(int i = 0;i<headcount;i++){
			StringBuffer insertField = new StringBuffer();
			StringBuffer insertFieldvalue = new StringBuffer(); 
			String strFiled = "";
			String p_status = "";
			sql = " ";
			if(!"".equals(ui.vdef.toString())){
				String vdefs [] = ui.vdef.toString().split(",");
				pkvalue = ui.getBillCardPanel().getBodyValueAt(i, ui.pkHiddleItemKey)==null?"":ui.getBillCardPanel().getBodyValueAt(i, ui.pkHiddleItemKey).toString();
				p_status = ui.getBillCardPanel().getBodyValueAt(i, ui.statusHiddleItemKey)==null?"":ui.getBillCardPanel().getBodyValueAt(i, ui.statusHiddleItemKey).toString();
				int rowState = ui.getBillCardPanel().getBillModel().getRowState(i);
				if(!"".equals(pkvalue) && rowState==BillModel.NORMAL){//未修改数据
					continue;
				}
				for(int j = 0;j<vdefs.length;j++){
					String value = ui.getBillCardPanel().getBodyValueAt(i, vdefs[j])==null?"":ui.getBillCardPanel().getBodyValueAt(i, vdefs[j]).toString();

					if("".equals(pkvalue) && ui.pkHiddleItemKey.equals(vdefs[j])){ //主键
//						ui.showErrorMessage("第"+(i+1)+"行主键为空！");
//						return;
//						value = queryfield.getOID();
						value = oiDs[i];
						addpkArray.append("'"+value+"',");
					}
					//datacheck
					if(null !=checkMap.get(contField[j])){
						if(null != checkValueMap.get(contField[j])){
							HashMap<String, String> hashMap = checkValueMap.get(contField[j]);
							String string = hashMap.get(value);
							if(null != string && "Y".equals(string)){
								
							}else{
								String name = ui.getBillCardPanel().getBodyItem(vdefs[j]).getName();
								String message = "第"+(i+1)+"行"+name+"";
								String checkDataMsg = DataCheckUtil.checkData(contField[j], value,vo1.getMiddletab());
								if(null != checkDataMsg){
									getSelfUI().showErrorMessage(message+"  "+checkDataMsg);
									return;
								}
								hashMap.put(value, "Y");
								checkValueMap.put(contField[j], hashMap);
							}
						}else{
							String name = ui.getBillCardPanel().getBodyItem(vdefs[j]).getName();
							String message = "第"+(i+1)+"行"+name+"";
							String checkDataMsg = DataCheckUtil.checkData(contField[j], value,vo1.getMiddletab());
							if(null != checkDataMsg){
								getSelfUI().showErrorMessage(message+"  "+checkDataMsg);
								return;
							}
							HashMap<String, String> hashMap = new HashMap<String, String>();
							hashMap.put(value, "Y");
							checkValueMap.put(contField[j], hashMap);
						}
					}
					
					insertField.append(contField[j]+",");
					
					if("".equals(pkvalue)){//插入时才替换
						if(null != autoInMap.get(contField[j]) && !ui.pkHiddleItemKey.equals(vdefs[j])){
							String string = autoInMap.get(contField[j]);
							if(FunctionUtil.SYSVERSIONPK.equals(string)){
								if("".equals(version)){
									value=FunctionUtil.getFuctionValue(string);
									version= value;
								}else{
									value = version;
								}
							}else{
								value=FunctionUtil.getFuctionValue(string);
							}
						}
					}
					String function = functionMap.get(contField[j]);
					if(null != function && !function.startsWith(FunctionUtil.SAVEREPLACEPK) && !function.startsWith(FunctionUtil.SHOWREPLACEPK)){
						RetMessage ret = FunctionUtil.getFuctionValue(value, functionMap.get(contField[j]));
						if(ret.getIssucc()){
							value=ret.getValue().toString();
						}else{
							getSelfUI().showErrorMessage(ret.getMessage());
							return;
						}
						ui.getBillCardPanel().setBodyValueAt(value, i, vdefs[j]);
					}else if(null != function && function.startsWith(FunctionUtil.SAVEREPLACEPK)){
						int indexOf = function.indexOf("(");
						String substring = function.substring(indexOf+1, function.length()-1);
						String[] split = substring.split(",");
						if(null != split && split.length==2){
							if(null != contMap.get(split[0].toUpperCase())){
								Integer xh = contMap.get(split[0].toUpperCase());
								String fieldvalue = ui.getBillCardPanel().getBodyValueAt(i, vdefs[xh])==null?"":ui.getBillCardPanel().getBodyValueAt(i, vdefs[xh]).toString();
								if("".equals(fieldvalue) && null != autoInMap.get(split[0].toUpperCase())){
									fieldvalue = FunctionUtil.getFuctionValue(autoInMap.get(split[0].toUpperCase()));
								}
								if(null != saveReplaceMap.get(contField[j])){
									HashMap<String, String> hashMap = saveReplaceMap.get(contField[j]);
									String string = hashMap.get(fieldvalue);
									if(null != string){
										value = string;
									}else{
										RetMessage ret=FunctionUtil.getSaveReplaceFuctionValue(split[0].toUpperCase(),fieldvalue, 
												split[1],pkdetadefinith);
										if(ret.getIssucc()){
											value=ret.getValue().toString();
										}
										hashMap.put(fieldvalue, value);
										saveReplaceMap.put(contField[j], hashMap);
									}
								}else{
									RetMessage ret=FunctionUtil.getSaveReplaceFuctionValue(split[0].toUpperCase(),fieldvalue, 
											split[1],pkdetadefinith);
									if(ret.getIssucc()){
										value=ret.getValue().toString();
									}
									HashMap<String, String> hashMap = new HashMap<String, String>();
									hashMap.put(fieldvalue, value);
									saveReplaceMap.put(contField[j], hashMap);
								}
							}
						}
						ui.getBillCardPanel().setBodyValueAt(value, i, vdefs[j]);
					}
					insertFieldvalue.append("'"+value+"',");
					
					if(null != updateAutoInMap.get(contField[j])){
						value = FunctionUtil.getFuctionValueWhenUpdate(updateAutoInMap.get(contField[j]));
					}
					strFiled = strFiled +contField[j]+"='"+value+"',";
				}
			}
			//如果没有主键插入
//			if("".equals(pkvalue)||isAdd){
			if("".equals(pkvalue)){	
				String field = insertField.toString().substring(0,insertField.toString().length()-1);
				String fieldvalue = insertFieldvalue.toString().substring(0,insertFieldvalue.toString().length()-1);
				sql = "insert into "+datadefinitVO.getMemorytable()+"("+field+") " +" values("+fieldvalue+")";
				sqlList.add(sql);
			}else{
				if(!"0".equals(p_status)){
					
				}else{
					sql = "update "+datadefinitVO.getMemorytable() + " set ";
					strFiled = strFiled.substring(0, strFiled.length()-1);
					sql = sql + strFiled+ " where "+pkTableField+"='" + pkvalue + "'";
					sqlList.add(sql);
//					queryfield.exesql(sql);
				}
			}

		}
		if(sqlList.size()>0){
			if(isInsert){
				queryfield.exectListSql(sqlList);
			}else{
				queryfield.exectListSql(sqlList);
			}
		}
		ui.setBillOperate(2);
		isAdd=false;
		isEdit=false;
		if(addpkArray!=null&&addpkArray.toString().trim().length()>0){//增加后查询语句
			String addpks=addpkArray.toString().trim();
			String whereCondition=" and "+pkTableField+" in ( "+addpks.substring(0, addpks.length()-1)+" ) and coalesce(dr,0)=0 ";
			queryDisplayValueMethod(datadefinitVO.getMemorytable(), whereCondition,datadefinitVO.getPk_datadefinit_h());
		}
		//2011-6-23 cl
//		getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(true);
//		getSelfUI().getButtonManager().getButton(IBtnDefine.DELETE).setEnabled(true);
//		getSelfUI().updateButtons();
		ui.onTreeSelectSetButtonState(treeNode);

	}
	protected void onBoDeleteDef() throws Exception {
		VOTreeNode treeNode=getSelectNode();
		if(treeNode ==null){
			getSelfUI().showErrorMessage("请选择要修改的节点!");
			return;
		}
		String pk=(String) treeNode.getParentnodeID();
		if(pk==null || "".equals(pk)){
			getSelfUI().showErrorMessage("系统节点不能修改!");
			return;
		}else{
			/*查询NC系统下的节点，不允许做修改操作 2011-5-24 begin */
			/*	String sql="select h.pk_sysregister_h from dip_sysregister_h h where h.code='0000' and h.pk_sysregister_h='"+pk+"' and nvl(h.dr,0)=0";
			String isNC=iq.queryfield(sql);
			if(isNC !=null && !"".equals(isNC.trim())){
				getSelfUI().showWarningMessage("NC系统下，只能浏览数据，不能修改！"); 
				return;
			}*/
			/* end */
		}
		this.onBoLineDel();/*
		// TODO Auto-generated method stub
//		super.onBoDelete();
		int bodycount = ui.getBillCardPanel().getBillTable().getSelectedRow();
		DipDatadefinitHVO datadefinitVO = (DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, ui.selectnode);

		String bodypk = ui.getBillCardPanel().getBillModel().getValueAt(bodycount, field)==null?"":
			ui.getBillCardPanel().getBillModel().getValueAt(bodycount, field).toString();

		String sql = "delete from  "+datadefinitVO.getMemorytable() + " where "+efield+"='"+bodypk+"'";
		int flag = MessageDialog.showOkCancelDlg(ui, "删除", "确定是否删除");
		if(flag==2){
			return;
		}
		IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
		queryfield.exesql(sql);
		//删除完成之后，执行刷新方法
		ui.getBillCardPanel().getBillModel().clearBodyData();
		List listValue = queryfield.queryListMapSingleSql(sqlQuery);
		String contField [] = contFields.toString().split(",");
		ui.getBillCardPanel().getBillModel().clearBodyData();
		if(listValue!=null && listValue.size()>0){

			for(int i = 0;i<listValue.size();i++){
				HashMap mapValue = (HashMap)listValue.get(i);
				ui.getBillCardPanel().getBillModel().addLine();
				for(int j = 0;j<contField.length;j++){

					ui.getBillCardPanel().getBillModel().setValueAt(mapValue.get(contField[j].toUpperCase()), i, "vdef"+String.valueOf(j+1));
				}


			}
		}
		ui.getBillCardPanel().setBillData(ui.getBillCardPanel().getBillData());
		ui.getBillCardPanel().getBillModel().setEditRow(1);

		 */}
	boolean isreturn=false;
	/*
	 * 添加修改修改方法
	 * 2011-5-24
	 * */
	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		VOTreeNode treeNode=getSelectNode();
		if(treeNode ==null){
			getSelfUI().showErrorMessage("请选择要修改的节点!");
			isreturn=true;
			return;
		}
		String pk=(String) treeNode.getParentnodeID();
		if(pk==null || "".equals(pk)){
			getSelfUI().showErrorMessage("系统节点不能修改!");
			isreturn=true;
			return;
		}else{
			/*查询NC系统下的节点，不允许做修改操作 2011-5-24 begin */
			/*String sql="select h.pk_sysregister_h from dip_sysregister_h h where h.code='0000' and h.pk_sysregister_h='"+pk+"' and nvl(h.dr,0)=0";
			String isNC=iq.queryfield(sql);
			if(isNC !=null && !"".equals(isNC.trim())){
				getSelfUI().showWarningMessage("NC系统下，只能浏览数据，不能修改！"); 
				return;
			}*/
			/* end */
		}
		int headcount = ui.getBillCardPanel().getBillModel().getRowCount();
		if(headcount<=0){
			getSelfUI().showWarningMessage("没有数据，不能修改！");
			isreturn=true;
			return;
		}
		/*2011-6-15 begin*/
		String str=(String) treeNode.getNodeID();
		//根据数据权限定义节点，找到权限定义表的数据定义主键
		String sqlCond=" pk_datadefinit_h=(select da.contabcode from dip_adcontdata da where da.pk_contdata_h=('"+str+"'))and nvl(dr,0)=0";
		
		DipDatadefinitBVO[] bvo=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, sqlCond);
		int c=0;
		//2011-6-23 cl 增加了 第448、457--462行，修改了第464行
		int s=0;
		for(int i=0;i<bvo.length;i++){
			UFBoolean ispk=bvo[i].getIspk();
			if(ispk!=null){
				if(ispk.booleanValue()){
					c++;
					break;
				}
			}

			UFBoolean issyspk=bvo[i].getIssyspk();
			if(issyspk !=null){
				if(issyspk.booleanValue()){
					s++;
					break;
				}
			}
		}
		if(c<=0 && s<=0){
			getSelfUI().showWarningMessage("没有主键，不能修改！");
			isreturn=true;
			return;
		}
		/*end*/
//		onBoLineAdd();
		isEdit=true;
		super.onBoEdit();

		//2011-6-23 cl
		getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
		getSelfUI().getButtonManager().getButton(IBillButton.ImportBill).setEnabled(false);
		getSelfUI().getButtonManager().getButton(IBillButton.ExportBill).setEnabled(false);
		getSelfUI().getButtonManager().getButton(IBtnDefine.DELETE).setEnabled(false);
		getSelfUI().getButtonManager().getButton(IBtnDefine.ACTION_SET).setEnabled(false);
		getSelfUI().getButtonManager().getButton(IBtnDefine.LCLineadd).setEnabled(false);
		getSelfUI().getButtonManager().getButton(IBtnDefine.LCLinedel).setEnabled(false);

		getSelfUI().updateButtons();
	}
	@Override
	protected void onBoLineDel() throws Exception {
		VOTreeNode treeNode=getSelectNode();
		if(treeNode ==null){
			getSelfUI().showErrorMessage("请选择要修改的节点!");
			isreturn=true;
			return;
		}
		String pk=(String) treeNode.getParentnodeID();
		if(pk==null || "".equals(pk)){
			getSelfUI().showErrorMessage("系统节点不能修改!");
			isreturn=true;
			return;
			}
		if(ui.pkHiddleItemKey==null||ui.pkHiddleItemKey.equals("")){
				getSelfUI().showErrorMessage("数据定义中没有找到主键不能删除!");
				return;
		}
		int bodycountArray[]=ui.getBillCardPanel().getBillTable().getSelectedRows();
		if(bodycountArray.length<1){
			getSelfUI().showErrorMessage("请选择要删除的记录！");
			return;
		}
		String bodypkArray[]=null;
		List<String> list=new ArrayList<String>();
		String statusMsg = "";
		for(int i=0;i<bodycountArray.length;i++){
			String aa=ui.getBillCardPanel().getBillModel().getValueAt(bodycountArray[i], ui.pkHiddleItemKey)==null?"":
					ui.getBillCardPanel().getBillModel().getValueAt(bodycountArray[i], ui.pkHiddleItemKey).toString();
			if(!aa.equals("")){
				list.add(aa);
			}
			Object valueAt = ui.getBillCardPanel().getBillModel().getValueAt(bodycountArray[i], ui.statusHiddleItemKey);//行审批状态
			if(null != valueAt && !"0".equals(valueAt)){
				statusMsg += "第"+(bodycountArray[i]+1)+"行不允许删除\n";
			}
		}
		if(list.size()>0){
			bodypkArray=list.toArray(new String[0]);
		}
//		String bodypk = ui.getBillCardPanel().getBillModel().getValueAt(bodycount, field)==null?"":
//			ui.getBillCardPanel().getBillModel().getValueAt(bodycount, field).toString();
		if(bodypkArray==null||bodypkArray.length<=0){
			getBillUI().showErrorMessage("没有找到选择行的主键值，不能删除！");
			return;
		}
		if(!"".equals(statusMsg)){
			getBillUI().showErrorMessage(statusMsg);
			return;
		}
		int flag = MessageDialog.showOkCancelDlg(ui, "删除", "确定是否删除选择记录！");
		if(flag==2){
			return;
		}
		DipADContdataVO vo1=(DipADContdataVO) treeNode.getData();
		//得到树上需要导出的表的主键
		String pkdetadefinith = vo1.getContabcode();
		DipDatadefinitHVO datafinitVO = (DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, pkdetadefinith);
		if(datafinitVO==null&&datafinitVO.getMemorytable()==null||datafinitVO.getMemorytable().toString().trim().equals("")){
			getBillUI().showErrorMessage("选择节点的数据定义找不到！");
			return;
		}
		int count=0;
		StringBuffer delsb=new StringBuffer();
		List<String> delList=new ArrayList<String>();
		//上级查询下级数据，不允许删除下级数据校验
		Map<String,String> lowMap=new HashMap<String, String>();//下级所有的主键map，key是主键，value是主键
		String pkcorp=ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		if(pkcorp!=null&&!pkcorp.equals("0001")){
			if(isStartAuth&&(isauthFlag.equals("1")||isauthFlag.equals("2"))&&!delSql.trim().equals("")){
				List lowlist=queryfield.queryfieldList(delSql);
				if(lowlist!=null&&lowlist.size()>0){
					for(int i=0;i<lowlist.size();i++){
						String lowpk=lowlist.get(i)==null?"":lowlist.get(i).toString();
						if(!lowpk.trim().equals("")){
							lowMap.put(lowpk, lowpk);
						}
					}
				}
			}
		}
		//	
		String is_Dr = (String) (datafinitVO.getIs_dr()==null?"N":datafinitVO.getIs_dr().toString());//是否逻辑删除
		
		for(int i=0;i<bodypkArray.length;i++){
			if(lowMap!=null&&lowMap.size()>0){
				if(bodypkArray[i]!=null&&lowMap.get(bodypkArray[i])!=null){
					getBillUI().showErrorMessage("选择记录中包含下级单位导入数据，不允许删除");
					return;
				}
			}
			delsb.append("'"+bodypkArray[i]+"',");
			if((i+1)%900==0||i==bodypkArray.length-1){
				String sql = null;
				if(is_Dr.equals("Y")){
					sql= "update "+datafinitVO.getMemorytable() + " set ";
					String strFiled = "dr=1,";
					Iterator<String> iterator = updateAutoInMap.keySet().iterator();
					while(iterator.hasNext()){
						String key = iterator.next();
						String value = FunctionUtil.getFuctionValueWhenUpdate(updateAutoInMap.get(key));
						strFiled = strFiled +key+"='"+value+"',";
					}
					strFiled = strFiled.substring(0, strFiled.length()-1);
					sql += strFiled + " where "+pkTableField+" in ("+delsb.toString().substring(0,delsb.toString().length()-1)+")";
				}else{
					sql= "delete from  "+datafinitVO.getMemorytable() + " where "+pkTableField+" in ("+delsb.toString().substring(0,delsb.toString().length()-1)+")";
					
				}
				delList.add(sql);
				delsb=new StringBuffer();
			}
//			
//			String sql= "delete from  "+datafinitVO.getMemorytable() + " where "+efield+"='"+bodypkArray[i]+"'";	
//			queryfield.exesql(sql);
			count++;
			//删除完成之后，执行刷新方法
		}
		if(delList.size()<=0){
			getBillUI().showErrorMessage("删除选择记录错误，刷新后在操作");
			return;
		}else{
			queryfield.exectListSql(delList);
		}
		
		ui.getBillCardPanel().getBillModel().clearBodyData();
		List listValue = queryfield.queryListMapSingleSql(sqlQuery);
		String contField [] = ui.contFields.toString().split(",");
		String vdefs [] = ui.vdef.toString().split(",");
		ui.getBillCardPanel().getBillModel().clearBodyData();
		if(listValue!=null && listValue.size()>0){
			HashMap<String, HashMap<String, String>> showReplaceMap = new HashMap<String, HashMap<String, String>>();
			for(int i = 0;i<listValue.size();i++){
				HashMap mapValue = (HashMap)listValue.get(i);
				ui.getBillCardPanel().getBillModel().addLine();
				for(int j = 0;j<contField.length;j++){
					String upperCase = contField[j].toUpperCase();
					Object value=mapValue.get(upperCase);
					String string = showFunctionMap.get(upperCase);
					if(null != string && string.startsWith(FunctionUtil.SHOWREPLACEPK)){
						int indexOf = string.indexOf("(");
						String substring = string.substring(indexOf+1, string.length()-1);
						String[] split = substring.split(",");
						if(null != split && split.length==2){
							Object object = mapValue.get(split[0].toUpperCase());
							if(null != object){
								if(null != showReplaceMap.get(contField[j])){
									HashMap<String, String> hashMap = showReplaceMap.get(contField[j]);
									String newStr = hashMap.get(object.toString());
									if(null != newStr){
										value = newStr;
									}else{
										value = putShowReplaceMap(
												pkdetadefinith, contField[j],
												showReplaceMap, value,
												split, object, hashMap);
									}
								}else{
									HashMap<String, String> hashMap = new HashMap<String, String>();
									value = putShowReplaceMap(pkdetadefinith,
											contField[j], showReplaceMap,
											value, split, object, hashMap);
								}
							}
						}
					}
					string = showAutoInMap.get(upperCase);
					if(null != string){
						value = FunctionUtil.getFuctionValue(string);
					}
					ui.getBillCardPanel().getBillModel().setValueAt(value, i, vdefs[j]);
				}
			}
		}
		ui.getBillCardPanel().setBillData(ui.getBillCardPanel().getBillData());
		ui.getBillCardPanel().getBillModel().setEditRow(1);
		ui.getBillCardPanel().getBillTable().setColumnSelectionAllowed(false);
		MessageDialog.showHintDlg(getSelfUI(), "成功", "成功删除"+count+"条数据");
	}
	private Object putShowReplaceMap(String pkdetadefinith, String contField,
			HashMap<String, HashMap<String, String>> showReplaceMap,
			Object value, String[] split, Object object,
			HashMap<String, String> hashMap) {
		RetMessage ret=FunctionUtil.getShowReplaceFuctionValue(split[0].toUpperCase(),object.toString(), 
				split[1],pkdetadefinith);
		if(ret.getIssucc()){
			value=ret.getValue().toString();
		}
		hashMap.put(object.toString(), value.toString());
		showReplaceMap.put(contField, hashMap);
		return value;
	}
	

	@Override
	public void onBoAdd(ButtonObject arg0) throws Exception {
		if(ui.contFields.toString()==null||ui.contFields.toString().length()<=0){
			getSelfUI().showErrorMessage("没有定义显示数据！");
			return;
		}
		isreturn=false;
//		onBoEdit();
		ui.getBillCardPanel().getBillModel().clearBodyData();
		onBoLineAdd();
		super.onBoEdit();
		if(!isreturn){
			isAdd=true;
		}
		whereCondition = "";
		getSelfUI().getButtonManager().getButton(IBillButton.ImportBill).setEnabled(false);
		getSelfUI().getButtonManager().getButton(IBillButton.ExportBill).setEnabled(false);
		getSelfUI().getButtonManager().getButton(IBtnDefine.ACTION_SET).setEnabled(false);
		getSelfUI().updateButtons();
	}
	protected void  add() throws Exception
	{
		VOTreeNode treeNode=getSelectNode();
		if(treeNode ==null){
			getSelfUI().showErrorMessage("请选择要修改的节点!");
			return;
		}
//		String pk=(String) treeNode.getParentnodeID();
//		if(pk==null || "".equals(pk)){
//		getSelfUI().showErrorMessage("系统节点不能修改!");
//		return;
//		}else{
//		/*查询NC系统下的节点，不允许做修改操作 2011-5-24 begin */
//		String sql="select h.pk_sysregister_h from dip_sysregister_h h where h.code='0000' and h.pk_sysregister_h='"+pk+"' and nvl(h.dr,0)=0";
//		String isNC=iq.queryfield(sql);
//		if(isNC !=null && !"".equals(isNC.trim())){
//		getSelfUI().showWarningMessage("NC系统下，只能浏览数据，不能修改！"); 
//		return;
//		}
//		/* end */
//		}
		// TODO Auto-generated method stub
		isAdd=true;
		int sta=getSelfUI().getBillOperate();
		if(sta!=IBillOperate.OP_ADD&&sta!=IBillOperate.OP_EDIT){
			getSelfUI().setBillOperate(IBillOperate.OP_ADD);
		}
		onBoLineAdd();
		getButtonManager().getButton(IBillButton.Save).setEnabled(true);
		getButtonManager().getButton(IBillButton.Cancel).setEnabled(true);
	}
//	@Override
//	protected void onBoLineAdd() throws Exception {
//		// TODO Auto-generated method stub
//		super.onBoLineAdd();
//	}
	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCancel();
		isAdd=false;
		isEdit=false;

		//2011-6-23 cl
//		getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(true);
//		getSelfUI().updateButtons();
		ui.onTreeSelectSetButtonState(getSelectNode());
	}
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {
		case IBtnDefine.DATALOOKQUERY:
			onDatalookQuery(intBtn);
			break;
		case IBtnDefine.DATACLEAR:
			onDataClear(intBtn);
			break;
//		case IBtnDefine.ADD:
//			onBoAdd();
//			break;
		case IBtnDefine.DELETE:
			onBoDeleteDef();
			break;
//		case IBtnDefine.SET:
//			onBoSet();
		case IBtnDefine.LCLineadd:
			onBoLineAdd();
			break;
		case IBtnDefine.LCLinedel:
			onBoLineDelete();
			break;
		case IBtnDefine.ACTION_SET:
			onBoActionSet();
			break;
		}
	}
	
	public void onBoActionSet() throws Exception {
		VOTreeNode treeNode=getSelectNode();
		if(treeNode==null){
			getSelfUI().showErrorMessage("请选择要导入的节点！");
			return ;
		}
		String str=(String)treeNode.getParentnodeID();
		DipADContdataVO vo=(DipADContdataVO) treeNode.getData();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("系统节点不能做保存操作！"); 
			return;
		}
		int[] rows = ui.getBillCardPanel().getBillTable().getSelectedRows();
		if(rows.length==0){
			getSelfUI().showWarningMessage("请先查询出需要操作的数据！"); 
			return;
		}
		ActionSetDailog dlg = new ActionSetDailog(getSelfUI(),vo.getPrimaryKey());
		dlg.show();
		if(dlg.getResult()==UIDialog.ID_OK){
			ActionSetBVO rowVO = dlg.getRowVO();
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < rows.length; i++) {
				list.add(ui.getBillCardPanel().getBillModel().getValueAt(rows[i], ui.pkHiddleItemKey).toString());
			}
			String message = FunctionUtil.execAction(rowVO, vo.getContabcode(),
					list,"",pkTableField);
			if(null != message){
				getSelfUI().showWarningMessage(message); 
			}else{
				MessageDialog.showHintDlg(getSelfUI(), "提示", "动作  "+rowVO.getName()+"执行完成！");
				String queryCondition = null;
				if("".equals(whereCondition)){
					int rowCount = ui.getBillCardPanel().getBillTable().getRowCount();
					list = new ArrayList<String>();
					String addpks = "";
					for (int i = 0; i < rowCount; i++) {
						String pk = ui.getBillCardPanel().getBillModel().getValueAt(i, ui.pkHiddleItemKey).toString();
						addpks+="'"+pk+"',";
					}
					queryCondition =" and "+pkTableField+" in ( "+addpks.substring(0, addpks.length()-1)+" ) ";
				}else{
					queryCondition = whereCondition;
				}
				DipDatadefinitHVO datadefinitVO = (DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, vo.getContabcode());
				queryDisplayValueMethod(datadefinitVO.getMemorytable(),queryCondition,datadefinitVO.getPk_datadefinit_h());
			}
		}
	}
	private void onBoLineDelete() {
		BillScrollPane bodyPanel = getBillCardPanelWrapper().getBillCardPanel().getBodyPanel();
		bodyPanel.delLine();
	}
	
//	//设置按钮
//	private void onBoSet() {
//		VOTreeNode treeNode=getSelectNode();
//		if(treeNode==null){
//			getSelfUI().showErrorMessage("请选择要导入的节点！");
//			return ;
//		}
//		String str=(String)treeNode.getParentnodeID();
//		DipADContdataVO vo=(DipADContdataVO) treeNode.getData();
//		if(str ==null || str.equals("")){
//			getSelfUI().showWarningMessage("系统节点不能做保存操作！"); 
//			return;
//		}
//		DesignDLG dlg=null;
//		if(vo.getContcolcode()!=null&&vo.getContcolcode().length()>0){
//			dlg=new DesignDLG(getSelfUI(),new String[]{vo.getContabcode()},new String[]{vo.getContcolcode()},true);
////			dlg=new DesignDLG(getSelfUI(),new String[]{vo.getExtetabcode()},new String[]{vo.()});
//		}else{
//			dlg=new DesignDLG(getSelfUI(),new String[]{vo.getContabcode()});
//		}
//		dlg.showModal();
//	}

	
	boolean fenye=false;
	int wenjian=0;
	//2011-6-13  张进双  根据设置里面的导出设置导出数据的导出按钮
	@Override
	protected void onBoExport() throws Exception {
		boolean expModel=false;//导出模板
		// TODO Auto-generated method stub
		//super.onBoExport();
		wenjian=0;
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getSelfUI().showWarningMessage("请选择要操作的节点！");
			return;
		}
		String fpk=(String) node.getParentnodeID();
		if(SJUtil.isNull(fpk)||fpk.length()==0){
			this.getSelfUI().showWarningMessage("系统节点不可编辑！");
			return;
		}
		AskDLG adlg=new AskDLG(getSelfUI(),"提示","选择数据浏览导出类型？",new String[]{"导出当前查询范围数据","导出所有数据","导出模板"});
		adlg.showModal();
		boolean isdqfw=false;
		if(adlg.getRes()==0){
			expModel=false;
			isdqfw=true;
		}else if(adlg.getRes()==1){
			expModel=false;
			isdqfw=false;
		}else if(adlg.getRes()==2){
			expModel=true;
		}else{
			return;
		}

		String condsql=null;
		String path=null;
		try {
			
			VOTreeNode treeNode=getSelectNode();
			if(treeNode==null){
				getSelfUI().showErrorMessage("请选择要查询的节点！");
				return ;
			}
			DipADContdataVO vo1=(DipADContdataVO) treeNode.getData();
			//得到树上需要导出的表的主键
			String pk = vo1.getContabcode();

			DipDatadefinitHVO datafinitVO = (DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, pk);
			//对照结果存储表
			String tablename = datafinitVO.getMemorytable();
			//找出所有要查的字段从dip_auth_design里面查询要导出的字段；
			String sql_ziduan = "select cname,ename from dip_auth_design where nvl(dr,0)=0 and pk_datadefinit_h = '"+vo1.getPrimaryKey()+"' and designtype = '3' order by disno ";
		
			List list_ziduan = queryfield.queryListMapSingleSql(sql_ziduan);
			//要查询的字段拼接的字符
			String ziduan = "";
			String cname = "";
			if(list_ziduan==null||list_ziduan.size()<=0){
				this.getSelfUI().showErrorMessage("没有进行导出设置！请重新设置");
				return;
			}
			JFileChooser jfileChooser = new JFileChooser();
			jfileChooser.setDialogType(jfileChooser.SAVE_DIALOG);
			jfileChooser.setFileFilter(new FileFilter());
			if (jfileChooser.showSaveDialog(this.getBillUI()) == javax.swing.JFileChooser.CANCEL_OPTION)
				return;
			path = jfileChooser.getSelectedFile().toString();
			String[] ss=new String[list_ziduan.size()];

			for(int i=0;i<list_ziduan.size();i++){
				HashMap map_ziduan = (HashMap)list_ziduan.get(i);
				String ename = map_ziduan.get("ename".toUpperCase()).toString();
				String name = map_ziduan.get("cname".toUpperCase()).toString();
				ziduan =ziduan + ename + ",";
				ss[i]=ename;
				cname =cname + name + ",";

			}
			if(expModel){
				String filePath = path.endsWith(".xls")?path:(path+".xls");
				ExpToExcel toexcle = new ExpToExcel(filePath,"数据对照结果",cname.substring(0,cname.length()-1),null,null);
				MessageDialog.showHintDlg(getSelfUI(), "成功", "导出成功");
				return;
			}
			//去选择的相对于的表里去查询数据；
			//2011-7-1 增加了验证表是否在数据库中存在
			boolean isExist=isTableExist(tablename);
			if(isExist){
				
				String sql="select count(*) from "+tablename.toUpperCase()+" where "+(isdqfw?"1=1 "+whereCondition:defwhere+" and coalesce(dr,0)=0 ");
				String count=queryfield.queryfield(sql);
				int s=Integer.parseInt(count);
				if(s==0){
					getSelfUI().showErrorMessage("没有符合条件的数据！");
					return;
				}
				
				if(s>50000){
					fenye=true;
					int k=s%50000==0?s/50000:s/50000+1;
					StringBuffer sb=new StringBuffer();
					for(int kkk=0;kkk<k;kkk++){
						String pp=path+"-"+kkk;
						wenjian++;
						if(method(ziduan, tablename, pp, ss, cname,isdqfw,pk)){
							sb.append(pp+"、");
							if(kkk==k-1){
								MessageDialog.showWarningDlg(this.getBillUI(), "提示", "导出完成!导出的文件是"+sb.substring(0, sb.length()-1)+"!");
								return ;
							}
						}else{
							getSelfUI().showErrorMessage("导出错误！");
        					return;
						}
					}
					
        			}else{
        				if(method(ziduan, tablename, path, ss, cname,isdqfw,pk)){
        					MessageDialog.showWarningDlg(this.getBillUI(), "提示", "导出完成!");
        					return;
        				}else{
        					getSelfUI().showErrorMessage("导出错误！");
        					return;
        				}
        			}
			}else{
				getSelfUI().showWarningMessage("表或视图不存在！");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			getSelfUI().showErrorMessage(e.getMessage());
		}
	}
	
	public boolean method(String ziduan,String tablename,String path,String[] ss,String cname,boolean isdqfw,String pk) throws BusinessException, SQLException, DbException, IOException{
		
		String sql_session = "select "+ziduan.substring(0, ziduan.length()-1)+" from "+tablename.toUpperCase()+(isdqfw?" where 1=1 "+whereCondition:(defwhere.trim().equals("1=1")?" where coalesce(dr,0)=0 ":" where 1=1 and coalesce(dr,0)=0 and "+defwhere));
		if(fenye){
		//select * from (
		//       select a1.*, rownum rwn  from emp a1   where rownum <=10
		//       ) where rwn >= 6;
	   String sss="select "+ziduan.substring(0, ziduan.length()-1) +" from ( "
	                      +"select "+ziduan.substring(0, ziduan.length()-1)+",rownum rwn  from "+tablename.toUpperCase()+"   where rownum<="+ +wenjian*50000
	                      +" )  where rwn>"+(wenjian-1)*50000;
			sql_session=sss;
		}
		List list=new ArrayList();
		list = queryfield.queryListMapSingleSql(sql_session);
	
		ExpExcelVO[] vosW = null;
		if(list!=null && list.size()>0){
			List<ExpExcelVO> listW = new ArrayList<ExpExcelVO>();
			HashMap<String, HashMap<String, String>> showReplaceMap = new HashMap<String, HashMap<String, String>>();
			for(int i = 0;i<list.size();i++){
				HashMap map = (HashMap)list.get(i);
				ExpExcelVO	vo = new ExpExcelVO();

				for(int j = 0;j<ss.length;j++){
					String upperCase = ss[j].toUpperCase();
					Object value=map.get(upperCase);
					String string = exportFunctionMap.get(upperCase);
					if(null != string && string.startsWith(FunctionUtil.SHOWREPLACEPK)){
						int indexOf = string.indexOf("(");
						String substring = string.substring(indexOf+1, string.length()-1);
						String[] split = substring.split(",");
						if(null != split && split.length==2){
							Object object = map.get(split[0].toUpperCase());
							if(null != object){
								if(null != showReplaceMap.get(upperCase)){
									HashMap<String, String> hashMap = showReplaceMap.get(upperCase);
									String newStr = hashMap.get(object.toString());
									if(null != newStr){
										value = newStr;
									}else{
										value = putShowReplaceMap(
												pk, upperCase,
												showReplaceMap, value,
												split, object, hashMap);
									}
								}else{
									HashMap<String, String> hashMap = new HashMap<String, String>();
									value = putShowReplaceMap(pk,
											upperCase, showReplaceMap,
											value, split, object, hashMap);
								}
							}
						}
					}
					string = exportAutoInMap.get(upperCase);
					if(null != string){
						value = FunctionUtil.getFuctionValue(string);
					}
					vo.setAttributeValue("line"+Integer.valueOf(j+1), value);
				}
				listW.add(vo);
			}

			vosW = listW.toArray(new ExpExcelVO[0]);
			String filePath = path.endsWith(".xls")?path:(path+".xls");
			File file = new File(filePath);	
			if(!file.exists())
				file.createNewFile();	
			String fielPathTemp = path+"-1.xls";
			ExpToExcel toexcle = new ExpToExcel(filePath,"数据权限浏览",cname.substring(0,cname.length()-1),vosW,fielPathTemp);
			return true;
			//MessageDialog.showWarningDlg(this.getBillUI(), "提示", "导出完成!");
		}else{
			getSelfUI().showErrorMessage("没有符合条件的数据！");
			return false;
		}
	}
	
	
	//	导入按钮 张进双 2011-6-14
	@Override
	protected void onBoImport() throws Exception {
		VOTreeNode treeNode=getSelectNode();
		if(treeNode==null){
			getSelfUI().showErrorMessage("请选择要导入的节点！");
			return ;
		}
		String str=(String)treeNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("系统节点不能做保存操作！"); 
			return;
		}
		if(treeNode==null){
			getSelfUI().showErrorMessage("请选择要查询的节点！");
			return ;
		}
		final DipADContdataVO vo1=(DipADContdataVO) treeNode.getData();
		String pk=vo1.getContabcode();
//		数据定义VO
		final DipDatadefinitHVO cvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, pk);
//		找出所有要查的字段从dip_auth_design里面查询要导入的字段；
		String sql_ziduan = "select cname,ename,consvalue from dip_auth_design where nvl(dr,0)=0 and pk_datadefinit_h = '"+vo1.getPrimaryKey()+"' and designtype = '2' order by disno ";
		DipDatadefinitBVO []datadefinitbvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "pk_datadefinit_h = '"+pk+"' and nvl(dr,0)=0 ");
		HashMap<String, DipDatadefinitBVO> databMap=new HashMap<String, DipDatadefinitBVO>();
		if(datadefinitbvos==null||datadefinitbvos.length==0){
			getSelfUI().showErrorMessage("找不到表"+cvo.getMemorytable()+"的数据定义子表！");
			return ;
		}
		for(int i=0;i<datadefinitbvos.length;i++){
			databMap.put(datadefinitbvos[i].getEname(), datadefinitbvos[i]);
		}
		List list_ziduan = queryfield.queryListMapSingleSql(sql_ziduan);
		//主键字段
		String pk_defin = null;
		//要查询的字段拼接的字符
		String ziduan = "";
		String cname = "";
		if(list_ziduan==null||list_ziduan.size()<=0){
			this.getSelfUI().showErrorMessage("没有进行导入设置！请重新设置");
			return;
		}
		List listimport=new ArrayList();
		List listAutoIn=new ArrayList(); //自动填充数据，例如填写函数会自动填充函数值,目前只有3个，公司主键，用户主键，主键函数
		List listFunction=new ArrayList();//函数，但不是自动填充函数，例如，转换大小写，转成月份函数
		HashMap autoInMap=new HashMap();//ename,consvalue
		HashMap functionMap=new HashMap();//ename,consvalue
		for(int i=0;i<list_ziduan.size();i++){
			if(list_ziduan.get(i)!=null&&list_ziduan.get(i) instanceof Map){
				if(((Map)(list_ziduan.get(i))).get("consvalue".toUpperCase())!=null&&((Map)(list_ziduan.get(i))).get("consvalue".toUpperCase()).toString().trim().length()>0){
					if(FunctionUtil.isAutoIn(((Map)(list_ziduan.get(i))).get("consvalue".toUpperCase()).toString())){
						listAutoIn.add(list_ziduan.get(i));
						if(((Map)list_ziduan.get(i)).get("ename".toUpperCase())!=null&&((Map)list_ziduan.get(i)).get("consvalue".toUpperCase())!=null){
							autoInMap.put(((Map)list_ziduan.get(i)).get("ename".toUpperCase()), ((Map)list_ziduan.get(i)).get("consvalue".toUpperCase()));
						}
					}else{
//						listimport.add(list_ziduan.get(i));
						if(((Map)list_ziduan.get(i)).get("ename".toUpperCase())!=null&&((Map)list_ziduan.get(i)).get("consvalue".toUpperCase())!=null){
							functionMap.put(((Map)list_ziduan.get(i)).get("ename".toUpperCase()), ((Map)list_ziduan.get(i)).get("consvalue".toUpperCase()));
						}
					}
				}else{
					listimport.add(list_ziduan.get(i));
				}
			}
		}
		JFileChooser jfileChooser = new JFileChooser();
		jfileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		jfileChooser.setFileFilter(new FileFilter());
		if (jfileChooser.showSaveDialog(this.getBillUI()) == javax.swing.JFileChooser.CANCEL_OPTION)
			return;
		String path = jfileChooser.getSelectedFile().toString();
		if(!path.endsWith(".xls")){
			path=path+".xls";
		}
		
//		new Thread() {
//			@Override
//			public void run() {
//				BannerDialog dialog = new BannerDialog(getSelfUI());
//				dialog.setTitle("正在执行流程，请稍候...");
//				dialog.setStartText("正在执行流程，请稍候...");
//				}
//		}.start();
		FileInputStream fin=new FileInputStream(path);
		TxtDataVO tvo=new TxtDataVO();
		String titlenames="";
		try{
			HSSFWorkbook book = new HSSFWorkbook(fin);
			HSSFSheet sheet = book.getSheetAt(0);
			if(sheet == null){
				getSelfUI().showErrorMessage("导入文件格式不正确！");
				return;
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
				if(getCellValue(titleRow.getCell(i))==null||getCellValue(titleRow.getCell(i)).toString().equals("")){
					getSelfUI().showErrorMessage("导入的excel头不能有空字段！");
					return;
				}
				String cellvalue=getCellValue(titleRow.getCell(i)).toString();
				if(titlemap.get(cellvalue)!=null){
					getSelfUI().showErrorMessage("导入的excel头不能有重复字段！");
					return;
				}
				titlemap.put(getCellValue(titleRow.getCell(i))==null?"":getCellValue(titleRow.getCell(i)).toString(),Short.toString(i));
				titlenames=titlenames+getCellValue(titleRow.getCell(i)).toString()+",";
			}
			tvo.setTitlemap(titlemap);
			int titlesize=titlemap.size();
			for(int i=1;i<=tvo.getRowCount();i++){
				HSSFRow row = sheet.getRow(tvo.getFirstDataRow() + i);
				if(null != row){
//					for(short j=0;j<titleRow.getPhysicalNumberOfCells();j++){
//						tvo.setCellData(i-1, j, getCellValue(row.getCell(j)));
//					}
					Set<String> keySet = titlemap.keySet();
					Iterator<String> iterator = keySet.iterator();
					while(iterator.hasNext()){
						String next = iterator.next();
						String j = titlemap.get(next);
						tvo.setCellData(i-1,Integer.valueOf(j), getCellValue(row.getCell(Short.valueOf(j))));
					}
				}
			}
		}finally{
			fin.close();
		}
//		if(listimport.size() != tvo.getColCount()){
//			getSelfUI().showErrorMessage("导入设置的字段与导入文件中的字段不一致，请重新进行设置或修改文件！");
//			return;
//		}
		boolean fieldsFlag=true;//中文字段对照7
		HashMap enametoExcelCol=new HashMap();
		String fileskey="";
		if(fieldsFlag){
			fileskey="cname".toUpperCase();
		}else{
			fileskey="ename".toUpperCase();
		}
		
		for(int c=0;c<listimport.size();c++){
			boolean isexist=false;
			Object object = ((Map)listimport.get(c)).get(fileskey);
			Iterator it=tvo.getTitlemap().keySet().iterator();
			while(it.hasNext()){
				String fieldCname=it.next().toString();
					if(object!=null&&object.equals(fieldCname)){
						isexist=true;
						if(fieldsFlag){
							enametoExcelCol.put(((Map)listimport.get(c)).get("ename".toUpperCase()), tvo.getTitlemap().get(((Map)listimport.get(c)).get("cname".toUpperCase())));
						}else{
							enametoExcelCol.put(object, tvo.getTitlemap().get(fileskey));
						}
						break;
					}
				}
				if(!isexist){
					getSelfUI().showErrorMessage("excel中没有找到导入设置中"+object+"对应的字段！");
					return;
				}
			}
		final String tablename = cvo.getMemorytable();
		if(tvo.getData()==null||tvo.getData().length==0){
			getSelfUI().showErrorMessage("excel文件不能为空");
			return;
		}
		
		final HashMap<String, DipDatadefinitBVO> final_databMap = databMap;
		final HashMap final_autoInMap = autoInMap;
		final HashMap final_functionMap = functionMap;
		final String final_path = path;
		final TxtDataVO final_tvo = tvo;
		final String final_titlenames = titlenames;
		final HashMap final_enametoExcelCol = enametoExcelCol;
		new Thread() {
			@Override
			public void run() {
				BannerDialog dialog = new BannerDialog(getSelfUI());
				dialog.setTitle("正在导入，请稍候...");
				dialog.setStartText("正在导入，请稍候...");
				dialog.start();
				try {
					Thread.sleep(500); // 等待1秒
					importData(vo1, cvo, final_databMap, final_autoInMap, final_functionMap, final_path, final_tvo,
							final_titlenames, final_enametoExcelCol, tablename, 0, new ArrayList<RowDataVO>(),
							new ArrayList<String>());
				} catch (Exception e) {
					MessageDialog.showErrorDlg(getBillUI(), "错误", e.getMessage());
				}finally {
					dialog.end();
				}
			}
		}.start();
		
	}
	private void importData(DipADContdataVO vo1, DipDatadefinitHVO cvo,
			HashMap<String, DipDatadefinitBVO> databMap, HashMap autoInMap,
			HashMap functionMap, String path, TxtDataVO tvo, String titlenames,
			HashMap enametoExcelCol, String tablename, int insertcount,
			List<RowDataVO> errList, List<String> insertlist)
			throws UifException, Exception, IOException {
		HashMap<String, DipDatadefinitCVO[]> checkMap = DataCheckUtil.getDataCheckMap(cvo.getPk_datadefinit_h());
		HashMap<String, HashMap<String, String>> checkValueMap = new HashMap<String, HashMap<String, String>>();
		HashMap<String, HashMap<String, String>> saveReplaceMap = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> enameMap = new HashMap<String, String>();
		String version = "";
		for(int i=0;i<tvo.getData().length;i++){
			StringBuffer insertsql=new StringBuffer( "insert into  "+tablename );
			StringBuffer filds=new StringBuffer();
			StringBuffer values=new StringBuffer();
			Iterator it2=enametoExcelCol.keySet().iterator();
			Iterator functionIt=autoInMap.keySet().iterator();//函数字段
			StringBuffer sb=new StringBuffer();
			HashMap<String, String> autoIn = new HashMap<String, String>();
			//1.先替换主键
			while(functionIt.hasNext()){
				String ename=functionIt.next().toString();
				filds.append(" "+ename+",");
				String string = autoInMap.get(ename).toString();
				String value = "";
				if(FunctionUtil.SYSVERSIONPK.equals(string)){
					if("".equals(version)){
						value=FunctionUtil.getFuctionValue(string);
						version= value;
					}else{
						value = version;
					}
				}else{
					value=FunctionUtil.getFuctionValue(string);
				}
				if(databMap.get(ename).getType().contains("CHAR")){
					values.append(" '"+value+"',");
				}else{
					if(value==null||value.trim().equals("")){
						value="null";
					}
					values.append(" "+value+",");
				}
				autoIn.put(ename, value);
			}
			
//			int j=0;
			while(it2.hasNext()){
				String ename=it2.next().toString();
				filds.append(" "+ename+",");
				String value="";
//				if(autoInMap.get(ename)!=null){
//					value=FunctionUtil.getFuctionValue(autoInMap.get(ename).toString());
//				}else{
					value=tvo.getCellData(i, Integer.parseInt(enametoExcelCol.get(ename).toString()))==null?"":tvo.getCellData(i, Integer.parseInt(enametoExcelCol.get(ename).toString())).toString();
//				}

					
				DipDatadefinitBVO bvo=databMap.get(ename);
				if(bvo!=null){
					StringBuffer allsb=new StringBuffer();
					if(null !=checkMap.get(ename)){
						if(null != checkValueMap.get(ename)){
							HashMap<String, String> hashMap = checkValueMap.get(ename);
							String string = hashMap.get(value);
							if(null != string){
								if(!"Y".equals(string)){
									allsb.append(string);
								}
							}else{
								String message = getEnameCN(tvo,
										enametoExcelCol, ename,enameMap);
								String checkDataMsg = DataCheckUtil.checkData(ename, value,vo1.getMiddletab());
								if(null != checkDataMsg){
									hashMap.put(value, message+"  "+checkDataMsg);
									allsb.append(message+"  "+checkDataMsg);
								}else{
									hashMap.put(value,"Y");
								}
								checkValueMap.put(ename, hashMap);
							}
						}else{
							String message = getEnameCN(tvo, enametoExcelCol,
									ename,enameMap);
							String checkDataMsg = DataCheckUtil.checkData(ename, value,vo1.getMiddletab());
							HashMap<String, String> hashMap = new HashMap<String, String>();
							if(null != checkDataMsg){
								hashMap.put(value, message+"  "+checkDataMsg);
								allsb.append(message+"  "+checkDataMsg);
							}else{
								hashMap.put(value,"Y");
							}
							checkValueMap.put(ename, hashMap);
						}
					}
					StringBuffer ss=checkImportValue(bvo, value,allsb);
					if(ss.toString().trim().length()>0){
						sb.append(ss.toString());//表示有错误信息
					}else{
						Object object = functionMap.get(ename);
						if(object!=null && !object.toString().startsWith(FunctionUtil.SAVEREPLACEPK) && !object.toString().startsWith(FunctionUtil.SHOWREPLACEPK)){
//									说明要通过下面方法得到函数值
							RetMessage ret=FunctionUtil.getFuctionValue(value, functionMap.get(ename).toString());
							if(ret.getIssucc()){
								value=ret.getValue().toString();
							}else{
								getSelfUI().showErrorMessage(ret.getMessage());
								return;
							}
						}else if(object!=null && object.toString().startsWith(FunctionUtil.SAVEREPLACEPK)){
							String function = object.toString();
							int indexOf = function.indexOf("(");
							String substring = function.substring(indexOf+1, function.length()-1);
							String[] split = substring.split(",");
							if(null != split && split.length==2){
								if(null != enametoExcelCol.get(split[0].toUpperCase())){
									Object object2 = enametoExcelCol.get(split[0].toUpperCase());
									String oldvalue=tvo.getCellData(i, Integer.parseInt(object2.toString()))==null?""
											:tvo.getCellData(i, Integer.parseInt(object2.toString())).toString();
									if(null != saveReplaceMap.get(ename)){
										HashMap<String, String> hashMap = saveReplaceMap.get(ename);
										String string = hashMap.get(oldvalue);
										if(null != string){
											value = string;
										}else{
											value = putSaveReplaceMap(cvo,
													saveReplaceMap, ename,
													split, value, oldvalue,
													hashMap);
										}
									}else{
										HashMap<String, String> hashMap = new HashMap<String, String>();
										value = putSaveReplaceMap(cvo,
												saveReplaceMap, ename,
												split, value, oldvalue,
												hashMap);
									}
								}
							}
						}
						if(bvo.getType().contains("CHAR")){
							values.append(" '"+value+"',");
						}else{
							if(value==null||value.trim().equals("")){
								value="null";
							}
							values.append(" "+value+",");
						}
					}
				}else{
					getSelfUI().showErrorMessage("表"+tablename+"数据定义中没有找到字段"+ename);
					return;
				}
			}
			if(sb.length()>0){
				RowDataVO rowvo=tvo.getData()[i];
				int m=tvo.getColCount();
				rowvo.setAttributeValue(m+"", sb.toString().substring(0, sb.length()-1));
				errList.add(rowvo);
			}
			if(errList.size()>0){
				continue;
			}
			Iterator iterator = functionMap.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next().toString();
				if(!enametoExcelCol.containsKey(key)){
					Object object = functionMap.get(key);
					if(object!=null && object.toString().startsWith(FunctionUtil.SAVEREPLACEPK)){
						filds.append(" "+key+",");
						String function = object.toString();
						int indexOf = function.indexOf("(");
						String substring = function.substring(indexOf+1, function.length()-1);
						String[] split = substring.split(",");
						String refvalue = null;
						if(null != split && split.length==2){
							if(null != enametoExcelCol.get(split[0].toUpperCase())){
								Object object2 = enametoExcelCol.get(split[0].toUpperCase());
								String value=tvo.getCellData(i, Integer.parseInt(object2.toString()))==null?""
										:tvo.getCellData(i, Integer.parseInt(object2.toString())).toString();
								if(null != saveReplaceMap.get(key)){
									HashMap<String, String> hashMap = saveReplaceMap.get(key);
									String string = hashMap.get(value);
									if(null != string){
										refvalue = string;
									}else{
										refvalue = putSaveReplaceMap(cvo,
												saveReplaceMap, key, split,
												refvalue, value, hashMap);
									}
								}else{
									HashMap<String, String> hashMap = new HashMap<String, String>();
									refvalue = putSaveReplaceMap(cvo,
											saveReplaceMap, key, split,
											refvalue, value, hashMap);
								}
							}else if(null != autoIn.get(split[0].toUpperCase())){
								String oldvalue = autoIn.get(split[0].toUpperCase());
								if(null != saveReplaceMap.get(key)){
									HashMap<String, String> hashMap = saveReplaceMap.get(key);
									String string = hashMap.get(oldvalue);
									if(null != string){
										refvalue = string;
									}else{
										refvalue = putSaveReplaceMap(cvo,
												saveReplaceMap, key, split,
												refvalue, oldvalue, hashMap);
									}
								}else{
									HashMap<String, String> hashMap = new HashMap<String, String>();
									refvalue = putSaveReplaceMap(cvo,
											saveReplaceMap, key, split,
											refvalue, oldvalue, hashMap);
								}
							}
						}
						if(databMap.get(key).getType().contains("CHAR")){
							values.append(" '"+refvalue+"',");
						}else{
							if(refvalue==null||refvalue.trim().equals("")){
								refvalue="null";
							}
							values.append(" "+refvalue+",");
						}
					}
				}
			}
			
			if(filds.length()>0&&values.length()>0){
				insertsql.append(" ( "+filds.toString().substring(0, filds.toString().length()-1)+") values ( "+values.toString().substring(0, values.toString().length()-1)+" )" );
				insertlist.add(insertsql.toString());
//				queryfield.exesql(insertsql.toString());
				insertcount++;
			}
		}
		if(errList.size()>0){
			path=path.replace(".xls", "-err.xls");
			String sheetNames[]=new String[]{"数据权限浏览"};
			titlenames=titlenames+"错误,";
			String fileNames[]=new String[]{titlenames.substring(0, titlenames.length()-1)};
			List<RowDataVO[]> list=new ArrayList<RowDataVO[]>();
			list.add(errList.toArray(new RowDataVO[0]));
			ExpToExcel1 excel=new ExpToExcel1(path,sheetNames,fileNames,list,null);
		 		 if(excel.create()){
		 			 getSelfUI().showWarningMessage("导入失败，请查看错误信息["+path+"]");
		 			 return;
		 		 }else{
		 			getSelfUI().showWarningMessage("导入失败");
		 			return;
		 		 }
		}
		if(insertlist.size()>0){
				try {
//					for(int i=0;i<insertlist.size();i++){
//						queryfield.exesql(insertlist.get(i));
//					}
					queryfield.exectListSql(insertlist);
//					queryfield.exesql(insertlist.get(i));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if(e.getMessage().startsWith("ORA-00001:")){
						getSelfUI().showErrorMessage("插入数据错误,数据唯一约束重复!");
						return;
					}else{
						getSelfUI().showErrorMessage("插入数据错误,"+e.getMessage()+"!");
						return;
					}
				}
		}
		MessageDialog.showHintDlg(getSelfUI(), "提示", "导入"+insertcount+"条数据！");
	}
	private String putSaveReplaceMap(DipDatadefinitHVO cvo,
			HashMap<String, HashMap<String, String>> saveReplaceMap,
			String key, String[] split, String refvalue, String oldvalue,
			HashMap<String, String> hashMap) {
		RetMessage ret=FunctionUtil.getSaveReplaceFuctionValue(split[0].toUpperCase(),oldvalue, 
				split[1],cvo.getPk_datadefinit_h());
		if(ret.getIssucc()){
			refvalue=ret.getValue().toString();
		}
		hashMap.put(oldvalue, refvalue);
		saveReplaceMap.put(key, hashMap);
		return refvalue;
	}
	private String getEnameCN(TxtDataVO tvo, HashMap enametoExcelCol,
			String ename,HashMap<String, String> enameMap) {
		if(null != enameMap.get(enameMap)){
			return enameMap.get(enameMap);
		}else{
			int j = Integer.parseInt(enametoExcelCol.get(ename).toString());
			HashMap<String, String> titlemap = tvo.getTitlemap();
			Set keySet = titlemap.keySet();
			String message = ename;
			Iterator iterator = keySet.iterator();
			while(iterator.hasNext()){
				Object key = iterator.next();
				if(Integer.valueOf(titlemap.get(key).toString())==j){
					message = key.toString();
					break;
				}
			}
			enameMap.put(ename, message);
			return message;
		}
	}
	public StringBuffer checkImportValue(DipDatadefinitBVO bvo,String value,StringBuffer sb){
		UFBoolean isnotnull=bvo.getIsimport();
		int length=bvo.getLength()==null?0:bvo.getLength();
		int deciplace=bvo.getDeciplace()==null?0:bvo.getDeciplace();
		if(isnotnull!=null&&isnotnull.booleanValue()){
			if(value==null||value.trim().equals("")){//为空
				sb.append("字段"+bvo.getCname()==null?"":bvo.getCname()+"不能为空,");
			}else{
				checkImportMethod(bvo, sb, value, length, deciplace);
			}
		}else{
			if(value!=null&&value.trim().length()>0){
				checkImportMethod(bvo, sb, value, length, deciplace);
			}
		}
		return sb;
	}
	public StringBuffer checkImportMethod(DipDatadefinitBVO bvo,StringBuffer sb,String value,int length,int deciplace){
		if(bvo.getType().equals("NUMBER")){
			String strs[];
			if(value.contains(".")){
				strs=value.split("\\.");
				if(strs.length==2){
					if(!isNumber(strs[0])||!isNumber(strs[1])){
						sb.append("字段"+(bvo.getCname()==null?"":bvo.getCname())+"必须是小数或者整数,");
					}else{
						while(strs[1].endsWith("0")){
							strs[1]=strs[1].substring(0, strs[1].length()-1);
						}
						if(strs[0].length()>(length-deciplace)||strs[1].length()>deciplace){
							sb.append("字段"+(bvo.getCname()==null?"":bvo.getCname())+"精度错误，必须满足number("+length+","+deciplace+"),");	
						}
					}
				}else{
					sb.append("字段"+(bvo.getCname()==null?"":bvo.getCname())+"必须是小数或者整数,");
				}
			}else{
				if(value!=null&&!value.trim().equals("")){
					strs=new String[]{value};
					if(!isNumber(strs[0])){
						sb.append("字段"+(bvo.getCname()==null?"":bvo.getCname())+"必须是小数或者整数,");
					}
				}
			}
		}else if(bvo.getType().equals("INTEGER")){
			if(!isNumber(value)){
				sb.append("字段"+(bvo.getCname()==null?"":bvo.getCname())+"必须是数字,");
			}
//			else{
//				if(value.length()>length){
//					sb.append(sb.append("字段"+bvo.getCname()==null?"":bvo.getCname()+"长度不能大于"+length));	
//				}
//			}
		}else{
			byte valuesbyte[]=value.getBytes();
			if(valuesbyte.length>length){
				sb.append("字段"+(bvo.getCname()==null?"":bvo.getCname())+"长度不能大于"+length+",");
			}
		}
		return sb;
	}
	

	//张进双 2011-6-14 判断设置里面有主键并设置小于外面的
	public int checkPk(String pk, String pk_defin){
		int i=0;
		String sql_ziduan = "select cname,ename from dip_auth_design where nvl(dr,0)=0 and pk_datadefinit_h = '"+pk+"' and designtype = '2' order by disno ";
		List list_ziduan = null;
		try {
			list_ziduan = queryfield.queryListMapSingleSql(sql_ziduan);
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
		for(int j=0;j<list_ziduan.size();j++){
			HashMap map_ziduan = (HashMap)list_ziduan.get(j);
			String ename = map_ziduan.get("ename".toUpperCase()).toString();
			if(ename.equals(pk_defin)){
				i = 1;
			}
		}
		return i;
	}

	//张进双  2011-6-14 根据数据定义主表主键和子表字段查询该字段的类型
	IUAPQueryBS iuap = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	public String checkType(String pk,String ename){
		String type = null;
		String sql = " select type from dip_datadefinit_b where nvl(dr,0)=0 and pk_datadefinit_h = '"+pk+"' and ename = '"+ename+"' ";
		try {
			DipDatadefinitBVO databvo = (DipDatadefinitBVO) iuap.executeQuery(sql, new BeanProcessor(DipDatadefinitBVO.class));
			type = databvo.getType();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return type;
	}

	//011-6-14 张进双 2判断类型是否匹配
	private int doTrance(Object value,String type) throws Exception{
		int i=0;
		if(value==null){
			return i; 
		}
		
		type=type.toLowerCase();
		if(type.contains("char")){
			String ret;
			//2011-6-23 cl 
			//if(value instanceof String) {
			if(value instanceof String || value instanceof Integer) {
				i=0;
			}else{
				i=1;
				return i;
			}
		}else if(type.equals("integer")||type.equals("smallint")){
			if(value instanceof Integer){
				i=0;
			}else{
				if (isDigit(value)){
					i= 0;
				}
				else {
					i=1;
					return i;
				}
			}
		}else if(type.equals("number")||type.equals("long")||type.equals("decimal")){
			if(value instanceof UFDouble||value instanceof Integer||value instanceof BigDecimal){
				i=0;
			}else{
				if(isNumber(value)){i=0;}
				else {
					i=1;
					return i;
				}
			}
		}else if(type.equals("date")){
			if(value instanceof String){
				if(value.toString().matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}")){
					i=0;
				}else{
					i=1;
					return i;
				}
			}else if(value instanceof UFDate){
				if(value.toString().matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}")){
					i=0;
				}else{
					i=1;
					return i;
				}
			}else if(value instanceof UFDateTime){
				if(value.toString().matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}")){
					i=0;
				}else{
					i=1;
					return i;
				} 
			}
		}
		return i;
	}
	/**
	 * 功能：判断是否是整
	 * */
	private boolean isDigit(Object value){
		try {
			String str=value.toString();
			int num=Integer.valueOf(str);//把字符串强制转换为数字
			return true;  //如果是数字，返回True
		} catch (Exception e) {
			return false; //如果抛出异常，返回False
		} 
	}
	/**
	 * 功能：判断是否是float
	 * */
	private boolean isNumber(Object value){
		try {

			String str=value.toString();
			UFDouble num = new UFDouble(str);
//			Number num=Number.valueOf(str);//把字符串强制转换为数字
			return true;  //如果是数字，返回True
		} catch (Exception e) {
			e.printStackTrace();
			return false; //如果抛出异常，返回False
		} 
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
						rst =cell.getNumericCellValue();
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
					rst = cell.getNumericCellValue();
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
	 * 判断系统里面是否有正准备创建的此表
	 * 在点击创建表按钮时判断：
	 * 1.如果有此表，查询表里是否有数据，如果有数据，给予提示，没有，则直接删掉，重新创建
	 * 2.没有此表，则直接创建
	 */
	public boolean isTableExist(String tableName){

		if(tableName==null){
			return false;
		}
		boolean isExist=false;//默认没有此表
		IUAPQueryBS queryBS=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String sql="select 1 from user_tables where table_name='"+tableName.toUpperCase()+"';";	
		try{
			ArrayList al=(ArrayList)queryBS.executeQuery(sql, new MapListProcessor());
			if(al.size()>=1){
				isExist=true;//有此表			
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return isExist;
	}
	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
		getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
		getSelfUI().updateButtonUI();
	}

	public class FileFilter extends javax.swing.filechooser.FileFilter{
		  public boolean accept(java.io.File f) {
		    if (f.isDirectory())return true;
		    return f.getName().endsWith(".xls");  //设置为选择以.class为后缀的文件
		  } 
		  public String getDescription(){
		    return ".xls";
		  }
	}
	
}