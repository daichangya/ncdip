package nc.impl.dip.pub;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.util.dip.sj.IContrastUtil;
import nc.vo.dip.dateprocess.DateprocessVO;

public class RunTimeLog  extends TimerTask{
	public RunTimeLog(){
		super();
	}
	/**
	 * 功能：写数据库，日志
	 * 作者：王艳冬
	 * 日期:2011-06-15
	 * */
	@Override
	public void run() {
		List<DateprocessVO> list=new ArrayList<DateprocessVO>();
		WriteLog.getOrAddList(WriteLog.LX_GET, null, list);
		if(list!=null&&list.size()>0){
			BaseDAO bd=new BaseDAO(IContrastUtil.DESIGN_CON);
			try {
				bd.insertVOList(list);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
