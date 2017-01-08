package nc.ui.dip.statemanage;

import java.sql.SQLException;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.exception.DbException;
import nc.ui.bd.ref.AbstractRefModel;
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
import nc.vo.dip.statemanage.DipStateManageHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
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
public class StateManageUI extends AbstractStateManageUI {
	private String YWLX="";
//	public String getYWLX(){
//		return YWLX;
//	}
	private String lob=null;

	public String delstr =" ";//ɾ��ɾ������, cl 2011-07-04
	public StateManageUI(){
		super();
		SJUtil.setAllButtonsEnalbe(this.getButtons());
		getSplitPane().setDividerLocation(200);
		getButtonManager().getButton(IBtnDefine.DataProce).setName("ִ��");
		try {
			this.updateButtonUI();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/** �ֶ����� */
	private static final long serialVersionUID = 5692169789554885827L;
	public  int FF=0;//�ж������Ӱ�ť���Ǳ�İ�ť����������Ӱ�ť��FF=1��
	public String selectnode = "";//ѡ�����Ľڵ�

	//2011-3-22 12:42 chengli �����������ұ�ʱ��� begin
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
		//2011-7-20 
		UIRefPane pane=(UIRefPane) getBillCardPanel().getHeadItem("firsttab").getComponent();
		AbstractRefModel model=pane.getRefModel();
		pane.setMultiSelectedEnabled(true);//����Ϊtrueʱ����ѡ
		pane.setRefModel(model);
	}

	/**
	 * 2011-3-22 14:45 chengli
	 * �޸Ĵ˷�����ʼ��ģ��ؼ�����
	 */
	@SuppressWarnings("unused")
	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	}


	//2011-3-22 12:42 chengli �����������ұ�ʱ��� begin_2
	@Override
	protected IVOTreeData createTableTreeData() {
		return null;
	}

	@Override
	protected IVOTreeData createTreeData() {
		return new nc.ui.dip.statemanage.SampleTreeCardData();
	}
	//end_2

	public void afterInit() throws Exception {

		super.afterInit();
		YWLX=SJUtil.getYWnameByLX(IContrastUtil.YWLX_JG);
		//�����ڵ㸳����
		this.modifyRootNodeShowName(YWLX);

		//ҳ�����ʱ,��"ɾ��"��ť����Ϊ������
		//getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
		//ҳ�����ʱ,��"�޸�"��ť����Ϊ������
		getButtonManager().getButton(IBillButton.Edit).setEnabled(false);

		updateButtonUI();
	}
	@Override
	protected void insertNodeToTree(CircularlyAccessibleValueObject arg0) throws Exception {
		super.insertNodeToTree(arg0);
	}

	@Override
	public boolean afterTreeSelected(VOTreeNode node) {
//		selectnode = node.getNodeID().toString();
//
//		System.out.println("node:"+selectnode);
		return super.afterTreeSelected(node);
	}
	
	IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	//2011-7-20 ��ѡ ԭʼ���� ��ֵ
	public void getFirstData(String tab){
		VOTreeNode node=getBillTreeSelectNode();
		if(node !=null){
			if(node.getParentnodeID() !=null && node.getNodeID() !=null){
				UIRefPane pane=(UIRefPane)getBillCardPanel().getHeadItem("firsttab").getComponent();
				String[] arrtab=tab.split(",");
				String asql="";
				String aname=null;
				for(int i=0;i<arrtab.length;i++){
					asql="select sysname from dip_datadefinit_h where memorytable='"+arrtab[i]+"' and nvl(dr,0)=0";
					if(i==0){
						try {
							aname=iq.queryfield(asql);
						} catch (BusinessException e) {
							e.printStackTrace();
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (DbException e) {
							e.printStackTrace();
						}
					}else{
						try {
							aname=aname+","+iq.queryfield(asql);
						} catch (BusinessException e) {
							e.printStackTrace();
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (DbException e) {
							e.printStackTrace();
						}
					}
				}
				pane.setValue(aname);		
			}		
		}
	}
	@Override
	protected void onTreeSelectSetButtonState(TableTreeNode snode) {
		DipStateManageHVO hvo=null;
		if ("root".equals(snode.getNodeID().toString().trim())){
			selectnode = "";
		}else{
			VOTreeNode treeNode=(VOTreeNode)snode;
			 hvo=(DipStateManageHVO) treeNode.getData();
		}
		super.onTreeSelectSetButtonState(snode);
		//getButtonManager().getButton(201).setEnabled(false);
		this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(false);
        
		try{
			
			String str = (String)snode.getParentnodeID();
//			snode.getd
			/*��ѯNCϵͳ�µĽڵ㣬�����������Ӳ��� */
//			IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
//			String sql="select h.pk_datachange_h from dip_datachange_h h where h.code='0000' and h.pk_datachange_h='"+str+"' and nvl(h.dr,0)=0";
//			String isNC=iq.queryfield(sql);
			if(str!=null&&str.length()>0&&snode!=snode.getRoot()){
//				&&hvo!=null&&!(hvo.isfolder==null?false:hvo.isfolder.booleanValue())
				if(hvo!=null&(hvo.isfolder==null?false:hvo.isfolder.booleanValue())){
					this.getButtonManager().getButton(IBillButton.Add).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(true);
					this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(false);
//					this.getButtonManager().getButton(IBtnDefine.DataValidate).setEnabled(false);
//					this.getButtonManager().getButton(IBtnDefine.DataProcessCheck).setEnabled(false);
//					this.getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(false);
//					this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
					this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
				}else{
					this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.moveFolderBtn).setEnabled(true);
					this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
					this.getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(true);
//					this.getButtonManager().getButton(IBtnDefine.DataValidate).setEnabled(true);
//					this.getButtonManager().getButton(IBtnDefine.DataProcessCheck).setEnabled(true);
//					this.getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(true);
//					this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(true);
				}
				
				//				this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
//				this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);

				updateButtonUI();
			}else if(snode==snode.getRoot()){//���ڵ�
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Refresh).setEnabled(true);
				updateButtonUI();
			}else{
				this.getButtonManager().getButton(IBillButton.Add).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
				this.getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
//				this.getButtonManager().getButton(IBtnDefine.DataValidate).setEnabled(false);
//				this.getButtonManager().getButton(IBtnDefine.DataProcessCheck).setEnabled(false);
//				this.getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(false);
////				this.getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(false);
//				this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
				this.getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
				if(snode.getParentnodeID()==null){
					this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
					this.getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
				}else{
					this.getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(true);
					this.getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(true);
				}
				updateButtonUI();
			}
		}catch (Exception e) {
		}

		//2011-6-28 cl ԭʼ��������ʾֵ����
		/*if(selectnode !=null &&!"".equals(selectnode) && selectnode.length()>0){
			try {
				DipDataproceHVO hvo=(DipDataproceHVO) HYPubBO_Client.queryByPrimaryKey(DipDataproceHVO.class, selectnode);
				if(hvo !=null){
					String firsttab=hvo.getFirsttab();
					if(firsttab !=null && !"".equals(firsttab)&& firsttab.length()>0){
						DipDatadefinitHVO ddhvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, firsttab);
						String memorytable=ddhvo.getMemorytable();
						this.getBillCardPanel().getHeadItem("firstdata").setValue(memorytable);
					}
				}
			} catch (UifException e) {
				e.printStackTrace();
			}
		}*/
		((MyEventHandler)this.getCardEventHandler()).rewrit();
	}

	/**
	 * @author cl
	 * @date 2011-6-22
	 * @param tab
	 * @param proceTab
	 * @param isAddProceTab
	 * @param proceType
	 * @throws Exception
	 */
//	private void setProceBtnIsEnable(String tab, String proceTab, UFBoolean isAddProceTab, String proceType) throws Exception {
//		if(tab!=null && !"".equals(tab)&& tab.length()>0){
//			boolean isExist=isTableExist(tab);	
//			if(isExist==false){
//				this.getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(false);
//				this.updateButtons();
//			}else{
//				if("���ݻ���".equals(proceType) || "����ת��".equals(proceType)){		
//					//�½��ӹ���Ϊnull����N
//					if(isAddProceTab==null || !isAddProceTab.booleanValue()){
//						this.getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(false);
//						this.getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
//						updateButtonUI();
//					}else{
//						//�жϼӹ����Ƿ��Ѿ������ݿ��д���
//						boolean isProceTab=isTableExist(proceTab);
//						if(isProceTab){
//							getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
//							getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(true);
//							updateButtonUI();
//						}else{
//							getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(true);
//							getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(false);
//							updateButtonUI();
//						}
//					}
//				}else /*if("������ϴ".equals(proceType) ||"����ж��".equals(proceType) || "����Ԥ��".equals(proceType))*/{
//					getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
//					updateButtonUI();
//				}
//			}
//		}		
//	}

