package nc.ui.dip.authorize.browse;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.datadefinit.WaTreeRenderer;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.VOTreeNode;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.FunctionUtil;
import nc.util.dip.sj.RetMessage;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.actionset.ActionSetHVO;
import nc.vo.dip.authorize.define.DipADContdataVO;
import nc.vo.pub.CircularlyAccessibleValueObject;


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
public class DataLookClientUI extends AbstractDataLookClientUI{
	
	private IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	
	private Integer count = 3000;
	
	public HashMap<String, HashMap<String, String>> refEditMap = new HashMap<String, HashMap<String, String>>();
	
	StringBuffer vdef = new StringBuffer();
	
	StringBuffer contFields = new StringBuffer();
	
	private Integer importCount = 3000;
	
	public Integer getImportCount() {
		return importCount;
	}

	public void setImportCount(Integer importCount) {
		this.importCount = importCount;
	}


	public Integer getCount() {
		return count;
	}


	public void setCount(Integer count) {
		this.count = count;
	}

	public DataLookClientUI(){
		super();
		getBillCardPanel().getBillTable().setColumnSelectionAllowed(false);
		SJUtil.setAllButtonsEnalbe(this.getButtons());
//		getButtonManager().getButton(IBillButton.Add).setVisible(false);
//		getButtonManager().getButton(IBillButton.Edit).setVisible(false);
//		String pk_corp=ClientEnvironment.getInstance().getCorporation().getPk_corp();
//		if(pk_corp!=null&&pk_corp.equals("0001")){
//			getButtonManager().getButton(IBtnDefine.SET).setVisible(true);
//		}else{
//			getButtonManager().getButton(IBtnDefine.SET).setVisible(false);
//		}
		
		getButtonManager().getButton(IBtnDefine.DATACLEAR).setVisible(false);
		getSplitPane().setDividerLocation(200);
		UITree t=this.getBillTree();
		WaTreeRenderer renderer=new WaTreeRenderer();
		t.setCellRenderer(renderer);
		getBillCardPanel().setTatolRowShow(true);
		getBillCardPanel().getBodyPanel().getPmBody().removeAll();
		
		String sql="select ss.sysvalue from dip_runsys_b ss  where ss.Syscode='DIP-0000015' and nvl(dr,0)=0 ";
		String importsql="select ss.sysvalue from dip_runsys_b ss  where ss.Syscode='DIP-0000016' and nvl(dr,0)=0 ";
		try {
			String value=iq.queryfield(sql);
			if(null != value && !"".equals(value)){
				setCount(Integer.valueOf(value));
			}
			String importvalue=iq.queryfield(importsql);
			if(null != importvalue && !"".equals(importvalue)){
				setImportCount(Integer.valueOf(importvalue));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/** 字段描述 */
	private static final long serialVersionUID = -2887298593239771766L;
	public String selectnode = "";//选择树的节点

	String statusHiddleItemKey = "";//界面显示设置审批状态字段key
	String pkHiddleItemKey = "";//界面显示设置隐藏字段key
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected ICardController createController() {
		// TODO Auto-generated method stub
		return new DataLookClientUICtrl();
	}

	/*public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
	throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
	throws Exception {	}*/

	protected void initSelfData() {	}

	public void setDefaultData() throws Exception {
	}


	protected CardEventHandler createEventHandler()
	{
		return new MyEventHandler(this, getUIControl());
	}

	@Override
	protected IVOTreeData createTableTreeData() {
		return null;
	}

	@Override
	protected IVOTreeData createTreeData() {
		return new SampleTreeCardData();
	}
	@Override
	protected void insertNodeToTree(CircularlyAccessibleValueObject arg0) throws Exception {
		super.insertNodeToTree(arg0);
	}
	public void afterInit() throws Exception {

		super.afterInit();
		// 给根节点赋名称
		this.modifyRootNodeShowName("数据浏览");
	}
	@Override
	public boolean afterTreeSelected(VOTreeNode node) {
		selectnode = node.getNodeID().toString();

		return super.afterTreeSelected(node);
	}


//	@Override
//	protected void onTreeSelectSetButtonState(nc.ui.trade.pub.TableTreeNode snode) {

//	if ("root".equals(snode.getNodeID().toString().trim())){
//	selectnode = "";
//	}
//	super.onTreeSelectSetButtonState(snode);
//	try{
//	String str = (String)snode.getParentnodeID();
//	if(str!=null&&str.length()>0){
//	this.getButtonManager().getButton(IBtnDefine.DATALOOKQUERY).setEnabled(true);
//	this.getButtonManager().getButton(IBtnDefine.DATACLEAR).setEnabled(true);
//	this.getButtonManager().getButton(IBtnDefine.ADD).setEnabled(false);
//	updateButtonUI();

//	}else if(snode==snode.getRoot()){
//	this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//	updateButtonUI();

//	}else{
//	this.getButtonManager().getButton(IBtnDefine.DATALOOKQUERY).setEnabled(false);
//	this.getButtonManager().getButton(IBtnDefine.DATACLEAR).setEnabled(false);
//	this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
//	this.getButtonManager().getButton(IBtnDefine.ADD).setEnabled(true);
//	this.getButtonManager().getButton(IBtnDefine.DELETE).setEnabled(false);

//	updateButtonUI();

//	}
//	} catch (Exception e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//	}
//	}

	@Override
	public void onTreeSelectSetButtonState(nc.ui.trade.pub.TableTreeNode snode) {
		if("root".equals(snode.getNodeID().toString().trim())){
			selectnode="";
		}
		try {
			super.onTreeSelectSetButtonState(snode);
			String str=(String) snode.getParentnodeID();
//			this.getButtonManager().getButton(IBtnDefine.ADD).setEnabled(false);
			DipADContdataVO dvo=(DipADContdataVO)((VOTreeNode)snode).getData();
			
			if(str!=null&&str.length()>0){
				if(dvo!=null&&dvo.getIsfolder()!=null&&dvo.getIsfolder().booleanValue()){
					this.getButtonManager().getButton(IBtnDefine.DATALOOKQUERY).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.DATACLEAR).setEnabled(false);
					this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//					this.getButtonManager().getButton(IBtnDefine.SET).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.DELETE).setEnabled(false);
					this.getButtonManager().getButton(IBillButton.ImportBill).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.ACTION_SET).setEnabled(false);
					this.getButtonManager().getButton(IBillButton.ExportBill).setEnabled(false);
					this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
				}else{
					setButtonVisible(dvo);
					this.getButtonManager().getButton(IBtnDefine.DATALOOKQUERY).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.DATACLEAR).setEnabled(true);
					this.getButtonManager().getButton(IBillButton.Add).setEnabled(true);
//					this.getButtonManager().getButton(IBtnDefine.SET).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.DELETE).setEnabled(true);
					this.getButtonManager().getButton(IBillButton.ImportBill).setEnabled(true);
					this.getButtonManager().getButton(IBillButton.ExportBill).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.ACTION_SET).setEnabled(true);
					
				}
				
//				updateButtonUI();

			}else if(snode==snode.getRoot()){
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.DATALOOKQUERY).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.DATACLEAR).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.DELETE).setEnabled(false);
//				this.getButtonManager().getButton(IBtnDefine.SET).setEnabled(false);
//				updateButtonUI();
			}else{
				this.getButtonManager().getButton(IBtnDefine.DATALOOKQUERY).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.DATACLEAR).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.DELETE).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.ImportBill).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.ExportBill).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.ACTION_SET).setEnabled(false);
