package nc.ui.dip.datalook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
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
import nc.bs.framework.exception.ComponentException;
import nc.itf.dip.pub.IQueryField;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.datalookquery.DatalookQueryDlg;
import nc.ui.dip.dlg.AskDLG;
import nc.ui.dip.dlg.design.DataDesignDLG;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.itemconverters.IntegerConverter;
import nc.ui.pub.bill.itemconverters.StringConverter;
import nc.ui.pub.bill.itemconverters.UFDoubleConverter;
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
import nc.vo.dip.datadefcheck.DipDatadefinitCVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.datalook.DipDatalookVO;
import nc.vo.dip.datalook.DipDesignVO;
import nc.vo.dip.in.RowDataVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
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
	StringBuffer contFields = new StringBuffer();
	StringBuffer vdef = new StringBuffer();
	String field = "";
	String efield = "";
	String sqlQuery = "";
	String whereCondition="";
	List<DipDesignVO> listVO;
	HashMap<String,String> functionMap=new HashMap<String,String>();
	HashMap<String,String> autoInMap=new HashMap<String,String>();
	HashMap<String,String> updateAutoInMap=new HashMap<String,String>();
	HashMap<String, DatalookQueryDlg> dlgMap = new HashMap<String, DatalookQueryDlg>();
	public HashMap<String, DatalookQueryDlg> getDlgMap() {
		return dlgMap;
	}
	public void setDlgMap(HashMap<String, DatalookQueryDlg> dlgMap) {
		this.dlgMap = dlgMap;
	}
	DatalookQueryDlg dlg = null;
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
			SuperVO vo = (SuperVO) node.getData();

			String pk_datalook = (String) vo.getAttributeValue("pk_datalook");

			SuperVO[] vos = HYPubBO_Client.queryByCondition(nc.vo.dip.datalook.DipDatalookVO.class, "pk_datalook='" + pk_datalook + "' and isnull(dr,0)=0 ");

			nc.vo.dip.datalook.MyBillVO billvo = new nc.vo.dip.datalook.MyBillVO();

			// 设置子VO
			billvo.setChildrenVO(vos);

			// 显示数据
			getBufferData().addVOToBuffer(billvo);

			String sql = "";
			//查询对照系统字段
			sql = "select * from dip_design d where d.designtype=0 and d.pk_datadefinit_h='"+ui.selectnode+"' order by d.disno";

			IUAPQueryBS querybs = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			
			DipDatadefinitBVO[] definitPKvo=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, " pk_datadefinit_h='"+ui.selectnode+"' and nvl(dr,0)=0 ");
			HashMap<String, Integer> bMap = new HashMap<String, Integer>();
			for (DipDatadefinitBVO bvo : definitPKvo) {
				bMap.put(bvo.getEname(), bvo.getLength());
			}
			
			List<DipDesignVO> listVO = (ArrayList<DipDesignVO>)querybs.executeQuery(sql, new BeanListProcessor(DipDesignVO.class));
			this.listVO=listVO;
			//修改界面上的表体字段
			//查询被对照系统字段
			StringBuffer strFileds = new StringBuffer();
			field="";
			efield="";
			contFields=new StringBuffer();
			vdef=new StringBuffer();
			for(int i=1;i<=100;i++){
				if(listVO!=null&&listVO.size()>0&&listVO.size()>=i){
					DipDesignVO bvo=listVO.get(i-1);
					String cname = bvo.getCname();
					UFBoolean ispk = new UFBoolean(bvo.getIspk()==null?false:bvo.getIspk().booleanValue());
					//2011-6-23 cl 修改了如下内容176--178
					if(ispk.booleanValue() ){
						field = "vdef"+String.valueOf(i);
						efield =bvo.getEname();
					}


					contFields.append(""+bvo.getEname()+",");
					vdef.append("vdef"+String.valueOf(i)+",");
					//卡片界面，更新表头字段名称
					String key="vdef"+String.valueOf(i);
					ui.getBillCardPanel().getBodyItem(key).setEdit(true);
					ui.getBillCardPanel().getBodyItem(key).setName(cname);
					ui.getBillCardPanel().getBodyItem(key).setShow(true);
					if(bvo.getEname().equals("P_STATUS")){
						ui.statusHiddleItemKey = key;
					}
					if(bvo.getDesignlen()==0){
						ui.getBillCardPanel().getBodyItem(key).setShow(false);
					}
					ui.getBillCardPanel().getBodyItem(key).setWidth(bvo.getDesignlen());
					if(bMap.get(bvo.getEname())==null){
						MessageDialog.showErrorDlg(getBillUI(), "错误", "模板相关字段和数据定义字段不匹配，请重置模板！");
						return;
					}
					ui.getBillCardPanel().getBodyItem(key).setLength(bMap.get(bvo.getEname()));
					if(bvo.getDatatype()!=null&&(",BINARY_DOUBLE,BINARY_FLOAT,INTEGER,INTERVAL,LONG,LONGRAW,NUMBER,".indexOf(bvo.getDatatype().toUpperCase())>=0)){
						if(null != bvo.getDesigplen() && bvo.getDesigplen()>0){
							ui.getBillCardPanel().getBodyItem(key).setDataType(BillItem.DECIMAL);
							ui.getBillCardPanel().getBodyItem(key).setDecimalDigits(bvo.getDesigplen());
							ui.getBillCardPanel().getBodyItem(key).setConverter(new UFDoubleConverter(bvo.getDesigplen()));
							ui.getBillCardPanel().getBodyItem(key).setTatol(true);
						}else{
							ui.getBillCardPanel().getBodyItem(key).setDataType(BillItem.INTEGER);
							ui.getBillCardPanel().getBodyItem(key).setConverter(new IntegerConverter());
							ui.getBillCardPanel().getBodyItem(key).setTatol(true);
						}
					}else{
						ui.getBillCardPanel().getBodyItem(key).setDataType(BillItem.STRING);
						ui.getBillCardPanel().getBodyItem(key).setConverter(new StringConverter());
					}
					strFileds.append("vdef"+String.valueOf(i)+",");
				}else{
					ui.getBillCardPanel().getBodyItem("vdef"+String.valueOf(i)).setShow(false);
				}
			}
			if(!"".equals(strFileds.toString())){
				String strField [] = strFileds.toString().split(",");
				if(strField.length>0){
					for(int i = strField.length;i<100-strField.length;i++){
						ui.getBillCardPanel().getBodyItem("vdef"+String.valueOf(i+1)).setShow(false);
					}

				}
			}

			ui.getBillCardPanel().setBillData(ui.getBillCardPanel().getBillData());
			autoInMap.clear();
			functionMap.clear();
			updateAutoInMap.clear();
			DipDesignVO[] dipDesignVOs = (DipDesignVO[])HYPubBO_Client.queryByCondition(DipDesignVO.class, "nvl(dr,0)=0 and pk_datadefinit_h = '"
					+ui.selectnode
					+"' and designtype = '2' and consvalue is not null ");
			if(null != dipDesignVOs && dipDesignVOs.length>0){
				for (DipDesignVO dipDesignVO : dipDesignVOs) {
					if(FunctionUtil.isAutoIn(dipDesignVO.getConsvalue())){
						autoInMap.put(dipDesignVO.getEname(), dipDesignVO.getConsvalue());
					}else if(FunctionUtil.isUpdateAutoIn(dipDesignVO.getConsvalue())){
						updateAutoInMap.put(dipDesignVO.getEname(), dipDesignVO.getConsvalue());
					}else{
						functionMap.put(dipDesignVO.getEname(), dipDesignVO.getConsvalue());
					}
				}
			}
