package nc.ui.dip.datasynch;

import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.treecard.ITreeCardController;
import nc.vo.dip.datasynch.DipDatasynchVO;
import nc.vo.dip.datasynch.MyBillVO;


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

public class DataSynchUICtrl implements ITreeCardController,ISingleController{

	public String[] getCardBodyHideCol() {
		return null;
	}

	//注释掉模板设置、数据校验 按钮
	public int[] getCardButtonAry() {

		return new int[]{
				IBillButton.Refresh,
				IBillButton.Add,
				IBillButton.Edit,
				IBillButton.Save,
				IBillButton.Cancel,
				IBillButton.Delete,
				IBtnDefine.TBFORM,
				IBtnDefine.Synch,
				IBtnDefine.DataValidate,
				IBtnDefine.EarlyWarning,
				IBtnDefine.CONTROL,
				IBtnDefine.folderManageBtn
//				IBtnDefine.DataStyle,
//				IBtnDefine.DataCheck,
//IBtnDefine.ModelSZ
		};

	}

	//注释掉模板设置数据校验 按钮
	public int[] getListButtonAry() {		
		return new int[]{
				IBillButton.Add,
				IBillButton.Edit,
				IBillButton.Save,
				IBillButton.Cancel,
				IBillButton.Delete,
				IBtnDefine.Synch,
				IBtnDefine.DataValidate,
				IBtnDefine.EarlyWarning,
				IBtnDefine.DataStyle,
//				IBtnDefine.DataCheck,
//IBtnDefine.ModelSZ

		};

	}

	public boolean isShowCardRowNo() {
		return false;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "H4H3H3H1";
	}

	public String[] getBillVoName() {
		return new String[]{
				MyBillVO.class.getName(),
				DipDatasynchVO.class.getName(),
				DipDatasynchVO.class.getName()
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

	public String[] getListBodyHideCol() {	
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
	}

	/**
	 * 是否单表
	 * @return boolean true:单表体，false:单表头
	 */ 
	public boolean isSingleDetail() {
		return false; //单表头
	}

	/**
	 * 2011-4-11 chengli
	 */
	public boolean isAutoManageTree() {
		return true;
	}

	public boolean isTableTree() {
		return false;
	}

	public boolean isChildTree() {
		// TODO Auto-generated method stub
		return false;
	}
}
