package nc.ui.dip.authorize.define;

import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.trade.treecard.TreeCardEventHandler;

/**
  *
  *该类是一个抽象类，主要目的是生成按钮事件处理的框架
  *@author author
  *@version tempProject version
  */
  
  public class AbstractMyEventHandler 
                                          extends TreeCardEventHandler {

        public AbstractMyEventHandler(BillTreeCardUI billUI, ICardController control){
		super(billUI,control);		
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		switch (intBtn) {
		case IBtnDefine.addFolderBtn:
			onBoAddFolder();
			break;
		case IBtnDefine.editFolderBtn:
			onBoEditFolder();
			break;
		case IBtnDefine.delFolderBtn:
			onBoDeleteFolder();
			break;
		case IBtnDefine.moveFolderBtn:
			onBoMoveFolder();
			break;
		case IBtnDefine.SET:
			onBoSet();
			break;
		case IBtnDefine.ACTION_SET:
			onBoActionSet();
			break;
		}
		
		
		     	}
	
	public void onBoEditFolder() throws Exception  {
		// TODO Auto-generated method stub
		
	}
	public void onBoMoveFolder() throws Exception  {
		// TODO Auto-generated method stub
		
	}



	public void onBoDeleteFolder() throws Exception {
		// TODO Auto-generated method stub
		
	}



	public void onBoAddFolder() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void onBoSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void onBoActionSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}