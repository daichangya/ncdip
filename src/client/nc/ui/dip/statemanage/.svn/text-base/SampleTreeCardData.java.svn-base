package nc.ui.dip.statemanage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.pub.IVOTreeDataByID;
import nc.vo.dip.datadefinit.ViewDipDatadefinitVO;
import nc.vo.dip.dataproce.DipDataproceHVO;
import nc.vo.dip.statemanage.DipStateManageHVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

public class SampleTreeCardData implements IVOTreeDataByID  {
	public String getIDFieldName() {
		return "pk_statemanage_h";
	}

	public String getParentIDFieldName() {
		return "fpk";
//		return "pk_sysregister_h";
	}

	public String getShowFieldName() {
		return "code+name";
	}

	public SuperVO[] getTreeVO() {
		DipStateManageHVO[] ys=null;
		BusinessDelegator business = new BDBusinessDelegator();
		

		SuperVO[] rts = null;
		SuperVO[] ret = null;
		
			try {
				//从视图中取到根节点rts
				rts=business.queryByCondition(ViewDipDatadefinitVO.class, " 0=0 order by syscode");
				//从主表中取到所有的叶子
//				String where="(dip_dataproce_h.fpk in  (select dip_sysregister_h.pk_sysregister_h"
//          +" from dip_sysregister_h"
//          +" where nvl(dip_sysregister_h.dr, 0) = 0) and nvl(dip_dataproce_h.dr,0)=0)or (dip_dataproce_h.fpk in"
//          +" (select dip_sysregister_h.pk_sysregister_h"
//          +" from dip_sysregister_h"
//          +" where nvl(dip_sysregister_h.dr, 0) = 0) and nvl(dip_dataproce_h.dr,0)=0)";
//				ys=(DipDataproceHVO[]) business.queryByCondition(DipDataproceHVO.class,where);
				ys=(DipStateManageHVO[]) business.queryByCondition(DipStateManageHVO.class," 0=0  and nvl(dr,0)=0  order by code  ");
				//根节点的个数
				int lroots=rts!=null&&rts.length>0?rts.length:0;
				//子节点叶子的个数
				int lys=ys!=null&&ys.length>0?ys.length:0;
				//最后返回的数据对应的list
				List<DipStateManageHVO> retlist=new ArrayList<DipStateManageHVO>();
				//如果根节点的个数大于0，那么把试图vo转换成主表vo，放到返回的list中去
				if(lroots>0){
					for(int i=0;i<lroots;i++){
						ViewDipDatadefinitVO root=(ViewDipDatadefinitVO) rts[i];
						DipStateManageHVO ii=new DipStateManageHVO();
						ii.setPk_datadefinit_h(root.getFpk());
						ii.setFpk(root.getFpk());
						ii.setPk_statemanage_h(root.getPk());
						ii.setCode(root.getSyscode());
						ii.setName(root.getSysname());
//						ii.setFirsttab(root.getMemorytable());
						ii.setTs(root.getTs()==null?null:root.getTs().toString());
						ii.setPk_xt(root.getPk());
						ii.setIsfolder(new UFBoolean(true));
						retlist.add(ii);
					}
				}
				//如果子节点的个数大于0，那么把所有的叶子放到返回的list中去
				if(lys>0){
					List<DipStateManageHVO> list=Arrays.asList(ys);
					for(int i=0;i<list.size();i++){
						if(list.get(i).getIsfolder()==null){
							list.get(i).setIsfolder(new UFBoolean(false));
						}	
						if(list.get(i).getPk_xt()==null){
							list.get(i).setPk_xt(list.get(i).getFpk());
						}
					}
					
					retlist.addAll(list);
				
				}
				//把返回的retlist转换成数组
				ret=retlist.toArray(new SuperVO[lys+lroots]);
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return ret;
	
	}
}
