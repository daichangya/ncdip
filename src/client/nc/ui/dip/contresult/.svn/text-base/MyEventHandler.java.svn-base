package nc.ui.dip.contresult;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.bd.pub.AbstractBdBillCardUI;
import nc.ui.bd.pub.DefaultBDBillCardEventHandle;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
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

	ContResultClientUI ui = (ContResultClientUI)this.getBillUI();
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
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
//		super.onBoDelete();
		int row = ui.getBillCardPanel().getBillModel().getRowCount();
		String contFieldvdef = "";
		contFieldvdef = ui.tableMap.get("contFieldvdef")==null?"":ui.tableMap.get("contFieldvdef").toString();
		StringBuffer strpk = new StringBuffer();
		
		for(int i = 0;i<row;i++){
			UFBoolean flag = ui.getBillCardPanel().getBillModel().getValueAt(i, "selectresult")==null?new UFBoolean(false):new UFBoolean(ui.getBillCardPanel().getBillModel().getValueAt(i, "selectresult").toString());
			if(flag.booleanValue()){
				String  pkvalue = ui.getBillCardPanel().getBillModel().getValueAt(i, contFieldvdef)==null?"":ui.getBillCardPanel().getBillModel().getValueAt(i, contFieldvdef).toString();
				strpk.append("'"+pkvalue+"',");
			}
		}
		String sql = "";
		if("".equals(strpk.toString()))
			return ;
		String tablename = ui.tableMap.get("tablename")==null?"":ui.tableMap.get("tablename").toString();
		sql = "delete from "+tablename+" where contpk in ("+strpk.toString().substring(0, strpk.toString().length()-1)+")";
		
		IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
		int flag = MessageDialog.showOkCancelDlg(ui, "删除", "确定是否删除");
		if(flag==2){
			return;
		}
		queryfield.exesql(sql);
		MessageDialog.showErrorDlg(ui, "提示", "删除成功");
	}
	
	
	
	
	

	
	
}