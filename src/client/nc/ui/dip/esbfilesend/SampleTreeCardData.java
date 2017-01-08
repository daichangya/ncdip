package nc.ui.dip.esbfilesend;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.pub.IVOTreeDataByID;
import nc.vo.dip.datadefinit.ViewDipDatadefinitVO;
import nc.vo.dip.esbfilesend.DipEsbSendVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

public class SampleTreeCardData implements IVOTreeDataByID  {
	public String getIDFieldName() {
		return "pk_esbsend";
	}

	public String getParentIDFieldName() {
		return "pk_sys";
	}

	public String getShowFieldName() {
		return "code+name";
	}

	public SuperVO[] getTreeVO() {
		DipEsbSendVO[] send = null;    
		BusinessDelegator business = new BDBusinessDelegator();
		SuperVO[] rts=null;
		SuperVO[] ret=null;
		try {
			//从视图中取到根节点rts
			rts=business.queryByCondition(ViewDipDatadefinitVO.class, " 0=0 order by syscode ");
//			从主表中取到所有的叶子
			send = (DipEsbSendVO[]) business.queryByCondition(DipEsbSendVO.class, "  0=0 order by code ");
			//根节点的个数
			int lroots=rts!=null&&rts.length>0?rts.length:0;
//			叶子的个数
			int lys=send!=null&&send.length>0?send.length:0;
			//最后返回的数组对应的list
			List<DipEsbSendVO> retlist=new ArrayList<DipEsbSendVO>();
			//如果根节点的个数大于0，那么把试图vo转换成主表vo，放到返回的list中去
			if(lroots>0){
				for(int i=0;i<lroots;i++){
					ViewDipDatadefinitVO root=(ViewDipDatadefinitVO) rts[i];
					DipEsbSendVO ii=new  DipEsbSendVO();
					ii.setPk_esbsend(root.getPk());
					ii.setPk_sys(root.getFpk());
					ii.setCode(root.getSyscode());
					ii.setPk_xt(root.getPk());
					ii.setIsfolder(new UFBoolean(true));
					ii.setName(root.getSysname());
					ii.setTs(root.getTs()==null?null:root.getTs().toString());
					retlist.add(ii);
				}
			}
			//如果叶子的个数大于0，那么把所有的叶子放到返回的list中去
			if(lys>0){
				List<DipEsbSendVO> ss=Arrays.asList(send);
				if(ss!=null&&ss.size()>0){
					for(DipEsbSendVO hvo:ss){
						if(hvo.getPk_xt()==null||hvo.getPk_xt().length()<=0){
							hvo.setPk_sys(hvo.getPk_sys());
						}
						if(hvo.getIsfolder()==null||!hvo.getIsfolder().booleanValue()){
							hvo.setIsfolder(new UFBoolean(false));
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
