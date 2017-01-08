package nc.ui.dip.datachange;

import nc.ui.dip.buttons.AddEffectFactorBtn;
import nc.ui.dip.buttons.ControlBtn;
import nc.ui.dip.buttons.ConversionBtn;
import nc.ui.dip.buttons.CreSetBtn;
import nc.ui.dip.buttons.CredentceBtn;
import nc.ui.dip.buttons.DataValidateBtn;
import nc.ui.dip.buttons.HbSet;
import nc.ui.dip.buttons.ModelBtn;
import nc.ui.dip.buttons.PasteModelBtn;
import nc.ui.dip.buttons.StartBtn;
import nc.ui.dip.buttons.StopBtn;
import nc.ui.dip.buttons.SysModelBtn;
import nc.ui.dip.buttons.YuJingBtn;
import nc.ui.dip.buttons.ZHmbBtn;
import nc.ui.dip.buttons.folder.AddFolderBtn;
import nc.ui.dip.buttons.folder.DeleteFolderBtn;
import nc.ui.dip.buttons.folder.EditFolderBtn;
import nc.ui.dip.buttons.folder.FolderManageBtn;
import nc.ui.dip.buttons.folder.MoveFolderBtn;
import nc.ui.dip.datachange.DataChangeClientUICheckRuleGetter;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.button.ButtonVO;




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
  public abstract class AbstractDataChangeClientUI  extends BillTreeCardUI implements  ILinkQuery{

	protected ICardController createController() {
		return new DataChangeClientUICtrl();
	}
	
	/**
	 * ������ݲ���ƽ̨ʱ��UI����Ҫ���ش˷��������ز���ƽ̨��ҵ������� 
	 * @return BusinessDelegator ����ƽ̨��ҵ�������
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new nc.ui.dip.datachange.MyDelegator();
	}

	/**
	 * ע���Զ��尴ť
	 */
	protected void initPrivateButton() {
		/**2011-4-12 15:01 chengli �Զ��塰ģ�塱����ת������ť begin */
		ZHmbBtn mbzh=new ZHmbBtn();
		addPrivateButton(mbzh.getButtonVO());
		
		ModelBtn mbtn=new ModelBtn();
		addPrivateButton(mbtn.getButtonVO());
		 
//		AddEffectFactorBtn factor=new AddEffectFactorBtn();
//		addPrivateButton(factor.getButtonVO());
//		
//		CredentceBtn btcre=new CredentceBtn();
//		addPrivateButton(btcre.getButtonVOCredence());
		
		ConversionBtn cbtn=new ConversionBtn();
		addPrivateButton(cbtn.getButtonVO());

		//���� 2011-7-20
		ControlBtn cb=new ControlBtn();
		addPrivateButton(cb.getButtonVO());
//		HbSet set=new HbSet();
//		addPrivateButton(set.getBtn());
//		/** end */
		
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
		
		//���ð�ť
		StopBtn stb=new StopBtn();
		addPrivateButton(stb.getButtonVO());
		//ͣ�ð�ť
		StartBtn spb=new StartBtn();
		addPrivateButton(spb.getButtonVO());
		//����У�鰴ť
		DataValidateBtn db=new DataValidateBtn();
		addPrivateButton(db.getButtonVO());
		//Ԥ����ť
		YuJingBtn yjb=new YuJingBtn();
		addPrivateButton(yjb.getButtonVO());
		
		//ճ��ģ��
		PasteModelBtn pmb=new PasteModelBtn();
		addPrivateButton(pmb.getButtonVO());
		
		//ϵͳģ��
		SysModelBtn smb=new SysModelBtn();
		addPrivateButton(smb.getButtonVO());
		
		//2011-7-13
		CreSetBtn csb=new CreSetBtn();
		addPrivateButton(csb.getButtonVOCredence());
		
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
		return new DataChangeClientUICheckRuleGetter();
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