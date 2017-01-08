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
public class ContDataClientUI extends AbstractContdataClientUI{
	public ContDataClientUI(){
		super();
		SJUtil.setAllButtonsEnalbe(this.getButtons());
//		getSplitPane().setDividerLocation(200);
		
		//add by zhw 2012-05-28 表头初始化自动对照字段
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
	/** 字段描述 */
	private static final long serialVersionUID = 5692169789554885827L;
	public String selectnode = "";//选择树的节点
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
		// 给根节点赋名称
		this.modifyRootNodeShowName("数据对照定义");
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
//		liyunzhe 删除增加，删除，文件夹管理按钮 2012-06-15
		String str = (String) snode.getParentnodeID();
//		this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
//		this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
//		this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
//		this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
//		this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(false);
		
		if(str!=null && str.length()>0){//不是系统节点
			DipContdataVO hvo=(DipContdataVO) ((VOTreeNode)snode).getData();
			this.getButtonManager().getButton(IBillButton.Refresh).setEnabled(true);
			if(hvo.getIsfolder()==null||!hvo.getIsfolder().booleanValue()){//是节点
//				this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(true);
//				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
//				this.getButtonManager().getButton(IBillButton.Delete).setEnabled(true);
			}else{//是文件夹
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
			
			//add by zhw 2012-05-28 节点切换后处理自动对照字段
			if(null != hvo && null != hvo.getDef_str_2() && !"".equals(hvo.getDef_str_2())){
				BillItem headItem = getBillCardPanel().getHeadItem("def_str_2");
				if(null != headItem){
					UIRefPane component2 = (UIRefPane)headItem.getComponent();
					component2.setText(hvo.getDef_str_2());
				}
			}
			//add by zhw 2012-05-28 ---------------------end
			
		}else if(snode==snode.getRoot()){//根节点
//			liyunzhe 删除增加，删除，文件夹管理按钮 2012-06-15
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
		}else{//系统节点
//			liyunzhe 删除增加，删除，文件夹管理按钮 2012-06-15
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
	/*增加对照系统物理字段和对照系统显示字段参照的筛选
	 * 2011-5-11 5-14
	 * 张龙超*/
	@Override
	public void afterEdit(BillEditEvent arg0) {


		super.afterEdit(arg0);
		//对照系统参照联动
		if("contabcode".equals(arg0.getKey())){
			Object pk=getBillCardPanel().getHeadItem("contabcode").getValueObject();
			if(!SJUtil.isNull(pk)){
				//表头，对照系统物理字段参照
				UIRefPane pa=(UIRefPane) this.getBillCardPanel().getHeadItem("contcolcode").getComponent();
				DataDefinitbRefModel model=new DataDefinitbRefModel();
				model.addWherePart(" and dip_datadefinit_h.pk_datadefinit_h='"+pk.toString()+"' and (dip_datadefinit_h.isfolder is null or  dip_datadefinit_h.isfolder='N')  and dip_datadefinit_h.tabsoucetype='自定义' and (dip_datadefinit_b.ispk = 'Y' or dip_datadefinit_b.isonlyconst='Y') and nvl(dip_datadefinit_b.dr, 0) = 0 ");
				pa.setRefModel(model);
				//表体,
//				UIRefPane pa1=(UIRefPane) this.getBillCardPanel().getHeadItem("def_str_2").getComponent();
//				DataDefinitbRefModel model1=new DataDefinitbRefModel();
//				model1.addWherePart(" and dip_datadefinit_h.pk_datadefinit_h='"+pk.toString()+"' and (dip_datadefinit_h.isfolder is null or  dip_datadefinit_h.isfolder='N')  and dip_datadefinit_h.tabsoucetype='自定义' and nvl(dip_datadefinit_b.dr, 0) = 0 ");
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
				//2011-6-3 被对照系统物理表 extetabcode
				UIRefPane pane2=(UIRefPane)this.getBillCardPanel().getHeadItem("extetabcode").getComponent();
				DataDefinitRefModel model2=new DataDefinitRefModel();
				//2011-7-21 增加了 and dip_datadefinit_h.iscreatetab='Y'
				model2.addWherePart(" and dip_datadefinit_h.iscreatetab='Y' and (dip_datadefinit_h.isfolder is null or  dip_datadefinit_h.isfolder='N')  and pk_sysregister_h ='"+extesys.toString()+"' and nvl(dr,0)=0");
				pane2.setRefModel(model2);
			}
			getBillCardPanel().setHeadItem("extetabcode", null);
			getBillCardPanel().setHeadItem("extetabname", null);
			getBillCardPanel().setHeadItem("extecolcode", null);
			getBillCardPanel().setHeadItem("extecolname", null);
			getBillCardPanel().setHeadItem("def_str_3", null);
		}

		//被对照系统参照联动
		else if(arg0.getKey().equals("extetabcode")){
			Object expk=getBillCardPanel().getHeadItem("extetabcode").getValueObject();
			if(!SJUtil.isNull(expk)){				
				//表头，对照系统物理字段参照
				UIRefPane pa=(UIRefPane) this.getBillCardPanel().getHeadItem("extecolcode").getComponent();
				DataDefinitbRefModel model=new DataDefinitbRefModel();
				model.addWherePart(" and dip_datadefinit_h.pk_datadefinit_h='"+expk.toString()+"' and (dip_datadefinit_h.isfolder is null or  dip_datadefinit_h.isfolder='N')  and dip_datadefinit_h.tabsoucetype='自定义' and (dip_datadefinit_b.ispk = 'Y' or dip_datadefinit_b.isonlyconst='Y') and nvl(dip_datadefinit_b.dr, 0) = 0 ");
				pa.setRefModel(model);
				
				UIRefPane pa1=(UIRefPane) this.getBillCardPanel().getHeadItem("def_str_3").getComponent();
				DataDefinitbRefModel model1=new DataDefinitbRefModel();
				model1.addWherePart(" and dip_datadefinit_h.pk_datadefinit_h='"+expk.toString()+"' and (dip_datadefinit_h.isfolder is null or  dip_datadefinit_h.isfolder='N')  and dip_datadefinit_h.tabsoucetype='自定义' and nvl(dip_datadefinit_b.dr, 0) = 0 ");
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
		
		//add by zhw 2012-05-29 选择对照字段后清空自动对照字段
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
