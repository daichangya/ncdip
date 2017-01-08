package nc.ui.dip.obtainsign;

import java.util.Map;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.card.BillCardUI;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.obtainsign.DipObtainsignVO;

/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler extends AbstractMyEventHandler{
	BillCardUI billUI;

	ICardController control;
	public MyEventHandler(BillCardUI billUI, ICardController control){
		super(billUI,control);	
		this.billUI=billUI;
		this.control=control;
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd !=null){
			bd.dataNotNullValidate();
		}
		//表头重复校验
//		ObtainSignClientUI ui=(ObtainSignClientUI)getBillUI();
//		String pk=(String)ui.getBillCardPanel().getBodyItem("pk_obtainsign").getValueObject();
//		if(pk==null || pk.trim().equals("")){
//			pk=" ";
//		}
//		String code=(String)ui.getBillCardPanel().getBodyItem("code").getValueObject();
//		String name=(String)ui.getBillCardPanel().getBodyItem("name").getValueObject();
//		
//		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//		Collection ccode=bs.retrieveByClause(DipObtainsignVO.class, "code='"+code+"' and nvl(dr,0)=0 and pk_obtainsign <>'"+pk+"'");
//		if(ccode !=null){
//			if(ccode.size()>=1){
//				ui.showWarningMessage("该【"+code+"】编码已经存在！");
//				return;
//			}
//		}
//		Collection cname=bs.retrieveByClause(DipObtainsignVO.class, "name='"+name+"' and nvl(dr,0)=0 and pk_obtainsign <>'"+pk+"'");
//		if(cname !=null){
//			if(cname.size()>=1){
//				ui.showWarningMessage("该【"+name+"】名称已经存在！");
//				return;
//			}
//		}
		super.onBoSave();
	}

	/*删除判断，如果被引用不可删除
	 * 2011-5-28
	 * zlc*/
	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
//		Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(),"提示","是否要删除?");
//		if(flag==1){
			Map<String,String> map=IContrastUtil.getDocRefMap();
			String key="dip_obtainsign";
			String value=map.get(key);
			int bodycount=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
			if(bodycount>=0){
				String pk=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new DipObtainsignVO().getPKFieldName().toLowerCase())==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new DipObtainsignVO().getPKFieldName().toLowerCase()).toString();
				String isref=SJUtil.isExitRef(value,pk);
				if(isref!=null&&isref.length()>0){
					getBillUI().showErrorMessage("此条数据被引用，不可删除！");
					return ;
				}
				this.onBoLineDel();
				HYPubBO_Client.deleteByWhereClause(DipObtainsignVO.class,new DipObtainsignVO().getPKFieldName().toLowerCase()+"='"+pk+"'");
			}else{
				getBillUI().showErrorMessage("请选择要删除的节点！");
				return;
			}
//		}
//		super.onBoDelete();
	}

	@Override
	public void onBoAdd(ButtonObject arg0) throws Exception {
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
	 * 打开节点，显示所有数据
	 * 2011-06-13
	 * zlc*/
	public void ini(){
		try {
			doBodyQuery("");
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
	
}