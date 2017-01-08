package nc.ui.dip.statemanage;

import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.treecard.ITreeCardController;
import nc.vo.dip.statemanage.MyBillVO;
import nc.vo.dip.statemanage.DipStateManageHVO;




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

public class StateManageUICtrl implements ITreeCardController, ISingleController {

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
                                                                                   // IBillButton.Line,
                                                                                   // IBillButton.Query,
                                                                                    IBtnDefine.DataProce,
//                                                                                    IBtnDefine.DataProcessCheck,
//                                                                                    IBtnDefine.DataValidate,
//                                                                                    IBtnDefine.YuJing,
//                                                                                    IBtnDefine.CONTROL,
                                                                                    IBtnDefine.folderManageBtn
//                                                                                    IBtnDefine.CreateTable,
//                                                                                    IBtnDefine.Movedup,
//                                                                                    IBtnDefine.Moveddown
                                                         };
  
	}
	
	public int[] getListButtonAry() {		
			        	        return new int[]{
	         	           	                         IBillButton.Add,
	         	           	                         IBillButton.Edit,
	           	         	           	             IBillButton.Delete,
	           	         	           	             IBillButton.Cancel,
	           	         	           	            // IBillButton.Line,
	           	         	           	            // IBillButton.Query,
	           	         	           	             IBillButton.Save,
	           	         	           	             IBtnDefine.DataProce,
	           	         	           	             IBtnDefine.DataValidate,
	           	         	           	             IBtnDefine.YuJing,
	           	         	           	             IBillButton.Refresh,
	           	         	           	             IBtnDefine.CONTROL
//	           	         	           	             IBtnDefine.CreateTable,
//	           	         	                         IBtnDefine.Movedup,
//	           	         	                         IBtnDefine.Moveddown
	           	         	           	      
	           	         	        
	        };
	
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "H4H3He";
	}

	public String[] getBillVoName() {
		return new String[]{
			MyBillVO.class.getName(),
			DipStateManageHVO.class.getName(),
			DipStateManageHVO.class.getName()
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
	 * 2011-04-8 wangshuai
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


