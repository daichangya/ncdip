package nc.ui.dip.warningset;


import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.bd.ref.DataDefinitTableTreeRefModel;
import nc.ui.dip.warningset.timeset.TimesetActionListener;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.uif.pub.exception.UifException;
import nc.vo.dip.warningset.DipWarningsetVO;
import nc.vo.dip.warningset.MyBillVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;


/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
 public class WarningSetClientUI extends AbstractWarningSetClientUI{
       
       protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
       
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

	protected void initSelfData() {
		
		
		getBillCardPanel().getBillTable().setSortEnabled(false);
	}

	public void setDefaultData() throws Exception {
	}
	
	/* 
	public void init(String name,String code){
		getBillCardPanel().getHeadItem("wname").setValue(name);
		getBillCardPanel().getHeadItem("wcode").setValue(code);
	}*/

	public void init(MyBillVO vo,boolean isadd,String fpk ,int flag) throws UifException{
		//预警弹出框动态参照   李鑫  2011-5-18 开始
		//动态参照
		String pk="";
			pk=fpk;
		   
			
			
			
		   UIRefPane pane=(UIRefPane) getBillCardPanel().getBodyItem("sjb").getComponent();
		  //liyunzhe modify 把预警选择表的参照改成树表型结构参照 2012-06-14
//		   DataDefinitRefModel model=new DataDefinitRefModel();
//		   model.setDefaultFieldCount(3);
//		   //model.addWherePart(" and dip_datadefinit_h.pk_sysregister_h='"+pk+"' and nvl(dip_datadefinit_h.dr,0)=0");
//		   model.addWherePart(" and dip_datadefinit_h.pk_xt='"+pk+"' and isfolder='N' and nvl(dip_datadefinit_h.dr,0)=0 and dip_dataDefinit_h.iscreatetab='Y'");
//		   pane.setRefModel(model);
		   
		   DataDefinitTableTreeRefModel model=new DataDefinitTableTreeRefModel();
			model.setClassWherePart(model.getClassWherePart() +" and pk_xt='"+pk+"'");
			model.addWherePart(" and tabsoucetype='自定义'");
			pane.setRefModel(model);
		   //预警弹出框动态参照   李鑫  2011-5-18  结束
		
		
		
		
		onButtonClicked(getButtonManager().getButton(IBillButton.Card));
		DipWarningsetVO hvo=null;
		if(!isadd){
			getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
			((MyEventHandler)getManageEventHandler()).lodDefaultVo(new SuperVO[]{(SuperVO) vo.getParentVO()});
		}

		try {
			updateBtnStateByCurrentVO();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(isadd){
			((MyEventHandler)getManageEventHandler()).add();
			hvo=(DipWarningsetVO) vo.getParentVO();
			hvo.setCondition("在下面填写条件");
			hvo.setIsnotwarning("禁用");
			hvo.setIsnottiming("定时");
			hvo.setPk_sys(hvo.getPk_sys());
			
			getBillCardPanel().getBillData().setHeaderValueVO(hvo);
			if(hvo.getTasktype()!=null&&hvo.getTasktype().equals("0001AA1000000001FVAD")){
				getBillCardPanel().getHeadItem("isnotwarning").setEdit(true);
			}else{
				getBillCardPanel().getHeadItem("isnotwarning").setEdit(false);
			}
//			add by zhw 2012-05-17 只要在数据流程节点中表头字段:是否启用预警;预警任务类型可以编辑
			if(flag != 2){
				getBillCardPanel().getHeadItem("isnottiming").setEdit(false);
				getBillCardPanel().getHeadItem("isnotwarning").setEdit(false);
			}
			//add by zhw 2012-05-17 -----------------------------------------------------end
		}else{
			hvo=(DipWarningsetVO) vo.getParentVO();
		}
		
		UIRefPane rf=(UIRefPane) getBillCardPanel().getHeadItem("def_timeset").getComponent();
		rf.getUITextField().setEnabled(false);
		Object fs=getBillCardPanel().getHeadItem("isnottiming").getValueObject();
//		if(fs!=null&&fs.toString().equals("定时")){
//			rf.getUIButton().setEnabled(true);
//		}else{
//			rf.getUIButton().setEnabled(false);
//		}
//		add by zhw 2012-05-18 只要在数据流程节点中表头字段:是否启用预警;预警任务类型可以编辑
//		if(flag != 2){
			rf.getUIButton().setEnabled(false);
//		}
		//add by zhw 2012-05-18 -----------------------------------------------------end
		rf.getUIButton().addActionListener(new TimesetActionListener(this));
		Object time=getBillCardPanel().getHeadItem("nexttime").getValueObject();
		if(time==null||time.toString().length()<=0){
			getBillCardPanel().getHeadItem("def_timeset").setValue("未定义");
		}else{
			getBillCardPanel().getHeadItem("def_timeset").setValue(time.toString());
		}
		
		
	}
	public void gongshi(){
		getBillCardPanel().getBillModel().execLoadFormula();
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		return super.beforeEdit(e);
	}
	
	public void onButtonAction(ButtonObject bo)throws Exception	{
		((MyEventHandler)this.getManageEventHandler()).onButtonAction(bo);
	}

	IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		super.afterEdit(e);
		if(e.getKey().equals("isnottiming")){
			UIRefPane rf=(UIRefPane) getBillCardPanel().getHeadItem("def_timeset").getComponent();
			Object fs=getBillCardPanel().getHeadItem("isnottiming").getValueObject();
			if(fs!=null&&fs.toString().equals("定时")){
				rf.getUIButton().setEnabled(true);
			}else{
				rf.getUIButton().setEnabled(false);
			}
		}
		else if(e.getKey().equals("sjb")){
			getBillCardPanel().execBodyFormulas(e.getRow(), getBillCardPanel().getBodyItem("sjb").getLoadFormula());
			getBillCardPanel().execBodyFormulas(e.getRow(), getBillCardPanel().getBodyItem("sjbm").getLoadFormula());
		}else if(e.getKey().equals("zt")){
			getBillCardPanel().execBodyFormulas(e.getRow(), getBillCardPanel().getBodyItem("zt").getLoadFormula());
		}
	}
	public void onBoWartime(){
		try {
			((MyEventHandler)this.getManageEventHandler()).onBoWarnTime();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
