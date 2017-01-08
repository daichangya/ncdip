package nc.ui.dip.tabstatus;

import nc.ui.trade.list.ListEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;


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
 public class TabStatusClientUI extends AbstractTabStatusClientUI{
       public TabStatusClientUI(){
    	   super();
    	   getBillListPanel().getBodyBillModel().sortByColumn("mydate", false);//.setSortColumn("mydate");
       }
       protected ListEventHandler  createEventHandler() {
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
	


}
