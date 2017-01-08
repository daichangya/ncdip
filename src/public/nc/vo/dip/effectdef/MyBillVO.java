package nc.vo.dip.effectdef;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.dip.effectdef.CdSbodyVO;

/**
 * 
 * 卡片型单表体,单子表/单表头/单表体聚合VO
 * 
 * 创建日期:Your Create Data
 * 
 * @author 何冰
 * @version Your Project 1.0
 */
public class MyBillVO extends HYBillVO {

	/** 字段描述 */
	private static final long serialVersionUID = 8568702298784656711L;

	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return super.getChildrenVO();
	}

	public CircularlyAccessibleValueObject getParentVO() {
		return (CdSbodyVO) super.getParentVO();
	}

	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if (children == null || children.length == 0) {
			super.setChildrenVO(null);
		} else {
			super.setChildrenVO(children);
		}
	}

	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((CdSbodyVO) parent);
	}

}
