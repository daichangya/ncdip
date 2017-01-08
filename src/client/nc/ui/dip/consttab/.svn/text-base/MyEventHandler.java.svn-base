package nc.ui.dip.consttab;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.card.BillCardUI;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.consttab.DipConsttabVO;

/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author 张进双
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillCardUI billUI, ICardController control){
		super(billUI,control);		
	}
    /*
     * (non-Javadoc)NC接收服务器校验
     * lx  2011-5-27
     * @see nc.ui.trade.manage.ManageEventHandler#onBoSave()
     */
	@Override
	protected void onBoSave() throws Exception {
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<k;i++){
			String descs=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DipConsttabVO().DESCS);
			String code=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DipConsttabVO().CODE);
			String name=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DipConsttabVO().NAME);

			if((code==null||code.trim().equals(""))&&(name==null||name.trim().equals(""))&&(descs==null||descs.trim().equals(""))){
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
		if(bd!=null){
			bd.dataNotNullValidate();
		}
		super.onBoSave();
	}
	//NC接收服务器：判断是否被引用（在数据发送的目标服务器），如果被引用则不能删除 lx 2011-5-27
	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
			Map<String,String> map=IContrastUtil.getDocRefMap();
			String key="dip_consttab";
			String value=map.get(key);
			int bodycount=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
			if(bodycount>=0){
				String pk=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new DipConsttabVO().getPKFieldName().toLowerCase())==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new DipConsttabVO().getPKFieldName().toLowerCase()).toString();;
				String isref=SJUtil.isExitRef(value,pk);
				if(isref!=null&&isref.length()>0){
					getBillUI().showErrorMessage("此条数据被引用，不可删除！");
					return ;
				}
				Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(),"提示","是否要删除?");
				if(flag==1){
				    this.onBoLineDel();
				    HYPubBO_Client.deleteByWhereClause(DipConsttabVO.class,new DipConsttabVO().getPKFieldName().toLowerCase()+"='"+pk+"'");
				}
			}else{
				getBillUI().showErrorMessage("请选择要删除的节点！");
				return;
			}
//		super.onBoDelete();
	}		
	@Override
	public void onBoAdd(ButtonObject arg0) throws Exception {
		// TODO Auto-generated method stub
		int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		if(rows>0){
			super.onBoEdit();
			super.onBoLineAdd();
		}else{
			super.onBoAdd(arg0);
			super.onBoLineAdd();
		}
	}
	/*
	 * 打开节点，显示所有表体数据
	 * 2011-06-13
	 * zlc*/
	public void ini(){
		try {
			doBodyQuery(" 1=1 order by code");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
		onBoLineAdd();
	}
}