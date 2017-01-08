package nc.ui.dip.alertmanag;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.bill.BillListPanel;

public class AlertSplitBodyPane extends JPanel implements ActionListener{
	UIScrollPane m_scrollPane;
	UISplitPane m_splitPane;
	BillListPanel mainValueSPl;
	public AlertSplitBodyPane(boolean isInit){
		super();
		setSize(800, 400);
		setLayout(new java.awt.BorderLayout());
		initialize(isInit);
	}
	
	public void initialize(boolean isInit ){
		try{
//			this.add(getSplitPane(),"Center");
//			getSplitPane().set
			this.add(getM_splitPane(), "Center");
			getM_splitPane().add(getMainPane());
		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
	}
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	public UIScrollPane getM_scrollPane(){
		if(m_scrollPane==null){
			m_scrollPane=new UIScrollPane();
		}
		return m_scrollPane;
	}
	public BillListPanel getMainPane() {
		if (mainValueSPl == null) {
			mainValueSPl = new BillListPanel();
			mainValueSPl.loadTemplet("H4H4H2", null, ClientEnvironment.getInstance().getUser().getPrimaryKey(), ClientEnvironment.getInstance().getCorporation().getPk_corp());
		}
		return mainValueSPl;
	}
	
	public UISplitPane getM_splitPane()
	{
		if (m_splitPane == null){
			m_splitPane = new UISplitPane(1);
			m_splitPane.setOneTouchExpandable(true);
		}
		return m_splitPane;
	}
}
