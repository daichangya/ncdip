package nc.ui.dip.buttons;

import nc.vo.trade.button.ButtonVO;

public class TestRecBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(137);
		btnVo.setBtnCode("TestRec");
		btnVo.setBtnName("接收测试");
		btnVo.setBtnChinaName("接收测试");
//		btnVo.setOperateStatus(
//				new int[]{IBillOperate.OP_INIT,IBillOperate.OP_NOTEDIT}
//				);// 设置那个状态可用
		btnVo.setChildAry(new int[]{});//设置子按钮的
		return btnVo;
	}
}

