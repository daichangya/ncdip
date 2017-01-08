package nc.ui.dip.datarec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.pub.IVOTreeDataByID;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.datadefinit.ViewDipDatadefinitVO;
import nc.vo.dip.datarec.DipDatarecHVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
/**
 *版权信息：商佳科技
 *作者：   程莉
 *版本：   
 *描述：   "数据接收定义"左树右表结构:左边树结构与"数据定义"树结构根节点一致
 *创建日期：2011-04-21
 * @throws Exception 
 */
public class SampleTreeCardData implements IVOTreeDataByID  {
	public String getIDFieldName() {
		return "pk_datarec_h";
	}

	public String getParentIDFieldName() {
		return "fpk";
	}

	public String getShowFieldName() {
		return "syscode+recname";
	}

	public SuperVO[] getTreeVO() {

		DipDatarecHVO[] ys = null;
		BusinessDelegator business = new BDBusinessDelegator();
		SuperVO[] rts=null;
		SuperVO[] ret=null;
		try {
			//从视图中取到根节点rts
			rts=business.queryByCondition(ViewDipDatadefinitVO.class, "0=0 order by syscode");
			//从主表中取到所有的叶子
			ys = (DipDatarecHVO[]) business.queryByCondition(DipDatarecHVO.class, "0=0 order by syscode");
			
			//根节点的个数
			int lroots=rts!=null&&rts.length>0?rts.length:0;
			//叶子的个数
			int lys=ys!=null&&ys.length>0?ys.length:0;
			//最后返回的数组对应的list
			List<DipDatarecHVO> retlist=new ArrayList<DipDatarecHVO>();
			//如果根节点的个数大于0，那么把试图vo转换成主表vo，放到返回的list中去
			if(lroots>0){
				//DipDatadefinitHVO[] roots=new DipDatadefinitHVO[lroots];
				for(int i=0;i<lroots;i++){
					ViewDipDatadefinitVO root=(ViewDipDatadefinitVO) rts[i];
					DipDatarecHVO ii=new  DipDatarecHVO();
					ii.setIsdeploy(root.getIsdeploy());
					ii.setIsfolder(new UFBoolean(true));
					ii.setPk_xt(root.getPrimaryKey());
					ii.setFpk("");
					ii.setPk_datarec_h(root.getPk());
					ii.setSyscode(root.getSyscode());
					ii.setRecname(root.getSysname());
					ii.setTs(root.getTs()==null?null:root.getTs().toString());
					retlist.add(ii);
				}
			}
			//如果叶子的个数大于0，那么把所有的叶子放到返回的list中去
			if(lys>0){
				List<DipDatarecHVO> ss=Arrays.asList(ys);
				if(ss!=null&&ss.size()>0){
					for(int i=0;i<ss.size();i++){
						if(ss.get(i).getIsfolder()==null){
							ss.get(i).setIsfolder(new UFBoolean(false));
						}
						if(ss.get(i).getPk_xt()==null||ss.get(i).getPk_xt().length()<=0){
							ss.get(i).setPk_xt(ss.get(i).getFpk());
						}
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
