package nc.ui.dip.buttons;

import nc.vo.trade.button.ButtonVO;

public class ClearCacheBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(145);
		btnVo.setBtnCode("CLEARCACHEBTN");
		btnVo.setBtnName("������");
		btnVo.setBtnChinaName("������");
		btnVo.setChildAry(new int[]{});
		return btnVo;
	}
}
