package nc.ui.dip.authorize.role;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.pub.IVOTreeDataByID;
import nc.vo.dip.authorize.role.RoleGroupVO;
import nc.vo.pub.SuperVO;

/**
 * 
 * <P>
 * <B>�������͵���ͷ�����ݿ�����</B>
 * </P>
 * <P>
 * ����˵��: <BR>
 * 
 * @author ����
 * @version 
 * @date 2011-4-29
 */
public class SampleTreeCardData implements IVOTreeDataByID {
	public String getIDFieldName() {
		return "pk_role_group";
	}

	public String getParentIDFieldName() {
		return "";
	}

	public String getShowFieldName() {
		return "group_code+group_name";
	}

	public SuperVO[] getTreeVO() {
			RoleGroupVO[] ys = null;
		BusinessDelegator business = new BDBusinessDelegator();
		SuperVO[] ret=null;
		try {
			//��������ȡ�����е�Ҷ��
					ys = (RoleGroupVO[]) business.queryByCondition(RoleGroupVO.class, " 0=0 order by group_code ");

			//Ҷ�ӵĸ���
					int lys=ys!=null&&ys.length>0?ys.length:0;
			//��󷵻ص������Ӧ��list
			List<RoleGroupVO> retlist=new ArrayList<RoleGroupVO>();
			if(lys>0){
				List<RoleGroupVO> ss=Arrays.asList(ys);
				retlist.addAll(ss);
			}
			//�ѷ��ص�listת��������
			ret=(SuperVO[]) retlist.toArray(new SuperVO[lys]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}
}
