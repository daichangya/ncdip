package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class SendFormBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(139);
		btnVo.setBtnCode("SENDFORM");
		btnVo.setBtnName("ǰ̨����");
		btnVo.setBtnChinaName("ǰ̨����");
		btnVo.setOperateStatus(
				new int[]{IBillOperate.OP_NOTEDIT}
				);// �����Ǹ�״̬����
		btnVo.setChildAry(new int[]{});//�����Ӱ�ť��
		return btnVo;
	}
}

