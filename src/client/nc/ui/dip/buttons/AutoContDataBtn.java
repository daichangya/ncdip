package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class AutoContDataBtn {
	public final static int autoContDataBtn=162;
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(autoContDataBtn);
		btnVo.setBtnCode("AutoContDataBtn");
		btnVo.setBtnName("�Զ�����");
		btnVo.setBtnChinaName("�Զ�����");
		btnVo.setChildAry(new int[]{});
		btnVo.setOperateStatus(new int[]{
				IBillOperate.OP_NOTEDIT
		});
		return btnVo;
	}
}
