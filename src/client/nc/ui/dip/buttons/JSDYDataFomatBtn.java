package nc.ui.dip.buttons;

import nc.vo.trade.button.ButtonVO;

public class JSDYDataFomatBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(143);
		btnVo.setBtnCode("JSDYdataFomat");
		btnVo.setBtnName("���ݸ�ʽ");
		btnVo.setBtnChinaName("���ݸ�ʽ");
//		btnVo.setOperateStatus(
//				new int[]{IBillOperate.OP_INIT,IBillOperate.OP_NOTEDIT}
//				);// �����Ǹ�״̬����
		btnVo.setChildAry(new int[]{});//�����Ӱ�ť��
		return btnVo;
	}
}

