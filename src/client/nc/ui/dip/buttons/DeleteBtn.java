package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class DeleteBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(132);
		btnVo.setBtnCode("Delete");
		btnVo.setBtnName("ɾ��");
		btnVo.setBtnChinaName("ɾ��");
		btnVo.setOperateStatus(
				new int[]{IBillOperate.OP_NOTEDIT}
				);// �����Ǹ�״̬����
		btnVo.setChildAry(new int[]{});//�����Ӱ�ť��
		return btnVo;
	}
}

