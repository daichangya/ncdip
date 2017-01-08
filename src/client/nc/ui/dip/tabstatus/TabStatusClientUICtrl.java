package nc.ui.dip.tabstatus;

import nc.ui.trade.bill.IListController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.dip.tabstatus.DipTabstatusVO;
import nc.vo.dip.tabstatus.MyBillVO;
import nc.vo.dip.tabstatus.TabstatusBVO;


/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 *
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
 * </p>
 *
 * Create on 2006-4-6 16:00:51
 *
 * @author authorName
 * @version tempProject version
 */

public class TabStatusClientUICtrl  implements IListController {

	public String[] getCardBodyHideCol() {
		return null;
	}

//	public int[] getListButtonAry() {
//		                                
//                return new int[]{
//                                                               IBillButton.Query,
////                                                                                    IBillButton.Add,
////                                                                                    IBillButton.Edit,
////                                                                                    IBillButton.Save,
////                                                                                    IBillButton.Cancel,
////                                                                                    IBillButton.Delete,
//                                                                                    IBillButton.Refresh,
////                                                                                    IBillButton.Return,
////                                                                                    IBillButton.Line
//                                                         };
//  
//	}
	
	public int[] getListButtonAry() {		
			        	        return new int[]{
	         	           	             IBillButton.Query,
//	           	         	           	             IBillButton.Add,
//	           	         	           	             IBillButton.Edit,
//	           	         	           	             IBillButton.Save,
//	           	         	           	             IBillButton.Cancel,
//	           	         	           	             IBillButton.Delete,
	           	         	           	             IBillButton.Refresh,
//	           	         	           	             IBillButton.Card
	           	         	        
	        };
	
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "H4H4H1";
	}

	public String[] getBillVoName() {
		return new String[]{
			MyBillVO.class.getName(),
			DipTabstatusVO.class.getName(),
			TabstatusBVO.class.getName()
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
	
	/**
	 * �Ƿ񵥱�
	 * @return boolean true:�����壬false:����ͷ
	 */ 
	public boolean isSingleDetail() {
		return false; //����ͷ
	}

	
	
}
