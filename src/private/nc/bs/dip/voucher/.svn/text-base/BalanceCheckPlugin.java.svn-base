package nc.bs.dip.voucher;

import java.util.ArrayList;

import nc.itf.dip.pub.checkplugin.ICheckPlugin;
import nc.util.dip.sj.CheckMessage;
import nc.vo.dip.voucher.VoucherVO;
import nc.vo.pub.lang.UFDouble;

public class BalanceCheckPlugin implements ICheckPlugin{

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
			UFDouble debit = new UFDouble(0);
			UFDouble credit = new UFDouble(0);
			for(int j=0;j<voucher[i].getChildren().length;j++){
				String adddebit = voucher[i].getChildren()[j].getPrimary_debit_amount();
				if(adddebit == null || adddebit.trim().equals("")){
					adddebit = "0.00";
				}
				debit = debit.add(new UFDouble(adddebit));
				
				String addcredit = voucher[i].getChildren()[j].getPrimary_credit_amount();
				if(addcredit == null || addcredit.trim().equals("")){
					addcredit = "0.00";
				}
				credit = credit.add(new UFDouble(addcredit));
			}
			
		    if(debit.compareTo(credit) != 0){
		    	voucher[i].setCheckpass(false);
		    	if(msg.getErrList() == null){
		    		msg.setErrList(new ArrayList());
		    	}
		    	msg.getErrList().add(voucher[i].getId());
		    	
		    	if(shutdown){
		    		ignore = true;
		    	}
		    	valid.add(voucher[i]);
		    	continue;
		    }
		    
		    valid.add(voucher[i]);
		}
		
		if(valid.size() > 0){
			msg.setValidData(valid.toArray(new VoucherVO[0]));
		}
		return msg;
	}

}