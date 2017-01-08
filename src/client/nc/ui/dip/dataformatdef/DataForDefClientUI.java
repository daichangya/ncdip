package nc.ui.dip.dataformatdef;


import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.bd.ref.MessTypeRefModel;
import nc.ui.bd.ref.VDipMDUnionRefModel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.card.CardEventHandler;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.dataformatdef.DataformatdefBVO;
import nc.vo.dip.dataformatdef.DataformatdefHVO;
import nc.vo.dip.dataformatdef.MyBillVO;
import nc.vo.dip.dataorigin.DipDataoriginVO;
import nc.vo.dip.datarec.DipDatarecHVO;
import nc.vo.ntb.outer.IAccessableBusiVO;
import nc.vo.ntb.outer.NtbParamVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;



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
//DataFormatDefClientUI

public class DataForDefClientUI extends AbstractDataForDefClientUI {
	private String type="";

	IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	//消息流类型
	UIRefPane pane=(UIRefPane) this.getBillCardPanel().getHeadItem("messflowtype").getComponent();
	MessTypeRefModel model=new MessTypeRefModel();

	//消息标志: 标志头、标志尾
	//2011-6-9 由于把表头 标志头、标志尾字段隐藏，所以把以下代码注释掉了
	/*UIRefPane pane2=(UIRefPane) this.getBillCardPanel().getHeadItem("beginsign").getComponent();
	MessageLogoRefModel model2=new MessageLogoRefModel();
	UIRefPane pane3=(UIRefPane) this.getBillCardPanel().getHeadItem("endsign").getComponent();
	MessageLogoRefModel model3=new MessageLogoRefModel();*/

	//"数据项"字段参照
	UIRefPane pane4 =(UIRefPane) this.getBillCardPanel().getBodyItem("datakind").getComponent();
	//2011-6-9 将数据格式定义表体 数据项 参照<nc.ui.bd.ref.DataDefinitbRefModel> 改为<nc.ui.bd.ref.V_Dip_MDUnionRefModel>
	/*DataDefinitbRefModel model4=new DataDefinitbRefModel();*/
	VDipMDUnionRefModel vmodel=new VDipMDUnionRefModel();

