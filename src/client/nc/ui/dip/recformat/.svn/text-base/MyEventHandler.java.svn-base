package nc.ui.dip.recformat;

import java.util.Collection;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.pub.bill.BillData;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.util.dip.sj.IContrastUtil;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.recformat.DipRecformatVO;
import nc.vo.pub.AggregatedValueObject;

/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		BillData bd=getBillCardPanelWrapper().getBillCardPanel().getBillData();
		if(bd !=null){
			bd.dataNotNullValidate();
		}
		//表头重复校验
		RecFormatClientUI ui=(RecFormatClientUI)getBillUI();
		String pk=(String)ui.getBillCardPanel().getHeadItem("pk_recformat").getValueObject();
		if(pk==null || pk.trim().equals("")){
			pk=" ";
		}
		String code=(String)ui.getBillCardPanel().getHeadItem("code").getValueObject();
		String name=(String)ui.getBillCardPanel().getHeadItem("name").getValueObject();

		IUAPQueryBS bs=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());

		Collection ccode=bs.retrieveByClause(DipRecformatVO.class, "code='"+code+"' and nvl(dr,0)=0 and pk_recformat <>'"+pk+"'");
		if(ccode !=null){
			if(ccode.size()>=1){
				ui.showWarningMessage("该【"+code+"】编码已经存在！");
				return;
			}
		}
//		Collection cname=bs.retrieveByClause(DipRecformatVO.class, "name='"+name+"' and nvl(dr,0)=0 and pk_recformat <>'"+pk+"'");
//		if(cname !=null){
//			if(cname.size()>=1){
//				ui.showWarningMessage("该【"+name+"】名称已经存在！");
//				return;
//			}
//		}
		super.onBoSave();
	}

	/*删除判断，如果被引用不可删除
	 * 2011-5-28
	 * zlc*/
	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
			Map<String,String> map=IContrastUtil.getDocRefMap();
			String key="dip_recformat";
			String value=map.get(key);
			AggregatedValueObject billvo=getBufferData().getCurrentVO();
			if(billvo!=null){
				String pk=billvo.getParentVO().getPrimaryKey();
				String isref=SJUtil.isExitRef(value,pk);
				if(isref!=null&&isref.length()>0){
					getBillUI().showErrorMessage("此条数据被引用，不可删除");
					return ;
				}
			}
		super.onBoDelete();
	}
	
		
}