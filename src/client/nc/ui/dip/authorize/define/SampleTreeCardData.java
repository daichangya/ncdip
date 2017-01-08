package nc.ui.dip.authorize.define;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.pub.IVOTreeDataByID;
import nc.vo.dip.authorize.define.DipADContdataVO;
import nc.vo.dip.datadefinit.ViewDipDatadefinitVO;
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
 * @author 程莉
 * @version 
 * @date 2011-4-29
 */
public class SampleTreeCardData implements IVOTreeDataByID {
	public String getIDFieldName() {
		return "pk_contdata_h";
	}

	public String getParentIDFieldName() {
		return "pk_sysregister_h";
	}

	public String getShowFieldName() {
		return "code+name";
	}

	public SuperVO[] getTreeVO() {

			DipADContdataVO[] ys = null;
		BusinessDelegator business = new BDBusinessDelegator();
		SuperVO[] rts=null;
		SuperVO[] ret=null;
		try {
			//从视图中取到根节点rts
			rts=business.queryByCondition(ViewDipDatadefinitVO.class, " 0=0 order by syscode");
			//从主表中取到所有的叶子
					ys = (DipADContdataVO[]) business.queryByCondition(DipADContdataVO.class, " 0=0 order by code ");

			//根节点的个数
			int lroots=rts!=null&&rts.length>0?rts.length:0;
			//叶子的个数
					int lys=ys!=null&&ys.length>0?ys.length:0;
			//最后返回的数组对应的list
			List<DipADContdataVO> retlist=new ArrayList<DipADContdataVO>();
			//如果根节点的个数大于0，那么把试图vo转换成主表vo，放到返回的list中去
			if(lroots>0){
				for(int i=0;i<lroots;i++){
					ViewDipDatadefinitVO root=(ViewDipDatadefinitVO) rts[i];
					DipADContdataVO ii=new  DipADContdataVO();
					ii.setPk_sysregister_h(root.getFpk());
					ii.setPk_contdata_h(root.getPk());
					ii.setCode(root.getSyscode());//编码
					ii.setName(root.getSysname());//名称
					ii.setTs(root.getTs()==null?null:root.getTs().toString());
					ii.setIsfolder(new UFBoolean(true));
					ii.setPk_xt(root.getPrimaryKey());
//					DipContdataVO vo=(DipContdataVO) HYPubBO_Client.queryByPrimaryKey(DipContdataVO.class, root.getPk());
//					if(vo==null){
//						HYPubBO_Client.insert(ii);
						retlist.add(ii);
//					}else{
//						retlist.add(vo);
//					}
				}
			}
			//如果叶子的个数大于0，那么把所有的叶子放到返回的list中去
			if(lys>0){
				List<DipADContdataVO> ss=Arrays.asList(ys);
				for(int i=0;i<ss.size();i++){
					if(ss.get(i).getIsfolder()==null){
						ss.get(i).setIsfolder(new UFBoolean(false));
					}
					if(ss.get(i).getPk_xt()==null){
						ss.get(i).setPk_xt(ss.get(i).getPk_sysregister_h());
					}
				}
				retlist.addAll(ss);
			}
			//把返回的list转换成数组
			ret=(SuperVO[]) retlist.toArray(new SuperVO[/*lroots*/lys+lroots]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}
}
