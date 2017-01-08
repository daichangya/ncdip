package nc.ui.dip.authorize.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.vo.dip.authorize.role.MyBillVO;
import nc.vo.dip.authorize.role.RoleGroupBVO;
import nc.vo.dip.authorize.role.RoleGroupVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.IExAggVO;
import nc.vo.trade.summarize.Hashlize;
import nc.vo.trade.summarize.VOHashPrimaryKeyAdapter;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillTreeCardUI billUI, ICardController control) {
		super(billUI, control);
	}

	/**
	 * 取得当前UI类
	 * 
	 * @return
	 */
	private RoleGroupUI getSelfUI() {
		return (RoleGroupUI) getBillUI();
	}

	/**
	 * 取得选中的节点对象
	 * 
	 * @return
	 */
	private VOTreeNode getSelectNode() {
		return getSelfUI().getBillTreeSelectNode();
	}

	@Override
	protected void onBoEdit() throws Exception {
		VOTreeNode treeNode = getSelectNode();
		if (treeNode == null) {
			getSelfUI().showErrorMessage("请选择要修改的节点！");
			return;
		}
		super.onBoEdit();
		onBoLineAdd();
	}

	@Override
	protected void onBoCancel() throws Exception {
		VOTreeNode node = getSelectNode();
		super.onBoCancel();
		if (node != null) {
			getSelfUI().onTreeSelectSetButtonState(node);
		}
	}

	private String valuedate() {
	    StringBuffer msg = new StringBuffer();
		BillModel bodyBillModel = getBillCardPanelWrapper().getBillCardPanel().getBillModel();
		int rowcount=bodyBillModel.getRowCount();
        if(rowcount>0){
        	Map<String, String> codemap = new HashMap<String, String>();
        	for(int i=0 ; i< rowcount;i ++ ){
        		int row = i+1;
        		RoleGroupBVO bodyValueRowVO = (RoleGroupBVO)bodyBillModel.getBodyValueRowVO(i, RoleGroupBVO.class.getName());
        		if(bodyValueRowVO.getPk_role()==null){	
        			msg.append("第"+row+"行 ,【角色】 不能为空!\n");
        		}else{
        			if(null!=codemap && null!=codemap.get(bodyValueRowVO.getPk_role())){
            			msg.append("第"+row+"行, 【角色】 出现重复! \n");
        			}else{
        				codemap.put(bodyValueRowVO.getPk_role(), "code");
        			}
        		}
        	}
        }	
        if(null!=msg && msg.toString().length()>0){
        	return "保存失败，校验不通过！\n"+msg.toString();
        }
		return null;
	}
	@Override
	protected void onBoSave() throws Exception {
		int rowcount = getBillCardPanelWrapper().getBillCardPanel()
				.getRowCount();
		if (rowcount > 0) {
			List list = new ArrayList();
			BillModel bodyBillModel = getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel();
			BillItem[] bodyItems = bodyBillModel.getBodyItems();
			for (int i = 0; i < rowcount; i++) {
				Boolean isNull = true;
				for (BillItem billItem : bodyItems) {
					if (billItem.isShow()) {
						Object valueAt = bodyBillModel.getValueAt(i,
								billItem.getKey());
						if (null != valueAt && !"".equals(valueAt)) {
							isNull = false;
							break;
						}
					}
				}
				if (isNull) {
					list.add(i);
				}
			}
			if (list.size() != 0) {
				int[] delrow = new int[list.size()];
				for (int i = 0; i < list.size(); i++) {
					delrow[i] = (Integer) list.get(i);
				}
				bodyBillModel.delLine(delrow);
			}
		}

		BillData bd = getBillCardPanelWrapper().getBillCardPanel()
				.getBillData();
		if (bd != null) {
			bd.dataNotNullValidate();
		}
		String message = valuedate();
		if(null !=message ){
			getBillUI().showWarningMessage(message);
			return;
		}
		AggregatedValueObject billVO = getBillUI().getChangedVOFromUI();
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);
		// 进行数据晴空

		boolean isSave = true;
		billVO.setParentVO(checkVO.getParentVO());

		// 判断是否有存盘数据
		if (billVO.getParentVO() == null
				&& (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0)) {
			isSave = false;
		} else {
			if (getBillUI().isSaveAndCommitTogether())
				billVO = getBusinessAction().saveAndCommit(billVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), checkVO);
			else

				// write to database
				billVO = getBusinessAction().save(billVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), checkVO);
		}
		int nCurrentRow = -1;
		if (isSave) {
			if (isEditing()) {
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(billVO);
					nCurrentRow = 0;

				} else {
					getBufferData().setCurrentVO(billVO);
					nCurrentRow = getBufferData().getCurrentRow();
				}
			} else {
				getBufferData().addVOsToBuffer(
						new AggregatedValueObject[] { billVO });
				nCurrentRow = getBufferData().getVOBufferSize() - 1;
			}
		}

		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRowWithOutTriggerEvent(nCurrentRow);
		}
		
		setAddNewOperate(isAdding(), billVO);

		// 设置保存后状态
		setSaveOperateState();
		if (getUITreeCardController().isAutoManageTree()) {			
			getSelfUI().insertNodeToTree(billVO.getParentVO());
		}
		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRow(nCurrentRow);
		}
		if (getSelectNode() != null) {
			getSelfUI().onTreeSelectSetButtonState(getSelectNode());
		}
	}

	@Override
	protected void onBoDelete() throws Exception {
		VOTreeNode tempNode = getSelectNode();
		if (tempNode == null) {
			getSelfUI().showErrorMessage("请选择要删除的节点！");
			return;
		}
		Integer flag = MessageDialog.showOkCancelDlg(this.getBillUI(), "提示",
				"是否确认要删除?");
		if (flag == 1) {
			String pk = (String) tempNode.getNodeID();
			HYPubBO_Client.delete(HYPubBO_Client.queryByPrimaryKey(
					RoleGroupVO.class, pk));
			// 更新树
			this.getBillTreeCardUI()
					.getBillTreeData()
					.deleteNodeFromTree(
							this.getBillTreeCardUI().getBillTreeSelectNode());
		}
	}

	@Override
	public void onTreeSelected(VOTreeNode selectnode) {
		super.onTreeSelected(selectnode);
		SuperVO vo = (SuperVO) selectnode.getData();
		// 获取VO
		try {
			MyBillVO billvo = new MyBillVO();
			billvo.setParentVO(vo);
			// 显示数据
			getBufferData().addVOToBuffer(billvo);
			getBillTreeCardUI().getTreeToBuffer().put(selectnode.getNodeID(),
					"" + (getBufferData().getVOBufferSize() - 1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		super.onBoAdd(bo);
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("pk_corp", ClientEnvironment.getInstance().getCorporation().getPk_corp());
		onBoLineAdd();
	}

	@Override
	protected void setTSFormBufferToVO(AggregatedValueObject setVo)
			throws Exception {
		if (setVo == null)
			return;
		AggregatedValueObject vo = getBufferData().getCurrentVO();
		if (vo == null)
			return;
		if (getBillUI().getBillOperate() == 0) {
			if (vo.getParentVO() != null && setVo.getParentVO() != null)
				setVo.getParentVO().setAttributeValue("ts",
						vo.getParentVO().getAttributeValue("ts"));
			SuperVO changedvos[] = (SuperVO[]) (SuperVO[]) getChildVO(setVo);
			if (changedvos != null && changedvos.length != 0) {
				HashMap bufferedVOMap = null;
				SuperVO bufferedVOs[] = (SuperVO[]) (SuperVO[]) getChildVO(vo);
				if (bufferedVOs != null && bufferedVOs.length != 0) {
					bufferedVOMap = Hashlize.hashlizeObjects(bufferedVOs,
							new VOHashPrimaryKeyAdapter());
					for (int i = 0; i < changedvos.length; i++) {
						if (changedvos[i].getPrimaryKey() == null)
							continue;
						ArrayList bufferedAl = (ArrayList) bufferedVOMap
								.get(changedvos[i].getPrimaryKey());
						if (bufferedAl != null) {
							SuperVO bufferedVO = (SuperVO) bufferedAl.get(0);
							changedvos[i].setAttributeValue("ts",
									bufferedVO.getAttributeValue("ts"));
						}
					}

				}
			}
		}
	}

	private CircularlyAccessibleValueObject[] getChildVO(
			AggregatedValueObject retVo) {
		CircularlyAccessibleValueObject childVos[] = null;
		if (retVo instanceof IExAggVO)
			childVos = ((IExAggVO) retVo).getAllChildrenVO();
		else
			childVos = retVo.getChildrenVO();
		return childVos;
	}

	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
		getSelfUI().updateButtonUI();
	}

	@Override
	protected void onBoLineDel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoLineDel();
	}
	
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {
		case IBtnDefine.RELATION_SET:
			onBoRelationSet();
			break;
		default:
			break;
		}
		super.onBoElse(intBtn);
	}
	
	private void onBoRelationSet() {
		BillItem headItem = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_role_group");
		RelationSetDailog setDailog = new RelationSetDailog((RoleGroupUI)getBillUI(), headItem.getValue());
		int showModal = setDailog.showModal();
	}
}