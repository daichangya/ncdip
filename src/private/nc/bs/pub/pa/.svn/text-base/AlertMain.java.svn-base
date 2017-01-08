package nc.bs.pub.pa;
//后台预警


import nc.bs.logging.Logger;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.impl.dip.pub.Alert;
import nc.itf.dip.pub.IAlert;
import nc.vo.pub.BusinessException;

public class AlertMain implements IBackgroundWorkPlugin {



	class ss implements IAlert{
		int i;
		public ss(int i){
			this.i=i;
		}
		public boolean doAlert() {
			Logger.debug(Thread.currentThread().getName()+":  systime:"+System.currentTimeMillis()+"    "+i+"");
			System.out.println(Thread.currentThread().getName()+":  systime:"+System.currentTimeMillis()+"    "+i+"");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		
	}
	public static void main(String [] arg){
		AlertMain a=new AlertMain();
		try {
			a.executeTask(null);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String executeTask(BgWorkingContext bgwc) throws BusinessException {
		Alert a=new Alert("0001AA1000000001BRWS");
/*//		TODO 把实现的IAert接口，都放到list里边
		List<IAlert> list=ThreadpoolFactory.getAlertList();
		for(int i=0;i<100;i++){
			list.add(new ss(i));
		}
		//TODO 其实，这个线程池的大小，可以通过闸数读过来，待实现
		ThreadPool cache = ThreadpoolFactory.getTP();
		try {
//			cache.startService();
			for(IAlert alert:list){
				boolean succ=false;
				while(!succ){
					succ=cache.execute(alert);
					if(succ){
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			cache.storpService();
		}*/
		return "";
	}
}
