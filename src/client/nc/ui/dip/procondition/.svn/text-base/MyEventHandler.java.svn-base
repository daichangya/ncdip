package nc.ui.dip.procondition;

import javax.swing.JOptionPane;

import nc.ui.bd.pub.AbstractBdBillCardUI;
import nc.ui.bd.pub.DefaultBDBillCardEventHandle;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.card.BillCardUI;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;


/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends DefaultBDBillCardEventHandle {

	ProconditionClientUI ui = (ProconditionClientUI)this.getBillUI();
	public MyEventHandler(BillCardUI arg0, ICardController arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public void onButtonAction(ButtonObject bo) throws Exception {
		int intBtn = Integer.parseInt(bo.getTag());
		long lngTime = System.currentTimeMillis();
		buttonActionBefore(getBillUI(), intBtn);

		switch (intBtn) {
		default:
			break;

		case 1: // '\001'
			if (!getBillUI().isBusinessType().booleanValue()) {
				getBillUI().showHintMessage(
						NCLangRes.getInstance().getStrByID("uifactory",
								"UPPuifactory-000061"));
				onBoAdd(bo);
				buttonActionAfter(getBillUI(), intBtn);
			}
			break;

		case 3: // '\003'
			getBillUI().showHintMessage(
					NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000067"));
			onBoEdit();
			buttonActionAfter(getBillUI(), intBtn);
			break;

		case 32: // ' '
			getBillUI().showHintMessage(
					NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000070"));
			onBoDelete();
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					(new StringBuilder()).append(
							NCLangRes.getInstance().getStrByID("uifactory",
									"UPPuifactory-000071")).append(
							System.currentTimeMillis() - lngTime).toString());
			break;

		case 0: // '\0'
			getBillUI().showHintMessage(
					NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000072"));
			onBoSave();
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					(new StringBuilder()).append(
							NCLangRes.getInstance().getStrByID("uifactory",
									"UPPuifactory-000073")).append(
							System.currentTimeMillis() - lngTime).toString());
			break;

		case 7: // '\007'
			onBoCancel();
			getBillUI().showHintMessage("");
			buttonActionAfter(getBillUI(), intBtn);
			break;

		case 8: // '\b'
			getBillUI().showHintMessage(
					NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000076"));
			onBoRefresh();
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					(new StringBuilder()).append(
							NCLangRes.getInstance().getStrByID("uifactory",
									"UPPuifactory-000077")).append(
							System.currentTimeMillis() - lngTime).toString());
			break;
		
		}
	}


	/**
	 * 编辑
	 */
	public void onBoEdit() throws Exception {
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable()
				.getSelectedRow();
		CircularlyAccessibleValueObject selectedVOs[] = getBillCardPanelWrapper()
				.getSelectedBodyVOs();
		if (row == -1 || selectedVOs == null || selectedVOs.length == 0) {
			JOptionPane.showMessageDialog(getBillCardPanelWrapper()
					.getBillCardPanel(), "必须选择一条数据");
		} else {
			super.onBoEdit();
			((AbstractBdBillCardUI) getBillUI()).setBDUIExtendStatus(2);
			int rowCount = getBillCardPanelWrapper().getBillCardPanel()
					.getRowCount();
			getBillCardPanelWrapper().getBillCardPanel().getBillModel()
					.setRowEditState(true);
			int index = 0;
			int rows[] = new int[rowCount - 1];
			for (int i = 0; i < rowCount; i++) {
				if (i != row) {
					rows[index++] = i;
				}
			}
			getBillCardPanelWrapper().getBillCardPanel().getBillModel()
					.setNotEditAllowedRows(rows);
		}
	}
	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCancel();
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
//		super.onBoSave();
		ProconditionClientUI ui = (ProconditionClientUI) this.getBillUI();
		String memorytable = "";
		String procetype = "";
		if(ui.tableMap!=null && ui.tableMap.size()>0){
			memorytable = ui.tableMap.get("firsttab").toString();
			procetype = ui.tableMap.get("procetype").toString();
		}
		//,数据汇总,数据清洗,数据卸载,数据预置,数据转换
		if("数据汇总".equals(procetype)){
			sjjg(memorytable,procetype);
		}
//		if("数据清洗".equals(procetype)){
//			sjqx(memorytable,procetype);
//		}
		if("数据卸载".equals(procetype)){
			sjxz(memorytable,procetype);
		}
		if("数据预置".equals(procetype)){
			sjyz(memorytable,procetype);
		}
		
	}
	/**
	 * 数据预置
	 * @param memorytable
	 * @param procetype
	 * @throws Exception 
	 */
	public void sjyz(String memorytable, String procetype) throws Exception {
		// TODO Auto-generated method stub
		DipDatadefinitHVO hvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, memorytable);
		if(hvo==null)
			return ;
		memorytable=hvo.getMemorytable();
		StringBuffer updateSql = new StringBuffer();
		int k=0;
		for(int i=0;i<ui.getBillCardPanel().getRowCount();i++){
			if(ui.getBillCardPanel().getBodyValueAt(i, "def3")!=null){
				String ename=ui.getBillCardPanel().getBodyValueAt(i, "field").toString();
				String enamevalue=ui.getBillCardPanel().getBodyValueAt(i,"def3").toString();				
				if(k==0){
					updateSql.append("set "+ename +"="+enamevalue);
				}else{
					updateSql.append(","+ename +"="+enamevalue);
				}	
			
			};
			
		}
		
		String sql="update "+memorytable+" "+updateSql.toString();
		ui.returnSql=sql;
	}

	/**
	 * 数据加工
	 * */
	public void sjjg(String memorytable,String procetype) throws Exception{
		DipDatadefinitHVO hvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, memorytable);
		if(hvo==null)
			return ;
		memorytable=hvo.getMemorytable();
		
		StringBuffer selectSql = new StringBuffer();
		StringBuffer groupSql = new StringBuffer();
		for (int row = 0; row < ui.getBillCardPanel().getBillModel().getRowCount(); row++) {
			UFBoolean flag = ui.getBillCardPanel().getBillModel().getValueAt(row, "issum")==null?new UFBoolean(false):new UFBoolean(ui.getBillCardPanel().getBillModel().getValueAt(row, "issum").toString());
			String ename = ui.getBillCardPanel().getBillModel().getValueAt(row, "field")==null?"":ui.getBillCardPanel().getBillModel().getValueAt(row, "field").toString();
			UFBoolean isSelect = ui.getBillCardPanel().getBillModel().getValueAt(row, "def1")==null?new UFBoolean(false):new UFBoolean(ui.getBillCardPanel().getBillModel().getValueAt(row, "def1").toString());
			//如果勾选是否显示
			if(isSelect.booleanValue()){
				if(flag.booleanValue()){
					selectSql.append(" sum("+ename+") as "+ename+",");
				}else{
					selectSql.append(ename+",");
					groupSql.append(ename+",");
					
				}
			}
			/*if(isSelect.booleanValue() ){
//				if(flag.booleanValue()){
					selectSql.append(" sum("+ename+"),"+ename+",");
//				}
			}if(flag.booleanValue()){
					selectSql.append(ename+",");
					groupSql.append(ename+",");
				
			}*/
		}
		String sql = "";
		sql = "select ";
		if(selectSql!=null && selectSql.length()>0){
			sql = sql + " "+selectSql.toString().substring(0, selectSql.toString().length()-1); 
		}
		sql = sql + " from "+memorytable +" where 1=1 ";
		if(groupSql!=null && groupSql.length()>0){
			sql = sql + " group by "+groupSql.toString().substring(0, groupSql.toString().length()-1); 
		}
		ui.returnSql = sql ; 
	}
	/**
	 * 数据清洗
	 * */
	public void sjqx(String memorytable,String procetype) throws Exception{
		DipDatadefinitHVO hvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, memorytable);
		if(hvo==null)
			return ;
		memorytable=hvo.getMemorytable();
		
		StringBuffer str = new StringBuffer();
		for (int row = 0; row < ui.getBillCardPanel().getBillModel().getRowCount(); row++) {
			
			String ename = ui.getBillCardPanel().getBillModel().getValueAt(row, "field")==null?"":ui.getBillCardPanel().getBillModel().getValueAt(row, "field").toString();
			String getvalue = ui.getBillCardPanel().getBillModel().getValueAt(row, "def3")==null?"":ui.getBillCardPanel().getBillModel().getValueAt(row, "def3").toString();
			if(!"".equals(getvalue)){
				String cztype = ui.getBillCardPanel().getBillModel().getValueAt(row, "def2")==null?"":ui.getBillCardPanel().getBillModel().getValueAt(row, "def2").toString();
				//目前只考虑到字符类型的sql拼接，后续处理小数类型
			  //if("like".equals(cztype))
				//2011-05-27 程莉
				if("like".equals(convertType(cztype)))
					str.append(" and "+ename+" "+convertType(cztype)+" '%"+getvalue+"%'"+"");
				else
					str.append(" and "+ename+" "+convertType(cztype)+" '"+getvalue+"' "+"");
			}
			
		}
		str.append(" and 1=1 ");
		String sql = "";
		sql = "delete from  "+memorytable+" where 1=1 "+str.toString();
		
		ui.returnSql = sql ; 
	}

	/**
	 * 数据卸载
	 * */
	public void sjxz(String memorytable,String procetype) throws Exception{
	
		
		StringBuffer str = new StringBuffer();
		for (int row = 0; row < ui.getBillCardPanel().getBillModel().getRowCount(); row++) {
			
			
			String getvalue = ui.getBillCardPanel().getBillModel().getValueAt(row, "def5")==null?"":ui.getBillCardPanel().getBillModel().getValueAt(row, "def5").toString();
			UFBoolean flag = ui.getBillCardPanel().getBillModel().getValueAt(row, "def6")==null?new UFBoolean(false):new UFBoolean(ui.getBillCardPanel().getBillModel().getValueAt(row, "def6").toString());
			if(flag.booleanValue()){
				str.append(getvalue+",");
			}
		}
		
		if(!"".equals(str.toString())){
			String strs [] = str.toString().split(",");
			if(strs.length==2){
				MessageDialog.showWarningDlg(ui, "提示", "只能选择一种方式备份");
				return ;
			}
		}else{//数据卸载，没做加工条件，直接return;2011-06-24 zlc
			return;
		}
		String sql = "";
		sql = str.toString().substring(0,str.toString().length()-1);
		
		ui.returnSql = sql ; 
	}
	public String convertType(String cztype){
		//SX,小于,小于等于,等于,大于,大于等于,包含
		String type = "";
		if("".equals(cztype))
			type = "=";
		if("小于".equals(cztype))
			type = "<";
		if("小于等于".equals(cztype))
			type = "<=";
		if("等于".equals(cztype))
			type = "=";
		if("大于".equals(cztype))
			type = ">";
		if("大于等于".equals(cztype))
			type = ">=";
		if("包含".equals(cztype))
			type = "like";
		
		
		return type;
	}
}