	/**
	 * @author cl
	 * @date 2011-6-22
	 * @param tab
	 */
	IUAPQueryBS queryBS=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//	private boolean isTableExist(String tab) {
//		boolean isExist=false;//Ĭ��û�д˱�
//		String sql="select 1 from user_tables where table_name='"+tab.toUpperCase()+"';";
//		try {
//			ArrayList arr=(ArrayList)queryBS.executeQuery(sql,new MapListProcessor());
//
//			if(arr.size()>=1){
//				isExist=true;
//			}
//		} catch (BusinessException e) {
//			e.printStackTrace();
//		}
//		return isExist;
//	}


	@Override
	public void afterEdit(BillEditEvent e) {

		super.afterEdit(e);
		String extcode=MyEventHandler.extcode;
//		if("isadd".equals(e.getKey())){
//			String isadd=getBillCardPanel().getHeadItem("isadd").getValue();
//			if(isadd.equals("false")||isadd.equals("N")||"".equals(isadd)){
//				this.getBillCardWrapper().getBillCardPanel().getHeadItem("procetab").clearViewData();
//				this.getBillCardWrapper().getBillCardPanel().getHeadItem("procetab").setEnabled(false);
//				int rows=this.getBillCardPanel().getBillTable().getRowCount();
//				for(int i=0;i<rows;i++){
////					this.getBillCardWrapper().getBillCardPanel().getBodyPanel().enable(false);
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "cname", false);//��������
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "ename", false);//Ӣ������
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "type", false);//����
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "length", false);//����
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "deciplace", false);//С��λ��
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "ispk", false);//�Ƿ�����
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "isonlyconst", false);//ΨһԼ��
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "isquote", false);//����
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "isimport", false);//�Ƿ����
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "quotetable", false);//���ñ�
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "quotecolu", false);//������
//
//				}
//			}else{
//				this.getBillCardWrapper().getBillCardPanel().getHeadItem("procetab").setEnabled(true);
//				int rows=this.getBillCardPanel().getBillTable().getRowCount();
//				for(int i=0;i<rows;i++){
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "cname", true);//��������
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "ename", true);//Ӣ������
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "type", true);//����
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "length", true);//����
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "deciplace", true);//С��λ��
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "ispk", true);//�Ƿ�����
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "isonlyconst", true);//ΨһԼ��
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "isquote", true);//����
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "isimport",true);//�Ƿ����
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "quotetable",true);//���ñ�
//					this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "quotecolu",true);//������
//				}
//				/*�޸ļӹ�����Ϊ��dip_dd_+ϵͳע����ⲿϵͳ���� ��ͷ
//				 * ͨ���ӹ�voȡ��ϵͳע������fpk����fpk�ҵ���Ӧ��ϵͳע��vo�ҵ��ⲿϵͳ�����ֵ���ӵ�dip_dd_����
//				 * 2011-06-24   
//				 * zlc  begin*/
//				try {
//					DipStateManageHVO prhvo=(DipStateManageHVO)this.getVOFromUI().getParentVO();
//					String fpk=prhvo.getFpk();
//					DipSysregisterHVO syshvo=(DipSysregisterHVO)HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, fpk);
//					String syscode=syshvo.getCode();
//					String  procetab=(String) this.getBillCardWrapper().getBillCardPanel().getHeadItem("procetab").getValueObject();
//					if(!syscode.equals("0000")){
//						getBillCardPanel().getHeadItem("procetab").setValue("DIP_DD_"+extcode.toUpperCase()+"_");
//					}
//				} catch (Exception e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//
//		}

