package nc.ui.dip.credence;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.pub.billtype.DefitemVO;
import nc.vo.pub.lang.UFBoolean;

public class CredenceListener implements ActionListener {
	public static final int TYPE_HEAD=0;
	public static final int TYPE_BODY=1;
	public static final int TYPE_TAIL=2;
	

	CredenceClientUI billUI;

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
	public CredenceListener(CredenceClientUI ui, String deffilename,
			String savefieldname, int ishead,UIRefPane rf) {
		this.billUI = ui;
		this.deffilename = deffilename;
		this.savefieldname = savefieldname;
		this.ishead = ishead;
		this.rf=rf;
	}

	public void actionPerformed(ActionEvent e) {
		String pk_credence_h=billUI.getPk_credence_h();
		String pk_datadef=billUI.getPk_datadef();
		DipDatadefinitBVO[] bvos = null;
		DipDatadefinitBVO[] hvos=null;
		String pk_sys="";
		//表头标志
//		boolean flag=true;
		try {
//			bvo = (DipDatadefinitBVO[]) HYPubBO_Client.
//					queryAll(DipDatadefinitBVO.class);
//			DipDatachangeHVO hchanvo=(DipDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipDatachangeHVO.class, pk_credence_h);
//			if(!SJUtil.isNull(hchanvo)&&hchanvo.getBusidata()!=null){
//				String bus=hchanvo.getBusidata();
				DipDatadefinitHVO hvo=(DipDatadefinitHVO)HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class,pk_datadef);
				if(hvo!=null){
					
//				    if(SJUtil.isNull(hvo.getCitetable())||hvo.getCitetable().equals("")){
	//				    flag=true;
					    hvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class,"pk_datadefinit_h='"+pk_datadef+"'");
					    pk_sys=hvo.getPk_xt();
	//				    hvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, hvo.getMemorytable());
//					    bvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class,"pk_datadefinit_h='"+pk_credence_h+"'");
//				    }else{
//	//				    flag=false;
//					    bvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class,"pk_datadefinit_h='"+pk_credence_h+"'");
//	//				    hvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class,hvo.getCitetable());
//				    	hvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class,"pk_datadefinit_h='"+hvo.getCitetable()+"'");
//				    }
				}
//			}
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
		if (ishead==TYPE_HEAD) {
			dlg.setFormula(billUI.getBillCardPanel().getHeadItem(savefieldname).getValue());
		}else{
			dlg.setFormula(rf.getText());
		}
		dlg.setBillItems(defvo);
//		dlg.setPk_sys(pk_sys);
		dlg.showModal();
		
		if(dlg.OK==1){//如果是确认按钮
			String tmpString = dlg.getFormula();
			rskey = tmpString;
			if(ishead==TYPE_TAIL){
				billUI.getBillCardPanel().getHeadItem(savefieldname).setValue(rskey);
				rf.setText(rskey);
			}else if (ishead==TYPE_HEAD) {
//				BillItem item12 = billUI.getBillCardPanel().getHeadItem(deffilename);// 加工条件
//				if (item12 != null) {
//					UIRefPane ref = (UIRefPane) item12.getComponent();
					billUI.getBillCardPanel().getHeadItem(savefieldname).setValue(rskey);
					rf.setText(rskey);
//				}
			}else{
//				int row=billUI.getBillCardPanel().getBillTable().getSelectedRow();
				int ro1=billUI.getBillCardWrapper().getBillCardPanel().getBillTable().getSelectedRow();
//				int c1=billUI.getBillCardPanel().getBillTable().getSelectedColumn();
				billUI.getBillCardPanel().setBodyValueAt(rskey, ro1, savefieldname);
//				int t=row+ro1+c1;
//				System.out.println(t+"");
				rf.setText(rskey);
////				BillItem item12=billUI.getBillCardPanel().getBodyItem(deffilename);
//				billUI.getBillCardWrapper().getSelectedBodyVOs();.getBillCardPanel().getBillModel().getBillModelData()..getBodyItem(savefieldname).setValue(rskey);
////				billUI.getBillCardPanel().setBodyValueAt(rskey, billUI.getMouseListeners(), );
////				billUI.getBillCardPanel().setBodyValueAt(rskey, 0, savefieldname);
			}
		}
		dlg.OK=0;
		dlg.destroy();

	}

}