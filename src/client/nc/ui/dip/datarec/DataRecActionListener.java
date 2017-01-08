package nc.ui.dip.datarec;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.pub.bill.BillItem;

public class DataRecActionListener implements ActionListener{
	String dataorigin;
	String display;
	String value;
	BillItem item;
	public DataRecActionListener(BillItem item,String dataorigin,String display) {
		this.dataorigin=dataorigin;
		this.display=display;
		this.value=value;
		this.item=item;
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		DataRecPanel dd=new DataRecPanel(item,dataorigin, display);
		dd.showModal();
	}

}
