package nc.ui.dip.roleandtable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.pub.ButtonObject;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.list.BillListUI;
import nc.uif.pub.exception.UifException;
import nc.vo.dip.roleandtable.DipRoleAndFunctionBVO;
import nc.vo.dip.roleandtable.DipRoleAndTableBVO;
import nc.vo.dip.roleandtable.DipRoleAndTableHVO;
import nc.vo.dip.roleandtable.MyBillVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;

/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillListUI billUI, IListController control){
		super(billUI,control);		
	}
	IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
//	MyBillVO billvo=new MyBillVO();
	String tablecodes[]=new MyBillVO().getTableCodes();
	@Override
	protected void onBoEdit() throws Exception {
			int row=getBillListPanelWrapper().getBillListPanel().getHeadTable().getSelectedRow();
			int count=getBillListPanelWrapper().getBillListPanel().getHeadTable().getRowCount();
//			DipRoleAndTableBVO bvos[]=null;
//				bvos=(DipRoleAndTableBVO[]) getBillListPanelWrapper().getBillListPanel().getBodyBillModel().getBodyValueVOs(DipRoleAndTableBVO.class.getName());
			if(count>0&&row>=0){
				DipRoleAndTableHVO bvo=(DipRoleAndTableHVO) getBillListPanelWrapper().getBillListPanel().getHeadBillModel().getBodyValueRowVO(row, DipRoleAndTableHVO.class.getName());
				try {
					getBillListPanelWrapper().getBillListPanel().getHeadBillModel().setBodyDataVO(new DipRoleAndTableHVO[]{bvo});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					if(!e.getMessage().equals("relation soft objct[] size not equal sorted object's size")){
						getBillUI().showErrorMessage(e.getMessage());
						return;
					}
				}
				getBillListPanelWrapper().getBillListPanel().getHeadBillModel().execLoadFormula();
//				getBillListPanelWrapper().getBillListPanel().getBodyBillModel().setBodyDataVO(bvos);	
				getBillListPanelWrapper().getBillListPanel().getHeadTable().changeSelection(0, 0, false, false);
				getBillListPanelWrapper().getBillListPanel().getBodyTable().changeSelection(0, 0, false, false);
				getBillUI().setBillOperate(IBillOperate.OP_EDIT);
				getBillListPanelWrapper().getBillListPanel().getHeadItem("role_code").setEdit(true);
				getBillListPanelWrapper().getBillListPanel().getHeadItem("strdef_1").setEdit(true);
				getBillListPanelWrapper().getBillListPanel().getHeadTable().setSortEnabled(false);
			}else{
				getBillUI().showErrorMessage("请选择修改的行");
			}
	}

	@Override
	protected void onBoSave() throws Exception {
		boolean addflag=false;
		int headrow=getBillListPanelWrapper().getBillListPanel().getHeadTable().getSelectedRow();
		getBillListPanelWrapper().getBillListPanel().getBodyTable();
//		String tablecode=getBillListPanelWrapper().getBillListPanel().getChildListPanel().getTableCode();
		int currentcunt=getBillListPanelWrapper().getBillListPanel().getChildListPanel().getTable().getRowCount();
		DipRoleAndTableHVO hvo=null;
		//校验表头
		if(headrow>=0){
			hvo=(DipRoleAndTableHVO) getBillListPanelWrapper().getBillListPanel().getHeadBillModel().getBodyValueRowVO(headrow, DipRoleAndTableHVO.class.getName());
			if(hvo!=null&&hvo.getPk_role()!=null&&hvo.getPk_role().length()==20){
				if(hvo.getStrdef_1()==null||hvo.getStrdef_1().trim().equals("")){
					getBillUI().showErrorMessage("权限类型不能为空");
					return;
				}
				if(hvo.getPk_roleandtable_h()!=null&&hvo.getPk_roleandtable_h().length()==20){//修改
					SuperVO supervos[]=HYPubBO_Client.queryByCondition(DipRoleAndTableHVO.class, " pk_role='"+hvo.getPk_role()+"' and pk_roleandtable_h<>'"+hvo.getPk_roleandtable_h()+"' and strdef_1='"+hvo.getStrdef_1()+"' and nvl(dr,0)=0 " );
					if(supervos!=null&&supervos.length>0){
						getBillUI().showErrorMessage("相同权限类型角色不能重复添加");
						return;
					}
					addflag=false;
					HYPubBO_Client.update(hvo);
				}else{//增加保存
					SuperVO supervos[]=HYPubBO_Client.queryByCondition(DipRoleAndTableHVO.class, " pk_role='"+hvo.getPk_role()+"' and strdef_1='"+hvo.getStrdef_1()+"' and nvl(dr,0)=0 " );
					if(supervos!=null&&supervos.length>0){
						getBillUI().showErrorMessage("相同权限类型角色不能重复添加");
						return;
					}
					addflag=true;
				}
			}else{
				getBillUI().showErrorMessage("表头不能为空");
				return;
			}
		}else{
			getBillUI().showErrorMessage("没有选择表头数据或者表头为空");
			return;
		}
		
		if(currentcunt<=0){
			getBillUI().showErrorMessage("当前选择表体不能为空");
			return;
		}
		if(tablecodes!=null&&tablecodes.length>0){
			Map<String,List<Integer>> delMap=new HashMap<String, List<Integer>>();
			Map<String,Map<String,String>> map=new HashMap<String,Map<String,String>>();//空行map
			boolean flag=false;//所有表体都为空标志
			//校验表体不能重复和删除表体空白行
			for(int k=0;k<tablecodes.length;k++){
				int rowcount= getBillListPanelWrapper().getBillListPanel().getChildListPanel(tablecodes[k]).getTableModel().getRowCount();
				delMap.put(tablecodes[k], new ArrayList<Integer>());
				map.put(tablecodes[k], new HashMap<String, String>());
				for(int i=0;i<rowcount;i++){
					
					if(tablecodes[k].equals(new DipRoleAndTableBVO().getTableName())){
						DipRoleAndTableBVO bvoi=(DipRoleAndTableBVO) getBillListPanelWrapper().getBillListPanel().getChildListPanel(tablecodes[k]).getTableModel().getBodyValueRowVO(i, DipRoleAndTableBVO.class.getName());
						if(bvoi.getMemorytable()==null||bvoi.getMemorytable().length()<=0){
							delMap.get(tablecodes[k]).add(i);
						}else{
							if(map.get(tablecodes[k]).get(bvoi.getMemorytable())==null){
								map.get(tablecodes[k]).put(bvoi.getMemorytable(), "Y");
								flag=true;
							}else{
								getBillUI().showErrorMessage(getBillListPanelWrapper().getBillListPanel().getChildListPanel().getTableName()+"页签表体不能重复添加");
								return;
							}
						}
					}else if(tablecodes[k].equals(new DipRoleAndFunctionBVO().getTableName())){
						DipRoleAndFunctionBVO bvoi=(DipRoleAndFunctionBVO) getBillListPanelWrapper().getBillListPanel().getBodyBillModel().getBodyValueRowVO(i, DipRoleAndFunctionBVO.class.getName());
						if(bvoi.getModule_pk()==null||bvoi.getModule_pk().length()<=0){
							delMap.get(tablecodes[k]).add(i);
						}else{
							if(map.get(tablecodes[k]).get(bvoi.getModule_pk())==null){
								map.get(tablecodes[k]).put(bvoi.getModule_pk(), "Y");
								flag=true;
							}else{
								getBillUI().showErrorMessage(getBillListPanelWrapper().getBillListPanel().getChildListPanel().getTableName()+"页签表体不能重复添加");
								return;
							}
						}
						
					}
					
					if(i==rowcount-1){
						if(delMap.get(tablecodes[k])!=null&&delMap.get(tablecodes[k]).size()>0){
							int del[]=new int[delMap.get(tablecodes[k]).size()];
							for(int w=0;w<del.length;w++){
								del[w]=delMap.get(tablecodes[k]).get(w);
							}
							 getBillListPanelWrapper().getBillListPanel().getChildListPanel(tablecodes[k]).getTableModel().delLine(del);
						}
					}
					
				}
			}
			if(!flag){
				getBillUI().showErrorMessage("所有页签表体不能全部为空");
				return;
			}
		}

		for(int i=0;i<tablecodes.length;i++){
			CircularlyAccessibleValueObject[] bvos=null;
			 if(tablecodes[i].equals(new DipRoleAndTableBVO().getTableName())){
				 bvos= getBillListPanelWrapper().getBillListPanel().getChildListPanel(tablecodes[i]).getTableModel().getBodyValueVOs(DipRoleAndTableBVO.class.getName()); 
			 }else if(tablecodes[i].equals(new DipRoleAndFunctionBVO().getTableName())){
				 bvos= getBillListPanelWrapper().getBillListPanel().getChildListPanel(tablecodes[i]).getTableModel().getBodyValueVOs(DipRoleAndFunctionBVO.class.getName());
			 }
			  List<CircularlyAccessibleValueObject> listupdate=new ArrayList<CircularlyAccessibleValueObject>();
			  List<CircularlyAccessibleValueObject> listinsert=new ArrayList<CircularlyAccessibleValueObject>();
			 if(bvos!=null&&bvos.length>0){
				 //保存表头,表头只保存一次
				 if(i==0){
					 if(addflag){
						 hvo.setTs(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						 String pk_hvo=HYPubBO_Client.insert(hvo);
						 hvo.setPk_roleandtable_h(pk_hvo);
					 }else{
						 HYPubBO_Client.update(hvo);
					 }
				 }
				 //给表体赋值，设置表体vo的主表pk
				 for(int k=0;k<bvos.length;k++){
					 bvos[k].setAttributeValue("ts", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
					 if(bvos[k].getPrimaryKey()!=null&&bvos[k].getPrimaryKey().length()==20){
						 listupdate.add(bvos[k]);
					 }else{
						 bvos[k].setAttributeValue(hvo.getPKFieldName(),(hvo.getPk_roleandtable_h()));
						 listinsert.add(bvos[k]);
					 }
				 }
				 //删除表体
				 iqf.exesql("update "+tablecodes[i]+" set dr=1 where pk_roleandtable_h='"+hvo.getPk_roleandtable_h()+"' and nvl(dr,0)=0");
				 //保存表体数据
				 if(listupdate.size()>0){
					 HYPubBO_Client.updateAry(listupdate.toArray(new SuperVO[0]));
				 }
				 if(listinsert.size()>0){
					 HYPubBO_Client.insertAry(listinsert.toArray(new SuperVO[0]));
				 }

			 }
		}
		 
		
			onBoRefresh();
	}

	@Override
	protected void onBoDelete() throws Exception {
			super.onBoDelete();
			int row=getBillListPanelWrapper().getBillListPanel().getHeadTable().getSelectedRow();
			DipRoleAndTableHVO hvo=(DipRoleAndTableHVO) getBillListPanelWrapper().getBillListPanel().getHeadBillModel().getBodyValueRowVO(row, DipRoleAndTableHVO.class.getName());
			if(hvo!=null){
				 if(tablecodes!=null&&tablecodes.length>0){
					 for(int i=0;i<tablecodes.length;i++){
						 iqf.exesql(" update "+tablecodes[i]+" set dr=1 where pk_roleandtable_h='"+hvo.getPk_roleandtable_h()+"' and nvl(dr,0)=0 ");		 
					 }
				 }
				 iqf.exesql(" update dip_roleandtable_h set dr=1 where pk_roleandtable_h='"+hvo.getPk_roleandtable_h()+"' and nvl(dr,0)=0 ");
			}
			onBoRefresh();
	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		
		getBillListPanelWrapper().getBillListPanel().getHeadBillModel().setBodyDataVO(null);
		getBillListPanelWrapper().getBillListPanel().getHeadBillModel().addLine();
		if(tablecodes!=null&&tablecodes.length>0){
			for(int i=0;i<tablecodes.length;i++){
				getBillListPanelWrapper().getBillListPanel().getChildListPanel(tablecodes[i]).getTableModel().setBodyDataVO(null);
			}
		}
		getBufferData().clear();
		getBillListPanelWrapper().getBillListPanel().setEnabled(true);
		getBillListPanelWrapper().getBillListPanel().getHeadTable().changeSelection(0, 0, false, false);
		getBillListPanelWrapper().getBillListPanel().getHeadItem("role_code").setEdit(true);
//		((UIComboBox)getBillListPanelWrapper().getBillListPanel().getHeadItem("strdef_1").getComponent()).setSelectedIndex(arg0)
		getBillUI().setBillOperate(IBillOperate.OP_ADD);
		getBillListPanelWrapper().getBillListPanel().getHeadTable().setSortEnabled(false);
	}
	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		onBoRefresh();
	}

	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
//		getBillListPanelWrapper().getBillListPanel().getBodyBillModel().setBodyDataVO(new DipRoleAndTableBVO[0]);
		SuperVO[] hvos=HYPubBO_Client.queryByCondition(DipRoleAndTableHVO.class,"nvl(dr,0)=0");
		getBufferData().clear();
		if(hvos!=null&&hvos.length>0){
			MyBillVO billvos[]=new MyBillVO[hvos.length];
			for(int i=0;i<hvos.length;i++){
				SuperVO bvos[]=HYPubBO_Client.queryByCondition(DipRoleAndTableBVO.class, " pk_roleandtable_h='"+hvos[0].getAttributeValue("pk_roleandtable_h")+"' and nvl(dr,0)=0");
				billvos[i]=new MyBillVO();
				billvos[i].setParentVO(hvos[i]);
				billvos[i].setTableVO(new DipRoleAndTableBVO().getTableName(), bvos);
				 bvos=HYPubBO_Client.queryByCondition(DipRoleAndFunctionBVO.class, " pk_roleandtable_h='"+hvos[0].getAttributeValue("pk_roleandtable_h")+"' and nvl(dr,0)=0");
				billvos[i].setTableVO(new DipRoleAndFunctionBVO().getTableName(), bvos);
			}
			getBufferData().addVOsToBuffer(billvos);
			getBillListPanelWrapper().getBillListPanel().getHeadBillModel().setBodyDataVO(hvos);
			getBillListPanelWrapper().getBillListPanel().getHeadBillModel().execLoadFormula();
			getBillListPanelWrapper().getBillListPanel().getHeadTable().changeSelection(0, 0, false, false);
			
			//加载子表数据
			
			if(tablecodes!=null&&tablecodes.length>0){
				for(int i=0;i<tablecodes.length;i++){
					String tablecode=tablecodes[i];	
					SuperVO[] bvos=new SuperVO[0];
					try {
						if(tablecode.equals(new DipRoleAndTableBVO().getTableName())){
							bvos=HYPubBO_Client.queryByCondition(DipRoleAndTableBVO.class, "pk_roleandtable_h='"+hvos[0].getAttributeValue("pk_roleandtable_h")+"' and nvl(dr,0)=0 ");
						}	
						if(tablecode.equals(new DipRoleAndFunctionBVO().getTableName())){
							bvos=HYPubBO_Client.queryByCondition(DipRoleAndFunctionBVO.class, "pk_roleandtable_h='"+hvos[0].getAttributeValue("pk_roleandtable_h")+"' and nvl(dr,0)=0 ");
						}
					} catch (UifException e1) {
						e1.printStackTrace();
					}	
					getBillListPanelWrapper().getBillListPanel().getChildListPanel(tablecode).getTableModel().setBodyDataVO(bvos);
					getBillListPanelWrapper().getBillListPanel().getChildListPanel(tablecode).getTableModel().execLoadFormula();
				}
			}
			getBillListPanelWrapper().getBillListPanel().getHeadItem("role_code").setEdit(false);
			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
		}else{
			getBillListPanelWrapper().getBillListPanel().getHeadBillModel().setBodyDataVO(null);
			if(tablecodes!=null&&tablecodes.length>0){
				for(int i=0;i<tablecodes.length;i++){
					getBillListPanelWrapper().getBillListPanel().getChildListPanel(tablecodes[i]).getTableModel().setBodyDataVO(null);
				}
			}
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
		}
		getBillListPanelWrapper().getBillListPanel().getHeadTable().setSortEnabled(true);
		
	}
	
}