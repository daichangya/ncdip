package nc.ui.dip.buttons;

import nc.vo.trade.button.ButtonVO;


public class MovedupBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(123);
		
		btnVo.setBtnCode("Movedup");
		btnVo.setBtnName("上移");
		btnVo.setBtnChinaName("上移");

		btnVo.setChildAry(new int[]{});//设置子按钮的
		return btnVo;
	}

}
