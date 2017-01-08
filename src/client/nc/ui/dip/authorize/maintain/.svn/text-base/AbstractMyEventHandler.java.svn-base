package nc.ui.dip.authorize.maintain;

import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.treemanage.BillTreeManageUI;
import nc.ui.trade.treemanage.TreeManageEventHandler;

/**
 *
 *该类是一个抽象类，主要目的是生成按钮事件处理的框架
 *@author author
 *@version tempProject version
 */

public class AbstractMyEventHandler 
extends TreeManageEventHandler  {

	public AbstractMyEventHandler(BillTreeManageUI  billUI, ICardController control){
		super(billUI,control);		
	}

	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {
		case IBtnDefine.CONTSYSQUERY:
			onContsysQuery(intBtn);
			break;
		case IBtnDefine.BCONTSYSQUERY:
			onBContsysQuery(intBtn);
			break;
		case IBtnDefine.CONTSAVE:
			onContSave(intBtn);
			break;
		case IBillButton.ExportBill:
			onExportBill(intBtn);
			break;
		case IBtnDefine.DATACLEAR:
			onDataClear(intBtn);
			break;
		case IBtnDefine.contresut:
			contResut(intBtn);
			break;
		case IBtnDefine.edit:
			edit(intBtn);
			break;
		//2011-6-10 校验检查
		case IBtnDefine.VALIDATECHECK:
			
			onDataCheck();
			break;
		case 
		IBtnDefine.SET:
			onBoSet();
			break;
		case
		IBtnDefine.DATACHECK:
			validateCheck(intBtn);
			break;
		case
		IBtnDefine.autoContData:
			onAutoContData();
			break;	
		case
		IBtnDefine.FAST_SET:
			onBoFastSet();
			break;		
		}
			
		
	}
	
	
		
	protected void onBoSet() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @desc 数据清理
	 * */
	protected void onDataClear(int intBtn)throws Exception{}
	/**
	 * 快速授权
	 * @throws Exception
	 */
	protected void onBoFastSet() throws Exception {}
	/**
	 * @desc 对照系统查询定义
	 * @author fjf
	 * */
	protected void onContsysQuery(int intBtn) throws Exception{};
	/**
	 * @desc 被对照系统查询定义
	 * @author fjf
	 * */
	protected void onBContsysQuery(int intBtn) throws Exception{};
	/**
	 * @desc 保存
	 * @author fjf
	 * */
	protected void onContSave(int intBtn) throws Exception{};
	/**
	 * @desc 导出
	 * */
	protected void onExportBill(int intBtn) throws Exception{};
	/**
	 * 对照结果
	 * */
	protected void contResut(int intBtn) throws Exception{};
	/**
	 * 修改
	 * */
	protected void edit(int intBtn) throws Exception{};
	
	/**
	 * @desc 校验检查
	 * @author cl
	 * @date 2011-6-10
	 */
	protected void validateCheck(int intBtn) throws Exception{};

	
	/**
	 * 数据校验检查
	 * @param intBtn
	 * @throws Exception
	 * @author lyz
	 */
	protected void onDataCheck() throws Exception{};
	
	/**
	 * 自动对照
	 * lyz
	 * @throws Exception
	 */
	protected void onAutoContData() throws Exception{};
	
}