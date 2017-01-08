package nc.ui.dip.actionset;




import java.util.HashMap;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.manage.ManageEventHandler;
import nc.uif.pub.exception.UifException;
import nc.vo.dip.actionset.ActionSetBVO;
import nc.vo.dip.actionset.ActionSetHVO;
import nc.vo.dip.actionset.MyBillVO;
import nc.vo.dip.authorize.define.DipADContdataVO;
import nc.vo.dip.authorize.role.RoleGroupVO;
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
//DataFormatDefClientUI
public class ActionSetClientUI extends AbstractActionSetClientUI{

	public HashMap tableMap = new HashMap();
	public ActionSetClientUI(HashMap map){
		super();
		this.tableMap = map;
		try {
			init(map);
			setDefaultData();
			DipADContdataVO vo = (DipADContdataVO)map.get("adcontdatevo");
			((MyEventHandler)this.getManageEventHandler()).show(vo.getPk_xt());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();  
		}
	}

	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}

	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
	throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
	throws Exception {	}

	protected void initSelfData() {
		setCurrentPanel("CARDPANEL");

	}

	public void setDefaultData() throws Exception {
//
//		String pk = tableMap.get("pk")==null?"":tableMap.get("pk").toString();
//		String type = tableMap.get("type")==null?"":tableMap.get("type").toString();
//		String sql = "select * from dip_actionset_h where vdef1='"+pk+"' and vdef2='"+type+"' and nvl(dr,0) = 0 ";
//		IUAPQueryBS query = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//		ActionSetHVO hvo = (ActionSetHVO)query.executeQuery(sql, new BeanProcessor(ActionSetHVO.class));
//		if(hvo==null){
//			getBillCardPanel().setHeadItem("code", tableMap.get("code"));
//			getBillCardPanel().setHeadItem("name", tableMap.get("name"));
//			return ;
//		}
//		sql = "select * from dip_actionset_b where pk_actionset_h='"+hvo.getPk_actionset_h()+"' and nvl(dr,0)=0";
//		List<ActionSetHVO> listbvo  = (ArrayList<ActionSetHVO>)query.executeQuery(sql, new BeanListProcessor(ActionSetHVO.class));
//		MyBillVO billvo = new MyBillVO();
//		billvo.setParentVO(hvo);
//		if(listbvo!=null && listbvo.size()>0){
//			ActionSetHVO bvo [] = listbvo.toArray(new ActionSetHVO[0]);
//			billvo.setChildrenVO(bvo);
//		}
//
//
//		this.getBufferData().addVOToBuffer(billvo);
//		getBufferData().updateView();

	}

	private void init(HashMap map){
		if(map !=null){
			ActionSetHVO hvo=null;
			String fpk=(String) map.get("fpk");
			String pk_fp=(String) map.get("pk_fp");
			DipADContdataVO vo = (DipADContdataVO)map.get("adcontdatevo");
			getBillCardPanel().getHeadItem("pk_contdata_h").setValue(fpk);
			getBillCardPanel().getHeadItem("pk_role_group").setValue(pk_fp);
			try {
				ActionSetHVO[] vos=(ActionSetHVO[]) HYPubBO_Client.queryByCondition(ActionSetHVO.class, 
						"pk_contdata_h ='"+fpk+"' and pk_role_group='"+pk_fp+"' and nvl(dr,0)=0") ;
				if(vos !=null && vos.length>0){
					hvo=vos[0];
					ActionSetBVO[] bvos=(ActionSetBVO[]) HYPubBO_Client.queryByCondition(ActionSetBVO.class, 
							hvo.getPKFieldName()+"='"+hvo.getPrimaryKey()+"'");
					MyBillVO billvo=new MyBillVO();
					billvo.setParentVO(hvo);
					billvo.setChildrenVO(bvos);
					((MyEventHandler)getManageEventHandler()).lodDefaultVo(new SuperVO[]{(SuperVO) billvo.getParentVO()});
				}else{
					getBillCardPanel().getHeadItem("code").setValue(vo.getCode());
					getBillCardPanel().getHeadItem("name").setValue(vo.getName());
					RoleGroupVO roleGroupVO = (RoleGroupVO)HYPubBO_Client.queryByPrimaryKey(RoleGroupVO.class, pk_fp);
					if(null != roleGroupVO){
						getBillCardPanel().getHeadItem("is_add").setValue(roleGroupVO.getIs_add());
						getBillCardPanel().getHeadItem("is_addline").setValue(roleGroupVO.getIs_addline());
						getBillCardPanel().getHeadItem("is_cancal").setValue(roleGroupVO.getIs_cancal());
						getBillCardPanel().getHeadItem("is_del").setValue(roleGroupVO.getIs_del());
						getBillCardPanel().getHeadItem("is_delline").setValue(roleGroupVO.getIs_delline());
						getBillCardPanel().getHeadItem("is_edit").setValue(roleGroupVO.getIs_edit());
						getBillCardPanel().getHeadItem("is_export").setValue(roleGroupVO.getIs_export());
						getBillCardPanel().getHeadItem("is_extend").setValue(roleGroupVO.getIs_extend());
						getBillCardPanel().getHeadItem("is_import").setValue(roleGroupVO.getIs_import());
						getBillCardPanel().getHeadItem("is_query").setValue(roleGroupVO.getIs_query());
						getBillCardPanel().getHeadItem("is_save").setValue(roleGroupVO.getIs_save());
					}
				}
			} catch (UifException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		getBillCardPanel().getBillTable().setSortEnabled(false);
		getBillCardPanel().execHeadLoadFormulas();
//		int rowcount=getBillCardPanel().getBillModel().getRowCount();
//		for(int i=0;i<rowcount;i++){
//			getBillCardPanel().setBodyValueAt(i+1, i, "disno");
//		}
	}
	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}
	public void onButtonAction(ButtonObject bo)throws Exception
	{
		((MyEventHandler)this.getManageEventHandler()).onButtonAction(bo);

	}
	
	@Override
	public void afterUpdate() {
		// TODO Auto-generated method stub
		super.afterUpdate();
		BillModel billModel = getBillCardPanel().getBillModel();
		int rowCount = billModel.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			Object valueAt = billModel.getValueAt(i, "verifycon");
			if(null != valueAt && !"".equals(valueAt)){
				billModel.setValueAt("已设置", i, "verifycon_ref");
			}
			Object valueAt1 = billModel.getValueAt(i, "updatecon");
			if(null != valueAt1 && !"".equals(valueAt1)){
				billModel.setValueAt("已设置", i, "updatecon_ref");
			}
		}
	}


}
