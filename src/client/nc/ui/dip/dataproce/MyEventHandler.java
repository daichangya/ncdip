package nc.ui.dip.dataproce;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.ILogAndTabMonitorSys;
import nc.itf.dip.pub.checkplugin.ICheckPlugin;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.exception.DbException;
import nc.ui.bd.ref.DataDefinitTableTreeRefModel;
import nc.ui.bd.ref.ProcessStyleRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.control.ControlDlg;
import nc.ui.dip.datarec.DatarecDlg;
import nc.ui.dip.dlg.AddFolderDlg;
import nc.ui.dip.dlg.movefoleder.MovefolderDLG;
import nc.ui.dip.warningset.WarningsetDlg;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.IBillItem;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.CheckMessage;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.control.ControlHVO;
import nc.vo.dip.control.ControlVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.dataproce.DipDataproceBVO;
import nc.vo.dip.dataproce.DipDataproceHVO;
import nc.vo.dip.dataproce.MyBillVO;
import nc.vo.dip.dataverify.DataverifyBVO;
import nc.vo.dip.dataverify.DataverifyHVO;
import nc.vo.dip.sysregister.DipSysregisterHVO;
import nc.vo.dip.warningset.DipWarningsetBVO;
import nc.vo.dip.warningset.DipWarningsetVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.pub.IExAggVO;
import nc.vo.trade.summarize.Hashlize;
import nc.vo.trade.summarize.VOHashPrimaryKeyAdapter;


/**
 *
 *������AbstractMyEventHandler�������ʵ���࣬
 *��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 *@author author
 *@version tempProject version
 */

