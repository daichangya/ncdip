package nc.ui.dip.messtype;

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
import nc.vo.dip.messtype.MesstypeVO;


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
			Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new MesstypeVO().ISSYSPREF);
			String code=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new MesstypeVO().CODE);
			String name=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new MesstypeVO().NAME);
			String princ=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i,"princ");			
			if((flag==null||flag==false)&&(code==null||code.trim().equals(""))&&(name==null||name.trim().equals(""))&&(princ==null||princ.trim().equals(""))){
//				getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(i, 0, false, false);
//				getBillCardPanelWrapper().getBillCardPanel().delLine();
				list.add(i);
			}else{
			   if(flag==null){
				   getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(false, i, new MesstypeVO().ISSYSPREF);
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
		MessTypeClientUI ui=(MessTypeClientUI)getBillUI();
		String pk=(String)ui.getBillCardPanel().getBodyItem("pk_messtype").getValueObject();
		if(pk==null || pk.trim().equals("")){
			pk=" ";
		}
//		String code=(String)ui.getBillCardPanel().getBodyItem("code").getValueObject();
//		String name=(String)ui.getBillCardPanel().getBodyItem("name").getValueObject();
//		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//
//		Collection ccode=bs.retrieveByClause(MesstypeVO.class, "code='"+code+"' and nvl(dr,0)=0 and pk_messtype <>'"+pk+"'");
//		if(ccode !=null){
//			if(ccode.size()>=1){
//				ui.showWarningMessage("该【"+code+"】编码已经存在！");
//				return;
//			}
//		}
//		Collection cname=bs.retrieveByClause(MesstypeVO.class, "name='"+name+"' and nvl(dr,0)=0 and pk_messtype <>'"+pk+"'");
//		if(cname !=null){
//			if(cname.size()>=1){
//				ui.showWarningMessage("该【"+name+"】名称已经存在！");
//				return;
//			}
//		}
		super.onBoSave();
		MessTypeClientUI.delFlag=true;
		ini();
		setSelectRow(0, 0);
	}
	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
//		Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(),"提示","是否要删除?");
//		if(flag==1){
			Map<String,String> map=IContrastUtil.getDocRefMap();
			String key="dip_messtype";
			String value=map.get(key);
			int bodycount=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
			if(bodycount>=0){
				Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new MesstypeVO().ISSYSPREF);
				if(flag!=null&&flag){
					getBillUI().showErrorMessage("系统预置不允许删除！");
					return;
				}
				String pk=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new MesstypeVO().getPKFieldName().toLowerCase())==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new MesstypeVO().getPKFieldName().toLowerCase()).toString();
				String isref=SJUtil.isExitRef(value,pk);
				if(isref!=null&&isref.length()>0){
					getBillUI().showErrorMessage("此条数据被引用，不可删除！");
					return ;
				}
				int i=MessageDialog.showOkCancelDlg(this.getBillUI(), "提示", "是否确定删除！");
				//是=1
			if(i==1){
				this.onBoLineDel();
				HYPubBO_Client.deleteByWhereClause(MesstypeVO.class,new MesstypeVO().getPKFieldName().toLowerCase()+"='"+pk+"'");
				setSelectRow(0, 0);
			}
			}else{
				getBillUI().showErrorMessage("请选择要删除的节点！");
				return;
			}
//		}
		
//		super.onBoDelete();
	}		

	@Override
	public void onBoAdd(ButtonObject arg0) throws Exception {
		// TODO Auto-generated method stub
		MessTypeClientUI.delFlag=false;
		int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		if(rows>0){
			super.onBoEdit();
			super.onBoLineAdd();
		}else{
			super.onBoAdd(arg0);
			super.onBoLineAdd();
		}
//		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		MesstypeVO vo=new MesstypeVO();
		for(int i=0;i<rows;i++){
			Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, vo.ISSYSPREF);
			if(flag!=null&&flag){
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,vo.CODE,false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.NAME, false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.ISSYSPREF, false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "princ", false);
			}
		}
		getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
		getBillUI().updateButtonUI();
	}
	/*
	 * 打开节点，显示所有数据
	 * 2011-06-13
	 * zlc*/
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
	

//	@Override
//	protected void onBoRefresh() throws Exception {
//		//2011-6-24 在刷新前调用ini()方法，避免弹出"当前的单据已经被删除了，是否需要在界面上保留这份数据？"询问框
//		ini();
//		super.onBoRefresh();
//		//刷新后，再次调用ini()方法，避免界面上无值显示
//		ini();
//	}
	
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		MessTypeClientUI.delFlag=false;
		super.onBoEdit();
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		MesstypeVO vo=new MesstypeVO();
		for(int i=0;i<k;i++){
			Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, vo.ISSYSPREF);
			if(flag!=null&&flag){
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,vo.CODE,false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.NAME, false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.ISSYSPREF, false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "princ", false);
			}
		}
		onBoLineAdd();
	}
	
	public void setSelectRow(int row,int col){
		if(getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount()>0){
			getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(row, col, false, false);
		}
	}
	
	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		MessTypeClientUI.delFlag=true;
		super.onBoCancel();
		ini();
		setSelectRow(0, 0);
	}
	

}