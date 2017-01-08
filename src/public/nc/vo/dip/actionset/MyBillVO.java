package nc.vo.dip.actionset;

import java.util.Arrays;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;



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
//		return (DipDatarecBVO[]) super.getChildrenVO();
		return  (ActionSetBVO[])super.getChildrenVO();
	}

	public CircularlyAccessibleValueObject getParentVO() {
//		return (DipDatarecHVO) super.getParentVO();
		return (ActionSetHVO)super.getParentVO();
	}

	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if( children == null || children.length == 0 ){
			super.setChildrenVO(null);
		}
		else{
//			super.setChildrenVO((CircularlyAccessibleValueObject[]) Arrays.asList(children).toArray(new DipDatarecBVO[0]));
			super.setChildrenVO((CircularlyAccessibleValueObject[]) Arrays.asList(children).toArray(new ActionSetBVO[0]));

		}
	}

	public void setParentVO(CircularlyAccessibleValueObject parent) {
//		super.setParentVO((DipDatarecHVO)parent);
		super.setParentVO((ActionSetHVO)parent);
	}

}
