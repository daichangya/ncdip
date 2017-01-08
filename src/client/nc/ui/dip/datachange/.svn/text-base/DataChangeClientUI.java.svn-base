package nc.ui.dip.datachange;

import java.sql.SQLException;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.jdbc.framework.exception.DbException;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.DataDefinitbRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.CardEventHandler;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.datachange.DipDatachangeBVO;
import nc.vo.dip.datachange.DipDatachangeHVO;
import nc.vo.pub.BusinessException;
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
public class DataChangeClientUI extends AbstractDataChangeClientUI{
	String ywlx;
	/*public String getYwlx(){
		return ywlx;
	}*/
	public String delstr =" ";//删行时删除条件, cl 2011-07-04
	public DataChangeClientUI(){
		super();
		SJUtil.setAllButtonsEnalbe(this.getButtons());
		getSplitPane().setDividerLocation(200);
	}
	/** 字段描述 */
	private static final long serialVersionUID = 5692169789554885827L;

	public String selectnode = "";//选择树的节点

	//2011-4-11 10:42 chengli 在制作左树右表时添加 begin
	protected CardEventHandler createEventHandler() {		
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
		UIRefPane uiRefPane=(UIRefPane) getBillCardPanel().getBodyItem("nczz").getComponent();
		AbstractRefModel model=uiRefPane.getRefModel();
		uiRefPane.setMultiSelectedEnabled(true);//设置为TRUE时 多选，FALSE 单选
		uiRefPane.setRefModel(model);
	}

	/**
	 * 2011-3-22 14:45 chengli
	 * 修改此方法初始化模板控件数据
	 */
	@SuppressWarnings("unused")
	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	}

