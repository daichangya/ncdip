package nc.ui.dip.authorize.define;

import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.bill.ISingleController;

import nc.ui.trade.button.IBillButton;
import nc.ui.trade.treecard.ITreeCardController;
import nc.vo.dip.authorize.define.DipADContdataBVO;
import nc.vo.dip.authorize.define.DipADContdataVO;
import nc.vo.dip.authorize.define.MyBillVO;

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

public class ContDataClientUICtrl implements ITreeCardController,ISingleController{

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		                                
                return new int[]{
                		IBillButton.Refresh,
                		IBtnDefine.SET,
                                                               IBillButton.Add,
                                                               IBillButton.Line,
	                                                           IBillButton.Edit,
	                                                           IBillButton.Save,
	                                                           IBillButton.Cancel,
	                                                           IBillButton.Delete,
	                                                           IBtnDefine.folderManageBtn,
	                                                           IBtnDefine.ACTION_SET
                                                         };
  
	}
	
	public int[] getListButtonAry() {		
			        	        return new int[]{
	         	           	              IBillButton.Add,
	         	           	              IBillButton.Line,
	   	         	           	             IBillButton.Edit,
	   	         	           	             IBillButton.Save,
	   	         	           	             IBillButton.Cancel,
	   	         	           	             IBillButton.Delete,
	   	         	           	             IBillButton.Refresh
	           	         	        
	        };
	
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "H4H3HcH1";
	}

	public String[] getBillVoName() {
		return new String[]{
			MyBillVO.class.getName(),
			DipADContdataVO.class.getName(),
			DipADContdataBVO.class.getName()
			
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
		return true; //单表头
	}
	
	/**
	 * 2011-4-11 chengli
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
}
