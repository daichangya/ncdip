package nc.ui.dip.dataverify;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import nc.ui.dip.dataproce.ProFormuDefUI;
import nc.ui.pub.bill.BillItem;

public class DataVeryActionListener implements ActionListener {
	DataVerifyClientUI ui;
	BillItem item;
	String fpk;
	public DataVeryActionListener(DataVerifyClientUI ui,BillItem item,String fpk){
		this.item=item;
		this.ui=ui;
		this.fpk=fpk;
	}
	public void actionPerformed(ActionEvent e) {
			ProFormuDefUI dlg = new ProFormuDefUI(ui,fpk);
			int row=ui.getBillCardPanel().getBillTable().getSelectedRow();
			Object ob=ui.getBillCardPanel().getBodyValueAt(row, "verifycon");
			dlg.setFormula(ob==null?"":ob.toString());
			dlg.showModal();
			if(dlg.OK==1){
				String tmpString = dlg.getFormula();
				ui.getBillCardPanel().setBodyValueAt(tmpString,row, "verifycon");//.getHeadItem("refprocecond").setValue(tmpString);
			}
		}

}