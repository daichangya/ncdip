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
 *������AbstractMyEventHandler�������ʵ���࣬
 *��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 *@author author
 *@version tempProject version
 */

public class MyEventHandler 
extends AbstractMyEventHandler {
	DataLookClientUI ui = (DataLookClientUI)this.getBillUI();
	String pkTableField = "";//���ݶ�������Ӣ������
	String sqlQuery = "";
	String whereCondition="";//��ѯʱ���where������
	String defwhere=" 1=1 ";
	String defDeleteAuthWhere=" 1=1 ";//Ȩ�޿���ʱ���ϼ�����ɾ���¼����ݡ�����������ж��Ƿ����¼����ݵ�sql���
	String delWhereCondition="";//ɾ��ʱ�����Ȩ�޿���ʱ�򣬲�ѯ�¼������ݵ�sql������ delWhereCondition ����defDeleteAuthWhere��delSql����delWhereCondition��
	String delSql="";//�����ɾ��У��ʱ���sql��䡣
	List<DipAuthDesignVO> listVO;
	boolean isdisplaypk=false;
	IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
	boolean isStartAuth=true;//��ʾ�Ƿ������ϼ��鿴�¼�Ȩ�ޣ��������أ�����ȡ�����Ȩ�ޡ�
	String isauthFlag="0";//0��ʾû������Ȩ������
	  //1��ʾû�����ò鿴ֱ���¼�Ȩ��
	  //2��ʾû�����ò鿴�����¼�Ȩ��
	Map <String,Map<String,String>> lowMap=new HashMap<String, Map<String,String>>();//�ڲ鿴�����¼���λ����ʱ�����ӻ��棬���map�Ǵ��¼��˾�������¼���λ��
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
	 * ȡ�õ�ǰUI��
	 * 
	 * @return
	 */
	private DataLookClientUI getSelfUI() {
		return (DataLookClientUI) getBillUI();
	}

	/**
	 * ȡ��ѡ�еĽڵ����
	 * 
	 * @return
	 */
	private VOTreeNode getSelectNode() {
		return getSelfUI().getBillTreeSelectNode();
	}
	/**
	 * �ķ��������кͲ��к󱻵��ã������ڸ÷�����Ϊ������������һЩĬ��ֵ
	 * 
	 * @param newBodyVO
	 * @return TODO
	 */
	protected CircularlyAccessibleValueObject processNewBodyVO(CircularlyAccessibleValueObject newBodyVO) {

		VOTreeNode parent = this.getSelectNode();

		newBodyVO.setAttributeValue("pk_datalook", parent == null ? null : parent.getNodeID());

		// ��ӽ���
		return newBodyVO;
	}

	/**
	 * TODO �����ڵ㱻ѡ��ʱ��ִ�и÷���
	 * 
	 * ����:��������������������
	 */
	public void onTreeSelected(VOTreeNode node) {
		try {
			// ��ȡȨVO
			DipADContdataVO vo = (DipADContdataVO) node.getData();
			if(vo.getContcolcode()!=null&&vo.getContcolcode().length()>0){
				String curuser=ClientEnvironment.getInstance().getUser().getPrimaryKey();
				String pkcorp=ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
				 isauthFlag="0";//0��ʾû������Ȩ������
									  //1��ʾû�����ò鿴ֱ���¼�Ȩ��
									  //2��ʾû�����ò鿴�����¼�Ȩ��
				if(isStartAuth&&vo.getDef_str_1()!=null&&!vo.getDef_str_1().trim().equals("")){
					if(vo.getDef_str_1().equals("ֱ���¼�")){
						isauthFlag="1";
					}
					if(vo.getDef_str_1().equals("�����¼�")){
						isauthFlag="2";
					}
				}
				if(pkcorp!=null&&!pkcorp.equals("0001")&&curuser!=null&&pkcorp.length()>0&&curuser.length()>0){
				String ss="";//��ѯ�����õġ�
				String ssdel="";//ɾ�������õġ�
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
			//��ѯ����ϵͳ�ֶ�
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
			//�޸Ľ����ϵı����ֶ�
			//��ѯ������ϵͳ�ֶ�
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
					((UIRefPane)bodyItem.getComponent()).setRefNodeName("��Ա��������");
				}
			}
			int j = 101;
			for(int i=1;i<=100;i++){
				if(listVO!=null&&listVO.size()>0&&listVO.size()>=i){
					DipAuthDesignVO bvo=listVO.get(i-1);
					String cname = bvo.getCname();
					UFBoolean ispk = new UFBoolean(bvo.getIspk()==null?false:bvo.getIspk().booleanValue());
					//2011-6-23 cl �޸�����������176--178
					if(ispk.booleanValue() ){
						ui.pkHiddleItemKey = "vdef"+String.valueOf(i);
						pkTableField =bvo.getEname();
					}


					ui.contFields.append(""+bvo.getEname()+",");
					
					//��Ƭ���棬���±�ͷ�ֶ�����
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
						MessageDialog.showErrorDlg(getBillUI(), "����", "ģ������ֶκ����ݶ����ֶβ�ƥ�䣬������ģ�壡");
						return;
					}
					bodyItem.setLength(bMap.get(bvo.getEname()));
					if(isRef){
						DipDatadefinitBVO dipDatadefinitBVO = dataBMap.get(bvo.getEname());
						if(null != dipDatadefinitBVO){
							String type = dipDatadefinitBVO.getType();
							if("DATE".equals(type)){
								((UIRefPane)bodyItem.getComponent()).setIsCustomDefined(false);
								((UIRefPane)bodyItem.getComponent()).setRefNodeName("����");
							}else{
								((UIRefPane)bodyItem.getComponent()).setRefNodeName("��Ա��������");
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
					if(DipAuthDesignVO.getDesigntype()==2){//����ģ������
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
					}else if(DipAuthDesignVO.getDesigntype()==0){//��ʾģ������
						if(FunctionUtil.isAutoIn(consvalue)){
							showAutoInMap.put(DipAuthDesignVO.getEname(), consvalue);
						}else{
							showFunctionMap.put(DipAuthDesignVO.getEname(), consvalue);
						}
					}else if(DipAuthDesignVO.getDesigntype()==3){//����ģ������
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
	 * �õ����е��¼���˾���ӹ�˾�Ĺ�˾����������1001 ���ӹ�˾��1002,1003,1004,1005�� ��1002���ӹ�˾ ��1010,1011����������˾��û���ӹ�˾���ͷ��أ�1002����
	 * ����1001 ���ӹ�˾��1002,1003�� ��1002���ӹ�˾ ��1010,1011����1010���ӹ�˾��1101,1102��������˾��û���ӹ�˾���ͷ��أ�1002��1010����
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
	
	
	//��������
	@Override

	protected void onDataClear(int intBtn) throws Exception {
		if(isAdd||isEdit){
			getSelfUI().showErrorMessage("���ڱ༭�У��뱣�棡");
			return;
		}
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			getSelfUI().showWarningMessage("��ѡ��ڵ㣡");
			return;
		}

		//String fnode=((DataLookClientUI)getBillUI()).selectnode;
		String pk=(String) node.getParentnodeID();
		String nn=(String) node.getNodeID();
		
		if(pk ==null || pk.trim().equals("")){
			getSelfUI().showWarningMessage("���ڵ㲻�ɱ༭��");
			return;
		}
		int flag = MessageDialog.showOkCancelDlg(ui, "����", "ȷ���Ƿ�����");
		if(flag==2){
			return;
		}
		
		DipADContdataVO vo1=(DipADContdataVO) node.getData();
		//�õ�������Ҫ�����ı������
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
			MessageDialog.showHintDlg(getSelfUI(), "�ɹ�", "����ɹ�");
		}else{
			getSelfUI().showWarningMessage("ѡ��ڵ��Ӧ�����ݶ����Ҳ���");
		}
		
	}
	boolean ishandaddpkdisplay=false;
	@Override
	protected void onDatalookQuery(int intBtn) throws Exception {
		if(isAdd||isEdit){
			getSelfUI().showErrorMessage("���ڱ༭�У��뱣�棡");
			return;
		}
		if(ui.contFields.toString()==null||ui.contFields.toString().length()<=0){
			getSelfUI().showErrorMessage("û�ж�����ʾ���ݣ�");
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
			getSelfUI().showErrorMessage("��ѡ��Ҫ��ѯ�Ľڵ㣡");
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
			if(dlg.getReturnSql()!=null&&dlg.getReturnSql().trim().length()>0){//���ز�ѯ�������ѯ�������
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
						throw new Exception(msg.substring(0,msg.indexOf("\""))+" �ֶ��ڱ��в�����");
					}else if(ex.getErrorCode()==936){
						throw new Exception("��ѯ�����������飡");
					}else if(ex.getErrorCode()==208){
						throw new Exception("�����ͼ������");
					}else{
						throw ex;
					}
				}else {
					throw ex;
				}
			}
			int count=Integer.parseInt(ssql);
			//��������������������ʾ������ѡ���ѯ���� 2011-7-5 cl
			if(count >ui.getCount()){
				MessageDialog.showHintDlg(getSelfUI(), "��ʾ", "��ǰ��ѯ�����������࣬�ᵼ��ϵͳЧ�ʱ����������ò�ѯ��������в�ѯ��");
				return;
			}
			//��ѯ���ձ��е�ֵ
			queryDisplayValueMethod(datadefinitVO.getMemorytable(),whereCondition,datadefinitVO.getPk_datadefinit_h());
		}
	}
	/**
	 * ���ݱ�����where������ѯ���ݣ�������˷�����Ϊ�������ӱ������ݺ󣬸�������������¼����ѯչʾ�����Ӽ�¼
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
						if(ss.equals("��")){
							l=SwingConstants.LEFT;
						}else if(ss.equals("��")){
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
			getSelfUI().showErrorMessage("��ѡ��Ҫ����Ľڵ�!");
			isreturn=true;
			return;
		}
		String pk=(String) treeNode.getParentnodeID();
		if(pk==null || "".equals(pk)){
			getSelfUI().showErrorMessage("ϵͳ�ڵ㲻�ܱ���!");
			isreturn=true;
			return;
			}
		
		DipADContdataVO vo1=(DipADContdataVO) treeNode.getData();
		//�õ�������Ҫ�����ı������
		String pkdetadefinith = vo1.getContabcode();
		DipDatadefinitHVO datadefinitVO = (DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, pkdetadefinith);
		int headcount = ui.getBillCardPanel().getBillModel().getRowCount();
		List list=new ArrayList();
		//��ձ������--start
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
//		��ձ������--end
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
		StringBuffer addpkArray=new StringBuffer("");//���ӱ���ʱ��Ԥ�Ƽ�¼����
		
		//�޸ı��湦��
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
				if(!"".equals(pkvalue) && rowState==BillModel.NORMAL){//δ�޸�����
					continue;
				}
				for(int j = 0;j<vdefs.length;j++){
					String value = ui.getBillCardPanel().getBodyValueAt(i, vdefs[j])==null?"":ui.getBillCardPanel().getBodyValueAt(i, vdefs[j]).toString();

					if("".equals(pkvalue) && ui.pkHiddleItemKey.equals(vdefs[j])){ //����
//						ui.showErrorMessage("��"+(i+1)+"������Ϊ�գ�");
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
								String message = "��"+(i+1)+"��"+name+"";
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
							String message = "��"+(i+1)+"��"+name+"";
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
					
					if("".equals(pkvalue)){//����ʱ���滻
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
			//���û����������
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
		if(addpkArray!=null&&addpkArray.toString().trim().length()>0){//���Ӻ��ѯ���
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
			getSelfUI().showErrorMessage("��ѡ��Ҫ�޸ĵĽڵ�!");
			return;
		}
		String pk=(String) treeNode.getParentnodeID();
		if(pk==null || "".equals(pk)){
			getSelfUI().showErrorMessage("ϵͳ�ڵ㲻���޸�!");
			return;
		}else{
			/*��ѯNCϵͳ�µĽڵ㣬���������޸Ĳ��� 2011-5-24 begin */
			/*	String sql="select h.pk_sysregister_h from dip_sysregister_h h where h.code='0000' and h.pk_sysregister_h='"+pk+"' and nvl(h.dr,0)=0";
			String isNC=iq.queryfield(sql);
			if(isNC !=null && !"".equals(isNC.trim())){
				getSelfUI().showWarningMessage("NCϵͳ�£�ֻ��������ݣ������޸ģ�"); 
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
		int flag = MessageDialog.showOkCancelDlg(ui, "ɾ��", "ȷ���Ƿ�ɾ��");
		if(flag==2){
			return;
		}
		IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
		queryfield.exesql(sql);
		//ɾ�����֮��ִ��ˢ�·���
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
	 * ����޸��޸ķ���
	 * 2011-5-24
	 * */
	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		VOTreeNode treeNode=getSelectNode();
		if(treeNode ==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ�޸ĵĽڵ�!");
			isreturn=true;
			return;
		}
		String pk=(String) treeNode.getParentnodeID();
		if(pk==null || "".equals(pk)){
			getSelfUI().showErrorMessage("ϵͳ�ڵ㲻���޸�!");
			isreturn=true;
			return;
		}else{
			/*��ѯNCϵͳ�µĽڵ㣬���������޸Ĳ��� 2011-5-24 begin */
			/*String sql="select h.pk_sysregister_h from dip_sysregister_h h where h.code='0000' and h.pk_sysregister_h='"+pk+"' and nvl(h.dr,0)=0";
			String isNC=iq.queryfield(sql);
			if(isNC !=null && !"".equals(isNC.trim())){
				getSelfUI().showWarningMessage("NCϵͳ�£�ֻ��������ݣ������޸ģ�"); 
				return;
			}*/
			/* end */
		}
		int headcount = ui.getBillCardPanel().getBillModel().getRowCount();
		if(headcount<=0){
			getSelfUI().showWarningMessage("û�����ݣ������޸ģ�");
			isreturn=true;
			return;
		}
		/*2011-6-15 begin*/
		String str=(String) treeNode.getNodeID();
		//��������Ȩ�޶���ڵ㣬�ҵ�Ȩ�޶��������ݶ�������
		String sqlCond=" pk_datadefinit_h=(select da.contabcode from dip_adcontdata da where da.pk_contdata_h=('"+str+"'))and nvl(dr,0)=0";
		
		DipDatadefinitBVO[] bvo=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, sqlCond);
		int c=0;
		//2011-6-23 cl ������ ��448��457--462�У��޸��˵�464��
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
			getSelfUI().showWarningMessage("û�������������޸ģ�");
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
			getSelfUI().showErrorMessage("��ѡ��Ҫ�޸ĵĽڵ�!");
			isreturn=true;
			return;
		}
		String pk=(String) treeNode.getParentnodeID();
		if(pk==null || "".equals(pk)){
			getSelfUI().showErrorMessage("ϵͳ�ڵ㲻���޸�!");
			isreturn=true;
			return;
			}
		if(ui.pkHiddleItemKey==null||ui.pkHiddleItemKey.equals("")){
				getSelfUI().showErrorMessage("���ݶ�����û���ҵ���������ɾ��!");
				return;
		}
		int bodycountArray[]=ui.getBillCardPanel().getBillTable().getSelectedRows();
		if(bodycountArray.length<1){
			getSelfUI().showErrorMessage("��ѡ��Ҫɾ���ļ�¼��");
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
			Object valueAt = ui.getBillCardPanel().getBillModel().getValueAt(bodycountArray[i], ui.statusHiddleItemKey);//������״̬
			if(null != valueAt && !"0".equals(valueAt)){
				statusMsg += "��"+(bodycountArray[i]+1)+"�в�����ɾ��\n";
			}
		}
		if(list.size()>0){
			bodypkArray=list.toArray(new String[0]);
		}
//		String bodypk = ui.getBillCardPanel().getBillModel().getValueAt(bodycount, field)==null?"":
//			ui.getBillCardPanel().getBillModel().getValueAt(bodycount, field).toString();
		if(bodypkArray==null||bodypkArray.length<=0){
			getBillUI().showErrorMessage("û���ҵ�ѡ���е�����ֵ������ɾ����");
			return;
		}
		if(!"".equals(statusMsg)){
			getBillUI().showErrorMessage(statusMsg);
			return;
		}
		int flag = MessageDialog.showOkCancelDlg(ui, "ɾ��", "ȷ���Ƿ�ɾ��ѡ���¼��");
		if(flag==2){
			return;
		}
		DipADContdataVO vo1=(DipADContdataVO) treeNode.getData();
		//�õ�������Ҫ�����ı������
		String pkdetadefinith = vo1.getContabcode();
		DipDatadefinitHVO datafinitVO = (DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, pkdetadefinith);
		if(datafinitVO==null&&datafinitVO.getMemorytable()==null||datafinitVO.getMemorytable().toString().trim().equals("")){
			getBillUI().showErrorMessage("ѡ��ڵ�����ݶ����Ҳ�����");
			return;
		}
		int count=0;
		StringBuffer delsb=new StringBuffer();
		List<String> delList=new ArrayList<String>();
		//�ϼ���ѯ�¼����ݣ�������ɾ���¼�����У��
		Map<String,String> lowMap=new HashMap<String, String>();//�¼����е�����map��key��������value������
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
		String is_Dr = (String) (datafinitVO.getIs_dr()==null?"N":datafinitVO.getIs_dr().toString());//�Ƿ��߼�ɾ��
		
		for(int i=0;i<bodypkArray.length;i++){
			if(lowMap!=null&&lowMap.size()>0){
				if(bodypkArray[i]!=null&&lowMap.get(bodypkArray[i])!=null){
					getBillUI().showErrorMessage("ѡ���¼�а����¼���λ�������ݣ�������ɾ��");
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
			//ɾ�����֮��ִ��ˢ�·���
		}
		if(delList.size()<=0){
			getBillUI().showErrorMessage("ɾ��ѡ���¼����ˢ�º��ڲ���");
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
		MessageDialog.showHintDlg(getSelfUI(), "�ɹ�", "�ɹ�ɾ��"+count+"������");
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
			getSelfUI().showErrorMessage("û�ж�����ʾ���ݣ�");
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
			getSelfUI().showErrorMessage("��ѡ��Ҫ�޸ĵĽڵ�!");
			return;
		}
//		String pk=(String) treeNode.getParentnodeID();
//		if(pk==null || "".equals(pk)){
//		getSelfUI().showErrorMessage("ϵͳ�ڵ㲻���޸�!");
//		return;
//		}else{
//		/*��ѯNCϵͳ�µĽڵ㣬���������޸Ĳ��� 2011-5-24 begin */
//		String sql="select h.pk_sysregister_h from dip_sysregister_h h where h.code='0000' and h.pk_sysregister_h='"+pk+"' and nvl(h.dr,0)=0";
//		String isNC=iq.queryfield(sql);
//		if(isNC !=null && !"".equals(isNC.trim())){
//		getSelfUI().showWarningMessage("NCϵͳ�£�ֻ��������ݣ������޸ģ�"); 
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
			getSelfUI().showErrorMessage("��ѡ��Ҫ����Ľڵ㣡");
			return ;
		}
		String str=(String)treeNode.getParentnodeID();
		DipADContdataVO vo=(DipADContdataVO) treeNode.getData();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("ϵͳ�ڵ㲻�������������"); 
			return;
		}
		int[] rows = ui.getBillCardPanel().getBillTable().getSelectedRows();
		if(rows.length==0){
			getSelfUI().showWarningMessage("���Ȳ�ѯ����Ҫ���������ݣ�"); 
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
				MessageDialog.showHintDlg(getSelfUI(), "��ʾ", "����  "+rowVO.getName()+"ִ����ɣ�");
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
	
//	//���ð�ť
//	private void onBoSet() {
//		VOTreeNode treeNode=getSelectNode();
//		if(treeNode==null){
//			getSelfUI().showErrorMessage("��ѡ��Ҫ����Ľڵ㣡");
//			return ;
//		}
//		String str=(String)treeNode.getParentnodeID();
//		DipADContdataVO vo=(DipADContdataVO) treeNode.getData();
//		if(str ==null || str.equals("")){
//			getSelfUI().showWarningMessage("ϵͳ�ڵ㲻�������������"); 
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
	//2011-6-13  �Ž�˫  ������������ĵ������õ������ݵĵ�����ť
	@Override
	protected void onBoExport() throws Exception {
		boolean expModel=false;//����ģ��
		// TODO Auto-generated method stub
		//super.onBoExport();
		wenjian=0;
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getSelfUI().showWarningMessage("��ѡ��Ҫ�����Ľڵ㣡");
			return;
		}
		String fpk=(String) node.getParentnodeID();
		if(SJUtil.isNull(fpk)||fpk.length()==0){
			this.getSelfUI().showWarningMessage("ϵͳ�ڵ㲻�ɱ༭��");
			return;
		}
		AskDLG adlg=new AskDLG(getSelfUI(),"��ʾ","ѡ����������������ͣ�",new String[]{"������ǰ��ѯ��Χ����","������������","����ģ��"});
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
				getSelfUI().showErrorMessage("��ѡ��Ҫ��ѯ�Ľڵ㣡");
				return ;
			}
			DipADContdataVO vo1=(DipADContdataVO) treeNode.getData();
			//�õ�������Ҫ�����ı������
			String pk = vo1.getContabcode();

			DipDatadefinitHVO datafinitVO = (DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, pk);
			//���ս���洢��
			String tablename = datafinitVO.getMemorytable();
			//�ҳ�����Ҫ����ֶδ�dip_auth_design�����ѯҪ�������ֶΣ�
			String sql_ziduan = "select cname,ename from dip_auth_design where nvl(dr,0)=0 and pk_datadefinit_h = '"+vo1.getPrimaryKey()+"' and designtype = '3' order by disno ";
		
			List list_ziduan = queryfield.queryListMapSingleSql(sql_ziduan);
			//Ҫ��ѯ���ֶ�ƴ�ӵ��ַ�
			String ziduan = "";
			String cname = "";
			if(list_ziduan==null||list_ziduan.size()<=0){
				this.getSelfUI().showErrorMessage("û�н��е������ã�����������");
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
				ExpToExcel toexcle = new ExpToExcel(filePath,"���ݶ��ս��",cname.substring(0,cname.length()-1),null,null);
				MessageDialog.showHintDlg(getSelfUI(), "�ɹ�", "�����ɹ�");
				return;
			}
			//ȥѡ�������ڵı���ȥ��ѯ���ݣ�
			//2011-7-1 ��������֤���Ƿ������ݿ��д���
			boolean isExist=isTableExist(tablename);
			if(isExist){
				
				String sql="select count(*) from "+tablename.toUpperCase()+" where "+(isdqfw?"1=1 "+whereCondition:defwhere+" and coalesce(dr,0)=0 ");
				String count=queryfield.queryfield(sql);
				int s=Integer.parseInt(count);
				if(s==0){
					getSelfUI().showErrorMessage("û�з������������ݣ�");
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
							sb.append(pp+"��");
							if(kkk==k-1){
								MessageDialog.showWarningDlg(this.getBillUI(), "��ʾ", "�������!�������ļ���"+sb.substring(0, sb.length()-1)+"!");
								return ;
							}
						}else{
							getSelfUI().showErrorMessage("��������");
        					return;
						}
					}
					
        			}else{
        				if(method(ziduan, tablename, path, ss, cname,isdqfw,pk)){
        					MessageDialog.showWarningDlg(this.getBillUI(), "��ʾ", "�������!");
        					return;
        				}else{
        					getSelfUI().showErrorMessage("��������");
        					return;
        				}
        			}
			}else{
				getSelfUI().showWarningMessage("�����ͼ�����ڣ�");
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
			ExpToExcel toexcle = new ExpToExcel(filePath,"����Ȩ�����",cname.substring(0,cname.length()-1),vosW,fielPathTemp);
			return true;
			//MessageDialog.showWarningDlg(this.getBillUI(), "��ʾ", "�������!");
		}else{
			getSelfUI().showErrorMessage("û�з������������ݣ�");
			return false;
		}
	}
	
	
	//	���밴ť �Ž�˫ 2011-6-14
	@Override
	protected void onBoImport() throws Exception {
		VOTreeNode treeNode=getSelectNode();
		if(treeNode==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ����Ľڵ㣡");
			return ;
		}
		String str=(String)treeNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("ϵͳ�ڵ㲻�������������"); 
			return;
		}
		if(treeNode==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ��ѯ�Ľڵ㣡");
			return ;
		}
		final DipADContdataVO vo1=(DipADContdataVO) treeNode.getData();
		String pk=vo1.getContabcode();
