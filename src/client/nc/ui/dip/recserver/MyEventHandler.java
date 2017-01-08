package nc.ui.dip.recserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.card.BillCardUI;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.recserver.DipRecserverVO;

/**
  *
  *������AbstractMyEventHandler�������ʵ���࣬
  *��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
  *@author �Ž�˫
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillCardUI billUI, ICardController control){
		super(billUI,control);		
	}
    /*
     * (non-Javadoc)NC���շ�����У��
     * lx  2011-5-27
     * @see nc.ui.trade.manage.ManageEventHandler#onBoSave()
     */
	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<k;i++){
			String descs=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DipRecserverVO().DESCS);
			String code=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DipRecserverVO().CODE);
			String name=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new DipRecserverVO().NAME);

			if((code==null||code.trim().equals(""))&&(name==null||name.trim().equals(""))&&(descs==null||descs.trim().equals(""))){
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
		
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd!=null){
			bd.dataNotNullValidate();
		}
//		RecseClientUI ui=(RecseClientUI) getBillUI();
//		String pk=(String) ui.getBillCardPanel().getBodyItem("pk_recserver").getValueObject();
//		if(pk==null||pk.trim().equals("")){
//			pk=" ";
//		}
//		String code=ui.getBillCardPanel().getBodyItem("code").getValueObject().toString();
//		
//		IUAPQueryBS bs=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//		Collection tion=bs.retrieveByClause(DipRecserverVO.class, "code='"+code+"' and nvl(dr,0)=0 and pk_recserver<>'"+pk+"'");
//		if(tion!=null){
//			if(tion.size()>=1){
//				ui.showWarningMessage("�ñ����Ѿ����ڣ�");
//				return;
//			}
//		}
		super.onBoSave();
	}
	//NC���շ��������ж��Ƿ����ã������ݷ��͵�Ŀ������������������������ɾ�� lx 2011-5-27
	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
			Map<String,String> map=IContrastUtil.getDocRefMap();
			String key="dip_recserver";
			String value=map.get(key);
			int bodycount=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
			if(bodycount>=0){
				String pk=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new DipRecserverVO().getPKFieldName().toLowerCase())==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(bodycount, new DipRecserverVO().getPKFieldName().toLowerCase()).toString();;
				String isref=SJUtil.isExitRef(value,pk);
				if(isref!=null&&isref.length()>0){
					getBillUI().showErrorMessage("�������ݱ����ã�����ɾ����");
					return ;
				}
				Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(),"��ʾ","�Ƿ�Ҫɾ��?");
				if(flag==1){
				    this.onBoLineDel();
				    HYPubBO_Client.deleteByWhereClause(DipRecserverVO.class,new DipRecserverVO().getPKFieldName().toLowerCase()+"='"+pk+"'");
				}
			}else{
				getBillUI().showErrorMessage("��ѡ��Ҫɾ���Ľڵ㣡");
				return;
			}
//		super.onBoDelete();
	}		
	@Override
	public void onBoAdd(ButtonObject arg0) throws Exception {
		// TODO Auto-generated method stub
		int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		if(rows>0){
			super.onBoEdit();
			super.onBoLineAdd();
		}else{
			super.onBoAdd(arg0);
			super.onBoLineAdd();
		}
	}
	/*
	 * �򿪽ڵ㣬��ʾ���б�������
	 * 2011-06-13
	 * zlc*/
	public void ini(){
		try {
			doBodyQuery(" 1=1 order by code");
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
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		super.onBoEdit();
		onBoLineAdd();
	}
}