package nc.ui.dip.procondition;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import nc.ui.dip.dataproce.DataProceUI;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.vo.pub.lang.UFBoolean;

public class ProconditionListener  implements ActionListener {

	DataProceUI billUI;
	
	String rskey = ""; 
	public ProconditionListener(DataProceUI ui) {
		this.billUI=ui;
	}
	
	public void actionPerformed(ActionEvent e) {
		
		billUI.transferFocus();

		String firsttab = billUI.getBillCardPanel().getHeadItem("firsttab").getValueObject()==null ?"":billUI.getBillCardPanel().getHeadItem("firsttab").getValueObject().toString();
		
		String procecond = billUI.getBillCardPanel().getHeadItem("procecond").getValueObject()==null ?"":billUI.getBillCardPanel().getHeadItem("procecond").getValueObject().toString();
		
		String procetype = billUI.getBillCardPanel().getHeadItem("procetype").getValueObject()==null?"":billUI.getBillCardPanel().getHeadItem("procetype").getValueObject().toString(); 
		
		HashMap tableMap = new HashMap();
		tableMap.put("firsttab", firsttab);
		tableMap.put("procecond", procecond);
		tableMap.put("procetype", procetype);
		ProconditionClientUI ui = new ProconditionClientUI();
		ProconditionDlg dlg = new ProconditionDlg(ui, new UFBoolean(true), tableMap);
		dlg.show();
		if(dlg.onSave==1){
			BillItem item12 = billUI.getBillCardPanel().getHeadItem("refprocecond");//加工条件
			if (item12 != null) {
				UIRefPane ref = (UIRefPane) item12.getComponent();
				if (ref != null) { 
					String sql = dlg.getReturnSql();
					ref.setText(sql);
//					ref.setValue(dlg.getReturnSql());
					billUI.getBillCardPanel().getHeadItem("procecond").setValue(sql);
//					billUI.getBillCardPanel().getHeadItem("procecond").setComponent(ref);
				}
			}
			dlg.onSave=0;
		}
		
		dlg.destroy();
	}

}