//		���ݶ���VO
		final DipDatadefinitHVO cvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, pk);
//		�ҳ�����Ҫ����ֶδ�dip_auth_design�����ѯҪ������ֶΣ�
		String sql_ziduan = "select cname,ename,consvalue from dip_auth_design where nvl(dr,0)=0 and pk_datadefinit_h = '"+vo1.getPrimaryKey()+"' and designtype = '2' order by disno ";
		DipDatadefinitBVO []datadefinitbvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "pk_datadefinit_h = '"+pk+"' and nvl(dr,0)=0 ");
		HashMap<String, DipDatadefinitBVO> databMap=new HashMap<String, DipDatadefinitBVO>();
		if(datadefinitbvos==null||datadefinitbvos.length==0){
			getSelfUI().showErrorMessage("�Ҳ�����"+cvo.getMemorytable()+"�����ݶ����ӱ�");
			return ;
		}
		for(int i=0;i<datadefinitbvos.length;i++){
			databMap.put(datadefinitbvos[i].getEname(), datadefinitbvos[i]);
		}
		List list_ziduan = queryfield.queryListMapSingleSql(sql_ziduan);
		//�����ֶ�
		String pk_defin = null;
		//Ҫ��ѯ���ֶ�ƴ�ӵ��ַ�
		String ziduan = "";
		String cname = "";
		if(list_ziduan==null||list_ziduan.size()<=0){
			this.getSelfUI().showErrorMessage("û�н��е������ã�����������");
			return;
		}
		List listimport=new ArrayList();
		List listAutoIn=new ArrayList(); //�Զ�������ݣ�������д�������Զ���亯��ֵ,Ŀǰֻ��3������˾�������û���������������
		List listFunction=new ArrayList();//�������������Զ���亯�������磬ת����Сд��ת���·ݺ���
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
//				dialog.setTitle("����ִ�����̣����Ժ�...");
//				dialog.setStartText("����ִ�����̣����Ժ�...");
//				}
//		}.start();
		FileInputStream fin=new FileInputStream(path);
		TxtDataVO tvo=new TxtDataVO();
		String titlenames="";
		try{
			HSSFWorkbook book = new HSSFWorkbook(fin);
			HSSFSheet sheet = book.getSheetAt(0);
			if(sheet == null){
				getSelfUI().showErrorMessage("�����ļ���ʽ����ȷ��");
				return;
			}
			tvo.setSheetName(book.getSheetName(0));
			tvo.setStartRow(sheet.getFirstRowNum());
			tvo.setStartCol(sheet.getLeftCol());
			tvo.setRowCount(sheet.getPhysicalNumberOfRows() - 1);//ȥ��������
			tvo.setColCount((short)sheet.getRow(sheet.getFirstRowNum()).getPhysicalNumberOfCells());
			tvo.setFirstDataRow(sheet.getFirstRowNum());
			tvo.setFirstDataCol(sheet.getLeftCol());

			HSSFRow titleRow = sheet.getRow(tvo.getStartRow());
			HashMap<String, String> titlemap = new HashMap<String, String>();
			for(short i=0;i<titleRow.getPhysicalNumberOfCells();i++){
				if(getCellValue(titleRow.getCell(i))==null||getCellValue(titleRow.getCell(i)).toString().equals("")){
					getSelfUI().showErrorMessage("�����excelͷ�����п��ֶΣ�");
					return;
				}
				String cellvalue=getCellValue(titleRow.getCell(i)).toString();
				if(titlemap.get(cellvalue)!=null){
					getSelfUI().showErrorMessage("�����excelͷ�������ظ��ֶΣ�");
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
//			getSelfUI().showErrorMessage("�������õ��ֶ��뵼���ļ��е��ֶβ�һ�£������½������û��޸��ļ���");
//			return;
//		}
		boolean fieldsFlag=true;//�����ֶζ���7
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
					getSelfUI().showErrorMessage("excel��û���ҵ�����������"+object+"��Ӧ���ֶΣ�");
					return;
				}
			}
		final String tablename = cvo.getMemorytable();
		if(tvo.getData()==null||tvo.getData().length==0){
			getSelfUI().showErrorMessage("excel�ļ�����Ϊ��");
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
				dialog.setTitle("���ڵ��룬���Ժ�...");
				dialog.setStartText("���ڵ��룬���Ժ�...");
				dialog.start();
				try {
					Thread.sleep(500); // �ȴ�1��
					importData(vo1, cvo, final_databMap, final_autoInMap, final_functionMap, final_path, final_tvo,
							final_titlenames, final_enametoExcelCol, tablename, 0, new ArrayList<RowDataVO>(),
							new ArrayList<String>());
				} catch (Exception e) {
					MessageDialog.showErrorDlg(getBillUI(), "����", e.getMessage());
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
			Iterator functionIt=autoInMap.keySet().iterator();//�����ֶ�
			StringBuffer sb=new StringBuffer();
			HashMap<String, String> autoIn = new HashMap<String, String>();
			//1.���滻����
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
						sb.append(ss.toString());//��ʾ�д�����Ϣ
					}else{
						Object object = functionMap.get(ename);
						if(object!=null && !object.toString().startsWith(FunctionUtil.SAVEREPLACEPK) && !object.toString().startsWith(FunctionUtil.SHOWREPLACEPK)){
//									˵��Ҫͨ�����淽���õ�����ֵ
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
					getSelfUI().showErrorMessage("��"+tablename+"���ݶ�����û���ҵ��ֶ�"+ename);
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
			String sheetNames[]=new String[]{"����Ȩ�����"};
			titlenames=titlenames+"����,";
			String fileNames[]=new String[]{titlenames.substring(0, titlenames.length()-1)};
			List<RowDataVO[]> list=new ArrayList<RowDataVO[]>();
			list.add(errList.toArray(new RowDataVO[0]));
			ExpToExcel1 excel=new ExpToExcel1(path,sheetNames,fileNames,list,null);
		 		 if(excel.create()){
		 			 getSelfUI().showWarningMessage("����ʧ�ܣ���鿴������Ϣ["+path+"]");
		 			 return;
		 		 }else{
		 			getSelfUI().showWarningMessage("����ʧ��");
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
						getSelfUI().showErrorMessage("�������ݴ���,����ΨһԼ���ظ�!");
						return;
					}else{
						getSelfUI().showErrorMessage("�������ݴ���,"+e.getMessage()+"!");
						return;
					}
				}
		}
		MessageDialog.showHintDlg(getSelfUI(), "��ʾ", "����"+insertcount+"�����ݣ�");
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
			if(value==null||value.trim().equals("")){//Ϊ��
				sb.append("�ֶ�"+bvo.getCname()==null?"":bvo.getCname()+"����Ϊ��,");
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
						sb.append("�ֶ�"+(bvo.getCname()==null?"":bvo.getCname())+"������С����������,");
					}else{
						while(strs[1].endsWith("0")){
							strs[1]=strs[1].substring(0, strs[1].length()-1);
						}
						if(strs[0].length()>(length-deciplace)||strs[1].length()>deciplace){
							sb.append("�ֶ�"+(bvo.getCname()==null?"":bvo.getCname())+"���ȴ��󣬱�������number("+length+","+deciplace+"),");	
						}
					}
				}else{
					sb.append("�ֶ�"+(bvo.getCname()==null?"":bvo.getCname())+"������С����������,");
				}
			}else{
				if(value!=null&&!value.trim().equals("")){
					strs=new String[]{value};
					if(!isNumber(strs[0])){
						sb.append("�ֶ�"+(bvo.getCname()==null?"":bvo.getCname())+"������С����������,");
					}
				}
			}
		}else if(bvo.getType().equals("INTEGER")){
			if(!isNumber(value)){
				sb.append("�ֶ�"+(bvo.getCname()==null?"":bvo.getCname())+"����������,");
			}
//			else{
//				if(value.length()>length){
//					sb.append(sb.append("�ֶ�"+bvo.getCname()==null?"":bvo.getCname()+"���Ȳ��ܴ���"+length));	
//				}
//			}
		}else{
			byte valuesbyte[]=value.getBytes();
			if(valuesbyte.length>length){
				sb.append("�ֶ�"+(bvo.getCname()==null?"":bvo.getCname())+"���Ȳ��ܴ���"+length+",");
			}
		}
		return sb;
	}
	

	//�Ž�˫ 2011-6-14 �ж���������������������С�������
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

	//�Ž�˫  2011-6-14 �������ݶ��������������ӱ��ֶβ�ѯ���ֶε�����
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

	//011-6-14 �Ž�˫ 2�ж������Ƿ�ƥ��
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
	 * ���ܣ��ж��Ƿ�����
	 * */
	private boolean isDigit(Object value){
		try {
			String str=value.toString();
			int num=Integer.valueOf(str);//���ַ���ǿ��ת��Ϊ����
			return true;  //��������֣�����True
		} catch (Exception e) {
			return false; //����׳��쳣������False
		} 
	}
	/**
	 * ���ܣ��ж��Ƿ���float
	 * */
	private boolean isNumber(Object value){
		try {

			String str=value.toString();
			UFDouble num = new UFDouble(str);
//			Number num=Number.valueOf(str);//���ַ���ǿ��ת��Ϊ����
			return true;  //��������֣�����True
		} catch (Exception e) {
			e.printStackTrace();
			return false; //����׳��쳣������False
		} 
	}
	private Object getCellValue(HSSFCell cell) {
		if(cell==null){
			return null;
		}
		Object rst = null;
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_BOOLEAN:
			// �õ�Boolean����ķ���
			System.out.print(cell.getBooleanCellValue() + " ");
			rst = new UFBoolean(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			String value = Double.toString(cell.getNumericCellValue());
			//2011-6-23 cl �����˵�1166--1183��
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
			// ��ȡ��ʽ
			rst = cell.getCellFormula();
			break;
		case HSSFCell.CELL_TYPE_STRING:
			// ��ȡString
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
	 * �ж�ϵͳ�����Ƿ�����׼�������Ĵ˱�
	 * �ڵ��������ťʱ�жϣ�
	 * 1.����д˱���ѯ�����Ƿ������ݣ���������ݣ�������ʾ��û�У���ֱ��ɾ�������´���
	 * 2.û�д˱���ֱ�Ӵ���
	 */
	public boolean isTableExist(String tableName){

		if(tableName==null){
			return false;
		}
		boolean isExist=false;//Ĭ��û�д˱�
		IUAPQueryBS queryBS=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String sql="select 1 from user_tables where table_name='"+tableName.toUpperCase()+"';";	
		try{
			ArrayList al=(ArrayList)queryBS.executeQuery(sql, new MapListProcessor());
			if(al.size()>=1){
				isExist=true;//�д˱�			
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
		    return f.getName().endsWith(".xls");  //����Ϊѡ����.classΪ��׺���ļ�
		  } 
		  public String getDescription(){
		    return ".xls";
		  }
	}
	
}