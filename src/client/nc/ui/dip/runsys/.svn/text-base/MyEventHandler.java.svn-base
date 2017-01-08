package nc.ui.dip.runsys;

import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.vo.dip.runsys.DipRunsysBVO;

/**
 *
 *该类是AbstractMyEventHandler抽象类的实现类，
 *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 *@author author
 *@version tempProject version
 */

public class MyEventHandler 
extends AbstractMyEventHandler{
	BillCardUI billUI;

	ICardController control;
	public MyEventHandler(BillCardUI billUI, ICardController control){
		super(billUI,control);	
		this.billUI=billUI;
		this.control=control;
	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		if(rows>0){
			super.onBoEdit();
			super.onBoLineAdd();
		}else{
			super.onBoAdd(bo);
			super.onBoLineAdd();
		}
	}

//	@Override
//	protected void onBoEdit() throws Exception {
//		super.onBoEdit();
//	}

	@Override
	protected void onBoQuery() throws Exception {
		super.onBoQuery();
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		
//		增行
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<k;i++){
			
			String code=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DipRunsysBVO().SYSCODE);
			String name=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DipRunsysBVO().SYSNAME);
			String value=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DipRunsysBVO().SYSVALUE);
			String remark=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DipRunsysBVO().REMARK);
			if((code==null||code.trim().equals(""))&&(name==null||name.trim().equals(""))&&(value==null||value.trim().equals(""))&&(remark==null||remark.trim().equals(""))){
//				getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(i, 0, false, false);
//				getBillCardPanelWrapper().getBillCardPanel().delLine();
				list.add(i);
			}
		}
		if(list!=null&&list.size()>0){
			int in[]=new int[list.size()];
			for(int i=0;i<list.size();i++){
				in[i]=list.get(i);
			}
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().delLine(in);
		}
		
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd !=null){
			bd.dataNotNullValidate();
		}
		/*//表头重复校验
		RunSysClientUI ui=(RunSysClientUI)getBillUI();
		String pk=(String)ui.getBillCardPanel().getHeadItem("pk_runsys_h").getValueObject();
		if(pk==null || pk.trim().equals("")){
			pk=" ";
		}
		String syscode=(String)ui.getBillCardPanel().getHeadItem("syscode").getValueObject();
		String sysname=(String)ui.getBillCardPanel().getHeadItem("sysname").getValueObject();

		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());

		Collection ccode=bs.retrieveByClause(DipRunsysHVO.class, "syscode='"+syscode+"' and nvl(dr,0)=0 and pk_runsys_h <>'"+pk+"'");
		if(ccode !=null){
			if(ccode.size()>=1){
				ui.showWarningMessage("该【"+syscode+"】编码已经存在！");
				return;
			}
		}
		Collection cname=bs.retrieveByClause(DipRunsysHVO.class, "sysname='"+sysname+"' and nvl(dr,0)=0 and pk_runsys_h <>'"+pk+"'");
		if(cname !=null){
			if(cname.size()>=1){
				ui.showWarningMessage("该【"+sysname+"】名称已经存在！");
				return;
			}
		}*/
		super.onBoSave();
	}

	@Override
	protected void onBoDelete() throws Exception {
		int rowcount=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		if(rowcount>0){
			int row=getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().getTable().getSelectedRow();
			if(row==-1){
				getBillUI().showWarningMessage("请选择要删除的数据!");
				return;
			}
			String pk=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(row, new DipRunsysBVO().getPKFieldName().toLowerCase())==null?"":getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(row, new DipRunsysBVO().getPKFieldName().toLowerCase()).toString();
			int i=MessageDialog.showOkCancelDlg(this.getBillUI(), "提示", "是否确定删除！");
			if(i==1){

			super.onBoLineDel();
			HYPubBO_Client.deleteByWhereClause(DipRunsysBVO.class,new DipRunsysBVO().getPKFieldName().toLowerCase()+"='"+pk+"'");
			}
		}else{
			getBillUI().getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
			getBillUI().updateButtons();
		}
	}

	public void init() throws Exception{
		doBodyQuery(" 1=1 order by syscode");
	}
	
	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		super.onBoEdit();
		onBoLineAdd();
	}
}