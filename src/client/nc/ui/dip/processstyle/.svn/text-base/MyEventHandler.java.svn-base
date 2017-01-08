package nc.ui.dip.processstyle;

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
import nc.vo.dip.datacheck.DataCheckVO;
import nc.vo.dip.processstyle.ProcessstyleVO;


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
			Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new ProcessstyleVO().DEF_STR_1);
			String code=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new ProcessstyleVO().CODE);
			String name=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new ProcessstyleVO().NAME);
			Boolean iscon=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new ProcessstyleVO().ISCON);
			Boolean iscreate=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new ProcessstyleVO().ISCREATE);
			String impclass=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new ProcessstyleVO().IMPCLASS);
			if((flag==null||flag==false)&&(code==null||code.trim().equals(""))&&(name==null||name.trim().equals(""))&&(iscon==null||iscon==false)&&(iscreate==null||iscreate==false)&&(impclass==null||impclass.trim().equals(""))){
//				getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(i, 0, false, false);
//				getBillCardPanelWrapper().getBillCardPanel().delLine();
				list.add(i);
			}else{
				if(flag==null){
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(false, i ,new ProcessstyleVO().DEF_STR_1);
				}
				if(iscon==null){
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(false, i ,new ProcessstyleVO().ISCON);
				}
				if(iscreate==null){
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(false, i ,new ProcessstyleVO().ISCREATE);
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
//		ProcessStyleClientUI ui=(ProcessStyleClientUI)getBillUI();
//		String pk=(String)ui.getBillCardPanel().getBodyItem("pk_processstype").getValueObject();
//		if(pk==null || pk.trim().equals("")){
//			pk=" ";
//		}
//		String code=(String)ui.getBillCardPanel().getBodyItem("code").getValueObject();
//		String name=(String)ui.getBillCardPanel().getBodyItem("name").getValueObject();
//		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//
//		Collection ccode=bs.retrieveByClause(ProcessstyleVO.class, "code='"+code+"' and nvl(dr,0)=0 and pk_processstype <>'"+pk+"'");
//		if(ccode !=null){
//			if(ccode.size()>=1){
//				ui.showWarningMessage("该【"+code+"】编码已经存在！");
//				return;
//			}
//		}
//		Collection cname=bs.retrieveByClause(ProcessstyleVO.class, "name='"+name+"' and nvl(dr,0)=0 and pk_processstype <>'"+pk+"'");
//		if(cname !=null){
//			if(cname.size()>=1){
//				ui.showWarningMessage("该【"+name+"】名称已经存在！");
//				return;
//			}
//		}
		super.onBoSave();
		ProcessStyleClientUI.delFlag=true;
		ini();
		setSelectRow(0, 0);
	}
	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
//		Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(),"提示","是否要删除");
//		if(flag==1){
			Map<String,String> map=IContrastUtil.getDocRefMap();
			String key="dip_processstyle";
			String value=map.get(key);
			int bodycount=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
			if(bodycount>=0){
				//将自定义1改为是否系统预置字段，若是系统预置字段 不可删除   2011-07-06   zlc  向下五行  
				Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new DataCheckVO().DEF_STR_1);
				if(flag!=null&&flag){
					getBillUI().showErrorMessage("系统预置不允许删除！");
					return;
				}
				String pk=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount,new ProcessstyleVO().getPKFieldName().toLowerCase())==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount,new ProcessstyleVO().getPKFieldName().toLowerCase()).toString();

				String isy=SJUtil.isExitRef(value, pk);
				if(isy!=null&&isy.length()>0){
					getBillUI().showWarningMessage("该数据已经被引用，不能删除！");
					return;
				}
				int i=MessageDialog.showOkCancelDlg(this.getBillUI(), "提示", "是否确定删除！");
				if(i==1){
					this.onBoLineDel();
					HYPubBO_Client.deleteByWhereClause(ProcessstyleVO.class,new ProcessstyleVO().getPKFieldName().toLowerCase()+"='"+pk+"'");
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
		ProcessStyleClientUI.delFlag=false;
		int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		if(rows>0){
			onBoEdit();
//			super.onBoLineAdd();
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
		//2011-6-24 在刷新前调用ini()方法，避免弹出"当前的单据已经被删除了，是否需要在界面上保留这份数据？"询问框
		ini();
		super.onBoRefresh();
		//刷新后，再次调用ini()方法，避免界面上无值显示
		ini();
	}

	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		ProcessStyleClientUI.delFlag=false;
		super.onBoEdit();
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		ProcessstyleVO vo=new ProcessstyleVO();
		for(int i=0;i<k;i++){
			Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, vo.DEF_STR_1);
			if(flag!=null&&flag){
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,vo.CODE,false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.NAME, false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,vo.ISCON,false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.ISCREATE, false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,vo.IMPCLASS,false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.DEF_STR_1, false);
			}
		}
		onBoLineAdd();
	}
	
	public void setSelectRow(int row,int col){
		if(getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount()>0){
			getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(row, col, false, false);
		}
	}
	
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		ProcessStyleClientUI.delFlag=true;
		super.onBoCancel();
		ini();
		setSelectRow(0, 0);
	}
}