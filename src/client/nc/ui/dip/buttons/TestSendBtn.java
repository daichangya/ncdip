package nc.ui.dip.buttons;

import nc.vo.trade.button.ButtonVO;

public class TestSendBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(138);
		btnVo.setBtnCode("TestSend");
		btnVo.setBtnName("���Ͳ���");
		btnVo.setBtnChinaName("���Ͳ���");
//		btnVo.setOperateStatus(
//				new int[]{IBillOperate.OP_INIT,IBillOperate.OP_NOTEDIT}
//				);// �����Ǹ�״̬����
		btnVo.setChildAry(new int[]{});//�����Ӱ�ť��
		return btnVo;
	}
}

