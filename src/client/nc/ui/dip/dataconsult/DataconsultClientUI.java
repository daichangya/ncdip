package nc.ui.dip.dataconsult;

import nc.ui.trade.list.ListEventHandler;
import nc.vo.ntb.outer.IAccessableBusiVO;
import nc.vo.ntb.outer.NtbParamVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;


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
 public class DataconsultClientUI extends AbstructDataConsultClientUI{
       
       protected 	ListEventHandler createEventHandler() {
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

	public NtbParamVO[] getLinkDatas(IAccessableBusiVO[] arg0)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}
	


}
