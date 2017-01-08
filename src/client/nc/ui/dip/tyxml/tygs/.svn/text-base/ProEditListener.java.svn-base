package nc.ui.dip.tyxml.tygs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class ProEditListener implements ActionListener {
	public static final int TYPE_HEAD=0;
	public static final int TYPE_BODY=1;
	public static final int TYPE_TAIL=2;
	

	ProDlg billUI;

	String rskey = "";
	String savefieldname = "";
	String pk_datadef = "";
	UIRefPane rf;


	/**
	 * @param ui
	 *            当前界面的ui
	 * @param deffilename
	 *            界面上显示的配置伪参照字段的那个字段名
	 * @param savefieldame
	 *            实际数据库保存字段的字段名
	 */
	public ProEditListener(ProDlg ui, 
			String savefieldname,UIRefPane rf,String pk_datadef) {
		this.billUI = ui;
		this.savefieldname = savefieldname;
		this.pk_datadef=pk_datadef;
		this.rf=rf;
	}

	public void actionPerformed(ActionEvent e) {
		DipDatadefinitBVO[] bvos = null;
		DipDatadefinitBVO[] hvos=null;
		String pk_sys="";
		//表头标志
		try {
				DipDatadefinitHVO hvo=(DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class,pk_datadef);
				if(hvo!=null){
					
					    hvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class,"pk_datadefinit_h='"+pk_datadef+"'");
//					    pk_sys=hvo.getPk_sysregister_h();
					    pk_sys=hvo.getPk_xt();
					    bvos=null;
				}
		} catch (UifException ex) {
			ex.printStackTrace();
		}
		
		DefitemVO[] defvo = new DefitemVO[(SJUtil.isNull(bvos)?0:bvos.length)+(SJUtil.isNull(hvos)?0:hvos.length)];
		int i = 0;
		if(!SJUtil.isNull(bvos)){
			for (DipDatadefinitBVO bvoi : bvos) {
				defvo[i] = new DefitemVO();
				defvo[i].setAttrname(bvoi.getEname());
				//以下标志是表头还是表体，表头defvo[i].setHeadflag(new UFBoolean(true));，表体defvo[i].setHeadflag(new UFBoolean(false))
				defvo[i].setHeadflag(new UFBoolean(false));
				defvo[i].setItemname(bvoi.getCname());
				defvo[i].setPrimaryKey(bvoi.getPrimaryKey());
				// TODO wyd 这个type值，要编辑
				defvo[i].setItemtype(0);
				i++;
			}
		}
		if(!SJUtil.isNull(hvos)){
			for(DipDatadefinitBVO hvoi:hvos){
				defvo[i] = new DefitemVO();
				defvo[i].setAttrname(hvoi.getEname());
				//以下标志是表头还是表体，表头defvo[i].setHeadflag(new UFBoolean(true));，表体defvo[i].setHeadflag(new UFBoolean(false))
				defvo[i].setHeadflag(new UFBoolean(true));
				defvo[i].setItemname(hvoi.getCname());
				defvo[i].setPrimaryKey(hvoi.getPrimaryKey());
				// TODO wyd 这个type值，要编辑
				defvo[i].setItemtype(0);
				i++;
			}
		}
		DapFormuDefUI dlg = new DapFormuDefUI(billUI,pk_datadef,pk_sys);
		dlg.setFormula((String)billUI.getMipanel().getBodyValueAt(billUI.getMipanel().getBillTable().getSelectedRow(), savefieldname));
		dlg.setBillItems(defvo);
		dlg.showModal();
		
		if(dlg.OK==1){//如果是确认按钮
			String tmpString = dlg.getFormula();
			rskey = tmpString;
			int ro1=billUI.getMipanel().getBillTable().getSelectedRow();
			billUI.getMipanel().setBodyValueAt(rskey, ro1, savefieldname);
			rf.setText(rskey);
		}
		dlg.OK=0;
		dlg.destroy();

	}

}