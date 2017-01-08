package nc.ui.dip.sysregister;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.ufsoft.script.base.UfoBoolean;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.exception.DbException;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.util.NCOptionPane;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.pub.TableTreeNode;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.uif.pub.exception.UifException;
import nc.vo.dip.authorize.define.DipADContdataBVO;
import nc.vo.dip.authorize.define.DipADContdataVO;
import nc.vo.dip.authorize.maintain.DipADContdatawhHVO;
import nc.vo.dip.contdata.DipContdataVO;
import nc.vo.dip.contdatawh.ContdatawhHVO;
import nc.vo.dip.datachange.DipDatachangeBVO;
import nc.vo.dip.datachange.DipDatachangeHVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.dataproce.DipDataproceHVO;
import nc.vo.dip.datarec.DipDatarecHVO;
import nc.vo.dip.datasend.DipDatasendVO;
import nc.vo.dip.datasynch.DipDatasynchVO;
import nc.vo.dip.esbfilesend.DipEsbSendVO;
import nc.vo.dip.processflow.DipProcessflowBVO;
import nc.vo.dip.processflow.DipProcessflowHVO;
import nc.vo.dip.statemanage.DipStateManageHVO;
import nc.vo.dip.sysregister.DipSysregisterBVO;
import nc.vo.dip.sysregister.DipSysregisterHVO;
import nc.vo.dip.tyxml.DipTYXMLDatachangeBVO;
import nc.vo.dip.tyxml.DipTYXMLDatachangeHVO;
import nc.vo.dip.tyzhq.DipTYZHDatachangeHVO;
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
	@Override
	protected void onBoLinePaste() throws Exception {
		// TODO Auto-generated method stub
		super.onBoLinePaste();
		int row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		getSelfUI().getBillCardPanel().setBodyValueAt(null, row, new DipSysregisterBVO().getPKFieldName());
		getSelfUI().getBillCardPanel().setBodyValueAt(new UFBoolean(false), row, new DipSysregisterBVO().ISYY);
		getSelfUI().getBillCardPanel().setBodyValueAt(new UFBoolean(false), row, new DipSysregisterBVO().ISSTOP);
	}

	BillManageUI billUI;
	IControllerBase control;

//	//��ѯʱ,��ϵͳĬ�ϼ��ص�and (isnull(dr,0)=0) and pk_corp='100X'��ѯ����ȥ��
//	protected String getHeadCondition() {
//	return null;
//	}

	public MyEventHandler(BillTreeCardUI clientUI, ICardController control) {
		super(clientUI, control);
	}

	/**
	 * ȡ�õ�ǰUI��
	 * 
	 * @return
	 */
	private SysRegisterClientUI getSelfUI() {
		return (SysRegisterClientUI) getBillUI();
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
		newBodyVO.setAttributeValue("pk_sysregister_h", parent == null ? null : parent.getNodeID());

		// ��ӽ���
		return newBodyVO;
	}

	/**
	 * 
	 * <P>�˷���Ϊ���Ǹ��෽��
	 * <BR>�������е�ISingleController�ж�ȥ�������򱣴�ʱ����ͷΪ��
	 * @throws Exception
	 * @see nc.ui.trade.treecard.TreeCardEventHandler#onBoSave()
	 */
	protected void onBoSave() throws Exception {
		changeStartStat(true);
		
//		����
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<k;i++){
			Boolean isstop=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DipSysregisterBVO().ISSTOP);
			Boolean isyy=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DipSysregisterBVO().ISYY);
			String code=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DipSysregisterBVO().SITECODE);
			String name=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DipSysregisterBVO().SITENAME);
			
			if((code==null||code.trim().equals(""))&&(name==null||name.trim().equals(""))&&(isstop==null||isstop==false)&&(isyy==null||isyy==false)){
//				getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(i, 0, false, false);
//				getBillCardPanelWrapper().getBillCardPanel().delLine();
				list.add(i);
			}
		}
		if(list!=null&&list.size()>0){
			int in[]=new int[list.size()];
			for(int i=0;i<list.size();i++){
				in[i]=list.get(i);
			}
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().delLine(in);
		}
		
		
		
		
		
		
		
		//У��ҳ�������Ƿ�Ϊ��
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd !=null){
			bd.dataNotNullValidate();
		}
		//��ȡ��ǰui
		SysRegisterClientUI ui=(SysRegisterClientUI)getBillUI();
		String pk=(String)getSelfUI().getBillCardWrapper().getBillCardPanel().getHeadItem("pk_sysregister_h").getValueObject();
		if(pk==null||pk.trim().equals("")){
			pk=" ";
		}
		String code=(String) ui.getBillCardWrapper().getBillCardPanel().getHeadItem("code").getValueObject();
		String extcode=(String)ui.getBillCardWrapper().getBillCardPanel().getHeadItem("extcode").getValueObject();
		String sitecode=(String) ui.getBillCardWrapper().getBillCardPanel().getBodyItem("sitecode").getValueObject();
