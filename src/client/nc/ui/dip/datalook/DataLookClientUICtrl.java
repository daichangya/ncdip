package nc.ui.dip.datalook;

import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.trade.bill.ISingleController;

import nc.vo.dip.datalook.DipDatalookVO;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.treecard.ITreeCardController;


/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 * Create on 2006-4-6 16:00:51
 *
 * @author authorName
 * @version tempProject version
 */

public class DataLookClientUICtrl  implements ITreeCardController, ISingleController{

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {

		return new int[]{
//				IBillButton.Query,

				IBillButton.Refresh,
				IBtnDefine.SET,
				IBtnDefine.DATALOOKQUERY,
				IBillButton.Add,
				IBillButton.Edit,
//				IBillButton.Line,
				IBillButton.Save,
				IBillButton.Cancel,
				IBtnDefine.DELETE,
				IBillButton.ImportBill,
				IBillButton.ExportBill,
				IBtnDefine.DATACLEAR,
				//IBillButton.Return,
//				IBillButton.Refresh
		};

	}

	/*public int[] getListButtonAry() {		
		return new int[]{
				IBillButton.Query,
				IBillButton.Add,
				IBillButton.Edit,
				IBillButton.Line,
				IBillButton.Save,
				IBillButton.Cancel,
				IBillButton.Delete,
				IBillButton.Card,
				IBillButton.Refresh

		};

	}*/

	public boolean isShowCardRowNo() {
		return false;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "H4H3Ha";
	}

	public String[] getBillVoName() {
		return new String[]{
//				MyBillVO.class.getName(),
				nc.vo.trade.pub.HYBillVO.class.getName(),
				DipDatalookVO.class.getName(),
				DipDatalookVO.class.getName()
		};
	}

	public String getBodyCondition() {
		return null;
	}

	public String getBodyZYXKey() {
		return null;
	}

	public int getBusinessActionType() {
		return IBusinessActionType.BD;
	}

	public String getChildPkField() {
		return null;
	}

	public String getHeadZYXKey() {
		return null;
	}

	public String getPkField() {
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

	/*public String[] getListBodyHideCol() {	
		return null;
	}

	public String[] getListHeadHideCol() {		
		return null;
	}

	public boolean isShowListRowNo() {		
		return false;
	}

	public boolean isShowListTotal() {
		return false;
	}*/

	/**
	 * 是否单表
	 * @return boolean true:单表体，false:单表头
	 */ 
	public boolean isSingleDetail() {
		return true; //单表体
	}
	/**
	 * 
	 * <P>
	 * 单据增加删除时,是否自动维护树结构 <BR>
	 * 功能说明
	 * 
	 * @return
	 * @see nc.ui.trade.treecard.ITreeCardController#isAutoManageTree()
	 */
	public boolean isAutoManageTree() {
		return false;
	}

	public boolean isChildTree() {
		return false;
	}

	public boolean isTableTree() {
		return false;
	}

}
