package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class ActionSetBtn {

	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(165);
		btnVo.setBtnCode("ActionSet");
		btnVo.setBtnName("����");
		btnVo.setBtnChinaName("����");
		btnVo.setOperateStatus(new int[]{
				IBillOperate.OP_NOTEDIT
		});
		btnVo.setChildAry(new int[]{});//�����Ӱ�ť��
		return btnVo;
	}
}