//		if("def_str_2".equals(e.getKey())){
//			getBillCardPanel().setHeadItem("refprocecond", "�����������±�");
//			getBillCardPanel().setHeadItem("procecond", "");
//			if("������ϴ".equals(e.getValue())||"����Ԥ��".equals(e.getValue())||"����ж��".equals(e.getValue())){
//				//2011-6-22 cl
//				getBillCardPanel().getHeadItem("isadd").setValue(false);
//				getBillCardPanel().getHeadItem("isadd").setEnabled(false);
//
//				getBillCardPanel().getHeadItem("procetab").setValue(null);
//				getBillCardPanel().getHeadItem("procetab").setEnabled(false);
//				if("����ж��".equals(e.getValue())){
//					getBillCardPanel().getHeadItem("refprocecond").setValue("�����������±�");
//					getBillCardPanel().getHeadItem("procecond").setValue("DIP_BAK_TS");
//				}
//
//			}else{
//				getBillCardPanel().getHeadItem("isadd").setEnabled(false);
//				getBillCardPanel().getHeadItem("isadd").setValue(true);
//				getBillCardPanel().getHeadItem("procetab").setEnabled(true);
//				if(!extcode.equals("NCXT")){                                       //�ж�ncϵͳ�´洢����û���ƣ�����ϵͳ���� dip_dd_+�ⲿϵͳ����+_ ����  2011-06-25 zlc
//					getBillCardPanel().getHeadItem("procetab").setValue("DIP_DD_"+extcode.toUpperCase()+"_");
//				}
//			}
////			if(e.getValue().equals("���ݻ���")){
////				//2011-6-23 cl ��������ݻ��ܣ��½��ӹ����ɱ༭
////				getBillCardPanel().getHeadItem("isadd").setEnabled(false);
////				getBillCardPanel().getHeadItem("isadd").setValue(true);
////				getBillCardPanel().getHeadItem("procetab").setEnabled(true);
////				if(!extcode.equals("NCXT"))                                          //�ж�ncϵͳ�´洢����û���ƣ�����ϵͳ���� dip_dd_+�ⲿϵͳ����+_ ����  2011-06-25 zlc
////					getBillCardPanel().getHeadItem("procetab").setValue("DIP_DD_"+extcode.toUpperCase()+"_");
////			}
//		}
		//2011-06-09 liyunzhe end
		/*if("firsttab".equals(e.getKey())){
			//liyunzhe 2011-06-04 start
			String tablename=((UIRefPane)getBillCardPanel().getHeadItem("firsttab").getComponent()).getSelectedData().get(0).toString().split(",")[0].substring(1);
			((UIRefPane)getBillCardPanel().getHeadItem("firstdata").getComponent()).setText(tablename);
			//this.getBillCardPanel().getHeadItem("firstdata").setValue(tablename);
			if(FF==1){//��������Ӱ�ť
				Object pk=getBillCardPanel().getHeadItem("firsttab").getValueObject();
				DipDatadefinitBVO[] dipdatavo=null;
				if(!SJUtil.isNull(pk)){

					try {
						String sql="pk_datadefinit_h='"+pk.toString()+"'";
						dipdatavo	=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, sql);

					} catch (UifException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					this.getBillCardWrapper().getBillCardPanel().getBillModel().clearBodyData();
					this.getBillCardWrapper().getBillCardPanel().getBillModel().setBodyDataVO(dipdatavo);
				}
			}
//			liyunzhe 2011-06-04 end
		}*/

		/*2011-5-19 ���� ��char��varchar��number������û�����볤��ʱ����Ĭ�ϳ��� begin */
//		if("type".equals(e.getKey())){
//			int row=e.getRow();
//			Object o=this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row, "type");
//			if(o!=null){
//				String type=o.toString();
//
//				if(type.toLowerCase().contains("char")){
//					if(this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row,"length")==null||
//							(this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row,"length")).equals("") ){
//						this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(100, row, "length");
//					}
//				}else if(type.equals("Number".toUpperCase())){
//					if(this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row,"length")==null||
//							(this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row,"length")).equals("")){
//						this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(20, row, "length");
//						this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(8, row,"deciplace");
//					}
//
//				}
//			}
//		}

