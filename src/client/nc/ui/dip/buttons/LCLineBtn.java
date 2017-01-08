package nc.ui.dip.buttons;

import nc.ui.pub.ButtonObject;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.button.IBillButton;
import nc.vo.trade.button.ButtonVO;

public class LCLineBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(146);
		btnVo.setBtnCode("LCLineBtn");
		btnVo.setBtnName("�в���");
		btnVo.setBtnChinaName("�в���");
		btnVo.setOperateStatus(
				new int[]{IBillOperate.OP_EDIT,IBillOperate.OP_ADD}
				);// �����Ǹ�״̬����
		btnVo.setChildAry(new int[]{IBtnDefine.LCLineadd,IBtnDefine.LCLinedel,IBtnDefine.LCLinecopy,IBtnDefine.LCLineins,IBtnDefine.LCLinepast,
				IBtnDefine.Movedup,IBtnDefine.Moveddown});
//		ButtonObject bt=new ButtonObject(IBillButton.Line);
//		ButtonObject[] grop=new ButtonObject[7];
//		for(int i=0;i<5;i++){
//			grop[i]=bt.getChildButtonGroup()[i];
//		}
//		grop[5]=getButtonManager().getButton(IBtnDefine.Movedup);
//		grop[6]=getButtonManager().getButton(IBtnDefine.Moveddown);
//		bt.setChildButtonGroup(grop);
//
//		btnVo.setChildAry(new int[]{IBillButton.Line});//�����Ӱ�ť��
		return btnVo;
	}
}

