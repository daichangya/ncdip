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
*��Ȩ��Ϣ���̼ѿƼ�
*���ߣ�   ����
*�汾��   
*������   "���ݶ���"�����ұ�ṹ:������ṹ���ⲿϵͳע�����ṹ���ڵ�һ��
*�������ڣ�2011-04-21
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
			//����ͼ��ȡ�����ڵ�rts
			rts=business.queryByCondition(CommandViewVO.class, null);
			//��������ȡ�����е�Ҷ��
			ys = (CredenceHVO[]) business.queryByCondition(CredenceHVO.class, null);
			
			//���ڵ�ĸ���
			int lroots=rts!=null&&rts.length>0?rts.length:0;
			//Ҷ�ӵĸ���
			int lys=ys!=null&&ys.length>0?ys.length:0;
			//��󷵻ص������Ӧ��list
			List<CredenceHVO> retlist=new ArrayList<CredenceHVO>();
			//������ڵ�ĸ�������0����ô����ͼvoת��������vo���ŵ����ص�list��ȥ
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
