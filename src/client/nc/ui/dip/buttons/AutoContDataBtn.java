package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class AutoContDataBtn {
	public final static int autoContDataBtn=162;
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(autoContDataBtn);
		btnVo.setBtnCode("AutoContDataBtn");
		btnVo.setBtnName("自动对照");
		btnVo.setBtnChinaName("自动对照");
		btnVo.setChildAry(new int[]{});
		btnVo.setOperateStatus(new int[]{
				IBillOperate.OP_NOTEDIT
		});
		return btnVo;
	}
}
