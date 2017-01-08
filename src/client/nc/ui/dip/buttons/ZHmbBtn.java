package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class ZHmbBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(142);
		btnVo.setBtnCode("ZHmb");
//		btnVo.setBtnName("模板整合");
		btnVo.setBtnName("模板");
		btnVo.setBtnChinaName("模板");
		btnVo.setChildAry(new int[]{});//设置子按钮的
		btnVo.setOperateStatus(new int[]{
				IBillOperate.OP_NOTEDIT
		});
		return btnVo;
	}
}

