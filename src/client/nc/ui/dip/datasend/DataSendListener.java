package nc.ui.dip.datasend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;



import nc.ui.pub.beans.UIRefPane;

public class DataSendListener implements ActionListener {
	public static final int TYPE_HEAD=0;
	public static final int TYPE_BODY=1;
	public static final int TYPE_TAIL=2;
	

	DataSendClientUI billUI;

	String rskey = "";

	String deffilename = "";

	String savefieldname = "";
	UIRefPane rf;

	int ishead;

	/**
	 * @param ui
	 *            当前界面的ui
	 * @param deffilename
	 *            界面上显示的配置伪参照字段的那个字段名
	 * @param savefieldame
	 *            实际数据库保存字段的字段名
	 */
	public DataSendListener(DataSendClientUI ui, String deffilename,
			String savefieldname, int ishead,UIRefPane rf) {
		this.billUI = ui;
		this.deffilename = deffilename;
		this.savefieldname = savefieldname;
		this.ishead = ishead;
		this.rf=rf;
	}

	public void actionPerformed(ActionEvent e) {
		billUI.transferFocus();

		JFileChooser jfileChooser = new JFileChooser();
		jfileChooser.setDialogType(jfileChooser.SAVE_DIALOG);
		if (jfileChooser.showSaveDialog(billUI) == javax.swing.JFileChooser.CANCEL_OPTION)
			return;
		String path = jfileChooser.getSelectedFile().toString();
		System.out.println(path+"---------------------------------------");
		billUI.getBillCardPanel().getHeadItem("def_str2").setValue(path);
		
	}
	
}

