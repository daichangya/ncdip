package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.button.ButtonVO;

public class YuJingBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(112);
		btnVo.setBtnCode("YuJing");
		btnVo.setBtnName("Ô¤¾¯");
		btnVo.setBtnChinaName("Ô¤¾¯");
		btnVo.setChildAry(new int[]{});
		btnVo.setOperateStatus(new int[]{
				IBillOperate.OP_NOTEDIT
		});
		return btnVo;
	}
}
