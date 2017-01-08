package nc.ui.dip.datachange;

import nc.bs.framework.common.NCLocator;
import nc.itf.dip.pub.IQueryField;
import nc.ui.dip.buttons.IBtnDefine;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.treecard.BillTreeCardUI;
import nc.ui.trade.treecard.TreeCardEventHandler;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.datachange.DipDatachangeBVO;
import nc.vo.dip.datachange.MyBillVO;
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
	    case IBtnDefine.Stop:
	    	onBoStop();
	    	break;
	    case IBtnDefine.Start:
	    	onBoStart();
	    	break;
	  /*  case IBtnDefine.DataValidate:
	    	onBoDataValidate();
	    	break;*/
//	    case IBtnDefine.YuJing:
//	    	onBoWarning();
//	    	break;
//	    case IBtnDefine.Model:
//	    	onBoModel();
//	    	break;
	    case IBtnDefine.Conversion:
	    	onBoConversion();
	    	break;
	    case IBtnDefine.addFolderBtn:
			onBoAddFolder();
			break;
		case IBtnDefine.editFolderBtn:
			onBoEditFolder();
			break;
		case IBtnDefine.delFolderBtn:
			onBoDeleteFolder();
	    	break;
	    }	     	
	}
	
	public void onBoEditFolder() throws Exception  {
		// TODO Auto-generated method stub
		
	}



	public void onBoDeleteFolder() throws Exception {
		// TODO Auto-generated method stub
		
	}



	public void onBoAddFolder() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void onBoWarning() throws Exception{
		
	}
	
	public void onBoStop() throws Exception{
		MyBillVO billvo=(MyBillVO) getBufferData().getCurrentVO();
		DataChangeClientUI ui=(DataChangeClientUI)getBillUI();
		int row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		if(row<0){
			ui.showErrorMessage("请选择需要操作的站点！");
			return ;
		}
		DipDatachangeBVO[] vos=(DipDatachangeBVO[]) getBillTreeCardUI().getVOFromUI().getChildrenVO();
		UFBoolean bool=vos[row].getDisable();
		if(!SJUtil.isNull(bool)&&bool.booleanValue()){
			ui.showWarningMessage("该系统已经停用");

			return;
		}
		vos[row].setDisable(new UFBoolean(true));
		HYPubBO_Client.update(vos[row]);
		getSelfUI().getBillCardPanel().getBillModel().setBodyRowVO(
				vos[row],
				row);
		

		billvo.setChildrenVO(vos);
		getBufferData().addVOToBuffer(billvo);
		getBufferData().setCurrentVO(billvo);
		getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(row,getBillCardPanelWrapper().getBillCardPanel().getBodyItem("nczz").getLoadFormula());
		
		/*
		 * 多选nc组织编码（nczz）存值问题
		 * 2011-6-28 
		 * 418--440行
		 */
		String orgName=null;//多个名称连接的字符串
		String[] arrayName=null;//名称数组
		String sql="";
		String unitcode=null;
		String nczz=null;
		if(vos !=null){
			for(int j=0;j<vos.length;j++){
				orgName=vos[j].getOrgname();
				arrayName=orgName.split(",");
				for(int k=0;k<arrayName.length;k++){
					if(k==0){
						nczz=arrayName[k];
						sql="select unitcode from bd_corp where unitname='"+nczz+"' and nvl(dr,0)=0";
						unitcode=iq.queryfield(sql);
					}else{
						nczz=arrayName[k];
						sql="select unitcode from bd_corp where unitname='"+nczz+"' and nvl(dr,0)=0";
						unitcode=unitcode+","+iq.queryfield(sql);
					}
				}
				this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(unitcode, j, "nczz");
			}
		}
	}
	
	IQueryField iq=(IQueryField) NCLocator.getInstance().lookup(IQueryField.class.getName());
	public void onBoStart() throws Exception{
		MyBillVO billvo=(MyBillVO) getBufferData().getCurrentVO();
		DataChangeClientUI ui=(DataChangeClientUI)getBillUI();
		int row=getSelfUI().getBillCardPanel().getBillTable().getSelectedRow();
		if(row<0){
			ui.showErrorMessage("请选择需要操作的站点！");
			return ;
		}
		DipDatachangeBVO[] vos=(DipDatachangeBVO[]) getBillTreeCardUI().getVOFromUI().getChildrenVO();
		UFBoolean bool=vos[row].getDisable();
		if(SJUtil.isNull(bool)||!bool.booleanValue()){
			ui.showWarningMessage("该系统已经启用");

			return;
		}
		vos[row].setDisable(new UFBoolean(false));
		HYPubBO_Client.update(vos[row]);
		getSelfUI().getBillCardPanel().getBillModel().setBodyRowVO(
				vos[row],
				row);
		

		billvo.setChildrenVO(vos);
		getBufferData().addVOToBuffer(billvo);
		getBufferData().setCurrentVO(billvo);
		getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(row, getBillCardPanelWrapper().getBillCardPanel().getBodyItem("nczz").getLoadFormula());
		
		/*
		 * 多选nc组织编码（nczz）存值问题
		 * 2011-6-28 
		 * 418--440行
		 */
		String orgName=null;//多个名称连接的字符串
		String[] arrayName=null;//名称数组
		String sql="";
		String unitcode=null;
		String nczz=null;
		if(vos !=null){
			for(int j=0;j<vos.length;j++){
				orgName=vos[j].getOrgname();
				arrayName=orgName.split(",");
				for(int k=0;k<arrayName.length;k++){
					if(k==0){
						nczz=arrayName[k];
						sql="select unitcode from bd_corp where unitname='"+nczz+"' and nvl(dr,0)=0";
						unitcode=iq.queryfield(sql);
					}else{
						nczz=arrayName[k];
						sql="select unitcode from bd_corp where unitname='"+nczz+"' and nvl(dr,0)=0";
						unitcode=unitcode+","+iq.queryfield(sql);
					}
				}
				this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(unitcode, j, "nczz");
			}
		}
	}
	
//	public void onBoDataValidate() throws Exception{}
	
//	private void onBoModel() throws Exception{
//		ToftPanel tp=SFClientUtil.showNode("H4H4H1", IFuncWindow.WINDOW_TYPE_DLG);
//		CredenceClientUI ui=(CredenceClientUI) tp;
//	}
	
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