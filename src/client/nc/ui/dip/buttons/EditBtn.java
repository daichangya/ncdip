package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class EditBtn {

	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(134);
		btnVo.setBtnCode("Edit");
		btnVo.setBtnName("修改");
		btnVo.setBtnChinaName("修改");
		btnVo.setOperateStatus(
				new int[]{IBillOperate.OP_INIT,IBillOperate.OP_NOTEDIT}
				);// 设置那个状态可用
		btnVo.setChildAry(new int[]{});//设置子按钮的
		return btnVo;
	}
}
