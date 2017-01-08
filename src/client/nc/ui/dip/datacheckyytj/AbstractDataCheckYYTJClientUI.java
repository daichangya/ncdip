package nc.ui.dip.datacheckyytj;

import nc.ui.trade.bill.IListController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.dip.buttons.DataValidateBtn;
import nc.ui.dip.buttons.StartBtn;
import nc.ui.dip.buttons.StopBtn;
import nc.ui.pub.linkoperate.*;
import nc.vo.trade.button.ButtonVO;
import nc.ui.trade.base.IBillOperate;




/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 *
 * <p>
 *     �ڴ˴����Ӵ����������Ϣ
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
  public abstract class AbstractDataCheckYYTJClientUI extends nc.ui.trade.list.BillListUI implements  ILinkQuery{

	  /**
		 * �˷�������������
		 */
		protected IListController createController() {
			return new DataCheckYYTJClientUICtrl();
		}

	
	/**
	 * ������ݲ���ƽ̨ʱ��UI����Ҫ���ش˷��������ز���ƽ̨��ҵ������� 
	 * @return BusinessDelegator ����ƽ̨��ҵ�������
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new nc.ui.dip.datacheckyytj.MyDelegator();
	}

	/**
	 * ע���Զ��尴ť
	 */
	protected void initPrivateButton() {		
		int[] listButns = getUIControl().getListButtonAry();
		boolean hasCommit = false;
		boolean hasAudit = false;
		boolean hasCancelAudit = false;
		for (int i = 0; i < listButns.length; i++) {
			if( listButns[i] == nc.ui.trade.button.IBillButton.Commit )
				hasCommit = true;
			if( listButns[i] == nc.ui.trade.button.IBillButton.Audit )
				hasAudit = true;
			if( listButns[i] == nc.ui.trade.button.IBillButton.CancelAudit )
				hasCancelAudit = true;
		}
//		int[] cardButns = getUIControl().getCardButtonAry();
//		for (int i = 0; i < cardButns.length; i++) {
//			if( cardButns[i] == nc.ui.trade.button.IBillButton.Commit )
//				hasCommit = true;
//			if( cardButns[i] == nc.ui.trade.button.IBillButton.Audit )
//				hasAudit = true;
//			if( cardButns[i] == nc.ui.trade.button.IBillButton.CancelAudit )
//				hasCancelAudit = true;
//		}		
		if( hasCommit ){
			ButtonVO btnVo = nc.ui.trade.button.ButtonVOFactory.getInstance()
			.build(nc.ui.trade.button.IBillButton.Commit);
			btnVo.setBtnCode(null);
			addPrivateButton(btnVo);
		}
		
		if( hasAudit ){
			ButtonVO btnVo2 = nc.ui.trade.button.ButtonVOFactory.getInstance()
				.build(nc.ui.trade.button.IBillButton.Audit);
			btnVo2.setBtnCode(null);
			addPrivateButton(btnVo2);
		}
		
		if( hasCancelAudit ){
			ButtonVO btnVo3 = nc.ui.trade.button.ButtonVOFactory.getInstance()
			.build(nc.ui.trade.button.IBillButton.CancelAudit);
			btnVo3.setBtnCode(null);
			addPrivateButton(btnVo3);	
		}	
		DataValidateBtn db=new DataValidateBtn();
		addPrivateButton(db.getButtonVO());
	}

	/**
	 * ע��ǰ̨У����
	 */
	public Object getUserObject() {
		return new DataCheckYYTJClientUICheckRuleGetter();
	}
	
	public void doQueryAction(ILinkQueryData querydata) {
	        String billId = querydata.getBillID();
	        if (billId != null) {
	            try {
//	            	setCurrentPanel(BillTemplateWrapper.CARDPANEL);
//	            	AggregatedValueObject vo = loadHeadData(billId);
//	                getBufferData().addVOToBuffer(vo);
//	                setListHeadData(new CircularlyAccessibleValueObject[]{vo.getParentVO()});
	            	getBufferData().addVOToBuffer(loadHeadData(billId));
	                getBufferData().setCurrentRow(getBufferData().getCurrentRow());
	                setBillOperate(IBillOperate.OP_NO_ADDANDEDIT);
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        }
    	}
}