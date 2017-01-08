package nc.vo.dip.dataverify;

import java.util.Arrays;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;



/**
 * 
 * ���ӱ�/����ͷ/������ۺ�VO
 *
 * ��������:Your Create Data
 * @author Your Author Name
 * @version Your Project 1.0
 */
public class  MyBillVO extends HYBillVO {

	public CircularlyAccessibleValueObject[] getChildrenVO() {
//		return (DipDatarecBVO[]) super.getChildrenVO();
		return  (DataverifyBVO[])super.getChildrenVO();
	}

	public CircularlyAccessibleValueObject getParentVO() {
//		return (DipDatarecHVO) super.getParentVO();
		return (DataverifyHVO)super.getParentVO();
	}

	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if( children == null || children.length == 0 ){
			super.setChildrenVO(null);
		}
		else{
//			super.setChildrenVO((CircularlyAccessibleValueObject[]) Arrays.asList(children).toArray(new DipDatarecBVO[0]));
			super.setChildrenVO((CircularlyAccessibleValueObject[]) Arrays.asList(children).toArray(new DataverifyBVO[0]));

		}
	}

	public void setParentVO(CircularlyAccessibleValueObject parent) {
//		super.setParentVO((DipDatarecHVO)parent);
		super.setParentVO((DataverifyHVO)parent);
	}

}