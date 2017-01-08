package nc.ui.dip.datasynch;


import java.util.ArrayList;
import java.util.List;

import java.util.Arrays;

import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.pub.IVOTreeDataByID;
import nc.vo.dip.datadefinit.ViewDipDatadefinitVO;
import nc.vo.dip.datasynch.DipDatasynchVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * 
 * <P>
 * <B>树管理型单表头，数据控制类</B>
 * </P>
 * <P>
 * 功能说明: <BR>
 * 
 * @author 何冰
 * @version 1.0
 * @date 2008-4-6
 */
public class SampleTreeCardData implements IVOTreeDataByID {

	public String getIDFieldName() {
		return "pk_datasynch";
	}

	public String getParentIDFieldName() {
		return "fpk";
	}

	public String getShowFieldName() {
		return "code+name";
	}

	public SuperVO[] getTreeVO() {
		 nc.vo.dip.datasynch.DipDatasynchVO[] ys=null;
	        BusinessDelegator business = new BDBusinessDelegator();
			SuperVO[] rts = null;
			SuperVO[]  ret=null;
			

			try {
				//从视图中取得根节点
				rts = business.queryByCondition(ViewDipDatadefinitVO.class, " 0=0 order by syscode ");
				//从表中取得所有的叶子
				ys=(DipDatasynchVO[])business.queryByCondition(DipDatasynchVO.class, " 0=0 order by code ");
				//根节点的个数
				int lroots=rts!=null&&rts.length>0?rts.length:0;
				//叶子的个数
				int lys=ys!=null&&ys.length>0?ys.length:0;
				//最后返回的数据对应的list
				List<DipDatasynchVO> retlist=new ArrayList<DipDatasynchVO>();
				
				//如果根节点的个数大于0.那么把视图vo转换成主表vo,放到返回的list中去
				if(lroots>0){
					for(int i=0;i<lroots;i++){
						ViewDipDatadefinitVO root=(ViewDipDatadefinitVO)rts[i];
						DipDatasynchVO ii=new DipDatasynchVO();
//						ii.setFpk(root.getFpk());
						ii.setPk_xt(root.getPrimaryKey());
						ii.setIsfolder(new UFBoolean(true));
						String pk_datasynch=root.getPk();
						ii.setPk_datasynch(pk_datasynch);
						ii.setCode(root.getSyscode());
						ii.setName(root.getSysname());
						ii.setTs(root.getTs()==null?null:root.getTs().toString());
						retlist.add(ii);
					}
				}
				//如果叶子的个数大于0，那么把所有的叶子放到返回的list中去
				if(lys>0){
					List<DipDatasynchVO> ss=Arrays.asList(ys);
					if(ss!=null&&ss.size()>0){
						for(DipDatasynchVO hvo : ss){
							if(hvo.getIsfolder()==null){
								hvo.setIsfolder(new UFBoolean(false));
							}
							if(hvo.getPk_xt()==null||hvo.getPk_xt().length()<=0){
								hvo.setPk_xt(hvo.getFpk());
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
