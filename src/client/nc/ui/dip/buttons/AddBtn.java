package nc.ui.dip.buttons;

import nc.vo.trade.button.ButtonVO;

public class AddBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(131);
		btnVo.setBtnCode("Add");
		btnVo.setBtnName("增加");
		btnVo.setBtnChinaName("增加");
//		btnVo.setOperateStatus(
//				new int[]{IBillOperate.OP_INIT,IBillOperate.OP_NOTEDIT}
//				);// 设置那个状态可用
		btnVo.setChildAry(new int[]{});//设置子按钮的
		return btnVo;
	}
}

