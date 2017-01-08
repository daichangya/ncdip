package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

//数据发送节点的发送按钮
//20110-5-23
//zlc
public class DataSendBtn {
    public ButtonVO getButtonVO(){
    	ButtonVO btnVO=new ButtonVO();
    	btnVO.setBtnNo(130);
    	btnVO.setBtnCode("DataSend");
    	btnVO.setBtnName("发送");
    	btnVO.setBtnChinaName("发送");
        btnVO.setChildAry(new int[]{});
        btnVO.setOperateStatus(new int[]{
        		IBillOperate.OP_NOTEDIT
        });
        return btnVO;
    	
    	
    }
}
