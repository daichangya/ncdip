package nc.ui.dip.datalook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.pub.IVOTreeDataByID;
import nc.vo.dip.datadefinit.DipDatadefinitHVO;
import nc.vo.dip.datadefinit.ViewDipDatadefinitVO;
import nc.vo.dip.datalook.DipDatalookVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

public class SampleTreeCardData implements IVOTreeDataByID{
	public String getIDFieldName() {
		return "pk_datalook";
	}

	public String getParentIDFieldName() {
		return "pk_datadefinit_h";
	}

	public String getShowFieldName() {
		return "code+name";
	}

	public SuperVO[] getTreeVO() {

		DipDatadefinitHVO[] ys = null;
		BusinessDelegator business = new BDBusinessDelegator();
		SuperVO[] rts=null;
		DipDatadefinitHVO[] ret=null;
		try {
			//从视图中取到根节点rts
			rts=business.queryByCondition(ViewDipDatadefinitVO.class, " 0=0 order by syscode ");
			//从主表中取到所有的叶子
			ys = (DipDatadefinitHVO[]) business.queryByCondition(DipDatadefinitHVO.class, " (iscreatetab='Y' or isfolder='Y') and nvl(dr,0)=0 order by syscode");
			
			//根节点的个数
			int lroots=rts!=null&&rts.length>0?rts.length:0;
			//叶子的个数
			int lys=ys!=null&&ys.length>0?ys.length:0;
			//最后返回的数组对应的list
			List<DipDatadefinitHVO> retlist=new ArrayList<DipDatadefinitHVO>();
			//如果根节点的个数大于0，那么把试图vo转换成主表vo，放到返回的list中去
			if(lroots>0){
				for(int i=0;i<lroots;i++){
					ViewDipDatadefinitVO root=(ViewDipDatadefinitVO) rts[i];
					DipDatadefinitHVO ii=new  DipDatadefinitHVO();
					ii.setIsdeploy(root.getIsdeploy());
					ii.setPk_sysregister_h(root.getFpk());
					ii.setPk_datadefinit_h(root.getPk());
					ii.setSyscode(root.getSyscode());
					ii.setSysname(root.getSysname());
					ii.setTs(root.getTs()==null?null:root.getTs().toString());
					ii.setIsfolder(new UFBoolean(true));
					ii.setPk_xt(root.getPrimaryKey());
					retlist.add(ii);
				}
			}
			//如果叶子的个数大于0，那么把所有的叶子放到返回的list中去
			if(lys>0){
				List<DipDatadefinitHVO> ss=Arrays.asList(ys);
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
			ret=(DipDatadefinitHVO[]) retlist.toArray(new DipDatadefinitHVO[lys+lroots]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		DipDatalookVO[] look=new DipDatalookVO[ret.length];
		int i=0;
		for(DipDatadefinitHVO hvo:ret){
			look[i]=new DipDatalookVO();
			look[i].setPk_datadefinit_h(hvo.getPk_sysregister_h());
			look[i].setPk_datalook(hvo.getPk_datadefinit_h());
			look[i].setDatatype(hvo.getDatatype());
			look[i].setCode(hvo.getSyscode());
			look[i].setName(hvo.getSysname());
			look[i].setPk_xt(hvo.getPk_xt());
			look[i].setIsfolder(hvo.getIsfolder());
			i++;
		}
		return look;
	}
}
