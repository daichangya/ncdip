package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class ExportModelBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(122);
		btnVo.setBtnCode("EXPORTMODEL");
		btnVo.setBtnName("����ģ��");
		btnVo.setBtnChinaName("����ģ��");
		btnVo.setChildAry(new int[]{});
		btnVo.setOperateStatus(new int[]{
				IBillOperate.OP_NOTEDIT
		});
		return btnVo;
	}
}
