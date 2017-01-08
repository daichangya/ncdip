package nc.ui.dip.authorize.define;

import javax.swing.JComponent;

import nc.ui.bd.ref.DataDefinitbRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.authorize.define.DipADContdataVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
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
public class ContDataClientUI extends AbstractContdataClientUI implements BillCardBeforeEditListener{
	public ContDataClientUI(){
		super();
		getButtonManager().getButton(  IBillButton.PasteLine).setVisible(false);
		getButtonManager().getButton(  IBillButton.CopyLine).setVisible(false);
		getButtonManager().getButton(  IBillButton.PasteLinetoTail).setVisible(false);
		String pk_corp=ClientEnvironment.getInstance().getCorporation().getPk_corp();
		if(pk_corp!=null&&pk_corp.equals("0001")){
			getButtonManager().getButton(IBtnDefine.SET).setVisible(true);
			getButtonManager().getButton(IBtnDefine.ACTION_SET).setVisible(true);
		}else{
			getButtonManager().getButton(IBtnDefine.SET).setVisible(false);
			getButtonManager().getButton(IBtnDefine.ACTION_SET).setVisible(false);
		}
		SJUtil.setAllButtonsEnalbe(this.getButtons());
		getSplitPane().setDividerLocation(200);
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
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
//		String pk_datadefinit_h = (String) getBillCardPanel().getHeadItem("code").getValueObject();
//		UIRefPane ref = (UIRefPane)getBillCardPanel().getHeadItem("extetabname").getComponent();
//		AbstractRefModel model = ref.getRefModel();
//		model.setWherePart(" and pk_datadefinit_h='"+pk_datadefinit_h+"'");
		//setCurrentPanel("CARDPANEL");
	}

