package nc.ui.dip.autocontdata;

import java.awt.event.ActionListener;

import nc.ui.bd.ref.DataDefinitRefModel;
import nc.ui.bd.ref.DataDefinitbRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.tyzhq.tygs.CredenceListener;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.contdata.DipContdataVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.field.BillField;


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
public class ContDataClientUI extends AbstractContdataClientUI{
	public ContDataClientUI(){
		super();
		SJUtil.setAllButtonsEnalbe(this.getButtons());
//		getSplitPane().setDividerLocation(200);
		
		//add by zhw 2012-05-28 ��ͷ��ʼ���Զ������ֶ�
		String[] bitem=new String[]{"def_str_2"};
		for(int i=0;i<bitem.length;i++){
			if(getBillCardPanel().getHeadItem(bitem[i])!=null){
				UIRefPane rf=(UIRefPane) getBillCardPanel().getHeadItem(bitem[i]).getComponent();
				CredenceListener listener = new CredenceListener(this,bitem[i],CredenceListener.TYPE_HEAD,rf,1);
				if(rf!=null){
					rf.getUIButton().removeActionListener(rf.getUIButton().getListeners(ActionListener.class)[0]);
					rf.getUIButton().addActionListener(listener);
					rf.setAutoCheck(false);
					rf.setEditable(false);
					rf.setIsCustomDefined(true);
					rf.setRefNodeName("");
					getBillCardPanel().getHeadItem(bitem[i]).setEdit(false);
				}
			}
		}
		//add by zhw 2012-05-28 ---------------------------end
		
	}
	/** �ֶ����� */
	private static final long serialVersionUID = 5692169789554885827L;
	public String selectnode = "";//ѡ�����Ľڵ�
	protected CardEventHandler  createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
	public String getRefBillType() {
		return null;
	}
	/** end */
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
	throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
	throws Exception {	}

	protected void initSelfData() {	
	}

