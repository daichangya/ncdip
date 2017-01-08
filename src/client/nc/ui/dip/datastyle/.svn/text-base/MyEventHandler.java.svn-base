package nc.ui.dip.datastyle;

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
import nc.vo.dip.datastyle.DipDatastyleVO;

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
	public void onBoAdd(ButtonObject arg0) throws Exception {
		DataStyleClientUI.delFlag=false;
		int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		if(rows>0){
			super.onBoEdit();
			super.onBoLineAdd();
		}else{
			super.onBoAdd(arg0);
			
			super.onBoLineAdd();
		}
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		DipDatastyleVO vo=new DipDatastyleVO();
		for(int i=0;i<k;i++){
			Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, vo.ISSYSPREF);
			if(flag!=null&&flag){
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,vo.CODE,false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.NAME, false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.ISSYSPREF, false);
			}
		}
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<k;i++){
			Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DipDatastyleVO().ISSYSPREF);
			String code=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DipDatastyleVO().CODE);
			String name=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DipDatastyleVO().NAME);
			if((flag==null||flag==false)&&(code==null||code.trim().equals(""))&&(name==null||name.trim().equals(""))){
//				getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(i, 0, false, false);
//				getBillCardPanelWrapper().getBillCardPanel().delLine();
				list.add(i);
			}else{
				if(flag==null){
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(false, i,new DipDatastyleVO().ISSYSPREF);
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
//		DataStyleClientUI ui=(DataStyleClientUI)getBillUI();
//		//主键
//		String pk=(String)ui.getBillCardPanel().getBodyItem("pk_datastyle").getValueObject();
//		if(pk ==null || pk.trim().equals("")){
//			pk=" ";//保存时避免重复的可以添加
//		}
//		
//		String code=(String)ui.getBillCardPanel().getBodyItem("code").getValueObject();
//		String name=(String)ui.getBillCardPanel().getBodyItem("name").getValueObject();
//		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//		
//		Collection ccode=bs.retrieveByClause(DipDatastyleVO.class, "code='"+code+"' and nvl(dr,0)=0 and pk_datastyle <>'"+pk+"'");
//		
//		if(ccode !=null){
//			if(ccode.size()>=1){
//				ui.showWarningMessage("该【"+code+"】编码已经存在！");
//				return;
//			}
//		}
//		
//		Collection cname=bs.retrieveByClause(DipDatastyleVO.class, "name='"+name+"' and nvl(dr,0)=0 and pk_datastyle <>'"+pk+"'");
//		if(cname !=null){
//			if(cname.size()>=1){
//				ui.showWarningMessage("该【"+name+"】名称已经存在！");
//				return;
//			}
//		}
		
		super.onBoSave();	
		DataStyleClientUI.delFlag=true;
		ini();
		setSelectRow(0, 0);
		
	}

	@Override
	protected void onBoEdit() throws Exception {
		DataStyleClientUI.delFlag=false;
		super.onBoEdit();
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		DipDatastyleVO vo=new DipDatastyleVO();
		for(int i=0;i<k;i++){
			Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, vo.ISSYSPREF);
			if(flag!=null&&flag){
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,vo.CODE,false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.NAME, false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.ISSYSPREF, false);
			}
		}
		onBoLineAdd();
	}

	
	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		super.onBoQuery();
	}

	@Override
	protected void onBoDelete() throws Exception {
			Map<String,String> map=IContrastUtil.getDocRefMap();
			String key="dip_datastyle";
			String value=map.get(key);
			int bodycount=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
			if(bodycount>=0){
				String pk=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new DipDatastyleVO().getPKFieldName().toLowerCase())==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new DipDatastyleVO().getPKFieldName().toLowerCase()).toString();
				String isref=SJUtil.isExitRef(value,pk);
				if(isref!=null&&isref.length()>0){
					getBillUI().showErrorMessage("此条数据被引用，不可删除！");
					return ;
				}
				Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(),"提示","是否要删除?");
				if(flag==1){
				    this.onBoLineDel();
				    HYPubBO_Client.deleteByWhereClause(DipDatastyleVO.class,new DipDatastyleVO().getPKFieldName().toLowerCase()+"='"+pk+"'");
				    setSelectRow(0, 0);
				}
			}else{
				getBillUI().showErrorMessage("请选择要删除的节点！");
				return;
			}
//		super.onBoDelete();
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
		DataStyleClientUI.delFlag=true;
		super.onBoCancel();
		ini();
		setSelectRow(0, 0);
	}
}