	@SuppressWarnings("unused")
	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();

//		getBillCardPanel().getHeadItem(fileDef.getField_Corp()).setValue(pkCorp);
//		getBillCardPanel().setTailItem(fileDef.getField_Operator(), ClientEnvironment.getInstance().getUser().getPrimaryKey());
//		getBillCardPanel().setTailItem("doperatordate", ClientEnvironment.getInstance().getDate());
//		getBillCardPanel().getHeadItem(fileDef.getField_Billtype()).setValue(billtype);
//		getBillCardPanel().getHeadItem(fileDef.getField_BillStatus()).setValue(new Integer(IBillStatus.FREE).toString());
	}

	@Override
	protected IVOTreeData createTableTreeData() {
		return null;
	}

	@Override
	protected IVOTreeData createTreeData() {
		return new SampleTreeCardData();
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
		selectnode = node.getNodeID().toString();

		System.out.println("node:"+selectnode);
		return super.afterTreeSelected(node);
	}

	@Override
	protected void onTreeSelectSetButtonState(TableTreeNode snode) {

		if ("root".equals(snode.getNodeID().toString().trim())){
			selectnode = "";
		}
		//super.setParameters(arg0)
		super.onTreeSelectSetButtonState(snode);
		getBillCardPanel().execHeadLoadFormulas();

		this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
		this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(false);
		String str = (String) snode.getParentnodeID();
		if(str!=null && str.length()>0){
			DipADContdataVO advo=(DipADContdataVO) ((VOTreeNode)snode).getData();
			if(advo!=null&&advo.getIsfolder()!=null&&advo.getIsfolder().booleanValue()){
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(true);
				this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Refresh).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(true);
				this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(true);
				this.getButtonManager().getButton(IBillButton.Refresh).setEnabled(true);
				this.getButtonManager().getButton(IBtnDefine.SET).setEnabled(false);
				getButtonManager().getButton(IBtnDefine.ACTION_SET).setEnabled(false);
			}else{
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
				this.getButtonManager().getButton(IBillButton.Delete).setEnabled(true);
				this.getButtonManager().getButton(IBillButton.Refresh).setEnabled(true);
				this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(true);
				this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.SET).setEnabled(true);
				getButtonManager().getButton(IBtnDefine.ACTION_SET).setEnabled(true);
			}
			
			try {
				updateButtonUI();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(snode==snode.getRoot()){
			this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
			this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
			this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
			this.getButtonManager().getButton(IBillButton.Refresh).setEnabled(true);
			this.getButtonManager().getButton(IBtnDefine.SET).setEnabled(false);
			this.getButtonManager().getButton(IBtnDefine.ACTION_SET).setEnabled(false);
			try {
				updateButtonUI();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
			this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
			this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
			this.getButtonManager().getButton(IBillButton.Refresh).setEnabled(true);
			this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
			this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
			this.getButtonManager().getButton(IBtnDefine.SET).setEnabled(false);
			this.getButtonManager().getButton(IBtnDefine.ACTION_SET).setEnabled(false);
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
				model.addWherePart(" and dip_datadefinit_h.pk_datadefinit_h='"+pk.toString()+"' and (dip_datadefinit_b.isquote = 'Y' ) and nvl(dip_datadefinit_b.dr, 0) = 0 ");
				pa.setRefModel(model);
				//表体,
				/*UIRefPane pa1=(UIRefPane) this.getBillCardPanel().getBodyItem("dip_contdata_b", "field").getComponent();
				DataDefinitbRefModel model1=new DataDefinitbRefModel();
				model1.addWherePart(" and dip_datadefinit_h.pk_datadefinit_h='"+pk.toString()+"'  and nvl(dip_datadefinit_b.dr, 0) = 0 ");
				pa1.setRefModel(model1);*/
			}

			getBillCardPanel().setHeadItem("contcolcode", null);
			getBillCardPanel().setHeadItem("contcolname", null);
		}else if("contcolcode".equals(arg0.getKey())){
			Object pk=getBillCardPanel().getHeadItem("contcolcode").getValueObject();
//			if(pk==null||pk.toString().length()<=0){
//				getBillCardPanel().getBillModel().clearBodyData();
//			}
		}else if("ismaster".equals(arg0.getKey())){
			Object ismaster=getBillCardPanel().getHeadItem("ismaster").getValueObject();
			if("Y".equals(ismaster)){
				getBillCardPanel().getHeadItem("pk_master").setEdit(false);
				getBillCardPanel().getHeadItem("pk_master").setValue(null);
				getBillCardPanel().getHeadItem("ismobile").setEdit(true);
			}else{
				getBillCardPanel().getHeadItem("pk_master").setEdit(true);
				getBillCardPanel().getHeadItem("ismobile").setEdit(false);
				getBillCardPanel().getHeadItem("ismobile").setValue("N");
			}
		}

		//2011-6-3
		/*if(arg0.getKey().equals("extesys")){
			Object extesys=getBillCardPanel().getHeadItem("extesys").getValueObject();
			if(!SJUtil.isNull(extesys)){
				//2011-6-3 被对照系统物理表 extetabcode
				UIRefPane pane2=(UIRefPane)this.getBillCardPanel().getHeadItem("extetabcode").getComponent();
				DataDefinitRefModel model2=new DataDefinitRefModel();
				//2011-7-21 增加了 and dip_datadefinit_h.iscreatetab='Y'
				model2.addWherePart(" and dip_datadefinit_h.iscreatetab='Y' and pk_sysregister_h ='"+extesys.toString()+"' and nvl(dr,0)=0");
				pane2.setRefModel(model2);
			}
			getBillCardPanel().setHeadItem("extetabcode", null);
			getBillCardPanel().setHeadItem("extetabname", null);
			getBillCardPanel().setHeadItem("extecolcode", null);
			getBillCardPanel().setHeadItem("extecolname", null);
		}

		//被对照系统参照联动
		if(arg0.getKey().equals("extetabcode")){
			Object expk=getBillCardPanel().getHeadItem("extetabcode").getValueObject();
			if(!SJUtil.isNull(expk)){				
				//表头，对照系统物理字段参照
				UIRefPane pa=(UIRefPane) this.getBillCardPanel().getHeadItem("extecolcode").getComponent();
				DataDefinitbRefModel model=new DataDefinitbRefModel();
				model.addWherePart(" and dip_datadefinit_h.pk_datadefinit_h='"+expk.toString()+"'  and dip_datadefinit_h.tabsoucetype='自定义' and (dip_datadefinit_b.ispk = 'Y' or dip_datadefinit_b.isonlyconst='Y') and nvl(dip_datadefinit_b.dr, 0) = 0 ");
				pa.setRefModel(model);

				//表体
				UIRefPane pa1=(UIRefPane) this.getBillCardPanel().getBodyItem("dip_contdata_b2", "field").getComponent();
				DataDefinitbRefModel model1=new DataDefinitbRefModel();
				model1.addWherePart(" and dip_datadefinit_h.pk_datadefinit_h='"+expk.toString()+"'  and nvl(dip_datadefinit_b.dr, 0) = 0 ");
				pa1.setRefModel(model1);



			}

			getBillCardPanel().setHeadItem("extecolcode", null);
			getBillCardPanel().setHeadItem("extecolname", null);
		}
*/

		this.getBillCardPanel().execHeadLoadFormulas();
		this.getBillCardPanel().execHeadEditFormulas();
		if((arg0.getRow()+1)==getBillCardPanel().getBillModel().getRowCount()){
			getBillCardPanel().addLine();
		}
	}
	@Override
	protected UITree getBillTree() {
		// TODO Auto-generated method stub
		return super.getBillTree();
	}
	public boolean beforeEdit(BillItemEvent e) {
		// TODO Auto-generated method stub
		if(e.getItem().getKey().equals("pk_master")){
			Object valueObject = getBillCardPanel().getHeadItem("contabcode").getValueObject();
			UIRefPane refPanel = (UIRefPane)getBillCardPanel().getHeadItem("pk_master").getComponent();
			if(null == valueObject || "".equals(valueObject)){
				refPanel.setWhereString(" 1=2 ");
			}else{
				String where = " contabcode='"+valueObject+"'";
				String pk_contdata_h = getBillCardPanel().getHeadItem("pk_contdata_h").getValue();
				if(null != pk_contdata_h && !"".equals(pk_contdata_h)){
					where += " and pk_contdata_h<>'"+pk_contdata_h+"'";
				}
				refPanel.setWhereString(where);
			}
		}
		return false;
	}

  

}
