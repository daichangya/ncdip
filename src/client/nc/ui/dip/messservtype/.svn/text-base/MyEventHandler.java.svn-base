package nc.ui.dip.messservtype;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.bs.pub.pa.PaConstant;
import nc.ui.dip.dlg.warn.WarnTimingDlg;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.card.BillCardUI;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.messservtype.MessservtypeVO;
import nc.vo.dip.messservtype.MyBillVO;
import nc.vo.dip.warningset.DipWarningsetDayTimeVO;
import nc.vo.dip.warningset.DipWarningsetVO;
import nc.vo.pub.pa.AlertTimeConfig;
import nc.vo.pub.pa.TimingSettingVO;
import nc.vo.uap.scheduler.TimeConfigVO;


/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler   extends AbstractMyEventHandler{
	  BillCardUI billUI;
	  ICardController control;

	public MyEventHandler(BillCardUI billUI, ICardController control){
		super(billUI,control);	
		this.billUI=billUI;
		this.control=control;
	}
	protected void onBoSave () throws Exception{
		//增行
		int k=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<k;i++){
			String messsev=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new MessservtypeVO().MESSSER);
			String code=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new MessservtypeVO().CODE);
			String name=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new MessservtypeVO().NAME);
			String type=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new MessservtypeVO().TYPE);
			String preference=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new MessservtypeVO().PREFERENCE);
			String vdef2=(String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, new MessservtypeVO().VDEF2);
			if((code==null||code.trim().equals(""))&&(name==null||name.trim().equals(""))&&(messsev==null||messsev.trim().equals(""))&&(vdef2==null||vdef2.trim().equals(""))&&(preference==null||preference.trim().equals(""))&&(type==null||type.trim().equals(""))){
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
		MyBillVO billvo=(MyBillVO) getBillCardPanelWrapper().getBillVOFromUI();
		if(billvo==null||billvo.getChildrenVO()==null||billvo.getChildrenVO().length<=0){
			super.onBoSave();
		}else{
			MessservtypeVO[] bvos=(MessservtypeVO[]) billvo.getChildrenVO();
			int i=1;
			String index="";
			for(MessservtypeVO bvo:bvos){
				String type=bvo.getType();
//				String prefer=bvo.getPreference();
				if(type!=null&&type.equals("时间段")){
					if(bvo.getDlm()==null||bvo.getVdef3()==null||bvo.getDlm().length()<=0||bvo.getVdef3().length()<=0){
						index=index+i+",";
					}
					/*
					if(prefer==null||prefer.length()<=0||prefer.indexOf("-")<0){
						index=index+i+",";
					}else{
						if(prefer.split("-").length==2){
							String[] str=prefer.split("-");
							if(str[0].indexOf(":")<0||str[1].indexOf(":")<0){
								index=index+i+",";
							}
							String[] str1=str[0].split(":");
							try{
								Date d1=new SimpleDateFormat("HH:mm").parse(str[0]);
								Date d2=new SimpleDateFormat("HH:mm").parse(str[1]);
								int int1=Integer.parseInt(str1[0]);
								if(int1<0||int1>24){
									index=index+i+",";
								}else if(int1==24&&(str1[1]==null||!(str1[1].equals("0")||str1[1].equals("00")))){
									index=index+i+",";
								}
								int int2=Integer.parseInt(str1[1]);
								if(int2<0||int2>60){
									index=index+i+",";
								}
								str1=str[1].split(":");
								int int3=Integer.parseInt(str1[0]);
								if(int3<0||int3>24){
									index=index+i+",";
								}else if(int3==24&&(str1[1]==null||!(str1[1].equals("0")||str1[1].equals("00")))){
									index=index+i+",";
								}
								int int4=Integer.parseInt(str1[1]);
								if(int4<0||int4>60){
									index=index+i+",";
								}
							}catch(Exception  e){
								index=index+i+",";
							}
						}else{
							index=index+i+",";
						}
					}
				*/}
				i++;
			}
			if(index!=null&&index.length()>0){
				getBillUI().showErrorMessage("第"+index.substring(0,index.length()-1)+"行数据时间段不完整");
//				getBillUI().showErrorMessage("第"+index.substring(0,index.length()-1)+"行数据时间段属性的格式不符合 HH:mm-HH:mm");
				return;
			}
			for(MessservtypeVO bvo:bvos){
				if(bvo.getPrimaryKey()==null||bvo.getPrimaryKey().length()<=0){
					HYPubBO_Client.insert(bvo);
				}else{
					HYPubBO_Client.update(bvo);
				}
			}
		}
		getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
//		super.onBoSave();
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
	}
	/*
	 * 添加数据库删除代码
	 * 2011-06-17
	 * zlc*/
	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		String pk=getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(row, new MessservtypeVO().getPKFieldName().toLowerCase())==null?"":
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(row, new MessservtypeVO().getPKFieldName().toLowerCase()).toString();
		if(row>=0){
			Integer flag=MessageDialog.showOkCancelDlg(this.getBillUI(),"提示","是否要删除?");
			if(flag==1){
			  super.onBoLineDel();
			  HYPubBO_Client.deleteByWhereClause(MessservtypeVO.class , new MessservtypeVO().getPKFieldName().toLowerCase()+"='"+pk+"'");
			}
		}else{
			getBillUI().showErrorMessage("请选择要删除的节点！");
			return;
		}
	}
	/*
	 * 打开节点，显示所有数据
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
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
		ini();
	}
	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		super.onBoEdit();
		onBoLineAdd();
	}
	public void onBoTimeSet() throws Exception{
		MessservtypeVO[] bvos=(MessservtypeVO[]) getBillCardPanelWrapper().getSelectedBodyVOs();
		String timepk="";
		if(bvos==null||bvos.length<=0){
			return;
		}
		MessservtypeVO bvo=bvos[0];
		String pkitem="";
		String valueitem="";
		String colk=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("dlm").getName();
		int colt=getBillCardPanelWrapper().getBillCardPanel().getBodyColByKey("vdef3");
		if(getBillCardPanelWrapper().getBillCardPanel().getBillTable().getColumnName(getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedColumn()).equals(colk)){
			timepk=bvo.getVdef4();
			pkitem="vdef4";
			valueitem="dlm";
		}else{
			timepk=bvo.getVdef5();
			pkitem="vdef5";
			valueitem="vdef3";
		}
			AlertTimeConfig atc = null;
			TimingSettingVO tvo=null;
			if(timepk!=null){
				tvo=(TimingSettingVO) HYPubBO_Client.queryByPrimaryKey(TimingSettingVO.class, timepk);
			}
			if(tvo!=null){
				atc=PaConstant.transTimingSettingVO2AlertTimeConfig(tvo);
			}
			WarnTimingDlg dlg=new WarnTimingDlg(getBillUI());
			if(atc!=null){
				dlg.setAlertTime(atc);
			}
			dlg.showModal();
			if (dlg.getResult() == UIDialog.ID_OK) {
				atc = dlg.getAlertTime();
				if(atc!=null){
					int selectrow=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
					TimingSettingVO tsvo=PaConstant.transAlertTimeConfig2TimingSettingVO(atc);
					if(tsvo.getPrimaryKey()!=null&&tsvo.getPrimaryKey().length()>0){
						HYPubBO_Client.update(tsvo);
					}else{
						String warn=HYPubBO_Client.insert(tsvo);
						getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(warn,selectrow, pkitem);
					}
					TimeConfigVO tcvo=PaConstant.transTimingSettingVO2TimeConfigVO(tsvo);
					if(tcvo!=null){
						tcvo.resume();
						Long time=tcvo.getScheExecTime();
						String times=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
						getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(times, selectrow, valueitem);
						System.out.println(times);
					}
				}
			}
	}
	
	
	
}