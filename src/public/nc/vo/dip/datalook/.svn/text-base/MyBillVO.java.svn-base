package nc.vo.dip.datalook;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

import nc.vo.dip.datalook.DipDatalookVO;

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
//		return (DipDatalookVO[]) super.getChildrenVO();
		return super.getChildrenVO();
	}

	public CircularlyAccessibleValueObject getParentVO() {
//		return super.getParentVO();
		return (DipDatalookVO) super.getParentVO();
	}

	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
//		if( children == null || children.length == 0 ){
//		super.setChildrenVO(null);
//		}
//		else{
//		super.setChildrenVO((CircularlyAccessibleValueObject[]) Arrays.asList(children).toArray(new DipDatalookVO[0]));
//		}
		if( children == null || children.length == 0 ){
			super.setChildrenVO(null);
		}
		else{
			super.setChildrenVO(children);
		}
	}

	public void setParentVO(CircularlyAccessibleValueObject parent) {
//		super.setParentVO(parent);
		super.setParentVO((DipDatalookVO)parent);
	}

}
