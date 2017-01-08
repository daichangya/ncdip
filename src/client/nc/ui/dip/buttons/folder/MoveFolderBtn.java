package nc.ui.dip.buttons.folder;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

public class MoveFolderBtn {
	public final static int MOVEFOLDERBTN=164;
	public ButtonVO getButtonVO(){
		ButtonVO btnVo=new ButtonVO();
		btnVo.setBtnNo(MOVEFOLDERBTN);
		btnVo.setBtnCode("MOVEFOLDERBTN");
		btnVo.setBtnName("文件移动");
		btnVo.setBtnChinaName("文件移动");
		btnVo.setChildAry(new int[]{});
		btnVo.setOperateStatus(new int[]{
				IBillOperate.OP_NOTEDIT
		});
		return btnVo;
	}
}
