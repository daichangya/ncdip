package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class RelationSetBtn {

	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(167);
		btnVo.setBtnCode("RelationSet");
		btnVo.setBtnName("��������");
		btnVo.setBtnChinaName("��������");
		btnVo.setOperateStatus(new int[]{
				IBillOperate.OP_NOADD_NOTEDIT
		});
		btnVo.setChildAry(new int[]{});//�����Ӱ�ť��
		return btnVo;
	}
}