//	2011-4-11 9:42 chengli 在制作左树右表时添加 begin_2
	@Override
	protected IVOTreeData createTableTreeData() {
		return null;
	}

	@Override
	protected IVOTreeData createTreeData() {
		return new nc.ui.dip.datachange.SampleTreeCardData();
	}
	//end_2




	public void afterInit() throws Exception {

		super.afterInit();


ywlx=SJUtil.getYWnameByLX(IContrastUtil.YWLX_ZH);
		//给根节点赋名称
		this.modifyRootNodeShowName(ywlx);

		//页面加载时,将"删除"按钮设置为不可用
		//getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
		//页面加载时,将"修改"按钮设置为不可用
		getButtonManager().getButton(IBillButton.Edit).setEnabled(false);

		updateButtonUI();
	}
	@Override
	protected void insertNodeToTree(CircularlyAccessibleValueObject arg0) throws Exception {
		super.insertNodeToTree(arg0);
	}

	//2011-6-28
	IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	@Override
	public boolean afterTreeSelected(VOTreeNode node) {
		selectnode = node.getNodeID().toString();

		System.out.println("node:"+selectnode);


		return super.afterTreeSelected(node);
	}

	@Override
	protected void onTreeSelectSetButtonState(TableTreeNode snode) {

		IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		if ("root".equals(snode.getNodeID().toString().trim())){
			selectnode = "";
		}
		super.onTreeSelectSetButtonState(snode);
		//getButtonManager().getButton(201).setEnabled(false);

		this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
		this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(false);
		try{
			String str = (String)snode.getParentnodeID();
			/*查询NC系统下的节点，不允许做修改操作 */
			String sql="select h.pk_datachange_h from dip_datachange_h h where h.code='0000' and h.pk_datachange_h='"+str+"' and nvl(h.dr,0)=0";
			String isNC=iq.queryfield(sql);
			if(str!=null&&str.length()>0){
				DipDatachangeHVO vo=(DipDatachangeHVO) ((VOTreeNode)snode).getData();
				
				if(vo!=null&&vo.getIsfolder()!=null&&vo.getIsfolder().booleanValue()){
					this.getButtonManager().getButton(IBillButton.Add).setEnabled(true);
					this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.Conversion).setEnabled(false);
					this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.DataValidate).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.MBZH).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.CRESET).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
					
				}else{
					this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
					this.getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.Conversion).setEnabled(true);
					this.getButtonManager().getButton(IBillButton.Delete).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.DataValidate).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.MBZH).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.CRESET).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(true);

				}
				
				
				updateButtonUI();
			}else if(isNC !=null && !"".equals(isNC.trim())){/*查询NC系统下的节点，不允许做增加、修改操作 */
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				updateButtonUI();
			}else if(snode==snode.getRoot()){
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
				updateButtonUI();
			}else{
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.Conversion).setEnabled(false);
				//this.getButtonManager().getButton(IBillButton.Copy).setEnabled(false);
				//this.getButtonManager().getButton(IBtnDefine.Model).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.DataValidate).setEnabled(false);
				//this.getButtonManager().getButton(IBtnDefine.Start).setEnabled(false);
				//this.getButtonManager().getButton(IBtnDefine.Stop).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(false);
			//	this.getButtonManager().getButton(IBtnDefine.HBSET).setEnabled(false);
				//this.getButtonManager().getButton(IBtnDefine.CREDENCE).setEnabled(false);
				//this.getButtonManager().getButton(IBtnDefine.PASTEMODEL).setEnabled(false);
				//this.getButtonManager().getButton(IBtnDefine.SYSMODEL).setEnabled(false);
			//	this.getButtonManager().getButton(IBtnDefine.AddEffectFactor).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.MBZH).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.CRESET).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
				updateButtonUI();
			}
		}catch (Exception e) {
		}

		//2011-6-28 选择左边树节点，nc组织编码显示问题
		if(selectnode !=null && !"".equals(selectnode) && selectnode.length()>0){
			try {
				DipDatachangeBVO[] bvo=(DipDatachangeBVO[]) HYPubBO_Client.queryByCondition(DipDatachangeBVO.class, " pk_datachange_h='"+selectnode+"' and isnull(dr,0)=0");
				String orgName=null;//多个名称连接的字符串
				String[] arrayName=null;//名称数组
				String sql="";
				String unitcode=null;//编码
				String nczz=null;
				if(bvo!=null && bvo.length>0){
					for(int j=0;j<bvo.length;j++){
						orgName=bvo[j].getOrgname();
						arrayName=orgName.split(",");
						for(int k=0;k<arrayName.length;k++){
							if(k==0){
								nczz=arrayName[k];
								sql="select unitcode from bd_corp where unitname='"+nczz+"' and nvl(dr,0)=0";
								try {
									unitcode=iq.queryfield(sql);
								} catch (BusinessException e) {
									e.printStackTrace();
								} catch (SQLException e) {
									e.printStackTrace();
								} catch (DbException e) {
									e.printStackTrace();
								}
							}else{
								nczz=arrayName[k];
								sql="select unitcode from bd_corp where unitname='"+nczz+"' and nvl(dr,0)=0";
								try {
									unitcode=unitcode+","+iq.queryfield(sql);
								} catch (BusinessException e) {
									e.printStackTrace();
								} catch (SQLException e) {
									e.printStackTrace();
								} catch (DbException e) {
									e.printStackTrace();
								}
							}
						}
						this.getBillCardPanel().setBodyValueAt(unitcode, j, "nczz");
					}
				}
			} catch (UifException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		super.afterEdit(e);
		
//		自动增加一行。
		if(!(e.getKey().equals("code")||e.getKey().equals("name")||e.getKey().equals("systype")||e.getKey().equals("busidata")||e.getKey().equals("ncorg")||e.getKey().equals("guding")||e.getKey().equals("org")||e.getKey().equals("pk_glorg")||e.getKey().equals("outpath")||e.getKey().equals("tasktype"))){
			int k=this.getBillCardPanel().getBillModel("dip_datachange_b").getRowCount();
			int m=this.getBillCardPanel().getBillTable("dip_datachange_b").getSelectedRow();
			if(k-1==m){
				this.getBillCardPanel().getBillModel().addLine();
				
			}
		}
		
		
		
		
		
		
		//根据业务数据busidata来判断要参照哪个表
		if("busidata".equals(e.getKey())){
			VOTreeNode node=this.getBillTreeSelectNode();
			String nodeid=(String) node.getNodeID();
			//业务数据对应数据定义的主键
			String pk_datadefinit_h= this.getBillCardPanel().getHeadItem("busidata").getValueObject()==null?"":this.getBillCardPanel().getHeadItem("busidata").getValueObject().toString();
			//父主键
			//String fpk=this.getBillCardPanel().getHeadItem("pk_sysregister_h").getValueObject().toString();
			//组织字段配了参照
			UIRefPane pane=(UIRefPane) getBillCardPanel().getHeadItem("org").getComponent();
			DataDefinitbRefModel model=new DataDefinitbRefModel();
			model.addWherePart("and  dip_datadefinit_b.pk_datadefinit_h='"+pk_datadefinit_h+"'  and nvl(dip_datadefinit_b.dr,0)=0");
			pane.setRefModel(model);
		}

		//nc组织编码 2011-6-28 
		if(e.getKey().equals("nczz")){
			try{
				//String pk_corp=this._getCorp().getPk_corp().trim();//得到当前登录人的公司的pkcorp
				UIRefPane pane=(UIRefPane) getBillCardPanel().getBodyItem("nczz").getComponent();
				String[] pk=pane.getRefPKs();
				e.getValue().toString();
				String unitname=e.getValue()==null?"":e.getValue().toString();//得到公司目录名称
				String[] arrayCorp=unitname.split(",");//分割成一个数组
				String nczz=null;
				for(int i=0;i<arrayCorp.length;i++){
					/* 
					 * 组合SQL语句查询
					 * 第一个参数：要查询的 表名；第二个参数：要显示的 字段名； 第三个参数：where条件
					 * 只选取了一个
					 */
					if(i==0){
						unitname=(String) HYPubBO_Client.findColValue("bd_corp", "unitname", "unitcode='"+arrayCorp[i]+"' and nvl(dr,0)=0");
						nczz=arrayCorp[i];
						getBillCardPanel().setBodyValueAt(nczz, e.getRow(), "nczz");
						getBillCardPanel().setBodyValueAt(unitname, e.getRow(), "orgname");
					}else{
						//选取多个时，以","号隔开
						unitname=unitname+","+HYPubBO_Client.findColValue("bd_corp", "unitname", "unitcode='"+arrayCorp[i]+"' and nvl(dr,0)=0");
						nczz=nczz+","+arrayCorp[i];
						getBillCardPanel().setBodyValueAt(nczz, e.getRow(), "nczz");
					}
				}
				System.out.println(nczz+"    "+unitname);
				getBillCardPanel().setBodyValueAt(unitname, e.getRow(), "orgname");

				String orgcode=null;//bd_corp表主键
				if(pk !=null && pk.length>0){				
					for(int j=0;j<pk.length;j++){
						if(j==0 && j==pk.length-1){
							orgcode=pk[j];
						}else if(j==0 && j !=pk.length){
							orgcode=pk[j]+",";
						}
						else if(j!=0 && j!=pk.length-1){
							//既不是第一行也不是最后一行
							orgcode+=pk[j]+",";
						}else if( j!=0 && j==pk.length-1){
							//最后一行
							orgcode+=pk[j];
						}

					}
					System.out.println("******* "+orgcode);
					getBillCardPanel().setBodyValueAt(orgcode, e.getRow(), "orgcode");
				}
			}catch (Exception e1) {
				e1.printStackTrace();
			}
		}

//		DipDatadefinitHVO hvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, busi);
//		String tbname=hvo.getMemorytable();
		//2011-07-09  若为固定组织，组织字段不可编辑 zlc
		if(e.getKey().equals("guding")){
			String guding=this.getBillCardPanel().getHeadItem("guding").getValueObject()==null?"N":this.getBillCardPanel().getHeadItem("guding").getValueObject().toString();
			if(guding!=null&&guding.length()>0&&new UFBoolean(guding).booleanValue()){
//				UIRefPane pane=(UIRefPane) this.getBillCardPanel().getHeadItem("org").getComponent();
//				pane.setEnabled(false);
				
				this.getBillCardPanel().getHeadItem("org").setEnabled(false);
			}else{
				this.getBillCardPanel().getHeadItem("org").setEnabled(true);
			}
		}
		if(e.getKey().equals("org")){
			String org=this.getBillCardPanel().getHeadItem("org").getValueObject()==null?"":this.getBillCardPanel().getHeadItem("org").getValueObject().toString();
			if(org!=null&& org.length()>0){	
				this.getBillCardPanel().getHeadItem("guding").setValue("N");
				this.getBillCardPanel().getHeadItem("guding").setEnabled(false);
			}else{
				this.getBillCardPanel().getHeadItem("guding").setEnabled(true);
			}
		}
	}
	
	@Override
	protected UITree getBillTree() {
		// TODO Auto-generated method stub
		return super.getBillTree();
	}
}
