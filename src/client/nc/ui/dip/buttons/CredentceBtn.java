package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class CredentceBtn {
	public ButtonVO getButtonVOCredence(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(133);
		btnVo.setBtnCode("CREDENCE");
		btnVo.setBtnName("凭证合并设置");
		btnVo.setBtnChinaName("凭证合并设置");
//		btnVo.setOperateStatus(
//				new int[]{IBillOperate.OP_INIT,IBillOperate.OP_NOTEDIT}
//				);// 设置那个状态可用
		btnVo.setChildAry(new int[]{});//设置子按钮的
		btnVo.setOperateStatus(new int[]{
				IBillOperate.OP_NOTEDIT
		});
		return btnVo;
	}
}