	@SuppressWarnings("unused")
	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();

	}

	@Override
	protected IVOTreeData createTableTreeData() {
		return null;
	}

	@Override
	protected IVOTreeData createTreeData() {
		return new nc.ui.dip.contdata.SampleTreeCardData();
	}

	public void afterInit() throws Exception {

		super.afterInit();
		// �����ڵ㸳����
		this.modifyRootNodeShowName("���ݶ��ն���");
		getButtonManager().getButton(IBillButton.Edit).setEnabled(false);

		updateButtonUI();
	}

	@Override
	protected void insertNodeToTree(CircularlyAccessibleValueObject arg0) throws Exception {
		super.insertNodeToTree(arg0);
	}

	@Override
	public boolean afterTreeSelected(VOTreeNode node) {
		if(node.getNodeID()!=null){
			selectnode = node.getNodeID().toString();

			System.out.println("node:"+selectnode);
	
		}
				return super.afterTreeSelected(node);
	}

	@Override
	protected void onTreeSelectSetButtonState(TableTreeNode snode) {

		if ("root".equals(snode.getNodeID().toString().trim())){
			selectnode = "";
		}
		super.onTreeSelectSetButtonState(snode);
		getBillCardPanel().execHeadLoadFormulas();
//		liyunzhe ɾ�����ӣ�ɾ�����ļ��й���ť 2012-06-15
		String str = (String) snode.getParentnodeID();
//		this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
//		this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
//		this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
//		this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
//		this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(false);
		
		if(str!=null && str.length()>0){//����ϵͳ�ڵ�
			DipContdataVO hvo=(DipContdataVO) ((VOTreeNode)snode).getData();
			this.getButtonManager().getButton(IBillButton.Refresh).setEnabled(true);
			if(hvo.getIsfolder()==null||!hvo.getIsfolder().booleanValue()){//�ǽڵ�
//				this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(true);
//				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
//				this.getButtonManager().getButton(IBillButton.Delete).setEnabled(true);
			}else{//���ļ���
//				this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
//				this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
//				this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(true);
//				this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(true);
//				this.getButtonManager().getButton(IBillButton.Add).setEnabled(true);
				this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
//				this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.YuJing ).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.autoContData).setEnabled(false);
			}
			try {
				updateButtonUI();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//add by zhw 2012-05-28 �ڵ��л������Զ������ֶ�
			if(null != hvo && null != hvo.getDef_str_2() && !"".equals(hvo.getDef_str_2())){
				BillItem headItem = getBillCardPanel().getHeadItem("def_str_2");
				if(null != headItem){
					UIRefPane component2 = (UIRefPane)headItem.getComponent();
					component2.setText(hvo.getDef_str_2());
				}
			}
			//add by zhw 2012-05-28 ---------------------end
			
		}else if(snode==snode.getRoot()){//���ڵ�
//			liyunzhe ɾ�����ӣ�ɾ�����ļ��й���ť 2012-06-15
//			this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
			this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
//			this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
			this.getButtonManager().getButton(IBillButton.Refresh).setEnabled(true);
//			this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
			try {
				updateButtonUI();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{//ϵͳ�ڵ�
//			liyunzhe ɾ�����ӣ�ɾ�����ļ��й���ť 2012-06-15
//			this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
			this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
//			this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
			this.getButtonManager().getButton(IBillButton.Refresh).setEnabled(true);
//			this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
//			this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(true);
//			this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
//			this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
			this.getButtonManager().getButton(IBtnDefine.YuJing ).setEnabled(false);
			this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
			this.getButtonManager().getButton(IBtnDefine.autoContData).setEnabled(false);
			
			try {
				updateButtonUI();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//getButtonManager().getButton(201).setEnabled(false);

	}
	/*���Ӷ���ϵͳ�����ֶκͶ���ϵͳ��ʾ�ֶβ��յ�ɸѡ
	 * 2011-5-11 5-14
	 * ������*/
	@Override
	public void afterEdit(BillEditEvent arg0) {


		super.afterEdit(arg0);
		//����ϵͳ��������
		if("contabcode".equals(arg0.getKey())){
			Object pk=getBillCardPanel().getHeadItem("contabcode").getValueObject();
			if(!SJUtil.isNull(pk)){
				//��ͷ������ϵͳ�����ֶβ���
				UIRefPane pa=(UIRefPane) this.getBillCardPanel().getHeadItem("contcolcode").getComponent();
				DataDefinitbRefModel model=new DataDefinitbRefModel();
				model.addWherePart(" and dip_datadefinit_h.pk_datadefinit_h='"+pk.toString()+"' and (dip_datadefinit_h.isfolder is null or  dip_datadefinit_h.isfolder='N')  and dip_datadefinit_h.tabsoucetype='�Զ���' and (dip_datadefinit_b.ispk = 'Y' or dip_datadefinit_b.isonlyconst='Y') and nvl(dip_datadefinit_b.dr, 0) = 0 ");
				pa.setRefModel(model);
				//����,
//				UIRefPane pa1=(UIRefPane) this.getBillCardPanel().getHeadItem("def_str_2").getComponent();
//				DataDefinitbRefModel model1=new DataDefinitbRefModel();
//				model1.addWherePart(" and dip_datadefinit_h.pk_datadefinit_h='"+pk.toString()+"' and (dip_datadefinit_h.isfolder is null or  dip_datadefinit_h.isfolder='N')  and dip_datadefinit_h.tabsoucetype='�Զ���' and nvl(dip_datadefinit_b.dr, 0) = 0 ");
//				pa1.setRefModel(model1);
			}

			getBillCardPanel().setHeadItem("contcolcode", null);
			getBillCardPanel().setHeadItem("contcolname", null);
			getBillCardPanel().setHeadItem("def_str_2", null);
		}

		//2011-6-3
		if(arg0.getKey().equals("extesys")){
			Object extesys=getBillCardPanel().getHeadItem("extesys").getValueObject();
			if(!SJUtil.isNull(extesys)){
				//2011-6-3 ������ϵͳ����� extetabcode
				UIRefPane pane2=(UIRefPane)this.getBillCardPanel().getHeadItem("extetabcode").getComponent();
				DataDefinitRefModel model2=new DataDefinitRefModel();
				//2011-7-21 ������ and dip_datadefinit_h.iscreatetab='Y'
				model2.addWherePart(" and dip_datadefinit_h.iscreatetab='Y' and (dip_datadefinit_h.isfolder is null or  dip_datadefinit_h.isfolder='N')  and pk_sysregister_h ='"+extesys.toString()+"' and nvl(dr,0)=0");
				pane2.setRefModel(model2);
			}
			getBillCardPanel().setHeadItem("extetabcode", null);
			getBillCardPanel().setHeadItem("extetabname", null);
			getBillCardPanel().setHeadItem("extecolcode", null);
			getBillCardPanel().setHeadItem("extecolname", null);
			getBillCardPanel().setHeadItem("def_str_3", null);
		}

		//������ϵͳ��������
		else if(arg0.getKey().equals("extetabcode")){
			Object expk=getBillCardPanel().getHeadItem("extetabcode").getValueObject();
			if(!SJUtil.isNull(expk)){				
				//��ͷ������ϵͳ�����ֶβ���
				UIRefPane pa=(UIRefPane) this.getBillCardPanel().getHeadItem("extecolcode").getComponent();
				DataDefinitbRefModel model=new DataDefinitbRefModel();
				model.addWherePart(" and dip_datadefinit_h.pk_datadefinit_h='"+expk.toString()+"' and (dip_datadefinit_h.isfolder is null or  dip_datadefinit_h.isfolder='N')  and dip_datadefinit_h.tabsoucetype='�Զ���' and (dip_datadefinit_b.ispk = 'Y' or dip_datadefinit_b.isonlyconst='Y') and nvl(dip_datadefinit_b.dr, 0) = 0 ");
				pa.setRefModel(model);
				
				UIRefPane pa1=(UIRefPane) this.getBillCardPanel().getHeadItem("def_str_3").getComponent();
				DataDefinitbRefModel model1=new DataDefinitbRefModel();
				model1.addWherePart(" and dip_datadefinit_h.pk_datadefinit_h='"+expk.toString()+"' and (dip_datadefinit_h.isfolder is null or  dip_datadefinit_h.isfolder='N')  and dip_datadefinit_h.tabsoucetype='�Զ���' and nvl(dip_datadefinit_b.dr, 0) = 0 ");
				pa1.setRefModel(model1);
			}

			getBillCardPanel().setHeadItem("extecolcode", null);
			getBillCardPanel().setHeadItem("extecolname", null);
			getBillCardPanel().setHeadItem("def_str_3", null);
		}

		else if(arg0.getKey().equals("def_str_1")){
			String def=(String) getBillCardPanel().getHeadItem("def_str_1").getValueObject();
			UFBoolean ob=arg0.getValue()==null?new UFBoolean(false):new UFBoolean(def);
			if(ob.booleanValue()){
				getBillCardPanel().getHeadItem("def_str_2").setEdit(true);
				getBillCardPanel().getHeadItem("def_str_3").setEdit(true);
			}else{
				getBillCardPanel().getHeadItem("def_str_2").setEdit(false);
				getBillCardPanel().getHeadItem("def_str_3").setEdit(false);
				getBillCardPanel().setHeadItem("def_str_2", null);
				getBillCardPanel().setHeadItem("def_str_3", null);
			}
		}
		
		//add by zhw 2012-05-29 ѡ������ֶκ�����Զ������ֶ�
		else if("contcolcode".equals(arg0.getKey())){
			String def=(String) getBillCardPanel().getHeadItem("def_str_1").getValueObject();
			UFBoolean ob=arg0.getValue()==null?new UFBoolean(false):new UFBoolean(def);
			if(ob.booleanValue()){
				getBillCardPanel().getHeadItem("def_str_2").setValue(null);
			}
		}
		//add by zhw 2012-05-29 ------------------------end
		
		
		this.getBillCardPanel().execHeadLoadFormulas();
		this.getBillCardPanel().execHeadEditFormulas();

	}

	@Override
	protected UITree getBillTree() {
		// TODO Auto-generated method stub
		return super.getBillTree();
	}


}
