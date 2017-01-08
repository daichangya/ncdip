package nc.bs.dip.voucher;

import java.util.ArrayList;

import nc.itf.dip.pub.checkplugin.ICheckPlugin;
import nc.util.dip.sj.CheckMessage;
import nc.vo.dip.voucher.FreeValueVO;
import nc.vo.dip.voucher.VoucherDetailVO;
import nc.vo.dip.voucher.VoucherVO;

public class AssCheckPlugin implements ICheckPlugin{

	@SuppressWarnings("unchecked")
	public CheckMessage doCheck(Object... ob) {
		CheckMessage msg = new CheckMessage();
		VoucherVO[] voucher = (VoucherVO[])ob[0];
		boolean shutdown = (Boolean)ob[1];
		boolean ignore = false;
		ArrayList valid = new ArrayList();
		for(int i=0;i<voucher.length;i++){
			if(!voucher[i].isCheckpass()){
				valid.add(voucher[i]);
				continue;
			}
			
			if(ignore){
				voucher[i].setCheckpass(false);
				msg.getErrList().add(voucher[i].getId());
				continue;
			}
			
			for(VoucherDetailVO bvo: voucher[i].getChildren()){
				FreeValueVO[] freeVo = bvo.getFreevalue();
				if(freeVo == null || freeVo.length == 0){
					continue;
				}
				
				for(FreeValueVO free: freeVo){
					if(free == null){
						continue;
					}
					if(free.getAssType() == null || free.getAssValue() == null){
						voucher[i].setCheckpass(false);
				    	if(msg.getErrList() == null){
				    		msg.setErrList(new ArrayList());
				    	}
				    	msg.getErrList().add(voucher[i].getId());
				    	
				    	if(shutdown){
				    		ignore = true;
				    	}
				    	valid.add(voucher[i]);
				    	break;
					}
				}
				
				if(ignore){
					break;
				}
			}
		    
		    valid.add(voucher[i]);
		}
		
		if(valid.size() > 0){
			msg.setValidData(valid.toArray(new VoucherVO[0]));
		}
		return msg;
	}

}
