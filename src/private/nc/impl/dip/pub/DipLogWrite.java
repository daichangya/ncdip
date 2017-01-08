package nc.impl.dip.pub;

import nc.bs.dip.log.DipAllLogParam;
import nc.bs.dip.log.DipErroLogParam;
import nc.bs.dip.log.DipSucessLogParam;
import nc.itf.dip.pub.IDipLogger;
import nc.util.dip.sj.IContrastUtil;

public class DipLogWrite implements IDipLogger{

	public void writediplog(String logmsg, String level) throws Exception {
		// TODO Auto-generated method stub
		if(IContrastUtil.LOG_ALL.equals(level)){
			DipAllLogParam.log.info(logmsg);
		}else if(IContrastUtil.LOG_ERROR.equals(level)){
			DipErroLogParam.log.info(logmsg);
		}else if(IContrastUtil.LOG_SUCESS.equals(level)){
			DipSucessLogParam.log.info(logmsg);
		}
	}

}
