package nc.ui.dip.dataconsult;

import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.list.BillListUI;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.pub.AggregatedValueObject;

public class MyEventHandler extends AbstractMyEventHandler {
	public MyEventHandler(BillListUI billUI, IListController control){
		super(billUI,control);		
	}
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd !=null){
			bd.dataNotNullValidate();
		}
		DataconsultClientUI ui=(DataconsultClientUI)getBillUI();
//		String pk=(String)ui.getBillCardPanel().getHeadItem("pk_dataconsult").getValueObject();
//		if(pk==null || pk.trim().equals("")){
//			pk=" ";
//		}
//		String code=(String)ui.getBillCardPanel().getHeadItem("code").getValueObject();
//		String name=(String)ui.getBillCardPanel().getHeadItem("name").getValueObject();
		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());

//		Collection ccode=bs.retrieveByClause(DataconsultVO.class, "code='"+code+"' and nvl(dr,0)=0 and pk_dataconsult <>'"+pk+"'");
//		if(ccode !=null){
//			if(ccode.size()>=1){
//				ui.showWarningMessage("该【"+code+"】编码已经存在！");
//				return;
//			}
//		}
//		Collection cname=bs.retrieveByClause(DataconsultVO.class, "name='"+name+"' and nvl(dr,0)=0 and pk_dataconsult<>'"+pk+"'");
//		if(cname !=null){
//			if(cname.size()>=1){
//				ui.showWarningMessage("该【"+name+"】名称已经存在！");
//				return;
//			}
//		}
		super.onBoSave();
	}
	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		Map<String,String> map=IContrastUtil.getDocRefMap();
		String key="dataconsult";
		String value=map.get(key);
		AggregatedValueObject billvo=getBufferData().getCurrentVO();
		if(!SJUtil.isNull(billvo)){
			String pk=billvo.getParentVO().getPrimaryKey();
			String isref=SJUtil.isExitRef(value, pk);
			if(isref!=null&&isref.length()>0){
				getBillUI().showErrorMessage("此条数据被引用，不可删除！");
				return;
			}
		}
		super.onBoDelete();
	}
	

}
