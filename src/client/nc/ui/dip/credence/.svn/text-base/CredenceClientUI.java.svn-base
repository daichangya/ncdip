package nc.ui.dip.credence;


import java.awt.event.ActionListener;
import java.sql.SQLException;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.jdbc.framework.exception.DbException;
import nc.ui.bd.ref.SubjectTreeRefModel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.card.CardEventHandler;
import nc.uif.pub.exception.UifException;
import nc.vo.dip.credence.CredenceHVO;
import nc.vo.dip.datachange.DipDatachangeBVO;
import nc.vo.dip.datadefinit.DipDatadefinitBVO;
import nc.vo.dip.effectdef.CdSbodyVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;


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
public class CredenceClientUI extends AbstractCredenceClientUI{



	/** 字段描述 */
	public String selectnode="";//选择树节点

	private String pk_credence_h="";		

	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
	throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
	throws Exception {	}


	public String getPk_credence_h() {
		return pk_credence_h;
	}

	public void setPk_credence_h(String pk_credence_h) {
		this.pk_credence_h = pk_credence_h;
	}
	private String pk_datadef;

	public String getPk_datadef() {
		return pk_datadef;
	}

	public void setPk_datadef(String pk_datadef) {
		this.pk_datadef = pk_datadef;
	}
	/**
	 * @param bvopk附表主键
	 * @param pk_datadef 数据定义的主表主键
	 * */
	public boolean initDataDef(String bvopk,String pk_datadef){
		boolean isadd=false;
		setPk_credence_h(bvopk);	
		setPk_datadef(pk_datadef);



		String[] saveitem=new String[]{"credtype","attmentnum","vdef1"};
		String[] showitem=new String[]{"def_credtype","def_attmentnum","vdef1"};
		for(int i=0;i<saveitem.length;i++){
			UIRefPane rf=(UIRefPane) getBillCardPanel().getHeadItem(showitem[i]).getComponent();
			CredenceListener listener = new CredenceListener(this,showitem[i],saveitem[i],CredenceListener.TYPE_HEAD,rf);
			if(rf!=null){
				rf.getUIButton().removeActionListener(rf.getUIButton().getListeners(ActionListener.class)[0]);
				rf.getUIButton().addActionListener(listener);
				rf.setAutoCheck(false);
				rf.setEditable(false);
			}
		}
		String[] savebitem=new String[]{"summary"/*,"assistant"*/,"currency","money","numbers","verificationno","xtno","busdate","remark","affect","affect2","affect3","affect4","affect5","affect6","affec7","affect8","ctrl"};
		String[] showbitem=new String[]{"def_summary"/*,"def_assistant"*/,"def_currency","def_money","def_numbers","def_verificationno","def_xtno","def_busdate","def_remark","affect","affect2","affect3","affect4","affect5","affect6","affec7","affect8","ctrl"};
		for(int i=0;i<savebitem.length;i++){
			if(getBillCardPanel().getBodyItem(showbitem[i])!=null){
				UIRefPane rf=(UIRefPane) getBillCardPanel().getBodyItem(showbitem[i]).getComponent();
				CredenceListener listener = new CredenceListener(this,showbitem[i],savebitem[i],CredenceListener.TYPE_BODY,rf);
				if(rf!=null){
					//			rf.getUIButton().removeMouseListener(rf.getUIButton().getMouseListeners()[0]);
					rf.getUIButton().removeActionListener(rf.getUIButton().getListeners(ActionListener.class)[0]);
					rf.getUIButton().addActionListener(listener);
					rf.setAutoCheck(false);
					rf.setEditable(false);
				}
			}
		}

		String[] tsaveitem=new String[]{"voperatorid","doperatordate"};
		String[] tshowitem=new String[]{"def_voperatorid","def_doperatordate"};
		for(int i=0;i<tsaveitem.length;i++){
			UIRefPane rf=(UIRefPane) getBillCardPanel().getTailItem(tshowitem[i]).getComponent();
			CredenceListener listener = new CredenceListener(this,tshowitem[i],tsaveitem[i],CredenceListener.TYPE_TAIL,rf);
			if(rf!=null){
				rf.getUIButton().removeActionListener(rf.getUIButton().getListeners(ActionListener.class)[0]);
				rf.getUIButton().addActionListener(listener);
				rf.setAutoCheck(false);
				rf.setEditable(false);
			}
		}
		DipDatachangeBVO ddbvo=null;
		try {
			ddbvo=(DipDatachangeBVO) HYPubBO_Client.queryByPrimaryKey(DipDatachangeBVO.class, bvopk);
			if(ddbvo!=null&&ddbvo.getPk_datachange_h()!=null&&ddbvo.getPk_datachange_h().length()>0){
				String pk_h=ddbvo.getPk_datachange_h();
				//select dip_effectdef.effectname,dip_effectdef.dip_datachange_h,dip_effectdef.dip_datachange_h from dip_effectdef
				CdSbodyVO[] bodyvo=(CdSbodyVO[]) HYPubBO_Client.queryByCondition(CdSbodyVO.class, "dip_datachange_h='"+pk_h+"' and flag=1");
				if(bodyvo!=null&&bodyvo.length>0){
					for(int i=0;i<bodyvo.length;i++){
						if(bodyvo[i].getEffectfiled()!=null&&bodyvo[i].getEffectfiled().length()>0){
							getBillCardPanel().getBodyPanel().showTableCol(bodyvo[i].getEffectname());//.hideTableCol("affect");
//							getBillCardPanel().getBodyItem(bodyvo[i].getEffectname()).setEnabled(true);
							DipDatadefinitBVO bvo=(DipDatadefinitBVO) HYPubBO_Client.queryByPrimaryKey(DipDatadefinitBVO.class, bodyvo[i].getEffectfiled());
							if(bvo!=null&&bvo.getCname()!=null&&bvo.getCname().length()>0){
								getBillCardPanel().getBodyItem(bodyvo[i].getEffectname()).setName(bvo.getCname());
								getBillCardPanel().setBillData(getBillCardPanel ().getBillData()); 
							}
						}
					}
				}
			}
		} catch (UifException e1) {
			e1.printStackTrace();
		}
		CredenceHVO[] hvo;
		String bd_glorgbook="";
		try {
			hvo = (CredenceHVO[]) HYPubBO_Client.queryByCondition(CredenceHVO.class, "pk_datadefinit_h='"+bvopk+"'");

			if(hvo==null||hvo.length==0){
				try {
					bd_glorgbook=((MyEventHandler)this.getCardEventHandler()).ini(ddbvo);//.onButtonAction(getButtonManager().getButton(IBillButton.Add));
//					getBillCardPanel().getHeadItem("pk_datadefinit_h").setValue(bvopk);
				} catch (Exception e) {
					e.printStackTrace();
				}
				isadd=true;
			}else{
				//2011-6-30 增加了下面一行代码：避免保存后，再打开凭证模板“单位”无值显示问题
				hvo[0].setCorp(ddbvo.getOrgname());
				((MyEventHandler)this.getCardEventHandler()).loadData(hvo[0]);
				bd_glorgbook=hvo[0].getAccoutbook();
				isadd=false;
			}
		} catch (UifException e) {
			e.printStackTrace();
		}
		IQueryField iqf=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
		UIRefPane rf=(UIRefPane) getBillCardPanel().getBodyItem("subjects").getComponent();
		SubjectTreeRefModel ref=(SubjectTreeRefModel) rf.getRefModel();

		//2011-6-30 
		String unicode="";
		String unit=null;//主键
		String s=null;
		String s1=null;		
		try {
			//2011-6-28 
			String orgCode=ddbvo.getOrgcode();
			String[] array=orgCode.split(",");

			String unitname=null;//公司名称
			for(int i=0;i<array.length;i++){
				if(i==0){
					unit=array[i];
					s=unit;
					unitname=(String) HYPubBO_Client.findColValue("bd_corp", "unitname", "pk_corp='"+unit+"' and nvl(dr,0)=0");
				}else{
					unitname=unitname+","+HYPubBO_Client.findColValue("bd_corp", "unitname", "pk_corp='"+array[i]+"' and nvl(dr,0)=0");
					unit=unit+","+array[i];
				}
			}

			//unicode=iqf.queryfield("select unitcode from bd_corp where pk_corp='"+(ddbvo==null?"":ddbvo.getOrgcode())+"'");
			//2011-6-30 注释了上面一行原本的代码。 公司多个时，凭证模板中科目参照默认为第一个公司的科目参照
			unicode=iqf.queryfield("select unitcode from bd_corp where pk_corp='"+(ddbvo==null?"":s)+"'");
			//s1=unicode;
		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DbException e) {
			e.printStackTrace();
		}
		String pk_glorgbook="";
		try{
			pk_glorgbook=iqf.queryfield("select pk_glorgbook from bd_glorgbook where pk_glbook='"+bd_glorgbook+"' and glorgbookcode like '"+unicode+"-%'");
		}catch (Exception e){
			e.printStackTrace();
		}
		ref.setWherePart(" bd_accsubj.pk_glorgbook='"+pk_glorgbook+"'");
		rf.setRefModel(ref);

		UIRefPane rfa=(UIRefPane) getBillCardPanel().getBodyItem("def_assistant").getComponent();
		FzhsCredenceListener lis=new FzhsCredenceListener(this,"def_assistant","assistant",pk_glorgbook,rfa);
		if(rfa!=null){
			rfa.getUIButton().removeActionListener(rfa.getUIButton().getListeners(ActionListener.class)[0]);
			rfa.getUIButton().addActionListener(lis);
			rfa.setAutoCheck(false);
			rfa.setEditable(false);
		}
		if(isadd){
			getBillCardPanel().addLine();
		}
		return isadd;
	}
	protected void initSelfData() {}

