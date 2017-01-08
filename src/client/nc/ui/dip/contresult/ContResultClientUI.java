package nc.ui.dip.contresult;

import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.bd.pub.AbstractBdBillCardUI;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.CardEventHandler;


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
 @SuppressWarnings("serial")
public class ContResultClientUI extends AbstractBdBillCardUI{
	 
	 HashMap tableMap = null;
	 public String returnSql = "";
	 
	 IUAPQueryBS query = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
	 
	public ContResultClientUI() {

	}
	
	
	 public ContResultClientUI(HashMap  map){
		 tableMap = map;
		 try {
			setDefaultData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		return new ContResultClientUICtrl();
	}


	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub
		String contField = "";
		String bodypk = "";
		String tablename = "";
		String contTablename = "";
		String contpk = "";
		String contsysfieldname = "";
		IQueryField queryfield = (IQueryField)NCLocator.getInstance().lookup(IQueryField.class.getName());
		if(tableMap!=null){
			contField = tableMap.get("contField").toString();
			bodypk = tableMap.get("bodypk").toString();
			tablename = tableMap.get("tablename").toString();
			contTablename = tableMap.get("contTablename").toString();
			contpk = tableMap.get("contpk").toString();
			contsysfieldname = tableMap.get("contsysfieldname").toString();
			String sql = "select "+contField.toString().substring(0, contField.toString().length()-1)+" from " +contTablename+" where "+contpk+" in ("+
					" select contpk from "+tablename+" where extepk='"+bodypk+"' )";
			String contFields [] = contField.split(",");
			String contsysfieldnames [] = contsysfieldname.split(",");
			//设置显示字段
			for(int j = 0;j<contFields.length;j++){
				this.getBillCardPanel().getBodyPanel().showTableCol("vdef"+String.valueOf(j+1));
				this.getBillCardPanel().getBodyItem("vdef"+String.valueOf(j+1)).setName(contsysfieldnames[j]); //设置字段的中文名称
			}
			//设置隐藏字段
			for(int i = contFields.length;i<101;i++){
				this.getBillCardPanel().getBodyPanel().hideTableCol("vdef"+String.valueOf(i+1));
			}
			this.getBillCardPanel().getBillModel().clearBodyData();
			getBillCardPanel().setBillData(getBillCardPanel().getBillData());
			//界面赋值
			List listValue = queryfield.queryListMapSingleSql(sql);
			
			if(listValue!=null && listValue.size()>0){
				for(int i = 0;i<listValue.size();i++){
					this.getBillCardPanel().getBillModel().addLine();
					HashMap mapValue = (HashMap)listValue.get(i);
					for(int j = 0;j<contFields.length;j++){
						Object value=mapValue.get(contFields[j].toUpperCase());
						this.getBillCardPanel().getBillModel().setValueAt(value, i, "vdef"+String.valueOf(j+1)); //为字段赋值
					}
				}
			}
		
		}
		
		
	}
	
	public void bodyRowChange(BillEditEvent arg0) {
		boolean save,cancel;
		save=this.getButtonManager().getButton(nc.ui.trade.button.IBillButton.Save).isEnabled();
		cancel=this.getButtonManager().getButton(nc.ui.trade.button.IBillButton.Edit).isEnabled();
		if((this.getBillCardPanel().getRowCount()>0) & (save==false & cancel==true)){
			try {
				((MyEventHandler)this.getCardEventHandler()).onBoEdit();
			} catch (Exception e) {
				//		 TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	} 
	@Override
	public boolean onClosing() {
		// TODO Auto-generated method stub
//		return super.onClosing();
		
		return true;
	}

}
