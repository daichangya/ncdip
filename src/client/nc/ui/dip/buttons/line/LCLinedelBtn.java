package nc.ui.dip.buttons.line;

import nc.vo.trade.button.ButtonVO;

public class LCLinedelBtn {
	public ButtonVO getButtonVO(){
//		public final static int LCLineadd=147;
//		public final static int LCLinedel=148;
//		public final static int LCLinecopy=149;������
//		public final static int LCLineins=150;������
//		public final static int LCLinepast=151;ճ����
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(148);
		btnVo.setBtnCode("LCLinedel");
		btnVo.setBtnName("ɾ��");
		btnVo.setBtnChinaName("ɾ��");
//		btnVo.setOperateStatus(
//				new int[]{IBillOperate.OP_EDIT}
//				);// �����Ǹ�״̬����
		btnVo.setChildAry(new int[]{});
//		ButtonObject bt=new ButtonObject(IBillButton.Line);
//		ButtonObject[] grop=new ButtonObject[7];
//		for(int i=0;i<5;i++){
//			grop[i]=bt.getChildButtonGroup()[i];
//		}
//		grop[5]=getButtonManager().getButton(IBtnDefine.Movedup);
//		grop[6]=getButtonManager().getButton(IBtnDefine.Moveddown);
//		bt.setChildButtonGroup(grop);
//
//		btnVo.setChildAry(new int[]{IBillButton.Line});//�����Ӱ�ť��
		return btnVo;
	}
}

