package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
//数据转换
public class PasteModelBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(135);
		btnVo.setBtnCode("PASTEMODEL");
		btnVo.setBtnName("粘贴模板");
		btnVo.setBtnChinaName("粘贴模板");
		btnVo.setChildAry(new int[]{});
		btnVo.setOperateStatus(new int[]{
				IBillOperate.OP_NOTEDIT
		});
		return btnVo;
	}
}
