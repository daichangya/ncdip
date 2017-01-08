package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class InitUFOENVBtn {
	public final static int initUFOENVBtn=163;
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(initUFOENVBtn);
		btnVo.setBtnCode("initUFOENVBtn");
		btnVo.setBtnName("初始环境");
		btnVo.setBtnChinaName("初始环境");
		btnVo.setChildAry(new int[]{});
		btnVo.setOperateStatus(new int[]{
				IBillOperate.OP_ALL
		});
		return btnVo;
	}
}
