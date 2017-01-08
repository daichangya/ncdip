package nc.ui.dip.authorize.maintain;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.pub.IVOTreeDataByID;
import nc.vo.dip.authorize.define.DipADContdataVO;
import nc.vo.dip.authorize.maintain.DipADContdatawhHVO;
import nc.vo.dip.datadefinit.ViewDipDatadefinitVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * 
 * <P>
 * <B>树管理型主子表，数据控制类</B>
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
		return "pk_contdatawh_h";
	}

	public String getParentIDFieldName() {
		return "fpk";
	}

	public String getShowFieldName() {
		return "wbbm+sysname";
	}

	public SuperVO[] getTreeVO() {

		DipADContdataVO[] ys = null;
		BusinessDelegator business = new BDBusinessDelegator();
		SuperVO[] rts=null;
		DipADContdataVO[] ret=null;
		try {
			//从视图中取到根节点rts
			rts=business.queryByCondition(ViewDipDatadefinitVO.class, " nvl(dr,0)=0 order by syscode");
			//从主表中取到所有的叶子
			ys = (DipADContdataVO[]) business.queryByCondition(DipADContdataVO.class, "   contcolname is not null or isfolder='Y' order by code");

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
					ii.setPk_xt(root.getPrimaryKey());
					ii.setIsfolder(new UFBoolean(true));
					retlist.add(ii);
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
						ss.get(i).setPk_xt(ss.get(i).getFpk());
					}
				}
				
				retlist.addAll(ss);
			}
			//把返回的list转换成数组
			ret=(DipADContdataVO[]) retlist.toArray(new DipADContdataVO[lys+lroots]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		DipADContdatawhHVO[] hys = new DipADContdatawhHVO[ret.length];
		int i=0;
		for(DipADContdataVO vo:ret){
			hys[i]=new DipADContdatawhHVO();
			hys[i].setPk_contdatawh_h(vo.getPk_contdata_h());
			hys[i].setFpk(vo.getPk_sysregister_h());
			hys[i].setWbbm(vo.getCode());
			hys[i].setSysname(vo.getName());
			hys[i].setIsfolder(vo.getIsfolder());
			hys[i].setPk_xt(vo.getPk_xt());
			i++;
		}
		return hys;
		
		/*
		ContdatawhHVO[] ys = null;
		BusinessDelegator business = new BDBusinessDelegator();
		SuperVO[] rts=null;
		SuperVO[] ret=null;
		try {
			//从视图中取到根节点rts
			rts=business.queryByCondition(ViewDipDatadefinitVO.class, null);
			//从主表中取到所有的叶子
			ys = (ContdatawhHVO[]) business.queryByCondition(ContdatawhHVO.class, null);

			//根节点的个数
			int lroots=rts!=null&&rts.length>0?rts.length:0;
			//叶子的个数
			int lys=ys!=null&&ys.length>0?ys.length:0;
			//最后返回的数组对应的list
			List<ContdatawhHVO> retlist=new ArrayList<ContdatawhHVO>();
			//如果根节点的个数大于0，那么把试图vo转换成主表vo，放到返回的list中去
			if(lroots>0){
				for(int i=0;i<lroots;i++){
					ViewDipDatadefinitVO root=(ViewDipDatadefinitVO) rts[i];
					ContdatawhHVO ii=new  ContdatawhHVO();
					ii.setFpk(root.getFpk());
					ii.setPk_contdatawh_h(root.getPk());
					ii.setWbbm(root.getSyscode());//编码
					ii.setSysname(root.getSysname());//名称
					ii.setTs(root.getTs());
					ii.setDr(root.getDr());

//					SuperVO vo=HYPubBO_Client.queryByPrimaryKey(ContdatawhVO.class, root.getPk());
//					if(vo==null){
//					HYPubBO_Client.insert(ii);
//					}
					retlist.add(ii);
				}
			}
			//如果叶子的个数大于0，那么把所有的叶子放到返回的list中去
			if(lys>0){
				List<ContdatawhHVO> ss=Arrays.asList(hys);
				retlist.addAll(ss);
			}
			//把返回的list转换成数组
			ret=(SuperVO[]) retlist.toArray(new SuperVO[lrootslys+lroots]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;*/
	}
}
