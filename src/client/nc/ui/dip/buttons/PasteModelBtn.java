package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
//����ת��
public class PasteModelBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(135);
		btnVo.setBtnCode("PASTEMODEL");
		btnVo.setBtnName("ճ��ģ��");
		btnVo.setBtnChinaName("ճ��ģ��");
		btnVo.setChildAry(new int[]{});
		btnVo.setOperateStatus(new int[]{
				IBillOperate.OP_NOTEDIT
		});
		return btnVo;
	}
}
