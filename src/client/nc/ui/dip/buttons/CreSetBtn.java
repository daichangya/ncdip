package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class CreSetBtn {
	public ButtonVO getButtonVOCredence(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(152);
		btnVo.setBtnCode("CRESET");
		btnVo.setBtnName("����");
		btnVo.setBtnChinaName("����");
		btnVo.setChildAry(new int[]{});//�����Ӱ�ť��
		btnVo.setOperateStatus(new int[]{
				IBillOperate.OP_NOTEDIT
		});
		return btnVo;
	}
}
