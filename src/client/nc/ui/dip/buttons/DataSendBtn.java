package nc.ui.dip.buttons;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

//���ݷ��ͽڵ�ķ��Ͱ�ť
//20110-5-23
//zlc
public class DataSendBtn {
    public ButtonVO getButtonVO(){
    	ButtonVO btnVO=new ButtonVO();
    	btnVO.setBtnNo(130);
    	btnVO.setBtnCode("DataSend");
    	btnVO.setBtnName("����");
    	btnVO.setBtnChinaName("����");
        btnVO.setChildAry(new int[]{});
        btnVO.setOperateStatus(new int[]{
        		IBillOperate.OP_NOTEDIT
        });
        return btnVO;
    	
    	
    }
}
