package nc.ui.dip.datasend;


import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.bill.ISingleController;

import nc.vo.dip.datasend.DipDatasendVO;
import nc.vo.dip.datasend.MyBillVO;
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

public class DataSendClientUICtrl implements ITreeCardController,ISingleController{

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		                                
                return new int[]{
                		IBillButton.Refresh,
                                                               IBillButton.Add,
                                                                                    IBillButton.Edit,
                                                                                    IBillButton.Save,
                                                                                    IBillButton.Cancel,
                                                                                    IBillButton.Delete,
                                                                                    IBtnDefine.SENDFORM,
                                                                                    IBtnDefine.DATASEND,
                                                                                    IBtnDefine.YuJing,
                                                                                    IBtnDefine.CONTROL,
                                                                                    IBtnDefine.folderManageBtn,
                                                                            
                                                         };
  
	}
	
	public int[] getListButtonAry() {		
			        	        return new int[]{
	         	           	             IBillButton.Add,
	           	         	           	             IBillButton.Edit,
	           	         	           	             IBillButton.Save,
	           	         	           	             IBillButton.Cancel,
	           	         	           	             IBillButton.Delete,
	           	         	           	             IBillButton.Refresh,
	           	         	           	      IBtnDefine.YuJing,
	           	         	           	      IBtnDefine.DATASEND,
	           	         	           	      IBtnDefine.SENDFORM
	           	         	        
	        };
	
	}

	public boolean isShowCardRowNo() {
		return false;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "H4H3H7H0";
	}

	public String[] getBillVoName() {
		return new String[]{
			MyBillVO.class.getName(),
			DipDatasendVO.class.getName(),
			DipDatasendVO.class.getName()
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
	 * 2011-4-11 wangshuai
	 * 自动构建树
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
