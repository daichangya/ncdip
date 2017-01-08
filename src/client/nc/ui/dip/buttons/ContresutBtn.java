package nc.ui.dip.buttons;

import nc.vo.trade.button.ButtonVO;

public class ContresutBtn 
{

	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(129);
		btnVo.setBtnCode("contresut");
		btnVo.setBtnName("对照结果");
		btnVo.setBtnChinaName("对照结果");
		btnVo.setChildAry(new int[]{});
		return btnVo;
	}
}