//		if(sitecode.length()!=4){
//		ui.showErrorMessage("�ֲ�ʽվ�����Ϊ4λ��");
//		return;
//		}
		/*
		 * ע�͵�������ⲿϵͳ������λ�����޶�
		 * 2011-5-8
		 * if(code.length()!=4){
				getSelfUI().showErrorMessage("���볤������λ��");
				return;
			}
			if(extcode.length()!=4){
				ui.showErrorMessage("�ⲿϵͳ����Ϊ4λ");
				return;
			}*/

		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		Collection ccode=bs.retrieveByClause(DipSysregisterHVO.class,"code='"+code+"' and  isnull(dr,0)=0 and pk_sysregister_h<>'"+pk+"'");
		if(ccode!=null&&ccode.size()>=1){

		ui.showWarningMessage("�á�"+code+"�������Ѿ����ڣ�");
		return;

		}

		Collection cname=bs.retrieveByClause(DipSysregisterHVO.class, "extcode='"+extcode+"' and isnull(dr,0)=0 and pk_sysregister_h <>'"+pk+"'");
		if(cname!=null&&cname.size()>=1){
		ui.showWarningMessage("�á�"+extcode+"���ⲿϵͳ�����Ѿ����ڣ�");
		return;
		}
//		String sitecode=(String) getSelfUI().getBillCardWrapper().getBillCardPanel().getBodyItem("sitecode").getValueObject();

		/*
		 * ע�͵��ֲ�ʽվ�������λ�޶�
		 * 2011-5-8
		 * if(sitecode.length()!=4){
					getSelfUI().showErrorMessage("�ֲ�ʽվ�����Ϊ4λ");
					return;
				}*/
