package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class ConversionBtn {
public ButtonVO getButtonVO(){
	ButtonVO btnVo=new ButtonVO();
	btnVo.setBtnNo(114);
	btnVo.setBtnCode("Conversion");
	btnVo.setBtnName("ת��");
	btnVo.setBtnChinaName("ת��");
	btnVo.setChildAry(new int[]{});
	btnVo.setOperateStatus(new int[]{
			IBillOperate.OP_NOTEDIT
	});
	return btnVo;
}
}
