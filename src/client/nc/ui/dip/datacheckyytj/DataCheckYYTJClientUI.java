package nc.ui.dip.datacheckyytj;

import nc.ui.trade.list.ListEventHandler;
//nc.ui.dip.datacheckyytj.DataCheckYYTJClientUI

/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 *
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
 public class DataCheckYYTJClientUI extends AbstractDataCheckYYTJClientUI{
	 public DataCheckYYTJClientUI(){
		 super();
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
   	 * �޸Ĵ˷�����ʼ��ģ��ؼ�����
   	 */
	protected void initSelfData() {	}

	public void setDefaultData() throws Exception {
	}
	


}
