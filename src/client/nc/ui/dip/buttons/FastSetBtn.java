package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class FastSetBtn {

	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(166);
		btnVo.setBtnCode("FastSet");
		btnVo.setBtnName("������Ȩ");
		btnVo.setBtnChinaName("������Ȩ");
		btnVo.setOperateStatus(new int[]{
				IBillOperate.OP_NOTEDIT
		});
		btnVo.setChildAry(new int[]{});//�����Ӱ�ť��
		return btnVo;
	}
}
