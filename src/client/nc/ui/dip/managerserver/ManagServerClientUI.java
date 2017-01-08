package nc.ui.dip.managerserver;

import nc.ui.dip.managerserver.AbstractManagServerClientUI;
import nc.ui.dip.managerserver.MyEventHandler;
import nc.ui.trade.list.ListEventHandler;


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
 public class ManagServerClientUI extends AbstractManagServerClientUI{
	 public ManagServerClientUI(){
		 super();
		 try {
			((MyEventHandler)this.getListEventHandler()).onBoQuery();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
       
       @Override
	protected ListEventHandler createEventHandler() {
		// TODO Auto-generated method stub
		return new MyEventHandler(this,getUIControl());
	}

	public String getRefBillType() {
   		return null;
   	}
//	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
//			throws Exception {}
//
//	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
//			int intRow) throws Exception {}
//
//	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
//			throws Exception {	}

       /**
   	 * 修改此方法初始化模板控件数据
   	 */
	protected void initSelfData() {	}

	public void setDefaultData() throws Exception {
	}
	


}
