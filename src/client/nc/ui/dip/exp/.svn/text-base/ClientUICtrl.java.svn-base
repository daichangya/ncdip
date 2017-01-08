package nc.ui.dip.exp;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.dip.exp.DataExpVO;
import nc.vo.dip.exp.MyBillVO;

/**
 * <b> 卡片型单表体UI控制器类</b><br>
 * 
 * <p>
 * 设置界面按钮，数据，是否平台相关等信息
 * </p>
 * <br>
 * 
 * @author 何冰
 * @version tempProject version
 */

public class ClientUICtrl implements ICardController, ISingleController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	/**
	 * 设置界面按钮
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
		return "H4H1He";
	}

	public String[] getBillVoName() {
		return new String[] { 
			MyBillVO.class.getName(),
			DataExpVO.class.getName(), 
			DataExpVO.class.getName() 
		};
	}

	/**
	 * 
	 * <P>单表体页面必须加上这个,查询时作为条件
	 * <BR>功能说明
	 * @return
	 * @see nc.ui.trade.controller.IControllerBase#getBodyCondition()
	 */
	public String getBodyCondition() {
//		return "pk_corp='" + ClientEnvironment.getInstance().getCorporation().getPk_corp() + "'";
		return null;
	}

	public String getBodyZYXKey() {
		return null;
	}

	/**
	 * 是否平台无关
	 */
	public int getBusinessActionType() {
		return IBusinessActionType.BD;
	}

	public String getChildPkField() {
//		return "pk_sbody";
		return null;
	}

	public String getHeadZYXKey() {
		return null;
	}

	public String getPkField() {
//		return "pk_sbody";
		return null;
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
	 * 是否单表
	 * 
	 * @return boolean true:单表体，false:单表头
	 */
	public boolean isSingleDetail() {
		return true; // 单表体
	}

}
