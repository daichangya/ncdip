package nc.ui.dip.buttons;

import nc.vo.trade.button.ButtonVO;


public class MovedupBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(123);
		
		btnVo.setBtnCode("Movedup");
		btnVo.setBtnName("����");
		btnVo.setBtnChinaName("����");

		btnVo.setChildAry(new int[]{});//�����Ӱ�ť��
		return btnVo;
	}

}
