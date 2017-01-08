package nc.ui.dip.tyxml.tygs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.borland.dbswing.DBTextDataBinder.SaveFileAction;

import nc.ui.dip.tyxml.DataChangeClientUI;
import nc.ui.dip.tyzhq.tygs.DapFormuDefUI;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.pub.billtype.DefitemVO;
import nc.vo.pub.lang.UFBoolean;

public class ProListener implements ActionListener {
	public static final int TYPE_HEAD=0;
	public static final int TYPE_BODY=1;
	public static final int TYPE_TAIL=2;
	

	DataChangeClientUI billUI;

	String rskey = "";
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
	public ProListener(DataChangeClientUI ui, 
			String savefieldname, int ishead,UIRefPane rf) {
		this.billUI = ui;
		this.savefieldname = savefieldname;
		this.ishead = ishead;
		this.rf=rf;
	}

	public void actionPerformed(ActionEvent e) {
		String pk_datadef=(String) billUI.getBillCardPanel().getHeadItem("sourcetab").getValueObject();
		int ro1=billUI.getBillCardWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		String fo=(String) billUI.getBillCardPanel().getBodyValueAt(ro1, savefieldname);
		ProDlg dlg=new ProDlg(billUI,fo,pk_datadef);
		dlg.showModal();
		
		if(dlg.isOK()){//如果是确认按钮
			String tmpString = dlg.getFormula();
			rskey = tmpString;
				billUI.getBillCardPanel().setBodyValueAt(rskey, ro1, savefieldname);
				rf.setText(rskey);
		}
		dlg.destroy();

	}

}