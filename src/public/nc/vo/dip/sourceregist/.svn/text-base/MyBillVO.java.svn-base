package nc.vo.dip.sourceregist;


import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.dip.sourceregist.SourceregistVO;

public class MyBillVO extends HYBillVO {

	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return super.getChildrenVO();
	}

	public CircularlyAccessibleValueObject getParentVO() {
		return (SourceregistVO) super.getParentVO();
	}

	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if( children == null || children.length == 0 ){
			super.setChildrenVO(null);
		}
		else{
			super.setChildrenVO(children);
		}
	}
	
	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((SourceregistVO)parent);
	}
}
