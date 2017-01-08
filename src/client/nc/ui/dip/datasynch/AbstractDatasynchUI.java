package nc.ui.dip.datasynch;

import nc.ui.dip.buttons.ControlBtn;
import nc.ui.dip.buttons.CreateTableBtn;
import nc.ui.dip.buttons.DataStyleBtn;
import nc.ui.dip.buttons.DataValidateBtn;
import nc.ui.dip.buttons.EarlyWarningBtn;
import nc.ui.dip.buttons.SynchBtn;
import nc.ui.dip.buttons.TBFormBtn;
import nc.ui.dip.buttons.folder.AddFolderBtn;
import nc.ui.dip.buttons.folder.DeleteFolderBtn;
import nc.ui.dip.buttons.folder.EditFolderBtn;
import nc.ui.dip.buttons.folder.FolderManageBtn;
import nc.ui.dip.buttons.folder.MoveFolderBtn;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
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
  public abstract class AbstractDatasynchUI extends BillTreeCardUI implements  ILinkQuery{

	protected ICardController createController() {
		return new DataSynchUICtrl();
	}
	
	/**
	 * ������ݲ���ƽ̨ʱ��UI����Ҫ���ش˷��������ز���ƽ̨��ҵ������� 
	 * @return BusinessDelegator ����ƽ̨��ҵ�������
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new nc.ui.dip.datasynch.MyDelegator();
	}

	/**
	 * ע���Զ��尴ť
	 */
	//��ģ�����á�����У��ע�͵�
	protected void initPrivateButton() {
//		int[] listButns = getUIControl().getListButtonAry();
		boolean hasCommit = false;
		boolean hasAudit = false;
		boolean hasCancelAudit = false;
//		for (int i = 0; i < listButns.length; i++) {
//			if( listButns[i] == nc.ui.trade.button.IBillButton.Commit )
//				hasCommit = true;
//			if( listButns[i] == nc.ui.trade.button.IBillButton.Audit )
//				hasAudit = true;
//			if( listButns[i] == nc.ui.trade.button.IBillButton.CancelAudit )
//				hasCancelAudit = true;
//		}
		int[] cardButns = getUIControl().getCardButtonAry();
		for (int i = 0; i < cardButns.length; i++) {
			if( cardButns[i] == nc.ui.trade.button.IBillButton.Commit )
				hasCommit = true;
			if( cardButns[i] == nc.ui.trade.button.IBillButton.Audit )
				hasAudit = true;
			if( cardButns[i] == nc.ui.trade.button.IBillButton.CancelAudit )
				hasCancelAudit = true;
		}		
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
		DataStyleBtn dsb=new DataStyleBtn();
		addPrivateButton(dsb.getButtonVO());

//		DataCheckBtn dcb=new DataCheckBtn();
//		addPrivateButton(dcb.getButtonVO());
//		ModelSZBtn mszb=new ModelSZBtn();
//		addPrivateButton(mszb.getButtonVO());
		
		//ͬ����ť
		SynchBtn synch=new SynchBtn();
		addPrivateButton(synch.getButtonVO());
		
		//����У�鰴ť
		DataValidateBtn dvb=new DataValidateBtn();
		addPrivateButton(dvb.getButtonVO());
		
		//Ԥ����ť
		EarlyWarningBtn ewb=new EarlyWarningBtn();
		addPrivateButton(ewb.getButtonVO());
		CreateTableBtn btn=new CreateTableBtn();
		addPrivateButton(btn.getButtonVO());
		TBFormBtn btn1=new TBFormBtn();
		addPrivateButton(btn1.getButtonVO());

		//���� 2011-7-20
		ControlBtn cb=new ControlBtn();
		addPrivateButton(cb.getButtonVO());

		addPrivateButton(new FolderManageBtn().getButtonVO());
		addPrivateButton(new AddFolderBtn().getButtonVO());
		addPrivateButton(new EditFolderBtn().getButtonVO());
		addPrivateButton(new DeleteFolderBtn().getButtonVO());
		addPrivateButton(new MoveFolderBtn().getButtonVO());
	}

	/**
	 * ע��ǰ̨У����
	 */
	public Object getUserObject() {
		return new DataSynchUICheckRuleGetter();
	}
	
	public void doQueryAction(ILinkQueryData querydata) {
	        String billId = querydata.getBillID();
	        if (billId != null) {
	            try {
//	            	setCurrentPanel(BillTemplateWrapper.CARDPANEL);
	            	AggregatedValueObject vo = loadHeadData(billId);
	                getBufferData().addVOToBuffer(vo);
	                setListHeadData(new CircularlyAccessibleValueObject[]{vo.getParentVO()});
	                getBufferData().setCurrentRow(getBufferData().getCurrentRow());
	                setBillOperate(IBillOperate.OP_NO_ADDANDEDIT);
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        }
    	}

	
	

	
}
