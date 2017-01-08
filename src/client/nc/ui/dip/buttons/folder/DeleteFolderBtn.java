package nc.ui.dip.buttons.folder;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class DeleteFolderBtn {
	public final static int DeleteFolder=159;
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(DeleteFolder);
		btnVo.setBtnCode("DeleteFolder");
		btnVo.setBtnName("删除文件夹");
		btnVo.setBtnChinaName("删除文件夹");
		btnVo.setChildAry(new int[]{});
		btnVo.setOperateStatus(new int[]{
				IBillOperate.OP_NOTEDIT
		});
		return btnVo;
	}
}
