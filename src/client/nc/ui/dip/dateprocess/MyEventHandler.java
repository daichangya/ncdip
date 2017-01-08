package nc.ui.dip.dateprocess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.uap.sf.facility.SFServiceFacility;
import nc.itf.dip.pub.IQueryField;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.dip.datastyle.DataStyleClientUI;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.vo.dip.dateprocess.DateprocessVO;

/**
  *
  *������AbstractMyEventHandler�������ʵ���࣬
  *��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillCardUI billUI, ICardController control){
		super(billUI,control);	
	}
	public void OnBoSave() throws Exception{
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd!=null){
			bd.dataNotNullValidate();
		}
		//���ӱ�ͷ�ظ�У�飺��������ʱ�䡢ϵͳ���ơ��������
		DataStyleClientUI ui=(DataStyleClientUI)getBillUI();
		//�õ�����
		String pk=(String) ui.getBillCardPanel().getHeadItem("pk_datacheckstat").getValueObject();
		if(pk==null||pk.trim().equals("")){
			pk=" ";
		}
		ClientEnvironment.getServerTime().toString();
		SFServiceFacility.getServiceProviderService().getServerTime().toString();

		String sysname=(String) ui.getBillCardPanel().getHeadItem("sysname").getValueObject();
		String content=(String) ui.getBillCardPanel().getHeadItem("content").getValueObject();
		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		Collection csysname=bs.retrieveByClause(DateprocessVO.class,"systname='"+sysname+"' and nvl(dr=0)" );
		
		if(csysname==null){
			ui.showWarningMessage("ϵͳ������Ϊ��!");
			return;
		}
		Collection ccontent=bs.retrieveByClause(DateprocessVO.class,"content='"+content+"' and nvl(dr=0)");
		if(ccontent==null){
			ui.showWarningMessage("���ݲ���Ϊ�գ�");
			return;
		}
		super.onBoSave();
		
		
	}
	/**
	 * �ķ��������кͲ��к󱻵��ã������ڸ÷�����Ϊ������������һЩĬ��ֵ
	 * @param newBodyVO
	 * @return TODO
//	 */
//	protected CircularlyAccessibleValueObject processNewBodyVO(CircularlyAccessibleValueObject newBodyVO) {
//		
//		newBodyVO.setAttributeValue("pk_corp", _getCorp().getPk_corp());
//
//		// ��ӽ���
//		return newBodyVO;
//	}
	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		DateProcessClientUI ui=(DateProcessClientUI)getBillUI();
		int selectrows[]=ui.getBillCardPanel().getBillTable().getSelectedRows();
		 List<String> list=new ArrayList<String>();//ɾ�����
		if(selectrows==null||selectrows.length==0){
			ui.showErrorMessage("ѡ��ɾ����");
			return;
		}else{
			int w=getBillUI().showYesNoMessage("ɾ��ѡ���¼��");
			if(w!=4){//ѡ��
			  	return;
			}
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<selectrows.length;i++){
				int row=selectrows[i];
				if(row>=0){
				 String deletepk=ui.getBillCardPanel().getBillModel().getValueAt(row, "pk_datacheckstat")==null?"":(String)(ui.getBillCardPanel().getBillModel().getValueAt(row, "pk_datacheckstat"));
				 if(deletepk!=null&&deletepk.trim().length()>0){
					 sb.append("'"+deletepk+"',");
				 }
				}
				if(i%900==0||i==selectrows.length-1){
					String sql="delete from dip_dataprocess dd where dd.pk_datacheckstat in("+sb.substring(0, sb.length()-1)+")";
       			    list.add(sql);	 
       			    sb=new StringBuffer();
				}
				
			}
			 
//			if(!flag){
						getBillCardPanelWrapper().getBillCardPanel().getBillModel().delLine(selectrows);
						IQueryField iq=(IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
						boolean flag= iq.exectEverySql(list);
						if(!flag){
							getBillCardPanelWrapper().getBillCardPanel().updateUI();
							ui.showWarningMessage("ɾ���ɹ���");			
						}
				
//			}else{
//				ui.showWarningMessage("ɾ��ʧ�ܣ�");
//			}
		}
//        String[] str;
//         if(k>0){
//        	 str=new String[k];
//         }else{
//        	 ui.showWarningMessage("û�м�¼��ɾ��!");
//        	 return;
//         }
//		int w=getBillUI().showYesNoMessage("�Ƿ�ɾ�����м�¼��");
//		
//		if(w!=4){//ѡ��
//		  	return;
//		}
//		
//         for(int i=0;i<k;i++){
//        	 str[i]=ui.getBillCardPanel().getBillModel().getValueAt(i, "pk_datacheckstat")==null?"":(String)(ui.getBillCardPanel().getBillModel().getValueAt(i, "pk_datacheckstat"));
//         }
//        
//         StringBuffer sb=new StringBuffer();
//         for(int i=0;i<str.length;i++){
//        	 if(str[i]!=null&&str[i].length()>0){
//        		 sb.append("'"+str[i]+"',");
//        		 if(i%900==0||i==str.length-1){
//        			 String sql="delete from dip_dataprocess dd where dd.pk_datacheckstat in("+sb.substring(0, sb.length()-1)+")";
//        			 list.add(sql);	 
//        			 sb=new StringBuffer();
//        		 }
//        	 }
//         }
//        
//		
//        boolean flag= iq.exectEverySql(list);
//		if(!flag){
//					getBillCardPanelWrapper().getBillCardPanel().getBillTable().selectAll();
//					getBillCardPanelWrapper().getBillCardPanel().delLine();
//			getBillCardPanelWrapper().getBillCardPanel().updateUI();
//			ui.showWarningMessage("ɾ���ɹ���");
//		}else{
//			ui.showWarningMessage("ɾ��ʧ�ܣ�");
//		}
		
		//super.onBoDelete();
	}
	//.@Override
//	protected void onBoRefresh() throws Exception {
//		// TODO Auto-generated method stub
//		super.onBoQuery();
//	}
		
	
	
}