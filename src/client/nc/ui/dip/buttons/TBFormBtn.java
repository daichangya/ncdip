package nc.ui.dip.buttons;

import nc.vo.trade.button.ButtonVO;

public class TBFormBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(140);
		btnVo.setBtnCode("TBFORM");
		btnVo.setBtnName("ǰ̨ͬ��");
		btnVo.setBtnChinaName("ǰ̨ͬ��");
//		btnVo.setOperateStatus(
//				new int[]{IBillOperate.OP_INIT,IBillOperate.OP_NOTEDIT}
//				);// �����Ǹ�״̬����
		btnVo.setChildAry(new int[]{});//�����Ӱ�ť��
		return btnVo;
	}
}

