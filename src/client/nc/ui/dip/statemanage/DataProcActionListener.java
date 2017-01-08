package nc.ui.dip.statemanage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;


import nc.ui.dip.procondition.ProconditionClientUI;
import nc.ui.dip.procondition.ProconditionDlg;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.processstyle.ProcessstyleVO;
import nc.vo.pub.billtype.DefitemVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.rs.CharClob;
/**
 * 数据加工的监听类
 * */
public class DataProcActionListener implements ActionListener {
	StateManageUI ui;
	Map<String,ProcessstyleVO> map=new HashMap<String, ProcessstyleVO>();
	BillItem item;
	public DataProcActionListener(StateManageUI ui,BillItem item){
		this.item=item;
		this.ui=ui;
		try {
			ProcessstyleVO[] vo=(ProcessstyleVO[]) HYPubBO_Client.queryByCondition(ProcessstyleVO.class, "nvl(dr,0)=0");
			if(vo!=null&&vo.length>0){
				for(ProcessstyleVO voi:vo){
					map.put(voi.getPrimaryKey(), voi);
				}
			}
		} catch (UifException e) {
			e.printStackTrace();
		}
	}
	public void actionPerformed(ActionEvent e) {
		if(item==null){
			return;
		}else
		{
				Object pk=ui.getBillCardPanel().getHeadItem("firsttab").getValueObject();
					ui.transferFocus();
					ProFormuDefUI dlg = new ProFormuDefUI(ui,ui.getBillCardPanel().getHeadItem("pk_xt").getValueObject().toString());
					dlg.setFormula(ui.getBillCardPanel().getHeadItem("procecond").getValueObject().toString());
					dlg.showModal();
					if(dlg.OK==1){
						String tmpString = dlg.getFormula();
						ui.getBillCardPanel().getHeadItem("procecond").setValue(tmpString);
						ui.getBillCardPanel().getHeadItem("refprocecond").setValue("条件保存在下边");
						ui.setLob(tmpString);
//						ui.getBillCardPanel().getHeadItem("proclob").setValue(new CharClob(tmpString.toCharArray()));
					}
		}

	
	
	}

}