	private DipDatarecHVO hvo=null;
	public DipDatarecHVO getHvo() {
		return hvo;
	}
	private DataformatdefHVO datahvo=null;
	public DataformatdefHVO getDatahvo(){
		return datahvo;
	}
	public DataForDefClientUI(DipDatarecHVO hvo) {
		this.hvo=hvo;
		DataformatdefHVO dhvo=new DataformatdefHVO();

		//2011-6-9 由于把表头 标志头、标志尾字段隐藏，所以把以下代码注释掉了
		/*model2.addWherePart(" and ctype='标志头'");
		pane2.setRefModel(model2);
		model3.addWherePart(" and ctype='标志尾'");
		pane3.setRefModel(model3);*/

		//"数据项"字段参照
		/*model4.addWherePart(" and dip_datadefinit_b.pk_datadefinit_h ='"+hvo.getMemorytable()+"' and nvl(dip_datadefinit_b.dr,0)=0");
		pane4.setRefModel(model4);*/
		//2011-6-9 "数据项"字段参照
//		vmodel.addWherePart(" and (v_dip_mdunion.hpk ='"+hvo.getMemorytable()+"' or v_dip_mdunion.type='m')");
		//把系统的自带 where 11 = 11条件去掉
		/*vmodel.setWherePart("v_dip_mdunion.hpk ='"+hvo.getMemorytable()+"' or v_dip_mdunion.type='m'");*/
//		pane4.setRefModel(vmodel);

		if(!SJUtil.isNull(hvo)){
			//数据接收定义主表主键
			String recpk=hvo.getPk_datarec_h();
			getBillCardPanel().getHeadItem("pk_datarec_h").setValue(recpk);
			String sourcetype=hvo.getSourcetype();//数据来源类型：由于是参照，存放的是主键
			UFBoolean trancon=hvo.getTrancon()==null?new UFBoolean(false):new UFBoolean(hvo.getTrancon());//传输标志控制
			UFBoolean backcon=hvo.getBackcon()==null?new UFBoolean(false):new UFBoolean(hvo.getBackcon());//回执标志控制
			try {
				//数据来源类型vo
				DipDataoriginVO ddvo=(DipDataoriginVO) HYPubBO_Client.queryByPrimaryKey(DipDataoriginVO.class, sourcetype);
				String type=ddvo.getName();//数据来源类型
				DataformatdefHVO[] vos=(DataformatdefHVO[]) HYPubBO_Client.queryByCondition(DataformatdefHVO.class, "pk_datarec_h ='"+recpk+"' and nvl(dr,0)=0") ;

				if(type !=null && type.equals("消息总线")){
					vmodel.setWherePart(" (v_dip_mdunion.hpk ='"+hvo.getMemorytable()+"' or v_dip_mdunion.type='m')");
					pane4.setRefModel(vmodel);
				}else{
					vmodel.setWherePart(" (v_dip_mdunion.hpk ='"+hvo.getMemorytable()+"' or v_dip_mdunion.datatype='自定义')");
					pane4.setRefModel(vmodel);

				}
				if(vos!=null && vos.length>0){
					dhvo=vos[0];
					this.datahvo=dhvo;
					DataformatdefBVO[] bvos=(DataformatdefBVO[]) HYPubBO_Client.queryByCondition(DataformatdefBVO.class, dhvo.getPKFieldName()+"='"+dhvo.getPrimaryKey()+"' order by code");
					MyBillVO billvo=new MyBillVO();
					if(type !=null && !type.trim().equals("")){
						//调用查询消息流类型的方法
						refOfType(type, trancon, backcon);						
					}

					billvo.setParentVO(dhvo);
					billvo.setChildrenVO(bvos);
					((MyEventHandler)getCardEventHandler()).lodDefaultVo(dhvo,bvos,type);
					//根据类型的不同，设置某一行的某一列是否可编辑
					this.type=type;
					if(!type.equals("消息总线")){
						setEditOfType(type,true);
					}

				}else{
					if(type !=null && !type.trim().equals("")){
						//调用查询消息流类型的方法
						refOfType(type, trancon, backcon);
						//根据类型的不同，设置某一行的某一列是否可编辑
						setEditOfType(type,false);
					}
				}	
			} catch (UifException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	/*
	 * 版权所有：商佳科技
	 * 作者：程莉
	 * 功能：数据格式定义 表头 消息流类型参照方法：根据不同的数据来源类型，参照出不同的消息类型
	 * 参数：数据来源类型、传输控制标志、回执标志
	 * 日期：2011-5-15
	 */
	public void refOfType(String type,UFBoolean trancon,UFBoolean backcon){
		if(type.equals("主动抓取")){
			model.addWherePart(" and pk_messtype='0001AA1000000001HYWO' and nvl(dr,0)=0");
			//标志头、标志尾不可编辑
			getBillCardPanel().getHeadItem("beginsign").setEdit(false);
			getBillCardPanel().getHeadItem("endsign").setEdit(false);
		}else if(type.equals("消息总线")){
			//消息总线：消息流格式不可编辑,默认为"顺序"
			getBillCardPanel().getHeadItem("messflowdef").setEdit(false);

			/*
			 * 1、如果传输控制标志为true，传输回执标志为false，则表头“消息流类型”参照显示“传输开始标志”、“传输数据标志”、“传输结束标志”
			 * 2、如果传输控制标志为true，传输回执标志为true，则表头“消息流类型”参照显示“传输开始标志”、“传输数据标志”、“传输结束标志”、“回执标志”
			 */
			if(trancon.booleanValue()){
				if(!backcon.booleanValue()){
					model.addWherePart(" and (pk_messtype='0001AA100000000142YQ' or pk_messtype='0001AA100000000142YS' or pk_messtype='0001ZZ10000000018K7M') and nvl(dr,0)=0");
				}else{
					model.addWherePart(" and (pk_messtype='0001AA100000000142YQ' or pk_messtype='0001AA100000000142YS' or pk_messtype='0001ZZ10000000018K7M' or pk_messtype='0001ZZ1000000001GFD7') and nvl(dr,0)=0");
				}
			}else{
				/*
				 * 1、如果传输控制标志为false，传输回执标志为true，则表头“消息流类型”参照显示“传输数据标志”、“回执标志”
				 * 2、如果传输控制标志为false，传输回执标志为false，则表头“消息流类型”参照显示“传输数据标志”
				 */
				if(backcon.booleanValue()){
					model.addWherePart(" and (pk_messtype='0001AA100000000142YS' or pk_messtype='0001ZZ1000000001GFD7') and nvl(dr,0)=0");
				}else{
					model.addWherePart(" and pk_messtype='0001AA100000000142YS' and nvl(dr,0)=0");
				}
			}
		}else if(type.equals("格式文件")){
			model.addWherePart(" and pk_messtype='0001AA1000000001HYWP' and nvl(dr,0)=0");
			getBillCardPanel().getHeadItem("beginsign").setEdit(false);
			getBillCardPanel().getHeadItem("endsign").setEdit(false);
		}else{
			//中间表
			model.addWherePart(" and pk_messtype='0001AA1000000001HYWQ' and nvl(dr,0)=0");
			getBillCardPanel().getHeadItem("beginsign").setEdit(false);
			getBillCardPanel().getHeadItem("endsign").setEdit(false);
		}
		pane.setRefModel(model);
	}

	/*
	 * 作者：程莉
	 * 描述：1.如果是"消息总线"类型，则前三行的"数据项"列固定且不可编辑，其余的该列处于可编辑状态；
	 * 2.如果不是，则该列都处于可编辑
	 * 参数：消息来源类型
	 * 日期：2011-5-17
	 */
	public void setEditOfType(String type,boolean isExist){
		if(type.equals("消息总线")){
			//标志头、系统标识、站点标识、业务标识
			if(!isExist){
				for(int i=1;i<=200;i++){		
					//增行
					getBillCardPanel().getBillModel().addLine();
					this.getBillCardPanel().getBillModel().setValueAt(i, (i-1), "code");
					if(i==2){
						this.getBillCardPanel().getBillModel().setValueAt("系统标志", (i-1), "datakind");
					}else if(i==3){
						this.getBillCardPanel().getBillModel().setValueAt("站点标识", (i-1), "datakind");
					}else if(i==4){
						this.getBillCardPanel().getBillModel().setValueAt("业务标识", (i-1), "datakind");
					}
				}
			}
			for(int i=0;i<200;i++){
				if(i==1 || i==2 || i==3){
					//设置某一行的某一列不可编辑状态
					this.getBillCardPanel().getBillModel("dip_dataformatdef_b").setCellEditable(i, "datakind", false);
				}else{
					try {
						this.setBillOperate(IBillOperate.OP_EDIT);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}else{
			if(!isExist){
				for(int i=0;i<200;i++){		
					//增行
					getBillCardPanel().getBillModel().addLine();
					this.getBillCardPanel().getBillModel().setValueAt((i+1), i, "code");
					try {
						this.setBillOperate(IBillOperate.OP_EDIT);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	protected CardEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}

	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
	throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
	throws Exception {	}

	protected void initSelfData() {

	}

	public void setDefaultData() throws Exception {
	}

	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}



	public NtbParamVO[] getLinkDatas(IAccessableBusiVO[] arg0)
	throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub

		super.afterEdit(e);	
		if(type.equals("消息总线")){
			if(e.getKey().equals("messflowtype")){
				String ss=getBillCardPanel().getHeadItem("messflowtype").getValueObject().toString();
//				if(ss.equals("0001ZZ10000000018K7M")){//传输结束标志
				try {
					DataformatdefHVO[] hvos=(DataformatdefHVO[]) HYPubBO_Client.queryByCondition(DataformatdefHVO.class, "messflowtype='"+ss+"' and pk_datarec_h='"+hvo.getPrimaryKey()+"' and nvl(dr,0)=0");
					if(hvos==null||hvos.length<=0){
						for(int i=1;i<=200;i++){		
							//增行
							this.getBillCardPanel().getBillModel().setValueAt(i, (i-1), "code");
							if(i==2){
								this.getBillCardPanel().getBillModel().setValueAt("系统标志", (i-1), "datakind");
							}else if(i==3){
								this.getBillCardPanel().getBillModel().setValueAt("站点标识", (i-1), "datakind");
							}else if(i==4){
								this.getBillCardPanel().getBillModel().setValueAt("业务标识", (i-1), "datakind");
							}else{
								//2011-6-30 
								//this.getBillCardPanel().getBillModel().setValueAt("", (i-1), "datakind");
								this.getBillCardPanel().getBillModel().setValueAt(null, (i-1), "datakind");
								this.getBillCardPanel().getBillModel().setValueAt(null, (i-1), "vdef1");
								this.getBillCardPanel().getBillModel().setValueAt(null, (i-1), "vdef2");
							}
							this.getBillCardPanel().getBillModel().setValueAt("", (i-1), "pk_datafornatdef_b");
						}
						getBillCardPanel().getHeadItem("pk_dataformatdef_h").setValue("");
						//设置某一行的某一列不可编辑状态
						this.getBillCardPanel().getBillModel("dip_dataformatdef_b").setCellEditable(1, "datakind", false);
						this.getBillCardPanel().getBillModel("dip_dataformatdef_b").setCellEditable(2, "datakind", false);
						this.getBillCardPanel().getBillModel("dip_dataformatdef_b").setCellEditable(3, "datakind", false);
						this.getBillCardPanel().getBillModel("dip_dataformatdef_b").setCellEditable(0, "datakind", true);

					}else{
						this.datahvo=hvos[0];
						DataformatdefBVO[] bvos=(DataformatdefBVO[]) HYPubBO_Client.queryByCondition(DataformatdefBVO.class, hvos[0].getPKFieldName()+"='"+hvos[0].getPrimaryKey()+"' order by code");
						MyBillVO billvo=new MyBillVO();

						billvo.setParentVO(hvos[0]);
						billvo.setChildrenVO(bvos);
						((MyEventHandler)getCardEventHandler()).lodDefaultVo(hvos[0],bvos,type);
						//根据类型的不同，设置某一行的某一列是否可编辑
						this.type=type;
						if(!type.equals("消息总线")){
							setEditOfType(type,true);
						}
					}
				} catch (UifException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//				}else if(ss.equals("0001AA100000000142YQ")){//传输开始标志

//				}else if(ss.equals("0001AA100000000142YS")){//传输数据标志

//				}
			}
		}

		/*
		 * 作者：程莉
		 * 日期：2011-6-14
		 * 描述："顺序"对照，则"对应项(correskind)"不可编辑；"字段"对照，则"对应项"可编辑 0:顺序对照；1：字段对照
		 */
		//消息流格式：0：顺序,1：字段
		if(e.getKey().equals("messflowdef")){
			String messflowdef=(String) getBillCardPanel().getHeadItem("messflowdef").getValueObject();
			int row=getBillCardPanel().getRowCount();
			//如果为"顺序"对照，则"对应项"不可编辑 0:顺序对照；1：字段对照
			if("0".equals(messflowdef)){
				for(int i=0;i<row;i++){
					getBillCardPanel().getBillModel("dip_dataformatdef_b").setCellEditable(i, "correskind", false);
				}
			}else{
				for(int i=0;i<row;i++){
					getBillCardPanel().getBillModel("dip_dataformatdef_b").setCellEditable(i, "correskind", true);
				}
			}
		}
		
		this.getBillCardPanel().execHeadEditFormulas();
	}

	public void onButtonAction(ButtonObject bo)throws Exception
	{
		((MyEventHandler)this.getCardEventHandler()).onButtonAction(bo);

	}
}
