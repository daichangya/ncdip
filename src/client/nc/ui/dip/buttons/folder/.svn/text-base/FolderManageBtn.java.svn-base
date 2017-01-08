package nc.ui.dip.buttons.folder;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class FolderManageBtn {
	public final static int FOLDERMANAGE=156;
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(FOLDERMANAGE);
		btnVo.setBtnCode("FolderManageBtn");
		btnVo.setBtnName("文件夹管理");
		btnVo.setBtnChinaName("文件夹管理");
		btnVo.setChildAry(new int[]{AddFolderBtn.ADDFOLDERBTN,EditFolderBtn.EditFolder,DeleteFolderBtn.DeleteFolder,MoveFolderBtn.MOVEFOLDERBTN});
		btnVo.setOperateStatus(new int[]{
				IBillOperate.OP_NOTEDIT
		});
		return btnVo;
	}
}
