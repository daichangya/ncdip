package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class DelLineBtn {

	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(148);
		btnVo.setBtnCode("DelLineBtn");
		btnVo.setBtnName("ɾ��");
		btnVo.setBtnChinaName("ɾ��");
		btnVo.setOperateStatus(
				new int[]{IBillOperate.OP_EDIT,IBillOperate.OP_ADD}
				);// �����Ǹ�״̬����
		return btnVo;
	}
}
