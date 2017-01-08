package nc.ui.dip.messagelogo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.card.BillCardUI;
import nc.util.dip.sj.FunctionUtil;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.messagelogo.MessagelogoVO;

/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillCardUI billUI, ICardController control){
		super(billUI,control);		
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<k;i++){
			String code=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, MessagelogoVO.CODE);
			String name=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i,  MessagelogoVO.CNAME);
			String type=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i,  MessagelogoVO.CTYPE);
			String data=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i,  MessagelogoVO.CDATA);
			if((code==null||code.trim().equals(""))&&(name==null||name.trim().equals(""))&&(type==null||type.trim().equals(""))&&(data==null||data.trim().equals(""))){
//				getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(i, 0, false, false);
//				getBillCardPanelWrapper().getBillCardPanel().delLine();
				list.add(i);
			}
			if(type!=null&&type.length()>0&&type.equals("时间函数")){
				if(data==null||data.length()<=0){
					getBillUI().showErrorMessage("第"+(i+1)+"行时间函数数值不能为空");
					return;
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
		if(bd !=null){
			bd.dataNotNullValidate();
		}
		super.onBoSave();
		ini();
	}
	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		
			Map<String,String> map=IContrastUtil.getDocRefMap();
			String key="dip_messagelogo";
			String value=map.get(key);
			int bodycount=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
			if(bodycount>=0){
				String pk=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new MessagelogoVO().getPKFieldName().toLowerCase())==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new MessagelogoVO().getPKFieldName().toLowerCase()).toString();
				String isref=SJUtil.isExitRef(value,pk);
				if(isref!=null&&isref.length()>0){
					getBillUI().showErrorMessage("此条数据被引用，不可删除！");
					return ;
				}
				Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(),"提示","是否要删除?");
				if(flag==1){
				    this.onBoLineDel();
				    HYPubBO_Client.deleteByWhereClause(MessagelogoVO.class,new MessagelogoVO().getPKFieldName().toLowerCase()+"='"+pk+"'");
				}
			}else{
				getBillUI().showErrorMessage("请选择要删除的节点！");
				return;
			}
//		super.onBoDelete();
			ini();
	}		

	@Override
	public void onBoAdd(ButtonObject arg0) throws Exception {
		// TODO Auto-generated method stub
		int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
//		if(rows>0){
			onBoEdit();
//			super.onBoLineAdd();
//		}else{
//			super.onBoAdd(arg0);
//			super.onBoLineAdd();
//		}
	}	
	public void ini(){
		try {
			doBodyQuery(" 1=1 order by code ");
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
	}
	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
		ini();
	}
	
	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		super.onBoEdit();
		onBoLineAdd();
		int row=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
		if(row>=0){
			for(int i=0;i<row;i++){
				Object type=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, "ctype");
				if(type!=null){
					for(int w=0;w<FunctionUtil.NOTEDITSTRS.length;w++){
						if(type.equals(FunctionUtil.NOTEDITSTRS[w])){
							getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "cdata", false);
							continue;
						}
					}
				}
//				else{
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(null, row, "cdata");
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", true);
//				}	
			}	
		}
			
		
	}
	
	
}