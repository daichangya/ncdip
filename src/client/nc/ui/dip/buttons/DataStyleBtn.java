package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class DataStyleBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(116);
		btnVo.setBtnCode("DataStyle");
		btnVo.setBtnName("���ݸ�ʽ");
		btnVo.setBtnChinaName("���ݸ�ʽ");
		btnVo.setChildAry(new int[]{});
		btnVo.setOperateStatus(new int[]{
				IBillOperate.OP_NOTEDIT
		});
		return btnVo;
	}
}
