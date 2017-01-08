package nc.ui.dip.roleandtable;

import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.list.ListEventHandler;
import nc.uif.pub.exception.UifException;
import nc.vo.dip.roleandtable.DipRoleAndFunctionBVO;
import nc.vo.dip.roleandtable.DipRoleAndTableBVO;
import nc.vo.dip.roleandtable.DipRoleAndTableHVO;
import nc.vo.dip.roleandtable.MyBillVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;


/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
 public class RoleAndTableClientUI extends AbstractRoleAndTableClientUI  {
       
	public RoleAndTableClientUI() {
		super();
		getButtonManager().getButton(IBillButton.Line).removeChildButton(getButtonManager().getButton(IBillButton.PasteLine));
		getButtonManager().getButton(IBillButton.Line).removeChildButton(getButtonManager().getButton(IBillButton.CopyLine));
		getButtonManager().getButton(IBillButton.Line).removeChildButton(getButtonManager().getButton(IBillButton.InsLine));
	}
	 
	 
       protected ListEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
	
       
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

	protected void initSelfData() {	}

	public void setDefaultData() throws Exception {
	}

	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void afterEdit(BillEditEvent arg0) {
		// TODO Auto-generated method stub
		super.afterEdit(arg0);
//		arg0.getTableCode();
//		arg0.getOldRow();
//		int k=arg0.getRow();
//		int w=getBillListPanel().getBodyBillModel().getRowCount();
//		if(k==w-1){
//			getBillListPanel().getBodyBillModel().addLine();
//		}
		
	}
	
	@Override
	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		super.bodyRowChange(e);
		getBillListPanel().getChildListPanel();
		int row=e.getRow();
		MyBillVO billvo=new MyBillVO();
		String tablecodes[]=billvo.getTableCodes();
        if(row>=0&&tablecodes!=null&&tablecodes.length>0){
        	DipRoleAndTableHVO hvo=(DipRoleAndTableHVO) getBillListPanel().getHeadBillModel().getBodyValueRowVO(row, DipRoleAndTableHVO.class.getName());
        	for(int i=0;i<tablecodes.length;i++){
        		String tablecode=tablecodes[i];	
				if(hvo!=null&&hvo.getPk_roleandtable_h()!=null){
					SuperVO[] bvos=new SuperVO[0];
					 try {
						if(tablecode.equals(new DipRoleAndTableBVO().getTableName())){
								 bvos=HYPubBO_Client.queryByCondition(DipRoleAndTableBVO.class, "pk_roleandtable_h='"+hvo.getPk_roleandtable_h()+"' and nvl(dr,0)=0 ");
							}	
						 if(tablecode.equals(new DipRoleAndFunctionBVO().getTableName())){
									 bvos=HYPubBO_Client.queryByCondition(DipRoleAndFunctionBVO.class, "pk_roleandtable_h='"+hvo.getPk_roleandtable_h()+"' and nvl(dr,0)=0 ");
							}
					} catch (UifException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
					 getBillListPanel().getChildListPanel(tablecode).getTableModel().setBodyDataVO(bvos);
					 getBillListPanel().getChildListPanel(tablecode).getTableModel().execLoadFormula();
				}
        	}
			}

//		}
	}


	@Override
	protected IListController createController() {
		// TODO Auto-generated method stub
		return new RoleAndTableClientUICtrl();
	}

	

	
	
	
	
}