	@SuppressWarnings("unused")
	public void setDefaultData() throws Exception {
	}




	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}
	public void onButtonAction(ButtonObject bo)throws Exception
	{
		((MyEventHandler)this.getCardEventHandler()).onButtonAction(bo);

	}
	protected CardEventHandler createEventHandler()
	{
		return new MyEventHandler(this, getUIControl());
	}
	@Override
	protected ICardController createController() {
		// TODO Auto-generated method stub
		return new CredenceClientUICtrl();
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		
//		自动增加一行。
		if(!(e.getKey().equals("vdef1")||e.getKey().equals("def_attmentnum")||e.getKey().equals("def_credtype"))){
			int k=this.getBillCardPanel().getBillModel("dip_credence_b").getRowCount();
			int m=this.getBillCardPanel().getBillTable("dip_credence_b").getSelectedRow();
			if(k-1==m){
				this.getBillCardPanel().getBillModel("dip_credence_b").addLine();
			}
		}
		
		
		//如果科目变了，所有的字段都清空
		if(e.getKey().equals("subjects")){
			String[] ss={"summary","assistant","currency","money","numbers","verificationno","xtno","busdate","remark","affect","affect2","affect3","affect4","affect5","affect6","affec7","affect8","ctrl",
					"def_summary","def_assistant","def_currency","def_money","def_numbers","def_verificationno","def_xtno","def_busdate","def_remark"};
			int row=e.getRow();
			for(int i=0;i<ss.length;i++){
				getBillCardPanel().setBodyValueAt("", row, ss[i]);
			}
		}

	}
}
