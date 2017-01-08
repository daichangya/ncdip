package nc.ui.dip.procondition;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.bd.pub.AbstractBdBillCardUI;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.CardEventHandler;
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
 @SuppressWarnings("serial")
public class ProconditionClientUI extends AbstractBdBillCardUI{
	 
	 HashMap tableMap = null;
	 public String returnSql = "";
	 
	 IUAPQueryBS query = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
	 
	public ProconditionClientUI() {

	}
	
	
	 public ProconditionClientUI(HashMap  map){
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
		return new ProconditionClientUICtrl();
	}


	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub
		
		String memorytable = "";
		String procecond = "";
		String procetype = "";
		if(tableMap!=null && tableMap.size()>0){
			memorytable = tableMap.get("firsttab").toString();
			procecond = tableMap.get("procecond").toString();
			procetype = tableMap.get("procetype").toString();
			
		}
		//,数据汇总,数据清洗,数据卸载,数据预置,数据转换
		if("数据汇总".equals(procetype)){
			sjjg(memorytable,procecond,procetype);
		}
		if("数据清洗".equals(procetype)){
			sjqx(memorytable,procecond,procetype);
		}
		if("数据卸载".equals(procetype)){
			sjxz(memorytable,procecond,procetype);
		}
		if("数据预置".equals(procetype)){
			sjyz(memorytable,procecond,procetype);
		}
		
	}
	/**
	 * 
	 * 数据汇总
	 * **/
	public void sjjg(String memorytable,String procecond,String procetype) throws Exception{
		String sql = "select cname,ename from dip_datadefinit_b b left join dip_datadefinit_h h on h.pk_datadefinit_h = b.pk_datadefinit_h " +
		" where h.pk_datadefinit_h = '"+memorytable+"' and nvl(b.dr,0) = 0 and nvl(h.dr,0) = 0 ";
		ArrayList list = (ArrayList)query.executeQuery(sql, new MapListProcessor());
//		getBillCardPanel().getBodyItem("def2").setShow(false);
//		getBillCardPanel().getBodyItem("def3").setShow(false);
		getBillCardPanel().getBodyPanel().hideTableCol("def2");//隐藏
		getBillCardPanel().getBodyPanel().hideTableCol("def3");//隐藏
		getBillCardPanel().getBodyPanel().hideTableCol("def4");//隐藏
		getBillCardPanel().getBodyPanel().hideTableCol("def5");//隐藏
		getBillCardPanel().getBodyPanel().hideTableCol("def6");//隐藏
		if(list!=null && list.size()>0){
			getBillCardPanel().getBillModel().clearBodyData();	//清除数据
			for (int row = 0; row < list.size(); row++) {
				
				HashMap map = (HashMap) list.get(row);
				
				getBillCardPanel().getBillModel().addLine();
				getBillCardPanel().getBillModel().setValueAt(map.get("ename"),row, "field");
				getBillCardPanel().getBillModel().setValueAt(map.get("cname"),row, "cname");
				
				String sumname = "sum("+map.get("ename").toString()+")";
				String ename = map.get("ename").toString();
				if(!"".equals(procecond)){
					
					if(procecond.indexOf(ename)>0){
						getBillCardPanel().getBillModel().setValueAt(UFBoolean.TRUE,row, "def1");
					}else{
						getBillCardPanel().getBillModel().setValueAt(UFBoolean.FALSE,row, "def1");
					}
					
					if(procecond.indexOf(sumname)>0 ){
						getBillCardPanel().getBillModel().setValueAt(UFBoolean.TRUE,row, "issum");
					}else{
						getBillCardPanel().getBillModel().setValueAt(UFBoolean.FALSE,row, "issum");
						
					}
					/*if(procecond.indexOf(sumname)<0 && procecond.indexOf(ename)>0 ){
						getBillCardPanel().getBillModel().setValueAt(UFBoolean.TRUE,row, "issum");
					}*/
					
					
				}
				getBillCardPanel().getBillModel().setCellEditable(row, "issum", true);
				getBillCardPanel().getBillModel().setCellEditable(row, "def1", true);
				
		}
//			getBillCardPanel().setBillData(getBillCardPanel().getBillData());
			
//			this.setBillOperate(2);
	
		}
	}
	
	/**
	 * 
	 * 数据清洗
	 * **/
	public void sjqx(String memorytable,String procecond,String procetype) throws Exception{
		String sql = "select cname,ename from dip_datadefinit_b b left join dip_datadefinit_h h on h.pk_datadefinit_h = b.pk_datadefinit_h " +
		" where h.pk_datadefinit_h = '"+memorytable+"' and nvl(b.dr,0) = 0 and nvl(h.dr,0) = 0 ";
		ArrayList list = (ArrayList)query.executeQuery(sql, new MapListProcessor());

		getBillCardPanel().getBodyPanel().hideTableCol("issum");//隐藏
		getBillCardPanel().getBodyPanel().hideTableCol("def1");//隐藏
		getBillCardPanel().getBodyPanel().hideTableCol("def4");//隐藏
		getBillCardPanel().getBodyPanel().hideTableCol("def5");//隐藏
		getBillCardPanel().getBodyPanel().hideTableCol("def6");//隐藏
		if(list!=null && list.size()>0){
			getBillCardPanel().getBillModel().clearBodyData();	//清除数据
			for (int row = 0; row < list.size(); row++) {
				
				HashMap map = (HashMap) list.get(row);
				
				getBillCardPanel().getBillModel().addLine();
				getBillCardPanel().getBillModel().setValueAt(map.get("ename"),row, "field");
				getBillCardPanel().getBillModel().setValueAt(map.get("cname"),row, "cname");
				
				
				getBillCardPanel().getBillModel().setCellEditable(row, "def2", true);
				getBillCardPanel().getBillModel().setCellEditable(row, "def3", true);
				
		}
//			this.setBillOperate(2);
	
		}
	}
	/**
	 * 
	 * 数据卸载
	 * **/
	public void sjxz(String memorytable,String procecond,String procetype) throws Exception{
		
		getBillCardPanel().getBodyPanel().hideTableCol("field");//隐藏
		getBillCardPanel().getBodyPanel().hideTableCol("cname");//隐藏
		getBillCardPanel().getBodyPanel().hideTableCol("issum");//隐藏
		getBillCardPanel().getBodyPanel().hideTableCol("def1");//隐藏
		getBillCardPanel().getBodyPanel().hideTableCol("def2");//隐藏
		getBillCardPanel().getBodyPanel().hideTableCol("def3");//隐藏
		
			getBillCardPanel().getBillModel().clearBodyData();	//清除数据
			
			String sql = "select xingshi,tabname from dip_datacanzhao where nvl(dr,0)=0  ";
			ArrayList list = (ArrayList)query.executeQuery(sql, new MapListProcessor());
			
			
			for (int row = 0; row < list.size(); row++) {
				
				HashMap map = (HashMap) list.get(row);
				
				getBillCardPanel().getBillModel().addLine();
			
				getBillCardPanel().getBillModel().setValueAt(map.get("xingshi"),row, "def4");
				getBillCardPanel().getBillModel().setValueAt(map.get("tabname"),row, "def5");
			
				
				getBillCardPanel().getBillModel().setCellEditable(row, "def6", true);
				
		
//			this.setBillOperate(2);
	
		}
	}
	/**
	 * 
	 * 数据预置
	 * **/
	public void sjyz(String memorytable,String procecond,String procetype) throws Exception{
		
		String sql = "select cname,ename from dip_datadefinit_b b left join dip_datadefinit_h h on h.pk_datadefinit_h = b.pk_datadefinit_h " +
		" where h.pk_datadefinit_h = '"+memorytable+"' and nvl(b.dr,0) = 0 and nvl(h.dr,0) = 0 ";
		ArrayList list = (ArrayList)query.executeQuery(sql, new MapListProcessor());

		getBillCardPanel().getBodyPanel().hideTableCol("issum");//隐藏
		getBillCardPanel().getBodyPanel().hideTableCol("def1");//隐藏
		getBillCardPanel().getBodyPanel().hideTableCol("def4");//隐藏
		getBillCardPanel().getBodyPanel().hideTableCol("def5");//隐藏
		getBillCardPanel().getBodyPanel().hideTableCol("def6");//隐藏
		getBillCardPanel().getBodyPanel().hideTableCol("def2");//隐藏//
		if(list!=null && list.size()>0){
			getBillCardPanel().getBillModel().clearBodyData();	//清除数据
			for (int row = 0; row < list.size(); row++) {
				
				HashMap map = (HashMap) list.get(row);
				
				getBillCardPanel().getBillModel().addLine();
				getBillCardPanel().getBillModel().setValueAt(map.get("ename"),row, "field");
				getBillCardPanel().getBillModel().setValueAt(map.get("cname"),row, "cname");
				
				
//				getBillCardPanel().getBillModel().setCellEditable(row, "def2", true);
				getBillCardPanel().getBillModel().setCellEditable(row, "def3", true);
				
		}
//			this.setBillOperate(2);
	
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
