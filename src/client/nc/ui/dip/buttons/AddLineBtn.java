package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class AddLineBtn {

	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(147);
		btnVo.setBtnCode("AddLineBtn");
		btnVo.setBtnName("增行");
		btnVo.setBtnChinaName("增行");
		btnVo.setOperateStatus(
				new int[]{IBillOperate.OP_EDIT,IBillOperate.OP_ADD}
				);// 设置那个状态可用
		return btnVo;
	}
}
