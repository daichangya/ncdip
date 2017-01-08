package nc.ui.dip.credence;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;

import nc.bs.dip.txt.TxtDataVO;
import nc.bs.excel.pub.ExpExcelVO;
import nc.bs.excel.pub.ExpToExcel;
import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.tyzhq.iniufoenv.InitUFOEnvDlg;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.treemanage.ITreeManageController;
import nc.vo.dip.credence.CredenceBVO;
import nc.vo.dip.credence.CredenceHVO;
import nc.vo.dip.credence.MyBillVO;
import nc.vo.dip.datachange.DipDatachangeBVO;
import nc.vo.dip.datachange.DipDatachangeHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

import org.apache.poi.hssf.usermodel.HSSFCell;
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
	public MyEventHandler(BillCardUI clientUI, ICardController control){
		super(clientUI,control);
	}
	public void loadData(CredenceHVO hvo){
		try {
//			MyBillVO billvo=new MyBillVO();
//			billvo.setParentVO(hvo);
//			billvo.setChildrenVO(HYPubBO_Client.queryByCondition(CredenceBVO.class, strWhere))
			getBufferData().clear();
			MyBillVO mbvo=new MyBillVO();			
			mbvo.setParentVO(hvo);
			CredenceBVO[] bvos=(CredenceBVO[]) HYPubBO_Client.queryByCondition(CredenceBVO.class, new CredenceBVO().getParentPKFieldName()+"='"+hvo.getPrimaryKey()+"'");
			CredenceBVO[] bvo=new CredenceBVO[1];
			if(bvos==null||bvos.length<=0){
				//for(int i=0;i<20;i++){
//					bvo[0]=new CredenceBVO();
//				//}
//				mbvo.setChildrenVO(bvo);
			}else{
//				if(bvos.length>=20){
//					mbvo.setChildrenVO(bvos);
//				}else{
//					for(int i=0;i<20;i++){
//						if(i<bvos.length){
//							bvo[i]=bvos[i];
//						}else{
//							bvo[i]=new CredenceBVO();
//						}
//					}
					mbvo.setChildrenVO(bvos);
				//}
			}
			getBufferData().addVOsToBuffer(new AggregatedValueObject[] { mbvo });
			getBufferData().setCurrentRow(0);

//			addDataToBuffer(new SuperVO[]{hvo});

//			updateBuffer();
//			getBufferData().setCurrentRow(0);
//			getBufferData().setCurrentVO(getBufferData().getCurrentVO());

			getSelfUI().getBillCardPanel().execHeadLoadFormulas();
			getSelfUI().getBillCardPanel().execTailLoadFormulas();
//			this.onBoEdit();
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
	}
	protected ITreeManageController getUITreeManageController() {
		return (ITreeManageController) getUIController();
	}

	/**
	 * ȡ�õ�ǰUI��
	 * 
	 * @return
	 */
	private CredenceClientUI getSelfUI() {
		return (CredenceClientUI) getBillUI();
	}




	/*	@Override
	protected void onBoDelete() throws Exception {

//		if (getSelfUI().isListPanelSelected() && getSelfUI().getBillListPanel().getHeadTable().getSelectedRow() < 0) {
//			getSelfUI().showErrorMessage("��ѡ�񴰿��б�����Ҫɾ���ļ�¼��");
//			return;
//		}
		Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(),"��ʾ","�Ƿ�Ҫɾ��");
		if(flag==1){
			String pk_node=getSelfUI().selectnode;
			if("".equals(pk_node)){
				NCOptionPane.showMessageDialog(this.getBillUI(),"��ѡ��Ҫɾ���Ľڵ㡣");
				return ;
			}
			CredenceHVO vo= (CredenceHVO) HYPubBO_Client.queryByPrimaryKey(CredenceHVO.class, pk_node);
			if(vo==null){
				getSelfUI().showWarningMessage("����ɾ�����ڵ㣡");
				return;
			}
			if(vo!=null)
				HYPubBO_Client.delete(vo);
		}

		CircularlyAccessibleValueObject billvo = getBufferData().getCurrentVO().getParentVO();

		try {
			onBoRefresh();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}*/


	/**
	 * �Զ��尴ť��ͬ��������У�顢Ԥ���Ķ����¼�
	 */
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		switch (intBtn) {
		case IBtnDefine.AddEffectFactor:
			onBoAddEffectFactor();
			break;

		}
	}
	public void onButtonAction(ButtonObject bo) throws Exception {
		int intBtn = Integer.parseInt(bo.getTag());
		long lngTime = System.currentTimeMillis();
		buttonActionBefore(getBillUI(), intBtn);

		switch (intBtn) {
		default:
			break;
		case IBtnDefine.initUFOENV:
			onBoInitUFOENV();
			break;
		case 1: // '\001'
			if (!getBillUI().isBusinessType().booleanValue()) {
				getBillUI().showHintMessage(
						NCLangRes.getInstance().getStrByID("uifactory",
						"UPPuifactory-000061"));
				onBoAdd(bo);
				buttonActionAfter(getBillUI(), intBtn);
			}
			break;

		case 3: // '\003'
			getBillUI().showHintMessage(
					NCLangRes.getInstance().getStrByID("uifactory",
					"UPPuifactory-000067"));
			onBoEdit();
			buttonActionAfter(getBillUI(), intBtn);
			break;

		case 32: // ' '
			getBillUI().showHintMessage(
					NCLangRes.getInstance().getStrByID("uifactory",
					"UPPuifactory-000070"));

			Object d_objpk_datadefinit_h = this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_datadefinit_h").getValueObject();
			String d_pk_datadefinit_h = d_objpk_datadefinit_h==null?"":d_objpk_datadefinit_h.toString();

			onBoDelete();
			//��д�Ƿ�ģ�涨��
			//begin------------------
			IQueryField d_exeSQL = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName()); 
			String d_updatesql = "update dip_datachange_b set tempexist=null where nvl(dr,0)=0 and pk_datachange_b='"+d_pk_datadefinit_h+"'";
			d_exeSQL.exesql(d_updatesql);
			//end--------------------
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					(new StringBuilder()).append(
							NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000071")).append(
									System.currentTimeMillis() - lngTime).toString());
			break;

		case 0: // '\0'
			getBillUI().showHintMessage(
					NCLangRes.getInstance().getStrByID("uifactory",
					"UPPuifactory-000072"));
			onBoSave();
			//��д�Ƿ�ģ�涨��
			//begin------------------
			IQueryField exeSQL = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName()); 
			Object objpk_datadefinit_h = this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_datadefinit_h").getValueObject();
			String pk_datadefinit_h = objpk_datadefinit_h==null?"":objpk_datadefinit_h.toString();
			String updatesql = "update dip_datachange_b set tempexist='�Ѷ���' where nvl(dr,0)=0 and pk_datachange_b='"+pk_datadefinit_h+"'";
			exeSQL.exesql(updatesql);
			//end--------------------
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					(new StringBuilder()).append(
							NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000073")).append(
									System.currentTimeMillis() - lngTime).toString());
			break;

		case 7: // '\007'
			onBoCancel();
			getBillUI().showHintMessage("");
			buttonActionAfter(getBillUI(), intBtn);
			break;

		case 8: // '\b'
			getBillUI().showHintMessage(
					NCLangRes.getInstance().getStrByID("uifactory",
					"UPPuifactory-000076"));
			onBoRefresh();
			buttonActionAfter(getBillUI(), intBtn);
			getBillUI().showHintMessage(
					(new StringBuilder()).append(
							NCLangRes.getInstance().getStrByID("uifactory",
							"UPPuifactory-000077")).append(
									System.currentTimeMillis() - lngTime).toString());
			break;
		case 11: onBoLineAdd();break;
		case IBillButton.DelLine:onBoLineDel();break;
		//2011-6-13
		case 52:onBoExport();break;

		//2011-6-14
		case 51:onBoImport();break;

		}
	}
	/**
	 * @desc ��ʼ��������ť
	 * */
	public void onBoInitUFOENV() {
		InitUFOEnvDlg a=new InitUFOEnvDlg(getSelfUI());
		a.showModal();
	}
	private void onBoAddEffectFactor() {
		getSelfUI().getBillCardPanel().getBodyPanel().showTableCol("affect");//.hideTableCol("affect");
		/*getSelfUI().updateUI();
		try {
			super.onBoCancel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	public String ini(DipDatachangeBVO bvo) {
		try {
			super.onBoAdd(getSelfUI().getButtonManager().getButton(IBillButton.Add));
			//�Ѹ��������ŵ�������
			getSelfUI().getBillCardPanel().getHeadItem("pk_datadefinit_h").setValue(bvo.getPrimaryKey());
//			DipDatachangeBVO bvo=(DipDatachangeBVO) HYPubBO_Client.queryByPrimaryKey(DipDatachangeBVO.class, bvopk);
			DipDatachangeHVO hvo=(DipDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipDatachangeHVO.class, bvo.getPk_datachange_h());

			//2011-6-28
			String orgCode=bvo.getOrgcode();
			String[] array=orgCode.split(",");
			String unit=null;
			String unitname=null;
			for(int i=0;i<array.length;i++){
				if(i==0){
					unit=array[i];
					unitname=(String) HYPubBO_Client.findColValue("bd_corp", "unitname", "pk_corp='"+unit+"' and nvl(dr,0)=0");
				}else{
					unitname=unitname+","+HYPubBO_Client.findColValue("bd_corp", "unitname", "pk_corp='"+array[i]+"' and nvl(dr,0)=0");
					unit=unit+","+array[i];
				}
			}
			//2011-6-28 ���汻ע�͵�һ����ԭ����	 begin
			//getSelfUI().getBillCardPanel().getHeadItem("unit").setValue(bvo.getOrgcode());
			getSelfUI().getBillCardPanel().getHeadItem("unit").setValue(unit);
			getSelfUI().getBillCardPanel().getHeadItem("corp").setValue(unitname);
			/*end*/

			getSelfUI().getBillCardPanel().getHeadItem("busdata").setValue(hvo.getBusidata());
			getSelfUI().getBillCardPanel().getHeadItem("accoutbook").setValue(hvo.getPk_glorg());
			getSelfUI().getBillCardPanel().getHeadItem("sysmodel").setValue(bvo.getIssystmp());
			getSelfUI().getBillCardPanel().getHeadItem("code").setValue(hvo.getCode());
			getSelfUI().getBillCardPanel().getHeadItem("name").setValue(hvo.getName());
			getSelfUI().getBillCardPanel().getHeadItem("systype").setValue(hvo.getSystype());
			getSelfUI().getBillCardPanel().execHeadLoadFormulas();
//			for(int i=0;i<20;i++){
//				this.onBoLineAdd();
//			}

			getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(0, 0, true, true);
			return hvo.getPk_glorg();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";


	}
	@Override
	protected void onBoSave() throws Exception {
		//�Զ�����  
	//	CredenceClientUI ui=getSelfUI();
		int kk=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		List<Integer> list=new ArrayList<Integer>();
		
		for(int i=0;i<kk;i++){
			String subjects=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_credence_b").getValueAt(i, "subjects");
			String direction=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_credence_b").getValueAt(i, "direction");
			if((subjects==null||subjects.trim().equals(""))&&(direction==null||direction.trim().equals(""))){
				//getBillCardPanelWrapper().getBillCardPanel().getBillTable("dip_credence_b").changeSelection(i, 0, true, true);
				list.add(i);
			}
		}
		if(list.size()>0){
			int w[]=new int[list.size()];
			for(int i=0;i<list.size();i++){
				w[i]=(Integer) list.get(i);
			}
			getBillCardPanelWrapper().getBillCardPanel().getBillModel("dip_credence_b").delLine(w);
		}
		
		
		//int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
//		if(row<2){
//		MessageDialog.showErrorDlg(getSelfUI(), "��ʾ", "��������¼����������");
//		return;
//		}
		AggregatedValueObject checkvo=getSelfUI().getVOFromUI();
//		CredenceBVO[] bvo=(CredenceBVO[]) checkvo.getChildrenVO();
//		int jie=0;
//		int dai=0;
//		if(bvo !=null && bvo.length>=2){
//		for(int i=0;i<bvo.length;i++){
//		String direction = bvo[i].getDirection()==null?"":bvo[i].getDirection();
//		if("��".equals(direction)){
//		jie ++;
//		}

//		if("��".equals(direction)){
//		dai ++;
//		}
//		}

//		if(jie==0){
//		MessageDialog.showErrorDlg(getSelfUI(), "��ʾ", "����Ҫ��һ�����跽����");
//		return;
//		}

//		if(dai==0){
//		MessageDialog.showErrorDlg(getSelfUI(), "��ʾ", "����Ҫ��һ������������");
//		return;
//		}
//		}

		//2011-6-24 ������һ��ע���ˣ����� �������ٵ��룬ֱ�ӵ�����棬�ر�ҳ����ٴδ򿪣��������ݷǵ��������ݣ����ǵ���֮ǰ������
		//if(getBillUI().getBillOperate()==IOperation.ADD||getBillUI().getBillOperate()==IOperation.MODIFY){

		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);
		CredenceHVO hvo= (checkVO.getParentVO()==null?null:(CredenceHVO)checkVO.getParentVO());
		if(hvo!=null){
			String pk;
			if(hvo.getPrimaryKey()==null||hvo.getPrimaryKey().length()<=0){
				pk=HYPubBO_Client.insert(hvo);
			}else{
				pk=hvo.getPrimaryKey();
				HYPubBO_Client.update(hvo);
			}
			HYPubBO_Client.deleteByWhereClause(CredenceBVO.class, "pk_credence_h='"+pk+"'");
			CredenceBVO[] checkbvos=checkVO.getChildrenVO()==null?null:(CredenceBVO[])checkVO.getChildrenVO();
			List<CredenceBVO> checklist=new ArrayList<CredenceBVO>();
			if(checkbvos!=null&&checkbvos.length>0){
				for(CredenceBVO b:checkbvos){
					if(b.getSubjects()!=null&&b.getSubjects().length()>0){
						b.setPk_credence_h(pk);
						checklist.add(b);

					}
				}
				if(checklist!=null&&checklist.size()>0){
					checkVO.setChildrenVO(checklist.toArray(new CredenceBVO[checklist.size()]) );
				}else{
					checkVO.setChildrenVO(null);
				}
				if(checkVO.getChildrenVO()!=null){
					HYPubBO_Client.insertAry((CredenceBVO[])checkVO.getChildrenVO());
				}
//				getBufferData().addVOToBuffer(checkVO);			
//				getBufferData().setCurrentVO(checkVO);
//				int nCurrentRow=getBufferData().getCurrentRow();
			}else{
				checkVO.setChildrenVO(null);
			}
			/*if(checkVO.getChildrenVO()!=null){
					HYPubBO_Client.insertAry((CredenceBVO[])checkVO.getChildrenVO());
				}*/
			getBufferData().addVOToBuffer(checkVO);	
			getBufferData().setCurrentVO(checkVO);
			int nCurrentRow=getBufferData().getCurrentRow();
		}
		// �������������
		setAddNewOperate(isAdding(), checkVO);
		// ���ñ����״̬
		setSaveOperateState();

		//}

		//2011-6-28 cl 429--442�� begin
		String orgCode=hvo.getUnit();
		String[] array=orgCode.split(",");
		String unit=null;
		String unitname=null;
		for(int i=0;i<array.length;i++){
			if(i==0){
				unit=array[i];
				unitname=(String) HYPubBO_Client.findColValue("bd_corp", "unitname", "pk_corp='"+unit+"' and nvl(dr,0)=0");
			}else{
				unitname=unitname+","+HYPubBO_Client.findColValue("bd_corp", "unitname", "pk_corp='"+array[i]+"' and nvl(dr,0)=0");
				unit=unit+","+array[i];
			}
		}
		getSelfUI().getBillCardPanel().getHeadItem("corp").setValue(unitname);
		/*end */

		getSelfUI().getBillCardPanel().execHeadLoadFormulas();
		getSelfUI().getBillCardPanel().execTailLoadFormulas();

		MessageDialog.showHintDlg(getSelfUI(), "����", "����ɹ���");
	}
	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCancel();
		getSelfUI().getBillCardPanel().execHeadLoadFormulas();
		getSelfUI().getBillCardPanel().execTailLoadFormulas();
	}
	@Override
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
//		int rowcount=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
//		if(rowcount<20){
//			for(int i=0;i<20-rowcount;i++){
				super.onBoLineAdd();
