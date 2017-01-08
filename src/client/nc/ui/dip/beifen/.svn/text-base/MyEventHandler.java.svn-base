package nc.ui.dip.beifen;

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
import nc.vo.dip.beifen.DatacanzhaoVO;



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
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		DatacanzhaoVO vo=new DatacanzhaoVO();
		for(int i=0;i<k;i++){
			Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, vo.ISSYSPREF);
			if(flag!=null&&flag){
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,vo.TABNAME,false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.XINGSHI, false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.ISSYSPREF, false);
			}
		}
		onBoLineAdd();
		getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
		getBillUI().updateButtons();
		BeifenClientUI.delFlag=false;
	}
	
	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		//自动增行去空行
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<k;i++){
			Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DatacanzhaoVO().ISSYSPREF);
			String name=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DatacanzhaoVO().TABNAME);
			String xingshi=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, "xingshi");
			if((flag==null||flag==false)&&(xingshi==null||xingshi.trim().equals(""))&&(name==null||name.trim().equals(""))){
//				getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(i, 0, false, false);
//				getBillCardPanelWrapper().getBillCardPanel().delLine();
				list.add(i);
			}else{
				if(flag==null){
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(false, i, new DatacanzhaoVO().ISSYSPREF);
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
		//此处增加表头重复校验：数据类型编码、名称
//		BeifenClientUI ui=(BeifenClientUI)getBillUI();
//		//主键
//		String pk=(String)ui.getBillCardPanel().getBodyItem("pk_dip_datacanzhao").getValueObject();
//		if(pk ==null || pk.trim().equals("")){
//			pk=" ";//保存时避免重复的可以添加
//		}
//		
//		String code=(String)ui.getBillCardPanel().getBodyItem("tabname").getValueObject();
//		String name=(String)ui.getBillCardPanel().getBodyItem("xingshi").getValueObject();
//		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//		
//		Collection ccode=bs.retrieveByClause(DatacanzhaoVO.class, "tabname='"+code+"' and nvl(dr,0)=0 and pk_dip_datacanzhao <>'"+pk+"'");
//		
//		if(ccode !=null){
//			if(ccode.size()>=1){
//				ui.showWarningMessage("该【"+code+"】表名已经存在！");
//				return;
//			}
//		}
//		
//		Collection cname=bs.retrieveByClause(DatacanzhaoVO.class, "xingshi='"+name+"' and nvl(dr,0)=0 and pk_dip_datacanzhao <>'"+pk+"'");
//		if(cname !=null){
//			if(cname.size()>=1){
//				ui.showWarningMessage("该【"+name+"】形式已经存在！");
//				return;
//			}
//		}
//		this.billUI.getVOFromUI();
		
		super.onBoSave();
		BeifenClientUI.delFlag=true;
		setSelectRow(0, 0);
		
	}
	

	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		Map<String,String> map=IContrastUtil.getDocRefMap();
		String key="pk_dip_datacanzhao";
		String value=map.get(key);
		int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();		
		if(row>=0){
			Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(row, new DatacanzhaoVO().ISSYSPREF);
			if(flag!=null&&flag){
				getBillUI().showErrorMessage("系统预置不允许删除！");
				return;
			}
			String pk=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(row, new DatacanzhaoVO().getPKFieldName().toLowerCase().toString()).toString();//项目主键	pk_dip_datacanzhao
			String isref=SJUtil.isExitRef(value,pk);
			if(isref!=null&&isref.length()>0){
				getBillUI().showErrorMessage("此条数据被引用，不可删除！");
				return ;
			}
			int i=MessageDialog.showOkCancelDlg(this.billUI, "提示", "是否确定删除！");
			//是=1
			if(i==1){
			super.onBoLineDel();
			HYPubBO_Client.deleteByWhereClause(DatacanzhaoVO.class,new DatacanzhaoVO().getPKFieldName().toLowerCase()+"='"+pk+"'");
			setSelectRow(0, 0);
			}
		}else{
			getBillUI().showErrorMessage("请选择要删除的节点！");
			return;
		}
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
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		DatacanzhaoVO vo=new DatacanzhaoVO();
		for(int i=0;i<k;i++){
			Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, vo.ISSYSPREF);
			if(flag!=null&&flag){
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,vo.TABNAME,false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.XINGSHI, false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.ISSYSPREF, false);
			}
		}
		getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
		billUI.updateButtonUI();
		BeifenClientUI.delFlag=false;
	}
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
	
	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCancel();
		BeifenClientUI.delFlag=true;
		ini();
		setSelectRow(0, 0);
	}
	
	public void setSelectRow(int row,int col){
		if(getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount()>0){
			getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(row, col, false, false);
		}
	}
	
	
}