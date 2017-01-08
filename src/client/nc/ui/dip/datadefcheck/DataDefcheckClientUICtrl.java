package nc.ui.dip.datadefcheck;

import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.treemanage.AbstractTreeManageController;
import nc.vo.dip.datadefcheck.DipDatadefinitBVO;
import nc.vo.dip.datadefcheck.DipDatadefinitCVO;
import nc.vo.dip.datadefcheck.MyBillVO;


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

public class DataDefcheckClientUICtrl extends AbstractTreeManageController{

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		                                
                return new int[]{
//                                                               IBillButton.Query,
                		IBillButton.Refresh,
//                		IBillButton.Copy,
//                                                                                    IBillButton.Add,
                                                                                    IBillButton.Edit,
                                                                                    IBillButton.Line,
                                                                                    IBillButton.Save,
                                                                                    IBillButton.Cancel,
                                                                                    IBillButton.Delete,
//                                                                                    IBtnDefine.CreateTable,
//                                                                                    IBtnDefine.folderManageBtn,
//                                                                                    IBtnDefine.MBZH
                                                         };
  
	}
	
	public int[] getListButtonAry() {		
			        	        return new int[]{
			        	        		IBillButton.Refresh,
//	         	           	             IBillButton.Query,
//	           	         	           	             IBillButton.Add,
	           	         	           	             IBillButton.Edit,
	           	         	           	             IBillButton.Line,
	           	         	           	             IBillButton.Save,
	           	         	           	             IBillButton.Cancel,
	           	         	           	             IBillButton.Delete,
//	           	         	           	             IBillButton.Copy,
//	           	         	           	             IBtnDefine.CreateTable
	           	         	        
	        };
	
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "H4H3Hg";
	}

	public String[] getBillVoName() {
		return new String[]{
			MyBillVO.class.getName(),
			DipDatadefinitBVO.class.getName(),
			DipDatadefinitCVO.class.getName()
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
		return true;
	}

	public boolean isShowListTotal() {
		return false;
	}
/*
 * (non-Javadoc)
 * @see nc.ui.trade.treecard.ITreeCardController#isAutoManageTree()
 */
	public boolean isAutoManageTree() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isChildTree() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isTableTree() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSingleDetail() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
