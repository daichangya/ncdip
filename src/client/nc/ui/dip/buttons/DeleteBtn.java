package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class DeleteBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(132);
		btnVo.setBtnCode("Delete");
		btnVo.setBtnName("删除");
		btnVo.setBtnChinaName("删除");
		btnVo.setOperateStatus(
				new int[]{IBillOperate.OP_NOTEDIT}
				);// 设置那个状态可用
		btnVo.setChildAry(new int[]{});//设置子按钮的
		return btnVo;
	}
}

