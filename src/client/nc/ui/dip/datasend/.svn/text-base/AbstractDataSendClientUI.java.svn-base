package nc.ui.dip.datasend;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.dip.buttons.ControlBtn;
import nc.ui.dip.buttons.DataSendBtn;
import nc.ui.dip.buttons.SendFormBtn;
import nc.ui.dip.buttons.YuJingBtn;
import nc.ui.dip.buttons.folder.AddFolderBtn;
import nc.ui.dip.buttons.folder.DeleteFolderBtn;
import nc.ui.dip.buttons.folder.EditFolderBtn;
import nc.ui.dip.buttons.folder.FolderManageBtn;
import nc.ui.dip.buttons.folder.MoveFolderBtn;
import nc.ui.dip.datasend.DataSendClientUICheckRuleGetter;
import nc.ui.pub.linkoperate.*;
import nc.ui.pub.report.treetableex.VOTreeNode;
import nc.vo.trade.button.ButtonVO;
import nc.vo.pub.AggregatedValueObject;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.treecard.BillTreeCardUI;




/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
  public abstract class AbstractDataSendClientUI extends BillTreeCardUI implements  ILinkQuery{
	  protected ICardController createController() {
			return new DataSendClientUICtrl();
		}
		
		/**
		 * 如果单据不走平台时，UI类需要重载此方法，返回不走平台的业务代理类 
		 * @return BusinessDelegator 不走平台的业务代理类
		 */
		protected BusinessDelegator createBusinessDelegator() {
			return new nc.ui.dip.datasend.MyDelegator();
		}

		/**
		 * 注册自定义按钮
		 */
		protected void initPrivateButton() {
//			int[] listButns = getUIControl().getListButtonAry();
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
			//张进双 2011-5-14
			YuJingBtn yjb=new YuJingBtn();
			addPrivateButton(yjb.getButtonVO());
			//ZLC   2011-5-23
			DataSendBtn send=new DataSendBtn();
			addPrivateButton(send.getButtonVO());
			SendFormBtn sendform=new SendFormBtn();
			addPrivateButton(sendform.getButtonVO());

			//控制 2011-7-20
			ControlBtn cb=new ControlBtn();
			addPrivateButton(cb.getButtonVO());
			addPrivateButton(new FolderManageBtn().getButtonVO());
			addPrivateButton(new AddFolderBtn().getButtonVO());
			addPrivateButton(new EditFolderBtn().getButtonVO());
			addPrivateButton(new DeleteFolderBtn().getButtonVO());
			addPrivateButton(new MoveFolderBtn().getButtonVO());
		}

		/**
		 * 注册前台校验类
		 */
		public Object getUserObject() {
			return new DataSendClientUICheckRuleGetter();
		}
		
		public void doQueryAction(ILinkQueryData querydata) {
		        String billId = querydata.getBillID();
		        if (billId != null) {
		            try {
//		            	setCurrentPanel(BillTemplateWrapper.CARDPANEL);
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

		public boolean afterTreeSelected(VOTreeNode node) {
			// TODO Auto-generated method stub
			return false;
		}

		public void onTreeSelectSetButtonState(String selectnode) {
			// TODO Auto-generated method stub
			
		}
		

		
	}
