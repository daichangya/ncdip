package nc.ui.dip.credence;

import java.util.ArrayList;
import java.util.List;

import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.pub.IVOTreeDataByID;
import nc.util.dip.sj.SJUtil;
import nc.vo.dip.commandview.CommandViewVO;
import nc.vo.dip.credence.CredenceHVO;
import nc.vo.pub.SuperVO;

/**
*版权信息：商佳科技
*作者：   程莉
*版本：   
*描述：   "数据定义"左树右表结构:左边树结构与外部系统注册树结构根节点一致
*创建日期：2011-04-21
* @throws Exception 
*/
public class SampleTreeCardData implements IVOTreeDataByID  {
	public String getIDFieldName() {
		return "pk_credence_h";
	}

	public String getParentIDFieldName() {
		return "pk_datadefinit_h";
	}

	public String getShowFieldName() {
		return "code+name";
	}

	public SuperVO[] getTreeVO() {

		CredenceHVO[] ys = null;
		BusinessDelegator business = new BDBusinessDelegator();
		SuperVO[] rts=null;
		SuperVO[] ret=null;
		try {
			//从视图中取到根节点rts
			rts=business.queryByCondition(CommandViewVO.class, null);
			//从主表中取到所有的叶子
			ys = (CredenceHVO[]) business.queryByCondition(CredenceHVO.class, null);
			
			//根节点的个数
			int lroots=rts!=null&&rts.length>0?rts.length:0;
			//叶子的个数
			int lys=ys!=null&&ys.length>0?ys.length:0;
			//最后返回的数组对应的list
			List<CredenceHVO> retlist=new ArrayList<CredenceHVO>();
			//如果根节点的个数大于0，那么把试图vo转换成主表vo，放到返回的list中去
			if(lroots>0){
				//DipDatadefinitHVO[] roots=new DipDatadefinitHVO[lroots];
				for(int i=0;i<lroots;i++){
					CommandViewVO root=(CommandViewVO) rts[i];
					CredenceHVO ii=new  CredenceHVO();
					String pk_datadefinit_h=root.getFpk();
					String pk_credence_h=root.getPk();
					ii.setPk_datadefinit_h(pk_datadefinit_h);
					ii.setPk_credence_h(pk_credence_h);
					ii.setCode(root.getSyscode());
					ii.setName(root.getSysname());
					ii.setTs(root.getTs()==null?null:root.getTs().toString());
					boolean iscontin=false;
					if(lys>0&&!SJUtil.isNull(pk_credence_h)&&!SJUtil.isNull(pk_datadefinit_h)){
						for(CredenceHVO hvo:ys){
							String hpk=hvo.getPk_credence_h();
							String hfk=hvo.getPk_datadefinit_h();
							if(!SJUtil.isNull(hpk)&&!SJUtil.isNull(hfk)&&hpk.equals(pk_credence_h)&&hfk.equals(pk_datadefinit_h)){
								iscontin=true;

								hvo.setDef_attmentnum(hvo.getAttmentnum());
								hvo.setDef_credtype(hvo.getCredtype());
								hvo.setDef_doperatordate(hvo.getDoperatordate());
								hvo.setDef_voperatorid(hvo.getVoperatorid());
								retlist.add(hvo);
								break;
							}
						}
					}
					if(!iscontin){
						if(!SJUtil.isNull(pk_datadefinit_h)){
							HYPubBO_Client.insert(ii);
						}
						ii.setDef_attmentnum(ii.getAttmentnum());
						ii.setDef_credtype(ii.getCredtype());
						ii.setDef_doperatordate(ii.getDoperatordate());
						ii.setDef_voperatorid(ii.getVoperatorid());
						retlist.add(ii);
					}
				}
			}
			ret=(SuperVO[]) retlist.toArray(new SuperVO[lroots]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}
}
