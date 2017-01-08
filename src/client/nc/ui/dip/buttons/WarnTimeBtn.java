package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class WarnTimeBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(119);
		btnVo.setBtnCode("WarnTime");
		btnVo.setBtnName("预警时间");
		btnVo.setBtnChinaName("预警时间");
		btnVo.setChildAry(new int[]{});
		btnVo.setOperateStatus(new int[]{IBillOperate.OP_EDIT,IBillOperate.OP_ADD});
		return btnVo;
	}
}
