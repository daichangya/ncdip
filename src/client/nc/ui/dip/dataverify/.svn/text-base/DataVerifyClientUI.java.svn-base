package nc.ui.dip.dataverify;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.manage.ManageEventHandler;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.dataverify.DataverifyBVO;
import nc.vo.dip.dataverify.DataverifyHVO;
import nc.vo.dip.dataverify.MyBillVO;
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
public class DataVerifyClientUI extends AbstractDataVerifyClientUI{

	public HashMap tableMap = new HashMap();
	public DataVerifyClientUI(HashMap map){
		super();
		this.tableMap = map;
		try {
			init(map);
			
			setDefaultData();
			String type=map.get("type").toString();
			if(type.equals(SJUtil.getYWnameByLX(IContrastUtil.YWLX_JG))){
				((MyEventHandler)this.getManageEventHandler()).show(map.get("fpk").toString());
				
			}/*else{
				getBillCardPanel().getBodyItem("verifycon").setEnabled(true);
			}*/
			

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

		String pk = tableMap.get("pk")==null?"":tableMap.get("pk").toString();
		String type = tableMap.get("type")==null?"":tableMap.get("type").toString();
		String sql = "select * from dip_dataverify_h where vdef1='"+pk+"' and vdef2='"+type+"' and nvl(dr,0) = 0 ";
		IUAPQueryBS query = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		DataverifyHVO hvo = (DataverifyHVO)query.executeQuery(sql, new BeanProcessor(DataverifyHVO.class));
		if(hvo==null){
			getBillCardPanel().setHeadItem("code", tableMap.get("code"));
			getBillCardPanel().setHeadItem("name", tableMap.get("name"));
			return ;
		}
		sql = "select * from dip_dataverify_b where pk_dataverify_h='"+hvo.getPk_dataverify_h()+"' and nvl(dr,0)=0";
		List<DataverifyBVO> listbvo  = (ArrayList<DataverifyBVO>)query.executeQuery(sql, new BeanListProcessor(DataverifyBVO.class));
		MyBillVO billvo = new MyBillVO();
		billvo.setParentVO(hvo);
		if(listbvo!=null && listbvo.size()>0){
			DataverifyBVO bvo [] = listbvo.toArray(new DataverifyBVO[0]);
			billvo.setChildrenVO(bvo);
		}


		this.getBufferData().addVOToBuffer(billvo);
		getBufferData().updateView();

	}

	private void init(HashMap map){
//		if(pk!=null){
//		try {
//		DataverifyBVO[] vo=(DataverifyBVO[]) HYPubBO_Client.queryByCondition(DataverifyBVO.class, "fpk='"+pk+"' and nvl(dr,0)=0");
//		if(vo!=null&&vo.length>0){

//		}
//		} catch (UifException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		}
//		}

//		2011-5-21
		if(map !=null){
			DataverifyHVO hvo=null;
			String fpk=(String) map.get("pk");
			String type=(String) map.get("type");
			
			getBillCardPanel().getHeadItem("pk_datachange_h").setValue(fpk);
			try {
				DataverifyHVO[] vos=(DataverifyHVO[]) HYPubBO_Client.queryByCondition(DataverifyHVO.class, "pk_datachange_h ='"+fpk+"' and nvl(dr,0)=0") ;
//				hvo = (DataverifyHVO) HYPubBO_Client.queryByPrimaryKey(DataverifyHVO.class, fpk);
				if(vos !=null && vos.length>0){
					hvo=vos[0];
					DataverifyBVO[] bvos=(DataverifyBVO[]) HYPubBO_Client.queryByCondition(DataverifyBVO.class, hvo.getPKFieldName()+"='"+hvo.getPrimaryKey()+"'");
					MyBillVO billvo=new MyBillVO();
					billvo.setParentVO(hvo);
					billvo.setChildrenVO(bvos);
					((MyEventHandler)getManageEventHandler()).lodDefaultVo(new SuperVO[]{(SuperVO) billvo.getParentVO()});
				}
			} catch (UifException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		getBillCardPanel().getBillTable().setSortEnabled(false);
		int rowcount=getBillCardPanel().getBillModel().getRowCount();
		for(int i=0;i<rowcount;i++){
			getBillCardPanel().setBodyValueAt(i+1, i, "disno");
		}
	}
//	public void init(MyBillVO vo,boolean isadd){
//	onButtonClicked (getButtonManager().getButton(IBillButton.Card));
//	DataverifyHVO hvo=null;
//	if(isadd){
//	hvo=(DataverifyHVO) vo.getParentVO();
//	((MyEventHandler)getManageEventHandler()).lodDefaultVo(new SuperVO[]{hvo});
//	}else{
//	getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
//	((MyEventHandler)getManageEventHandler()).lodDefaultVo(new SuperVO[]{(SuperVO)vo.getParentVO()});

//	}
//	try {
//	updateBtnStateByCurrentVO();
//	} catch (Exception e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//	}

//	}
	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}
	public void onButtonAction(ButtonObject bo)throws Exception
	{
		((MyEventHandler)this.getManageEventHandler()).onButtonAction(bo);

	}


}
