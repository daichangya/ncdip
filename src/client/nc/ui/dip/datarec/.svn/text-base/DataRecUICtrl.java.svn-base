package nc.ui.dip.datarec;

import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;

import nc.vo.dip.datarec.DipDatarecBVO;
import nc.vo.dip.datarec.DipDatarecHVO;
import nc.vo.dip.datarec.MyBillVO;
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

public class DataRecUICtrl implements ITreeCardController, ISingleController{

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {

		return new int[]{
				// IBillButton.Query,
				IBillButton.Refresh,
				IBillButton.Copy,
				IBillButton.Add,
				IBillButton.Edit,
				//IBillButton.Line,
				IBillButton.Save,
				IBillButton.Cancel,
				IBillButton.Delete,
				//IBtnDefine.DataStyle,
				IBtnDefine.JSDYDATAFORMAT,
				IBtnDefine.DataCheck,
				IBtnDefine.CONTROL,
				IBtnDefine.folderManageBtn,
				IBtnDefine.MBZH
		};

	}

	public int[] getListButtonAry() {		
		return new int[]{
				// IBillButton.Query,
				IBillButton.Add,
				IBillButton.Edit,
				//IBillButton.Line,
				IBillButton.Save,
				IBillButton.Cancel,
				IBillButton.Copy,
				IBillButton.Delete,
				IBillButton.Refresh,
				//IBtnDefine.DataStyle,
				IBtnDefine.DataCheck


		};

	}

	public boolean isShowCardRowNo() {
		return false;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "H4H3H3H0";
	}

	public String[] getBillVoName() {
		return new String[]{
				MyBillVO.class.getName(),
				DipDatarecHVO.class.getName(),
				DipDatarecHVO.class.getName()
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
	 * 2011-04-11 chengli
	 * <P>
	 * 单据增加删除时,是否自动维护树结构 <BR>
	 * 功能说明
	 * 
	 * @return
	 * @see nc.ui.trade.treecard.ITreeCardController#isAutoManageTree()
	 */
	public boolean isAutoManageTree() {
		return true;
	}

	public boolean isChildTree() {
		return false;
	}

	public boolean isTableTree() {
		return false;
	}

	public boolean isSingleDetail() {
		return true;
	}
}
