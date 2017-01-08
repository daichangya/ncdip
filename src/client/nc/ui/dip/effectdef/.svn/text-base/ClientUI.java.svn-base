package nc.ui.dip.effectdef;

//import nc.ui.jyprj.effectdef.MyEventHandler;S
import nc.ui.bd.ref.DataDefinitbRefModel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.card.CardEventHandler;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.datachange.DipDatachangeHVO;
import nc.vo.dip.effectdef.CdSbodyVO;

/**
 * 
 * <P>
 * <B>卡片型单表体UI类</B>
 * </P>
 * <P>
 * 功能说明: <BR>
 * 本界面提供增加、查询、修改、删除功能。
 * 
 * 注意:
 * 在MyEventHandler类中必须重写processNewBodyVO()方法
 * 在ClientUICtrl类中可以修改getBodyCondition()方法,为查询加上默认条件
 * 如果要在单据保存后，页面不刷新，不重新查询数据，则让ClientUICheckRuleGetter继承IRetCurrentDataAfterSave接口
 * 
 * @author 何冰
 * @version 1.0
 * @date 2008-3-26
 */
public class ClientUI extends AbstractClientUI {
	String hpk;

	/** 字段描述 */
	private static final long serialVersionUID = 3899425417611651215L;
	public void initdef(String hpk,boolean flag){
//		动态自定义参照
		DipDatachangeHVO hvo=null;
		try {
			hvo = (DipDatachangeHVO) HYPubBO_Client.queryByPrimaryKey(DipDatachangeHVO.class, hpk);
			//根据哪个字段判断要参照的是哪个表
			String pk_def=hvo.busidata;
			//弹出框的哪个字段配了参照
			UIRefPane pane=(UIRefPane) this.getBillCardPanel().getBodyItem("ys").getComponent();
			DataDefinitbRefModel ref=new DataDefinitbRefModel();
			ref.addWherePart("and dip_datadefinit_h.pk_datadefinit_h='"+pk_def+"' and nvl(dip_datadefinit_h.dr,0)=0");
			pane.setRefInputType(1 /** 名称*/);
			pane.setRefModel(ref);
		} catch (UifException e1) {
			e1.printStackTrace();
		}

		int isEffect;
		if(flag){
			isEffect=1;
		}else{
			isEffect=2;
		}
		this.hpk=hpk;
		try {
			if(hpk!=null){
				CdSbodyVO[] vo=(CdSbodyVO[]) HYPubBO_Client.queryByCondition(CdSbodyVO.class, "dip_datachange_h='"+hpk+"' and flag="+isEffect);
				if(SJUtil.isNull(vo)||vo.length==0){
					for(int i=1;i<9;i++){
						getBillCardPanel().getBillModel().addLine();
						if(i==1){
							getBillCardPanel().setBodyValueAt("affect", i-1, "effectname");
						}else{

							getBillCardPanel().setBodyValueAt("affect"+i, i-1, "effectname");
						}
						getBillCardPanel().setBodyValueAt("影响因素"+i, i-1, "effect");
						getBillCardPanel().setBodyValueAt(isEffect, i-1, "flag");
						getBillCardPanel().setBodyValueAt(hpk, i-1, "dip_datachange_h");
					}
				}else{

					for(int i=1;i<9;i++){
						getBillCardPanel().getBillModel().addLine();
					}
					for(int i=0;i<vo.length;i++){
						vo[i].setEffect("影响因素"+(i+1));
						getBillCardPanel().getBillModel().setBodyRowVO(vo[i],i);
					}
					getBillCardPanel().getBillModel().execLoadFormula();
				}
			}
		} catch (UifException e) {
			e.printStackTrace();
		}

	}

	protected CardEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}

//	@Override
//	public void windowClosed(WindowEvent arg0) {
//	// TODO Auto-generated method stub
////	super.windowClosed(arg0);
//	}


	public String getRefBillType() {
		return null;
	}


	/**
	 * 修改此方法初始化模板控件数据
	 */
	protected void initSelfData() {


		//getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
	}
	public void onButtonAction(ButtonObject bo)throws Exception
	{
		((MyEventHandler)this.getCardEventHandler()).onButtonAction(bo);

	}
	public void setDefaultData() throws Exception {
	}
//	public void init(MyBillVO vo,boolean isadd){
//	onButtonClicked(getButtonManager().getButton(IBillButton.Card));
//	CdSbodyVO hvo=null;
//	if(isadd){
//	//hvo=(CdSbodyVO) vo.getParentVO();
//	//hvo.setEffectname("影响因素名称");
////	hvo.setCondition("在下面填写条件");
////	hvo.setIsnotwarning("是");
////	hvo.setIsnottiming("定时");
////	((MyEventHandler)getManageEventHandler()).lodDefaultVo(new SuperVO[]{hvo});

//	}else{
//	getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
////	((MyEventHandler)getManageEventHandler()).lodDefaultVo(new SuperVO[]{(SuperVO) vo.getParentVO()});
//	}

//	try {
//	updateBtnStateByCurrentVO();
//	} catch (Exception e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//	}
//	if(isadd){

//	onButtonClicked(getButtonManager().getButton(IBillButton.Add));
//	getBillCardPanel().getBillData().setHeaderValueVO(hvo);
//	}
//	}

	@Override
	public boolean onClosing() {
//		return super.onClosing();

		return true;
	}

	//2011-7-19 影响因素第一行没录入数据，其他行都不可编辑。现在不需做控制，固注释了
	/*@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		if(e.getKey().equals("ys")){
			String value=null;
			int row=e.getRow();
			if(row==0){
				value=this.getBillCardPanel().getBodyValueAt(row, "ys")==null?"":this.getBillCardPanel().getBodyValueAt(row, "ys").toString();
				if(value==null || "".equals(value) || value.length()==0){
					for(int i=row+1;i<7;i++){
						this.getBillCardPanel().getBillModel("dip_effectdef").setValueAt("", i, "ys");
						this.getBillCardPanel().getBillModel("dip_effectdef").setCellEditable(i, "ys", false);
						return;
					}
				}
			}
			if(row+1 <this.getBillCardPanel().getRowCount()){
				this.getBillCardPanel().getBillModel("dip_effectdef").setCellEditable(row+1, "ys", true);
			}
			for(int i=row+2;i<8;i++){
				value=this.getBillCardPanel().getBodyValueAt(i, "ys")==null?"":this.getBillCardPanel().getBodyValueAt(i, "ys").toString();
				if(value==null || "".equals(value) || value.length()==0){
					this.getBillCardPanel().getBillModel("dip_effectdef").setCellEditable(i, "ys", false);
				}
			}
		}
	}*/


}