//		Collection csitecode=bs.retrieveByClause(DipSysregisterBVO.class,"sitecode='"+sitecode+"' and isnull(dr,0)=0 and pk_sysregister_h<>'"+pk+"'");
//		if(csitecode!=null&&csitecode.size()>=1){
//		getSelfUI().showWarningMessage("�á�+sitecode+���ֲ�ʽվ������Ѵ��ڣ�");
//		return;
//		}


		//��ý���ԭʼ���ݵ�vo
		AggregatedValueObject billVO = getBillTreeCardUI().getChangedVOFromUI();
		setTSFormBufferToVO(billVO);



		//��ý���������vo
		AggregatedValueObject checkVO = getBillTreeCardUI().getVOFromUI();
		
		//2011-5-14 ���� ����Ƿֲ�ϵͳ����������Ϊ�� begin
		DipSysregisterHVO hvo=(DipSysregisterHVO) checkVO.getParentVO();
		int row=this.getBillCardPanelWrapper().getBillCardPanel().getRowCount();
		if(hvo.getIsdeploy()!=null&&hvo.getIsdeploy().equals("Y")){
			if(row==0){
				getSelfUI().showWarningMessage("���岻��Ϊ�գ�");
				return;
			}
		}else{
			if(row>0){
				getSelfUI().showWarningMessage("��ɾ������");
				return;
			}
		}
		//end
		
		//setTSFormBufferToVO(billVO);
		//�жϱ�������ֻ��ѡȡһ��

	
		Object o = null;
		ISingleController sCtrl = null;

		boolean isSave = true;
		if (billVO.getParentVO() == null && (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0))
			isSave = false;
		else if (getBillUI().isSaveAndCommitTogether())
			billVO = getBusinessAction().saveAndCommit(billVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), checkVO);
		else{

			if(getSelfUI().delstr.trim().length()>0){
				String sql="update dip_sysregister_b set dr=1 where pk_sysregister_b in("+getSelfUI().delstr.substring(0,getSelfUI().delstr.length()-1)+")";
				IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
				iq.exesql(sql);
			}
			//����ʱ��checkVO����billVO�����Ᵽ��ʱ��ͷ��ֵΪ��
			if(pk.length()==20){
				billVO.setParentVO(checkVO.getParentVO());

			}
			if(checkVO.getParentVO().getPrimaryKey()!=null&&checkVO.getParentVO().getPrimaryKey().length()>0){
//				HYPubBO_Client.deleteByWhereClause(DipSysregisterBVO.class, "pk_sysregister_h='"+checkVO.getParentVO().getPrimaryKey()+"'");
				HYPubBO_Client.update((DipSysregisterHVO)checkVO.getParentVO());
			}else{
				DipSysregisterHVO hvo1=(DipSysregisterHVO) checkVO.getParentVO();
				pk=HYPubBO_Client.insert(hvo1);
				hvo1.setPk_sysregister_h(pk);
				billVO.setParentVO(hvo1);
			}
			if(checkVO.getChildrenVO()!=null&&checkVO.getChildrenVO().length>0){
				List listupdate=new ArrayList<DipSysregisterBVO>();
				List listinsert=new ArrayList<DipSysregisterBVO>();
				DipSysregisterBVO[] bvos=(DipSysregisterBVO[]) checkVO.getChildrenVO();
				if(bvos!=null&&bvos.length>0){
					for(int i=0;i<bvos.length;i++){
						if(bvos[i].getPk_sysregister_b()!=null&&bvos[i].getPk_sysregister_b().length()>0){
							listupdate.add(bvos[i]);
						}else{
							bvos[i].setPk_sysregister_h(pk);
							listinsert.add(bvos[i]);
						}
					}
				}
				
				if(listupdate.size()>0){
					DipSysregisterBVO updatarry[]=new DipSysregisterBVO[listupdate.size()];
					updatarry=(DipSysregisterBVO[]) listupdate.toArray(updatarry);
					HYPubBO_Client.updateAry(updatarry);
				}
				if(listinsert.size()>0){
					DipSysregisterBVO insertarry[]=new DipSysregisterBVO[listinsert.size()];
					insertarry=(DipSysregisterBVO[]) listinsert.toArray(insertarry);
					HYPubBO_Client.insertAry(insertarry);
				}
			}
			if(HYPubBO_Client.queryByCondition(DipSysregisterBVO.class,  "pk_sysregister_h='"+checkVO.getParentVO().getPrimaryKey()+"' and nvl(dr,0)=0")!=null){
				billVO .setChildrenVO(HYPubBO_Client.queryByCondition(DipSysregisterBVO.class,  "pk_sysregister_h='"+checkVO.getParentVO().getPrimaryKey()+"' and nvl(dr,0)=0"));	
			}
//			billVO.setParentVO(HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, checkVO.getParentVO().getPrimaryKey()));
			//getBusinessAction().save(checkVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), checkVO);


		}
		if (sCtrl != null && sCtrl.isSingleDetail())
			billVO.setParentVO((CircularlyAccessibleValueObject) o);
		int nCurrentRow = -1;
		if (isSave) {



			if (isEditing()){
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(billVO);
					nCurrentRow = 0;
				} else {
					getBufferData().setCurrentVO(billVO);
					nCurrentRow = getBufferData().getCurrentRow();

				}}else{
					getBufferData().addVOToBuffer(billVO);
//					int w=getBufferData().getVOBufferSize();
//					getBufferData().setCurrentRow(w);
//					getBufferData().refresh();
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
		
//		onBoRefresh();
//		getSelectNode();
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


	@Override
	protected void setTSFormBufferToVO(AggregatedValueObject setVo) throws Exception {
		// TODO Auto-generated method stub
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
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		//super.onBoDelete();
		VOTreeNode tempNode=getSelectNode();
	    TableTreeNode node1=(TableTreeNode) tempNode.getParent();
		if("".equals(tempNode)){
			NCOptionPane.showMessageDialog(this.getBillUI(),"��ѡ��Ҫɾ����ϵͳ��");
			return ;
		}

		String pk_sysregister_h=(String) tempNode.getNodeID();
		/*
		 * ���ncϵͳ�Ϳ�ϵͳ���������޸�
		 * 2011-5-24
		 * zlc*/
		if(pk_sysregister_h==null||pk_sysregister_h.equals("")){
			getSelfUI().showWarningMessage("��ǰ�ڵ㲻���޸ģ�");
			return;
		}else{

			DipSysregisterHVO vo=(DipSysregisterHVO) tempNode.getData();
			if(vo.getCode().equals("0000")){
			    getSelfUI().showWarningMessage("NCϵͳ����ɾ����");
			    return ;
			}
//			super.onBoEdit();          // ytq 2011-07-02 ע�͵�

		}
		
		
		int ret=getSelfUI().showOkCancelMessage("ȷ��Ҫɾ��ϵͳ���Ҽ���ɾ�����нڵ��и�ϵͳ���������ݣ�");
		if(ret!=1){
			return ;
		}else{
			DipSysregisterHVO vo = (DipSysregisterHVO) HYPubBO_Client.queryByPrimaryKey(DipSysregisterHVO.class, pk_sysregister_h);
			if(vo!=null)
			{
				try {
					//���ݶ����
					String tablename = null;
					String where = null ;
//					String sql="update "+tablename+"set dr=1 where "+where+"='"+pk_sysregister_h+"'";
					String pk="pk_xt";
					String[][] tablenames=new String[][]{
							//ϵͳע��
							{new DipSysregisterHVO().getTableName(),new DipSysregisterHVO().getPKFieldName(),"PK_SYSREGISTER_H",new DipSysregisterBVO().getTableName()},
						    //���ݶ���
							{new DipDatadefinitHVO().getTableName(),new DipDatadefinitHVO().getPKFieldName(),pk,new DipDatadefinitBVO().getTableName()},
							//����ͬ��
							{new DipDatarecHVO().getTableName() , new DipDatarecHVO().getPKFieldName(),pk},
							//����ͬ������
							{new DipDatasynchVO().getTableName() ,new DipDatasynchVO().getPKFieldName(),pk},
							//���ݶ��ն���
							{new DipContdataVO().getTableName(),new DipContdataVO().getPKFieldName(),pk},
							//���ݶ���ά��
							{new ContdatawhHVO().getTableName(),new ContdatawhHVO().getPKFieldName(),pk},
							//���ݼӹ�
							{new DipDataproceHVO().getTableName(),new DipDataproceHVO().getPKFieldName(),pk},
							//����ת��
							{new DipDatachangeHVO().getTableName(),new DipDatachangeHVO().getPKFieldName(),pk,new DipDatachangeBVO().getTableName()},
							//���ݷ���
							{new DipDatasendVO().getTableName(),new DipDatasendVO().getPKFieldName(),pk},
							//�ļ�����
							{new DipEsbSendVO().getTableName(),new DipEsbSendVO().getPKFieldName(),pk},
							//��������
							{new DipProcessflowHVO().getTableName(),new DipProcessflowHVO().getPKFieldName(),pk,new DipProcessflowBVO().getTableName()},
							//����Ȩ�޶���
							{new DipADContdataVO().getTableName(),new DipADContdataVO().getPKFieldName(),pk,new DipADContdataBVO().getTableName()},
							//����Ȩ��ά��
							{new DipADContdatawhHVO().getTableName(),new DipADContdatawhHVO().getPKFieldName(),pk},
							//����Ȩ�����
//							{new DipADDatalookVO().getTableName()}
							//ͨ������ת����
							{new DipTYZHDatachangeHVO().getTableName(),new DipTYZHDatachangeHVO().getPKFieldName(),pk},
							{new DipStateManageHVO().getTableName(),new DipStateManageHVO().getPKFieldName(),pk},
							{new DipTYXMLDatachangeHVO().getTableName(),new DipTYXMLDatachangeHVO().getPKFieldName(),pk,new DipTYXMLDatachangeBVO().getTableName()}
							};
					
					List<String> list=new ArrayList<String>();
					for(int i=0;i<tablenames.length;i++){
						String[] field=tablenames[i];
						if(field.length==4){//˵�����ӱ�Ҫɾ��
							if(field[0]!=null&&field[0].equals("dip_datarec_h")){//����ͬ�����ܣ�Ҫɾ�����ݸ�ʽ
								//ɾ�����ݸ�ʽ�ӱ�
								list.add("update dip_dataformatdef_b b set b.dr='1' where b.pk_dataformatdef_h in( select forh.pk_dataformatdef_h from dip_dataformatdef_h forh where forh.pk_datarec_h in(select reh.pk_datarec_h from dip_datarec_h reh where reh.pk_xt='"+pk_sysregister_h+"' and nvl(reh.dr,0)=0 ) and nvl(forh.dr,0)=0) and nvl(b.dr,0)=0 ");
								//ɾ�����ݸ�ʽ����
								list.add("update dip_dataformatdef_h forh set forh.dr='1' where forh.pk_datarec_h in(select reh.pk_datarec_h from dip_datarec_h reh where reh.pk_xt='"+pk_sysregister_h+"' and nvl(reh.dr,0)=0 ) and nvl(forh.dr,0)=0");
							}
							tablename=field[3];//�ӱ�
							list.add("update "+tablename+" set dr=1 where  nvl(dr,0)=0 and "+field[1]+" in( select "+field[1]+" from "+field[0]+" hh where hh."+field[2]+"='"+pk_sysregister_h+"' and nvl(hh.dr,0)=0 )  ");
							
						}
						tablename=field[0];//����
						where=field[2];//������������ϵͳ�ֶ�
						list.add("update "+tablename+" set dr=1 where "+where+"='"+pk_sysregister_h+"' and nvl(dr,0)=0 ");
					}
					IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
					if(iqf.exectEverySql(list)){
					getSelfUI().showErrorMessage("ɾ������");
					return;
					
					}
					//���ݽ���
//					DipDatarecHVO ddrhvo=hyp
					//����ͬ��
					//���ݼӹ�
					//���ݶ��ն���
					//���ݶ���ά��
					//����ת��
					//���ݷ���

					onBoRefresh();
//					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
				//HYPubBO_Client.delete(vo);	
			
		}

	/*	if(vos!=null && vos.length>0){
			for(int i=0;i< vos.length;i++){
				isyy=vos[i].getIsyy().toString();
//				isyy=Boolean.parseBoolean(vos[i].getIsyy().toString());
				if(isyy=="Y"){
					getSelfUI().showWarningMessage("��ϵͳΪ�Ƿֲ�ʽϵͳ�������Ѿ������ã�����ɾ����");
					return ; 
				}else {
					for(DipSysregisterBVO bvo : vos ){


						int ret=getSelfUI().showOkCancelMessage("��ȷ��Ҫɾ����");
						if(ret!=1){
							return ;
						}

						HYPubBO_Client.delete(bvo);
					}
				}
			}
		}*/
	}

//	@Override
//	public void onTreeSelected(VOTreeNode arg0) {
//		// TODO Auto-generated method stub
//		super.onTreeSelected(arg0);
//		
//		SuperVO vo=(SuperVO) arg0.getData();
//		String pk_sysregister_h= (String) vo.getAttributeValue("pk_sysregister_h");
//		SuperVO[] vos;
//		try {
//			vos = HYPubBO_Client.queryByCondition(DipSysregisterBVO.class, "pk_sysregister_h='"+pk_sysregister_h+"' and isnull(dr,0)=0 order by sitecode");
//			nc.vo.dip.sysregister.MyBillVO billvo=new nc.vo.dip.sysregister.MyBillVO();
//			//������VO
//			billvo.setParentVO(vo);
//			//������VO
//			billvo.setChildrenVO(vos);
//			// ��ʾ����
////			getBufferData().clear();
//			getBufferData();
//			getBufferData().addVOToBuffer(billvo);
////			getSelfUI().getBillCardPanel().getBillModel("dip_sysregister_b").getDataVector();
////			getSelfUI().getBillCardPanel().getBillModel("dip_sysregister_b").clearBodyData();
////			getSelfUI().getBillCardPanel().getBillModel("dip_sysregister_b").setBodyDataVO(vos);
////			getSelfUI().updateUI();
////		getBufferData().add
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}

	@Override
	protected void onBoEdit() throws Exception {
		getSelfUI().delstr="";
		changeStartStat(false);
		//��ȡ��ǰui


		VOTreeNode tempNode=getSelectNode();


		String pk_sysregister_h=(String) tempNode.getNodeID();
		
//		DipSysregisterBVO[] vos=(DipSysregisterBVO[]) HYPubBO_Client.queryByCondition(DipSysregisterBVO.class, " pk_sysregister_h='"+pk_sysregister_h+"' and isnull(dr,0)=0  ");
//		UFBoolean bool=new UFBoolean(true);
//		String boole=bool.toString();
		/*for (int i=0;i<vos.length;i++){
				String  isyy=vos[i].getIsyy().toString();
				if(isyy.equals("Y")){
					this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isdeploy").setEnabled(false);
				}
			}*/
		if(pk_sysregister_h==null||pk_sysregister_h.equals("")){
			getSelfUI().showWarningMessage("��ǰ�ڵ㲻���޸ģ�");
			return;
		}else{

			DipSysregisterHVO vo=(DipSysregisterHVO) tempNode.getData();
			if(vo.getCode().equals("0000")){
			    getSelfUI().showWarningMessage("NCϵͳ�����޸�");
			    return ;
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isdeploy").setEnabled(false);

			}
			
			super.onBoEdit();
			/*�в�����ť״̬���ƣ��ֲ�ϵͳ�ſɽ����в���
			 * 2011-06-21
			 * zlc begin*/
			if(vo.getIsdeploy()==null||!vo.getIsdeploy().equals("Y")){
//				getButtonManager().getButton(IBillButton.Line).setEnabled(false);
//				getBillUI().updateButtonUI();	
			}else{
//				getButtonManager().getButton(IBillButton.Line).setEnabled(true);
//				getBillUI().updateButtonUI();
				onBoLineAdd();
			}
			/*end*/
			
		}
//		int rowcount=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
//		if(rowcount>0){
//			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isdeploy").setEnabled(false);
//		}
		
	}
	protected void onBoQuery(){
		String nodecode=this.billUI.getModuleCode();

	}
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		VOTreeNode tempNode=getSelectNode();
//		Integer count=SysRegisterCount();
//		if(count!=null&&count>=2){
//			getSelfUI().showErrorMessage("�ýӿ�ϵͳֻ��Ȩ��������ϵͳע��ڵ㣡");
//			return;
//		}
		if(tempNode!=null){
			getSelfUI().showErrorMessage("���ӽڵ㲻������");
			return;
		}
		

		//getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isdeploy").setEnabled(false);




//		changeStartStat(false);
		super.onBoAdd(bo);



	}

	public Integer SysRegisterCount(){
		String sql=" Select Count(distinct(hh.pk_xt)) From dip_datadefinit_h hh where nvl(dr,0)=0  ";
		IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		Integer count=null;
		try {
			String ss=iq.queryfield(sql);
			count=Integer.parseInt(ss);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}



	public void changeStartStat(boolean arg){

		getSelfUI().getButtonManager().getButton( IBtnDefine.Start).setEnabled(arg);
		getSelfUI().getButtonManager().getButton( IBtnDefine.Stop).setEnabled(arg);

	}	

	@Override
	protected void onBoCancel() throws Exception {
		super.onBoCancel();
//		changeStartStat(true);
//		super.onBoRefresh();
		this.getButtonManager().getButton(IBtnDefine.Start).setEnabled(true);
		this.getButtonManager().getButton(IBtnDefine.Stop).setEnabled(true);

	}

	@Override
	protected void onBoLineAdd() throws Exception {
		// TODO Auto-generated method stub
		String deploy=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("isdeploy").getValueObject().toString();
		if(!(new UFBoolean(deploy).booleanValue())){
			getSelfUI().showErrorMessage("���Ƿֲ�ʽϵͳ���ɽ��������в�����");
			return ;
		}
		super.onBoLineAdd();
	}

	@Override
	protected void onBoLineDel() throws Exception {
		int row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		if (row<0){
			getSelfUI().showErrorMessage("��ѡ��Ҫɾ�����У�");
			return;
		}
		boolean is= (Boolean) getSelfUI().getBillCardPanel().getBodyValueAt(row, new DipSysregisterBVO().ISYY);
		if(is){
			getSelfUI().showErrorMessage("��վ���Ѿ������ã�����ɾ����");
			return;
		}
//		super.onBoLineDel();
		String ob=getSelfUI().getBillCardPanel().getBillModel().getValueAt(row, "pk_sysregister_b")==null?"":getSelfUI().getBillCardPanel().getBillModel().getValueAt(row, "pk_sysregister_b").toString();
		if(ob==null||"".equalsIgnoreCase(ob)){
			super.onBoLineDel();
			//.getSelfUI().delstr = getSelfUI().delstr +",'"+ob.toString()+"'";
		}else{
			super.onBoLineDel();
			getSelfUI().delstr = getSelfUI().delstr +"'"+ob.toString()+"',";			
			String sb=ob.toString();
			System.out.println("ɾ��������"+sb+"��vo");
		}
	
	}



	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
		getSelfUI().getButtonManager().getButton(IBillButton.Add).setEnabled(false);
		getSelfUI().updateButtonUI();
	}


}