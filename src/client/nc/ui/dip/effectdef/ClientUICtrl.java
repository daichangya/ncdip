package nc.ui.dip.effectdef;

import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.dip.effectdef.CdSbodyVO;
import nc.vo.dip.effectdef.MyBillVO;

/**
 * <b> ��Ƭ�͵�����UI��������</b><br>
 * 
 * <p>
 * ���ý��水ť�����ݣ��Ƿ�ƽ̨��ص���Ϣ
 * </p>
 * <br>
 * 
 * @author �α�
 * @version tempProject version
 */

public class ClientUICtrl implements ICardController, ISingleController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	/**
	 * ���ý��水ť
	 */
	public int[] getCardButtonAry() {
		return new int[] { 
			IBillButton.Query, 
			IBillButton.Add, 
			IBillButton.Edit, 
			IBillButton.Line, 
			IBillButton.Save, 
			IBillButton.Cancel,
			IBillButton.Print
			
			
		};
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "H4H1Ha";
	}

	public String[] getBillVoName() {
		return new String[] { 
			MyBillVO.class.getName(),
			CdSbodyVO.class.getName(), 
			CdSbodyVO.class.getName() 
		};
	}

	/**
	 * 
	 * <P>������ҳ�����������,��ѯʱ��Ϊ����
	 * <BR>����˵��
	 * @return
	 * @see nc.ui.trade.controller.IControllerBase#getBodyCondition()
	 */
	public String getBodyCondition() {
		return "pk_corp='" + ClientEnvironment.getInstance().getCorporation().getPk_corp() + "'";
	}

	public String getBodyZYXKey() {
		return null;
	}

	/**
	 * �Ƿ�ƽ̨�޹�
	 */
	public int getBusinessActionType() {
		return IBusinessActionType.BD;
	}

	public String getChildPkField() {
		return "pk_sbody";
	}

	public String getHeadZYXKey() {
		return null;
	}

	public String getPkField() {
		return "pk_sbody";
	}

	public Boolean isEditInGoing() throws Exception {
		return null;
	}

	public boolean isExistBillStatus() {
		return false;
	}

	public boolean isLoadCardFormula() {
		return false;
	}

	/**
	 * �Ƿ񵥱�
	 * 
	 * @return boolean true:�����壬false:����ͷ
	 */
	public boolean isSingleDetail() {
		return true; // ������
	}

}
