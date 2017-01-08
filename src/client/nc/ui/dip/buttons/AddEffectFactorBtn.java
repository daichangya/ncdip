package nc.ui.dip.buttons;

import nc.vo.trade.button.ButtonVO;

public class AddEffectFactorBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(115);
		btnVo.setBtnCode("AddEffectFactor");
		btnVo.setBtnName("添加影响因素");
		btnVo.setBtnChinaName("添加影响因素");
//		btnVo.setOperateStatus(
//				new int[]{IBillOperate.OP_INIT,IBillOperate.OP_NOTEDIT}
//				);// 设置那个状态可用
		btnVo.setChildAry(new int[]{});//设置子按钮的
		return btnVo;
	}
}

