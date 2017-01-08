package nc.ui.dip.tyzhq;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.trade.treecard.TreeCardEventHandler;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.tyzhq.DipTYZHDatachangeBVO;
import nc.vo.dip.tyzhq.MyBillVO;
import nc.vo.pub.lang.UFBoolean;



/**
  *
  *该类是一个抽象类，主要目的是生成按钮事件处理的框架
  *@author author
  *@version tempProject version
  */
  
  public class AbstractMyEventHandler 
                                          extends TreeCardEventHandler{

        public AbstractMyEventHandler(BillTreeCardUI billUI, ICardController control){
		super(billUI,control);		
	}
	/*
	 * 添加onBoElse及其中定义的方法
	 * 各个方法对应页面上的按钮事件
	 * 2011-05-08
	 * */
	protected void onBoElse(int intBtn) throws Exception {
	    switch(intBtn){
	    case IBtnDefine.Conversion:
	    	onBoConversion();
	    	break;
	    }	     	
	}
	
	
	
	IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	
	
	public void onBoConversion() throws Exception{}
	
	
	/**
	 * 取得当前UI类
	 * 
	 * @return
	 */
	private DataChangeClientUI getSelfUI() {
		return (DataChangeClientUI) getBillUI();
	}
		 
	public void changeStartStat(boolean arg){

		getSelfUI().getButtonManager().getButton( IBtnDefine.Start).setEnabled(arg);
		getSelfUI().getButtonManager().getButton( IBtnDefine.Stop).setEnabled(arg);
		
	}
	
}