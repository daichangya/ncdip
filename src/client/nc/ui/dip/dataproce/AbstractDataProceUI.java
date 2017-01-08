package nc.ui.dip.dataproce;


import nc.ui.dip.buttons.ControlBtn;
import nc.ui.dip.buttons.CreateTableBtn;
import nc.ui.dip.buttons.DataProceBtn;
import nc.ui.dip.buttons.DataProcessCheckBtn;
import nc.ui.dip.buttons.DataValidateBtn;
import nc.ui.dip.buttons.MoveddownBtn;
import nc.ui.dip.buttons.MovedupBtn;
import nc.ui.dip.buttons.YuJingBtn;
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
  public abstract class AbstractDataProceUI extends BillTreeCardUI implements ILinkQuery{

	  protected ICardController createController() {
			return new DataProceUICtrl();
		}
	
	/**
	 * ������ݲ���ƽ̨ʱ��UI����Ҫ���ش˷��������ز���ƽ̨��ҵ������� 
	 * @return BusinessDelegator ����ƽ̨��ҵ�������
	 */
	protected BusinessDelegator createBusinessDelegator() {
		return new nc.ui.dip.dataproce.MyDelegator();
	}

	/**
	 * ע���Զ��尴ť
	 */
	protected void initPrivateButton() {
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
		//���ư�ť
		MovedupBtn mub=new MovedupBtn();
		addPrivateButton(mub.getButtonVO());
		//���ư�ť
		MoveddownBtn mdb=new MoveddownBtn();
		addPrivateButton(mdb.getButtonVO());
	//�Զ���ӹ���ť
		DataProceBtn dpb=new DataProceBtn();
		addPrivateButton(dpb.getButtonVO());
		//����У��
		DataValidateBtn dvb=new DataValidateBtn();
		addPrivateButton(dvb.getButtonVO());
		//Ԥ����ť
		YuJingBtn yjb=new YuJingBtn();
		addPrivateButton(yjb.getButtonVO());
		//������
		CreateTableBtn ctn=new CreateTableBtn();
		addPrivateButton(ctn.getButtonVO());
		
		//���� 2011-7-20
		ControlBtn cb=new ControlBtn();
		addPrivateButton(cb.getButtonVO());
		//У����
		addPrivateButton(new DataProcessCheckBtn().getButtonVO());
		
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
		return new DataProceUICheckRuleGetter();
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
