package nc.ui.dip.datacheck;

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
	
	protected DataCheckClientUI ui;
	public MyEventHandler(BillCardUI billUI, ICardController control){
		super(billUI,control);	
		this.billUI=billUI;
		this.control=control;
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<k;i++){
			Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DataCheckVO().DEF_STR_1);
			String name=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DataCheckVO().NAME);
			String code=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DataCheckVO().CODE);
			String type=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DataCheckVO().TYPE);
			String implementss=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DataCheckVO().IMPLEMENTS);
			String miaosu=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DataCheckVO().MIAOSU);
			
			if((flag==null||flag==false)&&(code==null||code.trim().equals(""))&&(name==null||name.trim().equals(""))&&(type==null||type.trim().equals(""))&&(implementss==null||implementss.trim().equals(""))&&(miaosu==null||miaosu.trim().equals(""))){
				list.add(i);
			}else{
				if(flag==null){
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(false, i,new DataCheckVO().DEF_STR_1);
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
		/*//此处增加表头重复校验：数据类型编码、名称
		DataCheckClientUI ui=(DataCheckClientUI)getBillUI();
		//主键
		String pk=(String)ui.getBillCardPanel().getBodyItem("pk_dip_datacheckregist").getValueObject();
		if(pk ==null || pk.trim().equals("")){
			pk=" ";//保存时避免重复的可以添加
		}*/
		
//		String code=(String)ui.getBillCardPanel().getBodyItem("code").getValueObject();
//		String name=(String)ui.getBillCardPanel().getBodyItem("name").getValueObject();
//		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//		
//		Collection ccode=bs.retrieveByClause(DataCheckVO.class, "code='"+code+"' and nvl(dr,0)=0 and pk_dip_datacheckregist <>'"+pk+"'");
//		
//		if(ccode !=null){
//			if(ccode.size()>=1){
//				ui.showWarningMessage("该【"+code+"】编码已经存在！");
//				return;
//			}
//		}
//		
//		Collection cname=bs.retrieveByClause(DataCheckVO.class, "name='"+name+"' and nvl(dr,0)=0 and pk_dip_datacheckregist <>'"+pk+"'");
//		if(cname !=null){
//			if(cname.size()>=1){
//				ui.showWarningMessage("该【"+name+"】名称已经存在！");
//				return;
//			}
//		}
//		this.billUI.getVOFromUI();
		super.onBoSave();		
		DataCheckClientUI.delFlag=true;
		ini();
		setSelectRow(0, 0);
	}

	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
//		DataCheckClientUI ui=(DataCheckClientUI) getBillUI();
//		AggregatedValueObject obj=getBufferData().getCurrentVO();
//		DataCheckVO cvo=(DataCheckVO) obj.getParentVO();
//		String pk="";
//		if(cvo!=null){
//			pk=cvo.getPk_dip_datacheckregist();
//		}
//		String tabName=cvo.getTableName();
//		String refName=(String) IContrastUtil.getDocRefMap().get(tabName);
//		String s=SJUtil.isExitRef(refName, pk);
//        if(s!=null&&s.length()>0){
//        	ui.showWarningMessage("数据已经被引用，不能删除！");
//        	return;
//        }
//		super.onBoDelete();
		
//		int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
//		if(row>=0){
//			String pk=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(row, new DataCheckVO().getPKFieldName().toLowerCase().toString()).toString();
//			int i=MessageDialog.showOkCancelDlg(this.billUI, "提示", "是否确定删除!");
//			if(i==1){
//				super.onBoLineDel();	
//				HYPubBO_Client.deleteByWhereClause(DataCheckVO.class,new DataCheckVO().getPKFieldName().toLowerCase().toString()+"='"+pk+"'");
//			}
//			
//		}else{
//			getBillUI().showErrorMessage("请选择要删除的节点！");
//			return;
//		}
        //若数据被引用或是系统预置主键不可删除   2011-07-06   zlc
		Map<String,String> map=IContrastUtil.getDocRefMap();
		String key="dip_datacheckregist";
		String value=map.get(key);
		int bodycount=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		if(bodycount>=0){
			Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new DataCheckVO().DEF_STR_1);
			if(flag!=null&&flag){
				getBillUI().showErrorMessage("系统预置不允许删除！");
				return;
			}
			String pk=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new DataCheckVO().getPKFieldName().toLowerCase())==null?"":
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new DataCheckVO().getPKFieldName().toLowerCase()).toString();
			String isref=SJUtil.isExitRef(value,pk);
			if(isref!=null&&isref.length()>0){
				getBillUI().showErrorMessage("此条数据被引用，不可删除！");
				return ;
			}
			int i=MessageDialog.showOkCancelDlg(this.ui, "提示", "是否确定删除！");
			//是=1
			if(i==1){
				this.onBoLineDel();
				HYPubBO_Client.deleteByWhereClause(DataCheckVO.class,new DataCheckVO().getPKFieldName().toLowerCase()+"='"+pk+"'");
				setSelectRow(0, 0);
			}
			
		}else{
			getBillUI().showErrorMessage("请选择要删除的节点！");
			return;
		}
	}

	@Override
	public void onBoAdd(ButtonObject arg0) throws Exception {
		// TODO Auto-generated method stub
		DataCheckClientUI.delFlag=false;
		int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		if(rows>0){
			onBoEdit();
//			super.onBoLineAdd();
		}else{
			super.onBoAdd(arg0);
			super.onBoLineAdd();
		}
		/*UIRefPane pa=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getBodyItem("type").getComponent();
		TaskRegisterRefMode model=new TaskRegisterRefMode();
		model.addWherePart(" and dip_taskregister.name <> '混合流程'");
		pa.setRefModel(model);*/
	}
	
	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		DataCheckClientUI.delFlag=false;
		super.onBoEdit();
		/*UIRefPane pa=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getBodyItem("type").getComponent();
		TaskRegisterRefMode model=new TaskRegisterRefMode();
		model.addWherePart(" and dip_taskregister.name <> '混合流程'");
		pa.setRefModel(model);*/
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		DataCheckVO vo=new DataCheckVO();
		for(int i=0;i<k;i++){
			Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, vo.DEF_STR_1);
			if(flag!=null&&flag){
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,vo.CODE,false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.NAME, false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,vo.TYPE,false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.IMPLEMENTS, false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.MIAOSU, false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.DEF_STR_1, false);
			}
		}
		onBoLineAdd();
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
	public void setSelectRow(int row,int col){
		if(getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount()>0){
			getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(row, col, false, false);
		}
	}
	
	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		DataCheckClientUI.delFlag=true;
		super.onBoCancel();
		ini();
		setSelectRow(0, 0);
	}
}