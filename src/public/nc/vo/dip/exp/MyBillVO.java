package nc.vo.dip.exp;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

public class MyBillVO extends HYBillVO
{
  private static final long serialVersionUID = 8568702298784656711L;

  public CircularlyAccessibleValueObject[] getChildrenVO()
  {
    return super.getChildrenVO();
  }

  public CircularlyAccessibleValueObject getParentVO() {
    return ((DataExpVO)super.getParentVO());
  }

  public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
    if ((children == null) || (children.length == 0))
      super.setChildrenVO(null);
    else
      super.setChildrenVO(children);
  }

  public void setParentVO(CircularlyAccessibleValueObject parent)
  {
    super.setParentVO((DataExpVO)parent);
  }
}