package nc.ui.dip.datarec;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.dip.buttons.ControlBtn;
import nc.ui.dip.buttons.DataCheckBtn;
import nc.ui.dip.buttons.DataStyleBtn;
import nc.ui.dip.buttons.JSDYDataFomatBtn;
import nc.ui.dip.buttons.ZHmbBtn;
import nc.ui.dip.buttons.folder.AddFolderBtn;
import nc.ui.dip.buttons.folder.DeleteFolderBtn;
import nc.ui.dip.buttons.folder.EditFolderBtn;
import nc.ui.dip.buttons.folder.FolderManageBtn;
import nc.ui.dip.buttons.folder.MoveFolderBtn;
import nc.ui.dip.datarec.DataRecUICheckRuleGetter;
import nc.ui.pub.linkoperate.*;
import nc.vo.trade.button.ButtonVO;
import nc.vo.pub.AggregatedValueObject;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.treecard.BillTreeCardUI;




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
  public abstract class AbstractDataRecUI extends BillTreeCardUI implements  ILinkQuery{

	protected ICardController createController() {
		return new DataRecUICtrl();
	}
	
	/**
	 * ������ݲ���ƽ̨ʱ��UI����Ҫ���ش˷��������ز���ƽ̨��ҵ������� 
	 * @return BusinessDelegator ����ƽ̨��ҵ�������
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new nc.ui.dip.datarec.MyDelegator();
	}

	/**
	 * ע���Զ��尴ť
	 */
	protected void initPrivateButton() {
		//DataStyleBtn dsb=new DataStyleBtn();
		//addPrivateButton(dsb.getButtonVO());
		JSDYDataFomatBtn df=new JSDYDataFomatBtn();
		addPrivateButton(df.getButtonVO());
		
		DataCheckBtn dcb=new DataCheckBtn();
		addPrivateButton(dcb.getButtonVO());

		//���� 2011-7-20
		ControlBtn cb=new ControlBtn();
		addPrivateButton(cb.getButtonVO());
//		ModelSZBtn mszb=new ModelSZBtn();
//		addPrivateButton(mszb.getButtonVO());
		
		boolean hasCommit = false;
		boolean hasAudit = false;
		boolean hasCancelAudit = false;
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
		addPrivateButton(new FolderManageBtn().getButtonVO());
		addPrivateButton(new AddFolderBtn().getButtonVO());
		addPrivateButton(new EditFolderBtn().getButtonVO());
		addPrivateButton(new DeleteFolderBtn().getButtonVO());
		addPrivateButton(new MoveFolderBtn().getButtonVO());
		addPrivateButton(new  ZHmbBtn().getButtonVO() );
	}

	/**
	 * ע��ǰ̨У����
	 */
	public Object getUserObject() {
		return new DataRecUICheckRuleGetter();
	}
	
	public void doQueryAction(ILinkQueryData querydata) {
	        String billId = querydata.getBillID();
	        if (billId != null) {
	            try {
	            	AggregatedValueObject vo = loadHeadData(billId);
	                getBufferData().addVOToBuffer(vo);
	                getBufferData().setCurrentRow(getBufferData().getCurrentRow());
	                setBillOperate(IBillOperate.OP_NO_ADDANDEDIT);
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        }
    	}
}