public class MyEventHandler 
extends AbstractMyEventHandler{

	public static String extcode;
	public MyEventHandler(BillTreeCardUI clientUI, ICardController control) {
		super(clientUI, control);
	}

	/**
	 * ȡ�õ�ǰUI��
	 * 
	 * @return
	 */
	private DataProceUI getSelfUI() {
		return (DataProceUI) getBillUI();
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
	 * @return TODO
	 */
	protected CircularlyAccessibleValueObject processNewBodyVO(CircularlyAccessibleValueObject newBodyVO) {

		VOTreeNode parent = this.getSelectNode();

		// ����������д���ӱ���
		newBodyVO.setAttributeValue("pk_dataproce_h", parent == null ? null : parent.getNodeID());

		// ��ӽ���
		return newBodyVO;
	}
	private CircularlyAccessibleValueObject[] getChildVO(AggregatedValueObject retVo) {
		CircularlyAccessibleValueObject childVos[] = null;
		if (retVo instanceof IExAggVO)
			childVos = ((IExAggVO) retVo).getAllChildrenVO();
		else
			childVos = retVo.getChildrenVO();
		return childVos;
	}


	/**
	 * 
	 * <P>�˷���Ϊ���Ǹ��෽��
	 * <BR>�������е�ISingleController�ж�ȥ�������򱣴�ʱ����ͷΪ��
	 * @throws Exception
	 * @see nc.ui.trade.treecard.TreeCardEventHandler#onBoSave()
	 */
	protected void onBoSave() throws Exception {
		//��ȡ��ǰ���ݵ����� zgy
//		liyunzhe 2011-06-04 start
		if(isAdding()){
			getSelfUI().FF=1;
		}else{
			getSelfUI().FF=0;
		}
		UIRefPane pane=(UIRefPane)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("firsttab").getComponent();
		String[] arrpk=pane.getRefPKs();
		String data=null;
		if(arrpk!=null&&arrpk.length>0){
			for(int i=0;i<arrpk.length;i++){
				if(i==0){
					data=arrpk[i];
				}else{
					data=data+","+arrpk[i];
				}
			}
		}
		//liyunzhe 2011-06-04 end
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd !=null){
			bd.dataNotNullValidate();
		}
		//��ȡ��ǰui����������������zgy
		DataProceUI ui=(DataProceUI) getBillUI();
		//�жϱ��롢ҵ���ʶ�ڵ�ǰϵͳ��Ψһ��
		VOTreeNode node=getSelectNode();
		if(node==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ�����Ľڵ㣡");
			return ;
		}
		DipDataproceHVO hho=(DipDataproceHVO) node.getData();
		String pkxt=hho.getPk_xt();
		String fp=(String) node.getParentnodeID();			
		//�����pkΪ�գ���ѽڵ�pk����fpk����������ʱ���������ظ��ı��뻹���Ա���
//		if(fp==null||fp.trim().equals("")){
//			fp=(String) node.getNodeID();
//		}
		String  pk=(String) ui.getBillCardPanel().getHeadItem("pk_dataproce_h").getValueObject();
		String  fpk=(String) ui.getBillCardPanel().getHeadItem("fpk").getValueObject();
		//�жϵ�ǰui�������Ƿ�Ϊ��zgy
		if(pk==null||pk.trim().equals("")){
			pk=" ";
		}
		String code=(String)ui.getBillCardWrapper().getBillCardPanel().getHeadItem("code").getValueObject();
		if(code.length()>20){
			ui.showErrorMessage("���볤�Ȳ��ܴ���20λ��");
			return;
		}
		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		Collection ccode=bs.retrieveByClause(DipDataproceHVO.class, "code='"+code+"' and isnull(dr,0)=0 and pk_dataproce_h<>'"+pk+"' and pk_xt='"+pkxt+"'");
		if(ccode !=null){
			if(ccode.size()>=1){
				ui.showWarningMessage(IContrastUtil.getCodeRepeatHint("����",code));
				return;
			}
		}	
		String name=(String) (ui.getBillCardPanel().getHeadItem("name").getValueObject()==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("name").getValueObject());
//		Collection cname=bs.retrieveByClause(DipDataproceHVO.class,"name='"+name+"' and isnull(dr,0)=0 and pk_dataproce_h<>'"+pk+"' and pk_xt='"+pkxt+"' and isfolder='N'");
//		if(cname!=null){
//			if(cname.size()>=1){
//				ui.showWarningMessage("���ơ�"+name+"���Ѿ����ڣ�");
//				return;
//			}
//		}

		//��ý���ԭʼ���ݵ�vo
		AggregatedValueObject billVO = getBillTreeCardUI().getChangedVOFromUI();
		setTSFormBufferToVO(billVO);

		//��ý���������vo
		AggregatedValueObject checkVO = getBillTreeCardUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);
//		String tablename=null;

		DipDataproceHVO hvo=(DipDataproceHVO) checkVO.getParentVO();
		//2011-7-20 ҵ�����ݴ�ֵʱֻ��һ������
		/*UIRefPane pane=(UIRefPane)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("firsttab").getComponent();
		String[] arrpk=pane.getRefPKs();
		String data=null;
		for(int i=0;i<arrpk.length;i++){
			if(i==0){
				data=arrpk[i];
			}else{
				data=data+","+arrpk[i];
			}
		}
		hvo.setFirsttab(data);*/
		hvo.setFirsttab(data);
		hvo.setIsfolder(new UFBoolean(false));
		hvo.setPk_xt(pkxt);
		checkVO.setParentVO(hvo);


		Object o = null;
		ISingleController sCtrl = null;

		boolean isSave = true;
		if (billVO.getParentVO() == null && (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0))
			isSave = false;
		else if (getBillUI().isSaveAndCommitTogether()){
			billVO = getBusinessAction().saveAndCommit(billVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), checkVO);
		}
		else{
			billVO = getBusinessAction().save(checkVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), checkVO);
		}
		HYPubBO_Client.deleteByWhereClause(ControlVO.class, " pk_bus='"+hvo.getPrimaryKey()+"' and bustype='"+IContrastUtil.YWLX_JG+"' and nvl(dr,0)=0 and tabcname not in('"+data.replaceAll(",", "','")+"')");

		if (sCtrl != null && sCtrl.isSingleDetail())
			billVO.setParentVO((CircularlyAccessibleValueObject) o);
		int nCurrentRow = -1;
		if (isSave) {
			//�޸�
			if (isEditing())
				if (getBufferData().isVOBufferEmpty()) {	
					getBufferData().addVOToBuffer(billVO);
					nCurrentRow = 0;
				} else {

					getBufferData().setCurrentVO(billVO);
					nCurrentRow = getBufferData().getCurrentRow();
				}

			setAddNewOperate(isAdding(), billVO);

		}
		// �˳����棬�ָ����״̬
		setSaveOperateState();
		if (nCurrentRow >= 0)
			getBufferData().setCurrentRow(nCurrentRow);

		//����õ������ӱ���ʱ���Զ�ά�����ṹ����ִ�����²���
		if (getUITreeCardController().isAutoManageTree()) {		

			getSelfUI().insertNodeToTree(billVO.getParentVO());

		}



		/*2011-6-16 begin*/
		String tab=hvo.getFirstdata();//ԭʼ����
		String proceType=hvo.getProcetype();//�ӹ�����
		UFBoolean isAddProceTab=hvo.getIsadd();//�½��ӹ���
		//2011-6-22
		String proceTab=hvo.getProcetab();//�ӹ�����
		setProceBtnIsEnable(tab,proceTab,isAddProceTab,proceType);
		/* end */

		getFirstData(tab);
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("refprocecond", "�����������±�");
//		getSelfUI().getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
//		getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//		getSelfUI().updateButtonUI();
		getSelfUI().onTreeSelectSetButtonState(getSelectNode());
		
	}

	//2011-7-20 ��ѡ ԭʼ���� ��ֵ
	public void getFirstData(String tab){
		VOTreeNode node=getSelectNode();
		if(node !=null){
			if(node.getParentnodeID() !=null && node.getNodeID() !=null){
				UIRefPane pane=(UIRefPane)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("firsttab").getComponent();
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

	/**
	 * ���á��ӹ�����ť�Ƿ����
	 * 1������ԭʼ�������Ѿ������ݿ��д��ڣ����ӹ�����ť���ã����򲻿���
	 * 2����ԭʼ�������ڣ��жϣ�
	 * 2.1���ӹ������ǡ����ݻ��ܡ���
	 *	 2.1.1������½��ӹ���Ϊtrue�Ҽӹ����������ݿ��д��ڣ���ӹ���ť ���ã�������ť �����ã�
	 *	 2.1.2������½��ӹ���Ϊtrue�Ҽӹ����������ݿ��в����ڣ���ӹ���ť �����ã�������ť ���ã�
	 * 2.2:������ϴ��ж�ء�Ԥ��:ֻ��ԭʼ�������ڣ��ӹ���ť�Ϳ��ã����贴����������ť�����ã���
	 * 2.3:����ת����
	 * 	 2.3.1������½��ӹ���Ϊtrue�Ҽӹ����������ݿ��д��ڣ���ӹ���ť ���ã�������ť �����ã�
	 *	 2.3.2������½��ӹ���Ϊtrue�Ҽӹ����������ݿ��в����ڣ���ӹ���ť �����ã�������ť ���ã�
	 * @author ����
	 * @date 2011-6-16
	 * @date 2011-6-22�޸�
	 * @param tab
	 * 			ԭʼ����
	 * @param proceTab
	 * 			�ӹ�����
	 * @param isAddProceTab
	 * 			�½��ӹ���
	 * @param proceType
	 * 			�ӹ�����
	 */
	public void setProceBtnIsEnable(String tab,String proceTab,UFBoolean isAddProceTab,String proceType){
		if(tab!=null && !"".equals(tab)&& tab.length()>0){
//			boolean isExist=isTableExist(tab);	
//			if(isExist==false){
//				getSelfUI().getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(false);
//				getSelfUI().updateButtons();
//			}else{
				if("���ݻ���".equals(proceType) || "����ת��".equals(proceType)){		
					//�½��ӹ���Ϊnull����N
					if(isAddProceTab==null || !isAddProceTab.booleanValue()){
						getSelfUI().getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(false);
//						getSelfUI().getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
						getSelfUI().updateButtons();
					}else{
						//�жϼӹ����Ƿ��Ѿ������ݿ��д���
						boolean isProceTab=isTableExist(proceTab);
						if(isProceTab){
//							getSelfUI().getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
							getSelfUI().getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(true);
							getSelfUI().getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(true);
							getSelfUI().updateButtons();
						}else{
//							getSelfUI().getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(true);
							getSelfUI().getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(false);
							getSelfUI().getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
							getSelfUI().updateButtons();
						}
					}
				}else /*if("������ϴ".equals(proceType) ||"����ж��".equals(proceType) || "����Ԥ��".equals(proceType))*/{
//					getSelfUI().getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(false);
					getSelfUI().getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(true);
					getSelfUI().getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(true);
					getSelfUI().updateButtons();
				}
//			}
		}
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
	String errmsg="";
	public boolean nameType(String type,String value){

		errmsg="";
		boolean ret=true;
		if(type.equals("��������")&&(!value.matches("[\u4E00-\u9FA5]+[[\u4E00-\u9FA5]*+[0-9]*]*"))){
			ret=false;
			errmsg=errmsg+"���������ơ����������Ŀ�ͷ,���������ֽ�β��";
		}if(type.equals("Ӣ������")&&(!value.matches("[A-Za-z]+[[A-Za-z]*+[_]+[0-9]*]*"))){
			ret=false;
			errmsg=errmsg+"��Ӣ�����ơ�ֻ������ĸ��ͷ,���԰����»��ߣ���β���԰������֣�";
		}if(type.equals("�ӹ�����")&&(!value.matches("[A-Z]+[[A-z]*+[_]+[0-9]*]*"))){
			ret=false;
			errmsg=errmsg+"���ӹ�������ֻ���Դ�дӢ����ĸ��ͷ�����԰����»��ߣ���β���԰������֣�";
		}
		return ret;

	}


	@Override
	protected void onBoDelete() throws Exception {
		//�ҵ�Ҫɾ���ĵ�ǰ�ڵ������zgy

		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫɾ���Ľڵ㣡");
			return;
		}

		//String where="";
		AggregatedValueObject checkVO = getBillTreeCardUI().getVOFromUI();
//		DipDataproceBVO[] ddbvo=(DipDataproceBVO[]) checkVO.getChildrenVO();
//		if(ddbvo!=null && ddbvo.length>0){
//			for(int i=0;i<ddbvo.length;i++){
//				where=where+"'"+ddbvo[i].getPrimaryKey()+"',";
//			}
//		}
		String pk=(String) tempNode.getParentnodeID();
		String pk_dataproce_h=(String) tempNode.getNodeID();
		if(pk==null||pk.length()==0){
			getSelfUI().showErrorMessage("ϵͳ�ڵ㲻����ɾ��������");
			return;
		}/*else{
			 2011-5-23 NCϵͳ�µĽڵ㲻��ɾ�� 
			String sql="select h.pk_sysregister_h from dip_sysregister_h h where h.code='0000' and h.pk_sysregister_h='"+pk_dataproce_h+"' and nvl(h.dr,0)=0";
			String isNC=iq.queryfield(sql);
			if(isNC !=null && !"".equals(isNC.trim())){
				getSelfUI().showWarningMessage("NCϵͳ�£�ֻ��������ݣ�����ɾ����"); 
				return;
			}
			 end 
		}*/

		
		DataProceUI ui=(DataProceUI) getBillUI();
		Integer flag = MessageDialog.showOkCancelDlg(this.getBillUI(), "��ʾ", "�Ƿ�ȷ��Ҫɾ��?");
		if(flag != 1){
			return;
		}
		VOTreeNode node1=(VOTreeNode) tempNode.getParent();
//		ɾ������zgy
//		DipDataproceBVO[] vos = (DipDataproceBVO[])HYPubBO_Client.queryByCondition(DipDataproceBVO.class, " pk_dataproce_h='"+pk_dataproce_h+"' and isnull(dr,0)=0  ");
//		if(vos!=null && vos.length>0){
//			for(DipDataproceBVO bvo : vos ){
//				HYPubBO_Client.delete(bvo);
//			}
//		}
		//ɾ����ͷzgy
		DipDataproceHVO vo = (DipDataproceHVO) HYPubBO_Client.queryByPrimaryKey(DipDataproceHVO.class,pk_dataproce_h);
		if(vo==null){
			getSelfUI().showErrorMessage("�����ӽڵ㲻��ɾ��");
//			String uapsql="update dip_sysregister_b set isyy='N' where pk_sysregister_h='"+pk_dataproce_h+"'";
//			iq.exesql(uapsql);
			return;

		}
		if(vo!=null)
			HYPubBO_Client.delete(vo);	

		//2011-6-28 549--550��
		//String sql="update dip_dataproce_b set isquote='N',quotetable=null,quotecolu=null where dip_dataproce_b.quotetable in ("+(where.length()>0?where.substring(0, where.length()-1):where)+")";
		//iq.exesql(sql);

		//������
		this.getBillTreeCardUI().getBillTreeData().deleteNodeFromTree(this.getBillTreeCardUI().getBillTreeSelectNode());

		if(node1!=null){
		getSelfUI().getBillTree().setSelectionPath(new javax.swing.tree.TreePath(node1
							.getPath()));
		}
//		try {
//		onBoRefresh();

//		} catch (Exception e) {
//		e.printStackTrace();
//		}


	}


	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);

		switch(intBtn){

		case IBtnDefine.YuJing:
			onBoWarning(intBtn);
			break;
		case IBtnDefine.DataValidate:
			onBoValidate(intBtn);
			break;
		case IBtnDefine.Movedup:
			onBoMovedup(intBtn);
			break;
		case IBtnDefine.Moveddown:
			onBoMoveddown(intBtn);
			break;
		case IBtnDefine.DataProce:
			onBoDataPorce(intBtn);
			break;
		case IBtnDefine.CreateTable:
			onBoCreateImport(intBtn);
			break;
		case IBtnDefine.CONTROL:
			onBoControl();
			break;
		case IBtnDefine.moveFolderBtn:
			onBoMoveFolder();
			break;
		case IBtnDefine.DataProcessCheck:
			onBoCheck();
			break;

		} 
	}


	/**
		 * @desc �ļ��ƶ�
		 * */
		public void onBoMoveFolder() throws Exception{
			
			MyBillVO billvo=(MyBillVO) getBufferData().getCurrentVO();
			if(billvo!=null&&billvo.getParentVO()!=null){
				DipDataproceHVO hvo=(DipDataproceHVO) billvo.getParentVO();
				MovefolderDLG dlg=new MovefolderDLG(getBillUI(),IContrastUtil.YWLX_JG,hvo);
				dlg.showModal();
				String ret=dlg.getRes();
				if(ret!=null){
					hvo.setAttributeValue((String)IContrastUtil.getFpkMap().get(IContrastUtil.YWLX_JG), ret);
					HYPubBO_Client.update(hvo);
					hvo=(DipDataproceHVO) HYPubBO_Client.queryByPrimaryKey(DipDataproceHVO.class, hvo.getPrimaryKey());
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
	public void onBoCheck(){
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			getSelfUI().showErrorMessage("��ѡ��Ҫ�����Ľڵ�");
			return;
		}
		DipDataproceHVO hvo=null;
		try {
			hvo = (DipDataproceHVO) HYPubBO_Client.queryByPrimaryKey(DipDataproceHVO.class, node.getNodeID().toString());
		} catch (UifException e1) {
			e1.printStackTrace();
		}
		if(SJUtil.isNull(hvo)){
			getSelfUI().showErrorMessage("�˽ڵ㻹û�б��棬��༭��");
			return;
		}
		try {
			DataverifyHVO[] vhvo=(DataverifyHVO[]) HYPubBO_Client.queryByCondition(DataverifyHVO.class, "pk_datachange_h='"+hvo.getPrimaryKey()+"'");
			if(vhvo==null||vhvo.length<=0){
				getSelfUI().showErrorMessage("��û�ж�������У�飡");
				return;
			}else{
				DataverifyHVO hv=vhvo[0];
				DataverifyBVO[] bvo=(DataverifyBVO[]) HYPubBO_Client.queryByCondition(DataverifyBVO.class, "pk_dataverify_h='"+hv.getPrimaryKey()+"'");
				if(bvo==null||bvo.length<=0){
					getSelfUI().showErrorMessage("��û�ж�������У���У����");
					return;
				}else{
					String errlog="";
					for(int i=0;i<bvo.length;i++){
						DataverifyBVO bv=bvo[i];
						if(bv.getVdef2().equals("ͨ��У��")){

							String checkclass=bv.getName();//������ơ�
							Object inst;
							try {
								Class cls = Class.forName(checkclass);
								inst = cls.newInstance();
								if (inst instanceof ICheckPlugin) {
									ICheckPlugin m = (ICheckPlugin) inst;
									CheckMessage cmsg=m.doCheck(bv.getVerifycon());
									if(!cmsg.isSuccessful()&&cmsg.getMessage()!=null&&cmsg.getMessage().length()>0){
										errlog=errlog+bv.getVdef3()+"��"+cmsg.getMessage()+";  ";
										ILogAndTabMonitorSys ilt=(ILogAndTabMonitorSys) NCLocator.getInstance().lookup(ILogAndTabMonitorSys.class.getName());
										ilt.writToDataLog_RequiresNew(hvo.getPrimaryKey(), IContrastUtil.YWLX_JG, "����У��ʧ�ܣ�"+bv.getVdef3()+";"+cmsg.getMessage());
										if(bv.getVector()!=null&&bv.getVector().equals("��ִֹ��")){
											break;
										}
									}
								}
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								errlog=errlog+e.getMessage()+";  ";
							} catch (InstantiationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								errlog=errlog+e.getMessage()+";  ";
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								errlog=errlog+e.getMessage()+";  ";
							}
						
						}
					}
					if(errlog!=null&&errlog.length()>0){
						getSelfUI().showErrorMessage(errlog);
						return;
					}else{
						MessageDialog.showHintDlg(getSelfUI(), "У����", "У����ɹ���");
					}
				}
			}
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			getSelfUI().showErrorMessage("��ѯ����У�鶨��ʧ�ܣ�");
		}
	}
	/**
	 * @desc ���ư�ť�����Ʊ�״̬����
	 * */
	public void onBoControl(){



		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			getSelfUI().showErrorMessage("��ѡ��Ҫ�����Ľڵ�");
			return;
		}
		DipDataproceHVO hvo=null;
		try {
			hvo = (DipDataproceHVO) HYPubBO_Client.queryByPrimaryKey(DipDataproceHVO.class, node.getNodeID().toString());
		} catch (UifException e1) {
			e1.printStackTrace();
		}
		if(SJUtil.isNull(hvo)){
			getSelfUI().showErrorMessage("�˽ڵ㻹û�б��棬��༭��");
			return;
		}

		ControlHVO chvo=new ControlHVO();
		chvo.setBustype(SJUtil.getYWnameByLX(IContrastUtil.YWLX_JG));
		chvo.setCode(hvo.getCode());
		chvo.setName(hvo.getName());

		ControlDlg cd=new ControlDlg(getSelfUI(),chvo,hvo.getPrimaryKey(),IContrastUtil.YWLX_JG,hvo.getFirsttab());
		cd.showModal();



	
		/*
		VOTreeNode node=getSelectNode();
		DipDataproceHVO hvo=null;
		try {
			hvo = (DipDataproceHVO) HYPubBO_Client.queryByPrimaryKey(DipDataproceHVO.class, node.getNodeID().toString());
		} catch (UifException e1) {
			e1.printStackTrace();
		}
		ControlDlg cd=new ControlDlg(getSelfUI(),hvo.getPrimaryKey(),IContrastUtil.YWLX_JG,hvo.getFirsttab());
		cd.showModal();
		if(cd.getRes()==0){
			ControlVO[] vo=cd.getVos();
			try {
				HYPubBO_Client.updateAry(vo);
			} catch (UifException e) {
				e.printStackTrace();
			}
		}
	*/}
	private void onBoMoveddown(int intBtn) throws Exception {
		int  row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		DipDataproceBVO bvo=(DipDataproceBVO) getBillTreeCardUI().getVOFromUI().getChildrenVO()[row];

	}

	private void onBoMovedup(int intBtn) throws Exception {
		int  row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		DipDataproceBVO bvo=(DipDataproceBVO) getBillTreeCardUI().getVOFromUI().getChildrenVO()[row];

	}

	private void onBoValidate(int intBtn) {

		VOTreeNode tempNode=getSelectNode();
		if(SJUtil.isNull(tempNode)){
			getSelfUI().showErrorMessage("��ѡ��Ҫ�����Ľڵ�");
			return;
		}
		DipDataproceHVO hvo=(DipDataproceHVO) tempNode.getData();
		String  hpk=hvo.getPk_dataproce_h();
		try {
			hvo=(DipDataproceHVO) HYPubBO_Client.queryByPrimaryKey(DipDataproceHVO.class, hpk);
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(SJUtil.isNull(hvo)){
			getSelfUI().showErrorMessage("�˽ڵ㻹û�б��棬��༭��");
			return;
		}
		// TODO Auto-generated method stub
		DataProceUI dpui=(DataProceUI) getBillUI();
//		int row=getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().getTable().getSelectedRow();
//		if(row==-1){
//		dpui.showWarningMessage("���ڱ���ѡ��һ����Ϣ��");
//		return;
//		}else{
////		//��������У�鴰��
//		ToftPanel toft=SFClientUtil.showNode("H4H1H0",IFuncWindow.WINDOW_TYPE_DLG);
//		DataVerifyClientUI ui=(DataVerifyClientUI) toft;
		HashMap map = new HashMap();
		map.put("pk", hpk);
		map.put("type", SJUtil.getYWnameByLX(IContrastUtil.YWLX_JG));
		/*����ǰҳ��ı�ͷcode��name�ŵ�map�У����ڵ���������У�鴰��ʱ�����������Ϊ�գ��Զ����
		 * 2011-06-14
		 * zlc*/
		map.put("code", hvo.getCode());
		map.put("name", hvo.getName());
		map.put("fpk", getSelectNode().getParentnodeID().toString());
		DatarecDlg dlg = new DatarecDlg(getSelfUI(),new UFBoolean(true),map);
		dlg.show();


	}
//	Map<String,ProcessstyleVO> map=new HashMap<String, ProcessstyleVO>();
	public void onTreeSelected(VOTreeNode arg0){
		//��ȡvo
		try {
			SuperVO vo=(SuperVO) arg0.getData();
			String pk_dataproce_h=(String) vo.getAttributeValue("pk_dataproce_h");
//			SuperVO[] vos=HYPubBO_Client.queryByCondition(nc.vo.dip.dataproce.DipDataproceBVO.class ,"pk_dataproce_h='"+pk_dataproce_h+"' and isnull(dr,0)=0");
			nc.vo.dip.dataproce.MyBillVO billvo=new nc.vo.dip.dataproce.MyBillVO();
			DipDataproceHVO hvo = (DipDataproceHVO)vo;

			hvo.setRefprocecond("�����������±�");
			billvo.setParentVO(vo);
			//������VO
//			billvo.setChildrenVO(vos);
			getBufferData().addVOToBuffer(billvo);
			getBillTreeCardUI().getTreeToBuffer().put(arg0.getNodeID(), "" + (getBufferData().getVOBufferSize() - 1));


			//����Դ���ݱ�������
			VOTreeNode tempNode = getSelectNode();
			String pid=null;
			DipSysregisterHVO syshvo;//
			if(tempNode!=null/*&&tempNode.getParentnodeID()!=null*/){
				if(tempNode.getParentnodeID()!=null&&!"".equals(tempNode.getParentnodeID())&& (tempNode.getNodeID() !=null && !"".equals(tempNode.getNodeID()))){
//					String pk=(String) tempNode.getParentnodeID();
					DipDataproceHVO dataprocehvo=(DipDataproceHVO) tempNode.getData();
					//String firsttab=this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("firsttab").getValue();
					
//					if(firsttab!=null&&firsttab.length()>0){
//						uir.setPKs(firsttab.split(","));
//						model2.setFilterPks(firsttab.split(","));
//					}

					syshvo = (DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, dataprocehvo.getPk_xt());
					if(syshvo!=null){
						extcode=syshvo.getExtcode();
					}
				}else{
//					DipSysregisterHVO syshvo;
					try {
						syshvo = (DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, tempNode.getNodeID().toString());
						extcode=syshvo.getExtcode();

					} catch (UifException e) {
						e.printStackTrace();
					}
				}
			}
			
			/*liyunzhe modify ����ҵ����޸ĳ����β��� 2012-06-04 strat*/
			UIRefPane uir=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("firsttab").getComponent();
			DataDefinitTableTreeRefModel model=new DataDefinitTableTreeRefModel();
			model.setClassWherePart(model.getClassWherePart() +" and pk_xt='"+vo.getAttributeValue("pk_xt")+"'");
			model.addWherePart(" and tabsoucetype='�Զ���'");
			uir.setRefModel(model);
			/*liyunzhe modify ����ҵ����޸ĳ����β��� 2012-06-04 end*/
		} catch (UifException e) {
			e.printStackTrace();
		}
		
		
		
	}
	public void rewrit(){
		if(getSelectNode()!=null){
			DipDataproceHVO hvo=(DipDataproceHVO) getSelectNode().getData();
			if(hvo!=null&&hvo.getFpk()!=null&&hvo.getFpk().length()>0){
				
				String tab=hvo.getFirsttab();
				
				getSelfUI().getBillCardPanel().getHeadItem("firsttab").setValue(getName(tab));
			}	
		}
		
	}
	public String getName(String tab){
		String ret="";
		if(tab!=null&&tab.length()>0){
			try {
				DipDatadefinitHVO[] hvo=(DipDatadefinitHVO[]) HYPubBO_Client.queryByCondition(DipDatadefinitHVO.class, "pk_datadefinit_h in ('"+tab.replaceAll(",", "','")+"')");
				if(hvo!=null&&hvo.length>0){
					for(int i=0;i<hvo.length;i++){
						ret=ret+hvo[i].getSysname()+",";
					}
				}
			} catch (UifException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(ret!=null&&ret.length()>0){
			ret=ret.substring(0,ret.length()-1);
		}
		return ret;
	}
	public  void isAdd() throws Exception{

		DataProceUI ui=(DataProceUI) getBillUI();
		String  isadd=(String) ui.getBillCardWrapper().getBillCardPanel().getHeadItem("isadd").getValueObject();

		String  pk_dataproce_h=(String) ui.getBillCardPanel().getHeadItem("pk_dataproce_h").getValueObject();
		UFBoolean bool=new UFBoolean(false);
		Boolean boolea=new Boolean(false);
		String boole=bool.toString();
		DipDataproceHVO[]  dvo= (DipDataproceHVO[])HYPubBO_Client.queryByCondition(DipDataproceHVO.class,"pk_dataproce_h='"+pk_dataproce_h+"' and isnull(dr,0)=0");
		for(int i=0;i<dvo.length;i++){
			String disadd=dvo[i].getIsadd().toString();
			if(disadd==boole){
				getSelfUI().getBillCardWrapper().getBillCardPanel().getHeadItem("procetab").setEnabled(false);
				getSelfUI().getBillCardWrapper().getBillCardPanel().getBodyHeadMenu().enable(boolea);
				getSelfUI().getBillCardWrapper().getBillCardPanel().getBodyPanel().enable(boolea);
				return;
			}


		}
	}


	@Override
	protected void onBoEdit() throws Exception {
		getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(false);
		getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ�޸ĵĽڵ㣡");
			return;
		}
		String pk=(String) tempNode.getParentnodeID();
		if(pk==null||pk.equals("")){
			getSelfUI().showErrorMessage("ϵͳ�ڵ㲻�����޸Ĳ�����");
			return;

		}/*else{
			��ѯNCϵͳ�µĽڵ㣬���������޸Ĳ��� 2011-5-23 ���� begin 
			String sql="select h.pk_sysregister_h from dip_sysregister_h h where h.code='0000' and h.pk_sysregister_h='"+pk+"' and nvl(h.dr,0)=0";
			String isNC=iq.queryfield(sql);
			if(isNC !=null && !"".equals(isNC.trim())){
				getSelfUI().showWarningMessage("NCϵͳ�£�ֻ��������ݣ������޸ģ�"); 
				return;
			}
			 end 
		}*/
		super.onBoEdit();
		isAdd();

        DipDataproceHVO datahvo=(DipDataproceHVO) tempNode.getData();
		DataProceUI ui=(DataProceUI) getBillUI();
		String pk_dataproce_h=(String) ui.getBillCardWrapper().getBillCardPanel().getHeadItem("pk_dataproce_h").getValueObject();

		// TODO Auto-generated method stub


//		String name=this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("procecond").getValue();
//		this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("refprocecond").setValue("�����������±�");
//		BillItem item12 = this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("refprocecond");
//		UIRefPane ref = (UIRefPane) item12.getComponent();
//		ref.setValue(name);
		setHeadFieldVisble(false);
		//20110609 liyunzhe start
		/*liyunzhe ע�� 2012-06-13 start ���ڲ���Ҫ���ø��ֶ� */
//		if("������ϴ".equals(getSelfUI().getBillCardPanel().getHeadItem("procetype").getValue())||"����Ԥ��".equals(getSelfUI().getBillCardPanel().getHeadItem("procetype").getValue())||"����ж��".equals(getSelfUI().getBillCardPanel().getHeadItem("procetype").getValue())){
//			getSelfUI().getBillCardPanel().getHeadItem("isadd").setEnabled(false);
//		}else{
//			getSelfUI().getBillCardPanel().getHeadItem("isadd").setEnabled(true);
//		}
		/*liyunzhe ע�� 2012-06-13 end ���ڲ���Ҫ���ø��ֶ� */
		//20110609 liyunzhe end

//		String firsttab=this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("firsttab").getValue();
//		this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("firsttab").setValue(firsttab);
//		UIRefPane uir=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("firsttab").getComponent();
//		DataDefinitRefModel model2=new DataDefinitRefModel();
//		model2.addWherePart(" and dip_datadefinit_h.pk_datadefinit_h <>'"+pk+"' and pk_sysregister_h='"+tempNode.getParentnodeID()+"' and nvl(dr,0)=0");
//		uir.setRefModel(model2);
//		uir.setValue(firsttab);

//		���������ԡ�������ͷ
//		UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("procetab").getComponent();
//		String ss=jcp.getText();
//		docu=jcp.getUITextField().getDocument();
//		jcp.getUITextField().setDocument(new StringDocument(jcp.getUITextField(),"DIP_DD_"));
//		jcp.getUITextField().setText(ss);

		//2011-6-27 �������ñ���գ��������ݶ�������ϵͳ���Ѿ����������
		/*UIRefPane pane=(UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getBodyItem("def_quotetable").getComponent();
		DataProceYYBRefModel model=new DataProceYYBRefModel();
		model.addWherePart(" and pk_datadefinit_h <> '"+pk_dataproce_h+"'");
		pane.setRefModel(model);*/

		BillItem item1 = getSelfUI().getBillCardPanel().getHeadItem("refprocecond");//�ӹ�����
		if (item1 != null) {
			UIRefPane ref1 = (UIRefPane) item1.getComponent();

			if (ref1 != null) { 
				ref1.getUIButton().removeActionListener(ref1.getUIButton().getListeners(ActionListener.class)[0]);
				ref1.getUIButton().addActionListener(new DataProcActionListener(getSelfUI(),item1));
				ref1.setAutoCheck(false);
				ref1.setEditable(false);
			}
		}
		DipDataproceHVO hvo=(DipDataproceHVO) HYPubBO_Client.queryByPrimaryKey(DipDataproceHVO.class, tempNode.getNodeID().toString());
		String firsttab=hvo.getFirsttab();
//		String firsttab=((DipDataproceHVO)tempNode.getData()).getFirsttab();
//		this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("firsttab").setValue(firsttab);
		//2011-7-20
		/*liyunzhe 2012-06-04 modify �޸�ҵ���Ϊ���β��� start*/
		UIRefPane uir=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("firsttab").getComponent();
		DataDefinitTableTreeRefModel model=new DataDefinitTableTreeRefModel();
		model.setClassWherePart(model.getClassWherePart() +" and pk_xt='"+datahvo.getPk_xt()+"'");
		model.addWherePart(" and tabsoucetype='�Զ���'");
//		DataDefinitRefModel model2=new DataDefinitRefModel();
//		model2.addWherePart(" and dip_datadefinit_h.pk_datadefinit_h <>'"+pk+"' and pk_xt='"+datahvo.getPk_xt()+"' and nvl(dr,0)=0 and dip_datadefinit_h.iscreatetab='Y'");
		uir.setRefModel(model);
		uir.setPKs(firsttab.split(","));
		/*liyunzhe 2012-06-04 modify �޸�ҵ���Ϊ���β��� end*/
//		������Ԥ��״̬
		UIRefPane processStyleUir=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_str_2").getComponent();
		ProcessStyleRefModel processStyleModel=new ProcessStyleRefModel();
		processStyleModel.addWherePart(" and dip_processstyle.pk_processstype<>'0001AA1000000003HBMO' and nvl(dip_processstyle.dr,0)=0 ");
		processStyleUir.setRefModel(processStyleModel);
		processStyleUir.setPK(hvo.getDef_str_2());
	}
	public void setHeadFieldVisble(boolean b){
//		this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("procecond").setShow(b);
		this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("refprocecond").setShow(!b);
		this.getBillCardPanelWrapper().getBillCardPanel().initPanelByPos(IBillItem.HEAD);		
	}
	@Override
	/*
	 * ���ܣ�
	 * ������ʱ��İ���Ƿ����ӽڵ㲢�Ҹ���ͷ���������������ƣ�ԭʼ������ݶ������������ֶθ���ֵ
	 * zgy
	 */
	public void onBoAdd(ButtonObject bo) throws Exception {
		getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(false);
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ���ӵĽڵ㣡");
			return;
		}
		String pk=(String) tempNode.getParentnodeID();
		if(pk==null){
			getSelfUI().showErrorMessage("ϵͳ�ڵ㲻�������Ӳ�����");
			return;
		}
		DipDataproceHVO hvo=(DipDataproceHVO) tempNode.getData();
		if(!(hvo!=null&&hvo.getIsfolder().booleanValue())){
			getSelfUI().showErrorMessage("�����ļ��ڵ㣬���������Ӳ�����");
		}
		
		/*else{
			 2011-5-23 ���� �ж��Ƿ���NCϵͳ:����ǣ��������κεĲ�����ֻ����� begin 
			String ncsql="select code from dip_sysregister_h h where h.pk_sysregister_h='"+tempNode.getNodeID()+"' and nvl(h.dr,0)=0";
			try {
				String code=iq.queryfield(ncsql);
				if("0000".equals(code)){
					getSelfUI().showErrorMessage("NCϵͳ���������Ӳ�����");
					return;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (DbException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 end 
		}*/

		super.onBoAdd(bo);
		this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("procetab").setEnabled(false);


		getSelfUI().getBillCardPanel().setHeadItem("fpk", tempNode.getNodeID());

		DipDataproceHVO vo=(DipDataproceHVO) tempNode.getData();

//		getSelfUI().getBillCardPanel().setHeadItem("code",vo.getCode());
//		getSelfUI().getBillCardPanel().setHeadItem("name",vo.getName());
		getSelfUI().getBillCardPanel().setHeadItem("firsttab",vo.getFirsttab());
		//liyunzhe 20110604 start
		getSelfUI().FF=1;//ȷ�������Ӱ�ť��
		//liyunzhe 20110604 end

		//2011-7-20
//		UIRefPane uir=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("firsttab").getComponent();
//		DataDefinitRefModel model2=new DataDefinitRefModel();
////		model2.addWherePart(" and dip_datadefinit_h.pk_datadefinit_h <>'"+pk+"' and pk_sysregister_h='"+tempNode.getNodeID()+"' and tabsoucetype='�Զ���' and nvl(dr,0)=0");
//		model2.addWherePart(" and dip_datadefinit_h.pk_datadefinit_h <>'"+pk+"' and pk_xt='"+vo.getPk_xt()+"' and nvl(dr,0)=0 and dip_datadefinit_h.iscreatetab='Y'");//ȡ��ԭʼ����ֻ�ܲ����Զ����������� 2011-06-21 zlc
//		uir.setRefModel(model2);
		//������Ԥ��״̬
		UIRefPane processStyleUir=(UIRefPane)this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_str_2").getComponent();
		ProcessStyleRefModel processStyleModel=new ProcessStyleRefModel();
		processStyleModel.addWherePart(" and dip_processstyle.pk_processstype<>'0001AA1000000003HBMO' and nvl(dip_processstyle.dr,0)=0 ");
		processStyleUir.setRefModel(processStyleModel);
		
		//���������ԡ�������ͷ
//		UIRefPane panel=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("procetab").getComponent();
//		docu=panel.getUITextField().getDocument();
//		panel.getUITextField().setDocument(new StringDocument(panel.getUITextField(),"DIP_DD_"));
//		panel.setText("DIP_DD_");

		//2011-6-27 �������ñ���գ��������ݶ�������ϵͳ���Ѿ����������
	/*	UIRefPane pane=(UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getBodyItem("def_quotetable").getComponent();
		DataProceYYBRefModel model=new DataProceYYBRefModel();
		model.addWherePart(" and pk_datadefinit_h <> '"+tempNode.getNodeID()+"'");
		pane.setRefModel(model);
*/

		BillItem item12 = getSelfUI().getBillCardPanel().getHeadItem("refprocecond");//�ӹ�����
		if (item12 != null) {
			UIRefPane ref = (UIRefPane) item12.getComponent();

			if (ref != null) { 
				ref.getUIButton().removeActionListener(ref.getUIButton().getListeners(ActionListener.class)[0]);
				ref.getUIButton().addActionListener(new DataProcActionListener(getSelfUI(),item12));
				ref.setAutoCheck(false);
				ref.setEditable(false);
			}
		}
	}
//	Document docu;

//	@Override
//	protected void onBoCancel() throws Exception {
//	// TODO Auto-generated method stub
//	super.onBoCancel();
////	super.onBoRefresh();
//	}


	@Override
	/*�Ա����н���ɾ��
	 * 
	 * zgy
	 * 
	 */
	protected void onBoLineDel() throws Exception {
		//ѡ��Ҫɾ���ı��嵱ǰ��
		int rowcount=getSelfUI().getBillCardPanel().getBillTable().getRowCount();
		if(rowcount<=0)
			return;

		int  row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		if(row<0){
			getSelfUI().showErrorMessage("��ѡ��Ҫɾ�����У�");
			return;
		}
		DipDataproceBVO bvo=(DipDataproceBVO) getBillTreeCardUI().getVOFromUI().getChildrenVO()[row];


//		UFBoolean isquote=bvo.getIsquote();
		UFBoolean isquote =bvo.getIsquote()==null?new UFBoolean(false):bvo.getIsquote();
//		String  isquot=isquote.toString();
		//���Ƿ����ð�ť�����жϣ������þͲ���ɾ��
		if( isquote.booleanValue()){
			getSelfUI().showErrorMessage("�����Ѿ������ã�����ɾ����");
			return;
		}

		//2011-7-4 cl
		Object ob=getSelfUI().getBillCardPanel().getBillModel().getValueAt(row, "pk_dataproce_b");
		if(ob==null){
			super.onBoLineDel();
			getSelfUI().delstr = getSelfUI().delstr +",'"+ob.toString()+"'";
			//getSelfUI().delstr = getSelfUI().delstr +",'"+ob+"'";
		}else{
			super.onBoLineDel();
			getSelfUI().delstr = getSelfUI().delstr +",'"+ob.toString()+"'";			
			String sb=ob.toString();
			System.out.println("ɾ��������"+sb+"��vo");
		}

	}

	/**
	 * ���ܣ�����ɾ�����ϵ����ݣ������ɾ�У����ڱ���ʱ��������Ҫɾ����ɾ��
	 * ���ߣ�cl
	 * ����:2011-07-04
	 * */
	public void deleteLine(){
		String sql =" delete from dip_dataproce_b where pk_dataproce_b in ('11122aabb'"+getSelfUI().delstr+")";
		try {
			iq.exesql(sql);
		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DbException e) {
			e.printStackTrace();
		}
		getSelfUI().delstr = "";

	}

	@Override
	protected void onBoLineAdd() throws Exception {
//		super.onBoRefresh();
//		DataProceUI ui=(DataProceUI) getBillUI();
//		String  isimport=(String) ui.getBillCardWrapper().getBillCardPanel().getBodyItem("isimport").getValueObject();



//		getSelfUI().getBillCardWrapper().getBillCardPanel().getBodyItem("isimport").setEnabled(true);

		super.onBoLineAdd();
	}
	//Ԥ��


	private void onBoWarning(int intBtn) throws Exception {


		VOTreeNode tempNode=getSelectNode();
		if(SJUtil.isNull(tempNode)){
			getSelfUI().showErrorMessage("��ѡ��Ҫ�����Ľڵ�");
			return;
		}
		DipDataproceHVO hvo=(DipDataproceHVO) tempNode.getData();
		String  hpk=hvo.getPk_dataproce_h();
		hvo=(DipDataproceHVO) HYPubBO_Client.queryByPrimaryKey(DipDataproceHVO.class, hpk);
		if(SJUtil.isNull(hvo)){
			getSelfUI().showErrorMessage("�˽ڵ㻹û�б��棬��༭��");
			return;
		}
		//��ǰ���ݵ�Ԥ��ҵ������
		String tasktype=hvo.getTasktype();
		if(SJUtil.isNull(tasktype)||tasktype.length()==0){
			getSelfUI().showErrorMessage("��ѡ��Ԥ�����ͣ�");
			return;
		}
		/*ToftPanel toft = SFClientUtil.showNode("H4H2H5", IFuncWindow.WINDOW_TYPE_DLG);
		WarningSetClientUI ui=(WarningSetClientUI) toft;*/

		String pk_dataproce_h=hvo.getPrimaryKey();//.getPk_datadefinit_h();
		nc.vo.dip.warningset.MyBillVO mybillvo=new nc.vo.dip.warningset.MyBillVO() ;
		DipWarningsetVO[] warvo=(DipWarningsetVO[]) HYPubBO_Client.queryByCondition(DipWarningsetVO.class, " tasktype='"+tasktype+"' and pk_bustab='"+pk_dataproce_h+"'");

		DipWarningsetVO vo=null;
		boolean isadd=false;

		if(SJUtil.isNull(warvo)||warvo.length==0){
			vo=new  DipWarningsetVO();
			vo.setPk_bustab(hpk);
			vo.setTasktype(tasktype);
			vo.setPk_sys(hvo.getFpk());
			isadd=true;
		}else{
			vo=warvo[0];
			DipWarningsetBVO[] children=(DipWarningsetBVO[]) HYPubBO_Client.queryByCondition(DipWarningsetBVO.class, "pk_warningset='"+vo.getPk_warningset()+"'");
			mybillvo.setChildrenVO(children);
			isadd=false;

		}
		vo.setWcode(hvo.getCode());
		vo.setWname(hvo.getName());
		mybillvo.setParentVO(vo);

		//2011-7-18
		//ui.init(mybillvo, isadd,tempNode.getParentnodeID().toString(),1);
		WarningsetDlg wd=new WarningsetDlg(getSelfUI(),mybillvo, isadd,hvo.getPk_xt(),1);		
		wd.showModal();		



	}
	nc.vo.dip.dataproce.MyBillVO nodec=null;

	@Override
	protected void onBoCancel() throws Exception {
		this.getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(true);
		//2011-6-22 ������ע����
		//this.getButtonManager().getButton(IBtnDefine.CreateTable).setEnabled(true);
		this.getButtonManager().getButton(IBtnDefine.DataValidate).setEnabled(true);
		this.getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(true);
		//liyunzhe 2011-06-04 start
		getSelfUI().FF=0;
		DipDataproceHVO hh22=(DipDataproceHVO) getSelectNode().getData();
		super.onBoCancel();
		getSelfUI().onTreeSelectSetButtonState(getSelectNode());
//		if(nodec==null){
//			if(hh22!=null&&hh22.getIsfolder()!=null&&hh22.getIsfolder().booleanValue()){
//				getSelfUI().getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBtnDefine.YuJing).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBtnDefine.DataValidate).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBtnDefine.DataProcessCheck).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(true);
//			}else{
//				getSelfUI().getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//				getSelfUI().getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(true);
//	
//			}
//						getSelfUI().updateButtonUI();
			rewrit();
//			return;
//		}
//		String ss=(String) ((nodec.getParentVO().getAttributeValue("fpk")==null)?"":(nodec.getParentVO().getAttributeValue("fpk")));
//		if(ss.length()>0){
//			getSelfUI().getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
//			getSelfUI().getButtonManager().getButton(IBillButton.Delete).setEnabled(true);
//			getSelfUI().getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(true);
////			getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(f);
//		}else{
//			getSelfUI().getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
//			getSelfUI().getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
//		}
		
		
		
		//liyunzhe 2011-06-04 end
//		UIRefPane jcp=(UIRefPane) getSelfUI().getBillCardPanel().getHeadItem("procetab").getComponent();
//		jcp.getUITextField().setDocument(docu);
//		jcp.getUITextField().setText("DIP_DD_");


		/*2011-6-22 cl ���ð�ť���ӹ���������������ť״̬ begin*/
		AggregatedValueObject avo=getBillCardPanelWrapper().getBillVOFromUI();
		DipDataproceHVO ddhvo=(DipDataproceHVO) avo.getParentVO();
		String tab=ddhvo.getFirstdata();
//		String proceTab=ddhvo.getProcetab();
//		UFBoolean isAddProceTab=ddhvo.getIsadd();
//		String proceType=ddhvo.getProcetype();
//		setProceBtnIsEnable(tab,proceTab,isAddProceTab,proceType);
		/* end */
//
//		getFirstData(tab);
//
//		/**
//		 * @author ����
//		 * @date 2011-7-4
//		 * @���ܣ�����޸�ʱ�������κβ���,���ȡ����ť��"ԭʼ����"û��ʾֵ����
//		 */
//		VOTreeNode node=getSelectNode();
//		if(node!=null){
//			/*	String firsttab=(String) this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("firsttab").getValueObject();
//			String sql="select memorytable from dip_dataDefinit_h where pk_datadefinit_h='"+firsttab+"' and nvl(dr,0)=0";
//			String tabname=iq.queryfield(sql);*/
//			this.getBillCardPanelWrapper().getBillCardPanel().setHeadItem("firstdata", tab);
//		}
//
//		getSelfUI().delstr ="";//cl 2011-07-04 ȡ�����ɾ�л���
//		rewrit();
//		getSelfUI().getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
//		getSelfUI().getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
//		getSelfUI().getButtonManager().getButton(IBtnDefine.editFolderBtn).setEnabled(false);
//		getSelfUI().getButtonManager().getButton(IBtnDefine.delFolderBtn).setEnabled(false);
//		getSelfUI().updateButtonUI();
	}

	@Override
	protected void onBoLinePaste() throws Exception {
		// TODO Auto-generated method stub
		super.onBoLinePaste();
		int row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		getSelfUI().getBillCardPanel().setBodyValueAt(null, row, new DipDataproceBVO().getPKFieldName());
		getSelfUI().getBillCardPanel().setBodyValueAt(new UFBoolean(false), row, new DipDataproceBVO().ISPK);
		getSelfUI().getBillCardPanel().setBodyValueAt(new UFBoolean(false), row, new DipDataproceBVO().ISONLYCONST);
		getSelfUI().getBillCardPanel().setBodyValueAt(new UFBoolean(false), row, new DipDataproceBVO().ISQUOTE);
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
		DipDataproceHVO vo =(DipDataproceHVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
			DipDataproceHVO[] listvos=(DipDataproceHVO[]) HYPubBO_Client.queryByCondition(DipDataproceHVO.class, "pk_xt='"+vo.getPk_xt()+"' and isfolder='Y' and pk_dataproce_h<>'"+vo.getPrimaryKey()+"' and nvl(dr,0)=0");
			List<String> listcode=new ArrayList<String>();
			List<String> listname=new ArrayList<String>();
			if(listvos!=null&&listvos.length>0){
				for(int i=0;i<listvos.length;i++){
					listcode.add(listvos[i].getCode());
					listname.add(listvos[i].getName());
				}
			}
				
			AddFolderDlg adlg=new AddFolderDlg(getBillUI(),listcode,listname,vo.getCode(),vo.getName());
			adlg.showModal();
			if(adlg.isOk()){
				String tempc=adlg.getCode();
				String tempn=adlg.getName();
				if(!tempc.equals(vo.getCode())||!tempn.equals(vo.getName())){
					vo.setCode(tempc);
					vo.setName(tempn);
					HYPubBO_Client.update(vo);
					vo=(DipDataproceHVO) HYPubBO_Client.queryByPrimaryKey(DipDataproceHVO.class, vo.getPrimaryKey());
					if (getUITreeCardController().isAutoManageTree()) {	
						getSelfUI().insertNodeToTree(vo);
						getBillTreeCardUI().updateUI();
						getBillCardPanelWrapper().getBillCardPanel().setHeadItem("code", tempc);
						getBillCardPanelWrapper().getBillCardPanel().setHeadItem("name", tempn);
					}
				}
			}
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
		DipDataproceHVO vo =(DipDataproceHVO) tempNode.getData();
		VOTreeNode node1=(VOTreeNode) tempNode.getParent();
		if(vo.getIsfolder().booleanValue()){
			SuperVO[] hvos=HYPubBO_Client.queryByCondition(DipDataproceHVO.class, "fpk='"+vo.getPrimaryKey()+"'");
			if(hvos!=null&&hvos.length>0){
				throw new Exception("�ļ������Ѿ������ݶ��壬����ɾ����");
			}else{
				HYPubBO_Client.delete(vo);
				getBillTreeCardUI().getBillTreeData().deleteNodeFromTree(this.getBillTreeCardUI().getBillTreeSelectNode());
				if(node1!=null){
					getSelfUI().getBillTree().setSelectionPath(new javax.swing.tree.TreePath(node1
										.getPath()));
					}
			}
		}
	}
	public void onBoAddFolder() throws Exception {
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null||tempNode.getData()==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ���ӵĽڵ㣡");
			return ;
		}
		DipDataproceHVO hvo=(DipDataproceHVO) tempNode.getData();
		if(!hvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("�����ļ��в����������ļ��в�����");
			return ;
		}
		DipDataproceHVO newhvo=new DipDataproceHVO();
		newhvo.setFpk(hvo.getPrimaryKey());
		newhvo.setPk_xt(hvo.getPk_xt());
		newhvo.setIsfolder(new UFBoolean(true));
		DipDataproceHVO[] listvos=(DipDataproceHVO[]) HYPubBO_Client.queryByCondition(DipDataproceHVO.class, "pk_xt='"+hvo.getPk_xt()+"' and isfolder='Y' and nvl(dr,0)=0");
		List<String> listcode=new ArrayList<String>();
		List<String> listname=new ArrayList<String>();
		if(listvos!=null&&listvos.length>0){
			for(int i=0;i<listvos.length;i++){
				listcode.add(listvos[i].getCode());
				listname.add(listvos[i].getName());
			}
		}
			
		AddFolderDlg addlg=new AddFolderDlg(getBillUI(),listcode,listname,null,null);
		addlg.showModal();
		if(addlg.isOk()){
			newhvo.setCode(addlg.getCode());
			newhvo.setName(addlg.getName());
			String pk = null;
			try {
				pk = HYPubBO_Client.insert(newhvo);
			} catch (UifException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				newhvo=(DipDataproceHVO) HYPubBO_Client.queryByPrimaryKey(DipDataproceHVO.class, pk);
				
				if (getUITreeCardController().isAutoManageTree()) {	
//					MyBillVO mvo=new MyBillVO();
//					mvo.setParentVO(newhvo);
					getSelfUI().insertNodeToTree(newhvo);
				}
			} catch (UifException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
//		getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//		getSelfUI().getButtonManager().getButton(IBtnDefine.DataProce).setEnabled(false);
		SJUtil.setAllButtonsEnalbe(getSelfUI().getButtons());
//		getSelfUI().getButtons();
		getSelfUI().updateButtonUI();
	}
	
	
	
	
	
}