//			BillItem it=ui.getBillCardPanel().getBodyItem("vdef1");
//			if(it!=null){
//				ui.getBillCardPanel().getBillTable().getColumnModel().getColumn(0).setCellRenderer(new DefBillTableCellRender(it,new BillRendererVO(),SwingConstants.RIGHT));
//			}

		} catch (ComponentException e) {
			e.printStackTrace();
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	/* protected TableCellRenderer getDefaultCellRenderer(Column dataSetColumn)
	  {
	    int type = dataSetColumn.getDataType();

	    if ((type > 1) && (type < 17) && (type != 11))
	    {
	      if (type == 12)
	      {
	        return new RQTable.TableImageRenderer(this);
	      }
	      RQTable.RptTableFastStringRenderer renderer = new RQTable.RptTableFastStringRenderer(this);

	      if (dataSetColumn.getForeground() != null) {
	        renderer.setDefaultForeground(dataSetColumn.getForeground());
	      }

	      if (dataSetColumn.getBackground() != null) {
	        renderer.setDefaultBackground(dataSetColumn.getBackground());
	      }

	      if (dataSetColumn.getFont() != null) {
	        renderer.setDefaultFont(dataSetColumn.getFont());
	      }
	      renderer.setDefaultAlignment(dataSetColumn.getAlignment());

	      return renderer;
	    }

	    TableCellRenderer renderer = getDefaultRenderer(RptTableModel.getJavaClass(dataSetColumn.getDataType()));

	    if ((renderer instanceof DefaultTableCellRenderer)) {
	      DefaultTableCellRenderer cellRenderer = (DefaultTableCellRenderer)renderer;
	      if (dataSetColumn.getForeground() != null) {
	        cellRenderer.setForeground(dataSetColumn.getForeground());
	      }
	      if (dataSetColumn.getBackground() != null) {
	        cellRenderer.setBackground(dataSetColumn.getBackground());
	      }
	      if (dataSetColumn.getFont() != null) {
	        cellRenderer.setFont(dataSetColumn.getFont());
	      }
	      cellRenderer.setHorizontalAlignment(DBUtilities.convertJBCLToSwingAlignment(dataSetColumn.getAlignment(), true));

	      cellRenderer.setVerticalAlignment(DBUtilities.convertJBCLToSwingAlignment(dataSetColumn.getAlignment(), false));

	      return cellRenderer;
	    }

	    return renderer;
	  }*/
	
	
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
		int flag = MessageDialog.showOkCancelDlg(ui, "清理", "确定是否删除表中所有数据！");
		if(flag==2){
			return;
		}
		// TODO Auto-generated method stub
		int row=getBillCardPanelWrapper().getBillCardPanel().getRowCount();
		if(row>0){
			for(int i=0;i<row;i++){
				getBufferData().setCurrentRow(0);
				getBillCardPanelWrapper().getBillCardPanel().delLine();
			}
		}
//		ui.getBillCardPanel().getBillModel().addLine();
//		super.onDataClear(intBtn);


		DipDatadefinitHVO dfvo= (DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, nn);
		String tname=dfvo.getMemorytable();
		String sql="delete from "+tname;
		IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		iq.exesql(sql);
//		this.onBoRefresh();
	}

	@Override
	protected void onDatalookQuery(int intBtn) throws Exception {
		if(isAdd||isEdit){
			getSelfUI().showErrorMessage("正在编辑中，请保存！");
			return;
		}
		if(contFields.toString()==null||contFields.toString().length()<=0){
			getSelfUI().showErrorMessage("没有定义显示数据！");
			return;
		}
//		super.onBoQuery();
		super.onDatalookQuery(intBtn);
		HashMap map = new HashMap();	

		map.put("pk_datadefinit_h", ui.selectnode);
		if(null != getDlgMap().get(ui.selectnode)){
			dlg = getDlgMap().get(ui.selectnode);
		}else{
			dlg = new DatalookQueryDlg(this.getBillUI(),new UFBoolean(false),ui.selectnode);
			getDlgMap().put(ui.selectnode, dlg);
		}
		dlg.show();
		if(dlg.getOnBtnSave()==1){
			String sql = "";

			DipDatadefinitHVO datadefinitVO = (DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, ui.selectnode);


			IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
			String csql="select count(*) c from "+datadefinitVO.getMemorytable()+" where 1=1 "+dlg.getReturnSql();
			String ssql="";
			try{
				ssql=queryfield.queryfield(csql);
			}catch(SQLException ex){
				System.out.println("["+ex.getSQLState()+"]");
				System.out.println("["+ex.getErrorCode()+"]");
				System.out.println("["+ex.getMessage()+"]");
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
			sql = "select ";
			if(!"".equals(contFields.toString())){
				sql = sql+""+contFields.toString().substring(0, contFields.toString().length()-1)+" from "+datadefinitVO.getMemorytable()+" where 1=1 "+dlg.getReturnSql();
				whereCondition=dlg.getReturnSql();
			}
		
			sqlQuery = sql;
			List listValue = queryfield.queryListMapSingleSql(sql);
			String contField [] = contFields.toString().split(",");
			ui.getBillCardPanel().getBillModel().clearBodyData();
			int k = 0;
			for(int i=1;i<=100;i++){
				String key="vdef"+i;
				BillItem it=ui.getBillCardPanel().getBodyItem(key);
				if(listVO!=null&&listVO.size()>0&&listVO.size()>=i){
					DipDesignVO lvo=listVO.get(i-1);
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
				}
			}

			ui.getBillCardPanel().setTatolRowShow(true);
			if(listValue!=null && listValue.size()>0){

				for(int i = 0;i<listValue.size();i++){
					HashMap mapValue = (HashMap)listValue.get(i);
					ui.getBillCardPanel().getBillModel().addLine();
					DipDatalookVO vo=new DipDatalookVO();
					int c = 0;
					for(int j = 0;j<contField.length;j++){
						String upperCase = contField[j].toUpperCase();
						Object value=mapValue.get(upperCase);
						String string = functionMap.get(upperCase);
						if(null != string && string.startsWith(FunctionUtil.SHOWREPLACEPK)){
							int indexOf = string.indexOf("(");
							String substring = string.substring(indexOf+1, string.length()-1);
							String[] split = substring.split(",");
							if(null != split && split.length==2){
								if(null != mapValue.get(split[0])){
									RetMessage ret=FunctionUtil.getShowReplaceFuctionValue(split[0],mapValue.get(split[0]).toString(), 
											split[1],ui.selectnode);
									if(ret.getIssucc()){
										value=ret.getValue().toString();
									}
								}
							}
						}
						vo.setAttributeValue("vdef"+String.valueOf(j+1), value);
						ui.getBillCardPanel().getBillModel().setValueAt(value, i, "vdef"+String.valueOf(j+1));
						BillItem bodyItem = ui.getBillCardPanel().getBodyItem( "vdef"+String.valueOf(j+1));
						if(bodyItem!=null&&bodyItem.isShow()){
							ui.getBillCardPanel().getBillTable().getCellRenderer(i, c).getTableCellRendererComponent(ui.getBillCardPanel().getBillTable(), value, false, false, i, c);
							c++;
						}
					}
//					ui.getBillCardPanel().getBillModel().setBodyRowVO(vo, i);
				}
			}
			
//			ui.getBillCardPanel().setBillData(ui.getBillCardPanel().getBillData());

//			ui.getBillCardPanel().getBillModel().setEditRow(1);
			
		}

	}
	boolean isAdd=false;
	boolean isEdit=false;
	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		
		DipDatadefinitHVO datadefinitVO = (DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, ui.selectnode);
		List<String> listsqls=new ArrayList<String>();
		int headcount = ui.getBillCardPanel().getBillModel().getRowCount();
		List list=new ArrayList();
		for(int i=0;i<headcount;i++){
			String vdefs [] = vdef.toString().split(",");
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
		headcount = ui.getBillCardPanel().getBillModel().getRowCount();
		
		String sql = " ";
		String pkvalue = "";

		IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
		//修改保存功能
		String contField [] = contFields.toString().split(",");
		HashMap<String, Integer> contMap = new HashMap<String, Integer>();
		for (int i = 0; i < contField.length; i++) {
			contMap.put(contField[i], i);
		}
		HashMap<String, DipDatadefinitCVO[]> checkMap = DataCheckUtil.getDataCheckMap(ui.selectnode);
		for(int i = 0;i<headcount;i++){
			StringBuffer insertField = new StringBuffer();
			StringBuffer insertFieldvalue = new StringBuffer(); 
			String strFiled = "";
			sql = " ";
			String p_status = "";
			if(!"".equals(vdef.toString())){
				String vdefs [] = vdef.toString().split(",");
				pkvalue = ui.getBillCardPanel().getBodyValueAt(i, field)==null?"":ui.getBillCardPanel().getBodyValueAt(i, field).toString();
				p_status = ui.getBillCardPanel().getBodyValueAt(i, ui.statusHiddleItemKey)==null?"":ui.getBillCardPanel().getBodyValueAt(i, ui.statusHiddleItemKey).toString();
				for(int j = 0;j<vdefs.length;j++){
					String value = ui.getBillCardPanel().getBodyValueAt(i, vdefs[j])==null?"":ui.getBillCardPanel().getBodyValueAt(i, vdefs[j]).toString();
					if("".equals(pkvalue) && field.equals(vdefs[j])){ //主键
//						ui.showErrorMessage("第"+(i+1)+"行主键为空！");
//						return;
						value = queryfield.getOID();
					}
					//datacheck
					if(null !=checkMap.get(contField[j])){
						String name = ui.getBillCardPanel().getBodyItem(vdefs[j]).getName();
						String message = "第"+(i+1)+"行"+name+"";
						String checkDataMsg = DataCheckUtil.checkData(contField[j], value,null);
						if(null != checkDataMsg){
							getSelfUI().showErrorMessage(message+"  "+checkDataMsg);
							return;
						}
					}
					
					insertField.append(contField[j]+",");
					
					if("".equals(pkvalue)){//插入时才替换
						if(null != autoInMap.get(contField[j]) && !field.equals(vdefs[j])){
							value = FunctionUtil.getFuctionValue(autoInMap.get(contField[j]));
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
								RetMessage ret=FunctionUtil.getSaveReplaceFuctionValue(split[0].toUpperCase(),fieldvalue, 
										split[1],ui.selectnode);
								if(ret.getIssucc()){
									value=ret.getValue().toString();
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
			if("".equals(pkvalue)||isAdd){
				String field = insertField.toString().substring(0,insertField.toString().length()-1);
				String fieldvalue = insertFieldvalue.toString().substring(0,insertFieldvalue.toString().length()-1);
				sql = "insert into "+datadefinitVO.getMemorytable()+"("+field+") " +" values("+fieldvalue+")";
				queryfield.exesql(sql);

			}else{
				if(!"0".equals(p_status)){
					
				}else{
					sql = "update "+datadefinitVO.getMemorytable() + " set ";
					strFiled = strFiled.substring(0, strFiled.length()-1);
					sql = sql + strFiled+ " where "+efield+"='" + pkvalue + "'";
					queryfield.exesql(sql);
				}
			}

		}
//		if(listsqls!=null&&listsqls.size()>0){
//			queryfield.exectEverySql(sql);
//		}
		ui.setBillOperate(2);
		isAdd=false;
		isEdit=false;

		//2011-6-23 cl
		getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(true);
		getSelfUI().getButtonManager().getButton(IBtnDefine.DATALOOKQUERY).setEnabled(true);
		getSelfUI().getButtonManager().getButton(IBillButton.ImportBill).setEnabled(true);
		getSelfUI().getButtonManager().getButton(IBillButton.ExportBill).setEnabled(true);
		getSelfUI().updateButtons();

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
	IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	boolean isreturn=false;
	/*
	 * 添加修改修改方法
	 * 2011-5-24
	 * */
	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		if(contFields.toString()==null||contFields.toString().length()<=0){
			getSelfUI().showErrorMessage("没有定义显示数据！");
			return;
		}
		int count=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
		if(count<=0){
			getSelfUI().showErrorMessage("界面为空不能修改!");
			return;
		}
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

		/*2011-6-15 begin*/
		String str=(String) treeNode.getNodeID();
		DipDatadefinitBVO[] bvo=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "pk_datadefinit_h='"+str+"' and nvl(dr,0)=0");
		int c=0;
		//2011-6-23 cl 增加了 第448、457--462行，修改了第464行
		int s=0;
		for(int i=0;i<bvo.length;i++){
			UFBoolean ispk=bvo[i].getIspk();
			if(ispk!=null){
				if(ispk.booleanValue()){
					c++;
				}
			}

			UFBoolean issyspk=bvo[i].getIssyspk();
			if(issyspk !=null){
				if(issyspk.booleanValue()){
					s++;
				}
			}
		}
		if(c<=0 && s<=0){
			getSelfUI().showWarningMessage("没有主键，不能修改！");
			isreturn=true;
			return;
		}
		/*end*/
		super.onBoEdit();
//		onBoLineAdd();
		isEdit=true;

		//2011-6-23 cl
		getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
		getSelfUI().getButtonManager().getButton(IBtnDefine.DATALOOKQUERY).setEnabled(false);
		getSelfUI().getButtonManager().getButton(IBtnDefine.DELETE).setEnabled(false);
		getSelfUI().getButtonManager().getButton(IBillButton.ImportBill).setEnabled(false);
		getSelfUI().getButtonManager().getButton(IBillButton.ExportBill).setEnabled(false);
		getSelfUI().updateButtons();
	}
	@Override
	protected void onBoLineDel() throws Exception {
		// TODO Auto-generated method stub
		int bodycount = ui.getBillCardPanel().getBillTable().getSelectedRow();
		if(bodycount<0){
			getSelfUI().showErrorMessage("请选择要删除的记录！");
			return;
		}
		String bodypk = ui.getBillCardPanel().getBillModel().getValueAt(bodycount, field)==null?"":
			ui.getBillCardPanel().getBillModel().getValueAt(bodycount, field).toString();
		if(bodypk==null||bodypk.length()<=0){
			getBillUI().showErrorMessage("没有主键，不能删除！");
			return;
		}
		Object valueAt = ui.getBillCardPanel().getBillModel().getValueAt(bodycount, ui.statusHiddleItemKey);//行审批状态
		if(null != valueAt && !"0".equals(valueAt)){
			getBillUI().showErrorMessage("不允许删除！");
			return;
		}
		int flag = MessageDialog.showOkCancelDlg(ui, "删除", "确定是否删除选中记录！");
		if(flag==2){
			return;
		}
		DipDatadefinitHVO datadefinitVO = (DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, ui.selectnode);

//		String bodypk = ui.getBillCardPanel().getBillModel().getValueAt(bodycount, field)==null?"":
//			ui.getBillCardPanel().getBillModel().getValueAt(bodycount, field).toString();

		String sql = "delete from  "+datadefinitVO.getMemorytable() + " where "+efield+"='"+bodypk+"'";
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
					String upperCase = contField[j].toUpperCase();
					Object value=mapValue.get(upperCase);
					String string = functionMap.get(upperCase);
					if(null != string && string.startsWith(FunctionUtil.SHOWREPLACEPK)){
						int indexOf = string.indexOf("(");
						String substring = string.substring(indexOf+1, string.length()-1);
						String[] split = substring.split(",");
						if(null != split && split.length==2){
							if(null != mapValue.get(split[0])){
								RetMessage ret=FunctionUtil.getShowReplaceFuctionValue(split[0],mapValue.get(split[0]).toString(), 
										split[1],ui.selectnode);
								if(ret.getIssucc()){
									value=ret.getValue().toString();
								}
							}
						}
					}
					ui.getBillCardPanel().getBillModel().setValueAt(value, i, "vdef"+String.valueOf(j+1));
				}
			}
		}
		ui.getBillCardPanel().setBillData(ui.getBillCardPanel().getBillData());
		ui.getBillCardPanel().getBillModel().setEditRow(1);
		
	}
	

	@Override
	public void onBoAdd(ButtonObject arg0) throws Exception {
		if(contFields.toString()==null||contFields.toString().length()<=0){
			getSelfUI().showErrorMessage("没有定义显示数据！");
			return;
		}
		isreturn=false;
		int count=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
		if(count>0){
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().clearBodyData();
		}
		super.onBoLineAdd();
		super.onBoEdit();
//		if(!isreturn){
			isAdd=true;
//		}
			getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
			getSelfUI().getButtonManager().getButton(IBtnDefine.DATALOOKQUERY).setEnabled(false);
			getSelfUI().getButtonManager().getButton(IBtnDefine.DELETE).setEnabled(false);
			getSelfUI().getButtonManager().getButton(IBillButton.ImportBill).setEnabled(false);
			getSelfUI().getButtonManager().getButton(IBillButton.ExportBill).setEnabled(false);
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
		getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(true);
		getSelfUI().getButtonManager().getButton(IBtnDefine.DATALOOKQUERY).setEnabled(true);
		getSelfUI().getButtonManager().getButton(IBtnDefine.DELETE).setEnabled(true);
		getSelfUI().getButtonManager().getButton(IBillButton.ImportBill).setEnabled(true);
		getSelfUI().getButtonManager().getButton(IBillButton.ExportBill).setEnabled(true);
		getSelfUI().updateButtons();
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
		case IBtnDefine.SET:
			onBoSet();
		}
	}
	//设置按钮
	private void onBoSet() throws UifException {
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
		String pktab=treeNode.getNodeID().toString();
		boolean hspk=false;
		String pk="";
		if(pktab!=null&&pktab.length()>0){
			DipDatadefinitBVO[] bvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "pk_datadefinit_h='"+pktab+"' and ispk='Y' and nvl(dr,0)=0");
			if(bvos!=null&&bvos.length>0){
				hspk=true;
				pk=bvos[0].getPrimaryKey();
			}
		}
		if(hspk){
			DataDesignDLG dlg=new DataDesignDLG(getSelfUI(),new String[]{treeNode.getNodeID().toString()},new String[]{pk});
			dlg.showModal();
		}else{
			DataDesignDLG dlg=new DataDesignDLG(getSelfUI(),new String[]{treeNode.getNodeID().toString()});
			dlg.showModal();
		}
	}

	IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
	boolean fenye=false;
	int wenjian=0;
	//2011-6-13  张进双  根据设置里面的导出设置导出数据的导出按钮
	@Override
	protected void onBoExport() throws Exception {
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
		AskDLG adlg=new AskDLG(getSelfUI(),"提示","选择数据浏览导出类型？",new String[]{"导出当前查询范围数据","导出所有数据"});
		adlg.showModal();
		boolean isdqfw=false;
		if(adlg.getRes()==0){
			isdqfw=true;
		}else if(adlg.getRes()==1){
			isdqfw=false;
		}else{
			return;
		}

		String condsql=null;
		String path=null;
		try {
			

			//得到树上需要导出的表的主键
			String pk = (String) node.getNodeID();

			DipDatadefinitHVO datafinitVO = (DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, ui.selectnode);
			//对照结果存储表
			String tablename = datafinitVO.getMemorytable();
			//找出所有要查的字段从dip_design里面查询要导出的字段；
			String sql_ziduan = "select cname,ename from dip_design where nvl(dr,0)=0 and pk_datadefinit_h = '"+pk+"' and designtype = '3' order by disno ";
		
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

			//去选择的相对于的表里去查询数据；
			//2011-7-1 增加了验证表是否在数据库中存在
			boolean isExist=isTableExist(tablename);
			if(isExist){
				
				String sql="select count(*) from "+tablename.toUpperCase()+(isdqfw?" where 1=1 "+whereCondition:"");
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
						if(method(ziduan, tablename, pp, ss, cname,isdqfw)){
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
        				if(method(ziduan, tablename, path, ss, cname,isdqfw)){
        					MessageDialog.showWarningDlg(this.getBillUI(), "提示", "导出完成!");
        					return;
        				}else{
        					getSelfUI().showErrorMessage("导出错误！");
        					return;
        				}
        			}
//					String sql_session = "select "+ziduan.substring(0, ziduan.length()-1)+" from "+tablename.toUpperCase()+"";
//					List list = queryfield.queryListMapSingleSql(sql_session);
//					ExpExcelVO[] vosW = null;
//					if(list!=null && list.size()>0){
//						List<ExpExcelVO> listW = new ArrayList<ExpExcelVO>();
//						for(int i = 0;i<list.size();i++){
//							HashMap map = (HashMap)list.get(i);
//							ExpExcelVO	vo = new ExpExcelVO();
//
//							for(int j = 0;j<ss.length;j++){
//								vo.setAttributeValue("line"+Integer.valueOf(j+1), map.get(ss[j].toUpperCase())==null?"":map.get(ss[j].toUpperCase()));
//							}
//							listW.add(vo);
//						}
//
//						vosW = listW.toArray(new ExpExcelVO[0]);
//						String filePath = path.endsWith(".xls")?path:(path+".xls");
//						File file = new File(filePath);	
//						if(!file.exists())
//							file.createNewFile();	
//						String fielPathTemp = path+"-1.xls";
//						ExpToExcel toexcle = new ExpToExcel(filePath,"数据对照结果",cname.substring(0,cname.length()-1),vosW,fielPathTemp);
//
//						MessageDialog.showWarningDlg(this.getBillUI(), "提示", "导出完成!");
//					}else{
//						getSelfUI().showErrorMessage("没有符合条件的数据！");
//						return;
//					}
//				}
				
				
				
				
			}else{
				getSelfUI().showWarningMessage("表或视图不存在！");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			getSelfUI().showErrorMessage(e.getMessage());
		}
	}
	
	public boolean method(String ziduan,String tablename,String path,String[] ss,String cname,boolean isdqfw) throws BusinessException, SQLException, DbException, IOException{
		
		String sql_session = "select "+ziduan.substring(0, ziduan.length()-1)+" from "+tablename.toUpperCase()+(isdqfw?" where 1=1 "+whereCondition:"");
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
			for(int i = 0;i<list.size();i++){
				HashMap map = (HashMap)list.get(i);
				ExpExcelVO	vo = new ExpExcelVO();

				for(int j = 0;j<ss.length;j++){
					String upperCase = ss[j].toUpperCase();
					Object value=map.get(upperCase);
					String string = functionMap.get(upperCase);
					if(null != string && string.startsWith(FunctionUtil.SHOWREPLACEPK)){
						int indexOf = string.indexOf("(");
						String substring = string.substring(indexOf+1, string.length()-1);
						String[] split = substring.split(",");
						if(null != split && split.length==2){
							if(null != map.get(split[0])){
								RetMessage ret=FunctionUtil.getShowReplaceFuctionValue(split[0],map.get(split[0]).toString(), 
										split[1],ui.selectnode);
								if(ret.getIssucc()){
									value=ret.getValue().toString();
								}
							}
						}
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
			ExpToExcel toexcle = new ExpToExcel(filePath,"数据对照结果",cname.substring(0,cname.length()-1),vosW,fielPathTemp);
			return true;
			//MessageDialog.showWarningDlg(this.getBillUI(), "提示", "导出完成!");
		}else{
			//getSelfUI().showErrorMessage("没有符合条件的数据！");
			return false;
		}
	}
	
	
	//	导入按钮 张进双 2011-6-14
//	protected void onBoImport() throws Exception {
//		IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
//		VOTreeNode treeNode=getSelectNode();
//		if(treeNode==null){
//			getSelfUI().showErrorMessage("请选择要导入的节点！");
//			return ;
//		}
//		String str=(String)treeNode.getParentnodeID();
//		if(str ==null || str.equals("")){
//			getSelfUI().showWarningMessage("系统节点不能做保存操作！"); 
//			return;
//		}
//
//		VOTreeNode node=getSelectNode();
//		String pk=(String) node.getNodeID();
////		数据定义VO
//		DipDatadefinitHVO cvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, pk);
////		找出所有要查的字段从dip_design里面查询要导入的字段；
//		String sql_ziduan = "select cname,ename from dip_design where nvl(dr,0)=0 and pk_datadefinit_h = '"+pk+"' and designtype = '2' order by disno ";
//		IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
//		List list_ziduan = queryfield.queryListMapSingleSql(sql_ziduan);
//		//主键字段
//		String pk_defin = null;
//		//要查询的字段拼接的字符
//		String ziduan = "";
//		String cname = "";
//		if(list_ziduan==null||list_ziduan.size()<=0){
//			this.getSelfUI().showErrorMessage("没有进行导入设置！请重新设置");
//			return;
//		}
//		
//		JFileChooser jfileChooser = new JFileChooser();
//		jfileChooser.setDialogType(jfileChooser.SAVE_DIALOG);
//		if (jfileChooser.showSaveDialog(this.getBillUI()) == javax.swing.JFileChooser.CANCEL_OPTION)
//			return;
//		String path = jfileChooser.getSelectedFile().toString();
//		if(!path.endsWith(".xls")){
//			getSelfUI().showErrorMessage("请选择EXCEL文件导入！");
//			return;
//		}
//		FileInputStream fin=new FileInputStream(path);
//		TxtDataVO tvo=new TxtDataVO();
//		try{
//			HSSFWorkbook book = new HSSFWorkbook(fin);
//			HSSFSheet sheet = book.getSheetAt(0);
//			if(sheet == null){
//				getSelfUI().showErrorMessage("导入文件格式不正确！");
//				return;
//			}
//			tvo.setSheetName(book.getSheetName(0));
//			tvo.setStartRow(sheet.getFirstRowNum());
//			tvo.setStartCol(sheet.getLeftCol());
//			tvo.setRowCount(sheet.getPhysicalNumberOfRows() - 1);//去掉标题行
//			tvo.setColCount((short)sheet.getRow(sheet.getFirstRowNum()).getPhysicalNumberOfCells());
//			tvo.setFirstDataRow(sheet.getFirstRowNum());
//			tvo.setFirstDataCol(sheet.getLeftCol());
//
//			HSSFRow titleRow = sheet.getRow(tvo.getStartRow());
//			HashMap<String, String> titlemap = new HashMap<String, String>();
//			for(short i=0;i<titleRow.getPhysicalNumberOfCells();i++){
//				titlemap.put((String)getCellValue(titleRow.getCell(i)),Short.toString(i));
//			}
//			tvo.setTitlemap(titlemap);
//			int titlesize=titlemap.size();
//			for(int i=1;i<=tvo.getRowCount();i++){
//				HSSFRow row = sheet.getRow(tvo.getFirstDataRow() + i);
//				for(short j=0;j<titleRow.getPhysicalNumberOfCells();j++){
//					tvo.setCellData(i-1, j, getCellValue(row.getCell(j)));
//				}
//			}
//		}finally{
//			fin.close();
//		}
//		if(list_ziduan.size() != tvo.getColCount()){
//			getSelfUI().showErrorMessage("导入设置的字段与导入文件中的字段不一致，请重新进行设置或修改文件！");
//			return;
//		}
//
//
//		
//		String tablename = cvo.getMemorytable();
//		for(int i=0;i<list_ziduan.size();i++){
//			HashMap map_ziduan = (HashMap)list_ziduan.get(i);
//			String ename = map_ziduan.get("ename".toUpperCase()).toString();
//			String name = map_ziduan.get("cname".toUpperCase()).toString();
//			ziduan =ziduan + ename + ",";
//			cname =cname + name + ",";
//
//		}
//		String waibuename = "";
//		for(int i=0;i<tvo.getColCount();i++){
//			HashMap map_ziduan = (HashMap)list_ziduan.get(i);
//			String ename = map_ziduan.get("ename".toUpperCase()).toString();
//			waibuename =waibuename + ename + ",";
//		}
//		//校验数据类型是否和数据库里面的类型匹配
//		for(int m=0;m<tvo.getColCount();m++){
//			HashMap mapname = (HashMap)list_ziduan.get(m);
//			String ename = mapname.get("ename".toUpperCase()).toString();
//			for(int y=0;y<tvo.getRowCount();y++){
//				String type = checkType(pk,ename);
//				Object waitype = tvo.getCellData(y, m);
//				int check = doTrance(waitype,type);
//				if(check == 1){
//					if(!type.toLowerCase().equals("char")){
//						this.getSelfUI().showErrorMessage("第"+(m+1)+"列数据类型与数据库类型不匹配！");
//						return;
//					}
//				}
//			}
//		}
//
//
////		获取当前时间的年月日时分秒如2011-05-14 18:07:22
//		UFDateTime now = new UFDateTime(new Date());
//		String nowDate = now.toString();
//		//判断设置里面要导入到字段与外部信息的字段的大小
//		//如果设置大于外部,就只导入外部的信息
//		if(list_ziduan.size()>tvo.getColCount()){
//			//判断是否有主键
//			String sql_definb = "select ename from dip_datadefinit_b where nvl(dr,0) = 0 and pk_datadefinit_h = '"+pk+"' and ispk='Y'";
//			List defin_list = queryfield.queryListMapSingleSql(sql_definb);
//			if(defin_list == null || defin_list.size()<=0){
//				for(int i=0;i<tvo.getRowCount();i++){
//					StringBuffer sql_insertdefin = new StringBuffer();
//					sql_insertdefin.append("insert into "+tablename.toUpperCase()+" ");
//					sql_insertdefin.append("	("+waibuename.substring(0, waibuename.length())+"");
//					sql_insertdefin.append("	ts)");
//					sql_insertdefin.append("	values(");
//					String waibu = null;
//
//					//程莉 2011-6-15
//					String[] strename=waibuename.split(",");
//					for(int j=0;j<tvo.getColCount();j++){
//						if(tvo.getCellData(i, j)!=null){
//							waibu = tvo.getCellData(i, j).toString();	
//						}else{
//							waibu="null";
//						}
//						
//
//						/*begin*/
//						String ename=null;
//						String type=null;
//						ename=strename[j];
//						type=checkType(pk,ename).toLowerCase();
//						if(type.contains("char")){
//							if(waibu!="null"){
//								sql_insertdefin.append(" '");
//								sql_insertdefin.append(waibu);
//								sql_insertdefin.append( "'");
//								sql_insertdefin.append(",");	
//							}else{
//								sql_insertdefin.append(" '");
//								sql_insertdefin.append( "'");
//								sql_insertdefin.append(",");
//							}
//							
//						}else{
//							sql_insertdefin.append("	"+waibu+"");
//							sql_insertdefin.append("	,");
//						}
//						/*end*/
//					}
//					sql_insertdefin.append("	'"+nowDate+"')");
//					queryfield.exesql(sql_insertdefin.toString());
//				}
//			}else{
//				HashMap map_ziduan = (HashMap)defin_list.get(0);
//				pk_defin = map_ziduan.get("ename".toUpperCase()).toString();
//
//				int l=0;
//				for(int i=0;i<list_ziduan.size();i++){
//					HashMap map = (HashMap)list_ziduan.get(i);
//					String ename = map.get("ename".toUpperCase()).toString();
//					if(ename.equals(pk_defin)){
//						l = i+1;
//					}
//				}
//				if(l<tvo.getColCount()){
//					for(int i=0;i<tvo.getRowCount();i++){
//						StringBuffer sql_insertdefin = new StringBuffer();
//						sql_insertdefin.append("insert into "+tablename.toUpperCase()+" ");
//						sql_insertdefin.append("	("+waibuename.substring(0, waibuename.length())+"");
//						sql_insertdefin.append("	ts)");
//						sql_insertdefin.append("	values(");
//						String waibu = null;
//
//						//程莉 2011-6-15 
//						String[] strename=waibuename.split(",");
//						for(int j=0;j<tvo.getColCount();j++){
//							waibu = tvo.getCellData(i, j).toString();
//
//							/*begin*/
//							String ename=null;
//							String type=null;
//							ename=strename[j];
//							type=checkType(pk,ename).toLowerCase();
//							if(type.contains("char")){
//								sql_insertdefin.append(" '");
//								sql_insertdefin.append(waibu);
//								sql_insertdefin.append( "'");
//								sql_insertdefin.append(",");
//							}else{
//								sql_insertdefin.append("	"+waibu+"");
//								sql_insertdefin.append("	,");
//							}
//							/*end*/
//
//						}
//						sql_insertdefin.append("	'"+nowDate+"')");
//						queryfield.exesql(sql_insertdefin.toString());
//					}
//				}else{
//					String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
//					for(int i=0;i<tvo.getRowCount();i++){
//						//获取主键
//						String pk_table = new SequenceGenerator().generate(pkCorp);
//						StringBuffer sql_insertdefin = new StringBuffer();
//						sql_insertdefin.append("insert into "+tablename.toUpperCase()+" ");
//						sql_insertdefin.append("	("+waibuename.substring(0, waibuename.length())+"");
//						sql_insertdefin.append("	 "+pk_defin+")");
//						sql_insertdefin.append("	values(");
//						String waibu = null;
//
//						//程莉 2011-6-15 
//						String[] strename=waibuename.split(",");
//						for(int j=0;j<tvo.getColCount();j++){
//							waibu = tvo.getCellData(i, j).toString();
//
//							/*begin*/
//							String ename=null;
//							String type=null;
//							ename=strename[j];
//							type=checkType(pk,ename).toLowerCase();
//							if(type.contains("char")){
//								sql_insertdefin.append(" '");
//								sql_insertdefin.append(waibu);
//								sql_insertdefin.append( "'");
//								sql_insertdefin.append(",");
//							}else{
//								sql_insertdefin.append("	"+waibu+"");
//								sql_insertdefin.append("	,");
//							}
//							/*end*/
//
//						}
//						sql_insertdefin.append("	'"+pk_table+"')");
//						queryfield.exesql(sql_insertdefin.toString());
//					}
//				}
//			}
//		}else
//			//反之导入设置里面的字段
//		{
////			判断是否有主键
//			String sql_definb = "select ename from dip_datadefinit_b where nvl(dr,0) = 0 and pk_datadefinit_h = '"+pk+"' and ispk='Y'";
//			List defin_list = queryfield.queryListMapSingleSql(sql_definb);
//			if(defin_list == null || defin_list.size()<=0){
//				for(int i=0;i<tvo.getRowCount();i++){
//					StringBuffer sql_insertdefin = new StringBuffer();
//					sql_insertdefin.append("insert into "+tablename.toUpperCase()+" ");
//					sql_insertdefin.append("	("+ziduan.substring(0, ziduan.length())+"");
//					sql_insertdefin.append("	ts)");
//					sql_insertdefin.append("	values(");
//					String waibu = null;
//					Object ob=null;
//					//程莉 2011-6-15 
//					String[] strename=ziduan.split(",");
//					for(int j=0;j<list_ziduan.size();j++){
//						ob=tvo.getCellData(i, j);
////						waibu = tvo.getCellData(i, j).toString();
//
//						/*begin*/
//						String ename=null;
//						String type=null;
//						ename=strename[j];
//						type=checkType(pk,ename).toLowerCase();
//						if(type.contains("char")&&ob!=null){
//							sql_insertdefin.append(" '");
//							sql_insertdefin.append(ob.toString());
//							sql_insertdefin.append( "'");
//							sql_insertdefin.append(",");
//						}else{
//							sql_insertdefin.append("	"+(ob==null?null:ob.toString())+"");
//							sql_insertdefin.append("	,");
//						}
//						/*end*/
//
//					}
//					sql_insertdefin.append("	'"+nowDate+"'");
//					sql_insertdefin.append("	)");
//					queryfield.exesql(sql_insertdefin.toString());
//				}
//			}else{
//				HashMap map_ziduan = (HashMap)defin_list.get(0);
//				pk_defin = map_ziduan.get("ename".toUpperCase()).toString();
//				int k = checkPk(pk,pk_defin);
//				if(k==1){
//					for(int i=0;i<tvo.getRowCount();i++){
//						StringBuffer sql_insertdefin = new StringBuffer();
//						sql_insertdefin.append("insert into "+tablename.toUpperCase()+" ");
//						sql_insertdefin.append("	("+ziduan.substring(0, ziduan.length())+"");
//						sql_insertdefin.append("	 ts)");
//						sql_insertdefin.append("	values(");
//						//程莉 2011-6-15 
//						String[] strename=ziduan.split(",");
//						for(int j=0;j<list_ziduan.size();j++){
//							Object ob=tvo.getCellData(i, j);
////							String waibu = tvo.getCellData(i, j).toString();
//							
//							/*begin*/
//							String ename=null;
//							String type=null;
//							ename=strename[j];
//							if(ename.toUpperCase().equals(pk_defin.toUpperCase())&&ob==null){
//								getBillUI().showErrorMessage("第"+(i+1)+"行主键为空！");
//								return;
//							}
//							type=checkType(pk,ename).toLowerCase();
//							if(type.contains("char")&&ob!=null){
//								sql_insertdefin.append(" '");
//								sql_insertdefin.append(ob.toString());
//								sql_insertdefin.append( "'");
//								sql_insertdefin.append(",");
//							}else{
//								sql_insertdefin.append("	"+(ob==null?null:ob.toString())+"");
//								sql_insertdefin.append("	,");
//							}
//							/*end*/
//
//						}
//						sql_insertdefin.append("	'"+nowDate+"'");
//						sql_insertdefin.append("	)");
//						queryfield.exesql(sql_insertdefin.toString());
//					}
//				}else{
//					String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
//					for(int i=0;i<tvo.getRowCount();i++){
//						//获取主键
//						String pk_table = new SequenceGenerator().generate(pkCorp);
//						StringBuffer sql_insertdefin = new StringBuffer();
//						sql_insertdefin.append("insert into "+tablename.toUpperCase()+" ");
//						sql_insertdefin.append("	("+ziduan.substring(0, ziduan.length())+"");
//						sql_insertdefin.append("	 "+pk_defin+")");
//						sql_insertdefin.append("	values(");
//
//						//程莉 2011-6-15 
//						String[] strename=ziduan.split(",");
//						for(int j=0;j<list_ziduan.size();j++){
//							Object waibu = tvo.getCellData(i, j);
////							if((j+1)==k&&waibu==null){
////								getBillUI().showErrorMessage("第"+(i+1)+"行主键为空！");
////								return;
////							}
//
//							/*begin*/
//							String ename=null;
//							String type=null;
//							ename=strename[j];
//							type=checkType(pk,ename).toLowerCase();
//							if(type.contains("char")&&waibu!=null){
//								sql_insertdefin.append(" '");
//								sql_insertdefin.append(waibu.toString());
//								sql_insertdefin.append( "'");
//								sql_insertdefin.append(",");
//							}else{
//								sql_insertdefin.append("	"+(waibu==null?null:waibu.toString())+"");
//								sql_insertdefin.append("	,");
//							}
//							/*end*/
//
//						}
//						sql_insertdefin.append("	'"+pk_table+"'");
//						sql_insertdefin.append("	)");
//						queryfield.exesql(sql_insertdefin.toString());
//					}
//				}
//			}
//		}
//		MessageDialog.showHintDlg(getSelfUI(), "提示", "导入完成！");
//	}
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
		VOTreeNode node=getSelectNode();
		String pk=(String) node.getNodeID();
//		数据定义VO
		DipDatadefinitHVO cvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, pk);
//		找出所有要查的字段从dip_design里面查询要导入的字段；
		String sql_ziduan = "select cname,ename,consvalue from dip_design where nvl(dr,0)=0 and pk_datadefinit_h = '"+pk+"' and designtype = '2' order by disno ";
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
		String tablename = cvo.getMemorytable();
		int insertcount=0;
		List<RowDataVO> errList=new ArrayList<RowDataVO>();
		List<String> insertlist=new ArrayList<String>();
		if(tvo.getData()==null||tvo.getData().length==0){
			getSelfUI().showErrorMessage("excel文件不能为空");
			return;
		}
		HashMap<String, DipDatadefinitCVO[]> checkMap = DataCheckUtil.getDataCheckMap(cvo.getPk_datadefinit_h());
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
				String value=FunctionUtil.getFuctionValue(autoInMap.get(ename).toString());
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
						String checkDataMsg = DataCheckUtil.checkData(ename, value,null);
						if(null != checkDataMsg){
							allsb.append(message+"  "+checkDataMsg);
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
									value=tvo.getCellData(i, Integer.parseInt(object2.toString()))==null?""
											:tvo.getCellData(i, Integer.parseInt(object2.toString())).toString();
									RetMessage ret=FunctionUtil.getSaveReplaceFuctionValue(split[0].toUpperCase(),value, 
											split[1],cvo.getPk_datadefinit_h());
									if(ret.getIssucc()){
										value=ret.getValue().toString();
									}else{
										getSelfUI().showErrorMessage(ret.getMessage());
										return;
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
								RetMessage ret=FunctionUtil.getSaveReplaceFuctionValue(split[0].toUpperCase(),value, 
										split[1],cvo.getPk_datadefinit_h());
								if(ret.getIssucc()){
									refvalue=ret.getValue().toString();
								}else{
									getSelfUI().showErrorMessage(ret.getMessage());
									return;
								}
							}else if(null != autoIn.get(split[0].toUpperCase())){
								String oldvalue = autoIn.get(split[0].toUpperCase());
								RetMessage ret=FunctionUtil.getSaveReplaceFuctionValue(split[0].toUpperCase(),oldvalue, 
										split[1],cvo.getPk_datadefinit_h());
								if(ret.getIssucc()){
									refvalue=ret.getValue().toString();
								}else{
									getSelfUI().showErrorMessage(ret.getMessage());
									return;
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
			String sheetNames[]=new String[]{"数据浏览"};
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
		String sql_ziduan = "select cname,ename from dip_design where nvl(dr,0)=0 and pk_datadefinit_h = '"+pk+"' and designtype = '2' order by disno ";
		IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
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
	public String checkType(String pk,String ename){
		String type = null;
		IUAPQueryBS iuap = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
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
//		VOTreeNode node=getSelectNode();
//		TableTreeNode nnn=(TableTreeNode) node.getRoot();
//		VOTreeNode node1=(VOTreeNode) node.getParent();
		super.onBoRefresh();
		getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
		getSelfUI().getButtonManager().getButton(IBillButton.ImportBill).setEnabled(false);
		getSelfUI().getButtonManager().getButton(IBillButton.ExportBill).setEnabled(false);
		getSelfUI().getButtonManager().getButton(IBtnDefine.DATALOOKQUERY).setEnabled(false);
		getSelfUI().getButtonManager().getButton(IBtnDefine.DELETE).setEnabled(false);
		getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
		getSelfUI().updateButtonUI();
//		getSelfUI().getBillTree().setSelectionPath(new javax.swing.tree.TreePath( nnn.getPath()));
	}
	
	
}