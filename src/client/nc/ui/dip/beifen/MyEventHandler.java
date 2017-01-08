package nc.ui.dip.beifen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.beifen.DatacanzhaoVO;



/**
 *
 *������AbstractMyEventHandler�������ʵ���࣬
 *��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 *@author author
 *@version tempProject version
 */

public class MyEventHandler 
extends AbstractMyEventHandler{

	BillCardUI billUI;

	ICardController control;
	public MyEventHandler(BillCardUI billUI, ICardController control){
		super(billUI,control);	
		this.billUI=billUI;
		this.control=control;
	}

	@Override
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		DatacanzhaoVO vo=new DatacanzhaoVO();
		for(int i=0;i<k;i++){
			Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, vo.ISSYSPREF);
			if(flag!=null&&flag){
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,vo.TABNAME,false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.XINGSHI, false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.ISSYSPREF, false);
			}
		}
		onBoLineAdd();
		getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
		getBillUI().updateButtons();
		BeifenClientUI.delFlag=false;
	}
	
	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		//�Զ�����ȥ����
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<k;i++){
			Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DatacanzhaoVO().ISSYSPREF);
			String name=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DatacanzhaoVO().TABNAME);
			String xingshi=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, "xingshi");
			if((flag==null||flag==false)&&(xingshi==null||xingshi.trim().equals(""))&&(name==null||name.trim().equals(""))){
//				getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(i, 0, false, false);
//				getBillCardPanelWrapper().getBillCardPanel().delLine();
				list.add(i);
			}else{
				if(flag==null){
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(false, i, new DatacanzhaoVO().ISSYSPREF);
				}
			}
		}
		if(list!=null&&list.size()>0){
			int in[]=new int[list.size()];
			for(int i=0;i<list.size();i++){
				in[i]=list.get(i);
			}
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().delLine(in);
		}
		
		
		
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd !=null){
			bd.dataNotNullValidate();
		}
		//�˴����ӱ�ͷ�ظ�У�飺�������ͱ��롢����
//		BeifenClientUI ui=(BeifenClientUI)getBillUI();
//		//����
//		String pk=(String)ui.getBillCardPanel().getBodyItem("pk_dip_datacanzhao").getValueObject();
//		if(pk ==null || pk.trim().equals("")){
//			pk=" ";//����ʱ�����ظ��Ŀ������
//		}
//		
//		String code=(String)ui.getBillCardPanel().getBodyItem("tabname").getValueObject();
//		String name=(String)ui.getBillCardPanel().getBodyItem("xingshi").getValueObject();
//		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//		
//		Collection ccode=bs.retrieveByClause(DatacanzhaoVO.class, "tabname='"+code+"' and nvl(dr,0)=0 and pk_dip_datacanzhao <>'"+pk+"'");
//		
//		if(ccode !=null){
//			if(ccode.size()>=1){
//				ui.showWarningMessage("�á�"+code+"�������Ѿ����ڣ�");
//				return;
//			}
//		}
//		
//		Collection cname=bs.retrieveByClause(DatacanzhaoVO.class, "xingshi='"+name+"' and nvl(dr,0)=0 and pk_dip_datacanzhao <>'"+pk+"'");
//		if(cname !=null){
//			if(cname.size()>=1){
//				ui.showWarningMessage("�á�"+name+"����ʽ�Ѿ����ڣ�");
//				return;
//			}
//		}
//		this.billUI.getVOFromUI();
		
		super.onBoSave();
		BeifenClientUI.delFlag=true;
		setSelectRow(0, 0);
		
	}
	

	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		Map<String,String> map=IContrastUtil.getDocRefMap();
		String key="pk_dip_datacanzhao";
		String value=map.get(key);
		int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();		
		if(row>=0){
			Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(row, new DatacanzhaoVO().ISSYSPREF);
			if(flag!=null&&flag){
				getBillUI().showErrorMessage("ϵͳԤ�ò�����ɾ����");
				return;
			}
			String pk=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(row, new DatacanzhaoVO().getPKFieldName().toLowerCase().toString()).toString();//��Ŀ����	pk_dip_datacanzhao
			String isref=SJUtil.isExitRef(value,pk);
			if(isref!=null&&isref.length()>0){
				getBillUI().showErrorMessage("�������ݱ����ã�����ɾ����");
				return ;
			}
			int i=MessageDialog.showOkCancelDlg(this.billUI, "��ʾ", "�Ƿ�ȷ��ɾ����");
			//��=1
			if(i==1){
			super.onBoLineDel();
			HYPubBO_Client.deleteByWhereClause(DatacanzhaoVO.class,new DatacanzhaoVO().getPKFieldName().toLowerCase()+"='"+pk+"'");
			setSelectRow(0, 0);
			}
		}else{
			getBillUI().showErrorMessage("��ѡ��Ҫɾ���Ľڵ㣡");
			return;
		}
	}

	@Override
	public void onBoAdd(ButtonObject arg0) throws Exception {
		int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		
		if(rows>0){
			super.onBoEdit();
			super.onBoLineAdd();
		}else{
			super.onBoAdd(arg0);
			super.onBoLineAdd();
		}
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		DatacanzhaoVO vo=new DatacanzhaoVO();
		for(int i=0;i<k;i++){
			Boolean flag=(Boolean)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, vo.ISSYSPREF);
			if(flag!=null&&flag){
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,vo.TABNAME,false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.XINGSHI, false);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, vo.ISSYSPREF, false);
			}
		}
		getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
		billUI.updateButtonUI();
		BeifenClientUI.delFlag=false;
	}
	public void ini(){
		try {
			doBodyQuery("");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCancel();
		BeifenClientUI.delFlag=true;
		ini();
		setSelectRow(0, 0);
	}
	
	public void setSelectRow(int row,int col){
		if(getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount()>0){
			getBillCardPanelWrapper().getBillCardPanel().getBillTable().changeSelection(row, col, false, false);
		}
	}
	
	
}