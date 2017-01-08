package nc.ui.dip.datasynch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JFileChooser;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.dip.pub.IQueryField;
import nc.itf.dip.pub.ITaskExecute;
import nc.itf.uap.IUAPQueryBS;
import nc.servlet.dip.pub.DipServletUtil;
import nc.ui.bd.ref.DataReceiveTreeRefModel;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.control.ControlDlg;
import nc.ui.dip.datarec.DatarecDlg;
import nc.ui.dip.dlg.AddFolderDlg;
import nc.ui.dip.dlg.movefoleder.MovefolderDLG;
import nc.ui.dip.warningset.WarningsetDlg;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.util.NCOptionPane;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.sm.cmenu.Desktop;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.trade.treemanage.ITreeManageController;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.RetMessage;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.control.ControlHVO;
import nc.vo.dip.dataorigin.DipDataoriginVO;
import nc.vo.dip.datarec.DipDatarecHVO;
import nc.vo.dip.datasynch.DipDatasynchVO;
import nc.vo.dip.warningset.DipWarningsetBVO;
import nc.vo.dip.warningset.DipWarningsetVO;
import nc.vo.dip.warningset.MyBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.pub.IExAggVO;

/**
 *
 *������AbstractMyEventHandler�������ʵ���࣬
 *��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 *@author author
 *@version tempProject version
 */

