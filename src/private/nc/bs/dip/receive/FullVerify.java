package nc.bs.dip.receive;

import java.util.List;
import java.util.Map;

import nc.impl.dip.pub.EsbMapUtilVO;
import nc.itf.dip.pub.checkplugin.ICheckPlugin;
import nc.util.dip.sj.CheckMessage;
import nc.vo.dip.dataformatdef.DataformatdefBVO;


public class FullVerify implements ICheckPlugin{

		public CheckMessage doCheck(Object... ob) {
			// TODO Auto-generated method stub
			CheckMessage checkmsg=new CheckMessage();
			String msg=(String) ob[0];
			EsbMapUtilVO esbvo=(EsbMapUtilVO) ob[1];
			List<DataformatdefBVO> format=esbvo.getData();
			
			
//			##databegin##系统标准0##站点标志##业务标志##1##2##3##4##5##dataend##
			String separator=esbvo.getBj();//分隔符号 例如 ##
			String esbend=msg.split(separator)[msg.split(separator).length-1];
			String endflag=format.get(format.size()-1).getDatakind();
			if(esbend!=null&&endflag!=null&&esbend.equals(endflag)){
				if(format.size()==(msg.split(separator).length-1)){//
					checkmsg.setSuccessful(true);
					return checkmsg;
				}else{
					checkmsg.setSuccessful(false);
					checkmsg.setMessage("不满足完整性校验！");
					return checkmsg;
				}
			}else{
				checkmsg.setSuccessful(false);
				checkmsg.setMessage("不满足完整性校验！");
				return checkmsg;
			}
		
		}
}
