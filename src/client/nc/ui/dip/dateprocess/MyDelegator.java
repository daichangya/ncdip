package nc.ui.dip.dateprocess;

import nc.vo.pub.SuperVO;

/**
  *
  *����ҵ��������ȱʡʵ��
  *@author author
  *@version tempProject version
  */
public class MyDelegator extends AbstractMyDelegator{

 /**
   *
   *
   *�÷������ڻ�ȡ��ѯ�������û����Զ�������޸ġ�
   *
   */
 public String getBodyCondition(Class bodyClass,String key){
   return super.getBodyCondition(bodyClass,key);
 }
 @Override
public SuperVO[] queryByCondition(Class voClass, String strWhere) throws Exception {
	SuperVO[] superVOs = super.queryByCondition(voClass, strWhere+" and rownum<1000 order by sdate desc ");
	return superVOs;
}
}