//			}
//		}

		//getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(0, 0, true, true);
	}

	/**
	 * @description:����
	 * @date 2011-6-14
	 * @author ����
	 */
	String exportLineName="ƾ��ģ����������,����,����,��λ,NC�˲�,ҵ������,ϵͳ����,�Ƿ�ϵͳģ��,��������,ƾ֤���,�Ƶ���,�Ƶ�����,ƾ֤ģ����������,ƾ֤ģ���ӱ�����,��Ŀ,����,ժҪ,��������,����,���,����,������,Эͬ��,ҵ������,�Զ��屸ע,������,Ӱ������1,Ӱ������2,Ӱ������3,Ӱ������4,Ӱ������5,Ӱ������6,Ӱ������7,Ӱ������8";
	@Override
	protected void onBoExport() throws Exception {
		String path=null;
		try{
			JFileChooser jfc=new JFileChooser();
			jfc.setDialogType(jfc.SAVE_DIALOG);
			if(jfc.showSaveDialog(this.getBillUI())==JFileChooser.CANCEL_OPTION){
				return;				
			}
			path=jfc.getSelectedFile().toString();

			//ƾ֤ģ������
			AggregatedValueObject avo=getBillUI().getVOFromUI();
			CredenceHVO ch=(CredenceHVO) avo.getParentVO();
			CredenceBVO[]cbvo=(CredenceBVO[]) avo.getChildrenVO();
			if(ch !=null){
				if(cbvo !=null && cbvo.length>0){
					List list=new ArrayList();

					ExpExcelVO[] eevo=null;
					ExpExcelVO	evo =null;
					for(int i=0;i<cbvo.length;i++){
						if(cbvo[i].getSubjects()==null || cbvo[i].getSubjects().length()==0){
							continue;
						}
						evo=new ExpExcelVO();
						int j=1;
						evo.setAttributeValue("line"+j++, ch.getPrimaryKey());
						evo.setAttributeValue("line"+j++, ch.getCode());
						evo.setAttributeValue("line"+j++, ch.getName());
						evo.setAttributeValue("line"+j++, ch.getUnit());//��λ
						evo.setAttributeValue("line"+j++, ch.getAccoutbook());//NC�˲�
						evo.setAttributeValue("line"+j++, ch.getBusdata());//ҵ������
						evo.setAttributeValue("line"+j++, ch.getSystype());
						evo.setAttributeValue("line"+j++, ch.getSysmodel().booleanValue()?"Y":"N");
						evo.setAttributeValue("line"+j++, ch.getAttmentnum());
						evo.setAttributeValue("line"+j++, ch.getCredtype());//ƾ֤���
						evo.setAttributeValue("line"+j++, ch.getVoperatorid());
						evo.setAttributeValue("line"+j++, ch.getDoperatordate());


						evo.setAttributeValue("line"+j++, cbvo[i].getPk_credence_h());
						evo.setAttributeValue("line"+j++, cbvo[i].getPk_credence_b());
						evo.setAttributeValue("line"+j++, cbvo[i].getSubjects());
						evo.setAttributeValue("line"+j++, cbvo[i].getDirection());
						evo.setAttributeValue("line"+j++, cbvo[i].getSummary());

						evo.setAttributeValue("line"+j++, cbvo[i].getAssistant());
						evo.setAttributeValue("line"+j++, cbvo[i].getCurrency());
						evo.setAttributeValue("line"+j++, cbvo[i].getMoney());
						evo.setAttributeValue("line"+j++, cbvo[i].getNumbers());
						evo.setAttributeValue("line"+j++, cbvo[i].getVerificationno());

						evo.setAttributeValue("line"+j++, cbvo[i].getXtno());
						evo.setAttributeValue("line"+j++, cbvo[i].getBusdate());
						evo.setAttributeValue("line"+j++, cbvo[i].getRemark());
						evo.setAttributeValue("line"+j++, cbvo[i].getCtrl());

						evo.setAttributeValue("line"+j++, cbvo[i].getAffect());
						evo.setAttributeValue("line"+j++, cbvo[i].getAffect2());
						evo.setAttributeValue("line"+j++, cbvo[i].getAffect3());
						evo.setAttributeValue("line"+j++, cbvo[i].getAffect4());
						evo.setAttributeValue("line"+j++, cbvo[i].getAffect5());
						evo.setAttributeValue("line"+j++, cbvo[i].getAffect6());
						evo.setAttributeValue("line"+j++, cbvo[i].getAffect7());
						evo.setAttributeValue("line"+j++, cbvo[i].getAffect8());

						list.add(evo);
					}
					if(list !=null && list.size()>0){
						eevo=(ExpExcelVO[]) list.toArray(new ExpExcelVO[list.size()]);
						String filepath=path.endsWith(".xls")?path:(path+".xls");
						File file=new File(filepath);
						if(!file.exists()){
							file.createNewFile();
							String filePathTemp=path+"-1.xls";
							ExpToExcel ete=new ExpToExcel(filepath,"ƾ֤ģ�嵼��",exportLineName,eevo,filePathTemp);

							MessageDialog.showHintDlg(this.getBillUI(), "��ʾ", "������ɣ�");
						}
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @description:����
	 * @date 2011-6-14
	 * @author ����
	 */
	@Override
	protected void onBoImport() throws Exception {
		JFileChooser jfc=new JFileChooser();
		jfc.setDialogType(jfc.SAVE_DIALOG);
		if(jfc.showSaveDialog(this.getBillUI())==JFileChooser.CANCEL_OPTION){
			return;
		}

		String path=jfc.getSelectedFile().toString();
		if(!path.endsWith(".xls")){
			MessageDialog.showErrorDlg(getSelfUI(), "����", "��ѡ��excel�ļ����е��룡");
			return;
		}

		FileInputStream fis=new FileInputStream(path);
		TxtDataVO tdvo=new TxtDataVO();
		try{
			HSSFWorkbook book=new HSSFWorkbook(fis);
			HSSFSheet sheet=book.getSheetAt(0);
			if(sheet==null){
				MessageDialog.showErrorDlg(getSelfUI(), "����", "������ļ���ʽ����ȷ��");
				return;
			}
			tdvo.setSheetName(book.getSheetName(0));
			tdvo.setStartRow(sheet.getFirstRowNum());
			tdvo.setStartCol(sheet.getLeftCol());
			tdvo.setRowCount(sheet.getPhysicalNumberOfRows()-1);//ȥ��������
			tdvo.setColCount((short)sheet.getRow(sheet.getFirstRowNum()).getPhysicalNumberOfCells());
			tdvo.setFirstDataRow(sheet.getFirstRowNum());
			tdvo.setFirstDataCol(sheet.getLeftCol());

			HSSFRow titleRow=sheet.getRow(tdvo.getStartRow());
			HashMap<String, String> titleMap=new HashMap<String, String>();
			for(short i=0;i<titleRow.getPhysicalNumberOfCells();i++){
				titleMap.put((String)getCellValues(titleRow.getCell(i)), Short.toString(i));//����,key�Ǳ��庺�����ƣ�value��ʾ�ڼ��У��д�0��ʼ��
			}
			tdvo.setTitlemap(titleMap);
			int titleSize=titleMap.size();
			if(titleSize!=34){
				MessageDialog.showErrorDlg(getSelfUI(), "����", "excel��ʽ����Ӧ����34�У�ʵ��Ϊ"+titleSize+"�У�");
			}
			for(int i=1;i<=tdvo.getRowCount();i++){
				HSSFRow row=sheet.getRow(tdvo.getFirstDataRow()+i);
				for(short j=0;j<titleSize;j++){
					tdvo.setCellData(i-1, j, getCellValues(row.getCell(j)));
				}
			}
		}finally{
			fis.close();
		}
//"ƾ��ģ����������,����,����,��λ,NC�˲�,ҵ������,ϵͳ����,�Ƿ�ϵͳģ��,��������,   ƾ֤���,  �Ƶ���,�Ƶ�����,ƾ֤ģ����������,ƾ֤ģ���ӱ�����,��Ŀ,����,ժҪ,��������,����,���,����,������,Эͬ��,ҵ������,�Զ��屸ע,������,Ӱ������1,Ӱ������2,Ӱ������3,Ӱ������4,Ӱ������5,Ӱ������6,Ӱ������7,Ӱ������8";
//        0       1   2   3     4      5       6        7        8         9       10     11        12             13       14  15   16    17    18  19  20  21    22    23       24       25   26       27       28       29      30       31       32      33             
//		                                                     Attmentnum,Credtype,
		String exp[]=exportLineName.split(",");
		for(int m=0;m<exp.length;m++){
			if(tdvo.getTitlemap().get(exp[m])==null){
				//getSelfUI().showErrorMessage("excel��ʽ����û���ҵ�"+exp[m]+"�У�");
				MessageDialog.showErrorDlg(getSelfUI(), "����","excel��ʽ����û���ҵ�"+exp[m]+"�У�");
				return;
			}
		}
		
		
		AggregatedValueObject billvo=getBillUI().getVOFromUI();
		CredenceHVO chvo=(CredenceHVO) billvo.getParentVO();
		int k=Integer.parseInt(tdvo.getTitlemap().get("��������"));
		chvo.setAttmentnum(tdvo.getCellData(0, k)==null?"":tdvo.getCellData(0, k).toString());
		k=Integer.parseInt(tdvo.getTitlemap().get("ƾ֤���"));
		chvo.setCredtype(tdvo.getCellData(0, k)==null?"":tdvo.getCellData(0, k).toString());
		k=Integer.parseInt(tdvo.getTitlemap().get("�Ƶ���"));
		chvo.setVoperatorid(tdvo.getCellData(0, k)==null?"":tdvo.getCellData(0, k).toString());
		k=Integer.parseInt(tdvo.getTitlemap().get("�Ƶ�����"));
		chvo.setDoperatordate(tdvo.getCellData(0, k)==null?"":tdvo.getCellData(0, k).toString());

		CredenceBVO[] cbvo=new CredenceBVO[tdvo.getRowCount()];
		int[] row=new int[cbvo.length];
		for(int i=0;i<cbvo.length;i++){
			row[i]=i;
			cbvo[i]=new CredenceBVO();
//��Ŀ,����,ժҪ,��������,����,���,����,������,Эͬ��,ҵ������,�Զ��屸ע,������,Ӱ������1,Ӱ������2,Ӱ������3,Ӱ������4,Ӱ������5,Ӱ������6,Ӱ������7,Ӱ������8";
// 14  15   16    17    18  19  20  21    22    23       24       25   26       27       28       29      30       31       32      33
			k=Integer.parseInt(tdvo.getTitlemap().get("��Ŀ"));
			cbvo[i].setSubjects((tdvo.getCellData(i, k)==null)?"":tdvo.getCellData(i, k).toString());
			k=Integer.parseInt(tdvo.getTitlemap().get("����"));
			cbvo[i].setDirection(tdvo.getCellData(i, k)==null?"":tdvo.getCellData(i, k).toString());
			k=Integer.parseInt(tdvo.getTitlemap().get("ժҪ"));
			cbvo[i].setSummary(tdvo.getCellData(i, k)==null?"":tdvo.getCellData(i, k).toString());
			k=Integer.parseInt(tdvo.getTitlemap().get("��������"));
			cbvo[i].setAssistant(tdvo.getCellData(i, k)==null?"":tdvo.getCellData(i, k).toString());
			k=Integer.parseInt(tdvo.getTitlemap().get("����"));
			cbvo[i].setCurrency(tdvo.getCellData(i, k)==null?"":tdvo.getCellData(i, k).toString());
			k=Integer.parseInt(tdvo.getTitlemap().get("���"));
			cbvo[i].setMoney(tdvo.getCellData(i, k)==null?"":tdvo.getCellData(i, k).toString());
			k=Integer.parseInt(tdvo.getTitlemap().get("����"));
			cbvo[i].setNumbers(tdvo.getCellData(i, k)==null?"":tdvo.getCellData(i, k).toString());
			k=Integer.parseInt(tdvo.getTitlemap().get("������"));
			cbvo[i].setVerificationno(tdvo.getCellData(i, k)==null?"":tdvo.getCellData(i, k).toString());
			k=Integer.parseInt(tdvo.getTitlemap().get("Эͬ��"));
			cbvo[i].setXtno(tdvo.getCellData(i, k)==null?"":tdvo.getCellData(i, k).toString());
			k=Integer.parseInt(tdvo.getTitlemap().get("ҵ������"));
			cbvo[i].setBusdate(tdvo.getCellData(i, k)==null?"":tdvo.getCellData(i, k).toString());
			k=Integer.parseInt(tdvo.getTitlemap().get("�Զ��屸ע"));
			cbvo[i].setRemark(tdvo.getCellData(i, k)==null?"":tdvo.getCellData(i, k).toString());
			k=Integer.parseInt(tdvo.getTitlemap().get("������"));
			cbvo[i].setCtrl(tdvo.getCellData(i, k)==null?"":tdvo.getCellData(i, k).toString());
			k=Integer.parseInt(tdvo.getTitlemap().get("Ӱ������1"));

			cbvo[i].setAffect(tdvo.getCellData(i, k)==null?"":tdvo.getCellData(i, k).toString());
			k=Integer.parseInt(tdvo.getTitlemap().get("Ӱ������2"));
			cbvo[i].setAffect2(tdvo.getCellData(i, k)==null?"":tdvo.getCellData(i, k).toString());
			k=Integer.parseInt(tdvo.getTitlemap().get("Ӱ������3"));
			cbvo[i].setAffect3(tdvo.getCellData(i, k)==null?"":tdvo.getCellData(i, k).toString());
			k=Integer.parseInt(tdvo.getTitlemap().get("Ӱ������4"));
			cbvo[i].setAffect4(tdvo.getCellData(i, k)==null?"":tdvo.getCellData(i, k).toString());
			k=Integer.parseInt(tdvo.getTitlemap().get("Ӱ������5"));
			cbvo[i].setAffect5(tdvo.getCellData(i, k)==null?"":tdvo.getCellData(i, k).toString());
			k=Integer.parseInt(tdvo.getTitlemap().get("Ӱ������6"));
			cbvo[i].setAffect6(tdvo.getCellData(i, k)==null?"":tdvo.getCellData(i, k).toString());
			k=Integer.parseInt(tdvo.getTitlemap().get("Ӱ������7"));
			cbvo[i].setAffect7(tdvo.getCellData(i, k)==null?"":tdvo.getCellData(i, k).toString());
			k=Integer.parseInt(tdvo.getTitlemap().get("Ӱ������8"));
			cbvo[i].setAffect8(tdvo.getCellData(i, k)==null?"":tdvo.getCellData(i, k).toString());
		}
		billvo.setParentVO(chvo);
		billvo.setChildrenVO(cbvo);
		getBufferData().clear();
		getBufferData().addVOToBuffer(billvo);	
		getBufferData().setCurrentVO(billvo);
		getSelfUI().getBillCardPanel().getBillModel().clearBodyData();
		getSelfUI().getBillCardPanel().setHeadItem("attmentnum", chvo.getAttmentnum());
		getSelfUI().getBillCardPanel().setHeadItem("credtype", chvo.getCredtype());
		getSelfUI().getBillCardPanel().setHeadItem("voperatorid", chvo.getVoperatorid());
		getSelfUI().getBillCardPanel().setHeadItem("doperatordate", chvo.getDoperatordate());
		for(int i=0;i<row.length;i++){
			getSelfUI().getBillCardPanel().addLine();
		}
		getSelfUI().getBillCardPanel().getBillModel().setBodyRowVOs(cbvo, row);
		getSelfUI().getBillCardPanel().execHeadLoadFormulas();
		for(int i=0;i<row.length;i++){
			getSelfUI().getBillCardPanel().execBodyFormulas(i, getSelfUI().getBillCardPanel().getBodyItem("vdef1").getLoadFormula());
		}
		getSelfUI().getBillCardPanel().execTailLoadFormulas();
//		getSelfUI().updateUI();
		MessageDialog.showHintDlg(getSelfUI(), "��ʾ", "������ϣ�");
	}
private void errorMethod(String str){
	getSelfUI().showErrorMessage("excel��ʽ����û���ҵ�"+str+"�У�");
}
	private Object getCellValues(HSSFCell cell) {
		if(cell==null){
			return null;
		}
		Object rst = null;
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_BOOLEAN:
			// �õ�Boolean����ķ���
			System.out.print(cell.getBooleanCellValue() + " ");
			rst = new UFBoolean(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			String value = Double.toString(cell.getNumericCellValue());
			if (value.contains(".")) {
				try {
					String temp = value.replace(".", "-");
					int lastNo = Integer.parseInt(temp.split("-")[1]);
					if (lastNo == 0) {
						rst = Integer.parseInt(temp.split("-")[0]);
					} else {
						rst = new UFDouble(cell.getNumericCellValue());
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
					rst = new UFDouble(cell.getNumericCellValue());
				}
			}
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			// ��ȡ��ʽ
			rst = cell.getCellFormula();
			break;
		case HSSFCell.CELL_TYPE_STRING:
			// ��ȡString
			rst = cell.getRichStringCellValue();
			try {
				rst = new UFDate(rst.toString());
			} catch (Exception e) {
				rst = cell.getRichStringCellValue().toString();
			}
			System.out.print(cell.getRichStringCellValue().toString() + " ");
			break;
		}
		return rst;
	}
}