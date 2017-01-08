package nc.ui.dip.datacheckyytj;

import java.util.HashMap;

import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.dip.datarec.DatarecDlg;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.list.BillListUI;
import nc.ui.trade.pub.VOTreeNode;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.datachange.DipDatachangeHVO;
import nc.vo.dip.datacheckyytj.DataCheckYYTJVO;
import nc.vo.dip.datacheckyytj.MyBillVO;
import nc.vo.pub.lang.UFBoolean;

/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		// TODO Auto-generated method stub
		super.onBoElse(intBtn);
		switch (intBtn) {
		case IBtnDefine.DataValidate:
			dataCheck ();
			break;

		default:
			break;
		}
	}

	public void dataCheck () {
		MyBillVO billvo=(MyBillVO) getBufferData().getCurrentVO();
		if(billvo==null){
			getSelfUI().showErrorMessage("请选择！");
			return;
		}
		DataCheckYYTJVO hvo=(DataCheckYYTJVO) billvo.getParentVO();
		HashMap map = new HashMap();
		map.put("pk", hvo.getPkbus());
		map.put("type", hvo.getRwlx());
		/*将当前页面的表头code和name放到map中，用于当弹出数据校验窗口时，编码和名称为空，自动填充
		 * 2011-06-14
		 * zlc*/
		map.put("code", hvo.getBuscode());
		map.put("name", hvo.getBusname());
		map.put("fpk", hvo.getPksys());
		DatarecDlg dlg = new DatarecDlg(getSelfUI(),new UFBoolean(true),map,false);
		dlg.show();
	}
	public MyEventHandler(BillListUI billUI, IListController control) {
		super(billUI, control);
		// TODO Auto-generated constructor stub
	}


//	@Override
//	protected void onBoQuery() throws Exception {
////		// TODO Auto-generated method stub
//	}

	
	private DataCheckYYTJClientUI getSelfUI() {
		// TODO Auto-generated method stub
		return (DataCheckYYTJClientUI) getBillUI();
	}

	
	
	
	
	
}