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
			//从主表中取到所有的叶子
					ys = (RoleGroupVO[]) business.queryByCondition(RoleGroupVO.class, " 0=0 order by group_code ");

			//叶子的个数
					int lys=ys!=null&&ys.length>0?ys.length:0;
			//最后返回的数组对应的list
			List<RoleGroupVO> retlist=new ArrayList<RoleGroupVO>();
			if(lys>0){
				List<RoleGroupVO> ss=Arrays.asList(ys);
				retlist.addAll(ss);
			}
			//把返回的list转换成数组
			ret=(SuperVO[]) retlist.toArray(new SuperVO[lys]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}
}
