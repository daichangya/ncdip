package nc.ui.dip.authorize.maintain;

import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.trade.businessaction.IBusinessActionType;

import nc.ui.trade.button.IBillButton;
import nc.ui.trade.treemanage.AbstractTreeManageController;
import nc.vo.dip.authorize.maintain.DipADContdatawhHVO;
import nc.vo.dip.authorize.maintain.MyBillVO;

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

public class ContDataWHClientUICtrl extends AbstractTreeManageController {

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {

		return new int[]{
				IBillButton.Query,

//				IBillButton.Add,
//				IBillButton.Edit,
//				IBillButton.Save,
				IBillButton.Cancel,
				IBillButton.Refresh,
//				IBillButton.Delete,
//				IBillButton.Line,
//				IBillButton.Return,
//				IBillButton.ImportBill,
//				IBillButton.ExportBill,
//				IBtnDefine.VALIDATECHECK,
//				IBtnDefine.DATACLEAR,
//				IBtnDefine.EXPORTMODEL

//				IBillButton.Add,
//				IBillButton.Edit,
//				IBillButton.Save,
//				IBillButton.Cancel,
//				IBillButton.Delete,
//				IBillButton.Line,
//				IBillButton.Return,
//				IBillButton.ImportBill,
//				IBillButton.ExportBill,
//				IBtnDefine.VALIDATECHECK,
//				IBtnDefine.DATACLEAR,
//				IBtnDefine.EXPORTMODEL,
//				IBtnDefine.CONTSYSQUERY,
//				IBtnDefine.BCONTSYSQUERY

		};

	}

	public int[] getListButtonAry() {		
		return new int[]{
//				IBillButton.Query,
				IBillButton.Refresh,
				IBtnDefine.SET,
				IBtnDefine.BCONTSYSQUERY,
				IBtnDefine.edit,
				IBtnDefine.CONTSYSQUERY,
				
				IBtnDefine.CONTSAVE,
//				IBillButton.Add,
//				IBillButton.Edit,
//				IBillButton.Save,
				IBillButton.Cancel,
//				IBillButton.Delete,
//				IBillButton.Line,
//				IBillButton.Card,
				IBillButton.ImportBill,
				IBillButton.ExportBill,
				//2011-6-10 校验检查
				IBtnDefine.DATACLEAR,
				IBtnDefine.VALIDATECHECK,
//				IBtnDefine.EXPORTMODEL,
//				IBtnDefine.contresut,
				IBtnDefine.DATACHECK,
				IBtnDefine.autoContData,
				IBtnDefine.FAST_SET

		};

	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "H4H3H4H1";
	}

	public String[] getBillVoName() {
		return new String[]{
				MyBillVO.class.getName(),
				DipADContdatawhHVO.class.getName(),

				DipADContdatawhHVO.class.getName()
		};
	}

	public String getBodyCondition() {
		return null;
	}

	public String getBodyZYXKey() {
		return null;
	}

	public int getBusinessActionType() {
//		return IBusinessActionType.BD;
		return IBusinessActionType.PLATFORM;
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

	public String[] getListBodyHideCol() {	
		return null;
	}

	public String[] getListHeadHideCol() {		
		return null;
	}

	public boolean isShowListRowNo() {		
		return true;
	}

	public boolean isShowListTotal() {
		return false;
	}

	/**
	 * 是否单表
	 * @return boolean true:单表体，false:单表头
	 */ 
//	public boolean isSingleDetail() {
//		return false; 
//	}

	/**
	 * 2011-4-11 chengli
	 */
	public boolean isAutoManageTree() {
		return true;
	}
//	public boolean isChildTree() {
//		return false;
//	}

	public boolean isTableTree() {
		return false;
	}
}
