package nc.ui.dip.actionset;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.dip.dataproce.ProFormuDefUI;
import nc.ui.pub.bill.BillItem;

public class ActionSetActionListener implements ActionListener {
	ActionSetClientUI ui;
	BillItem item;
	String fpk;
	public ActionSetActionListener(ActionSetClientUI ui,BillItem item,String fpk){
		this.item=item;
		this.ui=ui;
		this.fpk=fpk;
	}
	public void actionPerformed(ActionEvent e) {
		ProFormuDefUI dlg = new ProFormuDefUI(ui, fpk);
		int row = ui.getBillCardPanel().getBillTable().getSelectedRow();
		String replace = item.getKey().replace("_ref", "");
		Object ob = ui.getBillCardPanel().getBodyValueAt(row, replace);
		dlg.setFormula(ob == null ? "" : ob.toString());
		dlg.showModal();
		if (dlg.OK == 1) {
			String tmpString = dlg.getFormula();
			ui.getBillCardPanel().setBodyValueAt(tmpString, row, replace);// .getHeadItem("refprocecond").setValue(tmpString);
			ui.getBillCardPanel().setBodyValueAt("“—…Ë÷√", row, item.getKey());
		}
	}

}