public class MyEventHandler 
extends AbstractMyEventHandler{
 IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	public MyEventHandler(BillTreeCardUI billUI, ICardController control){
		super(billUI,control);		
	}


	protected ITreeManageController getUITreeManageController() {
		return (ITreeManageController) getUIController();
	}

	/**
	 * ȡ�õ�ǰUI��
	 * 
	 * @return
	 */
	private DataSynchUI getSelfUI() {
		return (DataSynchUI) getBillUI();
	}
	/**
	 * ȡ��ѡ�еĽڵ����
	 * 
	 * @return
	 */
	private VOTreeNode getSelectNode() {
		return getSelfUI().getBillTreeSelectNode();
	}
	protected CircularlyAccessibleValueObject processNewBodyVO(CircularlyAccessibleValueObject newBodyVO) {

		VOTreeNode parent = this.getSelectNode();

		// ����������д���ӱ���
		newBodyVO.setAttributeValue("pk_datasynch", parent == null ? null : parent.getNodeID());

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
	 * @author wyd
	 * @desc ����д�޸İ�ť��������ݿ���û�������Ļ����޸ĵ�״̬Ϊ����״̬
	 * 
	 * */
	private boolean isnew=false;
	@Override
	protected void onBoEdit() throws Exception {
		VOTreeNode tempNode=getSelectNode();
		if(tempNode==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ�޸ĵĽڵ㣡");
			return;
		}
		String ss=(String) tempNode.getParentnodeID();
		if(ss==null){
			getSelfUI().showErrorMessage("ϵͳ�ڵ㲻�����޸Ĳ�����");

			return;
		}
		super.onBoEdit();
//		// 2011-06-04 ����� ��
		getSelfUI().getBillCardPanel().getHeadItem("code").setEdit(true);
		getSelfUI().getBillCardPanel().getHeadItem("name").setEdit(true);
////		   2011-06-04 ����� ��
		String fpk=this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_xt").getValueObject().toString();
		if(!SJUtil.isNull(fpk)){
			UIRefPane pa1=(UIRefPane) this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("dataname").getComponent();
//			DataReceiveRefModel model=(DataReceiveRefModel) pa1.getRefModel();
////			model.addWherePart(" and(( dip_datarec_h.pk_xt is not null and dip_datarec_h.pk_xt='"+fpk+"') or (dip_datarec_h.pk_xt is null and dip_datarec_h.fpk='"+fpk+"'))  and (dip_datarec_h.isfolder='N' or dip_datarec_h.isfolder is null) and (dip_datarec_h.sourcetype not in ('0001AA10000000013SVI','0001AA1000000003493X') or (dip_datarec_h.sourcetype='0001AA1000000003493X' and ioflag='����')  or (dip_datarec_h.sourcetype = '0001AA10000000013SVI'  and ((dip_datarec_h.ioflag = '���' and dip_datarec_h.databakfile='��Ϣ��' ) or dip_datarec_h.databakfile='�ļ���') )) and nvl(dip_datarec_h.dr, 0) = 0 ");
//			model.addWherePart(" and(( dip_datarec_h.pk_xt is not null and dip_datarec_h.pk_xt='"+fpk+"') or (dip_datarec_h.pk_xt is null and dip_datarec_h.fpk='"+fpk+"'))  and (dip_datarec_h.isfolder='N' or dip_datarec_h.isfolder is null)  and nvl(dip_datarec_h.dr, 0) = 0 ");
//			liyunzhe modify 2012-06-04 ��ҵ���ĳ����β��� start
			DataReceiveTreeRefModel model=new DataReceiveTreeRefModel();
			model.setClassWherePart(" pk_xt='"+fpk+"'");
			pa1.setRefModel(model);
//			liyunzhe modify 2012-06-04 ��ҵ���ĳ����β��� end
		}
		setButEnable(false);
	}


	@Override
	protected void onBoSave() throws Exception {
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd !=null){
			bd.dataNotNullValidate();
		}
		//2011-7-2 cl ����Ψһ��У�飺����ʱ�����fpkΪnull����nodeID��fpk
		VOTreeNode node=getSelectNode();
		String fpk=(String) node.getParentnodeID();
		if(fpk==null || fpk.trim().equals("")){
			fpk=(String) node.getNodeID();
		}
		DipDatasynchVO thvo=(DipDatasynchVO) node.getData();
		DataSynchUI ui=(DataSynchUI) getBillUI();
		String pk=(String) ui.getBillCardPanel().getHeadItem("pk_datasynch").getValueObject();
		if(pk==null || pk.trim().equals("")){
			pk=" ";
		}
		//2011-7-2 cl ����Ψһ��У��
		String code=(String) this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("code").getValueObject();
		IUAPQueryBS bs=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//		Collection ccode=bs.retrieveByClause(DipDatasynchVO.class, " code='"+code+"' and pk_datasynch <> '"+pk+"' and pk_xt='"+thvo.getPk_xt()+"' and nvl(dr,0)=0 and isfolder='N'");
		Collection ccode=bs.retrieveByClause(DipDatasynchVO.class, " code='"+code+"' and pk_datasynch <> '"+pk+"' and pk_xt='"+thvo.getPk_xt()+"' and nvl(dr,0)=0 ");
		if(ccode !=null){
			if(ccode.size()>=1){
				ui.showWarningMessage(IContrastUtil.getCodeRepeatHint("����",code));
				return;
			}
		}
		
		//��ý���ԭʼ���ݵ�vo
		AggregatedValueObject billVO = getBillTreeCardUI().getChangedVOFromUI();
		setTSFormBufferToVO(billVO);

		//��ý���������vo
		AggregatedValueObject checkVO = getBillTreeCardUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);

		Object o = null;
		ISingleController sCtrl = null;

		boolean isSave = true;
		if (billVO.getParentVO() == null && (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0))
			isSave = false;
		else if (getBillUI().isSaveAndCommitTogether())
			billVO = getBusinessAction().saveAndCommit(billVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), checkVO);
		else
			billVO = getBusinessAction().save(checkVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), checkVO);

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
//		setButEnable(true);
//		getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//		getSelfUI().getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
//		getSelfUI().updateButtonUI();
		getSelfUI().onTreeSelectSetButtonState(getSelectNode());
	}

	@Override
	protected void onBoDelete() throws Exception {

		Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(),"��ʾ","�Ƿ�Ҫɾ��?");
		if(flag==1){
			String pk_node=((DataSynchUI)getBillUI()).selectnode;
			if("".equals(pk_node)){
				NCOptionPane.showMessageDialog(this.getBillUI(),"��ѡ��Ҫɾ���Ľڵ㣡");
				return ;
			}
			DipDatasynchVO vo= (DipDatasynchVO) HYPubBO_Client.queryByPrimaryKey(DipDatasynchVO.class, pk_node);
			if(vo==null){
				getSelfUI().showWarningMessage("ϵͳ�ڵ㲻����ɾ��������");
				return;
			}
			VOTreeNode node1=(VOTreeNode) getSelectNode().getParent();
			if(vo!=null)
				HYPubBO_Client.delete(vo);
//			������
			this.getBillTreeCardUI().getBillTreeData().deleteNodeFromTree(this.getBillTreeCardUI().getBillTreeSelectNode());

			if(node1!=null){
			getSelfUI().getBillTree().setSelectionPath(new javax.swing.tree.TreePath(node1
								.getPath()));
			}
		}
		//������
		
	}
	//����У�鵯����
   public void dataCheck(){
	   HashMap map = new HashMap();
  		map.put("pk", getSelfUI().selectnode);
  		map.put("type",SJUtil.getYWnameByLX(IContrastUtil.YWLX_TB));
//  		map.put("type",getSelfUI().getYwlx());
  		VOTreeNode node=getSelectNode();
  		if(SJUtil.isNull(node)){
  			this.getSelfUI().showWarningMessage("��ѡ��ڵ㣡");
  			return;
  		}
  		DipDatasynchVO vo=(DipDatasynchVO) node.getData();
  		
  		String fpk=vo.getFpk();
  		if(fpk==null||fpk.length()<=0){
  			this.getSelfUI().showWarningMessage("ϵͳ�ڵ㲻���Ա༭��");
  			return;
  		}
  		map.put("code", vo.getCode());
  		map.put("name", vo.getName());
		DatarecDlg dlg = new DatarecDlg(getSelfUI(),new UFBoolean(true),map);
		dlg.show();
   }

	/**
	 * �Զ��尴ť��ͬ��������У�顢Ԥ���Ķ����¼�
	 */
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		if(intBtn==IBtnDefine.Synch){
			onBoTB();
			//�ð�ť�Ķ����¼�
		}else if(intBtn==IBtnDefine.DataValidate){
              this.dataCheck();
		}
		/**
		 * @author �Ž�˫
		 * Ԥ���趨
		 */
		else if(intBtn==IBtnDefine.EarlyWarning){
			onBoWarning();
		}else if(intBtn==IBtnDefine.CONTROL){
			onBoControl();
		}else if(intBtn==IBtnDefine.TBFORM){
			onBoTbForm();
		}else if(intBtn==IBtnDefine.addFolderBtn){
			onBoAddFolder();
		}else if(intBtn==IBtnDefine.editFolderBtn){
			onBoEditFolder();
		}else if(intBtn==IBtnDefine.delFolderBtn){
			onBoDeleteFolder();
		}else if(intBtn==IBtnDefine.moveFolderBtn){
			onBoMoveFolder();
		}
		
	}
	/**
	 * @desc �ļ��ƶ�
	 * */
	public void onBoMoveFolder() throws Exception{
		
		nc.vo.dip.datasynch.MyBillVO billvo=(nc.vo.dip.datasynch.MyBillVO) getBufferData().getCurrentVO();
		if(billvo!=null&&billvo.getParentVO()!=null){
			SuperVO hvo=(SuperVO) billvo.getParentVO();
			MovefolderDLG dlg=new MovefolderDLG(getBillUI(),IContrastUtil.YWLX_TB,hvo);
			dlg.showModal();
			String ret=dlg.getRes();
			if(ret!=null){
				hvo.setAttributeValue((String)IContrastUtil.getFpkMap().get(IContrastUtil.YWLX_TB), ret);
				HYPubBO_Client.update(hvo);
				hvo=(DipDatasynchVO) HYPubBO_Client.queryByPrimaryKey(DipDatasynchVO.class, hvo.getPrimaryKey());
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
	public void onBoDeleteFolder()  throws Exception{
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
		DipDatasynchVO vo =(DipDatasynchVO) tempNode.getData();
		VOTreeNode node1=(VOTreeNode) tempNode.getParent();		
		if(vo.getIsfolder().booleanValue()){
			SuperVO[] hvos=HYPubBO_Client.queryByCondition(DipDatasynchVO.class, "fpk='"+vo.getPrimaryKey()+"'");
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

	public void onBoAddFolder()  throws Exception{
		VOTreeNode tempNode = getSelectNode();
		if(tempNode==null||tempNode.getData()==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ���ӵĽڵ㣡");
			return ;
		}
		DipDatasynchVO hvo=(DipDatasynchVO) tempNode.getData();
		if(!hvo.getIsfolder().booleanValue()){
			getSelfUI().showErrorMessage("�����ļ��в����������ļ��в�����");
			return ;
		}
		DipDatasynchVO newhvo=new DipDatasynchVO();
		newhvo.setFpk(hvo.getPrimaryKey());
		newhvo.setPk_xt(hvo.getPk_xt());
		newhvo.setIsfolder(new UFBoolean(true));
		DipDatasynchVO[] listvos=(DipDatasynchVO[]) HYPubBO_Client.queryByCondition(DipDatasynchVO.class, "pk_xt='"+hvo.getPk_xt()+"' and isfolder='Y' and nvl(dr,0)=0");
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
				e.printStackTrace();
			}
			try {
				newhvo=(DipDatasynchVO) HYPubBO_Client.queryByPrimaryKey(DipDatasynchVO.class, pk);
				
				if (getUITreeCardController().isAutoManageTree()) {	
					getSelfUI().insertNodeToTree(newhvo);
				}
			} catch (UifException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void onBoEditFolder() throws Exception{
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
		DipDatasynchVO vo =(DipDatasynchVO) tempNode.getData();
		if(vo.getIsfolder().booleanValue()){
			DipDatasynchVO[] listvos=(DipDatasynchVO[]) HYPubBO_Client.queryByCondition(DipDatasynchVO.class, "fpk='"+str+"' and isfolder='Y' and pk_datasynch<>'"+vo.getPrimaryKey()+"' and nvl(dr,0)=0");
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
					vo=(DipDatasynchVO) HYPubBO_Client.queryByPrimaryKey(DipDatasynchVO.class, vo.getPrimaryKey());
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
	public void onBoTbForm() throws UifException{
		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			this.getSelfUI().showWarningMessage("��ѡ��Ҫ�����Ľڵ㣡");
			return;
		}
		Object fpk=node.getParentnodeID();
		if(fpk==null){
			getSelfUI().showErrorMessage("��ѡ���ӽڵ����!");
			return;
		}
		String pk=node.getNodeID().toString();
		DipDatasynchVO hvo=(DipDatasynchVO) HYPubBO_Client.queryByPrimaryKey(DipDatasynchVO.class, pk);
		if(hvo==null){
			getBillUI().showErrorMessage("��ѡ��");
			return;
		}
		DipDatarecHVO dhvo=(DipDatarecHVO) HYPubBO_Client.queryByPrimaryKey(DipDatarecHVO.class, hvo.getDataname());
		if(dhvo==null){
			getBillUI().showErrorMessage("��Ӧ��ͬ��������ɾ��");
			return;
		}
		String sourcetype=dhvo.getSourcetype();
		DipDataoriginVO ovo=(DipDataoriginVO)HYPubBO_Client.queryByPrimaryKey(DipDataoriginVO.class, sourcetype);
		if(ovo==null){
			getBillUI().showErrorMessage("û���ҵ�ͬ������");
			return;
		}
		String sourcetypename = ovo.getName();
		if (!"��ʽ�ļ�".equals(sourcetypename)){
			getBillUI().showErrorMessage("���Ǹ�ʽ�ļ���Ӧ��ͬ��������ʹ��ǰ̨ͬ����");
			return;
		}
		String path="";
		JFileChooser jfileChooser = new JFileChooser();
		if(dhvo.getIoflag()==null||dhvo.getIoflag().toString().trim().equals("")){
			getSelfUI().showErrorMessage("���������־����Ϊ�գ�");
			return;
		}
		if(dhvo.getIoflag().equals("����")){
			jfileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			if (jfileChooser.showSaveDialog(getSelfUI()) == javax.swing.JFileChooser.CANCEL_OPTION)
				return;
			path= jfileChooser.getSelectedFile().toString();
			if(dhvo.getIoflag()!=null&&dhvo.getIoflag().equals("����")&&!(path.endsWith(".xml")||path.endsWith(".txt")||path.endsWith("xls"))){
				getSelfUI().showErrorMessage("��ѡ��xml/txt/xls�ļ����ͣ�");
				return;
			}
			if(onUpload(path)){
				ITaskExecute ite=(ITaskExecute) NCLocator.getInstance().lookup(ITaskExecute.class.getName());
				RetMessage rm=ite.doTbTaskFrom(node.getNodeID().toString(), path);
				if(rm.getIssucc()){
					
					MessageDialog.showHintDlg(getSelfUI(), "ǰ̨ͬ������", "ǰ̨ͬ������ɹ�");
				}else{
					getSelfUI().showErrorMessage(rm.getMessage());
				}
			}else{
				getSelfUI().showErrorMessage("�ļ��ϴ�ʧ�ܣ�");
				return;
			}
		}else{
			jfileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			jfileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
			if (jfileChooser.showSaveDialog(getSelfUI()) == javax.swing.JFileChooser.CANCEL_OPTION)
				return;
			
			path= jfileChooser.getSelectedFile().toString();
			ITaskExecute ite=(ITaskExecute) NCLocator.getInstance().lookup(ITaskExecute.class.getName());
			RetMessage rm=ite.doTbTaskFrom(node.getNodeID().toString(), path);
			if(rm.getIssucc()){
				download(path,rm.getfilename());
				List fil=rm.getfilename();
				String filename="";
				if(fil!=null&&fil.size()>0){
					filename=",�ļ�����·��Ϊ��"+path+"\nͬ�����ļ�Ϊ��\n";
					for(int i=0;i<fil.size();i++){
						filename=filename+fil.get(i).toString().substring(fil.get(i).toString().lastIndexOf("/")+1)+"\n";
					}
				}
				MessageDialog.showHintDlg(getSelfUI(), "ǰ̨���ͬ��", "ǰ̨ͬ������ɹ�"+filename);
			}else{
				getSelfUI().showErrorMessage(rm.getMessage());
			}
		}
	}
	public void onBoControl(){

		VOTreeNode node=getSelectNode();
		if(SJUtil.isNull(node)){
			getSelfUI().showErrorMessage("��ѡ��Ҫ�����Ľڵ�");
			return;
		}
		DipDatasynchVO hvo=null;
		try {
			hvo = (DipDatasynchVO) HYPubBO_Client.queryByPrimaryKey(DipDatasynchVO.class, node.getNodeID().toString());
		} catch (UifException e1) {
			e1.printStackTrace();
		}
		if(SJUtil.isNull(hvo)){
			getSelfUI().showErrorMessage("�˽ڵ㻹û�б��棬��༭��");
			return;
		}

		ControlHVO chvo=new ControlHVO();
		chvo.setBustype(SJUtil.getYWnameByLX(IContrastUtil.YWLX_TB));
//		chvo.setBustype(getSelfUI().getYwlx());
		chvo.setCode(hvo.getCode());
		chvo.setName(hvo.getName());

		ControlDlg cd=new ControlDlg(getSelfUI(),chvo,hvo.getPrimaryKey(),IContrastUtil.YWLX_TB,hvo.getDatasite());
		cd.showModal();
	}
	String hpk;
	private void onBoTB() {
		VOTreeNode tempNode = getSelectNode();
		if(SJUtil.isNull(tempNode)){
			getSelfUI().showErrorMessage("��ѡ��Ҫ�����Ľڵ㣡");
			return;
		}
		Object fpk=tempNode.getParentnodeID();
		if(fpk==null){
			getSelfUI().showErrorMessage("��ѡ���ӽڵ���в�����");
			return;
		}
		DipDatasynchVO hvo=(DipDatasynchVO) tempNode.getData();
		//��ȡ����ͬ������
		hpk=hvo.getPk_datasynch();
		
		DipDatarecHVO datahvo=null;
		try {
			String pk=iqf.queryfield(" select dataname from Dip_Datasynch where pk_datasynch='"+hpk+"' and nvl(dr,0)=0 ");
			datahvo=(DipDatarecHVO) iqf.querySupervoByPk(DipDatarecHVO.class, pk);
//			datarecHvoArry=(DipDatarecHVO[]) HYPubBO_Client.queryByCondition(DipDatarecHVO.class, " pk_datarec_h=( select dataname from Dip_Datasynch where pk_datasynch='"+hpk+"' and nvl(dr,0)=0 ) and nvl(dr,0)=0 ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(datahvo==null){
			getSelfUI().showErrorMessage("û���ҵ���Ӧ��ͬ������");
		}else{
			if(IContrastUtil.DATAORIGIN_ZJB.equals(datahvo.getSourcetype())||IContrastUtil.DATAORIGIN_WEBSERVICEZQ.equals(datahvo.getSourcetype())){//ȥУ��ip���û�������Դ�Ƿ���Է���
					hpk=hpk+","+Desktop.getApplet().getParameter("USER_IP");
				}
		}
		
		new Thread() {
			@Override
			public void run() {
				BannerDialog dialog = new BannerDialog(getSelfUI());
				dialog.setTitle("����ͬ�������Ժ�...");
				dialog.setStartText("����ͬ�������Ժ�...");
							
				try {
					dialog.start();
					ITaskExecute ite=(ITaskExecute) NCLocator.getInstance().lookup(ITaskExecute.class.getName());
					RetMessage rt=ite.doTBTask(hpk);
					if(rt.getIssucc()){
						MessageDialog.showHintDlg(getSelfUI(), "����ͬ��", "����ɹ���"+(rt.getMessage()==null?"":rt.getMessage()));
					}else{
						getSelfUI().showErrorMessage(rt.getMessage());
					}
				} catch (Throwable er) {
					er.printStackTrace();
				} finally {
					dialog.end();
				}
			}			
		}.start();
	}


	/**
	 * @author �Ž�˫
	 * ����Ԥ��
	 * �趨Ԥ��
	 */
	public void onBoWarning() throws Exception {
		VOTreeNode tempNode = getSelectNode();
		if(SJUtil.isNull(tempNode)){
			getSelfUI().showErrorMessage("��ѡ��Ҫ�����Ľڵ㣡");
			return;
		}
		DipDatasynchVO hvo=(DipDatasynchVO) tempNode.getData();
		//��ȡ����ͬ������
		String hpk=hvo.getPk_datasynch();
		hvo=(DipDatasynchVO) HYPubBO_Client.queryByPrimaryKey(DipDatasynchVO.class, hpk);
		if(SJUtil.isNull(hvo)){
			getSelfUI().showErrorMessage("�˽ڵ㻹û�б��棬��༭��");
			return;
		}
		//��ǰ���ݵ�Ԥ��ҵ������
		String wartype=hvo.getTasktype();
		if(SJUtil.isNull(wartype)||wartype.length()==0){
			getSelfUI().showErrorMessage("��ѡ��Ԥ�����ͣ�");
			return;
		}
		/*//����Ԥ����
		ToftPanel toft = SFClientUtil.showNode("H4H2H5", IFuncWindow.WINDOW_TYPE_DLG);
		WarningSetClientUI ui = (WarningSetClientUI) toft;*/
       //��ȡ����ͬ������
		String pk_datasynch=hvo.getPk_datasynch();
		
		MyBillVO mybillvo=new MyBillVO();
		DipWarningsetVO[] warvo=(DipWarningsetVO[]) HYPubBO_Client.queryByCondition(DipWarningsetVO.class, " tasktype='"+wartype+"' and pk_bustab='"+pk_datasynch+"'");
		DipWarningsetVO vo=null;
		boolean isadd=false;
		if(SJUtil.isNull(warvo)||warvo.length==0){
			vo=new DipWarningsetVO();
			vo.setPk_bustab(pk_datasynch);
			vo.setTasktype(wartype);
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
		String fpk=(String) tempNode.getParentnodeID();
		mybillvo.setParentVO(vo);
		
		//2011-7-18 
		//ui.init(mybillvo,isadd,fpk,0);
		WarningsetDlg wd=new WarningsetDlg(getSelfUI(),mybillvo, isadd,hvo.getPk_xt(),0);		
		wd.showModal();		
	}


	public void onTreeSelected(VOTreeNode arg0){
		//��ȡVO
		try {
			DipDatasynchVO vo=(DipDatasynchVO) arg0.getData();
			String pk_datasynch= vo.getPrimaryKey();

			SuperVO[] vos = HYPubBO_Client.queryByCondition(nc.vo.dip.datasynch.DipDatasynchVO.class, " pk_datasynch='" + pk_datasynch + "' and isnull(dr,0)=0  ");

			nc.vo.dip.datasynch.MyBillVO billvo =new nc.vo.dip.datasynch.MyBillVO();
			//DipDatasynchVO hvo=(DipDatasynchVO) HYPubBO_Client.queryByPrimaryKey(DipDatasynchVO.class, pk_datasynch);
			// ������VO
		//	billvo.setParentVO(hvo);
			billvo.setParentVO(vo);
			billvo.setChildrenVO(vos);
			// ��ʾ����
			getBufferData().addVOToBuffer(billvo);
			getBillTreeCardUI().getTreeToBuffer().put(arg0.getNodeID(), "" + (getBufferData().getVOBufferSize() - 1));
		} catch (UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}


	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		VOTreeNode treeNode=getSelectNode();
		if(treeNode==null){
			getSelfUI().showErrorMessage("��ѡ��Ҫ���ӵ�ϵͳ�ڵ㣡");
			return ;
		}
		DipDatasynchVO hvo=(DipDatasynchVO) treeNode.getData();
		String str=(String)treeNode.getParentnodeID();
		if(!hvo.getIsfolder().booleanValue()&&(hvo.getFpk()==null||hvo.getFpk().length()<=0)){
			getSelfUI().showErrorMessage("��ѡ���ļ��������Ӳ�����");
			return ;
		}
		super.onBoAdd(bo);
		// 2011-06-04 ����� ��
		getSelfUI().getBillCardPanel().getHeadItem("code").setEdit(true);
		getSelfUI().getBillCardPanel().getHeadItem("name").setEdit(true);
		
		//��treeNode.getNodeID()д�뵽ϵͳע�����������ֶ���
		getSelfUI().getBillCardPanel().setHeadItem("fpk", treeNode.getNodeID());
		getSelfUI().getBillCardPanel().setHeadItem("isfolder", new UFBoolean(false));
		getSelfUI().getBillCardPanel().setHeadItem("pk_xt", hvo.getPk_xt());
		
//		 * 2011-06-04 ����� ��
		UIRefPane pane=(UIRefPane) this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("dataname").getComponent();
//		DataReceiveRefModel model=new DataReceiveRefModel();
////		model.addWherePart(" and(( dip_datarec_h.pk_xt is not null and dip_datarec_h.pk_xt='"+hvo.getPk_xt()+"') or (dip_datarec_h.pk_xt is null and dip_datarec_h.fpk='"+hvo.getPk_xt()+"'))  and (dip_datarec_h.isfolder='N' or dip_datarec_h.isfolder is null) and (dip_datarec_h.sourcetype not in ('0001AA10000000013SVI','0001AA1000000003493X') or (dip_datarec_h.sourcetype='0001AA1000000003493X' and ioflag='����')  or (dip_datarec_h.sourcetype = '0001AA10000000013SVI'  and ((dip_datarec_h.ioflag = '���' and dip_datarec_h.databakfile='��Ϣ��' ) or dip_datarec_h.databakfile='�ļ���') )) and nvl(dip_datarec_h.dr, 0) = 0 ");
//		model.addWherePart(" and(( dip_datarec_h.pk_xt is not null and dip_datarec_h.pk_xt='"+hvo.getPk_xt()+"') or (dip_datarec_h.pk_xt is null and dip_datarec_h.fpk='"+hvo.getPk_xt()+"'))  and (dip_datarec_h.isfolder='N' or dip_datarec_h.isfolder is null)  and nvl(dip_datarec_h.dr, 0) = 0 ");
//		pane.setRefModel(model);
		//liyunzhe modify 2012-06-04 ��ҵ���ĳ����β��� start
		DataReceiveTreeRefModel model=new DataReceiveTreeRefModel();
		model.setClassWherePart(" pk_xt='"+hvo.getPk_xt()+"'");
		pane.setRefModel(model);
//		liyunzhe modify 2012-06-04 ��ҵ���ĳ����β��� end
		setButEnable(false);
	}

	public void setButEnable(boolean flag){
		getButtonManager().getButton(IBtnDefine.DataValidate).setEnabled(flag);
   		getButtonManager().getButton(IBtnDefine.CONTROL).setEnabled(flag);
   		getButtonManager().getButton(IBtnDefine.EarlyWarning).setEnabled(flag);
   		getButtonManager().getButton(IBtnDefine.TBFORM).setEnabled(flag);
		this.getButtonManager().getButton(IBtnDefine.Synch).setEnabled(flag);
   	 	try {
			getSelfUI().updateButtonUI();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCancel();
		getSelfUI().onTreeSelectSetButtonState(getSelectNode());
		
//		DipDatasynchVO hvo=(DipDatasynchVO) getSelectNode().getData();
//		if(hvo!=null&&hvo.getIsfolder()!=null&&hvo.getIsfolder().booleanValue()){
//			setButEnable(false);
//			getSelfUI().getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
//			getSelfUI().getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
//			getSelfUI().getButtonManager().getButton(IBtnDefine.addFolderBtn).setEnabled(false);
//		}else{
//			setButEnable(true);	
//			getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
//			getSelfUI().getButtonManager().getButton(IBtnDefine.folderManageBtn).setEnabled(false);
//			
//		}
//		getSelfUI().updateButtonUI();
	}


	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
		getButtonManager().getButton(IBillButton.Add).setEnabled(false);
		setButEnable(false);
	}
	
	/**
	 * �ϴ��ļ�����
	 * ��������:(2003-2-28 10:15:04)
	 */
	private boolean onUpload(String localFilePath) {
		boolean ret=true;
		java.io.File file = new java.io.File(localFilePath);
		byte ba[] = new byte[(int) file.length()];
		try {
			FileInputStream fis = new FileInputStream(file);
			int nBebinReadLoc = 0;
			do {
				int nReadedInSize = fis.read(ba, nBebinReadLoc, ba.length
						- nBebinReadLoc);
				nBebinReadLoc += nReadedInSize;
			} while (nBebinReadLoc < ba.length);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		String rs = null;
		try {
			rs = iqf.upLoadFile(localFilePath, ba);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(rs!=null){
			ret=false;
		}
		return ret;
	}
	/**
	 * �ϴ��ļ�����
	 * ��������:(2003-2-28 10:15:04)
	 */
	private boolean download(String localFilePath, List<String> filenames) {
		boolean ret=true;
		String path=localFilePath;
		if(localFilePath.indexOf(".")>=0){
			localFilePath=localFilePath.replace("\\", "/");
			localFilePath=localFilePath.substring(0,localFilePath.lastIndexOf("/"));
		}
		File parentDir = new File(path);
		if (!parentDir.exists()) {
			parentDir.mkdirs();
		}
		IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		if(filenames!=null&&filenames.size()>0){
			for(int i=0;i<filenames.size();i++){

				byte[] ba=null;
				try {
					ba = iqf.downLoadFile(filenames.get(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(ba!=null&&ba.length>0){
					try {

						writeFile(localFilePath + filenames.get(i).substring( filenames.get(i).lastIndexOf("/")), ba);

					} catch (Exception e) {
						Logger.error("error",e);
						return false/* @res "�ϴ�ʧ��!" */;
					}
				}
			}
		}
		return ret;
	}
	public  void writeFile(String filePath, byte[] ba) {

        File file = new File(filePath);
        java.io.FileOutputStream out=null;
        try {
        	
            String dir = filePath.substring(0, filePath.lastIndexOf("/"));
            File dirFile = new File(dir);
            if (!dirFile.exists())
                dirFile.mkdirs();
            if(file.exists() && file.canWrite())
            	file.delete();
           out = new java.io.FileOutputStream(file);
            out.write(ba);
            out.flush();
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	if(out!=null){
        		try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
    }
}