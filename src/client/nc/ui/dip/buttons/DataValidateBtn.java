package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.fi.insbill.InsBillVO;
import nc.vo.trade.button.ButtonVO;

public class DataValidateBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(106);
		btnVo.setBtnCode("DataValidate");
		btnVo.setBtnName("����У��");
		btnVo.setBtnChinaName("����У��");
		btnVo.setChildAry(new int[]{});
		btnVo.setOperateStatus(new int[]{
				IBillOperate.OP_NOTEDIT
		});
		return btnVo;
	}
}
