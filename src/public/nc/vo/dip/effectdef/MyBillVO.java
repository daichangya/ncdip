package nc.vo.dip.effectdef;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.dip.effectdef.CdSbodyVO;

/**
 * 
 * ��Ƭ�͵�����,���ӱ�/����ͷ/������ۺ�VO
 * 
 * ��������:Your Create Data
 * 
 * @author �α�
 * @version Your Project 1.0
 */
public class MyBillVO extends HYBillVO {

	/** �ֶ����� */
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
