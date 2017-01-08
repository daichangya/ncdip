package nc.ui.dip.authorize.role;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillItem;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */
public class RoleGroupUI extends AbstractRoleGroupUI {
	public RoleGroupUI() {
		super();
		getButtonManager().getButton(IBillButton.CopyLine).setVisible(false);
		getButtonManager().getButton(IBillButton.PasteLine).setVisible(false);
		getButtonManager().getButton(IBillButton.PasteLinetoTail).setVisible(
				false);
		getSplitPane().setDividerLocation(200);
	}

	/** 字段描述 */
	private static final long serialVersionUID = 5692169789554885827L;
	public String selectnode = "";// 选择树的节点

	protected CardEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}

	public String getRefBillType() {
		return null;
	}

	/** end */
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void initSelfData() {
		BillItem bodyItem = getBillCardPanel().getBodyItem("role_code");
		if (null != bodyItem) {
			UIRefPane refPane = (UIRefPane) bodyItem.getComponent();
			refPane.setMultiSelectedEnabled(true);
		}
	}

	@SuppressWarnings("unused")
	public void setDefaultData() throws Exception {

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
		this.modifyRootNodeShowName("角色组");
	}

	@Override
	protected void insertNodeToTree(CircularlyAccessibleValueObject arg0)
			throws Exception {
		super.insertNodeToTree(arg0);
	}

	@Override
	public boolean afterTreeSelected(VOTreeNode node) {
		return super.afterTreeSelected(node);
	}

	@Override
	protected void onTreeSelectSetButtonState(TableTreeNode snode) {
		super.onTreeSelectSetButtonState(snode);
		getBillCardPanel().execHeadLoadFormulas();

	}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		if (e.getPos()==IBillItem.BODY && "role_code".equals(e.getKey())) {
			BillModel billModel = getBillCardPanel().getBillModel();
			UIRefPane refPane = (UIRefPane) getBillCardPanel().getBodyItem(
					"role_code").getComponent();
			String[] refPKs = refPane.getRefPKs();
			String[] refCodes = refPane.getRefCodes();
			String[] refNames = refPane.getRefNames();
			if (null != refPKs && refPKs.length > 0) {
				for (int i = 0; i < refPKs.length; i++) {
					if (i == 0) {
						billModel.setValueAt(refPKs[i], e.getRow(), "pk_role");
						billModel.setValueAt(refCodes[i], e.getRow(),
								"role_code");
						billModel.setValueAt(refNames[i], e.getRow(),
								"role_name");
					} else {
						getBillCardPanel().addLine();
						billModel.setValueAt(refPKs[i],
								billModel.getRowCount() - 1, "pk_role");
						billModel.setValueAt(refCodes[i],
								billModel.getRowCount() - 1, "role_code");
						billModel.setValueAt(refNames[i],
								billModel.getRowCount() - 1, "role_name");
					}
				}
			}
			getBillCardPanel().addLine();
		}
		this.getBillCardPanel().execHeadLoadFormulas();
		this.getBillCardPanel().execHeadEditFormulas();
		if ((e.getRow() + 1) == getBillCardPanel().getBillModel()
				.getRowCount()) {
			getBillCardPanel().addLine();
		}
	}

	@Override
	protected UITree getBillTree() {
		// TODO Auto-generated method stub
		return super.getBillTree();
	}
}
