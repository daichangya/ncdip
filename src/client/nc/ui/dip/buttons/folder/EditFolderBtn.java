package nc.ui.dip.buttons.folder;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class EditFolderBtn {
	public final static int EditFolder=158;
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(EditFolder);
		btnVo.setBtnCode("EditFolderBtn");
		btnVo.setBtnName("�޸��ļ���");
		btnVo.setBtnChinaName("�޸��ļ���");
		btnVo.setChildAry(new int[]{});
		btnVo.setOperateStatus(new int[]{
				IBillOperate.OP_NOTEDIT
		});
		return btnVo;
	}
}
