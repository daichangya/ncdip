package nc.ui.dip.tyzhq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.pub.IVOTreeDataByID;
import nc.vo.dip.datadefinit.ViewDipDatadefinitVO;
import nc.vo.dip.tyzhq.DipTYZHDatachangeHVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

public class SampleTreeCardData implements IVOTreeDataByID  {
	public String getIDFieldName() {
		return "pk_tyzhq_h";
	}

	public String getParentIDFieldName() {
		return "fpk";
	}

	public String getShowFieldName() {
		return "code+name";
	}

	public SuperVO[] getTreeVO() {
		DipTYZHDatachangeHVO[] ys=null;
		BusinessDelegator business = new BDBusinessDelegator();

		SuperVO[] rts = null;
		SuperVO[] ret=null;
	
		try {
			//����ͼ��ȡ�ø��ڵ�rts
			rts=business.queryByCondition(ViewDipDatadefinitVO.class," 0=0 order by syscode ");
			//��������ȡ�����е�Ҷ��
			ys=(DipTYZHDatachangeHVO[]) business.queryByCondition(DipTYZHDatachangeHVO.class," 0=0 order by code ");
			//���ڵ�ĸ���
			int lroots=rts!=null&&rts.length>0?rts.length:0;
			//�ӽڵ�ڵ�ĸ���
			int lys=ys!=null&&ys.length>0?ys.length:0;
			//��󷵻ص������Ӧ��relist
			List <DipTYZHDatachangeHVO> retlist=new ArrayList<DipTYZHDatachangeHVO>();
			//������ڵ�ĸ�������0����ô����ͼvoת��������vo,�ŵ����ص�list��ȥ
			if(lroots>0){
				for(int i=0;i<lroots;i++){
					ViewDipDatadefinitVO root=(ViewDipDatadefinitVO) rts[i];
					DipTYZHDatachangeHVO ii=new DipTYZHDatachangeHVO();
					ii.setFpk(root.getFpk());
					ii.setPk_tyzhq_h(root.getPk());
					ii.setCode(root.getSyscode());
					ii.setName(root.getSysname());
					ii.setTs(root.getTs()==null?null:root.getTs().toString());
					ii.setIsfolder(new UFBoolean(true));
					ii.setPk_xt(root.getPk());
					
					retlist.add(ii);
				}
			}
			//����ӽڵ�ĸ�������0����ô�����е��ӽڵ�ŵ����ص�retlist��ȥ
			if(lys>0){
				List<DipTYZHDatachangeHVO> ss=Arrays.asList(ys);
				
				retlist.addAll(ss);
			}
			//�ѷ��ص�retlistת��������
			ret=(SuperVO[]) retlist.toArray(new SuperVO[/*lroots*/lys+lroots]);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		return ret;
	}
}