package nc.ui.dip.dbcontype;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.card.BillCardUI;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.RetMessage;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.dbcontype.DbcontypeVO;

/**
 *
 *该类是AbstractMyEventHandler抽象类的实现类，
 *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 *@author author
 *@version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler{

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		// TODO Auto-generated method stub
		super.onBoElse(intBtn);
		switch (intBtn) {
		case IBtnDefine.TESTCONNECT:
			onBtnTestconnect();
			break;

		default:
			break;
		}
	}
	private void onBtnTestconnect() throws Exception{
		int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		if(row<0){
			getBillUI().showErrorMessage("请选择要测试的连接！");
			return;
		}
		String pk=(String) getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, "pk_dbcontype");
		if(pk==null||pk.length()<0){
			getBillUI().showErrorMessage("请选择保存的测试的连接！");
			return;
		}
		IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		RetMessage rm=iqf.testDSConnect(pk);
		if(rm.getIssucc()){
			MessageDialog.showHintDlg(getBillUI(), "测试连接", "连接成功！");
		}else{
			getBillUI().showErrorMessage("连接失败！ "+rm.getMessage());
		}
		
	}
	BillCardUI billUI;

	ICardController control;
	public MyEventHandler(BillCardUI billUI, ICardController control){
		super(billUI,control);	
		this.billUI=billUI;
		this.control=control;
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




	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<k;i++){
			String code=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DbcontypeVO().CODE);
			String name=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DbcontypeVO().NAME);
			String username=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DbcontypeVO().USERCODE);
			String password=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DbcontypeVO().PASSWORD);
			String type=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DbcontypeVO().TYPE);
			String constr=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DbcontypeVO().CONSTR);
			String vedf2=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DbcontypeVO().VDEF2);
			if((code==null||code.trim().equals(""))&&(name==null||name.trim().equals(""))&&(username==null||username.trim().equals(""))&&(password==null||password.trim().equals(""))&&(type==null||type.trim().equals(""))&&(constr==null||constr.trim().equals(""))&&(vedf2==null||vedf2.trim().equals(""))){
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
		//此处增加表头重复校验：数据类型编码、名称
//		DBConTypeClientUI ui=(DBConTypeClientUI)getBillUI();
//		//主键
//		String pk=(String)ui.getBillCardPanel().getBodyItem("pk_dbcontype").getValueObject();
//		if(pk ==null || pk.trim().equals("")){
//			pk=" ";//保存时避免重复的可以添加
//		}
//		
//		String code=(String)ui.getBillCardPanel().getBodyItem("code").getValueObject();
//		String name=(String)ui.getBillCardPanel().getBodyItem("name").getValueObject();
//		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//		
//		Collection ccode=bs.retrieveByClause(DbcontypeVO.class, "code='"+code+"' and nvl(dr,0)=0 and pk_dbcontype <>'"+pk+"'");
//		
//		if(ccode !=null){
//			if(ccode.size()>=1){
//				ui.showWarningMessage("该【"+code+"】编码已经存在！");
//				return;
//			}
//		}
//		
//		Collection cname=bs.retrieveByClause(DbcontypeVO.class, "name='"+name+"' and nvl(dr,0)=0 and pk_dbcontype <>'"+pk+"'");
//		if(cname !=null){
//			if(cname.size()>=1){
//				ui.showWarningMessage("该【"+name+"】名称已经存在！");
//				return;
//			}
//		}
		super.onBoSave();		
	}

	@Override
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
		onBoLineAdd();
	}

	
	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		super.onBoQuery();
	}




	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		
			Map<String,String> map=IContrastUtil.getDocRefMap();
			String key="dip_dbcontype";
			String value=map.get(key);
			int bodycount=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
			if(bodycount>=0){
				String pk=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new DbcontypeVO().getPKFieldName().toLowerCase())==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new DbcontypeVO().getPKFieldName().toLowerCase()).toString();
				String isref=SJUtil.isExitRef(value,pk);
				if(isref!=null&&isref.length()>0){
					getBillUI().showErrorMessage("此条数据被引用，不可删除！");
					return ;
				}
				Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(),"提示","是否要删除?");
				if(flag==1){
				    this.onBoLineDel();
				    HYPubBO_Client.deleteByWhereClause(DbcontypeVO.class,new DbcontypeVO().getPKFieldName().toLowerCase()+"='"+pk+"'");
				}
			}else{
				getBillUI().showErrorMessage("请选择要删除的节点！");
				return;
			}
//		super.onBoDelete();
	}
	public void ini(){
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
	}
	
}