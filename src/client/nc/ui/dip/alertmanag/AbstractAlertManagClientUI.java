package nc.ui.dip.alertmanag;

import nc.ui.dip.buttons.CleanBtn;
import nc.ui.dip.buttons.ControlBtn;
import nc.ui.dip.buttons.DisableBtn;
import nc.ui.dip.buttons.EnableBtn;
import nc.ui.dip.buttons.YuJingBtn;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.vo.trade.button.ButtonVO;

/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 *
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
/*�����б���ͷ�޸Ĵ���
 * 2011-5-14
 * zlc
 * */
  public abstract class AbstractAlertManagClientUI extends nc.ui.trade.list.BillListUI implements ILinkQuery/*nc.ui.trade.manage.BillManageUI implements  ILinkQuery*/{

	/*protected AbstractManageController createController() {
		return new AlertManagClientUICtrl();
	}*/
	  
	  /**
		 * �˷�������������
		 */
		protected IListController createController() {
			return new AlertManagClientUICtrl();
		}
	
	/**
	 * ������ݲ���ƽ̨ʱ��UI����Ҫ���ش˷��������ز���ƽ̨��ҵ������� 
	 * @return BusinessDelegator ����ƽ̨��ҵ�������
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new nc.ui.dip.alertmanag.MyDelegator();
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
		/*int[] cardButns = getUIControl().getCardButtonAry();
		for (int i = 0; i < cardButns.length; i++) {
			if( cardButns[i] == nc.ui.trade.button.IBillButton.Commit )
				hasCommit = true;
			if( cardButns[i] == nc.ui.trade.button.IBillButton.Audit )
				hasAudit = true;
			if( cardButns[i] == nc.ui.trade.button.IBillButton.CancelAudit )
				hasCancelAudit = true;
		}		*/
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
		
		DisableBtn db=new DisableBtn();
		addPrivateButton(db.getButtonVO());
		
		EnableBtn eb=new EnableBtn();
		addPrivateButton(eb.getButtonVO());
		
		ControlBtn cb=new ControlBtn();
		addPrivateButton(cb.getButtonVO());
		
		YuJingBtn yjb=new YuJingBtn();
		addPrivateButton(yjb.getButtonVO());
		addPrivateButton(new CleanBtn().getButtonVO());
	}

	/**
	 * ע��ǰ̨У����
	 */
	public Object getUserObject() {
		return new AlertManagClientUICheckRuleGetter();
	}
	
	public void doQueryAction(ILinkQueryData querydata) {
	        String billId = querydata.getBillID();
	        if (billId != null) {
	            try {
	            	getBufferData().addVOToBuffer(loadHeadData(billId));
					getBufferData().setCurrentRow(getBufferData().getCurrentRow());
					setBillOperate(IBillOperate.OP_NO_ADDANDEDIT);
	            	/*setCurrentPanel(BillTemplateWrapper.CARDPANEL);
	            	AggregatedValueObject vo = loadHeadData(billId);
	                getBufferData().addVOToBuffer(vo);
	                setListHeadData(new CircularlyAccessibleValueObject[]{vo.getParentVO()});
	                getBufferData().setCurrentRow(getBufferData().getCurrentRow());
	                setBillOperate(IBillOperate.OP_NO_ADDANDEDIT);*/
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        }
    	}
}
