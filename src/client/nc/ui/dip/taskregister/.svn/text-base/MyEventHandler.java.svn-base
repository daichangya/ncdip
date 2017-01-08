package nc.ui.dip.taskregister;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.taskregister.DipTaskregisterVO;

/**
 *
 *该类是AbstractMyEventHandler抽象类的实现类，
 *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 *@author author
 *@version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler   {

	protected  TaskRegisterClientUI  ui=null;
	public MyEventHandler(BillCardUI billUI, ICardController control){
		super(billUI,control);
//		ui = (TaskRegisterClientUI)billUI;
	}
	/*
	 * 判断参照是否被引用
	 * lx 2011-5-28
	 */
	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
//		Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(),"提示","是否要删除?");
//		if(flag==1){
			Map<String,String> map=IContrastUtil.getDocRefMap();
			String key="dip_taskregister";
			String value=map.get(key);
			int bodycount=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
			if(bodycount>=0){
				Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new DipTaskregisterVO().ISSYSPREF);
				if(flag!=null&&flag){
					getBillUI().showErrorMessage("系统预置不允许删除！");
					return;
				}
				String pk=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new DipTaskregisterVO().getPKFieldName().toLowerCase())==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new DipTaskregisterVO().getPKFieldName().toLowerCase()).toString();
				String isref=SJUtil.isExitRef(value,pk);
				if(isref!=null&&isref.length()>0){
					getBillUI().showErrorMessage("此条数据被引用，不可删除！");
					return ;
				}
				int i=MessageDialog.showOkCancelDlg(this.ui, "提示", "是否确定删除！");
				//是=1
				if(i==1){
					this.onBoLineDel();
					HYPubBO_Client.deleteByWhereClause(DipTaskregisterVO.class,new DipTaskregisterVO().getPKFieldName().toLowerCase()+"='"+pk+"'");
					setSelectRow(0, 0);
				}
				
			}else{
				getBillUI().showErrorMessage("请选择要删除的节点！");
				return;
			}
//		}
//		super.onBoDelete();
	}
	//保存校验 lx 2011-5-28
	@Override
	protected void onBoSave() throws Exception {
		
		// TODO Auto-generated method stub
//		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
//		if(bd!=null){
//			bd.dataNotNullValidate();
//		}
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<k;i++){
			Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DipTaskregisterVO().ISSYSPREF);
			String code=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DipTaskregisterVO().CODE);
			String name=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DipTaskregisterVO().NAME);
			if((flag==null||flag==false)&&(code==null||code.trim().equals(""))&&(name==null||name.trim().equals(""))){
//				getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(i, 0, false, false);
//				getBillCardPanelWrapper().getBillCardPanel().delLine();
				list.add(i);
			}else{
				if(flag==null){
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(false, i, new DipTaskregisterVO().ISSYSPREF);
				}
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
		if(bd!=null){
			bd.dataNotNullValidate();
		}
//		TaskRegisterClientUI ui=(TaskRegisterClientUI) getBillUI();
//		String pk=ui.getBillCardPanel().getBodyItem("pk_taskregister").getValueObject().toString();
//		if(pk==null||pk.trim().equals("")){
//			pk=" ";
//		}
//		String code=ui.getBillCardPanel().getBodyItem("code").getValueObject().toString();
//		IUAPQueryBS bs=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//
//		Collection tion=bs.retrieveByClause(DipTaskregisterVO.class, "code='"+code+"' and pk_taskregister<>'"+pk+"' and nvl(dr,0)=0");
//		if(tion!=null){
//			if(tion.size()>0){
//				ui.showWarningMessage("编码不能重复！");
//				return;
//			}
//		}
		super.onBoSave();
		TaskRegisterClientUI.delFlag=true;
		ini();
		setSelectRow(0, 0);
		
	}
	@Override
	protected void onBoEdit() throws Exception {
		
		   TaskRegisterClientUI.delFlag=false;
//			String sys = (String)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("issyspref").getValueObject();
//			if(sys.equals("true")){
//				ui.showWarningMessage("系统预置标志，不可修改！");
//				return;
//			}else{
				super.onBoEdit();
				//DipTaskregisterVO[] vo=(DipTaskregisterVO[]) getBillCardPanelWrapper().getSelectedBodyVOs();
				//getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(arg0, arg1)
				//if(vo[0].getIssyspref().booleanValue()){
				//	ui.showErrorMessage("不可以修改！");
				//}
//			}
				
				int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
				DipTaskregisterVO vo=new DipTaskregisterVO();
				for(int i=0;i<k;i++){
					Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, vo.ISSYSPREF);
					if(flag!=null&&flag){
						getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,vo.CODE,false);
						getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.NAME, false);
						getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.ISSYSPREF, false);
					}
				}
				
				onBoLineAdd();
				getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
				((TaskRegisterClientUI)getBillUI()).updateButtonUI();
				
		}
	@Override
	public void onBoAdd(ButtonObject arg0) throws Exception {
		TaskRegisterClientUI.delFlag=false;
//		getBufferData();
		int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		if(rows>0){
//			super.
			onBoEdit();
//			super.onBoLineAdd();
		}else{
			super.onBoAdd(arg0);
			super.onBoLineAdd();
		}
		getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
		getBillUI().updateButtonUI();
	}
	public void ini() {
		try {
			
			doBodyQuery(" 1=1  order by code");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*MyBillVO my=new MyBillVO();
		try {
			DipTaskregisterVO[] b=(DipTaskregisterVO[]) HYPubBO_Client.queryByCondition(DipTaskregisterVO.class, "nvl(dr,0)=0");
			my.setChildrenVO(b);
			getBufferData().clear();
//			getBufferData().addVOsToBuffer(new MyBillVO[]{my});
			getBufferData().addVOToBuffer(my);
//			getBufferData().setCurrentVO(my);
//			getBillUI().updateUI();
			try {
				updateBuffer();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			getBufferData().setCurrentRow(0);/
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}

	public void setSelectRow(int row,int col){
		if(getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount()>0){
			getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(row, col, false, false);
		}
	}
	
	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		
		TaskRegisterClientUI.delFlag=true;
		super.onBoCancel();
		ini();
		setSelectRow(0, 0);
		
	}
}