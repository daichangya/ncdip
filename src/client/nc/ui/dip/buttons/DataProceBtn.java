package nc.ui.dip.buttons;

import nc.vo.trade.button.ButtonVO;

public class DataProceBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(110);
		btnVo.setBtnCode("DataProce");
		btnVo.setBtnName("加工");
		btnVo.setBtnChinaName("加工");
		btnVo.setChildAry(new int[]{});
		return btnVo;
	}
}