//				this.getButtonManager().getButton(IBtnDefine.SET).setEnabled(false);
			}
			updateButtonUI();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void setButtonVisible(DipADContdataVO dvo) throws UifException {
		String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
		if(!"0001".equals(pk_corp)){
			getButtonManager().getButton(IBillButton.Add).setVisible(false);
			getButtonManager().getButton(IBtnDefine.LCLineadd).setVisible(false);
			getButtonManager().getButton(IBtnDefine.DELETE).setVisible(false);
			getButtonManager().getButton(IBtnDefine.LCLinedel).setVisible(false);
			getButtonManager().getButton(IBillButton.Edit).setVisible(false);
			getButtonManager().getButton(IBillButton.ExportBill).setVisible(false);
			getButtonManager().getButton(IBillButton.ImportBill).setVisible(false);
			getButtonManager().getButton(IBtnDefine.DATALOOKQUERY).setVisible(false);
			getButtonManager().getButton(IBillButton.Save).setVisible(false);
			getButtonManager().getButton(IBtnDefine.ACTION_SET).setVisible(false);
			ActionSetHVO[] groupVOs = (ActionSetHVO[])HYPubBO_Client.queryByCondition(ActionSetHVO.class, "pk_role_group in("
						+"SELECT pk_role_group FROM dip_rolegroup_b WHERE pk_role in("
						+"SELECT pk_role FROM sm_user_role WHERE cuserid='"
						+userid
						+"') and coalesce(dr,0)=0) and pk_contdata_h='"
						+dvo.getPrimaryKey()
						+"' and coalesce(dr,0)=0");
			if(null != groupVOs && groupVOs.length>0){
				Boolean is_add = false;
				Boolean is_addline = false;
				Boolean is_del = false;
				Boolean is_delline = false;
				Boolean is_edit = false;
				Boolean is_export = false;
				Boolean is_import = false;
				Boolean is_query = false;
				Boolean is_save = false;
				Boolean is_extend = false;
				for (ActionSetHVO vo : groupVOs) {
					if(!is_add && null != vo.getIs_add() && vo.getIs_add().booleanValue()){
						is_add=true;
					}
					if(!is_addline && null != vo.getIs_addline() && vo.getIs_addline().booleanValue()){
						is_addline=true;
					}
					if(!is_del && null != vo.getIs_del() && vo.getIs_del().booleanValue()){
						is_del=true;
					}
					if(!is_delline && null != vo.getIs_delline() && vo.getIs_delline().booleanValue()){
						is_delline=true;
					}
					if(!is_edit && null != vo.getIs_edit() && vo.getIs_edit().booleanValue()){
						is_edit=true;
					}
					if(!is_export && null != vo.getIs_export() && vo.getIs_export().booleanValue()){
						is_export=true;
					}
					if(!is_import && null != vo.getIs_import() && vo.getIs_import().booleanValue()){
						is_import=true;
					}
					if(!is_query && null != vo.getIs_query() && vo.getIs_query().booleanValue()){
						is_query=true;
					}
					if(!is_save && null != vo.getIs_save() && vo.getIs_save().booleanValue()){
						is_save=true;
					}
					if(!is_extend && null != vo.getIs_extend() && vo.getIs_extend().booleanValue()){
						is_extend=true;
					}
				}
				if(is_add){
					getButtonManager().getButton(IBillButton.Add).setVisible(true);
				}
				if(is_addline){
					getButtonManager().getButton(IBtnDefine.LCLineadd).setVisible(true);
				}
				if(is_del){
					getButtonManager().getButton(IBtnDefine.DELETE).setVisible(true);
				}
				if(is_delline){
					getButtonManager().getButton(IBtnDefine.LCLinedel).setVisible(true);
				}
				if(is_edit){
					getButtonManager().getButton(IBillButton.Edit).setVisible(true);
				}
				if(is_export){
					getButtonManager().getButton(IBillButton.ExportBill).setVisible(true);
				}
				if(is_import){
					getButtonManager().getButton(IBillButton.ImportBill).setVisible(true);
				}
				if(is_query){
					getButtonManager().getButton(IBtnDefine.DATALOOKQUERY).setVisible(true);
				}
				if(is_save){
					getButtonManager().getButton(IBillButton.Save).setVisible(true);
				}
				if(is_extend){
					getButtonManager().getButton(IBtnDefine.ACTION_SET).setVisible(true);
				}
			}
		}
	}


	@Override
	public void afterEdit(BillEditEvent arg0) {
		// TODO Auto-generated method stub
		super.afterEdit(arg0);
		String key = arg0.getKey();
		String vdefs [] = vdef.toString().split(",");
		String contField [] = contFields.toString().split(",");
		int n = 0;
		for (int i = 0; i < vdefs.length; i++) {
			if(key.equals(vdefs[i])){
				n = i;
				break;
			}
		}
		String string = contField[n];
		if(null != refEditMap.get(string)){
			HashMap<String, String> map = refEditMap.get(string);
			Set<String> keySet = map.keySet();
			Iterator<String> iterator = keySet.iterator();
			String saveRepKey = "";
			String function = "";
			while(iterator.hasNext()){
				saveRepKey = iterator.next();
				function = map.get(saveRepKey);
			}
			int indexOf = function.indexOf("(");
			String substring = function.substring(indexOf+1, function.length()-1);
			String[] split = substring.split(",");
			for (int i = 0; i < contField.length; i++) {
				if(saveRepKey.equals(contField[i])){
					n = i;
					break;
				}
			}
			saveRepKey = vdefs[n];
			DipADContdataVO vo1=(DipADContdataVO) getBillTreeSelectNode().getData();
			//得到树上需要导出的表的主键
			String pkdetadefinith = vo1.getContabcode();
			String upperCase = split[0].toUpperCase();
			if(null != arg0.getValue() && !"".equals(arg0.getValue())){
				RetMessage ret=FunctionUtil.getSaveReplaceFuctionValue(upperCase,arg0.getValue().toString(), 
						split[1],pkdetadefinith);
				getBillCardPanel().getBillModel().setValueAt(ret.getValue(), arg0.getRow(), saveRepKey);
			}else{
				getBillCardPanel().getBillModel().setValueAt(null, arg0.getRow(), saveRepKey);
			}
		}
		int k=getBillCardPanel().getBillTable().getRowCount();
		int m=getBillCardPanel().getBillTable().getSelectedRow();
		Object value = getBillCardPanel().getBodyValueAt(arg0.getRow(), pkHiddleItemKey);
		if(null == value || "".equals(value)){
			if(m==k-1){
				getBillCardPanel().getBillModel().addLine();
			}
		}
	}
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		Object value = getBillCardPanel().getBodyValueAt(e.getRow(), statusHiddleItemKey);
		if(null != value && !"0".equals(value)){//审批状态<>0的不允许编辑
			return false;
		}
		return super.beforeEdit(e);
	}
	
}

