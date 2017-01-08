package nc.ui.dip.datachange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.pub.IVOTreeDataByID;
import nc.vo.dip.datachange.DipDatachangeHVO;
import nc.vo.dip.datadefinit.ViewDipDatadefinitVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

public class SampleTreeCardData implements IVOTreeDataByID  {
	public String getIDFieldName() {
		return "pk_datachange_h";
	}

	public String getParentIDFieldName() {
		return "fpk";
	}

	public String getShowFieldName() {
		return "code+name";
	}

	public SuperVO[] getTreeVO() {
		DipDatachangeHVO[] ys=null;
		BusinessDelegator business = new BDBusinessDelegator();

		SuperVO[] rts = null;
		SuperVO[] ret=null;
	
		try {
			//从视图中取得根节点rts
			rts=business.queryByCondition(ViewDipDatadefinitVO.class," 0=0 order by syscode ");
			//从主表中取得所有的叶子
			ys=(DipDatachangeHVO[]) business.queryByCondition(DipDatachangeHVO.class," 0=0 order by code ");
			//根节点的个数
			int lroots=rts!=null&&rts.length>0?rts.length:0;
			//子节点节点的个数
			int lys=ys!=null&&ys.length>0?ys.length:0;
			//最后返回的数组对应的relist
			List <DipDatachangeHVO> retlist=new ArrayList<DipDatachangeHVO>();
			//如果根节点的个数大于0，那么把视图vo转换成主表vo,放到返回的list中去
			if(lroots>0){
				for(int i=0;i<lroots;i++){
					ViewDipDatadefinitVO root=(ViewDipDatadefinitVO) rts[i];
					DipDatachangeHVO ii=new DipDatachangeHVO();
//					ii.setPk_datadefinit_h(root.getFpk());
					ii.setFpk(root.getFpk());
					ii.setPk_datachange_h(root.getPk());
					ii.setCode(root.getSyscode());
					ii.setName(root.getSysname());
					ii.setBusidata(root.getSysname());
//					ii.setMiddletab(root.getMemorytable());
//					ii.setSystype();
					ii.setTs(root.getTs()==null?null:root.getTs().toString());
					ii.setIsfolder(new UFBoolean(true));
					ii.setPk_xt(root.getPk());
//					
//					SuperVO vo =  HYPubBO_Client.queryByPrimaryKey(DipDatachangeHVO.class, root.getPk());
//					if(vo==null){
//						HYPubBO_Client.insert(ii);
//					}
					retlist.add(ii);
				}
			}
			//如果子节点的个数大于0，那么把所有的子节点放到返回的retlist中去
			if(lys>0){
				List<DipDatachangeHVO> ss=Arrays.asList(ys);
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
			//把返回的retlist转还成数组
			ret=(SuperVO[]) retlist.toArray(new SuperVO[/*lroots*/lys+lroots]);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		return ret;
	}
}
