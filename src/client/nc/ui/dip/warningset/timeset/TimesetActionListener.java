package nc.ui.dip.warningset.timeset;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.dip.warningset.MyEventHandler;
import nc.ui.dip.warningset.WarningSetClientUI;

public class TimesetActionListener implements ActionListener {
	WarningSetClientUI clientui;
	public TimesetActionListener(WarningSetClientUI clientui){
		super();
		this.clientui=clientui;
	}
	public void actionPerformed(ActionEvent arg0) {
		clientui.onBoWartime();
	}

}
