package nc.ui.dip.processflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.pub.IVOTreeDataByID;
import nc.vo.dip.datadefinit.ViewDipDatadefinitVO;
import nc.vo.dip.processflow.DipProcessflowHVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

public class SampleTreeCardData implements IVOTreeDataByID  {
	public String getIDFieldName() {
		return "pk_processflow_h";
	}

	public String getParentIDFieldName() {
		return "fpk";
	}

	public String getShowFieldName() {
		return "code+name";
	}

	public SuperVO[] getTreeVO() {
		
		DipProcessflowHVO[] ys = null;
		BusinessDelegator business = new BDBusinessDelegator();
		SuperVO[] rts=null;
		SuperVO[] ret=null;
		try {
			//从视图中取到根节点rts
			rts=business.queryByCondition(ViewDipDatadefinitVO.class, " 0=0 order by syscode ");
			//从主表中取到所有的叶子
			ys = (DipProcessflowHVO[]) business.queryByCondition(DipProcessflowHVO.class, "  0=0 order by code ");
			
			//根节点的个数
			int lroots=rts!=null&&rts.length>0?rts.length:0;
			//叶子的个数
			int lys=ys!=null&&ys.length>0?ys.length:0;
			//最后返回的数组对应的list
			List<DipProcessflowHVO> retlist=new ArrayList<DipProcessflowHVO>();
			//如果根节点的个数大于0，那么把试图vo转换成主表vo，放到返回的list中去
			if(lroots>0){
				//DipDatadefinitHVO[] roots=new DipDatadefinitHVO[lroots];
				for(int i=0;i<lroots;i++){
					ViewDipDatadefinitVO root=(ViewDipDatadefinitVO) rts[i];
					DipProcessflowHVO ii=new  DipProcessflowHVO();
					ii.setPk_datadefinit_h(root.getFpk());
					
					ii.setPk_processflow_h(root.getPk());
					
					ii.setCode(root.getSyscode());
					ii.setName(root.getSysname());
					ii.setIsfolder(new UFBoolean(true));
					ii.setPk_xt(root.getPk());
					ii.setTs(root.getTs()==null?null:root.getTs().toString());
					retlist.add(ii);
				}
			}
			//如果叶子的个数大于0，那么把所有的叶子放到返回的list中去
			if(lys>0){
				List<DipProcessflowHVO> ss=Arrays.asList(ys);
				for(int i=0;i<ss.size();i++){
					if(ss.get(i).getIsfolder()==null){
						ss.get(i).setIsfolder(new UFBoolean(false));
					}
					if(ss.get(i).getPk_xt()==null){
						ss.get(i).setPk_xt(ss.get(i).getFpk());
					}
				}
				retlist.addAll(ss);
			}
			//把返回的list转换成数组
			ret=(SuperVO[]) retlist.toArray(new SuperVO[lys+lroots]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}


}
