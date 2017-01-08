package nc.ui.dip.datalookquery;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.button.IBillButton;
import nc.vo.dip.contwhquery.ContwhqueryVO;


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

public class DatalookQueryClientUICtrl implements ICardController, ISingleController{


	public DatalookQueryClientUICtrl()
	{
	}

	public String[] getCardBodyHideCol()
	{
		return null;
	}

	public int[] getCardButtonAry()
	{
		return new int[]{
				IBillButton.Add,
				IBillButton.Save,
				IBillButton.Edit,
				IBillButton.Line,
				IBillButton.Cancel,
		};
	}

	public boolean isShowCardRowNo()
	{
		return true;
	}

	public boolean isShowCardTotal()
	{
		return false;
	}

	public String getBillType()
	{
		return "H4H6H0";
	}

	public String[] getBillVoName()
	{
		return (new String[] {
				nc.vo.trade.pub.HYBillVO.class.getName(),
				ContwhqueryVO.class.getName(),
				ContwhqueryVO.class.getName()
		});

	}

	public String getBodyCondition()
	{
		return null;
	}

	public String getBodyZYXKey()
	{
		return null;
	}

	public int getBusinessActionType()
	{
		return 1;
	}

	public String getChildPkField()
	{
		return "pk_contwhquery";
	}

	public String getHeadZYXKey()
	{
		return null;
	}

	public String getPkField()
	{
		return "pk_contwhquery";
	}

	public Boolean isEditInGoing()
	throws Exception
	{
		return null;
	}

	public boolean isExistBillStatus()
	{
		return false;
	}

	public boolean isLoadCardFormula()
	{
		return false;
	}

	public boolean isSingleDetail()
	{
		return true;
	}
}
