package nc.ui.dip.statusregist;

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
import nc.vo.dip.statusregist.StatusregistVO;

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
    
	
	/**
	 * 取得当前UI类
	 * 
	 * @return
	 */
	private StatusRegistClientUI getSelfUI() {
		return (StatusRegistClientUI) getBillUI();
	}

	
	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		
//		增行
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<k;i++){
			Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new StatusregistVO().ISSTACK);
			String code=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new StatusregistVO().CODE);
			String name=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new StatusregistVO().NAME);

			if((code==null||code.trim().equals(""))&&(name==null||name.trim().equals(""))&&(flag==null||flag==false)){
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
		
		/*StatusRegistClientUI ui=(StatusRegistClientUI)getBillUI();
		String pk=(String)ui.getBillCardPanel().getBodyItem("pk_statusregist").getValueObject();
		if(pk==null || pk.trim().equals("")){
			pk=" ";
		}
		String code=(String)ui.getBillCardPanel().getBodyItem("code").getValueObject();
		String name=(String)ui.getBillCardPanel().getBodyItem("name").getValueObject();
		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		Collection ccode=bs.retrieveByClause(StatusregistVO.class, "code='"+code+"' and nvl(dr,0)=0 and pk_statusregist <>'"+pk+"'");
		if(ccode !=null){
			if(ccode.size()>=1){
				ui.showWarningMessage("该【"+code+"】编码已经存在！");
				return;
			}
		}
		
		Collection cname=bs.retrieveByClause(StatusregistVO.class, "name='"+name+"' and nvl(dr,0)=0 and pk_statusregist <>'"+pk+"'");
		if(cname !=null){
			if(cname.size()>=1){
				ui.showWarningMessage("该【"+name+"】名称已经存在！");
				return;
			}
		}*/
		super.onBoSave();
	}
    /*
     * (non-Javadoc)如果数据已经被引用，则不能删除
     * lx 2011-5-27
     */
	@Override
	protected void onBoDelete() throws Exception {
		
			Map<String,String> map=IContrastUtil.getDocRefMap();
			String key="dip_statusregist";
			String value=map.get(key);
			int bodycount=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
			if(bodycount>=0){
				String pk=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new StatusregistVO().getPKFieldName().toLowerCase())==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new StatusregistVO().getPKFieldName().toLowerCase()).toString();
				String isref=SJUtil.isExitRef(value,pk);
				if(isref!=null&&isref.length()>0){
					getBillUI().showErrorMessage("此条数据被引用，不可删除！");
					return ;
				}
				Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(),"提示","是否要删除?");
				if(flag==1){
				    this.onBoLineDel();
				    HYPubBO_Client.deleteByWhereClause(StatusregistVO.class,new StatusregistVO().getPKFieldName().toLowerCase()+"='"+pk+"'");
			
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
	public void init(){
		try {
			doBodyQuery(" 1=1 order by code");
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
		init();
	}
		
	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		super.onBoEdit();
		onBoLineAdd();
	}
}