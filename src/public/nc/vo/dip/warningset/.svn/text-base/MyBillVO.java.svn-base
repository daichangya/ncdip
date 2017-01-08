package nc.vo.dip.warningset;

import java.util.Arrays;

import nc.util.dip.sj.SJUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

import nc.vo.dip.warningset.DipWarningsetBVO;
import nc.vo.dip.warningset.DipWarningsetVO;

/**
 * 
 * 单子表/单表头/单表体聚合VO
 *
 * 创建日期:Your Create Data
 * @author Your Author Name
 * @version Your Project 1.0
 */
public class  MyBillVO extends HYBillVO {

	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return (DipWarningsetBVO[]) super.getChildrenVO();
	}

	public CircularlyAccessibleValueObject getParentVO() {
		return (DipWarningsetVO) super.getParentVO();
	}

	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if( children == null || children.length == 0 ){
			super.setChildrenVO(null);
		}
		else{
			super.setChildrenVO((CircularlyAccessibleValueObject[]) Arrays.asList(children).toArray(new DipWarningsetBVO[0]));
		}
	}

	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((DipWarningsetVO)parent);
	}

	@Override
	public boolean equals(Object obj) {
		if(SJUtil.isNull(obj)){
			return false;
		}
		if (!(obj instanceof MyBillVO)) {
			return false;
		}else{
			MyBillVO mybillvo=(MyBillVO) obj;
			try {
				if(getParentVO().getPrimaryKey().equals(mybillvo.getParentVO().getPrimaryKey())
						&&((SJUtil.isNull(getParentVO().getAttributeValue("ts"))&&SJUtil.isNull(mybillvo.getParentVO().getAttributeValue("ts")))||getParentVO().getAttributeValue("ts").equals(mybillvo.getParentVO().getAttributeValue("ts")))){
					return true;
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
		return false;
	} 

}
