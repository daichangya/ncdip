package nc.ui.dip.buttons.folder;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class AddFolderBtn {
	public final static int ADDFOLDERBTN=157;
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(ADDFOLDERBTN);
		btnVo.setBtnCode("AddFolderBtn");
		btnVo.setBtnName("�½��ļ���");
		btnVo.setBtnChinaName("�½��ļ���");
		btnVo.setChildAry(new int[]{});
		btnVo.setOperateStatus(new int[]{
				IBillOperate.OP_NOTEDIT
		});
		return btnVo;
	}
}
