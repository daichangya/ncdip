package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class DataProcessCheckBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(153);
		btnVo.setBtnCode("DataProcessCheckBtn");
		btnVo.setBtnName("У����");
		btnVo.setBtnChinaName("У����");
		btnVo.setChildAry(new int[]{});//�����Ӱ�ť��
		btnVo.setOperateStatus(new int[]{
				IBillOperate.OP_NOTEDIT
		});
		return btnVo;
	}
}
