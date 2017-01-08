package nc.ui.dip.messagelogo;

import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.card.CardEventHandler;
import nc.uif.pub.exception.UifException;
import nc.util.dip.sj.FunctionUtil;
import nc.util.dip.sj.IContrastUtil;
import nc.vo.dip.runsys.DipRunsysBVO;
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
 public class MessageLogoClientUI extends AbstractMessageLogoClientUI{
	 
	 public MessageLogoClientUI(){
    	   super();
    	   ((MyEventHandler)this.getCardEventHandler()).ini();
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
		getBillCardPanel().getBodyPanel().setRowNOShow(true);
	}

	public void setDefaultData() throws Exception {
	}

	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		super.afterEdit(e);
		
//		自动增行
		int k=this.getBillCardPanel().getBillModel().getRowCount();
		int m=this.getBillCardPanel().getBillTable().getSelectedRow();
		if(k-1==m){
			this.getBillCardPanel().getBillModel().addLine();
		}
		if(e.getKey().equalsIgnoreCase("cdata")){
			int row=e.getRow();
			String cdata=this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row, "cdata")==null?"":this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row, "cdata").toString();
			String type=(String) getBillCardPanel().getBillModel().getValueAt(row, "ctype");
			if(cdata!=null&&cdata.length()>0){
				if(type==null||!type.equals(FunctionUtil.TIMENAME)){
					this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(cdata.toUpperCase(), row, "cdata");
				}
				//add by zhw 2012-05-17 类型为时间函数时，数值中填写的内容必须满足规范
				else if(null != type && FunctionUtil.TIMENAME.equals(type)){
					String str[]=null;
					DipRunsysBVO[] runbvos=null;
					try {
						runbvos=(DipRunsysBVO[]) HYPubBO_Client.queryByCondition(DipRunsysBVO.class, " syscode='DIP-0000014' and nvl(dr,0)=0 ");
					} catch (UifException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(runbvos!=null&&runbvos.length>0){
						String value=runbvos[0].getSysvalue();
						if(value!=null&&value.toString().trim().length()>0){
							if(value.contains(",")){
								str=value.split(",");
							}else{
								str=new String[]{value};
							}
						}else{
							this.showErrorMessage("DIP-0000014参数不能为空");
							return;
						}
					}else{
						this.showErrorMessage("DIP-0000014参数不能为空");
						return;
					}
//					String str[]={"yyMMdd","yyyyMMdd","HHmmss","yyMMddHHmmss","yyyyMMddHHmmss","yyyy-MM-dd HH:mm:ss"};
					Boolean bool = false;
					for (int i = 0; i < str.length; i++) {
						if(str[i].equals(cdata)){
							bool = true;
						}
					}
					if(!bool){
						this.showWarningMessage("类型为时间函数,数值必须为以下几种:"+runbvos[0].getSysvalue()+"");
						this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(null, row, "cdata");
					}
				}
				//add by zhw 2012-05-17 ----------------------------------------end
				
				if(type!=null&&type.equals(FunctionUtil.MARKNAME)){
					if(cdata!=null&&cdata.trim().length()>0){
						String escs[]=IContrastUtil.ESCS.split(",");
						if(escs!=null&&escs.length>0){
							for(int i=0;i<escs.length;i++){
								if(cdata.equals(escs[i])){
									this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(new UFBoolean(true), row, "vdef1");
								}
							}
						}
					}
				}
				
				
			}
		}
		if(e.getKey().equalsIgnoreCase("ctype")){
			int row=e.getRow();
			String cdata=this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row, "cdata")==null?"":this.getBillCardWrapper().getBillCardPanel().getBillModel().getValueAt(row, "cdata").toString();
			String type=(String) getBillCardPanel().getBillModel().getValueAt(row, "ctype");
			if(cdata!=null&&cdata.length()>0){
				if(type!=null||!type.equals(FunctionUtil.TIMENAME)){
					this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(cdata.toUpperCase(), row, "cdata");
				}
			}
			//add by zhw 2012-05-17 类型为时间函数时，数值中填写的内容必须满足规范
			if(null != type && FunctionUtil.TIMENAME.equals(type)){

				String str[]=null;
				DipRunsysBVO[] runbvos=null;
				try {
					runbvos=(DipRunsysBVO[]) HYPubBO_Client.queryByCondition(DipRunsysBVO.class, " syscode='DIP-0000014' and nvl(dr,0)=0 ");
				} catch (UifException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(runbvos!=null&&runbvos.length>0){
					String value=runbvos[0].getSysvalue();
					if(value!=null&&value.toString().trim().length()>0){
						if(value.contains(",")){
							str=value.split(",");
						}else{
							str=new String[]{value};
						}
					}else{
						this.showErrorMessage("DIP-0000014参数不能为空");
						return;
					}
				}else{
					this.showErrorMessage("DIP-0000014参数不能为空");
					return;
				}
//				String str[]={"yyMMdd","yyyyMMdd","HHmmss","yyMMddHHmmss","yyyyMMddHHmmss","yyyy-MM-dd HH:mm:ss"};
				Boolean bool = false;
				for (int i = 0; i < str.length; i++) {
					if(str[i].equals(cdata)){
						bool = true;
					}
				}
				if(!bool){
					this.showWarningMessage("类型为时间函数,数值必须为以下几种:"+runbvos[0].getSysvalue()+"");
					this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(null, row, "cdata");
				}
			
			//	this.showHintMessage("类型为时间函数,数值必须为以下几种:yyMMdd,yyyyMMdd,HHmmss,\nyyMMddHHmmss,yyyyMMddHHmmss,\nyyyy-MM-dd HH:mm:ss");
//				this.getBillCardPanel().getHeadItem("ctype").getComponent().get
			}
			//add by zhw 2012-05-17 ----------------------------------------end
			
			else if(type!=null&&type.equals(FunctionUtil.COUNTNAME)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.COUNTENAME, row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.MESSAGECOUNTNAME)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.MESSAGECOUNTENAME, row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.PKNAME)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.PKENAME, row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.CORPPKNAME)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.CORPPKENAME, row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.USERIDNAME)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.USERIDENAME, row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			
			else if(type!=null&&type.equals(FunctionUtil.TOMONTHFUN)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.TOMONTHFUNPK, row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.TOLOWER)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.TOLOWERPK, row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.TOUPPER)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.TOUPPERPK, row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.TRIM)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.TRIMPK, row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.SYSUSERID)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.SYSUSERIDPK, row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.SYSUSERCODE)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.SYSUSERCODEPK, row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.SYSUSERNAME)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.SYSUSERNAMEPK, row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.SYSUSERDATE)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.SYSUSERDATEPK, row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.SELECTPK)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.SELECTPKPK, row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.SHOWREPLACE)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.SHOWREPLACEPK+"(,)", row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.SAVEREPLACE)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.SAVEREPLACEPK+"(,)", row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.STRREPLACE)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.STRREPLACEPK+"()", row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.SYSTS)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.SYSTSPK, row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.SYSUPDATETS)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.SYSUPDATETSPK, row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.SYSUPDATEUSERID)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.SYSUPDATEUSERIDPK, row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.SYSUPDATEUSERCODE)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.SYSUPDATEUSERCODEPK, row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.SYSUPDATEUSERNAME)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.SYSUPDATEUSERNAMEPK, row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.SYSREF)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.SYSREFPK, row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.SYSVERSION)){
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(FunctionUtil.SYSVERSIONPK, row, "cdata");
				this.getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(row, "cdata", false);
			}
			else if(type!=null&&type.equals(FunctionUtil.MARKNAME)){
				if(cdata!=null&&cdata.trim().length()>0){
					String escs[]=IContrastUtil.ESCS.split(",");
					if(escs!=null&&escs.length>0){
						for(int i=0;i<escs.length;i++){
							if(cdata.equals(escs[i])){
								this.getBillCardWrapper().getBillCardPanel().getBillModel().setValueAt(new UFBoolean(true), row, "vdef1");
							}
						}
					}
				}
			}
		}
		
	}
	


}
