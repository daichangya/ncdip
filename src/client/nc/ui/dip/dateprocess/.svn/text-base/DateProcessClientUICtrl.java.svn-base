package nc.ui.dip.dateprocess;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.dip.dateprocess.DateprocessVO;
import nc.vo.dip.dateprocess.MyBillVO;


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

public class DateProcessClientUICtrl /*extends AbstractManageController*/ implements ICardController,ISingleController{

	public String[] getCardBodyHideCol() {
		return null;
	}

	public int[] getCardButtonAry() {
		                                
                return new int[]{
                                                               IBillButton.Query,
//                                                                                    IBillButton.Add,
//                                                                                    IBillButton.Edit,
//                                                                                    IBillButton.Save,
//                                                                                    IBillButton.Cancel,
                                                                                    IBillButton.Delete,
                                                                                 //   IBillButton.Refresh,
//                                                                                    IBillButton.Return,
                          //                                                          IBillButton.ExportBill
                                                                                   
                                                         };
  
	}
	
	public int[] getListButtonAry() {		
			        	        return new int[]{
	         	           	             IBillButton.Query,
//	           	         	           	             IBillButton.Add,
//	           	         	           	             IBillButton.Edit,
//	           	         	           	             IBillButton.Save,
//	           	         	           	             IBillButton.Cancel,
	           	         	           	             IBillButton.Delete,
	           	         	           	            // IBillButton.Refresh,
//	           	         	           	             IBillButton.Card,
//	           	         	           	             IBillButton.ExportBill
	           	         	           	          
	           	         	        
	        };
	
	}

	public boolean isShowCardRowNo() {
		return true;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBillType() {
		return "H4H4H4";
	}

	public String[] getBillVoName() {
		return new String[]{
			MyBillVO.class.getName(),
			DateprocessVO.class.getName(),
			DateprocessVO.class.getName()
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
		return null;
	}

	public String getBodyZYXKey() {
		return null;
	}

	public int getBusinessActionType() {
		return IBusinessActionType.BD;
	}

	public String getChildPkField() {
		return "pk_datacheckstat";
	}

	public String getHeadZYXKey() {
		return null;
	}

	public String getPkField() {
		return "pk_datacheckstat";
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

//	public String[] getListBodyHideCol() {	
//		return null;
//	}
//
//	public String[] getListHeadHideCol() {		
//		return null;
//	}
//
//	public boolean isShowListRowNo() {		
//		return true;
//	}
//
//	public boolean isShowListTotal() {
//		return false;
//	}
	
	/**
	 * 是否单表
	 * @return boolean true:单表体，false:单表头
	 */ 
	public boolean isSingleDetail() {
		return true; //单表体
	}
}
