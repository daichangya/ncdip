package nc.pub.dip.startup;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.dip.pub.IServerBufferManage;
import nc.util.dip.sj.IContrastUtil;
import nc.vo.dip.managerserver.DipManagerserverVO;
import nc.vo.dip.messservtype.MessservtypeVO;

public class UpdataBuffThread implements Runnable {
	private Long sleeplong;
	private String servername;
	boolean isini=true;//是否是初始启动。如果是中间件启动，则为初始启动，否则不是初始启动
	public UpdataBuffThread(Long l,String servername) {
		super();
		this.sleeplong=l;
		this.servername=servername;
		isini=true;
	}

	public void run() {
		Thread.currentThread().setName(servername+"-dip-updatebuf");
		while(true){
			//同步主服务器上当前的servername下都有哪些可运行的消息服务设置
			ThreadUtil.syhDipMserState(servername);
			doTask();
			try {
				Thread.sleep(sleeplong);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void doTask() {
		List<MessservtypeVO> mtvo=null;
		List<DipManagerserverVO> messServerBuufer = null;
		if(isini){
			Logger.debug("初始化的查询-----------------");

			try {
				mtvo = (List<MessservtypeVO>) new BaseDAO(IContrastUtil.DESIGN_CON).retrieveByClause(MessservtypeVO.class,"nvl(dr,0)=0 and vdef2='"+servername+"'");
			} catch (DAOException e) {
				e.printStackTrace();
			}
			
			if(mtvo!=null&&mtvo.size()>0){
				messServerBuufer=new ArrayList<DipManagerserverVO>();
				for(MessservtypeVO tvo:mtvo){
					if(tvo.dr!=null&&tvo.dr==1){
						continue;
					}
					DipManagerserverVO vo=new DipManagerserverVO();
					vo.setMesvo(tvo);
					vo.setPk_managerserver(tvo.getPrimaryKey());
					vo.setIsmsg(true);
					vo.setSysname(tvo.getName());
					messServerBuufer.add(vo);
//					getOrSetUpdateBufList(null, false, vo);
				}
			}
			isini=false;
		}
		Logger.debug("开始去SBM里边取有哪些服务-----------------------");
		IServerBufferManage ibm=(IServerBufferManage) NCLocator.getInstance().lookup(IServerBufferManage.class.getName());
		List<DipManagerserverVO> ldmvo= ibm.getOrSetUpdateBufList(servername,true,null);
		Logger.debug("去SBM里边取有哪些服务:"+((messServerBuufer!=null&&messServerBuufer.size()>0)?messServerBuufer.size():0)+"--------------");
		if((messServerBuufer!=null&&messServerBuufer.size()>0)){
//			for(int i=0;i<messServerBuufer.size();i++){
				ThreadUtil.Instance(/*messServerBuufer.get(i)*/);
//			}
		}
		boolean reins=false;
		if(ldmvo!=null&&ldmvo.size()>0){
			for(int i=0;i<ldmvo.size();i++){
				if(ldmvo.get(i).getMstatus().equals("运行启动中"))
					reins=true;
				break;
			}
			if(reins){
				ThreadUtil.Instance(/*ldmvo.get(i)*/);
			}
		/*	for(int i=0;i<ldmvo.size();i++){
				if(ldmvo.get(i).getMstatus().equals("运行启动中")){
					ldmvo.get(i).setMstatus("运行");
					ldmvo.get(i).setRunservice(servername);
					ibm.getOrSetUpdateBufList(servername, false, ldmvo.get(i));
				}else if(ldmvo.get(i).getMstatus().equals("停止中")){
					ldmvo.get(i).setMstatus("停止");
					ldmvo.get(i).setRunservice(servername);
					ibm.getOrSetUpdateBufList(servername, false, ldmvo.get(i));
					
				}
			}*/
		}
		
	}

}