//		if("procetab".equals(e.getKey())&&"����ת��".equals(e.getValue())){
//			UIRefPane ref3=(UIRefPane)this.getBillCardPanel().getHeadItem("procetab").getComponent();
//			String newtable=null;
//			if(ref3!=null){
//				newtable=ref3.getText();
//			}
//			if(newtable!=null){
//				UIRefPane ref=(UIRefPane)this.getBillCardPanel().getHeadItem("refprocecond").getComponent();
//				if(ref!=null){
//					UIRefPane ref2=(UIRefPane)this.getBillCardPanel().getHeadItem("firstdata").getComponent();
//					String where=ref.getText();
//					String sql="insert into "+ newtable+" select * from "+ref2.getText()+" "+where.split("where")[1];
//					ref.setText(sql);
//				}
//
//			}
//		}
		/* end */
		/*��Ӣ������ת��Ϊ��д 2011-06-24*/
//		if("ename".equals(e.getKey())){
//			int row=e.getRow();
//			Object o= this.getBillCardPanel().getBillModel("dip_dataproce_b").getValueAt(row, "ename");
//			String str=null;
//			if(o!=null&&o.toString().length()>0){
//				str=o.toString().toUpperCase();
//				this.getBillCardPanel().getBillModel("dip_dataproce_b").setValueAt(str, row,"ename");
//			}		
//		}

		//2011-6-24 cl ���ñ�
//		if("def_quotetable".equals(e.getKey())){			
//			//ִ����ʾ��ʽ
//			int row=e.getRow();
//			String pk_datadefinit_h= this.getBillCardPanel().getBodyItem("def_quotetable").getValueObject()==null?"":this.getBillCardPanel().getBodyItem("def_quotetable").getValueObject().toString();
//
//			if(!"".equals(pk_datadefinit_h)){
//				this.getBillCardPanel().setBodyValueAt(new UFBoolean(true), row, "isquote");
//			}else{
//				this.getBillCardPanel().setBodyValueAt(new UFBoolean(false), row, "isquote");
//			}
//			this.getBillCardPanel().execBodyFormulas(row, getBillCardPanel().getBodyItem("def_quotetable").getEditFormulas());//.execBodyFormula(row, "def_quotetable");
//
//		}

		//2011-7-20
		if("firsttab".equals(e.getKey())){
			UIRefPane pane=(UIRefPane) getBillCardPanel().getHeadItem("firsttab").getComponent();
			String[] pk=pane.getRefPKs();
			String firstdata=(String) e.getValue();
			if(pk !=null && pk.length>0){
				for(int i=0;i<pk.length;i++){
					/**
					 * ���sql����ѯ
					 * ��һ����������ѯ�ı������ڶ���������Ҫ��ʾ���ֶ�����������������where����
					 */
					if(i==0){
						try {
							firstdata=(String) HYPubBO_Client.findColValue("dip_datadefinit_h", "memorytable", "pk_datadefinit_h='"+pk[i]+"' and nvl(dr,0)=0");
						} catch (UifException e1) {
							e1.printStackTrace();
						}
					}else{
						try {
							firstdata=firstdata+","+HYPubBO_Client.findColValue("dip_datadefinit_h", "memorytable", "pk_datadefinit_h='"+pk[i]+"' and nvl(dr,0)=0");
						} catch (UifException e1) {
							e1.printStackTrace();
						}
					}
				}
				getBillCardPanel().setHeadItem("firstdata", firstdata);
			}else{
				//���ⲻ���κβ�������꽹��ӡ�ԭʼ���ݡ��ֶ��ƿ�ʱ�����ֶ�ֵΪ������
				String tab=this.getBillCardPanel().getHeadItem("firstdata").getValueObject()==null?"":this.getBillCardPanel().getHeadItem("firstdata").getValueObject().toString();
				if(tab !=null && !"".equals(tab)){
					getFirstData(tab);
				}
			}
		}

		//ִ�б༭��ʽ
		this.getBillCardPanel().execHeadEditFormulas();
	}


	@Override
	public boolean beforeEdit(BillEditEvent e){
		return true;
	}
	public String getLob() {
		return lob;
	}
	public void setLob(String lob) {
		this.lob = lob;
	}
	
	@Override
	protected UITree getBillTree() {
		// TODO Auto-generated method stub
		return super.getBillTree();
	}
	
}
