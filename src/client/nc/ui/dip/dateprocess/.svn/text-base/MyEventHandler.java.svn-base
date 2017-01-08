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
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
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
		//增加表头重复校验：数据类型时间、系统名称、活动、内容
		DataStyleClientUI ui=(DataStyleClientUI)getBillUI();
		//得到主键
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
			ui.showWarningMessage("系统名不能为空!");
			return;
		}
		Collection ccontent=bs.retrieveByClause(DateprocessVO.class,"content='"+content+"' and nvl(dr=0)");
		if(ccontent==null){
			ui.showWarningMessage("内容不能为空！");
			return;
		}
		super.onBoSave();
		
		
	}
	/**
	 * 改方法在增行和插行后被调用，可以在该方法中为新增的行设置一些默认值
	 * @param newBodyVO
	 * @return TODO
//	 */
//	protected CircularlyAccessibleValueObject processNewBodyVO(CircularlyAccessibleValueObject newBodyVO) {
//		
//		newBodyVO.setAttributeValue("pk_corp", _getCorp().getPk_corp());
//
//		// 添加结束
//		return newBodyVO;
//	}
	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		DateProcessClientUI ui=(DateProcessClientUI)getBillUI();
		int selectrows[]=ui.getBillCardPanel().getBillTable().getSelectedRows();
		 List<String> list=new ArrayList<String>();//删除语句
		if(selectrows==null||selectrows.length==0){
			ui.showErrorMessage("选择删除行");
			return;
		}else{
			int w=getBillUI().showYesNoMessage("删除选择记录！");
			if(w!=4){//选择不
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
							ui.showWarningMessage("删除成功！");			
						}
				
//			}else{
//				ui.showWarningMessage("删除失败！");
//			}
		}
//        String[] str;
//         if(k>0){
//        	 str=new String[k];
//         }else{
//        	 ui.showWarningMessage("没有记录可删除!");
//        	 return;
//         }
//		int w=getBillUI().showYesNoMessage("是否删除所有记录！");
//		
//		if(w!=4){//选择不
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
//			ui.showWarningMessage("删除成功！");
//		}else{
//			ui.showWarningMessage("删除失败！");
//		}
		
		//super.onBoDelete();
	}
	//.@Override
//	protected void onBoRefresh() throws Exception {
//		// TODO Auto-generated method stub
//		super.onBoQuery();
//	}
		
	
	
}