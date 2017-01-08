package nc.ui.dip.datadefcheck;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.text.Document;

import nc.bs.dip.txt.TxtDataVO;
import nc.bs.excel.pub.ExpExcelVO;
import nc.bs.excel.pub.ExpToExcel;
import nc.bs.excel.pub.ExpToExcel1;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.exception.ComponentException;
import nc.itf.dip.pub.IQueryField;
import nc.itf.uap.ddc.IBizObjStorage;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bd.ref.DataDefFieldbRefModel;
import nc.ui.bd.ref.PhysicTableRefModel;
import nc.ui.bd.ref.PubDatadictTreeRefModel;
import nc.ui.bd.ref.QuoteTableTreeRefModel;
import nc.ui.bd.ref.SysRegisterRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.dlg.AddFolderDlg;
import nc.ui.dip.dlg.movefoleder.MovefolderDLG;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treemanage.BillTreeManageUI;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.datadefcheck.DipDatadefinitBVO;
import nc.vo.dip.datadefcheck.DipDatadefinitCVO;
import nc.vo.dip.datadefcheck.DipDatadefinitHVO;
import nc.vo.dip.datadefcheck.MyBillVO;
import nc.vo.dip.in.RowDataVO;
import nc.vo.dip.sysregister.DipSysregisterHVO;
import nc.vo.dip.util.PhysicTableVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.core.FolderNode;
import nc.vo.pub.core.ObjectNode;
import nc.vo.pub.ddc.datadict.FieldDef;
import nc.vo.pub.ddc.datadict.FieldDefList;
import nc.vo.pub.ddc.datadict.TableDef;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.pub.IExAggVO;
import nc.vo.trade.summarize.Hashlize;
import nc.vo.trade.summarize.VOHashPrimaryKeyAdapter;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 *������AbstractMyEventHandler�������ʵ���࣬
 *��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 *@author author
 *@version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler{


	public MyEventHandler(BillTreeManageUI billUI, ICardController control) {
		super(billUI, control);
	}

	public static String extcode;
	
	Integer selectrow=0;

	/**
	 * ȡ�õ�ǰUI��
	 * 
	 * @return
	 */
	private DataDefcheckClientUI getSelfUI() {
		return (DataDefcheckClientUI) getBillUI();
	}

	/**
	 * ȡ��ѡ�еĽڵ����
	 * 
	 * @return
	 */
	private VOTreeNode getSelectNode() {
		return getSelfUI().getBillTreeSelectNode();
	}

	/**
	 * �÷��������кͲ��к󱻵��ã������ڸ÷�����Ϊ������������һЩĬ��ֵ
	 * 
	 * @param newBodyVO
	 */
	protected CircularlyAccessibleValueObject processNewBodyVO(CircularlyAccessibleValueObject newBodyVO) {

		VOTreeNode parent = this.getSelectNode();

		// ����������д���ӱ���
		newBodyVO.setAttributeValue("pk_datadefinit_h", parent == null ? null : parent.getNodeID());

		// ��ӽ���
		return newBodyVO;
	}

	@SuppressWarnings("unused")
	private CircularlyAccessibleValueObject[] getChildVO(AggregatedValueObject retVo) {
		CircularlyAccessibleValueObject childVos[] = null;
		if (retVo instanceof IExAggVO)
			childVos = ((IExAggVO) retVo).getAllChildrenVO();
		else
			childVos = retVo.getChildrenVO();
		return childVos;
	}


	/**
	 * ��ͷ����������
	 * */
	public void onCheckQuery() throws Exception{
		
//add by qw 2016-11-28
		int headcount = getSelfUI().getBillListPanel().getHeadTable().getSelectedRow();
		//������ϵͳ����
		String headpk = getSelfUI().getBillListPanel().getHeadBillModel().getValueAt(headcount, DipDatadefinitCVO.PK_DATADEFINIT_B)==null?"":getSelfUI().getBillListPanel().getHeadBillModel().getValueAt(headcount, DipDatadefinitCVO.PK_DATADEFINIT_B).toString();

		SuperVO[] vos = HYPubBO_Client.queryByCondition(DipDatadefinitCVO.class, "pk_datadefinit_b='" + headpk + "' and isnull(dr,0)=0  ");

		getSelfUI().getBillListPanel().getBodyBillModel().setBodyDataVO(vos);
		getSelfUI().getBillListPanel().getBodyBillModel().execLoadFormula();
		getSelfUI().onTreeSelectSetButtonState(getSelfUI().getBillTreeSelectNode());

	}

	IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());

	@Override
	protected void onBoDelete() throws Exception {		
//		IUAPQueryBS queryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//
//		Integer flags=0;//ɾ����־��1��������ȷ����ť
//		String pk_node = ((DataDefcheckClientUI) getBillUI()).selectnode;
//		if("".equals(pk_node)){
//			NCOptionPane.showMessageDialog(this.getBillUI(),"��ѡ��Ҫɾ���Ľڵ㡣");
//			return ;
//		}
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ�޸ĵĽڵ㣡");
			return ;
		}
		String str=(String)tempNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("ϵͳ�ڵ㲻�����޸Ĳ�����"); 
			return;
		}
		DipDatadefinitHVO hvo =(DipDatadefinitHVO) tempNode.getData();
		
		if(null!= hvo && !hvo.getMemorytype().equals("��")){//
			getSelfUI().showWarningMessage("ֻ���޸ı�ڵ�"); 
			return;
		}
		
		if(getSelfUI().getBillListPanel().getHeadTable().getSelectedRows().length!=1 ){
			getSelfUI().showWarningMessage("��ѡ��һ����������"); 
			return;
		}

		int rowcount=getSelfUI().getBillListPanel().getBodyBillModel().getRowCount();
		if(rowcount<1){
			getSelfUI().showWarningMessage("�ӱ�û�����ݣ�ɾ��ʧ��"); 
			return;
		}

		int headcount = getSelfUI().getBillListPanel().getHeadTable().getSelectedRow();
		String headpk = getSelfUI().getBillListPanel().getHeadBillModel().getValueAt(headcount, DipDatadefinitCVO.PK_DATADEFINIT_B)==null?"":getSelfUI().getBillListPanel().getHeadBillModel().getValueAt(headcount, DipDatadefinitCVO.PK_DATADEFINIT_B).toString();
		
		int i =getSelfUI().showOkCancelMessage("��ɾ���ӱ��е��������ݣ��Ƿ������");
		if(i==1){//ȷ��
		}else{
			return;	
		}
		String sql=" DELETE FROM dip_datadefinit_c where pk_datadefinit_b = '"+headpk+"' ";
		iq.exesql(sql);
		//������
		onCheckQuery();
		getSelfUI().showWarningMessage("ɾ���ɹ�");

		
	}
	private void delete() throws Exception{
		return ;
	}

	/**
	 * ����:����
	 * ���ܣ�ɾ��ʱ���޸��Ƿ����ñ�־�ķ���
	 * �������Ƿ�ֲ�ʽϵͳ��ϵͳע�������������ֲ�ʽ��ʶ���ڵ�pk
	 * ���ڣ�2011-4-25
	 */
	public void updateIsYY(boolean isdeploy,String pk_sysregister_h,String deploycode,String pk_node){
		//��ѯ���ݶ���������ǰϵͳ�Ƿ��и÷ֲ�ʽ��ʶ�ı�û�еĻ�����ϵͳע���ӱ���Ƿ����ñ�־��ΪN
		ArrayList arrayList=null;
		String sql="select deploycode from dip_datadefinit_h where pk_xt ='"+pk_sysregister_h+"'" +
		" and nvl(dip_datadefinit_h.dr,0)=0 and deploycode='"+deploycode+"' and pk_datadefinit_h<>'"+pk_node+"' and nvl(dr,0)=0";
		if(isdeploy){			
			try {
				arrayList=(ArrayList)queryBS.executeQuery(sql, new MapListProcessor());
				if(arrayList !=null){
					//��ǰϵͳ��־���˱����⣬��<=1�����ʾû��
					if(arrayList.size()<1){
						//��ϵͳע���ӱ���Ƿ����ñ�־��ΪN
						String uapsql="update dip_sysregister_b set isyy='N' where pk_sysregister_h='"+pk_sysregister_h+"'"
						+" and dip_sysregister_b.pk_sysregister_b='"+deploycode+"' and nvl(dr,0)=0" ;					
						try {
							iq.exesql(uapsql);
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (DbException e) {
							e.printStackTrace();
						}
					}
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 2011-4-27 ����
	 * ���ӱ���ʱ�ж��Ƿ��Ƿֲ�ʽϵͳ������ǣ�����µ�ǰϵͳ��ǰվ����Ƿ����ñ�־ΪY��������ǣ�����µ�ǰϵͳ�µ�����վ����Ƿ����ñ�־ΪY
	 */
	public void updateIsYY(boolean isdeploy,String isYY,String pk_sysregister_h,String deploycode,String pk_node){
		if(isdeploy){			
			//��ϵͳע���ӱ���Ƿ����ñ�־��ΪN
			String uapsql="update dip_sysregister_b set isyy='"+isYY+"' where pk_sysregister_h='"+pk_sysregister_h+"'"
			+" and dip_sysregister_b.pk_sysregister_b='"+deploycode+"' and nvl(dr,0)=0" ;					
			try {
				try {
					iq.exesql(uapsql);
				} catch (BusinessException e) {
					e.printStackTrace();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
//		else{
//			//��ϵͳע���ӱ���Ƿ����ñ�־��ΪY
//			String uapsql="update dip_sysregister_b set isyy='"+isYY+"' where pk_sysregister_h='"+pk_sysregister_h+"' and nvl(dr,0)=0";					
//			try {
//				try {
//					iq.exesql(uapsql);
//				} catch (SQLException e) {
//					e.printStackTrace();
//				} catch (DbException e) {
//					e.printStackTrace();
//				}
//			} catch (BusinessException e) {
//				e.printStackTrace();
//			}
//		}
	}

	/**
	 * �޸ı���ʱ���޸��Ƿ����ñ�־
	 * �޸�ʱ���ж�վ���Ƿ�ı�
	 * 1.�ı��ˣ���ѯ�Ƿ�������ϵͳ�����˱仯ǰ��վ�㣬����У���Թ����������û�У��򲻴��϶Թ�
	 * 2.û�ı䣬������ǰ��״̬
	 */
	public void updateIsYY(){

	}

	/**
	 * �޸�"�Ƿ��ѽ���"��־
	 * @description ����Ѿ�����"�Ƿ��ѽ���"���϶Թ��������򲻴�Թ�
	 * @param  �Ƿ��ѽ���ϵͳע���������������ݶ�������
	 * @date 2011-5-8
	 * @author ����
	 */
	public void updateIsCreateFlag(String iscreatetab, String fpk,String pk){
		String sql="update dip_datadefinit_h set iscreatetab='"+iscreatetab+"' where pk_datadefinit_h='"+pk+"' and nvl(dr,0)=0";
		try{		
			iq.exesql(sql);		
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��Ȩ���� �̼ѿƼ�
	 * ���� ����
	 * ���Ӱ�ť
	 * ����������1.���ѡ����������ڵ㣬���費�ܲ�������ʾ
	 * 2.���ѡ����ǵ������ڵ㣨0000NC ��Ա�����������ӣ��磺���ݶ���--NCϵͳ--0000NC ��Ա��
	 * �������� 2011-4-21
	 */
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ���ӵ�ϵͳ�ڵ㣡");
			return ;
		}
		DipDatadefinitHVO tnhvo=(DipDatadefinitHVO) tempNode.getData();
		if(!tnhvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("��ѡ���ļ��н������Ӳ�����");
			return ;
		}
		super.onBoAdd(bo);
		if(this.getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()!=null){
				super.onBoLineAdd();
				
		}
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("memorytype").setEnabled(true);
		getSelfUI().getBillCardPanel().setHeadItem("pk_sysregister_h", tempNode.getNodeID());
		getSelfUI().getBillCardPanel().setHeadItem("pk_xt", tnhvo.getPk_xt());
		getSelfUI().getBillCardPanel().setHeadItem("isfolder", new UFBoolean(false));
		getSelfUI().getBillCardPanel().setHeadItem("tabsoucetype", "�Զ���");
		//���ϵͳע�� ��ڵ�ֲ�ʽϵͳΪtrue���������ݶ��塢���ݽ��ն����ͷ�ж�Ӧ�Ľڵ� �еġ��Ƿ�ֲ�ʽ���ݡ���Ӧ��true����Ӧ�ô��Ϲ�
		DipSysregisterHVO syshvo=(DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, tnhvo.getPk_xt());
//		DipDatadefinitHVO vo =(DipDatadefinitHVO) tempNode.getData();

		if(syshvo.getIsdeploy()==null||syshvo.getIsdeploy().equals("N")){
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isdeploy").setEnabled(false);
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").setEnabled(false);
		}else{
			//2011-6-3 ����Ƿֲ�ʽϵͳ����ֲ�ʽϵͳ���ݿɱ༭
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isdeploy").setEnabled(true);
			//liyunzhe
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").setEnabled(true);
		}

		getSelfUI().getBillCardPanel().setHeadItem("isdeploy", syshvo.getIsdeploy());

		/*
		 * �޸�ɸѡ������������Ƿֲ�ϵͳ�������Ƿ�ֲ�ϵͳΪ��Y��ʱ���ܲ��ճ��ֲ�վ��
		 * 2011-06-03
		 * zlc*/
		String pk_sysregister_h =tnhvo.getPk_xt();
		//2011-4-26 ���� ��̬�Զ�����գ���ѯ��ǰϵͳ�µķֲ�ʽ��ʶ	
		boolean f=false;
		String isdep=this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isdeploy").getValueObject()==null?null:
			this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isdeploy").getValueObject().toString();
		if(isdep!=null&&isdep.length()>0){
			f=Boolean.parseBoolean(isdep);
			if(f==true){


				UIRefPane pane=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").getComponent();
				SysRegisterRefModel model = new SysRegisterRefModel();
				model.addWherePart(" and dip_sysregister_b.pk_sysregister_h='"+pk_sysregister_h+"' and dip_sysregister_h.isdeploy='"+(f?'Y':'N')+ "' and nvl(dip_sysregister_b.dr,0)=0  and (dip_sysregister_b.isstop='N' or dip_sysregister_b.isstop='') ");
				pane.setRefModel(model);
				this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").setComponent(pane);
			}}
		//���ñ� �Զ������

		String pk_datadefinit_h=tnhvo.getPk_datadefinit_h();
		UIRefPane uir=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getBodyItem("def_quotetable").getComponent();
		//2011-6-29 ����Ҫ��������ϵͳ�µ��Ѿ�����ı�,�Լ�ԭʼ�Ĳ���������û�мӡ��Ƿ�ϵͳԤ������=Y�������԰Ѳ���������ˣ����Ʊ�ϵͳ�µ�������ע����
		QuoteTableTreeRefModel model2=new QuoteTableTreeRefModel();
		model2.addWherePart(" and pk_datadefinit_h1 <>'"+pk_datadefinit_h+"'");
		uir.setRefModel(model2);

		UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("memorytable").getComponent();
//		docu=jcp.getUITextField().getDocument();
		if(syshvo!=null){
			String code=syshvo.getCode();
			if(!code.equals("0000")){
				jcp.getUITextField().setDocument(new StringDocument(jcp.getUITextField(),"DIP_DD_"+extcode.toUpperCase()+"_"));
				jcp.getUITextField().setText("DIP_DD_"+extcode.toUpperCase()+"_");
			}
		}
		this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(0,0,true,true);
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(0, "def_quotetable", false);

	}
	Document docu;

	
	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		// ��ȡȨVO
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ��ѯ�Ľڵ㣡");
			return ;
		}
		String str=(String)tempNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("ֻ�ܲ�ѯ��ڵ�"); 
			return;
		}
		DipDatadefinitHVO vo =(DipDatadefinitHVO) tempNode.getData();
		if(null!= vo && !vo.getMemorytype().equals("��")){//
			getSelfUI().showWarningMessage("ֻ�ܲ�ѯ��ڵ�"); 
			return;
		}
		String pk_datadefinit_h = (String) vo.getAttributeValue("pk_datadefinit_h");

		SuperVO[] vos = HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "pk_datadefinit_h='" + pk_datadefinit_h + "' and isnull(is_check,'N')='Y'  ");

//		nc.vo.dip.datadefcheck.MyBillVO billvo = new nc.vo.dip.datadefcheck.MyBillVO();
//		
//		DipDatadefinitHVO hvo= (DipDatadefinitHVO)vo;

		
		getSelfUI().getBillListPanel().getHeadBillModel().setBodyDataVO(vos);
		getSelfUI().getBillListPanel().getBodyBillModel().setBodyDataVO(null);		
	}
	/**
	 * 2011-4-18 cl
	 */
	@Override
	protected void onBoEdit() throws Exception {
		//�޸�ʱ����"������"��ť����Ϊ������
//		getSelfUI().getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);

		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ�޸ĵĽڵ㣡");
			return ;
		}
		String str=(String)tempNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("ϵͳ�ڵ㲻�����޸Ĳ�����"); 
			return;
		}
		DipDatadefinitHVO hvo =(DipDatadefinitHVO) tempNode.getData();
		
		if(null!= hvo && !hvo.getMemorytype().equals("��")){//
			getSelfUI().showWarningMessage("ֻ���޸ı�ڵ�"); 
			return;
		}
		
		if(getSelfUI().getBillListPanel().getHeadTable().getSelectedRows().length!=1 ){
			getSelfUI().showWarningMessage("��ѡ��һ����������"); 
			return;
		}

//		super.onBoEdit();	
		selectrow=getSelfUI().getBillListPanel().getHeadTable().getSelectedRow();

		getBillUI().setBillOperate(IBillOperate.OP_EDIT);
//		getSelfUI().getBillListPanel().getHeadTable().changeSelection(0, 0, false, false);
		getSelfUI().getBillListPanel().getHeadTable().setEnabled(false);
		getSelfUI().isEditBtn(true);
		getSelfUI().setTreeEnable(false);

//		int row=getSelfUI().getBillListPanel().getBodyBillModel().getRowCount();
		if(getSelfUI().getBillListPanel().getBodyBillModel()!=null){
			//for(int i=row;i<201;i++){
				onBoLineAdd();
			//}
		}
		//�༭ǰ
		getSelfUI().getBillListPanel().getBodyScrollPane("dip_datadefinit_c").addEditListener2(new BillEditListener2() {
			
			public boolean beforeEdit(BillEditEvent e) {
				if("def_quote_field".equals(e.getKey())){			
					int row=e.getRow();
					String pk_datadefinit_h=getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(row, "quote_table")==null?"":getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(row,"quote_table").toString();
					if("".equals(pk_datadefinit_h)||pk_datadefinit_h.length()==0){
						getSelfUI().showWarningMessage("����ѡ�����ñ�");
						return false;
					}
					
					UIRefPane pane3=(UIRefPane) getSelfUI().getBillListPanel().getBodyItem("def_quote_field").getComponent();
					DataDefFieldbRefModel model3=new DataDefFieldbRefModel();
					model3.addWherePart(" and dip_datadefinit_b.pk_datadefinit_h = '"+pk_datadefinit_h+"' and nvl(dip_datadefinit_b.dr ,0)=0 " );
					pane3.setRefModel(model3);	
				}	
				return true;
			}
		});
		//�༭��	
		getSelfUI().getBillListPanel().getBodyScrollPane("dip_datadefinit_c").addEditListener(new BillEditListener() {
			
			public void bodyRowChange(BillEditEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterEdit(BillEditEvent e) {
				
				if("def_quote_table".equals(e.getKey())){			
					int row=e.getRow();
					String pk_datadefinit_h=getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(row, "def_quote_table")==null?"":getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(row,"def_quote_table").toString();
					
					if(!"".equals(pk_datadefinit_h)){
						getSelfUI().getBillListPanel().getBodyBillModel().setValueAt(2, row, "dtype");
					}else{
						getSelfUI().getBillListPanel().getBodyBillModel().setValueAt(2, row, "dtype");
					}
				}
				if("input_value".equals(e.getKey())){
					int row=e.getRow();
					String dtype=getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(row,"dtype")==null?"":getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(row,"dtype").toString();
					String input_value=getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(row, "input_value")==null?"":getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(row,"input_value").toString();
					if(input_value==null||input_value.trim().length()<=0){
						return;
					}
					if(input_value!=null&& null!=dtype && dtype.equalsIgnoreCase("����У��")){
						String regex = "^[0-9]*[1-9][0-9]*$";
						if(!input_value.matches(regex)){//����������ʱ
							getSelfUI().showWarningMessage("ֻ����������");
							getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "input_value");
						}
					}
				}
				
				if(e.getKey().equals(DipDatadefinitCVO.DTYPE)){
					int row=e.getRow();
					Object isdo= getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(row, DipDatadefinitCVO.DTYPE);
		            String istype = isdo ==null?"":isdo.toString();
		           if(istype.equals("����У��")||istype.equals("ת��У��")||istype.equals("��ֵ�Ƚ�")){
		        	   getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "input_value", true);
		        	   getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "def1", true);
		        	   getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "ref", false);
		        	   getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "ref");
		        	   getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "ref_pk");
		           }else if(istype.equals("����У��") || istype.equals("����У��") || istype.equals("����Ȩ��У��")){
						getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "input_value", false);
						getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "def1", false);
						getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "ref", false);
						getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "input_value");
						getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "def1");
						getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "ref");
						getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "ref_pk");
					}else if(istype.equals("��������У��") || istype.equals("���ùؼ���")){
						getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "input_value", false);
						getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "def1", false);
						getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "ref", true);
						getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "input_value");
						getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "def1");
					}else if(istype.equals("�ֹ�����У��") || istype.equals("�ֹ��ؼ���")){
						getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "input_value", true);
						getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "def1", false);
						getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(row, "ref", false);
						getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "ref");
						getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "def1");
						getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", row, "ref_pk");
					}
				}
				int k=getSelfUI().getBillListPanel().getBodyBillModel().getRowCount();
				int m=getSelfUI().getBillListPanel().getBodyTable().getSelectedRow();
				if(k-1==m){
					getSelfUI().getBillListWrapper().addLine();
				}
			}
		});
		
		
		int rowcount=getSelfUI().getBillListPanel().getBodyBillModel().getRowCount();
		getSelfUI().getBillListPanel().getBodyBillModel().setEnabledAllItems(true);
		
		for(int i=0;i<rowcount;i++){
			getSelfUI().getBillListPanel().getBodyBillModel().setEditRow(i);

			Object isdo= getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(i, DipDatadefinitCVO.DTYPE);
            String istype = isdo ==null?"":isdo.toString();
            if(istype.equals("����У��") || istype.equals("�ֹ�����У��") ||istype.equals("�ֹ��ؼ���")||istype.equals("��������У��") || istype.equals("���ùؼ���")){
            	getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "is_content", true);
            }else{
            	getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "is_content", false);
            }
            
            if(istype.equals("����У��") ||istype.equals("�ֹ�����У��") ||istype.equals("�ֹ��ؼ���")){
            	getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "input_value", true);
				getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "def_quote_value", false);
				getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "def_quote_table", false);
				getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "def_quote_field", false);


			}else if(istype.equals("����У��") || istype.equals("����У��") || istype.equals("����Ȩ��У��")){
				getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "input_value", false);
				getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "def1", false);
				getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "ref", false);
				
			}else if(istype.equals("��������У��") || istype.equals("���ùؼ���")){
				getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "input_value", false);
				getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "def_quote_value", true);
				getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "def_quote_table", false);
				getSelfUI().getBillListPanel().getBodyBillModel().setCellEditable(i, "def_quote_field", false);

			}
		}
	}

	/**
	 * У���������ơ�Ӣ������
	 * @param name
	 * @description:��������ֻ�������Ŀ�ʼ����β���԰������֣�Ӣ������ֻ����Ӣ����ĸ���»��߿�ʼ����β���԰�������
	 * @author ����
	 * @date 2011-5-5
	 */
	String errmsg="";
	public boolean nameType(String type,String value){
		errmsg="";
		boolean ret=true;
		if(type.equals("��������")&& (! value.matches("[\u4E00-\u9FA5]+[[\u4E00-\u9FA5]*+[0-9]*]*"))){
			ret=false;
			errmsg=errmsg+" ���������ơ����������Ŀ�ͷ,���������ֽ�β��";
		}

		if(type.equals("Ӣ������")&& (! value .matches("[A-Za-z]+[[A-Za-z]*+[_]+[0-9]*]*"))){
			ret=false;
			errmsg=errmsg+" ��Ӣ�����ơ�ֻ���Ǵ�дӢ����ĸ��ͷ�����԰����»��ߣ���β���԰������֣�";
		}
		if(type.equals("�洢����")&&(! value .matches("[A-Za-z]+[[A-Za-z]*+[_]+[0-9]*]*"))){
			ret=false;
			errmsg=errmsg+" ��Ӣ�����ơ�ֻ���Ǵ�дӢ����ĸ��ͷ�����԰����»��ߣ���β���԰������֣�";
		}
		return ret;
	}
	public void onBoEditFolder() throws Exception {
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ�޸ĵĽڵ㣡");
			return ;
		}
		String str=(String)tempNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("ϵͳ�ڵ㲻�����޸Ĳ�����"); 
			return;
		}
		DipDatadefinitHVO vo =(DipDatadefinitHVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
			DipDatadefinitHVO[] listvos=(DipDatadefinitHVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitHVO.class, "pk_xt='"+vo.getPk_xt()+"' and isfolder='Y' and pk_datadefinit_h<>'"+vo.getPrimaryKey()+"' and nvl(dr,0)=0");
			List<String> listcode=new ArrayList<String>();
			List<String> listname=new ArrayList<String>();
			if(listvos!=null&&listvos.length>0){
				for(int i=0;i<listvos.length;i++){
					listcode.add(listvos[i].getSyscode());
					listname.add(listvos[i].getSysname());
				}
			}
				
			AddFolderDlg adlg=new AddFolderDlg(getBillUI(),listcode,listname,vo.getSyscode(),vo.getSysname());
			adlg.showModal();
//			if(adlg.isOk()){
//				String tempc=adlg.getCode();
//				String tempn=adlg.getName();
//				if(!tempc.equals(vo.getSyscode())||!tempn.equals(vo.getSysname())){
//					vo.setSyscode(tempc);
//					vo.setSysname(tempn);
//					HYPubBO_Client.update(vo);
//					vo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, vo.getPrimaryKey());
//					if (getUITreeCardController().isAutoManageTree()) {	
//						getSelfUI().insertNodeToTree(vo);
//						getBillTreeManageUI().updateUI();
//						getBillCardPanelWrapper().getBillCardPanel().setHeadItem("syscode", tempc);
//						getBillCardPanelWrapper().getBillCardPanel().setHeadItem("sysname", tempn);
//					}
//				}
//			}
			return;
		}
	}

	public void onBoDeleteFolder() throws Exception {
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ�޸ĵĽڵ㣡");
			return ;
		}
		String str=(String)tempNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("ϵͳ�ڵ㲻�����޸Ĳ�����"); 
			return;
		}
		VOTreeNode node1=(VOTreeNode) tempNode.getParent();
		DipDatadefinitHVO vo =(DipDatadefinitHVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
			SuperVO[] hvos=HYPubBO_Client.queryByCondition(DipDatadefinitHVO.class, "pk_sysregister_h='"+vo.getPrimaryKey()+"'");
			if(hvos!=null&&hvos.length>0){
				throw new Exception("�ļ������Ѿ������ݶ��壬����ɾ����");
			}else{
				HYPubBO_Client.delete(vo);
				getBillTreeManageUI().getBillTreeData().deleteNodeFromTree(this.getBillTreeManageUI().getBillTreeSelectNode());
				if(node1!=null){
					getSelfUI().getBillTree().setSelectionPath(new javax.swing.tree.TreePath(node1
							.getPath()));
				}
			}
		}
	}
	/**
	 * @desc �ļ��ƶ�
	 * */
	public void onBoMoveFolder() throws Exception{
		
		MyBillVO billvo=(MyBillVO) getBufferData().getCurrentVO();
		if(billvo!=null&&billvo.getParentVO()!=null){
			DipDatadefinitHVO hvo=(DipDatadefinitHVO) billvo.getParentVO();
			MovefolderDLG dlg=new MovefolderDLG(getBillUI(),IContrastUtil.YWLX_DY,hvo);
			dlg.showModal();
			String ret=dlg.getRes();
			if(ret!=null){
				hvo.setAttributeValue((String)IContrastUtil.getFpkMap().get(IContrastUtil.YWLX_DY), ret);
				HYPubBO_Client.update(hvo);
				hvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, hvo.getPrimaryKey());
				billvo.setParentVO(hvo);
				getBufferData().addVOToBuffer(billvo);
				getBufferData().setCurrentVO(billvo);
				getSelfUI().getBillTree().updateUI();
				onBoRefresh();
				TableTreeNode node=(TableTreeNode) getSelfUI().getBillTree().getModel().getRoot();
				Vector v=new Vector();
				getAllNode(node, v);
				if(v!=null&&v.size()>0){
					TableTreeNode tempnode=null;
					for(int i=0;i<v.size();i++){
						String pkf=(String)((TableTreeNode)v.get(i)).getNodeID();
						if(pkf!=null&&pkf.equals(hvo.getPrimaryKey())){
							tempnode=(TableTreeNode) v.get(i);
							break;
						}
					}
					if(tempnode!=null){
						getSelfUI().getBillTree().setSelectionPath(new javax.swing.tree.TreePath( tempnode.getPath()));
					}
				}
			}
		}
		
	}
	private void getAllNode(Object node, Vector v){
	    v.add(node);
	    int childCount = getSelfUI().getBillTree().getModel().getChildCount(node);
	    for (int i = 0; i < childCount; i++) {
            Object child = getSelfUI().getBillTree().getModel().getChild(node, i);
            getAllNode(child, v);
        }
	}
	public void onBoAddFolder() throws Exception {
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null||tempNode.getData()==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ���ӵĽڵ㣡");
			return ;
		}
		DipDatadefinitHVO hvo=(DipDatadefinitHVO) tempNode.getData();
		if(!hvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("�����ļ��в����������ļ��в�����");
			return ;
		}
		DipDatadefinitHVO newhvo=new DipDatadefinitHVO();
		newhvo.setPk_sysregister_h(hvo.getPrimaryKey());
		newhvo.setPk_xt(hvo.getPk_xt());
		newhvo.setIsfolder(new UFBoolean(true));
		DipDatadefinitHVO[] listvos=(DipDatadefinitHVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitHVO.class, "pk_xt='"+hvo.getPk_xt()+"' and isfolder='Y' and nvl(dr,0)=0");
		List<String> listcode=new ArrayList<String>();
		List<String> listname=new ArrayList<String>();
		if(listvos!=null&&listvos.length>0){
			for(int i=0;i<listvos.length;i++){
				listcode.add(listvos[i].getSyscode());
				listname.add(listvos[i].getSysname());
			}
		}
			
		AddFolderDlg addlg=new AddFolderDlg(getBillUI(),listcode,listname,null,null);
		addlg.showModal();
		if(addlg.isOk()){
			newhvo.setSyscode(addlg.getCode());
			newhvo.setSysname(addlg.getName());
			String pk = null;
			try {
				pk = HYPubBO_Client.insert(newhvo);
			} catch (UifException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				newhvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, pk);
				
//				if (getUITreeCardController().isAutoManageTree()) {	
////					MyBillVO mvo=new MyBillVO();
////					mvo.setParentVO(newhvo);
//					getSelfUI().insertNodeToTree(newhvo);
//				}
			} catch (UifException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 
	 * <P>
	 * ���ܣ�����ʱ����֤��
	 * 1������Ƿֲ�ϵͳ����ֲ���־���
	 * 2��Ψһ��У�飺���롢ҵ���־���洢����
	 * 3����ͷ��
	 * 4������:  a\
	 * 5����ť������󣬴�����ť��ʾ��
	 * 
	 * �˷���Ϊ���Ǹ��෽�� <BR>
	 * �������е�ISingleController�ж�ȥ�������򱣴�ʱ����ͷΪ��
	 * ��Ȩ��Ϣ �̼ѿƼ�
	 * @author ����
	 * @description:Ψһ��У�飺���롢ҵ���־���洢����
	 * @throws Exception
	 * @see nc.ui.trade.treecard.TreeCardEventHandler#onBoSave()
	 * �������� 2011-4-21
	 */
	protected void onBoSave() throws Exception {
//		dataNotNullValidate();
		int headcount = getSelfUI().getBillListPanel().getHeadTable().getSelectedRow();
		if(headcount==-1){
			getSelfUI().showWarningMessage("��ѡ��һ����������"); 
			return;
		}
		int rowcount=getSelfUI().getBillListPanel().getBodyBillModel().getRowCount();
        if(rowcount>0){
        	List list=new ArrayList();
        	BillModel bodyBillModel = getSelfUI().getBillListPanel().getBodyBillModel();
        	BillItem[] bodyItems = bodyBillModel.getBodyItems();
        	for(int i=0 ; i< rowcount;i ++ ){
				Boolean isNull = true;
        		for (BillItem billItem : bodyItems) {
					if(billItem.isShow()){
						Object valueAt = bodyBillModel.getValueAt(i, billItem.getKey());
						if(null != valueAt && !"".equals(valueAt)){
							isNull = false;
							break;
						}
					}
				}
        		if(isNull){
        			list.add(i);
        		}
        	}
        	if(list.size()!=0){
    			int []delrow=new int [list.size()];
    			for(int i=0;i<list.size();i++){
    				delrow[i]=(Integer)list.get(i);
    			}
    			bodyBillModel.delLine(delrow);
    		}
        }
		String bool =  valuedate();
		if(null !=bool && bool.length()>0){
			getSelfUI().showWarningMessage(bool); 
			return;
		}
		String headpk = getSelfUI().getBillListPanel().getHeadBillModel().getValueAt(headcount, DipDatadefinitBVO.PK_DATADEFINIT_B)==null?"":getSelfUI().getBillListPanel().getHeadBillModel().getValueAt(headcount, DipDatadefinitBVO.PK_DATADEFINIT_B).toString();
		//��ɾ���ӱ�����
		String sql=" DELETE FROM dip_datadefinit_c where pk_datadefinit_b = '"+headpk+"' ";
		iq.exesql(sql);
		
		rowcount=getSelfUI().getBillListPanel().getBodyBillModel().getRowCount();
        if(rowcount>0){
        	List<DipDatadefinitCVO> bvos = new ArrayList<DipDatadefinitCVO>();
        	for(int i=0 ; i< rowcount;i ++ ){
        		DipDatadefinitCVO bodyValueRowVO = (DipDatadefinitCVO)getSelfUI().getBillListPanel().getBodyBillModel().getBodyValueRowVO(i, DipDatadefinitCVO.class.getName());
        		bodyValueRowVO.setPk_datadefinit_b(headpk);
        		bodyValueRowVO.setPk_datadefinit_c(null);
        		bvos.add(bodyValueRowVO);
        	}
        	HYPubBO_Client.insertAry(bvos.toArray(new DipDatadefinitCVO[0]));
        }	
		// ���ñ����״̬
        setSaveOperateState();
		getSelfUI().onTreeSelectSetButtonState(getSelectNode());
		getSelfUI().getBillListPanel().getHeadTable().setEnabled(true);
        getSelfUI().showWarningMessage("����ɹ�");
	}
	
	private String valuedate() {
	    StringBuffer msg = new StringBuffer();
		int rowcount=getSelfUI().getBillListPanel().getBodyBillModel().getRowCount();
        if(rowcount>0){
        	Map<String, String> codemap = new HashMap<String, String>();
        	Map<String, String> namemap = new HashMap<String, String>();

        	for(int i=0 ; i< rowcount;i ++ ){
        		int row = i+1;
        		DipDatadefinitCVO bodyValueRowVO = (DipDatadefinitCVO)getSelfUI().getBillListPanel().getBodyBillModel().getBodyValueRowVO(i, DipDatadefinitCVO.class.getName());
        		if(bodyValueRowVO.getDcode()==null || "".equals(bodyValueRowVO.getDcode()) ||
        				bodyValueRowVO.getDname()==null || "".equals(bodyValueRowVO.getDname())||
        				bodyValueRowVO.getDtype()==null || "".equals(bodyValueRowVO.getDtype())){	
        			msg.append("��"+row+"�� ,��У����롢У�����ơ�У�����͡� ����Ϊ��!\n");
        		}else{
        			if(null!=codemap && null!=codemap.get(bodyValueRowVO.getDcode())){
            			msg.append("��"+row+"��, ��У����롿 �����ظ�! \n");
        			}else{
        				codemap.put(bodyValueRowVO.getDcode(), "code");
        			}
        			if(null!=namemap && null!=namemap.get(bodyValueRowVO.getDname())){
            			msg.append("��"+row+"��, ��У�����ơ� �����ظ�! \n");
        			}else{
        				namemap.put(bodyValueRowVO.getDname(), "code");
        			}
        		}
        		if(null!=bodyValueRowVO.getDtype() &&(bodyValueRowVO.getDtype()==1||bodyValueRowVO.getDtype()==3||bodyValueRowVO.getDtype()==5)){
        			if(bodyValueRowVO.getInput_value()==null || "".equals(bodyValueRowVO.getInput_value())){
            			msg.append("��"+row+"��, ���ֹ����롿 ����Ϊ��! \n");
        			}
        		}else if(null!=bodyValueRowVO.getDtype() &&(bodyValueRowVO.getDtype()==4||bodyValueRowVO.getDtype()==6)){
        			if(bodyValueRowVO.getRef_pk()==null || "".equals(bodyValueRowVO.getRef_pk())){
            			msg.append("��"+row+"��, �����ò��ա� ����Ϊ��! \n");
        			}
        		}
        		if(null!=bodyValueRowVO.getDtype() &&(bodyValueRowVO.getDtype()==1||bodyValueRowVO.getDtype()==7)){
        			if(bodyValueRowVO.getDef1()==null || "".equals(bodyValueRowVO.getDef1())){
            			msg.append("��"+row+"��, ���������� ����Ϊ��! \n");
        			}
        		}
        	}
        }	
        if(null!=msg && msg.toString().length()>0){
        	return "����ʧ�ܣ�У�鲻ͨ����\n"+msg.toString();
        }
		
		return null;
	}

	/**
	 * ���ܣ�����ɾ�����ϵ����ݣ������ɾ�У����ڱ���ʱ��������Ҫɾ����ɾ��
	 * ���ߣ�ytq
	 * ����:2011-07-02
	 * */
	public void deleteLine(){
		String sql =" delete from dip_datadefinit_b where pk_datadefinit_b in ('11122aabb'"+getSelfUI().delstr+")";
		IQueryField qry = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
		try {
			qry.exesql(sql);
		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DbException e) {
			e.printStackTrace();
		}
		getSelfUI().delstr = "";

	}
	/**��֤���ֲ���־
	 * ���ܣ�
	 * ���ߣ�����
	 * ���ڣ�2011-07-02
	 * */
	public String checkTableHead(DataDefcheckClientUI ui,String pk){
		boolean isdeploy=Boolean.parseBoolean( ui.getBillCardPanel().getHeadItem("isdeploy").getValueObject().toString());//�Ƿ�ֲ�ʽϵͳ
		String pk_sysregister_h=(String) ui.getBillCardPanel().getHeadItem("pk_sysregister_h").getValueObject();
		String deploycode=(String) ui.getBillCardPanel().getHeadItem("deploycode").getValueObject();//�仯��ķֲ�ʽ��ʶ
		String pk_node=ui.selectnode;
		//2011-6-1 �޸ı���ʱ���޸����Ƿ����ñ�ʶ�ķ���
		//1.�ı��ˣ���ѯ�Ƿ�������ϵͳ�����˱仯ǰ��վ�㣬����У���Թ����������û�У��򲻴��϶Թ�
		//2.û�ı䣬������ǰ��״̬

		if(isdeploy){
			String sql="select deploycode from dip_datadefinit_h where pk_datadefinit_h='"+pk+"'";
			String olddeploycode = null;
			try {
				olddeploycode = iq.queryfield(sql);
			} catch (BusinessException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (DbException e) {
				e.printStackTrace();
			}//�仯ǰ�ķֲ�ʽ��ʶ:���olddeploycode=""�Ļ�����Ϊ����,����Ϊ�޸�
			if(olddeploycode==null)
				olddeploycode = "";
			if(deploycode==null)
				deploycode = "";
			if("".equals(deploycode)){
				getSelfUI().showWarningMessage("�ֲ�ʽ��ʶ����Ϊ�գ�");
				return "";
			}else{
				if(!olddeploycode.equals(deploycode)){
					String sql2="select deploycode from dip_datadefinit_h where pk_sysregister_h ='"+pk_sysregister_h+"'" +
					" and nvl(dip_datadefinit_h.dr,0)=0 and deploycode='"+olddeploycode+"' and pk_datadefinit_h<>'"+pk_node+"' and nvl(dr,0)=0";
					ArrayList arrayList = null;
					try {
						arrayList = (ArrayList)queryBS.executeQuery(sql2, new MapListProcessor());
					} catch (BusinessException e) {
						e.printStackTrace();
					}
					if(arrayList==null || arrayList.size()==0){
						String uapsql="update dip_sysregister_b set isyy='N' where pk_sysregister_h='"+pk_sysregister_h+"'"
						+" and dip_sysregister_b.pk_sysregister_b='"+olddeploycode+"' and nvl(dr,0)=0" ;	
						//�޸ı仯ǰ�ķֱ�վ���Ƿ����ñ�־
						try {
							iq.exesql(uapsql);
						} catch (BusinessException e) {
							e.printStackTrace();
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (DbException e) {
							e.printStackTrace();
						}

						//�޸ı仯����վ����Ƿ����ñ�־
						updateIsYY(isdeploy,"Y", pk_sysregister_h, deploycode, pk_node);					
					}
				}
			}
		}else{
			//2011-6-9 �������ǰ�� �Ƿ�ֲ�ʽ���� �޸�Ϊ �Ƿֲ�ʽ���ݣ���ô�ֲ�ʽ��־�ͷֲ�ʽ���ƣ�վ�㣩��Ϊ�գ���ʱ�޸�ϵͳע�������Ƿ������ֶ�ΪN
			String sql="select deploycode from dip_datadefinit_h where pk_datadefinit_h='"+pk+"'";
			String olddeploycode = null;
			try {
				olddeploycode = iq.queryfield(sql);
			} catch (BusinessException e) {
				e.printStackTrace();
				return "";
			} catch (SQLException e) {
				e.printStackTrace();
				return "";
			} catch (DbException e) {
				e.printStackTrace();
				return "";
			}//�仯ǰ�ķֲ�ʽ��ʶ:���olddeploycode=""�Ļ�����Ϊ����,����Ϊ�޸�
			if(olddeploycode==null)
				olddeploycode = "";
			if(deploycode==null)
				deploycode = "";
			if(!olddeploycode.equals(deploycode)){
				String sql2="select deploycode from dip_datadefinit_h where pk_sysregister_h ='"+pk_sysregister_h+"'" +
				" and nvl(dip_datadefinit_h.dr,0)=0 and deploycode='"+olddeploycode+"' and pk_datadefinit_h<>'"+pk_node+"' and nvl(dr,0)=0";
				ArrayList arrayList = null;
				try {
					arrayList = (ArrayList)queryBS.executeQuery(sql2, new MapListProcessor());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(arrayList==null || arrayList.size()==0){
					String uapsql="update dip_sysregister_b set isyy='N' where pk_sysregister_h='"+pk_sysregister_h+"'"
					+" and dip_sysregister_b.pk_sysregister_b='"+olddeploycode+"' and nvl(dr,0)=0" ;	
					//�޸ı仯ǰ�ķֱ�վ���Ƿ����ñ�־
					try {
						iq.exesql(uapsql);
					} catch (BusinessException e) {
						e.printStackTrace();
						return "";
					} catch (SQLException e) {
						e.printStackTrace();
						return "";
					} catch (DbException e) {
						e.printStackTrace();
						return "";
					}				
				}
			}
		}
		return "ok";
	}
	@Override
	protected void setTSFormBufferToVO(AggregatedValueObject setVo) throws Exception {
		if (setVo == null)
			return;
		AggregatedValueObject vo = getBufferData().getCurrentVO();
		if (vo == null)
			return;
		if (getBillUI().getBillOperate() == 0) {
			if (vo.getParentVO() != null && setVo.getParentVO() != null)
				setVo.getParentVO().setAttributeValue("ts", vo.getParentVO().getAttributeValue("ts"));
			SuperVO changedvos[] = (SuperVO[]) (SuperVO[]) getChildVO(setVo);
			if (changedvos != null && changedvos.length != 0) {
				HashMap bufferedVOMap = null;
				SuperVO bufferedVOs[] = (SuperVO[]) (SuperVO[]) getChildVO(vo);
				if (bufferedVOs != null && bufferedVOs.length != 0) {
					bufferedVOMap = Hashlize.hashlizeObjects(bufferedVOs, new VOHashPrimaryKeyAdapter());
					for (int i = 0; i < changedvos.length; i++) {
						if (changedvos[i].getPrimaryKey() == null)
							continue;
						ArrayList bufferedAl = (ArrayList) bufferedVOMap.get(changedvos[i].getPrimaryKey());
						if (bufferedAl != null) {
							SuperVO bufferedVO = (SuperVO) bufferedAl.get(0);
							changedvos[i].setAttributeValue("ts", bufferedVO.getAttributeValue("ts"));
						}
					}

				}
			}
		}
	}

	@Override
	public void onTreeSelected(VOTreeNode arg0) {
		try {
			// ��ȡȨVO
			SuperVO vo = (SuperVO) arg0.getData();
//			DipDatadefinitHVO tnhvo=(DipDatadefinitHVO) arg0.getData();
			
//			String tab=tnhvo.getMemorytable()==null?"":tnhvo.getMemorytable().toString();
//			UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("memorytable").getComponent();
//			if(docu!=null){
//				jcp.getUITextField().setDocument(docu);
//				jcp.setText(tab);	
//			}else{
//				docu=jcp.getUITextField().getDocument();
//			}
			
			String pk_datadefinit_h = (String) vo.getAttributeValue("pk_datadefinit_h");

			SuperVO[] vos = HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "pk_datadefinit_h='" + pk_datadefinit_h + "' and isnull(dr,0)=0 and isnull(is_check,'N')='Y' ");

			nc.vo.dip.datadefcheck.MyBillVO billvo = new nc.vo.dip.datadefcheck.MyBillVO();
			
			DipDatadefinitHVO hvo= (DipDatadefinitHVO)vo;
//			// ����������VO
//			billvo.setParentVO(hvo);
//			billvo.setChildrenVO(vos);
//			// ��ʾ����
//			getBufferData().addVOToBuffer(billvo);
			
			getSelfUI().getBillListPanel().getHeadBillModel().setBodyDataVO(vos);
			getSelfUI().getBillListPanel().getBodyBillModel().setBodyDataVO(null);

			/*
			 * ���������Ƿ�ֲ�ʽ���ݡ���Թ����ı仯��
			 * ���ϵͳע���еġ��ֲ�ϵͳ���޸��ˣ���δ���϶Թ��޸�Ϊ���϶Թ��ˣ����ϵͳ�µ��ӽڵ�ġ��Ƿ�ֲ�ʽ���ݡ�ҲӦ����δ���϶Թ���Ϊ���϶Թ�
			 * @date 2011-5-9 13:42
			 * begin
			 */
//			VOTreeNode treeNode=getSelectNode();
//			String pid=null;
//			DipSysregisterHVO syshvo;
//			if(treeNode !=null){
//				pid=(String) treeNode.getParentnodeID();
//			}
//			if(treeNode!=null&&"".equals(treeNode.getParentnodeID())){
////				DipSysregisterHVO syshvo;
//				try{
//					syshvo=(DipSysregisterHVO)HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, treeNode.getNodeID().toString());
//					extcode=syshvo.getExtcode();
//				}catch(UifException e){
//					e.printStackTrace();
//				}
//			}else if(treeNode!=null&&treeNode.getParentnodeID()!=null&&treeNode.getParentnodeID().toString().length()>0){
//				try{
//					syshvo=(DipSysregisterHVO)HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, tnhvo.getPk_xt()/*treeNode.getParentnodeID().toString()*/);
//					extcode=syshvo.getExtcode();
//				}catch(UifException e){
//					e.printStackTrace();
//				}
//			}
//
//
//			String sql="select isdeploy from dip_sysregister_h where pk_sysregister_h='"+pid+"' and nvl(dr,0)=0";
//			try{
//				//�Ƿ�ֲ�ʽ���ݲ��ɱ༭
//				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isdeploy").setEnabled(false);
//				String isdeploy=iq.queryfield(sql);	
//				//����Ƿֲ�ʽ����
//				if(isdeploy.equals("Y")){
//					//�ֲ�ʽ��ʶΪ�ɱ༭	
//					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").setEnabled(true);
//				}else{
//					//�ֲ�ʽ��ʶΪ���ɱ༭	
//					getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").setEnabled(false);
//				}
//			}catch (Exception e) {
//				e.printStackTrace();
//			}




			//2011-4-26 ���� ��̬�Զ�����գ���ѯ��ǰϵͳ�µķֲ�ʽ��ʶ			
////			String pk_sysregister_h = hvo.getPk_sysregister_h();
//			UIRefPane pane=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("deploycode").getComponent();
//			SysRegisterRefModel model = new SysRegisterRefModel();
//			model.addWherePart(" and dip_sysregister_b.pk_sysregister_h='"+fpk+"' and nvl(dip_sysregister_b.dr,0)=0  and (dip_sysregister_b.isstop='N' or dip_sysregister_b.isstop='') ");
//			pane.setRefModel(model);
//
//
//
//			UIRefPane pane2=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getBodyItem("def_quotetable").getComponent();
//			QuoteTableTreeRefModel model2=new QuoteTableTreeRefModel();
//			model2.addWherePart(" and pk_datadefinit_h1 <>'"+pk_datadefinit_h+"'");
//			pane2.setRefModel(model2);

//			getBillTreeManageUI().getTreeToBuffer().put(arg0.getNodeID(), "" + (getBufferData().getVOBufferSize() - 1));
			//��ʾ���ӱ��
			


		} catch (ComponentException e) {
			e.printStackTrace();
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��Ȩ���У� �̼ѿƼ�
	 * ���ߣ� ����
	 * ���������Ƶ�ǰ��������ݣ���������һ����ǰ�Ľڵ㣬�ı��������������Ϊ�գ��Ҵ��ڿɱ༭״̬
	 * ʱ�䣺2011-4-22
	 */
	@Override
	protected void onBoCopy() throws Exception {	
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ���ƵĽڵ㣡");
			return ;
		}
		String str=(String)tempNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("ϵͳ�ڵ㲻�������Ʋ�����"); 
			return;
		}else{
			DipDatadefinitHVO defhvo=(DipDatadefinitHVO) tempNode.getData();
			
			if(defhvo!=null&&defhvo.getIsfolder().booleanValue()){
				getSelfUI().showErrorMessage("�ļ��нڵ㲻�ܸ��ƣ�");
				return;
			}
			
			if(defhvo!=null&&defhvo.getIsfolder().booleanValue()==false){
				DipSysregisterHVO hvo=(DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, defhvo.getPk_xt());
				if(hvo==null){
					getSelfUI().showErrorMessage("û���ҵ���Ӧ��ϵͳע�ᣡ");
					return;
				}
				/*��ѯNCϵͳ�µĽڵ㣬���������޸Ĳ��� 2011-5-23 ���� begin */
					getSelfUI().getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
					try {
						super.onBoCopy();
					} catch (Exception e) {
						e.printStackTrace();
					}
					UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("memorytable").getComponent();
					docu=jcp.getUITextField().getDocument();
					if(defhvo.getMemorytype().equals("��")){
						if(defhvo.getPk_xt()!=null&&!defhvo.getPk_xt().equals("0001AA1000000001XQ1B")){
							jcp.getUITextField().setDocument(new StringDocument(jcp.getUITextField(),"DIP_DD_"+extcode.toUpperCase()+"_"));
							jcp.getUITextField().setText("DIP_DD_"+extcode.toUpperCase()+"_");	
						}
						
					}
					//2011-6-29 ���� ���Ƶ�ʱ�򣬱��������ǰ�����Ƶ���������������ΨһԼ��������£��������ñ��޷����ճ��ñ�����
					UIRefPane pane=(UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getBodyItem("def_quotetable").getComponent();
					//���ֻ���յ�ǰϵͳ���Ѿ������,�������������������ȫ��ϵͳ�¶���ı������������ע�ͼ���
					//2011-6-29 ����Ҫ��������ϵͳ�µ��Ѿ�����ı�,�Լ�ԭʼ�Ĳ���������û�мӡ��Ƿ�ϵͳԤ������=Y�������԰Ѳ���������ˣ��������Ʊ�ϵͳ�µ�������ע����
					QuoteTableTreeRefModel model=new QuoteTableTreeRefModel();
					model.addWherePart(" and pk_datadefinit_h1 <> '"+tempNode.getNodeID()+"'");
					pane.setRefModel(model);
					if(hvo.getIsdeploy()==null||hvo.getIsdeploy().equals("N")){//���Ƿֲ�ϵͳ
						getSelfUI().getBillCardPanel().getHeadItem("isdeploy").setEnabled(false);
						getSelfUI().getBillCardPanel().getHeadItem("deploycode").setEnabled(false);
					}else{
						getSelfUI().getBillCardPanel().getHeadItem("isdeploy").setEnabled(true);
						getSelfUI().getBillCardPanel().getHeadItem("deploycode").setEnabled(true);
					}
			}
			
			}

		this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("iscreatetab").setValue(new UFBoolean(false));
	}
//	public void onBoMBCopy(AskMBDLG ask) throws Exception{
//		VOTreeNode tempNode = getSelectNode();
//		if(tempNode==null){
//			getSelfUI().showErrorMessage("��ѡ��Ҫ���ƵĽڵ㣡");
//			return ;
//		}
//		String str=(String)tempNode.getParentnodeID();
//		if(str ==null || str.equals("")){
//			getSelfUI().showWarningMessage("ϵͳ�ڵ㲻�������Ʋ�����"); 
//			return;
//		}else{
//			DipDatadefinitHVO defhvo=(DipDatadefinitHVO) tempNode.getData();
//			
//			if(defhvo!=null&&defhvo.getIsfolder().booleanValue()){
////				UIRefPane ref=new UIRefPane();
////				ContDataBussinessTableRefModel mo=new ContDataBussinessTableRefModel();
////				ref.setRefModel(mo);
////				ref.connEtoC1();
//				String selectPk=ask.getOrgnizeRefPnl().getRefValue("pk_datadefinit_h")==null?"":ask.getOrgnizeRefPnl().getRefValue("pk_datadefinit_h").toString();
//				if(selectPk!=null&&selectPk.trim().length()==20){
//						
//							DipDatadefinitHVO hvo=(DipDatadefinitHVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitHVO.class, selectPk);
//							SuperVO[] bvos=HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, " pk_datadefinit_h='"+selectPk+"' and nvl(dr,0)=0 ");
//							if(bvos!=null&&bvos.length>0){
//								for(int i=0;i<bvos.length;i++){
//								  bvos[i].setPrimaryKey(null);	
//								}	
//							}
//							onBoAdd(null);
//							getSelfUI().getBillCardPanel().getBillModel("dip_datadefinit_b").setBodyDataVO(bvos);
//								UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("memorytable").getComponent();
////								docu=jcp.getUITextField().getDocument();
//								if(hvo.getMemorytype().equals("��")){
//									if(defhvo.getPk_xt()!=null&&!defhvo.getPk_xt().equals("0001AA1000000001XQ1B")){
//										jcp.getUITextField().setDocument(new StringDocument(jcp.getUITextField(),"DIP_DD_"+extcode.toUpperCase()+"_"));
//										jcp.getUITextField().setText("DIP_DD_"+extcode.toUpperCase()+"_");	
//									}
//								}
//								UIRefPane pane=(UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getBodyItem("def_quotetable").getComponent();
//								//���ֻ���յ�ǰϵͳ���Ѿ������,�������������������ȫ��ϵͳ�¶���ı������������ע�ͼ���
//								//2011-6-29 ����Ҫ��������ϵͳ�µ��Ѿ�����ı�,�Լ�ԭʼ�Ĳ���������û�мӡ��Ƿ�ϵͳԤ������=Y�������԰Ѳ���������ˣ��������Ʊ�ϵͳ�µ�������ע����
//								QuoteTableTreeRefModel model=new QuoteTableTreeRefModel();
//								model.addWherePart(" and pk_datadefinit_h1 <> '"+selectPk+"'");
//								pane.setRefModel(model);
//								if(hvo.getIsdeploy()==null||hvo.getIsdeploy().equals("Y")){//���Ƿֲ�ϵͳ
//									getSelfUI().getBillCardPanel().getHeadItem("isdeploy").setEnabled(true);
//									getSelfUI().getBillCardPanel().getHeadItem("deploycode").setEnabled(true);
//								}else{
//									getSelfUI().getBillCardPanel().getHeadItem("isdeploy").setEnabled(false);
//									getSelfUI().getBillCardPanel().getHeadItem("deploycode").setEnabled(false);
//								}
//					this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("iscreatetab").setValue(new UFBoolean(false));
//				}
//			}else{
//				getSelfUI().showErrorMessage("Ҷ�ڵ㲻�ܸ��ƣ�");
//				return;
//			}
//		}
//		
//		
//	
//	}
	
	@Override
	protected void onBoCancel() throws Exception {
		//ȡ������"������"��ť����Ϊ����
//		if(docu!=null){
//			getSelfUI().getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(true);
//			UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("memorytable").getComponent();
//			jcp.getUITextField().setDocument(docu);
//			//jcp.getUITextField().setText("DIP_DD_"+extcode.toUpperCase()+"_");
//		}
		super.onBoCancel();
		getSelfUI().delstr ="";//ytq 2011-07-02 ȡ�����ɾ�л���
//		getSelfUI().onTreeSelectSetButtonState(getSelectNode());
//		getSelfUI().getBillListPanel().getHeadTable().changeSelection(selectrow, 0, false, false);
		getSelfUI().isEditBtn(false);
		getSelfUI().setTreeEnable(true);
		onCheckQuery();
		getSelfUI().getBillListPanel().getHeadTable().setEnabled(true);

//		VOTreeNode node=getSelectNode();
//		if(node!=null){
//			DipDatadefinitHVO hvo=(DipDatadefinitHVO) node.getData();
//			if(hvo.getIsfolder()!=null&&hvo.getIsfolder().booleanValue()){
//				getSelfUI().getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBillButton.Copy).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
//				
//			}else{
//				getSelfUI().getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//				
//			}
//			getSelfUI().updateButtonUI();
//		}
		
		
		
	}


	@Override
	protected void onBoLinePaste() throws Exception {
//		super.onBoLinePaste();
//		int currow=getSelfUI().getBillListPanel().getBodyTable().getSelectedRow();
//		DipDatadefinitCVO bodyValueRowVO = (DipDatadefinitCVO)getSelfUI().getBillListPanel().getBodyBillModel().getBodyValueRowVO(currow, DipDatadefinitCVO.class.getName());
//		int count = getSelfUI().getBillListPanel().getBodyTable().getRowCount();
//		getSelfUI().getBillListPanel().getBodyBillModel().addLine();
//		//����ճ����ʱ�ӱ�������Ϊ�գ����Ᵽ��ʱ��Υ��Ψһ�Դ���
//		getSelfUI().getBillListPanel().getBodyBillModel().setBodyRowVO(bodyValueRowVO, count);
//		getSelfUI().getBillListPanel().getBodyBillModel().setValueAt("", count, "pk_datadefinit_c");
	}
	
	@Override
	protected void onBoLineCopy() throws Exception {
		// TODO Auto-generated method stub
//		super.onBoLineCopy();
//		int selectedRow = getSelfUI() getBilllist.getBillTable().getSelectedRow();
//		if (selectedRow != -1) {
//			CircularlyAccessibleValueObject[] vos = getSelectedBodyVOs();
//			// getBillCardPanel().getBillData().getBillModel().getBodySelectedVOs(
//			// m_BodyVOClass.getName());
//			setCopyedBodyVOs(vos);
//
//		}
	}
	
	@Override
	protected void onBoLineIns() throws Exception {
		// TODO Auto-generated method stub
//		super.onBoLineIns();

	}
	
	@Override
	protected void onBoLinePasteToTail() throws Exception {
		// TODO Auto-generated method stub
//		super.onBoLinePasteToTail();
	}

	@Override
	protected void onBoLineDel() throws Exception {

		int countrow=getSelfUI().getBillListPanel().getBodyBillModel().getRowCount();
		if(countrow<0){
			return;
		}
 
		int rownos[]=getSelfUI().getBillListPanel().getBodyTable().getSelectedRows();
		if(rownos!=null&&rownos.length>0){
			int rowno=-1;
			for(int i=0;i<rownos.length;i++){
				rowno=rownos[i];	
				if(rowno>=0){
//					getSelfUI().showErrorMessage("��ѡ��Ҫ�������У�");
//					return;
//					String ob=getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(rowno, "pk_datadefinit_c")==null?"":getSelfUI().getBillListPanel().getBodyBillModel().getValueAt(rowno, "pk_datadefinit_c").toString();
//					if(ob==null||"".equalsIgnoreCase("pk_datadefinit_c")){
//						super.onBoLineDel();
//						getSelfUI().delstr = getSelfUI().delstr +",'"+ob.toString()+"'";
//					}else{
////						super.onBoLineDel();
////						
//						getSelfUI().delstr = getSelfUI().delstr +",'"+ob.toString()+"'";			
//						String sb=ob.toString();
//						System.out.println("ɾ��������"+sb+"��vo");
						if(i==rownos.length-1){
							getSelfUI().getBillListPanel().getBodyBillModel().delLine(rownos);	
//						}
					}
				}
				
			}
			
		}
		
	}	
	@Override
	protected void onBoLineAdd() throws Exception {
		int rowcount=getSelfUI().getBillListPanel().getBodyBillModel().getRowCount();
		getSelfUI().getBillListPanel().getBodyBillModel().addLine();	
		getSelfUI().getBillListPanel().getBodyBillModel().setEditRow(rowcount-1);
		
	}
	
	
	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
//		super.onBoRefresh();
		onTreeSelected(getSelfUI().getBillTreeSelectNode());
		getSelfUI().showHintMessage("ˢ�³ɹ�");
	}
	
	public void onBoMBZH()throws Exception{
//		 VOTreeNode tempNode = getSelectNode();
//		 if(tempNode==null){
//			 getSelfUI().showErrorMessage("ѡ��ڵ����ݴ���");
//			 return;
//		 }
//		 DipDatadefinitHVO hvo=(DipDatadefinitHVO) tempNode.getData();
//		 AskMBDLG ask=null;
//		 if(hvo!=null&&hvo.getPk_xt().equals("0001AA1000000001XQ1B")){
//			 ask=new AskMBDLG(getSelfUI(),null,"ģ��","        ��ѡ���������?",new String[]{"�����ⲿ�ļ����ݶ����ʽ","�����ⲿ�ļ����ݶ����ʽ","����NC�����ֵ��Զ��������ݶ����ʽ","�������ݿ�������������ݶ����ʽ","�������ݶ��帴�Ƹ�ʽ"});
//			 ask.showModal();
//			 int result=ask.getRes();
//				if(result==0){
//					onBoImport();
//				}else if(result==1){
//					onBoExport();
//				}else if(result==2){
//					onBoAutoBuiltDatadefinit();
//				}else if(result==3){
//					onBoPhysicTable();
//				}else if(result==4){
//					onBoMBCopy(ask);
//				}
//		 }else{
//			  ask=new AskMBDLG(getSelfUI(),null,"ģ��","        ��ѡ���������?",new String[]{"�����ⲿ�ļ����ݶ����ʽ","�����ⲿ�ļ����ݶ����ʽ","�������ݿ�������������ݶ����ʽ","�������ݶ��帴�Ƹ�ʽ"});
//			  ask.showModal();
//			  int result=ask.getRes();
//				if(result==0){
//					onBoImport();
//				}else if(result==1){
//					onBoExport();
//				}else if(result==2){
//					onBoPhysicTable();
//				}else if(result==3){
//					onBoMBCopy(ask);
//				}
//		 }
		 
		
	}
	private void onBoPhysicTable() throws Exception {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		 VOTreeNode tempNode = getSelectNode();
			if(tempNode==null){
				getSelfUI().showErrorMessage("��ѡ��Ҫ�����Ľڵ㣡");
				return ;
			}
			String str=(String)tempNode.getParentnodeID();
			if(str ==null || str.equals("")){
				getSelfUI().showWarningMessage("ϵͳ�ڵ㲻��������������"); 
				return;
			}else{
				DipDatadefinitHVO defhvo=(DipDatadefinitHVO) tempNode.getData();

				if(defhvo==null){
					getSelfUI().showErrorMessage("�ýڵ����ݴ�����ˢ�º��ڲ�����");
					return;
				}
				if(defhvo.getIsfolder().booleanValue()){
					UIRefPane ref=new UIRefPane();
					PhysicTableRefModel mo=new PhysicTableRefModel();
					ref.setRefModel(mo);
					ref.connEtoC1();
					String tablename=ref.getRefValue("table_name")==null?"":ref.getRefValue("table_name").toString();
					
					if(tablename!=null&&tablename.trim().length()>0){
						PhysicTableVO tablevos[]=null;
//						tablevos=(PhysicTableVO[])HYPubBO_Client.queryByCondition(PhysicTableVO.class, "TABLE_NAME='"+tablename+"'" );
						String sqlt=" select column_name ,data_type ,data_length ,data_precision , data_Scale,nullable,data_default  " +
							 	   " from user_tab_columns where table_name='"+tablename+"'";
						List listcolumns=iq.queryVOBySql(sqlt, new PhysicTableVO());//���б��ֶΡ�
						List listPk=iq.queryfieldList("select ss.constraint_name from user_constraints ss where  ss.table_name='"+tablename+"' and ss.constraint_type='P'");
						String pkcolum="";
						if(listPk!=null&&listPk.size()==1){
							pkcolum=listPk.get(0)==null?"":listPk.get(0).toString();
						}
						if(listcolumns!=null&&listcolumns.size()>0){
							DipDatadefinitBVO bvos[]=new DipDatadefinitBVO[listcolumns.size()];
							for(int i=0;i<listcolumns.size();i++){
								PhysicTableVO tablevo=(PhysicTableVO) listcolumns.get(i);
								bvos[i]=new DipDatadefinitBVO();
								bvos[i].setEname(tablevo.getColumn_name());
								String type=tablevo.getData_type();
								bvos[i].setDefaultvalue(tablevo.getData_default());
								bvos[i].setIsimport(new UFBoolean(!tablevo.getNullable().booleanValue()));
								if(type.equals("NUMBER")){
									if(tablevo.getData_precision()==0){
										bvos[i].setType("INTEGER");	
									}else{
										bvos[i].setType(type);
									}
									bvos[i].setDeciplace(tablevo.getData_Scale());
									bvos[i].setLength(tablevo.getData_precision());	
									
								}else{
									if(type.contains("VARCHAR")){
										bvos[i].setType("VARCHAR");
									}else{
										bvos[i].setType(type);
									}
										bvos[i].setLength(tablevo.getData_length());
								}
								if(pkcolum.length()>0&&pkcolum.equals(tablevo.getColumn_name())){
									bvos[i].setIspk(new UFBoolean(true));
									bvos[i].setIsimport(new UFBoolean(true));
								}
							}	
							onBoAdd(null);
							getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyDataVO(bvos);
						}
						
					}
				}else{
					showErrorMessage("Ҷ�ڵ㲻�����Ĳ���");
					return;
				}
				
			}
		
	
		
	}

	private void onBoAutoBuiltDatadefinit() throws Exception{
		// TODO Auto-generated method stub
		 VOTreeNode tempNode = getSelectNode();
			if(tempNode==null){
				getSelfUI().showErrorMessage("��ѡ��Ҫ�����Ľڵ㣡");
				return ;
			}
			String str=(String)tempNode.getParentnodeID();
			if(str ==null || str.equals("")){
				getSelfUI().showWarningMessage("ϵͳ�ڵ㲻��������������"); 
				return;
			}else{
				DipDatadefinitHVO defhvo=(DipDatadefinitHVO) tempNode.getData();

				if(defhvo==null){
					getSelfUI().showErrorMessage("�ýڵ����ݴ�����ˢ�º��ڲ�����");
					return;
				}
				if(defhvo.getIsfolder().booleanValue()){
					UIRefPane ref=new UIRefPane();
					PubDatadictTreeRefModel mo=new PubDatadictTreeRefModel();
					ref.setRefModel(mo);
					ref.connEtoC1();
					String tablename=ref.getRefValue("id")==null?"":ref.getRefValue("id").toString();
					String display=ref.getRefValue("display")==null?"":ref.getRefValue("display").toString();
					if(tablename!=null&&tablename.trim().length()>0){
						String sqlexist=" select syscode from  dip_datadefinit_h  where memorytable='"+tablename+"' and nvl(dr,0)=0" ;
						String value=iq.queryfield(sqlexist);
						if(value!=null&&value.trim().length()>0){
							showErrorMessage("��"+tablename+"���ݶ����Ѿ����ڣ�������"+value);
							return;
						}
						List<DipDatadefinitBVO> list=getDataDictVOS(tablename);
						if(list!=null&&list.size()>0){
							DipDatadefinitHVO hvo=new DipDatadefinitHVO();
							hvo.setBusicode(tablename.toUpperCase());
							hvo.setSyscode(tablename);
							hvo.setSysname(display);
							hvo.setDatatype("0001BB100000000JJOKC");//0001BB100000000JJOKC ������Ϣ�ṹ
							hvo.setMemorytable(tablename.toUpperCase());
							hvo.setMemorytype("��");
							hvo.setIscreatetab("Y");
							hvo.setTabsoucetype("�Զ���");
							hvo.setIsfolder(new UFBoolean("N"));
							hvo.setPk_xt(defhvo.getPk_xt());
							hvo.setPk_sysregister_h(defhvo.getPk_datadefinit_h());
							hvo.setIsdeploy(new UFBoolean("N"));
							String newpk=HYPubBO_Client.insert(hvo);
							if(newpk!=null&&newpk.length()==20){
								hvo.setPk_datadefinit_h(newpk);
								DipDatadefinitBVO bvos[]=list.toArray(new DipDatadefinitBVO[0]);
								if(bvos!=null&&bvos.length>0){
									String pk=defhvo.getPk_datadefinit_h();
									if(pk!=null&&pk.trim().length()>0){
									 String sql="update dip_datadefinit_b set dr=1 where pk_datadefinit_h='"+newpk+"'"+"and nvl(dr,0)=0 " ;
									 iq.exesql(sql);
									  for(int i=0;i<bvos.length;i++){
										  bvos[i].setPk_datadefinit_h(newpk);
									  }
									  HYPubBO_Client.insertAry(bvos);
//									  SuperVO[] supvos=HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, "pk_datadefinit_h='"+newpk+"' and nvl(dr,0)=0 ");
//									  if(supvos!=null&&supvos.length>0){
//										  getSelfUI().getBillCardPanel().getBillModel("dip_datadefinit_b").setBodyDataVO(supvos) ;
//									  }
									  
//									  if (getUITreeCardController().isAutoManageTree()) {			
//											getSelfUI().insertNodeToTree(hvo);
//										}
										
										getSelfUI().onTreeSelectSetButtonState(getSelectNode());
									  if(errType.length()>0){
										  showErrorMessage("���������ǡ�"+errType.substring(0, errType.length()-1)+"�����ֶ�ȡ�������ʹ������ó�Ĭ������varchar");
									  }
										
									}
								}
							}else{
								showErrorMessage("�������ݶ������������ˢ�º�����");
							}
							
						}else{
							showErrorMessage("�����ֵ���û�в鵽��"+tablename+"����Ϣ");
						}
					}
				}else{
					showErrorMessage("Ҷ�ڵ㲻�����Ĳ���");
					return;
				}
				
			}
		
	}

	final  String  EXPSTRINGNAME="��������,Ӣ������,����,����,С��λ,Ĭ��ֵ,����,����,Ԥ������,ΨһԼ��,����,���ñ�,������";
	final  String  IMPSTRINGCODE="cname,ename,type,length,deciplace,defaultvalue,isimport,ispk,issyspk,isonlyconst,isquote,def_quotetable,quotecolu";
   @Override
	protected void onBoExport() throws Exception {
		// TODO Auto-generated method stub
		   VOTreeNode tempNode = getSelectNode();
			if(tempNode==null){
				getSelfUI().showErrorMessage("��ѡ��Ҫ�����Ľڵ㣡");
				return ;
			}
			String str=(String)tempNode.getParentnodeID();
			if(str ==null || str.equals("")){
				getSelfUI().showWarningMessage("ϵͳ�ڵ㲻��������������"); 
				return;
			}else{
				DipDatadefinitHVO defhvo=(DipDatadefinitHVO) tempNode.getData();

				if(defhvo==null){
					getSelfUI().showErrorMessage("�ýڵ����ݴ�����ˢ�º��ڲ�����");
					return;
				}
				if(defhvo.getIsfolder().booleanValue()){
					getSelfUI().showErrorMessage("�ļ��нڵ㲻��������������");
					return;
				}else{
					String pk_datadefinit_h=defhvo.getPk_datadefinit_h();
					String tablename=defhvo.getMemorytable()==null?"sheet1":defhvo.getMemorytable()+(defhvo.getSysname()==null?"":defhvo.getSysname());
					DipDatadefinitBVO datadefinitBvos[]=null;
					datadefinitBvos=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, " pk_datadefinit_h='"+pk_datadefinit_h+"' and nvl(dr,0)=0 ");
					if(datadefinitBvos!=null&&datadefinitBvos.length>0){
						ExpExcelVO expExcelvos[]=new ExpExcelVO[datadefinitBvos.length];
						for(int i=0;i<datadefinitBvos.length;i++){
							DipDatadefinitBVO bvo=datadefinitBvos[i];
							if(bvo!=null){
								expExcelvos[i]=new ExpExcelVO();
								expExcelvos[i].setAttributeValue("line1",bvo.getCname()==null?"":bvo.getCname());
								expExcelvos[i].setAttributeValue("line2",bvo.getEname()==null?"":bvo.getEname());
								expExcelvos[i].setAttributeValue("line3",bvo.getType()==null?"":bvo.getType());
								expExcelvos[i].setAttributeValue("line4",bvo.getLength()==null?"":bvo.getLength());
								expExcelvos[i].setAttributeValue("line5",bvo.getDeciplace()==null?"":bvo.getDeciplace());
								expExcelvos[i].setAttributeValue("line6",bvo.getDefaultvalue()==null?"":bvo.getDefaultvalue());
								expExcelvos[i].setAttributeValue("line7",bvo.getIsimport()==null?"":bvo.getIsimport());
								expExcelvos[i].setAttributeValue("line8",bvo.getIspk()==null?"":bvo.getIspk());
								expExcelvos[i].setAttributeValue("line9",bvo.getIssyspk()==null?"":bvo.getIssyspk());
								expExcelvos[i].setAttributeValue("line10",bvo.getIsonlyconst()==null?"":bvo.getIsonlyconst());
								expExcelvos[i].setAttributeValue("line11",bvo.getIsquote()==null?"":bvo.getIsquote());
								if(bvo.getIsquote()!=null&&bvo.getIsquote().booleanValue()){
									if(bvo.getQuotetable()!=null&&bvo.getQuotetable().trim().length()>0){
									String sql="select  h.memorytable from dip_datadefinit_h h where h.pk_datadefinit_h in( select pk_datadefinit_h from dip_datadefinit_b b where b.pk_datadefinit_b ='"+bvo.getQuotetable()+"' and nvl(dr,0)=0 ) and nvl(h.dr,0)=0 ";
									String quoteTablename=iq.queryfield(sql);
										if(quoteTablename!=null&&quoteTablename.length()>0){
											expExcelvos[i].setAttributeValue("line12",quoteTablename);
											expExcelvos[i].setAttributeValue("line13",bvo.getQuotecolu()==null?"":bvo.getQuotecolu());
										}else{
											expExcelvos[i].setAttributeValue("line12","");
											expExcelvos[i].setAttributeValue("line13","");
										}
								    }else{
										expExcelvos[i].setAttributeValue("line12","");
										expExcelvos[i].setAttributeValue("line13","");
								    }
								}else{
									expExcelvos[i].setAttributeValue("line12","");
									expExcelvos[i].setAttributeValue("line13","");
								}
						}
					   }
						JFileChooser jfileChooser = new JFileChooser();
						jfileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
						if(jfileChooser.showSaveDialog(this.getSelfUI()) == javax.swing.JFileChooser.CANCEL_OPTION){
							return;
						}else{
							String file=jfileChooser.getSelectedFile().toString();
							if(!file.endsWith(".xls")){
								file=file+".xls";
							}
							File jfile=new File(file);
							if(jfile.exists()){
								if(2==MessageDialog.showOkCancelDlg(this.getBillUI(), "��ʾ", file+"�ļ��Ѿ����ڣ��Ƿ񸲸�")){
									return;
								}
							}
							ExpToExcel exp=new ExpToExcel(file,tablename,EXPSTRINGNAME,expExcelvos,null);
							this.getSelfUI().showWarningMessage("�����ɹ�");
						}
					}
				}
			}
		 
	}
   @Override
	protected void onBoImport() throws Exception {
		// TODO Auto-generated method stub
	   
	   String []ss={"BLOB","BINARY_DOUBLE","BINARY_FLOAT","CHAR","CLOB","DATE","INTEGER","INTERVAL","LONG","LONGRAW","NCLOB","NUMBER","NVARCHAR","NVARCHAR2","RAW","TIMESTAMP","VARCHAR"};
	    Map<String, String> styleMap=new HashMap<String, String>();
	    for(int i=0;i<ss.length;i++){
	    	styleMap.put(ss[i], "Y");
	    }
	    VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ����Ľڵ㣡");
			return ;
		}
		String str=(String)tempNode.getParentnodeID();
		if(str ==null || str.equals("")){
			getSelfUI().showWarningMessage("ϵͳ�ڵ㲻�������������"); 
			return;
		}else{
			DipDatadefinitHVO defhvo=(DipDatadefinitHVO) tempNode.getData();
			if(defhvo==null){
				getSelfUI().showErrorMessage("�ýڵ����ݴ�����ˢ�º��ڲ�����");
				return;
			}
			if(defhvo.getIsfolder().booleanValue()){
				//getSelfUI().showErrorMessage("�ļ��нڵ��������������");
				JFileChooser jfileChooser = new JFileChooser();
				jfileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
				if(jfileChooser.showSaveDialog(this.getSelfUI()) == javax.swing.JFileChooser.CANCEL_OPTION){
					return;
				}else{
					String file=jfileChooser.getSelectedFile().toString();
					if(file!=null&&file.length()>0){
						if(!file.endsWith(".xls")){
							file=file+".xls";
							}
							TxtDataVO tvo=getTxtDataVO(file,EXPSTRINGNAME,IMPSTRINGCODE,DipDatadefinitBVO.class);
							String[] notNullLine=new String[]{"cname","ename","type"};
							if(tvo!=null){
								Vector vec=tvo.getDatavo();
								if(vec!=null&&vec.size()>0){
									List<DipDatadefinitBVO> displayList=new ArrayList<DipDatadefinitBVO>();//������ȷvo��Ҫ��ʾ�������vo
									DipDatadefinitBVO bvos[]=new DipDatadefinitBVO[vec.size()];
										vec.toArray(bvos);
										if(bvos!=null&&bvos.length>0){
											StringBuffer sb=new StringBuffer();
											Map enameMap=new HashMap();
											Map cnameMap=new HashMap();
											for(int i=0;i<bvos.length;i++){
											    sb=new StringBuffer();
												boolean flag=false;
												DipDatadefinitBVO bvo=bvos[i];
												if(bvo!=null){
													//һ�㲻��Ϊ��У��
													HashMap map=tvo.getCodeToNameMap();
													if(notNullLine!=null&&notNullLine.length>0){
														for(int w=0;w<notNullLine.length;w++){
															String notnullcode=notNullLine[w];
															Object notNullCodeValue=bvo.getAttributeValue(notnullcode);
															if(notNullCodeValue==null||notNullCodeValue.toString().trim().equals("")){
																
																if(map!=null&&map.get(notnullcode)!=null){
																	sb.append(map.get(notnullcode)+"�ֶβ���Ϊ��");
																	break;
																}
																
															}else{
																if(notnullcode.equals("ename")){
																	if(enameMap.get(notNullCodeValue)==null){
																		enameMap.put(notNullCodeValue, i+1);	
																	}else{
																		sb.append("��"+enameMap.get(notNullCodeValue)+"�к͵�"+(i+1)+"��"+map.get(notnullcode)+"�ֶ��ظ�");
																	}
																}
																if(notnullcode.equals("cname")){
																	if(cnameMap.get(notNullCodeValue)==null){
																		cnameMap.put(notNullCodeValue, i+1);	
																	}else{
																		sb.append("��"+cnameMap.get(notNullCodeValue)+"�к͵�"+(i+1)+"��"+map.get(notnullcode)+"�ֶ��ظ�");
																	}
																}
																
															}	
														}
															
													}
													if(styleMap!=null){
														String type=bvo.getType();
														if(!(type!=null&&styleMap.get(type)!=null)){
															sb.append("�����ֶβ�����"+type);
														}
													}
													
													//����У��У��
													if(sb.length()<=0&&bvo.getIsquote()!=null&&bvo.getIsquote().booleanValue()){
														String def_quotetable=bvo.getDef_quotetable();
														String quotecolu=bvo.getQuotecolu();
														if(def_quotetable!=null&&def_quotetable.trim().length()>0){
															String sql=" select h.pk_datadefinit_h from Dip_Datadefinit_h h where h.memorytable='"+def_quotetable+"' and h.iscreatetab='Y' and nvl(h.dr,0)=0 ";
															List list=iq.queryfieldList(sql);
															if(list!=null&&list.size()>0){
																if(list.size()==1){
																	String value=list.get(0)==null?"":list.get(0).toString();
																	if(value.trim().length()>0){
																		if(quotecolu!=null&&quotecolu.trim().length()>0){
																		DipDatadefinitBVO queryBvo[]=null;
																		queryBvo=(DipDatadefinitBVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitBVO.class, " pk_datadefinit_h='"+value+"' and ename='"+quotecolu+"' and nvl(dr,0)=0 ");
																			if(queryBvo!=null&&queryBvo.length>0){
																				if(queryBvo.length==1){
																					DipDatadefinitBVO bbvo=queryBvo[0];
																					if(bbvo!=null&&((bbvo.getIspk()!=null&&bbvo.getIspk().booleanValue())||(bbvo.getIsonlyconst()!=null&&bbvo.getIsonlyconst().booleanValue()))){
																						flag=true;
																						bvos[i].setDef_quotetable(def_quotetable);
																						bvos[i].setQuotetable(bbvo.getPk_datadefinit_b());
																					}else{
																						sb=sb.append("���ñ�"+def_quotetable+"��"+quotecolu+"�ֶ�û��Ψһ��ʶ");
																					}
																				}else{
																					//�ñ���ֶδ��ڶ����
																					sb=sb.append("���ñ�"+def_quotetable+"�ظ�����"+quotecolu+"�ֶ�");
																				}
																			}else{
																				//û�и��ֶ�
																				sb=sb.append("���ñ�"+def_quotetable+"������"+quotecolu+"�ֶ�");
																			}
																		}
																	}else{
																		sb=sb.append("���ñ�"+def_quotetable+"������");
																	}
																	
																}else{
																	//����һ��
																	sb=sb.append("���ñ�"+def_quotetable+"����"+list.size()+"��");
																}
															}else{
																//�����ڸñ� 
																sb=sb.append("���ñ�"+def_quotetable+"������");
															}
															
														}else{
															//û�����ñ�
															sb=sb.append("���ñ�"+def_quotetable+"������");
														}
														
														
													}
												}
												
												if(!flag){
													bvos[i].setIsquote(new UFBoolean(false));
													bvos[i].setDef_quotetable(null);
													bvos[i].setQuotecolu(null);
													bvos[i].setQuotetable(null);
												}
												
												if(sb!=null&&sb.toString().trim().length()>0){
													RowDataVO vo=tvo.getData()[i];
													int w=tvo.getColCount();
													vo.setAttributeValue(w+"", sb);
													tvo.getErrList().add(vo);
													
												}else{
													displayList.add(bvos[i]);
												}
											}
										}
										String errPath="";
									if(tvo.getErrList()!=null&&tvo.getErrList().size()>0){
//										ExpToExcel exp=new ExpToExcel();
										 errPath=file.replace(".xls", "-err.xls");
										tvo.getErrList().toArray(new RowDataVO[0]);
										List<RowDataVO []> vosList=new ArrayList<RowDataVO[]>();
										RowDataVO[] rowvo=tvo.getErrList().toArray(new RowDataVO[0]);
										vosList.add(rowvo);
										ExpToExcel1 toexcel=new ExpToExcel1(errPath,new String[]{tvo.getSheetName()},new String[]{tvo.getErrorTitle()},vosList,null);
										toexcel.create();
									}	
										onBoAdd(null);
										if(displayList!=null&&displayList.size()>0){
											SuperVO supervos[]=displayList.toArray(new SuperVO[0]);
											getSelfUI().getBillCardPanel().getBillModel("dip_datadefinit_b").setBodyDataVO(supervos);	
										}
										
										if(errPath.length()>0){
											getSelfUI().showErrorMessage("��鿴������Ϣ��"+errPath+"��");	
										}
								}
								
							//}
							
							
						}	
					 }
				}
					
					
			}else{
				getSelfUI().showErrorMessage("���ļ��нڵ㲻�������������");
				return;
			}
		}
		
	}
   
   /**
    * �õ�ѡ��·���µ�excel��vo����TxtDataVO��
    * @param path
    * @return
    */
   public TxtDataVO getTxtDataVO(String path,String expTitleName,String impTitleCode,Class classvo){
		TxtDataVO tvo=new TxtDataVO();
		FileInputStream fin=null;
		HSSFWorkbook book=null;
		try{
			fin=new FileInputStream(path);
			book = new HSSFWorkbook(fin);
			HSSFSheet sheet = book.getSheetAt(0);
			if(sheet == null){
				showErrorMessage("�����ļ���ʽ����ȷ��");
				return null;
			}
			String sheetName=book.getSheetName(0);
			if(sheetName==null||sheetName.trim().equals("")){
				showErrorMessage("�����ļ���ʽ����ȷ��");
				return null;
			}
			//tvo.setTableName(sheetName);
			tvo.setSheetName(sheetName);
			tvo.setStartRow(sheet.getFirstRowNum());
			tvo.setStartCol(sheet.getLeftCol());
			tvo.setRowCount(sheet.getPhysicalNumberOfRows() - 1);//ȥ��������
			tvo.setColCount((short)sheet.getRow(sheet.getFirstRowNum()).getPhysicalNumberOfCells());
			tvo.setFirstDataRow(sheet.getFirstRowNum());
			tvo.setFirstDataCol(sheet.getLeftCol());

			HSSFRow titleRow = sheet.getRow(tvo.getStartRow());
			HashMap<String, String> titlemap = new HashMap<String, String>();
			for(short i=0;i<titleRow.getPhysicalNumberOfCells();i++){
				titlemap.put(Short.toString(i),(String)SJUtil.getCellValue(titleRow.getCell(i)));
			}
			tvo.setTitlemap(titlemap);
			String codes[]=impTitleCode.split(",");
			String names[]=expTitleName.split(",");//����name
			tvo.setErrorTitle(expTitleName+",����");
			HashMap<String,String> lineToCode=new HashMap<String, String>();
			HashMap<String,String> codeToName=new HashMap<String, String>();
			HashMap<String,String> nameToCode=new HashMap<String, String>();
			Vector<SuperVO> datavos=new Vector<SuperVO>();
			if(names!=null&&codes!=null&&codes.length==names.length){
				for(int w=0;w<names.length;w++){
					if(names[w]!=null&&!names[w].trim().equals("")&&codes[w]!=null&&!codes[w].trim().equals("")){
						nameToCode.put(names[w], codes[w]);
						codeToName.put(codes[w], names[w]);
					}
				}
			}else{
				showErrorMessage("����excel�ֶγ��Ⱥ͵����ֶγ��Ȳ���ȣ�");
				return null;
			}
			if(titlemap!=null&&titlemap.size()>0&&codes!=null&&codes.length==titlemap.size()){
				for(int i=0;i<titlemap.size();i++){
					String impName="";
					if(titlemap.get(i)==null){
					   impName=titlemap.get(i+"");//����name
					  if(impName==null||impName.trim().equals("")){
						  getSelfUI().showErrorMessage("�����ֶ����Ʋ���Ϊ�գ�");
						  return null;
					  }
					  if(names.length<i){
						  showErrorMessage("����excel�ֶδ���");
						  return null;
					  }else{
						 if(!(names[i]!=null&&names[i].equals(impName))){
							 showErrorMessage("��"+(i+1)+"�������ֶ����͵����ֶ�������ͬ��");
							 return null;
						 }
					  }
					}
				 	if(nameToCode!=null){
				 		if(!impName.equals("")&&nameToCode.get(impName)!=null&&!nameToCode.get(impName).trim().equals("")){
				 			lineToCode.put(i+"", nameToCode.get(impName));
				 		}else{
				 			showErrorMessage("�����ֶ�["+impName+"]û���ҵ���Ӧ���룡");
				 			return null;
				 		}
				 	}else{
				 		showErrorMessage("�����ֶ�û���ҵ���Ӧ���룡");
				 		return null;
				 	}
					
				}
			}else{
				showErrorMessage("����excel�ֶγ��Ⱥ͵����ֶγ��Ȳ���ȣ�");
				return null;
			}
			tvo.setLinetocodeMap(lineToCode);
			tvo.setNametocodeMap(nameToCode);
			tvo.setCodeToNameMap(codeToName);
			for(int i=1;i<=tvo.getRowCount();i++){
				HSSFRow row = sheet.getRow(tvo.getFirstDataRow() + i);
				SuperVO supervo=(SuperVO) Class.forName(classvo.getName()).newInstance();
				for(short j=0;j<titleRow.getPhysicalNumberOfCells();j++){
					tvo.setCellData(i-1, j, SJUtil.getCellValue(row.getCell(j)));
					if(lineToCode.get(j+"")!=null){
						String field=lineToCode.get(j+"");
						Class<Field> fiel=null;
						String ss="";
						try{
						 fiel=Class.forName(classvo.getName()).getDeclaredField(field)==null?null:(Class<Field>) Class.forName(classvo.getName()).getDeclaredField(field).getType();
							ss=SJUtil.getCellValue(row.getCell(j))==null?"":SJUtil.getCellValue(row.getCell(j)).toString();
							if(field!=null&&(field.equals("ename")||field.equals("type"))){
								ss=ss.trim().toUpperCase();
							}else{
								ss=ss.trim();
							}
//							if(checkline!=null&&checkline.length>0){
//								for(int c=0;c<checkline.length;c++){
//									if(c==j&&ss.trim().equals("")){
//										
//									}
//								}
//							}
						}catch(java.lang.NoSuchFieldException e){
							fiel=null;
						}
						if(fiel==null||fiel.toString().equals("class java.lang.String")){
							supervo.setAttributeValue(field, ss);	
						}else if(fiel.toString().equals("class java.lang.Integer")){
						
							int l=ss.length();
							if(l==0){
								ss="0";	
							}else{
								if(!ss.matches("[0-9]{1,"+l+"}")){
									ss="0";
								}
							}
							supervo.setAttributeValue(field, Integer.parseInt(ss));	
						}else if(fiel.toString().equals("class nc.vo.pub.lang.UFBoolean")){
							UFBoolean bol=null;
							if(ss.equals("Y")){
								bol=new UFBoolean(true);
							}else{
								bol=new UFBoolean(false);
							}
							supervo.setAttributeValue(field, bol);	
						}else{
							supervo.setAttributeValue(field, ss);	
						}
							
					}else{
						showErrorMessage("��"+j+"�У�û���ҵ���Ӧ�ı����ֶΣ�");
						return null;
					}
				}
				datavos.add(i-1, supervo);
			}
			if(datavos!=null){
				tvo.setDatavo(datavos);
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(fin!=null){try {
				fin.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
		}
		return tvo;
	}
   public void volidateTitleMap(HashMap<String,String> titleMap){
	   if(titleMap!=null){
		   
	   } 
   }
   
   public void showErrorMessage(String message){
	   MessageDialog.showErrorDlg(getSelfUI(), "����", message);
   }
   StringBuffer errType;
      public List<DipDatadefinitBVO> getDataDictVOS(String tablename){
    	
	   List<DipDatadefinitBVO> list=new ArrayList<DipDatadefinitBVO>();
	   errType=new StringBuffer();
	   try {
		if(tablename!=null&&!tablename.trim().equals("")&&!tablename.startsWith("dip_")){
			if(!tablename.contains("_")){
				showErrorMessage("������������»���");
				return null;
			}
			if(tablename.startsWith("_")){
				showErrorMessage("������ͷ�������»���");
				return null;
			}
			String parent=tablename.substring(0, tablename.indexOf("_"));
			ObjectNode node=new FolderNode();
			node.setID(tablename.toLowerCase());
			node.setGUID(tablename.toLowerCase());
			node.setParentGUID(parent.toLowerCase());
			node.setKind("table");
			IBizObjStorage storage = (IBizObjStorage) NCLocator.getInstance().lookup(
					IBizObjStorage.class.getName());
			Object obj=storage.loadObject(IContrastUtil.DESIGN_CON, "nc.bs.pub.ddc.datadict.DatadictStorage", node);
			if(obj!=null&&obj instanceof TableDef){
				TableDef def=(TableDef) obj;
				FieldDefList deflist=def.getFieldDefs();
				if(deflist!=null){
				 int count=deflist.getCount();
				 for(int i=0;i<count;i++){
					 DipDatadefinitBVO bvo=new DipDatadefinitBVO();
					
					 FieldDef fieldDef= (FieldDef) deflist.get(i);
					 String cname=fieldDef.getDisplayName();//��������
					 String ename=fieldDef.getID();//Ӣ������
					 int type=-1;
					 type=fieldDef.getDataType();//����
					 if(cname==null||cname.trim().equals("")||ename==null||ename.trim().equals("")||type==-1){
						 showErrorMessage("�����ֵ�ȡ������");
						 return null;
					 }else{
						 bvo.setCname(cname);
						 bvo.setEname(ename.toUpperCase());
						 String tt=getType(type);
						 if(tt.equals("")){
							 tt="VARCHAR";
							 errType.append(cname+",");
						 }
						 bvo.setType(tt);
					 }
					 int length=fieldDef.getLength();// ����
					 if(length!=0){
						 bvo.setLength(length);
					 }
					 int precision=fieldDef.getPrecision();//С��
					 if(precision!=0){
						 bvo.setDeciplace(precision);
					 }
					 boolean isnull=fieldDef.isNull();//�Ƿ�����Ϊ��
					 if(!isnull){
						 bvo.setIsimport(new UFBoolean(true));//�Ƿ����
					 }
					 boolean isPrimary=fieldDef.getIsPrimary();//�Ƿ�����
					 if(isPrimary){
						 bvo.setIspk(new UFBoolean(true));
					 }
					 list.add(bvo);
				 }	
				}
				
			}
		}
	} catch (BusinessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return list;
   }
   public String getType(int style){
	   String ss="";
	   switch(style){
	    case 1:ss="CHAR";break;
	    case 2:ss="NUMBER";break;
	    case 3:ss="NUMBER";break;
	    case 4:ss="INTEGER";break;
        case 5:ss="INTEGER";break;//SMALLINT
        case 12:ss="VARCHAR";break;
        case 2240:ss="BLOB";break;
	   }
	   return ss;
   }
   
}