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

public class CredenceListener implements ActionListener {
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
	 *            ��ǰ�����ui
	 * @param deffilename
	 *            ��������ʾ������α�����ֶε��Ǹ��ֶ���
	 * @param savefieldame
	 *            ʵ�����ݿⱣ���ֶε��ֶ���
	 */
	public CredenceListener(DataChangeClientUI ui, 
			String savefieldname, int ishead,UIRefPane rf) {
		this.billUI = ui;
		this.savefieldname = savefieldname;
		this.ishead = ishead;
		this.rf=rf;
	}

	public void actionPerformed(ActionEvent e) {
//		String pk_credence_h=billUI.getPk_credence_h();
		String pk_datadef=(String) billUI.getBillCardPanel().getHeadItem("sourcetab").getValueObject();
		DipDatadefinitBVO[] bvos = null;
		DipDatadefinitBVO[] hvos=null;
		String pk_sys="";
		//��ͷ��־
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
				//���±�־�Ǳ�ͷ���Ǳ��壬��ͷdefvo[i].setHeadflag(new UFBoolean(true));������defvo[i].setHeadflag(new UFBoolean(false))
				defvo[i].setHeadflag(new UFBoolean(false));
				defvo[i].setItemname(bvoi.getCname());
				defvo[i].setPrimaryKey(bvoi.getPrimaryKey());
				// TODO wyd ���typeֵ��Ҫ�༭
				defvo[i].setItemtype(0);
				i++;
			}
		}
		if(!SJUtil.isNull(hvos)){
			for(DipDatadefinitBVO hvoi:hvos){
				defvo[i] = new DefitemVO();
				defvo[i].setAttrname(hvoi.getEname());
				//���±�־�Ǳ�ͷ���Ǳ��壬��ͷdefvo[i].setHeadflag(new UFBoolean(true));������defvo[i].setHeadflag(new UFBoolean(false))
				defvo[i].setHeadflag(new UFBoolean(true));
				defvo[i].setItemname(hvoi.getCname());
				defvo[i].setPrimaryKey(hvoi.getPrimaryKey());
				// TODO wyd ���typeֵ��Ҫ�༭
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
		
		if(dlg.OK==1){//�����ȷ�ϰ�ť
			String tmpString = dlg.getFormula();
			rskey = tmpString;
				int ro1=billUI.getBillCardWrapper().getBillCardPanel().getBillTable().getSelectedRow();
				billUI.getBillCardPanel().setBodyValueAt(rskey, ro1, savefieldname);
				rf.setText(rskey);
		}
		dlg.OK=0;
		dlg.destroy();

	}

}