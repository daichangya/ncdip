package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
/**
 * ����ת����ϵͳģ��
 * @author db2admin
 *
 */
public class SysModelBtn {
	public ButtonVO getButtonVO(){
		ButtonVO btnVO=new ButtonVO();
		btnVO.setBtnNo(136);
		btnVO.setBtnCode("SYSMODEL");
		btnVO.setBtnName("ϵͳģ��");
		btnVO.setBtnChinaName("ϵͳģ��");
		btnVO.setChildAry(new int[]{});
		btnVO.setOperateStatus(new int[]{
				IBillOperate.OP_NOTEDIT
		});
		return btnVO;
	}
}
