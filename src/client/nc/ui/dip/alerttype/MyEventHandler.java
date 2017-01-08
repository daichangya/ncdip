package nc.ui.dip.alerttype;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;

/**
 *
 *该类是AlertTypeMyEventHandler抽象类的实现类，
 *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 *@author author
 *@version tempProject version
 */

public class MyEventHandler 
extends AbstractMyEventHandler{

	BillManageUI billUI;

	IControllerBase control;
	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);	
		this.billUI=billUI;
		this.control=control;
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		super.onBoSave();		
	}
}