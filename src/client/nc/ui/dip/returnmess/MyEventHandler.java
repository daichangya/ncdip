package nc.ui.dip.returnmess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.buffer.BillUIBuffer;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.list.BillListUI;
import nc.ui.trade.manage.BillManageUI;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.returnmess.DipReturnmessBVO;
import nc.vo.dip.returnmess.DipReturnmessHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;

/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillListUI billUI, IListController control){
		super(billUI,control);		
	}
	boolean isedit=false;
	BillUIBuffer buf=null;
	@Override
	protected void onBoEdit() throws Exception {
		isedit=true;
		try{
			 AggregatedValueObject billVO = getBillUI().getChangedVOFromUI();
				setTSFormBufferToVO(billVO);
				AggregatedValueObject checkVO = getBillUI().getVOFromUI();
				setTSFormBufferToVO(checkVO);
			}catch(Exception e){
				getBillUI().showErrorMessage("请选择要修改的定义！");
				return;
				
			}
//
//			CircularlyAccessibleValueObject[] cur=null;
//		
//			if(getBufferData()==null||getBufferData().getAllHeadVOsFromBuffer().length<=0){
//				cur=new CircularlyAccessibleValueObject[1];
//				cur[0]=new DipReturnmessHVO();
//			}else{
//				int len=getBufferData().getAllHeadVOsFromBuffer().length;
//				cur=new CircularlyAccessibleValueObject[len];
//				for(int i=0;i<len;i++){
//					cur[i]=getBufferData().getAllHeadVOsFromBuffer()[i];
//				}
//			}
//			getBillUI().setListHeadData(cur);
////			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
//			getBufferData().setCurrentRow(getBufferData().getVOBufferSize()-1);
			
			getBillListPanelWrapper().getBillListPanel().getHeadItem("code").setEnabled(true);
			getBillListPanelWrapper().getBillListPanel().getHeadItem("name").setEnabled(true);
			getBillListPanelWrapper().getBillListPanel().getHeadItem("propty").setEnabled(true);
			getBillListPanelWrapper().getBillListPanel().setEnabled(true);
			getBillUI().setBillOperate(IBillOperate.OP_ADD);
//			onBoLineAdd();
		
		/*buf=getBufferData();
		super.onBoEdit();
		onBoLineAdd();*/
//		getBillListPanelWrapper().getBillListPanel().getHeadBillModel().
	}

	@Override
	protected void onBoSave() throws Exception {
		
		try{
		 AggregatedValueObject billVO = getBillUI().getChangedVOFromUI();
			setTSFormBufferToVO(billVO);
			AggregatedValueObject checkVO = getBillUI().getVOFromUI();
			setTSFormBufferToVO(checkVO);
		}catch(Exception e){
			getBillUI().showErrorMessage("请选择要保存的定义！");
			return;
			
		}
		int row=getBufferData().getCurrentRow();
		if(row<0){
			getBillUI().showErrorMessage("请选择要保存的定义！");
			return;
		}
		int rowcount=getBillListPanelWrapper().getBillListPanel().getBodyTable().getRowCount();
		List<Integer> del=new ArrayList<Integer>();
		for(int i=0;i<rowcount;i++){
			DipReturnmessBVO bvoi=(DipReturnmessBVO) getBillListPanelWrapper().getBillListPanel().getBodyBillModel().getBodyValueRowVO(i, DipReturnmessBVO.class.getName());
			if(bvoi.getCode()==null||bvoi.getCode().length()<=0){
				del.add(i);
			}
		}
		if(del!=null&&del.size()>0);
		int[] dell=new int[del.size()];
		for(int i=0;i<del.size();i++){
			dell[i]=del.get(i);
		}
		 getBillListPanelWrapper().getBillListPanel().getBodyBillModel().delLine(dell);
		
		super.onBoSave();
		if(isedit){
			CircularlyAccessibleValueObject[] cur=null;
			if(getBufferData()==null||getBufferData().getAllHeadVOsFromBuffer().length<=0){
			}else{
				cur=getBufferData().getAllHeadVOsFromBuffer();
				int rowi=getBufferData().getCurrentRow();
				cur[row]=cur[rowi];
				CircularlyAccessibleValueObject[] after=new CircularlyAccessibleValueObject[cur.length-1];
				for(int i=0;i<after.length;i++){
					after[i]=cur[i];
				}

				getBillUI().setListHeadData(after);
				getBufferData().setCurrentRow(row);
				
			}
		}
	}

	/*删除判断，如果被引用不可删除
	 * 2011-5-28
	 * zlc*/
	@Override
	protected void onBoDelete() throws Exception {
			/*Map<String,String> map=IContrastUtil.getDocRefMap();
			String key="dip_returnmess_h";
			String value=map.get(key);
			AggregatedValueObject billvo=getBufferData().getCurrentVO();
			if(!SJUtil.isNull(billvo)){
				String pk=billvo.getParentVO().getPrimaryKey();
				String isref=SJUtil.isExitRef(value,pk);
				if(isref!=null&&isref.length()>0){
					getBillUI().showErrorMessage("此条数据被引用，不可删除！");
					return ;
				}
			}*/
			super.onBoDelete();
			updateBuffer();
	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		isedit=false;
		CircularlyAccessibleValueObject[] cur=null;
	
		if(getBufferData()==null||getBufferData().getAllHeadVOsFromBuffer().length<=0){
			cur=new CircularlyAccessibleValueObject[1];
			cur[0]=new DipReturnmessHVO();
		}else{
			int len=getBufferData().getAllHeadVOsFromBuffer().length+1;
			cur=new CircularlyAccessibleValueObject[len];
			for(int i=0;i<len-1;i++){
				cur[i]=getBufferData().getAllHeadVOsFromBuffer()[i];
			}
			cur[len-1]=new DipReturnmessHVO();
		}
		getBillUI().setListHeadData(cur);
//		getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
		getBufferData().setCurrentRow(getBufferData().getVOBufferSize());
		
		getBillListPanelWrapper().getBillListPanel().getHeadItem("code").setEnabled(true);
		getBillListPanelWrapper().getBillListPanel().getHeadItem("name").setEnabled(true);
		getBillListPanelWrapper().getBillListPanel().getHeadItem("propty").setEnabled(true);
		getBillListPanelWrapper().getBillListPanel().setEnabled(true);
		getBillUI().setBillOperate(IBillOperate.OP_ADD);
//		onBoLineAdd();
	}
	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCancel();
		updateBuffer();
		getBufferData().setCurrentRow(0);
	}

	
}